package kr.co.moumou.smartwords.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kr.co.moumou.smartwords.dao.DBHelper.Table;
import kr.co.moumou.smartwords.util.LogTraceMin;

public class WordTestDownloadDao extends BaseDao {
	
	private volatile static WordTestDownloadDao instance;
	
	private WordTestDownloadDao(Context context) {
		this.ctx = context;
		tableName = Table.T_SMART_WORD_DOWNLOAD;
	}
	
	public static WordTestDownloadDao getInstance(Context context) {
		if(instance == null) {
			synchronized (WordTestDownloadDao.class) {
				if(instance == null) {
					instance = new WordTestDownloadDao(context);
				}
			}
		}
		return instance;
	}
	
	public void insertWordTestDownload(String name) {
		LogTraceMin.I("insertWordTestDownload : " + name);
		ContentValues values = new ContentValues();
		values.put(DaoColumns.Columns.DOWNLOAD_NM, name);
//		values.put(DaoColumns.Columns.DOWNLOAD_TM, createDate);
		DBHelper.getInstance(ctx).insertWithOnConflict(Table.T_SMART_WORD_DOWNLOAD, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public boolean isExistWords(String name) {
		Cursor cursor = DBHelper.getReadableInstance(ctx).query(
				Table.T_SMART_WORD_DOWNLOAD, 
				new String[]{DaoColumns.Columns.DOWNLOAD_NM},
				DaoColumns.Columns.DOWNLOAD_NM + "=? ",
				new String[]{name}, null, null, null);
		
		if(cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}
	
	public void deleteAllWordTestDownload() {
		LogTraceMin.D("deleteAllWordTestDownload()");
		DBHelper.getInstance(ctx).delete(tableName, null, null);
	}
}

