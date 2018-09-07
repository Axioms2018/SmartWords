package kr.co.moumou.smartwords.communication;

public class ConstantsCommURL {
	
	public static boolean ISTEST = false;
	public static boolean IS_LOCAL_TEST = false;
	public static boolean IS_PASS_TEST_SERVER_CODE = false;
	public static boolean IS_MONTHLY_TEST_USEING_WEBVIEW = false;
	
	public final static int COMM_SUCCESS = 777;
	public final static int COMM_FAIL= 0000;
	
	public final static String SOCKET_POST_FIX = "<The End>";

	public static final String MOUMOU_TEST_DOMAIN = "http://devapi.mm.moumou.co.kr/api/";
	public static final String MOUMOU_DOMAIN = "http://api.word.moumou.co.kr/api/";
	public static String REQUEST_TEST_DOMAIN = MOUMOU_TEST_DOMAIN;
	public static String REQUEST_DOMAIN = MOUMOU_DOMAIN;
	private final static String REQUEST_TEST_WORD_DOMAIN = "http://devapi.word.moumou.co.kr/api/";
	private final static String REQUEST_WORD_DOMAIN = "http://api.word.moumou.co.kr/api/";
//	private  static String REQUEST_DOMAIN = "http://192.168.1.53/api/";     //이사님 PC
//	private final static String REQUEST_DOMAIN = "http://devapi.mm.moumou.co.kr/api/"; //테스트 서버
	
//	private final static String REQUEST_TEST_EBOOK = "http://testalpha.moumou.co.kr/api/";
	private final static String REQUEST_EBOOK = "http://api.ebook.moumou.co.kr/api/";
	private final static String REQUEST_TEST_EBOOK = "http://devapi.ebook.moumou.co.kr/api/";
//	11-26 15:20:09.241: I/MMCommLog(14783):  URL http://devapi.mm.moumou.co.kr/api/Common/SysAccLog?SESSIONID=2015112615195099c1d2&USERID=ss02&sysgb=101003&appver=0.3.9&ANDROIDVER=4.4.4&model=EMST&cpu=armeabi-v7a
//		11-26 15:20:23.101: I/MMCommLog(14783):  URL http://api.ebook.moumou.co.kr/api/std/GetAuthSmart?USERID=ss02&SESSIONID=2015112615195099c1d2&COMMAND=1515

	public final static String URL_STD = "std/";
	public final static String URL_EVA = "eva/";
	public final static String URL_UPLOAD = "FileUpload/";
	public final static String URL_COMMON = "Common/";
	public final static String URL_WC = "WritingCorrection/";
	public final static String URL_HOME = "home/";
	public final static String URL_PAINT = "/Upload/Paint/";

	public final static String REQUEST_GET_GETLEVELINFO = "GetLevelInfo";
	public final static String REQUEST_TAG_GETLEVELINFO = "GetLevelInfo";
	public final static String REQUEST_GET_GETDAYINFO = "GetDayInfo";
	public final static String REQUEST_TAG_GETDAYINFO = "GetDayInfo";
	public final static String REQUEST_GET_STDINFO = "GetStdInfo";
	public final static String REQUEST_TAG_STDINFO = "GetStdInfo";
	public final static String REQUEST_GET_DAYREPORT = "GetDayReport";
	public final static String REQUEST_TAG_DAYREPORT = "GetDayReport";
	public final static String REQUEST_GET_BANNER = "GetBanner";
	public final static String REQUEST_TAG_BANNER = "GetBanner";
	public final static String REQUEST_GET_QUIZINFO = "GetQuizInfo";
	public final static String REQUEST_TAG_QUIZINFO = "GetQuizInfo";
	public final static String REQUEST_TAG_SETSTDINFO = "SetStdInfo";
	public final static String REQUEST_GET_SETSTDINFO = "SetStdInfo";
	public final static String REQUEST_GET_LEVELREPORT = "GetLevelReport";
	public final static String REQUEST_TAG_LEVELREPORT = "GetLevelReport";
	public final static String REQUEST_GET_USERNOTE = "GetUserNote";
	public final static String REQUEST_TAG_USERNOTE = "GetUserNote";
	public final static String REQUEST_GET_USERCALENDAR = "GetUserCalendar";
	public final static String REQUEST_TAG_USERCALENDAR = "GetUserCalendar";
	public final static String REQUEST_GET_WORDRANKING = "GetWordRanking";
	public final static String REQUEST_TAG_WORDRANKING = "GetWordRanking";



	public static String getImageUrl(String pCode, String type) {
		return "http://img.moumou.co.kr/BookImgHandler.ashx" + "?pcode=" + pCode + "&s=" + type;
	}
	
	public static String getWordUrl(String request) {
		return getWordUrl(URL_STD, request);
	}
	
	public static String getWordUrl(String addUrl, String request) {
		return (ISTEST ? REQUEST_TEST_WORD_DOMAIN : REQUEST_WORD_DOMAIN) + addUrl + request;
	}
	
	public static String getUrl(String request) {
		return getUrl(URL_STD, request);
	}
		
	public static String getUrl(String addUrl, String request){
		return (ISTEST ? REQUEST_TEST_DOMAIN : REQUEST_DOMAIN) + addUrl  + request;
	}

	public static String getDevUrl(String addUrl, String request){
		return REQUEST_TEST_DOMAIN + addUrl  + request;
	}
	
	public static String getEbookUrl(String addUrl, String request){
		return (ISTEST ? REQUEST_TEST_EBOOK : REQUEST_EBOOK ) + addUrl + request;
	}

	public static String getWCUrl(String request) {
		return (ISTEST ? REQUEST_TEST_DOMAIN : REQUEST_DOMAIN) + URL_WC + request;
	}
	
	public static String getHOMEUrl(String request) {
		String homeUrl =(ISTEST ? REQUEST_TEST_DOMAIN : REQUEST_DOMAIN) + URL_HOME + request;
		return homeUrl.replace("api/", "");
	}
	
	public static String getPaintUrl(String request) {
		String homeUrl =(ISTEST ? REQUEST_TEST_DOMAIN : REQUEST_DOMAIN) + URL_PAINT + request;
		return homeUrl.replace("api/", "");
	}
}