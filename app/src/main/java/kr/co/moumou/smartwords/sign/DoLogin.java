package kr.co.moumou.smartwords.sign;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.HashMap;
import java.util.Map;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.communication.Const;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.communication.GlobalApplication;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.util.ToastUtil;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoUserInfo;

/**
 * Created by 김민정 on 2018-07-04.
 */

public class DoLogin extends Thread {

    private Context mContext;
    private DoLoginListener listener;
    private String id;
    private String pw;
    private boolean isLogin = false;
    private String sid;

    public interface DoLoginListener {
        void failLogin();
        void successLogin(String response);
    }

    public DoLogin(Context context, DoLoginListener listener, String id, String pw) {
        super();
        this.mContext = context;
        this.listener = listener;
        this.id = id;
        this.pw = pw;
    }

    @Override
    public void run() {
        super.run();
        versionCheck();
        getTOkenID();
        login();
//        if(isLogin){
//            login();
//        }else certification();

    }

    /**
     * 로그인
     */
    private void login() {
        String url = ConstantsCommURL.getUserUrl(ConstantsCommURL.REQUEST_GET_LOGIN);
        String tag = ConstantsCommURL.REQUEST_TAG_LOGIN;

        Uri.Builder builder = Uri.parse(url).buildUpon();

        builder.appendQueryParameter("USERID",  id);
        builder.appendQueryParameter("USERPW",  pw);

//        Crashlytics.setUserEmail(id);

//        Crashlytics.log(Log.ERROR, "tag", "log message");

        AndroidNetworkRequest.getInstance(mContext).StringRequest(tag,
                builder.toString(),
                new AndroidNetworkRequest.ListenerAndroidResponse() {
                    @Override
                    public void success(String response) {
                        LogUtil.d("response : " + response);

                        ApplicationPool.getGson().fromJson(response, VoUserInfo.class);
                        VoUserInfo mUserInfo = VoUserInfo.getInstance();
                        sid = VoUserInfo.getInstance().getSID();
//                        Crashlytics.setUserName(VoUserInfo.getInstance().getUSERNM());

//                        Answers.getInstance().logLogin(new LoginEvent()
//                                .putMethod("Normal")
//                                .putSuccess(true));


//                        Preferences.putPref(mContext, Preferences.PREF_USER_ID, VoUserInfo.getInstance().getUSERID());
//                        Preferences.putPref(mContext, Preferences.PREF_USER_PW, pw);
//                        Preferences.putPref(mContext, Preferences.PREF_IS_MEMBER, true);

                        certification();
//                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }

                    @Override
                    public void systemcheck(String response) {
                        LogUtil.d("systemcheck : " + response);
                    }

                    @Override
                    public void fail(VoBase voBase) {
                        listener.failLogin();
                    }

                    @Override
                    public void exception(ANError error) {
                        LogUtil.d("exception : " + error.toString());
                        ToastUtil.show(mContext,"네트워크 연결 상태를 확인해주세요", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void dismissDialog() {
                    }
//                    @Override
//                    public void networkerror() {
//                        listener.failLogin();
//                        LogUtil.d("networkerror");
//                    }
//
//                    @Override
//                    public void networkConnected() {
//
//                    }
                });
    }

    /**
     * 회원인증
     */
    private void certification() {

        String url = ConstantsCommURL.getAppUrl(ConstantsCommURL.REQUEST_SET_USERAPP);
        String tag = ConstantsCommURL.REQUEST_TAG_USERAPP;

        Map<String, String> params = new HashMap<>();
//
//        if(StringUtil.isNull(Preferences.getPref(mContext, Preferences.PREF_TOKEN_KEY, null)) &&
//                !StringUtil.isNull(FirebaseInstanceId.getInstance().getToken()))
//            Preferences.putPref(mContext, Preferences.PREF_TOKEN_KEY, FirebaseInstanceId.getInstance().getToken());

        params.put("SID", sid);
        params.put("APPID", Const.APPID);
        params.put("MAC", Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
        params.put("USERID", VoUserInfo.getInstance().getUSERID());
        params.put("TOKEN", Preferences.getPref(mContext, Preferences.PREF_TOKEN_KEY, ""));
        params.put("APPVER", Preferences.getPref(mContext, Preferences.PREF_APP_VERSION_CODE, 0) + "");
        params.put("ANDROIDVER", android.os.Build.VERSION.SDK_INT + "");
        params.put("MODEL", android.os.Build.MODEL);

        //LogUtil.i("params : " + params);

//        builder.appendQueryParameter("SID",  Preferences.getPref(mContext, Preferences.PREF_USER_ID, null));
//        builder.appendQueryParameter("APPID",  Preferences.getPref(mContext, Preferences.PREF_USER_PW, null));

        AndroidNetworkRequest.getInstance(mContext).postRequest(tag,
                url.toString(),
                params,
                new AndroidNetworkRequest.ListenerAndroidResponse() {
                    @Override
                    public void success(String response) {
                        LogUtil.d("response : " + response);
                        listener.successLogin(response);
                    }

                    @Override
                    public void systemcheck(String response) {
                        LogUtil.d("systemcheck : " + response);
                    }

                    @Override
                    public void fail(VoBase voBase) {
                        listener.failLogin();
                    }

                    @Override
                    public void exception(ANError error) {
                        LogUtil.d("exception : " + error.toString());
                    }

                    @Override
                    public void dismissDialog() {

                    }
//                    @Override
//                    public void networkerror() {
//                        listener.failLogin();
//                        LogUtil.d("networkerror");
//                    }
//
//                    @Override
//                    public void networkConnected() {
//
//                    }
                });
    }

    private void versionCheck() {
        String versionName = "";
        int versionCode = 0;
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        Preferences.putPref(mContext, Preferences.PREF_APP_VERSION_NM, versionName);
        Preferences.putPref(mContext, Preferences.PREF_APP_VERSION_CODE, versionCode);
    }

    private void getTOkenID() {
//        String regId = FirebaseInstanceId.getInstance().getToken();
//
//        //토큰이 같지 않을 경우에만 서버에 전송한다.
//        String token_id = PreferenceUtil.getStringSharedData(this, Preferences.PREF_TOKEN_KEY, null);
//        if(null == regId || regId.equals(token_id)) return;
    }
}
