package kr.co.moumou.smartwords.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
//import kr.co.moumou.smartwords.activity.ActivityWordsDownload;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
//import kr.co.moumou.smartwords.dao.DaoStudyProgress;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoSocketComm;

public class AppUtil {
    public static void startActivity(Activity act, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
    }

    public static void startActivity(Context act, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(intent);
    }
//
//	public static boolean checkRunningService(Context paramContext,
//			String paramString) {
//		List info = ((ActivityManager) paramContext
//				.getSystemService("activity")).getRunningServices(200);
//		for (Iterator localIterator = info.iterator(); localIterator.hasNext();) {
//			if (((ActivityManager.RunningServiceInfo) localIterator.next()).service
//					.getClassName().equalsIgnoreCase(paramString))
//				return true;
//		}
//		return false;
//	}

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static void showKeyboard(final Context context, final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
//            	InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                LogUtil.d("context : " + context);
                if (context == null)
                    return;

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static DisplayImageOptions getImageCacheOption() {
        return getImageCacheOption(0, 0);
    }

    public static DisplayImageOptions getImageCacheOption(int round) {
        return getImageCacheOption(round, 0);
    }

    public static DisplayImageOptions getImageCacheOption(int round, int fadeInTime) {
        return new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.ic_stub)
                .showImageOnLoading(android.R.color.white)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(round))
                .displayer(new FadeInBitmapDisplayer(fadeInTime))
                .build();

//    	return new DisplayImageOptions.Builder()
//		.showImageForEmptyUri(R.drawable.ic_empty)
//		.showImageOnFail(R.drawable.ic_error)
//		.resetViewBeforeLoading(true)
//		.cacheOnDisk(true)
//		.imageScaleType(ImageScaleType.EXACTLY)
//		.bitmapConfig(Bitmap.Config.RGB_565)
//		.considerExifParams(true)
//		.displayer(new FadeInBitmapDisplayer(300))
//		.build();

//    	DisplayImageOptions options = new DisplayImageOptions.Builder()
//        .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//        .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//        .showImageOnFail(R.drawable.ic_error) // resource or drawable
//        .resetViewBeforeLoading(false)  // default
//        .delayBeforeLoading(1000)
//        .cacheInMemory(false) // default
//        .cacheOnDisc(false) // default
//        .preProcessor(...)
//        .postProcessor(...)
//        .extraForDownloader(...)
//        .considerExifParams(false) // default
//        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//        .decodingOptions(...)
//        .displayer(new SimpleBitmapDisplayer()) // default
//        .handler(new Handler()) // default
//        .build();
    }

    public static DisplayImageOptions getImageCacheOptionDefaultImg(int round, int defaultImg) {
        return new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.ic_stub)
                .showImageOnLoading(android.R.color.white)
                .showImageForEmptyUri(defaultImg)
                .showImageOnFail(defaultImg)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(round))
                .displayer(new FadeInBitmapDisplayer(0))
                .build();
    }

    public static DisplayImageOptions getImageOption() {
        return new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.ic_stub)
                .showImageOnLoading(android.R.color.white)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0))
                .displayer(new FadeInBitmapDisplayer(0))
                .build();

    }

    public static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    public static void clearApplicationData(Context context) {
        clearServerData(context);
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (s.equals("shared_prefs")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static void clearServerData(Context context) {
//        DaoStudyProgress.getInstance(context).clearDB();
    }

    public static void errRestartApp() {
        errRestartApp(null);
    }

    public static void errRestartApp(final Context context) {

        if (context != null) {
            //토스트는 ui쓰레드서 명시적으로 띄워줘야 한다.
            new Thread() {
                @Override
                public void run() {
                    // UI쓰레드에서 토스트 뿌림
                    Looper.prepare();
                    Toast.makeText(context, context.getString(R.string.big_error_3), Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
            }.start();

            // 쓰레드 잠깐 쉼
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                LogTraceMin.EW(e);
            }

            Intent intent = new Intent(context, ActivityWordTestMain.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);

    }

    public static int getVersionCode(final Context context) {
        int versionCode = 0;

        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static String getVersionName(final Context context) {
        String versionName = "";

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    private static TimerTask mTask;
    private static Timer mTimer;
    private static int runTime = 0;

    /**
     * 타이머
     */
    public static void startTimer() {

        mTask = new TimerTask() {

            @Override
            public void run() {
                //LogTraceMin.I("runTime :: " + runTime);
                runTime++;
                VoSocketComm.getInstance().setParams(ConstantsCommParameter.Keys.RUN_TM, runTime);
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 1000);
    }

    public static void stopTimer() {
        mTimer.cancel();
        mTask.cancel();

        mTimer = null;
        mTask = null;

        runTime = 0;
        VoSocketComm.getInstance().setParams(ConstantsCommParameter.Keys.RUN_TM, 0);
    }

    // 2016 08 1st. 워터마크적용
//    public static void addWaterMarkView(Context context) {
//
//        try {
//            String strUserGB = VoMyInfo.getInstance().getUSERGB();
//
//            if (StringUtil.isNull(strUserGB))
//                return;
//
//            int userGB = Integer.parseInt(VoMyInfo.getInstance().getUSERGB());
//
//            if (Constant.USER_TYPE_DIRCTOR == userGB || Constant.USER_TYPE_TICHER == userGB) {
//
//                Activity act = (Activity) context;
//
//                if (act instanceof ActivityIntro || act instanceof ActivityLogin || act instanceof ActivityAnswerView ||
//                        act instanceof ActivityFileDownload || act instanceof ActivityWordsDownload || act instanceof ActivityNetworkCheckDialog ||
//                        act instanceof ActivityFindID || act instanceof ActivityAddAccount)
//                    return;
//
//                View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.always_on_top_view_touch, null);
//                ViewGroup viewGroup = (ViewGroup) ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
//
//                if (viewGroup == null)
//                    return;
//
//                boolean isExist = false;
//                for (int i = 0; i < viewGroup.getChildCount(); i++) {
//                    View child = viewGroup.getChildAt(i);
//                    Object tag = child.getTag();
//                    if (tag == null)
//                        continue;
//
//                    if (tag instanceof String) {
//                        String tagNo = (String) tag;
//                        if ("777".equals(tagNo)) {
//                            isExist = true;
//                            break;
//                        }
//
//                    }
//                }
//
//                if (isExist)
//                    return;
//
//                //    	TextView tvClassName = (TextView) view.findViewById(R.id.tvClassName);
//                //    	tvClassName.setText(context.getClass().getSimpleName());
//                viewGroup.addView(view);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void addScreenWaitTime(Context context) {

        try {
            String strUserGB = VoMyInfo.getInstance().getUSERGB();

            if (StringUtil.isNull(strUserGB))
                return;

            int userGB = Integer.parseInt(VoMyInfo.getInstance().getUSERGB());


            Activity act = (Activity) context;

            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.touch_time_view, null);
            ViewGroup viewGroup = (ViewGroup) ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);

            if (viewGroup == null)
                return;

            boolean isExist = false;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                Object tag = child.getTag();
                if (tag == null)
                    continue;

                if (tag instanceof String) {
                    String tagNo = (String) tag;
                    if ("888".equals(tagNo)) {
                        isExist = true;
                    }
                }
            }

            if (isExist)
                return;

            //    	TextView tvClassName = (TextView) view.findViewById(R.id.tvClassName);
            //    	tvClassName.setText(context.getClass().getSimpleName());
            viewGroup.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addScreenWaitTime(Context context, String time) {

        try {
            String strUserGB = VoMyInfo.getInstance().getUSERGB();

            if (StringUtil.isNull(strUserGB))
                return;

            int userGB = Integer.parseInt(VoMyInfo.getInstance().getUSERGB());


            Activity act = (Activity) context;

            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.touch_time_view, null);
            ViewGroup viewGroup = (ViewGroup) ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);

            if (viewGroup == null)
                return;

            boolean isExist = false;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                Object tag = child.getTag();
                if (tag == null)
                    continue;

                if (tag instanceof String) {
                    String tagNo = (String) tag;
                    if ("777".equals(tagNo)) {
                        ((TextView) ((RelativeLayout) child).getChildAt(0)).setText(time);
                        isExist = true;
                    }
                }
            }

            if (isExist)
                return;

            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvTime.setText(time);

            //    	TextView tvClassName = (TextView) view.findViewById(R.id.tvClassName);
            //    	tvClassName.setText(context.getClass().getSimpleName());
            viewGroup.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MediaPlayer mediaPlayer;

    public static void playSound(Context context, String fileName) {
        if (fileName == null) {
            return;
        }

        String pathSound = "android.resource://kr.co.moumou.smartwords.raw/" + fileName;
        LogUtil.d("pathSound : " + pathSound);

        try {
            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer();
            else {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();

                mediaPlayer.reset();
            }

            Uri mUri = Uri.parse(pathSound);
            mediaPlayer.setDataSource(context, mUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

//			mediaPlayer.start();
        } catch (Exception e) {
            ToastUtil.show(context, context.getString(R.string.unable_play_file));
        }
    }

    public static void setScreenTouchTime(Context context) {

        LogUtil.d("ljh setScreenTouchTime");
//
//        if (!ApplicationPool.IS_STUDY_ROOM_MODE)
//            return;

        SharedPrefData.setLongSharedData(context, SharedPrefData.SHARED_SCREEN_TOUCH_TIME_L, SystemClock.elapsedRealtime());
    }

    public static Boolean isServiceRunning(Context context, String serviceName) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (RunningServiceInfo runningServiceInfo : activityManager
                .getRunningServices(Integer.MAX_VALUE)) {

            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }

        }

        return false;
    }
}
