package kr.co.moumou.smartwords.customview;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.Constant;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoWordsDReportDetail;

public class WrongAdapter extends BaseAdapter {
	Context mContext;
	
	ArrayList<VoWordsDReportDetail> mArray;
	
	ViewHoder viewHolder;
	boolean isAllWords;
	String path;
	
	public class ViewHoder {
		public LinearLayout ll_item;
		public TextView tv_question;
		public TextView tv_answer;
		public TextView tv_myans;
		public ImageButton btn_player;
	}

	public WrongAdapter(Context context) {
		this.mContext = context;
	}
	
	public void setList(ArrayList<VoWordsDReportDetail> list, boolean isAllWords, String path) {
		this.mArray = list;
		this.isAllWords = isAllWords;
		notifyDataSetChanged();
		this.path = path;
		//미리 엠피 담아두자
		
		if(isAllWords){
			for(VoWordsDReportDetail reportList : list) {
				mpPaths.add(reportList.getSTD_MP3());
				reportList.setSelected(false);
			}
		}
		mPos = -1;
		mAllPos = 0;
		isAllPlay = false;
		playerView = null;
		isMpPlaying = false;
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
			
			view = inflater.inflate(R.layout.wrong_list_item, null);
			
			viewHolder = new ViewHoder();
			viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
			viewHolder.tv_question = (TextView) view.findViewById(R.id.tv_question);
			viewHolder.tv_answer = (TextView) view.findViewById(R.id.tv_answer);
			viewHolder.tv_myans = (TextView) view.findViewById(R.id.tv_myans);
			viewHolder.btn_player = (ImageButton) view.findViewById(R.id.btn_player);
			
			DisplayUtil.setLayoutHeight((Activity) mContext, 70, viewHolder.ll_item);
			
			view.setTag(viewHolder);			
		} else {
			viewHolder = (ViewHoder) view.getTag();			
		}	
		
		LinearLayout.LayoutParams params = null;

		if(isAllWords) {
			params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
			viewHolder.tv_question.setLayoutParams(params);
			viewHolder.tv_question.setText(Integer.toString(position + 1));
			viewHolder.tv_answer.setText(mArray.get(position).getWORD());
			viewHolder.tv_myans.setText(mArray.get(position).getMEAN());
			viewHolder.tv_myans.setTextColor(mContext.getResources().getColor(R.color.black));
			viewHolder.btn_player.setVisibility(View.VISIBLE);
			viewHolder.btn_player.setOnClickListener(playMpListener);
			viewHolder.btn_player.setTag(position);
			if(mArray.get(position).isSelected()) {
				viewHolder.btn_player.setSelected(true);
			}else{
				viewHolder.btn_player.setSelected(false);
			}
		}else{
			viewHolder.btn_player.setVisibility(View.GONE);
			viewHolder.btn_player.setOnClickListener(null);
			
			//User답에 # 포함되어 있는지 여부
			boolean isContainSharp = mArray.get(position).getANSWER().contains(Constant.GUBUN_SHARP)
                    && mArray.get(position).getUSER_ANS().contains(Constant.GUBUN_SHARP);
			
			params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
			viewHolder.tv_question.setLayoutParams(params);
			viewHolder.tv_question.setText(mArray.get(position).getWORD());
			
			if(isContainSharp) {
				
				String[] userAnswers = mArray.get(position).getUSER_ANS().split(Constant.GUBUN_SHARP);
				
				if(userAnswers.length > 0) {
					viewHolder.tv_myans.setText(mArray.get(position).getUSER_ANS().replaceAll(Constant.GUBUN_SHARP, Constant.GUBUN_SLASH));
				}else{
					viewHolder.tv_myans.setText("");
				}
				
				viewHolder.tv_answer.setText(mArray.get(position).getANSWER().replaceAll(Constant.GUBUN_SHARP, Constant.GUBUN_SLASH));

			} else {
				viewHolder.tv_answer.setText(mArray.get(position).getANSWER());
				viewHolder.tv_myans.setText(mArray.get(position).getUSER_ANS());
			}
			viewHolder.tv_myans.setTextColor(mContext.getResources().getColor(R.color.daily_wrong_my));
		}
		
		return view;
	}
	
	MediaPlayer mMediaPlayer = null;
	ArrayList<String> mpPaths = new ArrayList<>();
	View playerView = null;
	int mPos = -1;
	boolean isMpPlaying = false;	//음성이 재생중인게 있을때를 위해서
	
	View.OnClickListener playMpListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(isMpPlaying) return;
			
			//AllPlay 재생 멈춤
			stopAllMp3Play();
			
			//재생을 위한 조건
			isMpPlaying = true;
			
			if(playerView != null && mPos != -1) {
				playerView.setSelected(false);
				mArray.get(mPos).setSelected(false);
			}
			
			mPos = (Integer) v.getTag();
			playerView = v;
			mArray.get(mPos).setSelected(true);
			playerView.setSelected(true);
			
			LogTraceMin.I("mPos : " + mPos);
			//String domain = "http://install.moumou.co.kr/smartteam/smartwords/";
			String url = mpPaths.get(mPos);
			
			if(mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}
			
			if(mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
				mMediaPlayer.reset();
				if(allMp3CompleteListener != null) allMp3CompleteListener.onComplete();
			}
			
			try {
				LogTraceMin.I(path + url);
				mMediaPlayer.setDataSource(path + url);
				mMediaPlayer.setOnPreparedListener(preparedListener);
				mMediaPlayer.prepareAsync();
				mMediaPlayer.setOnCompletionListener(completeListener);
				
			} catch (Exception e) {
				LogTraceMin.E(e.toString());
			}
		}
	};
	
	MediaPlayer.OnCompletionListener completeListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			LogTraceMin.I("completeListener  mp.stop() ");
			mp.stop();
	        mp.reset();
	        if(playerView != null) playerView.setSelected(false);
	        if(mPos != -1) mArray.get(mPos).setSelected(false);
	        
	        isMpPlaying = false;
		}
	};
	
	
	MediaPlayer.OnPreparedListener preparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			LogTraceMin.I("preparedListener  mp.start() ");
			
			if(!isMpPlaying) return;
			mp.start();
		}
	};
	
	private void stopMp3Play() {
		if(mMediaPlayer != null) {
			isMpPlaying = false;
			mMediaPlayer.stop();
			mMediaPlayer.reset();
		}
	}
	
	/**
	 * 전체재생
	 */
	int mAllPos = 0;
	boolean isAllPlay = false;	//전체 재생중
	//MediaPlayer mAllMediaPlayer = null;
	
	public void allMp3Play(AllMp3Complete completeListener) {
		//mListView = (ListView) listView;
		if(isAllPlay) return;	//지금 AllMp3 재생중이면 우선 두번 안 눌린다.
		
		stopMp3Play();	//MP3가 재생중이면 정지
		
		allMp3CompleteListener = completeListener;
		allMp3CompleteListener.onPrepared();
		
		isAllPlay = true;
		
		if(playerView != null) {
			playerView.setSelected(false);
			mArray.get(mPos).setSelected(false);
		}
		
		allMp3Play(0);
	}
	
	public void allMp3Play(int pos) {
		
		mAllPos = pos;
		
		//String domain = "http://install.moumou.co.kr/smartteam/smartwords/";
		String url = mpPaths.get(mAllPos);
		
		if(mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		
		if(mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
		}
		
		try {
			LogTraceMin.I(path + url);
			mMediaPlayer.setDataSource(path + url);
			mMediaPlayer.setOnPreparedListener(allMpPreparedListener);
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnCompletionListener(allMpCompleteListener);
			
		} catch (Exception e) {
			LogTraceMin.E(e.toString());
		}
	}
	
	MediaPlayer.OnCompletionListener allMpCompleteListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			LogTraceMin.I("completeListener  all mp.stop() ");
			mp.stop();
	        mp.reset();
	        
	        if(!isAllPlay) return;

	        mAllPos++;
	        
	        if(mAllPos > (mpPaths.size() - 1)) {
	        	allMp3CompleteListener.onComplete();
	        	return;
	        }
	        allMp3Play(mAllPos);
		}
	};
	
	
	MediaPlayer.OnPreparedListener allMpPreparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			LogTraceMin.I("preparedListener  all mp.start() ");
			
			if(!isAllPlay) {
				return;
			}
			mp.start();
		}
	};
	
	AllMp3Complete allMp3CompleteListener = null;
	
	
	public interface AllMp3Complete {
		void onPrepared();
		void onComplete();
	}
	
	private void stopAllMp3Play() {
		
		isAllPlay = false;
		if(allMp3CompleteListener != null) {
			allMp3CompleteListener.onComplete();
		}
		
		if(mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
		}
	}

	/**
	 * 모든 플레이어 정지
	 */
	public void destoryAllMp3Play() {
		isMpPlaying = false;
		isAllPlay = false;
		
		for(VoWordsDReportDetail wordDetail :  mArray) {
			wordDetail.setSelected(false);
		}
		
		if(mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		
		if(completeListener != null) {
			completeListener = null;
		}
		if(preparedListener != null) {
			preparedListener = null;
		}
		
		if(allMp3CompleteListener != null) {
			allMp3CompleteListener.onComplete();
			allMp3CompleteListener = null;
		}
		if(allMpPreparedListener != null) {
			allMpPreparedListener = null;
		}
	}
	
//	public void destoryAllMp3Play() {
//		isMpPlaying = false;
//		isAllPlay = false;
//	}
	
//	class Player extends AsyncTask<String, Void, Boolean> {
//		
//		@Override
//		protected Boolean doInBackground(String... params) {
//			
//			Boolean prepared = false;
//			
//			try {
//				
//				LogTraceMin.I("params[0] :: " + params[0]);
//				
//				mMediaPlayer.setDataSource(params[0]);
//				//mMediaPlayer.setOnPreparedListener(preparedListener);
//				mMediaPlayer.setOnCompletionListener(completeListener);
//				
//				
//				mMediaPlayer.prepare();
//				prepared = true;
//
//	        } catch (Exception e) {
//	            LogTraceMin.E(e.getMessage());
//	            
//	        }
//			
//			
//
//	        return prepared;
//		}
//		
//		@Override
//		protected void onPostExecute(Boolean result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//			mMediaPlayer.start();
//            //initialStage = false;
//		}
//	}
	
}
