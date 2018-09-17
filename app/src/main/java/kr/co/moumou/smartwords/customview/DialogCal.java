package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoCalData;

public class DialogCal extends Dialog implements View.OnClickListener{
	
	Context mContext;
	
	VoCalData mData = new VoCalData();
	
	int date;
	
	LinearLayout ll_diatitle;
	TextView tv_date;
	ImageButton ib_close;
	ListView lv_stdhistory;
	
	HistoryAdapter hisAdapter;

	public DialogCal(Context context, VoCalData data) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.mContext = context;
		this.mData = data;		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cal);
		
		ll_diatitle = (LinearLayout) findViewById(R.id.ll_diatitle);
		tv_date = (TextView) findViewById(R.id.tv_date);
		ib_close = (ImageButton) findViewById(R.id.ib_close);
		lv_stdhistory = (ListView) findViewById(R.id.lv_stdhistory);
		
		WindowManager.LayoutParams diaWindow = new WindowManager.LayoutParams();
		diaWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		diaWindow.dimAmount = 0.8f;
		
		getWindow().setAttributes(diaWindow);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		DisplayUtil.setLayout((Activity) mContext, 584, 84, ll_diatitle);
		DisplayUtil.setLayoutMargin((Activity) mContext, 26, 0, 0, 0, tv_date);
		DisplayUtil.setLayout((Activity)mContext,70,70,ib_close);
		DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 10, 0, ib_close);
		DisplayUtil.setLayout((Activity) mContext, 584, 462, lv_stdhistory);
		
		date = mData.getSTD_DD();
		
		tv_date.setText(date + "일");
		
		hisAdapter = new HistoryAdapter(mContext, mData.getUSERSTD());
		
		lv_stdhistory.setDividerHeight((int) DisplayUtil.getHeightUsingRate((Activity) mContext, 2));
		lv_stdhistory.setAdapter(hisAdapter);
		
		ib_close.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_close :
			dismiss();
			break;

		default:
			break;
		}
		
	}
	
}
