package kr.co.moumou.smartwords.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

    public static final String PREF_TOKEN_KEY               = "pref_token_key";
    public static final String PREF_APP_VERSION_NM          = "pref_app_version_nm";
    public static final String PREF_APP_VERSION_CODE        = "pref_app_version_code";


    public static final String PREF_IS_CHECK_FIRST          = "pref_is_first";          //최초 실행인지 체크
    public static final String PREF_USER_ID                 = "pref_user_id";
    public static final String PREF_USER_PW                 = "pref_user_password";
    public static final String PREF_IS_MEMBER               = "pref_is_member";



    private static final String PREFERENCE_NAME         = "MOUMOU_PREFERENCE";
    private static final String KEY_SCREEN_WIDTH		= "KEY_SCREEN_WIDTH";
    private static final String KEY_SCREEN_HEIGHT		= "KEY_SCREEN_HEIGHT";
    private static final String KEY_STATUS_BAR_HEIGHT		= "KEY_STATUS_BAR_HEIGHT";
    private static final String KEY_MARGIN_LEFT			= "KEY_MARGIN_LEFT";
    private static final String KEY_MARGIN_TOP			= "KEY_MARGIN_TOP";
    private static final String KEY_VIEW_WIDTH			= "KEY_VIEW_WIDTH";
    private static final String KEY_VIEW_HEIGHT			= "KEY_VIEW_HEIGHT";
    private static final String KEY_IS_TABLE			= "KEY_IS_TABLE";

    // 옵션에서 큰소리학습 목소리 데시벨 레벨
    public final static String PREF_DECIBEL_LEVEL_ = "PREF_DECIBEL_LEVEL_";
    public final static String PREF_IS_FF_PLAYER = "PREF_IS_FF_PLAYER";

    public final static String PREF_LEFT_HAND_ = "is_left_hand_";
    public final static String PREF_BOOK_RAGE2_S = "book_range_2";

    // SharedPreference 가져오기
    public static SharedPreferences getPreferences(Context ctx){
        return ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getPreferencesEditor(Context ctx) {
        SharedPreferences pref = getPreferences(ctx);
        return pref.edit();
    }

    public static void put(Context ctx, String key, String value) {
        SharedPreferences.Editor editor = getPreferencesEditor(ctx);
        editor.putString(key, value);
        editor.commit();
    }

    public static void put(Context ctx, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferencesEditor(ctx);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void put(Context ctx, String key, long value) {
        SharedPreferences.Editor editor = getPreferencesEditor(ctx);
        editor.putLong(key, value);
        editor.commit();
    }

    public static void put(Context ctx, String key, int value) {
        SharedPreferences.Editor editor = getPreferencesEditor(ctx);
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getValue(Context ctx, String key, String dftValue) {
        try {
            return  getPreferences(ctx).getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public static long getValue(Context ctx, String key, long dftValue) {
        try {
            return getPreferences(ctx).getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public static int getValue(Context ctx, String key, int dftValue) {
        try {
            return getPreferences(ctx).getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public static boolean getValue(Context ctx, String key, boolean dftValue) {
        try {
            return getPreferences(ctx).getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public static void preClear(Context ctx) {
        SharedPreferences.Editor editor = getPreferencesEditor(ctx);
        editor.clear();
        editor.commit();
    }

    public static int getMarginLeft(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_MARGIN_LEFT, 0);
    }

    public static void setMarginLeft(Context cxt, int marginTop){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_MARGIN_LEFT, marginTop);
        et.commit();
    }

    public static int getMarginTop(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_MARGIN_TOP, 0);
    }

    public static void setMarginTop(Context cxt, int marginTop){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_MARGIN_TOP, marginTop);
        et.commit();
    }

    public static int getViewWidth(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_VIEW_WIDTH, 0);
    }

    public static void setViewWidth(Context cxt, int width){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_VIEW_WIDTH, width);
        et.commit();
    }

    public static int getViewHeight(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_VIEW_HEIGHT, 0);
    }

    public static void setViewHeight(Context cxt, int height){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_VIEW_HEIGHT, height);
        et.commit();
    }

    public static boolean getIsTable(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return false;
        }

        return sp.getBoolean(KEY_IS_TABLE, false);
    }

    public static void setIsTable(Context cxt, boolean isTable){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putBoolean(KEY_IS_TABLE, isTable);
        et.commit();
    }

    public static void setScreenWidth(Context cxt, int width){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_SCREEN_WIDTH, width);
        et.commit();
    }

    public static int getScreenWidth(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_SCREEN_WIDTH, 0);
    }

    public static void setScreenHeight(Context cxt, int height){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_SCREEN_HEIGHT, height);
        et.commit();
    }

    public static int getScreenHeight(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_SCREEN_HEIGHT, 0);
    }

    public static void setStatusBarHeight(Context cxt, int height){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putInt(KEY_STATUS_BAR_HEIGHT, height);
        et.commit();
    }

    public static int getStatusBarHeight(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return 0;
        }

        return sp.getInt(KEY_STATUS_BAR_HEIGHT, 0);
    }
    public static void putPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putString(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, int value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, long value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getPref(Context context, String key, String defaultValue) {
        try {
            return getPreferences(context).getString(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getPref(Context context, String key, int defaultValue) {
        try {
            return getPreferences(context).getInt(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getPref(Context context, String key, boolean defaultValue) {
        try {
            return getPreferences(context).getBoolean(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getPref(Context context, String key, long defaultValue) {
        try {
            return getPreferences(context).getLong(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }
}
