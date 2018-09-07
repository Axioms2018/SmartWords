package kr.co.moumou.smartwords.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Queue;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.ViewIndicator;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoQustList.VoQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;

public abstract class BaseFragmentWordTest extends Fragment implements OnClickListener {

	protected FragmentActivityWordTest wordTestAcitivity = null;
	protected VoWordsTest wordsTestInfo = null;
	protected VoWordQuest quizInfo = null;
	
	protected boolean isEnterEnable = false;	//Enter 반복적으로 눌리지 않기 위해서
	
	protected String wordsPath = Constant.DATA_PATH + "smartwords/";
	
	private TextView tv_quiz;
	private LinearLayout timer_bg;
	private TextView tv_timer;
	private ViewIndicator viewIndicator;
	private Button btn_next;
	
	protected int total_time = 0;
	
	protected MediaPlayer mediaPlayer = null;
	protected Queue complateMediaPlayerQue = new LinkedList();
	
	protected boolean hasMp = false;		//음성이 있는지 체크 여부를 위해
	protected boolean isPrepareMp = false;	//음성이 있을 경우 음성 준비 체크
	
	protected boolean isPlayedMp = false;	//한번 재생했는지 여부 체크를 위해서
	protected boolean isDestroy = false;
	
	/**
	 * QuizInfo에 맞게 퀴즈 정보 리셋하기
	 */
	protected abstract void resetQuizInfo();
	
	/**
	 * 타임 End시, 혹은 정답 입력시 정답 체크
	 */
	protected abstract boolean checkAnswerNextQuiz();
	
	/**
	 * 정답 보여주기
	 */
	protected abstract void showCheckAnswer(boolean isCorrect);
	
	/**
	 * 정답 입력 된 후 정답 고치지 못하게 하기 위해서
	 */
	protected abstract void cantAnswerInput();
	
	protected abstract void onKeyUp(int keyCode);

	public interface OnCompleteMediaPlayerListener {
		/**
		 * 미디어 플레이어 종료
		 * @throws Exception
		 */
		void OnCompleteMediaPlayer() throws Exception;

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wordTestAcitivity = (FragmentActivityWordTest) getActivity();
		wordsTestInfo = wordTestAcitivity.getWordsTestInfo();
		quizInfo = wordTestAcitivity.getQuizInfo();
		wordTestAcitivity.isQuizing = true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		blockPaste();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		isDestroy = true;
		if(null != mHandler) {
			mHandler.removeCallbacks(mRunnable);
			mHandler = null;
		}
		if(null != mRunnable) {
			mRunnable = null;
		}
		if(null != mediaPlayer && mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	/**
	 * 기본 레이아웃 세팅
	 */
	protected void setLayoutSetting(View v) {
		LinearLayout ll_cont_bg = (LinearLayout) v.findViewById(R.id.ll_cont_bg);
		DisplayUtil.setLayoutPadding(wordTestAcitivity, 18, 18, 18, 40, ll_cont_bg);
		tv_quiz = (TextView) v.findViewById(R.id.tv_quiz);
		DisplayUtil.setLayoutMargin(wordTestAcitivity, 16, 18, 0, 0, tv_quiz);
		
		setQuizTxt(wordTestAcitivity.getQuizTypeText(wordTestAcitivity.getCurrtQuizType()));
		resetQuizInfo();
		total_time = wordsTestInfo.getSTD_TIME();
		if(wordTestAcitivity.getHasTimer()) setTimerAni(v);
		
		viewIndicator = (ViewIndicator) v.findViewById(R.id.view_indicator);
		viewIndicator.setMarginValue(0, 18);
		viewIndicator.setSize(22, 22);
		viewIndicator.setImgResource(R.drawable.sel_indicator_dot_word_temp);
		viewIndicator.setPageCount(wordTestAcitivity.getTotalCountQuiz());
		viewIndicator.setCurrentPage2(wordTestAcitivity.getCurrentQuizNum());
		//DisplayUtil.setLayoutMargin(wordTestAcitivity, 0, 60, 0, 0, viewIndicator);
		DisplayUtil.setLayoutHeight(wordTestAcitivity, 22, viewIndicator);
		LinearLayout ll_indicator = (LinearLayout) v.findViewById(R.id.ll_indicator);
		DisplayUtil.setLayoutHeight(wordTestAcitivity, 80, ll_indicator);
		
		LogTraceMin.I("setCurrentPage2 :: " + wordTestAcitivity.getCurrentQuizNum());
	}
	
	protected void setNextBtn(View v) {
		btn_next = (Button) v.findViewById(R.id.btn_next);
		DisplayUtil.setLayout(wordTestAcitivity, 150, 80, btn_next);
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onKeyUp(KeyEvent.KEYCODE_ENTER);
			}
		});
	}
	
	protected void setNextBtnEnableState(boolean isEnable) {
		btn_next.setEnabled(isEnable);
	}
	
	/**
	 * Quiz에 맞게 정보 가져오기
	 */
	protected void setQuizInfo() {
		if(null == wordsTestInfo) return;
		
		this.quizInfo = wordTestAcitivity.getQuizInfo();
		resetQuizInfo();
		blockPaste();
	}
	
	/**
	 * 붙여쓰기 안되게 막기
	 */
	private void blockPaste() {
		ClipboardManager clipService = (ClipboardManager) wordTestAcitivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", "");
        clipService.setPrimaryClip(clipData);
	}
	
	/**
	 * QuizType에 맞게 제목 수정하기
	 */
	protected void setQuizTxt(String txt) {
		if(tv_quiz != null) tv_quiz.setText(txt);
	}
	
	/**
	 * 퀴즈 TimerAni
	 */
	int count = 0;
	protected void setTimerAni(View v) {
		
		timer_bg = (LinearLayout) v.findViewById(R.id.timer_bg);
		tv_timer = (TextView) v.findViewById(R.id.tv_timer);
		tv_timer.setText(Integer.toString(total_time));
		
		DisplayUtil.setLayout(wordTestAcitivity, 81, 81, timer_bg);
		
		mHandler = new Handler();
		
		mRunnable = new Runnable() {
			
			@SuppressLint("NewApi")
			@Override
			public void run() {
				
				if(stopAni) return;
				
				if(Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1){
					if(wordTestAcitivity.isDestroyed()) {
						LogTraceMin.I("wordTestAcitivity.isDestroyed() :: " + wordTestAcitivity.isDestroyed() + "-----------------------------");
						stopTimerAni();
						return;
					}
				}else{
					if(wordTestAcitivity.isFinishing()) {
						stopTimerAni();
						return;
					}
				}
				
				if(total_time == 0) {
					LogTraceMin.I("Timer End");
					isEnterEnable = false;
					cantAnswerInput();
					wordTestAcitivity.nextQuiz(checkAnswerNextQuiz());
					count = 0;
				}else{
					
					if(count < 10) {
						timer_bg.setBackgroundResource(drawArry[count]);
						count++;
						runningDraw();
					}else{
						count = 0;
						LogTraceMin.I("total_time :: " + total_time + "초");
						total_time--;
						setTextTimer(total_time);
						runningDraw();
					}
				}
			}
		};
	}
	
	int[] drawArry = {R.drawable.ani1, R.drawable.ani2, R.drawable.ani3, R.drawable.ani4, R.drawable.ani5, 
						R.drawable.ani6, R.drawable.ani7, R.drawable.ani8, R.drawable.ani9, R.drawable.ani10};
	
	Runnable mRunnable = null;
	Handler mHandler = null;
	boolean stopAni = false;
	
	protected void runningDraw() {
		if(null != mHandler) mHandler.postDelayed(mRunnable, 100);
	}
	
	/**
	 * 키보드 보이기
	 */
	protected void showSoftkeyboard(EditText editText){
		InputMethodManager imm = (InputMethodManager) wordTestAcitivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, 0);
//		editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	}

	/**
	 * TimerText 바꾸기
	 */
	protected void setTextTimer(int time) {
		if(null != tv_timer) tv_timer.setText(Integer.toString(time));
	}
	
	/**
	 * TimerText 바꾸기
	 */
	protected void resetTimerDraw() {
		if(null != timer_bg) timer_bg.setBackgroundResource(drawArry[0]);
	}
	
	/**
	 * 현재 TotalTime 가져오기
	 */
	protected int getTimer() {
		return total_time;
	}
	
	/**
	 * Timer 애니 시작
	 */
	protected void startTimerAni() {
		LogTraceMin.I("Start Timer Ani!");
		if(null != mediaPlayer && wordTestAcitivity.stopReadyPlay == false) {
			mediaPlayer.start();
			wordTestAcitivity.stopReadyPlay = true;
		}
		setTextTimer(total_time);
		stopAni = false;
		runningDraw();
	}
	
	/**
	 * Timer 애니 스톱
	 */
	protected void stopTimerAni() {
		LogTraceMin.I("Stop Timer Ani!");
		count = 0;
		stopAni = true;
	}
	
	/**
	 * Total Timer 리셋
	 */
	protected void resetTotalTime() {
		total_time = wordsTestInfo.getSTD_TIME();
	}
	
	/**
	 * 현재 TotalTime 가져오기
	 */
	protected void setIndicator(int index) {
		if(null != viewIndicator)
			viewIndicator.setCurrentPage2(index);
	}
	
	protected void playSoundDelayed(String fileName) {
		Message msg = new Message();
		msg.obj = fileName;
		mPlaySoundHandler.sendMessageDelayed(msg, 0);
	}
	
	protected void playSoundDelayed(String fileName, long delayTime, OnCompleteMediaPlayerListener listener) {
		complateMediaPlayerQue.offer(listener);
		Message msg = new Message();
		msg.obj = fileName;
		mPlaySoundHandler.sendMessageDelayed(msg, delayTime);
	}
	
	Handler mPlaySoundHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String fileName = (String) msg.obj;
			playSound(fileName);
		}
	};
	
	/**
	 * 음원길이 계산하기
	 * @return
	 */
	public int playTime(String pathSound) {
		
		pathSound = wordsPath + pathSound;
		LogTraceMin.I("playTime path :: " + pathSound);
		
		if (!new File(pathSound).exists())
			return -1;
		
		try {
			
			if (mediaPlayer == null){
				mediaPlayer = new MediaPlayer();
			}else{
				mediaPlayer.reset();
			}
			
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			FileInputStream fs = new FileInputStream(new File(pathSound));
			FileDescriptor fd = fs.getFD();
			mediaPlayer.setDataSource(fd);
			
			mediaPlayer.prepare();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int duration = mediaPlayer.getDuration();
		mediaPlayer.release();
		mediaPlayer = null;
		
		LogTraceMin.I("duration :: " + duration);
		return duration;
	}
	
	/**
	 * 음원재생
	 * @param fileName
	 */
	private void playSound(String fileName) {

		LogTraceMin.I("파일 재생 : " + fileName);
		String path = "";
		
		if(StringUtil.isNull(fileName)) {
			//doException(null, "재생할 파일이 없습니다.\n" + fileName);
			return;
		}
		
		if(fileName.contains(".")) {
			//path = Constant.DATA_PATH + Constant.PATH_WORDS + fileName;
			path = wordsPath + fileName;
		}
		
		LogTraceMin.I("path : " + path);
		
		if(!new File(path).exists()) {
			//doException(null, "재생할 파일이 없습니다.\n" + fileName);
			return;
		}
		
		String errStr = "";
		
		try {
			
			
			if(mediaPlayer == null) {
				errStr = "new MediaPlayer";
				mediaPlayer = new MediaPlayer();
			}else{
				errStr = "media reset";
				if(mediaPlayer.isPlaying()) mediaPlayer.stop();
				mediaPlayer.reset();
			}
			//mediaPlayer.start();
			
			/*Uri mUri = Uri.parse(path);
			mediaPlayer.setDataSource(wordTestAcitivity, mUri);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);*/
			
			FileInputStream fs = new FileInputStream(new File(path));
			FileDescriptor fd = fs.getFD();
			mediaPlayer.setDataSource(fd);
			
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(preparedListener);
			mediaPlayer.setOnCompletionListener(mpCompleteListener);
			
		} catch (Exception e) {
			//doException(null, fileName + " 음원을 재생하는데 오류가 생겼습니다.\n" + e.toString() + " / " + errStr);
		}
	}
	
	OnPreparedListener preparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			
			LogTraceMin.I("preparedListener 음원준비 완료 ");
			isPrepareMp = true;
			
			if(wordTestAcitivity.stopReadyPlay && !isDestroy) 
				mediaPlayer.start();
		}
	};
	
	/**
     * 음원 재생 완료 후 호출됨
     */
	OnCompletionListener mpCompleteListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			try {
				LogTraceMin.I("mpCompleteListener()");
				if (!complateMediaPlayerQue.isEmpty() && !isDestroy) {
					OnCompleteMediaPlayerListener listener = (OnCompleteMediaPlayerListener) complateMediaPlayerQue.poll();
					listener.OnCompleteMediaPlayer();
				}
			} catch (Exception e) {
				//doException(e, "FragmentPractice mpCompleteListener");
			}
				
		}
	};
	
	/**
	 * Practice일 경우에만 Chance를 두번 주기 위해서!!
	 */
	protected boolean isPractice() {
		return false;
	}
	
	/**
	 * 한번 더 기회를 주기 위해서
	 */
	protected void addChance() {}
	
	private VoQuest setExceptionInfo() {
		VoQuest voError = new VoQuest();
		voError.setQUST_SEQ(quizInfo.getWORD_SEQ());
		voError.setPCODE("");
		return voError;
	}
	
//	/**
//	 * 에러 Exception
//	 * @param e
//	 * @param msg
//	 */
//	protected void doException(Exception e, String msg) {
//
//		VoQuest voError = setExceptionInfo();
//		voError.setSYSGB(ConstantsCommParameter.Values.SYSBG_MOUMOU_WORDS);
//
//		ExceptionManager.getInstance().doException(wordTestAcitivity, e, msg, voError, new OnExceptionListener() {
//
//			@Override
//			public void onPrePared() {
//				try {
//					errResult();
//				} catch (Exception e) {
//					ExceptionManager.getInstance().doException(wordTestAcitivity, e);
//				}
//			}
//
//			@Override
//			public void onComplete() {
//				try {
//					errResult();
//				} catch (Exception e) {
//					ExceptionManager.getInstance().doException(wordTestAcitivity, e);
//				}
//			}
//		});
//	}
	
	public void errResult() {
		if (quizInfo != null) {
			wordTestAcitivity.nextQuiz(false);
        }
	}
	
	@Override
	public void onClick(View v) {}

}
