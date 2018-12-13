package kr.co.moumou.smartwords.common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.NetworkState;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.util.StringUtil;
//import kr.co.moumou.smartwords.vo.VoReplaceWord;

public class ApplicationPool extends Application {

	public static String SOCKET_IP = "";  //연결될 소켓 IP
	public static int SOCKET_PORT = 5001;
	public static int SOCKET_PORT_UPLOAD = 5002;
	public static int SOCKET_PORT_STRAMING = 5003;
	public static int SOCKET_PORT_STRAMING2 = 5004;
	public static final int SOCKET_TIME_OUT = (1000 * 2);
	public static boolean IS_STUDY_ROOM_MODE = false;
//	public static boolean IS_PAYED_USER = true;

	public static boolean IS_SOCKET_USE = false;
	public static boolean CONTROLLER_LOCKED = true;

	public static String PATH_DOWNLOAD_BOOK_RESOURCE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "files/";
	public static String PATH_DOWNLOAD_CHANT_RESOURCE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "files/chant/";
	public static String PATH_DOWNLOAD_WORDS_RESOURCE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "files/smartwords/";
	
	public static String PATH_BOOK_RESOURCE = "data/data/kr.co.moumou.smartwords/files/";
	public static String PATH_CHANT_RESOURCE = "data/data/kr.co.moumou.smartwords/files/chant/";
	public static String PATH_WORDS_RESOURCE = "data/data/kr.co.moumou.smartwords/files/smartwords/";

	private final static boolean	TRACE				= false;
	private final static String TRACE_TAG			= "ApplicationPool";
	private final static long		INVALID_EXTRA_ID	= -999;
	private final static String KEY_ID_SEPRATOR 	= ";";
	private ArrayList<Pool> mPoolList;	

	private final static int		TYPE_NONE			= -1;
	public final static String TYPE_PUBLIC			= "PUBLIC";

	public static Calendar STUDY_START_TIME = null;
	public static Calendar STUDY_END_TIME = null;
	public static String STUDY_BOOK_TITLE = "";

	private static void setStudyStartTime(){
		STUDY_START_TIME = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		STUDY_END_TIME = null;
	}
	public static void setStudyEndTime(){
		if(STUDY_END_TIME != null || STUDY_START_TIME == null){
			return;
		}
		STUDY_END_TIME = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	}
	public static void resetStudyTime(){
		STUDY_START_TIME = null;
		STUDY_END_TIME = null;
	}
	public static Calendar getStudyStartTime(){
		return STUDY_START_TIME;
	}
	public static Calendar getStudyEndTime(){
		return STUDY_END_TIME;
	}
	public static String getSTUDY_BOOK_TITLE() {
		return STUDY_BOOK_TITLE;
	}
	public static void setSTUDY_BOOK_TITLE(String sTUDY_BOOK_TITLE) {
		if(StringUtil.isNull(sTUDY_BOOK_TITLE)){
			STUDY_BOOK_TITLE ="";
			return;
		}
		STUDY_BOOK_TITLE = sTUDY_BOOK_TITLE;	
		setStudyStartTime();
	}

	private static HashMap<Integer, int[]> wordPageMap = null;
	public static HashMap<Integer, int[]> getWordPage(){
		if(wordPageMap != null && wordPageMap.size() != 0){
			return wordPageMap; 
		}

		//extra
		wordPageMap = new HashMap<Integer, int[]>();
		wordPageMap.put(2, new int[]{2});
		wordPageMap.put(3, new int[]{3});
		wordPageMap.put(4, new int[]{4});
		wordPageMap.put(5, new int[]{5});
		wordPageMap.put(6, new int[]{3,3});
		wordPageMap.put(7, new int[]{4,3});
		wordPageMap.put(8, new int[]{4,4});
		wordPageMap.put(9, new int[]{5,4});
		wordPageMap.put(10, new int[]{5,5});
		wordPageMap.put(11, new int[]{4,4,3});
		wordPageMap.put(12, new int[]{4,4,4});
		wordPageMap.put(13, new int[]{5,5,3});
		wordPageMap.put(14, new int[]{5,5,4});
		wordPageMap.put(15, new int[]{5,5,5});
		wordPageMap.put(16, new int[]{4,4,4,4});
		wordPageMap.put(17, new int[]{5,4,4,4});
		wordPageMap.put(18, new int[]{5,5,5,3});
		wordPageMap.put(19, new int[]{5,5,5,4});
		wordPageMap.put(20, new int[]{5,5,5,5});
		wordPageMap.put(21, new int[]{5,4,4,4,4});
		wordPageMap.put(22, new int[]{4,3,3,3,3,3,3});
		wordPageMap.put(23, new int[]{5,5,5,5,3});
		wordPageMap.put(24, new int[]{5,5,5,5,4});
		wordPageMap.put(25, new int[]{5,5,5,5,5});
		wordPageMap.put(26, new int[]{5,5,4,4,4,4});
		wordPageMap.put(27, new int[]{5,5,5,4,4,4});
		wordPageMap.put(28, new int[]{5,5,5,5,5,3});
		wordPageMap.put(29, new int[]{5,5,5,5,5,4});
		wordPageMap.put(30, new int[]{5,5,5,5,5,5});
		
		return wordPageMap;
	}
	
//	private static ArrayList<VoReplaceWord> changeWordMap = null;
	private static LinkedHashMap<String, String[]> bookStudyMenuMap = null;

//	public static ArrayList<VoReplaceWord> getChangeWords(){
//
//		//How, What << 느낌표로 끝나야 됨.
//		//문장 마지막에, 일 때  . 도 정답.
//		if(changeWordMap != null && changeWordMap.size() != 0){
//			return changeWordMap;
//		}
//
//		//extra
//		changeWordMap = new ArrayList();
////		changeWordMap.add(new VoReplaceWord("\''", "\""));
//		changeWordMap.add(new VoReplaceWord("\"", ""));
//		changeWordMap.add(new VoReplaceWord("(", ""));
//		changeWordMap.add(new VoReplaceWord(")", ""));
////		changeWordMap.add(new VoReplaceWord("!", "."));
//
//		changeWordMap.add(new VoReplaceWord(" ~ ", " "));
//		changeWordMap.add(new VoReplaceWord(" ~", " "));
//		changeWordMap.add(new VoReplaceWord("~ ", " "));
//		changeWordMap.add(new VoReplaceWord("~", " "));
//
//		changeWordMap.add(new VoReplaceWord(" - ", " "));
//		changeWordMap.add(new VoReplaceWord(" -", " "));
//		changeWordMap.add(new VoReplaceWord("- ", " "));
//		changeWordMap.add(new VoReplaceWord("-", " "));
//
//		changeWordMap.add(new VoReplaceWord(" ... ", ""));
//		changeWordMap.add(new VoReplaceWord(" ...", ""));
//		changeWordMap.add(new VoReplaceWord("... ", ""));
//		changeWordMap.add(new VoReplaceWord("...", ""));
//
//		changeWordMap.add(new VoReplaceWord(" … ", ""));
//		changeWordMap.add(new VoReplaceWord(" …", ""));
//		changeWordMap.add(new VoReplaceWord("… ", ""));
//		changeWordMap.add(new VoReplaceWord("…", ""));
//
//
//
//		//key : 변경할 값  , value : 변경될 값     = key 값을 value 값으로 변경한다.
//		changeWordMap.add(new VoReplaceWord("are not", "aren't"));
//		changeWordMap.add(new VoReplaceWord("Are not", "Aren't"));
//		changeWordMap.add(new VoReplaceWord("was not", "wasn't"));
//		changeWordMap.add(new VoReplaceWord("Was not", "Wasn't"));
//		changeWordMap.add(new VoReplaceWord("cannot", "can't"));
//		changeWordMap.add(new VoReplaceWord("could not", "couldn't"));
//		changeWordMap.add(new VoReplaceWord("did not", "didn't"));
//		changeWordMap.add(new VoReplaceWord("Did not", "Didn't"));
//		changeWordMap.add(new VoReplaceWord("does not", "doesn't"));
//		changeWordMap.add(new VoReplaceWord("Does not", "Doesn't"));
//
//		changeWordMap.add(new VoReplaceWord("do not", "don't"));
//		changeWordMap.add(new VoReplaceWord("Do not", "Don't"));
//		changeWordMap.add(new VoReplaceWord("had not", "hadn't"));
//		changeWordMap.add(new VoReplaceWord("has not", "hasn't"));
//		changeWordMap.add(new VoReplaceWord("have not", "haven't"));
//		changeWordMap.add(new VoReplaceWord("he would", "he'd"));
//		changeWordMap.add(new VoReplaceWord("He would", "He'd"));
//		changeWordMap.add(new VoReplaceWord("he will", "he'll"));
//		changeWordMap.add(new VoReplaceWord("He will", "He'll"));
//		changeWordMap.add(new VoReplaceWord("he is", "he's"));
//
//		changeWordMap.add(new VoReplaceWord("He is", "He's"));
//		changeWordMap.add(new VoReplaceWord("I would", "I'd"));
//		changeWordMap.add(new VoReplaceWord("I had", "I'd"));
//		changeWordMap.add(new VoReplaceWord("I will", "I'll"));
//		changeWordMap.add(new VoReplaceWord("I am", "I'm"));
//		changeWordMap.add(new VoReplaceWord("I have", "I've"));
//		changeWordMap.add(new VoReplaceWord("is not", "isn't"));
//		changeWordMap.add(new VoReplaceWord("Is not", "Isn't"));
//		changeWordMap.add(new VoReplaceWord("it is", "it's"));
//		changeWordMap.add(new VoReplaceWord("It is", "It's"));
//
//		changeWordMap.add(new VoReplaceWord("let us", "let's"));
//		changeWordMap.add(new VoReplaceWord("Let us", "Let's"));
//		changeWordMap.add(new VoReplaceWord("might not", "mightn't"));
//		changeWordMap.add(new VoReplaceWord("must not", "mustn't"));
//		changeWordMap.add(new VoReplaceWord("she would", "she'd"));
//		changeWordMap.add(new VoReplaceWord("She would", "She'd"));
//		changeWordMap.add(new VoReplaceWord("she will", "she'll"));
//		changeWordMap.add(new VoReplaceWord("She will", "She'll"));
//		changeWordMap.add(new VoReplaceWord("she is", "she's"));
//		changeWordMap.add(new VoReplaceWord("She is", "She's"));
//
//		changeWordMap.add(new VoReplaceWord("there is", "there's"));
//		changeWordMap.add(new VoReplaceWord("There is", "There's"));
//		changeWordMap.add(new VoReplaceWord("there are", "there're"));
//		changeWordMap.add(new VoReplaceWord("There are", "There're"));
//		changeWordMap.add(new VoReplaceWord("here is", "here's"));
//		changeWordMap.add(new VoReplaceWord("Here is", "Here's"));
//		changeWordMap.add(new VoReplaceWord("here are", "here're"));
//		changeWordMap.add(new VoReplaceWord("they would", "they'd"));
//		changeWordMap.add(new VoReplaceWord("They would", "They'd"));
//
//		changeWordMap.add(new VoReplaceWord("they will", "they'll"));
//		changeWordMap.add(new VoReplaceWord("They will", "They'll"));
//		changeWordMap.add(new VoReplaceWord("they are", "they're"));
//		changeWordMap.add(new VoReplaceWord("They are", "They're"));
//		changeWordMap.add(new VoReplaceWord("they have", "they've"));
//		changeWordMap.add(new VoReplaceWord("They have", "They've"));
//		changeWordMap.add(new VoReplaceWord("we would", "we'd"));
//		changeWordMap.add(new VoReplaceWord("We would", "We'd"));
//		changeWordMap.add(new VoReplaceWord("we are", "we're"));
//		changeWordMap.add(new VoReplaceWord("We are", "We're"));
//		changeWordMap.add(new VoReplaceWord("we have", "we've"));
//
//		changeWordMap.add(new VoReplaceWord("We have", "We've"));
//		changeWordMap.add(new VoReplaceWord("were not", "weren't"));
//		changeWordMap.add(new VoReplaceWord("What will", "What'll"));
//		changeWordMap.add(new VoReplaceWord("What are", "What're"));
//		changeWordMap.add(new VoReplaceWord("What is", "What's"));
//		changeWordMap.add(new VoReplaceWord("What have", "What've"));
//		changeWordMap.add(new VoReplaceWord("Where is", "Where's"));
//		changeWordMap.add(new VoReplaceWord("Who would", "Who'd"));
//		changeWordMap.add(new VoReplaceWord("Who will", "Who'll"));
//		changeWordMap.add(new VoReplaceWord("Who are", "Who're"));
//
//		changeWordMap.add(new VoReplaceWord("Who is", "Who's"));
//		changeWordMap.add(new VoReplaceWord("Who have", "Who've"));
//		changeWordMap.add(new VoReplaceWord("will not", "won't"));
//		changeWordMap.add(new VoReplaceWord("would not", "wouldn't"));
//		changeWordMap.add(new VoReplaceWord("you would", "you'd"));
//		changeWordMap.add(new VoReplaceWord("You would", "You'd"));
//		changeWordMap.add(new VoReplaceWord("you will", "you'll"));
//		changeWordMap.add(new VoReplaceWord("You will", "You'll"));
//		changeWordMap.add(new VoReplaceWord("you are", "you're"));
//		changeWordMap.add(new VoReplaceWord("You are", "You're"));
//
//		changeWordMap.add(new VoReplaceWord("you have", "you've"));
//		changeWordMap.add(new VoReplaceWord("You have", "You've"));
//		changeWordMap.add(new VoReplaceWord("should not", "shouldn't"));
//		changeWordMap.add(new VoReplaceWord("he had", "he'd"));
//		changeWordMap.add(new VoReplaceWord("He had", "He'd"));
//		changeWordMap.add(new VoReplaceWord("I had", "I'd"));
//		changeWordMap.add(new VoReplaceWord("she had", "she'd"));
//		changeWordMap.add(new VoReplaceWord("She had", "She'd"));
//		changeWordMap.add(new VoReplaceWord("they had", "they'd"));
//		changeWordMap.add(new VoReplaceWord("They had", "They'd"));
//
//		changeWordMap.add(new VoReplaceWord("we had", "we'd"));
//		changeWordMap.add(new VoReplaceWord("We had", "We'd"));
//		changeWordMap.add(new VoReplaceWord("Who had", "Who'd"));
//		changeWordMap.add(new VoReplaceWord("you had", "you'd"));
//		changeWordMap.add(new VoReplaceWord("You had", "You'd"));
//		changeWordMap.add(new VoReplaceWord("that is", "that's"));
//		changeWordMap.add(new VoReplaceWord("That is", "That's"));
//		changeWordMap.add(new VoReplaceWord("what is", "what's"));
//
//
//
//		return changeWordMap;
//	}

	public static String[] getBookStudyMenu(Context context, String pCode){

//		String[] case1 = new String[]{"단어","원리","문제풀이","유창성","받아쓰기","워크북"};
//		String[] case2 = new String[]{"단어","Step1","Step2","Step3","Step4","워크북"};
//		String[] case3 = new String[]{"단어","원리","문제","워크북"};
//		String[] case4 = new String[]{"부교재"};
//		String[] case5 = new String[]{"단어","원리","문제","유창성","워크북"};
//		String[] case6 = new String[]{"원리","문제","유창성","받아쓰기","워크북"};
		
		String[] case1 = context.getResources().getStringArray(R.array.history_title_1);
		String[] case2 = context.getResources().getStringArray(R.array.history_title_2);
		String[] case3 = context.getResources().getStringArray(R.array.history_title_3);
		String[] case4 = context.getResources().getStringArray(R.array.history_title_4);
		String[] case5 = context.getResources().getStringArray(R.array.history_title_5);
		String[] case6 = context.getResources().getStringArray(R.array.history_title_6);
		
		// 교재코드 앞자리 4개
		String firstPcode = pCode.substring(0,4);
		
		if(bookStudyMenuMap == null || bookStudyMenuMap.size() == 0) {
			bookStudyMenuMap = new LinkedHashMap<String, String[]>();
			bookStudyMenuMap.put("5090", case2);
			bookStudyMenuMap.put("5091", case2);
			bookStudyMenuMap.put("5010", case3);
			bookStudyMenuMap.put("5011", case5);
			bookStudyMenuMap.put("Z096", case4);
		}
		
		// Wow! School1
		if ("50109000".equals(pCode)) {
			return case5;
		}
		
		// Collocations 4,5,6
		if ("50548000".equals(pCode) || "50546000".equals(pCode) || "50550000".equals(pCode)) {
			return case6;
		}
		
		if (bookStudyMenuMap.get(firstPcode) == null)
			return case1;
		
		return bookStudyMenuMap.get(firstPcode);
	}
	
	private UncaughtExceptionHandler defaultUEH;

	private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			String errorMsg = getStackTrace(ex); 
			LogTraceMin.E(errorMsg);

			SharedPrefData.setStringSharedData(getApplicationContext(), SharedPrefData.PREF_EXCEPTION_SHORT_S, errorMsg.substring(0, errorMsg.indexOf("at")).trim());
			SharedPrefData.setStringSharedData(getApplicationContext(), SharedPrefData.PREF_EXCEPTION_DETAIL_S, errorMsg.trim());

			new ExceptionSender(getApplicationContext()).execute(
					SharedPrefData.getStringSharedData(getApplicationContext(), 
							SharedPrefData.SHARED_USER_ID_S, SharedPrefData.SHARED_DEFAULT_S),
					errorMsg.trim());
			
			
			//펜딩인텐트 준비.
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


			PendingIntent restart = PendingIntent.getActivity(getApplicationContext(), 192837, intent, PendingIntent.FLAG_ONE_SHOT);
			//토스트는 ui쓰레드서 명시적으로 띄워줘야 한다.
			new Thread() {
				@Override
				public void run() {
					// UI쓰레드에서 토스트 뿌림
					Looper.prepare();
					Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.big_error_3), Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
			}.start();

			// 쓰레드 잠깐 쉼
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				LogTraceMin.EW(e);
			}
			// re-throw critical exception further to the os (important)
			//			defaultUEH.uncaughtException(thread, ex);

			//앱 재실행 등록.
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000, restart );

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	};


	private String getAPP_VER(){
		String version = null;
		try {
			PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = i.versionName;
		} catch(PackageManager.NameNotFoundException e) { }

		return version;
	}
	
	class ExceptionSender extends AsyncTask<String, Void, Void> {
		private Context context;
		public ExceptionSender(Context context){
			this.context = context;
		}
		@Override
		protected Void doInBackground(String... params) {
			if(StringUtil.isNull(params[1])){
				return null;
			}

			Gson gson = new Gson();
			
			String subErrMsg = "";
			if(params[1].length() > 200){
				subErrMsg = params[1].substring(0, 200);
			} else {
				subErrMsg = params[1];
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(ConstantsCommParameter.Keys.USERID, params[0]);
			map.put(ConstantsCommParameter.Keys.ANDROIDVER, Build.VERSION.RELEASE);
			map.put(ConstantsCommParameter.Keys.APPVER, getAPP_VER());
			map.put(ConstantsCommParameter.Keys.CPU, Build.CPU_ABI);
			map.put(ConstantsCommParameter.Keys.MODEL, Build.MODEL);
			map.put(ConstantsCommParameter.Keys.ERRMSG, subErrMsg);
			map.put(ConstantsCommParameter.Keys.ERRLOG, params[1]);
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

				if(context != null){
					SharedPrefData.setStringSharedData(context, SharedPrefData.PREF_EXCEPTION_SHORT_S, null);
					SharedPrefData.setStringSharedData(context, SharedPrefData.PREF_EXCEPTION_DETAIL_S, null);					
				}
				
			} catch (ClientProtocolException e) {
				LogTraceMin.EW(e);
			} catch (IOException e) {
				LogTraceMin.EW(e);
			}
			return null;
		}
	}
	private String getStackTrace(Throwable th) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);

		Throwable cause = th;

		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		final String stacktraceAsString = result.toString();
		printWriter.close();
		return stacktraceAsString;

	}


	@Override
	public void onCreate() {
		super.onCreate();
		if(TRACE){
			Log.e(TRACE_TAG, "created");				
		}
		createPoolList();

		Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

//		ExceptionManager.getInstance().setContext(getApplicationContext());

		initImageLoader(this);
		initSingleTons();

		File resourceFolder = new File(PATH_BOOK_RESOURCE);
		if(!resourceFolder.exists()){
			resourceFolder.mkdirs();
		}
		File chantFolder = new File(PATH_CHANT_RESOURCE);
		if(!chantFolder.exists()){
			chantFolder.mkdirs();
		}

		//		// ShareLive Fatal 로그 저장용
		//        UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
		//
		//        if(!(handler instanceof ExUncaughtExceptionHandler)) {
		//            Thread.setDefaultUncaughtExceptionHandler(new ExUncaughtExceptionHandler(this));
		//        } 
	}

	private void initSingleTons(){

		NetworkState.getInstance(ApplicationPool.this).setContext(getApplicationContext());
	}

	private static Gson gson;
	public static Gson getGson(){
		if(gson != null){
			return gson;
		}

		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.serializeNulls()
				.create();
		
		return gson;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//		.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	private static DisplayImageOptions options;
	public static DisplayImageOptions getAUILDisplayOptions(){
		if(options == null){
			options = new DisplayImageOptions.Builder()
					.resetViewBeforeLoading(false)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.ARGB_8888)
					.build();
		}

		return options;
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		if(TRACE){
			Log.e(TRACE_TAG, "terminated");
		}
	}	

	/**
	 * 
	 */
	private void createPoolList(){
		if(null == mPoolList){
			mPoolList = new ArrayList<Pool>();
			if(TRACE){
				Log.e(TRACE_TAG, "pool list created - " + mPoolList);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean isEmpty(){
		return (null == mPoolList || mPoolList.isEmpty());
	}

	public boolean isEmpty(String type) {
		boolean isEmpty = true;

		if(isEmpty()){
			return isEmpty;
		}

		for(Pool p : mPoolList){
			if(null != p && type.equalsIgnoreCase(p.getId())) {
				isEmpty = false;
			}
		}

		return isEmpty;
	}

	/**
	 * 저장된 데이터를 꺼내줍니다.
	 * 한 번 꺼낸 데이터는 삭제됩니다.
	 * @param key
	 * @return
	 */
	synchronized public Object getExtra(final String type, final String key){

		if(isEmpty()){
			return null;
		}
		for(Pool p : mPoolList){
			if(null != p && type.equalsIgnoreCase(p.getId())) {
				return p.getData(key);
			}
		}
		return null;  
	}


	/**
	 * 데이터를 저장합니다.
	 * @param key
	 * @param obj
	 * @return
	 */
	synchronized public void putExtra(final String type, final String key, final Object obj){
		LogUtil.d("key : " + key + " / value : " + obj);

		createPoolList();
		for(Pool p : mPoolList){
			if(null != p && type.equalsIgnoreCase(p.getId())) {
				p.putData2(key, obj);
			}
		}
		Pool p = new Pool(type);
		mPoolList.add(p);
		p.putData2(key, obj); 
	}

	/**
	 * 
	 * @param type
	 */
	synchronized public void removeExtras(final String type){
		for(Pool p : mPoolList){
			if(null != p && type.equalsIgnoreCase(p.getId())) {
				p.destory();
				mPoolList.remove(p);
				return;
			}
		}
	}

	/**
	 * 
	 */
	synchronized public void removePoolList(){
		if(isEmpty()){
			return;
		}
		for(Pool p : mPoolList){
			if(null != p){
				p.destory();
			}
		}
		mPoolList = null;
	}

	/**
	 * 
	 * @author hjlee
	 *
	 */
	private class Pool {
		private Map<String, Object> mMap = null;
		//		private int						mId  = TYPE_NONE;
		private String mId  = null;

		public Pool(String id) {
			mId = id;
			mMap = new HashMap<String, Object>();
		}

		public void destory(){
			if(null != mMap){
				Set<String> keys = mMap.keySet();
				if(null != keys && !keys.isEmpty()){
					for(String key : keys){
						mMap.remove(key);
					}
				}
			}
			mId = null;
		}

		public String getId(){
			return mId;
		}

		/**
		 * 
		 */
		public void printDatas(){
			try{
				Set<String> keys = mMap.keySet();
				if(null == keys|| keys.isEmpty()){
					if(TRACE){
						Log.e(TRACE_TAG, "[printDatas] storage is empty.");
					}
				} else {
					for(String key : keys){
						Class cls = mMap.get(key).getClass();
						if(TRACE){
							Log.e(TRACE_TAG, "[printDatas] " + key + " : " + cls.getName());
						}
					}
				}	
			} catch (Exception e){
				if(TRACE){
					Log.e(TRACE_TAG, "[printDatas] fail. exception occurred - " + e.getMessage());
				}
			} catch (Error e){
				if(TRACE){
					Log.e(TRACE_TAG, "[printDatas] fail. error occurred - " + e.getMessage());
				}
			}
		}


		/**
		 * 저장된 데이터를 꺼내줍니다.
		 * 한 번 꺼낸 데이터는 삭제됩니다.
		 * @param key
		 * @return
		 */
		synchronized public Object getData(final String key){
			Object obj = mMap.get(key);
			if(TRACE){
				Log.e(TRACE_TAG, "[getData] key : " + key);
				if(null == obj){
					Log.e(TRACE_TAG, "[getData] obj : " + null);
				} else {
					Log.e(TRACE_TAG, "[getData] obj : " + obj.getClass().getName());
				}		
				printDatas();
			}

			//			mMap.remove(key);

			LogUtil.d("key : " + key + " / value : " + obj);
			return obj;  
		}

		/*
		 *//**
		 * intent 저장되어 있는 id를 통해 데이터를 꺼내줍니다.
		 * @param key
		 * @param i
		 * @return
		 *//*
		synchronized public Object getData(final String key, final Intent i){
			if(null == i || TextUtils.isEmpty(key)){
				return null;
			}
			long extraId = i.getLongExtra(key, INVALID_EXTRA_ID);
			if(INVALID_EXTRA_ID == extraId){
				return null;
			}
			return getExtra(key, extraId);
		}

		  *//**
		  * 데이터를 저장하고 intent에 데이터의 id를 설정합니다.
		  * @param key
		  * @param i
		  * @param data
		  *//*
		synchronized public void putData(final String key, Intent i, final Object data){
			if(null == i){
				return;
			}
			long extraId = putData(key, data);
			i.putExtra(key, extraId);
		}*/

		/**
		 * 데이터를 저장합니다.
		 * @param key
		 * @param data
		 * @return
		 */
		synchronized public long putData(final String key, final Object data){
			long extraId = System.nanoTime();

			String dataKey = null;
			int increaseForAvoidDuplicate = 0;
			do{
				extraId += increaseForAvoidDuplicate;
				dataKey = makeDataKey(extraId, key);
				increaseForAvoidDuplicate++;
			}while(mMap.containsKey(dataKey));

			mMap.put(dataKey, data);

			if(TRACE){
				Log.e(TRACE_TAG, "[putData] key : " + dataKey);
				if(null == data){
					Log.e(TRACE_TAG, "[putData] obj : " + null);
				} else {
					Log.e(TRACE_TAG, "[putData] obj : " + data.getClass().getName());
				}
				printDatas();
			}
			return extraId;
		}

		synchronized public void putData2(final String key, final Object data){

			mMap.put(key, data);

			if(TRACE){
				if(null == data){
					Log.e(TRACE_TAG, "[putData] obj : " + null);
				} else {
					Log.e(TRACE_TAG, "[putData] obj : " + data.getClass().getName());
				}
				printDatas();
			}
		}

		/**
		 * 
		 * @param extraId
		 * @param key
		 * @return
		 */
		private String makeDataKey(final long extraId, final String key){
			return extraId + KEY_ID_SEPRATOR + key;
		}
	}

	public String getSessionID(){
		String sessionID = SharedPrefData.getStringSharedData(this, SharedPrefData.SHARED_USER_SESSIONID, "null");

		if("null".equals(sessionID)){
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			return "null";
		}

		return sessionID;
	}

	/*
	public void createPool(Context cxt){
		if(null == mExtraList){
			mExtraList = new ArrayList<Map<Context,Object>>();
		}

		for(Map<Context, Object> map : mExtraList){
			if(null != map){
				if(map.containsKey(cxt)){
					// already has pool.
					return;
				}
			}
		}
		Map<Context, Object> pool = new HashMap<Context, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		pool.put(cxt, map);
		mExtraList.add(pool);
	}

	public void destroyPool(Context cxt){

	}*/

	//	public static void initImageLoader(Context context) {
	//		// This configuration tuning is custom. You can tune every option, you may tune some of them,
	//		// or you can create default configuration by
	//		//  ImageLoaderConfiguration.createDefault(this);
	//		// method.
	//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
	//				.threadPriority(Thread.NORM_PRIORITY - 2)
	//				.denyCacheImageMultipleSizesInMemory()
	//				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
	//				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
	//				.tasksProcessingOrder(QueueProcessingType.LIFO)
	//				.writeDebugLogs() // Remove for release app
	//				.build();
	//		// Initialize ImageLoader with configuration.
	//		ImageLoader.getInstance().init(config);
	//	}	


}
