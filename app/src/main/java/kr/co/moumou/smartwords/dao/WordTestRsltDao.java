package kr.co.moumou.smartwords.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import kr.co.moumou.smartwords.util.LogTraceMin;
import kr.co.moumou.smartwords.vo.VoWordsSaveList;
import kr.co.moumou.smartwords.vo.VoWordsSaveList.VoWordsSave;

public class WordTestRsltDao extends BaseDao {
	
	private static WordTestRsltDao wordTestRsltDao;
	
	private WordTestRsltDao(Context ctx) {
		tableName = DBHelper.Table.T_SMART_WORD_TEST;
		this.ctx = ctx;
	}

	public static WordTestRsltDao getInstance(Context ctx) {
		if(wordTestRsltDao == null)
			wordTestRsltDao = new WordTestRsltDao(ctx);
		return wordTestRsltDao;
	}
	
	/**
	 * Selecte 하고 값이 없으면 Insert 있으면 update
	 * @param info
	 */
	public void putWordTest(VoWordsSave info) {
		
		StringBuffer where = new StringBuffer();
		where.append(DaoColumns.Columns.USERID + " = ? and ");
		where.append(DaoColumns.Columns.STD_W_GB + " = ? and ");
		where.append(DaoColumns.Columns.STD_GB + " = ? and ");
		where.append(DaoColumns.Columns.STD_LEVEL + " = ? and ");
		where.append(DaoColumns.Columns.STD_DAY + " = ? and ");
		where.append(DaoColumns.Columns.WORD_SEQ + " = ? and ");
		where.append(DaoColumns.Columns.STD_STEP + " = ? and ");
		where.append(DaoColumns.Columns.STD_TYPE + " = ?");
		
		String[] whereValue = new String[] {
			info.getUSERID(), info.getSTD_W_GB(), info.getSTD_GB(), info.getSTD_LEVEL(), String.valueOf(info.getSTD_DAY()),
			String.valueOf(info.getWORD_SEQ()), info.getSTD_STEP(), info.getSTD_TYPE()
		};
		SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
		
		cursor = db.query(tableName, 
				null, 
				where.toString(), 
				whereValue, 
				null, 
				null, 
				null);
		
		if(cursor.getCount() > 0) updateWordTest(info);
		else insertWordTest(info);
	}
	
	public void insertWordTest(VoWordsSave info) {
		
		LogTraceMin.D("insertWordTest()");
		SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
		
		ContentValues values = new ContentValues();
		values.put(DaoColumns.Columns.USERID, info.getUSERID());
		values.put(DaoColumns.Columns.STD_GB, info.getSTD_GB());
		values.put(DaoColumns.Columns.STD_W_GB, info.getSTD_W_GB());
		values.put(DaoColumns.Columns.STD_LEVEL, info.getSTD_LEVEL());
		values.put(DaoColumns.Columns.STD_DAY, info.getSTD_DAY());
		values.put(DaoColumns.Columns.WORD_SEQ, info.getWORD_SEQ());
		values.put(DaoColumns.Columns.STD_NUM, info.getSTD_NUM());
		values.put(DaoColumns.Columns.USER_ANS, info.getUSER_ANS());
		values.put(DaoColumns.Columns.ANSWER, info.getANSWER());
		values.put(DaoColumns.Columns.RESULT_YN, info.getRESULT_YN());
		values.put(DaoColumns.Columns.STD_STEP, info.getSTD_STEP());
		values.put(DaoColumns.Columns.STD_TIME, info.getSTD_TIME());
		values.put(DaoColumns.Columns.STD_TYPE, info.getSTD_TYPE());
		
		db.insert(tableName, null, values);
	}
	
	public void updateWordTest(VoWordsSave info) {
		
		LogTraceMin.D("updateWordTest()");
		SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
		
		ContentValues values = new ContentValues();
		values.put(DaoColumns.Columns.USER_ANS, info.getUSER_ANS());
		values.put(DaoColumns.Columns.RESULT_YN, info.getRESULT_YN());
		values.put(DaoColumns.Columns.STD_TIME, info.getSTD_TIME());
		
		StringBuffer where = new StringBuffer();
		where.append(DaoColumns.Columns.USERID + " = ? and ");
		where.append(DaoColumns.Columns.STD_W_GB + " = ? and ");
		where.append(DaoColumns.Columns.STD_GB + " = ? and ");
		where.append(DaoColumns.Columns.STD_LEVEL + " = ? and ");
		where.append(DaoColumns.Columns.STD_DAY + " = ? and ");
		where.append(DaoColumns.Columns.WORD_SEQ + " = ? and ");
		where.append(DaoColumns.Columns.STD_STEP + " = ? and ");
		where.append(DaoColumns.Columns.STD_TYPE + " = ?");
		
		String[] whereValue = new String[] {
			info.getUSERID(), info.getSTD_GB(), info.getSTD_LEVEL(), String.valueOf(info.getSTD_DAY()),
			String.valueOf(info.getWORD_SEQ()), info.getSTD_STEP(), info.getSTD_TYPE()
		};
		
		db.update(tableName, values, where.toString(), whereValue);
	}
	
	public void deleteAllWordTest() {
		
		LogTraceMin.D("deleteAllWordTest()");
		SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
		db.delete(tableName, null, null);
	}
	
	/**
	 * 모든 VO데이터를 내부 DB에 저장한다.
	 */
	public void putInnerDBFromVoData() {
		for(VoWordsSave wordsSave : VoWordsSaveList.getInstance().getSAVE_WORDS_LIST()) {
			putWordTest(wordsSave);
		}
	}
	
	/**
	 * 모든 DB데이터를 Vo데이터에 넣는다.
	 */
	public void putVoDataFromInnerDB() {
		
		LogTraceMin.D("putWordInfoFromInnerDB()");
		
		SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
		
		cursor = db.query(tableName, 
							null, 
							null, 
							null,
							null, 
							null, 
							null);
		
		if(cursor.moveToFirst()) {
			
			VoWordsSaveList.getInstance().deleteSAVE_WORDS_LIST();	//기존에 저장된 데이터 삭제
			
			do {
				VoWordsSave voWordsSave = VoWordsSaveList.getInstance().new VoWordsSave();
				
				voWordsSave.setUSERID(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.USERID)));
				voWordsSave.setSTD_W_GB(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.STD_W_GB)));
				voWordsSave.setSTD_GB(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.STD_GB)));
				voWordsSave.setSTD_LEVEL(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.STD_LEVEL)));
				voWordsSave.setSTD_DAY(cursor.getInt(cursor.getColumnIndex(DaoColumns.Columns.STD_DAY)));
				voWordsSave.setWORD_SEQ(cursor.getInt(cursor.getColumnIndex(DaoColumns.Columns.WORD_SEQ)));
				voWordsSave.setSTD_NUM(cursor.getInt(cursor.getColumnIndex(DaoColumns.Columns.STD_NUM)));
				voWordsSave.setUSER_ANS(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.USER_ANS)));
				voWordsSave.setANSWER(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.ANSWER)));
				voWordsSave.setRESULT_YN(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.RESULT_YN)));
				voWordsSave.setSTD_STEP(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.STD_STEP)));
				voWordsSave.setSTD_TIME(cursor.getInt(cursor.getColumnIndex(DaoColumns.Columns.STD_TIME)));
				voWordsSave.setSTD_TYPE(cursor.getString(cursor.getColumnIndex(DaoColumns.Columns.STD_TYPE)));
				
				VoWordsSaveList.getInstance().putSAVE_WORDS_LIST(voWordsSave);
			}while(cursor.moveToNext());
		}
	}
	
	
}


