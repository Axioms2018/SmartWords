package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoRankData {	
	private int COMMAND;
	private int RES_CODE;
	private String RES_MSG;
	private String SESSIONID;
	private String USERID;
	private String USERNAME;
	private int KNOW;
	private int COMP_AVG;
	private int ALL_AVG;
	private ArrayList<VoRankDetail> RANKING = new ArrayList<VoRankDetail>();
	
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
	
	public String getUSERNAME() {
		return USERNAME;
	}
	
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	
	public int getKNOW() {
		return KNOW;
	}
	
	public void setKNOW(int kNOW) {
		KNOW = kNOW;
	}
	
	public int getCOMP_AVG() {
		return COMP_AVG;
	}
	
	public void setCOMP_AVG(int cOMP_AVG) {
		COMP_AVG = cOMP_AVG;
	}
	
	public int getALL_AVG() {
		return ALL_AVG;
	}
	
	public void setALL_AVG(int aLL_AVG) {
		ALL_AVG = aLL_AVG;
	}

	public ArrayList<VoRankDetail> getRANKING() {
		return RANKING;
	}

	public void setRANKING(ArrayList<VoRankDetail> rANKING) {
		RANKING = rANKING;
	}	
}
