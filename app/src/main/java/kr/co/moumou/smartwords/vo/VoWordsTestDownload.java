package kr.co.moumou.smartwords.vo;

import java.io.Serializable;

public class VoWordsTestDownload implements Serializable {

	private static final long serialVersionUID = -7188166185967797399L;
	private String FILE_NAME;
	private String FILE_DATE;
	
	public String getFILE_NAME() {
		return FILE_NAME;
	}
	public void setFILE_NAME(String fILE_NAME) {
		FILE_NAME = fILE_NAME;
	}
	public String getFILE_DATE() {
		return FILE_DATE;
	}
	public void setFILE_DATE(String fILE_DATE) {
		FILE_DATE = fILE_DATE;
	}
}
