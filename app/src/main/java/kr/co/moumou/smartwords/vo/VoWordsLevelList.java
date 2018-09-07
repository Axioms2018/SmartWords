package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoWordsLevelList extends VoBase{
	
	private String FIRSTYN;	//최초 접속사인지 여부를 위해
	
	private ArrayList<VoWordsLevel> LEVELSTDLIST = new ArrayList<VoWordsLevel>();

	public String getFIRSTYN() {
		return FIRSTYN;
	}

	public void setFIRSTYN(String fIRSTYN) {
		FIRSTYN = fIRSTYN;
	}

	public ArrayList<VoWordsLevel> getLEVELSTDLIST() {
		return LEVELSTDLIST;
	}

	public void setLEVELSTDLIST(ArrayList<VoWordsLevel> lEVELSTDLIST) {
		LEVELSTDLIST = lEVELSTDLIST;
	}

	public class VoWordsLevel{
		public static final int STUDY_NONE = 0;
		public static final int STUDY_ING = 1;
		public static final int STUDY_FINISH = 2;
		public static final int STUDY_CERTIFICATE = 9;
		
		public static final String LEVEL_LOW = "ele";
		public static final String LEVEL_HIGH = "mid";
		
		private String STD_LEVEL;
		private int STATUS;
		private String GUBUN; //저학년인지 고학년인지
		
		public String getSTD_LEVEL() {
			return STD_LEVEL;
		}
		public void setSTD_LEVEL(String sTD_LEVEL) {
			STD_LEVEL = sTD_LEVEL;
		}
		public int getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(int sTATUS) {
			STATUS = sTATUS;
		}
		public String getGUBUN() {
			return GUBUN;
		}
		public void setGUBUN(String gUBUN) {
			GUBUN = gUBUN;
		}
	}	
}
