
package kr.co.moumou.smartwords.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.dao.WordTestRsltDao;
import kr.co.moumou.smartwords.activity.SaveWords.SaveWordsComplete;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.ToastUtil;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoWordsLevelList.VoWordsLevel;
import kr.co.moumou.smartwords.vo.VoWordsSaveList;
import kr.co.moumou.smartwords.vo.VoWordsSaveList.VoWordsSave;
import kr.co.moumou.smartwords.vo.VoWordsTestList;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;
import kr.co.moumou.smartwords.dialog.DialogWordsStart;
import kr.co.moumou.smartwords.dialog.DialogWordsStart.ListenerDialogButton;
//import moumou.co.kr.smartwords.dialog.DialogReport;

public class FragmentActivityWordTest extends BaseFragmentActivity implements OnClickListener {

	protected static final String PARAM_WORDTEST_CODE = "wordtest_code";
	protected static final String IS_COME_FROM_REVIEW = "iscome_from_review";
	protected static final String IS_COME_FROM_REVIEW_TEST = "iscome_from_review_test";
	protected static final int reviewTestQuizCnt = 4;
	
	public enum TestType {Review, Test1, Practice, Test2, Test3, Quiz, ReviewTest1, ReviewTest2, ReviewTest3}

	public enum ReviewType {None, Practice, Test2}
	//public enum ReviewTestType {None, ReviewTest1, ReviewTest2}
	
	private TestType currentTestType = TestType.Test1;
	private ReviewType currentReviewType = ReviewType.None;
	//private ReviewTestType currentReviewTestType = ReviewTestType.None;
	
	private String LEVEL_TYPE;
	private String LEVEL;
	private int DAY;

	private BaseFragmentWordTest currentFragment;	//현재 TestType
	private VoWordsTest wordsTestInfo;				//현재 Test정보
	private VoWordQuest quizInfo;					//현재 Quiz 정보

	private LinearLayout ll_fragment;
	
	private String currentQuizType;	//현재 퀴즈 정보의 QuizType
	private int currentNum = 0;		//현재 문제 Num
	private int totalQuiz = 0;
	private int showAnswerTime = 0;		//정답 보여주는 시간
	
	String wordtest_code;	//불러온 Temp Zone
	String curr_zone;		//현재 저장된 Zone
	
	private boolean isComeReview = false;		//리뷰에서 왔는지
	private boolean isComeQuiz = false;			//퀴즈에서 왔는지
	private boolean isComeReviewTest = false;	//리뷰테스트에서 왔는지
	
	private boolean hasTimer = false;	//타이머가 있는지
	
	private boolean isQuizComplete = false;		//Quiz 완료 했을때
	private boolean closeQuizAll = false;		//Quiz 전부 Close할지
	private boolean notChangeCrrtZone = false;	//CurrentZone을 바꾸지 않는다. 
												//Practice이 재 반복일 경우, CurrentZone을 저장하지 않기 위해서.
	
	private boolean test1AllCorrect = false;	//테스트가 전부 맞았을 경우 체크 해 주기 위해서
	
	private LinearLayout ll_check_quiz, ll_check_result, ll_quiz_result;
	private ImageView iv_check_quiz, iv_check_result, iv_check_result_txt;
	private TextView tv_quiz_total, tv_quiz_correct;
	
	protected MediaPlayer correctCheckMPlayer; // 정답확인용 미디어 플레이어
	
	public static final int SOUND_PLAY_CORRECT = 201;		//정답
	public static final int SOUND_PLAY_INCORRECT = 202;		//오답
	public static final int CORRECT_PROCESS_ALL = 203;		//음성 끝
	
	public boolean stopReadyPlay = true;	//Start 버튼이 나올때는 음성이 바로 재생 되는 것을 막기 위해서..
	
	protected boolean isQuizing = false;		//퀴즈 중인지 아닌지 체크 (창을 띄우고 있는지 아닌지 여부)
	private boolean enableStartQuiz = false;	//퀴즈 시작 가능여부 
	
	TextView tv_answer_test;
	
	public interface ProcessCorrectCompleteListener {
		/**
	     * 정답 처리 완료
		 * @throws Exception
	     */
		void processCorrectComplete() throws Exception;
		
	}
	
	public interface ProcessIncorrectCompleteListener {
		/**
	     * 오답 처리 완료
		 * @throws Exception
	     */
		void processIncorrectComplete() throws Exception;
		
	}

	@Override
	protected void onCreate(Bundle arg0) {
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(arg0);
		setContentView(R.layout.activity_wordtest_fragment);
		
		LEVEL_TYPE = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE);
		LEVEL = getIntent().getStringExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL);
		DAY = getIntent().getIntExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, 0);
		isComeReview = getIntent().getBooleanExtra(IS_COME_FROM_REVIEW, false);
		isComeQuiz = getIntent().getBooleanExtra(ActivityWordTestQuizSelect.IS_COME_FROM_QUIZ, false);
		isComeReviewTest = getIntent().getBooleanExtra(IS_COME_FROM_REVIEW_TEST, false);
		
		tv_answer_test = (TextView) findViewById(R.id.tv_answer_test);
		
		if(VoWordsLevel.LEVEL_LOW.equals(LEVEL_TYPE)) showAnswerTime = 2;
		else showAnswerTime = 3;
		
		setTestType(getIntent().getStringExtra(PARAM_WORDTEST_CODE));			//현재 테스트 타입 담기
		if(VoWordsTest.CODE_REVIEW_TEST1.equals(curr_zone)) showAnswerTime = 3;	//레벨 상관없이 ReviewTest에 Test1일 경우, 3초 보여준다
		
		setTotalAndCurrentQuizNum();	//현재 TotalNum and CurrentNum
		setQuizInfo();					//현재 퀴즈 정보 담기
		
		if(TestType.Practice == currentTestType && 
				VoWordsTestList.getInstance().isCompletePractice(VoWordsTestList.getInstance().getCURR_ZONE())) 
			notChangeCrrtZone = true;
		

		if("08".equals(VoMyInfo.getInstance().getUSERGB()) || "ssss02".equals(VoMyInfo.getInstance().getUSERID()) 
				|| "ss02".equals(VoMyInfo.getInstance().getUSERID())){
			tv_answer_test.setVisibility(View.VISIBLE);
			
			Button btn_passBtn = (Button) findViewById(R.id.btn_passBtn);
			DisplayUtil.setLayoutHeight(this, 60, btn_passBtn);
			btn_passBtn.setVisibility(View.VISIBLE);
			btn_passBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					boolean isSaveDb = true;
					
					if(ReviewType.Practice == currentReviewType) isSaveDb = false;	//ReviewType가 Practice일 경우 내부에 저장안함
					if(isComeQuiz && currentTestType == TestType.Practice) isSaveDb = false;	//퀴즈이고 Pratice일 경우 내부에 저장안함
					
					currentFragment.complateMediaPlayerQue.clear();
					currentFragment.stopTimerAni();
					
					for(int i = currentNum; i < totalQuiz + 1; i++) {
						currentNum = i;
						quizInfo = wordsTestInfo.getSTD_LIST().get(i - 1);
						quizInfo.setUSER_ANS(quizInfo.getANSWER());
						quizInfo.setRESULT_YN("Y");
						quizInfo.setTIME_USER(0);
						tv_answer_test.setText(quizInfo.getANSWER());
						if(isSaveDb) setSaveWordsInfo(quizInfo);
					}
					resetQuizInfo();
					currentFragment.setIndicator(currentNum);
				}
			});
		}
		
		Button btn_sub = null;
		
		btn_sub = (Button) findViewById(R.id.btn_level);
		DisplayUtil.setLayout(this, 104, 52, btn_sub);
		DisplayUtil.setLayoutMargin(this, 36, 28, 0, 0, btn_sub);
		if(isComeQuiz) btn_sub.setText("Quiz");
		else btn_sub.setText("Level " + LEVEL);
		
		if(!isComeReview && !isComeQuiz) {
			btn_sub = (Button) findViewById(R.id.btn_day);
			
			if(isComeReviewTest) {
				btn_sub.setText("Review " + DAY);
				DisplayUtil.setLayout(this, 130, 52, btn_sub);
			}else{
				btn_sub.setText("Day " + DAY);
				DisplayUtil.setLayout(this, 104, 52, btn_sub);
			}

			DisplayUtil.setLayoutMargin(this, 12, 28, 0, 0, btn_sub);
			btn_sub.setVisibility(View.VISIBLE);
		}
		
		btn_sub = (Button) findViewById(R.id.btn_testtype);
		if(TestType.Practice == currentTestType) DisplayUtil.setLayout(this, 126, 52, btn_sub);
		else DisplayUtil.setLayout(this, 104, 52, btn_sub);
		DisplayUtil.setLayoutMargin(this, 12, 28, 0, 0, btn_sub);
		setSubTitle(btn_sub);
		
		ImageButton btn_temp_close = (ImageButton) findViewById(R.id.btn_temp_close);
		DisplayUtil.setLayout(this, 52, 52, btn_temp_close);
		DisplayUtil.setLayoutMargin(this, 0, 26, 30, 0, btn_temp_close);
		btn_temp_close.setOnClickListener(this);
		
		ll_check_quiz = (LinearLayout) findViewById(R.id.ll_check_quiz);
		ll_check_quiz.setClickable(false);
		iv_check_quiz = (ImageView) findViewById(R.id.iv_check_quiz);
		DisplayUtil.setLayout(this, 319, 323, iv_check_quiz);
		
		ll_check_result = (LinearLayout) findViewById(R.id.ll_check_result);
		ll_check_result.setClickable(false);
		
		//Result pop
		DisplayUtil.setLayout(this, 448, 251, findViewById(R.id.ll_check_result_bg));
		iv_check_result = (ImageView) findViewById(R.id.iv_check_result);
		DisplayUtil.setLayoutMargin(this, 0, 20, 0, 0, iv_check_result);
		iv_check_result_txt = (ImageView) findViewById(R.id.iv_check_result_txt);
		
		ll_fragment = (LinearLayout) findViewById(R.id.ll_fragment);
		DisplayUtil.setLayoutMargin(this, 30, 30, 30, 30, ll_fragment);
		
		if(isComeQuiz) {
			ll_quiz_result = (LinearLayout) findViewById(R.id.ll_quiz_result);
			DisplayUtil.setLayout(this, 664, 216, findViewById(R.id.quiz_result_bg));
			ll_quiz_result.setClickable(false);
			
			tv_quiz_total = (TextView) findViewById(R.id.tv_quiz_total);
			tv_quiz_correct = (TextView) findViewById(R.id.tv_quiz_correct);
		}
		
		initFragment();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isQuizing) isQuizing = true;
		if(enableStartQuiz) {
			if(null != answerCompleteHandler) 
				answerCompleteHandler.sendEmptyMessageDelayed(CORRECT_PROCESS_ALL, 300); 
		}
		IntentFilter receiveVodPlayer = new IntentFilter();
		receiveVodPlayer.addAction(Constant.BROADCAST_ACTION_VOD_PLAYER_STATE);
		registerReceiver(broadCastShild, receiveVodPlayer);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isQuizing) isQuizing = false;
		new SaveWords(this).saveReqInnerDB();
	}
	
	protected void onDestroy() {	
		LogTraceMin.I("FragmentActivity onDestroy");
		super.onDestroy();
		if(null != answerCompleteHandler) {
			answerCompleteHandler.removeMessages(CORRECT_PROCESS_ALL);
			answerCompleteHandler = null;
		}
		if(null != correctMPCompleteListener) correctMPCompleteListener = null;
		if(null != inCorrectMPCompleteListener) inCorrectMPCompleteListener = null;
		try {
			if(broadCastShild != null){
				unregisterReceiver(broadCastShild); // 브로드캐스트 연결 해제
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_ENTER) {
			currentFragment.onKeyUp(keyCode);
			return false;
		}
		
		if(TestType.Test2  == currentTestType || TestType.Quiz == currentTestType) {
			if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_TAB || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_PAGE_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
				
		if(keyCode == KeyEvent.KEYCODE_DEL) {
			currentFragment.onKeyUp(keyCode);
			return false;
		}
		
		if(TestType.Test2  == currentTestType || TestType.Quiz == currentTestType) {
			if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_TAB || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_PAGE_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private BroadcastReceiver broadCastShild = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			int state = intent.getIntExtra(Constant.BROADCAST_ACTION_SLIDE_STATE, -1);
			
//			if(state == Constant.BROADCAST_ACTION_SLIDE_SHOW) {
//				LogTraceMin.I("slide show");
//				if(isQuizing) isQuizing = false;
//			}
//			else if(!view_sliding_menu.isShowReportDialog() && state == Constant.BROADCAST_ACTION_SLIDE_HIDE) {
//				LogTraceMin.I("slide hide");
//				if(!isQuizing) isQuizing = true;
//				if(enableStartQuiz) {
//					if(null != answerCompleteHandler)
//						answerCompleteHandler.sendEmptyMessageDelayed(CORRECT_PROCESS_ALL, 300);
//				}
//			}
		}
	};
	
	/**
	 * TestType 설정, wordsTestInfo
	 */
	private void setTestType(String code) {
		
		//String code = getIntent().getStringExtra(PARAM_WORDTEST_CODE);
		wordsTestInfo = VoWordsTestList.getInstance().getVoWordsTest(code);
		LogTraceMin.I("setTestType :: " +  code);

		switch (code) {
		case VoWordsTest.CODE_REVIEW:
			currentTestType = TestType.Review; //Review일 경우 타입을 두개로 분리해준다.
			currentNum = VoWordsTestList.getInstance().getCURR_QUST_NUM();
			if(currentNum <= 1) {
				currentReviewType = ReviewType.Practice;
				hasTimer = false;
			}
			else {
				currentReviewType = ReviewType.Test2;
				hasTimer = true;
			}
			break;
		case VoWordsTest.CODE_TEST1:
			currentTestType = TestType.Test1;
			hasTimer = true;
			break;

		case VoWordsTest.CODE_PRACTICE:
			currentTestType = TestType.Practice;
			hasTimer = false;
			break;
		case VoWordsTest.CODE_TEST2:
			currentTestType = TestType.Test2;
			hasTimer = true;
			break;
		case VoWordsTest.CODE_TEST3:
			currentTestType = TestType.Test3;
			hasTimer = true;
			break;
		case VoWordsTest.CODE_QUIZ:
			currentTestType = TestType.Quiz;
			hasTimer = true;
			break;
		case VoWordsTest.CODE_REVIEW_TEST1:
			currentTestType = TestType.ReviewTest1;
			hasTimer = true;
			break;
		case VoWordsTest.CODE_REVIEW_TEST2:
			currentTestType = TestType.ReviewTest2;
			hasTimer = true;
			break;
		case VoWordsTest.CODE_REVIEW_TEST3:
			currentTestType = TestType.ReviewTest3;
			hasTimer = true;
			break;
		}
	}
	
	private void setSubTitle(Button btn) {
		if(TestType.Review == currentTestType) {
			btn.setText("Review");
		}else if(TestType.Test1 == currentTestType) {
			btn.setText("Test 1");
		}else if(TestType.Practice == currentTestType) {
			btn.setText("Practice");
		}else if(TestType.Test2 == currentTestType) {
			btn.setText("Test 2");
		}else if(TestType.Test3 == currentTestType) {
			btn.setText("Test 3");
		}else if(TestType.Quiz == currentTestType) {
			btn.setText("Quiz");
		}else if(TestType.ReviewTest1 == currentTestType) {
			btn.setText("Test 1");
		}else if(TestType.ReviewTest2 == currentTestType) {
			btn.setText("Test 2");
		}else if(TestType.ReviewTest3 == currentTestType) {
			btn.setText("Test 3");
		}
	}
	
	protected TestType getCurrentTestType() {
		return currentTestType;
	}
	
	private void initFragment() {
		if(TestType.Review == currentTestType) {
			if(ReviewType.Practice == currentReviewType) 
				currentFragment = new FragmentPractice();
			else currentFragment = new FragmentTest2();
		}else if(TestType.Test1 == currentTestType) {
			currentFragment = new FragmentTest1();
		}else if(TestType.Practice == currentTestType) {
			currentFragment = new FragmentPractice();
		}else if(TestType.Test2 == currentTestType || TestType.Quiz == currentTestType) {
			currentFragment = new FragmentTest2();
		}else if(TestType.Test3 == currentTestType) {
			currentFragment = new FragmentTest3();
		}else if(TestType.ReviewTest1 == currentTestType) {
			currentFragment = new FragmentReview();
		}else if(TestType.ReviewTest2 == currentTestType) {
			currentFragment = new FragmentTest1();
		}else if(TestType.ReviewTest3 == currentTestType) {
			currentFragment = new FragmentTest3();
		}
		fragmentReplace();
		resetQuizInfo();
	}
	
	private void fragmentReplace() {
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_fragment, currentFragment);
        transaction.commit();
	}
	
	/**
	 * 현재 Fragment 가져오기
	 */
	protected Fragment getWordsTestFragment() {
		if(currentFragment == null) initFragment();
		return currentFragment;
	}
	
	/**
	 * 현재 WordsTest 정보 가져오기
	 */
	protected VoWordsTest getWordsTestInfo() {
		return wordsTestInfo;
	}

	/**
	 * 현재 퀴즈 정보 set
	 */
	private void setQuizInfo() {
		
		if(currentTestType == TestType.ReviewTest1) {
			int count = (currentNum - 1) * reviewTestQuizCnt;
			quizInfo = wordsTestInfo.getSTD_LIST().get(count);
		} 
		quizInfo = wordsTestInfo.getSTD_LIST().get(currentNum - 1);
		
		LogTraceMin.I("setQuizInfo currentNum :: " + currentNum);
		
		if(null != currentFragment) {
			//if(currentTestType == TestType.ReviewTest && currentReviewTestType == ReviewTestType.ReviewTest1)
			//	currentFragment.setIndicator(currentNum);
			currentFragment.setIndicator(currentNum);
		}
		if(null != tv_answer_test) tv_answer_test.setText(quizInfo.getANSWER());
	}
	
	/**
	 * 현재 퀴즈 정보 get
	 */
	protected VoWordQuest getQuizInfo() {
		return quizInfo;
	}
	
	/**
	 * Quiz에 맞게 정보 리셋하기
	 */
	private void resetQuizInfo() {
		LogTraceMin.I("resetQuizInfo 퀴즈 정보 리셋");
		if(currentTestType == TestType.Review && currentReviewType == ReviewType.Practice) 
			setCurrtQuizType(VoWordQuest.QUIZ_CODE_PRACTICE);
		else setCurrtQuizType(quizInfo.getSTD_TYPE());
		currentFragment.setQuizInfo();	//해당 퀴즈정보 넣기
	}
	
	/**
	 * Review에서 Practice 다음으로 바로 Test2로 보낼때 세팅
	 */
	private void afterRwPracticeGoRwTest2() {
		currentNum = 1;
		currentReviewType = ReviewType.Test2;
		hasTimer = true;
		setQuizInfo();	//현재 퀴즈 정보 담기
		initFragment();
	}
	
	/**
	 * 타임 설정할지 안 할지 여부 가져오기
	 */
	protected boolean getHasTimer() {
		return hasTimer;
	}
	
	/**
	 * 퀴즈 Total 가져오기
	 */
	protected int getTotalCountQuiz() {
		return totalQuiz;
	}
	
	/**
	 * 현재 CurrentNum
	 * Practice일 경우 한가지 조건을 거친다.
	 * 조건 : 지금 현대 CURR_ZONE 가 Pratice가 아닐 경우 무조건 currentNum = 0
	 */
	private void setTotalAndCurrentQuizNum() {
		
		totalQuiz = wordsTestInfo.getSTD_LIST().size();
		
		if(TestType.ReviewTest1 == currentTestType)
			totalQuiz = totalQuiz / reviewTestQuizCnt;
		
		currentNum = VoWordsTestList.getInstance().getCURR_QUST_NUM() == 0 ? 1 : VoWordsTestList.getInstance().getCURR_QUST_NUM();
		wordtest_code = getIntent().getStringExtra(PARAM_WORDTEST_CODE);
		curr_zone = VoWordsTestList.getInstance().getCURR_ZONE();
		
		if(VoWordsTest.CODE_PRACTICE.equals(wordtest_code) && !wordtest_code.equals(curr_zone)) 
			currentNum = 1;
		
		if(VoWordsTest.CODE_REVIEW_TEST1.equals(wordtest_code)) {
			int curt = VoWordsTestList.getInstance().getCURR_QUST_NUM() - 1;
			currentNum = (curt / reviewTestQuizCnt) + 1;
		}

		LogTraceMin.I("wordtest_code :: " + wordtest_code);
		LogTraceMin.I("curr_zone :: " + curr_zone);
		LogTraceMin.I("totalQuiz :: " + totalQuiz);
		LogTraceMin.I("currentNum :: " + currentNum);
	}
	
	/**
	 * 퀴즈 Current 가져오기
	 */
	protected int getCurrentQuizNum() {
		return currentNum;
	}
	
	/**
	 * 현재 QuizType 다를때 팝업
	 */
	protected void setCurrtQuizType(String quizCode) {
		LogTraceMin.I("setCurrentType :: "  + quizCode);
		
		if(null == currentQuizType || !currentQuizType.equals(quizCode)) {
			this.currentQuizType = quizCode;
			showQuizStartPop();
			setQuizText();
		}else{
			startQuiz();
		}
	}
	
	/**
	 * 현재 QuizType 가져오기
	 */
	protected String getCurrtQuizType() {
		if(currentQuizType == null) 
			return VoWordQuest.QUIZ_CODE_TEST1_K;
		return currentQuizType;
	}
	
	/**
	 * 퀴즈 타입이 다를때 뜨는 팝업
	 */
	private void showQuizStartPop() {
		
		if(currentFragment.isDestroy) return;
		
		stopReadyPlay = false;
		
		DialogWordsStart dialog = new DialogWordsStart(this);
		dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		dialog.setTitle(getQuizTypeText2(currentQuizType));
		if(isComeReview) dialog.setBtnText("Review"); else dialog.setBtnText("Start!");
		if(isComeReview) dialog.setBackground(quizTypeBackground[1]); else dialog.setBackground(quizTypeBackground[0]);
		currentFragment.setTextTimer(wordsTestInfo.getSTD_TIME());
		
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				if(isComeReview) justFinish = true;
				saveDBFromAllInnerDB();
			}
		});
		
		dialog.setListener(new ListenerDialogButton() {
			
			@Override
			public void onClick(Dialog dialog, int result) {
				if(currentFragment.hasMp) {
					if(!currentFragment.isPrepareMp) return;
				}
				startQuiz();
				if(dialog.isShowing()) dialog.dismiss();
			}
		});
	}
	
	private int[] quizTypeBackground = {R.drawable.wordtest_bg_start, R.drawable.bg_reivew};
	private int[] quizTypeText = {R.string.wordstest_test1_ko, R.string.wordstest_test1_en, R.string.wordstest_practice, 
							R.string.wordstest_test2, R.string.wordstest_test3, R.string.wordstest_review, R.string.wordstest_test1_en_opt};
	/**
	 *  퀴즈 타입에 맞게 설명 가져오기
	 */
	protected String getQuizTypeText(String text) {
		String txt = "";
		if(VoWordQuest.QUIZ_CODE_TEST1_K.equals(text)) {
			txt = getString(quizTypeText[0]);
		}else if(VoWordQuest.QUIZ_CODE_TEST1_E.equals(text)) {
			txt = getString(quizTypeText[1]);
		}else if(VoWordQuest.QUIZ_CODE_PRACTICE.equals(text)) {
			txt = getString(quizTypeText[2]);
		}else if(VoWordQuest.QUIZ_CODE_TEST2.equals(text)) {
			txt = getString(quizTypeText[3]);
		}else if(VoWordQuest.QUIZ_CODE_TEST3.equals(text)) {
			txt = getString(quizTypeText[4]);
		}else if(VoWordQuest.QUIZ_CODE_REVIEWTEST1.equals(text)) {
			txt = getString(quizTypeText[5]);
		}
		return txt;
	}
	
	/**
	 *  퀴즈 타입에 맞게 설명 가져오기
	 */
	protected String getQuizTypeText2(String text) {
		String txt = "";
		if(VoWordQuest.QUIZ_CODE_TEST1_E.equals(text)) {
			if(TestType.ReviewTest2 == currentTestType) txt = getQuizTypeText(text);
			else txt = getString(quizTypeText[6]);
		}else{
			txt = getQuizTypeText(text);
		}
		return txt;
	}
	
	
	/**
	 * QuizType에 따른 문제넣기
	 */
	private void setQuizText() {
		currentFragment.setQuizTxt(getQuizTypeText(currentQuizType));
	}
	
	/**
	 * 퀴즈 스타트 하기
	 */
	private void startQuiz() {
		if(!isQuizing) return;
		currentFragment.startTimerAni();	//애니타이머 시작
	}
	
	protected void nextQuiz(boolean isCorrect) {
		currentNum++;
//		if(currentTestType == TestType.Practice || currentTestType == TestType.Review) {
//			if(!isCorrect && correntChance > 0) currentNum--;
//			
//		}
//		if(!isCorrect && correntChance > 0) {
//			if(currentTestType == TestType.Practice) currentNum--;
//			if(currentReviewType == ReviewType.Practice) currentNum--;
//		}
		
		if(!isCorrect && correntChance > 0) {
			if(currentTestType == TestType.Practice || currentReviewType == ReviewType.Practice) currentNum--;
		}
		
		checkAnswer(isCorrect);	//정답체크
		LogTraceMin.I("nextQuiz 다음 퀴즈 불러오기 --------------------------------------------------------- ");
	}
	
//	protected void newQuiz(VoWordQuest[] wordQuestList, boolean isCorrect) {
//		currentNum++;
//		checkAnswer(isCorrect);
//	}
	
	/**
	 * 내부 DB에 퀴즈정보 저장 
	 */
	private void saveInnerDB(boolean isCorrect) {
		
		if(ReviewType.Practice == currentReviewType) return;	//ReviewType가 Practice일 경우 내부에 저장안함
		if(isComeQuiz && currentTestType == TestType.Practice) return;	//퀴즈이고 Pratice일 경우 내부에 저장안함
		if(currentTestType == TestType.Practice && correntChance > 0 && !isCorrect) return;		//Practice일때 1번째 시도일 경우 저장안함
		LogTraceMin.I("saveInnerDB :: 내부 DB에 저장");
		
		if(currentTestType == TestType.ReviewTest1) {
			
			int current  = (currentNum - 2) * reviewTestQuizCnt;
			int total = current + reviewTestQuizCnt;
			
			for(int i = current; i < total; i++) {
				VoWordQuest voWordQuest = wordsTestInfo.getSTD_LIST().get(i);
				//LogTraceMin.I("saveInnerDB word : " + voWordQuest.getSTD_NUM() + " / " + voWordQuest.getWORD() + " / " + voWordQuest.getUSER_ANS());
				setSaveWordsInfo(voWordQuest);
			}
			
		}else setSaveWordsInfo(quizInfo);
	}
	
	private void setSaveWordsInfo(VoWordQuest info) {
		
		VoWordsSave wordSave = VoWordsSaveList.getInstance().new VoWordsSave();
		wordSave.setUSERID(VoMyInfo.getInstance().getUSERID());
		wordSave.setSTD_GB(info.getSTD_GB());
		wordSave.setSTD_W_GB(info.getSTD_W_GB());
		wordSave.setSTD_LEVEL(info.getSTD_LEVEL());
		wordSave.setSTD_DAY(DAY);
		wordSave.setWORD_SEQ(info.getWORD_SEQ());
		wordSave.setSTD_NUM(info.getSTD_NUM());
		wordSave.setUSER_ANS(info.getUSER_ANS());
		wordSave.setANSWER(info.getANSWER());
		wordSave.setRESULT_YN(info.getRESULT_YN());
		wordSave.setSTD_STEP(info.getSTD_STEP());
		wordSave.setSTD_TIME(info.getTIME_USER());
		wordSave.setSTD_TYPE(info.getSTD_TYPE());
		
		String json = new Gson().toJson(wordSave);
		LogTraceMin.I("setSaveWordsInfo :: " + json);
		
		WordTestRsltDao.getInstance(this).putWordTest(wordSave);
	}
	
	/**
	 * 정답체크
	 * @param isCorrect
	 */
	private void checkAnswer(boolean isCorrect) {
		
		int time = wordsTestInfo.getSTD_TIME() - currentFragment.getTimer();
		if(time == 0) quizInfo.setTIME_USER(time + 1);
		else quizInfo.setTIME_USER(time);
		
		currentFragment.resetTotalTime();	//리셋 타이머
		
		LogTraceMin.I("답 :: " + quizInfo.getANSWER());
		LogTraceMin.I("입력한 답 :: " + quizInfo.getUSER_ANS());
		LogTraceMin.I("문제 푼 시간 :: " + time);
		
		if(isCorrect) {
			LogTraceMin.I("정답체크 :: >> 정답");
			processCorrect();
			currentFragment.showCheckAnswer(true);	//정답 보여주기
		}else{
			LogTraceMin.I("정답체크 :: >> 오답");
			processIncorrect();
			currentFragment.showCheckAnswer(false);
		}
		
		/** 현재 Current 정보 및 Paractice 정보 
		 *  현재 CurrentZone과 같을 경우에만 저장한다.
		 */
		if(wordtest_code.equals(curr_zone)) VoWordsTestList.getInstance().setCURR_QUST_NUM(currentNum);
		saveCurrentQuizAndPractice(isCorrect); /** 완료일 경우에만 저장하는 Current */
		
		LogTraceMin.I("checkAnswer() current_zone :: " + VoWordsTestList.getInstance().getCURR_ZONE());
		LogTraceMin.I("checkAnswer() current_num :: " + VoWordsTestList.getInstance().getCURR_QUST_NUM());
	}
	
	/**
	 * 정답체크
	 */
//	public void checkAnswer(String answer) {
//
//		answer = answer.trim();	//앞뒤 공백제거
//
//		int time = wordsTestInfo.getSTD_TIME() - currentFragment.getTimer();
//		if(time == 0) quizInfo.setTIME_USER(time + 1);
//		else quizInfo.setTIME_USER(time);
//
//		LogTraceMin.I("답 :: " + quizInfo.getANSWER());
//		LogTraceMin.I("입력한 답 :: " + answer);
//		LogTraceMin.I("문제 푼 시간 :: " + time);
//
//		quizInfo.setUSER_ANS(answer);
//		currentFragment.resetTotalTime();	//리셋 타이머
//
//		if(quizInfo.getANSWER().equals(answer)) {
//			LogTraceMin.I("정답체크 :: " + answer + ">> 정답");
//			processCorrect();
//			currentFragment.showCheckAnswer(true, answer);	//정답 보여주기
//
//		}else{
//			LogTraceMin.I("정답체크 :: " + answer + ">> 오답");
//			processIncorrect();
//			currentFragment.showCheckAnswer(false, answer);
//		}
//
//		/** 현재 Current 정보 및 Paractice 정보
//		 *  현재 CurrentZone과 같을 경우에만 저장한다.
//		 */
//		if(wordtest_code.equals(curr_zone)) VoWordsTestList.getInstance().setCURR_QUST_NUM(currentNum);
//		saveCurrentQuizAndPractice(); /** 완료일 경우에만 저장하는 Current */
//
//		LogTraceMin.I("checkAnswer() current_zone :: " + VoWordsTestList.getInstance().getCURR_ZONE());
//		LogTraceMin.I("checkAnswer() current_num :: " + VoWordsTestList.getInstance().getCURR_QUST_NUM());
//	}
	
	/**
     * 정답확인 음원 재생
     */
	public void playSound(String filePath, int playType, final boolean isAnswer) {
		LogUtil.d("playSound playType : " + playType);
		
		try {
			if (correctCheckMPlayer == null)
				correctCheckMPlayer = new MediaPlayer();
			else {
				if (correctCheckMPlayer.isPlaying())
					correctCheckMPlayer.stop();
				
				correctCheckMPlayer.reset();
			}
			switch (playType) {
			case SOUND_PLAY_CORRECT:
				correctCheckMPlayer.setOnCompletionListener(correctMPCompleteListener);
				break;
			case SOUND_PLAY_INCORRECT:
				correctCheckMPlayer.setOnCompletionListener(inCorrectMPCompleteListener);
				break;
			}
			
			correctCheckMPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					showCorrectImg(isAnswer);
					correctCheckMPlayer.start();
				}
			});
			
			String path = filePath;
			Uri mUri = Uri.parse(path);
			correctCheckMPlayer.setDataSource(this, mUri);
			correctCheckMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			correctCheckMPlayer.prepare();

		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.show(this, "음원을 재생할 수 없습니다.");
		}
	}
	
	/**
     * 정답 음원 재생 완료 후 호출됨
     */
	OnCompletionListener correctMPCompleteListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			//정답 이미지 표시 숨기기
			hideCorrentImg();
			if(null != answerCompleteHandler) 
				answerCompleteHandler.sendEmptyMessageDelayed(CORRECT_PROCESS_ALL, showAnswerTime * 1000); 
		}
		
	};
	
	protected int correntChance = 1;	//Practice일 경우에만 한번 더 기회를 준다.
	/**
     * 오답 음원 재생 완료 후 호출됨
     */
	OnCompletionListener inCorrectMPCompleteListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			//오답 이미지 표시 숨기기
			hideCorrentImg();
			if(null == answerCompleteHandler) return;
			
			if(currentFragment.isPractice()) {	//Practice일 경우에만 한번 더 기회를 준다.
				if(correntChance > 0){
					//한번 더
					currentFragment.addChance();
				}
				else answerCompleteHandler.sendEmptyMessageDelayed(CORRECT_PROCESS_ALL, showAnswerTime * 1000);
				correntChance--;
			}else{
				answerCompleteHandler.sendEmptyMessageDelayed(CORRECT_PROCESS_ALL, showAnswerTime * 1000);
			}
		}
	};
	
	/**
	 * 정답 완료 후 재생되는 Handler
	 */
	Handler answerCompleteHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(currentFragment.isDestroy) return;
			
			if(!isQuizing) {
				enableStartQuiz = true;
				return;
			}
			
			enableStartQuiz = false;
			
			if(currentNum <= totalQuiz) { 
				setQuizInfo();
				resetQuizInfo();
				return;
			}
			
			LogTraceMin.I("테스트 끝 !! --------------------------------------------------------- END");
			
			if(TestType.Review == currentTestType) {
				if(ReviewType.Practice == currentReviewType) {
					afterRwPracticeGoRwTest2();
					return;
				}
			}
			if(TestType.Test1 == currentTestType) {	//Test1 일경우, 

			}
			
			if(TestType.Test2 == currentTestType && VoWordsTestList.getInstance().getTEST3() == null) {
				checkResult(VoWordsTestList.getInstance().getTEST2().getSTD_LIST());
				return;
			}
			
			if(TestType.Test3 == currentTestType) {
				checkResult(VoWordsTestList.getInstance().getTEST3().getSTD_LIST());
				return;
			}
			
			if(isComeQuiz && TestType.Practice == currentTestType) {
				finish();
				return;
			}
			if(TestType.Quiz == currentTestType) {
				checkQuizResult(VoWordsTestList.getInstance().getQUIZ().getSTD_LIST());
				return;
			}
			if(TestType.ReviewTest1 == currentTestType) {
				LogTraceMin.I("Review Test1 끝나서 Review Tst2 로 갑니다.");
				saveDBFromAllInnerDB();
				return;
			}
			if(TestType.ReviewTest2 == currentTestType) {
				LogTraceMin.I("Review Test2 끝나서 Review Tst3 로 갑니다.");
				if(VoWordsTestList.getInstance().getTEST3() == null) isGotoReport = true;
				saveDBFromAllInnerDB();
				return;
			}
			if(TestType.ReviewTest3 == currentTestType) {
				LogTraceMin.I("Review Test3 끝나서 레포트로 갑니다.");
				isGotoReport = true;
				saveDBFromAllInnerDB();
				return;
			}
			
			LogTraceMin.I("current_zone :: " + VoWordsTestList.getInstance().getCURR_ZONE());
			saveDBFromAllInnerDB();
		}
	};
	
	/**
	 * Review에서 Practice 다음으로 바로 Test2로 보낼때 세팅
	 */
	private void gotoReviewTest2() {
//		setTestType(VoWordsTest.CODE_REVIEW_TEST2); 
		//wordsTestInfo = VoWordsTestList.getInstance().getVoWordsTest(VoWordsTest.CODE_REVIEW_TEST2);
		this.currentQuizType = null;
		getIntent().putExtra(PARAM_WORDTEST_CODE, VoWordsTest.CODE_REVIEW_TEST2);	//ReviewTest2로 넘어가면서 wordtest_code 변경해준다.
		VoWordsTestList.getInstance().setCURR_QUST_NUM(1);	//Current_qust_num을 1로 세팅
		
		setTestType(VoWordsTest.CODE_REVIEW_TEST2);
		Button btn_sub = (Button) findViewById(R.id.btn_testtype);
		setSubTitle(btn_sub);
		setTotalAndCurrentQuizNum();
		setQuizInfo();	//현재 퀴즈 정보 담기
		initFragment();
		
		
		///resetTotalTime

//		currentNum = 1;
//		currentReviewType = ReviewType.ReviewTest2;
//		hasTimer = true;
//		setQuizInfo();	//현재 퀴즈 정보 담기
//		initFragment();
	}
	
	/**
	 * Review에서 Practice 다음으로 바로 Test2로 보낼때 세팅
	 */
	private void gotoReviewTest3() {
		
		this.currentQuizType = null;
		getIntent().putExtra(PARAM_WORDTEST_CODE, VoWordsTest.CODE_REVIEW_TEST3);	//ReviewTest2로 넘어가면서 wordtest_code 변경해준다.
		VoWordsTestList.getInstance().setCURR_QUST_NUM(1);	//Current_qust_num을 1로 세팅
		
		setTestType(VoWordsTest.CODE_REVIEW_TEST3);
		Button btn_sub = (Button) findViewById(R.id.btn_testtype);
		setSubTitle(btn_sub);
		setTotalAndCurrentQuizNum();
		setQuizInfo();	//현재 퀴즈 정보 담기
		initFragment();
	}
	
	/** 
	 * 다음 CurrentZon And CurrentNum 저장하기, Pracetice저장
	 */
	private void saveCurrentQuizAndPractice(boolean isCorrect) {
		
		//Review 일때 빼고은 현재 CurrentNum과 TotalQuiz 수가 같지 않으면 저장하지 않는다.
		if(currentNum <= totalQuiz) return;		//완료가 아니면 저장하지 않는다
		
		if(currentTestType == TestType.Practice) {
			if(!isCorrect && correntChance > 0) return; // Practice일 경우, 틀렸을때 기회가 1개일 경우 한번 기회를 더 준다.
		}
		
		String current_zone = VoWordsTestList.getInstance().getCURR_ZONE();
		
		current_zone = VoWordsTestList.getInstance().getNextWordsStdStep(current_zone);
		
		if(isComeQuiz && currentTestType == TestType.Practice) current_zone = VoWordsTest.CODE_QUIZ;	//퀴즈 일때, Practice일때
		
//		if(isComeReviewTest && currentReviewTestType == ReviewTestType.ReviewTest1) 	//ReviewTest1 일 경우,
//			current_zone = VoWordsTest.CODE_TEST1;
//		
		//Review일때는 currentTestType 저장을 Test1으로 한다.
//		if(currentTestType == TestType.Review && currentReviewType == ReviewType.ReviewTest2) {
//			current_zone = VoWordsTest.CODE_TEST1;
//		}
		
		if(!notChangeCrrtZone && null != current_zone) {
			LogTraceMin.I("Test가 완료 되어서 CurrentZone을 바꿉니다.  " + current_zone);
			VoWordsTestList.getInstance().setCURR_ZONE(current_zone);
			VoWordsTestList.getInstance().setCURR_QUST_NUM(1);
		}
		
		if(TestType.Test1 == currentTestType) {
			makePractice();
			if(test1AllCorrect) {
				LogTraceMin.I("Test가 완료 되어서 CurrentZone을 바꿉니다.  test1AllCorrect :: " + VoWordsTest.CODE_TEST2);
				//Test1일 경우, 전부 맞았을 경우 CurrentZone을 Test2로 저장
				VoWordsTestList.getInstance().setCURR_ZONE(VoWordsTest.CODE_TEST2);
			}
		}
	}
	
	/**
	 * 최종테스트 결과가 통과인지 체크
	 */
	private void checkResult(ArrayList<VoWordQuest> quizList) {
		
		int total = quizList.size();
		int corrent = 0;
		
		for(VoWordQuest quiz : quizList) {
			if("Y".equals(quiz.getRESULT_YN())) corrent++;
		}
		
		int percent = (corrent * 100) / total;
		
		LogTraceMin.I("percent ::: " + percent);
		
		if(percent >= 80)
			showResultDialog(true);
		else showResultDialog(false);
	}
	
	private AnimationDrawable frameAnimation;
	
	@SuppressLint("ResourceType")
	private void showResultDialog(boolean isPass) {
		
		if(isPass){
			iv_check_result.setBackgroundResource(R.anim.words_ani_success);
			iv_check_result_txt.setBackgroundResource(R.drawable.text_cong);
			DisplayUtil.setLayout(this, 280, 55, iv_check_result_txt);
		}else{
			iv_check_result.setBackgroundResource(R.anim.words_ani_fail);
			iv_check_result_txt.setBackgroundResource(R.drawable.text_try);
			DisplayUtil.setLayout(this, 188, 56, iv_check_result_txt);
		}
		
		DisplayUtil.setLayout(this, 143, 150, iv_check_result);
		ll_check_result.setVisibility(View.VISIBLE);
		
		frameAnimation = (AnimationDrawable) iv_check_result.getBackground();
		frameAnimation.start();
		
		resultHandler.sendEmptyMessageDelayed(0, 3000);	//결과 이미지 2초 보여준 후, 결과 레포트로
	}
	
	private boolean isGotoReport = false;
	
	Handler resultHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(TestType.Quiz != currentTestType) {
				isGotoReport = true;
				frameAnimation.stop();
			}
			saveDBFromAllInnerDB();
		}
	};
	
	/**
	 * 최종 퀴즈 테스트 결과 
	 */
	private void checkQuizResult(ArrayList<VoWordQuest> quizList) {
		
		isQuizComplete = true;
		int total = quizList.size();
		int corrent = 0;
		
		for(VoWordQuest quiz : quizList) {
			if("Y".equals(quiz.getRESULT_YN())) corrent++;
		}
		showDialog(total, corrent);
	}
	
	private void showDialog(int total, int corrent) {
		tv_quiz_total.setText(" " + total);
		tv_quiz_correct.setText(" " + corrent);
		ll_quiz_result.setVisibility(View.VISIBLE);
		resultHandler.sendEmptyMessageDelayed(0, 3000);	//결과 이미지 3초 보여준 후 결과 풍선화면
	}
	
	public void onBackPressed() {
		if(isComeQuiz) {
			if(closeQuizAll) setResult(ActivityWordTestQuizSelect.WORDTEST_QUIZ_ALL_CLOSE);
			
			if(currentTestType == TestType.Practice) {
				VoWordsTestList.getInstance().setCURR_ZONE(VoWordsTest.CODE_PRACTICE);	//CurrentZone저장
				super.onBackPressed();
			}else{
				VoWordsTestList.getInstance().setCURR_ZONE(VoWordsTest.CODE_QUIZ);	//CurrentZone저장
				saveDBFromAllInnerDB();
			}
			return;
		}
		if(isComeReview) justFinish = true;
		saveDBFromAllInnerDB();
	}

	/**
	 * 내부 DB에 있는 데이터 전부 서버에 저장
	 */
	private void saveDBFromAllInnerDB() {
		LogTraceMin.I("내부 DB에 있는 데이터 전부 서버에 저장");
		LogTraceMin.I("currentFragment.isDestroy :: " + currentFragment.isDestroy);
		showLoadingProgress(getResources().getString(R.string.wait_for_data));
		SaveWords saveWords = new SaveWords(FragmentActivityWordTest.this);
		saveWords.setSaveWordsComplete(saveComplete);
		saveWords.saveReqInnerDB();
	}
	
	private boolean justFinish = false;
	SaveWordsComplete saveComplete = new SaveWordsComplete() {
		
		@Override
		public void success() {
			
			hideProgress();
			if(isGotoReport) {
				
				if(null != ActivityWordTestTypeSelect.activityWordTestType) {
					ActivityWordTestTypeSelect.activityWordTestType.finish();
					ActivityWordTestTypeSelect.activityWordTestType = null;
				}
				LogTraceMin.I("결과 레포트로 넘어갑니다.");
				
				if(TestType.ReviewTest2 == currentTestType) {
					LogTraceMin.I("ReiviewTest 결과");
					
					Intent intent = new Intent(FragmentActivityWordTest.this, ActivityWordsDailyReport.class);
					intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_GUBN, VoWordsTestList.getInstance().getSTD_GB());
					intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_W_GUBN, VoWordsTestList.getInstance().getSTD_W_GB());
					intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
					intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, DAY);
					startActivity(intent);
					finish();
					return;
				}
				
				if(TestType.ReviewTest3 == currentTestType) {
					LogTraceMin.I("ReiviewTest 결과");
					
					Intent intent = new Intent(FragmentActivityWordTest.this, ActivityWordsDailyReport.class);
					intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_GUBN, VoWordsTestList.getInstance().getSTD_GB());
					intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_W_GUBN, VoWordsTestList.getInstance().getSTD_W_GB());
					intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
					intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, DAY);
					startActivity(intent);
					finish();
					return;
				}

				Intent intent = new Intent(FragmentActivityWordTest.this, ActivityWordsDailyReport.class);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_GUBN, VoWordsTestList.getInstance().getSTD_GB());
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_W_GUBN, VoWordsTestList.getInstance().getSTD_W_GB());
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, DAY);
				startActivity(intent);
			}
			
			if(ReviewType.Test2 == currentReviewType && !justFinish) {
				Intent intent = new Intent(FragmentActivityWordTest.this, ActivityWordTestTypeSelect.class);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL, LEVEL);
				intent.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL_TYPE, LEVEL_TYPE);
				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, DAY);
				intent.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_STD_DB, Constant.STD_GB_S);
				intent.putExtra(IS_COME_FROM_REVIEW, isComeReview);
				startActivity(intent);
			}
			
			if(TestType.Quiz == currentTestType && isQuizComplete)
				setResult(ActivityWordTestQuizSelect.WORDTEST_QUIZ_COMPLETE);
			
			if(TestType.ReviewTest1 == currentTestType) {
				if(currentNum <= totalQuiz) { 
					finish();
					return;
				}
				gotoReviewHandler.sendEmptyMessage(0);
				return;
			}
			
			if(TestType.ReviewTest2 == currentTestType) {
				if(currentNum <= totalQuiz) { 
					finish();
					return;
				}
				gotoReviewHandler.sendEmptyMessage(1);
				return;
			}

			finish();
		}

		@Override
		public void fail() {
			hideProgress();
			doubleLoginDetected();
		}

		@Override
		public void exception() {
			hideProgress();
			doubleLoginDetected();
		}
	};
	
	
	@SuppressLint("HandlerLeak")
    Handler gotoReviewHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			int msgWhat = msg.what;
			
			if(msgWhat == 0) {
				gotoReviewTest2();
			}else if(msgWhat == 1) {
				gotoReviewTest3();
			}
		}
	};
	
	private void doubleLoginDetected() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Constant.IntentKeys.INTENT_DOUBLE_LOGIN, true);
		startActivity(intent);
	}
	
	/**
	 * 틀린문제가 있을 경우 Practice를 만든다.
	 */
	private void makePractice() {
		
		LogTraceMin.I("TEST1 일경우 틀린문제가 있으면 Pratice를 만든다.");
		ArrayList<VoWordQuest> STD_LIST = new ArrayList<VoWordQuest>();
		
		int i = 1;
		
		for(VoWordQuest voWordQuest : wordsTestInfo.getSTD_LIST()) {
			
			if("N".equals(voWordQuest.getRESULT_YN())) {
				
				LogTraceMin.I("setSTD_NUM :: " + i);
				
				VoWordQuest practiceQuest = VoWordsTestList.getInstance().new VoWordQuest();
				
				practiceQuest.setSTD_STEP(VoWordsTest.CODE_PRACTICE);
				practiceQuest.setSTD_TYPE(VoWordQuest.QUIZ_CODE_PRACTICE);
				practiceQuest.setSTD_W_GB(voWordQuest.getSTD_W_GB());
				practiceQuest.setSTD_GB(voWordQuest.getSTD_GB());
				practiceQuest.setSTD_LEVEL(voWordQuest.getSTD_LEVEL());
				practiceQuest.setSTD_DAY(voWordQuest.getSTD_DAY());
				practiceQuest.setWORD_SEQ(voWordQuest.getWORD_SEQ());
				practiceQuest.setWORD(voWordQuest.getWORD());
				practiceQuest.setMEAN(voWordQuest.getMEAN());
				practiceQuest.setSTD_MP3(voWordQuest.getSTD_MP3());
				practiceQuest.setANSWER(voWordQuest.getWORD());	//정답은 Word로
				practiceQuest.setTIME_USER(0);
				practiceQuest.setSTD_NUM(i);
				practiceQuest.setUSER_ANS(null);
				practiceQuest.setRESULT_YN(null);
				STD_LIST.add(practiceQuest);
				
				setSaveWordsInfo(practiceQuest);
				i++;
			}
		}
		
		//Test1이 전부 맞았을 경우, 모두 RESULT_YN = "Y" 로 저장한다.
		if(1 == i) {
			LogTraceMin.I("Test1 전부 맞았을 경우 Practice에 전부 담는다.");
			test1AllCorrect = true;
			for(VoWordQuest voWordQuest : wordsTestInfo.getSTD_LIST()) {
					
				VoWordQuest practiceQuest = VoWordsTestList.getInstance().new VoWordQuest();
				practiceQuest = voWordQuest;
				
				practiceQuest.setSTD_STEP(VoWordsTest.CODE_PRACTICE);
				practiceQuest.setSTD_TYPE(VoWordQuest.QUIZ_CODE_PRACTICE);
				practiceQuest.setANSWER(voWordQuest.getWORD());	//정답은 Word로
				practiceQuest.setTIME_USER(0);
				practiceQuest.setSTD_NUM(voWordQuest.getSTD_NUM());
				practiceQuest.setUSER_ANS(voWordQuest.getWORD());
				practiceQuest.setRESULT_YN("Y");
				STD_LIST.add(practiceQuest);
				
				setSaveWordsInfo(practiceQuest);
			}
		}
		VoWordsTestList.getInstance().getPRACTICE().setSTD_LIST(STD_LIST);
	}
	
	/**
     * 정답 이미지 표시
     */
	protected void showCorrectImg(boolean answer_result) {
		ll_check_quiz.setVisibility(View.VISIBLE);
		if(answer_result) iv_check_quiz.setBackgroundResource(R.drawable.ic_o);
		else iv_check_quiz.setBackgroundResource(R.drawable.ic_x);
	}
	
	/**
	 * 정답 이미지 숨기기
	 */
	protected void hideCorrentImg() {
		ll_check_quiz.setVisibility(View.GONE);
	}
	
	/**
     * 정답 처리
     */
	protected void processCorrect() {
		
		quizInfo.setRESULT_YN("Y");
		saveInnerDB(true);
		
		String filePath = "android.resource://moumou.co.kr.smartwords/raw/dingdong";
		
		// 정답 음원 재생
		playSound(filePath, SOUND_PLAY_CORRECT, true);
	}
	/**
	 * 오답처리
	 */
	protected void processIncorrect() {

		quizInfo.setRESULT_YN("N");
		saveInnerDB(false);
		
		String filePath = "android.resource://moumou.co.kr.smartwords/raw/incorrect";
		
		// 오답 음원 재생
		playSound(filePath, SOUND_PLAY_INCORRECT, false);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_temp_close:
			if(isComeQuiz) closeQuizAll = true;
			onBackPressed();
			break;
		}
		
	}

}
