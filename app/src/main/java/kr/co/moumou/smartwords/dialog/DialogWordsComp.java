package kr.co.moumou.smartwords.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;

/**
 * @Project  : 단어학습 결과 팝업창
 */
public class DialogWordsComp extends Dialog implements OnClickListener {
		
    private Context dialogContext;
    private Activity activity;

    private ImageButton btn_close;
    private LinearLayout btn_relearning;
    private LinearLayout btn_report;
    private TextView tv_left, tv_right;
    
	public interface ListenerDialogButton{
	
		int DIALOG_RELEARNING	= 0;
		int DIALOG_REPORT 	= 1;
		
		void onClick(Dialog dialog, int result);
	}

    private ListenerDialogButton listener;
    
    public void setListener(ListenerDialogButton listener){
    	this.listener = listener;
    }
    
    public DialogWordsComp(Context context) {
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
		
		setContentView(R.layout.wordstest_comp_popup);
		
		btn_close = (ImageButton) findViewById(R.id.btn_close);
		btn_relearning = (LinearLayout) findViewById(R.id.btn_relearning);
		btn_report = (LinearLayout) findViewById(R.id.btn_report);
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_right = (TextView) findViewById(R.id.tv_right);
		
		btn_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		btn_relearning.setOnClickListener(this);
		btn_report.setOnClickListener(this);
		
		DisplayUtil.setLayout(activity, 52, 52, btn_close);
		DisplayUtil.setLayoutMargin(activity, 0, 14, 14, 14, btn_close);
		DisplayUtil.setLayout(activity, 224, 224, btn_relearning);
		DisplayUtil.setLayoutMargin(activity, 24, 0, 28, 24, btn_relearning);
		DisplayUtil.setLayout(activity, 224, 224, btn_report);
		DisplayUtil.setLayoutMargin(activity, 0, 0, 24, 24, btn_report);
		
		ImageView iv_restudy = (ImageView) findViewById(R.id.iv_restudy);
		ImageView iv_report = (ImageView) findViewById(R.id.iv_report);
		DisplayUtil.setLayout(activity, 53, 55, iv_restudy);
		DisplayUtil.setLayoutMargin(activity, 0, 0, 0, 12, iv_restudy);
		DisplayUtil.setLayout(activity, 54, 54, iv_report);
		DisplayUtil.setLayoutMargin(activity, 0, 0, 0, 12, iv_report);
	}
	
	public void setButtonMsg(String left, String right){
		tv_left.setText(left);
		tv_right.setText(right);
	}

	private void returnResult(int result){
		if(listener == null){
			return;
		}
		
		if (isShowing())
			this.dismiss();
		
		listener.onClick(this, result);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_relearning:
			returnResult(ListenerDialogButton.DIALOG_RELEARNING);
			break;
		case R.id.btn_report:
			returnResult(ListenerDialogButton.DIALOG_REPORT);
			break;
		default:
			break;
		}
	}
	
}
