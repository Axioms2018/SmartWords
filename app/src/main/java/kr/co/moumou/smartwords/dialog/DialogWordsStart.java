package kr.co.moumou.smartwords.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;

/**
 * @Project  : 단어학습 결과 팝업창
 */
public class DialogWordsStart extends Dialog implements OnClickListener {
		
    private Context dialogContext;
    private Activity activity;

    private RelativeLayout rl_bg;
    private TextView tv_cont;
    private Button btn_start;
    
	public interface ListenerDialogButton{
	
		int DIALOG_RELEARNING	= 0;
		
		void onClick(Dialog dialog, int result);
	}

    private ListenerDialogButton listener;
    
    public void setListener(ListenerDialogButton listener){
    	this.listener = listener;
    }
    
    public DialogWordsStart(Context context) {
		super(context);
		dialogContext = context;
		activity = (Activity)context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setLayout();
	}
	
	public void setLayout(){
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.wordstest_start_popup);
		
		rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
		tv_cont = (TextView) findViewById(R.id.tv_cont);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		btn_start.requestFocus();
		
		DisplayUtil.setLayout(activity, 637, 250, rl_bg);
		DisplayUtil.setLayoutMargin(activity, 32, 46, 0, 0, tv_cont);
		DisplayUtil.setLayoutMargin(activity, 32, 0, 0, 30, btn_start);
	}
	
	public void setTitle(String title) {
		tv_cont.setText(title);
	}
	
	public void setBackground(int background) {
		rl_bg.setBackgroundResource(background);
	}
	
	public void setBtnText(String txt) {
		btn_start.setText(txt);
	}

	private void returnResult(int result){
		if(listener == null){
			return;
		}
		
		/*if (isShowing())
			this.dismiss();*/
		
		listener.onClick(this, result);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			returnResult(ListenerDialogButton.DIALOG_RELEARNING);
			break;
		default:
			break;
		}
	}
	
}
