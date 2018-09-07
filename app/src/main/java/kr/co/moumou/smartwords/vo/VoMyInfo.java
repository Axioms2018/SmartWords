package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;
import java.util.Date;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.util.StringUtil;

public class VoMyInfo extends VoResponseBase{
	
//	<사용자구분 USERGB>
//	01:정회원 
//	02:예비회원(사용암함)
//	03:온라인회원(체험회원)
//	04:학부모 
//	05:학습관(원장) 
//	06:학습관(교사)
//	07:본사(타부서) 
//	08:관리자(시스템)
//	09:비회원(일반)
	
	private volatile static VoMyInfo instance;

	private String USERID;
	private String USERNAME;
	private String MEMNAME;
	private String MCODE;
	private String COMPCODE;
	private String COMPNAME;
	private String MEMNO;
	private String USERGB;
	private Date LOGN_TM;
	private int GRADE;
	private String TEACHERID;
	private String TEACHERNAME;
	private int ALP_LOUD;
	private int ALP_LOUD_REC;
	private String TEACHER_IP;
	private String PNAME;
	private int RUN_TM;
	private String HAND_GUBN; //왼손(0001) 오른손 (0002)
	private String IMG_PATH;  //이미지 경로.
	private String LEVL;      //큰소리 레벨(0001~0005)
	private String SEX;
	private String CONT_ERR_YN;
	private String defaultHandGUBN;
	private String COMP_PW;
	private String defaultLevl;
	private String FILE_TRANS_YN;
	private String STDY_BOOK = "T";
	private String NOTI_NEW_NO;
	private String SYS_ERR_YN = "n";
	private String ISPAY = "N";
	private String CLASS_RULE_YN = "N";
	private int VRSN;
	private String STDY_TERM;
	
	// 2016 08 1st. 학생이 특정 앱 사용시 교사pc에 경고 문구 보내기
	private ArrayList<VoWarningAppList> DEF_APP_LIST = new ArrayList<VoWarningAppList>();
	
	public static VoMyInfo getInstance() {
		if (instance == null) {
			synchronized (VoMyInfo.class) {
				if (instance == null) {
					instance = new VoMyInfo();
				}
			}
		}
		return instance;
	}

	public VoMyInfo() {
		instance = this;
	}

	public boolean isPrevMemeber(){
        return getIntUSERGB() == Constant.USER_TYPE_STUDENT_RESERVE;
    }
	public boolean isPayedUser(){
		if(isPrevMemeber()){
			return true;
		}
		
		if("o".equals(STDY_BOOK.toLowerCase())){
			if("n".equals(ISPAY.toLowerCase())){
				return false;
			}
		}
		return true;
	}
	

	public String getISPAY() {
		return ISPAY;
	}

	public void setISPAY(String iSPAY) {
		ISPAY = iSPAY;
	}
	
	public String getCLASS_RULE_YN() {
		return CLASS_RULE_YN;
	}

	public void setCLASS_RULE_YN(String cLASS_RULE_YN) {
		CLASS_RULE_YN = cLASS_RULE_YN;
	}

	public String getSYS_ERR_YN() {
		if(SYS_ERR_YN == null || SYS_ERR_YN.equals("")){
			return "y";
		}
		return SYS_ERR_YN;
	}

	public void setSYS_ERR_YN(String sYS_ERR_YN) {
		SYS_ERR_YN = sYS_ERR_YN;
	}

	public boolean isCanRecFileTrans(){
        return !(StringUtil.isNull(FILE_TRANS_YN) || "N".equals(FILE_TRANS_YN));
    }

	public String getCOMP_PW() {
		return COMP_PW;
	}

	public void setCOMP_PW(String cOMP_PW) {
		COMP_PW = cOMP_PW;
	}

	public String getSTDY_BOOK() {
		return STDY_BOOK;
	}

	public void setSTDY_BOOK(String sTDY_BOOK) {
		STDY_BOOK = sTDY_BOOK;
	}

	public String getFILE_TRANS_YN() {
		return FILE_TRANS_YN;
	}
	public void setFILE_TRANS_YN(String FILE_TRANS_YN) {
		this.FILE_TRANS_YN = FILE_TRANS_YN;
	}

	public String getPassword() {
		if(StringUtil.isNull(COMP_PW)){
			COMP_PW = "1111";
		}
		return COMP_PW;
	}

	public void setPassword(String password) {
		this.COMP_PW = password;
	}

	public String getSEX() {
		return SEX;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public String getCOMPNAME() {
		if(StringUtil.isNull(COMPNAME)){
			return "무무 학습관";
		}
		return COMPNAME;
	}
	
	public boolean isExperienceMember(){
        return USERID.startsWith(Constant.EXPERIENCE_USER_ID_PREFIX);
    }
	
	public String getMCODE() {
		if(StringUtil.isNull(MCODE)){
			return "M00000000";
		}
		return MCODE;
	}

	public String getHAND_GUBN() {
		if(StringUtil.isNull(HAND_GUBN)){
			return "0001";
		}
		return HAND_GUBN;
	}
	
	public String getSTDY_TERM() {
		return STDY_TERM;
	}

	public void setSTDY_TERM(String sTDY_TERM) {
		STDY_TERM = sTDY_TERM;
	}

	public boolean isLeftHand(){
        return "0001".equals(HAND_GUBN);
    }
	public void setHandGUBN(boolean isLeftHand){
		if(isLeftHand){
			HAND_GUBN = "0001";
		}else{
			HAND_GUBN = "0002";
		}
	}

	public void setLoudLevel(int level){
		LEVL = String.format("%04d", level);
	}
	
	public boolean isDataChanged(){
		if(!defaultHandGUBN.equals(HAND_GUBN)){
			return true;
		}
        return !defaultLevl.equals(LEVL);

    }
	public String getDefaultHandGUBN() {
		return defaultHandGUBN;
	}

	public void setDefaultHandGUBN(String defaultHandGUBN) {
		this.defaultHandGUBN = defaultHandGUBN;
	}

	public String getDefaultLevl() {
		return defaultLevl;
	}

	public void setDefaultLevl(String defaultLevl) {
		this.defaultLevl = defaultLevl;
	}

	public void setHAND_GUBN(String hAND_GUBN) {
		HAND_GUBN = hAND_GUBN;
	}

	public String getIMG_PATH() {
		return IMG_PATH;
	}

	public void setIMG_PATH(String iMG_PATH) {
		IMG_PATH = iMG_PATH;
	}

	public String getLEVL() {
		if(StringUtil.isNull(LEVL)){
			return "0003";
		}
		return LEVL;
	}
	public int getLoudLevel(){
		return Integer.parseInt(getLEVL());
	}

	public void setLEVL(String lEVL) {
		LEVL = lEVL;
	}

	public String getPNAME() {
		return PNAME;
	}

	public void setPNAME(String pNAME) {
		PNAME = pNAME;
	}

	public int getRUN_TM() {
		return RUN_TM;
	}

	public void setRUN_TM(int rUN_TM) {
		RUN_TM = rUN_TM;
	}

	public void setMCODE(String mCODE) {
		MCODE = mCODE;
	}

	public void setCOMPNAME(String cOMPNAME) {
		COMPNAME = cOMPNAME;
	}

	public String getTEACHER_IP() {
		return TEACHER_IP;
	}
	public void setTEACHER_IP(String tEACHER_IP) {
		TEACHER_IP = tEACHER_IP;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getTEACHERID() {
		return TEACHERID;
	}
	public void setTEACHERID(String tEACHERID) {
		TEACHERID = tEACHERID;
	}
	public String getTEACHERNAME() {
		return TEACHERNAME;
	}
	public void setTEACHERNAME(String tEACHERNAME) {
		TEACHERNAME = tEACHERNAME;
	}
	public String getCOMPCODE() {
		return COMPCODE;
	}
	public void setCOMPCODE(String cOMPCODE) {
		COMPCODE = cOMPCODE;
	}
	public String getMEMNO() {
		return MEMNO;
	}
	public void setMEMNO(String mEMNO) {
		MEMNO = mEMNO;
	}
	public String getUSERGB() {
		return USERGB;
	}
	public int getIntUSERGB(){
		if(StringUtil.isNull(USERGB)){
			return 1;
		}
		return Integer.parseInt(USERGB);
	}
	public void setUSERGB(String uSERGB) {
		USERGB = uSERGB;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getMEMNAME() {
		if (USERID == null)
			return MEMNAME;
		
		if(Constant.EXPERIENCE_USER_ID_PREFIX.startsWith(USERID)){
			return "체험회원";
		}
		return MEMNAME;
	}

	public void setMEMNAME(String mEMNAME) {
		MEMNAME = mEMNAME;
	}

	public Date getLOGN_TM() {
		return LOGN_TM;
	}

	public void setLOGN_TM(Date lOGN_TM) {
		LOGN_TM = lOGN_TM;
	}
	
	public int getGRADE() {
		return GRADE;
	}


	public void setGRADE(int gRADE) {
		GRADE = gRADE;
	}


	public int getALP_LOUD() {
		return ALP_LOUD;
	}


	public void setALP_LOUD(int aLP_LOUD) {
		ALP_LOUD = aLP_LOUD;
	}


	public int getALP_LOUD_REC() {
		return ALP_LOUD_REC;
	}


	public void setALP_LOUD_REC(int aLP_LOUD_REC) {
		ALP_LOUD_REC = aLP_LOUD_REC;
	}

	public String getCONT_ERR_YN() {
		return CONT_ERR_YN;
	}

	public void setCONT_ERR_YN(String cONT_ERR_YN) {
		CONT_ERR_YN = cONT_ERR_YN;
	}

	public String getNOTI_NEW_NO() {
		return NOTI_NEW_NO;
	}

	public void setNOTI_NEW_NO(String nOTI_NEW_NO) {
		NOTI_NEW_NO = nOTI_NEW_NO;
	}

	public ArrayList<VoWarningAppList> getDEF_APP_LIST() {
		return DEF_APP_LIST;
	}

	public void setDEF_APP_LIST(ArrayList<VoWarningAppList> dEF_APP_LIST) {
		DEF_APP_LIST = dEF_APP_LIST;
	}

	public int getVRSN() {
		if (VRSN <= 0)
			VRSN = 1;
		else if (VRSN > 4)
			VRSN = 4;
		
		return VRSN;
	}

	public void setVRSN(int vRSN) {
		VRSN = vRSN;
	}


	public class VoWarningAppList  {
		private String PACKAGE_NM;
		private String NAME;
		
		public String getPACKAGE_NM() {
			return PACKAGE_NM;
		}
		public void setPACKAGE_NM(String pACKAGE_NM) {
			PACKAGE_NM = pACKAGE_NM;
		}
		public String getNAME() {
			return NAME;
		}
		public void setNAME(String nAME) {
			NAME = nAME;
		}
		
		
	}
	
}
