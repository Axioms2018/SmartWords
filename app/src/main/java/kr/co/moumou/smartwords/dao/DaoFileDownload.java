package kr.co.moumou.smartwords.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.moumou.smartwords.dao.DBHelper.Table;
import kr.co.moumou.smartwords.file.FileController;

public class DaoFileDownload extends BaseDao{

	private volatile static DaoFileDownload instance;

	protected class Columns {
		protected static final String C_NAME_V = "name";
		protected static final String C_FLIE_COUNT_I = "file_count";
		protected static final String C_HIT_COUNT_I = "hit_count";
		protected static final String C_FILE_CREATE_DATE_L = "create_date";
		protected static final String C_HIT_DATE_L = "hit_date";
	}


	public static DaoFileDownload getInstance(Context context) {
		if (instance == null) {
			synchronized (DaoFileDownload.class) {
				if (instance == null) {
					instance = new DaoFileDownload(context);
				}
			}
		}
		return instance;
	}

	protected DaoFileDownload(Context context) {
		this.ctx = context;
		tableName = Table.T_DOWNLOAD_FILES;
	}

	public void insertDownloadBookInfo(String name, long createDate){

		ContentValues values = new ContentValues();
		values.put(Columns.C_NAME_V, FileController.getFileName(name));
		values.put(Columns.C_FILE_CREATE_DATE_L, createDate);
		values.put(Columns.C_HIT_DATE_L, Calendar.getInstance().getTimeInMillis());
		DBHelper.getInstance(ctx).insertWithOnConflict(Table.T_DOWNLOAD_FILES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public boolean isExistBook(String name, long createDate){
		Cursor cursor = DBHelper.getReadableInstance(ctx).query(
				Table.T_DOWNLOAD_FILES, new String[] { Columns.C_NAME_V, Columns.C_FILE_CREATE_DATE_L, Columns.C_HIT_COUNT_I}, 
				Columns.C_NAME_V + "=? and " + Columns.C_FILE_CREATE_DATE_L + "=?", 
				new String[] { FileController.getFileName(name), String.valueOf(createDate) }, null, null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			int count = cursor.getInt(cursor.getColumnIndex(Columns.C_HIT_COUNT_I)); 
			ContentValues values = new ContentValues();
			values.put(Columns.C_HIT_COUNT_I, ++count);
			values.put(Columns.C_HIT_DATE_L, Calendar.getInstance().getTimeInMillis());
			
			DBHelper.getInstance(ctx).update(Table.T_DOWNLOAD_FILES, values, Columns.C_NAME_V + "=?" , new String[]{name});
			cursor.close();
			return true;
		}

		cursor.close();
		return false;
	}
	
	public void insertDownloadFileCount(String name, int fileCount){
		ContentValues values = new ContentValues();
		values.put(Columns.C_HIT_DATE_L, Calendar.getInstance().getTimeInMillis());
		values.put(Columns.C_FLIE_COUNT_I, fileCount);
		
		DBHelper.getInstance(ctx).update(Table.T_DOWNLOAD_FILES, values, Columns.C_NAME_V + "=?", new String[]{FileController.getFileName(name)});
	}
	
	public int getBookFileCount(String fileName) {
		int result = 0;
		
		Cursor cursor =  DBHelper.getReadableInstance(ctx)
				.query(Table.T_DOWNLOAD_FILES, new String[] { Columns.C_FLIE_COUNT_I }, 
						Columns.C_NAME_V + "=?", new String[] { FileController.getFileName(fileName) }, null, null, null);

		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			result = cursor.getInt(cursor.getColumnIndex(Columns.C_FLIE_COUNT_I));
		}
		cursor.close();

		return result;
	}

	public ArrayList<String> getDownloadFlieList(){
		ArrayList<String> result = new ArrayList<String>();
		Cursor cursor =  DBHelper.getReadableInstance(ctx)
				.query(Table.T_DOWNLOAD_FILES, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(cursor.getString(cursor.getColumnIndex(Columns.C_NAME_V)));
			cursor.moveToNext();
		}
		cursor.close();
		return result;
	}

	public void removeAllDownloadHistory(){
		SQLiteDatabase db =  DBHelper.getInstance(ctx);
		db.delete(Table.T_DOWNLOAD_FILES, null, null);
	}
	public void removeDownloadHistory(String name){
		if(name == null){
			return;
		}
		SQLiteDatabase db =  DBHelper.getInstance(ctx);
		db.delete(Table.T_DOWNLOAD_FILES, Columns.C_NAME_V + " = ? ", new String[]{name});
	}

}
