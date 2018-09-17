package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.activity.ActivityWordTestDaySelect;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoWordsDayList.VoDay;

public class ViewDay extends RelativeLayout implements OnClickListener {
	private Context context;

	//미진행 : 0, 진행 : 1, 완료 : 2, 실패 : 3
	private final String[] studyState = new String[]{"", "진행중", "PASS", "TRY AGAIN", ""};
	private final int[] studyStateTextColor = new int[]{R.color.white, R.color.orange, R.color.blue, R.color.blue, R.color.white};
	private final int[] studyStateCharactor = new int[]{R.drawable.ic_lock, R.drawable.ic_ing, R.drawable.ic_pass,
													R.drawable.ic_tryagain, R.drawable.ic_lockgreen, R.drawable.ic_cerfi};
	private final int[] reviewStateCharactor = new int[]{R.drawable.ic_lock, R.drawable.ic_reviewing, R.drawable.ic_reviewpass, R.drawable.ic_lockgreen};

	private VoDay info;
	private ActivityWordTestDaySelect activity;
	private boolean isReview = false;

	private ImageView img_state, iv_certi;
	private TextView tv_day;
	private TextView tv_study_state;
	private View base;
	
	public ViewDay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public ViewDay(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ViewDay(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init(){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
		this.setLayoutParams(param);
		
		base = inflater.inflate(R.layout.view_day, this);
		base.setOnClickListener(this);

		img_state = (ImageView)base.findViewById(R.id.img_state);
		tv_day = (TextView)base.findViewById(R.id.tv_day);
		tv_study_state = (TextView)base.findViewById(R.id.tv_study_state);
		iv_certi = (ImageView)base.findViewById(R.id.iv_certi);
		
		DisplayUtil.setLayout((Activity) context, 75, 75, img_state);
		DisplayUtil.setLayoutMargin((Activity) context, 0, 8, 0, 0, tv_day);
		DisplayUtil.setLayoutMargin((Activity) context, 0, 3, 0, 0, tv_study_state);
		
		DisplayUtil.setLayout((Activity) context, 22, 32, iv_certi);
		DisplayUtil.setLayoutMargin((Activity) context, 85, 88, 0, 0, iv_certi);
		//DisplayUtil.setLayoutPadding((Activity) context, 0, 0, 0, 10, tv_study_state);
	}

	public void setData(VoDay info, boolean isReview){
		this.info = info;
		this.isReview = isReview;
		
		if(isReview) {
			tv_day.setText(" Review " + info.getSTD_DAY());
			//tv_study_state.setText(info.getRWORD_PER() > 0 ? Integer.toString(info.getRWORD_PER()) :  Integer.toString(info.getWORD_PER()));
			if(VoDay.DAY_PASS  == info.getSTATUS()) tv_study_state.setText(Integer.toString(info.getLAST_WORD_PER()));
			tv_study_state.setTextColor(getResources().getColor(studyStateTextColor[info.getSTATUS()]));
			
			if(VoDay.DAY_NONE == info.getSTATUS() && VoDay.OPENGB_PASS == info.getOPEN_GB()) 
				img_state.setImageResource(reviewStateCharactor[3]);
			else img_state.setImageResource(reviewStateCharactor[info.getSTATUS()]);
		}
		else{
			tv_day.setText("Day " + info.getSTD_DAY());
			tv_study_state.setText(VoDay.DAY_CERT == info.getSTATUS() ? studyState[VoDay.DAY_PASS] : studyState[info.getSTATUS()]);
			tv_study_state.setTextColor(getResources().getColor(VoDay.DAY_CERT == info.getSTATUS() ? 
																studyStateTextColor[VoDay.DAY_PASS] : studyStateTextColor[info.getSTATUS()]));
			
			if(VoDay.DAY_NONE == info.getSTATUS() && VoDay.OPENGB_PASS == info.getOPEN_GB())
				img_state.setImageResource(studyStateCharactor[4]);
			else if(VoDay.DAY_CERT == info.getSTATUS()) {
				img_state.setImageResource(studyStateCharactor[5]);
				iv_certi.setVisibility(View.VISIBLE);
			}else img_state.setImageResource(studyStateCharactor[info.getSTATUS()]);
		}
		
	}
	
	private int getStudyStateIndex(int state){
		if(VoDay.DAY_NONE == state){
			return 0;
		}else if(VoDay.DAY_ING == state){
			return 1;
		}else if(VoDay.DAY_PASS == state){
			return 2;
		}else{
			return 3;
		}
	}
	
	public void setActivity(ActivityWordTestDaySelect activity){
		this.activity = activity;
	}
	
	public void onClick(View v) {
		if(isReview) activity.selectedReview(info);
		else activity.selectedDay(info);
	}

}
