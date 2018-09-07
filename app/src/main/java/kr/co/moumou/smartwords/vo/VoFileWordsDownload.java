package kr.co.moumou.smartwords.vo;

import java.io.Serializable;

public class VoFileWordsDownload implements Serializable {
	
	private static final long serialVersionUID = -5565568974889101577L;
	
	private String filename;
	private String date;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
