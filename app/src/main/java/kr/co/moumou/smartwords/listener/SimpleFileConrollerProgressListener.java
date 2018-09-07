package kr.co.moumou.smartwords.listener;

import kr.co.moumou.smartwords.file.FileControllerProgress;


public class SimpleFileConrollerProgressListener implements FileControllerProgress{

	@Override
	public void onDBCheck() {
	}

	@Override
	public void onUploading(String progress) {
	}

	@Override
	public void onUploadComplete() {
	}

	@Override
	public void onConnectingServer(String url) {
	}

	@Override
	public void onDownloading(String progress) {
	}

	@Override
	public void onDownloadComplete() {
	}

	@Override
	public void onDecompressing(String path, int currentPos, int totalCount) {
	}
	
	@Override
	public void onDecompressComplete() {
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onException(String msg) {
	}

	@Override
	public void onFileExist() {
	}

}
