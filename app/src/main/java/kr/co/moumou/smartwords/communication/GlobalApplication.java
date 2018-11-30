package kr.co.moumou.smartwords.communication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import kr.co.moumou.smartwords.BuildConfig;
import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.StringUtil;

/**
 * Created by 김민정 on 2018-03-28.
 */

public class GlobalApplication extends Application {

    private static GlobalApplication singleton;

    public static GlobalApplication getInstance() {
        return singleton;
    }
    private static volatile Activity currentActivity = null;

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultUEH;
    private Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // Custom logic goes here

            LogUtil.d("fabric uncaughtException");

            Date dt = new Date();
            SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
//            Crashlytics.setString("time",full_sdf.format(dt).toString());

//            Crashlytics.logException(ex);

            new Thread() {
                @Override
                public void run() {
                    // UI쓰레드에서 토스트 뿌림
                    Looper.prepare();
                    Toast.makeText(mContext, "알수없는 오류가 발생했습니다. 앱을 재실행 합니다.\n오류 정보는 본사로 전송됩니다.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();

            // 쓰레드 잠깐 쉼
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(mContext, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            System.exit(1);

//            mDefaultUEH.uncaughtException(thread, ex);

//            PendingIntent myActivity = PendingIntent
//                    .getActivity(getApplicationContext(),
//                            192837,
//                            new Intent(getApplicationContext(), IntroActivity.class),
//                            PendingIntent.FLAG_ONE_SHOT);
//
//
//            AlarmManager alarmManager;
//            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 15000, myActivity);
//            System.exit(2);
//
//            mDefaultUEH.uncaughtException(thread, ex);

//            Crashlytics.setInt("test", 123);
//            Crashlytics.setUserEmail(Preferences.PREF_USER_ID);
//            Crashlytics.log(Log.ERROR, "moumouTag", "log message");
//
//
//            //펜딩인텐트 준비.
//            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//            PendingIntent restart = PendingIntent.getActivity(getApplicationContext(), 192837, intent, PendingIntent.FLAG_ONE_SHOT);
//            //토스트는 ui쓰레드서 명시적으로 띄워줘야 한다.
//            new Thread() {
//                @Override
//                public void run() {
//                    // UI쓰레드에서 토스트 뿌림
//                    Looper.prepare();
//                    Toast.makeText(getApplicationContext(), "error!!!!!!!", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
//            }.start();
//
//            // 쓰레드 잠깐 쉼
//            try {
//                Thread.sleep(1000);
//
//            } catch (InterruptedException e) {
//                LogUtil.e(e.getMessage());
//            }
//            // re-throw critical exception further to the os (important)
//            //			defaultUEH.uncaughtException(thread, ex);
//
//            //앱 재실행 등록.
//            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000, restart );
//
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(10);

//            mDefaultUEH.uncaughtException(thread, ex);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
//
        singleton = this;
        mContext = getApplicationContext();
//
//        Fabric.with(this, new Crashlytics());
//
//        Crashlytics crashlyticsKit = new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//                .build();
//
//        Fabric.with(this, crashlyticsKit);
//
        if (!BuildConfig.DEBUG) {
            mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);
        }
    }

    private static Gson gson;

    public static Gson getGson() {
        if (gson != null) return gson;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .serializeNulls()
                .create();
        return gson;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }




    private static Map<String, String> replaceWords = new HashMap<>();

    public static String setReplaceWords(String key) {

        if(replaceWords != null && replaceWords.size() != 0){
            if(StringUtil.isNull(replaceWords.get(key))) return key;
            else return replaceWords.get(key);
        }

        replaceWords.put("are not", "aren't");
        replaceWords.put("Are not", "Aren't");
        replaceWords.put("was not", "wasn't");
        replaceWords.put("Was not", "Wasn't");
        replaceWords.put("cannot", "can't");
        replaceWords.put("could not", "couldn't");
        replaceWords.put("did not", "didn't");
        replaceWords.put("Did not", "Didn't");
        replaceWords.put("does not", "doesn't");
        replaceWords.put("Does not", "Doesn't");

        replaceWords.put("do not", "don't");
        replaceWords.put("Do not", "Don't");
        replaceWords.put("had not", "hadn't");
        replaceWords.put("has not", "hasn't");
        replaceWords.put("have not", "haven't");
        replaceWords.put("he would", "he'd");
        replaceWords.put("He would", "He'd");
        replaceWords.put("he will", "he'll");
        replaceWords.put("He will", "He'll");
        replaceWords.put("he is", "he's");

        replaceWords.put("He is", "He's");
        replaceWords.put("I would", "I'd");
        replaceWords.put("I had", "I'd");
        replaceWords.put("I will", "I'll");
        replaceWords.put("I am", "I'm");
        replaceWords.put("I have", "I've");
        replaceWords.put("is not", "isn't");
        replaceWords.put("Is not", "Isn't");
        replaceWords.put("it is", "it's");
        replaceWords.put("It is", "It's");

        replaceWords.put("let us", "let's");
        replaceWords.put("Let us", "Let's");
        replaceWords.put("might not", "mightn't");
        replaceWords.put("must not", "mustn't");
        replaceWords.put("she would", "she'd");
        replaceWords.put("She would", "She'd");
        replaceWords.put("she will", "she'll");
        replaceWords.put("She will", "She'll");
        replaceWords.put("she is", "she's");
        replaceWords.put("She is", "She's");

        replaceWords.put("there is", "there's");
        replaceWords.put("There is", "There's");
        replaceWords.put("there are", "there're");
        replaceWords.put("There are", "There're");
        replaceWords.put("here is", "here's");
        replaceWords.put("Here is", "Here's");
        replaceWords.put("here are", "here're");
        replaceWords.put("they would", "they'd");
        replaceWords.put("They would", "They'd");

        replaceWords.put("they will", "they'll");
        replaceWords.put("They will", "They'll");
        replaceWords.put("they are", "they're");
        replaceWords.put("They are", "They're");
        replaceWords.put("they have", "they've");
        replaceWords.put("They have", "They've");
        replaceWords.put("we would", "we'd");
        replaceWords.put("We would", "We'd");
        replaceWords.put("we are", "we're");
        replaceWords.put("We are", "We're");
        replaceWords.put("we have", "we've");

        replaceWords.put("We have", "We've");
        replaceWords.put("were not", "weren't");
        replaceWords.put("What will", "What'll");
        replaceWords.put("What are", "What're");
        replaceWords.put("What is", "What's");
        replaceWords.put("What have", "What've");
        replaceWords.put("Where is", "Where's");
        replaceWords.put("Who would", "Who'd");
        replaceWords.put("Who will", "Who'll");
        replaceWords.put("Who are", "Who're");

        replaceWords.put("Who is", "Who's");
        replaceWords.put("Who have", "Who've");
        replaceWords.put("will not", "won't");
        replaceWords.put("would not", "wouldn't");
        replaceWords.put("you would", "you'd");
        replaceWords.put("You would", "You'd");
        replaceWords.put("you will", "you'll");
        replaceWords.put("You will", "You'll");
        replaceWords.put("you are", "you're");
        replaceWords.put("You are", "You're");

        replaceWords.put("you have", "you've");
        replaceWords.put("You have", "You've");
        replaceWords.put("should not", "shouldn't");
        replaceWords.put("he had", "he'd");
        replaceWords.put("He had", "He'd");
        replaceWords.put("I had", "I'd");
        replaceWords.put("she had", "she'd");
        replaceWords.put("She had", "She'd");
        replaceWords.put("they had", "they'd");
        replaceWords.put("They had", "They'd");

        replaceWords.put("we had", "we'd");
        replaceWords.put("We had", "We'd");
        replaceWords.put("Who had", "Who'd");
        replaceWords.put("you had", "you'd");
        replaceWords.put("You had", "You'd");
        replaceWords.put("that is", "that's");
        replaceWords.put("That is", "That's");
        replaceWords.put("what is", "what's");

        if(StringUtil.isNull(replaceWords.get(key))) return key;
        else return replaceWords.get(key);
    }

    public static String setReplaceUnDoText(String replaceWords) {

        replaceWords = replaceWords.replaceAll("isn't ", "is not ");
        replaceWords = replaceWords.replaceAll("Isn't ", "Is not ");
        replaceWords = replaceWords.replaceAll("aren't", "are not");
        replaceWords = replaceWords.replaceAll("Aren't", "Are not");
        replaceWords = replaceWords.replaceAll("wasn't", "was not");
        replaceWords = replaceWords.replaceAll("Wasn't", "Was not");
        replaceWords = replaceWords.replaceAll("weren't", "were not");
        replaceWords = replaceWords.replaceAll("Weren't", "Were not");
        replaceWords = replaceWords.replaceAll("haven't", "have not");
        replaceWords = replaceWords.replaceAll("Haven't", "Have not");
        replaceWords = replaceWords.replaceAll("hadn't", "had not");
        replaceWords = replaceWords.replaceAll("Hadn't", "Had not");

        return replaceWords;
    }

    public static String setReplaceText(String replaceWords) {


        replaceWords = replaceWords.replaceAll("cannot", "can't");
        replaceWords = replaceWords.replaceAll("could not", "couldn't");
        replaceWords = replaceWords.replaceAll("did not", "didn't");
        replaceWords = replaceWords.replaceAll("Did not", "Didn't");
        replaceWords = replaceWords.replaceAll("does not", "doesn't");
        replaceWords = replaceWords.replaceAll("Does not", "Doesn't");

        replaceWords = replaceWords.replaceAll("do not", "don't");
        replaceWords = replaceWords.replaceAll("Do not", "Don't");

        replaceWords = replaceWords.replaceAll("he would ", "he'd ");
        replaceWords = replaceWords.replaceAll("He would ", "He'd ");
        replaceWords = replaceWords.replaceAll("he will", "he'll");
        replaceWords = replaceWords.replaceAll("He will", "He'll");
        replaceWords = replaceWords.replaceAll("he is ", "he's ");
        replaceWords = replaceWords.replaceAll("He is ", "He's ");

        replaceWords = replaceWords.replaceAll("I would ", "I'd ");
        replaceWords = replaceWords.replaceAll("I had ", "I'd ");
        replaceWords = replaceWords.replaceAll("I will ", "I'll ");
        replaceWords = replaceWords.replaceAll("I am ", "I'm ");
        replaceWords = replaceWords.replaceAll("I have ", "I've ");
        replaceWords = replaceWords.replaceAll("it is ", "it's ");
        replaceWords = replaceWords.replaceAll("It is ", "It's ");

        replaceWords = replaceWords.replaceAll("let us", "let's");
        replaceWords = replaceWords.replaceAll("Let us", "Let's");
        replaceWords = replaceWords.replaceAll("might not", "mightn't");
        replaceWords = replaceWords.replaceAll("must not", "mustn't");
        replaceWords = replaceWords.replaceAll("she would ", "she'd ");
        replaceWords = replaceWords.replaceAll("She would ", "She'd ");
        replaceWords = replaceWords.replaceAll("she will", "she'll");
        replaceWords = replaceWords.replaceAll("She will", "She'll");
        replaceWords = replaceWords.replaceAll("she is ", "she's ");
        replaceWords = replaceWords.replaceAll("She is ", "She's ");

        replaceWords = replaceWords.replaceAll("there is ", "there's ");
        replaceWords = replaceWords.replaceAll("There is ", "There's ");
        replaceWords = replaceWords.replaceAll("there are ", "there're ");
        replaceWords = replaceWords.replaceAll("There are ", "There're ");
        replaceWords = replaceWords.replaceAll("here is ", "here's ");
        replaceWords = replaceWords.replaceAll("Here is ", "Here's ");
        replaceWords = replaceWords.replaceAll("here are ", "here're ");
        replaceWords = replaceWords.replaceAll("they would ", "they'd ");
        replaceWords = replaceWords.replaceAll("They would ", "They'd ");

        replaceWords = replaceWords.replaceAll("they will ", "they'll ");
        replaceWords = replaceWords.replaceAll("They will ", "They'll ");
        replaceWords = replaceWords.replaceAll("they are ", "they're ");
        replaceWords = replaceWords.replaceAll("They are ", "They're ");
        replaceWords = replaceWords.replaceAll("they have ", "they've ");
        replaceWords = replaceWords.replaceAll("They have ", "They've ");
        replaceWords = replaceWords.replaceAll("we would ", "we'd ");
        replaceWords = replaceWords.replaceAll("We would ", "We'd ");
        replaceWords = replaceWords.replaceAll("we are ", "we're ");
        replaceWords = replaceWords.replaceAll("We are ", "We're ");
        replaceWords = replaceWords.replaceAll("we have ", "we've ");

        replaceWords = replaceWords.replaceAll("We have ", "We've ");

        replaceWords = replaceWords.replaceAll("What will", "What'll");
        replaceWords = replaceWords.replaceAll("What is", "What's");
        replaceWords = replaceWords.replaceAll("What have ", "What've ");
        replaceWords = replaceWords.replaceAll("Where is ", "Where's ");
        replaceWords = replaceWords.replaceAll("Who would ", "Who'd ");
        replaceWords = replaceWords.replaceAll("Who will", "Who'll");
        replaceWords = replaceWords.replaceAll("Who are ", "Who're ");

        replaceWords = replaceWords.replaceAll("Who is ", "Who's ");
        replaceWords = replaceWords.replaceAll("Who have ", "Who've ");

        replaceWords = replaceWords.replaceAll("you would ", "you'd ");
        replaceWords = replaceWords.replaceAll("You would ", "You'd ");
        replaceWords = replaceWords.replaceAll("you will ", "you'll ");
        replaceWords = replaceWords.replaceAll("You will ", "You'll ");
        replaceWords = replaceWords.replaceAll("you are ", "you're ");
        replaceWords = replaceWords.replaceAll("You are ", "You're ");

        replaceWords = replaceWords.replaceAll("you have ", "you've ");
        replaceWords = replaceWords.replaceAll("You have ", "You've ");
        replaceWords = replaceWords.replaceAll("he had ", "he'd ");
        replaceWords = replaceWords.replaceAll("He had ", "He'd ");
        replaceWords = replaceWords.replaceAll("I had ", "I'd ");
        replaceWords = replaceWords.replaceAll("she had ", "she'd ");
        replaceWords = replaceWords.replaceAll("She had ", "She'd ");
        replaceWords = replaceWords.replaceAll("they had ", "they'd ");
        replaceWords = replaceWords.replaceAll("They had ", "They'd ");

        replaceWords = replaceWords.replaceAll("we had ", "we'd ");
        replaceWords = replaceWords.replaceAll("We had ", "We'd ");
        replaceWords = replaceWords.replaceAll("Who had ", "Who'd ");
        replaceWords = replaceWords.replaceAll("you had ", "you'd ");
        replaceWords = replaceWords.replaceAll("You had ", "You'd ");
        replaceWords = replaceWords.replaceAll("that is ", "that's ");
        replaceWords = replaceWords.replaceAll("That is ", "That's ");
        replaceWords = replaceWords.replaceAll("what is ", "what's ");


        replaceWords = replaceWords.replaceAll("is not ", "isn't ");
        replaceWords = replaceWords.replaceAll("Is not ", "Isn't ");
        replaceWords = replaceWords.replaceAll("are not", "aren't");
        replaceWords = replaceWords.replaceAll("Are not", "Aren't");
        replaceWords = replaceWords.replaceAll("was not", "wasn't");
        replaceWords = replaceWords.replaceAll("Was not", "Wasn't");
        replaceWords = replaceWords.replaceAll("were not ", "weren't ");
        replaceWords = replaceWords.replaceAll("Were not ", "Weren't ");
        replaceWords = replaceWords.replaceAll("had not", "hadn't");
        replaceWords = replaceWords.replaceAll("Had not", "Hadn't");
        replaceWords = replaceWords.replaceAll("has not", "hasn't");
        replaceWords = replaceWords.replaceAll("Has not", "Hasn't");
        replaceWords = replaceWords.replaceAll("have not", "haven't");
        replaceWords = replaceWords.replaceAll("Have not", "Haven't");
        replaceWords = replaceWords.replaceAll("will not", "won't");
        replaceWords = replaceWords.replaceAll("Will not", "Won't");
        replaceWords = replaceWords.replaceAll("would not", "wouldn't");
        replaceWords = replaceWords.replaceAll("Would not", "Wouldn't");
        replaceWords = replaceWords.replaceAll("should not", "shouldn't");
        replaceWords = replaceWords.replaceAll("Should not", "Shouldn't");

        return  replaceWords;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


}
