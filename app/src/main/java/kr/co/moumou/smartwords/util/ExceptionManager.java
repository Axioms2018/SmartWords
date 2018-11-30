package kr.co.moumou.smartwords.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.google.gson.Gson;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import kr.co.moumou.smartwords.common.Constant;

import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoQustList.VoQuest;


public class ExceptionManager {

	private static Context ctx;
	private Activity activity;
	private VoQuest templateInfo;
	
	private static ExceptionManager exceptionManager;
	protected OnExceptionListener onExceptionListener;
	
	private Exception mException;
	private String mErrMsg;
	private String mErrGubn;
	private Bitmap screenShot;
	private View mView;
	
	public void setOnExceptionListener(OnExceptionListener onExceptionListener) {
		this.onExceptionListener = onExceptionListener; 
	}
	
	public interface OnExceptionListener {
		void onPrePared();
		void onComplete();
	}
	
	public ExceptionManager() {
	}
	
	public static ExceptionManager getInstance() {
		if (exceptionManager == null)
			exceptionManager = new ExceptionManager();
		return exceptionManager;
	}
	
	public void setActivity(Activity act) {
		this.activity = act;
	}
	public void setContext(Context context) {
		ctx = context;
	}
	
	public void doExceptionWithContext(Context context, Exception e){
		
		if(StringUtil.isNull(VoMyInfo.getInstance().getSYS_ERR_YN()) || "n".equals(VoMyInfo.getInstance().getSYS_ERR_YN().toLowerCase())){
			return;
		}
		
		if(context == null){
			context = ctx;
		}
		
		new ExceptionSender(context).execute(getStackTrace(e).trim());
	}
	
	private String getAPP_VER(Context context){
		String version = null;
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = i.versionName;
		} catch(PackageManager.NameNotFoundException e) {
			ExceptionManager.getInstance().doExceptionWithContext(null, e);
		}

		return version;
	}
	
	class ExceptionSender extends AsyncTask<String, Void, Void> {

		private Context context;
		public ExceptionSender(Context context) {
			this.context = context;
		}	
		
		@Override
		protected Void doInBackground(String... params) {
			Gson gson = new Gson();

			HashMap<String, String> map = new HashMap<String, String>();
			if(context != null){
				map.put(ConstantsCommParameter.Keys.USERID, SharedPrefData.getStringSharedData(context, SharedPrefData.SHARED_USER_ID_S, ""));
				map.put(ConstantsCommParameter.Keys.APPVER, getAPP_VER(context));
			}else{
				map.put(ConstantsCommParameter.Keys.USERID, "context is null");
				map.put(ConstantsCommParameter.Keys.APPVER, "context is null");
			}
			
			map.put(ConstantsCommParameter.Keys.ANDROIDVER, Build.VERSION.RELEASE);
			map.put(ConstantsCommParameter.Keys.CPU, Build.CPU_ABI);
			map.put(ConstantsCommParameter.Keys.MODEL, Build.MODEL);
			map.put(ConstantsCommParameter.Keys.ERRMSG, "Other Exception");
			map.put(ConstantsCommParameter.Keys.ERRLOG, params[0]);
			map.put(ConstantsCommParameter.Keys.SYSGB, "101003");

			String uploadData = gson.toJson(map);

			HttpClient httpclient = new DefaultHttpClient();
			String url = ConstantsCommURL.getUrl(ConstantsCommURL.URL_COMMON, "SysErrLog");
			LogTraceMin.D(" URL : " + url);
			HttpPost httppost = new HttpPost(ConstantsCommURL.getUrl(ConstantsCommURL.URL_COMMON, "SysErrLog"));

			try {
				httppost.setEntity(new ByteArrayEntity(uploadData.getBytes()));
				httppost.addHeader("Content-Type", "application/json");
				httpclient.execute(httppost);

			} catch (ClientProtocolException e) {
				ExceptionManager.getInstance().doExceptionWithContext(null, e);
				LogTraceMin.EW(e);
			} catch (IOException e) {
				ExceptionManager.getInstance().doExceptionWithContext(null, e);
				LogTraceMin.EW(e);
			}
			return null;
		}
	}
	
	public void doException(Activity act, Exception e) {
		this.activity = act;
		this.templateInfo = null;
		onExceptionListener = null;
		e.printStackTrace();
		doException(act, e, e.toString(), null, null);
	}
	
	public void doException(Activity act, Exception e, VoQuest info, OnExceptionListener listener) {
		this.activity = act;
		this.templateInfo = info;
		onExceptionListener = listener;
		e.printStackTrace();
		doException(act, e, e.toString(), info, listener);
	}
	
	public void doException(Activity act, Exception e, String errMsg, VoQuest templateInfo, OnExceptionListener listener) {
		
		if (e != null)
			e.printStackTrace();
		
		this.activity = act;
		this.templateInfo = templateInfo;
		onExceptionListener = listener;
		
		mException = e;
		mErrMsg = errMsg;
		
		if (e instanceof FileNotFoundException)
			errMsg = "파일이 존재하지 않습니다.";
		
		// 앱을 종료하는 경우 토스트 알림 안띄움.
		if (!StringUtil.isNull(errMsg) && !errMsg.contains("finish app")) {
			// 스터디 액티비티만 종료하는 경우 에러메시지 안보여줌.
			if (errMsg.contains("finish study"))
				errMsg = "";
			
			if (templateInfo != null) {
				ToastUtil.show(activity, "문항에 오류가 발생하였습니다..\n"+errMsg);
				
				mErrMsg += "\n";
				mErrMsg += "GUBN : " + templateInfo.getANSW_GUBN() + "/ RSLT : " + templateInfo.getANSW_RSLT() + " / MEM_ANSW : " + templateInfo.getMEM_ANSW(); 
			} else
				ToastUtil.show(activity, "시스템에 오류가 발생하였습니다..\n"+errMsg);
		}
		
		VoMyInfo myInfo = VoMyInfo.getInstance();
		
		if ("N".equals(myInfo.getCONT_ERR_YN())) {
			if (onExceptionListener != null)
				onExceptionListener.onComplete();
		} else {
			if (onExceptionListener != null)
				onExceptionListener.onPrePared();
			
			sendErrHandler.sendEmptyMessageDelayed(0, 100);
		}
		
        LogUtil.d("message : " + errMsg);
	}
	
	public void doExceptionSendOnly(Activity act, Exception e, String errMsg, VoQuest templateInfo) {
		
		if (e != null)
			e.printStackTrace();
		
		this.activity = act;
		this.templateInfo = templateInfo;
		
		mException = e;
		mErrMsg = errMsg;
		
		// 앱을 종료하는 경우 토스트 알림 안띄움.
		if (!StringUtil.isNull(errMsg) && !errMsg.contains("finish app")) {
			
			// 스터디 액티비티만 종료하는 경우 에러메시지 안보여줌.
			if (errMsg.contains("finish study"))
				errMsg = "";
			
			if (templateInfo != null) {
				mErrMsg += "\n";
				mErrMsg += "SEQ : " + templateInfo.getQUST_SEQ() + " / GUBN : " + templateInfo.getANSW_GUBN() + " / RSLT : " + templateInfo.getANSW_RSLT() + " / MEM_ANSW : " + templateInfo.getMEM_ANSW() + " / STDY_TYPE : " + templateInfo.getSTDY_TYPE();
				
			}
		}
		
		VoMyInfo myInfo = VoMyInfo.getInstance();
		
		if ("N".equals(myInfo.getCONT_ERR_YN())) {
			if (onExceptionListener != null)
				onExceptionListener.onComplete();
		} else {
			if (onExceptionListener != null)
				onExceptionListener.onPrePared();
			
			sendErrHandler.sendEmptyMessageDelayed(0, 100);
		}
		
		if (!"N".equals(myInfo.getCONT_ERR_YN())) {
			sendErrHandler.sendEmptyMessageDelayed(0, 100);
		}
		
        LogUtil.d("message : " + errMsg);
        
	}

	public void doExceptionSTT(Activity act, String errMsg, String errGubn, VoQuest templateInfo) {
		
		this.activity = act;
		this.templateInfo = templateInfo;
		
		mException = null;
		mErrMsg = errMsg;
		mErrGubn = errGubn;
		
		VoMyInfo myInfo = VoMyInfo.getInstance();
		
		if ("Y".equals(myInfo.getCONT_ERR_YN())) {
			sendErrHandler.sendEmptyMessageDelayed(0, 100);
		}
		
        LogUtil.d("message : " + errMsg);
	}


	
	Handler resultListener = new Handler(Looper.getMainLooper()) {
		
		@Override
		public void handleMessage(Message msg) {
			if (onExceptionListener != null)
				onExceptionListener.onComplete();
		}
	};
	
	private String getTodayError(){
		
		String currentDateandTime = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		currentDateandTime = sdf.format(new Date());
		
		return "error__moumou_" + currentDateandTime;
	}
	
	public static String getStackTrace(final Throwable throwable) {
	     final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
	
	Handler sendErrHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

		}
	};

	public View getmView() {
		return mView;
	}

	public void setmView(View mView) {
		this.mView = mView;
	}
	
	
}
