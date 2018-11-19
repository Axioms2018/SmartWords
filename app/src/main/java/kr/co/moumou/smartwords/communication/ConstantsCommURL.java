package kr.co.moumou.smartwords.communication;

public class ConstantsCommURL {
	
	public static boolean ISTEST = true;
	public static boolean IS_LOCAL_TEST = false;
	public static boolean IS_PASS_TEST_SERVER_CODE = false;
	public static boolean IS_MONTHLY_TEST_USEING_WEBVIEW = false;
	
	public final static int COMM_SUCCESS = 777;
	public final static int COMM_FAIL= 0000;
	
	public final static String SOCKET_POST_FIX = "<The End>";

	public static final String MOUMOU_TEST_DOMAIN = "http://dev.api.axioms.co.kr/";
	public static final String MOUMOU_DOMAIN = "http://api.word.moumou.co.kr/api/";
	public static String REQUEST_TEST_DOMAIN = MOUMOU_TEST_DOMAIN;
	public static String REQUEST_DOMAIN = MOUMOU_DOMAIN;
	private final static String REQUEST_TEST_WORD_DOMAIN = "http://dev.api.axioms.co.kr/";
	private final static String REQUEST_WORD_DOMAIN = "http://api.word.moumou.co.kr/api/";


	public final static String URL_STD = "SmartWord/";
	public final static String URL_COMMON = "Common/";
	public final static String URL_USER = "User/";
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
	public final static String REQUEST_GET_USERINFO = "GetUserInfo";                    //회원정보
	public final static String REQUEST_TAG_USERINFO = "TagUserInfo";                       //회원정보




	public final static String REQUEST_GET_LOGIN = "Loign";                             //로그인
	public final static String REQUEST_TAG_LOGIN = "TagLoign";                             //로그인
	public final static String URL_APP= "App/";


	public final static String REQUEST_SET_USERAPP = "SetUserApp";                      //회원인증
	public final static String REQUEST_TAG_USERAPP = "TagUserApp";                         //회원인증


	public final static String REQUEST_GET_MAXAPPVER = "GetMaxAppVer";                  //앱버전 체크
	public final static String REQUEST_TAG_MAXAPPVER = "TagMaxAppVer";                      //앱버전 체크

	//================================== 파일 관련 ==================================//
	public static final String DATA_PATH = "data/data/";
	public static final String DATA_FILE = "/files/";




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
	public static String getAppUrl(String request){
		return (ISTEST ? MOUMOU_TEST_DOMAIN : MOUMOU_DOMAIN) + URL_APP  + request;
	}

	public static String getUserUrl(String request){
		return (ISTEST ? REQUEST_TEST_DOMAIN : REQUEST_DOMAIN) + URL_USER  + request;
	}

	public static String getFilePath(String path){
		return DATA_PATH + GlobalApplication.getInstance().getPackageName() + DATA_FILE + path;
	}


}