package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoWordsDayList extends VoBase {

	private String STD_LEVEL;
	
	private ArrayList<VoDay> DAYSTDLIST = new ArrayList<VoDay>();

	private ArrayList<VoDay> REVIEWLIST = new ArrayList<VoDay>();
	
	private VoCertificate CERTIFICATE = null;
	
	public String getSTD_LEVEL() {
		return STD_LEVEL;
	}

	public void setSTD_LEVEL(String sTD_LEVEL) {
		STD_LEVEL = sTD_LEVEL;
	}

	public ArrayList<VoDay> getDAYSTDLIST() {
		return DAYSTDLIST;
	}

	public void setDAYSTDLIST(ArrayList<VoDay> dAYSTDLIST) {
		DAYSTDLIST = dAYSTDLIST;
	}

	public ArrayList<VoDay> getREVIEWLIST() {
		return REVIEWLIST;
	}

	public void setREVIEWLIST(ArrayList<VoDay> rEVIEWLIST) {
		REVIEWLIST = rEVIEWLIST;
	}

	public int getTotalList() {
		return DAYSTDLIST.size();
	}
	
	public VoCertificate getCERTIFICATE() {
		return CERTIFICATE;
	}

	public void setCERTIFICATE(VoCertificate cERTIFICATE) {
		CERTIFICATE = cERTIFICATE;
	}




	public class VoDay {
		public static final int OPENGB_PASS = 1;
		public static final int DAY_NONE = 0;
		public static final int DAY_ING = 1;
		public static final int DAY_PASS = 2;
		public static final int DAY_FAIL = 3;
		public static final int DAY_CERT = 9;

		private int STD_DAY;
		private int STATUS;			//본)학습내역 0:미진행, 1:진행중, 2:pass, 3:fail, 9:인증
		private int RSTATUS;		//(재)학습내역 0:미진행, 1:진행중,2:완료
		private int OPEN_GB;		//지정Day학습오픈 0:미오픈 1:오픈	
		private int LAST_WORD_PER;	//마지막 완료한 학습의 점수 (UI사용)
		private int WORD_PER;		//(본)학습 점수
		private int RWORD_PER;		//(재)학습 점수
		
		public int getSTD_DAY() {
			return STD_DAY;
		}
		
		public void setSTD_DAY(int sTD_DAY) {
			STD_DAY = sTD_DAY;
		}
		
		public int getSTATUS() {
			return STATUS;
		}
		
		public void setSTATUS(int sTATUS) {
			STATUS = sTATUS;
		}

		public int getRSTATUS() {
			return RSTATUS;
		}

		public void setRSTATUS(int rSTATUS) {
			RSTATUS = rSTATUS;
		}

		public int getOPEN_GB() {
			return OPEN_GB;
		}

		public void setOPEN_GB(int oPEN_GB) {
			OPEN_GB = oPEN_GB;
		}

		public int getLAST_WORD_PER() {
			return LAST_WORD_PER;
		}

		public void setLAST_WORD_PER(int lAST_WORD_PER) {
			LAST_WORD_PER = lAST_WORD_PER;
		}

		public int getWORD_PER() {
			return WORD_PER;
		}

		public void setWORD_PER(int wORD_PER) {
			WORD_PER = wORD_PER;
		}

		public int getRWORD_PER() {
			return RWORD_PER;
		}

		public void setRWORD_PER(int rWORD_PER) {
			RWORD_PER = rWORD_PER;
		}

	}
	
}
