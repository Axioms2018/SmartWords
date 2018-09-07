package kr.co.moumou.smartwords.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;

public class MouMouDialog extends Dialog implements OnClickListener {

	public interface DialogButtonListener{

		int DIALOG_BTN_FIRST = 1;
		int DIALOG_BTN_SECOND = 2;
		int DIALOG_BTN_THIRD = 3;

		void onClick(Dialog dialog, int result);
	}

	private class FakeListener implements DialogButtonListener{
		@Override
		public void onClick(Dialog dialog, int result) {

		}
	}

	private DialogButtonListener listener = new FakeListener();

	public void setListener(DialogButtonListener listener){
		this.listener = listener;
	}

	public MouMouDialog(Context context) {
		super(context);
	}
	
	private TextView tv_title;
	private TextView tv_contents;
	private Button btn_first;
	private Button btn_second;
	private Button btn_third;

	private LinearLayout lay_title;
	private LinearLayout lay_second;
	private LinearLayout lay_third;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);

		
		Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = (int) (display.getWidth() * 0.6); //Display 사이즈의 60%
		int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 50%
		getWindow().getAttributes().width = width;
		getWindow().getAttributes().height = height;

		
		tv_title = (TextView)findViewById(R.id.tv_title);
		tv_contents = (TextView)findViewById(R.id.tv_contents);

		btn_first = (Button)findViewById(R.id.btn_first);
		btn_second = (Button)findViewById(R.id.btn_second);
		btn_third = (Button)findViewById(R.id.btn_third);

		lay_title= (LinearLayout)findViewById(R.id.lay_title);
		lay_second = (LinearLayout)findViewById(R.id.lay_second);
		lay_third = (LinearLayout)findViewById(R.id.lay_third);

		btn_first.setTag(1);
		btn_second.setTag(2);
		btn_third.setTag(3);

		btn_first.setOnClickListener(this);
		btn_second.setOnClickListener(this);
		btn_third.setOnClickListener(this);

		lay_title.setVisibility(View.GONE);
		
	}

	public void setTitle(String title, ColorStateList colors){
		setTitle(title);
		if(colors != null){
			tv_title.setTextColor(colors);
		}
	}
	public void setTitle(String title){
		lay_title.setVisibility(View.VISIBLE);
		tv_title.setText(Html.fromHtml(title));
	}
	public void setTitleColor(ColorStateList colors){
		tv_title.setTextColor(colors);
	}
	
	public void setDescript(String descript, ColorStateList colors){
		tv_contents.setText(Html.fromHtml(descript));
		if(colors != null){
			tv_contents.setTextColor(colors);
		}
	}
	public void setDescript(String descript){
		tv_contents.setText(Html.fromHtml(descript));
	}
	public void setDescriptColor(ColorStateList colors){
		tv_contents.setTextColor(colors);
	}
	
	public void setButtonColor(StateListDrawable colors){
		btn_first.setBackground(colors);
		btn_second.setBackground(colors);
		btn_third.setBackground(colors);
	}
	public void setButtonTextColor(ColorStateList colors){
		btn_first.setTextColor(colors);
		btn_second.setTextColor(colors);
		btn_third.setTextColor(colors);
	}
	
	public void setButtonText(String... text){
		if(text.length == 1){
			btn_first.setText(text[0]);
			lay_second.setVisibility(View.GONE);
			lay_third.setVisibility(View.GONE);
		}else if(text.length == 2){
			btn_first.setText(text[0]);
			btn_second.setText(text[1]);
			lay_third.setVisibility(View.GONE);
		}else{
			btn_first.setText(text[0]);
			btn_second.setText(text[1]);
			btn_third.setText(text[2]);
		}
	}


	@Override
	public void onClick(View v) {
		listener.onClick(this, (Integer)v.getTag());
	}

}