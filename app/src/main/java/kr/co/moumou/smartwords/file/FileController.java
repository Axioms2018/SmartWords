package kr.co.moumou.smartwords.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.dao.DaoFileDownload;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.vo.VoAppResourceData;
import kr.co.moumou.smartwords.vo.VoFileDownloadnfo;


public class FileController {

	public static final String CHANT_FILE_FOLDER = "chant";

	private final int REQUEST_TAG_DOWNLOAD_FILE = 106;
	private volatile static FileController instance;
	private static Context context;

	private FileDownloadController fdc;
	private FileUploadController fuc;

	private FileDecompresser fileDecompress;

	private Queue<VoFileDownloadnfo> downloadQueue = new LinkedList<VoFileDownloadnfo>();
	private Queue<VoAppResourceData.ResourceData> downloadQueueNotDecompress = new LinkedList<VoAppResourceData.ResourceData>();

	private Queue<String> uploadQueue = new LinkedList<String>();

	private FileControllerProgress listener;
	private DaoFileDownload daoFileDownload;

	public static FileController getInstance(Context ctx) {
		if (instance == null) {
			synchronized (FileController.class) {
				if (instance == null) {
					instance = new FileController();
					context = ctx;
				}
			}
		}
		return instance;
	}

	public static boolean isChant(String fileName){
		return !(fileName.toLowerCase().indexOf("study") > -1 || fileName.toLowerCase().indexOf("work") > -1
				|| fileName.toLowerCase().indexOf("eval") > -1 || fileName.toLowerCase().indexOf("evla") > -1
				|| fileName.toLowerCase().indexOf("answer") > -1
				|| fileName.toLowerCase().indexOf("test") > -1);
	}

	public static String getFileName(String fileName){
		if(isChant(fileName)){
			String[] parser = fileName.split(File.separator);
			return CHANT_FILE_FOLDER + File.separator + parser[(parser.length-1)];
		}
		return fileName;
	}

	protected FileController() {}

	public void setListener(FileControllerProgress listener){
		this.listener = listener;
	}

	public void uploadFiles(String filePath){
		if(ApplicationPool.IS_STUDY_ROOM_MODE){
			uploadQueue.add(filePath);
			upload();
		}
	}

	private void upload(){
		if(uploadQueue.size() <= 0){
			listener.onComplete();
			return;
		}

		final String filePath = uploadQueue.poll();
		LogUtil.d(" Upload PATH : "  + filePath);
		File file = new File(filePath);

		if(!file.exists()){
			return;
		}

		fuc = new FileUploadController(filePath, file.length(), new FileControllerProgress() {

			@Override
			public void onFileExist() {
				listener.onFileExist();
				LogUtil.d(" FILE EXIST");
			}

			@Override
			public void onException(String msg) {
				listener.onException(msg);
				LogUtil.d(" Exception " + msg);
			}

			@Override
			public void onDownloading(String progress) {
				listener.onDownloading(progress);
			}

			@Override
			public void onDownloadComplete() {
				listener.onDownloadComplete();
				LogUtil.d(" onDownloadComplete ");
			}

			@Override
			public void onDecompressing(String path, int currentPos, int totalCount) {
				listener.onDecompressing(path, currentPos, totalCount);
				LogUtil.d(" Decompressing " + path  +  " " +  currentPos +" / " + totalCount);
			}

			@Override
			public void onDecompressComplete() {
				download();
			}

			@Override
			public void onDBCheck() {
				listener.onDBCheck();
				LogUtil.d(" ON DB CHECK! ");
			}

			@Override
			public void onConnectingServer(String url) {
				listener.onConnectingServer(url);
				LogUtil.d(" ServerCOnnecteding  " + url);
			}

			@Override
			public void onComplete() {
				listener.onComplete();
				LogUtil.d(" DOWNLOAD COMPLATE");
			}

			@Override
			public void onUploading(String progress) {
				listener.onUploading(progress);
			}

			@Override
			public void onUploadComplete() {
				listener.onUploadComplete();
			}

		});

	}

	public void downloadFiles(VoAppResourceData data){
		if(data == null){
			return;
		}
		if(data.getRES_LIST() == null || data.getRES_LIST().size() == 0){
			return;
		}

		downloadQueueNotDecompress.addAll(data.getRES_LIST());
	}

	public void downloadFiles(ArrayList<VoFileDownloadnfo> fileList, boolean beforeRemove){
		if(beforeRemove){
			cancel();
			for (int i = 0; i < fileList.size(); i++) {
				deleteDownloadHistory(fileList.get(i).getpCode() + File.separator + fileList.get(i).getName());
			}
		}
		downloadQueue.addAll(fileList);
		download();
	}


	public void downloadFiles(ArrayList<VoFileDownloadnfo> fileList){
		downloadQueue.addAll(fileList);
		download();
	}

	public void clearDownloadHistory(ArrayList<VoFileDownloadnfo> fileList){

	}

	public void cancel(){
		downloadQueue.clear();
		if(fdc != null){
			fdc.cancel();
		}

		if(fileDecompress != null){
			fileDecompress.cancel();
		}

	}

	private void download(){
		if(downloadQueue.size() <= 0){
			if(downloadQueueNotDecompress.size() <= 0){
				listener.onComplete();
				return;
			}
		}

		if(downloadQueue.isEmpty()){
			VoAppResourceData.ResourceData item = downloadQueueNotDecompress.poll();
			fileDownload(true, item.getFILE_PATH(), item.getFILE_NAME(), 0L, "monthly_test", 398548);
			return;
		}

		final VoFileDownloadnfo info = downloadQueue.poll();
		if(isExsistFile(info.getpCode(), info.getName(), info.getCreateDate())){      //다운로드 받은 이력 확인.
			if(isDecompressed(info.getpCode(), info.getName())){     //압축 푼 이력 확인.
				download();                               //다운로드도 받았고 압축도 풀었으니 다음으로.
				return;
			}
			bookResourceDecompress(info.getName(), info.getpCode(), info.getCreateDate()); //다운로드는 받았으나 압축을 풀지 않아 압축 풀러
			return;
		}

		fileDownload(false, info.getUrl(), info.getName(), info.getFileSize(), info.getpCode(), info.getCreateDate());
	}

	private void fileDownload(final boolean isTest, final String url, final String name, final long fileSize, final String pcode, final long createDate){
		fdc = new FileDownloadController(context, url, SharedPrefData.getStringSharedData(context, SharedPrefData.SHARED_USER_SESSIONID, Constant.STRING_DEFAULT),
				name, fileSize, pcode, SharedPrefData.getStringSharedData(context, SharedPrefData.SHARED_CHACI_S, Constant.STRING_DEFAULT), new FileControllerProgress() {

			@Override
			public void onFileExist() {
				listener.onFileExist();
				LogUtil.d(" FILE EXIST");
			}

			@Override
			public void onException(String msg) {
				listener.onException(msg);
				LogUtil.d(" Exception " + msg);
			}

			@Override
			public void onDownloading(String progress) {
				listener.onDownloading(progress);
			}

			@Override
			public void onDownloadComplete() {
				listener.onDownloadComplete();

				if(isTest){
					download();
					return;
				}
//				insertDownloadInfo(pcode, name, createDate);
				bookResourceDecompress(name, pcode, createDate);
				LogUtil.d(" onDownloadComplete ");
			}

			@Override
			public void onDecompressing(String path, int currentPos, int totalCount) {
				listener.onDecompressing(path, currentPos, totalCount);
				LogUtil.d(" Decompressing " + path  +  " " +  currentPos +" / " + totalCount);
			}

			@Override
			public void onDecompressComplete() {
				download();
			}

			@Override
			public void onDBCheck() {
				listener.onDBCheck();
				LogUtil.d(" ON DB CHECK! ");
			}

			@Override
			public void onConnectingServer(String url) {
				listener.onConnectingServer(url);
				LogUtil.d(" ServerCOnnecteding  " + url);
			}

			@Override
			public void onComplete() {
				listener.onComplete();
				LogUtil.d(" DOWNLOAD COMPLATE");
			}

			@Override
			public void onUploading(String progress) {
			}

			@Override
			public void onUploadComplete() {
			}

		});
	}
	private void bookResourceDecompress(final String name, final String pCode, final long createDate){
		LogUtil.d(" DEcompress!!!!!! ");
		fileDecompress = new FileDecompresser(name , pCode);
		fileDecompress.setListener(new FileControllerProgress() {

			@Override
			public void onFileExist() {
			}

			@Override
			public void onException(String msg) {
				listener.onException(msg);
			}

			@Override
			public void onDownloading(String progress) {

			}

			@Override
			public void onDownloadComplete() {

			}

			@Override
			public void onDecompressing(String path, int currentPos, int totalCount) {
				listener.onDecompressing(path, currentPos, totalCount);

			}

			@Override
			public void onDecompressComplete() {
				insertDownloadInfo(pCode, name, createDate);
				insertDecompressFileCount(pCode, name, fileDecompress.getFileCount());
				download();
			}
			@Override
			public void onDBCheck() {

			}

			@Override
			public void onConnectingServer(String url) {

			}

			@Override
			public void onComplete() {
				listener.onComplete();
			}

			@Override
			public void onUploading(String progress) {
			}

			@Override
			public void onUploadComplete() {
			}

		});
		fileDecompress.execute();
	}


	private DaoFileDownload getDB(){
		if(daoFileDownload == null){
			daoFileDownload = DaoFileDownload.getInstance(context);
		}
		return daoFileDownload;
	}

	private int deleteDir(String a_path){
		File file = new File(a_path);
		if(file.exists()){
			File[] childFileList = file.listFiles();
			if (childFileList != null) {
				for(File childFile : childFileList){
					if(childFile.isDirectory()){
						deleteDir(childFile.getAbsolutePath());
					}
					else{
						childFile.delete();
					}
				}
			}
			file.delete();
			return 1;
		}else{
			return 0;
		}
	}

	public void deleteDownloadHistory(String fileName){
		getDB().removeDownloadHistory(getFileName(fileName));
		File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "files");
		try {
			if(file.exists()){
				deleteDir(file.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isExsistFile(String pCode, String name, long createDate){
		String fileName = (pCode + File.separator + name);
		return getDB().isExistBook(getFileName(fileName), createDate);
	}

	private void insertDownloadInfo(String pCode, String name, long createDate){
		String fileName = (pCode + File.separator + name);
		getDB().insertDownloadBookInfo(getFileName(fileName), createDate);
	}

	private boolean isDecompressed(String pCode, String name){
		String fileName = (pCode + File.separator + name);

		return getDB().getBookFileCount(getFileName(fileName)) > 0;
	}

	private void insertDecompressFileCount(String pCode, String name, int count){
		String fileName = (pCode + File.separator + name);
		getDB().insertDownloadFileCount(getFileName(fileName), fileDecompress.getFileCount());
	}


}