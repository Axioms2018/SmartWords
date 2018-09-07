package kr.co.moumou.smartwords.activity;

public class WordsDownloadController {
	
	public final static int DOWNLOAD_BUFFER_SIZE = 1024 * 10;
	
	private WordsDownloadWithHttp http;
	
	public WordsDownloadController(String url, String sessionID, String fileName, WordsControllerProgress listener) {
		http = new WordsDownloadWithHttp();
		
		if(http == null) {
			listener.onException("네트웍 연결을 확인해주세요.");
			return;
		}
		
		http.setListner(listener);
		http.downloadStart(url, sessionID, fileName);
	}
	
	public void cancel() {
		http.cancel();
	}

}