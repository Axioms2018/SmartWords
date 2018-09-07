package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoWordsSaveList {
	
	private volatile static VoWordsSaveList instance;
	
	private ArrayList<VoWordsSave> SAVE_WORDS_LIST = new ArrayList<>();
	
	public static VoWordsSaveList getInstance() {
		if(instance == null) {
			synchronized (VoWordsSaveList.class) {
				if(instance == null) {
					instance = new VoWordsSaveList();
				}
			}
		}
		return instance;
	}

	public ArrayList<VoWordsSave> getSAVE_WORDS_LIST() {
		return SAVE_WORDS_LIST;
	}

	public void setSAVE_WORDS_LIST(ArrayList<VoWordsSave> sAVE_WORDS_LIST) {
		SAVE_WORDS_LIST = sAVE_WORDS_LIST;
	}
	
	public void putSAVE_WORDS_LIST(VoWordsSave sAVE_WORDS) {
		SAVE_WORDS_LIST.add(sAVE_WORDS);
	}

	public void deleteSAVE_WORDS_LIST() {
		SAVE_WORDS_LIST.clear();
	}

	public class VoWordsSave {
		
		private String USERID;		//회원아이디
		private String STD_W_GB;	//데일리 : D / 리뷰 : R
		private String STD_GB;		//본학습 : S / 재학습 : R
		private String STD_LEVEL; 	//학습레벨
		private int STD_DAY;		//학습데이
		private int WORD_SEQ;		//문항_SEQ(고유번호)
		private int STD_NUM;		//학습문항번호
		private String USER_ANS;	//사용자답안
		private String ANSWER;		//정답
		private String RESULT_YN;	//정답여부(Y/N)
		private String STD_STEP;	//학습영역(TEST1:103002,... 공통코드 103..)
		private int STD_TIME;		//학습시간 ※초단위
		private String STD_TYPE;	//문항구분(한글객관식:104001,... 공통코드 104..)
		
		public String getUSERID() {
			return USERID;
		}
		
		public void setUSERID(String uSERID) {
			USERID = uSERID;
		}
		
		public String getSTD_W_GB() {
			return STD_W_GB;
		}

		public void setSTD_W_GB(String sTD_W_GB) {
			STD_W_GB = sTD_W_GB;
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
		
		public int getWORD_SEQ() {
			return WORD_SEQ;
		}
		
		public void setWORD_SEQ(int wORD_SEQ) {
			WORD_SEQ = wORD_SEQ;
		}
		
		public int getSTD_NUM() {
			return STD_NUM;
		}
		
		public void setSTD_NUM(int sTD_NUM) {
			STD_NUM = sTD_NUM;
		}
		
		public String getUSER_ANS() {
			return USER_ANS;
		}
		
		public void setUSER_ANS(String uSER_ANS) {
			USER_ANS = uSER_ANS;
		}
		
		public String getANSWER() {
			return ANSWER;
		}
		
		public void setANSWER(String aNSWER) {
			ANSWER = aNSWER;
		}
		
		public String getRESULT_YN() {
			return RESULT_YN;
		}
		
		public void setRESULT_YN(String rESULT_YN) {
			RESULT_YN = rESULT_YN;
		}
		
		public String getSTD_STEP() {
			return STD_STEP;
		}
		
		public void setSTD_STEP(String sTD_STEP) {
			STD_STEP = sTD_STEP;
		}
		
		public int getSTD_TIME() {
			return STD_TIME;
		}
		
		public void setSTD_TIME(int sTD_TIME) {
			STD_TIME = sTD_TIME;
		}
		
		public String getSTD_TYPE() {
			return STD_TYPE;
		}
		
		public void setSTD_TYPE(String sTD_TYPE) {
			STD_TYPE = sTD_TYPE;
		}
	}

}
