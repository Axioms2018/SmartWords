package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;

public class ViewIndicator extends LinearLayout {
	private Context context;
	private LayoutInflater inflater;
	private int imgResource = R.drawable.sel_indicator_dot;
	private int rightMargin = 5;
	private int leftMargin = 5;
	private int width, height = 0;
	private boolean isLastPadding = false;
	
	public ViewIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public ViewIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ViewIndicator(Context context) {
		super(context);
		this.context = context;
	}
	
	public void setImgResource(int imgResource) {
		this.imgResource = imgResource;
	}
	
	public void setMarginValue(int left, int right) {
		this.leftMargin = left;
		this.rightMargin = right;
		this.isLastPadding = true;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setCurrentPage(int page){
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if(i == (page)){
				this.getChildAt(i).setSelected(true);
			}else{
				this.getChildAt(i).setSelected(false);
			}
		}
	}
	
	public void setCurrentPage2(int page){
		for(int i = 0; i < page; i++) {
			this.getChildAt(i).setSelected(true);
		}
	}
	
	public void setPageCount(int count){
		removeAllViews();
		
		for (int i = 0; i < count; i++) {
			ImageView view = new ImageView(context);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.leftMargin = leftMargin;
			params.rightMargin = rightMargin;
			
			if(isLastPadding && count - 1 == i) params.rightMargin = 0;
			
			view.setImageResource(imgResource);
			view.setLayoutParams(params);
			this.addView(view);
			
			if(width > 0 && height > 0) {
				DisplayUtil.setLayout((Activity) context, width, height, view);
			}
			
			if(i == 0){
				view.setSelected(true);	
			}
		}
		
		this.refreshDrawableState();
	}
	
}
