package kr.co.moumou.smartwords.file;

import android.os.AsyncTask;

import kr.co.moumou.smartwords.util.LogUtil;

public class FileUploadController {
	
	public final static int DOWNLOAD_BUFFER_SIZE = 1024*10;
	
	private final int COMM_NOT_USE = -1;
	private final int COMM_TYPE_SOCKET = 1;
	private final int COMM_TYPE_HTTP = 2;

	private FileUpDownloaderWithSocket socket;
//	private FileUpDownloaderWithHttp http;

	public FileUploadController(String filePath, long fileSize, FileControllerProgress listener){
		socket = new FileUpDownloaderWithSocket();
//		http = new FileUpDownloaderWithHttp();

		new FileUpload().execute(filePath, fileSize, listener);
	}

	public void cancel(){
		socket.cancel();
//		http.cancel();
	}


	private class FileUpload extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... params) {

			createUploader((String)params[0], (long)params[1], (FileControllerProgress)params[2]);
			return null;
		}


		private void createUploader(String filePath, long fileSize, FileControllerProgress listener){
			
			listener.onConnectingServer("서버에 연결 중입니다.");
			FileUpDownloader uploader = getAvailableCommType();

			if (uploader != null) {
				uploader.setListener(listener);
				uploader.uploadStart(filePath, fileSize);
			}
		}

		/**
		 * 현제 사용가능한 네트웍 연결 상태를 확인한다.
		 * 연결 우선순위는 1.소켓(학습관 PC) 2.HTTP(무무서버) 
		 *  
		 * @return  소켓 연결 COMM_TYPE_SOCKET, HTTP 연결  COMM_TYPE_HTTP, 네트웍 사용불가 COMM_NOT_USE
		 * 
		 */
		private FileUpDownloader getAvailableCommType(){
			socket.connect();
			
			if(socket.isConnected()){
				LogUtil.d(" SOCKET DOWNLOAD ");
				return socket;
			}

//			if(NetworkState.getInstance().isNetworkConnected()){
//				LogUtil.d(" HTTP DOWNLOAD ");
//				return http;
//			}

			return null;
		}

	}
}
