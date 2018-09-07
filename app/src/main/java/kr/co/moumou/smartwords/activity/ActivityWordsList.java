package kr.co.moumou.smartwords.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.customview.CustomButton;
import kr.co.moumou.smartwords.customview.NoteAdapter;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.dao.WordTestDownloadDao;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoFileWordsDownload;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoNoteData;
import kr.co.moumou.smartwords.vo.VoNoteDetail;
import kr.co.moumou.smartwords.vo.VoWordsTestDownload;
import kr.co.moumou.smartwords.vo.VoWordsTestList;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordQuest;
import kr.co.moumou.smartwords.vo.VoWordsTestList.VoWordsTest;

public class ActivityWordsList extends ActivityBase implements OnClickListener {
	
//	private ViewSlidingMenuMM view_sliding_menu;
	
	NoteAdapter known;
	NoteAdapter unknown;
	
	ArrayList<VoNoteData> all;
	
	@SuppressWarnings("unchecked")
    ArrayList<VoNoteDetail> lv[] = new ArrayList[6];
	
	ArrayList<VoNoteDetail> lv_k = new ArrayList<VoNoteDetail>();
	ArrayList<VoNoteDetail> lv_u = new ArrayList<VoNoteDetail>();
	
	ArrayList<String> array = new ArrayList<String>();
	
	LinearLayout ll_close;
	ImageButton bt_close;
	LinearLayout ll_detail;
	LinearLayout bt_quiz;
	Button bt_lv[] = new CustomButton[6];
	LinearLayout ll_known;
	TextView tv_known;
	ListView lv_known;
	LinearLayout ll_unknown;
	TextView tv_unknown;
	ListView lv_unknown;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordslist);
		
		/*view_sliding_menu = (ViewSlidingMenuMM) findViewById(R.id.view_sliding_menu);
		view_sliding_menu.showErrorReportButton();
		view_sliding_menu.setListener(menuClickListener);*/
		
		ll_close = (LinearLayout) findViewById(R.id.ll_close);
		bt_close = (ImageButton) findViewById(R.id.bt_close);
		ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
		bt_quiz = (LinearLayout) findViewById(R.id.bt_quiz);
		bt_lv[1] = (CustomButton) findViewById(R.id.bt_lv1);
		bt_lv[2] = (CustomButton) findViewById(R.id.bt_lv2);
		bt_lv[3] = (CustomButton) findViewById(R.id.bt_lv3);
		bt_lv[4] = (CustomButton) findViewById(R.id.bt_lv4);
		bt_lv[5] = (CustomButton) findViewById(R.id.bt_lv5);
		bt_lv[0] = (CustomButton) findViewById(R.id.bt_lvall);
		ll_known = (LinearLayout) findViewById(R.id.ll_known);
		tv_known = (TextView) findViewById(R.id.tv_known);
		lv_known = (ListView) findViewById(R.id.lv_known);
		ll_unknown = (LinearLayout) findViewById(R.id.ll_unknown);
		tv_unknown = (TextView) findViewById(R.id.tv_unknown);
		lv_unknown = (ListView) findViewById(R.id.lv_unknown);
		
		DisplayUtil.setLayoutHeight(this, 102, ll_close);
		DisplayUtil.setLayout(this, 52, 52, bt_close);
		DisplayUtil.setLayoutMargin(this, 0, 0, 30, 0, bt_close);
		DisplayUtil.setLayoutMargin(this, 30, 0, 30, 30, ll_detail);
		DisplayUtil.setLayout(this, 138, 52, bt_quiz);
		DisplayUtil.setLayout(this, 52, 52, findViewById(R.id.bt_close));
		DisplayUtil.setLayout(this, 28, 28, findViewById(R.id.iv_icon_pen));
		DisplayUtil.setLayoutMargin(this, 30, 30, 0, 30, bt_quiz);
		DisplayUtil.setLayout(this, 109, 52, bt_lv[1]);
		DisplayUtil.setLayout(this, 109, 52, bt_lv[2]);
		DisplayUtil.setLayout(this, 109, 52, bt_lv[3]);
		DisplayUtil.setLayout(this, 109, 52, bt_lv[4]);
		DisplayUtil.setLayout(this, 109, 52, bt_lv[5]);
		DisplayUtil.setLayout(this, 109, 52, bt_lv[0]);
		DisplayUtil.setLayoutMargin(this, 0, 0, 30, 0, bt_lv[0]);
		DisplayUtil.setLayout(this, 570, 478, ll_known);
		DisplayUtil.setLayoutMargin(this, 30, 0, 10, 0, ll_known);
		DisplayUtil.setLayoutHeight(this, 70, tv_known);
		DisplayUtil.setLayoutMargin(this, 0, 0, 0, 30, lv_known);
		DisplayUtil.setLayout(this, 570, 478, ll_unknown);
		DisplayUtil.setLayoutMargin(this, 10, 0, 30, 0, ll_unknown);
		DisplayUtil.setLayoutHeight(this, 70, tv_unknown);		
		DisplayUtil.setLayoutMargin(this, 0, 0, 0, 30, lv_unknown);
		
		playBtnQuizAnim();
		
		bt_quiz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reqQuizInfo();
			}
		});
		
		for(int i = 0 ; i < 6 ; i ++) {
			bt_lv[i].setOnClickListener(this);
		}
		
		Intent intent = getIntent();
		all = (ArrayList<VoNoteData>) intent.getSerializableExtra("array");
		
		group();
		
		setSelect(0);
		
	}
	
	protected void onStop() {
		super.onStop();
		handlerAnim.removeMessages(0);
	}

    protected void onStart() {
		super.onStart();
		if(null != handlerAnim) {
			handlerAnim.sendEmptyMessageDelayed(0, 0);
		}
	}

    protected void onDestroy() {
		if(null != handlerAnim) handlerAnim = null;
		super.onDestroy();
	}

    AnimationSet animation = null;
	
	private void playBtnQuizAnim() {
		
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setDuration(1000);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setStartOffset(1000);
		fadeOut.setDuration(1000);
		
		animation = new AnimationSet(false);
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);
		bt_quiz.setAnimation(animation);
	}
	
	Handler handlerAnim = new Handler() {
		public void handleMessage(android.os.Message msg) {
			bt_quiz.startAnimation(animation);
			handlerAnim.sendEmptyMessageDelayed(0, 2000);
		}
    };
	
	private void reqQuizInfo() {
		
		showLoadingProgress(getResources().getString(R.string.wait_for_data));

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_QUIZINFO);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoMyInfo.getInstance().getSESSIONID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, VoMyInfo.getInstance().getUSERID());
		builder.appendQueryParameter("COMMAND", ConstantsCommCommand.COMMAND_1112_SMARTWORDS_QUIZ);

		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_QUIZINFO, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				hideProgress();
				ApplicationPool.getGson().fromJson(response, VoWordsTestList.class);

				createPractice();   //퀴즈로 Practice만들기

				ArrayList<VoFileWordsDownload> downLoadArray = checkDownload();

				if(null != downLoadArray && downLoadArray.size() > 0){
					Intent intent = new Intent(ActivityWordsList.this, ActivityWordsDownload.class);
					intent.putExtra(ActivityWordsDownload.C_PARAM_DOWNLOAD_URL, VoWordsTestList.getInstance().getPATH());
					intent.putExtra(ActivityWordsDownload.C_PARAM_DOWNLOAD_DATA, downLoadArray);
					startActivityForResult(intent, ActivityWordTestDaySelect.CHANT_FILE_DOWNLOAD_RESULT);
				}else
					gotoQuiz();

			}

			@Override
			public void systemcheck(String response) {

			}

			@Override
			public void fail(VoBase base) {

			}

			@Override
			public void exception(ANError error) {

			}

			@Override
			public void dismissDialog() {

			}
		});
	}
	
	/**
	 * 퀴즈로 Practice 만들기
	 */
	private void createPractice() {

		VoWordsTest VoWordsQuiz = VoWordsTestList.getInstance().getQUIZ();
		VoWordsTest VoWordsPractice = VoWordsTestList.getInstance().new VoWordsTest();
		
		VoWordsPractice.setSTD_STEP(VoWordsTest.CODE_PRACTICE);
		VoWordsPractice.setSTD_TYPE(VoWordQuest.QUIZ_CODE_PRACTICE);
		VoWordsPractice.setSTD_NAME("Practice");
		
		ArrayList<VoWordQuest> STD_LIST = new ArrayList<>();
		
		for(VoWordQuest wordQuest : VoWordsQuiz.getSTD_LIST()) {
			
			VoWordQuest wordPractice = VoWordsTestList.getInstance().new VoWordQuest();
			wordPractice.setSTD_GB(wordQuest.getSTD_GB());
			wordPractice.setSTD_LEVEL(wordQuest.getSTD_LEVEL());
			wordPractice.setSTD_DAY(wordQuest.getSTD_DAY());
			wordPractice.setWORD_SEQ(wordQuest.getWORD_SEQ());
			wordPractice.setWORD(wordQuest.getWORD());
			wordPractice.setMEAN(wordQuest.getMEAN());
			wordPractice.setSTD_STEP(wordQuest.getSTD_STEP());
			wordPractice.setSTD_TYPE(wordQuest.getSTD_TYPE());
			wordPractice.setSTD_NUM(wordQuest.getSTD_NUM());
			wordPractice.setUSER_ANS(wordQuest.getUSER_ANS());
			wordPractice.setANSWER(wordQuest.getANSWER());
			wordPractice.setRESULT_YN(wordQuest.getRESULT_YN());
			wordPractice.setSTD_MP3(wordQuest.getSTD_MP3());
			wordPractice.setSENTENCE(wordQuest.getSENTENCE());
			wordPractice.setSEN_MEAN(wordQuest.getSEN_MEAN());
			wordPractice.setBOGI_LIST(wordQuest.getBOGI_LIST());
			
			STD_LIST.add(wordPractice);
		}
		
		VoWordsPractice.setSTD_LIST(STD_LIST);
		VoWordsTestList.getInstance().setPRACTICE(VoWordsPractice);
	}
	
	/**
	 * 퀴즈 시작
	 */
	private void gotoQuiz() {
		Intent intent = new Intent(this, ActivityWordTestQuizSelect.class);
		startActivityForResult(intent, ActivityWordTestQuizSelect.WORDTEST_QUIZ_TYPE);
	}
	
	/**
	 * 음성 다운로드 체크
	 * @return
	 */
	private ArrayList<VoFileWordsDownload> checkDownload() {
		
		ArrayList<VoFileWordsDownload> downloadArray = new ArrayList<>();
		
		for(VoWordsTestDownload voDownload : VoWordsTestList.getInstance().getDOWNLOAD()) {
			
			if(!WordTestDownloadDao.getInstance(this).isExistWords(voDownload.getFILE_NAME(), voDownload.getFILE_DATE())){
				LogTraceMin.I("DB없음 URL" + voDownload.getFILE_NAME());
				VoFileWordsDownload voFileWords = new VoFileWordsDownload();
				voFileWords.setFilename(voDownload.getFILE_NAME());
				voFileWords.setDate(voDownload.getFILE_DATE());
				downloadArray.add(voFileWords);
			}
		}
		if(downloadArray.size() > 0) return downloadArray;
		else return null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == ActivityWordTestDaySelect.CHANT_FILE_DOWNLOAD_RESULT) {
			LogTraceMin.I("다운로드 끝 퀴즈 시작");
			gotoQuiz();
		}else if(requestCode == ActivityWordTestQuizSelect.WORDTEST_QUIZ_TYPE) {
			if(resultCode == ActivityWordTestQuizSelect.WORDTEST_QUIZ_COMPLETE) {
				setResult(ActivityWordTestQuizSelect.WORDTEST_QUIZ_COMPLETE);
				finish();
			}
		}
	}

	private void group() {
		for(int i = 0 ; i < all.size() ; i ++) {
			if(all.get(i).getUSER_NOTE().size() != 0) {
				lv[i] = all.get(i).getUSER_NOTE();
				bt_lv[i].setEnabled(true);
			} else {
				bt_lv[i].setEnabled(false);
			}
		}
	}
	
	public void setSelect(int position) {
		for(int i = 0 ; i < 6 ; i ++) {
			if(i == position) {
				bt_lv[i].setSelected(true);
			} else {
				bt_lv[i].setSelected(false);
			}
		}
		
		setYN(position);		
	}

	private void setYN(int position) {
		lv_k.clear();
		lv_u.clear();
		
		for(int i = 0 ; i < lv[position].size() ; i ++) {
			if("Y".equals(lv[position].get(i).getRESULT_YN())) {
				lv_k.add(lv[position].get(i));
			} else {
				lv_u.add(lv[position].get(i));
			}
		}
		
		setListView();
	}
	
	private void setListView() {
		known = new NoteAdapter(this);
		unknown = new NoteAdapter(this);
		
		known.setList(lv_k);
		unknown.setList(lv_u);
		
		bt_close.setOnClickListener(this);
		
		lv_known.setDividerHeight((int) DisplayUtil.getHeightUsingRate(this, 2));
		lv_known.setAdapter(known);
		
		lv_unknown.setDividerHeight((int) DisplayUtil.getHeightUsingRate(this, 2));
		lv_unknown.setAdapter(unknown);		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_close :
				finish();
				break;
				
			case R.id.bt_lv1 :
				setSelect(1);				
				break;
				
			case R.id.bt_lv2 :
				setSelect(2);				
				break;
				
			case R.id.bt_lv3 :
				setSelect(3);				
				break;
				
			case R.id.bt_lv4 :
				setSelect(4);				
				break;
				
			case R.id.bt_lv5 :
				setSelect(5);				
				break;
				
			case R.id.bt_lvall :
				setSelect(0);				
				break;

			default:
				break;
		}
		
	}
	
	/*private OnMenuItemClickListener menuClickListener = new OnMenuItemClickListener() {

        @Override
        public void onClick(int type, int value) {
            if(type == OnMenuItemClickListener.TYPE_ERROR_REPORT){
                view_sliding_menu.showErrorReportDialog(ActivityWordsList.this, getErrorReportInfo());
            }
        }

        @Override
        public void exception(String msg) {

        }
    };
    
    private HashMap<String, Object> getErrorReportInfo(){
        HashMap<String, Object> bookInfo = new HashMap<String, Object>();
		bookInfo.put(DialogReport.KEY_SYSGB, ConstantsCommParameter.Values.SYSBG_MOUMOU_WORDS);
		bookInfo.put(DialogReport.KEY_USERID, VoMyInfo.getInstance().getUSERID());
		bookInfo.put(DialogReport.KEY_VIEW, findViewById(R.id.lay_base));
		bookInfo.put(DialogReport.KEY_IS_LIVE, true);
		bookInfo.put(DialogReport.KEY_PCODE, "99999999");
		bookInfo.put(DialogReport.KEY_CHACI, "00");
		bookInfo.put(DialogReport.KEY_PSNAME, "기타");
		bookInfo.put(DialogReport.KEY_COMPNAME, VoMyInfo.getInstance().getCOMPNAME());
		bookInfo.put(DialogReport.KEY_STDGUBUN, DialogReport.STDGUBUN.WORDTEST.code());
        bookInfo.put(DialogReport.KEY_IS_WORKBOOK, "N");

        return bookInfo;
    }*/

	@Override
	public ViewTopMenu getTopManu() {
		return null;
	}

	@Override
	public int getTopMenuPos() {
		return 0;
	}	

}