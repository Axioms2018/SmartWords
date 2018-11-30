package kr.co.moumou.smartwords.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dpizarror.autolabel.library.AutoLabelUI;
import com.dpizarror.autolabel.library.KeyboardEventListener;
import com.dpizarror.autolabel.library.Label;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;


public class FragmentTest3 extends BaseFragmentWordTest {
	
	private TextView tv_quiz_ko;
	private LinearLayout ll_cont_quiz;
	private AutoLabelUI ll_quiz;
	private LinearLayout ll_answer;
	private TextView tv_answer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_words_test3, container, false);
		setLayoutSetting(v);
		return v;
	}

	@Override
	public void setLayoutSetting(View v) {
		super.setLayoutSetting(v);
		tv_quiz_ko = (TextView) v.findViewById(R.id.tv_quiz_ko);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 0, 0, 48, tv_quiz_ko);
		ll_cont_quiz = (LinearLayout) v.findViewById(R.id.ll_cont_quiz);
		ll_quiz = (AutoLabelUI) v.findViewById(R.id.ll_quiz);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 34, 0, 34, 0, ll_cont_quiz);
		DisplayUtil.setLayoutPadding(wordTestAcitivity, 20, 20, 20, 10, ll_cont_quiz);
		ll_answer = (LinearLayout) v.findViewById(R.id.ll_answer);

		DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 15, 0, 0, ll_answer);

		tv_answer = (TextView) v.findViewById(R.id.tv_answer);
		DisplayUtil.setLayoutHeight(wordTestAcitivity, 100, tv_answer);
		DisplayUtil.setLayoutPadding(wordTestAcitivity, 30, 20, 30, 10, tv_answer);
		setNextBtn(v);
		
		mHandler = new Handler();
		setQuizLayout();
	}
	
	private void resetSetting() {
		isEnterEnable = true;
		ll_answer.setVisibility(View.INVISIBLE);
		setNextBtnEnableState(true);
	}
	
	private void blockEditText() {
		for(Label label : labelArr) {
			label.blockEditText();
		}
	}
	
	@Override
	protected void cantAnswerInput() {
		blockEditText();
		setNextBtnEnableState(false);
	}
	
	/**
	 * 퀴즈에 맞게 레이아웃 구성
	 */
	private void setQuizLayout() {
		
		resetSetting();
		tv_quiz_ko.setText(quizInfo.getSEN_MEAN());
		
		if(ll_quiz.getChildCount() > 0) ll_quiz.clear();

		signArry.clear();
		labelArr.clear();
		
		int[] answerSizeArr = getAnswerArr(quizInfo.getANSWER());
		
		String quizSentence = quizInfo.getSENTENCE();
		String[] quizAllArr = quizSentence.split(" ");

		Label label = null;
		int i = 0;
		
		for(String str : quizAllArr) {
			LogTraceMin.I("str :: " + str);

			if(str.contains(Constant.GUBUN_SHARP)) {	//add EditText
				LogTraceMin.I("answerSizeArr :: " + answerSizeArr[i]);
				
				label = new Label(getActivity(), true, answerSizeArr[i] * 22, 35, Color.BLACK, R.drawable.bg_textfield_white, 5, keyListener);
				label.setText("");

				ll_quiz.addLabel(label);
				labelArr.add(label);
				if(i == 0) label.onRequestFocus();
				i++;

			}else{	//add TextView
				label = new Label(getActivity(), false, 0, 30, Color.BLACK, Color.TRANSPARENT, 3, keyListener);
				label.setText(str);
				ll_quiz.addLabel(label);
			}
		}
		tv_answer.setText(quizInfo.getANSWER().trim());
		if(labelArr.size() > 0){
			showSoftkeyboard(labelArr.get(0).getEditText());

		}else{
//			doException(null, "문제정보가 잘못 되었습니다.\n" + quizInfo.getSENTENCE());
		}
	}
	
	KeyboardEventListener keyListener = new KeyboardEventListener() {
		
		@Override
		public void onKeyEvent() {
			if(isEnterEnable) {	//엔터시 다음 문제
				isEnterEnable = false;
				stopTimerAni();
				cantAnswerInput();
				wordTestAcitivity.nextQuiz(checkAnswerNextQuiz());
			}
		}
	};
	
	/**
	 * 정답길이 Array 만들기
	 */
	private int[] getAnswerArr(String answer) {
		
		//answer : 정답
		ArrayList<String> answerArray = new ArrayList<String>();
		int[] answerSizeArr = null;
		
		addAnswerArray(answerArray, answer);
		
		int size = answerArray.size();
		answerSizeArr = new int[size];
		
		for(int i = 0; i < answerArray.size(); i++) {
			answerSizeArr[i] = answerArray.get(i).length();
		}
		return answerSizeArr;
	}
	
	/**
	 * 문자열 리스트
	 */
	ArrayList<String> signArry = new ArrayList<>();	//특수문자, 혹은 띄우쓰기 일때 담기
	ArrayList<Label> labelArr = new ArrayList<>();	//EditText Array
	
	private void addAnswerArray(ArrayList<String> answerArray, String answer) {
//		if(answer.contains(Constant.GUBUN_APO)) {
//			for(String aa : answer.split(Constant.GUBUN_APO)){
//				answerArray.add(aa);
//			}
//			signArry.add(Constant.GUBUN_APO);
//
//		}else
		if(answer.contains(Constant.GUBUN_HYPHEN)) {
			for(String aa : answer.split(Constant.GUBUN_HYPHEN)){
				answerArray.add(aa);
			}
			signArry.add(Constant.GUBUN_HYPHEN);
		}else if(answer.contains(Constant.GUBUN_SPACE)) {
			for(String aa : answer.split(Constant.GUBUN_SPACE)){
				answerArray.add(aa);
			}
			signArry.add(Constant.GUBUN_SPACE);
		}else{
			answerArray.add(answer);
		}
	}
	
	@Override
	protected void resetQuizInfo() {
		if(ll_quiz != null) setQuizLayout();
	}

	@Override
	protected boolean checkAnswerNextQuiz() {
		
		StringBuilder userAnswer = new StringBuilder();
		
		if(labelArr.size() > 1) {
			int i = 0;
			for(Label label : labelArr) {
				userAnswer.append(label.getText());
				if(signArry.size() > i) userAnswer.append(signArry.get(i));
				i++;
			}
			
		}else if(labelArr.size() == 1) {
			userAnswer.append(labelArr.get(0).getText());
		}else {
			userAnswer.append(quizInfo.getANSWER());
		}
		
		LogTraceMin.I("Test3 정답 :: " + userAnswer.toString());
		
		String user_answer = userAnswer.toString().trim();
		quizInfo.setUSER_ANS(user_answer);
		boolean isCorrect = quizInfo.getANSWER().equals(user_answer);
		
		return isCorrect;
	}

	@Override
	protected void showCheckAnswer(boolean isCorrect) {
		if(!isCorrect) ll_answer.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onKeyUp(int keyCode) {
		if(keyCode == KeyEvent.KEYCODE_ENTER && isEnterEnable) {	//엔터시 다음 문제
			isEnterEnable = false;
			stopTimerAni();
			cantAnswerInput();
			wordTestAcitivity.nextQuiz(checkAnswerNextQuiz());
		}
	}

}
