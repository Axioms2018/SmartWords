package kr.co.moumou.smartwords.common;

import android.os.Environment;

public class Constant {

	public static String APP_PACKAGE_NAME = "kr.co.moumou.smartwords";
	public static String OTHER_APK_URL = "http://install.kr.co.moumou/firmware/";
	public static String SILENT_INSTALL_APK_NAME = "SystemManagerForMooMoo.apk";
	public static String SECOND_FIRMWARE_UPDATE_APK_NAME = "MPGIO_RKUpdater.apk";
	public class IntentAction{
		//Activit가 forground에 있을 때 Intent를 수신하기 위한 action.  
		public static final String ACTION_TEACHER_SEND = "mm.teacher.send";
		public static final String ACTION_STUDY_ACTIONS = "mm.study.action";
		public static final String ACTION_DOWNLOAD_CANCEL = "mm.download.cancel";
	}

	public class IntentKeys{
		public static final String INTENT_TERMINATE_APP = "terminate_app";
		public static final String INTENT_DOUBLE_LOGIN = "double_login";
		public static final String INTENT_TEACHER_MESSAGE = "teacher_msg";
		public static final String INTENT_DOWNLOAD_CANCEL = "download_cancel";

	}

	
	public static final int USER_TYPE_STUDENT = 1;               //정회원(학생)
	public static final int USER_TYPE_STUDENT_RESERVE = 2;       //예비회원
	public static final int USER_TYPE_STUDENT_EXPERIENCE= 3;     //체험회원
	public static final int USER_TYPE_PARENTS = 4;               //학부모
	public static final int USER_TYPE_DIRCTOR = 5;               //원장님
	public static final int USER_TYPE_TICHER = 6;                //선생님
	public static final int USER_TYPE_HEADQUARTER = 7;           //본사(타부서)
	public static final int USER_TYPE_MANAGER = 8;               //관리자(시스템)
	public static final int USER_TYPE_NONMEMBER = 9;             //비회원

	public static final String STRING_DEFAULT = "default";
	public static final String STRING_CON_SEQ_DEFAUL = "000000000000";
	public static final String EXPERIENCE_USER_ID_PREFIX = "$test_";

	private static final int APP_DEFAULT_MODE = 100;
	public static final int APP_DEV_MODE = APP_DEFAULT_MODE + 1;
	public static final int APP_OPEN_MODE = APP_DEFAULT_MODE + 2;
	public static final int APP_ADMIN_MODE = APP_DEFAULT_MODE + 3;
	public static final int APP_EDU_MODE = APP_DEFAULT_MODE + 4;
	public static boolean isSuperDev = false;

	public static int APP_MODE = APP_OPEN_MODE;

	private static final int DEFAULT_DATA_MODE = 200;
	public static final int LOCAL_DATA_MODE = DEFAULT_DATA_MODE + 1;


	public final static String FONT_NAME = "BareunDotumPro1.ttf";
	public final static String FONT_BOLD_NAME = "BareunDotumPro2.ttf";
	

	public final static String GUBUN_SPACE = " ";
	public final static String GUBUN_SHARP = "#";
	public final static String GUBUN_COMMA = ",";
	public final static String GUBUN_DOT = ".";
	public final static String GUBUN_SLASH = "/";
	public final static String GUBUN_SEMI_COLON = ";";
	public final static String GUBUN_NEW_LINE = "\n";
	public final static String GUBUN_STAR = "*";
	public final static String GUBUN_DOLLAR = "$";
	public final static String GUBUN_AND = "&";
	public final static String GUBUN_APO = "'";
	public final static String GUBUN_PIPE = "\\|";
	public final static String GUBUN_HYPHEN = "-";
	public final static String GUBUN_UNSLASH = "\\";
	public final static String GUBUN_QUESTION = "?";
	public final static String GUBUN_EX_MARK = "!";



	//================================== 파일 관련 ==================================//
	public static final String DATA_PATH = "data/data/" + APP_PACKAGE_NAME + "/files/";
	public static final String PATH_CHANT = "chant/";
	public static final String PATH_DRAW = "draw/";
	public static final String PATH_WORDS = "smartWords/";
	
	public static final String PATH_MOUMOU = "/MouMou/";
	public static final String EXTERNAL_PATH_MOUMOU = Environment.getExternalStorageDirectory().getAbsolutePath() + PATH_MOUMOU;

	public static final String PATH_REC_FILE_FOLDER = "AudioRecorder/";
	public static final String PATH_REC_FILE = "rec";
	//================================== 파일 관련 ==================================//

	public final static int BIC_VOICE_LEVEL_0 = 50;
	public final static int BIC_VOICE_LEVEL_1 = 55;
	public final static int BIC_VOICE_LEVEL_2 = 65;
	public final static int BIC_VOICE_LEVEL_3 = 70;
	public final static int BIC_VOICE_LEVEL_4 = 75;
	public final static int BIC_VOICE_LEVEL_5 = 86;

	
	public static final String BROADCAST_ACTION_VOD_PLAYER_STATE 		= "BROADCAST_ACTION_VOD_PLAYER_STATE";
	public static final String BROADCAST_ACTION_SLIDE_STATE 		= "BROADCAST_ACTION_SLIDE_STATE";

	
	public final static String STD_GB_S = "S"; // 본학습
	public final static String STD_GB_R = "R"; // 재학습
	
	public final static String STD_W_GB_D = "D"; // 데일리
	public final static String STD_W_GB_R = "R"; // Reivew

	
}
