package kr.co.moumou.smartwords.vo;

/**
 * Created by 김민정 on 2018-04-04.
 */

public class VoDownload {
    private String TAG;
    private String URL;
    private String PCODE;
    private String LESSON;
    private String TYPE;
    private String FILENAME;

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPCODE() {
        return PCODE;
    }

    public void setPCODE(String PCODE) {
        this.PCODE = PCODE;
    }

    public String getLESSON() {
        return LESSON;
    }

    public void setLESSON(String LESSON) {
        this.LESSON = LESSON;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }
}
