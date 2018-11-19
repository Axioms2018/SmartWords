package kr.co.moumou.smartwords.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import kr.co.moumou.smartwords.listener.ListenerFileDownload;

public class Decompress extends AsyncTask<String, Integer, Void> {

    private boolean cancel = false;
    private ListenerFileDownload listener = null;
    private String zipPath;
    private String filePath;
    private String fileName;
    private float size;

    public Decompress(String zipPath, String filePath, String fileName, ListenerFileDownload listener) {
        this.zipPath = zipPath;
        this.filePath = filePath;
        this.fileName = fileName;
        this.listener = listener;
        this.cancel = false;
    }

    public void cancel(){
        LogUtil.d("Decompress cancel");
        this.cancel = true;
    }

    @Override
    protected Void doInBackground(String... strings) {

        String zipFilePath = zipPath + "/" + fileName;
        //float counter = 0;
        LogUtil.i("zipFilePath : " + zipFilePath);

        FileInputStream fis = null;
        ZipInputStream zis = null;
//        ZipEntry zipentry = null;

        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            fis = new FileInputStream(zipFilePath);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry zipentry = null;

            size = zipFile.size();
            int count = 0;

            while ((zipentry = zis.getNextEntry()) != null) {

                //if(zipentry == null) break;

                String filename = zipentry.getName();

                LogUtil.i("filename : " + filename);

                File file = new File(filePath, filename);
                if(zipentry.isDirectory()) {
                    file.mkdirs();
                }else{
                    try {
                        count++;
                        publishProgress((int)((count/size) * 100));
                        createFile(file, zis);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                zis.closeEntry();
            }
            zis.close();
            fis.close();

            if (listener != null && !cancel) {

                File f = new File(zipFilePath);
                f.delete();
                LogUtil.i("onDecompressComplete : " + fileName);
                listener.onDecompressComplete();
            }

        }catch(Exception e) {
            e.printStackTrace();
            listener.onException(e.getMessage());
            cancel();
        }finally {
            if(zis != null) {
                try { zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        listener.onDecompressing(values[0]);
    }

    private void createFile(File file, ZipInputStream zis) throws Throwable {

        final int BUFFER_SIZE = 4096;

        BufferedOutputStream bufferedOutputStream = null;
        File parentDir = new File(file.getParent());

        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        //파일 스트림 선언
        FileOutputStream fos = new FileOutputStream(file);
        bufferedOutputStream = new BufferedOutputStream(fos, BUFFER_SIZE);
        byte[] buffer = new byte[BUFFER_SIZE];
        int size = 0;

        //Zip스트림으로부터 byte뽑아내기
        while ((size = zis.read(buffer, 0, BUFFER_SIZE)) != -1) {
            //byte로 파일 만들기
            //fos.write(buffer, 0, size);
            bufferedOutputStream.write(buffer, 0, size);
        }
        fos.flush();
        bufferedOutputStream.close();
    }


}
