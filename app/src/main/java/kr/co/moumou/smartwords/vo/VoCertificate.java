package kr.co.moumou.smartwords.vo;

import java.io.Serializable;
import java.sql.Date;

public class VoCertificate extends VoResponseBase implements Serializable {
	
	private String STD_LEVEL;
	private String CERT_YN;
	private Date STD_DATE;
	private int RIGHT_CNT;
	private int WRONG_CNT;
	private int MAX_DAY;
	
	public String getSTD_LEVEL() {
		return STD_LEVEL;
	}
	public void setSTD_LEVEL(String sTD_LEVEL) {
		STD_LEVEL = sTD_LEVEL;
	}
	public String getCERT_YN() {
		return CERT_YN;
	}
	public void setCERT_YN(String cERT_YN) {
		CERT_YN = cERT_YN;
	}
	public Date getSTD_DATE() {
		return STD_DATE;
	}
	public void setSTD_DATE(Date sTD_DATE) {
		STD_DATE = sTD_DATE;
	}
	public int getRIGHT_CNT() {
		return RIGHT_CNT;
	}
	public void setRIGHT_CNT(int rIGHT_CNT) {
		RIGHT_CNT = rIGHT_CNT;
	}
	public int getWRONG_CNT() {
		return WRONG_CNT;
	}
	public void setWRONG_CNT(int wRONG_CNT) {
		WRONG_CNT = wRONG_CNT;
	}
	public int getMAX_DAY() {
		return MAX_DAY;
	}
	public void setMAX_DAY(int mAX_DAY) {
		MAX_DAY = mAX_DAY;
	}
}
