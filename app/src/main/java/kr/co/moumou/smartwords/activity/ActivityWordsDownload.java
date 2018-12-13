package kr.co.moumou.smartwords.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.customview.ViewIndicator;
import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.StringUtil;
import kr.co.moumou.smartwords.vo.VoBanner;
import kr.co.moumou.smartwords.vo.VoBanner.BannerInfo;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoFileWordsDownload;
import kr.co.moumou.smartwords.vo.VoWordsTestDownload;

public class ActivityWordsDownload extends ActivityBase {

	private ArrayList<VoWordsTestDownload> downloadArry;

	private final int ADVERTISE_AUTO_MOVE_TIME = 3000;
	private final int WHAT_ADVERTISE_AUTO_MOVE_TIME = 1;
	protected final static String C_PARAM_DOWNLOAD_URL = "wordtest_download_url";
	protected final static String C_PARAM_DOWNLOAD_DATA = "wordtest_download_data";

	private String download_path;
	private ArrayList<VoFileWordsDownload> wordsDownloadArry;
	private int totalFileCnt;
	private int currentFileCnt;

	private TextView tv_state;
	private Button btn_retry,btn_close;
	private ImageView img_icon;

	@Override
	public ViewTopMenu getTopManu() {
		return null;
	}

	@Override
	public int getTopMenuPos() {
		return -1;
	}

	@Override
	protected void onConnectedNetwork(boolean retry) {
		if(retry) downloadStart();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_words_download);

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = (int) (display.getWidth() * 0.40); // Display 사이즈의 70%
		int height = (int) (display.getHeight() * 0.38); // Display 사이즈의 90%
		getWindow().getAttributes().width = width;
		getWindow().getAttributes().height = height;

		Intent intent = getIntent();
		download_path = intent.getStringExtra(C_PARAM_DOWNLOAD_URL);
		wordsDownloadArry = (ArrayList<VoFileWordsDownload>) intent.getSerializableExtra(C_PARAM_DOWNLOAD_DATA);
		totalFileCnt = wordsDownloadArry.size();

		tv_state = (TextView) findViewById(R.id.tv_state);
		btn_close = (Button) findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDownload();
			}
		});
		btn_retry = (Button) findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadRetry();
			}
		});


		DisplayUtil.setLayout(this,72,72,btn_close);


		downloadStart();

	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.IntentAction.ACTION_DOWNLOAD_CANCEL);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onBackPressed() {
		cancelDownload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (Constant.IntentAction.ACTION_DOWNLOAD_CANCEL.equals(action)) {
				boolean isDownloadCancel = intent.getBooleanExtra(Constant.IntentKeys.INTENT_DOWNLOAD_CANCEL, false);
				if (isDownloadCancel) {
					cancelDownload();
				}
			}
		}
	};

	private void cancelDownload() {
		WordsController.getInstance(this).cancel();
		try {
			Thread.sleep(300);
		} catch (Exception e) {
		}
		finish();
	}

	private void downloadStart() {

		if(!checkNetworking(false)) return;

		showLoadingProgress("다운로드 중입니다. 잠시만 기다려 주세요.");
		WordsController.getInstance(this).setURL(download_path);
		WordsController.getInstance(this).setListener(new WordsControllerProgress() {


			@Override
			public void onFileExist() {
				hideProgress();
				LogUtil.d(" FILE Exist ");
			}

			@Override
			public void onException(String msg) {
				hideProgress();
				showDownloadState(" 에러 : " + msg);
				showRetryButton();
			}

			@Override
			public void onDownloading(String progress) {
			}

			@Override
			public void onDownloadComplete() {
				currentFileCnt++;
				showDownloadState(" 다운로드 중  : (" + currentFileCnt + "/" + totalFileCnt + ")");
			}

			@Override
			public void onDBCheck() {
			}

			@Override
			public void onConnectingServer(String url) {
			}

			@Override
			public void onComplete() {
				showDownloadState(" 다운로드 완료 되었습니다. ");
				setResult(ActivityWordTestDaySelect.CHANT_FILE_DOWNLOAD_RESULT, getIntent());
				finish();
			}
		});

		WordsController.getInstance(this).downloadFiles(wordsDownloadArry);
	}

	private void downloadRetry() {
		btn_retry.setVisibility(View.GONE);
		downloadStart();
	}

	private void showDownloadState(final String msg) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tv_state.setText(msg);
			}
		});
	}

	private void showRetryButton() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btn_retry.setVisibility(View.VISIBLE);
			}
		});
	}




}
