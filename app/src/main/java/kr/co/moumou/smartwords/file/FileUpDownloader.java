package kr.co.moumou.smartwords.file;

import android.content.Context;

public interface FileUpDownloader {
	void uploadStart(String path, long fileSize);
	void downloadStart(Context context, String url, String sessionID, String fileName, long fileSize, String chaci, String pCode);
	void setListener(FileControllerProgress listener);
	void cancel();
}
