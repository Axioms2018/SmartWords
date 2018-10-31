package kr.co.moumou.smartwords.vo;

public class VoResponseBase {
	private String COMMAND;
	private int RSLT_COMMAND = -1;
	private final int IS_NOT_FOUND_TEACHER = 100;



	private String SESSIONID;
	private String ERROR_MSG;

	private final int IS_DOUBLE_LOGIN = 001;
	private final int IS_SUCCESS = 777;
	private String RES_MSG;
	private int RES_CODE;



	public int getRES_CODE() {
		return RES_CODE;
	}

	public void setRES_CODE(int rES_CODE) {
		RES_CODE = rES_CODE;
	}

	public String getRES_MSG() {
		return RES_MSG;
	}

	public void setRES_MSG(String rES_MSG) {
		RES_MSG = rES_MSG;
	}

	public String getSESSIONID() {
		return SESSIONID;
	}

	public void setSESSIONID(String sESSIONID) {
		SESSIONID = sESSIONID;
	}

	public String getCOMMAND() {
		return COMMAND;
	}

	public void setCOMMAND(String cOMMAND) {
		COMMAND = cOMMAND;
	}

	public int getRSLT_COMMAND() {
		return RSLT_COMMAND;
	}

	public void setRSLT_COMMAND(int rSLT_COMMAND) {
		RSLT_COMMAND = rSLT_COMMAND;
	}

	public String getERROR_MSG() {
		return ERROR_MSG;
	}

	public void setERROR_MSG(String eRROR_MSG) {
		ERROR_MSG = eRROR_MSG;
	}

	public int getIS_DOUBLE_LOGIN() {
		return IS_DOUBLE_LOGIN;
	}

	public int getIS_SUCCESS() {
		return IS_SUCCESS;
	}

	public boolean isSuccess() {
		return (RSLT_COMMAND == IS_SUCCESS);
	}

	public boolean isDoubleLogin() {
		return (RSLT_COMMAND == IS_DOUBLE_LOGIN);
	}

	public boolean isNotFoundTeacher(){
		return (RSLT_COMMAND == IS_NOT_FOUND_TEACHER);
	}
}