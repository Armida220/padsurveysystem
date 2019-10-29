package com.padsurveysystem;

import java.text.SimpleDateFormat;
import java.util.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.*;

//���ݿ������
public class MyDataBaseAdapter {
	// ��������
	private static String mProjectName;
	// ����Context����
	private static Context mContext = null;
	// ���ݿ�����Ϊdata
	private static final String DB_NAME = "PADSURVEY.db";
	// ���ݿ�汾
	private static final int DB_VERSION = 1;

	// ���ݿ����
	public static String AppInformationTable;// Ӧ�ó�����Ϣ��
	public static String ProjectInformationTable;// ������Ϣ��
	public static String PointTable;// ���ݵ��
	public static String CoordinateSystemTable;// ����ϵͳ��
	public static String CoordinatetransTormationTable;// ����ת����

	// �ֶ�ID,ͨ���ֶ�
	public static final String KEY_ID = "ID";

	// �����ǹ�����Ϣ���ֶ�
	// �ֶι��̴���ʱ��
	public static final String KEY_CREATETIME = "CREATETIME";
	// �Ƿ�ʹ�ù���У��
	public static final String KEY_USERCOORDINATETRANSFORMATION = "USERCOORDINATETRANSFORMATION";
	// ���ݵ���ʾ����������
	public static final String KEY_POINTVIEWFORMAT = "POINTVIEWFORMAT";
	// ������������ʾ����
	public static final String KEY_STAKINGOUTPOINTVIEWFORMAT = "STAKINGOUTPOINTVIEWFORMAT";

	// ����������ϵͳ���ֶ�

	// �ֶ����������
	public static final String KEY_ENAME = "ENAME";
	// �ֶ�����ĳ�����
	public static final String KEY_EA = "EA";
	// �ֶ��������
	public static final String KEY_EF = "EF";
	// �ֶ�����������
	public static final String KEY_MIDDLELONGITUDE = "MIDDLELONGITUDE";
	// �ֶα�ƫ��
	public static final String KEY_OFFSETNORTH = "OFFSETNORTH";
	// �ֶζ�ƫ��
	public static final String KEY_OFFSETEAST = "OFFSETEAST";
	// �ֶα�������
	public static final String KEY_CORRECTNORTH = "CORRECTNORTH";
	// �ֶζ�������
	public static final String KEY_CORRECTEAST = "CORRECTEAST";
	// WGS84ת�����߲���
	// Xƽ��
	public static final String KEY_WDX = "WDX";
	// Yƽ��
	public static final String KEY_WDY = "WDY";
	// Zƽ��
	public static final String KEY_WDZ = "WDZ";
	// X��ת
	public static final String KEY_WRX = "WRX";
	// Y��ת
	public static final String KEY_WRY = "WRY";
	// Z��ת
	public static final String KEY_WRZ = "WRZ";
	// ����ϵ��
	public static final String KEY_WK = "WK";
	// ����תWGS84�߲���
	// Xƽ��
	public static final String KEY_CDX = "CDX";
	// Yƽ��
	public static final String KEY_CDY = "CDY";
	// Zƽ��
	public static final String KEY_CDZ = "CDZ";
	// X��ת
	public static final String KEY_CRX = "CRX";
	// Y��ת
	public static final String KEY_CRY = "CRY";
	// Z��ת
	public static final String KEY_CRZ = "CRZ";
	// ����ϵ��
	public static final String KEY_CK = "CK";

	// ���������ݵ���ֶ�
	// �ֶε�����
	public static final String KEY_POINTFLAG = "POINTFLAG";
	// �ֶε���
	public static final String KEY_NAME = "NAME";
	// �ֶδ���
	public static final String KEY_CODE = "CODE";
	// �ֶ�X
	public static final String KEY_X = "X";
	// �ֶ�Y
	public static final String KEY_Y = "Y";
	// �ֶ�Z
	public static final String KEY_Z = "Z";

	// ����������ת�����ֶ�
	// �ֶ�GPS���������
	public static final String KEY_WGSPOINTNAME = "WGSPOINTNAME";
	// �ֶ�������������
	public static final String KEY_GRIDPOINTNAME = "GRIDPOINTNAME";
	// �ֶ�ˮƽ�в�
	public static final String KEY_HERROR = "HERROR";
	// �ֶδ�ֱ�в�
	public static final String KEY_VERROR = "VERROR";

	// ִ��open���������ݿ�ʱ�����淵�ص����ݿ����
	public static SQLiteDatabase mSQLiteDatabase = null;

	// ��SQLiteOpenHelper�̳й���
	public static DatabaseHelper mDatabaseHelper = null;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		/* ���캯��-����һ�����ݿ� */
		DatabaseHelper(Context context) {
			// ������getWritableDatabase()
			// �� getReadableDatabase()����ʱ
			// �򴴽�һ�����ݿ�
			super(context, DB_NAME, null, DB_VERSION);
		}

		/* ����һ���� */
		// ��һ�δ������ݿ�ʱ������ϵͳĬ�ϱ�
		@Override
		public void onCreate(SQLiteDatabase db) {
			String TABLE_INFORMATION = "CREATE TABLE PDefault_INFORMATION"
					+ " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CREATETIME
					+ " TEXT," + KEY_USERCOORDINATETRANSFORMATION + " INTEGER,"
					+ KEY_POINTVIEWFORMAT + " INTEGER,"
					+ KEY_STAKINGOUTPOINTVIEWFORMAT + " INTEGER)";

			String TABLE_POINTTABLE = "CREATE TABLE PDefault_POINTTABLE" + " ("
					+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_POINTFLAG
					+ " INTEGER," + KEY_NAME + " TEXT UNIQUE," + KEY_CODE
					+ " TEXT," + KEY_X + " REAL," + KEY_Y + " REAL," + KEY_Z
					+ " REAL)";

			String TABLE_SYSTEMTABLE = "CREATE TABLE PDefault_SYSTEMTABLE"
					+ " ("
					+ KEY_ID
					+ " INTEGER PRIMARY KEY,"
					+ KEY_ENAME
					+ " TEXT,"
					+ KEY_EA
					+ " REAL,"
					+ KEY_EF
					+ " REAL,"
					+ KEY_MIDDLELONGITUDE
					+ " REAL,"
					+ KEY_OFFSETNORTH
					+ " REAL,"
					+ KEY_OFFSETEAST
					+ " REAL,"
					+ KEY_CORRECTNORTH
					+ " REAL,"
					+ KEY_CORRECTEAST
					+ " REAL,"
					+ KEY_WDX
					+ " REAL,"
					+ KEY_WDY
					+ " REAL,"
					+ KEY_WDZ
					+ " REAL,"
					+ KEY_WRX
					+ " REAL,"
					+ KEY_WRY
					+ " REAL,"
					+ KEY_WRZ
					+ " REAL,"
					+ KEY_WK
					+ " REAL,"
					+ KEY_CDX
					+ " REAL,"
					+ KEY_CDY
					+ " REAL,"
					+ KEY_CDZ
					+ " REAL,"
					+ KEY_CRX
					+ " REAL,"
					+ KEY_CRY
					+ " REAL,"
					+ KEY_CRZ
					+ " REAL,"
					+ KEY_CK + " REAL)";

			String TABLE_CTTABLE = "CREATE TABLE PDefault_CTTABLE" + " ("
					+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_WGSPOINTNAME
					+ " TEXT," + KEY_GRIDPOINTNAME + " TEXT," + KEY_HERROR
					+ " REAL," + KEY_VERROR + " REAL)";

			db.execSQL(TABLE_INFORMATION);
			db.execSQL(TABLE_POINTTABLE);
			db.execSQL(TABLE_SYSTEMTABLE);
			db.execSQL(TABLE_CTTABLE);

			mSQLiteDatabase = db;
			ContentValues newValuesCoordinateSystem = new ContentValues();
			newValuesCoordinateSystem.put("ENAME", "WGS84����ϵ");
			newValuesCoordinateSystem.put("EA", 6378137);
			newValuesCoordinateSystem.put("EF", 1 / 298.257223563);
			newValuesCoordinateSystem.put("MIDDLELONGITUDE", 117);
			newValuesCoordinateSystem.put("OFFSETNORTH", 0);
			newValuesCoordinateSystem.put("OFFSETEAST", 500000);
			newValuesCoordinateSystem.put("CORRECTNORTH", 0);
			newValuesCoordinateSystem.put("CORRECTEAST", 0);
			newValuesCoordinateSystem.put("WDX", 0);
			newValuesCoordinateSystem.put("WDY", 0);
			newValuesCoordinateSystem.put("WDZ", 0);
			newValuesCoordinateSystem.put("WRX", 0);
			newValuesCoordinateSystem.put("WRY", 0);
			newValuesCoordinateSystem.put("WRZ", 0);
			newValuesCoordinateSystem.put("WK", 0);
			newValuesCoordinateSystem.put("CDX", 0);
			newValuesCoordinateSystem.put("CDY", 0);
			newValuesCoordinateSystem.put("CDZ", 0);
			newValuesCoordinateSystem.put("CRX", 0);
			newValuesCoordinateSystem.put("CRY", 0);
			newValuesCoordinateSystem.put("CRZ", 0);
			newValuesCoordinateSystem.put("CK", 0);
			insertData("PDefault_SYSTEMTABLE", newValuesCoordinateSystem);

			ContentValues newValuesSystemInformation = new ContentValues();
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss"); // ("yyyy-MM-dd hh:mm:ss");
			String sDate = sDateFormat.format(new java.util.Date());
			newValuesSystemInformation.put("CREATETIME", sDate);
			newValuesSystemInformation.put("USERCOORDINATETRANSFORMATION", 0);
			newValuesSystemInformation.put("POINTVIEWFORMAT", 0);
			newValuesSystemInformation.put("STAKINGOUTPOINTVIEWFORMAT", 0);
			insertData("PDefault_INFORMATION", newValuesSystemInformation);

			PadApplication.ProjectName = "Default";
			PadApplication.SaveStauts();
		}

		/* �������ݿ� */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/* ���캯��-ȡ��Context */
	public MyDataBaseAdapter(Context context) {
		mContext = context;
	}

	// ��ʼ�����ݱ���
	public static void SetTableName(String projectName) {
		mProjectName = projectName;
		ProjectInformationTable = "P" + mProjectName + "_INFORMATION";// ������Ϣ��
		PointTable = "P" + mProjectName + "_POINTTABLE";// ���ݵ��
		CoordinateSystemTable = "P" + mProjectName + "_SYSTEMTABLE";// ����ϵͳ��
		CoordinatetransTormationTable = "P" + mProjectName + "_CTTABLE";// ����ת����
	}

	// �����ݿ⣬�������ݿ����
	public static void open() throws SQLException {
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	/**
	 * ���ұ���ȡ���й�����
	 * 
	 * @param tabName
	 *            ����
	 * @return
	 */
	public static List<String> GetTables() {
		List<String> strList = new ArrayList<String>();
		Cursor cursor = null;
		cursor = mSQLiteDatabase
				.rawQuery(
						"select name from sqlite_master where type='table' order by name",
						null);
		String sName = "";
		while (cursor.moveToNext()) {
			// ����������
			String name = cursor.getString(0);
			int iFlag = name.indexOf("_");
			if (!name.substring(0, 1).equals("P")) {
				continue;
			}
			String sP = name.substring(1, iFlag);
			if (!sName.equals(sP)) {
				sName = sP;
				strList.add(sP);
			}
		}
		return strList;
	}

	public static List<String> GetTables(String ExceptionTable) {
		List<String> strList = new ArrayList<String>();
		Cursor cursor = null;
		cursor = mSQLiteDatabase
				.rawQuery(
						"select name from sqlite_master where type='table' order by name",
						null);
		String sName = "";
		while (cursor.moveToNext()) {
			// ����������
			String name = cursor.getString(0);
			int iFlag = name.indexOf("_");
			if (!name.substring(0, 1).equals("P")) {
				continue;
			}
			String sP = name.substring(1, iFlag);
			if (!sName.equals(sP) & !sP.equals(ExceptionTable)) {
				sName = sP;
				strList.add(sP);
			}
		}
		return strList;
	}

	/**
	 * �ж�ĳ�ű��Ƿ����
	 * 
	 * @param tabName
	 *            ����
	 * @return
	 */
	private static boolean TabbleIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	// �½����̱�
	public static boolean CreateNewTable(String projectName) {
		if (TabbleIsExist("P" + projectName + "_INFORMATION")) {
			return false;// �����ʱ���˳�
		}

		SetTableName(projectName);
		String DB_INFORMATION = "CREATE TABLE " + ProjectInformationTable
				+ " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CREATETIME
				+ " TEXT NOT NULL," + KEY_USERCOORDINATETRANSFORMATION
				+ " INTEGER," + KEY_POINTVIEWFORMAT + " INTEGER,"
				+ KEY_STAKINGOUTPOINTVIEWFORMAT + " INTEGER)";

		String DB_POINTTABLE = "CREATE TABLE " + PointTable + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_POINTFLAG + " INTEGER,"
				+ KEY_NAME + " TEXT UNIQUE," + KEY_CODE + " TEXT," + KEY_X
				+ " REAL," + KEY_Y + " REAL," + KEY_Z + " REAL)";

		String DB_SYSTEMTABLE = "CREATE TABLE " + CoordinateSystemTable + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_ENAME + " TEXT,"
				+ KEY_EA + " REAL," + KEY_EF + " REAL," + KEY_MIDDLELONGITUDE
				+ " REAL," + KEY_OFFSETNORTH + " REAL," + KEY_OFFSETEAST
				+ " REAL," + KEY_CORRECTNORTH + " REAL," + KEY_CORRECTEAST
				+ " REAL," + KEY_WDX + " REAL," + KEY_WDY + " REAL," + KEY_WDZ
				+ " REAL," + KEY_WRX + " REAL," + KEY_WRY + " REAL," + KEY_WRZ
				+ " REAL," + KEY_WK + " REAL," + KEY_CDX + " REAL," + KEY_CDY
				+ " REAL," + KEY_CDZ + " REAL," + KEY_CRX + " REAL," + KEY_CRY
				+ " REAL," + KEY_CRZ + " REAL," + KEY_CK + " REAL)";

		String DB_CTTABLE = "CREATE TABLE " + CoordinatetransTormationTable
				+ " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WGSPOINTNAME
				+ " TEXT," + KEY_GRIDPOINTNAME + " TEXT," + KEY_HERROR
				+ " REAL," + KEY_VERROR + " REAL)";

		mSQLiteDatabase.execSQL(DB_INFORMATION);
		mSQLiteDatabase.execSQL(DB_POINTTABLE);
		mSQLiteDatabase.execSQL(DB_SYSTEMTABLE);
		mSQLiteDatabase.execSQL(DB_CTTABLE);
		return true;
	}

	// ɾ�����̱�
	public static void DeleteTable(String DeleteProjectName) {
		String DB_INFORMATION = "DROP TABLE P" + DeleteProjectName
				+ "_INFORMATION";
		String DB_POINTTABLE = "DROP TABLE P" + DeleteProjectName
				+ "_POINTTABLE";
		String DB_SYSTEMTABLE = "DROP TABLE P" + DeleteProjectName
				+ "_SYSTEMTABLE";
		String DB_CTTABLE = "DROP TABLE P" + DeleteProjectName + "_CTTABLE";
		mSQLiteDatabase.execSQL(DB_INFORMATION);
		mSQLiteDatabase.execSQL(DB_POINTTABLE);
		mSQLiteDatabase.execSQL(DB_SYSTEMTABLE);
		mSQLiteDatabase.execSQL(DB_CTTABLE);
	}

	// �ر����ݿ�
	public static void close() {
		mDatabaseHelper.close();
	}

	/* ����һ������ */
	public static long insertData(String TableName, ContentValues insertValues) {
		return mSQLiteDatabase.insert(TableName, KEY_ID, insertValues);
	}

	/* ��ID����һ������ */
	public static boolean updateData(String TableName, long rowId,
			ContentValues insertValues) {
		return mSQLiteDatabase.update(TableName, insertValues, KEY_ID + "="
				+ rowId, null) > 0;
	}

	/* ����������һ������ */
	public static boolean updateDataByPointName(String TableName,
			String PointName, ContentValues insertValues) {
		String[] args = { PointName };
		return mSQLiteDatabase.update(TableName, insertValues, KEY_NAME + "=?",
				args) > 0;
	}

	/* ɾ��һ������ */
	public static boolean deleteDataByID(String TableName, long rowId) {
		return mSQLiteDatabase.delete(TableName, KEY_ID + "=" + rowId, null) > 0;
	}

	/* ɾ��һ������ */
	public static boolean deleteDataByName(String TableName, String PointName) {
		String[] args = { PointName };
		return mSQLiteDatabase.delete(TableName, KEY_NAME + "=?", args) > 0;
	}

	/* ͨ��Cursor��ѯ�������� */
	public static Cursor fetchAllData(String TableName, String[] Columns) {
		return mSQLiteDatabase.query(TableName, Columns, null, null, null,
				null, null);
	}

	/* ��ID��ѯ���� */
	public static Cursor fetchData(String TableName, long rowId,
			String[] Columns) throws SQLException {

		Cursor mCursor = mSQLiteDatabase.query(true, TableName, Columns, KEY_ID
				+ "=" + rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/* ��ѯָ���ֶ����� */
	public static Cursor fetchData(String TableName, String Key,
			String KeyValue, String[] Columns) throws SQLException {
		Cursor mCursor = mSQLiteDatabase.query(true, TableName, Columns, Key
				+ "= '" + KeyValue + "'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
