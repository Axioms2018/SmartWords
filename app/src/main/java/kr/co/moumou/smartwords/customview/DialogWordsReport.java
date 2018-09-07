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

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.customview.WrongAdapter.AllMp3Complete;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoWordsDReportData;
import kr.co.moumou.smartwords.vo.VoWordsDReportDetail;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.dialog.DialogStudent.ListenerDialogButton;

public class DialogWordsReport extends Dialog implements View.OnClickListener {
	
	Context mContext;
	
	VoWordsDReportData voWordsDReportData;
	
	//private int Level;
	private boolean hasTest3 = false;
	private boolean isWordsList = false;
	private String stdWGb = "D";
	private String path;
	
	ArrayList<VoWordsDReportDetail> all_words = new ArrayList<VoWordsDReportDetail>();
	ArrayList<VoWordsDReportDetail> all_test = new ArrayList<VoWordsDReportDetail>();
	ArrayList<VoWordsDReportDetail> test1 = new ArrayList<VoWordsDReportDetail>();
	ArrayList<VoWordsDReportDetail> test2 = new ArrayList<VoWordsDReportDetail>();
	ArrayList<VoWordsDReportDetail> test3 = new ArrayList<VoWordsDReportDetail>();
	
	WrongAdapter wAdapter;
	
	LinearLayout ll_diatitle;
	CustomButton bt_test[] = new CustomButton[3];
	ImageButton ib_close, btn_allplayer;
	LinearLayout ll_diasubtitle, ll_diasubtitle2;
	ListView lv_wrong;
	TextView title_user_answer, txt_no_wrong_test;
	CustomButton bt_allview, bt_wrongview;

	public DialogWordsReport(Context context, VoWordsDReportData data, boolean hasTest3, boolean isWordList, ArrayList<VoWordsDReportDetail> wordList, String stdWGb, String path) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.mContext = context;
		this.voWordsDReportData = data;
		this.hasTest3 = hasTest3;
		this.isWordsList = isWordList;
		this.all_words = wordList;
		this.stdWGb = stdWGb;
		this.path = path;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_dreport);
		
		ll_diatitle = (LinearLayout) findViewById(R.id.ll_diatitle);
		bt_test[0] = (CustomButton) findViewById(R.id.bt_test1);
		bt_test[1] = (CustomButton) findViewById(R.id.bt_test2);
		bt_test[2] = (CustomButton) findViewById(R.id.bt_test3);
		ib_close = (ImageButton) findViewById(R.id.ib_close);
		ll_diasubtitle = (LinearLayout) findViewById(R.id.ll_diasubtitle);
		ll_diasubtitle2 = (LinearLayout) findViewById(R.id.ll_diasubtitle2);
		lv_wrong = (ListView) findViewById(R.id.lv_wrong);
		title_user_answer = (TextView) findViewById(R.id.title_user_answer);
		txt_no_wrong_test = (TextView) findViewById(R.id.txt_no_wrong_test);
		
		bt_allview = (CustomButton) findViewById(R.id.bt_allview);
		bt_wrongview = (CustomButton) findViewById(R.id.bt_wrongview);
		bt_allview.setOnClickListener(this);
		bt_wrongview.setOnClickListener(this);
		
		btn_allplayer = (ImageButton) findViewById(R.id.btn_allplayer);
		btn_allplayer.setOnClickListener(this);
		
		WindowManager.LayoutParams diaWindow = new WindowManager.LayoutParams();
		diaWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		diaWindow.dimAmount = 0.8f;
		
		getWindow().setAttributes(diaWindow);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		DisplayUtil.setLayout((Activity) mContext, 150, 52, bt_allview);
		DisplayUtil.setLayout((Activity) mContext, 150, 52, bt_wrongview);
		DisplayUtil.setLayout((Activity) mContext, 756, 102, ll_diatitle);
		DisplayUtil.setLayout((Activity) mContext, 109, 52, bt_test[0]);
		DisplayUtil.setLayout((Activity) mContext, 109, 52, bt_test[1]);
		DisplayUtil.setLayout((Activity) mContext, 109, 52, bt_test[2]);
		DisplayUtil.setLayoutMargin((Activity) mContext, 20, 0, 0, 0, bt_test[0]);
		DisplayUtil.setLayout((Activity) mContext, 756, 70, ll_diasubtitle);
		DisplayUtil.setLayout((Activity) mContext, 756, 70, ll_diasubtitle2);
		DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 20, 0, ib_close);
		DisplayUtil.setLayout((Activity) mContext, 52, 52, ib_close);
		DisplayUtil.setLayout((Activity) mContext, 756, 525, findViewById(R.id.ll_list));
		
		init();
		
		wAdapter = new WrongAdapter(mContext);
		ib_close.setOnClickListener(this);
		
		for(int i = 0 ; i < 3 ; i ++) {
			bt_test[i].setOnClickListener(this);
		}
		
		if(isWordsList) {
			setSelect(3);
		}else{
			setSelect(0);
		}
		
		lv_wrong.setDividerHeight((int) DisplayUtil.getHeightUsingRate((Activity) mContext, 2));
		lv_wrong.setAdapter(wAdapter);
	}
	
	public void init() {
		all_test = voWordsDReportData.getUSERWORD();
		
		if(all_test != null && all_test.size() > 0) {
			
			if(Constant.STD_W_GB_R.equals(stdWGb)) {	// Review일 때,
				for(int i = 0 ; i < all_test.size() ; i ++) {
					
					if("103007".equals(all_test.get(i).getSTD_STEP())) {
						test1.add(all_test.get(i));
					} else if("103008".equals(all_test.get(i).getSTD_STEP())) {
						test2.add(all_test.get(i));
					}else if("103009".equals(all_test.get(i).getSTD_STEP())) {
						test3.add(all_test.get(i));
					}
				}
			}else{
			
				for(int i = 0 ; i < all_test.size() ; i ++) {	// Day일 때,
					
					if("103002".equals(all_test.get(i).getSTD_STEP())) {
						test1.add(all_test.get(i));
					} else if("103004".equals(all_test.get(i).getSTD_STEP())) {
						test2.add(all_test.get(i));
					} else if("103005".equals(all_test.get(i).getSTD_STEP())) {
						test3.add(all_test.get(i));
					}
				}
			}
		}
		
		if(!hasTest3) {
			bt_test[2].setVisibility(View.GONE);
			bt_test[1].setBackgroundResource(R.drawable.sel_tab_right);
		} else {
			bt_test[2].setVisibility(View.VISIBLE);
			bt_test[1].setBackgroundResource(R.drawable.sel_tab_center);
		}
	}
	
	public void setTest(int index) {
		txt_no_wrong_test.setVisibility(View.GONE);
		if(index == 0) {
			if(test1.size() == 0) txt_no_wrong_test.setVisibility(View.VISIBLE);
			findViewById(R.id.ll_btns).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_std_words).setVisibility(View.GONE);
			ll_diasubtitle.setVisibility(View.VISIBLE);
			ll_diasubtitle2.setVisibility(View.GONE);
			wAdapter.setList(test1, false, path);
			title_user_answer.setText(mContext.getResources().getString(R.string.diadreport_title3));
			bt_allview.setVisibility(View.VISIBLE);
			bt_wrongview.setVisibility(View.GONE);
			btn_allplayer.setSelected(false);
		} else if(index == 1) {
			if(test2.size() == 0) txt_no_wrong_test.setVisibility(View.VISIBLE);
			ll_diasubtitle.setVisibility(View.VISIBLE);
			ll_diasubtitle2.setVisibility(View.GONE);
			wAdapter.setList(test2, false, path);
			title_user_answer.setText(mContext.getResources().getString(R.string.diadreport_title3_2));
			btn_allplayer.setSelected(false);
		} else if(index == 2) {
			if(test3.size() == 0) txt_no_wrong_test.setVisibility(View.VISIBLE);
			ll_diasubtitle.setVisibility(View.VISIBLE);
			ll_diasubtitle2.setVisibility(View.GONE);
			wAdapter.setList(test3, false, path);
			title_user_answer.setText(mContext.getResources().getString(R.string.diadreport_title3_2));
			btn_allplayer.setSelected(false);
		} else if(index == 3) {
			findViewById(R.id.ll_btns).setVisibility(View.GONE);
			findViewById(R.id.tv_std_words).setVisibility(View.VISIBLE);
			bt_allview.setVisibility(View.GONE);
			bt_wrongview.setVisibility(View.VISIBLE);
			ll_diasubtitle.setVisibility(View.GONE);
			ll_diasubtitle2.setVisibility(View.VISIBLE);
			wAdapter.setList(all_words, true, path);
			title_user_answer.setText(mContext.getResources().getString(R.string.diadreport_title3_2));
		} 
		wAdapter.notifyDataSetChanged();
	}
	
	public void setSelect(int position) {
		for(int i = 0 ; i < 3 ; i ++) {
			if(i == position) {
				bt_test[i].setSelected(true);
			} else {
				bt_test[i].setSelected(false);
			}
		}
		
		setTest(position);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_close :
				onBackPressed();
				break;
			case R.id.bt_wrongview :
				if(all_test == null || all_test.size() == 0) {
					final DialogStudent mDialog = new DialogStudent(mContext);
					mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
					mDialog.show();
					mDialog.setButtonMsg("확인");
					mDialog.setMessage("틀린 단어가 없습니다.");
					mDialog.setCancelable(false);
					mDialog.setCanceledOnTouchOutside(true);
					mDialog.setListener(new ListenerDialogButton() {
			            @Override
			            public void onClick(Dialog dialog, int result) {
			            	mDialog.dismiss();
			            }
			        });
					break;
				}
				
			case R.id.bt_test1 :
				setSelect(0);
				break;
				
			case R.id.bt_test2 :
				setSelect(1);
				break;
				
			case R.id.bt_test3 :
				setSelect(2);
				break;
				
			case R.id.bt_allview :
				setSelect(3);
				break;
				
			case R.id.btn_allplayer :
				
				wAdapter.allMp3Play(allMp3Complete);
				break;

			default:
				break;
		}
		
	}
	
	AllMp3Complete allMp3Complete = new AllMp3Complete() {
		
		@Override
		public void onPrepared() {
			btn_allplayer.setSelected(true);
		}
		
		@Override
		public void onComplete() {
			btn_allplayer.setSelected(false);
		}
		
	};
	
	public void onBackPressed() {
		wAdapter.destoryAllMp3Play();
		dismiss();
	}


}
