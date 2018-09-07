package kr.co.moumou.smartwords.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import kr.co.moumou.smartwords.customview.CustomTextView;

public class DisplayUtil {

	public static int getStatusBarHeight(Context context){
	     
	    int statusHeight = 0;
	    int screenSizeType = (context.getResources().getConfiguration().screenLayout &
	            Configuration.SCREENLAYOUT_SIZE_MASK);
	 
	    if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
	        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
	 
	        if (resourceId > 0) {
	            statusHeight = context.getResources().getDimensionPixelSize(resourceId);
	        }
	    }

	    LogUtil.d(statusHeight+"");
	    return statusHeight;
	}
	
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		LogUtil.d(metrics.widthPixels+"");
		return metrics.widthPixels;
	}
	
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		LogUtil.d(metrics.heightPixels+"");
		return metrics.heightPixels;
	}
	
	public static void setHeight(Activity activity, int layoutHeight, View view) {
		
		if (view == null)
			return;
		
		ViewGroup.LayoutParams params = null;
		
		if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
			params = view.getLayoutParams();
		}
		
		if (params == null)
			return;
		
		params.height = layoutHeight;
		
		view.setLayoutParams(params);
		
	}

	public static void setLayoutHeight(Activity activity, int layoutHeight, View view) {
		
		if (view == null)
			return;
		
		ViewGroup.LayoutParams params = null;
		
		if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
			params = view.getLayoutParams();
		}
		
		if (params == null)
			return;
		
		params.height = (int) getHeightUsingRate(activity, layoutHeight);
		
		view.setLayoutParams(params);
		
	}
	
	public static void setLayoutWidth(Activity activity, int layoutWidth, View view) {
		
		if (view == null)
			return;
		
		ViewGroup.LayoutParams params = null;
		
		if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
			params = view.getLayoutParams();
		}
		
		if (params == null)
			return;
		
		params.width = (int) getWidthUsingRate(activity, layoutWidth);
		
		view.setLayoutParams(params);
		
	}
	
	public static void setLayout(Activity activity, int layoutWidth, int layoutHeight, View view) {
		
		if (view == null)
			return;
		
		ViewGroup.LayoutParams params = null;
		
		if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
			params = view.getLayoutParams();
		}
		
		if (params == null)
			return;
		
		params.width = (int) getWidthUsingRate(activity, layoutWidth);
		params.height = (int) getHeightUsingRate(activity, layoutHeight);
		
		view.setLayoutParams(params);
	}
	
	public static void setWidth(Activity activity, int layoutWidth, View view) {
		
		if (view == null)
			return;
		
		ViewGroup.LayoutParams params = null;
		
		if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
			params = view.getLayoutParams();
		} else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
			params = view.getLayoutParams();
		}
		
		if (params == null)
			return;
		
		params.width = layoutWidth;
		
		view.setLayoutParams(params);
	}

	public static void setLayoutMargin(Activity activity, int left, int top, int right, int bottom, View view) {
		
		if (view == null)
			return;
		
		left = (int) getWidthUsingRate(activity, left);
		top = (int) getHeightUsingRate(activity, top);
		right = (int) getWidthUsingRate(activity, right);
		bottom = (int) getHeightUsingRate(activity, bottom);
		
		if (view.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
			android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) view.getLayoutParams();
			params.setMargins(left, top, right, bottom);
		} else if (view.getLayoutParams() instanceof android.widget.LinearLayout.LayoutParams) {
			android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view.getLayoutParams();
			params.setMargins(left, top, right, bottom);
		} else if (view.getLayoutParams() instanceof android.widget.FrameLayout.LayoutParams) {
			android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
			params.setMargins(left, top, right, bottom);
		}
		
	}
	
	public static void setMinLayout(Activity activity, int layoutWidth, int layoutHeight, View view) {
		
		if (view == null)
			return;
		
		view.setMinimumWidth((int) getWidthUsingRate(activity, layoutWidth));
		view.setMinimumHeight((int) getHeightUsingRate(activity, layoutHeight));
		
	}
	
	public static void setMaxLayoutWidth(Activity activity, int layoutWidth, TextView view) {
		
		if (view == null)
			return;
		
		view.setMaxWidth((int) getWidthUsingRate(activity, layoutWidth));
	}
	
	public static void setMaxLayout(Activity activity, int layoutWidth, int layoutHeight, TextView view) {
		
		if (view == null)
			return;
		
		view.setMaxWidth((int) getWidthUsingRate(activity, layoutWidth));
		view.setMaxHeight((int) getWidthUsingRate(activity, layoutHeight));
	}
	
	public static void setMaxLayout(Activity activity, int layoutWidth, int layoutHeight, ImageView view) {
		
		if (view == null)
			return;
		
		view.setMaxWidth((int) getWidthUsingRate(activity, layoutWidth));
		view.setMaxHeight((int) getWidthUsingRate(activity, layoutHeight));
	}

	public static void setMaxLayoutHeight(Activity activity, int layoutHeight, TextView view) {
		
		if (view == null)
			return;
		
		view.setMaxHeight((int) getWidthUsingRate(activity, layoutHeight));
	}

	public static void setMinLayoutWidth(Activity activity, int layoutWidth, View view) {
		
		if (view == null)
			return;
		
		view.setMinimumWidth((int) getWidthUsingRate(activity, layoutWidth));
		
	}
	
	public static void setMinLayoutHeight(Activity activity, int layoutHeight, View view) {
		
		if (view == null)
			return;
		
		view.setMinimumHeight((int) getHeightUsingRate(activity, layoutHeight));
		
//		setLayoutPadding(activity, 20, 0, 20, 0, view);
		
	}
	
	public static void setLayoutPadding(Activity activity, float left, float top, float right, float bottom, View view) {

		if (view == null)
			return;
		
		int retLeft = (int) getWidthUsingRate(activity, left);
		int retTop = (int) getHeightUsingRate(activity, top);
		int retRight = (int) getWidthUsingRate(activity, right);
		int retBottom = (int) getHeightUsingRate(activity, bottom);
		
		view.setPadding(retLeft, retTop, retRight, retBottom);
		
	}
	
	public static void setLayoutPadding(Activity activity, int[] padding, View view) {

		if (view == null)
			return;
		
		if (padding.length < 4)
			return;
		
		int retLeft = (int) getWidthUsingRate(activity, padding[0]);
		int retTop = (int) getHeightUsingRate(activity, padding[1]);
		int retRight = (int) getWidthUsingRate(activity, padding[2]);
		int retBottom = (int) getHeightUsingRate(activity, padding[3]);
		
		view.setPadding(retLeft, retTop, retRight, retBottom);
		
	}
	
	public static float getHeightUsingRate(Activity activity, float height) {
		// edit height
//		int targetDeviceHeight = 727; // 대상 디바이스 높이 1280 * 800 기준 48은 소프트 키보드 높이
		int targetDeviceHeight = 752; // 대상 디바이스 높이 1280 * 800 기준 48은 소프트 키보드 높이
		
		// 현재 디바이스의 높이
//		float screenHeight = Preferences.getScreenHeight(activity) - getStatusBarHeight(activity);
		float screenHeight = Preferences.getScreenHeight(activity) - Preferences.getStatusBarHeight(activity);
		
		// 대상 디바이스 높이를 기준으로 레이아웃의 높이를 비율로 가져온다
		float rate = height / targetDeviceHeight  * 100;
		
//		LogUtil.d("screenHeight : " + screenHeight + " / height : " + height);
//		LogUtil.d("getHeightUsingRate rate : " + rate);
		
		// 현재 디바이스를 기준으로 위에서 구한 비율만큼의 레이아웃의 높이를 가져온다
		float retHeight = (float) (screenHeight * rate / 100.0);
		
//		LogUtil.d("retHeight : " + retHeight);
				
		return retHeight;
	}
	
	public static float getWidthUsingRate(Activity activity, float width) {
		int targetDeviceWidth = 1280; // 대상 디바이스 가로
		
		// 현재 디바이스의 높이
		float screenWidth = Preferences.getScreenWidth(activity);
//		float screenWidth = DisplayUtil.getScreenWidth(activity);
		
//		LogUtil.d("screenWidth : " + screenWidth + " / " + screenWidth2);
		
		// 대상 디바이스 높이를 기준으로 레이아웃의 높이를 비율로 가져온다
		float rate = width / targetDeviceWidth  * 100;
		
		// 현재 디바이스를 기준으로 위에서 구한 비율만큼의 레이아웃의 높이를 가져온다
		float retWidth = (float) (screenWidth * rate / 100.0);
		
//		LogUtil.d("retWidth : " + retWidth);
		
		return retWidth;
	}
	
	public static int pxToDIP(Activity activity, int px) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float dpip = metrics.density;
		return (int) (px * dpip);
	}
	
	
	public static int getMaxTextWidth(Activity activity, String[] arrStr, int minWidth) {
		
		int maxWidth = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setTextSizeCustom(24);
			
			int textWidth = textView.getTextWidth();
			
			if (maxWidth == 0) {
				maxWidth = textWidth;
			} else {
				if (maxWidth < textWidth)
					maxWidth = textWidth;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textWidth);
			
			textView.destroyDrawingCache();
			textView = null;
		}

		int width = (int) getWidthUsingRate(activity, minWidth);
		
//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
		
		if (width < maxWidth+getWidthUsingRate(activity, 10))
			return maxWidth + (int)getWidthUsingRate(activity, 50);
		else
			return width;
		
	}
	
	public static int getMaxTextHeight(Activity activity, String[] arrStr, int textWidth, int textSize, int minHeight) {
		
		int maxHeight = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setWidth(textWidth);
//			textView.setMaxWidth(textWidth);
			textView.setSingleLine(false);
			textView.setTextSizeCustom(textSize);
			
			int textHeight = textView.getTextHeight();
			
			if (maxHeight == 0) {
				maxHeight = textHeight;
			} else {
				if (maxHeight < textHeight)
					maxHeight = textHeight;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textHeight + " / " + textView.getTextWidth());
			
			textView.destroyDrawingCache();
			textView = null;
		}

		if (maxHeight > minHeight)
			return maxHeight;
		else
			return minHeight;
//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
	}
	
	public static int getMaxTextHeight(Activity activity, String[] arrStr, int textWidth, int textSize, int minHeight, int[] padding) {
		
		int maxHeight = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setWidth(textWidth);
//			textView.setMaxWidth(textWidth);
			textView.setSingleLine(false);
			textView.setTextSizeCustom(textSize);
			textView.setPadding(padding[0], padding[1], padding[2], padding[3]);
			
			int textHeight = textView.getTextHeight();
			
			if (maxHeight == 0) {
				maxHeight = textHeight;
			} else {
				if (maxHeight < textHeight)
					maxHeight = textHeight;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textHeight + " / " + textView.getTextWidth());
			
			textView.destroyDrawingCache();
			textView = null;
		}

		if (maxHeight > minHeight)
			return maxHeight;
		else
			return minHeight;
//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
	}
	
	public static int getMaxTextHeight(Activity activity, String[] arrStr, int textWidth, int textSize, int[] padding) {
		
		int maxHeight = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setWidth(textWidth);
//			textView.setMaxWidth(textWidth);
			textView.setSingleLine(false);
			textView.setTextSizeCustom(textSize);
			textView.setPadding(padding[0], padding[1], padding[2], padding[3]);
			
			int textHeight = textView.getTextHeight();
			
			if (maxHeight == 0) {
				maxHeight = textHeight;
			} else {
				if (maxHeight < textHeight)
					maxHeight = textHeight;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textHeight + " / " + textView.getTextWidth());
			
			textView.destroyDrawingCache();
			textView = null;
		}

//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
		return maxHeight;
		
	}

	public static int getTextHeight(Activity activity, String str, int textWidth, int textSize, int[] padding) {
		
		CustomTextView textView = new CustomTextView(activity);
		textView.setText(str);
		textView.setWidth(textWidth);
		textView.setSingleLine(false);
		textView.setTextSizeCustom(textSize);
		DisplayUtil.setLayoutPadding(activity, padding[0], padding[1], padding[2], padding[3], textView);
		
		return textView.getTextHeight();
		
	}

	public static int getMaxTextWidth2(Activity activity, String[] arrStr, int defaultMaxWidth, int textSize, int[] padding) {
		
		int maxWidth = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setTextSizeCustom(textSize);
			textView.setPadding(padding[0], padding[1], padding[2], padding[3]);
			
			int textWidth = textView.getTextWidth();
			
			if (textWidth > defaultMaxWidth) {
				return defaultMaxWidth;
			}
			
			if (maxWidth == 0) {
				maxWidth = textWidth;
			} else {
				if (maxWidth < textWidth)
					maxWidth = textWidth;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textWidth);
			
			textView.destroyDrawingCache();
			textView = null;
		}

		return maxWidth;
		
	}

	public static int getMaxTextWidth(Activity activity, String[] arrStr, int minWidth, int textSize) {
		
		int maxWidth = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setTextSizeCustom(textSize);
			
			int textWidth = textView.getTextWidth();
			
			if (maxWidth == 0) {
				maxWidth = textWidth;
			} else {
				if (maxWidth < textWidth)
					maxWidth = textWidth;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textWidth);
			
			textView.destroyDrawingCache();
			textView = null;
		}

//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
		int width = (int) getWidthUsingRate(activity, minWidth);
		
		if (width < maxWidth+getWidthUsingRate(activity, 10))
			return maxWidth + (int)getWidthUsingRate(activity, 50);
		else
			return width;
		
	}

	public static int getMaxTextWidthPlus(Activity activity, String[] arrStr, int minWidth, int textSize, int plusCount) {
		
		int maxWidth = 0;
		
		String strPlus = "";
		for (int idx=0; idx<plusCount; idx++) {
			strPlus += "a";
		}
		
		for (String str : arrStr) {
			str = str + strPlus;
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str);
			textView.setTextSizeCustom(textSize);
			
			int textWidth = textView.getTextWidth();
			
			if (maxWidth == 0) {
				maxWidth = textWidth;
			} else {
				if (maxWidth < textWidth)
					maxWidth = textWidth;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textWidth);
			
			textView.destroyDrawingCache();
			textView = null;
		}

//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
		int width = (int) getWidthUsingRate(activity, minWidth);
		
		if (width < maxWidth+getWidthUsingRate(activity, 10))
			return maxWidth + (int)getWidthUsingRate(activity, 10);
		else
			return width;
		
	}

	public static int getMaxTextWidth2(Activity activity, String[] arrStr, int minWidth, int textSize) {
		
		int maxWidth = 0;
		
		for (String str : arrStr) {
			CustomTextView textView = new CustomTextView(activity);
			textView.setText(str+"  ");
			textView.setTextSizeCustom(textSize);
			
			int textWidth = textView.getTextWidth();
			
			if (maxWidth == 0) {
				maxWidth = textWidth;
			} else {
				if (maxWidth < textWidth)
					maxWidth = textWidth;
			}
			
			LogUtil.d("text : " + textView.getText().toString() + " / size : " + textWidth);
			
			textView.destroyDrawingCache();
			textView = null;
		}

//		LogUtil.d("minWidth : " + width + " / maxWidth : " + maxWidth);
		
		int width = (int) getWidthUsingRate(activity, minWidth);
		
		if (width < maxWidth+getWidthUsingRate(activity, 10))
			return maxWidth + (int)getWidthUsingRate(activity, 10);
		else
			return width;
		
	}

	public static int getMinTextWidth(Activity activity, String str, int minWidth) {
		
		int width = 0;
		
		CustomTextView textView = new CustomTextView(activity);
		textView.setText(str);
			
		width = textView.getTextWidth();
		textView.destroyDrawingCache();
		textView = null;
		
//		LogUtil.d("width : " + width);
		
		return width;
		
	}
	
	public static int getTextWidth(Activity activity, String str, int textSize) {
		
		int width = 0;
		
		CustomTextView textView = new CustomTextView(activity);
		textView.setText(str);
		textView.setTextSizeCustom(textSize);
			
		width = textView.getTextWidth();
		textView.destroyDrawingCache();
		textView = null;
		
		LogUtil.d("str : " + str + " / width : " + width + " / size : " + textSize);
		
		return width;
		
	}
	
	public static int getTextWidth(Activity activity, String str, int textSize, int minWidth) {
		
		int width = 0;
		
		CustomTextView textView = new CustomTextView(activity);
		textView.setText(str);
		textView.setTextSizeCustom(textSize);
			
		width = textView.getTextWidth();
		textView.destroyDrawingCache();
		textView = null;
		
//		LogUtil.d("width : " + width);
		
		if (width < minWidth)
			return minWidth;
		
		return width;
		
	}

	public static float getTextSize(Activity activity, float px) {
		
		if (Preferences.getIsTable(activity))
			return px;
		else
			return px/2;
		
	}
	
	public static boolean isTablet(Activity activity) {
		int portrait_width_pixel = Math.min(activity.getResources().getDisplayMetrics().widthPixels, activity.getResources().getDisplayMetrics().heightPixels);
		int dots_per_virtual_inch = activity.getResources().getDisplayMetrics().densityDpi;
		float virutal_width_inch = portrait_width_pixel / dots_per_virtual_inch;

		return (virutal_width_inch > 2);
	}
	
	public static boolean isThisAppForground(Context context){

		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
		ComponentName topActivity = Info.get(0).topActivity;
		String topactivityname = topActivity.getPackageName();
        return topactivityname.startsWith(context.getPackageName());

    }

	public static String byteCalculation(double size) {
		String retFormat = "0";
		String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };

		if (size != 0) {
			int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
			DecimalFormat df = new DecimalFormat("#,###.##");
			double ret = ((size / Math.pow(1024, Math.floor(idx))));
			retFormat = df.format(ret) + " " + s[idx];
		} else {
			retFormat += " " + s[0];
		}
		return retFormat;
	}
	
	public static void setBrigthnessValue(Activity act, float value) {
		WindowManager.LayoutParams lp = act.getWindow().getAttributes();
		lp.screenBrightness = value;
		act.getWindow().setAttributes(lp);
	}
}
