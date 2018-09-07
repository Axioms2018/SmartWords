package kr.co.moumou.smartwords.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import kr.co.moumou.smartwords.R;

import kr.co.moumou.smartwords.customview.ViewTopMenu;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.SharedPrefData;
import kr.co.moumou.smartwords.vo.VoCertificate;

public class ActivityCertificate extends ActivityBase implements OnClickListener {

	protected static String P_CER_LEVEL = "p_certi_level";
	protected static String P_CERTIFICATE = "p_certificate";
	private VoCertificate voCertifi;
	private int level = 1;
	
	private RelativeLayout rl_bg;
	private ImageView iv_title;
	private LinearLayout ll_txt1, ll_txt2;
	private TextView tv_name, tv_words, tv_level, tv_date;
	private ImageButton btn_close;
	
	private ImageView iv_ani;
	private AnimationDrawable frameAnimation;
	
	private int[] certificateBg = {R.drawable.ic_certi_bg_a, R.drawable.ic_certi_bg_b, R.drawable.ic_certi_bg_c, R.drawable.ic_certi_bg_d, R.drawable.ic_certi_bg_e};
	private int[] certificateColor = {R.color.words_certfi_lv1, R.color.words_certfi_lv2, R.color.words_certfi_lv3, R.color.words_certfi_lv4, R.color.words_certfi_lv5};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_words_certificate);
		
		level = Integer.parseInt(getIntent().getStringExtra(P_CER_LEVEL));
		voCertifi = (VoCertificate) getIntent().getSerializableExtra(P_CERTIFICATE);
		
		rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
		iv_title = (ImageView) findViewById(R.id.iv_title);
		DisplayUtil.setLayout(this, 393, 50, iv_title);
		DisplayUtil.setLayoutMargin(this, 0, 52, 0, 0, iv_title);
		ll_txt1 = (LinearLayout) findViewById(R.id.ll_txt1);
		ll_txt2 = (LinearLayout) findViewById(R.id.ll_txt2);
		DisplayUtil.setLayoutMargin(this, 0, 52, 0, 0, ll_txt1);
		DisplayUtil.setLayoutMargin(this, 0, 7, 0, 0, ll_txt2);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_words = (TextView) findViewById(R.id.tv_words);
		tv_level = (TextView) findViewById(R.id.tv_level);
		tv_date = (TextView) findViewById(R.id.tv_date);
		DisplayUtil.setLayoutMargin(this, 0, 30, 0, 0, tv_date);
		btn_close = (ImageButton) findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);
		DisplayUtil.setLayout(this, 77, 77, btn_close);
		DisplayUtil.setLayoutMargin(this, 0, 60, 60, 0, btn_close);

		iv_ani = (ImageView) findViewById(R.id.iv_ani);
		DisplayUtil.setLayout(this, 198, 248, iv_ani);
		DisplayUtil.setLayoutMargin(this, 0, 24, 0, 0, iv_ani);
		
		setReportInfo();
//		startAnimation();
	}
	
	@Override
	protected void onDestroy() {
//		stopAnimtaion();
		super.onDestroy();
	}
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	
	private void setReportInfo() {
		
		rl_bg.setBackgroundResource(certificateBg[level - 1]);
		tv_words.setTextColor(getResources().getColor(certificateColor[level - 1]));
		tv_level.setTextColor(getResources().getColor(certificateColor[level - 1]));

		tv_name.setText(SharedPrefData.getStringSharedData(this, SharedPrefData.SHARED_USER_ID_S, ""));
		tv_words.setText(voCertifi.getRIGHT_CNT() + " / 1,000");
		tv_level.setText("Smart Words Level " + level + ".");
		
		rl_bg.setBackgroundResource(certificateBg[level - 1]);
		tv_date.setText(df.format(voCertifi.getSTD_DATE()));
	}
	
//	private void startAnimation() {
//		frameAnimation = (AnimationDrawable) iv_ani.getBackground();
//		frameAnimation.start();
//	}
	
	private void stopAnimtaion() {
		if(null != frameAnimation)
		frameAnimation.stop();
	}
	
	@Override
	public ViewTopMenu getTopManu() {
		return null;
	}

	@Override
	public int getTopMenuPos() {
		return 0;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_close) {
			onBackPressed();
		}
	}
	
	

}
