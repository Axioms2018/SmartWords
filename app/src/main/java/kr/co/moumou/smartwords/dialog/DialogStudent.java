package kr.co.moumou.smartwords.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.StringUtil;

/**
 * @Project  : 스마트무무 프로토타입
 * @FileName : DialogTeacher.java
 * @comment  : 학생화면에서 대기, 점검 팝업
 */
public class DialogStudent extends Dialog implements OnClickListener {

	
	/**
     * LogTraceJong 로그켓 : 해당 클래스 출력
     */
    private String TagClass = getClass().getSimpleName();
    //LogUtil.d("[onCreate 함수] {onCreate 시작} (Bundle : " + savedInstanceState + ")");
    
    Context dialogContext;
    
    int dialogSize = 0;
    int dialogType = 0;
    
    public static final int DIALOG_SIZE_SMALL			= 0;
    public static final int DIALOG_SIZE_BIG			= 1;
    public static final int DIALOG_CHECK			= 2;
    public static final int DIALOG_DEFAULT_MSG		= 3;
	
    public static final int DIALOG_TYPE_SMALL_FORWARD	= 0;
    public static final int DIALOG_TYPE_SMALL_END		= 1;
    public static final int DIALOG_TYPE_BIG		 	= 2;
    public static final int DIALOG_TYPE_SMALL_BOARD_MOUTH	= 3;
    public static final int DIALOG_TYPE_SMALL_BOARD_CHANT	= 4;
    public static final int DIALOG_TYPE_SMALL_d	= 5;
    
    Button btn_delete_data;
    TextView tv_message_dialog_small;
    Button btn_ok_dialog_small;
    Button btn_no_dialog_small;
    
    TextView tv_message_dialog_big;
    
    TextView tv_content_header_dialog_big;
    TextView tv_content_msg_dialog_big;
    TextView tv_details_header_dialog_big;
    TextView tv_details_msg_dialog_big;
    Button btn_ok_dialog_big;
    Button btn_no_dialog_big;
    
    View layoutOkDialogSmall;
    View layoutNoDialogSmall;
    
    TextView tvMsgPopup;
    
    private RelativeLayout lay_small_button;

    boolean isNotDismiss;
    
	public interface ListenerDialogButton{
	
		int DIALOG_BTN_BACK	= 0;
		int DIALOG_BTN_ON 	= 1;
		int DIALOG_BTN_NO 	= 2;
		int DIALOG_BTN_ANSWER 	= 3;
		
		void onClick(Dialog dialog, int result);
	}

    private ListenerDialogButton listener;
    
    public void setListener(ListenerDialogButton listener){
    	this.listener = listener;
    }
    
    public void setListener(ListenerDialogButton listener, boolean isNotDismiss){
    	this.listener = listener;
    	this.isNotDismiss = isNotDismiss;
    }
    
    public DialogStudent(Context context) {
		super(context);
		dialogContext = context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d("[onCreate] (다이얼로그 시작)");
		super.onCreate(savedInstanceState);
		
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}

	//저장된 시간보다 앞의 시간으로 빨리감기한경우 다이얼로그
	private void makeDialogSmall(){
		LogUtil.d("[makeDialogSmall] (makeDialogSmall 시작)");
		
		setContentView(R.layout.dialog_small);
		
		
//		Display display = ((WindowManager)dialogContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		int width = (int) (display.getWidth() * 0.4); //Display 사이즈의 40%
//		int height = (int) (display.getHeight() * 0.4);  //Display 사이즈의 30%
//		getWindow().getAttributes().width = width;
//		getWindow().getAttributes().height = height;
		RelativeLayout layoutContents = (RelativeLayout) findViewById(R.id.layoutContents);
		DisplayUtil.setLayoutHeight((Activity)dialogContext, 220, layoutContents);
		
		tv_message_dialog_small = (TextView) findViewById(R.id.tv_message_dialog_small);
		btn_delete_data = (Button) findViewById(R.id.btn_delete_data);
		btn_ok_dialog_small = (Button) findViewById(R.id.btn_ok_dialog_small);
		btn_no_dialog_small = (Button) findViewById(R.id.btn_no_dialog_small);
		
		lay_small_button = (RelativeLayout)findViewById(R.id.lay_small_button);
		
		DisplayUtil.setLayout((Activity)dialogContext, 180, 45, btn_delete_data);
		DisplayUtil.setLayoutMargin((Activity)dialogContext, 0,30,0,0, btn_delete_data);
		
		DisplayUtil.setLayoutHeight((Activity)dialogContext, 81, btn_ok_dialog_small);
		DisplayUtil.setLayoutHeight((Activity)dialogContext, 81, btn_no_dialog_small);
		
		layoutOkDialogSmall = findViewById(R.id.layoutOkDialogSmall);
		layoutNoDialogSmall = findViewById(R.id.layoutNoDialogSmall);
		
		
		btn_ok_dialog_small.setOnClickListener(this);
		btn_no_dialog_small.setOnClickListener(this);
		
		switch (dialogType) {
		case DIALOG_TYPE_SMALL_FORWARD:
			layoutNoDialogSmall.setVisibility(View.GONE);
			tv_message_dialog_small.setText(dialogContext.getString(R.string.ff_enable_studing));//학습한 부분까지만 빨리감기 됩니다. viee
			break;
		case DIALOG_TYPE_SMALL_BOARD_MOUTH:
			tv_message_dialog_small.setText(dialogContext.getString(R.string.exit_panics_chant));//파닉스챈트을 종료하시겠습니까? viee
			break;
		case DIALOG_TYPE_SMALL_END:
			tv_message_dialog_small.setText(dialogContext.getString(R.string.exit_study));//학습을 종료하시겠습니까? viee
			break;
		default:
			break;
		}
	}
		
	private void makeDialogBig(){
		LogUtil.d("[makeDialogBig] (makeDialogBig 시작)");
		
		setContentView(R.layout.dialog_big);
		
		
		Display display = ((WindowManager)dialogContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = (int) (display.getWidth() * 0.6); //Display 사이즈의 60%
		int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 50%
		getWindow().getAttributes().width = width;
		getWindow().getAttributes().height = height;
		
	    tv_message_dialog_big = (TextView) findViewById(R.id.tv_message_dialog_big);
	    
	    tv_content_header_dialog_big = (TextView) findViewById(R.id.tv_content_header_dialog_big);
	    tv_content_msg_dialog_big = (TextView) findViewById(R.id.tv_content_msg_dialog_big);
	    tv_details_header_dialog_big = (TextView) findViewById(R.id.tv_details_header_dialog_big);
	    tv_details_msg_dialog_big = (TextView) findViewById(R.id.tv_details_msg_dialog_big);
	    
		btn_ok_dialog_big = (Button) findViewById(R.id.btn_ok_dialog_big);
		btn_no_dialog_big = (Button) findViewById(R.id.btn_no_dialog_big);
		
		btn_ok_dialog_big.setOnClickListener(this);
		btn_no_dialog_big.setOnClickListener(this);
		
	}
	
	private void makeDialogCheck(){
		
//		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		setContentView(R.layout.dialog_teacher_check);
		
		btn_confirmCancel = (Button)findViewById(R.id.btn_confirmCancel);
		DisplayUtil.setLayout((Activity)dialogContext, 170, 80, btn_confirmCancel);
		DisplayUtil.setLayoutMargin((Activity)dialogContext, 380, 210, 0, 0, btn_confirmCancel);
		btn_confirmCancel.setOnClickListener(this);
		
	}
	
	private Button btn_confirmCancel, btn_answer;
	private void makeDialogDefaultMsg(){
		
		setContentView(R.layout.dialog_msg_popup);
		
		View layoutPopup = findViewById(R.id.layoutPopup);
		tvMsgPopup = (TextView) findViewById(R.id.tvMsgPopup);
		DisplayUtil.setLayout((Activity)dialogContext, 400, 114, tvMsgPopup);
		
		btn_confirmCancel = (Button)findViewById(R.id.btn_confirmCancel);
		
		DisplayUtil.setLayout((Activity)dialogContext, 664, 291, layoutPopup);
//		DisplayUtil.setLayoutPadding((Activity)dialogContext, 180, 60, 40, 124, layoutPopup);
		DisplayUtil.setLayoutPadding((Activity)dialogContext, 0, 0, 20, 20, layoutPopup);
		
		
		DisplayUtil.setLayout((Activity)dialogContext, 140, 80, btn_confirmCancel);
		DisplayUtil.setLayoutMargin((Activity)dialogContext, 10, 30, 0, 0, btn_confirmCancel);
		btn_confirmCancel.setOnClickListener(this);
		
		btn_answer = (Button)findViewById(R.id.btn_answer);
		DisplayUtil.setLayout((Activity)dialogContext, 140, 80, btn_answer);
//		DisplayUtil.setLayoutMargin((Activity)dialogContext, 250, 210, 0, 0, btn_answer);
		btn_answer.setOnClickListener(this);
	}
	
	public void showAnswerButton(){
		btn_answer.setVisibility(View.VISIBLE);
	}
	public void hideAnswerButton(){
		btn_answer.setVisibility(View.INVISIBLE);
	}

	public void showConfirmCancelButton(){
		btn_confirmCancel.setVisibility(View.VISIBLE);
	}
	public void hideConfirmCancelButton(){
		btn_confirmCancel.setVisibility(View.INVISIBLE);
	}
	
	public void setDefaultMsg(String msg) {
		tvMsgPopup.setText(msg);
	}
	
	public void setDialogSize(int size){
		dialogSize = size;
	}
	
	public void setDialogType(int type){
		dialogType = type;
	}
	
	public void setMessage(String msg){
		LogUtil.i("    DIOALOG SET MSG : " + msg);
		if(dialogSize == DIALOG_SIZE_BIG){
			tv_message_dialog_big.setText(msg);
			return;
		}
		tv_message_dialog_small.setText(msg);
	}

	/*
	 * 데이터 초기화
	 */
	public void setDeleteDataBtn(final Activity activity){
		
//		btn_delete_data.setVisibility(View.VISIBLE);
//		btn_delete_data.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//
//				((ActivityBase)activity).showLoadingProgress(activity.getResources().getString(R.string.wait_for_data));
//
//
//
//				// 서버 api호출
//				Uri.Builder builder = Uri.parse(ConstantsCommURL.getUrl("DeleteUserDataMoumouStdy")).buildUpon();
//				builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoMyInfo.getInstance().getSESSIONID());
//				builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, SharedPrefData.getStringSharedData(activity, SharedPrefData.SHARED_USER_ID_S, Constant.STRING_DEFAULT));
//
//				Request.getInstance().sendMessage(ConstantsCommCommand.COMMAND_133_DELETE_USER_DATA, "GET", builder.toString(), null, new CommunicationEventListener() {
//					@Override
//					public void onResponseFail(String msg) {
//						((ActivityBase)activity).hideProgress();
//						ToastUtil.show(activity, "데이터 초기화 실패");
//					}
//
//					@Override
//					public void onResponse(String msg) {
//						((ActivityBase)activity).hideProgress();
//						ToastUtil.show(activity, "데이터 초기화 완료");
//					}
//
//					@Override
//					public void exception(Exception e) {
////						((ActivityStudyChapterSelect)activity).dataReceiveFail(activity.getString(R.string.sorry_data_receive_exception, e.toString()), false);
//						((ActivityBase)activity).hideProgress();
//						ToastUtil.show(activity, "데이터 초기화 실패");
//					}
//
//					@Override
//					public void networkNotAvailable(String msg) {
//						((ActivityBase)activity).hideProgress();					}
//
//				});
//
//			}
//		});
		
	}
	
	public void setContentMsg(Spanned msg){
		if(StringUtil.isNull(String.valueOf(msg))){
			tv_content_header_dialog_big.setVisibility(View.GONE);
			tv_content_msg_dialog_big.setVisibility(View.GONE);
			return;
		}
		tv_content_msg_dialog_big.setVisibility(View.VISIBLE);
		tv_content_msg_dialog_big.setText(msg);
	}
	
	public void setDetailMsg(Spanned msg){
		if(StringUtil.isNull(String.valueOf(msg))){
			tv_details_header_dialog_big.setVisibility(View.GONE);
			tv_details_msg_dialog_big.setVisibility(View.GONE);
			return;
		}
		tv_details_msg_dialog_big.setVisibility(View.VISIBLE);
		tv_details_msg_dialog_big.setText(msg);
	}
	
	public void setHideButtonLayout(){
		if(lay_small_button == null){
			return;
		}
		lay_small_button.setVisibility(View.GONE);
	}
	
	public void setButtonMsg(){
		if(dialogSize == DIALOG_SIZE_BIG){
			btn_ok_dialog_big.setVisibility(View.GONE);
			btn_no_dialog_big.setVisibility(View.GONE);
			return;
		}
		layoutOkDialogSmall.setVisibility(View.GONE);
		layoutNoDialogSmall.setVisibility(View.GONE);
	}
	
	public void setButtonMsg(String left){
		if(dialogSize == DIALOG_SIZE_BIG){
			btn_ok_dialog_big.setText(left);
			btn_no_dialog_big.setVisibility(View.GONE);
			return;
		}
		btn_ok_dialog_small.setText(left);
		layoutNoDialogSmall.setVisibility(View.GONE);
	}
	public void setButtonMsg(String left, String right){
		if(dialogSize == DIALOG_SIZE_BIG){
			btn_ok_dialog_big.setText(left);
			btn_no_dialog_big.setText(right);
			return;
		}
		layoutNoDialogSmall.setVisibility(View.VISIBLE);
		btn_ok_dialog_small.setText(left);
		btn_no_dialog_small.setText(right);
	}
	
	@Override
	public void show() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		switch (dialogSize) {
		case DIALOG_SIZE_SMALL:
			makeDialogSmall();
			break;
		case DIALOG_SIZE_BIG:
			makeDialogBig();
			break;
		case DIALOG_CHECK:
			makeDialogCheck();
			break;
		case DIALOG_DEFAULT_MSG:
			makeDialogDefaultMsg();
		default:
			break;
		}
		
		
		super.show();
	}


	private void returnResult(int result){
		if(listener == null){
			return;
		}
		
		if (!isNotDismiss && isShowing())
			this.dismiss();
		
		listener.onClick(this, result);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok_dialog_small:
			returnResult(ListenerDialogButton.DIALOG_BTN_ON);
			break;
		case R.id.btn_no_dialog_small:
			returnResult(ListenerDialogButton.DIALOG_BTN_NO);
			break;
		case R.id.btn_ok_dialog_big:
			returnResult(ListenerDialogButton.DIALOG_BTN_ON);
			break;
		case R.id.btn_no_dialog_big:
			returnResult(ListenerDialogButton.DIALOG_BTN_NO);
			break;
		case R.id.btn_answer:
			returnResult(ListenerDialogButton.DIALOG_BTN_ANSWER);
			break;
		case R.id.btn_confirmCancel:
			returnResult(ListenerDialogButton.DIALOG_BTN_NO);
		default:
			break;
		}
	}
	
}
