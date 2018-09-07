package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoWordsLevelList.VoWordsLevel;

public class ViewWordsLevel extends RelativeLayout implements OnTouchListener {

	private Context context;
	private ActivityWordTestMain activity;
	
	public enum LevelType {
		LowLevel,	//저학년
		MidLevel	//중학교이상
	}
	
	private final int[] levelStateCharactor = new int[] {R.drawable.sel_words_level_none_state, R.drawable.sel_words_level_ing_state, 
										R.drawable.sel_words_level_complate_low_state, R.drawable.sel_words_level_complate_mid_state};
	
	private VoWordsLevel info = null;
	
	private View base;
	private LinearLayout lay_center;
	private TextView tv_level_num;
	private TextView tv_level_state;
	private ImageView iv_certi;
	
	public ViewWordsLevel(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		base = inflater.inflate(R.layout.view_words_level, this);
		base.setOnTouchListener(this);
		
		lay_center = (LinearLayout) base.findViewById(R.id.lay_center);
		tv_level_num = (TextView) base.findViewById(R.id.tv_level_num);
		tv_level_state = (TextView) base.findViewById(R.id.tv_level_state);
		iv_certi = (ImageView) base.findViewById(R.id.iv_certi);
		TextView tv_level_state = (TextView) base.findViewById(R.id.tv_level);
		
		DisplayUtil.setLayout((Activity) context, 110, 112, lay_center);
		DisplayUtil.setLayout((Activity) context, 34, 47, iv_certi);
		DisplayUtil.setLayoutMargin((Activity) context, 0, 10, 0, 0, tv_level_state);
		DisplayUtil.setLayoutMargin((Activity) context, 0, 7, 0, 0, tv_level_num);
	}
	
	public void setActivity(ActivityWordTestMain activity){
		this.activity = activity;
	}
	
	public void setData(VoWordsLevel info) {
		this.info = info;
		base.setTag(info);
		
		if(VoWordsLevel.STUDY_NONE == info.getSTATUS()){
			lay_center.setBackgroundResource((levelStateCharactor[0]));
		}else if(VoWordsLevel.STUDY_ING == info.getSTATUS()){
			
			lay_center.setBackgroundResource((levelStateCharactor[1]));
			tv_level_state.setVisibility(View.VISIBLE);
		}else if(VoWordsLevel.STUDY_FINISH == info.getSTATUS() || VoWordsLevel.STUDY_CERTIFICATE  == info.getSTATUS()){
			
			if(VoWordsLevel.LEVEL_HIGH.equals(info.getGUBUN()))
				lay_center.setBackgroundResource((levelStateCharactor[2]));
			else lay_center.setBackgroundResource((levelStateCharactor[3]));
		}
		
		if(VoWordsLevel.STUDY_CERTIFICATE == info.getSTATUS()) {
			iv_certi.setVisibility(View.VISIBLE);
		}else{
			iv_certi.setVisibility(View.GONE);
		}
		tv_level_num.setText(info.getSTD_LEVEL());
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lay_center.setPressed(true);
			break;

		case MotionEvent.ACTION_UP:
			lay_center.setPressed(false);
			activity.selectedLevel(info.getSTD_LEVEL(), info.getGUBUN());
			break;
		default:
			break;
		}
		return true;
	}

}
