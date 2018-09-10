package kr.co.moumou.smartwords.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.customview.CustomTextView;
import kr.co.moumou.smartwords.activity.SaveWords.SaveWordsComplete;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoNoteData;
import kr.co.moumou.smartwords.vo.VoNoteFrame;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DialogStudent.ListenerDialogButton;

@SuppressLint("HandlerLeak")
public class FragmentMyNote extends Fragment implements OnClickListener {
	
	private ActivityMywordsMain wordsMain = null;
	
	VoNoteFrame voNoteFrame;
	ArrayList<VoNoteData> noteData = new ArrayList<VoNoteData>();
	
	LinearLayout ll_background;
	LinearLayout ll_known;
	ImageView iv_leftballoon;
	TextView tv_knowncnt;
	Button bt_view;
	LinearLayout ll_unknown;
	ImageView iv_rightballoon;
	TextView tv_unknowncnt;

	int all;
	int known;
	int unknown;
	int passRate;
	int wrongRate;
	int rate;
	
	private int left_width = 0;
	private int right_width = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_words_mynote, container, false);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		wordsMain = (ActivityMywordsMain) getActivity();
	}
	
	@SuppressLint("ResourceType")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ll_background = (LinearLayout) getView().findViewById(R.id.ll_background);
		ll_known = (LinearLayout) getView().findViewById(R.id.ll_known);
		iv_leftballoon = (ImageView) getView().findViewById(R.id.iv_leftballoon);
		tv_knowncnt = (CustomTextView) getView().findViewById(R.id.tv_knowcnt);
        bt_view = (Button) getView().findViewById(R.id.bt_view);
        ll_unknown = (LinearLayout) getView().findViewById(R.id.ll_unknown);
        iv_rightballoon = (ImageView) getView().findViewById(R.id.iv_rightballoon);
        tv_unknowncnt = (CustomTextView) getView().findViewById(R.id.tv_unknowcnt);


        DisplayUtil.setLayout(wordsMain, 206, 187, iv_leftballoon);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 0, tv_knowncnt);
        DisplayUtil.setLayout(wordsMain, 184, 60, bt_view);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 44, 0, 0, bt_view);
        DisplayUtil.setLayout(wordsMain, 206, 187, iv_rightballoon);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 0, tv_unknowncnt);
        
        rate = (int) DisplayUtil.getWidthUsingRate(wordsMain, 2);
        
        bt_view.setOnClickListener(this);
        
        LogTraceMin.I("rate : " + DisplayUtil.getWidthUsingRate(wordsMain, 2) + "--------------------------------------------------------------");
        //requestData();
        
        width = (int) DisplayUtil.getWidthUsingRate(wordsMain, 206);
        height = (int) DisplayUtil.getHeightUsingRate(wordsMain, 193);
        
	}
	
	
	int width = 0;
	int height = 0;
	
	public void setAnimation(int x, int y) {
			
		LogTraceMin.I("setAnimation :: left_width : " + left_width + ", x : " + x + " / right_width : " + right_width + " , y : " + y);
		
		Message msg = Message.obtain();
		
		if(left_width < x){
			if(x != 0 && left_width < x){
				left_width = left_width + rate;
			}
			msg.arg1 = left_width * rate;
		}
		if(right_width < y){
			if(y != 0 && right_width < y){
				right_width = right_width + rate;
			}
			msg.arg2 = right_width * rate;
		}
		mHandler.sendMessageDelayed(msg, 20);
	}
	
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.arg1 == 0 && msg.arg2 == 0) {
				msg.arg1 = 0;
				msg.arg2 = 0;
				left_width = 0;
				right_width = 0;
			}
			else{
				
				//바구니 크기
				if(msg.arg1 > 0) {
					LinearLayout.LayoutParams left_params = new LinearLayout.LayoutParams(width + msg.arg1, height + msg.arg1);
					left_params.bottomMargin = -13;
					iv_leftballoon.setLayoutParams(left_params);
				}
				
				if(msg.arg2 > 0) {
					LinearLayout.LayoutParams right_params = new LinearLayout.LayoutParams(width + msg.arg2, height + msg.arg2);
					right_params.bottomMargin = -13;
					iv_rightballoon.setLayoutParams(right_params);
				}
				
				setAnimation(passRate, wrongRate);
			}
			
			LogTraceMin.I("pass : " + (passRate * rate) + " / arg1 : " + msg.arg1);
			LogTraceMin.I("wrong : " + (wrongRate * rate) + " / arg2 : " + msg.arg2);
		}				
	};

	public void setData() {
		all = known + unknown;
		try {
			passRate = known * 100 / all;
			wrongRate = unknown * 100 / all;
		} catch (ArithmeticException e) {
			passRate = 0;
			wrongRate = 0;
		}
		
		tv_knowncnt.setText(known + "");
		tv_unknowncnt.setText(unknown + "");

		setAnimation(passRate, wrongRate);
	}
	
	private void requestData() {		
        wordsMain.showLoadingProgress(getResources().getString(R.string.wait_for_data));

        String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_USERNOTE);
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, VoMyInfo.getInstance().getUSERID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoMyInfo.getInstance().getSESSIONID());
		builder.appendQueryParameter("COMMAND",ConstantsCommCommand.COMMAND_1894_SMARTWORDS_MY);

		AndroidNetworkRequest.getInstance(getActivity()).StringRequest(ConstantsCommURL.REQUEST_TAG_USERNOTE, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				wordsMain.hideProgress();

				voNoteFrame = ApplicationPool.getGson().fromJson(response, VoNoteFrame.class);
				noteData = voNoteFrame.getSTD_LEVEL();
				known = voNoteFrame.getRIGHT_CNT();
				unknown = voNoteFrame.getWRONG_CNT();

				setData();
			}

			@Override
			public void systemcheck(String response) {

			}

			@Override
			public void fail(VoBase base) {

			}

			@Override
			public void exception(ANError error) {

			}

			@Override
			public void dismissDialog() {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_view :
				if(all != 0) {
					Intent intent = new Intent(wordsMain, ActivityWordsList.class);
					intent.putExtra("array", noteData);
					//wordsMain.startActivity(intent);
					startActivityForResult(intent, ActivityWordTestQuizSelect.WORDTEST_QUIZ_TYPE);
				} else {
					final DialogStudent notePop = new DialogStudent(wordsMain);
					notePop.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
					notePop.show();
					notePop.setButtonMsg("확인");
					notePop.setMessage("학습한 단어가 없습니다.");
					notePop.setCancelable(false);
					notePop.setCanceledOnTouchOutside(true);
					notePop.setListener(new ListenerDialogButton() {
			            @Override
			            public void onClick(Dialog dialog, int result) {
			            	if (dialog != null && dialog.isShowing()){
			            		notePop.dismiss();
			            	}			            		
			            }
			        });
				}
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//LogTraceMin.I("재실행 풍선 애니메이션");
		DisplayUtil.setLayout(wordsMain, 206, 187, iv_leftballoon);
		DisplayUtil.setLayout(wordsMain, 206, 187, iv_rightballoon);
		saveDBFromAllInnerDB();
	}
	
	/**
	 * 내부 DB에 있는 데이터 전부 서버에 저장
	 */
	private void saveDBFromAllInnerDB() {
		LogTraceMin.I("내부 DB에 있는 데이터 전부 서버에 저장");
		wordsMain.showLoadingProgress(getResources().getString(R.string.wait_for_data));
		SaveWords saveWords = new SaveWords(wordsMain);
		saveWords.setSaveWordsComplete(saveComplete);
		saveWords.saveReqInnerDB();
	}
	
	SaveWordsComplete saveComplete = new SaveWordsComplete() {

		@Override
		public void success() {
			requestData();
		}

		@Override
		public void fail() {

		}

		@Override
		public void exception() {

		}
	
	};
	
//	private void doubleLoginDetected() {
//		Intent intent = new Intent(wordsMain, ActivityLogin.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra(Constant.IntentKeys.INTENT_DOUBLE_LOGIN, true);
//		startActivity(intent);
//	}

}
