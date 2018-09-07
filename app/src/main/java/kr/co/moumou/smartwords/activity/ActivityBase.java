package kr.co.moumou.smartwords.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.DecimalFormat;

import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.DialogLoding;
import kr.co.moumou.smartwords.communication.MouMouDialog;
import kr.co.moumou.smartwords.communication.MouMouDialog.DialogButtonListener;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.util.AppUtil;
import kr.co.moumou.smartwords.util.NetworkState;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.common.Constant;


public abstract class ActivityBase extends Activity {

	private DialogLoding progress;
	private boolean isBack = false;
	public static final String BROADCAST_ACTION_WRING = "com.mm.waring";
	public static final int MM_WARING_NETWORK_NOT_AVAILABLE = 4;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
	}

	ServiceReceiver serviceReceiver;
	
	// 2016 08 1st. 워터마크적용
	@Override
	protected void onStart() {
		super.onStart();
		
//		AppUtil.addWaterMarkView(this);
		
	}

	private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {

            if (arg1.hasExtra("TIME")) {
            	String time = arg1.getStringExtra("TIME");
            	AppUtil.addScreenWaitTime(ActivityBase.this, time);
            }
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		this.overridePendingTransition(0,0);

		ViewTopMenu topMenu = getTopManu();
		if(topMenu != null){
			topMenu.setSelectedTab(getTopMenuPos());
		}
		IntentFilter completeFilter = new IntentFilter(BROADCAST_ACTION_WRING);
		registerReceiver(waringReceiver, completeFilter);
		
//		serviceReceiver = new ServiceReceiver();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("SCREEN_WAIT_TIME");
//		registerReceiver(serviceReceiver, intentFilter);
	}

	private BroadcastReceiver waringReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_ACTION_WRING.equals(intent.getAction())) {
				showNetworkNotAvailablePopup(intent.getStringExtra("msg"));
			}
		}
	};

	private MouMouDialog dialog;
	public void showNetworkNotAvailablePopup(final String msg){
		ActivityBase.this.runOnUiThread(new Runnable() {
			public void run() {

				if(dialog != null && dialog.isShowing()){
					return;
				}

				new NetworkChecker().start();
				dialog = new MouMouDialog(ActivityBase.this);
				dialog.setSize(0.5, 0.4);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.show();
				dialog.setDescriptGravity(Gravity.CENTER);
				dialog.setButtonText(getString(R.string.network_setting), getString(R.string.terminate));
				if(StringUtil.isNull(msg)){
					dialog.setDescript(getString(R.string.warning_not_available_network));	
				}else{
					dialog.setDescript(msg);
				}

				dialog.setListener(new DialogButtonListener() {

					@Override
					public void onClick(Dialog dialog, int result) {
						switch (result) {
						case DialogButtonListener.DIALOG_BTN_FIRST:
							launchWifiSetting();
							break;
						case DialogButtonListener.DIALOG_BTN_SECOND:
//							terminateApplication();
							break;

						default:
							break;
						}
						dialog.dismiss();
					}
				});
			}
		});
	}
	
	private void setNetworkAvailable(){
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
			onResume();
		}
	}

	private class NetworkChecker extends Thread {
		boolean isRun = true;
		@Override
		public void run() {
			while (isRun) {
				if(NetworkState.getInstance().isNetworkConnected(ActivityBase.this)){
					setNetworkAvailable();
					isRun = false;
				}else{
					try {
						sleep(300);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	private void launchWifiSetting(){
		Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
//
//	private void terminateApplication(){
//		Intent intent = new Intent(ActivityBase.this, ActivityLogin.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra(Constant.IntentKeys.INTENT_TERMINATE_APP, true);
//		startActivity(intent);
//		finish();
//	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			isBack = false;
		}
	};

	public void showToast(final String msg){
		ActivityBase.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ActivityBase.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void showToast(final int msgID){
		ActivityBase.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ActivityBase.this, msgID, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void activityFinish(){
		this.finish();
	}

	public void showLoadingProgress(final String msg){
		
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progress != null && progress.isShowing()){
					return;
				}

				progress = new DialogLoding(ActivityBase.this, true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						activityFinish();
					}
				});
				progress.show();
				progress.setText(msg);
			}
		});
	}

	public void showLoadingProgress(final int resID){
		
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progress != null && progress.isShowing()){
					return;
				}

				progress = new DialogLoding(ActivityBase.this, true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						activityFinish();
					}
				});
				progress.show();
				progress.setText(getString(resID));
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(waringReceiver);
		
//		if (serviceReceiver != null)
//			unregisterReceiver(serviceReceiver);
	}

	@Override
	protected void onDestroy() {
		hideProgress();
		hideDialog();
		super.onDestroy();
	}
	
	protected void doubleLoginDetected() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Constant.IntentKeys.INTENT_DOUBLE_LOGIN, true);
		startActivity(intent);
	}
	
	private void hideDialog(){
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
	}

	public void hideProgress(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progress == null){
					return;
				}

				if(progress.isShowing()){
					progress.dismiss();
				}
			}
		});
	}

	public void recycleView(View view) {

		if(view != null) {
			Drawable bg = view.getBackground();
			if(bg != null) {
				if(((BitmapDrawable)bg).getBitmap() != null){
					bg.setCallback(null);	
					((BitmapDrawable)bg).getBitmap().recycle();
				}
				view.setBackgroundDrawable(null);
			}	
		}
	}
	private DecimalFormat format = new DecimalFormat("00");
	public String getChaci(int chaci){
		return format.format(chaci);
	}

	public String getChaci(String chaci){
		String result = null;
		try {
			result = format.format(Integer.parseInt(chaci));
		} catch (Exception e) {
			chaci = chaci.replace(getString(R.string.chapter), "");
			result = format.format(Integer.parseInt(chaci.trim()));
		}
		return result;
	}

	public abstract ViewTopMenu getTopManu();
	public abstract int getTopMenuPos();

//	public ViewSlidingMenuMM getMenu(){
//		return null;
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//키보드 TAB 키로 focus 이동 후 Enter로 실행제어 하기 위함(로그아웃 등 이벤트 처리 됨)
		if(keyCode == KeyEvent.KEYCODE_TAB){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//키보드 TAB 키로 focus 이동 후 Enter로 실행제어 하기 위함(로그아웃 등 이벤트 처리 됨)
		if(keyCode == KeyEvent.KEYCODE_TAB){
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		AppUtil.setScreenTouchTime(this);
		
		return super.dispatchTouchEvent(ev);
	}
	
	
}
