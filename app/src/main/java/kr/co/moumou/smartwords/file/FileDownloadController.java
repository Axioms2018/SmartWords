package kr.co.moumou.smartwords.file;


import android.content.Context;
import android.os.AsyncTask;

import kr.co.moumou.smartwords.R;

public class FileDownloadController {
	
	public final static int DOWNLOAD_BUFFER_SIZE = 1024*10;

	private FileUpDownloaderWithHttp http;

	public FileDownloadController(Context context, String url, String sessionID, String fileName, long fileSize, String pCode, String chaci, FileControllerProgress listener){

		http = new FileUpDownloaderWithHttp();

		new FileDownload().execute(context, url, sessionID, fileName, fileSize, pCode, chaci, listener);
	}

	public void cancel(){
		http.cancel();
	}


	private class FileDownload extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... params) {

			createDownloader((Context) params[0], (String) params[1], (String) params[2], (String) params[3],
					(Long) params[4], (String) params[5], (String) params[6], (FileControllerProgress) params[7]);
			return null;
		}


		private void createDownloader(Context context, String url, String sessionID, String fileName, long fileSize, String pCode, String chaci, FileControllerProgress listener){
			
			listener.onConnectingServer(context.getResources().getString(R.string.connecting_server));

			if(http == null){
				listener.onException(context.getResources().getString(R.string.check_your_network));
				return;
			}

			http.setListener(listener);
			http.downloadStart(context, url, sessionID, fileName, fileSize, chaci, pCode);
		}
	}
}
