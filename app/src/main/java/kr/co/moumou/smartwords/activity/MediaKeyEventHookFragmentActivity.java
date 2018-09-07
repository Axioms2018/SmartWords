package kr.co.moumou.smartwords.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import kr.co.moumou.smartwords.receiver.ReceiverMediaKeyEvent;
import kr.co.moumou.smartwords.util.AppUtil;

public class MediaKeyEventHookFragmentActivity extends FragmentActivity {
	
	// 2016 08 1st. 워터마크적용
	 @Override
	 protected void onStart() {
		 super.onStart();
	 }
	 
	private ReceiverMediaKeyEvent mMediaButtonReceiver = new ReceiverMediaKeyEvent();
	    private ComponentName receiverMediaKeyEventComponent;
	    private void setMediaKeyReceiver(){
	    	receiverMediaKeyEventComponent = new ComponentName(this, ReceiverMediaKeyEvent.class);
			AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			audioManager.registerMediaButtonEventReceiver(receiverMediaKeyEventComponent);
	    }
	    private void removeMediaKeyReceiver(){
			AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			audioManager.unregisterMediaButtonEventReceiver(receiverMediaKeyEventComponent);
	    }
		private void getHookKeyEvent(){
			IntentFilter mediaFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
			mediaFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY + 1);
			registerReceiver(mMediaButtonReceiver, mediaFilter);
		}

	ServiceReceiver serviceReceiver;
	
	private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {

            if (arg1.hasExtra("TIME")) {
            	String time = arg1.getStringExtra("TIME");
            	AppUtil.addScreenWaitTime(MediaKeyEventHookFragmentActivity.this, time);
            }
        }
    }
	
	@Override
	protected void onResume() {
		super.onResume();
        
        getHookKeyEvent();
        setMediaKeyReceiver();
	
//        serviceReceiver = new ServiceReceiver();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("SCREEN_WAIT_TIME");
//		registerReceiver(serviceReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
//		if (serviceReceiver != null)
//			unregisterReceiver(serviceReceiver);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
        unregisterReceiver(mMediaButtonReceiver);
        removeMediaKeyReceiver();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		AppUtil.setScreenTouchTime(this);
		
		return super.dispatchTouchEvent(ev);
	}
}
