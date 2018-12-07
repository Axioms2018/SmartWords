package kr.co.moumou.smartwords;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Window;
import android.webkit.MimeTypeMap;
import com.androidnetworking.error.ANError;

import java.io.File;

import kr.co.moumou.smartwords.activity.ActivityBase;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.Const;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.communication.GlobalApplication;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DownloadDialog;
import kr.co.moumou.smartwords.sign.ActivityLogin;
import kr.co.moumou.smartwords.sign.DoLogin;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.ToastUtil;
import kr.co.moumou.smartwords.vo.VoAppVer;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoDownload;
import kr.co.moumou.smartwords.vo.VoUserInfo;

public class MainActivity extends ActivityBase {

    private VoAppVer voAppVer;
    private boolean hasPemissionFileAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.lay_background);
        Preferences.setScreenWidth(this, DisplayUtil.getScreenWidth(this));
        Preferences.setScreenHeight(this, DisplayUtil.getScreenHeight(this));
        Preferences.setIsTable(this, DisplayUtil.isTablet(this));
        layout.post(new Runnable() {
            @Override
            public void run() {
                getStatusBarSize();
            }
        });

        getlogininfoAxioms();

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


    private void getlogininfoAxioms(){
        Intent intent = getIntent();


        Uri uri = intent.getData();
        if(uri != null) {
            String userid = uri.getQueryParameter("userid");
            String userpwd = uri.getQueryParameter("userpwd");

            Preferences.put(MainActivity.this,Preferences.PREF_USER_ID,userid);
            Preferences.put(MainActivity.this,Preferences.PREF_USER_PW,userpwd);

            if(!(Preferences.getPref(this, Preferences.PREF_USER_PW, null) == null) && !(Preferences.getPref(this, Preferences.PREF_USER_ID, null) == null)){
                Preferences.put(this,Preferences.PREF_IS_MEMBER,true);
            }

//        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            }
        }
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

                        //"http://install.moumou.co.kr/axioms/axiom_beta_v0.0.5.apk"
                        voAppVer = GlobalApplication.getGson().fromJson(response, VoAppVer.class);
                        voAppVer.setAPPURL("http://install.moumou.co.kr/axioms/axiom_beta_v0.0.5.apk");
                        voAppVer.setFILENAME("axiom_beta_v0.0.5.apk");

                        if(!"Y".equals(voAppVer.getOPEN_YN())) return;

                        int versionCode = -1;
                        try {
                            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            versionCode = packageInfo.versionCode;
                        } catch (PackageManager.NameNotFoundException e) {
                            LogUtil.e(e.toString());
                        }

                        if(versionCode != -1 && versionCode < voAppVer.getAPPVER()) {

                            if("N".equals(voAppVer.getCHOICE_YN())) {
                                //필수 업데이트
                                final DialogStudent mDialog = new DialogStudent(MainActivity.this);

                                mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
                                mDialog.show();
                                mDialog.setButtonMsg("종료", "업데이트");
                                mDialog.setMessage("최신 버전의 파일이 있습니다.\n" +
                                        "        업데이트 후 사용하실 수 있으며, 업데이트를 진행하지 않을 경우 앱을 사용하실 수 없습니다.?");
                                mDialog.setCancelable(false);
                                mDialog.setCanceledOnTouchOutside(false);
                                mDialog.setListener(new DialogStudent.ListenerDialogButton() {
                                    @Override
                                    public void onClick(Dialog dialog, int result) {
                                        if(result == DIALOG_BTN_ON) {
                                            mDialog.dismiss();
                                            finish();
                                        }else{
                                            mDialog.dismiss();
                                            checkPermissionForce();

                                        }
                                    }
                                });


                            }else{
                                //선택 업데이트
                                final DialogStudent mDialog = new DialogStudent(MainActivity.this);

                                mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
                                mDialog.show();
                                mDialog.setButtonMsg("종료", "업데이트");
                                mDialog.setMessage("최신 버전의 파일이 있습니다.\n지금 업데이트 하시겠습니까?");
                                mDialog.setCancelable(false);
                                mDialog.setCanceledOnTouchOutside(true);
                                mDialog.setListener(new DialogStudent.ListenerDialogButton() {
                                    @Override
                                    public void onClick(Dialog dialog, int result) {
                                        if(result == DIALOG_BTN_ON) {
                                            mDialog.dismiss();
                                            checkPermission();
                                        }else{
                                            mDialog.dismiss();
                                            checkPermissionForce();
                                        }
                                    }
                                });
                            }
                        } else{
                            checkPermission();
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

    private void apiLogin() {
        LogUtil.d("start apiLogin");

//        if(!checkNetworking(false)) return;

        LogUtil.d("pw : " + Preferences.getPref(this, Preferences.PREF_USER_PW, null));
        LogUtil.d("is_member : " + Preferences.getPref(this, Preferences.PREF_IS_MEMBER, null));

        if(Preferences.getPref(this, Preferences.PREF_USER_PW, null) == null ||
                !Preferences.getPref(this, Preferences.PREF_IS_MEMBER, false)){

            Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return;
        }



        LogUtil.d("showDialog()");

        //showDialog();

        new DoLogin(this, new DoLogin.DoLoginListener() {

            @Override
            public void failLogin() {
//                hideDialog();

                //Preferences.putPref(IntroActivity.this, Preferences.PREF_USER_ID, null);
                Preferences.putPref(MainActivity.this, Preferences.PREF_USER_PW, null);
                Preferences.putPref(MainActivity.this, Preferences.PREF_IS_MEMBER, false);

                VoUserInfo.getInstance().clear();

                Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //ToastUtil.show(IntroActivity.this, getResources().getString(R.string.dialog_certi_fail));
            }

            @Override
            public void successLogin(String response) {
//                hideDialog();

//                Preferences.putPref(IntroActivity.this, Preferences.PREF_USER_ID, null);
//                Preferences.putPref(IntroActivity.this, Preferences.PREF_USER_PW, pw);
//                Preferences.putPref(IntroActivity.this, Preferences.PREF_IS_MEMBER, true);

//                if (hasPemissionFileAccess)
//                    deleteApkFiles();

                Intent intent = new Intent(MainActivity.this, ActivityWordTestMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, Preferences.getPref(this, Preferences.PREF_USER_ID, null),
                Preferences.getPref(this, Preferences.PREF_USER_PW, null)).run();

    }


    private void downloadAPKFIle(String fileName, String fileUrl) {

        deleteApkFiles();

        final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileName;

        VoDownload voDownload = new VoDownload();
        voDownload.setFILENAME(fileName);
        voDownload.setURL(fileUrl);
        voDownload.setTAG(fileName);

        DownloadDialog dialog = new DownloadDialog(this, new DownloadDialog.DownloadDialogListener() {

            @Override
            public void onDownloadComplete() {
                LogUtil.i("다운로드완료 APK파일 설치");
                installApk(filePath);
            }
        }, voDownload);


        dialog.show();
        dialog.downloadAPKFile();



        //지워야댐
        //dialog.dismiss();
    }

    private void installApk(String url){

        File toInstall = new File(url);

        LogUtil.d("toInstall exist : " + toInstall.exists());
        LogUtil.d("toInstall url : " + url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, Const.RESULT_APK_INSTALL);

        } else {

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = toInstall.getName().substring(toInstall.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);

            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, Const.RESULT_APK_INSTALL);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Const.RESULT_APK_INSTALL) {
            if(resultCode == RESULT_CANCELED) {
                LogUtil.d("취소");
                finish();
            }else{
                LogUtil.d("설치완료");
            }
        }
    }

    private void deleteApkFiles(){
        File files = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        LogUtil.i("deleteApkFiles path : "  + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        LogUtil.i("deleteApkFiles files : "  + (files == null));
        LogUtil.i("files.listFiles files : "  + (files.listFiles() == null));
        if(files == null || !files.exists()) {
            return;
        }
        if(files.listFiles().length > 0){
            for(File file : files.listFiles()){
                if((file.getName().startsWith(Const.APKFILENAME) && file.getName().endsWith("apk"))) {
                    file.delete();
                }
            }
        }
    }


    private void checkPermissionForce() {
        ///권한 체크
        String[] permissions = new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        boolean isPermission = checkPermission1(permissions, ActivityBase.PERMISSIONS_REQUEST_PERMISSION_FORCE);

        LogUtil.w("checkPermission : " + isPermission);

        if(isPermission) {
            downloadAPKFIle(voAppVer.getFILENAME(), voAppVer.getAPPURL());
        }
    }


    private void checkPermission() {
        ///권한 체크
        String[] permissions = new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isPermission = checkPermission1(permissions, ActivityBase.PERMISSIONS_REQUEST_PERMISSION);

        LogUtil.w("checkPermission : " + isPermission);

        if(isPermission) {
//            deleteApkFiles();
            apiLogin();
        }
    }

    public boolean checkPermission1(final String[] permissions, final int requestPermission) {

        int permissionCheck = 0;

        for(String permission : permissions) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if(permissionCheck == PackageManager.PERMISSION_DENIED) break;
        }
        LogUtil.i("permissionCheck : " + permissionCheck);

        //권한없음
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            LogUtil.i("PERMISSION_DENIED : " + PackageManager.PERMISSION_DENIED);

            final DialogStudent mDialog = new DialogStudent(MainActivity.this);
            mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
            mDialog.show();
            mDialog.setButtonMsg("확인");
            mDialog.setTitle("권한요청");
            mDialog.setMessage("요청하신 기능을 실행하려면 아래의 접근 권한 허용이 필요합니다.");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnCancelListener(onCancelListener);
            mDialog.setListener(new DialogStudent.ListenerDialogButton() {
                @Override
                public void onClick(Dialog dialog, int result) {
                    if (result == DialogStudent.ListenerDialogButton.DIALOG_BTN_ON) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, requestPermission);
                        dialog.dismiss();
                    }
                }
            });
            return false;
        }else{
            return true;

        }
    }


    public DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            LogUtil.i("permission Dialog onCancel");
            //다음으로
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        LogUtil.i("requestCode : " + requestCode);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_PERMISSION:

                for (int idx=0; idx<permissions.length; idx++) {
                    if (permissions[idx].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[idx] == PackageManager.PERMISSION_GRANTED) {
                        hasPemissionFileAccess = true;
                        break;
                    }
                }

                apiLogin();

                break;
            case PERMISSIONS_REQUEST_PERMISSION_FORCE:
                for (int idx=0; idx<permissions.length; idx++) {
                    if (permissions[idx].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[idx] != PackageManager.PERMISSION_GRANTED) {
                        ToastUtil.show(MainActivity.this, "파일 액세스 권한을 허용해 주세요.");
                        checkPermissionForce();
                        return;
                    }
                }

                downloadAPKFIle(voAppVer.getFILENAME(), voAppVer.getAPPURL());

                break;
            case PERMISSIONS_REQUEST_CHECK_EXIST:

                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    deleteApkFiles();
                }

                break;
        }
    }

    private DialogInterface.OnCancelListener permissionCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            LogUtil.i("permission Dialog onCancel");

//            checkUpdate();
//            deleteApkFiles();
//            apiLogin();
        }
    };



}


