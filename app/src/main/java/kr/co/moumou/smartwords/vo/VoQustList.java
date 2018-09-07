package kr.co.moumou.smartwords.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.util.LogUtil;

public class VoQustList extends VoResponseBase {

	private volatile static VoQustList instance;
	private ArrayList<VoStudyChapter> Data = new ArrayList<VoStudyChapter>();
	private ArrayList<VoTemplateAnim> Data1 = new ArrayList<VoTemplateAnim>();
	private int VRSN;
	private String USERID;
	private String PCODE;
	private String CON_SEQ;
	private String CHACI;
	private String STDY_GUBN;
	private int STDY_GUBN_SEQ;
	private String COMPCODE;
	private String MEMNO;
	private String MEMNAME;
	private String PNAME;
	private String PSNAME;
	private String TEACHERID;
	private String TEACHERNAME;
	private String COMP_INOUT_GUBN;
	private Date STRT_TM;
	private Date END_TM;
	private String CLOS_YN;
	private String CNFM_YN;
	private String STDY_TYPE;
	
	public static VoQustList getInstance() {
		if (instance == null) {
			synchronized (VoQustList.class) {
				if (instance == null) {
					instance = new VoQustList();
				}
			}
		}
		return instance;
	}

	
	public String getPSNAME() {
		return PSNAME;
	}


	public void setPSNAME(String pSNAME) {
		PSNAME = pSNAME;
	}


	public String getSTDY_TYPE() {
		return STDY_TYPE;
	}

	public void setSTDY_TYPE(String STDY_TYPE) {
		this.STDY_TYPE = STDY_TYPE;
	}

	public VoQustList() {
		instance = this;
	}

	public void clear() {
		Data.clear();
		instance = null;
	}

	public HashMap<String, Object> getDataMap() {
		if (Data.size() < 0) {
			return null;
		}
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ConstantsCommParameter.Keys.COMMAND, getCOMMAND());
		result.put(ConstantsCommParameter.Keys.ERROR_MSG, "");
		result.put(ConstantsCommParameter.Keys.SESSIONID, getSESSIONID());
		result.put(ConstantsCommParameter.Keys.USERID, getUSERID());
		
		result.put(ConstantsCommParameter.Keys.PCODE, PCODE);
		result.put(ConstantsCommParameter.Keys.CON_SEQ, CON_SEQ);
		result.put(ConstantsCommParameter.Keys.CHACI, CHACI);
		result.put(ConstantsCommParameter.Keys.STDY_GUBN, STDY_GUBN);
		result.put(ConstantsCommParameter.Keys.STDY_GUBN_SEQ, STDY_GUBN_SEQ);
		result.put(ConstantsCommParameter.Keys.COMPCODE, COMPCODE);
		result.put(ConstantsCommParameter.Keys.MEMNO, MEMNO);
		result.put(ConstantsCommParameter.Keys.MEMNAME, MEMNAME);
		result.put(ConstantsCommParameter.Keys.PNAME, PNAME);
		result.put(ConstantsCommParameter.Keys.TEACHERID, TEACHERID);
		result.put(ConstantsCommParameter.Keys.TEACHERNAME, TEACHERNAME);
		result.put(ConstantsCommParameter.Keys.COMP_INOUT_GUBN, COMP_INOUT_GUBN);
		result.put(ConstantsCommParameter.Keys.STRT_TM, STRT_TM);
		result.put(ConstantsCommParameter.Keys.END_TM, END_TM);
		result.put(ConstantsCommParameter.Keys.CLOS_YN, CLOS_YN);
		result.put(ConstantsCommParameter.Keys.CNFM_YN, CNFM_YN);
		result.put(ConstantsCommParameter.Keys.VRSN, VRSN);
		result.put(ConstantsCommParameter.Keys.COMP_INOUT_GUBN, COMP_INOUT_GUBN);
		result.put(ConstantsCommParameter.Keys.STDY_TYPE, STDY_TYPE);
		
		result.put(ConstantsCommParameter.Keys.DATA, Data);
		return result;
	}
	
	public HashMap<String, Object> getDataMap2() {
		if (Data.size() < 0) {
			return null;
		}
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ConstantsCommParameter.Keys.COMMAND, getCOMMAND());
		result.put(ConstantsCommParameter.Keys.ERROR_MSG, "");
		result.put(ConstantsCommParameter.Keys.SESSIONID, getSESSIONID());
		result.put(ConstantsCommParameter.Keys.USERID, getUSERID());
		
		result.put(ConstantsCommParameter.Keys.PCODE, PCODE);
		result.put(ConstantsCommParameter.Keys.CON_SEQ, CON_SEQ);
		result.put(ConstantsCommParameter.Keys.CHACI, "가나다라마바사");
		result.put(ConstantsCommParameter.Keys.STDY_GUBN, STDY_GUBN);
		result.put(ConstantsCommParameter.Keys.STDY_GUBN_SEQ, STDY_GUBN_SEQ);
		result.put(ConstantsCommParameter.Keys.COMPCODE, COMPCODE);
		result.put(ConstantsCommParameter.Keys.MEMNO, MEMNO);
		result.put(ConstantsCommParameter.Keys.MEMNAME, MEMNAME);
		result.put(ConstantsCommParameter.Keys.PNAME, PNAME);
		result.put(ConstantsCommParameter.Keys.TEACHERID, TEACHERID);
		result.put(ConstantsCommParameter.Keys.TEACHERNAME, TEACHERNAME);
		result.put(ConstantsCommParameter.Keys.COMP_INOUT_GUBN, COMP_INOUT_GUBN);
		result.put(ConstantsCommParameter.Keys.STRT_TM, STRT_TM);
		result.put(ConstantsCommParameter.Keys.END_TM, END_TM);
		result.put(ConstantsCommParameter.Keys.CLOS_YN, CLOS_YN);
		result.put(ConstantsCommParameter.Keys.CNFM_YN, CNFM_YN);
		result.put(ConstantsCommParameter.Keys.VRSN, VRSN);
		result.put(ConstantsCommParameter.Keys.COMP_INOUT_GUBN, COMP_INOUT_GUBN);
		result.put(ConstantsCommParameter.Keys.STDY_TYPE, STDY_TYPE);
		
		result.put(ConstantsCommParameter.Keys.DATA, Data);
		return result;
	}
	
	public String getPCODE() {
		return PCODE;
	}

	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}

	public String getCON_SEQ() {
		return CON_SEQ;
	}

	public void setCON_SEQ(String cON_SEQ) {
		CON_SEQ = cON_SEQ;
	}

	public String getCHACI() {
		return CHACI;
	}

	public void setCHACI(String cHACI) {
		CHACI = cHACI;
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

	public String getCOMP_INOUT_GUBN() {
		return COMP_INOUT_GUBN;
	}

	public void setCOMP_INOUT_GUBN(String cOMP_INOUT_GUBN) {
		COMP_INOUT_GUBN = cOMP_INOUT_GUBN;
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

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public ArrayList<VoStudyChapter> getData() {
//		Collections.sort(Data, new SortIncrease());
		return Data;
	}

	public void setData(ArrayList<VoStudyChapter> data) {
		LogUtil.d("VoQustList setData : " + data);
		Data = data;
	}

	public VoStudyChapter getData(int pos) {
//		Collections.sort(Data, new SortIncrease());
		if (Data == null || Data.size() == 0) {
			LogUtil.d("VoStudyChapter Data is null");
			return null;
		}
		
		return Data.get(pos);
	}

	public ArrayList<VoTemplateAnim> getData1() {
		return Data1;
	}

	public void setData1(ArrayList<VoTemplateAnim> data1) {
		Data1 = data1;
	}

	public int getVRSN() {
		return VRSN;
	}

	public void setVRSN(int vRSN) {
		VRSN = vRSN;
	}

	private class SortIncrease implements Comparator<TempIndexInterface> {
		@Override
		public int compare(TempIndexInterface lhs, TempIndexInterface rhs) {
			if (lhs.getSEQ() > rhs.getSEQ()) {
				return 1;
			} else if (lhs.getSEQ() < rhs.getSEQ()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private interface TempIndexInterface {
		int getSEQ();
	}

	public class VoStudyChapter implements TempIndexInterface {
		private String PCODE;
		private String CON_SEQ;
		private String STDY_STGE; // 학습단계
		private String STDY_STGE_NM; // 학습단계명
		private int NRML_TM; // 적정시간
		private int QUST_CNT; // 문항수
		private String STDY_GUDE; // 학습가이드
		private int STDY_NUM; // 학습순번
		private String TCHR_CHCK_YN; // 교사와 점검유무
		private String STGE_BACK_YN; // 단계 외 뒤로가기 유무
		private String BACK_YN; // 단계 내 뒤로가기 유무
		private String PAPR_YN; // 답안지 유무
		private String PAPR_PATH; // 답안지 경로
		private int RUN_TM; // 학습시간
		private int ACT_AVG; // 평균데시벨
		private String STDY_PGSS_STAT; // 학습진행단계
		private String STDY_PGSS_STAT_NM; // 학습진행단계명
		private int STRT_NUM; // 시작문항
		private int COUNT; // 문항수
		private int ACT_AVG_DEC; // 평균데시벨
		private Date STRT_TM;
		private Date END_TM;
		private String BIGVOC_YN; // 큰소리 체크 여부
		private boolean isSendNrml; // 적정시간 지났을 때 서버에 전송했는지 여부(안드로이드 내부에서만 사용)  
		private String RESTDY_YN; // 점검신청, 답안지요청 등의 상태일때 학습인지 재학습인지 여부를 소켓으로 전달하기위해 필요
		private String TRANS_YN; // 해석보기
		
		ArrayList<VoQuest> QUST = new ArrayList<VoQuest>();

		@Override
		public int getSEQ() {
			return STDY_NUM;
		}

		public String getBIGVOC_YN() {
			return BIGVOC_YN;
		}

		public void setBIGVOC_YN(String bIGVOC_YN) {
			BIGVOC_YN = bIGVOC_YN;
		}

		public String getSTDY_PGSS_STAT_NM() {
			return STDY_PGSS_STAT_NM;
		}

		public void setSTDY_PGSS_STAT_NM(String sTDY_PGSS_STAT_NM) {
			STDY_PGSS_STAT_NM = sTDY_PGSS_STAT_NM;
		}

		public int getCOUNT() {
			return COUNT;
		}

		public void setCOUNT(int cOUNT) {
			COUNT = cOUNT;
		}

		public int getACT_AVG_DEC() {
			return ACT_AVG_DEC;
		}

		public void setACT_AVG_DEC(int aCT_AVG_DEC) {
			ACT_AVG_DEC = aCT_AVG_DEC;
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

		public void setRUN_TM(int rUN_TM) {
			RUN_TM = rUN_TM;
		}

		public int getQustSize() {
			return QUST.size();
		}

		public String getPCODE() {
			return PCODE;
		}

		public void setPCODE(String pCODE) {
			PCODE = pCODE;
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

		public int getNRML_TM() {
			return NRML_TM;
		}

		public void setNRML_TM(int nRML_TM) {
			NRML_TM = nRML_TM;
		}

		public int getQUST_CNT() {
			return QUST_CNT;
		}

		public void setQUST_CNT(int qUST_CNT) {
			QUST_CNT = qUST_CNT;
		}

		public String getSTDY_GUDE() {
			return STDY_GUDE;
		}

		public void setSTDY_GUDE(String sTDY_GUDE) {
			STDY_GUDE = sTDY_GUDE;
		}

		public int getSTDY_NUM() {
			return STDY_NUM;
		}

		public void setSTDY_NUM(int sTDY_NUM) {
			STDY_NUM = sTDY_NUM;
		}

		public String getTCHR_CHCK_YN() {
			return TCHR_CHCK_YN;
		}

		public void setTCHR_CHCK_YN(String tCHR_CHCK_YN) {
			TCHR_CHCK_YN = tCHR_CHCK_YN;
		}

		public String getBACK_YN() {
			return BACK_YN;
		}

		public void setBACK_YN(String bACK_YN) {
			BACK_YN = bACK_YN;
		}

		public ArrayList<VoQuest> getQUST() {
//			Collections.sort(QUST, new SortIncrease());
			return QUST;
		}

		public void setQUST(ArrayList<VoQuest> qUST) {
			QUST = qUST;
		}

		public String getSTGE_BACK_YN() {
			return STGE_BACK_YN;
		}

		public void setSTGE_BACK_YN(String sTGE_BACK_YN) {
			STGE_BACK_YN = sTGE_BACK_YN;
		}

		public int getRUN_TM() {
			return RUN_TM;
		}

		public void setRUN_TM(long rUN_TM) {
			RUN_TM = (int) rUN_TM;
		}

		public String getPAPR_YN() {
			return PAPR_YN;
		}

		public void setPAPR_YN(String pAPR_YN) {
			PAPR_YN = pAPR_YN;
		}

		public String getPAPR_PATH() {
			return PAPR_PATH;
		}

		public void setPAPR_PATH(String pAPR_PATH) {
			PAPR_PATH = pAPR_PATH;
		}

		public int getACT_AVG() {
			return ACT_AVG;
		}

		public void setACT_AVG(int aCT_AVG) {
			ACT_AVG = aCT_AVG;
		}

		public String getSTDY_PGSS_STAT() {
			return STDY_PGSS_STAT;
		}

		public void setSTDY_PGSS_STAT(String sTDY_PGSS_STAT) {
			STDY_PGSS_STAT = sTDY_PGSS_STAT;
		}

		public int getSTRT_NUM() {
			return STRT_NUM;
		}

		public void setSTRT_NUM(int sTRT_NUM) {
			STRT_NUM = sTRT_NUM;
		}

		public boolean getIsSendNrml() {
			return isSendNrml;
		}

		public void setIsSendNrml(boolean isSendNrml) {
			this.isSendNrml = isSendNrml;
		}

		public String getCON_SEQ() {
			return CON_SEQ;
		}

		public void setCON_SEQ(String cON_SEQ) {
			CON_SEQ = cON_SEQ;
		}
		
		public String getRESTDY_YN() {
			return RESTDY_YN;
		}

		public void setRESTDY_YN(String rESTDY_YN) {
			RESTDY_YN = rESTDY_YN;
		}
		
		public String getTRANS_YN() {
			return TRANS_YN;
		}

		public void setTRANS_YN(String tRANS_YN) {
			TRANS_YN = tRANS_YN;
		}

		public VoQuest getQuest(int num) {
			int size = QUST.size();
			for (int i = 0; i < size; i++) {
				VoQuest item = QUST.get(i);
				if (num == item.getNUM()) {
					return item;
				}
			}
			return null;
		}
	}

	public static class VoQuest implements TempIndexInterface, Cloneable {

		private String PCODE;
		private String CON_SEQ;
		private int QUST_SEQ; // 문항
		private String CHACI; // 차시
		private String STDY_STGE; // 학습단계
		private String WKBK_STDY_STGE; // 워크북학습단계
		private int NUM; // 문항번호
		private String TMPT_CD; // 템플릿 코드
		private String QURY; // 질문

		private String QURY_CONT; // 질문 내용
		private String QURY_BOGI; // 질문 보기
		private String IMG_PATH; // 이미지 파일명
		private String MOV_PATH; // 동영상 파일명
		private String QURY_VOC_PATH; // 질문음원 파일명
		private String ANSW; // 정답
		private String GRAD_YN; // 채점여부
		private String CHLD_QUST_YN; // 자식문항여부
		private int PRNT_QUST_SEQ; // 부모문항
		private String WORD_STNC_GUBN; // 단어문장구분
		private String EXIST_CHLD_YN; // 자식문항존재여부
		private int CHLD_NUM; // 자식 문항 순번
		private String LEVL; // 중요도
		private String QURY_DISP; // 질문표현
		private int MIN_CHAR_CNT; // 최소 글자수
		private String MEM_ANSW; // 사용자 정답
		private String ANSW_RSLT; // 정답결과
		private int RETY_CNT; // 재시도 횟수
		private String COMP_INOUT_GUBN; // 학습관내외구분
		/**
		 * 정답구분 : null(풀기전),  S(학습완료), W(재학습완료)
		 */
		private String ANSW_GUBN;
		private int ACT_SPE_CNT; // 큰소리 학습 건수
		private int ACT_SPE_PASS_CNT; // 큰소리학습성공건수
		private int SCOR; // 평균점수
		private String STDY_GUBN; // 학습구분
		private int STDY_GUBN_SEQ; // 학습구분 SEQ

		private String AN_YN;
		private String PAPR_PATH;
		private String REC_YN;
		private String REC_URL;
		private int ACT_AVG_DEC; // 평균데시벨
		private int RUN_TM; // 총학습시간(큰소리학습)
		private String BOGI_VOC_PATH;

		private String COMPCODE;
		private String MEMNO;
		private String STDY_DT;
		private String STDY_MM;
		private String STDY_YYYY;
		
		private String STDY_TYPE;
		private String ERRORGUBUN;
		private String SYSGB;
		
		private String MULTI_ANSW_YN;
		
		private ArrayList<VoBogi> BOGI_LIST = new ArrayList<VoBogi>();


		public String getREC_URL() {
			return REC_URL;
		}

		public void setREC_URL(String rEC_URL) {
			REC_URL = rEC_URL;
		}

		public int getACT_SPE_CNT() {
			return ACT_SPE_CNT;
		}

		public void setACT_SPE_CNT(int aCT_SPE_CNT) {
			ACT_SPE_CNT = aCT_SPE_CNT;
		}

		public int getACT_SPE_PASS_CNT() {
			return ACT_SPE_PASS_CNT;
		}

		public void setACT_SPE_PASS_CNT(int aCT_SPE_PASS_CNT) {
			ACT_SPE_PASS_CNT = aCT_SPE_PASS_CNT;
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

		public String getCHACI() {
			return CHACI;
		}

		public void setCHACI(String cHACI) {
			CHACI = cHACI;
		}

		public String getWORD_STNC_GUBN() {
			return WORD_STNC_GUBN;
		}

		public void setWORD_STNC_GUBN(String wORD_STNC_GUBN) {
			WORD_STNC_GUBN = wORD_STNC_GUBN;
		}

		public int getSCOR() {
			return SCOR;
		}

		public void setSCOR(int sCOR) {
			SCOR = sCOR;
		}

		public void setRUN_TM(int rUN_TM) {
			RUN_TM = rUN_TM;
		}

		public void setSEQ(int nUM) {
			NUM = nUM;
		}

		@Override
		public int getSEQ() {
			return NUM;
		}

		public String getPCODE() {
			return PCODE;
		}

		public void setPCODE(String pCODE) {
			PCODE = pCODE;
		}

		public int getQUST_SEQ() {
			return QUST_SEQ;
		}

		public void setQUST_SEQ(int qUST_SEQ) {
			QUST_SEQ = qUST_SEQ;
		}

		public String getSTDY_STGE() {
			return STDY_STGE;
		}

		public void setSTDY_STGE(String sTDY_STGE) {
			STDY_STGE = sTDY_STGE;
		}

		public String getWKBK_STDY_STGE() {
			return WKBK_STDY_STGE;
		}

		public void setWKBK_STDY_STGE(String wKBK_STDY_STGE) {
			WKBK_STDY_STGE = wKBK_STDY_STGE;
		}

		public int getNUM() {
			return NUM;
		}

		public void setNUM(int nUM) {
			NUM = nUM;
		}

		public String getTMPT_CD() {
			return TMPT_CD;
		}

		public void setTMPT_CD(String tMPT_CD) {
			TMPT_CD = tMPT_CD;
		}

		public String getQURY() {
			return QURY;
		}

		public void setQURY(String qURY) {
			QURY = qURY;
		}

		public String getQURY_CONT() {
			return QURY_CONT;
		}

		public void setQURY_CONT(String qURY_CONT) {
			QURY_CONT = qURY_CONT;
		}

		public String getQURY_BOGI() {
			return QURY_BOGI;
		}

		public void setQURY_BOGI(String qURY_BOGI) {
			QURY_BOGI = qURY_BOGI;
		}

		public String getIMG_PATH() {
//			return IMG_PATH.toUpperCase();
			return IMG_PATH;
		}

		public void setIMG_PATH(String iMG_PATH) {
			IMG_PATH = iMG_PATH;
		}

		public String getMOV_PATH() {
			return MOV_PATH;
		}

		public void setMOV_PATH(String mOV_PATH) {
			MOV_PATH = mOV_PATH;
		}

		public String getQURY_VOC_PATH() {
			return QURY_VOC_PATH;
		}

		public void setQURY_VOC_PATH(String qURY_VOC_PATH) {
			QURY_VOC_PATH = qURY_VOC_PATH;
		}

		public String getBOGI_VOC_PATH() {
			return BOGI_VOC_PATH;
		}

		public void setBOGI_VOC_PATH(String bOGI_VOC_PATH) {
			BOGI_VOC_PATH = bOGI_VOC_PATH;
		}

		public String getANSW() {
			return ANSW;
		}

		public void setANSW(String aNSW) {
			ANSW = aNSW;
		}

		public String getGRAD_YN() {
			return GRAD_YN;
		}

		public void setGRAD_YN(String gRAD_YN) {
			GRAD_YN = gRAD_YN;
		}

		public String getAN_YN() {
			return AN_YN;
		}

		public void setAN_YN(String aN_YN) {
			AN_YN = aN_YN;
		}

		public String getCHLD_QUST_YN() {
			return CHLD_QUST_YN;
		}

		public void setCHLD_QUST_YN(String cHLD_QUST_YN) {
			CHLD_QUST_YN = cHLD_QUST_YN;
		}

		public int getPRNT_QUST_SEQ() {
			return PRNT_QUST_SEQ;
		}

		public void setPRNT_QUST_SEQ(int pRNT_QUST_SEQ) {
			PRNT_QUST_SEQ = pRNT_QUST_SEQ;
		}

		public String getPAPR_PATH() {
			return PAPR_PATH;
		}

		public void setPAPR_PATH(String pAPR_PATH) {
			PAPR_PATH = pAPR_PATH;
		}

		public String getEXIST_CHLD_YN() {
			return EXIST_CHLD_YN;
		}

		public void setEXIST_CHLD_YN(String eXIST_CHLD_YN) {
			EXIST_CHLD_YN = eXIST_CHLD_YN;
		}

		public int getCHLD_NUM() {
			return CHLD_NUM;
		}

		public void setCHLD_NUM(int cHLD_NUM) {
			CHLD_NUM = cHLD_NUM;
		}

		public String getLEVL() {
			return LEVL;
		}

		public void setLEVL(String lEVL) {
			LEVL = lEVL;
		}

		public String getQURY_DISP() {
			return QURY_DISP;
		}

		public void setQURY_DISP(String qURY_DISP) {
			QURY_DISP = qURY_DISP;
		}

		public int getMIN_CHAR_CNT() {
			return MIN_CHAR_CNT;
		}

		public void setMIN_CHAR_CN(int mIN_CHAR_CNT) {
			MIN_CHAR_CNT = mIN_CHAR_CNT;
		}

		public ArrayList<VoBogi> getBOGI_LIST() {
			return BOGI_LIST;
		}

		public void setBOGI_LIST(ArrayList<VoBogi> bOGI_LIST) {
			BOGI_LIST = bOGI_LIST;
		}

		public String getMEM_ANSW() {
			return MEM_ANSW;
		}

		public void setMEM_ANSW(String mEM_ANSW) {
			MEM_ANSW = mEM_ANSW;
		}

		public String getANSW_RSLT() {
			return ANSW_RSLT;
		}

		public void setANSW_RSLT(String aNSW_RSLT) {
			ANSW_RSLT = aNSW_RSLT;
		}

		public int getRETY_CNT() {
			return RETY_CNT;
		}

		public void setRETY_CNT(int rETY_CNT) {
			RETY_CNT = rETY_CNT;
		}

		public String getCOMP_INOUT_GUBN() {
			return COMP_INOUT_GUBN;
		}

		public void setCOMP_INOUT_GUBN(String cOMP_INOUT_GUBN) {
			COMP_INOUT_GUBN = cOMP_INOUT_GUBN;
		}

		public String getANSW_GUBN() {
			return ANSW_GUBN;
		}

		public void setANSW_GUBN(String aNSW_GUBN) {
			ANSW_GUBN = aNSW_GUBN;
		}

		public String getREC_YN() {
			return REC_YN;
		}

		public void setREC_YN(String rEC_YN) {
			REC_YN = rEC_YN;
		}

		public int getACT_AVG_DEC() {
			return ACT_AVG_DEC;
		}

		public void setACT_AVG_DEC(int aCT_AVG_DEC) {
			ACT_AVG_DEC = aCT_AVG_DEC;
		}

		public void setMIN_CHAR_CNT(int mIN_CHAR_CNT) {
			MIN_CHAR_CNT = mIN_CHAR_CNT;
		}

		public int getRUN_TM() {
			return RUN_TM;
		}

		public void setRUN_TM(long l) {
			RUN_TM = (int) l;
		}

		public String getCON_SEQ() {
			return CON_SEQ;
		}

		public void setCON_SEQ(String cON_SEQ) {
			CON_SEQ = cON_SEQ;
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

		public String getSTDY_DT() {
			return STDY_DT;
		}

		public void setSTDY_DT(String sTDY_DT) {
			STDY_DT = sTDY_DT;
		}

		public String getSTDY_MM() {
			return STDY_MM;
		}

		public void setSTDY_MM(String sTDY_MM) {
			STDY_MM = sTDY_MM;
		}

		public String getSTDY_YYYY() {
			return STDY_YYYY;
		}

		public void setSTDY_YYYY(String sTDY_YYYY) {
			STDY_YYYY = sTDY_YYYY;
		}

		public String getSTDY_TYPE() {
			return STDY_TYPE;
		}

		public void setSTDY_TYPE(String sTDY_TYPE) {
			STDY_TYPE = sTDY_TYPE;
		}

		public String getERRORGUBUN() {
			return ERRORGUBUN;
		}

		public void setERRORGUBUN(String eRRORGUBUN) {
			ERRORGUBUN = eRRORGUBUN;
		}

		public String getSYSGB() {
			return SYSGB;
		}

		public void setSYSGB(String sYSGB) {
			SYSGB = sYSGB;
		}
		
		public String getMULTI_ANSW_YN() {
			return MULTI_ANSW_YN;
		}

		public void setMULTI_ANSW_YN(String mULTI_ANSW_YN) {
			MULTI_ANSW_YN = mULTI_ANSW_YN;
		}

		@Override
		public Object clone() {
			// 내 객체 생성
			Object objReturn;

			try {
				objReturn = super.clone();
				return objReturn;
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static class VoBogi implements Cloneable, Parcelable {
		private String PCODE;
		private int QUST_SEQ;
		private int BOGI_SEQ;
		private int NUM;
		private String BOGI;
		private String VOC_PATH;
		private String IMG_PATH;
		private String MOV_PATH;

		public String getPCODE() {
			return PCODE;
		}

		public void setPCODE(String pCODE) {
			PCODE = pCODE;
		}

		public int getQUST_SEQ() {
			return QUST_SEQ;
		}

		public void setQUST_SEQ(int qUST_SEQ) {
			QUST_SEQ = qUST_SEQ;
		}

		public int getBOGI_SEQ() {
			return BOGI_SEQ;
		}

		public void setBOGI_SEQ(int bOGI_SEQ) {
			BOGI_SEQ = bOGI_SEQ;
		}

		public int getNUM() {
			return NUM;
		}

		public void setNUM(int nUM) {
			NUM = nUM;
		}

		public String getBOGI() {
			return BOGI;
		}

		public void setBOGI(String bOGI) {
			BOGI = bOGI;
		}

		public String getVOC_PATH() {
			return VOC_PATH;
		}

		public void setVOC_PATH(String vOC_PATH) {
			VOC_PATH = vOC_PATH;
		}

		public String getIMG_PATH() {
//			return IMG_PATH.toUpperCase();
			return IMG_PATH;
		}

		public void setIMG_PATH(String iMG_PATH) {
			IMG_PATH = iMG_PATH;
		}

		public String getMOV_PATH() {
			return MOV_PATH;
		}

		public void setMOV_PATH(String mOV_PATH) {
			MOV_PATH = mOV_PATH;
		}

		@Override
		public Object clone() {
			// 내 객체 생성
			Object objReturn;

			try {
				objReturn = super.clone();
				return objReturn;
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}

		public VoBogi() {
			
		}
		
		public VoBogi(String PCODE, int QUST_SEQ, int BOGI_SEQ, int NUM, String BOGI, String VOC_PATH, String IMG_PATH, String MOV_PATH) {
			super();
			this.PCODE = PCODE;
			this.QUST_SEQ = QUST_SEQ;
			this.BOGI_SEQ = BOGI_SEQ;
			this.NUM = NUM;
			this.BOGI = BOGI;
			this.VOC_PATH = VOC_PATH;
			this.IMG_PATH = IMG_PATH;
			this.MOV_PATH = MOV_PATH;
		}
		
		public VoBogi(Parcel in) {
			readFromParcel(in);
		}
		
		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeString(PCODE);
			dest.writeInt(QUST_SEQ);
			dest.writeInt(BOGI_SEQ);
			dest.writeInt(NUM);
			dest.writeString(BOGI);
			dest.writeString(VOC_PATH);
			dest.writeString(IMG_PATH);
			dest.writeString(MOV_PATH);
			
		}
		
		public void readFromParcel(Parcel in) {
			PCODE = in.readString();
			QUST_SEQ = in.readInt();
			BOGI_SEQ = in.readInt();
			NUM = in.readInt();
			BOGI = in.readString();
			VOC_PATH = in.readString();
			IMG_PATH = in.readString();
			MOV_PATH = in.readString();
		}
		
		@SuppressWarnings("rawtypes")
	    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	 
	        @Override
	        public VoBogi createFromParcel(Parcel in) {
	            return new VoBogi(in);
	        }
	 
	        @Override
	        public VoBogi[] newArray(int size) {
	            // TODO Auto-generated method stub
	            return new VoBogi[size];
	        }
	 
	    };

	}
	
	public class VoTemplateAnim  {
		private String PCODE;
		private String TMPT_A;
		private String TMPT_B;
		public String getPCODE() {
			return PCODE;
		}
		public void setPCODE(String pCODE) {
			PCODE = pCODE;
		}
		public String getTMPT_A() {
			return TMPT_A;
		}
		public void setTMPT_A(String tMPT_A) {
			TMPT_A = tMPT_A;
		}
		public String getTMPT_B() {
			return TMPT_B;
		}
		public void setTMPT_B(String tMPT_B) {
			TMPT_B = tMPT_B;
		}
		
	}
}
