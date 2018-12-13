package kr.co.moumou.smartwords.activity;

import android.content.Context;
import android.os.Environment;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import kr.co.moumou.smartwords.communication.AndroidNetworkRequest;
import kr.co.moumou.smartwords.communication.ConstantsCommURL;
import kr.co.moumou.smartwords.dao.WordTestRsltDao;
import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.vo.VoBase;
import kr.co.moumou.smartwords.vo.VoMyInfo;
import kr.co.moumou.smartwords.vo.VoUserInfo;
import kr.co.moumou.smartwords.vo.VoWordsSaveList;
import kr.co.moumou.smartwords.common.ApplicationPool;

public class SaveWords {

	private Context mContext;
	public VoUserInfo mUserInfo;

	public SaveWords(Context context) {
		this.mContext = context;
	}
	
	public void saveReqInnerDB() {
		
		WordTestRsltDao.getInstance(mContext).putVoDataFromInnerDB();//모든 InnerDB > VoWordsSaveList 로 담는다.
		
		if(VoWordsSaveList.getInstance().getSAVE_WORDS_LIST().size() == 0) {
			if(null != saveWordsComplete) saveWordsComplete.success();
			return;
		}
		String json = new Gson().toJson(VoWordsSaveList.getInstance().getSAVE_WORDS_LIST());
		LogTraceMin.I("SaveWords 저장할 DATA JSON :: " + json);

//		VoWordsSaveList.VoWordsSave voWordSave = VoWordsSaveList.getInstance().getSAVE_WORDS_LIST().get(0);
//		saveTxt("Lv" + voWordSave.getSTD_LEVEL() + "_Day" + voWordSave.getSTD_DAY(), json);
		
		reqSaveWords();
	}
	
	public void setSaveWordsComplete(SaveWordsComplete listener) {
		this.saveWordsComplete = listener;
	}
	
	private void reqSaveWords() {

		String jsonData = new ApplicationPool().getGson().toJson(VoWordsSaveList.getInstance().getSAVE_WORDS_LIST());
		WordTestRsltDao.getInstance(mContext).deleteAllWordTest();	//InnerDB에 있는 WordTest전부 삭제

		JSONArray jsonArray = null;
		try{
			jsonArray = new JSONArray(jsonData);
		}catch (Exception e){
			LogUtil.e(e.toString());
		}

		AndroidNetworkRequest.getInstance(mContext).postJSONArrayRequest(ConstantsCommURL.REQUEST_TAG_SETSTDINFO, ConstantsCommURL.getUrl(ConstantsCommURL.REQUEST_GET_SETSTDINFO), jsonArray, new AndroidNetworkRequest.ListenerAndroidResponse() {
			@Override
			public void success(String response) {
				LogTraceMin.D("onResponse " + response);

				//VoWordSaveList 모두 삭제
				VoWordsSaveList.getInstance().deleteSAVE_WORDS_LIST();
				if(null != saveWordsComplete) saveWordsComplete.success();	//전송 완료후 해야할 액션이 있을 경우
			}

			@Override
			public void systemcheck(String response) {

			}

			@Override
			public void fail(VoBase base) {
				LogTraceMin.D("onResponseFail :: " + base);
				WordTestRsltDao.getInstance(mContext).putInnerDBFromVoData();	//실패시 모든 VO데이터를 InnerDB에 다시 담는다.

				VoWordsSaveList.getInstance().deleteSAVE_WORDS_LIST();	//성공해도 실패해도 VoWordsSaveList에 든 모든 데이터는 삭제한다.
				if(null != saveWordsComplete) saveWordsComplete.fail();
			}

			@Override
			public void exception(ANError error) {
				LogTraceMin.D("exception :: " + error.toString());
				WordTestRsltDao.getInstance(mContext).putInnerDBFromVoData();	//실패시 모든 VO데이터를 InnerDB에 다시 담는다.
				VoWordsSaveList.getInstance().deleteSAVE_WORDS_LIST();
				if(null != saveWordsComplete) saveWordsComplete.exception();
			}

			@Override
			public void dismissDialog() {

			}

		});
	}
	
	private SaveWordsComplete saveWordsComplete = null;
	
	public interface SaveWordsComplete {
		void success();
		void fail();
		void exception();
	}
	
	

	/**
	 * 테스트 파일 저장
	 */
	private void saveTxt(String title, String txt) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String currentDate = sdf.format(new Date());
		
		String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LOG/";
		String fileName = dirPath + VoUserInfo.getInstance().getSID() + "_" + title + "_" + currentDate +".txt";
		
		File file = new File(dirPath);
		
		LogTraceMin.I("fileName :: " + fileName);
		
		if(!file.exists()) file.mkdirs();

		try {	
			
			FileOutputStream fos = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(txt);
			bw.flush();
			osw.close();
			fos.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
