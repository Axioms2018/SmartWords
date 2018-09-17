package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.MediaDecibelReader;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoMyInfo;


public class ViewDecibel extends RelativeLayout implements MediaDecibelReader.DecibelCheckListener {

	protected Activity activity;

	protected LayoutInflater inflator;
	
	protected ImageView ivSpeaker;
	protected TextView tvDialog;
	protected LinearLayout lay_big_voice;
	protected ImageView img_state;

	public final static int CHECK_VOICE_EQUALIZER_NOT = 0;
	protected final int CHECK_VOICE_EQUALIZER_SMALL = 1; // 목소리가 작을때 이퀄라이저 이미지 변경
	protected final int CHECK_VOICE_EQUALIZER_BIG = 2; // 목소리가 클때 이퀄라이저 이미지 변경

	public final static int CHECK_VOICE_BUBBLE_NOT = 0;
	protected final int CHECK_VOICE_BUBBLE_SMALL = 1; // 큰소리학습시 목소리가 작을때 텍스트뷰 변경
	protected final int CHECK_VOICE_BUBBLE_BIG = 2; // 큰소리학습시 목소리가 클때 텍스트뷰 변경

	private boolean isBigVoice = false;	//큰소리로 말했다구!
	private boolean isStop = false;
	
	protected RequestAvgDecibelListener requestAvgDecibelListener;
	protected RequestBigVoiceListener requestBigVoiceListener;
	
	private MediaPlayer warningPlayer;
	private final int DURATION_CHANGE_TIME = 200;
	private final int MAX_DURATION = 4;
	private final int MIN_DURATION = -2;
	
	private CustomButton[] speedBtn = new CustomButton[7];

	private DurationChangeListener durationChangeListener;
	
	private boolean isNoStt;
	
	public interface RequestAvgDecibelListener {
		void requestAvgDecibel(int avgDecibel); // 평균 데시벨을 요청
	}
	
	public void setRequestAvgDecibel(RequestAvgDecibelListener requestAvgDecibelListener) {
		this.requestAvgDecibelListener = requestAvgDecibelListener;
	}
	
	public interface RequestBigVoiceListener {
		void requestBigVoice(boolean isBigVoice);
	}
	
	public void setRequestBigVoiceL(RequestBigVoiceListener requestBigVoiceListener) {
		this.requestBigVoiceListener = requestBigVoiceListener; 
	}
	
	public ViewDecibel(FragmentActivity activity) {
		super(activity);
		this.activity = activity;
		
//		DioSTTManager.getInstance().setActivity(activity);
//		DioSTTManager.getInstance().setOnSTTListener(this);
//		DioSTTManager.getInstance().create();
		
		init();
		
		setLayout();
	}
	
	public ViewDecibel(FragmentActivity activity, boolean isNoStt) {
		super(activity);
		this.activity = activity;
		this.isNoStt = isNoStt;
		
		if (!isNoStt) {
//			DioSTTManager.getInstance().setActivity(activity);
//			DioSTTManager.getInstance().setOnSTTListener(this);
//			DioSTTManager.getInstance().create();
		}
		
		init();
		
		setLayout();
	}
	
	private String templateCode;
	public ViewDecibel(FragmentActivity activity, boolean isChangeSpeed, String templateCode, DurationChangeListener durationChangeListener) {
		super(activity);
		this.activity = activity;
		this.templateCode = templateCode;
		this.durationChangeListener = durationChangeListener;
		
//		DioSTTManager.getInstance().setActivity(activity);
//		DioSTTManager.getInstance().setOnSTTListener(this);
//		DioSTTManager.getInstance().create();
		
		init();
		setLayout();
		if(isChangeSpeed){
			changeSpeedDisplay();
			setSpeedLayout();
		}
	}
	
	public ViewDecibel(FragmentActivity activity, boolean isChangeSpeed, String templateCode, DurationChangeListener durationChangeListener, boolean isNoStt) {
		super(activity);
		this.activity = activity;
		this.templateCode = templateCode;
		this.durationChangeListener = durationChangeListener;
		
		init();
		setLayout();
		if(isChangeSpeed){
			changeSpeedDisplay();
			setSpeedLayout();
		}
	}
	
	private void changeSpeedDisplay(){
		lay_speak.setGravity(Gravity.RIGHT);
	}
	
	private LinearLayout lay_speak;
	private View baseView;
	public void init() {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		baseView = inflater.inflate(R.layout.view_decibel, this);
		
		lay_speak = (LinearLayout)baseView.findViewById(R.id.lay_speak);
		
		ivSpeaker = (ImageView)baseView.findViewById(R.id.ivSpeaker);
		tvDialog = (TextView)baseView.findViewById(R.id.tvDialog);
		img_state = (ImageView)baseView.findViewById(R.id.img_state);
		lay_big_voice = (LinearLayout)baseView.findViewById(R.id.lay_big_voice);

		DisplayUtil.setLayout(activity, 126, 88, ivSpeaker);
		DisplayUtil.setLayout(activity, 654, 98, lay_big_voice);

		
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	public void setLayout() {
		
	}
	
	

	// 좌측상단 이퀄라이저 이미지변경
	public void setImgEqualizer(int checkEqualizer) {
		
		if (ivSpeaker == null)
			return;
		
		switch (checkEqualizer) {
		case CHECK_VOICE_EQUALIZER_SMALL:
			ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_01);
			break;
		case CHECK_VOICE_EQUALIZER_BIG:
			ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_05);
			break;
		case CHECK_VOICE_EQUALIZER_NOT:
			ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_00);
			break;
		default:
			break;
		}
		
	}

	// 상단 말풍선 텍스트뷰 변경
	public void setTextSpeechBubble(int checkText) {
		if (tvDialog == null)
			return;
		
		switch (checkText) {
		case CHECK_VOICE_BUBBLE_SMALL:
			lay_big_voice.setVisibility(View.VISIBLE);
			tvDialog.setText(StringUtil.getVoiceMsg(false));
			DisplayUtil.setLayoutPadding(activity,30,0,0,0,tvDialog);
			img_state.setImageResource(R.drawable.icon_dialog_good_bad);
			break;
		case CHECK_VOICE_BUBBLE_BIG:
			lay_big_voice.setVisibility(View.VISIBLE);
			tvDialog.setText(StringUtil.getVoiceMsg(true));
			DisplayUtil.setLayoutPadding(activity,30,0,0,0,tvDialog);
			img_state.setImageResource(R.drawable.icon_dialog_good);
			break;
		case CHECK_VOICE_BUBBLE_NOT:
			tvDialog.setText("");
			lay_big_voice.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	// 데시벨 측정 시작
	public void doDecibelStart() {
		isStop = false;
//		DioSTTManager.getInstance().mp3RecordStart(null);
	}
	
	// 데시벨 측정 시작
	public void doDecibelStart(File recFile) {
		isStop = false;
//		DioSTTManager.getInstance().mp3RecordStart(recFile);
	}
	
	public void doDecibelStop() {
		
		isStop = true;
		
		if(isBigVoice){
			mWarningOkVoice();
		} else {	
			mWarningBigVoice();
		}
		
//		DioSTTManager.getInstance().mp3RecordStop();
		
		isBigVoice = false;
		
	}
	
	public void showVoice() {
		
		
		if(isBigVoice){
//			if (requestBigVoiceListener != null)
//				requestBigVoiceListener.requestBigVoice(true);
			mWarningOkVoice();
		} else {
//			if (requestBigVoiceListener != null)
//				requestBigVoiceListener.requestBigVoice(false);
			mWarningBigVoice();
		}
		
		isBigVoice = false;
		
	}

	public void destroy() {
		
		isStop = true;
//		DioSTTManager.getInstance().mp3RecordStop();
		
	}
	
	// 데시벨 레벨을 프리퍼런스에서 갖고오기
	private int getDecibelLevel() {
		int decibelLevel = 85;

		// 로그인 아이디 검색
		String userId = SharedPrefData.getStringSharedData(activity, SharedPrefData.SHARED_USER_ID_S, "");
		LogUtil.d(
				"[onCreate 함수] {프리퍼런스에 저장된 아이디 검색} (userId : " + userId + ")");

		// 데시벨 레벨 검색
		decibelLevel = Preferences.getValue(activity,
				Preferences.PREF_DECIBEL_LEVEL_ + userId, 75);
		LogUtil.d(
				"[getDecibelLevel 함수] {프리퍼런스에 저장된 데시벨 레벨 검색} (decibelLevel : "
						+ decibelLevel + ")");

		return decibelLevel;
	}

	// 큰소리학습 못했을때 경고
	private void mWarningBigVoice() {
		// 좌측상단 스피커와 안테나 활성화 이미지로 변경
		setImgEqualizer(CHECK_VOICE_EQUALIZER_SMALL);

		// 큰소리 텍스트 보이게하고 큰소리 텍스트 표시
		setTextSpeechBubble(CHECK_VOICE_BUBBLE_SMALL);

		mWarningStartPlay();
	}

	// 큰소리학습을 잘했을때
	private void mWarningOkVoice() {
		// 좌측상단 스피커와 안테나 활성화 이미지로 변경
		setImgEqualizer(CHECK_VOICE_EQUALIZER_BIG);

		// 큰소리 텍스트 보이게하고 참잘했어요 텍스트 표시
		setTextSpeechBubble(CHECK_VOICE_BUBBLE_BIG);

	}

	
	public void onPause() {
//		DioSTTManager.getInstance().mp3RecordStop();
		isBigVoice = false;
		isStop = true;
		
		lay_big_voice.setVisibility(View.INVISIBLE);
		ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_00);
	}

	public void dialogInvisible() {
		setTextSpeechBubble(CHECK_VOICE_BUBBLE_NOT);
	}
	
	public void onResume() {

	}
	
	public void onDestroy() {
		
	}

	@Override
	public void getCheckVoiceDecibel(int mDb, int returnDecibel, int avgDecibel) {

		showEqualizer(mDb);
		
		switch (returnDecibel) {
		case MediaDecibelReader.RETURN_DECIBEL_SMALL_VOICE:
			break;
		case MediaDecibelReader.RETURN_DECIBEL_BIG_VOICE:
			isBigVoice = true;
			break;
		default:
			break;
		}
	}

	public ImageView getIvSpeaker() {
		return ivSpeaker;
	}
	
	public TextView getTvDialog() {
		return tvDialog;
	}
	
	public boolean isBigVoice() {
		return isBigVoice;
	}
	
	public void showEqualizer(final int mDb){
		
		//이퀄라이즈 표시
    	if(activity == null){
			return;
		}
    	
    	activity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				try {
					
					LogUtil.d("mdb : " + mDb);
					if(mDb < Constant.BIC_VOICE_LEVEL_0) {
    					ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_00);
    				}
    				
    				if(mDb >= Constant.BIC_VOICE_LEVEL_0 && mDb < Constant.BIC_VOICE_LEVEL_1) {
    					ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_01);
    				}
    				
    				if(mDb >= Constant.BIC_VOICE_LEVEL_1 && mDb < Constant.BIC_VOICE_LEVEL_2) {
    					ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_02);
    				}
    				
    				if(mDb >= Constant.BIC_VOICE_LEVEL_2 && mDb < Constant.BIC_VOICE_LEVEL_3) {
    					ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_03);
    				}
    				
    				if(mDb >= Constant.BIC_VOICE_LEVEL_3 && mDb < Constant.BIC_VOICE_LEVEL_4) {
    					ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_04);
    				}
    				
    				if(mDb > Constant.BIC_VOICE_LEVEL_4) {
    					ivSpeaker.setBackgroundResource(R.drawable.img_mainbook_equalizer_voice_05);
    				}
				
				} catch (Exception e) {
					e.getStackTrace();
					LogUtil.d("[showEqualizer 함수] {runOnUiThread} {catch => 이퀄라이즈 표시} ( getMessage : " + e.getMessage() + ")");
				}
			}
		});
			    		
	}


//	@Override
//	public void onSTTRecoFail(RecognitionResult result) {
//	}
//
//
//	@Override
//	public void onSTTRecoError(RecognitionResult result) {
//	}
//
//
//	@Override
//	public void onSTTRecoSuccess(RecognitionResult result) {
//	}
//
//
//	@Override
//	public void onSTTCheckDecibel(int mDb, int returnDecibel, int avgDecibel) {
//		if (isStop)
//			return;
//
//		if (requestAvgDecibelListener != null)
//			requestAvgDecibelListener.requestAvgDecibel(avgDecibel);
//
//		showEqualizer(mDb);
//
//		if(mDb >= Constant.BIC_VOICE_LEVELS[(VoMyInfo.getInstance().getLoudLevel()-1)]){
//			isBigVoice = true;
//		}
//
//	}
	
	public void mWarningStartPlay() {
		
		if(warningPlayer == null){
			warningPlayer = MediaPlayer.create(activity, R.raw.student_warning_big_voice);
			warningPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					
				}
			});
		}
		warningPlayer.start();
	}
	
	private CustomTextView tv_current_duration;
//	private EditText editPer;
	//단어학습, 유창성 속도 조절 기능 추가.  Start
	private void setSpeedLayout(){
		
		
		
		LinearLayout lay_speed_change = (LinearLayout)baseView.findViewById(R.id.lay_speed_change);
				
		lay_speed_change.setVisibility(View.VISIBLE);
		baseView.findViewById(R.id.ibt_dec).setOnClickListener(speedChangeListener);
		baseView.findViewById(R.id.ibt_inc).setOnClickListener(speedChangeListener);
		
//		editPer = (EditText) baseView.findViewById(R.id.editPer);
		tv_current_duration = (CustomTextView)baseView.findViewById(R.id.tv_current_duration);
		CustomTextView tv_duration_title = (CustomTextView)baseView.findViewById(R.id.tv_duration_title);
		tv_duration_title.setTextSizeCustom(24);
		tv_current_duration.setTextSizeCustom(30);
		
		DisplayUtil.setLayoutWidth(activity, 64*3, tv_duration_title);
		DisplayUtil.setLayout(activity, 64, 64, baseView.findViewById(R.id.ibt_dec));
		DisplayUtil.setLayout(activity, 64, 64, baseView.findViewById(R.id.ibt_inc));
		DisplayUtil.setLayout(activity, 64, 64, tv_current_duration);
		
		
		DisplayUtil.setLayoutPadding(activity, new int[]{15, 10, 15, 10 }, 	tv_duration_title);
		DisplayUtil.setLayout(activity, 200, 120, lay_speed_change);
		DisplayUtil.setLayoutMargin(activity, 20, 0, 20, 0, lay_speed_change);
		setUserSpeed();
	}
	
	private void setUserSpeed(){
		currentDuration = SharedPrefData.getIntSharedData(activity, SharedPrefData.SHARED_USER_DURATION_VALUE_ID_ +templateCode + "_" +VoMyInfo.getInstance().getUSERID(), 0);
//		currentPer = SharedPrefData.getIntSharedData(activity, SharedPrefData.SHARED_USER_DURATION_TEST_VALUE_ID_ +templateCode + "_" +VoMyInfo.getInstance().getUSERID(), 20);
		if(currentDuration <= 0){
			tv_current_duration.setText(Math.abs(currentDuration)+ "");
		}else{
			tv_current_duration.setText("-" + currentDuration);
		}
		
//		editPer.setText(currentPer+"");
		sendChange();
	}
	
	private int currentDuration = 0;
//	private int currentPer = 20;

	//Duration에서(간격 증가 할 수록 말하는 시간이 늘어남.)
	//Speed로 변경되어 (속도 증가 할 수록 말하는 시간이 짧아짐)
	//+ / - 개념이 달라짐. 
	private OnClickListener speedChangeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ibt_dec:
				changeDuration(true);
				break;
			case R.id.ibt_inc:
				changeDuration(false);
				break;
			default:
				break;
			}
		}
	};
	
	private void changeDuration(boolean isIncrease){
		if(isIncrease){
			if(MAX_DURATION > currentDuration){
				++currentDuration;
				
			}
		}else{
			if(MIN_DURATION < currentDuration){
				--currentDuration;
			}
		}
		
		SharedPrefData.setIntSharedData(activity, SharedPrefData.SHARED_USER_DURATION_VALUE_ID_ +templateCode + "_" +VoMyInfo.getInstance().getUSERID(), currentDuration);
//		SharedPrefData.setIntSharedData(activity, SharedPrefData.SHARED_USER_DURATION_TEST_VALUE_ID_ +templateCode + "_" +VoMyInfo.getInstance().getUSERID(), Integer.valueOf(editPer.getText().toString()));
		setUserSpeed();
	}
	
	public interface DurationChangeListener{
		void durationChanged(int duration);
	}
	
	private void sendChange(){
		if(durationChangeListener != null){
//			durationChangeListener.durationChanged( userDuration + (currentDuration * DURATION_CHANGE_TIME));
			
//			String strPer = editPer.getText().toString();
//			if (StringUtil.isNull(strPer))
//				return;

			LogUtil.d("userDuration : " + userDuration + " / currentDuration : " + currentDuration);
			
			if (currentDuration == 0) {
				durationChangeListener.durationChanged(userDuration);
				return;
			}

			int nPer = Math.abs(15 * currentDuration);
			
			LogUtil.d("nPer : " + nPer);

			int ret = (int) (userDuration * (nPer / 100.0));
			
			LogUtil.d("ret : " + ret);
			
			if (currentDuration < 0) {
				LogUtil.d("ret 22 : " + (userDuration - ret));
				durationChangeListener.durationChanged( userDuration - ret);
			} else {
				LogUtil.d("ret 22 : " + (userDuration + ret));
				durationChangeListener.durationChanged( userDuration + ret);
			}
			
		}
	}
	private int userDuration = 0;
	public void setDuration(int duration){
		userDuration = duration;
		sendChange();
	}
	

}
