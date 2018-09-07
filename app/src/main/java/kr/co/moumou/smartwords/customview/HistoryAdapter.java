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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.activity.ActivityWordTestDaySelect;
import kr.co.moumou.smartwords.activity.ActivityWordTestTypeSelect;
import kr.co.moumou.smartwords.activity.ActivityWordsDailyReport;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.vo.VoCalDetail;

public class HistoryAdapter extends BaseAdapter implements OnClickListener {
	
	Context mContext;
	
	private ArrayList<VoCalDetail> arrDetail;
	
	ViewHoder viewHolder;
	
	public class ViewHoder {
		public LinearLayout ll_item;
		public LinearLayout ll_detail;
		public LinearLayout ll_word;
		public LinearLayout ll_time;
		public TextView tv_level;
		public TextView tv_slash;
		public TextView tv_day;
		public TextView tv_words_t;
		public TextView tv_words;
		public TextView tv_stdtime_t;
		public TextView tv_stdtime;
		public Button bt_report;
		public TextView tv_restudy;
	}
	
	public HistoryAdapter(Context context, ArrayList<VoCalDetail> detail) {
		this.mContext = context;
		this.arrDetail = detail;
	}

	@Override
	public int getCount() {
		return arrDetail.size();
	}

	@Override
	public Object getItem(int position) {
		return arrDetail.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = inflater.inflate(R.layout.history_item, null);
			
			viewHolder = new ViewHoder();
			viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
			viewHolder.ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
			viewHolder.ll_word = (LinearLayout) view.findViewById(R.id.ll_word);
			viewHolder.ll_time = (LinearLayout) view.findViewById(R.id.ll_time);
			viewHolder.tv_level = (TextView) view.findViewById(R.id.tv_level);
			viewHolder.tv_slash = (TextView) view.findViewById(R.id.tv_slash);
			viewHolder.tv_day = (TextView) view.findViewById(R.id.tv_stdday);
			viewHolder.tv_words_t = (TextView) view.findViewById(R.id.tv_word_t);
			viewHolder.tv_words = (TextView) view.findViewById(R.id.tv_word);
			viewHolder.tv_stdtime_t = (TextView) view.findViewById(R.id.tv_stdtime_t);
			viewHolder.tv_stdtime = (TextView) view.findViewById(R.id.tv_stdtime);
			viewHolder.bt_report = (Button) view.findViewById(R.id.bt_dailyreport);
			viewHolder.tv_restudy = (TextView) view.findViewById(R.id.tv_restudy);
			
			DisplayUtil.setLayout((Activity) mContext, 584, 100, viewHolder.ll_item);
			DisplayUtil.setLayoutMargin((Activity) mContext, 26, 0, 0, 0, viewHolder.ll_detail);
			DisplayUtil.setLayout((Activity) mContext, 178, 62, viewHolder.bt_report);
			DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 30, 0, viewHolder.bt_report);
			DisplayUtil.setLayoutMargin((Activity) mContext, 4, 0, 4, 0, viewHolder.tv_slash);
			DisplayUtil.setLayoutMargin((Activity) mContext, 0, 10, 0, 0, viewHolder.ll_word);
			DisplayUtil.setLayoutMargin((Activity) mContext, 0, 10, 0, 0, viewHolder.ll_time);
			DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 5, 0, viewHolder.tv_words_t);
			DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 5, 0, viewHolder.tv_stdtime_t);
			DisplayUtil.setLayoutMargin((Activity) mContext, 0, 0, 4, 0, viewHolder.tv_slash);
			
			view.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHoder) view.getTag();
		}
		String std_w_gb = arrDetail.get(position).getSTD_W_GB();
		
		viewHolder.tv_level.setText("Level " + arrDetail.get(position).getSTD_LEVEL());
		viewHolder.tv_day.setText((Constant.STD_W_GB_R.equals(std_w_gb) ? "Review " : "Day ") + arrDetail.get(position).getSTD_DAY());
		viewHolder.tv_words.setText(arrDetail.get(position).getWORD_CNT() + "개");
		viewHolder.tv_stdtime.setText(arrDetail.get(position).getSTD_TIME() + "초");
		if("R".equals(arrDetail.get(position).getSTD_GB()))
			viewHolder.tv_restudy.setVisibility(View.VISIBLE);
		else viewHolder.tv_restudy.setVisibility(View.GONE);
		
		if("N".equals(arrDetail.get(position).getDELETE_YN())) {
			viewHolder.bt_report.setVisibility(View.VISIBLE);
			viewHolder.ll_word.setVisibility(View.VISIBLE);
		}
		else {
			viewHolder.bt_report.setVisibility(View.INVISIBLE);
			viewHolder.ll_word.setVisibility(View.GONE);
		}
		
		viewHolder.bt_report.setText(Constant.STD_W_GB_R.equals(std_w_gb) ? "Review Report" : "Daily Report");
		viewHolder.bt_report.setTag(position);
		viewHolder.bt_report.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {			
			case R.id.bt_dailyreport :	
				int pos = (Integer) v.getTag();
				
				Intent report = new Intent(mContext, ActivityWordsDailyReport.class);
				report.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_LEVEL , String.valueOf(arrDetail.get(pos).getSTD_LEVEL()));
				report.putExtra(ActivityWordTestTypeSelect.PARAM_WORDTEST_DAY, arrDetail.get(pos).getSTD_DAY());
				report.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_W_GUBN, arrDetail.get(pos).getSTD_W_GB());
				report.putExtra(ActivityWordTestDaySelect.PARAM_WORDTEST_STDY_GUBN, arrDetail.get((Integer) v.getTag()).getSTD_GB());
				mContext.startActivity(report);
				break;
		}
		
	}

}
