package kr.co.moumou.smartwords.util;

import android.util.Log;


public class LogTraceMin {
	public static String TAG = "EvilStorm";

	private final static boolean SHOW_LOG = false;
	
//	private final static boolean SHOW_LOG = true;
//	private final static boolean SHOW_LOG = false;
	
	private final static boolean LOG_D = true;
	private final static boolean LOG_I = true;
	private final static boolean LOG_W = true;
	private final static boolean LOG_E = true;
	private final static boolean LINK_SORUCE = false;

	public static void D(String msg){
		if(!LOG_D || !SHOW_LOG) return;
		try {
			StackTraceElement[] trace = new Throwable().getStackTrace();
			if (trace.length >= 1) {
				StackTraceElement elt = trace[1];
				Log.d(TAG, msg);
				if(LINK_SORUCE){
					Log.d(TAG, "at " + elt.toString());
				}
			} else {
				Log.d(TAG, msg);
			}
		} catch (NullPointerException e) {
			LogTraceMin.D(" Log Data is null ");
		}

	}

	public static void I(String msg){
		if(!LOG_I || !SHOW_LOG) return;
		try {
			StackTraceElement[] trace = new Throwable().getStackTrace();
			if (trace.length >= 1) {
				StackTraceElement elt = trace[1];
				Log.i(TAG, msg);
				if(LINK_SORUCE){
					Log.i(TAG, "at " + elt.toString());
				}
			} else {
				Log.i(TAG, msg);
			}
		} catch (NullPointerException e) {
			LogTraceMin.I(" Log Data is null ");
		}
	}
	public static void W(String msg){
		if(!LOG_W || !SHOW_LOG) return;
		try {
			StackTraceElement[] trace = new Throwable().getStackTrace();
			if (trace.length >= 1) {
				StackTraceElement elt = trace[1];
				Log.w(TAG, msg);
				if(LINK_SORUCE){
					Log.w(TAG, "at " + elt.toString());
				}
			} else {
				Log.w(TAG, msg);
			}
		} catch (NullPointerException e) {
			LogTraceMin.W(" Log Data is null ");
		}
	}
	public static void E(String msg){
		if(!LOG_E || !SHOW_LOG) return;
		try {
			StackTraceElement[] trace = new Throwable().getStackTrace();
			if (trace.length >= 1) {
				StackTraceElement elt = trace[1];
				Log.e(TAG, msg);
				if(LINK_SORUCE){
					Log.e(TAG, "at " + elt.toString());
				}
			} else {
				Log.e(TAG, msg);
			}
		} catch (NullPointerException e) {
			LogTraceMin.E(" Log Data is null ");
		}
	}

	public static void EW(Exception e){
		Log.w(TAG, e.toString(), e);
	}
}
