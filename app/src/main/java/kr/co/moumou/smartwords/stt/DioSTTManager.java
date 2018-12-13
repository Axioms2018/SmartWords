package kr.co.moumou.smartwords.stt;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.Diotek.STT.EduEng.IDioSttEduENG;
import com.Diotek.STT.EduEng.IDioSttEduENGInterface;
import com.Diotek.STT.EduEng.RecognitionResult;
import com.Diotek.STT.MediaLib.Mp3util;
import com.Diotek.STT.common.ALOG;

import java.io.File;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.vo.VoMyInfo;

public class DioSTTManager {

	public static final int RETURN_DECIBEL_SMALL_VOICE = 0;
    public static final int RETURN_DECIBEL_BIG_VOICE = 1;
    
	private static Context ctx;
	private Activity activity;
	private static DioSTTManager dioSTTManager;
	private IDioSttEduENG stt = null;
	private RecognitionResult rest = null;
	private short[] lastEpdBuffer = null;
	private boolean isSuccess = false;
	private int maxScore=0;
	private String saveMp3FilePath = null;
	private File saveMp3File;
	
	private int decibelLevel = 65;
	private int avgDecibel = 0;
	private int nowDecibel = 0;
	private int totalDecibel = 0;
	private int decibelCount;
    
	private boolean isSendMsg = true;
	
	protected OnSTTListener onSTTListener;
	
	private String strStatus="";
	
	public void setOnSTTListener(OnSTTListener onSTTListener) {
		this.onSTTListener = onSTTListener; 
	}
	
	public interface OnSTTListener {
		void onSTTRecoFail(RecognitionResult result); // 인식실패
		void onSTTRecoError(RecognitionResult result); // 인식에러
		void onSTTRecoSuccess(RecognitionResult result); // 인식성공
		void onSTTCheckDecibel(int mDb, int returnDecibel, int avgDecibel);
	}
	
	private DioSTTManager() {
	}
	
	public void init() {
		setSuccess(false);
		maxScore=0;
		rest = null;
	}
	
	public static DioSTTManager getInstance() {
		if (dioSTTManager == null)
			dioSTTManager = new DioSTTManager();
		return dioSTTManager;
	}
	
	public void setActivity(Activity act) {
		this.activity = act;
	}
	
	int w_frame=15;
	int w_thres=2;
	int w_gpause=1000;
	int w_gender=7;
	
	public IDioSttEduENG getSTT() {
		if (stt == null) {
			stt = DioSTTEduENG4MOUMOU.getDioSttEduENG();
		}
		
		stt.ChangeProfile(w_gender);
		stt.ChangeLevel(VoMyInfo.getInstance().getVRSN());
//		stt.ChangeLevel(1);
		stt.ChangeTimeLimit(w_frame);
		stt.ChangePauseThreshold(w_gpause);
		return stt;
	}

	public void create() {
		LogUtil.d("STT create");
		getSTT().Create(handler, Constant.STT_RESOURCE_PATH);
	}
	
	public void create(int timeOut) {
		LogUtil.d("STT create");
		w_frame = timeOut;
		getSTT().Create(handler, Constant.STT_RESOURCE_PATH);
	}
	
	public void create(int timeOut, int level) {
		LogUtil.d("STT create");
		w_frame = timeOut;
		w_thres = level;
		getSTT().Create(handler, Constant.STT_RESOURCE_PATH);
	}
	
	public void setText(String list) {
		getSTT().SetText(IDioSttEduENGInterface.FORM_CHUNK, list);
	}
	
	public void RecognitionStop() {
		getSTT().RecognitionStop();
	}
	
	public void RecognitionStart() {
		getSTT().RecognitionStart();
	}
	private File recordStartMp3File;
	public void mp3RecordStart(File mp3File) {
		this.recordStartMp3File = mp3File;
		stt.Mp3RecordStart(mp3File);
	}
	
	public void mp3RecordStop() {
		stt.Mp3RecordStop();
	}
	
	public void setMp3FilePath(String mp3FilePath) {
		saveMp3FilePath = mp3FilePath;
	}
	
	public void setMp3FilePath(File mp3File) {
		saveMp3File = mp3File;
	}
	
	private int recZeroPointCount = 0;
	private final int REC_RESTART_MAX_COUNT = 5;
	
	private void reRecoding(){
		mp3RecordStop();
		mp3RecordStart(recordStartMp3File);
	}
	
	
	
	public Handler handler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what == IDioSttEduENGInterface.MESSAGE_RECO_END ){ // 음성인식끝
				
				initResult();
				
				if(msg.arg1 ==0 ){
					
					strStatus = "인식실패";
					
					LogUtil.d("인식실패");
					 if (onSTTListener != null)
						 onSTTListener.onSTTRecoFail(rest);
					 
					return;
				}
				if(msg.obj==null){
					LogUtil.d("ERROR");
					
					strStatus = "에러";
					
					 if (onSTTListener != null)
						 onSTTListener.onSTTRecoError(rest);
					 
					return ;
				}
				rest = (RecognitionResult)msg.obj;

				strStatus = rest.total_score+"";
				
				if (onSTTListener != null)
					 onSTTListener.onSTTRecoSuccess(rest);
				
				ALOG.e("res.result_cnt : " + rest.result_cnt);
				ALOG.e("res.total_score : " + rest.total_score);
				
				if(rest.EPD_sample_cnt>0){
						lastEpdBuffer = rest.EPD_Buf;
						
						if (saveMp3File != null)
							Mp3util.buffToMp3_16(lastEpdBuffer,saveMp3File.getAbsolutePath());
						
//						ALOG.d("lastEpdBuffer : " + lastEpdBuffer.length);
//						{
//							Mp3util mp = new Mp3util();
//					    	if(mp.openMp3(saveMp3File)){
//						    	long lenth = mp.length();
//						    	mp.close();
//						    	ALOG.d("lenth : " + lenth);	
//					    	}
//						}
				} else {
					lastEpdBuffer = null;
				}
			} 
			else if (msg.what == IDioSttEduENGInterface.MESSAGE_RECORDING_BUFFER) {
				short[] buffer = stt.GetRecordingBuffer();
				//final double sDB = SoundFunc.getSound_dB_Value(buffer);
				final double sDB = msg.arg2/100;

				//TODO 인식이 되지 않을 때 0의 값으로 계속 넘어오는 경우가있음. 
				//이때 마이크를 껐다 키든 화면을 껐다 키든 해야 인식됨. 
//				if(sDB == 0){
//					if(recZeroPointCount >= REC_RESTART_MAX_COUNT){
//						recZeroPointCount = 0;
//						reRecoding();
//					}
//					recZeroPointCount++;
//				}else{
//					recZeroPointCount = 0;
//				}
				
				nowDecibel = 100 + (int)sDB;
//				LogUtil.d("ret decibel : " + sDB + " / " + nowDecibel);
				
				if (nowDecibel < 0 || nowDecibel >= 100)
					return;

				totalDecibel += nowDecibel;
						
				decibelCount++;
				
				avgDecibel = totalDecibel / decibelCount;
				
//				avgDecibel = (avgDecibel + nowDecibel) / 2;
				
				if (isSendMsg) {
					if (onSTTListener != null) {
//						LogUtil.d("ljh Decibel : " + nowDecibel + " / " + decibelLevel);
						if(nowDecibel >= Constant.BIC_VOICE_LEVELS[(VoMyInfo.getInstance().getLoudLevel()-1)]){ //데시벨이 큰소리일때!!
							onSTTListener.onSTTCheckDecibel(nowDecibel, RETURN_DECIBEL_BIG_VOICE, avgDecibel);
						} else {
							onSTTListener.onSTTCheckDecibel(nowDecibel, RETURN_DECIBEL_SMALL_VOICE, avgDecibel);
						}
					}
					isSendMsg = false;
					sendMessagerHandler.sendEmptyMessageDelayed(0, 100);
				}
				
//				int max_value = -1000;
//				for (int i = 0; i < 800; i++) {
//					if (max_value < buffer[i]) {
//						max_value = buffer[i];
//					}
//				}
//				max_value /= 100;
//
//				String value = "" + max_value;
//				ALOG.e("RecordRecordingBuffer " + value + msg.arg1 +" | "+ msg.arg2);
//				activity.runOnUiThread(new Runnable()
//				 {
//					 public void run(){
//						 LogUtil.d(String.format("%.2f",sDB ));
//						 LogUtil.d("녹음중");
////							((TextView)rootView.findViewById(R.id.TextView04)).setText(String.format("%.2f",sDB ));
////							((TextView)rootView.findViewById(R.id.TextViewResult)).setText("녹음중");
//					 }
//				 });
				return;
			} else if (msg.what == IDioSttEduENGInterface.MESSAGE_RECORD_TIMELIMIT) {
				
				LogUtil.d("타임아웃");
				
				initResult();
				
				strStatus = "타임아웃";
				
				if (onSTTListener != null)
					 onSTTListener.onSTTRecoFail(rest);
				ALOG.e("RecordTimeLimit " + "timelimit" + msg.arg1 +" | "+ msg.arg2);
				return;
			} else if (msg.what == IDioSttEduENGInterface.MESSAGE_BATCH_END) { // wParam 1
																		// : 성공,
																		// 0 :
																		// 실패


				ALOG.e("MESSAGE_BATCH_END " + "" + msg.arg1 +" | "+ msg.arg2);
				return;
			} else if (msg.what == IDioSttEduENGInterface.MESSAGE_ASSESSMENT_END) { // 평가
																			// 완료
				
				
				ALOG.e("MESSAGE_ASSESSMENT_END " + "" + msg.arg1 +" | "+ msg.arg2);
		
				return;
			} else if (msg.what == IDioSttEduENGInterface.MESSAGE_SIMILARITY_ASSESSMENT_END) { // 평가
				// 완료
				ALOG.e("MESSAGE_ASSESSMENT_END " + "" + msg.arg1 +" | "+ msg.arg2);

				return;
			}

		}
    };
	
	public void showResult(ImageView ivResult, TextView tvResult) {
			showResult(ivResult, tvResult, false);
	}
	
	public void showResult(ImageView ivResult, TextView tvResult, boolean isLast) {
		
//		int totalScore = 0;
//		
//		if (rest == null)
//			totalScore = -1;
//		else
//			totalScore = rest.total_score;
//		
//		LogUtil.d("showResult : " + totalScore);
		
		setResult();
		
		if (maxScore >= 60) {
			tvResult.setText("EXCELLENT");
//			ivResult.setBackgroundResource(R.drawable.icon_stt_excellent);
		} else if (maxScore >= 30) {
			tvResult.setText("GOOD");
//			ivResult.setBackgroundResource(R.drawable.icon_stt_good);
		} else {
			if (isLast)
				tvResult.setText("One more time");
			else
				tvResult.setText("Cheer up");
			
//			ivResult.setBackgroundResource(R.drawable.icon_stt_tryagain);
		}
		
//		tvResult.setText(strStatus);
	}
	
	public void setResult() {
		
		int totalScore = 0;
		
		if (rest == null)
			totalScore = -1;
		else
			totalScore = rest.total_score;
		
		LogUtil.d("setwResult : " + totalScore);
		
		if (maxScore < totalScore)
			maxScore = totalScore;
		
		LogUtil.d("setResult maxScore : " + maxScore + " / totalScore : " + totalScore);
		
		if (totalScore >= 30)
			setSuccess(true);
		
	}

	public void showPassResult(ImageView ivResult, TextView tvResult) {
		tvResult.setText("GOOD");
//		ivResult.setBackgroundResource(R.drawable.icon_stt_good);
	}

	public boolean isSuccess() {
		return isSuccess;
	}
	
//	void getPreferences(){
//
//		// 환경 설정에서 인식 엔진 설정을 위한 값을 가져온다.
//		String w_thres_str = getDefaultSharedPreferencesMultiProcess().getString("w_thres", "1");
//		w_thres=Integer.parseInt(w_thres_str);;
//		String w_gender_str = getDefaultSharedPreferencesMultiProcess().getString("w_gender", "7");
//		w_gender=Integer.parseInt(w_gender_str);
//
//		w_frame=getDefaultSharedPreferencesMultiProcess().getInt("w_frame", 30);
//		w_gpause=getDefaultSharedPreferencesMultiProcess().getInt("w_gpause", 500);
//		stt.ChangeProfile(w_gender);
//		stt.ChangeLevel(w_thres);
//		stt.ChangeTimeLimit(w_frame);
//		stt.ChangePauseThreshold(w_gpause);
//
//		waveView.setMaxFrameCount(w_frame * 16000);
//		setTopText();
//	}
	
	Handler sendMessagerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			isSendMsg = true;
		}
		
	};
	
	public void initResult() {
		rest = new RecognitionResult();
		rest.total_score = -1;
	}
	
	public void setTimeOut(int time) {
		stt.ChangeTimeLimit(time);
	}

	public void setSuccess(boolean isSuccess) {
		LogUtil.d("setSuccess : " + isSuccess);
		this.isSuccess = isSuccess;
	}
	
}
