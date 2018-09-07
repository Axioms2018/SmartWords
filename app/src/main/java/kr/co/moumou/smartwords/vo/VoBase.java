package kr.co.moumou.smartwords.vo;


/**
 * Created by 김민정 on 2018-03-28.
 */

public class VoBase {
//    public static final int RES_STOP_SERVICE = -1;     //서비스 제한
    public static final int RES_SERVER_ERROR = -2;     //서버 장애
    public static final int RES_SYSTEM = 0;            //시스템 점검
    public int RSLT_COMMAND = -1;

    private String SESSIONID;
    private String ERROR_MSG;

    public static final int RES_SUCCESS = 777;           //성공
    public static final int IS_CHECK_LOGIN = 001;       //강제 로그아웃 >> 3개 이상의 기계
    public String RES_MSG;
    public int RES_CODE;
    private String COMMAND;


    public int getRES_CODE() {
        return RES_CODE;
    }

    public void setRES_CODE(int RES_CODE) {
        this.RES_CODE = RES_CODE;
    }

    public String getRES_MSG() {
        return RES_MSG;
    }

    public void setRES_MSG(String RES_MSG) {
        this.RES_MSG = RES_MSG;
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

    public String getERROR_MSG() {
        return ERROR_MSG;
    }

    public void setERROR_MSG(String eRROR_MSG) {
        ERROR_MSG = eRROR_MSG;
    }


    public int getIS_CHECK_LOGIN() {return IS_CHECK_LOGIN;}

    public int getRES_SUCCESS() {
        return RES_SUCCESS;
    }


    public int getRSLT_COMMAND() { return RSLT_COMMAND; }

    public void setRSLT_COMMAND(int RSLT_COMMAND) { this.RSLT_COMMAND = RSLT_COMMAND; }

    public boolean isSuccess() {
        if(RSLT_COMMAND == RES_SUCCESS) return true;
        return false;
    }

    public boolean isSystemCheck() {
        if(RSLT_COMMAND == RES_SYSTEM) return true;
        return false;
    }


    public boolean loginError() {
        if(RSLT_COMMAND == IS_CHECK_LOGIN) return true;
        return false;
    }

    public boolean isStopService() {
//        if(RES_CODE == RES_STOP_SERVICE) return true;
        return false;
    }

    public boolean isServerError() {
        if(RSLT_COMMAND == RES_SERVER_ERROR) return true;
        return false;
    }
}
