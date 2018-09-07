package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoWordsReportData {
	private int COMMAND;
	private int RSLT_COMMAND;
	private String ERROR_MSG;
	private String SESSIONID;
	private String USERID;
	private ArrayList<VoWordsReportDetail> LEVELREPORT = new ArrayList<VoWordsReportDetail>();
	
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
	
	public ArrayList<VoWordsReportDetail> getLEVELRPORT() {
		return LEVELREPORT;
	}
	
	public void setLEVELRPORT(ArrayList<VoWordsReportDetail> lEVELRPORT) {
		LEVELREPORT = lEVELRPORT;
	}	
}
