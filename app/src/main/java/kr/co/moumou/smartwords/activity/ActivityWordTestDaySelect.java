package kr.co.moumou.smartwords.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.dialog.MouMouDialog;
import kr.co.moumou.smartwords.customview.ViewDay;
import kr.co.moumou.smartwords.customview.ViewIndicator;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.dao.WordTestDownloadDao;
import kr.co.moumou.smartwords.activity.SaveWords.SaveWordsComplete;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.util.ToastUtil;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoCertificate;
import kr.co.moumou.smartwords.vo.VoFileWordsDownload;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoUserInfo;
import kr.co.moumou.smartwords.vo.VoWordsDayList;
import kr.co.moumou.smartwords.vo.VoWordsDayList.VoDay;
import kr.co.moumou.smartwords.vo.VoWordsTestDownload;
import kr.co.moumou.smartwords.vo.VoWordsTestList;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DialogStudent.ListenerDialogButton;
import kr.co.moumou.smartwords.dialog.DialogWordsComp;

public class ActivityWordTestDaySelect extends ActivityBase {
	
	public static final String PARAM_WORDTEST_LEVEL = "wordtest_level";
	protected static final String PARAM_WORDTEST_LEVEL_TYPE = "wordtest_level_type";
	public static final String PARAM_WORDTEST_STDY_GUBN = "wordtest_stdy_gubn";
	public static final String PARAM_WORDTEST_STDY_W_GUBN = "wordtest_stdy_w_gubn";
	//protected static final String PARAM_ISREVIEW = "wordtest_is_review";
	protected static final int CHANT_FILE_DOWNLOAD_RESULT = 2;
	protected static final int QUIZ_FILE_DOWNLOAD_RESULT = 3;
	protected static final int INT_QUIZ_OPEN_NUMBER = 5;
	
	protected static final String C_PARAM_DAY = "day";
	protected static final String C_PARAM_ISSTUDY = "isStudy";
	protected static final String C_PARAM_STD_GB = "std_gb";
	protected static final String C_PARAM_ISREVIEW = "isReview";

	private String LEVEL;
	private String LEVEL_TYPE;
	private ViewTopMenu view_top_menu;
	private ViewPager view_pager;
	private ViewIndicator view_indicator;
	private WordDayPagerAdapter adapter;
	private LinearLayout ll_reviews, bt_quiz;
	private LinearLayout bt_certificate;

	private VoUserInfo voUserInfo;

	private int pager_total = 0;			//총 페이지 수
	private int day_total = 0;				//총 Day 수
	private final int day_count = 20;		//한 화면에 Day 수
	private final int day_row_count = 10;	//한줄에 Day 수
	
	private boolean isSetDay = false;		//처음 Day 그릴때

	@Override
	protected void onConnectedNetwork(boolean retry) {
//		if(retry) reqDayInfo1(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityWordTestMain.actList.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordtest_day_select);


		LEVEL = getIntent().getStringExtra(PARAM_WORDTEST_LEVEL);
		LEVEL_TYPE = getIntent().getStringExtra(PARAM_WORDTEST_LEVEL_TYPE);
		
		view_top_menu = (ViewTopMenu)findViewById(R.id.view_top_menu);
		view_top_menu.setActivity(this);
		
		DisplayUtil.setLayoutHeight(this, 67, view_top_menu);
		
		Button btn_level = (Button) findViewById(R.id.btn_level);
		DisplayUtil.setLayout(this, 104, 66, btn_level);
		DisplayUtil.setLayoutMargin(this, 36, 28, 0, 20, btn_level);
		btn_level.setText("Level " + LEVEL);
		
		LinearLayout ll_conts = (LinearLayout) findViewById(R.id.ll_conts);
		DisplayUtil.setLayoutHeight(this, 380, ll_conts);
		DisplayUtil.setLayoutMargin(this, 34, 0, 34, 0, ll_conts);
		
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		DisplayUtil.setLayoutPadding(this, 15, 30, 15, 0, view_pager);
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				view_indicator.setCurrentPage(view_pager.getCurrentItem());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
		ll_reviews = (LinearLayout) findViewById(R.id.ll_reviews);
		DisplayUtil.setLayoutHeight(this, 150, ll_reviews);
		DisplayUtil.setLayoutMargin(this, 34, 20, 34, 0, ll_reviews);
		
		bt_quiz = (LinearLayout) findViewById(R.id.bt_quiz);
		DisplayUtil.setLayout(this, 138, 66, bt_quiz);
		DisplayUtil.setLayoutMargin(this, 0, 30, 35, 0, bt_quiz);
		DisplayUtil.setLayout(this, 28, 28, findViewById(R.id.iv_icon_pen));
		bt_quiz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reqQuizInfo();
			}
		});
		setQuizBtnAnim();
		
		reqDayInfo1(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isSetDay){
			LogTraceMin.I("Day 정보 재실행");
			saveDBFromAllInnerDB();
			//reqDayInfo(true);
		}
	}
	
	@Override
	protected void onStart() {
		if(null != handlerAnim) 
			handlerAnim.sendEmptyMessageDelayed(ANIMSG_WHAT, 1000);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		handlerAnim.removeMessages(ANIMSG_WHAT);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != handlerAnim) {
			handlerAnim.removeMessages(ANIMSG_WHAT);
			handlerAnim = null;
		}
	}
	
	@Override
	public ViewTopMenu getTopManu() {
		return view_top_menu;
	}

	@Override
	public int getTopMenuPos() {
		return 0;
	}
	
	/**
	 * Day 선택
	 * @param info
	 */
	public void selectedDay(VoDay info) {
		
		if(VoDay.DAY_NONE == info.getSTATUS()) {
			
			LogTraceMin.I("USERGB :: " + VoMyInfo.getInstance().getUSERGB());
			
			if("08".equals(VoMyInfo.getInstance().getUSERGB())){
				reqStdInfo1(ConstantsCommURL.getWordUrl("GetStdInfo"), info.getSTD_DAY(), false, Constant.STD_GB_S, false, false);
				return;
			}else{
				if(VoDay.OPENGB_PASS == info.getOPEN_GB() || checkPrevDayPass(info.getSTD_DAY() - 1)) {
					reqStdInfo1(ConstantsCommURL.getWordUrl("GetStdInfo"), info.getSTD_DAY(), false, Constant.STD_GB_S, false, false);
					return;
				}
				showDialogNotPrev(R.string.wordstest_cant_day);
				return;
			}
		}
		
		//학습을 완료 : 성공
		if(VoDay.DAY_PASS == info.getSTATUS() || VoDay.DAY_CERT == info.getSTATUS()) {
			showResultDialog(info.getSTD_DAY(), info.getRSTATUS(), false);
			return;
		}
		//학습을 완료 : 실패
		if(VoDay.DAY_FAIL == info.getSTATUS()) {
			showResetDataDialog(info.getSTD_DAY());
			return;
		}
		
		//혹은 이어하기 일 경우
		if(VoDay.DAY_ING == info.getSTATUS()) {
			reqStdInfo1(ConstantsCommURL.getWordUrl("GetStdInfo"), info.getSTD_DAY(), true, Constant.STD_GB_S, false, false);
		}
	}
	
	/**
	 * Review 선택
	 * @param info
	 */
	public void selectedReview(VoDay info) {
		
		if(VoDay.OPENGB_PASS != info.getOPEN_GB()) {
			showDialogNotPrev("Day " + (info.getSTD_DAY() * INT_QUIZ_OPEN_NUMBER) + "를 PASS해야 학습할 수 있습니다.");
			return;
		}
		
		LogTraceMin.I("USERGB :: " + VoMyInfo.getInstance().getUSERGB());
		
		//Reivew 진행 : NONE
		if(VoDay.DAY_NONE == info.getSTATUS()) {
			reqStdInfo1(ConstantsCommURL.getWordUrl("GetReviewStdInfo"), info.getSTD_DAY(), false, Constant.STD_GB_S, true, false);
			return;
		}
		
		//Reivew 진행 : 완료
		if(VoDay.DAY_PASS == info.getSTATUS()) {
			showResultDialog(info.getSTD_DAY(), info.getRSTATUS(), true);
			return;
		}
		
		//Reivew 진행 : ING
		if(VoDay.DAY_ING == info.getSTATUS()) {
			reqStdInfo1(ConstantsCommURL.getWordUrl("GetReviewStdInfo"), info.getSTD_DAY(), true, Constant.STD_GB_S, true, false);
			return;
		}
	}
	
	/**
	 * 이전 Day상태 체크하기
	 */
	private boolean checkPrevDayPass(int day) {
		
		if(day == 0) return true;
		
		VoDay voDay = adapter.getInfo(day - 1);
		return VoDay.DAY_PASS == voDay.getSTATUS();
	}
	
	/**
	 * 음성 다운로드를 먼저 체크한다.
	 * @return
	 */
	private ArrayList<VoFileWordsDownload> checkDownload() {
		
		ArrayList<VoFileWordsDownload> downloadArray = new ArrayList<>();
		
		for(VoWordsTestDownload voDownload : VoWordsTestList.getInstance().getDOWNLOAD()) {
			
			if(!WordTestDownloadDao.getInstance(this).isExistWords(voDownload.getFILE_NAME())){
				LogTraceMin.I("DB없음 URL" + voDownload.getFILE_NAME());
				VoFileWordsDownload voFileWords = new VoFileWordsDownload();
				voFileWords.setFilename(voDownload.getFILE_NAME());
//				voFileWords.setDate(voDownload.getFILE_DATE());
				downloadArray.add(voFileWords);
			}
		}
		
		if(downloadArray.size() > 0) return downloadArray;
		else return null;
	}
	
	/**
	 * Review 시작
	 */
	private void gotoReview(int day, String std_gb) {
		LogTraceMin.I("gotoReview --------------------------------- ");
		Intent intent = new Intent(this, FragmentActivityWordTest.class);
		intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
		intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, LEVEL_TYPE);
		intent.putExtra(FragmentActivityWordTest.PARAM_WORDTEST_CODE, VoWordsTestList.getInstance().getCURR_ZONE());
		intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_STD_DB, std_gb);
		intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, day);
		intent.putExtra(FragmentActivityWordTest.IS_COME_FROM_REVIEW_TEST, true);
		startActivity(intent);
	}
	
	/**
	 * 단어학습 Test로 이동 : 학습 진행중 / 처음 학습 시작시 
	 */
	private void gotoTestTypeSelect(int day, boolean isStudy, String std_gb) {
		LogTraceMin.I("gotoTestTypeSelect ---------------------------------- ");
		LogTraceMin.I("Day " + day);
		LogTraceMin.I("Day " + day);
		
		if(isStudy) {
			//학습미완료,
			Intent intent = new Intent(this, ActivityWordTestTypeSelect.class);
			intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
			intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, LEVEL_TYPE);
			intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_STD_DB, std_gb);
			intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, day);
			startActivity(intent);
		
		}else{
			//학습완료후 다음 차시 처음 시작시,
			if(VoWordsTest.CODE_REVIEW.equals(VoWordsTestList.getInstance().getCURR_ZONE()) 
					&& null != VoWordsTestList.getInstance().getREVIEW() && VoWordsTestList.getInstance().getREVIEW().getSTD_LIST().size() > 0) {
				
				//틀린 단어가 있을 경우
				LogTraceMin.I("틀린 단어가 있을 경우 : 틀린단어 복습");
				
				Intent intent = new Intent(this, FragmentActivityWordTest.class);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, LEVEL_TYPE);
				intent.putExtra(FragmentActivityWordTest.PARAM_WORDTEST_CODE, VoWordsTest.CODE_REVIEW);
				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, day);
				intent.putExtra(FragmentActivityWordTest.IS_COME_FROM_REVIEW, true);
				startActivity(intent);
				
			}else{
				//틀린 단어가 없을 경우,
				LogTraceMin.I("틀린 단어가 없을 경우 : 다음차시");
				
				Intent intent = new Intent(this, ActivityWordTestTypeSelect.class);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, LEVEL_TYPE);
				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_STD_DB, std_gb);
				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, day);
				startActivity(intent);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == CHANT_FILE_DOWNLOAD_RESULT) {
			if(null == data){
				LogTraceMin.I("다운로드 취소");
				return;
			}
			LogTraceMin.I("다운로드 끝 학습 시작");
			LogTraceMin.I("C_PARAM_STD_GB :: " + data.getStringExtra(C_PARAM_STD_GB));
			
			if(data.getBooleanExtra(C_PARAM_ISREVIEW, false)) {
				//Review일때,
				gotoReview(data.getIntExtra(C_PARAM_DAY, 0), data.getStringExtra(C_PARAM_STD_GB));
			}else{
				//Day일때,
				gotoTestTypeSelect(data.getIntExtra(C_PARAM_DAY, 0), 
						data.getBooleanExtra(C_PARAM_ISSTUDY, false), 
						data.getStringExtra(C_PARAM_STD_GB));	//미완료인지 새로하기인지 여부에 따라서
			}
		
		}else if(requestCode == QUIZ_FILE_DOWNLOAD_RESULT) {
			LogTraceMin.I("다운로드 끝 퀴즈 시작");
			gotoQuiz();
		}
	}
	
	/**
	 * 결과 Dailog 띄우기
	 */
	private void showResultDialog(final int day, final int restd_std_gb, final boolean isReview) {
		
		DialogWordsComp dialog = new DialogWordsComp(this);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        if(isReview) dialog.setButtonMsg("Review 재학습", "Reivew Report\n확인");
		
        dialog.setListener(new DialogWordsComp.ListenerDialogButton() {
            @Override
            public void onClick(Dialog dialog, int result) {
            	
            	if(result == DialogWordsComp.ListenerDialogButton.DIALOG_RELEARNING){
            		
            		if(VoDay.DAY_PASS == restd_std_gb) {
            			LogTraceMin.I("재학습 재실행 >> 재학습 Test 초기화");
            			if(isReview) showRestudyPop( day, Constant.STD_GB_R, true, true);
            			else showRestudyPop(day, Constant.STD_GB_R, false, true);
            		
            		}else{
            			LogTraceMin.I("재학습 처음 OR 이어하기");
                		if(isReview) reqStdInfo1(ConstantsCommURL.getWordUrl("GetReviewStdInfo"), day, false, Constant.STD_GB_R, true, false);
                		else reqStdInfo1(ConstantsCommURL.getWordUrl("GetStdInfo"), day, false, Constant.STD_GB_R, false, false);
            		}
            	
            	}else if(result == DialogWordsComp.ListenerDialogButton.DIALOG_REPORT){
            		LogTraceMin.I("Report");
            		Intent intent = new Intent(ActivityWordTestDaySelect.this, ActivityWordsDailyReport.class);
    				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
    				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, day);
    				if(isReview) intent.putExtra(PARAM_WORDTEST_STDY_W_GUBN, Constant.STD_W_GB_R);
    				else intent.putExtra(PARAM_WORDTEST_STDY_W_GUBN, Constant.STD_W_GB_D);

    				if(restd_std_gb == 2) intent.putExtra(PARAM_WORDTEST_STDY_GUBN, Constant.STD_GB_R);
    				else intent.putExtra(PARAM_WORDTEST_STDY_GUBN, Constant.STD_GB_S);

    				startActivity(intent);
            	}
            	
            	if (dialog != null && dialog.isShowing())
            		dialog.dismiss();
            }
        });
	}
	
	/**
	 * Dailog 창 띄우기
	 */
	private void showDialog(ListenerDialogButton listener, String msg, String btn_left, String btn_right) {
		
		DialogStudent dialog = new DialogStudent(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        
        if(StringUtil.isNull(btn_right)) dialog.setButtonMsg(btn_left);
        else dialog.setButtonMsg(btn_left, btn_right);
        dialog.setMessage(msg);
        dialog.setListener(listener);
	}
	
	/**
	 * Dailog 창 띄우기
	 */
	private void showDialogNotPrev(int msg) {
		
		showDialog(new ListenerDialogButton() {
			
			@Override
			public void onClick(Dialog dialog, int result) {
				if (dialog != null && dialog.isShowing())
            		dialog.dismiss();
			}
		}, getString(msg), getString(R.string.monthly_test_close), null);
	}
	
	/**
	 * Dailog 창 띄우기
	 */
	private void showDialogNotPrev(String msg) {
		
		showDialog(new ListenerDialogButton() {
			
			@Override
			public void onClick(Dialog dialog, int result) {
				if (dialog != null && dialog.isShowing())
					dialog.dismiss();
			}
		}, msg, getString(R.string.monthly_test_close), null);
	}
	
	/**
	 * Dialog Fail후 재 시작시 모든 데이터를 초기화
	 */
	 private void showResetDataDialog(final int day) {
		 
		 showDialog(new ListenerDialogButton() {
				
				@Override
				public void onClick(Dialog dialog, int result) {
					
					if(result == MouMouDialog.DialogButtonListener.DIALOG_BTN_FIRST){
						reqStdInfo1(ConstantsCommURL.getWordUrl("GetStdInfo"), day, true, Constant.STD_GB_S, false, true);
	                }
	            	
	            	if (dialog != null && dialog.isShowing()){
	            		dialog.dismiss();
	            	}
				}
			}, getString(R.string.wordstest_resetdata), getString(R.string.comfirm), getString(R.string.cancel));
	 }
	
	 /**
	  * Dialog 재학습 재 시작시 모든 재학습 데이터 초기화
	  */
	private void showRestudyPop(final int day, final String std_gb, final boolean isReview, final boolean isDelete) {
		
		showDialog(new ListenerDialogButton() {
			
			@Override
			public void onClick(Dialog dialog, int result) {
				
				if(result == MouMouDialog.DialogButtonListener.DIALOG_BTN_FIRST){
					if(isReview) reqStdInfo1(ConstantsCommURL.getWordUrl("GetReviewStdInfo"), day, false, std_gb, isReview, isDelete);
					else reqStdInfo1(ConstantsCommURL.getWordUrl("GetStdInfo"), day, false, std_gb, isReview, isDelete);
                }
            	
            	if (dialog != null && dialog.isShowing()){
            		dialog.dismiss();
            	}
			}
		}, getString(R.string.wordstest_resetdata_restudy), getString(R.string.comfirm), getString(R.string.cancel));
	}
	
	/**
	 * 내부 DB에 있는 데이터 전부 서버에 저장
	 */
	private void saveDBFromAllInnerDB() {
		LogTraceMin.I("내부 DB에 있는 데이터 전부 서버에 저장");
		showLoadingProgress(getResources().getString(R.string.wait_for_data));
		SaveWords saveWords = new SaveWords(this);
		saveWords.setSaveWordsComplete(saveComplete);
		saveWords.saveReqInnerDB();
	}
	
	SaveWordsComplete saveComplete = new SaveWordsComplete() {

		@Override
		public void success() {
			reqDayInfo1(true);
		}

		@Override
		public void fail() {
			doubleLoginDetected();
		}

		@Override
		public void exception() {
			doubleLoginDetected();
		}
	};
	
	/**
	 * DayList 정보 가져오기
	 */
	private void reqDayInfo1(final boolean isReset){
//		if(!checkNetworking(false)) return;

		showLoadingProgress(getResources().getString(R.string.wait_for_data));


		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_GETDAYINFO);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, voUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(this,Preferences.PREF_USER_ID,null));
		builder.appendQueryParameter("STD_LEVEL", LEVEL);

		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_GETDAYINFO, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();
				VoWordsDayList dayList = ApplicationPool.getGson().fromJson(response, VoWordsDayList.class);

//				//인증서 버튼 보이게 할지 결정
				VoDay voDay = dayList.getDAYSTDLIST().get(dayList.getDAYSTDLIST().size() - 1);
				if(VoDay.DAY_CERT == voDay.getSTATUS()) showCertBtn(dayList.getSTD_LEVEL(), dayList.getCERTIFICATE());

				if(isReset) resetDayInfo(dayList);
				else addDayInfo(dayList);
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
				LogUtil.i("reprot exception" + error);
				ToastUtil.show(ActivityWordTestDaySelect.this,"네트워크 연결 상태를 확인해주세요", Toast.LENGTH_SHORT);
				hideProgress();
			}

			@Override
			public void dismissDialog() {
				hideProgress();
			}
		});
	}
	/**
	 * Day페이지 구하기
	 * @param dayList
	 */
	private void addDayInfo(VoWordsDayList dayList) {
		isSetDay = true;
		
		day_total = dayList.getTotalList();
		pager_total = day_total % day_count > 0 ? day_total / day_count + 1 : day_total / day_count;
		
		view_indicator = (ViewIndicator) findViewById(R.id.view_indicator);
		view_indicator.setImgResource(R.drawable.sel_indicator_dot);
		view_indicator.setSize(13, 13);
		view_indicator.setPageCount(pager_total);
		
		DisplayUtil.setLayoutMargin(this, 0, 20, 0, 30, view_indicator);
		
		adapter = new WordDayPagerAdapter(this, dayList.getDAYSTDLIST());
		view_pager.setAdapter(adapter);
		
		addReviewInfo(dayList.getREVIEWLIST());
	}
	
	private void resetDayInfo(VoWordsDayList dayList) {
		LogTraceMin.I("resetDayInfo");
		adapter.setDayList(dayList.getDAYSTDLIST());
		adapter.notifyDataSetChanged();
		ll_reviews.removeAllViews();
		addReviewInfo(dayList.getREVIEWLIST());
		
	}
	
	/**
	 * 인증서 버튼 보이게
	 */
	boolean isShowCerBtn = false;	//한번 보이면 더 이상 안 보이게 하기 위해서
	private void showCertBtn(final String level, final VoCertificate certification) {
		
		if(isShowCerBtn) return;
		
		isShowCerBtn = true;
		
		bt_certificate = (LinearLayout) findViewById(R.id.bt_certificate);
		DisplayUtil.setLayout(this, 140, 74, bt_certificate);
		DisplayUtil.setLayoutMargin(this, 20, 20, 0, 0, bt_certificate);
		DisplayUtil.setLayoutPadding(this,0,25,0,0,bt_certificate);
		DisplayUtil.setLayout(this, 27, 33, findViewById(R.id.iv_icon_cert));
		
		bt_certificate.setVisibility(View.VISIBLE);
		bt_certificate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityWordTestDaySelect.this, ActivityCertificate.class);
				intent.putExtra(ActivityCertificate.P_CER_LEVEL, level);
				intent.putExtra(ActivityCertificate.P_CERTIFICATE, certification);
				startActivity(intent);
			}
		});
	}
	
	
	/**
	 * 리뷰
	 */
	private void addReviewInfo(ArrayList<VoDay> voDayList) {	
		for(VoDay voDay : voDayList) {
			ViewDay day = new ViewDay(this);
			day.setActivity(this);
			day.setData(voDay, true);
			ll_reviews.addView(day);
		}
	}
	
	/**
	 * Day 정보 가져오기
	 */
	private void reqStdInfo1(String url, final int day, final boolean isStudy, final String std_gb, final boolean isReview, boolean isDelete){
		showLoadingProgress(getResources().getString(R.string.wait_for_data));

		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, voUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(this,Preferences.PREF_USER_ID,null));
		builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_LEVEL, LEVEL);
		builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_DAY, Integer.toString(day));
		builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_STD_GB, std_gb);
		if(isDelete) builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_DELETE, "Y");


		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_GETLEVELINFO, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();
				ApplicationPool.getGson().fromJson(response, VoWordsTestList.class);

				ArrayList<VoFileWordsDownload> downLoadArray = checkDownload();

				if(null != downLoadArray && downLoadArray.size() > 0){
					Intent intent = new Intent(ActivityWordTestDaySelect.this, ActivityWordsDownload.class);
					intent.putExtra(ActivityWordsDownload.C_PARAM_DOWNLOAD_URL, VoWordsTestList.getInstance().getPATH());
					intent.putExtra(ActivityWordsDownload.C_PARAM_DOWNLOAD_DATA, downLoadArray);
					intent.putExtra(C_PARAM_DAY, day);
					intent.putExtra(C_PARAM_ISSTUDY, isStudy);
					intent.putExtra(C_PARAM_STD_GB, std_gb);
					intent.putExtra(C_PARAM_ISREVIEW, isReview);
					startActivityForResult(intent, CHANT_FILE_DOWNLOAD_RESULT);
				}else {
					if(isReview) gotoReview(day, std_gb);
					else gotoTestTypeSelect(day, isStudy, std_gb);	//미완료인지 새로하기인지 여부에 따라서
				}

				LogTraceMin.I("----------------------------------------------------------------");
				LogTraceMin.I("CURR_ZONE : " + VoWordsTestList.getInstance().getCURR_ZONE());
				LogTraceMin.I("CURR_QUST_NUM : " + VoWordsTestList.getInstance().getCURR_QUST_NUM());
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
				LogUtil.i("reprot exception" + error);
				ToastUtil.show(ActivityWordTestDaySelect.this,"네트워크 연결 상태를 확인해주세요", Toast.LENGTH_SHORT);
				hideProgress();
			}

			@Override
			public void dismissDialog() {
				hideProgress();
			}
		});
	}

	/**
	 * 퀴즈 부분
	 */
	Handler handlerAnim = new Handler() {
		public void handleMessage(android.os.Message msg) {
			bt_quiz.startAnimation(animation);
			handlerAnim.sendEmptyMessageDelayed(ANIMSG_WHAT, 2000);
		}
	};
	
	AnimationSet animation = null;
	private final int ANIMSG_WHAT = 100;
	
	private void setQuizBtnAnim() {
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setDuration(1000);
		
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setStartOffset(1000);
		fadeOut.setDuration(1000);
		
		animation = new AnimationSet(false);
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);
		bt_quiz.setAnimation(animation);
	}
	
	/**
	 * 퀴즈 정보 풀러오기
	 */
	private void reqQuizInfo() {

		showLoadingProgress(getResources().getString(R.string.wait_for_data));

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_QUIZINFO);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(this,Preferences.PREF_USER_ID,null));


		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_QUIZINFO, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();
				ApplicationPool.getGson().fromJson(response, VoWordsTestList.class);

				if(null == VoWordsTestList.getInstance().getDOWNLOAD()) {
					showDialogNotPrev(R.string.wordstest_quiz_no);
					return;
				}

				createPractice();   //퀴즈로 Practice만들기

				ArrayList<VoFileWordsDownload> downLoadArray = checkDownload();

				if(null != downLoadArray && downLoadArray.size() > 0){
					Intent intent = new Intent(ActivityWordTestDaySelect.this, ActivityWordsDownload.class);
					intent.putExtra(ActivityWordsDownload.C_PARAM_DOWNLOAD_URL, VoWordsTestList.getInstance().getPATH());
					intent.putExtra(ActivityWordsDownload.C_PARAM_DOWNLOAD_DATA, downLoadArray);
					startActivityForResult(intent, ActivityWordTestDaySelect.QUIZ_FILE_DOWNLOAD_RESULT);
				}else
					gotoQuiz();

			}

			@Override
			public void systemcheck(String response) {

			}

			@Override
			public void fail(VoBase base) {

			}

			@Override
			public void exception(ANError error) {
				LogUtil.i("reprot exception" + error);
				ToastUtil.show(ActivityWordTestDaySelect.this,"네트워크 연결 상태를 확인해주세요", Toast.LENGTH_SHORT);
				hideProgress();

			}

			@Override
			public void dismissDialog() {

			}
		});
	}
	
	/**
	 * 퀴즈 시작
	 */
	private void gotoQuiz() {
		Intent intent = new Intent(this, ActivityWordTestQuizSelect.class);
		startActivityForResult(intent, ActivityWordTestQuizSelect.WORDTEST_QUIZ_TYPE);
	}
	
	/**
	 * 퀴즈로 Practice 만들기
	 */
	private void createPractice() {

		VoWordsTest VoWordsQuiz = VoWordsTestList.getInstance().getQUIZ();
		VoWordsTest VoWordsPractice = VoWordsTestList.getInstance().new VoWordsTest();
		
		VoWordsPractice.setSTD_STEP(VoWordsTest.CODE_PRACTICE);
		VoWordsPractice.setSTD_TYPE(VoWordQuest.QUIZ_CODE_PRACTICE);
		VoWordsPractice.setSTD_NAME("Practice");
		
		ArrayList<VoWordQuest> STD_LIST = new ArrayList<>();
		
		for(VoWordQuest wordQuest : VoWordsQuiz.getSTD_LIST()) {
			
			VoWordQuest wordPractice = VoWordsTestList.getInstance().new VoWordQuest();
			wordPractice.setSTD_GB(wordQuest.getSTD_GB());
			wordPractice.setSTD_LEVEL(wordQuest.getSTD_LEVEL());
			wordPractice.setSTD_DAY(wordQuest.getSTD_DAY());
			wordPractice.setWORD_SEQ(wordQuest.getWORD_SEQ());
			wordPractice.setWORD(wordQuest.getWORD());
			wordPractice.setMEAN(wordQuest.getMEAN());
			wordPractice.setSTD_STEP(wordQuest.getSTD_STEP());
			wordPractice.setSTD_TYPE(wordQuest.getSTD_TYPE());
			wordPractice.setSTD_NUM(wordQuest.getSTD_NUM());
			wordPractice.setUSER_ANS(wordQuest.getUSER_ANS());
			wordPractice.setANSWER(wordQuest.getANSWER());
			wordPractice.setRESULT_YN(wordQuest.getRESULT_YN());
			wordPractice.setSTD_MP3(wordQuest.getSTD_MP3());
			wordPractice.setSENTENCE(wordQuest.getSENTENCE());
			wordPractice.setSEN_MEAN(wordQuest.getSEN_MEAN());
			wordPractice.setBOGI_LIST(wordQuest.getBOGI_LIST());
			
			STD_LIST.add(wordPractice);
		}
		
		VoWordsPractice.setSTD_LIST(STD_LIST);
		VoWordsTestList.getInstance().setPRACTICE(VoWordsPractice);
		
		LogTraceMin.I("Quiz List :: " + new Gson().toJson(VoWordsTestList.getInstance()));
	}
	
	
	/**
	 * Day PagerAdapter
	 *
	 */
	private class WordDayPagerAdapter extends PagerAdapter {

		LayoutInflater mInflater;
		Context context;
		SparseArray<View> views = new SparseArray<View>();
		ArrayList<VoDay> dayList = null;
		
		public WordDayPagerAdapter(Context context, ArrayList<VoDay> dayList) {
			this.context = context;
			mInflater = LayoutInflater.from(context);
			this.dayList = dayList;
		}
		
		public void setDayList(ArrayList<VoDay> dayList) {
			this.dayList = dayList;
		}
		
		public VoDay getInfo(int pos) {
			if(null != dayList) {
				return dayList.get(pos);
			}
			return null;
		}
		
		@Override
		public int getCount() {
			return pager_total;
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			
			View view = mInflater.inflate(R.layout.item_day_pager, null);
			resetView(position, view);
			
			((ViewPager)container).addView(view);
			views.put(position, view);
			return view;
		}
		
		private void resetView(int position, View view) {
			
			LinearLayout ll_day_first = (LinearLayout) view.findViewById(R.id.ll_day_first);
			LinearLayout ll_day_second = (LinearLayout) view.findViewById(R.id.ll_day_second);
			
			ll_day_first.removeAllViews();
			ll_day_second.removeAllViews();
			
			int start_day = position * day_count;
			
			for(int i = 0; i < day_row_count; i++) {
				if(start_day + i < day_total) {
					ViewDay child = new ViewDay(context);
					child.setActivity((ActivityWordTestDaySelect) context);
					child.setData(dayList.get(start_day + i), false);
					ll_day_first.addView(child);
				}
			}
			
			for(int i = 0; i < day_row_count; i++) {
				if(start_day + day_row_count + i < day_total) {
					ViewDay child = new ViewDay(context);
					child.setActivity((ActivityWordTestDaySelect) context);
					child.setData(dayList.get(start_day + day_row_count + i), false);
					ll_day_second.addView(child);
				}
			}
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
			views.remove(position);
		}
		
		@Override
		public boolean isViewFromObject(View v, Object obj) {
			return v == obj;
		}
		
		@Override
		public void notifyDataSetChanged() {
			
			int key = 0;
		    for(int i = 0; i < views.size(); i++) {
		       key = views.keyAt(i);
		       View view = views.get(key);
		       resetView(key, view);
		    }
			super.notifyDataSetChanged();
		}
	}
}
