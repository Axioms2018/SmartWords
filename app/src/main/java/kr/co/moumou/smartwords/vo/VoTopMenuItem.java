package kr.co.moumou.smartwords.vo;

public class VoTopMenuItem {

	private String title;
	private Class launchActivity;
	
	
	public VoTopMenuItem(String title, Class launchActivity) {
		super();
		this.title = title;
		this.launchActivity = launchActivity;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Class getLaunchActivity() {
		return launchActivity;
	}
	public void setLaunchActivity(Class launchActivity) {
		this.launchActivity = launchActivity;
	}

	
}
