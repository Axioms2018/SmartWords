package kr.co.moumou.smartwords.vo;

import java.util.HashMap;

import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.util.LogUtil;

public class VoSocketComm {
	
	private volatile static VoSocketComm instance;

	public static VoSocketComm getInstance() {
		if (instance == null) {
			synchronized (VoSocketComm.class) {
				if (instance == null) {
					instance = new VoSocketComm();
				}
			}
		}
		return instance;
	}

	protected VoSocketComm() {}

	
	private HashMap<String, Object> params = new HashMap<String, Object>();
	
	public void init(){
		params.clear();
		params.put(ConstantsCommParameter.Keys.COMMAND, null);
		params.put(ConstantsCommParameter.Keys.SESSIONID, null);
		params.put(ConstantsCommParameter.Keys.USERID, null);
		params.put(ConstantsCommParameter.Keys.PWD, null);
		params.put(ConstantsCommParameter.Keys.USERGB, null);
		params.put(ConstantsCommParameter.Keys.USERNAME, null);
		params.put(ConstantsCommParameter.Keys.IMG_PATH, null);
		params.put(ConstantsCommParameter.Keys.COMPCODE, null);
		params.put(ConstantsCommParameter.Keys.TEACHERID, null);
		params.put(ConstantsCommParameter.Keys.TEACHERNAME, null);
		params.put(ConstantsCommParameter.Keys.SEX, null);
		
		params.put(ConstantsCommParameter.Keys.EXE_NM, "스마트무무");
		params.put(ConstantsCommParameter.Keys.MOU_EXE_YN, "Y");
		params.put(ConstantsCommParameter.Keys.CLIENT_TYPE, "TBL");
		params.put(ConstantsCommParameter.Keys.DATA_DEL_YN, "N");
		clearStudyData();
	}
	
	public void clearStudyData(){
		LogUtil.e("CLEAR STUDY DATA ");
		params.put(ConstantsCommParameter.Keys.PCODE, null);
		params.put(ConstantsCommParameter.Keys.CON_SEQ, null);
		params.put(ConstantsCommParameter.Keys.PNAME, null);
		params.put(ConstantsCommParameter.Keys.PSNAME, null);
		
		params.put(ConstantsCommParameter.Keys.CHACI, null);
		params.put(ConstantsCommParameter.Keys.STDY_TYPE, null);
		params.put(ConstantsCommParameter.Keys.STDY_GUBN, null);
		params.put(ConstantsCommParameter.Keys.STDY_GUBN_SEQ, 1);
		params.put(ConstantsCommParameter.Keys.COMP_INOUT_GUBN, null);
		params.put(ConstantsCommParameter.Keys.STDY_STGE, null);
		params.put(ConstantsCommParameter.Keys.STDY_STGE_NM, null);
		params.put(ConstantsCommParameter.Keys.STDY_PGSS_STAT, null);
		params.put(ConstantsCommParameter.Keys.STDY_PGSS_STAT_NM, null);
		params.put(ConstantsCommParameter.Keys.RESTDY_YN, "N");
		params.put(ConstantsCommParameter.Keys.STRT_TM, null);
		params.put(ConstantsCommParameter.Keys.END_TM, null);
		params.put(ConstantsCommParameter.Keys.RUN_TM, 0);
		params.put(ConstantsCommParameter.Keys.RESTDY_NUM, null);
		params.put(ConstantsCommParameter.Keys.CLOS_YN, "N");
		params.put(ConstantsCommParameter.Keys.CNFM_YN, "N");
		params.put(ConstantsCommParameter.Keys.STDY_PGSS_OVER_YN, "N");
		params.put(ConstantsCommParameter.Keys.REC_YN, "N");
		params.put(ConstantsCommParameter.Keys.REC_URL, null);
		params.put(ConstantsCommParameter.Keys.REC_FILES, null);
		
	}
	
	public void clearAll(){
		params.clear();
	}
	
	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> map) {
		params.putAll(map);
	}
	
	public void setParams(String key, Object value) {
		//Thread가 바로 중지 되지 않아 clear 이후 Run_tm이 몇번 호출 됨.
		//Clear 된 Pcode 활용.
		if(ConstantsCommParameter.Keys.RUN_TM.equals(key)){
			if(params.get(ConstantsCommParameter.Keys.PCODE) == null){
				return;
			}
		}
		params.put(key, value);
	}
	
}
