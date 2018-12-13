package kr.co.moumou.smartwords.util;


import com.Diotek.STT.EduEng.RecognitionResult;

import kr.co.moumou.smartwords.customview.DialogLoding;

/**
 * @Project  : 스마트알파
 * @FileName : MediaDecibelReader.java
 * @comment  : 데시벨 측정하는 클래스 
 *  		   - 학생이 따라 말할때 데시벨 측정하여 목소리 크게 말하게 한다.
 *			   - DecibelAudioReader 클래스를 참조한다.
 */
public class MediaDecibelReader {

	/**
     * SmartMoumou 로그켓 : 해당 클래스 출력
     */
    private String TagClass = getClass().getSimpleName();
    //LogUtil.d("[onCreate 함수] {onCreate 시작} (Bundle : " + savedInstanceState + ")");
    
    
	//=================================== 데시벨 측정 관련 ===================================//
    public static MediaDecibelAudio mMediaDecibelAudio;	//데시벨측정하는 클래스
    private int decibelRate = 8000; 			//속도
//  private int decibelRate = 10000; 			//속도
	private int decibelInputBlockSize = 256;	//구간설정 0.1초 정도
//  private int decibelInputBlockSize = 1024;
//	private int decibelInputBlockSize = 2048;	//구간설정 30초정도
//  private int decibelInputBlockSize = 3072;	//구간설정 1초정도
//	private int decibelInputBlockSize = 4096;	//구간설정 1초 30 정도
    private int decibelDecimate = 1;
    //=================================== 데시벨 측정 관련 ===================================//

    private int counterDecibel = 0;
    int decibelLevel = 85;
    String userId = "";
    
    public static final int RETURN_DECIBEL_SMALL_VOICE = 0;
    public static final int RETURN_DECIBEL_BIG_VOICE = 1;
    
    int avgDecibel = 0;
    int nowDecibel = 0;
    
    private boolean isStart = false;
    private boolean isRec = false;
    
    private DialogLoding progress;
    
    private DecibelCheckListener mDecibelCheckListener;
    
    public interface DecibelCheckListener {
    	void getCheckVoiceDecibel(int mDb, int returnDecibel, int avgDecibel);

		void onSTTRecoFail(RecognitionResult result);

		void onSTTRecoError(RecognitionResult result);

		void onSTTRecoSuccess(RecognitionResult result);

		void onSTTCheckDecibel(int mDb, int returnDecibel, int avgDecibel);
	}
    
    //데시벨 측정 시작
    public void doDecibelStart(DecibelCheckListener listener, boolean isRec, int level) {
    	isStart = true;
    	this.isRec = isRec;
		this.mDecibelCheckListener = listener;
		decibelLevel = level;
		
		if(mMediaDecibelAudio == null){
			mMediaDecibelAudio = new MediaDecibelAudio();
		}
		try {
			mMediaDecibelAudio.startReader(isRec, new MediaDecibelAudio.Listener() {
					@Override
					public final void onReadComplete(int mDb) {
						//데시벨 큰소리 체크
						checkDecibelSound(mDb);
					}
					@Override
					public void onReadError(int error) {

					}
					@Override
					public void onSaveStart() {
						
					}
					@Override
					public void onSaveEnd() {
						
					}
					
				});
		} catch (Exception e) {
			e.getStackTrace();
			LogUtil.d("[doDecibelStart 함수] {DecibelAudioReader.Listener()} {catch => onReadComplete} ( getMessage : " + e.getMessage() + ")");
		}
		
	}
    
    
    //데시벨 큰소리 체크
    private void checkDecibelSound(int mDb){
//    	LogUtil.d("[checkDecibelSound 함수] {데시벨 측정} (decibel : " + mDb + ")");
    	
    	counterDecibel++;
    	if(counterDecibel > 4) {
			
    		nowDecibel = mDb;
    		//LogUtil.d("[checkDecibelSound 함수] {현재 데시벨} (nowDecibel : " + nowDecibel + ")");
    		
    		avgDecibel = (avgDecibel + nowDecibel) / 2;
    		//LogUtil.d("[checkDecibelSound 함수] {평균 데시벨} (avgDecibel : " + avgDecibel + ")");
    		
        	
//    		LogUtil.d("mDb : " + mDb + " / " + decibelLevel);
			if(mDb > decibelLevel){ //데시벨이 큰소리일때!!
				//LogUtil.d("[checkDecibelSound 함수] {데시벨 측정} {큰소리일때!!}");
				mDecibelCheckListener.getCheckVoiceDecibel(mDb, RETURN_DECIBEL_BIG_VOICE, avgDecibel);
			} else{
				//if(mDb > 68)
				//LogUtil.d("[checkDecibelSound 함수] {데시벨 측정} {작은소리일때!!}");
				mDecibelCheckListener.getCheckVoiceDecibel(mDb, RETURN_DECIBEL_SMALL_VOICE, avgDecibel);
			}
			//============================= 데시벨 체크 =============================//
			
			counterDecibel = 5;
		}
    }
    
    
    //데시벨 측정 멈춤
    public void doDecibelStop() {
		LogUtil.d("[doDecibelStop 함수] {데시벨측정 멈춤}");
		if(!isStart){
			return;
		}
		
		isStart = false;
		//큰소리학습 팝업창 및 사운드 종료 (핸들러삭제)
		//모든 카운트 초기화
		counterDecibel = 0;
		
		
		try {
			mMediaDecibelAudio.stopReader();
			LogUtil.d("[doDecibelStop 함수] {데시벨측정 멈춤} {AudioRecord 정지}");
		} catch (Exception e) {
			e.getStackTrace();
			LogUtil.d("[doDecibelStop 함수] {데시벨측정 멈춤} (getMessage : " + e.getMessage() + ")");
		}
		
	}
    
}
