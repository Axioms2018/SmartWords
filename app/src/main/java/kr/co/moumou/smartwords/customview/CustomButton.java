package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.util.AppUtil;
import kr.co.moumou.smartwords.util.DisplayUtil;

public class CustomButton extends Button {

	private Context mContext;
	
	public CustomButton(Context context) {
		super(context);
		
		if (!isInEditMode())
			init(context, getTextSize(), -1);
	}
	
	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if (!isInEditMode()) {
			int[] textAppearanceAttr = new int[] { android.R.attr.textSize, android.R.attr.textStyle };
			TypedArray appearance = context.obtainStyledAttributes(attrs, textAppearanceAttr);
			int textSize = appearance.getDimensionPixelSize(0, -1);
			int textStyle = appearance.getInt(1, -1);
			
			appearance.recycle();
			
			if (textSize < 1)
				textSize = 22;
			
			init(context, textSize, textStyle);
		}
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		if (!isInEditMode())
			init(context, getTextSize(), -1);
		
	}

	private void init(Context context, float textSize, int textStyle) {
		
		mContext = context;
		
		Activity activity = AppUtil.scanForActivity(context);
		
		float customTextSize = DisplayUtil.getWidthUsingRate(activity, textSize);
		setTextSize(TypedValue.COMPLEX_UNIT_PX, customTextSize);
		
		if (textStyle == 1)
			setTypefaceBold(context);
		else
			setTypeface(context);
	}

	public void setTypeface(Context context) {
		new TypeFaceCache();
		setTypeface(TypeFaceCache.get(context.getAssets(), Constant.FONT_NAME));
	}
	
	public void setTypefaceBold(Context context) {
		
		new TypeFaceCache();
		setTypeface(TypeFaceCache.get(context.getAssets(), Constant.FONT_BOLD_NAME));
		
	}
	
	public void setTextSizeCustom(float textSize) {
		
		Activity activity = AppUtil.scanForActivity(mContext);
		
		float customTextSize = DisplayUtil.getWidthUsingRate(activity, textSize);
		setTextSize(TypedValue.COMPLEX_UNIT_PX, customTextSize);
		
	}
}
