package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoNoteFrame {
	private int COMMAND;
	private int RSLT_COMMAND;
	private String ERROR_MSG;
	private String SESSIONID;
	private String USERID;
	private int RIGHT_CNT;
	private int WRONG_CNT;
	private ArrayList<VoNoteData> STD_LEVEL = new ArrayList<VoNoteData>();
	
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
	
	public ArrayList<VoNoteData> getSTD_LEVEL() {
		return STD_LEVEL;
	}
	
	public void setSTD_LEVEL(ArrayList<VoNoteData> sTD_LEVEL) {
		STD_LEVEL = sTD_LEVEL;
	}	
}
