package kr.co.moumou.smartwords.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.customview.ViewWordsTestType;
import kr.co.moumou.smartwords.customview.ViewWordsTestType.TestState;
import kr.co.moumou.smartwords.customview.ViewWordsTestType.WordTestTypeClickListener;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoWordsTestList;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;

public class ActivityWordTestQuizSelect extends ActivityBase {

	public static Activity activityWordTestType = null;
	protected final static String IS_COME_FROM_QUIZ = "iscomeQuiz";
	
	protected static final int WORDTEST_QUIZ_TYPE = 6;
	protected static final int WORDTEST_QUIZ_ALL_CLOSE = 7;
	protected static final int WORDTEST_QUIZ_COMPLETE = 8;	//퀴즈 전부 완료
	
	
	private ImageButton btn_temp_close;
	private LinearLayout ll_test_type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityWordTestMain.actList.add(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordtest_type_select);
		
		findViewById(R.id.view_top_menu).setVisibility(View.GONE);
		
		Button btn_level = (Button) findViewById(R.id.btn_level);
		DisplayUtil.setLayout(this, 104, 52, btn_level);
		DisplayUtil.setLayoutMargin(this, 36, 28, 0, 71, btn_level);
		btn_level.setText("Quiz");
		
		findViewById(R.id.btn_day).setVisibility(View.GONE);

        LinearLayout ll_conts = (LinearLayout) findViewById(R.id.ll_conts);
		DisplayUtil.setLayoutHeight(this, 167, ll_conts);
		DisplayUtil.setLayoutMargin(this, 90, 130, 90, 0, ll_conts);
		DisplayUtil.setLayoutPadding(this, 0, 0, 0, 0, ll_conts);

//		findViewById(R.id.iv_char).setVisibility(View.GONE);

        btn_temp_close = (ImageButton) findViewById(R.id.btn_temp_close);
		DisplayUtil.setLayout(this, 52, 52, btn_temp_close);
		DisplayUtil.setLayoutMargin(this, 0, 26, 30, 0, btn_temp_close);
		btn_temp_close.setVisibility(View.VISIBLE);
		btn_temp_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ll_test_type = (LinearLayout) findViewById(R.id.ll_test_type);
		
		//CURR_ZONE이 null일 경우 퀴즈를 처음 시작하는 것이므로 CURR_ZONE을 Practice로 설정
		if(StringUtil.isNull(VoWordsTestList.getInstance().getCURR_ZONE()))
			VoWordsTestList.getInstance().setCURR_ZONE(VoWordsTest.CODE_PRACTICE);
		
		setTypeBtn();
	}
	
	@Override
	public ViewTopMenu getTopManu() {
		return null;
	}

	@Override
	public int getTopMenuPos() {
		return 0;
	}
	
	/**
	 * TestType 버튼 설정
	 */
	private void setTypeBtn() {
		
		if(ll_test_type.getChildCount() > 0) ll_test_type.removeAllViews();
		
		String codeType = VoWordsTestList.getInstance().getCURR_ZONE();
		
		addViewWordsTestType(VoWordsTestList.getInstance().getPRACTICE(), TestState.Ing);
		
		if(VoWordsTest.CODE_QUIZ.equals(codeType)) {
			if(VoWordsTestList.getInstance().getCURR_QUST_NUM() > VoWordsTestList.getInstance().getQUIZ().getSTD_LIST().size())
				addViewWordsTestType(VoWordsTestList.getInstance().getQUIZ(), TestState.End);
			else
				addViewWordsTestType(VoWordsTestList.getInstance().getQUIZ(), TestState.Ing);
		}else{
			addViewWordsTestType(VoWordsTestList.getInstance().getQUIZ(), TestState.None);
		}
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
			gotoTestAcitivity(code);
		}
	};
	
	private void gotoTestAcitivity(String code) {
		Intent intent = new Intent(this, FragmentActivityWordTest.class);
		intent.putExtra(FragmentActivityWordTest.PARAM_WORDTEST_CODE, code);
		intent.putExtra(IS_COME_FROM_QUIZ, true);
		startActivityForResult(intent, WORDTEST_QUIZ_TYPE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == WORDTEST_QUIZ_TYPE) {
			if(resultCode == WORDTEST_QUIZ_COMPLETE){	//퀴즈 완료
				setResult(WORDTEST_QUIZ_COMPLETE);
				finish();
			}else if(resultCode == WORDTEST_QUIZ_ALL_CLOSE)	//퀴즈 닫기
				finish();
			else setTypeBtn();
			
		}
	}

}
