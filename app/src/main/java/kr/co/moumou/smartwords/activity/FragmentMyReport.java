package kr.co.moumou.smartwords.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.Const;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.customview.CustomButton;
import kr.co.moumou.smartwords.customview.CustomTextView;
import kr.co.moumou.smartwords.sign.ActivityLogin;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoUserInfo;
import kr.co.moumou.smartwords.vo.VoWordsReportData;
import kr.co.moumou.smartwords.vo.VoWordsReportDetail;
import kr.co.moumou.smartwords.wordschart.PieGraph;
import kr.co.moumou.smartwords.wordschart.PieSlice;

@SuppressLint("HandlerLeak")
public class FragmentMyReport extends Fragment implements OnClickListener {
	private ActivityMywordsMain wordsMain = null;
	Resources resources;
	public VoUserInfo mUserInfo;
	private Activity activity;

	VoWordsReportData voReportData;
	ArrayList<VoWordsReportDetail> reportDetail;
	
	LinearLayout ll_bg,ll_toggle;

	Button bt_lv[] = new CustomButton[6];

	LinearLayout ll_known,ll_unknown;
	TextView tv_kcount,tv_ucount;
	PieGraph pg_known,pg_unknown;

	LinearLayout ll_result;
	TextView tv_step_t,tv_step,tv_line1,tv_day_t,tv_day,tv_line2,tv_total_t,tv_total,tv_line3,tv_known_t,tv_known,tv_line4,tv_unknown_t,tv_unknown,tv_line5,tv_score_t,tv_score,tv_line6,tv_line7,tv_name,tv_logout;
	
	private String Level;
	private int Day = 0;
	String lv_all;

	@Override
	public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_words_myreport, container, false);
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		wordsMain = (ActivityMywordsMain) getActivity();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if(context instanceof Activity){
			activity = (Activity) context;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ll_bg = (LinearLayout) wordsMain.findViewById(R.id.ll_bg);
		ll_toggle = (LinearLayout) wordsMain.findViewById(R.id.ll_toggle);
		bt_lv[1] = (CustomButton) wordsMain.findViewById(R.id.bt_lv1);
		bt_lv[2] = (CustomButton) wordsMain.findViewById(R.id.bt_lv2);
		bt_lv[3] = (CustomButton) wordsMain.findViewById(R.id.bt_lv3);
		bt_lv[4] = (CustomButton) wordsMain.findViewById(R.id.bt_lv4);
		bt_lv[5] = (CustomButton) wordsMain.findViewById(R.id.bt_lv5);
		bt_lv[0] = (CustomButton) wordsMain.findViewById(R.id.bt_lvall);
		ll_known = (LinearLayout) wordsMain.findViewById(R.id.ll_known);
		pg_known = (PieGraph) wordsMain.findViewById(R.id.pg_known);
		tv_kcount = (CustomTextView) wordsMain.findViewById(R.id.tv_kcount);
		ll_unknown = (LinearLayout) wordsMain.findViewById(R.id.ll_unknown);
		pg_unknown = (PieGraph) wordsMain.findViewById(R.id.pg_unknown);
		tv_ucount = (CustomTextView) wordsMain.findViewById(R.id.tv_ucount);
		ll_result = (LinearLayout) wordsMain.findViewById(R.id.ll_result);
		tv_step_t = (CustomTextView) wordsMain.findViewById(R.id.tv_step_t);
		tv_step = (CustomTextView) wordsMain.findViewById(R.id.tv_step);
		tv_line1 = (CustomTextView) wordsMain.findViewById(R.id.tv_line1);
		tv_day_t = (CustomTextView) wordsMain.findViewById(R.id.tv_day_t);
		tv_day = (CustomTextView) wordsMain.findViewById(R.id.tv_day);
		tv_line2 = (CustomTextView) wordsMain.findViewById(R.id.tv_line2);
		tv_total_t = (CustomTextView) wordsMain.findViewById(R.id.tv_total_t);
		tv_total = (CustomTextView) wordsMain.findViewById(R.id.tv_total);
		tv_line3 = (CustomTextView) wordsMain.findViewById(R.id.tv_line3);
		tv_known_t = (CustomTextView) wordsMain.findViewById(R.id.tv_known_t);
		tv_known = (CustomTextView) wordsMain.findViewById(R.id.tv_known);
		tv_line4 = (CustomTextView) wordsMain.findViewById(R.id.tv_line4);
		tv_unknown_t = (CustomTextView) wordsMain.findViewById(R.id.tv_unknown_t);
		tv_unknown = (CustomTextView) wordsMain.findViewById(R.id.tv_unknown);
		tv_line5 = (CustomTextView) wordsMain.findViewById(R.id.tv_line5);
		tv_score_t = (CustomTextView) wordsMain.findViewById(R.id.tv_score_t);
		tv_score = (CustomTextView) wordsMain.findViewById(R.id.tv_score);
		tv_line6 = (CustomTextView) wordsMain.findViewById(R.id.tv_line6);
		tv_name = (CustomTextView) wordsMain.findViewById(R.id.tv_name);
		tv_logout = (CustomTextView) wordsMain.findViewById(R.id.tv_logout);
		tv_line7 = (CustomTextView) wordsMain.findViewById(R.id.tv_line7);


		tv_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("로그아웃");
				builder.setMessage(R.string.dialog_logout);
				builder.setPositiveButton("예",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								logoutGoLoginActivity();
							}
						});
				builder.setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								return;
							}
						});
				builder.show();
			}
		});


		DisplayUtil.setLayoutMargin(wordsMain, 30, 30, 30, 30, ll_bg);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 30, 0, 0, ll_toggle);
		DisplayUtil.setLayout(wordsMain, 109, 52, bt_lv[1]);
		DisplayUtil.setLayout(wordsMain, 109, 52, bt_lv[2]);
		DisplayUtil.setLayout(wordsMain, 109, 52, bt_lv[3]);
		DisplayUtil.setLayout(wordsMain, 109, 52, bt_lv[4]);
		DisplayUtil.setLayout(wordsMain, 109, 52, bt_lv[5]);
		DisplayUtil.setLayout(wordsMain, 109, 52, bt_lv[0]);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 30, 0, bt_lv[0]);
		DisplayUtil.setLayoutMargin(wordsMain, 70, 0, 30, 0, ll_known);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 0, 16, tv_kcount);
		DisplayUtil.setLayoutMargin(wordsMain, 30, 0, 70, 0, ll_unknown);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 0, 16, tv_ucount);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 40, 0, ll_result);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 0, 0, 0, tv_step_t);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 0, 0, 0, tv_day_t);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 0, 0, 0, tv_total_t);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 0, 0, 0, tv_known_t);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 0, 0, 0, tv_unknown_t);
		DisplayUtil.setLayoutMargin(wordsMain, 10, 0, 0, 0, tv_score_t);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line1);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line1);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line2);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line2);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line3);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line3);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line4);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line4);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line5);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line5);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line6);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line6);
		DisplayUtil.setLayoutHeight(wordsMain, 2, tv_line7);
		DisplayUtil.setLayoutMargin(wordsMain, 0, 20, 0, 30, tv_line7);
		
		for(int i = 0 ; i < 6 ; i ++) {
			bt_lv[i].setEnabled(false);
			bt_lv[i].setOnClickListener(this);
		}
		
		resources = getResources();
		
		lv_all = getResources().getString(R.string.note_taball);
		
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bg_graph);
        
		int w = (int) DisplayUtil.getWidthUsingRate(getActivity(), 320);

		int width = b.getWidth();
		int height = b.getHeight();
		
		Bitmap resized = null;
		while (height > w) {
			resized = Bitmap.createScaledBitmap(b, (width * w) / height, w, true);
			height = resized.getHeight();
			width = resized.getWidth();
		}
		
        if(resized == null) {
        	pg_known.setBackgroundBitmap(b);
            pg_unknown.setBackgroundBitmap(b);
        }else{
        	pg_known.setBackgroundBitmap(resized);
            pg_unknown.setBackgroundBitmap(resized);
        }
        
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(height + 10, height + 10);
        pg_known.setLayoutParams(params);
        pg_unknown.setLayoutParams(params);
        
        
		pg_known.setmLineSize(DisplayUtil.getWidthUsingRate(wordsMain, -14));
		pg_unknown.setmLineSize(DisplayUtil.getWidthUsingRate(wordsMain, -14));

//		DisplayUtil.setLayoutHeight(getActivity(), height + 40, pg_known);
//		DisplayUtil.setLayoutHeight(getActivity(), height + 40, pg_unknown);
		DisplayUtil.setLayoutMargin(getActivity(), 0, 0, 0, 22, pg_known);
		DisplayUtil.setLayoutMargin(getActivity(), 0, 0, 0, 22, pg_unknown);
		
		requestData();
	}
	
	private void setData(int position) {		
		if("0".equals(reportDetail.get(position).getSTD_LEVEL())){
			tv_step.setText(lv_all);
		} else {
			tv_step.setText("Level " + reportDetail.get(position).getSTD_LEVEL());
		}
		tv_kcount.setText(reportDetail.get(position).getRIGHT_CNT() + "");
		tv_ucount.setText(reportDetail.get(position).getWRONG_CNT() + "");
		tv_day.setText(reportDetail.get(position).getSTD_DAY() + " 일");
		tv_total.setText(reportDetail.get(position).getWORD_CNT() + " 단어");
		tv_known.setText(reportDetail.get(position).getRIGHT_CNT() + " 단어");
		tv_unknown.setText(reportDetail.get(position).getWRONG_CNT() + " 단어");
		tv_score.setText(reportDetail.get(position).getWORD_PER() + "%");
		
		setGraph(reportDetail.get(position));
	}
	
	private void setGraph(VoWordsReportDetail voWordsReportDetail) {
		pg_known.removeSlices();
		pg_unknown.removeSlices();
		
		PieSlice kslice = new PieSlice();		
        kslice.setColor(resources.getColor(R.color.daily_graph_right));
        kslice.setGoalValue(voWordsReportDetail.getRIGHT_CNT());
        pg_known.addSlice(kslice);
                
        kslice = new PieSlice();
        kslice.setColor(Color.TRANSPARENT);
        kslice.setValue(10);
        kslice.setGoalValue(voWordsReportDetail.getWRONG_CNT());        
        pg_known.addSlice(kslice);
        
        PieSlice uslice = new PieSlice();		
        uslice.setColor(resources.getColor(R.color.daily_graph_wrong));
        uslice.setGoalValue(voWordsReportDetail.getWRONG_CNT());
        pg_unknown.addSlice(uslice);
        
        uslice = new PieSlice();
        uslice.setColor(Color.TRANSPARENT);
        uslice.setValue(10);
        uslice.setGoalValue(voWordsReportDetail.getRIGHT_CNT());
        pg_unknown.addSlice(uslice);
		
		mHandler.sendEmptyMessage(0);
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pg_known.setDuration(2000);
			pg_known.setAnimationListener(pg_known.getAnimationListener());
			pg_known.animateToGoalValues();
			
			pg_unknown.setDuration(2000);
			pg_unknown.setAnimationListener(pg_known.getAnimationListener());
			pg_unknown.animateToGoalValues();
		}    	
    };
	
	private void requestData() {
		wordsMain.showLoadingProgress(getResources().getString(R.string.wait_for_data));

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_LEVELREPORT);
		Uri.Builder builder = Uri.parse(url).buildUpon();

		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(getContext(),Preferences.PREF_USER_ID,null));



		AndroidNetworkRequest.getInstance(getActivity()).StringRequest(ConstantsCommURL.REQUEST_TAG_LEVELREPORT, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				wordsMain.hideProgress();

				voReportData = ApplicationPool.getGson().fromJson(response, VoWordsReportData.class);
				reportDetail = voReportData.getLEVELRPORT();
				setAble();
				setSelect(0);
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
	
	public void setAble() {
		for(int i = 0 ; i < reportDetail.size() ; i ++) {
			if("Y".equals(reportDetail.get(i).getSTD_YN())) {
				bt_lv[i].setEnabled(true);
			}
		}
	}
	
	public void setSelect(int position) {
		for(int i = 0 ; i < 6 ; i ++) {
			if(i == position) {
				bt_lv[i].setSelected(true);
				if(i == 0) {
					Level = "5";
				} else {
					Level = i + "";
				}				
			} else {
				bt_lv[i].setSelected(false);
			}
		}
		
		setData(position);		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_lv1 :
				setSelect(1);
				break;
				
			case R.id.bt_lv2 :
				setSelect(2);
				break;
				
			case R.id.bt_lv3 :
				setSelect(3);
				break;
				
			case R.id.bt_lv4 :
				setSelect(4);
				break;
				
			case R.id.bt_lv5 :
				setSelect(5);
				break;
				
			case R.id.bt_lvall :
				setSelect(0);
				break;


			default:
				break;
		}
		
	}
	private void logoutGoLoginActivity() {
		//Preferences.putPref(mContext, Preferences.PREF_USER_ID, null);

		Preferences.putPref(activity, Preferences.PREF_USER_PW, null);
		Preferences.putPref(activity, Preferences.PREF_IS_MEMBER, false);
		VoUserInfo.getInstance().clear();

		Intent intent = new Intent(activity, ActivityLogin.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		intent.putExtra(Const.IntentKeys.INTENT_DOUBLE_LOGIN, true);
		activity.startActivity(intent);

//		activity.finish();


	}
	
}
