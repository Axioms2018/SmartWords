package kr.co.moumou.smartwords.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import kr.co.moumou.smartwords.util.LogUtil;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "smartmm.db";
	private static final int DB_VERSION = 14;

	static DBHelper instance;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public static SQLiteDatabase getInstance(Context ctx) {
		if (instance == null)
			instance = new DBHelper(ctx);
		return instance.getWritableDatabase();
	}
	
	public static SQLiteDatabase getReadableInstance(Context ctx)
	{
		if(instance == null)
			instance = new DBHelper(ctx);
		return instance.getReadableDatabase();
	}

	public class Table {
		public static final String T_DOWNLOAD_FILES = "download_files";
		public static final String T_SMART_STDY_PGSS = "TB_SMART_STDY_PGSS";
		public static final String T_SMART_STDY_PGSS_DTL = "TB_SMART_STDY_PGSS_DTL";
		public static final String T_SMART_STDY_RSLT = "TB_SMART_STDY_RSLT";
		public static final String T_SMART_STDY_SYNC_RSLT = "T_SMART_STDY_SYNC_RSLT";
		public static final String T_SMART_STUDY_HISTORY = "T_SMART_STUDY_HISTORY";
		public static final String T_SMART_MONTHLY_TEST = "T_SMART_MONTHLY_TEST";
		public static final String T_SMART_STDY_WC_RESULT = "T_SMART_STDY_WC_RESULT";
		public static final String T_SMART_WORD_TEST = "T_SMART_WORD_TEST";
		public static final String T_SMART_WORD_DOWNLOAD = "T_SMART_WORD_DOWNLOAD";
		public static final String T_SMART_CLASS_RULE = "T_SMART_CLASS_RULE";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.d("onCreate");

		db.execSQL(createFileDownloadTable());
		db.execSQL(crateSmartStdyRsltTable());
		db.execSQL(crateSmartStdyStdyPgss());
		db.execSQL(crateSmartStdyStdyPgssDtl());
		db.execSQL(crateMonthlyTestTable());
		db.execSQL(createWordTestTable());

		db.execSQL(createStudyHistory());
		db.execSQL(crateSmartStdyWCResult());
		db.execSQL(createWordTestDownloadTable());
		db.execSQL(createClassRuleTable());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		LogUtil.d("onUpgrade old " + oldVersion + " / " + newVersion);
		Log.i("dsu", "onUpgrade old " + oldVersion + " / " + newVersion);
		
//		if (oldVersion == 1 || oldVersion == 2) {
//			db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_STDY_RSLT);
//			db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_STDY_PGSS);
//			db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_STDY_PGSS_DTL);
//			
//			db.execSQL(crateSmartStdyRsltTable());
//			db.execSQL(crateSmartStdyStdyPgss());
//			db.execSQL(crateSmartStdyStdyPgssDtl());
//			
//			db.execSQL(crateMonthlyTestTable());
//			db.execSQL(crateSmartStdyWCResult());
//		} else if (oldVersion == 3) {
//			db.execSQL(crateSmartStdyWCResult());
//		} else if (oldVersion == 4 || oldVersion == 5 || oldVersion == 6) {
//			db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_STDY_WC_RESULT);
//			db.execSQL(crateSmartStdyWCResult());
//		} else if(oldVersion == 7) {
//			db.execSQL(createWordTestTable());
//		}else if(oldVersion == 8){
//			db.execSQL(createWordTestDownloadTable());
//		}
		
//		switch(oldVersion){
//			case 1:
//			case 2:
//				db.execSQL(crateSmartStdyRsltTable());
//				db.execSQL(crateSmartStdyStdyPgss());
//				db.execSQL(crateSmartStdyStdyPgssDtl());
//
//				db.execSQL(crateMonthlyTestTable());
//				db.execSQL(crateSmartStdyWCResult());
//			case 3:
//				db.execSQL(crateSmartStdyWCResult());
//			case 4:
//			case 5:
//			case 6:
//				db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_STDY_WC_RESULT);
//				db.execSQL(crateSmartStdyWCResult());
//			case 7:
//				db.execSQL(createWordTestTable());
//			case 8:
//				db.execSQL(createWordTestDownloadTable());
//			case 9:
//				db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_STDY_RSLT);
//				db.execSQL(crateSmartStdyRsltTable());
//			case 10:
//				db.execSQL(createWordTestTable());
//			case 11:
//				db.execSQL(createWordTestDownloadTable());
//			case 12:
//				db.execSQL(createClassRuleTable());
//			case 13:
//				db.execSQL("DROP TABLE IF EXISTS " + Table.T_SMART_WORD_TEST);
//				db.execSQL(createWordTestTable());
//
//		}
	}
	
	
	
	private String crateSmartStdyStdyPgss(){
		StringBuffer query = new StringBuffer();

		query.append("Create table IF NOT EXISTS " + Table.T_SMART_STDY_PGSS + " ( ");
		query.append(DaoColumns.Columns.USERID 			+ " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.PCODE 			+ " VARCHAR (8)    NOT NULL , ");
		query.append(DaoColumns.Columns.CON_SEQ 			+ " VARCHAR (15)   DEFAULT '0', ");
		query.append(DaoColumns.Columns.CHACI 			+ " VARCHAR (2)				, ");
		query.append(DaoColumns.Columns.STDY_GUBN 		+ " VARCHAR (4)    NOT NULL , ");
		query.append(DaoColumns.Columns.STDY_GUBN_SEQ 	+ " INTEGER        NOT NULL , ");
		query.append(DaoColumns.Columns.COMPCODE 			+ " VARCHAR (8)				, ");
		query.append(DaoColumns.Columns.MEMNO 			+ " VARCHAR (8)				, ");
		query.append(DaoColumns.Columns.MEMNAME           + " VARCHAR (50)			, ");
		query.append(DaoColumns.Columns.PNAME             + " VARCHAR (50)			, ");
		query.append(DaoColumns.Columns.TEACHERID         + " VARCHAR (20)			, ");
		query.append(DaoColumns.Columns.TEACHERNAME       + " VARCHAR (50)			, ");
		query.append(DaoColumns.Columns.GRADE             + " VARCHAR (10)			, ");
		query.append(DaoColumns.Columns.STRT_TM           + " DATE		        	, ");
		query.append(DaoColumns.Columns.END_TM            + " DATE		        	, ");
		query.append(DaoColumns.Columns.SRVR_SEND_YN		+ " VARCHAR (1) DEFAULT	 'N'	, ");
		query.append(DaoColumns.Columns.SRVR_SEND_TM		+ " DATE					, ");
		query.append(DaoColumns.Columns.COMP_INOUT_GUBN	+ " VARCHAR(4)				, ");
		query.append(DaoColumns.Columns.VRSN				+ " INTEGER					, ");
		query.append(DaoColumns.Columns.CLOS_YN			+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.STDY_TYPE			+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.CNFM_YN			+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.REG_DT			+ " DATE					, ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	PCODE,");
		query.append("	CHACI,");
		query.append("	STDY_GUBN,");
		query.append("	STDY_GUBN_SEQ )");
		query.append(" ); ");
		
		return query.toString();
	}

	private String crateSmartStdyStdyPgssDtl(){
		StringBuffer query = new StringBuffer();

		query.append("Create table IF NOT EXISTS " + Table.T_SMART_STDY_PGSS_DTL + " ( ");
		query.append(DaoColumns.Columns.USERID 			+ " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.PCODE 			+ " VARCHAR (8)    NOT NULL , ");
		query.append(DaoColumns.Columns.CON_SEQ 			+ " VARCHAR (15)   DEFAULT '0', ");
		query.append(DaoColumns.Columns.CHACI 			+ " VARCHAR (2)				, ");
		query.append(DaoColumns.Columns.STDY_GUBN 		+ " VARCHAR (4)    NOT NULL , ");
		query.append(DaoColumns.Columns.STDY_GUBN_SEQ 	+ " INTEGER        NOT NULL , ");
		query.append(DaoColumns.Columns.STDY_STGE 		+ " VARCHAR (4)			, ");
		query.append(DaoColumns.Columns.STDY_STGE_NM 		+ " VARCHAR (20)			, ");
		query.append(DaoColumns.Columns.STDY_PGSS_STAT    + " VARCHAR (4)				, ");
		query.append(DaoColumns.Columns.STDY_PGSS_STAT_NM + " VARCHAR (20)			, ");
		query.append(DaoColumns.Columns.SRVR_SEND_YN		+ " VARCHAR (1)	DEFAULT  'N'	, ");
		query.append(DaoColumns.Columns.SRVR_SEND_TM		+ " DATE					, ");
		query.append(DaoColumns.Columns.STRT_TM           + " DATE		        	, ");
		query.append(DaoColumns.Columns.END_TM            + " DATE		        	, ");
		query.append(DaoColumns.Columns.VRSN				+ " INTEGER					, ");
		query.append(DaoColumns.Columns.RUN_TM			+ " INTEGER					, ");
		query.append(DaoColumns.Columns.ACT_AVG_DEC		+ " INTEGER					, ");
		query.append(DaoColumns.Columns.REG_DT			+ " DATE					, ");
		query.append(DaoColumns.Columns.STDY_TYPE			+ " VARCAHR(1)					, ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	PCODE,");
		query.append("	CHACI,");
		query.append("	STDY_GUBN,");
		query.append("	STDY_GUBN_SEQ, ");
		query.append("	STDY_STGE )");
		query.append(" ); ");
		
		return query.toString();

	}
	
	private String crateMonthlyTestTable(){
		StringBuffer query = new StringBuffer();
		
		query.append("Create table IF NOT EXISTS " + Table.T_SMART_MONTHLY_TEST + " ( ");
		query.append(DaoColumns.Columns.USERID 			+ " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.PCODE 			+ " VARCHAR (8)    NOT NULL , ");
		query.append(DaoColumns.Columns.COMPCODE 		+ " VARCHAR (8)    			, ");
		query.append(DaoColumns.Columns.MEMNO 			+ " VARCHAR (8)    			, ");
		query.append(DaoColumns.Columns.QUST_SEQ		+ " VARCHAR (2)    NOT NULL	, ");
		query.append(DaoColumns.Columns.EVAL_GUBN		+ " VARCHAR (1)	   NOT NULL	, ");
		query.append(DaoColumns.Columns.ANSW_USER		+ " VARCHAR (1000)			, ");
		query.append(DaoColumns.Columns.ANSW_RSLT		+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.RUN_TM			+ " INTEGER					, ");
		query.append(DaoColumns.Columns.END_YN			+ " VARCHAR (1)				, ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	PCODE,");
		query.append("	QUST_SEQ )");
		query.append(" ); ");
		
		return query.toString();
		
	}
	
	private String createWordTestTable() {
		StringBuffer query = new StringBuffer();
		
		query.append("Create table IF NOT EXISTS " + Table.T_SMART_WORD_TEST + " ( ");
		query.append(DaoColumns.Columns.USERID 			+ " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.STD_W_GB 		+ " VARCHAR (1)    NOT NULL , ");
		query.append(DaoColumns.Columns.STD_GB 			+ " VARCHAR (1)    NOT NULL , ");
		query.append(DaoColumns.Columns.STD_LEVEL 		+ " VARCHAR (1)    NOT NULL , ");
		query.append(DaoColumns.Columns.STD_DAY 		+ " INTEGER        NOT NULL , ");
		query.append(DaoColumns.Columns.WORD_SEQ		+ " INTEGER		   NOT NULL , ");
		query.append(DaoColumns.Columns.STD_NUM			+ " INTEGER					, ");
		query.append(DaoColumns.Columns.USER_ANS		+ " VARCHAR (30)			, ");
		query.append(DaoColumns.Columns.ANSWER			+ " VARCHAR (30)			, ");
		query.append(DaoColumns.Columns.RESULT_YN		+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.STD_STEP		+ " VARCHAR (6)	   NOT NULL , ");
		query.append(DaoColumns.Columns.STD_TIME		+ " INTEGER                 , ");
		query.append(DaoColumns.Columns.STD_TYPE		+ " VARCHAR (6)    NOT NULL , ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	STD_W_GB,");
		query.append("	STD_GB,");
		query.append("	STD_LEVEL,");
		query.append("	STD_DAY,");
		query.append("	WORD_SEQ,");
		query.append("	STD_STEP,");
		query.append("	STD_TYPE )");
		query.append(" ); ");
		
		return query.toString();
	}
	
	private String createWordTestDownloadTable() {
		StringBuffer query = new StringBuffer();
		
		query.append("Create table IF NOT EXISTS " + Table.T_SMART_WORD_DOWNLOAD + " ( ");
		query.append(DaoColumns.Columns.DOWNLOAD_NM	+ " VARCHAR (30) PRIMARY KEY ");
//		query.append(DaoColumns.Columns.DOWNLOAD_TM + " VARCHAR (20) NOT NULL ");
		query.append(" ); ");
		
		return query.toString();
	}
	
	private String createClassRuleTable() {
		StringBuffer query = new StringBuffer();
		query.append("Create table IF NOT EXISTS " + Table.T_SMART_CLASS_RULE + " ( ");
		query.append(DaoColumns.Columns.CR_USERID + " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.CR_POPUP_CHECK + " VARCHAR(5), ");
		query.append(DaoColumns.Columns.CR_DATE_CEHCK + " VARCHAR(5)");
		query.append(" ); ");

		return query.toString();
	}
	
	private String createStudyHistory(){
		StringBuffer query = new StringBuffer();
		query.append("Create table IF NOT EXISTS " + Table.T_SMART_STUDY_HISTORY + " ( ");
		query.append(DaoColumns.Columns.USERID + " VARCHAR(30), ");
		query.append(DaoColumns.Columns.PCODE + " VARCHAR(15), ");
		query.append(DaoColumns.Columns.CON_SEQ 			+ " VARCHAR (15)   DEFAULT '0', ");
		query.append(DaoColumns.Columns.CHACI + " VARCHAR(5), ");
		query.append(DaoColumns.Columns.STDY_GUBN 		+ " VARCHAR (4)    NOT NULL , ");
		query.append(DaoColumns.Columns.STDY_GUBN_SEQ 	+ " INTEGER        NOT NULL , ");
		query.append(DaoColumns.Columns.SYNC_VRESION + " UNSIGNED SMALL INT, ");
		query.append(DaoColumns.Columns.QUST_LIST + " Text, ");
		query.append(DaoColumns.Columns.SYNC_DATE + " DATETIME, ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	PCODE,");
		query.append("	STDY_GUBN,");
		query.append("	STDY_GUBN_SEQ,");
		query.append("	CHACI)");
		query.append(" ); ");
		
		return query.toString();
	}
	
	private String createFileDownloadTable(){
		
		StringBuffer query = new StringBuffer();
		
		query.append("Create table IF NOT EXISTS " + Table.T_DOWNLOAD_FILES + " ( ");
		query.append(DaoFileDownload.Columns.C_NAME_V + " VARCHAR2(100) PRIMARY KEY, ");
		query.append(DaoFileDownload.Columns.C_FLIE_COUNT_I + " UNSIGNED SAMLL INT, ");
		query.append(DaoFileDownload.Columns.C_HIT_COUNT_I + " UNSIGNED SAMLL INT, ");
		query.append(DaoFileDownload.Columns.C_HIT_DATE_L + " UNSIGNED BIG INT, ");
		query.append(DaoFileDownload.Columns.C_FILE_CREATE_DATE_L + " UNSIGNED BIG INT ");
		query.append(" ); ");
		
		return query.toString();
	}
	
	public String crateSmartStdyRsltTable() {
		StringBuffer query = new StringBuffer();

		query.append("Create table IF NOT EXISTS " + Table.T_SMART_STDY_RSLT + " ( ");
		query.append(DaoColumns.Columns.USERID 			+ " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.PCODE 			+ " VARCHAR (8)    NOT NULL , ");
		query.append(DaoColumns.Columns.CON_SEQ 			+ " VARCHAR (15)   DEFAULT '0', ");
		query.append(DaoColumns.Columns.QUST_SEQ		 	+ " INTEGER		   NOT NULL , ");
		query.append(DaoColumns.Columns.STDY_GUBN 		+ " VARCHAR (4)    NOT NULL , ");
		query.append(DaoColumns.Columns.STDY_GUBN_SEQ 	+ " INTEGER        NOT NULL , ");
		query.append(DaoColumns.Columns.COMPCODE 			+ " VARCHAR (8)				, ");
		query.append(DaoColumns.Columns.TMPT_CD 			+ " VARCHAR (20)				, ");
		query.append(DaoColumns.Columns.MEMNO 			+ " VARCHAR (8)				, ");
		query.append(DaoColumns.Columns.STDY_DT 			+ " DATE 					, ");
		query.append(DaoColumns.Columns.STDY_MM 			+ " VARCHAR (6)				, ");
		query.append(DaoColumns.Columns.STDY_YYYY 		+ " VARCHAR (4)				, ");
		query.append(DaoColumns.Columns.STDY_STGE			+ " VARCHAR (4)				, ");
		query.append(DaoColumns.Columns.CHACI 			+ " VARCHAR (2)				, ");
		query.append(DaoColumns.Columns.COMP_INOUT_GUBN	+ " VARCHAR (4)				, ");
		query.append(DaoColumns.Columns.MEM_ANSW			+ " VARCHAR (1000)			, ");
		query.append(DaoColumns.Columns.ANSW_RSLT			+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.RUN_TM 			+ " NUMBER					, ");
		query.append(DaoColumns.Columns.RETY_CNT 			+ " INTEGER					, ");
		query.append(DaoColumns.Columns.VRSN 				+ " INTEGER					, ");
		query.append(DaoColumns.Columns.ANSW_GUBN			+ " VARCHAR (1)				, ");
		query.append(DaoColumns.Columns.ACT_SPE_CNT 		+ " INTEGER					, ");
		query.append(DaoColumns.Columns.ACT_AVG_DEC 		+ " INTEGER					, ");
		query.append(DaoColumns.Columns.ACT_SPE_PASS_CNT	+ " INTEGER					, ");
		query.append(DaoColumns.Columns.SCOR				+ " INTEGER					, ");
		query.append(DaoColumns.Columns.REG_DT 			+ " DATE 					, ");
		query.append(DaoColumns.Columns.SRVR_SEND_YN		+ " VARCHAR (1) DEFAULT 'N' , ");
		query.append(DaoColumns.Columns.SRVR_SEND_TM 		+ " DATE 					, ");
		query.append(DaoColumns.Columns.STDY_TYPE			+ " VARCHAR (1)				, ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	PCODE,");
		query.append("	QUST_SEQ,");
		query.append("	STDY_GUBN,");
		query.append("	STDY_GUBN_SEQ )");
		query.append(" ); ");
		
		return query.toString();
	}

	public String crateSmartStdyWCResult() {
		StringBuffer query = new StringBuffer();

		query.append("Create table IF NOT EXISTS " + Table.T_SMART_STDY_WC_RESULT + " ( ");
		query.append(DaoColumns.Columns.USERID 			+ " VARCHAR (20)   NOT NULL , ");
		query.append(DaoColumns.Columns.PCODE 			+ " VARCHAR (8)    NOT NULL , ");
		query.append(DaoColumns.Columns.WCCODE 			+ " VARCHAR (15)   NOT NULL , ");
		query.append(DaoColumns.Columns.ESSAYCODE		+ " VARCHAR	(15)   NOT NULL , ");
		query.append(DaoColumns.Columns.ESSAYSTEP 		+ " VARCHAR (15)   			, ");
		query.append(DaoColumns.Columns.ESSAYSTEP_NAME 	+ " VARCHAR (15)   	        , ");
		query.append(DaoColumns.Columns.BRAINSTORMING 	+ " VARCHAR (500)           , ");
		query.append(DaoColumns.Columns.DRAFTING 		+ " VARCHAR (500)           , ");
		query.append(DaoColumns.Columns.WRITING 		+ " VARCHAR (500)           , ");
		query.append(DaoColumns.Columns.REVISING 		+ " VARCHAR (500)           , ");
		query.append(DaoColumns.Columns.REVISING_CHK 	+ " VARCHAR (50)            , ");
		query.append(DaoColumns.Columns.EDITING			+ " VARCHAR (500)           , ");
		query.append(DaoColumns.Columns.EDITING_CHK 	+ " VARCHAR (50)            , ");
		query.append(DaoColumns.Columns.EVAL_CHK		+ " VARCHAR (50)            , ");
		query.append(DaoColumns.Columns.EVAL_SCORE		+ " VARCHAR (10)            , ");
		query.append("PRIMARY KEY (");
		query.append("	USERID,");
		query.append("	PCODE,");
		query.append("	WCCODE )");
		query.append(" ); ");
		
		return query.toString();
	}
	
}
