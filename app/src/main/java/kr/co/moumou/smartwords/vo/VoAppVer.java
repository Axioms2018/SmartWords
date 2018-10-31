package kr.co.moumou.smartwords.vo;

/**
 * Created by 김민정 on 2018-06-04.
 */

public class VoAppVer extends VoBase {

    private volatile static VoAppVer instance;

    private int APPID;
    private int APPVER;
    private String VERNAME;
    private String VERINFO;
    private String APPURL;
    private String FILENAME;
    private String OPEN_YN;
    private String CHOICE_YN;
    private String FFPLAYER;

    public VoAppVer() {
        this.instance = this;
    }

    public static VoAppVer getInstance() {
        if (instance == null) {
            synchronized (VoAppVer.class) {
                instance = new VoAppVer();
            }
        }
        return instance;
    }

    public int getAPPID() {
        return APPID;
    }

    public void setAPPID(int APPID) {
        this.APPID = APPID;
    }

    public int getAPPVER() {
        return APPVER;
    }

    public void setAPPVER(int APPVER) {
        this.APPVER = APPVER;
    }

    public String getVERNAME() {
        return VERNAME;
    }

    public void setVERNAME(String VERNAME) {
        this.VERNAME = VERNAME;
    }

    public String getVERINFO() {
        return VERINFO;
    }

    public void setVERINFO(String VERINFO) {
        this.VERINFO = VERINFO;
    }

    public String getAPPURL() {
        return APPURL;
    }

    public void setAPPURL(String APPURL) {
        this.APPURL = APPURL;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getOPEN_YN() {
        return OPEN_YN;
    }

    public void setOPEN_YN(String OPEN_YN) {
        this.OPEN_YN = OPEN_YN;
    }

    public String getCHOICE_YN() {
        return CHOICE_YN;
    }

    public void setCHOICE_YN(String CHOICE_YN) {
        this.CHOICE_YN = CHOICE_YN;
    }

    public String getFFPLAYER() {
        return FFPLAYER;
    }

    public void setFFPLAYER(String FFPLAYER) {
        this.FFPLAYER = FFPLAYER;
    }
}
