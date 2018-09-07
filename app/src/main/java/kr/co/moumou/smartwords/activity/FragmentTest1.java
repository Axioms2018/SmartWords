package kr.co.moumou.smartwords.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.ViewWordsBogi;
import kr.co.moumou.smartwords.customview.ViewWordsBogi.WordBogiClickListener;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsBogi;

public class FragmentTest1 extends BaseFragmentWordTest {

	private ImageButton btn_player;
	private LinearLayout ll_bogi = null;
	private TextView tv_quiz_word = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_words_test1, container, false);
		setLayoutSetting(v);
		return v;
	}
	
	@Override
	public void setLayoutSetting(View v) {
		super.setLayoutSetting(v);
		tv_quiz_word = (TextView) v.findViewById(R.id.tv_quiz_word);
		if(VoWordQuest.QUIZ_CODE_TEST1_K.equals(wordTestAcitivity.getCurrtQuizType())) 
			tv_quiz_word.setText(quizInfo.getWORD());
		else tv_quiz_word.setText(quizInfo.getMEAN());
		ll_bogi = (LinearLayout) v.findViewById(R.id.ll_bogi);
		
		btn_player = (ImageButton) v.findViewById(R.id.btn_player);
		
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 0, 0, 30, tv_quiz_word);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 0, 0, 30, ll_bogi);
		DisplayUtil.setLayout(wordTestAcitivity, 102, 102, btn_player);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 40, 90, 0, 0, btn_player);
		
		addBogiList();
		setQuizLayout();
	}
	
	private ViewWordsBogi[] bogiList = new ViewWordsBogi[4];
	
	private void addBogiList() {
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout layout = null;
		
		for(int i = 0; i < quizInfo.getBOGI_LIST().size(); i++) {
		
			//VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
			
			if(i % 2 == 0) {
				layout = new LinearLayout(wordTestAcitivity);
				layout.setLayoutParams(params);
				DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 0, 0, 20, layout);
				ll_bogi.addView(layout);
			}
			ViewWordsBogi viewBogi = new ViewWordsBogi(wordTestAcitivity);
//			viewBogi.setText(bogi.getBOGI_WORD());
			bogiList[i] = viewBogi;
			viewBogi.setBogiClickListener(bogiClickListener);
			layout.addView(viewBogi);
		}
	}
	
	private void setQuizLayout() {
		
		if(tv_quiz_word == null) return;
		
		resetSetting();
		
		if(VoWordQuest.QUIZ_CODE_TEST1_K.equals(wordTestAcitivity.getCurrtQuizType())) {
			tv_quiz_word.setText(quizInfo.getWORD());
			playMp3(quizInfo.getSTD_MP3());
			isEnableAnswerInput(false);
			btn_player.setVisibility(View.VISIBLE);
		}else {
			tv_quiz_word.setText(quizInfo.getMEAN());
			isEnableAnswerInput(true);
			btn_player.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 모든 세팅 초기화
	 */
	private void resetSetting() {
		
		for(int i = 0; i < bogiList.length; i++) {
			VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
			bogiList[i].resetBg();
			bogiList[i].setText(bogi.getBOGI_WORD());
		}
		isPlayedMp = false;
	}
	
	
	WordBogiClickListener bogiClickListener = new WordBogiClickListener() {
		
		@Override
		public void onClick(String str) {
			stopTimerAni();
			cantAnswerInput();
			
			String user_answer = str.trim();
			quizInfo.setUSER_ANS(user_answer);
			boolean isCorrect = quizInfo.getANSWER().equals(user_answer);
			wordTestAcitivity.nextQuiz(isCorrect);
		}
	};
	
	@Override
	protected void cantAnswerInput() {
		isEnableAnswerInput(false);
	}


    private void isEnableAnswerInput(boolean isEnable) {
		for(ViewWordsBogi btn : bogiList) {
			if(isEnable) btn.setEnableClick();
			else btn.setNotEnableClick();
		}
	}

    @Override
	protected void resetQuizInfo() {
		if(tv_quiz_word != null) {
			setQuizLayout();
		}
	}
	
	private void playMp3(String pathSound) {
		
		playSoundDelayed(pathSound, 500, new OnCompleteMediaPlayerListener() {
			@Override
			public void OnCompleteMediaPlayer() {
				LogTraceMin.I("음성 완료");
				if(isDestroy || isPlayedMp) return;
				isPlayedMp = true;
				stopAni = false;
				runningDraw();
				isEnterEnable = true;
				isEnableAnswerInput(true);
			}
		});
	}
	
	@Override
	protected void startTimerAni() {
		LogTraceMin.I("Start Timer Ani!");
		
		LogTraceMin.I("Start Timer Ani!" + wordTestAcitivity.getCurrtQuizType());
		
		if(VoWordQuest.QUIZ_CODE_TEST1_E.equals(quizInfo.getSTD_TYPE())) {
			wordTestAcitivity.stopReadyPlay = true;
			super.startTimerAni();
			return;
		}
		
		if(null != mediaPlayer && wordTestAcitivity.stopReadyPlay == false) {
			mediaPlayer.start();
			wordTestAcitivity.stopReadyPlay = true;
		}
		setTextTimer(total_time);
		resetTimerDraw();
		
//		LogTraceMin.I("Start Timer Ani!");
//		if(null != mediaPlayer && wordTestAcitivity.stopReadyPlay == false) {
//			mediaPlayer.start();
//			wordTestAcitivity.stopReadyPlay = true;
//		}
//		setTextTimer(total_time);
//		stopAni = false;
//		runningDraw();
	}
	
	@Override
	protected boolean checkAnswerNextQuiz() { return false;}

	@Override
	protected void showCheckAnswer(boolean isCorrect) {
		if(!isCorrect) {
			for(int i = 0; i < bogiList.length; i++) {
				VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
				if(quizInfo.getANSWER().equals(bogi.getBOGI_WORD())) bogiList[i].setCorrentBg();
			}
		}
	}

	@Override
	protected void onKeyUp(int keyCode) {}
}
