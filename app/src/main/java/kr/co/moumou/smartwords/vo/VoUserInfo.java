package kr.co.moumou.smartwords.vo;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by 김민정 on 2018-05-04.
 */

@Parcel
public class VoUserInfo extends VoBase {

    public volatile static VoUserInfo instance;

    public String SID;                 //세션 아이디
    public String USERID;              //아이디(이메일)
    public String USERNM;              //이름
    public String USERTYPE;            //사용자구분
    public String AUTH_GB;             //사용자권한
    public String IMGURL;              //회원사진URL
    public String POINT;               //포인트
    public String FREE_YN;             //프리여부
    public String PAY_YN;              //결제여부
    public String OID;                 //주문번호
    public String TERM_GB;             //학습가능기간적용구분
    public int DAYCNT;                 //학습가능기간
    public String ENDATE;              //학습가능최종일자
    public int POLLID;                 //신규설문알림
    public String COMPNAME;            //사업장(학교)명
    public String OPT1_YN;             //공지알림여부
    public String OPT2_YN;             //학습도착알림여부
    public String OPT2VAL;             //학습도착알림시간
    public String FUSERID;             //나눔벗아이디
    public String FUSERNM;             //나눔벗이름
    public String FIMGURL;             //나눔벗사진URL
    public String FDAYCNT;             //나눔벗무료학습일수
    public ArrayList<VoApp> APP_LIST;  //성인영어 다른앱

    public VoUserInfo() {
        this.instance = this;
    }

    public static VoUserInfo getInstance() {
        if (instance == null) {
            synchronized (VoUserInfo.class) {
                instance = new VoUserInfo();
            }
        }
        return instance;
    }

    public void clear() {
        instance = null;
        APP_LIST = null;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNM() {
        return USERNM;
    }

    public void setUSERNM(String USERNM) {
        this.USERNM = USERNM;
    }

    public String getUSERTYPE() {
        return USERTYPE;
    }

    public void setUSERTYPE(String USERTYPE) {
        this.USERTYPE = USERTYPE;
    }

    public String getAUTH_GB() {
        return AUTH_GB;
    }

    public void setAUTH_GB(String AUTH_GB) {
        this.AUTH_GB = AUTH_GB;
    }

    public String getIMGURL() {
        return IMGURL;
    }

    public void setIMGURL(String IMGURL) {
        this.IMGURL = IMGURL;
    }

    public String getPOINT() {
        return POINT;
    }

    public void setPOINT(String POINT) {
        this.POINT = POINT;
    }

    public String getPAY_YN() {
        return PAY_YN;
    }

    public String getFREE_YN() {
        return FREE_YN;
    }

    public void setFREE_YN(String FREE_YN) {
        this.FREE_YN = FREE_YN;
    }

    public void setPAY_YN(String PAY_YN) {
        this.PAY_YN = PAY_YN;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getTERM_GB() {
        return TERM_GB;
    }

    public void setTERM_GB(String TERM_GB) {
        this.TERM_GB = TERM_GB;
    }

    public int getDAYCNT() {
        return DAYCNT;
    }

    public void setDAYCNT(int DAYCNT) {
        this.DAYCNT = DAYCNT;
    }

    public String getENDATE() {
        return ENDATE;
    }

    public void setENDATE(String ENDATE) {
        this.ENDATE = ENDATE;
    }

    public int getPOLLID() {
        return POLLID;
    }

    public void setPOLLID(int POLLID) {
        this.POLLID = POLLID;
    }

    public String getCOMPNAME() {
        return COMPNAME;
    }

    public void setCOMPNAME(String COMPNAME) {
        this.COMPNAME = COMPNAME;
    }

    public String getOPT1_YN() {
        return OPT1_YN;
    }

    public void setOPT1_YN(String OPT1_YN) {
        this.OPT1_YN = OPT1_YN;
    }

    public String getOPT2_YN() {
        return OPT2_YN;
    }

    public void setOPT2_YN(String OPT2_YN) {
        this.OPT2_YN = OPT2_YN;
    }

    public String getOPT2VAL() {
        return OPT2VAL;
    }

    public void setOPT2VAL(String OPT2VAL) {
        this.OPT2VAL = OPT2VAL;
    }

    public String getFUSERID() {
        return FUSERID;
    }

    public void setFUSERID(String FUSERID) {
        this.FUSERID = FUSERID;
    }

    public String getFUSERNM() {
        return FUSERNM;
    }

    public void setFUSERNM(String FUSERNM) {
        this.FUSERNM = FUSERNM;
    }

    public String getFIMGURL() {
        return FIMGURL;
    }

    public void setFIMGURL(String FIMGURL) {
        this.FIMGURL = FIMGURL;
    }

    public String getFDAYCNT() {
        return FDAYCNT;
    }

    public void setFDAYCNT(String FDAYCNT) {
        this.FDAYCNT = FDAYCNT;
    }

    public ArrayList<VoApp> getAPP_LIST() {
        return APP_LIST;
    }

    public void setAPP_LIST(ArrayList<VoApp> APP_LIST) {
        this.APP_LIST = APP_LIST;
    }

    @Parcel
    public static class VoApp {
        public String APPNM;           //앱이름
        public String APPIMGURL;       //앱아이콘URL
        public String APPURL;          //앱링크(점프)정보
        public String PACKAGENM;       //패키지

        public String getAPPNM() {
            return APPNM;
        }

        public void setAPPNM(String APPNM) {
            this.APPNM = APPNM;
        }

        public String getAPPIMGURL() {
            return APPIMGURL;
        }

        public void setAPPIMGURL(String APPIMGURL) {
            this.APPIMGURL = APPIMGURL;
        }

        public String getAPPURL() {
            return APPURL;
        }

        public void setAPPURL(String APPURL) {
            this.APPURL = APPURL;
        }

        public String getPACKAGENM() {
            return PACKAGENM;
        }

        public void setPACKAGENM(String PACKAGENM) {
            this.PACKAGENM = PACKAGENM;
        }
    }
}
