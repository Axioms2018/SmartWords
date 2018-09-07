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
import kr.co.moumou.smartwords.vo.VoNoteDetail;

public class NoteAdapter extends BaseAdapter {
	Context mContext;
	
	ArrayList<VoNoteDetail> mArray;
	
	ViewHoder viewHolder;
	
	public class ViewHoder {
		public LinearLayout ll_item;
		public TextView tv_eng;
		public TextView tv_kor;
	}

	public NoteAdapter(Context context) {
		this.mContext = context;
	}
	
	public void setList(ArrayList<VoNoteDetail> lv) {
		this.mArray = lv;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mArray.size();
	}

	@Override
	public Object getItem(int position) {
		return mArray.get(position);
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
			
			view = inflater.inflate(R.layout.note_list_item, null);
			
			viewHolder = new ViewHoder();
			viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
			viewHolder.tv_eng = (TextView) view.findViewById(R.id.tv_eng);
			viewHolder.tv_kor = (TextView) view.findViewById(R.id.tv_kor);
			
			DisplayUtil.setLayoutHeight((Activity) mContext, 70, viewHolder.ll_item);
			
			view.setTag(viewHolder);			
		} else {
			viewHolder = (ViewHoder) view.getTag();			
		}	

		viewHolder.tv_eng.setText(mArray.get(position).getWORD());
		viewHolder.tv_kor.setText(mArray.get(position).getMEAN());
		
		return view;
	}
}
