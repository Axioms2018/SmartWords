package kr.co.moumou.smartwords.vo;

import org.parceler.Parcel;

/**
 * Created by 김민정 on 2018-05-04.
 */

@Parcel
public class VoCertificate1 extends VoBase {

    public String SID;
    public int APPID;
    public String MAC;
    public String USERID;
    public String TOKEN;
    public int APPVER;
    public String ANDROIDVER;
    public String MODEL;
    public String USERIP;
    public String RES_DEVICE_LIST;
    public String PAY_YN;

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public int getAPPID() {
        return APPID;
    }

    public void setAPPID(int APPID) {
        this.APPID = APPID;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public int getAPPVER() {
        return APPVER;
    }

    public void setAPPVER(int APPVER) {
        this.APPVER = APPVER;
    }

    public String getANDROIDVER() {
        return ANDROIDVER;
    }

    public void setANDROIDVER(String ANDROIDVER) {
        this.ANDROIDVER = ANDROIDVER;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getUSERIP() {
        return USERIP;
    }

    public void setUSERIP(String USERIP) {
        this.USERIP = USERIP;
    }

    public String getRES_DEVICE_LIST() {
        return RES_DEVICE_LIST;
    }

    public void setRES_DEVICE_LIST(String RES_DEVICE_LIST) {
        this.RES_DEVICE_LIST = RES_DEVICE_LIST;
    }

    public String getPAY_YN() {
        return PAY_YN;
    }

    public void setPAY_YN(String PAY_YN) {
        this.PAY_YN = PAY_YN;
    }
}
