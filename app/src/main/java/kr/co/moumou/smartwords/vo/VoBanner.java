package kr.co.moumou.smartwords.vo;

import java.util.ArrayList;

public class VoBanner extends VoResponseBase{

	ArrayList<BannerInfo> RES_LIST = new ArrayList<BannerInfo>();

	public ArrayList<BannerInfo> getRES_LIST() {
		return RES_LIST;
	}

	public void setRES_LIST(ArrayList<BannerInfo> RES_LIST) {
		RES_LIST = RES_LIST;
	}

	public class BannerInfo{
		private String TITLE;
		private String IMG_URL;
		private String LINK_URL;
		public String getTITLE() {
			return TITLE;
		}
		public void setTITLE(String tITLE) {
			TITLE = tITLE;
		}
		public String getIMG_URL() {
			return IMG_URL;
		}
		public void setIMG_URL(String iMG_URL) {
			IMG_URL = iMG_URL;
		}
		public String getLINK_URL() {
			return LINK_URL;
		}
		public void setLINK_URL(String lINK_URL) {
			LINK_URL = lINK_URL;
		}
		
		
	}
}
