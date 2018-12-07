package kr.co.moumou.smartwords.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.moumou.smartwords.activity.ActivityMywordsMain;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
//import kr.co.moumou.smartwords.activity.ActivityMywordsMain;
import kr.co.moumou.smartwords.activity.ActivityWordTestMain;
import kr.co.moumou.smartwords.communication.Const;
import kr.co.moumou.smartwords.dialog.DialogStudent;
import kr.co.moumou.smartwords.sign.ActivityLogin;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoTopMenuItem;
import kr.co.moumou.smartwords.vo.VoUserInfo;

public class ViewTopMenu extends LinearLayout {

	private Context context;
	private Activity activity;

	private View base;
	private LinearLayout lay_menu;
	private LinearLayout lay_sub_study;

	private View layoutTitle;
	private TextView tv_title;
	private TextView tv_subtitle;
	private int menuType = -1;

	private ImageButton btn_close_words;

	private boolean isMain = false;

	private static int CURRENT_TABI_NDEX = 0;
	public ViewTopMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public ViewTopMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		int menuType = context.obtainStyledAttributes( attrs, R.styleable.ViewTopMenu ).getInt( R.styleable.ViewTopMenu_menuType, 0 );

		init(menuType);
	}

	public ViewTopMenu(Context context) {
		super(context);
		this.context = context;
		init();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if(hasWindowFocus){
			lay_menu.getChildAt(CURRENT_TABI_NDEX).setSelected(true);
		}
	}
	public void setTabClickEnable(boolean isEnable){
		int count = lay_menu.getChildCount();
		for (int i = 0; i < count; i++) {
			View v = lay_menu.getChildAt(i);
			v.setEnabled(isEnable);
		}
	}

	public void setActivity(Activity activity){
		this.activity = activity;
	}

	public void setaaa(boolean abc){
		isMain = abc;
	}
	public void setTitle(String title){
		tv_title.setText(title);
	}
	public void setTitle(String title, String date){
		tv_title.setText(title);
		tv_subtitle.setVisibility(View.VISIBLE);
		tv_subtitle.setText(date);
	}
	public void init() {
		init(0);
	}

		String userGb = VoMyInfo.getInstance().getUSERGB();
		String userId = SharedPrefData.getStringSharedData(context, SharedPrefData.SHARED_USER_ID_S, Constant.STRING_DEFAULT);




	public void init(int menuType){
		LayoutInflater infaltor = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		base = infaltor.inflate(R.layout.view_top_menu_words, this);

		lay_menu = (LinearLayout)base.findViewById(R.id.lay_top_menu);
		DisplayUtil.setLayoutHeight((Activity)context, 66, lay_menu);
		DisplayUtil.setLayoutHeight((Activity)context, 67, lay_menu);

		btn_close_words = (ImageButton) base.findViewById(R.id.btn_close_words);
		DisplayUtil.setLayout((Activity) context, 84, 67, btn_close_words);




				btn_close_words.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//10번 마지막창에서 종료시 로그아웃 안내 나오게 만들기
					//test1 practice test2,3 선택화면도 첫화면으로되서 그상황에 눌러도 로그아웃 안내 나와서 수정필요

					LogTraceMin.I("ismain : " + isMain);


					if(isMain) {
						final DialogStudent mDialog = new DialogStudent(getContext());
						mDialog.setDialogSize(DialogStudent.DIALOG_SIZE_SMALL);
						mDialog.show();
						mDialog.setButtonMsg("예", "아니오");
						mDialog.setMessage("앱을 종료하시겠습니까?");
						mDialog.setCancelable(true);
						mDialog.setCanceledOnTouchOutside(true);
						mDialog.setListener(new DialogStudent.ListenerDialogButton() {
							@Override
							public void onClick(Dialog dialog, int result) {
								if(result == DIALOG_BTN_ON) {
									((Activity) context).finish();
								}else{
									mDialog.dismiss();
								}
							}
						});
					}else {
						activity.finish();
					}


//					activity.finish();


//					for (int i = 0; i < ActivityWordTestMain.actList.size(); i++) {
//						if(i == 0){
//							AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//							builder.setTitle("로그아웃");
//							builder.setMessage(R.string.dialog_logout);
//							builder.setPositiveButton("예",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog, int which) {
//											logoutGoLoginActivity();
//										}
//									});
//							builder.setNegativeButton("아니오",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog, int which) {
//											return;
//										}
//									});
//							builder.show();
//						}else {
//							for(int j = 1; i < j ; j++)
//							ActivityWordTestMain.actList.get(i).finish();
//						}
//
//					}
				}
			});


		addMenu(getWordTest());

	}

	private ArrayList<VoTopMenuItem> getTopMenu(){
		ArrayList<VoTopMenuItem> result = new ArrayList<VoTopMenuItem>();
//
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_study_select), ActivityStudyBookSelect.class));
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_study_history), ActivityStudyHistory.class));
//
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_smart_words), ActivityWordTestMain.class));
//
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_ebook), ActivityEbookSelect.class));
//
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_more), ActivityMoreLibrary.class));

		return result;
	}

	public ArrayList<VoTopMenuItem> getMoreTopMenu(){
		ArrayList<VoTopMenuItem> result = new ArrayList<VoTopMenuItem>();
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_library), ActivityMoreLibrary.class));
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_support), ActivityMoreFaq.class));

		return result;
	}

	public ArrayList<VoTopMenuItem> getMoreSupportMenu(){
		ArrayList<VoTopMenuItem> result = new ArrayList<VoTopMenuItem>();
//
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_faq), ActivityMoreFaq.class));
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_notice), ActivityMoreNotice.class));
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_access_terms), ActivityMoreClause.class));
//		result.add(new VoTopMenuItem(context.getString(R.string.top_menu_surpport_service), ActivityMoreRemote.class));

		return result;
	}

	public ArrayList<VoTopMenuItem> getWordTest(){
		ArrayList<VoTopMenuItem> result = new ArrayList<VoTopMenuItem>();
		result.add(new VoTopMenuItem("WordTest", ActivityWordTestMain.class));
		result.add(new VoTopMenuItem("MyPage", ActivityMywordsMain.class));

		return result;
	}

//		public void setMenus(ArrayList<VoTopMenuItem> menu){
//			addMenu(menu);
//		}

	@SuppressLint("ResourceType")
	private void addMenu(ArrayList<VoTopMenuItem> menu) {
		if (lay_menu.getChildCount() > 0) {
			lay_menu.removeAllViews();
		}

		int size = menu.size();

		for (int i = 0; i < size; i++) {
			VoTopMenuItem item = menu.get(i);

			CustomTextView tv = new CustomTextView(context);
			tv.setText(item.getTitle());
			tv.setTag(item.getLaunchActivity());
			tv.setTag(tv.getId(), i);
			tv.setGravity(Gravity.CENTER);

			tv.setBackgroundResource(R.drawable.words_menu_bg_selector);
			tv.setTextColor(context.getResources().getColorStateList(R.drawable.ebook_menu_text_color_selector));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
			params.gravity= Gravity.CENTER;
			tv.setTextSizeCustom(getResources().getDimension(R.dimen.efont16));
			tv.setLayoutParams(params);
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(CURRENT_TABI_NDEX == (Integer)v.getTag(v.getId())) return;
					launchActivity((Class<?>)v.getTag(), (Integer)v.getTag(v.getId()), true);
				}
			});



			lay_menu.addView(tv);
		}
	}
//			else if(menuType == 3){
//				tv.setBackgroundResource(R.drawable.ebook_menu_bg_selector);
//				tv.setTextColor(context.getResources().getColorStateList(R.drawable.ebook_menu_text_color_selector));
//			} else {
//				tv.setBackgroundResource(R.drawable.workbook_menu_bg_selector);
//				tv.setTextColor(context.getResources().getColorStateList(R.drawable.workbook_menu_text_color_selector));
//
//				if (ConstantsCommURL.ISTEST || ConstantsCommURL.IS_LOCAL_TEST)
//					tv.setBackgroundColor(Color.parseColor("#333333"));
//			}
//
//			if (menuType == 0) {
//				DisplayUtil.setLayout((Activity)context, 256, 66, tv);
//			}
//
//			((CustomTextView) tv).setTextSizeCustom(26);
//		}
//
////
//		int maxNoticeIdx = SharedPrefData.getIntSharedData(context, SharedPrefData.SHARED_NOTICE_I, 0);
//		int maxFaqIdx = SharedPrefData.getIntSharedData(context, SharedPrefData.SHARED_FAQ_I, 0);
//		int maxLibIdx = SharedPrefData.getIntSharedData(context, SharedPrefData.SHARED_LIBRARY_I, 0);
//
//		boolean isMoreNew = false;
//		boolean isLibNew = false;
//		boolean isCSNew = false;
//		boolean isNotiNew = false;
//		boolean isFaqNew = false;
//
//		SplitInfo maxIdxInfo = null;
//		try {
//			maxIdxInfo = StringUtil.getSplitInfo(VoMyInfo.getInstance().getNOTI_NEW_NO(), Constant.GUBUN_COMMA);
//		} catch (Exception e) {
//			maxIdxInfo.setStrings(new String[]{"0,0,0"});
//		}
//
//		if (maxLibIdx < changeIntToString(maxIdxInfo.get(0)))
//			isLibNew = true;
//
//		if (maxFaqIdx < changeIntToString(maxIdxInfo.get(1)))
//			isFaqNew = true;
//
//		if (maxNoticeIdx < changeIntToString(maxIdxInfo.get(2)))
//			isNotiNew = true;
//
//		if (isLibNew || isFaqNew || isNotiNew)
//			isMoreNew = true;
//
//		if (isFaqNew || isNotiNew)
//			isCSNew = true;
//

//
//			if (("라이브러리".equals(item.getTitle()) && isLibNew) ||
//				("FAQ".equals(item.getTitle()) && isFaqNew) ||
//				("공지사항".equals(item.getTitle()) && isNotiNew) ||
//				("더보기".equals(item.getTitle()) && isMoreNew) ||
//				("고객지원".equals(item.getTitle()) && isCSNew)) {
//
//					tv.setPadding(0, 0, 10, 0);
//
//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
//					params.gravity= Gravity.CENTER;
//
//					LinearLayout layout = new LinearLayout(context);
//					layout.setGravity(Gravity.CENTER);
//					layout.setLayoutParams(params);
//
//					layout.setBackgroundResource(R.drawable.workbook_menu_bg_selector);
//
//					ImageView ivNew = new ImageView(context);
//					ivNew.setBackgroundResource(R.drawable.icon_new);
//					layout.addView(tv);
//					layout.addView(ivNew);
//					lay_menu.addView(layout);
//					DisplayUtil.setLayout((Activity)context, 28, 28, ivNew);
//
//					layout.setTag(item.getLaunchActivity());
//					layout.setTag(layout.getId(), i);
//
//					layout.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							if(CURRENT_TABI_NDEX == (Integer)v.getTag(v.getId())) return;
//
////							launchActivity((Class<?>)v.getTag());
////							setSelectedTab((Integer)v.getTag(v.getId()));
//							launchActivity((Class<?>)v.getTag(), (Integer)v.getTag(v.getId()));
//
//						}
//					});
//			} else {
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
//				params.gravity= Gravity.CENTER;
//				tv.setLayoutParams(params);
//
//				tv.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						TextView tv = (TextView)v;
//						if("학습 선택".equals(tv.getText().toString()) ||
//								"Smart Words".equals(tv.getText().toString()) ||
//								"E-Book".equals(tv.getText().toString())
//								){
//							//학습 상태 초기화
//							VoSocketComm.getInstance().clearStudyData();
//						}
//
//						if("E-Book Library".equals(tv.getText().toString())){
//							if(CURRENT_TABI_NDEX == (Integer)v.getTag(v.getId())) return;
//							/*if(VoMyInfo.getInstance().isPrevMemeber()){
//								showCannotStudyPop();
//								return;
//							}*/
//							launchActivity((Class<?>)v.getTag(), (Integer)v.getTag(v.getId()), false);
//						}else if("E-Book".equals(tv.getText().toString())){
//							/*if("E".equals(VoMyInfo.getInstance().getSTDY_BOOK()) || VoMyInfo.getInstance().isPrevMemeber()){
//								showCannotStudyPop();
//								return;
//							}*/
//							if(CURRENT_TABI_NDEX == (Integer)v.getTag(v.getId())) return;
//							launchActivity((Class<?>)v.getTag(), (Integer)v.getTag(v.getId()));
//
//						}else if("Smart Words".equals(tv.getText().toString())){
//							if(CURRENT_TABI_NDEX == (Integer)v.getTag(v.getId())) return;
//							/*if(VoMyInfo.getInstance().isPrevMemeber()){
//								showCannotStudyPop();
//								return;
//							}*/
//							launchActivity((Class<?>)v.getTag(), (Integer)v.getTag(v.getId()), true);
//						}else{
//							if(CURRENT_TABI_NDEX == (Integer)v.getTag(v.getId())) return;
//							launchActivity((Class<?>)v.getTag(), (Integer)v.getTag(v.getId()));
//						}
//					}
//				});
//
//				lay_menu.addView(tv);
//			}
//		}
//	}

	private int changeIntToString(String str){
		int result = 0;
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return result;
	}
//	private MouMouDialog dialog;
//	private void showCannotStudyPop(){
//		if(dialog != null && dialog.isShowing()){
//			return;
//		}
//		dialog = new MouMouDialog(context);
//		dialog.setSize(0.5, 0.5);
//		dialog.setCancelable(false);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
//		dialog.setButtonText(context.getString(R.string.monthly_test_close));
//		dialog.setDescript(context.getString(R.string.prev_member_cannot_use));
//		dialog.setDescriptGravity(Gravity.CENTER);
//		dialog.setListener(new MouMouDialog.DialogButtonListener() {
//			@Override
//			public void onClick(Dialog dialog, int result) {
//
//				if (dialog != null && dialog.isShowing())
//					dialog.dismiss();
//			}
//		});
//	}


	public void setSelectedTab(int index){
		if(index == -1){
			return;
		}

		if(CURRENT_TABI_NDEX == index){
			return;
		}
		
		CURRENT_TABI_NDEX = index;

		int tabCount = lay_menu.getChildCount();

		if(index >= tabCount){
			return;
		}

		SharedPrefData.setStringSharedData(context, SharedPrefData.SHARED_CON_SEQ_S, Constant.STRING_CON_SEQ_DEFAUL);

		for (int i = 0; i < tabCount; i++) {
			lay_menu.getChildAt(i).setSelected(false);
			if(i == index){
				lay_menu.getChildAt(i).setSelected(true);
			}
		}
	}
	
	private void launchActivity(Class<?> clazz, int index, boolean isFristChange){
		if(!VoMyInfo.getInstance().isPayedUser()){
			Toast.makeText(context, "학습가능기간이 아닙니다.\n학습기간을 확인해주시기 바랍니다.\n학습 현황만 이용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		launchActivity(clazz, isFristChange);
		setSelectedTab(index);
	}
	
	private void launchActivity(Class<?> clazz, int index){
		if(!VoMyInfo.getInstance().isPayedUser()){
			Toast.makeText(context, "학습가능기간이 아닙니다.\n학습기간을 확인해주시기 바랍니다.\n학습 현황만 이용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		launchActivity(clazz);
		setSelectedTab(index);
	}

	private void launchActivity(Class<?> clazz, boolean isFristChange){
		Intent intent = new Intent(context, clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("changeTopView", isFristChange);
		context.startActivity(intent);
	}

	private void launchActivity(Class<?> clazz){
		Intent intent = new Intent(context, clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	public void setTitleVisibility(int visibility) {
		layoutTitle.setVisibility(visibility);
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
