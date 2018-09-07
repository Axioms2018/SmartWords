package kr.co.moumou.smartwords.communication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;

public class DialogLoding extends Dialog {

	private Context context;
	
	public DialogLoding(Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public DialogLoding(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public DialogLoding(Context context) {
		super(context);
		this.context = context;
	}

	private TextView tv_loding_text;
	private TextView tv_loding_text2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_loading_moumou);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.setCanceledOnTouchOutside(false);

		tv_loding_text = (TextView) findViewById(R.id.tv_loding_text);
		tv_loding_text2 = (TextView) findViewById(R.id.tv_loding_text2);
		
		View loding_moumou = findViewById(R.id.loding_moumou);
		
//		DisplayUtil.setLayout((Activity)context, 135, 140, loding_moumou);
	}

	public void setText(String msg) {
		tv_loding_text.setText(msg);
	}

	public void setSubText(String msg) {
		tv_loding_text2.setText(msg);
		tv_loding_text2.setVisibility(View.VISIBLE);
	}
}
