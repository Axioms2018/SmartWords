package kr.co.moumou.smartwords.file;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.List;

import kr.co.moumou.smartwords.R;

public class FileUpDownloaderWithHttp implements FileUpDownloader{
	private FileControllerProgress listener;
	private Context context;
	
	@Override
	public void downloadStart(Context context, String url, String sessionID, String fileName, long fileSize, String chaci, String pCode ) {
		this.context = context;
		new Download().execute(url, fileName, pCode, chaci);
	}

	@Override
	public void setListener(FileControllerProgress listener) {
		this.listener = listener;
	}

	@Override
	public void cancel() {
		downloadManager.remove(downloadID);
	}

	private long downloadID;
	DownloadManager downloadManager;

	private class Download extends AsyncTask<Object, Void, Void> {
		
		@Override
		protected Void doInBackground(Object... params) {
			
			try {
				String downloadUrl = (String)params[0];
				String fileName = (String)params[1];
				String pCode = (String)params[2];
				String chaci = (String)params[3];
				
				String subpath = "/files/" + pCode;
				
				if(FileController.isChant(fileName)){
					subpath = "/files/chant";
				}
				
				downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	            Uri urlToDownload = Uri.parse(downloadUrl);
	            List<String> pathSegments = urlToDownload.getPathSegments();
	            DownloadManager.Request request = new DownloadManager.Request(urlToDownload);
	            request.setTitle(context.getString(R.string.notice_download_study_data_title));
	            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + subpath).mkdirs();
	            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , subpath+ File.separator+pathSegments.get(pathSegments.size()-1));
	            
	            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
	            downloadID = downloadManager.enqueue(request);
	            downloadingState(downloadID);
			} catch (Exception e) {
				e.printStackTrace();
				listener.onException(e.toString());
			}
			return null;
		}

		
		private void downloadingState(final long downloadID){
			 new Thread(new Runnable() {
		            @Override
		            public void run() {

		                boolean downloading = true;

		                while (downloading) {

		                    DownloadManager.Query q = new DownloadManager.Query();
		                    q.setFilterById(downloadID);

		                    Cursor cursor = downloadManager.query(q);
		                    if(cursor.getCount() <= 0){
		                        return;
		                    }

		                    cursor.moveToFirst();
		                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
		                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

		                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
		                        downloading = false;
		                        listener.onDownloadComplete();
		                    }else if(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED){
		                        downloading = false;
		                        listener.onException("Download Fail");
		                    }

		                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
		                    if(listener != null){
		                    	listener.onDownloading(dl_progress + "%");
		                    }
		                    

		                    cursor.close();

		                    try {
		                        Thread.sleep(100);
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    }
		                }

		            }
		        }).start();
		}
	}

	@Override
	public void uploadStart(String path, long fileSize) {
		// TODO Auto-generated method stub
		
	}
	

}
