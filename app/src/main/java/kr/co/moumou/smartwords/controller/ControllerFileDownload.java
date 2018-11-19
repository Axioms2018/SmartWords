package kr.co.moumou.smartwords.controller;

import android.content.Context;
import android.os.Environment;

import com.androidnetworking.error.ANError;

import java.io.File;

import kr.co.moumou.smartwords.communication.Const;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.dao.DaoFileDownload;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.listener.ListenerFileDownload;
import kr.co.moumou.smartwords.util.Decompress;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.vo.VoDownload;

/**
 * Created by 김민정 on 2018-04-04.
 */

public class ControllerFileDownload {

    private static final int BUFFER_SIZE = 1024 * 2;
    private volatile static ControllerFileDownload instance;
    private static Context context;
    private ListenerFileDownload listener;
    private Decompress decompress;

    public static ControllerFileDownload getInstance(Context ctx) {
        if (instance == null) {
            synchronized (ControllerFileDownload.class) {
                if (instance == null) {
                    instance = new ControllerFileDownload();
                    context = ctx;
                }
            }
        }
        return instance;
    }

    public void setListener(ListenerFileDownload listener) {
        this.listener = listener;
    }

    /**
     * File Download
     * @param voDownload
     */
    public void download(VoDownload voDownload) {

        boolean isDownload = checkDownload(voDownload.getPCODE(), voDownload.getLESSON(), voDownload.getTYPE(), voDownload.getURL());
        if(isDownload) {
            fileDownload(voDownload);
        }else{
            descompress(voDownload);
        }
//        else {
////            final String dirPath = ConstCommURL.getFilePath(voDownload.getCode() + "/" + voDownload.getLesson());
////            final String fileName = voDownload.getType() + Const.FILE_TYPE_ZIP;
////            listener.onNoDownload(dirPath, fileName);
//        }
    }

    /**
     * File Descompress
     * @param voDownload
     */
    public void descompress(VoDownload voDownload) {

        boolean isDecompress = checkDecompress(voDownload.getPCODE(), voDownload.getLESSON(), voDownload.getTYPE(), voDownload.getURL());
        if(isDecompress){
            listener.onStartFileDownload();
            String zipPath = ConstantsCommURL.getFilePath(voDownload.getPCODE() + "/" + voDownload.getLESSON());
            String filePath = ConstantsCommURL.getFilePath(voDownload.getPCODE() + "/" + voDownload.getLESSON() + "/" + voDownload.getTYPE());
            String fileName = voDownload.getTYPE() + Const.FILE_TYPE_ZIP;
            fileDecompress(zipPath, filePath, fileName);
        } else {
            listener.onDecompressComplete();
        }
    }

    /**
     * check Download
     */
    private boolean checkDownload(String code, String lesson, String type, String url) {
        return DaoFileDownload.getInstance(context).isDownload(code, lesson , type, url);
    }

    /**
     * check Decompress
     */
    private boolean checkDecompress(String code, String lesson, String type, String url) {
        return DaoFileDownload.getInstance(context).isDecompressed(code, lesson , type, url);
    }

    /**
     * save FileDownload DB
     */
    public void saveDownloadDB(VoDownload voDownload) {
        //FileDownload DB 저장
        DaoFileDownload.getInstance(context).
                insertDownloadFile(voDownload.getPCODE(),
                        voDownload.getLESSON(), voDownload.getTYPE(),
                        voDownload.getFILENAME(), 0, voDownload.getURL());
    }

    /**
     * save Decompress DB
     */
    public void saveDecompress(VoDownload voDownload) {
        DaoFileDownload.getInstance(context).
                insertDownloadFile(voDownload.getPCODE(),
                        voDownload.getLESSON(), voDownload.getTYPE(),
                        voDownload.getFILENAME(), 1, voDownload.getURL());
    }

    /**
     * unzip
     */
    private void fileDecompress(String zipPath, String filePath, String fileName) {
        LogUtil.d("Descompress Start");
        listener.onStartDecompress();
        decompress = new Decompress(zipPath, filePath, fileName, listener);
        decompress.execute();
    }

    /**
     * File Download
     */
    private void fileDownload(final VoDownload voDownload) {

        LogUtil.d("Download Start");
        LogUtil.d("voDownload.getURL() : " + voDownload.getURL());

        listener.onStartDownload();
        final String filePath = ConstantsCommURL.getFilePath(voDownload.getPCODE() + "/" + voDownload.getLESSON());
        final String fileName = voDownload.getTYPE() + Const.FILE_TYPE_ZIP;

        File file = new File(filePath + "/" + voDownload.getTYPE());

        if(file.exists()) {
            deleteFile(file.getAbsolutePath());
        }

        AndroidNetworkRequest.getInstance(context).downloadRequest(voDownload.getTAG(),
                voDownload.getURL(),
                filePath, fileName, new AndroidNetworkRequest.ListenerDownloadResponse() {

                    @Override
                    public void onComplete() {
                        LogUtil.i("onDownloadComplete : " + voDownload.getFILENAME());
                        listener.onDownloadComplete();
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.i("onError : " + anError.getErrorDetail());
                        listener.onException(anError.getErrorDetail());
                    }

                    @Override
                    public void onProgress(long byteDownloaded, long totalBytes) {
                        int percent = (int) ((double)byteDownloaded / (double)totalBytes * 100);
                        listener.onDownloading(percent);
                    }

                    @Override
                    public void networkerror() {

                    }
                });
    }

    /**
     * APK File Download
     */
    public void downloadApk(final VoDownload voDownload) {

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        //deleteApkFiles();

        AndroidNetworkRequest.getInstance(context).downloadRequest(voDownload.getTAG(),
                voDownload.getURL(),
                filePath, voDownload.getFILENAME(), new AndroidNetworkRequest.ListenerDownloadResponse() {

                    @Override
                    public void onComplete() {
                        LogUtil.i("onDownloadComplete : " + voDownload.getFILENAME());
                        listener.onEndFileDownload();
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogUtil.i("onError : " + anError.getErrorDetail());
                    }

                    @Override
                    public void onProgress(long byteDownloaded, long totalBytes) {
                        int percent = (int) ((double)byteDownloaded / (double)totalBytes * 100);
                        listener.onDownloading(percent);
                    }

                    @Override
                    public void networkerror() {
                        listener.onException("networkerror not connected");
                    }
                });
    }

//    private void deleteApkFiles(){
//
//        File files = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
//        String apkName = Const.APPNAME;
//
//        if(!files.exists()){
//            return;
//        }
//
//        if(files.listFiles().length>0){
//            for(File file : files.listFiles()){
//                if((file.getName().startsWith(apkName) && file.getName().endsWith("apk")))
//                {
//                    file.delete();
//                }
//            }
//        }
//    }

    private void deleteFile(String path) {
        LogUtil.d("deleteFile : " + path);

        File file = new File(path);
        File[] childFileList = file.listFiles();
        for(File childFile : childFileList)
        {
            if(childFile.isDirectory()) {
                deleteFile(childFile.getAbsolutePath());     //하위 디렉토리 루프
            }
            else {
                childFile.delete();    //하위 파일삭제
            }
        }
        file.delete();    //root 삭제
    }


    public void cancelDownload(String tag) {
        AndroidNetworkRequest.getInstance(context).cancel(tag);
        //listener.onDownloadCancel();
    }

    public void cancelDecompress() {
        if(null != decompress) decompress.cancel();
        //listener.onDownloadCancel();
    }

}
