package kr.co.moumou.smartwords.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.CustomEditText;
import kr.co.moumou.smartwords.customview.CustomTextView;
import kr.co.moumou.smartwords.customview.ViewWordsBogi;
import kr.co.moumou.smartwords.customview.ViewWordsBogi.WordBogiClickListener;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsBogi;

public class FragmentTest2 extends BaseFragmentWordTest {

	private ImageButton btn_player;
	private LinearLayout ll_quiz_layout, ll_quiz_bogi;
	private LinearLayout ll_answer;
	private TextView tv_answer;
	
	private int focusPos = 0;	//포커스 위치
	private int hasUpper = 0;	//대문자를 가지고 있을 경우 1
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_words_test2, container, false);
		setLayoutSetting(v);
		return v;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		isDestroy = true;
	}
	
	@Override
	public void setLayoutSetting(View v) {
		super.setLayoutSetting(v);
		btn_player = (ImageButton) v.findViewById(R.id.btn_player);
		DisplayUtil.setLayout(wordTestAcitivity, 102, 102, btn_player);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 16, 0, 0, 0, btn_player);
		btn_player.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mediaPlayer.isPlaying()) return;
				playMp3(quizInfo.getSTD_MP3());
			}
		});
		
		hasMp = true;
		setNextBtn(v);
		
		TextView tv_quiz = (TextView) v.findViewById(R.id.tv_quiz);
		//DisplayUtil.setLayoutMargin(wordTestAcitivity, 16, 10, 0, 0, tv_quiz);
		
		ll_quiz_layout = (LinearLayout) v.findViewById(R.id.ll_quiz_layout);
		ll_quiz_bogi = (LinearLayout) v.findViewById(R.id.ll_quiz_bogi);
		ll_answer = (LinearLayout) v.findViewById(R.id.ll_answer);
		//DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 20, 0, 0, ll_quiz_layout);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 15, 0, 0, ll_quiz_bogi);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 15, 0, 0, ll_answer);
		tv_answer = (TextView) v.findViewById(R.id.tv_answer);
		DisplayUtil.setLayoutHeight(wordTestAcitivity, 100, tv_answer);
		DisplayUtil.setLayoutPadding(wordTestAcitivity, 20, 10, 20, 0, tv_answer);
		
		addBogiList();
		setQuizLayout();
	}
	
	private ViewWordsBogi[] bogiList = new ViewWordsBogi[4];
	
	/**
	 * 보기 리스트
	 */
	private void addBogiList() {
			
		if(null == quizInfo.getBOGI_LIST() || quizInfo.getBOGI_LIST().size() == 0) return;
		
		for(int i = 0; i < quizInfo.getBOGI_LIST().size(); i++ ) {
			ViewWordsBogi viewBogi = new ViewWordsBogi(wordTestAcitivity);
			viewBogi.setSize(260, 95, 25);
			viewBogi.setBogiClickListener(bogiClickListener);
			bogiList[i] = viewBogi;
			ll_quiz_bogi.addView(viewBogi);
		}
	}
	
	private void resetBogi() {

		if(null == quizInfo.getBOGI_LIST() || quizInfo.getBOGI_LIST().size() == 0) return;
		
		for(int i = 0; i < bogiList.length; i++) {
			VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
			bogiList[i].resetBg();
			bogiList[i].setText(bogi.getBOGI_WORD());
			bogiList[i].setEnableClick();
		}
	}
	
	/**
	 * 보기정답 Listener
	 */
	WordBogiClickListener bogiClickListener = new WordBogiClickListener() {
		
		@Override
		public void onClick(String str) {
			stopTimerAni();
			cantAnswerInput();
			LogTraceMin.I("answer onClick():: " + str);
//			String user_answer = (quizInfo.getUSER_ANS() + Constant.GUBUN_SHARP + str).trim();
			quizInfo.setUSER_ANS(str);
			wordTestAcitivity.nextQuiz(checkAnswerNextQuiz());
		}
	};
	
	private void cantEditInput() {
		for(EditText editText : editTextArray) {
			editText.setEnabled(false);
			editText.setFocusable(false);
		}
		setNextBtnEnableState(false);
	}
	
	private void checkEditAnswer() {
		
		isEnableEidtText = false;
		
		StringBuffer answer = new StringBuffer();

		int i = 0;
		for(EditText editText : editTextArray) {
			editText.setEnabled(false);
			editText.setFocusable(false);
			answer.append(editText.getText());
			i++;
		}
		for(TextSign textSign : textSignArray) {
			if(textSign.index > answer.length()) break;
			answer.insert(textSign.index, textSign.str);
		}
		
		String userAnswer = StringUtil.isNull(quizInfo.getUSER_ANS()) ?
				Constant.GUBUN_SHARP  :  Constant.GUBUN_SHARP + quizInfo.getUSER_ANS();
		quizInfo.setUSER_ANS(answer.toString() +  userAnswer);
		
		LogTraceMin.I("editAnswer :: " + quizInfo.getUSER_ANS());
	}
	
	/**
	 * Edit 정답 확인
	 */
	private void showEditAnswer() {
		
		boolean isCorrect = true;

		int i = 0;
		for(EditText editText : editTextArray) {
			if(worngEditText(editText.getText().toString(), i)) {
				editText.setBackgroundResource(R.drawable.bg_textfield_wrong);
				editText.setTextColor(wordTestAcitivity.getResources().getColor(R.color.words_txt_wrong));
				isCorrect = false;
			}
			editText.setEnabled(false);
			editText.setFocusable(false);
			i++;
		}
		
		if(!isCorrect)
			ll_answer.setVisibility(View.VISIBLE);
	}
	
	private CustomEditText[] editTextArray = null;
	private ArrayList<TextSign> textSignArray = new ArrayList<TextSign>();
	String answer_except_sign = "";
	/**
	 * 정답에 따른 레이아웃 세팅
	 */
	private void setQuizLayout() {
		
		resetSetting();//모든 세팅 초기화
		
		if(ll_quiz_layout.getChildCount() > 0) ll_quiz_layout.removeAllViews();
		
		//quizInfo.setANSWER("I'm a boy test-tt");
		//String word = quizInfo.getWORD().trim();
		String word = "";
		if(quizInfo.getANSWER().contains(Constant.GUBUN_SHARP)) {
			word = quizInfo.getANSWER().split(Constant.GUBUN_SHARP)[0];
		}else word = quizInfo.getANSWER();
		
		answer_except_sign = word.replaceAll(Constant.GUBUN_SPACE, "").replaceAll(Constant.GUBUN_APO, "").replaceAll(Constant.GUBUN_HYPHEN, "");
		int size = answer_except_sign.length();
		tv_answer.setText(word);
		
		editTextArray = new CustomEditText[size];
		String str = "";
		
		int editTextIndex = 0;	//editText만 Array에 담기 위해서
		
		LogTraceMin.I("answer :: " + word.length());
		LogTraceMin.I("size :: " + size);
		
		for(int i = 0; i < word.length(); i++) {
			str = Character.toString(word.charAt(i));
			
			if(Constant.GUBUN_SPACE.equals(str)) {
				LinearLayout ll = new LinearLayout(wordTestAcitivity);
				ll_quiz_layout.addView(ll);
				DisplayUtil.setLayout(wordTestAcitivity, 20, 0, ll);
				textSignArray.add(new TextSign(i, str));
			}
			else if(Constant.GUBUN_APO.equals(str)) {
				ll_quiz_layout.addView(getTextView(str));
				textSignArray.add(new TextSign(i, str));
			}else if(Constant.GUBUN_HYPHEN.equals(str)) {
				ll_quiz_layout.addView(getTextView(str));
				textSignArray.add(new TextSign(i, str));
			}else{
				if(i == 0 && size > 1 && isUpper(str)) { focusPos = 1; hasUpper = 1;} else str = "";
				CustomEditText editText = getEditText(str);
//				editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
				editText.setTag(editTextIndex);
				editTextArray[editTextIndex] = editText;
				editText.setEnabled(false);
				editText.setFocusable(false);
				editText.setFocusableInTouchMode(false);
				ll_quiz_layout.addView(editText);
				DisplayUtil.setLayout(wordTestAcitivity, 64, 64, editText);
				DisplayUtil.setLayoutMargin(wordTestAcitivity, 5, 0, 5, 0, editText);
				editTextIndex++;
			}
		}
	}
	
	private void setEditText(boolean state) {
		for(EditText editText : editTextArray) {
			editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
			editText.setEnabled(state);
			editText.setFocusable(state);
			editText.setFocusableInTouchMode(state);
		}
		if(state) {
			
			//혹시 Pos위치가 length보다 커 졌을 경우 최대 index로
			if(editTextArray.length - 1 < focusPos) 
				focusPos = editTextArray.length - 1;
			
			editTextArray[focusPos].requestFocus();
			showSoftkeyboard(editTextArray[focusPos]);
		}
	}
	
	@Override
	protected void startTimerAni() {
		LogTraceMin.I("Start Timer Ani!");
		if(null != mediaPlayer && wordTestAcitivity.stopReadyPlay == false) {
			mediaPlayer.start();
			wordTestAcitivity.stopReadyPlay = true;
		}
		setTextTimer(total_time);
		resetTimerDraw();
	}
	
	boolean isEnableEidtText = true;
	
	@Override
	protected void cantAnswerInput() {
		LogTraceMin.I("cantAnswerInput()");
		for(ViewWordsBogi btn : bogiList) {
			btn.setNotEnableClick();
		}
	}
	
	/**
	 * 모든 설정값 초기화
	 */
	private void resetSetting() {
		focusPos = 0;
		hasUpper = 0;
		ll_answer.setVisibility(View.GONE);
		textSignArray.clear();
		isBackEnable = false;
		answer_except_sign = "";
		isEnterEnable = false;
		isEnableEidtText = true;
		isPlayedMp = false;
		ll_quiz_bogi.setVisibility(View.INVISIBLE);
		resetBogi();
		//setNextBtnEnableState(false);
	}
	
	private CustomEditText getEditText(String str) {
		CustomEditText editText = new CustomEditText(wordTestAcitivity);
		editText.setBackgroundResource(R.drawable.words_bg_textfield);
		editText.setTextSizeCustom(29);
		editText.setText(str);
		editText.setGravity(Gravity.CENTER);
		editText.setTextColor(Color.BLACK);
		editText.setMaxLength(1);
		editText.setPrivateImeOptions("defaultInputmode=english;");
		editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
		//editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setOnFocusChangeListener(focusListener);
		editText.addTextChangedListener(watcher);
		editText.setOnEditorActionListener(editorListener);
		return editText;
	}
	
	private TextView getTextView(String str) {
		CustomTextView textView = new CustomTextView(wordTestAcitivity);
		textView.setTextSizeCustom(29);
		textView.setText(str);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.BLACK);
		return textView;
	}
	
	/**
	 * 대문자 체크
	 */
	private boolean isUpper(String str) {
		boolean b = Pattern.matches("[A-Z]", str);
		return b;
	}
	
	OnEditorActionListener editorListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			final boolean isEnterEvent = event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
			final boolean isEnterUpEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_UP;
//			final boolean isEnterDownEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_DOWN;

			if(actionId == EditorInfo.IME_ACTION_DONE || isEnterUpEvent){
				cantEditInput();
				ll_quiz_bogi.setVisibility(View.VISIBLE);
				isEnterEnable = false;
			}
//			if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//
//				if (event.getAction() != KeyEvent.ACTION_UP){
//					if(isEnterEnable) {
//						//cantAnswerInput();
//						//checkEditAnswer();
//						cantEditInput();
//						ll_quiz_bogi.setVisibility(View.VISIBLE);
//						isEnterEnable = false;
//					}
//				}
//			}
			return false;
		}
	};
	
	OnFocusChangeListener focusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			int index = (Integer) v.getTag();
			focusPos = index;
		}
	};
	
	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			if(!isEnableEidtText) return;
			
			if(s.toString().length() > 0) {
				if(editTextArray.length > focusPos + 1) {
					focusPos++;
					editTextArray[focusPos].requestFocus();
					isBackEnable = true;
				}else if(editTextArray.length == focusPos + 1){
					focusPos++;
					isBackEnable = true;
				}
			}else{
				//LogTraceMin.I("onTextChanged 0 :::" + focusPos);
				isBackEnable = true;
			}
			
			LogTraceMin.I("s.toString().length() :: " + s.toString().length());
			LogTraceMin.I("focusPos :: " + focusPos);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {}
	};
			
	@Override
	protected void resetQuizInfo() {
		if(ll_quiz_layout != null) setQuizLayout();
		String pathSound = quizInfo.getSTD_MP3();
		playMp3(pathSound);
	}
	
	private void playMp3(String pathSound) {
		
		playSoundDelayed(pathSound, 500, new OnCompleteMediaPlayerListener() {
			@Override
			public void OnCompleteMediaPlayer() {
				LogTraceMin.I("음성 완료");
				if(isDestroy || isPlayedMp) return;
				isPlayedMp = true;
				setEditText(true);
				stopAni = false;
				runningDraw();
				isEnterEnable = true;
				setNextBtnEnableState(true);
				//setNextBtnEnableState(true);
			}
		});
	}
	
	@Override
	protected boolean checkAnswerNextQuiz() {
		
//		StringBuffer answer = new StringBuffer();
//		
//		int i = 0;
//		for(EditText editText : editTextArray) {
//			if(worngEditText(editText.getText().toString(), i)) {
//				editText.setBackgroundResource(R.drawable.bg_textfield_wrong);
//				editText.setTextColor(wordTestAcitivity.getResources().getColor(R.color.words_txt_wrong));
//			}
//			editText.setEnabled(false);
//			editText.setFocusable(false);
//			answer.append(editText.getText());
//			i++;
//		}
//		for(TextSign textSign : textSignArray) {
//			if(textSign.index > answer.length()) break;
//			answer.insert(textSign.index, textSign.str);
//		}
		checkEditAnswer();
		String user_answer = quizInfo.getUSER_ANS();
		//if(StringUtil.isNull(user_answer)) quizInfo.setUSER_ANS(Constant.GUBUN_SHARP);
		boolean isCorrect = quizInfo.getANSWER().equals(user_answer);
		return isCorrect;
	}
	
	/**
	 * 문제가 틀렸을 경우 배경표시
	 */
	private boolean worngEditText(String str, int index) {
		String result = Character.toString(answer_except_sign.charAt(index));
		return !result.equals(str);
	}

	@Override
	protected void showCheckAnswer(boolean isCorrect) {
		
		if(isCorrect) return;
		
		LogTraceMin.I("showCheckAnswer :: " + isCorrect);
		LogTraceMin.I("userAnswer :: " + quizInfo.getUSER_ANS());
		LogTraceMin.I("answer :: " + quizInfo.getANSWER());
		
		LogTraceMin.I("contains :: " + quizInfo.getANSWER().contains(Constant.GUBUN_SHARP));
		
		//if(null == quizInfo.getUSER_ANS()) quizInfo.setUSER_ANS("");;
		if(quizInfo.getUSER_ANS().contains(Constant.GUBUN_SHARP) && quizInfo.getANSWER().contains(Constant.GUBUN_SHARP)) {
			//정답과 유저 정답 둘 다 #포함 했을 경우에만
			
//			String[] userArr = quizInfo.getUSER_ANS().split(Constant.GUBUN_SHARP);
//			LogTraceMin.I("userArr.length :: " + userArr.length);
//			
//			if(userArr.length == 0) {
//				
//				//유저가 아무것도 선택하지 않았을 경우
//				String answer = quizInfo.getANSWER().split(Constant.GUBUN_SHARP)[1];
//				
//				LogTraceMin.I("아무것도 선택하지 않았을 경우");
//				
//				for(int i = 0; i < bogiList.length; i++) {
//					VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
//					if(answer.equals(bogi.getBOGI_WORD())) bogiList[i].setCorrentBg();
//				}
//				
//				ll_quiz_bogi.setVisibility(View.VISIBLE);
//				showEditAnswer();
//				
//				return;
//			}		
//			if(userArr.length > 1) {
//			
//				//유저가 하나라도 선택 했을 경우
//				//String user = quizInfo.getUSER_ANS().split(Constant.GUBUN_SHARP)[1];
//				String answer = quizInfo.getANSWER().split(Constant.GUBUN_SHARP)[1];
//				
//				LogTraceMin.I("하나라도 선택 했을 경우");
//				LogTraceMin.I("user :: " + quizInfo.getUSER_ANS());
//				LogTraceMin.I("answer :: " + quizInfo.getANSWER());
//				
//				if(!user.equals(answer)) {
//					for(int i = 0; i < bogiList.length; i++) {
//						VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
//						
//						if(answer.equals(bogi.getBOGI_WORD())) bogiList[i].setCorrentBg();
//					}
//				}
//				ll_quiz_bogi.setVisibility(View.VISIBLE);
//				showEditAnswer();
//				
//				return;
//			}
			
			String answer = quizInfo.getANSWER().split(Constant.GUBUN_SHARP)[1];
			String[] userArr = quizInfo.getUSER_ANS().split(Constant.GUBUN_SHARP);
			boolean isBogiAnswer = false;
			
			if(userArr.length > 1) {
				String user = quizInfo.getUSER_ANS().split(Constant.GUBUN_SHARP)[1];
				if(!user.equals(answer)) isBogiAnswer = true;
			}else isBogiAnswer = true;
			
			if(isBogiAnswer) {
				for(int i = 0; i < bogiList.length; i++) {
					VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
					if(answer.equals(bogi.getBOGI_WORD())) bogiList[i].setCorrentBg();
				}
			}
			ll_quiz_bogi.setVisibility(View.VISIBLE);
			showEditAnswer();
		
		}else{
			LogTraceMin.I("예전 Test2");
			for(int i = 0; i < bogiList.length; i++) {
				VoWordsBogi bogi = quizInfo.getBOGI_LIST().get(i);
				if(quizInfo.getANSWER().equals(bogi.getBOGI_WORD())) bogiList[i].setCorrentBg();
			}
		}
		
//		String[] userAnswers =  userAnswer.split(Constant.GUBUN_SHARP);
//		String[] userAn
//		
//		if(userAnswers.length > 1) {
//			
//			String user = userAnswers[1];
//			
//			
//			
//		}
		
		
	}
	
	/**
	 * 정답 기호를 체크를 위한 class
	 */
	class TextSign {
		public int index;
		public String str;
		
		public TextSign(int index, String str) {
			this.index = index;
			this.str = str;
		}
	}

	boolean isBackEnable = false;	//back버튼 가능
	
	@Override
	protected void onKeyUp(int keyCode) {
		if(keyCode == KeyEvent.KEYCODE_ENTER && isEnterEnable) {	//엔터시 다음 문제
			if(!isEnterEnable) return;
			//cantAnswerInput();
			//checkEditAnswer();
			cantEditInput();
			ll_quiz_bogi.setVisibility(View.VISIBLE);
			isEnterEnable = false;
		}else if(keyCode == KeyEvent.KEYCODE_DEL) {
			if(!isEnterEnable) return;
				
			LogTraceMin.I("focusPos :: " + focusPos);
			
			if(focusPos > hasUpper && isBackEnable && isEnableEidtText) {
				isBackEnable = false;
				focusPos--;
				editTextArray[focusPos].setText("");
				editTextArray[focusPos].requestFocus();				
			}
		}
	}
}


