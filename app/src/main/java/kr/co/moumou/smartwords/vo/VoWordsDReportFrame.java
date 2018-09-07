package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoWordsDReportFrame {
	private int COMMAND;
	private int RSLT_COMMAND;
	private String ERROR_MSG;
	private String SESSIONID;
	private String USERID;
	private String STD_LEVEL;
	private String PATH;
	private int STD_DAY;
	private VoWordsDReportData S_REPORT = new VoWordsDReportData();
	private VoWordsDReportData R_REPORT = new VoWordsDReportData();
	private ArrayList<VoWordsDReportDetail> WORD_LIST = new ArrayList<>();
	private ArrayList<VoWordsDReportDetail> R_WORD_LIST = new ArrayList<>();
	private VoCertificate CERTIFICATE;
	
	public int getCOMMAND() {
		return COMMAND;
	}
	
	public void setCOMMAND(int cOMMAND) {
		COMMAND = cOMMAND;
	}	
	
	public int getRSLT_COMMAND() {
		return RSLT_COMMAND;
	}
	
	public void setRSLT_COMMAND(int rSLT_COMMAND) {
		RSLT_COMMAND = rSLT_COMMAND;
	}
	
	public String getERROR_MSG() {
		return ERROR_MSG;
	}
	
	public void setERROR_MSG(String eRROR_MSG) {
		ERROR_MSG = eRROR_MSG;
	}
	
	public String getSESSIONID() {
		return SESSIONID;
	}
	
	public void setSESSIONID(String sESSIONID) {
		SESSIONID = sESSIONID;
	}
	
	public String getUSERID() {
		return USERID;
	}
	
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	
	public String getSTD_LEVEL() {
		return STD_LEVEL;
	}
	
	public void setSTD_LEVEL(String sTD_LEVEL) {
		STD_LEVEL = sTD_LEVEL;
	}

	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public int getSTD_DAY() {
		return STD_DAY;
	}
	
	public void setSTD_DAY(int sTD_DAY) {
		STD_DAY = sTD_DAY;
	}

	public VoWordsDReportData getS_REPORT() {
		return S_REPORT;
	}

	public void setS_REPORT(VoWordsDReportData s_REPORT) {
		S_REPORT = s_REPORT;
	}

	public VoWordsDReportData getR_REPORT() {
		return R_REPORT;
	}

	public void setR_REPORT(VoWordsDReportData r_REPORT) {
		R_REPORT = r_REPORT;
	}

	public ArrayList<VoWordsDReportDetail> getWORD_LIST() {
		return WORD_LIST;
	}

	public void setWORD_LIST(ArrayList<VoWordsDReportDetail> wORD_LIST) {
		WORD_LIST = wORD_LIST;
	}

	public ArrayList<VoWordsDReportDetail> getR_WORD_LIST() {
		return R_WORD_LIST;
	}

	public void setR_WORD_LIST(ArrayList<VoWordsDReportDetail> r_WORD_LIST) {
		R_WORD_LIST = r_WORD_LIST;
	}

	public VoCertificate getCERTIFICATE() {
		return CERTIFICATE;
	}

	public void setCERTIFICATE(VoCertificate cERTIFICATE) {
		CERTIFICATE = cERTIFICATE;
	}
}
