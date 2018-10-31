package kr.co.moumou.smartwords.communication;

import android.os.Environment;

/**
 * Created by 김민정 on 2018-04-03.
 */

public class Const {
    public static final String PACKAGENAME = "kr.co.axioms";
    public static final String APPNAME = "Axiom";
    public static final String APKFILENAME = "axiom";
//    public static final String RESOURCE = "android.resource://" + GlobalApplication.getInstance().getPackageName();
    public static final String FILE_TYPE_ZIP = ".zip";
    public static final String APPID = "2";                      //앱아이디
    public static final int VRSN = 1;                          //본학습

    public static final String SAVE_USER_INFO = "SAVE_USER_INFO";


    public static final String SAVE_STUDY_STEP = "SAVE_STUDY_STEP";
    public static final String SAVE_STUDY_QUST = "SAVE_STUDY_QUST";

    public final static int RESULT_APK_INSTALL = 1002;

    public final static String STUDY_GB_REPORT = "106004";

    /** 로그인시 설정 **/
    public static final String USER_TEST = "$test_";            //비로그인
    public static final String USER_INIT = "$init_";            //처음 접속 시

    /** 학습시 진행 값 **/
    public static final float LESSON_PRCESS_NONE = 0;             //미진행
    public static final float LESSON_PRCESS_COMPLETE = 100;       //완료

    /** 특수문자 **/
    public final static String GUBUN_SPACE = " ";
    public final static String GUBUN_SEMI_COLON = ";";
    public final static String GUBUN_NEW_LINE = "\n";
    public final static String GUBUN_SEMI_SHARP = "#";
    public final static String GUBUN_SEMI_PERCENT = "%";

    /** 인텐트 공통 **/
    public class IntentKeys {
        public final static String INTENT_DOUBLE_LOGIN = "double_login";
    }

    //final public static String STT_RESOURCE_PATH = android.os.Environment.getExternalStorageDirectory().getPath() + "/STTEng/";
//    final public static String STT_RESOURCE_PATH = "data/data/" + GlobalApplication.getInstance().getPackageName() + "/files/STTEng/";
//    public static final String DATA_PATH = "data/data/" + GlobalApplication.getInstance().getPackageName() + "/files/";

    public static final String PATH_ADULT_AXIOMS = "/MouMou/";
    public static final String EXTERNAL_PATH_MOUMOU = Environment.getExternalStorageDirectory().getAbsolutePath() + PATH_ADULT_AXIOMS;

}
