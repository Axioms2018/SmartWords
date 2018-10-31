package kr.co.moumou.smartwords.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.moumou.smartwords.vo.SplitInfo;


public class StringUtil {
	
	public static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}
	
	public static SplitInfo getSplitInfo(String strTarget, String strSplit) {
		SplitInfo info = new SplitInfo();
		
		if (StringUtil.isNull(strTarget))
			return info;
		
		String[] arrTarget = strTarget.split(strSplit);
		
		int contSize = arrTarget.length;
		info.setSize(contSize);
		
		info.setStrings(arrTarget);
		
		return info;
	}
	
	public static SplitInfo getSplitInfo(String strTarget, String strSplit, int limit) {
		
		SplitInfo info = new SplitInfo();
		
		if (StringUtil.isNull(strTarget))
			return info;
		
		String[] arrTarget = strTarget.split(strSplit, limit);
		
		int contSize = arrTarget.length;
		info.setSize(contSize);
		
		info.setStrings(arrTarget);
		
		return info;
	}

	public static String[] getRandomArr(String strTarget, String strSplit) {
		ArrayList<Integer> chkDup = new ArrayList<Integer>();

		String[] arrTarget = strTarget.split(strSplit);
		String[] arrRet = new String[arrTarget.length];
		
		Random rnd = new Random();

		int retIndex=0;
		
		for (int idx=0; idx<arrTarget.length; idx++) {
			
			while(true) {
				int nRandom = rnd.nextInt(arrTarget.length);
				
				if (!chkDup.contains(nRandom)) {
					chkDup.add(nRandom);
					String str = arrTarget[nRandom];
					arrRet[retIndex] = str;
					retIndex++;
					break;
				}
			}
		}
		
		boolean isSame = true;
		
		for (int idx=0; idx<arrTarget.length; idx++) {
			if (!arrTarget[idx].equals(arrRet[idx])) {
				isSame = false;
				break;
			}
		}
		
		if (isSame) {
			return getRandomArr(strTarget, strSplit);
		}
		
		return arrRet;
	}
	
	public static String[] shuffle(String[] result) {

		String temp;
		int seed;

		for(int i=0;i<result.length;i++){

			seed=(int)(Math.random()*result.length);

			temp = result[i];

			result[i]=result[seed];

			result[seed]=temp;

		}

		return result;

	}
	
	public static TextView[] shuffle(TextView[] result) {

		TextView temp;
		int seed;

		for(int i=0;i<result.length;i++){

			seed=(int)(Math.random()*result.length);

			temp = result[i];

			result[i]=result[seed];

			result[seed]=temp;

		}

		return result;

	}
	
	public static <T> ArrayList<T> shuffle(ArrayList<T> result) {

		T temp;
		int seed;

		for(int i=0;i<result.size();i++){

			seed=(int)(Math.random()*result.size());

			temp = result.get(i);

			result.set(i, result.get(seed));
			result.set(seed, temp);

		}

		return result;

	}
	
	public static int getMaxLength(String[] strs) {
		int maxLength=0;
		
		for(int i=0;i<strs.length;i++){

			if (maxLength < strs[i].length())
				maxLength = strs[i].length();
		}
		
		return maxLength;
	}
	
	public static boolean isNull(String str){
		return str == null || "".equals(str.trim());
	}
	
	public static boolean isNull(EditText view){
		String txt = view.getText().toString().trim();
		return txt == null || "".equals(txt);
	}

	public static String getString(TextView v){
		return v.getText().toString().trim();
	}
	public static String getString(EditText v){
		return v.getText().toString().trim();
	}

	public static int countOccurrences(String input, String word) {
		int count = 0;
		Pattern pattern = Pattern.compile(word);
		Matcher matcher = pattern.matcher(input);
		
		while (matcher.find()) {
			count++;
		}
		
		return count;
	}
	
	public static String getData(Context context, String fileName) throws IOException {

		String result = "";
		
		AssetManager am = context.getResources().getAssets();
		InputStream is = null;
		
		try {
			is = am.open(fileName);
			int size = is.available();

			if (size > 0) {
				byte[] data = new byte[size];
				is.read(data);
				result = new String(data);

				return result;
			}
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e) {
				}
			}
		}
		
		return result;
	}
	
	static public String convertToHHMMSS(int second) {
		
		int hh = (second / 3600);
		int mm = (second % 3600 / 60);
		int ss = (second % 3600 % 60);
		
		StringBuffer time = new StringBuffer();
		
		if(hh > 0) time.append(String.format("%02d", hh) + ":");
		time.append(String.format("%02d", mm) + ":" + String.format("%02d", ss));
		
		return time.toString();
	}
	
	static public String JangconvertToHHMMSS(int second) {
		
		int hh = (second / 3600);
		int mm = (second % 3600 / 60);
		int ss = (second % 3600 % 60);
		
		StringBuffer time = new StringBuffer();
		
		if(hh > 0) time.append(String.format("%02d", hh) + "시간 ");
		if(mm > 0) time.append(String.format("%02d", mm) + "분 ");
		time.append(String.format("%02d", ss) + "초");
		
		return time.toString();
	}
	
	static public String getVoiceMsg(boolean isBig) {
		
		String voiceMsg="";
		
		Random rnd = new Random();
		
		String[] voiceBigMsg =
			{"Way to go!","Awesome!","You're amazing!","You've improved!","Fantastic work!",
			 "Nice going!","Keep it up!","You rule!","Two thumbs up!","Excellent!","Great job!","Hooray for you!"};
		
		String[] voiceSmallMsg = {"Keep on trying~","Cheer up~","Try harder~","You can do it better~"};
		
		if (isBig) {
			int nRandom = rnd.nextInt(voiceBigMsg.length);
			return voiceBigMsg[nRandom];
		} else {
			int nRandom = rnd.nextInt(voiceSmallMsg.length);
			return voiceSmallMsg[nRandom];
		}
		
	}


	
//	static public String getRecPath(VoQuest templateInfo, String fileName) {
//
//		File moumouFolder = new File(Constant.EXTERNAL_PATH_MOUMOU);
//		if (!moumouFolder.exists()) {
//			LogUtil.d("make moumouFolder");
//			moumouFolder.mkdir();
//		}
//
//		String recPath = Constant.EXTERNAL_PATH_MOUMOU
//				+ "sessionid_pass" + "_"
//				+ templateInfo.getPCODE() + "_"
//				+ templateInfo.getQUST_SEQ() + "_"
//				+ templateInfo.getSTDY_GUBN() + "_"
//				+ templateInfo.getSTDY_GUBN_SEQ() + "_"
//				+ templateInfo.getCON_SEQ() + "_"
//				+ templateInfo.getSTDY_TYPE() + "_"
//				+ fileName;
//
//		return recPath;
//	}
//
//	static public String getRecShortPath(VoQuest templateInfo, String fileName) {
//		String recPath = "sessionid_pass" + "_"
//				+ templateInfo.getPCODE() + "_"
//				+ templateInfo.getCHACI() + "_"
//				+ templateInfo.getQUST_SEQ() + "_"
//				+ templateInfo.getSTDY_STGE() + "_"
//				+ templateInfo.getSTDY_GUBN() + "_"
//				+ templateInfo.getCON_SEQ() + "_"
//				+ fileName;
//
//		return recPath;
//
//	}
//	static public String getRecPathForSend(VoQuest templateInfo, String fileName) {
//
//		File moumouFolder = new File(Constant.EXTERNAL_PATH_MOUMOU);
//		if (!moumouFolder.exists()) {
//			LogUtil.d("make moumouFolder");
//			moumouFolder.mkdir();
//		}
//
//		String recPath = Constant.EXTERNAL_PATH_MOUMOU
//				+ "sessionid_pass" + "_"
//				+ templateInfo.getPCODE() + "_"
//				+ templateInfo.getCHACI() + "_"
//				+ templateInfo.getQUST_SEQ() + "_"
//				+ templateInfo.getSTDY_STGE() + "_"
//				+ templateInfo.getSTDY_GUBN() + "_"
//				+ templateInfo.getCON_SEQ() + "_"
//				+ fileName;
//
//		return recPath;
//	}
//
//	static public String getDrawFileName(VoWS301 ws301) {
//
//		String drawPath = "sessionid_pass" + "_"
//				+ ws301.getSTDY_GUBN() + "_"
//				+ ws301.getSTDY_TYPE() + "_"
//				+ ws301.getIMG_PATH();
//
//		return drawPath;
//	}
//
//	static public String getDrawPath(VoWS301 ws301) {
//
//		String filePath = Constant.DATA_PATH + Constant.PATH_DRAW;
//
//		File drawFolder = new File(filePath);
//		if (!drawFolder.exists()) {
//			LogUtil.d("make drawFolder");
//			drawFolder.mkdir();
//		}
//
//		String drawPath = filePath
//				+ getDrawFileName(ws301);
//
//		return drawPath;
//	}
//
//	static public String getDrawFileName(VoQuest tempalteInfo, String fileName) {
//
//		String drawPath = "sessionid_pass" + "_"
//				+ tempalteInfo.getSTDY_GUBN() + "_"
//				+ tempalteInfo.getSTDY_TYPE() + "_"
//				+ fileName;
//
//		return drawPath;
//	}
//
//	static public String getDrawPath(VoQuest templateInfo, String fileName) {
//
//		String filePath = Constant.DATA_PATH + Constant.PATH_DRAW;
//
//		File drawFolder = new File(filePath);
//		if (!drawFolder.exists()) {
//			LogUtil.d("make drawFolder");
//			drawFolder.mkdir();
//		}
//
//		String drawPath = filePath
//				+ "sessionid_pass" + "_"
//				+ templateInfo.getSTDY_GUBN() + "_"
//				+ templateInfo.getSTDY_TYPE() + "_"
//				+ fileName;
//
//		return drawPath;
//	}
}
