package kr.co.moumou.smartwords.listener;

public interface CommunicationEventListener {

	void onResponse(String msg);
	void onResponseFail(String msg);
	void exception(Exception e);
	void networkNotAvailable(String msg);
}
