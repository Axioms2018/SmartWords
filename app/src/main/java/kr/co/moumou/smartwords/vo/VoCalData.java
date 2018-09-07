package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoCalData {	
	private int STD_DD;	
	private ArrayList<VoCalDetail> USERSTD = new ArrayList<VoCalDetail>();
	
	public int getSTD_DD() {
		return STD_DD;
	}

	public void setSTD_DD(int sTD_DD) {
		STD_DD = sTD_DD;
	}

	public ArrayList<VoCalDetail> getUSERSTD() {
		return USERSTD;
	}

	public void setUSERSTD(ArrayList<VoCalDetail> uSERSTD) {
		USERSTD = uSERSTD;
	}	
}

