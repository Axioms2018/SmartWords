package kr.co.moumou.smartwords.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.ViewDecibel.RequestAvgDecibelListener;
import kr.co.moumou.smartwords.customview.ViewWordsDecibel;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.LogUtil;

public class FragmentPractice extends BaseFragmentWordTest {

	private LinearLayout ll_decibel;
	private LinearLayout ll_quiz_layout;
	private LinearLayout ll_line;
	private TextView tv_quiz_en;
	private TextView tv_quiz_ko;
	private ImageView[] startBtns;
	private EditText et_answer;
	
	private int[] imageBtnIds = {R.id.iv_start0, R.id.iv_start1, R.id.iv_start2, R.id.iv_start3};
	
	private ViewWordsDecibel decibelView;
	private int currentPos;
	private int mDuration;
	private Button btn_next;
	//private boolean isDestroy = false;
	
	//private MediaPlayer wordsMediaPlayer; // 음원 재생용 플레이어
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_words_practice, container, false);
		setLayoutSetting(v);
		return v;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		isDestroy = true;
		
		try {
			startHandler.removeCallbacksAndMessages(null);
			decibelView.destroy();
		} catch (Exception e) {
//			doException(e, "FragmentPractice onDestroy()");
		}
	}
	
	@Override
	public void setLayoutSetting(View v) {
		super.setLayoutSetting(v);
		
		ll_decibel = (LinearLayout) v.findViewById(R.id.ll_decibel);
		ll_quiz_layout = (LinearLayout) v.findViewById(R.id.ll_quiz_layout);
		ll_line = (LinearLayout) v.findViewById(R.id.ll_line);
		tv_quiz_en = (TextView) v.findViewById(R.id.tv_quiz_en);
		tv_quiz_ko = (TextView) v.findViewById(R.id.tv_quiz_ko);
		et_answer = (EditText) v.findViewById(R.id.et_answer);
		et_answer.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		et_answer.setFocusableInTouchMode(false);
		
		//최대길이 제한
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(50);
		et_answer.setFilters(FilterArray);
		
		decibelView = new ViewWordsDecibel(wordTestAcitivity);
		ll_decibel.addView(decibelView);
		
		hasMp = true;
		setNextBtn(v);
		
		startBtns = new ImageView[imageBtnIds.length];
		ImageView iv = null;
		for(int i = 0; i < imageBtnIds.length; i++) {
			iv = (ImageView) v.findViewById(imageBtnIds[i]);
			startBtns[i] = iv;
			DisplayUtil.setLayoutMargin(getActivity(), 5, 13, 5, 10, startBtns[i]);
			DisplayUtil.setLayout(getActivity(), 14, 14, startBtns[i]);
		}
		
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 34, 25, 0, 0, ll_decibel);
		//DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 30, 0, 35, ll_quiz_layout);
		DisplayUtil.setLayout(wordTestAcitivity, 1, 90, ll_line);
		DisplayUtil.setLayout(wordTestAcitivity, 540, 95, et_answer);
		
		decibelView.setRequestAvgDecibel(new RequestAvgDecibelListener() {
			
			@Override
			public void requestAvgDecibel(int avgDecibel) {
				//LogTraceMin.I("deceibel :: " + avgDecibel);
				
			}
		});
		
		tv_quiz_en.setText(quizInfo.getWORD());
		tv_quiz_ko.setText(quizInfo.getMEAN());
	}
	
	@Override
	protected void startTimerAni() {
		LogTraceMin.I("Start Timer Ani!");
		if(null != mediaPlayer && wordTestAcitivity.stopReadyPlay == false) {
			mediaPlayer.start();
			wordTestAcitivity.stopReadyPlay = true;
		}
	}

	@Override
	protected void resetQuizInfo() {
		resetSetting();
		startQuest();
	}

	@Override
	protected boolean checkAnswerNextQuiz() {
		String user_answer = et_answer.getText().toString().trim();
		quizInfo.setUSER_ANS(user_answer);
		if(quizInfo.getANSWER().contains(Constant.GUBUN_SHARP)) {
			user_answer = user_answer + Constant.GUBUN_SHARP + quizInfo.getMEAN();
		}
		boolean isCorrect = quizInfo.getANSWER().equals(user_answer);
		return isCorrect;
	}

	@Override
	protected void showCheckAnswer(boolean isCorrect) {
		// TODO Auto-generated method stub
		
	}
	
	private void resetSetting() {
		if(null == tv_quiz_en) return;
		
		LogTraceMin.I("resetSetting");
		
		tv_quiz_en.setText(quizInfo.getWORD());
		tv_quiz_ko.setText(quizInfo.getMEAN());
		et_answer.setText("");
		blockEditText(false);
		setNextBtnEnableState(false);
		decibelView.hideSpeechBubble();
		currentPos = 0;
		mDuration = 0;
		wordTestAcitivity.correntChance = 1;
		for(int i = 0; i < startBtns.length; i++)
			startBtns[i].setBackgroundResource(R.drawable.icon_think_b);
	}
	
	/**
	 * EidtText Enable 여부
	 */
	private void blockEditText(boolean isBlock) {
		et_answer.setEnabled(isBlock);
		et_answer.setFocusable(isBlock);
		et_answer.setFocusableInTouchMode(isBlock);
		
		if(isBlock) et_answer.requestFocus();
		else ll_quiz_layout.requestFocus();
	}
	
	
	private void startQuest() {
		
		String pathSound = quizInfo.getSTD_MP3();
		
		mDuration = playTime(pathSound);
		
		playSoundDelayed(pathSound, 500, new OnCompleteMediaPlayerListener() {
			@Override
			public void OnCompleteMediaPlayer() {
				LogTraceMin.I("음성 완료");
				if(!isDestroy) decibelView.doDecibelStart();
				startHandler.sendEmptyMessageDelayed(0, 0);
			}
		});
	}
	
	protected void cantAnswerInput() {
		blockEditText(false);
		setNextBtnEnableState(false);
	}

    Handler startHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			LogUtil.d("startHandler");
			
			if (isDestroy) return;
			
			try {
				LogUtil.d("currentPos : " + currentPos);
				if (currentPos < 4) {
					
					switch (currentPos) {
					case 0:
						break;
					case 1:
						tv_quiz_en.setVisibility(View.INVISIBLE);
						break;
					case 2:
						tv_quiz_en.setVisibility(View.VISIBLE);
						break;
					case 3:
						tv_quiz_ko.setVisibility(View.INVISIBLE);
						break;
					default:
						break;
					}
					
					startBtns[currentPos].setBackgroundResource(R.drawable.icon_think_a);
					startHandler.sendEmptyMessageDelayed(0, mDuration);
				
				}else{
					blockEditText(true);
					isEnterEnable = true;
					setNextBtnEnableState(true);
					showSoftkeyboard(et_answer);
					tv_quiz_ko.setVisibility(View.VISIBLE);
					boolean isBigVoice = decibelView.isBigVoice();
					decibelView.doDecibelStop();
					LogUtil.d("isBigVoice : " + isBigVoice);
				}
				currentPos++;
				LogUtil.d("currentPos plus : " + currentPos);
			} catch (Exception e) {
			}
		}
		
	};
	
	protected boolean isPractice() {
		return true;
	}

    protected void addChance() {
		blockEditText(true);
		setNextBtnEnableState(true);
		isEnterEnable = true;
		et_answer.setText("");
		showSoftkeyboard(et_answer);
	}

    @Override
	protected void onKeyUp(int keyCode) {
		//LogTraceMin.I("isEnterEnable :: " + isEnterEnable);
		if(keyCode == KeyEvent.KEYCODE_ENTER && isEnterEnable) {	//엔터시 다음 문제
			cantAnswerInput();
			isEnterEnable = false;
			wordTestAcitivity.nextQuiz(checkAnswerNextQuiz());
		}
	}
	
}


