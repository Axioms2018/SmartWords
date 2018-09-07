package kr.co.moumou.smartwords.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.moumou.smartwords.MainActivity;
import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.NetworkState;

public class ActivityNetworkCheckDialog extends Activity implements OnClickListener {


	private TextView tv_title;
	private TextView tv_contents;
	private Button btn_first;
	private Button btn_second;
	private Button btn_third;

	private LinearLayout lay_title;
	private LinearLayout lay_second;
	private LinearLayout lay_third;
	private double widthPer = 0.5;
	private double heightPer = 0.4;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);

		setFinishOnTouchOutside(false);

		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = (int) (display.getWidth() * widthPer); //Display 사이즈의 60%
		int height = (int) (display.getHeight() * heightPer);  //Display 사이즈의 50%
		getWindow().getAttributes().width = width;
		getWindow().getAttributes().height = height;


		tv_title = (TextView)findViewById(R.id.tv_title);
		tv_contents = (TextView)findViewById(R.id.tv_contents);

		btn_first = (Button)findViewById(R.id.btn_first);
		btn_second = (Button)findViewById(R.id.btn_second);
		btn_third = (Button)findViewById(R.id.btn_third);

		lay_title= (LinearLayout)findViewById(R.id.lay_title);
		lay_second = (LinearLayout)findViewById(R.id.lay_second);
		lay_third = (LinearLayout)findViewById(R.id.lay_third);

		btn_first.setTag(1);
		btn_second.setTag(2);
		btn_third.setTag(3);

		btn_first.setOnClickListener(this);
		btn_second.setOnClickListener(this);
		btn_third.setOnClickListener(this);

		lay_title.setVisibility(View.GONE);

		tv_contents.setText(getString(R.string.warning_not_available_network));
		tv_contents.setGravity(Gravity.CENTER);
		setButtonText(getString(R.string.network_setting), getString(R.string.terminate));

		new NetworkChecker().start();
	}

	@Override
	public void onBackPressed() {
		
	}
	
	public void setButtonText(String... text){
		if(text.length == 1){
			btn_first.setText(text[0]);
			lay_second.setVisibility(View.GONE);
			lay_third.setVisibility(View.GONE);
		}else if(text.length == 2){
			btn_first.setText(text[0]);
			btn_second.setText(text[1]);
			lay_third.setVisibility(View.GONE);
		}else{
			btn_first.setText(text[0]);
			btn_second.setText(text[1]);
			btn_third.setText(text[2]);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_first:
			launchWifiSetting();
			break;
		case R.id.btn_second:
			terminateApplication();
			break;
		}

	}

	private void launchWifiSetting(){
		Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
		startActivity(intent);
	}

	private void terminateApplication(){
		Intent intent = new Intent(ActivityNetworkCheckDialog.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Constant.IntentKeys.INTENT_TERMINATE_APP, true);
		startActivity(intent);
		finish();
	}

	private class NetworkChecker extends Thread {
		boolean isRun = true;
		@Override
		public void run() {
			while (isRun) {
				if(NetworkState.getInstance().isNetworkConnected(ActivityNetworkCheckDialog.this)){
					finish();
				}else{
					try {
						sleep(300);
					} catch (Exception e) {
					}
				}
			}
		}
	}
}
