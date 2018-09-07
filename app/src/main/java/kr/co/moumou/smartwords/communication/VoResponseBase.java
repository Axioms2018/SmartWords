package kr.co.moumou.smartwords.communication;

public class VoResponseBase {
	private int RES_CODE = 1;
	private String RES_MSG;
	private final int IS_DOUBLE_LOGIN = 0;
	private final int IS_SUCCESS = 1;

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

	public boolean isSuccess() {
		return (RES_CODE == IS_SUCCESS);
	}

	public boolean isDoubleLogin() {
		return (RES_CODE == IS_DOUBLE_LOGIN);
	}
}
