package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.app.Dialog;
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
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DialogStudent.ListenerDialogButton;

public class ViewWordsTestType extends RelativeLayout implements OnTouchListener {

	private Context context;
	private WordTestTypeClickListener listener;
	
	public enum TestType {
		Test1,
		Pratice,
		Test2,
		Test3,
		Quiz
	}
	
	public enum TestState {
		None,
		Ing,
		End
	}
	
	private final int[] testTypeBg = new int[] {R.drawable.sel_words_type_sel_btn_none, 
				R.drawable.sel_words_type_sel_btn_prac, R.drawable.sel_words_type_sel_btn_test};
	private final int[] testTypeCharactor = new int[] {R.drawable.icon_practice, R.drawable.icon_test};
	
	private VoWordsTest info = null;
	private TestState testState = TestState.None;
	
	private View base;
	private LinearLayout btn_type;
	private ImageView iv_type;
	private TextView tv_type;
	
	public ViewWordsTestType(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		base = inflater.inflate(R.layout.view_words_test_type, this);
		base.setOnTouchListener(this);
		
		btn_type = (LinearLayout) base.findViewById(R.id.btn_type);
		DisplayUtil.setLayoutMargin((Activity) context, 0, 0, 28, 0, btn_type);
		iv_type = (ImageView) base.findViewById(R.id.iv_type);
		tv_type = (TextView) base.findViewById(R.id.tv_type);
		
		DisplayUtil.setLayout((Activity) context, 234, 92, btn_type);
		DisplayUtil.setLayout((Activity) context, 42, 38, iv_type);
	}
	
	public void setClickLisetener(WordTestTypeClickListener listener) {
		this.listener = listener;
	}
	
	public void setData(VoWordsTest info, TestState state) {
		this.info = info;
		base.setTag(info);
		this.testState = state;
		
		if(TestState.None == state) {
			btn_type.setBackgroundResource(testTypeBg[0]);
		}else{
			
			if(VoWordsTest.CODE_PRACTICE.equals(info.getSTD_STEP())){
				btn_type.setBackgroundResource(testTypeBg[1]);
			}else{
				btn_type.setBackgroundResource(testTypeBg[2]);
			}
		}
		
		if(VoWordsTest.CODE_PRACTICE.equals(info.getSTD_STEP())){
			iv_type.setBackgroundResource(testTypeCharactor[0]);
		}else{
			iv_type.setBackgroundResource(testTypeCharactor[1]);
		}
		
		if(TestState.End == state) {
			btn_type.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showDialog();
				}
			});
		}
		
		tv_type.setText(info.getSTD_NAME());
	}
	
	private void showDialog() {
		String testType = VoWordsTest.CODE_TEST1.equals(info.getSTD_STEP()) ? "을 " : "를 ";
		DialogStudent dialog = new DialogStudent(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setButtonMsg(context.getString(R.string.monthly_test_ok));
		dialog.setMessage(info.getSTD_NAME() + testType + context.getString(R.string.wordstest_test_end));
        dialog.setListener(new ListenerDialogButton() {
            @Override
            public void onClick(Dialog dialog, int result) {
            	if(dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			btn_type.setPressed(true);
			break;

		case MotionEvent.ACTION_UP:
			//activity.selectedQuiz(info.getSTD_STEP(), testState);
			listener.selectedQuiz(info.getSTD_STEP(), testState);
			break;
		default:
			break;
		}
		return true;
	}
	
	public interface WordTestTypeClickListener {
		void selectedQuiz(String code, TestState testState);
	}

}
