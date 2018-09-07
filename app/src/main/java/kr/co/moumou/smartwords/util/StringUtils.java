package kr.co.moumou.smartwords.util;

import android.widget.EditText;
import android.widget.TextView;

public class StringUtils {

	public static String getYear(String date){
		return getParsingDate(0, 4, date);
	}
	public static String getMonth(String date){
		return getParsingDate(5, 7, date);
	}
	public static String getDay(String date){
		return getParsingDate(8, 10, date);
	}
	public static String getHour(String date){
		return getParsingDate(11, 13, date);
	}
	public static String getMinitu(String date){
		return getParsingDate(14, 16, date);
	}
	public static String getSecond(String date){
		return getParsingDate(17, 19, date);
	}
	
	private static String getParsingDate(int start, int end, String date){
		if(date == null){
			return "";
		}
		
		if(date.length() < end){
			return "";
		}

		return date.substring(start, end);
	}
	
	/**
	 * 특수문자 제거
	 * @param str
	 * @return
	 */
	public static String StringReplace(String str){
	      String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
	      str =str.replaceAll(match, " ");
	      return str;
	   }

	
	/**
	 * 확장자 제외한 파일 이름 반환 
	 * ex) 
	 * parameter : http://moumou.co.kr/2234_232_TDL_1.jgp  
	 * return : 2234_232_TDL_1
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path){
		return path.substring((path.lastIndexOf("/")+1), path.lastIndexOf("."));
	}
	
	/**
	 * 확장자 포함된 파일 이름 반환 
	 * ex) 
	 * parameter : http://moumou.co.kr/2234_232_TDL_1.jgp  
	 * return : 2234_232_TDL_1.jgp
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileFullName(String path){
		return path.substring((path.lastIndexOf("/") + 1) , path.length());
	}

	public static String getString(TextView v){
		return v.getText().toString().trim();
	}
	public static String getString(EditText v){
		return v.getText().toString().trim();
	}
	
	public static boolean isNull(String str){
        return str == null || "".equals(str);
    }
	
	public static boolean isNull(EditText view){
		String txt = view.getText().toString().trim();
        return txt == null || "".equals(txt);
    }
}
