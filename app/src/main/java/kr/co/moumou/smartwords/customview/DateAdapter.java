package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.activity.ActivityWordTestDaySelect;
import kr.co.moumou.smartwords.activity.ActivityWordTestTypeSelect;
import kr.co.moumou.smartwords.activity.ActivityWordsDailyReport;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoCalData;
import kr.co.moumou.smartwords.vo.VoCalDetail;

public class DateAdapter extends BaseAdapter implements OnClickListener {
	
	private Context mContext;
	private ArrayList<VoCalData> arrData;
	private ArrayList<VoCalDetail> arrDetail;
	private LayoutInflater mInflater;
	
	DialogCal mDialog;
	
	ViewHolder viewHolder;
	
	private class ViewHolder {
		LinearLayout ll_oneday;
		LinearLayout ll_today;
		LinearLayout ll_detail;
		LinearLayout ll_levday;
		LinearLayout ll_word;
		LinearLayout ll_time;
		LinearLayout ll_day;
		TextView tv_day;
		ImageButton ib_more;
		TextView tv_level;
		TextView tv_slash;
		TextView tv_stdday;
		TextView tv_word_t;
		TextView tv_word;
		TextView tv_stdtime_t;
		TextView tv_stdtime;
		Button bt_dailyreport;
		TextView tv_restudy;
		//TextView tv_report;
	}
	
	public DateAdapter(Context context, ArrayList<VoCalData> arrayList) {
		this.mContext = context;
		this.arrData = arrayList;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setList(ArrayList<VoCalData> arrayList) {
		this.arrData = arrayList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return arrData.size();
	}

	@Override
	public Object getItem(int position) {
		return arrData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.view_oneday, parent, false);
		}
		
		viewHolder = new ViewHolder();
		viewHolder.ll_oneday = (LinearLayout) convertView.findViewById(R.id.ll_oneday);
		viewHolder.ll_today = (LinearLayout) convertView.findViewById(R.id.ll_today);
		viewHolder.ll_detail = (LinearLayout) convertView.findViewById(R.id.ll_detail);
		viewHolder.ll_levday = (LinearLayout) convertView.findViewById(R.id.ll_levday);
		viewHolder.ll_word = (LinearLayout) convertView.findViewById(R.id.ll_word);
		viewHolder.ll_time = (LinearLayout) convertView.findViewById(R.id.ll_time);
		viewHolder.tv_day = (CustomTextView) convertView.findViewById(R.id.tv_day);
		viewHolder.ib_more = (ImageButton) convertView.findViewById(R.id.ib_more);
		viewHolder.tv_level = (CustomTextView) convertView.findViewById(R.id.tv_level);
		viewHolder.tv_slash = (CustomTextView) convertView.findViewById(R.id.tv_slash);
		viewHolder.tv_stdday = (CustomTextView) convertView.findViewById(R.id.tv_stdday);
		viewHolder.tv_word_t = (CustomTextView) convertView.findViewById(R.id.tv_word_t);
		viewHolder.tv_word = (CustomTextView) convertView.findViewById(R.id.tv_word);
		viewHolder.tv_stdtime_t = (CustomTextView) convertView.findViewById(R.id.tv_stdtime_t);
		viewHolder.tv_stdtime = (CustomTextView) convertView.findViewById(R.id.tv_stdtime);
		viewHolder.bt_dailyreport = (CustomButton) convertView.findViewById(R.id.bt_dailyreport);
		viewHolder.tv_restudy = (CustomTextView) convertView.findViewById(R.id.tv_restudy);
		//viewHolder.tv_report = (CustomTextView) convertView.findViewById(R.id.tv_report);
		
		DisplayUtil.setLayoutHeight((Activity) mContext, 52, viewHolder.ll_today);
		DisplayUtil.setLayoutHeight((Activity) mContext, 150, viewHolder.ll_detail);
		DisplayUtil.setLayoutMargin((Activity) mContext, 14, 0, 0, 0, viewHolder.tv_day);
		DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 8, 0, viewHolder.ib_more);
		DisplayUtil.setLayout((Activity) mContext, 63, 26, viewHolder.ib_more);
		DisplayUtil.setLayoutHeight((Activity) mContext, 44, viewHolder.bt_dailyreport);
		DisplayUtil.setLayoutMargin((Activity) mContext, 14, 5, 0, 0, viewHolder.ll_levday);
		DisplayUtil.setLayoutMargin((Activity) mContext, 14, 9, 0, 9, viewHolder.ll_word);
		DisplayUtil.setLayoutMargin((Activity) mContext, 14, 0, 0, 9, viewHolder.ll_time);
		DisplayUtil.setLayoutMargin((Activity) mContext, 4, 0, 4, 0, viewHolder.tv_slash);
		DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 5, 0, viewHolder.tv_word_t);
		DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 5, 0, viewHolder.tv_stdtime_t);
		DisplayUtil.setLayoutMargin((Activity) mContext, 5, 0, 0, 0, viewHolder.tv_restudy);
		
		viewHolder.ll_day = (LinearLayout) convertView.findViewById(R.id.ll_day);
		DisplayUtil.setLayoutMargin((Activity) mContext, 12, 5, 0, 0, viewHolder.ll_day);
		
		viewHolder.tv_day.setTag(position);		
		
		if(arrData.get(position).getSTD_DD() == 0) {
			viewHolder.tv_day.setVisibility(View.INVISIBLE);
			setView(View.INVISIBLE);
		} else {
			viewHolder.tv_day.setVisibility(View.VISIBLE);
			setView(View.VISIBLE);
			
			viewHolder.ib_more.setTag(position);
			viewHolder.bt_dailyreport.setTag(position);
			
			arrDetail = arrData.get(position).getUSERSTD();
			
			viewHolder.tv_day.setText(arrData.get(position).getSTD_DD() + "");
		
			if(arrDetail.size() > 0) {
				
				if(arrDetail.size() == 1) {	
					if("N".equals(arrDetail.get(0).getDELETE_YN())) {
						viewHolder.bt_dailyreport.setVisibility(View.VISIBLE);
						viewHolder.bt_dailyreport.setOnClickListener(this);
					}else{
						viewHolder.bt_dailyreport.setVisibility(View.GONE);
					}
					viewHolder.ib_more.setVisibility(View.INVISIBLE);
					
				}else{
					viewHolder.ib_more.setVisibility(View.VISIBLE);
					viewHolder.bt_dailyreport.setVisibility(View.GONE);
					viewHolder.ll_word.setVisibility(View.VISIBLE);
					viewHolder.ib_more.setOnClickListener(this);
				}
					
				String std_w_gb = arrDetail.get(0).getSTD_W_GB();
				
				viewHolder.tv_level.setText("Level " + arrDetail.get(0).getSTD_LEVEL());
				viewHolder.tv_stdday.setText((Constant.STD_W_GB_R.equals(std_w_gb) ? "Review " : "Day ") + arrDetail.get(0).getSTD_DAY());
				viewHolder.tv_word.setText(arrDetail.get(0).getWORD_CNT() + "개");
				viewHolder.tv_stdtime.setText(arrDetail.get(0).getSTD_TIME() + "초");
				
				if("R".equals(arrDetail.get(0).getSTD_GB())) {
					viewHolder.tv_restudy.setVisibility(View.VISIBLE);
				}else{
					viewHolder.tv_restudy.setVisibility(View.INVISIBLE);
				}
				
				if("N".equals(arrDetail.get(0).getDELETE_YN())) {
					viewHolder.ll_word.setVisibility(View.VISIBLE);
				}else{
					viewHolder.ll_word.setVisibility(View.GONE);
				}
				
			}else{
				setView(View.INVISIBLE);
			}
			
			/*if(arrDetail.size() == 0){
				setView(View.INVISIBLE);
			} else if(arrDetail.size() == 1){
				setView(View.VISIBLE);
				
				viewHolder.ib_more.setVisibility(View.INVISIBLE);
				viewHolder.bt_dailyreport.setVisibility(View.VISIBLE);
				
				viewHolder.tv_level.setText("Level " + arrDetail.get(0).getSTD_LEVEL());
				viewHolder.tv_stdday.setText("Day " + arrDetail.get(0).getSTD_DAY());
				viewHolder.tv_word.setText(arrDetail.get(0).getWORD_CNT() + "개");
				viewHolder.tv_stdtime.setText(arrDetail.get(0).getSTD_TIME() + "초");
			} else {
				setView(View.VISIBLE);
				
				viewHolder.ib_more.setVisibility(View.VISIBLE);
				viewHolder.bt_dailyreport.setVisibility(View.INVISIBLE);
				
				viewHolder.tv_level.setText("Level " + arrDetail.get(0).getSTD_LEVEL());
				viewHolder.tv_stdday.setText("Day " + arrDetail.get(0).getSTD_DAY());
				viewHolder.tv_word.setText(arrDetail.get(0).getWORD_CNT() + "개");
				viewHolder.tv_stdtime.setText(arrDetail.get(0).getSTD_TIME() + "초");
			}*/
			
			if((Integer) viewHolder.tv_day.getTag()%7 == 0) {
				viewHolder.tv_day.setTextColor(mContext.getResources().getColor(R.color.cal_sunday));
			} else if ((Integer) viewHolder.tv_day.getTag()%7 == 6) {
				viewHolder.tv_day.setTextColor(mContext.getResources().getColor(R.color.words_blue));
			} else {
				viewHolder.tv_day.setTextColor(mContext.getResources().getColor(R.color.cal_day));
			}
		}
		
		return convertView;		
	}
	
	public void setView(int visibility) {
		if(visibility == View.VISIBLE) {
			//viewHolder.ib_more.setVisibility(View.VISIBLE);
			viewHolder.tv_level.setVisibility(View.VISIBLE);
			viewHolder.tv_slash.setVisibility(View.VISIBLE);
			viewHolder.tv_stdday.setVisibility(View.VISIBLE);
			viewHolder.tv_word_t.setVisibility(View.VISIBLE);
			viewHolder.tv_word.setVisibility(View.VISIBLE);
			viewHolder.tv_stdtime_t.setVisibility(View.VISIBLE);
			viewHolder.tv_stdtime.setVisibility(View.VISIBLE);
			//viewHolder.bt_dailyreport.setVisibility(View.VISIBLE);
		} else if(visibility == View.INVISIBLE){
			viewHolder.ib_more.setVisibility(View.INVISIBLE);
			viewHolder.tv_level.setVisibility(View.INVISIBLE);
			viewHolder.tv_slash.setVisibility(View.INVISIBLE);
			viewHolder.tv_stdday.setVisibility(View.INVISIBLE);
			viewHolder.tv_word_t.setVisibility(View.INVISIBLE);
			viewHolder.tv_word.setVisibility(View.INVISIBLE);
			viewHolder.tv_stdtime_t.setVisibility(View.INVISIBLE);
			viewHolder.tv_stdtime.setVisibility(View.INVISIBLE);
			viewHolder.bt_dailyreport.setVisibility(View.INVISIBLE);
			viewHolder.tv_restudy.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.ib_more :				
				mDialog = new DialogCal(mContext, arrData.get((Integer) v.getTag()));
				mDialog.show();
				break;
				
			case R.id.bt_dailyreport :
//				
//				Intent report = new Intent(mContext, ActivityWordsDailyReport.class);
//				report.putExtra("wordtest_level", String.valueOf(arrData.get((Integer) v.getTag()).getUSERSTD().get(0).getSTD_LEVEL()));
//				report.putExtra("wordtest_day", arrData.get((Integer) v.getTag()).getUSERSTD().get(0).getSTD_DAY());
//				report.putExtra("wordtest_stdy_gubn", "S");
//				mContext.startActivity(report);
				
				
				int pos = (Integer) v.getTag();
				
				Intent report = new Intent(mContext, ActivityWordsDailyReport.class);
				report.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL , String.valueOf(arrData.get(pos).getUSERSTD().get(0).getSTD_LEVEL()));
				report.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, arrData.get(pos).getUSERSTD().get(0).getSTD_DAY());
				report.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_W_GUBN, arrData.get(pos).getUSERSTD().get(0).getSTD_W_GB());
				report.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_GUBN, arrData.get(pos).getUSERSTD().get(0).getSTD_GB());
				mContext.startActivity(report);
				
				break;
		}
		
	}
	
}