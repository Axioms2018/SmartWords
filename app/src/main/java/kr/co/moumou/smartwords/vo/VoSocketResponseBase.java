package kr.co.moumou.smartwords.vo;

public class VoSocketResponseBase extends VoResponseBase{

	private String STDY_PGSS_STAT_NM;
	private String STDY_PGSS_STAT;
	private String RESTDY_NUM;
	private String TEACHERID;
	private String TEACHERNAME;
	
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
	public String getSTDY_PGSS_STAT_NM() {
		return STDY_PGSS_STAT_NM;
	}
	public void setSTDY_PGSS_STAT_NM(String sTDY_PGSS_STAT_NM) {
		STDY_PGSS_STAT_NM = sTDY_PGSS_STAT_NM;
	}
	public String getSTDY_PGSS_STAT() {
		return STDY_PGSS_STAT;
	}
	public void setSTDY_PGSS_STAT(String sTDY_PGSS_STAT) {
		STDY_PGSS_STAT = sTDY_PGSS_STAT;
	}
	public String getRESTDY_NUM() {
		return RESTDY_NUM;
	}
	public void setRESTDY_NUM(String rESTDY_NUM) {
		RESTDY_NUM = rESTDY_NUM;
	}
	
	
}
