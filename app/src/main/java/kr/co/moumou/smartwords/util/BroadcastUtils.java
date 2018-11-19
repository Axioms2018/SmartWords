package kr.co.moumou.smartwords.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;


public class BroadcastUtils {
    private BroadcastUtils(){

    }
    static public String getCurrentTime(){
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        Date resultDate = new Date(currentTime);
        return sdf.format(resultDate);
    }

    /** 데이터 저장 유무 */
    static public void sestDateTime(Context ctx, String input) {
        SharedPreferences preferences = ctx.getSharedPreferences("kr.co.cs.packagetestclientapp.datetime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("kr.co.cs.packagetestclientapp.datetime", input);
        editor.commit();
    }
    /** 자이로 센서 안내문구 표시 유무 */
    static public String getDatetime(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("kr.co.cs.packagetestclientapp.datetime", Context.MODE_PRIVATE);
        return preferences.getString("kr.co.cs.packagetestclientapp.datetime", "do not record");
    }

}
