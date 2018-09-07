package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

import kr.co.moumou.smartwords.util.StringUtil;

public class VoWordsTestList extends VoResponseBase {

	private volatile static VoWordsTestList instance;
	
	//코드 Review : 103001,  Test1 : 103002, Practice : 103003, Test2 : 103004, Test3 : 103005, ReviewTest1 : 103006
	private String STD_LEVEL;
	private int STD_DAY;
	private String STD_W_GB;		//데일리 D: 학습, R: 리뷰
	private String STD_GB;			//평가구분 S: 학습, R: 재학습
	private String CURR_ZONE; 		//진행 코드
	private int CURR_QUST_NUM;		//진행 번호
	private String PATH;			//음성 대표 URL
	
	private ArrayList<VoWordsTestDownload> DOWNLOAD = new ArrayList<VoWordsTestDownload>();
	
	private VoWordsTest REVIEW;
	private VoWordsTest TEST1;
	private VoWordsTest PRACTICE;
	private VoWordsTest TEST2;
	private VoWordsTest TEST3;
	private VoWordsTest QUIZ;
	
	public static VoWordsTestList getInstance() {
		if(instance == null) {
			synchronized (VoWordsTestList.class) {
				if(instance == null){
					instance = new VoWordsTestList();
				}
			}
		}
		return instance;
	}

	public VoWordsTestList() {
		instance = this;
	}
	
	public void clear() {
		instance = null;
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

	public String getCURR_ZONE() {
		return CURR_ZONE;
	}

	public void setCURR_ZONE(String cURR_ZONE) {
		CURR_ZONE = cURR_ZONE;
	}

	public int getCURR_QUST_NUM() {
		return CURR_QUST_NUM;
	}

	public void setCURR_QUST_NUM(int cURR_QUST_NUM) {
		CURR_QUST_NUM = cURR_QUST_NUM;
	}

	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public ArrayList<VoWordsTestDownload> getDOWNLOAD() {
		return DOWNLOAD;
	}

	public void setDOWNLOAD(ArrayList<VoWordsTestDownload> dOWNLOAD) {
		DOWNLOAD = dOWNLOAD;
	}

	public VoWordsTest getREVIEW() {
		return REVIEW;
	}

	public void setREVIEW(VoWordsTest rEVIEW) {
		REVIEW = rEVIEW;
	}

	public VoWordsTest getTEST1() {
		return TEST1;
	}

	public void setTEST1(VoWordsTest tEST1) {
		TEST1 = tEST1;
	}

	public VoWordsTest getPRACTICE() {
		return PRACTICE;
	}

	public void setPRACTICE(VoWordsTest pRACTICE) {
		PRACTICE = pRACTICE;
	}

	public VoWordsTest getTEST2() {
		return TEST2;
	}

	public void setTEST2(VoWordsTest tEST2) {
		TEST2 = tEST2;
	}

	public VoWordsTest getTEST3() {
		return TEST3;
	}

	public void setTEST3(VoWordsTest tEST3) {
		TEST3 = tEST3;
	}
	
	public VoWordsTest getQUIZ() {
		return QUIZ;
	}
	
	public VoWordsTest getREVIEWTEST1() {
		// TODO :: REVIEWTEST
		return TEST1;
	}
	
	public VoWordsTest getREVIEWTEST2() {
		// TODO :: REVIEWTEST
		return TEST2;
	}
	
	public VoWordsTest getREVIEWTEST3() {
		// TODO :: REVIEWTEST
		return TEST3;
	}
	
	public void setQUIZ(VoWordsTest qUIZ) {
		QUIZ = qUIZ;
	}
	
	public VoWordsTest getVoWordsTest(String code) {
		
		switch (code) {
		case VoWordsTest.CODE_REVIEW:
			return getREVIEW();
		
		case VoWordsTest.CODE_TEST1:
			return getTEST1();
			
		case VoWordsTest.CODE_PRACTICE:
			return getPRACTICE();
			
		case VoWordsTest.CODE_TEST2:
			return getTEST2();
			
		case VoWordsTest.CODE_TEST3:
			return getTEST3();
			
		case VoWordsTest.CODE_QUIZ:
			return getQUIZ();
			
		case VoWordsTest.CODE_REVIEW_TEST1:
			return getREVIEWTEST1();
			
		case VoWordsTest.CODE_REVIEW_TEST2:
			return getREVIEWTEST2();
			
		case VoWordsTest.CODE_REVIEW_TEST3:
			return getREVIEWTEST3();

		default:
			return null;
		}
	}
	
	public String getNextWordsStdStep(String code) {
		if(StringUtil.isNull(code)) return null;
		
		switch (code) {
		case VoWordsTest.CODE_REVIEW:
			return VoWordsTest.CODE_TEST1;
		
		case VoWordsTest.CODE_TEST1:
			return VoWordsTest.CODE_PRACTICE;
			
		case VoWordsTest.CODE_PRACTICE:
			return VoWordsTest.CODE_TEST2;
			
		case VoWordsTest.CODE_TEST2:
			return VoWordsTest.CODE_TEST3;
			
		default:
			return null;
		}
	}
	
	/**
	 * 코드가 Practice이상일 경우 Practice는 완료이다
	 */
	public boolean isCompletePractice(String code) {
		
		if(StringUtil.isNull(code)) return false;
		
		switch (code) {
		case VoWordsTest.CODE_REVIEW:
			return false;
		
		case VoWordsTest.CODE_TEST1:
			return false;
			
		case VoWordsTest.CODE_PRACTICE:
			return false;
			
		case VoWordsTest.CODE_TEST2:
			return true;
			
		case VoWordsTest.CODE_TEST3:
			return true;

		case VoWordsTest.CODE_QUIZ:
			return true;
			
		case VoWordsTest.CODE_REVIEW_TEST1:
			return true;
			
		case VoWordsTest.CODE_REVIEW_TEST2:
			return true;
			
		default:
			return false; 
		}
	}

	public class VoWordsTest {
		
		public static final String CODE_REVIEW = "103001";		//Review
		public static final String CODE_TEST1 = "103002";		//Test1
		public static final String CODE_PRACTICE = "103003";	//Practice
		public static final String CODE_TEST2 = "103004";		//Test2
		public static final String CODE_TEST3 = "103005";		//Test3
		public static final String CODE_QUIZ = "103006";		//Quiz
		public static final String CODE_REVIEW_TEST1 = "103007";	//Review Test1
		public static final String CODE_REVIEW_TEST2 = "103008";	//Review Test2
		public static final String CODE_REVIEW_TEST3 = "103009";	//Review Test3
		
		private String STD_NAME;		//타입이름
		private String STD_STEP;		//코드 Review : 103001,  Test1 : 103002, Practice : 103003, Test2 : 103004, Test3 : 103005
		private String STD_TYPE;		//질문타입
		private int STD_TIME;			//시간
		
		ArrayList<VoWordQuest> STD_LIST = new ArrayList<VoWordQuest>();

		public String getSTD_NAME() {
			return STD_NAME;
		}

		public void setSTD_NAME(String sTD_NAME) {
			STD_NAME = sTD_NAME;
		}

		public String getSTD_STEP() {
			return STD_STEP;
		}

		public void setSTD_STEP(String sTD_STEP) {
			STD_STEP = sTD_STEP;
		}

		public String getSTD_TYPE() {
			return STD_TYPE;
		}

		public void setSTD_TYPE(String sTD_TYPE) {
			STD_TYPE = sTD_TYPE;
		}

		public int getSTD_TIME() {
			return STD_TIME;
		}

		public void setSTD_TIME(int sTD_TIME) {
			STD_TIME = sTD_TIME;
		}

		public ArrayList<VoWordQuest> getSTD_LIST() {
			return STD_LIST;
		}

		public void setSTD_LIST(ArrayList<VoWordQuest> sTD_LIST) {
			STD_LIST = sTD_LIST;
		}
	}

	public class VoWordQuest {

		public static final String QUIZ_CODE_TEST1_K = "104001";	//영어 > 한글
		public static final String QUIZ_CODE_TEST1_E = "104002";	//한글 > 영어
		public static final String QUIZ_CODE_PRACTICE = "104003";
		public static final String QUIZ_CODE_TEST2 = "104004";
		public static final String QUIZ_CODE_TEST3 = "104005";
		public static final String QUIZ_CODE_REVIEWTEST1 = "104006";

		private String STD_W_GB;	//데일리 D: 학습, R: 리뷰
		private String STD_GB;		//평가구분 S: 학습, R: 재학습
		private String STD_LEVEL;	//레벨
		private int STD_DAY;		//날짜
		private int STD_NUM;
		private String STD_STEP;	//코드 Review : 103001,  Test1 : 103002, Practice : 103003, Test2 : 103004, Test3 : 103005
		private String STD_TYPE;	//질문 타입 Test1일 경우(E/K) , 1)영어 > 우리말, 2)우리말 > 영어
		private int WORD_SEQ;		//문제 고유번호
		private String WORD;		//문제 TEST1
		private String MEAN;		//뜻
		private String STD_MP3;		//음원
		private String ANSWER;		//정답
		private String USER_ANS;	//사용자 답
		private String RESULT_YN;	//정답여부(Y/N)
		private int TIME_USER;		//사용자가 푼시간

		private String SENTENCE;	//TEST3 영어문장
		private String SEN_MEAN;	//TEST3 영어뜻

		ArrayList<VoWordsBogi> BOGI_LIST = new ArrayList<VoWordsBogi>();
		
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

		public int getSTD_NUM() {
			return STD_NUM;
		}

		public void setSTD_NUM(int sTD_NUM) {
			STD_NUM = sTD_NUM;
		}

		public String getSTD_STEP() {
			return STD_STEP;
		}

		public void setSTD_STEP(String sTD_STEP) {
			STD_STEP = sTD_STEP;
		}

		public String getSTD_TYPE() {
			return STD_TYPE;
		}

		public void setSTD_TYPE(String sTD_TYPE) {
			STD_TYPE = sTD_TYPE;
		}

		public int getWORD_SEQ() {
			return WORD_SEQ;
		}

		public void setWORD_SEQ(int wORD_SEQ) {
			WORD_SEQ = wORD_SEQ;
		}

		public String getWORD() {
			return WORD;
		}

		public void setWORD(String wORD) {
			WORD = wORD;
		}

		public String getMEAN() {
			return MEAN;
		}

		public void setMEAN(String mEAN) {
			MEAN = mEAN;
		}

		public String getSTD_MP3() {
			return STD_MP3;
		}

		public void setSTD_MP3(String sTD_MP3) {
			STD_MP3 = sTD_MP3;
		}

		public String getSENTENCE() {
			return SENTENCE;
		}

		public void setSENTENCE(String sENTENCE) {
			SENTENCE = sENTENCE;
		}

		public String getSEN_MEAN() {
			return SEN_MEAN;
		}

		public void setSEN_MEAN(String sEN_MEAN) {
			SEN_MEAN = sEN_MEAN;
		}

		public String getANSWER() {
			return ANSWER;
		}

		public void setANSWER(String aNSWER) {
			ANSWER = aNSWER;
		}

		public String getUSER_ANS() {
			return USER_ANS;
		}

		public void setUSER_ANS(String uSER_ANS) {
			USER_ANS = uSER_ANS;
		}

		public String getRESULT_YN() {
			return RESULT_YN;
		}

		public void setRESULT_YN(String rESULT_YN) {
			RESULT_YN = rESULT_YN;
		}

		public int getTIME_USER() {
			return TIME_USER;
		}

		public void setTIME_USER(int tIME_USER) {
			TIME_USER = tIME_USER;
		}

		public ArrayList<VoWordsBogi> getBOGI_LIST() {
			return BOGI_LIST;
		}

		public void setBOGI_LIST(ArrayList<VoWordsBogi> bOGI_LIST) {
			BOGI_LIST = bOGI_LIST;
		}
	}
	
	public class VoWordsBogi {
		
		private String BOGI_WORD;	//보기텍스트
		private int BOGI_SEQ;		//보기번호
		
		public String getBOGI_WORD() {
			return BOGI_WORD;
		}
		public void setBOGI_WORD(String bOGI_WORD) {
			BOGI_WORD = bOGI_WORD;
		}
		public int getBOGI_SEQ() {
			return BOGI_SEQ;
		}
		public void setBOGI_SEQ(int bOGI_SEQ) {
			BOGI_SEQ = bOGI_SEQ;
		}
	}
}
