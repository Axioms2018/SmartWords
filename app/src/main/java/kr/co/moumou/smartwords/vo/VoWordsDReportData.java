package kr.co.moumou.smartwords.vo;

import java.sql.Date;
import java.util.ArrayList;

public class VoWordsDReportData {
	private String USERID;
	private String STD_GB;
	private String STD_LEVEL;
	private int STD_DAY;
	private Date STD_DATE;
	private int WORD_CNT;
	private int RIGHT_CNT;
	private int WRONG_CNT;
	private int WORD_PER;
	private String TEST1;
	private String TEST2;
	private String TEST3;
	private ArrayList<VoWordsDReportDetail> USERWORD = new ArrayList<VoWordsDReportDetail>();
	
	public String getUSERID() {
		return USERID;
	}
	
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	
	public String getSTD_GB() {
		return STD_GB;
	}
	
	public void setSTD_GB(String sTD_GB) {
		STD_GB = sTD_GB;
	}
	
	public String getSTD_LEVEL() {
		return STD_LEVEL;
	}
	
	public void setSTD_LEVEL(String sTD_LEVEL) {
		STD_LEVEL = sTD_LEVEL;
	}
	
	public int getSTD_DAY() {
		return STD_DAY;
	}
	
	public void setSTD_DAY(int sTD_DAY) {
		STD_DAY = sTD_DAY;
	}
	
	public Date getSTD_DATE() {
		return STD_DATE;
	}

	public void setSTD_DATE(Date sTD_DATE) {
		STD_DATE = sTD_DATE;
	}

	public int getWORD_CNT() {
		return WORD_CNT;
	}
	
	public void setWORD_CNT(int wORD_CNT) {
		WORD_CNT = wORD_CNT;
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
	
	public int getWORD_PER() {
		return WORD_PER;
	}
	
	public void setWORD_PER(int wORD_PER) {
		WORD_PER = wORD_PER;
	}
	
	public String getTEST1() {
		return TEST1;
	}
	
	public void setTEST1(String tEST1) {
		TEST1 = tEST1;
	}
	
	public String getTEST2() {
		return TEST2;
	}
	
	public void setTEST2(String tEST2) {
		TEST2 = tEST2;
	}
	
	public String getTEST3() {
		return TEST3;
	}
	
	public void setTEST3(String tEST3) {
		TEST3 = tEST3;
	}
	
	public ArrayList<VoWordsDReportDetail> getUSERWORD() {
		return USERWORD;
	}
	
	public void setUSERWORD(ArrayList<VoWordsDReportDetail> uSERWORD) {
		USERWORD = uSERWORD;
	}
}
