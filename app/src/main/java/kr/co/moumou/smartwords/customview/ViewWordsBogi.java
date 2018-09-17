package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;

public class ViewWordsBogi extends LinearLayout {

	private Context mContext;
	private String answer;
	
	private CustomButton btn_bogi;
	
	public ViewWordsBogi(Context context) {
		super(context);
		this.mContext = context;
		
		initLayout();
	}
	
	public void setText(String str) {
		this.answer = str;
		btn_bogi.setText(answer);
	}
	
	public void setSize(int width, int height, int textSize) {
		DisplayUtil.setLayout((Activity) mContext , width, height, btn_bogi);
		btn_bogi.setTextSizeCustom(textSize);
	}
	
	private void initLayout() {
		
		LayoutInflater infalater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = infalater.inflate(R.layout.view_words_bogi, this);
		
		btn_bogi = (CustomButton) view.findViewById(R.id.btn_bogi);
		btn_bogi.setOnClickListener(onClickListener);
		
		DisplayUtil.setLayout((Activity) mContext , 260, 102, btn_bogi);
		DisplayUtil.setLayoutMargin((Activity) mContext, 15, 0, 15, 0, btn_bogi);

	}
	
	View.OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			v.setBackgroundResource(R.drawable.btn_darkblue);
			bogiClickListener.onClick(answer);
		}
	};
	
	public void setCorrentBg() {
		btn_bogi.setBackgroundResource(R.drawable.btn_red);
		btn_bogi.setSelected(false);
	}
	
	public void resetBg() {
		btn_bogi.setBackgroundResource(R.drawable.sel_words_temp_bogi_btn);
		btn_bogi.setSelected(false);
	}
	
	public void setEnableClick() {
		btn_bogi.setClickable(true);
		btn_bogi.setEnabled(true);
	}
	
	public void setNotEnableClick() {
		btn_bogi.setClickable(false);
		btn_bogi.setEnabled(false);
	}
	
	public void setBogiClickListener (WordBogiClickListener listener) {
		this.bogiClickListener = listener;
	}
	
	WordBogiClickListener bogiClickListener = null;
	
	public interface WordBogiClickListener {
		void onClick(String str);
	}

}
