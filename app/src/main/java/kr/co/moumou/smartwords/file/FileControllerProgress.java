package kr.co.moumou.smartwords.file;

public interface FileControllerProgress {
	void onDBCheck();
	void onConnectingServer(String url);
	void onDownloading(String progress);
	void onUploading(String progress);
	void onUploadComplete();
	void onDownloadComplete();
	void onDecompressing(String path, int currentPos, int totalCount);
	void onDecompressComplete();
	void onComplete();
	void onException(String msg);
	void onFileExist();
}
