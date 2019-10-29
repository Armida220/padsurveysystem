package com.padsurveysystem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import Jama.Matrix;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;

public class PadApplication {
	private static Context mContext;
	private static MyDataBaseAdapter mDataBaseAdapter;

	public static String ProjectName = null;// Ĭ�ϴ򿪵Ĺ�������
	public static String LastCode = "";// ���ʹ�õĴ���

	// ��ǰʹ�õ�����ϵͳ
	public static CoordinateSystem CurrentCoordinateSystem = new CoordinateSystem();
	
	// �Ƿ�ʹ�ù���У����0-��ʹ�ã�1-ʹ��
	public static int UseCoordinateTransfomation = 0;
	//��ǰʹ�õ��߲���
	public static CoordTransSevenParam CoordinateTrans = new CoordTransSevenParam();
	// ���ݵ���ʾ����������
	public static int PointViewFormat = 0;
	//�������������ʾ����
	public static int StakingoutPointViewFormat=0;

	// ��������ͼ�ļ���
	public static String AnotherMapFileName = "";

	/* ���캯��-ȡ��Context */
	public static void Initialize(Context context) {
		mContext = context;
		LoadStatus();
		// ��ʼ��ϵͳ��Ϣ��
		if (PadApplication.ProjectName == null) {
			PadApplication.ProjectName = "Default";
		}
		// ��ȡ�����ļ�����Ĭ�ϵĹ���
		/* ����MyDataBaseAdapter���� */
		mDataBaseAdapter = new MyDataBaseAdapter(context);
		/* ȡ�����ݿ���� */
		MyDataBaseAdapter.open();
		// ��ʼ�����ݱ�
		SetProjectName(ProjectName);
		// ���ϵͳ�Ƿ�����SD�����������������ʾ
		// ���ϵͳ�ļ����Ƿ���ڣ�������������Զ�����
		CreateSDCardDir();
		// ��ʼ������ϵͳ���߲���
		GetCoordinateSystem();
		// ��ʼ��������Ϣ���Ƿ�ʹ���߲������ݵ���ʾ����������,������������ʾ����
		GetProjectInformation();

	}

	// ��SD���ϴ���ϵͳ�����ļ���
	public static void CreateSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
			File sdcardDir = Environment.getExternalStorageDirectory();
			// �õ�һ��·����������sdcard���ļ���·��������
			String pathImport = sdcardDir.getPath() + "/PadSurveyData/Import";
			String pathExport = sdcardDir.getPath() + "/PadSurveyData/Export";
			String pathAnotherMap = sdcardDir.getPath()
					+ "/PadSurveyData/AnotherMap";
			File fImport = new File(pathImport);
			File fExport = new File(pathExport);
			File fAnotherMap = new File(pathAnotherMap);
			// �������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
			if (!fImport.exists()) {
				fImport.mkdirs();
			}
			if (!fExport.exists()) {
				fExport.mkdirs();
			}
			if (!fAnotherMap.exists()) {
				fAnotherMap.mkdirs();
			}
		} else {
			final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
					mContext);
			builder.setTitle("PadSurvey");
			builder.setMessage("û�м�⵽SD����ϵͳ���޷����뵼�����ݣ�");
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {

				}
			});
			builder.create().show();
			return;
		}
	}

	//���ô��µĹ���
	public static void SetProjectName(String projectName) {
		ProjectName = projectName;
		MyDataBaseAdapter.SetTableName(projectName);
		ReGetInformation();
		SaveStauts();
	}

	public static List<String> GetProjectList(String projectName) {
		return MyDataBaseAdapter.GetTables(projectName);
	}

	public static void DeleteProject(String projectName) {
		MyDataBaseAdapter.DeleteTable(projectName);
	}

	public static boolean CreateNewProject(String projectName,
			int ellipsoidbase, double middlelongitude, double offsetnorth,
			double offseteeast, double correctnorth, double correcteast,
			int coordinateview) {

		// �����¹���
		if (!MyDataBaseAdapter.CreateNewTable(projectName)) {
			return false;
		}

		ProjectName = projectName;
		SaveStauts();
		MyDataBaseAdapter.SetTableName(projectName);
		ContentValues newValuesCoordinateSystem = new ContentValues();

		ReferenceEllipsoid tR=CoordinateSystem.GetReferenceEllipsoid(ellipsoidbase);
		newValuesCoordinateSystem.put("ENAME", tR.Name);
		newValuesCoordinateSystem.put("EA", tR.a());
		newValuesCoordinateSystem.put("EF", tR.f());
		newValuesCoordinateSystem.put("MIDDLELONGITUDE", SurveyMath.DMSToDEG(middlelongitude));
		newValuesCoordinateSystem.put("OFFSETNORTH", offsetnorth);
		newValuesCoordinateSystem.put("OFFSETEAST", offseteeast);
		newValuesCoordinateSystem.put("CORRECTNORTH", correctnorth);
		newValuesCoordinateSystem.put("CORRECTEAST", correcteast);

		MyDataBaseAdapter.insertData(MyDataBaseAdapter.CoordinateSystemTable,
				newValuesCoordinateSystem);

		ContentValues newValuesProjectInformation = new ContentValues();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss"); // ("yyyy-MM-dd hh:mm:ss");
		String sDate = sDateFormat.format(new java.util.Date());
		newValuesProjectInformation.put("CREATETIME", sDate);
		newValuesProjectInformation.put("USERCOORDINATETRANSFORMATION", 0);
		newValuesProjectInformation.put("POINTVIEWFORMAT", coordinateview);
		PointViewFormat =coordinateview;
		newValuesCoordinateSystem.put("STAKINGOUTPOINTVIEWFORMAT", 0);
		MyDataBaseAdapter.insertData(MyDataBaseAdapter.ProjectInformationTable,
				newValuesProjectInformation);
		ReGetInformation();
		
		return true;
		
		
	}

	/* װ�ء���ȡ���ݣ����һ�δ򿪵Ĺ��� */
	public static void LoadStatus() {
		/* ����Properties�Զ��� */
		Properties properties = new Properties();

		try {
			/* �����ļ� */
			FileInputStream stream = mContext.openFileInput("app.cfg");

			/* ��ȡ�ļ����� */
			properties.load(stream);
			/* ȡ������ */
			ProjectName = properties.get("projectname").toString();

		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}

	}

	/* �������� �����һ�δ򿪵Ĺ��� */
	public static boolean SaveStauts() {
		Properties properties = new Properties();

		/* �����ݴ����Properties */
		properties.put("projectname", String.valueOf(ProjectName));
		try {
			FileOutputStream stream = mContext.openFileOutput("app.cfg",
					Context.MODE_PRIVATE);

			/* ������õ�����д���ļ��� */
			properties.store(stream, "");
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	// �����ݱ������ӵ�
	public static long AddPoint(String Flag, String Name, String Code,
			String X, String Y, String Z) {
		ContentValues newValues = new ContentValues();
		newValues.put("POINTFLAG", Flag);
		newValues.put("NAME", Name);
		newValues.put("CODE", Code);
		newValues.put("X", X);
		newValues.put("Y", Y);
		newValues.put("Z", Z);
		return MyDataBaseAdapter.insertData(MyDataBaseAdapter.PointTable,
				newValues);
	}

	// �޸����ݱ��д��ڵĵ�
	public static void EditPoint(String Flag, String Name, String Code,
			String X, String Y, String Z) {
		ContentValues newValues = new ContentValues();
		newValues.put("POINTFLAG", Flag);
		newValues.put("NAME", Name);
		newValues.put("CODE", Code);
		newValues.put("X", X);
		newValues.put("Y", Y);
		newValues.put("Z", Z);
		MyDataBaseAdapter.updateDataByPointName(MyDataBaseAdapter.PointTable,
				Name, newValues);
	}

	// ɾ�����ݱ��д��ڵĵ�
	public static void RemovePoint(String Name) {
		MyDataBaseAdapter.deleteDataByName(MyDataBaseAdapter.PointTable, Name);
	}

	// ͨ�������������ݵ�
	public static Cursor FindPointByName(String PointName)throws SQLException {
		String[] sCols = {"POINTFLAG", "NAME", "CODE", "X", "Y", "Z"};
		return MyDataBaseAdapter.fetchData(MyDataBaseAdapter.PointTable,
				"NAME", PointName, sCols);
	}

	// ��ȡ����ϵͳ, ���������׼���߲���
	private static void SetCoordinateSystem() {
		
		ContentValues newValuesCoordinateSystem = new ContentValues();
		newValuesCoordinateSystem.put("ENAME", CurrentCoordinateSystem.ReferenceEllipsoid.Name);
		newValuesCoordinateSystem.put("EA", CurrentCoordinateSystem.ReferenceEllipsoid.a());
		newValuesCoordinateSystem.put("EF", CurrentCoordinateSystem.ReferenceEllipsoid.f());
		newValuesCoordinateSystem.put("MIDDLELONGITUDE", SurveyMath.RadianToDEG(CurrentCoordinateSystem.MiddleLongitude));
		newValuesCoordinateSystem.put("OFFSETNORTH", CurrentCoordinateSystem.OffsetNorth);
		newValuesCoordinateSystem.put("OFFSETEAST", CurrentCoordinateSystem.OffsetEast);
		newValuesCoordinateSystem.put("CORRECTNORTH", CurrentCoordinateSystem.CorrectNorth);
		newValuesCoordinateSystem.put("CORRECTEAST", CurrentCoordinateSystem.CorrectEast);
		newValuesCoordinateSystem.put("WDX", CoordinateTrans.WDX);
		newValuesCoordinateSystem.put("WDY", CoordinateTrans.WDY);		
		newValuesCoordinateSystem.put("WDZ", CoordinateTrans.WDZ);	
		newValuesCoordinateSystem.put("WRX", CoordinateTrans.WRX);			
		newValuesCoordinateSystem.put("WRY", CoordinateTrans.WRY);	
		newValuesCoordinateSystem.put("WRZ", CoordinateTrans.WRZ);		
		newValuesCoordinateSystem.put("WK", CoordinateTrans.WK);		
		newValuesCoordinateSystem.put("CDX", CoordinateTrans.CDX);
		newValuesCoordinateSystem.put("CDY", CoordinateTrans.CDY);		
		newValuesCoordinateSystem.put("CDZ", CoordinateTrans.CDZ);	
		newValuesCoordinateSystem.put("CRX", CoordinateTrans.CRX);			
		newValuesCoordinateSystem.put("CRY", CoordinateTrans.CRY);	
		newValuesCoordinateSystem.put("CRZ", CoordinateTrans.CRZ);		
		newValuesCoordinateSystem.put("CK", CoordinateTrans.CK);
		
		MyDataBaseAdapter.updateData(MyDataBaseAdapter.CoordinateSystemTable,1, newValuesCoordinateSystem);
	}
	// ��ȡ����ϵͳ, ���������׼���߲���
	private static void GetCoordinateSystem() {

		String[] sCols = { "ENAME", "EA", "EF", "MIDDLELONGITUDE",
				"OFFSETNORTH", "OFFSETEAST", "CORRECTNORTH", "CORRECTEAST",
				"WDX", "WDY", "WDZ", "WRX", "WRY", "WRZ", "WK", "CDX", "CDY",
				"CDZ", "CRX", "CRY", "CRZ", "CK" };
		Cursor tCursor = MyDataBaseAdapter.fetchAllData(
				MyDataBaseAdapter.CoordinateSystemTable, sCols);
		tCursor.moveToFirst();

		CurrentCoordinateSystem.ReferenceEllipsoid=new ReferenceEllipsoid(tCursor.getString(0),
				tCursor.getDouble(1),tCursor.getDouble(2));

		CurrentCoordinateSystem.MiddleLongitude = SurveyMath.DEGToRadian(tCursor.getDouble(3));
		CurrentCoordinateSystem.OffsetNorth = tCursor.getDouble(4);
		CurrentCoordinateSystem.OffsetEast = tCursor.getDouble(5);
		CurrentCoordinateSystem.CorrectNorth = tCursor.getDouble(6);
		CurrentCoordinateSystem.CorrectEast = tCursor.getDouble(7);
		
		CoordinateTrans.setWGS84TransCartesianSevenParameter(
				tCursor.getDouble(8), tCursor.getDouble(9),
				tCursor.getDouble(10), tCursor.getDouble(11),
				tCursor.getDouble(12), tCursor.getDouble(13),
				tCursor.getDouble(14));
		CoordinateTrans.setCartesianTransWGS84SevenParameter(
				tCursor.getDouble(15), tCursor.getDouble(16),
				tCursor.getDouble(17), tCursor.getDouble(18),
				tCursor.getDouble(19), tCursor.getDouble(20),
				tCursor.getDouble(21));
	}

	// ��ȡ������Ϣ
	private static void GetProjectInformation() {
		String[] sCols = { "CREATETIME", "USERCOORDINATETRANSFORMATION",
				"POINTVIEWFORMAT", "STAKINGOUTPOINTVIEWFORMAT" };
		Cursor tCursor = MyDataBaseAdapter.fetchAllData(
				MyDataBaseAdapter.ProjectInformationTable, sCols);
		tCursor.moveToFirst();
		try {
			UseCoordinateTransfomation = tCursor.getInt(1); // �Ƿ�ʹ�ù���У��
			PointViewFormat = tCursor.getInt(2); // ���ݵ���ʾ����������
		} catch (Exception e) {
			UseCoordinateTransfomation = 0; // �Ƿ�ʹ�ù���У��
			PointViewFormat = 0; // ���ݵ���ʾ����������
		}
		// StakingoutPointViewFormat = tCursor.getInt(3); // ������������ʾ����
	}
	// ���ù�����Ϣ
	private static void SetProjectInformation() {
				
		ContentValues newValuesCoordinateSystem = new ContentValues();
		newValuesCoordinateSystem.put("USERCOORDINATETRANSFORMATION", UseCoordinateTransfomation);
		newValuesCoordinateSystem.put("POINTVIEWFORMAT", PointViewFormat);
		newValuesCoordinateSystem.put("STAKINGOUTPOINTVIEWFORMAT", StakingoutPointViewFormat);

		
		MyDataBaseAdapter.updateData(MyDataBaseAdapter.ProjectInformationTable,1, newValuesCoordinateSystem);
	}
	//���»�ȡ��������
	public static void ReGetInformation(){
		GetCoordinateSystem();
		GetProjectInformation();
	}
	//�������ø�������
	public static void ReSetInformation(){
		SetCoordinateSystem();
		SetProjectInformation();
	}
}
