package kr.co.moumou.smartwords.activity;

public interface WordsControllerProgress {
	void onDBCheck();
	void onConnectingServer(String url);
	void onDownloading(String progress);
	void onDownloadComplete();
	void onException(String msg);
	void onFileExist();
	void onComplete();
}
