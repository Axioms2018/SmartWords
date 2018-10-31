package kr.co.moumou.smartwords.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.error.ANError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.customview.DialogWordsReport;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.activity.SaveWords.SaveWordsComplete;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoCertificate;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoUserInfo;
import kr.co.moumou.smartwords.vo.VoWordsDReportData;
import kr.co.moumou.smartwords.vo.VoWordsDReportDetail;
import kr.co.moumou.smartwords.vo.VoWordsDReportFrame;
import kr.co.moumou.smartwords.wordschart.PieGraph;
import kr.co.moumou.smartwords.wordschart.PieSlice;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DialogStudent.ListenerDialogButton;

public class ActivityWordsDailyReport extends ActivityBase implements View.OnClickListener {
	
//	private ViewSlidingMenuMM view_sliding_menu;
	
	private String LEVEL;
	private int DAY;
	public VoUserInfo mUserInfo;
	LinearLayout ll_top;
	ImageButton bt_bon;
	ImageButton bt_jae;
	Button bt_dailyreport;
	LinearLayout bt_certificate;
	LinearLayout ll_detail;
	LinearLayout ll_graph;
	PieGraph graph;
	LinearLayout ll_k;
	ImageView iv_kcount;	
	TextView tv_kcount;
	LinearLayout ll_margin2;
	LinearLayout ll_u;
	ImageView iv_ucount;
	TextView tv_ucount;
	LinearLayout ll_margin;
	Button bt_wrongview;
	LinearLayout ll_data;
	LinearLayout ll_title1;
	LinearLayout ll_info1;
	TextView tv_infostep;
	TextView tv_infoday;
	TextView tv_infotest1;
	TextView tv_infotest2;
	LinearLayout ll_title2;
	LinearLayout ll_info2;
	TextView tv_infocount;
	TextView tv_infoknown;
	TextView tv_infounknown;
	TextView tv_infoscore;
	LinearLayout ll_test3;
	TextView tv_titletest3;
	TextView tv_infotest3;
	ImageView iv_stamp;
	ImageView iv_dummy;
	ImageButton bt_close;
	
	VoWordsDReportFrame voDReportFrame;
	VoWordsDReportData s_study = new VoWordsDReportData();
	VoWordsDReportData r_study = new VoWordsDReportData();
	ArrayList<VoWordsDReportDetail> wordList = null;
	ArrayList<VoWordsDReportDetail> r_wordList = null;
	
	private String srFlag = "S";
	private String stdWGb = "D";
	private boolean hasTest3 = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordsdailyreport);

		
		LEVEL = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL);
		DAY = getIntent().getIntExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, 0);
		srFlag = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_GUBN);
		stdWGb = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_W_GUBN);
		
		/*view_sliding_menu = (ViewSlidingMenuMM) findViewById(R.id.view_sliding_menu);
		view_sliding_menu.showErrorReportButton();
		view_sliding_menu.setListener(menuClickListener);*/
		
		bt_certificate = (LinearLayout) findViewById(R.id.bt_certificate);
		ll_top = (LinearLayout) findViewById(R.id.ll_top);
		bt_bon = (ImageButton) findViewById(R.id.bt_bon);
		bt_jae = (ImageButton) findViewById(R.id.bt_jae);
		bt_dailyreport = (Button) findViewById(R.id.bt_dailyreport);
		ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
		ll_graph = (LinearLayout) findViewById(R.id.ll_graph);
		graph = (PieGraph) findViewById(R.id.piegraph);
		ll_k = (LinearLayout) findViewById(R.id.ll_k);		
		iv_kcount = (ImageView) findViewById(R.id.iv_kcount);
		tv_kcount = (TextView) findViewById(R.id.tv_kcount);
		ll_margin2 = (LinearLayout) findViewById(R.id.ll_margin2);
		ll_u = (LinearLayout) findViewById(R.id.ll_u);
		iv_ucount = (ImageView) findViewById(R.id.iv_ucount);
		tv_ucount = (TextView) findViewById(R.id.tv_ucount);
		ll_margin = (LinearLayout) findViewById(R.id.ll_margin);
		bt_wrongview = (Button) findViewById(R.id.bt_wrongview);
		ll_data = (LinearLayout) findViewById(R.id.ll_data);
		ll_title1 = (LinearLayout) findViewById(R.id.ll_title1);
		ll_info1 = (LinearLayout) findViewById(R.id.ll_info1);
		tv_infostep = (TextView) findViewById(R.id.tv_infostep);
		tv_infoday = (TextView) findViewById(R.id.tv_infoday);
		tv_infotest1 = (TextView) findViewById(R.id.tv_infotest1);
		tv_infotest2 = (TextView) findViewById(R.id.tv_infotest2);
		ll_title2 = (LinearLayout) findViewById(R.id.ll_title2);
		ll_info2 = (LinearLayout) findViewById(R.id.ll_info2);
		tv_infocount = (TextView) findViewById(R.id.tv_infocount);
		tv_infoknown = (TextView) findViewById(R.id.tv_infoknown);
		tv_infounknown = (TextView) findViewById(R.id.tv_infounknown);
		tv_infoscore = (TextView) findViewById(R.id.tv_infoscore);
		ll_test3 = (LinearLayout) findViewById(R.id.ll_test3);
		tv_titletest3 = (TextView) findViewById(R.id.tv_titletest3);
		tv_infotest3 = (TextView) findViewById(R.id.tv_infotest3);
		iv_stamp = (ImageView) findViewById(R.id.iv_stamp);
		iv_dummy = (ImageView) findViewById(R.id.iv_dummy);
		bt_close = (ImageButton) findViewById(R.id.bt_close);
		
		DisplayUtil.setLayoutMargin(this, 30, 26, 30, 24, ll_top);
		DisplayUtil.setLayoutMargin(this, 0, 0, 16, 0, bt_jae);
		DisplayUtil.setLayoutMargin(this, 30, 0, 30, 110, ll_detail);
		DisplayUtil.setLayoutWidth(this, 580, ll_graph); 
		DisplayUtil.setLayoutMargin(this, 0, 0, 6, 0, ll_graph);
		DisplayUtil.setLayoutWidth(this, 120, ll_k);
		DisplayUtil.setLayoutMargin(this, 0, 0, 0, 10, iv_kcount);
		DisplayUtil.setLayoutMargin(this, 0, 10, 0, 0, tv_kcount);
		DisplayUtil.setLayoutWidth(this, 20, ll_margin2);
		DisplayUtil.setLayoutWidth(this, 120, ll_u);
		DisplayUtil.setLayout(this, 20, 20, iv_ucount);
		DisplayUtil.setLayout(this, 20, 20, iv_kcount);
		DisplayUtil.setLayoutMargin(this, 0, 0, 0, 10, iv_ucount);
		DisplayUtil.setLayoutMargin(this, 0, 10, 0, 0, tv_ucount);
		DisplayUtil.setLayoutHeight(this, 30, ll_margin);
		DisplayUtil.setLayout(this, 110, 60, bt_wrongview);
		DisplayUtil.setLayoutWidth(this, 636, ll_data);
		DisplayUtil.setLayoutMargin(this, 38, 56, 44, 0, ll_title1);
		DisplayUtil.setLayoutMargin(this, 0, 56, 40, 0, ll_info1);
		DisplayUtil.setLayoutMargin(this, 0, 56, 26, 0, ll_title2);
		DisplayUtil.setLayoutMargin(this, 0, 56, 0, 0, ll_info2);
		DisplayUtil.setLayoutMargin(this, 38, 0, 0, 0, ll_test3);
		DisplayUtil.setLayoutMargin(this, 70, 0, 0, 0, tv_infotest3);
		DisplayUtil.setLayoutMargin(this, 30, 30, 60, 140, iv_stamp);
		DisplayUtil.setLayoutMargin(this, 30, 30, 30, 30, iv_dummy);
		DisplayUtil.setLayout(this, 210, 210, iv_dummy);
		
		DisplayUtil.setLayout(this, 52, 53, bt_close);
		DisplayUtil.setLayout(this, 184, 53, bt_bon);
		DisplayUtil.setLayout(this, 184, 53, bt_jae);
		DisplayUtil.setLayout(this, 210, 210, iv_stamp);
		
		//DisplayUtil.setLayout(this, 140, 52, bt_certificate);
		//DisplayUtil.setLayoutMargin(this, 20, 0, 0, 0, bt_certificate);
		DisplayUtil.setLayout(this, 140, 60, bt_certificate);
		DisplayUtil.setLayoutMargin(this, 20, -10, 0, 0, bt_certificate);
		DisplayUtil.setLayout(this, 27, 33, findViewById(R.id.iv_icon_cert));
		bt_certificate.setOnClickListener(ActivityWordsDailyReport.this);
		
		if(Constant.STD_W_GB_R.equals(stdWGb)) {
			bt_dailyreport.setText(getResources().getString(R.string.review_button));
			DisplayUtil.setLayout(this, 190, 52, bt_dailyreport);
		}else{
			DisplayUtil.setLayout(this, 161, 52, bt_dailyreport);
		}
		
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.circle_word);
		int w = (int) DisplayUtil.getWidthUsingRate(this, 380);

		int width = b.getWidth();
		int height = b.getHeight();
		
		Bitmap resized = null;
		while (height > w) {
			resized = Bitmap.createScaledBitmap(b, (width * w) / height, w, true);
			height = resized.getHeight();
			width = resized.getWidth();
		}
		
        if(resized == null) graph.setBackgroundBitmap(b);
        else graph.setBackgroundBitmap(resized);        
        graph.setmLineSize(DisplayUtil.getWidthUsingRate(this, 30));
		
		bt_wrongview.setOnClickListener(this);
		bt_close.setOnClickListener(this);
		bt_bon.setOnClickListener(this);
		bt_jae.setOnClickListener(this);
		
		Button bt_allview = (Button) findViewById(R.id.bt_allview);
		DisplayUtil.setLayout(this, 110, 60, bt_allview);
		bt_allview.setOnClickListener(this);
		DisplayUtil.setLayoutMargin(this, 0, 0, 10, 0, bt_allview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		saveDBFromAllInnerDB();
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
			requestData();
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
	
	private void requestData() {
		showLoadingProgress(getResources().getString(R.string.wait_for_data));


		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_DAYREPORT);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(this,Preferences.PREF_USER_ID,null));
		builder.appendQueryParameter("STD_LEVEL", LEVEL);
		builder.appendQueryParameter("STD_DAY", String.valueOf(DAY));
		builder.appendQueryParameter("STD_W_GB", stdWGb);


		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_DAYREPORT, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();
				voDReportFrame = ApplicationPool.getGson().fromJson(response, VoWordsDReportFrame.class);
				s_study = voDReportFrame.getS_REPORT();
				r_study = voDReportFrame.getR_REPORT();
				wordList = voDReportFrame.getWORD_LIST();
				r_wordList = voDReportFrame.getR_WORD_LIST();

				if( null != voDReportFrame.getCERTIFICATE() && "Y".equals(voDReportFrame.getCERTIFICATE().getCERT_YN())) {

					String pref_words = voDReportFrame.getUSERID() + "_" + SharedPrefData.PREF_WORDS_CERT_L + voDReportFrame.getSTD_LEVEL();
					boolean isShowCertificate = SharedPrefData.getBooleanSharedData(ActivityWordsDailyReport.this, pref_words, false);
					LogTraceMin.I("SharedPrefData : " + pref_words + " : " + isShowCertificate);

					if(!isShowCertificate) {
						showCertificate(voDReportFrame.getSTD_LEVEL(), voDReportFrame.getCERTIFICATE());
						SharedPrefData.setBooleanSharedData(ActivityWordsDailyReport.this, pref_words, true);
					}
					bt_certificate.setVisibility(View.VISIBLE);
				}

				if(s_study == null) return;

				if(r_study != null) {
					bt_bon.setVisibility(View.VISIBLE);
					bt_jae.setVisibility(View.VISIBLE);

					if("R".equals(srFlag)) {
						setData(r_study);
						bt_jae.setSelected(true);
					} else {
						setData(s_study);
						bt_bon.setSelected(true);
					}
				}else{
					setData(s_study);
				}
			}

			@Override
			public void systemcheck(String response) {

			}

			@Override
			public void fail(VoBase base) {

			}

			@Override
			public void exception(ANError error) {

			}

			@Override
			public void dismissDialog() {

			}
		});
		

	}
	
	/**
	 * 인증서 개시
	 */
	private void showCertificate(String level, VoCertificate certification) {
//
		Intent intent = new Intent(this, ActivityCertificate.class);
		intent.putExtra(ActivityCertificate.P_CER_LEVEL, level);
		intent.putExtra(ActivityCertificate.P_CERTIFICATE, certification);
		startActivity(intent);
	}
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
	
	private void setData(VoWordsDReportData data) {
		
		//SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초");
		
		tv_kcount.setText(data.getRIGHT_CNT() + "");
		tv_ucount.setText(data.getWRONG_CNT() + "");
		tv_infostep.setText("Level " + data.getSTD_LEVEL() + (Constant.STD_W_GB_R.equals(stdWGb) ? " / Review " : " / Day ") + data.getSTD_DAY());
		tv_infoday.setText(df.format(data.getSTD_DATE()));
		tv_infotest1.setText(data.getTEST1() + " 단어");
		tv_infotest2.setText(data.getTEST2() + " 단어");
		hasTest3 = !StringUtil.isNull(data.getTEST3());
		
		if(hasTest3) {
			tv_titletest3.setVisibility(View.VISIBLE);
			tv_infotest3.setVisibility(View.VISIBLE);
			tv_infotest3.setText(data.getTEST3() + " 단어");
		} else {
			tv_titletest3.setVisibility(View.INVISIBLE);
			tv_infotest3.setVisibility(View.INVISIBLE);
		}

		tv_infocount.setText(data.getWORD_CNT() + " 단어");
		tv_infoknown.setText(data.getRIGHT_CNT() + " 단어");
		tv_infounknown.setText(data.getWRONG_CNT() + " 단어");
		tv_infoscore.setText(data.getWORD_PER() + (Constant.STD_W_GB_R.equals(stdWGb) ? "점" : "%"));

		setStamp(data.getWORD_PER());
		
		setGraph(data);
	}
	
	private void setGraph(VoWordsDReportData data) {
		graph.removeSlices();
		
		PieSlice slice = new PieSlice();		
        slice.setColor(getResources().getColor(R.color.daily_graph_right));
        slice.setValue(0);
        slice.setGoalValue(data.getRIGHT_CNT());
        graph.addSlice(slice);
        
        slice = new PieSlice();		
        slice.setColor(getResources().getColor(R.color.daily_graph_wrong));
        slice.setValue(10);
        slice.setGoalValue(data.getWRONG_CNT());
        graph.addSlice(slice);
        
		mHandler.sendEmptyMessage(0);
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			graph.setDuration(2000);
            graph.setAnimationListener(graph.getAnimationListener());
            graph.animateToGoalValues();
		}    	
    };
	
	@SuppressWarnings("deprecation")
	private void setStamp(int score) {
		
		if(Constant.STD_W_GB_R.equals(stdWGb)) {
			iv_stamp.setImageDrawable(getResources().getDrawable(R.drawable.ic_stamp_reviewpass));
		}else if(score >= 80) {
			iv_stamp.setImageDrawable(getResources().getDrawable(R.drawable.ic_stamp_pass));

		} else {
			iv_stamp.setImageDrawable(getResources().getDrawable(R.drawable.ic_stamp_try));
		}

		AnimationSet set = new AnimationSet(true);
		
		Animation alpha = new AlphaAnimation(0.0f, 1.0f);
		alpha.setDuration(500);
		set.addAnimation(alpha);
		
		Animation scale = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f, 0.5f, 0.5f);
		scale.setDuration(500);
		set.addAnimation(scale);
		
		iv_stamp.setAnimation(set);
		iv_stamp.setVisibility(View.VISIBLE);
		set.start();
	}
	
	private void PopWrong(VoWordsDReportData wrong, boolean isWordList) {
		if(wrong == null) {
			final DialogStudent mDialog = new DialogStudent(this);
			mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
			mDialog.show();
			mDialog.setButtonMsg("확인");
			mDialog.setMessage("데이터가 수신되지 않았습니다.");
			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(true);
			mDialog.setListener(new ListenerDialogButton() {
	            @Override
	            public void onClick(Dialog dialog, int result) {
	            	mDialog.dismiss();
	            }
	        });
		} else {
			
			if(isWordList) {
				
				DialogWordsReport wrongpop = null;
				if("R".equals(stdWGb) && "R".equals(srFlag)) {
					wrongpop = new DialogWordsReport(this, wrong, hasTest3, isWordList, r_wordList, stdWGb, voDReportFrame.getPATH());
				}else{
					wrongpop = new DialogWordsReport(this, wrong, hasTest3, isWordList, wordList, stdWGb, voDReportFrame.getPATH());
				}
				wrongpop.show();
			}
			else{
				if(wrong.getUSERWORD() == null) {
					final DialogStudent mDialog = new DialogStudent(this);
					mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
					mDialog.show();
					mDialog.setButtonMsg("확인");
					mDialog.setMessage("틀린 단어가 없습니다.");
					mDialog.setCancelable(false);
					mDialog.setCanceledOnTouchOutside(true);
					mDialog.setListener(new ListenerDialogButton() {
			            @Override
			            public void onClick(Dialog dialog, int result) {
			            	mDialog.dismiss();
			            }
			        });
				} else {
					
					DialogWordsReport wrongpop = null;
					if("R".equals(stdWGb) && "R".equals(srFlag)) {
						wrongpop = new DialogWordsReport(this, wrong, hasTest3, isWordList, r_wordList, stdWGb, voDReportFrame.getPATH());
					}else{
						wrongpop = new DialogWordsReport(this, wrong, hasTest3, isWordList, wordList, stdWGb, voDReportFrame.getPATH());
					}
					wrongpop.show();
					
//					DialogWordsReport wrongpop = new DialogWordsReport(this, wrong, hasTest3, isWordList, wordList, stdWGb, voDReportFrame.getPATH());
//					wrongpop.show();
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_wrongview :
				if("S".equals(srFlag)) {
					PopWrong(s_study, false);
				} else if("R".equals(srFlag)) {
					PopWrong(r_study, false);
				}				
				break;
				
			case R.id.bt_allview :
				if("S".equals(srFlag)) {
					PopWrong(s_study, true);
				} else if("R".equals(srFlag)) {
					PopWrong(r_study, true);
				}
				break;
				
			case R.id.bt_bon :
				srFlag = "S";
				setData(s_study);
				v.setSelected(true);
				bt_jae.setSelected(false);
				break;
				
			case R.id.bt_jae :
				srFlag = "R";
				setData(r_study);
				v.setSelected(true);
				bt_bon.setSelected(false);
				break;
				
			case R.id.bt_close :
				finish();				
				break;
				
			case R.id.bt_certificate :
				showCertificate(voDReportFrame.getSTD_LEVEL(), voDReportFrame.getCERTIFICATE());
				break;

			default:
				break;
		}
		
	}

	@Override
	public ViewTopMenu getTopManu() {
		return null;
	}

	@Override
	public int getTopMenuPos() {
		return 0;
	}	

}
