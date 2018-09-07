package kr.co.moumou.smartwords.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.vo.VoMyWordsTopMenuItem;

public class ViewMyWordsTopMenu extends LinearLayout {

	private Context context;
	private Activity activity;
	
	private View base;
	public LinearLayout lay_menu;
	
	private MenuClickListener listener = null;

	private int CURRENT_TABI_NDEX = 0;
	
	@SuppressLint("NewApi")
	public ViewMyWordsTopMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public ViewMyWordsTopMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;		
		init();
	}

	public ViewMyWordsTopMenu(Context context) {
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
	
	public void setActivity(Activity activity){
		this.activity = activity;	
	}
	
	public void setListener(MenuClickListener listener) {
		this.listener = listener;
	}
	
	public interface MenuClickListener {
		void onClick(int index);
	}
	
	private void init(){
		LayoutInflater infaltor = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		base = infaltor.inflate(R.layout.view_mywords_top_menu, this);
		
		lay_menu = (LinearLayout)base.findViewById(R.id.lay_top_menu);
		
		addMenu(getWordsTopMenu());
	}
	
	public ArrayList<VoMyWordsTopMenuItem> getWordsTopMenu(){
		ArrayList<VoMyWordsTopMenuItem> result = new ArrayList<VoMyWordsTopMenuItem>();
		result.add(new VoMyWordsTopMenuItem("결과레포트"));
		result.add(new VoMyWordsTopMenuItem("단어장"));
		result.add(new VoMyWordsTopMenuItem("학습달력"));
		result.add(new VoMyWordsTopMenuItem("단어왕"));

		return result;
	}	
	
	public void setMenus(ArrayList<VoMyWordsTopMenuItem> menu){
		addMenu(menu);
	}
	
	private void addMenu(ArrayList<VoMyWordsTopMenuItem> menu){
		if(lay_menu.getChildCount() > 0){
			lay_menu.removeAllViews();
		}			
			
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		params.gravity= Gravity.CENTER;

		int size = menu.size();
		for (int i = 0; i < size; i++) {
			VoMyWordsTopMenuItem item = menu.get(i);

			CustomTextView tv = new CustomTextView(context);
			tv.setText(item.getTitle());
			tv.setTag(tv.getId(), i);
			tv.setGravity(Gravity.CENTER);

			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setSelectedTab((Integer)v.getTag(v.getId()));
				}
			});

			tv.setBackgroundResource(R.drawable.menu_bg_selector);
			tv.setTextColor(context.getResources().getColorStateList(R.drawable.menu_text_color_selector));

			tv.setTextSizeCustom(getResources().getDimension(R.dimen.efont12));
			tv.setLayoutParams(params);
			lay_menu.addView(tv);
		}

		LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(-1, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0.0f);
				
//		ImageButton close = new ImageButton(context);
//		close.setBackgroundResource(R.drawable.word_test_close);
//		close.setImageResource(R.drawable.icon_main_close);
//		close.setScaleType(ScaleType.CENTER);
//		close.setLayoutParams(btn_params);
		
		ImageButton close = (ImageButton) base.findViewById(R.id.btn_close_words);
		DisplayUtil.setLayoutWidth((Activity) context, 84, close);
		
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
				
			}
		});
		
		//DisplayUtil.setLayoutWidth((Activity) context, 67, close);
		//lay_menu.addView(close);
		
	}
	
	public void setSelectedTab(int index){
		if(index == -1){
			return;
		}
		
		if(CURRENT_TABI_NDEX == index){
			return;
		}
		
		CURRENT_TABI_NDEX = index;
		
		listener.onClick(index);
		
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
	
}
