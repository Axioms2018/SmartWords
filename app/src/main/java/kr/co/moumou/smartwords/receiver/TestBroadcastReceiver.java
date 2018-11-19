package kr.co.moumou.smartwords.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kr.co.moumou.smartwords.util.BroadcastUtils;

public class TestBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "TestBroadcastReceiver";
    public static final String BROADCAST_SERVER = "kr.co.kcs.packagetestserver.server";
    public static final String BROADCAST_CLIENT0_ONE = "kr.co.kcs.packagetestserver.client_one";
    public static final String BROADCAST_CLIENT0_TWO = "kr.co.kcs.packagetestserver.client_two";
    public static final String KEY_DATE = "date";

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();

        if (name.equals(BROADCAST_CLIENT0_TWO)) {
            //SharedPreferences 에 데이터 저장 부분
            BroadcastUtils.sestDateTime(context, intent.getStringExtra(KEY_DATE));
        }
    }
}