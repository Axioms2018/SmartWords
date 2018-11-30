package kr.co.moumou.smartwords.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
import kr.co.moumou.smartwords.communication.GlobalApplication;
import kr.co.moumou.smartwords.util.CommUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.vo.VoCertificate;
import kr.co.moumou.smartwords.vo.VoCertificate1;
import kr.co.moumou.smartwords.vo.VoUserInfo;

/**
 * Created by moumouna on 2018-09-21.
 */

public class ActivityLogin extends AppCompatActivity implements View.OnKeyListener, TextView.OnEditorActionListener {



    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private UserLoginTask mAuthTask = null;


//    @BindView(R.id.edit_email) EditText mEditEmail;
//    @BindView(R.id.edit_pwd) EditText mEditPwd;
//    @BindView(R.id.btn_sign) Button mBtn_Sign;

    private EditText mEditEmail,mEditPwd;
    private Button mBtn_Sign;

    private long backKeyPressedTime = 0;    // '뒤로' 버튼을 클릭했을 때의 시간
    private long TIME_INTERVAL = 2000;      // 첫번째 버튼 클릭과 두번째 버튼 클릭 사이의 종료를 위한 시간차를 정의
    private Toast toast;                    // 종료 안내 문구 Toast


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);



        mEditEmail = (EditText) findViewById(R.id.edit_email);
        mEditPwd = (EditText) findViewById(R.id.edit_pwd);
        mBtn_Sign = (Button) findViewById(R.id.btn_sign);


//        mEditPwd.setOnKeyListener(this);
        mEditPwd.setOnEditorActionListener(this);
        mBtn_Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });


//        mEditEmail.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
//                    mEditEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//                    return true;
//                }else {
//                    return false;
//                }
//
//            }
//        });



        String userId = Preferences.getPref(ActivityLogin.this, Preferences.PREF_USER_ID, "");
        String testId = "$test_" + CommUtil.getAndroidID(this);
        if(userId.equals(testId)){
            String nulltext = "";
            mEditPwd.setText(nulltext);
        }else{
            mEditEmail.setText(userId);
        }



    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP ){
            LogUtil.i("onKeyEnter");
            attemptLogin();
            LogUtil.i("onKeyEnter123");
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE ){
            LogUtil.i("onKeyEnter");
            attemptLogin();
            LogUtil.i("onKeyEnter123");
        }
        return false;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
            } else {
                mEditPwd.setError(getString(R.string.error_incorrect_password));
                mEditPwd.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEditEmail.setError(null);
        mEditPwd.setError(null);

        // Store values at the time of the login attempt.
        String email = mEditEmail.getText().toString();
        String password = mEditPwd.getText().toString();


        if (TextUtils.isEmpty(email)) {
            inValidLogin(mEditEmail, getString(R.string.error_email_field_required));
            return;
        }
        if (!isEmailValid(email)) {
            inValidLogin(mEditEmail, getString(R.string.error_invalid_email));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            inValidLogin(mEditPwd, getString(R.string.error_pwd_field_required));
            return;
        }


        apiLogin(email, password);

    }
    private void inValidLogin(View focusView, String errMsg) {
        focusView.requestFocus();

        if (focusView instanceof EditText) {
            ((EditText)focusView).setError(errMsg);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }


    private void apiLogin(final String userID, final String userPwd) {

        new DoLogin(this, new DoLogin.DoLoginListener() {


            @Override
            public void failLogin() {

            }

            @Override
            public void successLogin(String response) {

                VoCertificate1 voCertificate = GlobalApplication.getGson().fromJson(response, VoCertificate1.class);

                VoUserInfo.getInstance().setUSERID(voCertificate.getUSERID());
                VoUserInfo.getInstance().setSID(voCertificate.getSID());
                VoUserInfo.getInstance().setPAY_YN("Y");

                VoUserInfo.getInstance().getSID();

                Preferences.putPref(ActivityLogin.this, Preferences.PREF_USER_ID, userID);
                Preferences.putPref(ActivityLogin.this, Preferences.PREF_USER_PW, userPwd);
                Preferences.putPref(ActivityLogin.this, Preferences.PREF_IS_MEMBER, true);       //비회원 체크를 위해

                Intent intent = new Intent(ActivityLogin.this, ActivityWordTestMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, userID, userPwd).run();

    }

//    @Override
//    public void onBackPressed() {
//        // '뒤로' 버튼 클릭 시간과 현재 시간을 비교 게산한다.
//
//        // 마지막 '뒤로'버튼 클릭 시간이 이전 '뒤로'버튼 클릭시간과의 차이가 TIME_INTERVAL(여기서는 2초)보다 클 때 true
//        if (System.currentTimeMillis() > backKeyPressedTime + TIME_INTERVAL) {
//
//            // 현재 시간을 backKeyPressedTime에 저장한다.
//            backKeyPressedTime = System.currentTimeMillis();
//
//            // 종료 안내문구를 노출한다.
//            showMessage();
//        }else{
//            // 마지막 '뒤로'버튼 클릭시간이 이전 '뒤로'버튼 클릭시간과의 차이가 TIME_INTERVAL(2초)보다 작을때
//
//            // Toast가 아직 노출중이라면 취소한다.
//            toast.cancel();
//
//            // 앱을 종료한다.
//            this.finish();
//        }
//    }

    public void showMessage() {
        toast = Toast.makeText(ActivityLogin.this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
