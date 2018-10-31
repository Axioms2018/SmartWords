package kr.co.moumou.smartwords.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.androidnetworking.error.ANError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.customview.CalSelAdapter;
import kr.co.moumou.smartwords.customview.CustomTextView;
import kr.co.moumou.smartwords.customview.DateAdapter;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoCalData;
import kr.co.moumou.smartwords.vo.VoCalFrame;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoUserInfo;

public class FragmentMyCal extends Fragment implements OnClickListener {
	
	private final static int SMARTWORDS_START_YEAR = 2016;
	public VoUserInfo mUserInfo;

	private ActivityMywordsMain wordsMain = null;
	GridView mGridView;
	DateAdapter dAdapter;
	CalSelAdapter yAdapter;
	CalSelAdapter mAdapter;
	VoCalFrame voCalFrame;
	VoCalData voCalData;
	ArrayList<VoCalData> mArrData;
	
	ArrayList<String> arrayDayName = new ArrayList<String>();
	ArrayList<String> year_array = new ArrayList<String>();
	ArrayList<String> month_array = new ArrayList<String>();
	
	Calendar mCal = Calendar.getInstance();
	
	int now_year = mCal.get(Calendar.YEAR);
	int now_month = mCal.get(Calendar.MONTH) + 1;
	int change_year = mCal.get(Calendar.YEAR);
	int change_month = mCal.get(Calendar.MONTH) + 1;
	
	LinearLayout ll_selyear;
	LinearLayout ll_selmonth;
	LinearLayout ll_margin;
	TextView tv_year;
	ImageView iv_year;
	TextView tv_month;
	ImageView iv_month;
	TextView tv_day;
	TextView tv_dayname[] = new TextView[7];
	private ListPopupWindow year_popup;
	private ListPopupWindow month_popup;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_words_mycal, container, false);
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wordsMain = (ActivityMywordsMain) getActivity();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Date date = VoMyInfo.getInstance().getLOGN_TM();
		
		if(null != date) {
			now_year = change_year = date.getYear() + 1900;
			now_month = change_month = date.getMonth() + 1;
		}
		
		dAdapter = new DateAdapter(wordsMain, mArrData);
        
        for(int i = SMARTWORDS_START_YEAR ; i <= now_year ; i ++) {
        	year_array.add(i + "");
        }
        for(int i = 0 ; i < 12 ; i ++) {
        	month_array.add(i+1 + "");
        }
        
        yAdapter = new CalSelAdapter(wordsMain, year_array);
        mAdapter = new CalSelAdapter(wordsMain, month_array);
        
        ll_selyear = (LinearLayout) getView().findViewById(R.id.ll_selyear);
        ll_selmonth = (LinearLayout) getView().findViewById(R.id.ll_selmonth);
        ll_margin = (LinearLayout) getView().findViewById(R.id.ll_margin);
        tv_year = (CustomTextView) getView().findViewById(R.id.tv_year);
        iv_year = (ImageView) getView().findViewById(R.id.iv_year);
        tv_month = (CustomTextView) getView().findViewById(R.id.tv_month);
        iv_month = (ImageView) getView().findViewById(R.id.iv_month);
        tv_dayname[0] = (CustomTextView) getView().findViewById(R.id.tv_dayname1);
        tv_dayname[1] = (CustomTextView) getView().findViewById(R.id.tv_dayname2);
        tv_dayname[2] = (CustomTextView) getView().findViewById(R.id.tv_dayname3);
        tv_dayname[3] = (CustomTextView) getView().findViewById(R.id.tv_dayname4);
        tv_dayname[4] = (CustomTextView) getView().findViewById(R.id.tv_dayname5);
        tv_dayname[5] = (CustomTextView) getView().findViewById(R.id.tv_dayname6);
        tv_dayname[6] = (CustomTextView) getView().findViewById(R.id.tv_dayname7);
        mGridView = (GridView) getView().findViewById(R.id.gv_cal);
        
        DisplayUtil.setLayoutWidth(wordsMain, 88, ll_margin);
        DisplayUtil.setLayoutHeight(wordsMain, 66, tv_year);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 16, 0, tv_year);
        DisplayUtil.setLayoutHeight(wordsMain, 66, tv_month);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 16, 0, tv_month);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[0]);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[1]);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[2]);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[3]);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[4]);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[5]);
        DisplayUtil.setLayoutHeight(wordsMain, 50, tv_dayname[6]);
        
        arrayDayName.add("SUN");
        arrayDayName.add("MON");
        arrayDayName.add("TUE");
        arrayDayName.add("WED");
        arrayDayName.add("THU");
        arrayDayName.add("FRI");
        arrayDayName.add("SAT");
                
        for(int i = 0 ; i < 7 ; i ++) {        	
        	tv_dayname[i].setText(arrayDayName.get(i));
        }
        
        requestData(now_year, now_month);
        
        tv_year.setText(now_year + " 년");
        ll_selyear.setOnClickListener(this);
        tv_month.setText(now_month + " 월");
        ll_selmonth.setOnClickListener(this);
        
        year_popup = new ListPopupWindow(wordsMain);
        year_popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        year_popup.setAdapter(yAdapter);
        year_popup.setAnchorView(ll_selyear);
        year_popup.setModal(true);
        year_popup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogTraceMin.I("position :::"  + position);
				tv_year.setText(parent.getItemAtPosition(position) + " 년");
				change_year = Integer.valueOf((String) parent.getItemAtPosition(position));
				
				requestData(change_year, change_month);
				
				year_popup.dismiss();
				
			}
        	
		});
        
        month_popup = new ListPopupWindow(wordsMain);
        month_popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        month_popup.setAdapter(mAdapter);
        month_popup.setAnchorView(ll_selmonth);
        month_popup.setModal(true);
        month_popup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				tv_month.setText(parent.getItemAtPosition(position) + " 월");
				change_month = Integer.valueOf((String) parent.getItemAtPosition(position));
				
				requestData(change_year, change_month);
				
				month_popup.dismiss();
				
			}
        	
		});
	}

	public void setCalendar(int year, int month) {
		
		mArrData = new ArrayList<VoCalData>();
    	
		mCal.set(Calendar.YEAR, year);
		mCal.set(Calendar.MONTH, month - 1);
	    mCal.set(Calendar.DATE, 1);
	    
	    int maxday = mCal.getActualMaximum(Calendar.DATE);
	    int startday = mCal.get(Calendar.DAY_OF_WEEK);
	    int size = (startday - 1) + maxday;
	    int dummy;
	    int count = 0;		
	    	
	    if(startday != 1) {
	    	for(int i = 0 ; i < startday - 1 ; i ++) {
	    		voCalData = new VoCalData();
	    		voCalData.setSTD_DD(0);
	    		mArrData.add(voCalData);
	       	}
	    }	    

		for(int i = 0 ; i < maxday ; i ++) {			
			voCalData = new VoCalData();
			
			if(count < voCalFrame.getSTD_LIST().size()) {
				if(i+1 == voCalFrame.getSTD_LIST().get(count).getSTD_DD()) {
					voCalData.setSTD_DD(voCalFrame.getSTD_LIST().get(count).getSTD_DD());
					voCalData.setUSERSTD(voCalFrame.getSTD_LIST().get(count).getUSERSTD());
					count ++;				
				} else {
					voCalData.setSTD_DD(i+1);
				}
			} else {
				voCalData.setSTD_DD(i+1);
			}
			
			mArrData.add(voCalData);
	    }
	    	
	    if(size > 35) {
	    	dummy = 42 - size;
	    } else {
	    	dummy = 35 - size;
	    }
	    	
	    if(dummy != 0) {
	    	for(int i = 0 ; i < dummy ; i ++) {
	    		voCalData = new VoCalData();
	    		voCalData.setSTD_DD(0);
	    		mArrData.add(voCalData);
	       	}
	    }
	    	
	    dAdapter.setList(mArrData);	    	
		
		mGridView.setAdapter(dAdapter);
	}
	
	private void requestData(final int year, final int month) {
		wordsMain.showLoadingProgress(getResources().getString(R.string.wait_for_data));

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_USERCALENDAR);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoUserInfo.getInstance().getSID());
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(getContext(),Preferences.PREF_USER_ID,null));

		if(month < 10) {
			builder.appendQueryParameter("STD_DATE", String.valueOf(year) + "0" + String.valueOf(month));
		} else {
			builder.appendQueryParameter("STD_DATE", String.valueOf(year) + String.valueOf(month));
		}


		AndroidNetworkRequest.getInstance(getActivity()).StringRequest(ConstantsCommURL.REQUEST_TAG_USERCALENDAR, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				wordsMain.hideProgress();
				voCalFrame = ApplicationPool.getGson().fromJson(response, VoCalFrame.class);
				setCalendar(year, month);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_selyear :
				year_popup.setAnimationStyle(-1);
				year_popup.show();
				break;
			
			case R.id.ll_selmonth :
				month_popup.setAnimationStyle(-1);
				month_popup.show();
				break;

			default:
				break;
		}
		
	}

}
