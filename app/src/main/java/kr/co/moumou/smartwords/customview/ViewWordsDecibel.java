package kr.co.moumou.smartwords.customview;

import android.content.Context;
import android.media.AudioManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;

public class ViewWordsDecibel extends ViewDecibel {

	public ViewWordsDecibel(FragmentActivity activity) {
		super(activity);
	}
	
	@Override
	public void init() {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.view_words_decibel, this);
		
		ivSpeaker = (ImageView)v.findViewById(R.id.ivSpeaker);
		DisplayUtil.setLayout(activity, 102, 102, ivSpeaker);
		
		tvDialog = (TextView)v.findViewById(R.id.tvDialog);
		img_state = (ImageView)v.findViewById(R.id.img_state);
		lay_big_voice = (LinearLayout)v.findViewById(R.id.lay_big_voice);
		DisplayUtil.setLayoutMargin(activity, 20, 0, 9, 9, lay_big_voice);
		DisplayUtil.setLayoutPadding(activity, 60, 0, 0, 0, lay_big_voice);
		DisplayUtil.setLayoutHeight(activity, 115, lay_big_voice);
		DisplayUtil.setLayout(activity, 42, 45, img_state);

		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	public void hideSpeechBubble() {
		if(lay_big_voice != null) lay_big_voice.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void setImgEqualizer(int checkEqualizer) {
		
		if (ivSpeaker == null)
			return;
		
		switch (checkEqualizer) {
		case CHECK_VOICE_EQUALIZER_SMALL:
			ivSpeaker.setBackgroundResource(R.drawable.btn_speak1);
			break;
		case CHECK_VOICE_EQUALIZER_BIG:
			ivSpeaker.setBackgroundResource(R.drawable.btn_speak2);
			break;
		case CHECK_VOICE_EQUALIZER_NOT:
			ivSpeaker.setBackgroundResource(R.drawable.btn_speak3);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void showEqualizer(final int mDb) {
		
		//이퀄라이즈 표시
    	if(activity == null){
			return;
		}
    	
    	activity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				try {
					
					int startDb = 60;
					
    				if(mDb < startDb) { //60
    					ivSpeaker.setBackgroundResource(R.drawable.btn_speak0);
    				}
    				
    				if(mDb >= startDb && mDb < Constant.BIC_VOICE_LEVEL_2) {
    					ivSpeaker.setBackgroundResource(R.drawable.btn_speak1);
    				}
    				
    				if(mDb >= Constant.BIC_VOICE_LEVEL_2 && mDb < Constant.BIC_VOICE_LEVEL_4) {
    					ivSpeaker.setBackgroundResource(R.drawable.btn_speak2);
    				}
    				
    				if(mDb > Constant.BIC_VOICE_LEVEL_4) {
    					ivSpeaker.setBackgroundResource(R.drawable.btn_speak3);
    				}
				
				} catch (Exception e) {
					e.getStackTrace();
					LogUtil.d("[showEqualizer 함수] {runOnUiThread} {catch => 이퀄라이즈 표시} ( getMessage : " + e.getMessage() + ")");
				}
			}
		});
	}

}
