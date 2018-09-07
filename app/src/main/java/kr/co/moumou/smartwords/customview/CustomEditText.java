package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.util.AppUtil;
import kr.co.moumou.smartwords.util.DisplayUtil;

public class CustomEditText extends EditText {

	private Context mContext;
	
	public CustomEditText(Context context) {
		super(context);
		
		if (!isInEditMode())
			init(context, getTextSize(), -1);
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if (!isInEditMode()) {
			int[] textAppearanceAttr = new int[] { android.R.attr.textSize, android.R.attr.textStyle };
			TypedArray appearance = context.obtainStyledAttributes(attrs, textAppearanceAttr);
			int textSize = appearance.getDimensionPixelSize(0, -1);
			int textStyle = appearance.getInt(1, -1);
			
			appearance.recycle();
			
	//		LogUtil.d("textSize 11 : " + textSize);
			
			if (textSize < 1)
				textSize = 22;
			
			init(context, textSize, textStyle);
		}
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		if (!isInEditMode())
			init(context, getTextSize(), -1);
	}

	public void setMaxLength(int length) {
		InputFilter[] filter = new InputFilter[1];
		filter[0] = new InputFilter.LengthFilter(length);

		setFilters(filter);
	}
	
	private void init(Context context, float textSize, int textStyle) {
		
		mContext = context;
		
		setMaxLength(650);
		
		setImeOptions(EditorInfo.IME_ACTION_NONE);
//		setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		
		setPrivateImeOptions("defaultInputmode=english;");
		
		setSingleLine();
		
		Activity activity = AppUtil.scanForActivity(context);
		
		float customTextSize = DisplayUtil.getWidthUsingRate(activity, textSize);
		setTextSize(TypedValue.COMPLEX_UNIT_PX, customTextSize);
		
		if (textStyle == 1)
			setTypefaceBold(context);
		else
			setTypeface(context);
		
		setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
		setLongClickable(false);
	}

	public void setTypeface(Context context) {
		
		if (context == null)
			return;
		
		new TypeFaceCache();
		setTypeface(TypeFaceCache.get(context.getAssets(), Constant.FONT_NAME));
	}
	
	public void setTypefaceBold(Context context) {
		
		if (context == null)
			return;
		
		new TypeFaceCache();
		setTypeface(TypeFaceCache.get(context.getAssets(), Constant.FONT_BOLD_NAME));
		
	}

	public int getTextWidth() {
		measure(0, 0);
		
		return getMeasuredWidth();
		
//		return DisplayUtil.getTextWidth((Activity)mContext, getText().toString(), (int)getTextSize());
	}
	
	public void setTextSizeCustom(float textSize) {
		
		Activity activity = AppUtil.scanForActivity(mContext);
		
		float customTextSize = DisplayUtil.getWidthUsingRate(activity, textSize);
		setTextSize(TypedValue.COMPLEX_UNIT_PX, customTextSize);
		
	}
	
	boolean canPaste()
    {
        return false;
    }
	
	@Override
    public boolean isSuggestionsEnabled()
    {
        return false;
    }
	
	private class ActionModeCallbackInterceptor implements ActionMode.Callback
    {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) { return false; }
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) { return false; }
        public void onDestroyActionMode(ActionMode mode) {}
    }
}
