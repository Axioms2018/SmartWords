package kr.co.moumou.smartwords.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import kr.co.moumou.smartwords.common.ApplicationPool;
import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommCommand;
import kr.co.moumou.smartwords.communication.ConstantsCommParameter;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.util.DisplayUtil;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.util.Preferences;
import kr.co.moumou.smartwords.util.ToastUtil;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoRankData;
import kr.co.moumou.smartwords.vo.VoRankDetail;
import kr.co.moumou.smartwords.vo.VoUserInfo;

public class FragmentMyRank extends Fragment {
	
	private ActivityMywordsMain wordsMain = null;
	VoRankData voRankData;
	ArrayList<VoRankDetail> rankDetail;
	
	LinearLayout ll_title;
	LinearLayout ll_detail;
	Button bt_title;
	LinearLayout ll_rank;
	LinearLayout ll_ranktitle;
	LinearLayout ll_ranklist;
	public VoUserInfo mUserInfo;

	LinearLayout ll_myscore;
	LinearLayout ll_knowword;
	TextView tv_myname;
	TextView tv_myid;
	TextView tv_knowword;
	
	LinearLayout ll_my;
	LinearLayout ll_ouraver;
	TextView tv_ourtitle;
	TextView tv_ouraver;
	LinearLayout ll_allaver;
	TextView tv_alltitle;
	TextView tv_allaver;
	
	View rank_item;

	@Override
	public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_words_myrank, container, false);
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
		
		ll_title = (LinearLayout) getView().findViewById(R.id.ll_title);
        ll_detail = (LinearLayout) getView().findViewById(R.id.ll_detail);
        bt_title = (Button) getView().findViewById(R.id.bt_title);
        ll_rank = (LinearLayout) getView().findViewById(R.id.ll_rank);
        ll_ranktitle = (LinearLayout) getView().findViewById(R.id.ll_ranktitle);
        ll_ranklist = (LinearLayout) getView().findViewById(R.id.ll_ranklist);
        ll_myscore = (LinearLayout) getView().findViewById(R.id.ll_myscore);
        ll_knowword = (LinearLayout) getView().findViewById(R.id.ll_knowword);
        tv_myname = (TextView) getView().findViewById(R.id.tv_myname);
        tv_myid = (TextView) getView().findViewById(R.id.tv_myid);
        tv_knowword = (TextView) getView().findViewById(R.id.tv_knowword);
        ll_my = (LinearLayout) getView().findViewById(R.id.ll_my);
        ll_ouraver = (LinearLayout) getView().findViewById(R.id.ll_ouraver);
        tv_ourtitle = (TextView) getView().findViewById(R.id.tv_ourtitle);
        tv_ouraver = (TextView) getView().findViewById(R.id.tv_ouraver);
        ll_allaver = (LinearLayout) getView().findViewById(R.id.ll_allaver);
        tv_allaver = (TextView) getView().findViewById(R.id.tv_allaver);
        tv_alltitle = (TextView) getView().findViewById(R.id.tv_alltitle);
                
        DisplayUtil.setLayout(wordsMain, 182, 52, bt_title);
        DisplayUtil.setLayoutMargin(wordsMain, 30, 22, 0, 22, bt_title);
        DisplayUtil.setLayout(wordsMain, 1220, 476, ll_detail);
        DisplayUtil.setLayoutMargin(wordsMain, 30, 0, 30, 118, ll_detail);
        DisplayUtil.setLayout(wordsMain, 636, 420, ll_rank);
        DisplayUtil.setLayoutMargin(wordsMain, 28, 28, 28, 28, ll_rank);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 28, 0, 0, ll_my);
        DisplayUtil.setLayoutHeight(wordsMain, 70, ll_ranktitle);
        DisplayUtil.setLayout(wordsMain, 510, 240, ll_myscore);
        DisplayUtil.setLayoutMargin(wordsMain, 28, 10, 28, 30, ll_knowword);        
        DisplayUtil.setLayoutMargin(wordsMain, 28, 30, 0, 0, tv_myname);
        DisplayUtil.setLayoutMargin(wordsMain, 28, 18, 0, 0, tv_myid);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 0, 10, 0, tv_knowword);
        DisplayUtil.setLayout(wordsMain, 244, 152, ll_ouraver);        
        DisplayUtil.setLayoutMargin(wordsMain, 0, 22, 22, 0, ll_ouraver);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 30, 0, 22, tv_ourtitle);
        DisplayUtil.setLayout(wordsMain, 244, 152, ll_allaver);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 22, 0, 0, ll_allaver);
        DisplayUtil.setLayoutMargin(wordsMain, 0, 30, 0, 22, tv_alltitle);
        
        ll_ranklist.removeAllViews();
        
        requestData();
	}
	
	private void setRankData() {		
		for(int i = 0 ; i < rankDetail.size() ; i ++) {
			LayoutInflater inflater = wordsMain.getLayoutInflater();
			rank_item = inflater.inflate(R.layout.rank_item, null);

			LinearLayout ll_rankitem = (LinearLayout) rank_item.findViewById(R.id.ll_rankitem);
			TextView tv_detailrank = (TextView) rank_item.findViewById(R.id.tv_detailrank);
			TextView tv_detailname = (TextView) rank_item.findViewById(R.id.tv_detailname);
			TextView tv_detailword = (TextView) rank_item.findViewById(R.id.tv_detailword);

			DisplayUtil.setLayout(wordsMain, 636, 70, ll_rankitem);

			tv_detailrank.setText(rankDetail.get(i).getNUM() + "");
			tv_detailname.setText(rankDetail.get(i).getUSERID());
			tv_detailword.setText(rankDetail.get(i).getWORD_CNT() + "");
			ll_ranklist.addView(rank_item);
		}
		
		tv_myname.setText(voRankData.getUSERNAME());
		tv_myid.setText(voRankData.getUSERID());
		tv_knowword.setText(voRankData.getKNOW() + "");
		
		tv_ouraver.setText(voRankData.getCOMP_AVG() + "");
		tv_allaver.setText(voRankData.getALL_AVG() + "");		
	}
	
	private void requestData() {
		wordsMain.showLoadingProgress(getResources().getString(R.string.wait_for_data));

		String url = ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_WORDRANKING);


		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter(ConstantsCommParameter.Keys.USERID, Preferences.getPref(getContext(),Preferences.PREF_USER_ID,null));
		builder.appendQueryParameter(ConstantsCommParameter.Keys.SESSIONID, VoUserInfo.getInstance().getSID());


		AndroidNetworkRequest.getInstance(getActivity()).StringRequest(ConstantsCommURL.REQUEST_TAG_WORDRANKING, builder.toString(), new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				wordsMain.hideProgress();

				voRankData = ApplicationPool.getGson().fromJson(response, VoRankData.class);
				rankDetail = voRankData.getRANKING();
				setRankData();
			}

			@Override
			public void systemcheck(String response) {
				wordsMain.hideProgress();

			}

			@Override
			public void fail(VoBase base) {
				wordsMain.hideProgress();

			}

			@Override
			public void exception(ANError error) {
				LogUtil.i("reprot exception" + error);
				ToastUtil.show(wordsMain,"네트워크 연결 상태를 확인해주세요", Toast.LENGTH_SHORT);
				wordsMain.hideProgress();

			}

			@Override
			public void dismissDialog() {
				wordsMain.hideProgress();

			}
		});

	}
	
}
