package kr.co.moumou.smartwords.vo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SplitInfo {
	int size;
	String[] arrString;
	boolean isManyCorrect;
	
	public int size() {
		return size;
	}
	public void setSize(int size) {
		if (size > 1)
			isManyCorrect = true;
		
		this.size = size;
	}
	public String[] getStrings() {
		return arrString;
	}
	public List<String> getList() {
//		return Arrays.asList(arrString);
//		List<String> list = new LinkedList(Arrays.asList(arrString));
		
		return new LinkedList(Arrays.asList(arrString));
	}
	public void setStrings(String[] arrString) {
		setSize(arrString.length);
		
		this.arrString = arrString;
	}
	
	public String get(int index) {
		if(arrString == null){
			return null;
		}
		if(arrString.length <= index){
			return null;
		}
		return arrString[index];
	}
	
	public boolean isManyCorrect() {
		return isManyCorrect;
	}
	
	public String getString() {
		String retStr="";
		for (String str : arrString) {
			retStr += str;
		}
		return retStr;
	}
}
