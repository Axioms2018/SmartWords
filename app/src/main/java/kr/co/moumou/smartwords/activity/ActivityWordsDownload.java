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

	private int COUNT = 3;
	private ArrayList<BannerInfo> advertiseUrl = new ArrayList<BannerInfo>();
	private ImageLoader imgLoader;

	private ViewPager view_pager;
	private ViewIndicator view_indicator;
	private AdpaterAdvertise adapter;

	private TextView tv_state;
	private Button btn_retry;

	@Override
	public ViewTopMenu getTopManu() {
		return null;
	}

	@Override
	public int getTopMenuPos() {
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_words_download);

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = (int) (display.getWidth() * 0.7); // Display 사이즈의 70%
		int height = (int) (display.getHeight() * 0.96); // Display 사이즈의 90%
		getWindow().getAttributes().width = width;
		getWindow().getAttributes().height = height;

		Intent intent = getIntent();
		download_path = intent.getStringExtra(C_PARAM_DOWNLOAD_URL);
		wordsDownloadArry = (ArrayList<VoFileWordsDownload>) intent.getSerializableExtra(C_PARAM_DOWNLOAD_DATA);
		totalFileCnt = wordsDownloadArry.size();

		imgLoader = ImageLoader.getInstance();
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		view_indicator = (ViewIndicator) findViewById(R.id.view_indicator);
		view_pager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					adveriseAutoMoveCancel();
					break;
				case MotionEvent.ACTION_UP:
					advertiseAutoMove();
					break;
				default:
					break;
				}
				return false;
			}
		});

		view_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				int movePos = position;
				if (position < COUNT) {// 1번째 아이템에서 마지막 아이템으로 이동하면
					movePos = (position + COUNT);
					view_pager.setCurrentItem(position + COUNT, false); // 이동
																		// 애니메이션을
																		// 제거 해야
																		// 한다
				} else if (position >= COUNT * 2) {// 마지막 아이템에서 1번째 아이템으로 이동하면
					movePos = (position - COUNT);
					view_pager.setCurrentItem(movePos, false);
				}
				view_indicator.setCurrentPage(movePos %= COUNT);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		tv_state = (TextView) findViewById(R.id.tv_state);

		findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {

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

		downloadStart();
		advertiseAutoMove();
	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.IntentAction.ACTION_DOWNLOAD_CANCEL);
		registerReceiver(broadcastReceiver, intentFilter);

		if (adapter == null) {
			adapter = new AdpaterAdvertise(this, advertiseUrl);
		}

		if (advertiseUrl.size() <= 0) {
//			reqAdvertiseUrl();
			return;
		}

		view_pager.setAdapter(adapter);
		view_pager.setCurrentItem(COUNT);
		view_indicator.setPageCount(COUNT);
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

		WordsController.getInstance(this).setURL(download_path);
		WordsController.getInstance(this).setListener(new WordsControllerProgress() {

			@Override
			public void onFileExist() {
				LogUtil.d(" FILE Exist ");
			}

			@Override
			public void onException(String msg) {
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

	// 광고 관련
	private void adveriseAutoMoveCancel() {
		advertiseMoveHandler.removeMessages(WHAT_ADVERTISE_AUTO_MOVE_TIME);
	}

	private void advertiseAutoMove() {
		advertiseMoveHandler.removeMessages(WHAT_ADVERTISE_AUTO_MOVE_TIME);
		advertiseMoveHandler.sendEmptyMessageDelayed(WHAT_ADVERTISE_AUTO_MOVE_TIME, ADVERTISE_AUTO_MOVE_TIME);
	}

	private void advertiseMoveNext() {
		view_pager.setCurrentItem((view_pager.getCurrentItem() + 1));
		view_indicator.setCurrentPage((view_pager.getCurrentItem() % COUNT));

		advertiseMoveHandler.removeMessages(WHAT_ADVERTISE_AUTO_MOVE_TIME);
		advertiseMoveHandler.sendEmptyMessageDelayed(WHAT_ADVERTISE_AUTO_MOVE_TIME, ADVERTISE_AUTO_MOVE_TIME);
	}

	private Handler advertiseMoveHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			advertiseMoveNext();
		}
	};

	private void reqAdvertiseUrl() {

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.URL_COMMON,ConstantsCommURL.REQUEST_GET_BANNER);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("COMMAND",ConstantsCommCommand.COMMAND_1578_GET_BENNAER);
		AndroidNetworkRequest.getInstance(this).StringRequest(ConstantsCommURL.REQUEST_TAG_BANNER, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				LogUtil.e(" Response Msg : " + response);

				VoBanner result = new Gson().fromJson(response, VoBanner.class);
				advertiseUrl = result.getRES_LIST();
				COUNT = advertiseUrl.size();
				adapter.setList(advertiseUrl);
				view_pager.setAdapter(adapter);
				view_pager.setCurrentItem(COUNT);
				adapter.notifyDataSetChanged();
				view_indicator.setPageCount(COUNT);
			}

			@Override
			public void systemcheck(String response) {

			}

			@Override
			public void fail(VoBase base) {
				Toast.makeText(ActivityWordsDownload.this, "onResponseFail 광고를 받아오지 못 했습니다. (" + base + ")",
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void exception(ANError error) {
				Toast.makeText(ActivityWordsDownload.this, "exception 광고를 받아오지 못 했습니다. (" + error.toString() + ")",
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void dismissDialog() {

			}
		});
	}

	private class AdpaterAdvertise extends PagerAdapter {

		private Context context;
		private ArrayList<BannerInfo> list;

		public void setList(ArrayList<BannerInfo> list) {
			this.list = list;
		}

		public AdpaterAdvertise(Context context, ArrayList<BannerInfo> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return COUNT * 3;
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			position %= COUNT;

			ImageView v = new ImageView(context);
			imgLoader.displayImage(list.get(position).getIMG_URL(), v, ApplicationPool.getAUILDisplayOptions());
			final String linkUrl = list.get(position).getLINK_URL();

			((ViewPager) pager).addView(v, 0);

			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (StringUtil.isNull(linkUrl)) {
						return;
					}
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(linkUrl));
					startActivity(intent);
				}
			});

			return v;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

}
