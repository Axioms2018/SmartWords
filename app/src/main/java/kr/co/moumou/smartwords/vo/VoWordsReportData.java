package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoWordsReportData {
	private int COMMAND;
	private int RES_CODE;
	private String RES_MSG;
	private String SESSIONID;
	private String USERID;
	private ArrayList<VoWordsReportDetail> LEVELREPORT = new ArrayList<VoWordsReportDetail>();
	
	public int getCOMMAND() {
		return COMMAND;
	}
	
	public void setCOMMAND(int cOMMAND) {
		COMMAND = cOMMAND;
	}
	
	public int getRES_CODE() {
		return RES_CODE;
	}
	
	public void setRES_CODE(int rSLT_COMMAND) {
		RES_CODE = rSLT_COMMAND;
	}
	
	public String getRES_MSG() {
		return RES_MSG;
	}
	
	public void setRES_MSG(String eRROR_MSG) {
		RES_MSG = eRROR_MSG;
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
