package kr.co.moumou.smartwords.activity;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.file.FileDownloadController;
import kr.co.moumou.smartwords.util.LogUtil;

public class WordsDownloadWithHttp implements WordsDownloader {

	private WordsControllerProgress listener;
	private boolean isCancel = false;
	
	@Override
	public void downloadStart(String url, String sessionID, String fileName) {
		new Download().execute(url, fileName);
	}
	@Override
	public void setListner(WordsControllerProgress listener) {
		this.listener = listener;
	}
	@Override
	public void cancel() {
		isCancel = true;
	}
	
	private class Download extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			String downloadUrl = (String) params[0];
			String fileName = (String) params[1];
			int downloadFileLength;
			
			if(listener != null) {
				listener.onConnectingServer("서버 연결 중입니다.");
			}
			
			try {
				URL url = new URL(downloadUrl + fileName);
				
				LogUtil.d("Download URL : " + url.toString());
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.connect();
				
				downloadFileLength = urlConnection.getContentLength();
				
				File folder = new File(ApplicationPool.PATH_WORDS_RESOURCE);
				if(!folder.exists()) {
					folder.mkdir();
				}
				
				final File file = new File(ApplicationPool.PATH_WORDS_RESOURCE + fileName);
				LogUtil.d("Download FILE PATH : " + file.getAbsolutePath());
				
				if(file.exists()) {
					file.delete();
				}
				file.createNewFile();
				
				InputStream is = urlConnection.getInputStream();
				BufferedInputStream in = new BufferedInputStream(is);
				FileOutputStream fos = new FileOutputStream(file);
				
				int len = 0;
				byte data[] = new byte[FileDownloadController.DOWNLOAD_BUFFER_SIZE];
				long downloadByteSize = 0;
				
				while ((len = in.read(data)) != -1) {
					
					if(isCancel) {
						fos.flush();
						fos.close();
						in.close();
						return null;
					}
					
					downloadByteSize += len;
					fos.write(data, 0, len);
				}
				
				fos.flush();
				fos.close();
				in.close();
				
				if(listener != null)
					listener.onDownloadComplete();
				
			} catch (Exception e) {
				LogUtil.e(e.toString());
				if(listener != null)
					listener.onException(e.toString());
			}
			return null;
		}
	}
}

interface WordsDownloader {
	void downloadStart(String url, String sessionID, String fileName);
	void setListner(WordsControllerProgress listener);
	void cancel();
}
