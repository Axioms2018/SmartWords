package kr.co.moumou.smartwords.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.widget.Toast;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.DialogLoding;
import kr.co.moumou.smartwords.customview.ViewMyWordsTopMenu;
import kr.co.moumou.smartwords.customview.ViewMyWordsTopMenu.MenuClickListener;
import kr.co.moumou.smartwords.util.AppUtil;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;

public class ActivityMywordsMain extends FragmentActivity implements MenuClickListener {
	
	private ViewMyWordsTopMenu view_top_menu;
	
//	private ViewSlidingMenuMM view_sliding_menu;
	
	private Fragment fr = null;
	private DialogLoding progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordsmy_main);
		// 2016 08 1st. 워터마크적용
		view_top_menu = (ViewMyWordsTopMenu) findViewById(R.id.view_top_menu);
		view_top_menu.setActivity(this);
		view_top_menu.setListener(this);
		
		/*view_sliding_menu = (ViewSlidingMenuMM) findViewById(R.id.view_sliding_menu);
		view_sliding_menu.showErrorReportButton();
		view_sliding_menu.setListener(menuClickListener);*/
		
		setFragment();
        
        DisplayUtil.setLayoutHeight(this, 67, view_top_menu);        
	}
	
	private void init() {
		fr = new FragmentMyReport();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.ll_frag, fr);
		fragmentTransaction.commit();
	}
        
	@Override
	public void onClick(int index) {
		
		if(index == 0) {
			fr = new FragmentMyReport();
		} else if(index == 1) {
			fr = new FragmentMyNote();
		} else if(index == 2) {
			fr = new FragmentMyCal();
		} else if(index == 3) {
			fr = new FragmentMyRank();
		}

		setFragment();
	}
	
	private void setFragment() {
		
		if(fr == null) {
			init();
			return;
		}
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.ll_frag, fr);
		fragmentTransaction.commit();
	}
	
	private void activityFinish(){
		this.finish();
	}
	
	public void showToast(final String msg){
		ActivityMywordsMain.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ActivityMywordsMain.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void showLoadingProgress(String msg){
		if(progress == null){
			progress = new DialogLoding(this, true, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					activityFinish();
				}
			});
			progress.show();
			progress.setText(msg);
		}else{
			try {
				progress.show();
			} catch (Exception e) {
				LogUtil.e(getClass().getSimpleName() + "[showLoadingProgress()] (Exception : " + e.getMessage() + ")");
			}
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
	
	/*private OnMenuItemClickListener menuClickListener = new OnMenuItemClickListener() {

        @Override
        public void onClick(int type, int value) {
            if(type == OnMenuItemClickListener.TYPE_ERROR_REPORT){
                view_sliding_menu.showErrorReportDialog(ActivityMywordsMain.this, getErrorReportInfo());
            }
        }

        @Override
        public void exception(String msg) {

        }
    };
    
    private HashMap<String, Object> getErrorReportInfo(){
        HashMap<String, Object> bookInfo = new HashMap<String, Object>();
		bookInfo.put(DialogReport.KEY_SYSGB, ConstantsCommParameter.Values.SYSBG_MOUMOU_WORDS);
		bookInfo.put(DialogReport.KEY_USERID, VoMyInfo.getInstance().getUSERID());
		bookInfo.put(DialogReport.KEY_VIEW, findViewById(R.id.lay_base));
		bookInfo.put(DialogReport.KEY_IS_LIVE, true);
		bookInfo.put(DialogReport.KEY_PCODE, "99999999");
		bookInfo.put(DialogReport.KEY_CHACI, "00");
		bookInfo.put(DialogReport.KEY_PSNAME, "기타");
		bookInfo.put(DialogReport.KEY_COMPNAME, VoMyInfo.getInstance().getCOMPNAME());
		bookInfo.put(DialogReport.KEY_STDGUBUN, DialogReport.STDGUBUN.WORDTEST.code());
        bookInfo.put(DialogReport.KEY_IS_WORKBOOK, "N");

        return bookInfo;
    }*/
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		AppUtil.setScreenTouchTime(this);
		
		return super.dispatchTouchEvent(ev);
	}

	ServiceReceiver serviceReceiver;
	
	private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {

            if (arg1.hasExtra("TIME")) {
            	String time = arg1.getStringExtra("TIME");
            	AppUtil.addScreenWaitTime(ActivityMywordsMain.this, time);
            }
        }
    }
	
	@Override
	protected void onResume() {
		super.onResume();
	
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
}