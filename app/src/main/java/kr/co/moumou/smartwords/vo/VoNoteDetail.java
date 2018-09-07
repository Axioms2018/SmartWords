package kr.co.moumou.smartwords.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VoNoteDetail implements Serializable {
	private String WORD;
	private String MEAN;
	private String RESULT_YN;
	
	public String getWORD() {
		return WORD;
	}
	
	public void setWORD(String wORD) {
		WORD = wORD;
	}
	
	public String getMEAN() {
		return MEAN;
	}
	
	public void setMEAN(String mEAN) {
		MEAN = mEAN;
	}
	
	public String getRESULT_YN() {
		return RESULT_YN;
	}
	
	public void setRESULT_YN(String rESULT_YN) {
		RESULT_YN = rESULT_YN;
	}	
}
