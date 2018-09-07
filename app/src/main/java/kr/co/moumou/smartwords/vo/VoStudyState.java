package kr.co.moumou.smartwords.vo;

import java.util.Date;
import java.util.HashMap;

import kr.co.moumou.smartwords.communication.ConstantsCommParameter;

public class VoStudyState {
	
	private String COMMAND;
	private String SESSIONID;
	private String CON_SEQ;
	private String USERID;
	private String PCODE;
	private String CHACI;
	private String COMPCODE;
	private String STDY_GUBN;
	private int STDY_GUBN_SEQ;
	private String MEMNAME;
	private String MEMNO;
	private String PNAME;
	private String TEACHERID;
	private String TEACHERNAME;
	private int GRADE;
	private String STDY_STGE;
	private String STDY_STGE_NM;
	private String STDY_PGSS_STAT;
	private String STDY_PGSS_STAT_NM;
	private int RUN_TM;
	private int ACT_AVG_DEC;
	private Date STRT_TM;
	private Date END_TM;
	private Date REG_DT;
	private String SRVR_SEND_YN;
	private Date SRVR_SEND_TM;
	private String COMP_INOUT_GUBN;
	private int VRSN;
	private String CLOS_YN;
	private String CNFM_YN;
	private String RESTDY_NUM;
	private String RESTDY_YN;
	private String STDY_TYPE;
	private String REC_YN;
	private String REC_URL;
	private String REC_FILES;

	private volatile static VoStudyState instance;

	public static VoStudyState getInstance() {
		if (instance == null) {
			synchronized (VoStudyState.class) {
				if (instance == null) {
					instance = new VoStudyState();
				}
			}
		}
		return instance;
	}

	protected VoStudyState() {
	}

	


	private HashMap<String, Object> dataMap = new HashMap<String, Object>();
	
	public HashMap<String, Object> getDataMap(){
//		if(dataMap.size() > 0){
//			return dataMap;
//		}
		
		dataMap.put(ConstantsCommParameter.Keys.COMMAND, COMMAND);
		dataMap.put(ConstantsCommParameter.Keys.SESSIONID, SESSIONID);
		dataMap.put(ConstantsCommParameter.Keys.USERID, USERID);
		dataMap.put(ConstantsCommParameter.Keys.PCODE, PCODE);
		dataMap.put(ConstantsCommParameter.Keys.CON_SEQ, CON_SEQ);
		dataMap.put(ConstantsCommParameter.Keys.CHACI, CHACI);
		dataMap.put(ConstantsCommParameter.Keys.STDY_GUBN, STDY_GUBN);
		dataMap.put(ConstantsCommParameter.Keys.STDY_GUBN_SEQ, STDY_GUBN_SEQ);
//		dataMap.put(ConstantsCommParameter.Keys.MEMNAME, MEMNAME);
		dataMap.put(ConstantsCommParameter.Keys.PNAME, PNAME);
		dataMap.put(ConstantsCommParameter.Keys.TEACHERID, TEACHERID);
		dataMap.put(ConstantsCommParameter.Keys.TEACHERNAME, TEACHERNAME);
//		dataMap.put(ConstantsCommParameter.Keys.GRADE, GRADE);
		dataMap.put(ConstantsCommParameter.Keys.STDY_STGE, STDY_STGE);
		dataMap.put(ConstantsCommParameter.Keys.STDY_STGE_NM, STDY_STGE_NM);
		dataMap.put(ConstantsCommParameter.Keys.STDY_PGSS_STAT, STDY_PGSS_STAT);
		dataMap.put(ConstantsCommParameter.Keys.STDY_PGSS_STAT_NM, STDY_PGSS_STAT_NM);
		dataMap.put(ConstantsCommParameter.Keys.RUN_TM, RUN_TM);
//		dataMap.put(ConstantsCommParameter.Keys.ACT_AVG_DEC, ACT_AVG_DEC);
		dataMap.put(ConstantsCommParameter.Keys.STRT_TM, STRT_TM);
		dataMap.put(ConstantsCommParameter.Keys.END_TM, END_TM);
//		dataMap.put(ConstantsCommParameter.Keys.REG_DT, REG_DT);
//		dataMap.put(ConstantsCommParameter.Keys.SRVR_SEND_YN, SRVR_SEND_YN);
//		dataMap.put(ConstantsCommParameter.Keys.SRVR_SEND_TM, SRVR_SEND_TM);
		dataMap.put(ConstantsCommParameter.Keys.COMP_INOUT_GUBN, COMP_INOUT_GUBN);
//		dataMap.put(ConstantsCommParameter.Keys.VRSN, VRSN);
		dataMap.put(ConstantsCommParameter.Keys.CLOS_YN, CLOS_YN);
		dataMap.put(ConstantsCommParameter.Keys.CNFM_YN, CNFM_YN);
		dataMap.put(ConstantsCommParameter.Keys.RESTDY_NUM, RESTDY_NUM);
		dataMap.put(ConstantsCommParameter.Keys.RESTDY_YN, RESTDY_YN);
		dataMap.put(ConstantsCommParameter.Keys.STDY_TYPE, STDY_TYPE);
		dataMap.put(ConstantsCommParameter.Keys.REC_YN, REC_YN);
		dataMap.put(ConstantsCommParameter.Keys.REC_URL, REC_URL);
		dataMap.put(ConstantsCommParameter.Keys.REC_FILES, REC_FILES);
	
		return dataMap;
	}
	
	public void clear(){
		dataMap.clear();
		instance = null;
	}

	public String getCOMMAND() {
		return COMMAND;
	}

	public void setCOMMAND(String cOMMAND) {
		COMMAND = cOMMAND;
	}

	public String getSESSIONID() {
		return SESSIONID;
	}

	public void setSESSIONID(String sESSIONID) {
		SESSIONID = sESSIONID;
	}

	public String getCON_SEQ() {
		return CON_SEQ;
	}

	public void setCON_SEQ(String cON_SEQ) {
		CON_SEQ = cON_SEQ;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public String getPCODE() {
		return PCODE;
	}

	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}

	public String getREC_URL() {
		return REC_URL;
	}

	public void setREC_URL(String rEC_URL) {
		REC_URL = rEC_URL;
	}

	public String getREC_FILES() {
		return REC_FILES;
	}

	public void setREC_FILES(String rEC_FILES) {
		REC_FILES = rEC_FILES;
	}

	public String getCHACI() {
		return CHACI;
	}

	public void setCHACI(String cHACI) {
		CHACI = cHACI;
	}

	public String getCOMPCODE() {
		return COMPCODE;
	}

	public void setCOMPCODE(String cOMPCODE) {
		COMPCODE = cOMPCODE;
	}

	public String getSTDY_GUBN() {
		return STDY_GUBN;
	}

	public void setSTDY_GUBN(String sTDY_GUBN) {
		STDY_GUBN = sTDY_GUBN;
	}

	public int getSTDY_GUBN_SEQ() {
		return STDY_GUBN_SEQ;
	}

	public void setSTDY_GUBN_SEQ(int sTDY_GUBN_SEQ) {
		STDY_GUBN_SEQ = sTDY_GUBN_SEQ;
	}

	public String getMEMNAME() {
		return MEMNAME;
	}

	public void setMEMNAME(String mEMNAME) {
		MEMNAME = mEMNAME;
	}

	public String getPNAME() {
		return PNAME;
	}

	public void setPNAME(String pNAME) {
		PNAME = pNAME;
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

	public String getSTDY_STGE() {
		return STDY_STGE;
	}

	public void setSTDY_STGE(String sTDY_STGE) {
		STDY_STGE = sTDY_STGE;
	}

	public String getSTDY_STGE_NM() {
		return STDY_STGE_NM;
	}

	public void setSTDY_STGE_NM(String sTDY_STGE_NM) {
		STDY_STGE_NM = sTDY_STGE_NM;
	}

	public String getSTDY_PGSS_STAT() {
		return STDY_PGSS_STAT;
	}

	public void setSTDY_PGSS_STAT(String sTDY_PGSS_STAT) {
		STDY_PGSS_STAT = sTDY_PGSS_STAT;
	}

	public String getSTDY_PGSS_STAT_NM() {
		return STDY_PGSS_STAT_NM;
	}

	public void setSTDY_PGSS_STAT_NM(String sTDY_PGSS_STAT_NM) {
		STDY_PGSS_STAT_NM = sTDY_PGSS_STAT_NM;
	}

	public int getRUN_TM() {
		return RUN_TM;
	}

	public void setRUN_TM(int rUN_TM) {
		RUN_TM = rUN_TM;
	}

	public Date getSTRT_TM() {
		return STRT_TM;
	}

	public void setSTRT_TM(Date sTRT_TM) {
		STRT_TM = sTRT_TM;
	}

	public Date getEND_TM() {
		return END_TM;
	}

	public void setEND_TM(Date eND_TM) {
		END_TM = eND_TM;
	}

	public String getCOMP_INOUT_GUBN() {
		return COMP_INOUT_GUBN;
	}

	public void setCOMP_INOUT_GUBN(String cOMP_INOUT_GUBN) {
		COMP_INOUT_GUBN = cOMP_INOUT_GUBN;
	}

	public String getCLOS_YN() {
		return CLOS_YN;
	}

	public void setCLOS_YN(String cLOS_YN) {
		CLOS_YN = cLOS_YN;
	}

	public String getCNFM_YN() {
		return CNFM_YN;
	}

	public void setCNFM_YN(String cNFM_YN) {
		CNFM_YN = cNFM_YN;
	}

	public String getRESTDY_NUM() {
		return RESTDY_NUM;
	}

	public void setRESTDY_NUM(String rESTDY_NUM) {
		RESTDY_NUM = rESTDY_NUM;
	}

	public String getRESTDY_YN() {
		return RESTDY_YN;
	}

	public void setRESTDY_YN(String rESTDY_YN) {
		RESTDY_YN = rESTDY_YN;
	}

	public String getSTDY_TYPE() {
		return STDY_TYPE;
	}

	public void setSTDY_TYPE(String sTDY_TYPE) {
		STDY_TYPE = sTDY_TYPE;
	}

	public String getREC_YN() {
		return REC_YN;
	}

	public void setREC_YN(String rEC_YN) {
		REC_YN = rEC_YN;
	}

	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public String getMEMNO() {
		return MEMNO;
	}

	public void setMEMNO(String mEMNO) {
		MEMNO = mEMNO;
	}

	public int getGRADE() {
		return GRADE;
	}

	public void setGRADE(int gRADE) {
		GRADE = gRADE;
	}

	public int getACT_AVG_DEC() {
		return ACT_AVG_DEC;
	}

	public void setACT_AVG_DEC(int aCT_AVG_DEC) {
		ACT_AVG_DEC = aCT_AVG_DEC;
	}

	public Date getREG_DT() {
		return REG_DT;
	}

	public void setREG_DT(Date rEG_DT) {
		REG_DT = rEG_DT;
	}

	public String getSRVR_SEND_YN() {
		return SRVR_SEND_YN;
	}

	public void setSRVR_SEND_YN(String sRVR_SEND_YN) {
		SRVR_SEND_YN = sRVR_SEND_YN;
	}

	public Date getSRVR_SEND_TM() {
		return SRVR_SEND_TM;
	}

	public void setSRVR_SEND_TM(Date sRVR_SEND_TM) {
		SRVR_SEND_TM = sRVR_SEND_TM;
	}

	public int getVRSN() {
		return VRSN;
	}

	public void setVRSN(int vRSN) {
		VRSN = vRSN;
	}

	
}
