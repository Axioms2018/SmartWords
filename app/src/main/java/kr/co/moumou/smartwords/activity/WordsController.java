package kr.co.moumou.smartwords.activity;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.dao.WordTestDownloadDao;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.vo.VoFileWordsDownload;

public class WordsController {

	private volatile static WordsController instance;
	private static Context context;
	private String url;
	
	private Queue<VoFileWordsDownload> downloadQueue = new LinkedList<VoFileWordsDownload>();
	
	private WordsDownloadController wdc;
	private WordsControllerProgress listener;
	private WordTestDownloadDao daoFileDownload;
	
	public static WordsController getInstance(Context ctx) {
		if(instance == null) {
			synchronized(WordsController.class) {
				if(instance == null) {
					instance = new WordsController();
					context = ctx;
				}
			}
		}
		return instance;
	}
	
	public void setListener(WordsControllerProgress listener) {
		this.listener = listener;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public void downloadFiles(ArrayList<VoFileWordsDownload> fileList) {
		downloadQueue.addAll(fileList);
		download();
	}
	
	public void cancel() {
		downloadQueue.clear();
		if(wdc != null) wdc.cancel();
	}
	
	private void checkFolder() {
		File wordsFolder = new File(ApplicationPool.PATH_WORDS_RESOURCE);
		if(!wordsFolder.exists()){
			wordsFolder.mkdir();
		}
	}
	
	private void download() {
		
		if(downloadQueue.size() <= 0) {
			LogUtil.d(" onDownloadAllComplete ");
			listener.onComplete();
			return;
		}
		checkFolder();
		final VoFileWordsDownload info = downloadQueue.poll();
		fileDownload(info.getFilename(), info.getDate());
	}
	
	private void fileDownload(final String filename, final String createDate) {
		wdc = new WordsDownloadController(url, SharedPrefData.getStringSharedData(context, SharedPrefData.SHARED_USER_SESSIONID, Constant.STRING_DEFAULT), filename, new WordsControllerProgress() {
			
			@Override
			public void onFileExist() {
				listener.onFileExist();
				LogUtil.d("FILE EXIST");
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
				insertDownloadInfo(filename);
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
			public void onComplete() {}
		} );
	}
	
	private WordTestDownloadDao getDB() {
		if(daoFileDownload == null){
			daoFileDownload = WordTestDownloadDao.getInstance(context);
		}
		return daoFileDownload;
	}
	
	private void insertDownloadInfo(String filename) {
		getDB().insertWordTestDownload(filename);
	}
	
}


