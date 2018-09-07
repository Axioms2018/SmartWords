package kr.co.moumou.smartwords.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VoWordsDReportDetail implements Serializable {
	private String STD_GB;
	private String STD_STEP;
	private String WORD;
	private String USER_ANS;
	private String ANSWER;
	private String MEAN;
	private String STD_MP3;
	private boolean isSelected;
	
	public String getSTD_GB() {
		return STD_GB;
	}
	
	public void setSTD_GB(String sTD_GB) {
		STD_GB = sTD_GB;
	}
	
	public String getSTD_STEP() {
		return STD_STEP;
	}
	
	public void setSTD_STEP(String sTD_STEP) {
		STD_STEP = sTD_STEP;
	}
	
	public String getWORD() {
		return WORD;
	}
	
	public void setWORD(String wORD) {
		WORD = wORD;
	}
	
	public String getUSER_ANS() {
		return USER_ANS;
	}
	
	public void setUSER_ANS(String uSER_ANS) {
		USER_ANS = uSER_ANS;
	}
	
	public String getANSWER() {
		return ANSWER;
	}
	
	public void setANSWER(String aNSWER) {
		ANSWER = aNSWER;
	}

	public String getMEAN() {
		return MEAN;
	}

	public void setMEAN(String mEAN) {
		MEAN = mEAN;
	}

	public String getSTD_MP3() {
		return STD_MP3;
	}

	public void setSTD_MP3(String sTD_MP3) {
		STD_MP3 = sTD_MP3;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
