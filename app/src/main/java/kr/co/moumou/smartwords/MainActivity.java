package kr.co.moumou.smartwords;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.androidnetworking.error.ANError;

import kr.co.moumou.smartwords.activity.ActivityBase;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.Const;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.communication.GlobalApplication;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.sign.ActivityLogin;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.vo.VoAppVer;
import kr.co.moumou.smartwords.vo.VoBase;

public class MainActivity extends ActivityBase {

    private VoAppVer voAppVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout)findViewById(R.id.lay_background);
        Preferences.setScreenWidth(this, DisplayUtil.getScreenWidth(this));
        Preferences.setScreenHeight(this, DisplayUtil.getScreenHeight(this));
        Preferences.setIsTable(this, DisplayUtil.isTablet(this));
        layout.post(new Runnable() {
            @Override
            public void run() {
                getStatusBarSize();
            }
        });
        
        Button login = (Button) findViewById(R.id.loginbutton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent def = new Intent(MainActivity.this,ActivityLogin.class);
                startActivity(def);
            }
        });

        checkUpdate();


    }

    @Override
    public ViewTopMenu getTopManu() {
        return null;
    }

    @Override
    public int getTopMenuPos() {
        return 0;
    }


    private void getStatusBarSize(){
        Rect rectgle= new Rect();
        Window window= getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int StatusBarHeight= rectgle.top;
        int contentViewTop=
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int TitleBarHeight= contentViewTop - StatusBarHeight;

        Preferences.setStatusBarHeight(this, StatusBarHeight);
    }



    private void checkUpdate() {

//        if(!checkNetworking(true)) return;
        showDialog();

        String url = ConstantsCommURL.getAppUrl(ConstantsCommURL.REQUEST_GET_MAXAPPVER);

        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("APPID", Const.APPID);

        AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_MAXAPPVER,
                builder.toString(),
                new AndroidNetworkRequest.ListenerAndroidResponse() {
                    @Override
                    public void success(String response) {
                        hideDialog1();

                        voAppVer = GlobalApplication.getGson().fromJson(response, VoAppVer.class);

                        if(!"Y".equals(voAppVer.getOPEN_YN())) return;

                        int versionCode = -1;
                        try {
                            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            versionCode = packageInfo.versionCode;
                        } catch (PackageManager.NameNotFoundException e) {
                            LogUtil.e(e.toString());
                        }

                        // 해당 모델만 내장플레이어로 재생. 나머지는 모두 FFMPEG 플레이어로 재생.


                        if(versionCode != -1 && versionCode < voAppVer.getAPPVER()) {

                            if("N".equals(voAppVer.getCHOICE_YN())) {
                                //필수 업데이트
//                                new CommDialog.Builder(IntroActivity.this)
//                                        .setDescribe(getResources().getString(R.string.dialog_update_check02))
//                                        .setBtns(new String[]{getResources().getString(R.string.dialog_end), getResources().getString(R.string.dialog_update)})
//                                        .setListener(new ListenerDialog() {
//                                            @Override
//                                            public void onClick(Dialog dialog, int result, Object object) {
//                                                if (ListenerDialog.DIALOG_BTN_FIRST == result) {
//                                                    dialog.dismiss();
//                                                    finish();
//                                                } else if (ListenerDialog.DIALOG_BTN_SECOND == result) {
//                                                    dialog.dismiss();
//                                                    checkPermissionForce();
//                                                }
//                                            }
//                                        }).setCancelable(false).build().show();
                            }else{
                                //선택 업데이트
//                                new CommDialog.Builder(IntroActivity.this)
//                                        .setDescribe(getResources().getString(R.string.dialog_update_check01))
//                                        .setBtns(new String[]{getResources().getString(R.string.dialog_later), getResources().getString(R.string.dialog_update)})
//                                        .setListener(new ListenerDialog() {
//                                            @Override
//                                            public void onClick(Dialog dialog, int result, Object object) {
//                                                if (ListenerDialog.DIALOG_BTN_FIRST == result) {
//                                                    dialog.dismiss();
//                                                    checkPermission();
//                                                } else if (ListenerDialog.DIALOG_BTN_SECOND == result) {
//                                                    dialog.dismiss();
//                                                    checkPermissionForce();
//                                                }
//                                            }
//                                        }).setCancelable(false).build().show();
                            }
                        } else{
//                            checkPermission();
                        }
                    }

                    @Override
                    public void systemcheck(String response) {
                        LogUtil.e("systemcheck : " + response);
                        hideDialog1();
                    }

                    @Override
                    public void fail(VoBase voBase) {
                        hideDialog1();
//                        showCommDialog(voBase.getRES_MSG());
                    }

                    @Override
                    public void exception(ANError error) {
                        LogUtil.e("exception : " + error.getMessage());
                        hideDialog1();
//                        showCommDialog(error.getErrorCode()+" : "+error.getErrorDetail());
                    }

                    @Override
                    public void dismissDialog() {
                        hideDialog1();
                    }
//                    @Override
//                    public void networkerror() {hideDialog();}
//
//
//                    @Override
//                    public void networkConnected() {
//                        //checkUpdate();
//                        //handler.sendEmptyMessage(1005);
//                    }
                });
    }






}


