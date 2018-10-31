package kr.co.moumou.smartwords.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;

import java.io.IOException;
import java.util.ArrayList;

import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.communication.GlobalApplication;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.customview.ViewWordsLevel;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoUserInfo;
import kr.co.moumou.smartwords.vo.VoWordsLevelList;
import kr.co.moumou.smartwords.vo.VoWordsLevelList.VoWordsLevel;
import kr.co.moumou.smartwords.common.ApplicationPool;

public class ActivityWordTestMain extends ActivityBase {

	public static ArrayList<Activity> actList = new ArrayList<Activity>();
	
	private final int WORDTEST_LEVEL_SELECT = 1;
	private ViewTopMenu view_top_menu;
//	private StudyDataComm studyDataComm;
	
	private LinearLayout ll_low_level, ll_mid_level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		actList.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordtest_main);


		boolean isMember = Preferences.getPref(ActivityWordTestMain.this, Preferences.PREF_IS_MEMBER, false);

//		getTestData();
		
		if(ConstantsCommURL.ISTEST) {
			findViewById(R.id.iv_logo).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isAdmin) {
						VoMyInfo.getInstance().setUSERGB("08");
						Toast.makeText(ActivityWordTestMain.this, "관리자 모드로 변경", Toast.LENGTH_SHORT).show();
						return;
					}
					
					isAdmin = true;
					testhandler.sendEmptyMessageDelayed(0, 1000);
					return;
				}
			});
		}
		
		view_top_menu = (ViewTopMenu)findViewById(R.id.view_top_menu);
		view_top_menu.setActivity(this);
		
		DisplayUtil.setLayoutHeight(this, 67, view_top_menu);
		
		ll_low_level = (LinearLayout) findViewById(R.id.ll_low_level);
		ll_mid_level = (LinearLayout) findViewById(R.id.ll_mid_level);
		
		DisplayUtil.setLayout(this, 388, 244, ll_low_level);
		DisplayUtil.setLayout(this, 538, 244, ll_mid_level);
		DisplayUtil.setLayoutPadding(this, 20, 0, 20, 0, ll_low_level);
		DisplayUtil.setLayoutPadding(this, 20, 0, 20, 0, ll_mid_level);
		
		ImageView iv_logo = (ImageView) findViewById(R.id.iv_logo);
		DisplayUtil.setLayout(this, 706, 95, iv_logo);
		DisplayUtil.setLayoutMargin(this, 0, 0, 0, 54, iv_logo);
		
		LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
		DisplayUtil.setLayoutPadding(this, 0, 0, 0, 70, ll_main);
//		requestMyInfo();
		reqWordsLevelinfo1();
	}


	private String getTestData() {
		String msg = null;
		try {
			msg = StringUtil.getData(this, "UserInfo.json");
			VoMyInfo voMyInfo = ApplicationPool.getGson().fromJson(msg, VoMyInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return msg;
	}
	
	boolean isAdmin = false;
	
	private Handler testhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			isAdmin = false;
		}
	};

	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		AppUtil.stopTimer();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogTraceMin.I("resume");
	}
	
	@Override
	public ViewTopMenu getTopManu() {
		return view_top_menu;
	}

	@Override
	public int getTopMenuPos() {
		return 0;
	}

	private void reqWordsLevelinfo1(){

		showLoadingProgress(getResources().getString(R.string.wait_for_data));


		String userId = Preferences.getPref(this, Preferences.PREF_USER_ID, null);

//		Uri.Builder builder = Uri.parse(ConstantsCommURL.getWordUrl("GetLevelInfo")).buildUpon();

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_GETLEVELINFO);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, userId);

		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_GETLEVELINFO, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();

//				GlobalApplication.getGson().fromJson(response, VoUserInfo.class);
//				ApplicationPool.getGson().fromJson(response, VoUserInfo.class);
//				mUserInfo= VoUserInfo.getInstance();

				VoWordsLevelList levelList = ApplicationPool.getGson().fromJson(response, VoWordsLevelList.class);
				addWordsLevel(levelList);


			}

			@Override
			public void systemcheck(String response) {
				hideProgress();
			}

			@Override
			public void fail(VoBase base) {
				hideProgress();
			}

			@Override
			public void exception(ANError error) {
				hideProgress();
			}

			@Override
			public void dismissDialog() {
				hideProgress();
			}
		});
	}
	private void addWordsLevel(VoWordsLevelList levelList) {
		
		ArrayList<VoWordsLevel> data = levelList.getLEVELSTDLIST();
		
		ll_low_level.removeAllViews();
		ll_mid_level.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		
		for(VoWordsLevel voLevel : data) {
			
			ViewWordsLevel view = new ViewWordsLevel(this);
			view.setLayoutParams(params);
			view.setActivity(this);
			view.setData(voLevel);
			
			if(voLevel.getGUBUN().equals(VoWordsLevel.LEVEL_LOW)){
				ll_low_level.addView(view);
			}else{
				ll_mid_level.addView(view);
			}
		}		
	}
	
	public void selectedLevel(String level, String type) {

		Intent intent = new Intent(this, ActivityWordTestDaySelect.class);
		intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, level);
		intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, type);
		startActivityForResult(intent, WORDTEST_LEVEL_SELECT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == WORDTEST_LEVEL_SELECT) {
			LogTraceMin.I("정보 재실행");
//			requestMyInfo();
			reqWordsLevelinfo1();
		}
		
	}

	private void requestMyInfo() {

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_USERINFO);
		String tag = ConstantsCommURL.REQUEST_TAG_USERINFO;

		String userId = Preferences.getPref(this, Preferences.PREF_USER_ID, null);

		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("SID",  VoUserInfo.getInstance().getSID());
		builder.appendQueryParameter("USERID",  userId);


		AndroidNetworkRequest.getInstance(this).StringRequest(tag, builder.toString(),
				new AndroidNetworkRequest.ListenerAndroidResponse() {
					@Override
					public void success(String response) {

						ApplicationPool.getGson().fromJson(response, VoUserInfo.class);

					}

					@Override
					public void systemcheck(String response) {}

	   				@Override
					public void fail(VoBase voBase) {}

					@Override
					public void exception(ANError error) {}

					@Override
					public void dismissDialog() {}

				});
	}



}
