package kr.co.moumou.smartwords.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.androidnetworking.error.ANError;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.dialog.MouMouDialog;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.customview.ViewWordsTestType;
import kr.co.moumou.smartwords.customview.ViewWordsTestType.TestState;
import kr.co.moumou.smartwords.customview.ViewWordsTestType.WordTestTypeClickListener;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoWordsLevelList.VoWordsLevel;
import kr.co.moumou.smartwords.vo.VoWordsTestList;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DialogStudent.ListenerDialogButton;

public class ActivityWordTestTypeSelect extends ActivityBase {

	public static Activity activityWordTestType = null;
	
	public static final String PARAM_WORDTEST_DAY = "wordtest_day";
	protected static final String PARAM_WORDTEST_STD_DB = "wordtest_std_gb";
	protected static final int WORDTEST_TEST_TYPE = 3;
	
	private String LEVEL;
	private String LEVEL_TYPE;
	private int DAY;
	private String STD_GB;
	
	private ViewTopMenu view_top_menu;
	private LinearLayout ll_test_type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityWordTestMain.actList.add(this);
		activityWordTestType = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordtest_type_select);
		
		LEVEL = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL);
		LEVEL_TYPE = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE);
		DAY = getIntent().getIntExtra(PARAM_WORDTEST_DAY, 1);
		STD_GB = getIntent().getStringExtra(PARAM_WORDTEST_STD_DB);
		
		view_top_menu = (ViewTopMenu)findViewById(R.id.view_top_menu);
		view_top_menu.setActivity(this);
		
		DisplayUtil.setLayoutHeight(this, 67, view_top_menu);
		
		Button btn_level = (Button) findViewById(R.id.btn_level);
		DisplayUtil.setLayout(this, 104, 52, btn_level);
		DisplayUtil.setLayoutMargin(this, 36, 28, 0, 71, btn_level);
		btn_level.setText("Level " + LEVEL);
		
		btn_level = (Button) findViewById(R.id.btn_day);
		DisplayUtil.setLayout(this, 104, 52, btn_level);
		DisplayUtil.setLayoutMargin(this, 12, 28, 0, 0, btn_level);
		btn_level.setText("Day " + DAY);
		
		LinearLayout ll_conts = (LinearLayout) findViewById(R.id.ll_conts);
		DisplayUtil.setLayoutHeight(this, 167, ll_conts);
		DisplayUtil.setLayoutMargin(this, 90, 60, 90, 0, ll_conts);
		DisplayUtil.setLayoutPadding(this, 0, 0, 0, 0, ll_conts);
		
//		ImageView iv_char = (ImageView) findViewById(R.id.iv_char);
//		DisplayUtil.setLayout(this, 251, 258, iv_char);
//		DisplayUtil.setLayoutMargin(this, 72, 30, 9, 0, iv_char);
		
		ll_test_type = (LinearLayout) findViewById(R.id.ll_test_type);
		
		if(getIntent().getBooleanExtra(FragmentActivityWordTest.IS_COME_FROM_REVIEW, false)) {
			
			VoWordsTestList.getInstance().setCURR_ZONE(VoWordsTest.CODE_TEST1);
			VoWordsTestList.getInstance().setCURR_QUST_NUM(1);
			setTypeBtn();
			reqQuizInfo();
		}else setTypeBtn();
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
	 * TestType 버튼 설정
	 */
	private void setTypeBtn() {
		
		LogTraceMin.I("setTypeBtn :: " + VoWordsTestList.getInstance().getCURR_ZONE());
		LogTraceMin.I("current_num :: " + VoWordsTestList.getInstance().getCURR_QUST_NUM());
		
		if(ll_test_type.getChildCount() > 0) ll_test_type.removeAllViews();

		String codeType = VoWordsTestList.getInstance().getCURR_ZONE();
		
		//Test1
		if(null != VoWordsTestList.getInstance().getTEST1()) {
			
			if(VoWordsTest.CODE_PRACTICE.equals(codeType) || VoWordsTest.CODE_TEST2.equals(codeType) 
					|| VoWordsTest.CODE_TEST3.equals(codeType))
				addViewWordsTestType(VoWordsTestList.getInstance().getTEST1(), TestState.End);
			else addViewWordsTestType(VoWordsTestList.getInstance().getTEST1(), TestState.Ing);
		}
		
		//Practice
		if(null != VoWordsTestList.getInstance().getPRACTICE()) {
			
			if(VoWordsTest.CODE_PRACTICE.equals(codeType) || VoWordsTest.CODE_TEST2.equals(codeType) 
					|| VoWordsTest.CODE_TEST3.equals(codeType)) {
				addViewWordsTestType(VoWordsTestList.getInstance().getPRACTICE(), TestState.Ing);
			} else {
				addViewWordsTestType(VoWordsTestList.getInstance().getPRACTICE(), TestState.None);
			}
		}
		
		//Test2
		if(null != VoWordsTestList.getInstance().getTEST2()) {
			
			if(VoWordsTest.CODE_TEST3.equals(codeType)) {
				addViewWordsTestType(VoWordsTestList.getInstance().getTEST2(), TestState.End);
			}else if(VoWordsTest.CODE_TEST2.equals(codeType)) {
				if(VoWordsLevel.LEVEL_HIGH.equals(LEVEL_TYPE))
					addViewWordsTestType(VoWordsTestList.getInstance().getTEST2(), TestState.Ing);
				else {
					if(VoWordsTestList.getInstance().getCURR_QUST_NUM() > VoWordsTestList.getInstance().getTEST2().getSTD_LIST().size())
						addViewWordsTestType(VoWordsTestList.getInstance().getTEST2(), TestState.End);
					else addViewWordsTestType(VoWordsTestList.getInstance().getTEST2(), TestState.Ing);
				}
			} else {
				addViewWordsTestType(VoWordsTestList.getInstance().getTEST2(), TestState.None);
			}
		}
		
		if(null != VoWordsTestList.getInstance().getTEST3()) {
			
			if(VoWordsTest.CODE_TEST3.equals(codeType)) {
				if(VoWordsTestList.getInstance().getCURR_QUST_NUM() > VoWordsTestList.getInstance().getTEST3().getSTD_LIST().size())
					addViewWordsTestType(VoWordsTestList.getInstance().getTEST3(), TestState.End);
				else addViewWordsTestType(VoWordsTestList.getInstance().getTEST3(), TestState.Ing);
			} else {
				addViewWordsTestType(VoWordsTestList.getInstance().getTEST3(), TestState.None);
			}
		}
		
		//Test3
//		if(VoWordsLevel.LEVEL_HIGH.equals(LEVEL_TYPE)) {
//			
//		}
	}
	
	private void addViewWordsTestType(VoWordsTest info, TestState state) {
		
		ViewWordsTestType viewTestType = new ViewWordsTestType(this);
		viewTestType.setClickLisetener(testTypeListener);
		viewTestType.setData(info, state);
		ll_test_type.addView(viewTestType);
	}
	
	/**
	 * Test 선택에 따른 퀴즈
	 * code : Test1, Practice.. / testState : 상태 none, ing..
	 */
	WordTestTypeClickListener testTypeListener = new WordTestTypeClickListener() {
		
		@Override
		public void selectedQuiz(String code, TestState testState) {
			if(TestState.None == testState) return;
			if(VoWordsTest.CODE_PRACTICE.equals(code) && isAllTest1()) {
				showDialog(code);
				return;
			}
			gotoTestAcitivity(code);
		}
	};
	
	private void gotoTestAcitivity(String code) {
		Intent intent = new Intent(this, FragmentActivityWordTest.class);
		intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
		intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, LEVEL_TYPE);
		intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, DAY);
		intent.putExtra(FragmentActivityWordTest.PARAM_WORDTEST_CODE, code);
		startActivityForResult(intent, WORDTEST_TEST_TYPE);
	}
	
	/**
	 * Test1 모두 맞았는지 체크
	 */
	private boolean isAllTest1() {
		for(VoWordQuest voWordQuest : VoWordsTestList.getInstance().getTEST1().getSTD_LIST()) {
			if("N".equals(voWordQuest.getRESULT_YN())) {
				LogTraceMin.I("Test1 틀림 : " + voWordQuest.getANSWER());
				return false;
			}
		}
		LogTraceMin.I("Test1 전부 맞음");
		return true;
	}
	
	/**
	 * 틀린 문제가 없을때 뜨는 팝업
	 */
	private void showDialog(final String code) {
		DialogStudent dialog = new DialogStudent(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setButtonMsg(getString(R.string.dialog_yes), getString(R.string.dialog_no));
		dialog.setMessage(getString(R.string.wordstest_test_test1_no_wrong));
        dialog.setListener(new ListenerDialogButton() {
            @Override
            public void onClick(Dialog dialog, int result) {
            	
            	if(result == MouMouDialog.DialogButtonListener.DIALOG_BTN_FIRST){
            		gotoTestAcitivity(code);
            	}
            	if(dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == WORDTEST_TEST_TYPE) {
			setTypeBtn();
		}
	}
	
	/**
	 * 퀴즈 정보 가져오기
	 */
	private void reqQuizInfo() {
		LogTraceMin.I("정보 재실행");
		showLoadingProgress(getResources().getString(R.string.wait_for_data));

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_STDINFO);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoMyInfo.getInstance().getSESSIONID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, VoMyInfo.getInstance().getUSERID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_LEVEL, LEVEL);
		builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_DAY, Integer.toString(DAY));
		builder.appendQueryParameter(ConstantsCommParameter.Keys.WORDTEST_STD_GB, STD_GB);
		builder.appendQueryParameter("COMMAND",ConstantsCommCommand.COMMAND_1112_SMARTWORDS_QUIZ);

		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_STDINFO, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();
				VoWordsTestList.getInstance().clear();	//기존 데이터 삭제 후, 현재 데이터 저장
				ApplicationPool.getGson().fromJson(response, VoWordsTestList.class);
				setTypeBtn();
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
}
