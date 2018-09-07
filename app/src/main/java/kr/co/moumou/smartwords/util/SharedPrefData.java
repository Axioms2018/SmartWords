package kr.co.moumou.smartwords.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefData {
	private static final String SHARED_FILE_TITLE = "pref_moumou";

    public static final String SHARED_DEFAULT_S = "default";
    public static final String SHARED_API_IP = "api_ip";
	public static final String SHARED_SOCKET_IP = "socket_ip";
    public static final String SHARED_SAVE_ID_B = "save_id";
    public static final String SHARED_USER_PWD_S = "pwd";
    public static final String SHARED_STUDY_ROOM_MODE_B = "study_room_mode";
    public static final String SHARED_USER_ID_S = "user_id";
    public static final String SHARED_USER_SESSIONID = "session_id";
    public static final String SHARED_TEACHER_ID_S_ = "teacher_id_";
    public static final String SHARED_TEACHER_NAME_S_ = "teacher_name";
    public static final String SHARED_SHORT_CUT_B = "short_cut";
    public static final String SHARED_RESOURCE_VER = "resource_ver";

    public static final String SHARED_NOTICE_SEE_HISTORY_S_ = "notice_";
    public static final String SHARED_GRADE_I = "grade";
    
    public static final String SHARED_MEMNAME_S = "mem_name";
    public static final String SHARED_CHACI_S = "chaci";
    public static final String SHARED_PCODE_S = "pcode";
    public static final String SHARED_CON_SEQ_S = "conseq";
    public static final String SHARED_PNAME_S = "pname";
    public static final String SHARED_PSNAME_S = "psname";
    public static final String SHARED_STDY_PGSS_STAT_S = "stdy_pgss_state";
    
    public static final String SHARED_STDY_GUBN_S = "stdy_gubn";
    public static final String SHARED_STDY_GUBN_SEQ_I = "stdy_gubn_seq";
    public static final String SHARED_COMPCODE_S = "comp_code";
    public static final String SHARED_MEMNO_S = "mem_no";
    public static final String SHARED_COMP_INOUT_GUBN_S = "inout_gubn";
    
    public static final String SHARED_BRIGHTNESS_VALUE = "brigth_value";
    public static final String SHARED_USER_PIC_ = "user_pic_";
	public static final String SHARED_SOUND_VALUE = "sound_value";
    public static final String SHARED_LOUD_LEVEL_I = "loud_level_";
    public static final String SHARED_IS_LEFT_HAND_B = "is_left_hand_";
    public static final String SHARED_IS_FFMPEG_USE_B = "ffmpeg_use";
    
    public static final String SHARED_SHOW_BACKGROUND_B = "show_background";
    
    public static final String PREF_EXCEPTION_SHORT_S = "exception_short";
    public static final String PREF_EXCEPTION_DETAIL_S = "exception_detail";
    public static final String SHARED_DOWNLOAD_ID_L = "download_id";
    public static final String SHARED_TEST_DOWNLOAD_B = "test_downalod";
    
    public static final String SHARED_CALL_TIME_L = "call_time_l";
    
    public static final String SHARED_CURRENT_DATE = "current_date";
    public static final String SHARED_CLASSRULE_POPUP_SHOW = "classrule_popup_show";

    /* 먼슬리 데이터 내역 저장 */
    public static final String PREF_MONTHLY_TEST_TYPE = "monthly_test_type";
    public static final String PREF_MONTHLY_CURRENT_TAB = "monthly_test_current_tab";
    public static final String PREF_MONTHLY_CURRENT_INDEX = "monthly_test_current_index";
    
    public static final String SHARED_WCCODE_S = "wccode";
    
    public static final String SHARED_LIBRARY_I = "library";
    public static final String SHARED_FAQ_I = "faq";
    public static final String SHARED_NOTICE_I = "notice";
    
    public static final String SHARED_WS301_BGM = "ws301_bgm";
    
    public static final String SHARED_BOOK_TYPE_S = "book_type";
    public static final String SHARED_USER_DURATION_VALUE_ID_ = "my_duration_value_";
    
    public static final String SHARED_SECOND_FIRMWARE_VER = "second_firm_ver";
    public static final String SHARED_SECOND_FIRMWARE_FILE_NAME = "second_firm_file_name";
    
    public static final String SHARED_SCREEN_TOUCH_TIME_L = "screen_touch_time";
    
    public static final String SHARED_SCREEN_WAIT_TIME_L = "screen_wait_time";
    public static final String SHARED_SCREEN_TIME_OUT = "screent_time_out";
    
    public static final String SHARED_SERVICE_EXECUTE_TIME_L = "service_execute_time";
    
    public static final String PREF_WORDS_CERT_L = "words_certificate_level";
    
    public static boolean getBooleanSharedData(Context context, String key, boolean defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultData);
    }
    public static void setBooleanSharedData(Context context, String key, boolean flag) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(key, flag);
        e.commit();
    }
    public static int getIntSharedData(Context context, String key, int defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultData);
    }
    public static void setIntSharedData(Context context, String _key, int _data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putInt(_key, _data);
        e.commit();
    }
    public static long getLongSharedData(Context context, String key, long defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getLong(key, defaultData);
    }
    public static void setLongSharedData(Context context, String _key, long _data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putLong(_key, _data);
        e.commit();
    }
    public static String getStringSharedData(Context context, String key, String defaultData) {
    	if (context == null)
    		return "";
    	
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getString(key, defaultData);
    }
    public static void setStringSharedData(Context context, String key, String data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString(key, data);
        e.commit();
    }
}
