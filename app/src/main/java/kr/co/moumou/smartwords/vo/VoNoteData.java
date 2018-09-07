package kr.co.moumou.smartwords.vo;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class VoNoteData implements Serializable {
	private String STD_LEVEL;
	private ArrayList<VoNoteDetail> USER_NOTE = new ArrayList<VoNoteDetail>();
	
	public String getSTD_LEVEL() {
		return STD_LEVEL;
	}
	
	public void setSTD_LEVEL(String sTD_LEVEL) {
		STD_LEVEL = sTD_LEVEL;
	}
	
	public ArrayList<VoNoteDetail> getUSER_NOTE() {
		return USER_NOTE;
	}
	
	public void setUSER_NOTE(ArrayList<VoNoteDetail> uSER_NOTE) {
		USER_NOTE = uSER_NOTE;
	}	
}
