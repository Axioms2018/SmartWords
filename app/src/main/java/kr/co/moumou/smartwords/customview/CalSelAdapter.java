package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;

public class CalSelAdapter extends BaseAdapter {
	
	Context mContext;
	ArrayList<String> array;
	LayoutInflater mInflater;
	
	LinearLayout ll_cal;
	TextView tv_cal;

	public CalSelAdapter(Context context, ArrayList<String> array) {
		this.mContext = context;
		this.array = array;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		return array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.cal_list_item, parent, false);
		}
		
		ll_cal = (LinearLayout) convertView.findViewById(R.id.ll_cal);
		tv_cal = (TextView) convertView.findViewById(R.id.tv_cal);
		
		DisplayUtil.setLayoutHeight((Activity) mContext, 40, ll_cal);
		
		tv_cal.setText(array.get(position));
		tv_cal.setTag(array.get(position));
		return convertView;
	}
}
