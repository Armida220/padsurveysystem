package com.padsurveysystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.padsurveysystem.ViewPointActivity.MyAdapter;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.*;
import android.widget.AdapterView.*;

public class ExportPointActivity extends Activity {

	Button btnExport;
	Spinner spnPointStyle;
	ProgressDialog progressDialog;
	TextView txtFileName;

	String m_Name = "1";
	String m_Code = "";
	String m_X = "0";
	String m_Y = "0";
	String m_Z = "0";
	String m_FilePath;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("������");
		setContentView(R.layout.view_file_exportpoint);
		btnExport = (Button) findViewById(R.id.button_export_view_file_exportpoint);
		btnExport.setOnClickListener(new ButtonExportClickEvent());

		String[] slCoorType = { "��������", "�������" };
		ArrayAdapter<String> spAdapter;
		spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, slCoorType);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPointStyle = (Spinner) findViewById(R.id.spinner_export_view_file_exportpoint);
		spnPointStyle.setAdapter(spAdapter);

		txtFileName=(TextView)findViewById(R.id.edittext_filename_view_file_exportpoint);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MMddhhmmss"); // ("yyyy-MM-dd hh:mm:ss");
		String sDate = sDateFormat.format(new java.util.Date());
		txtFileName.setText(sDate);
	}

	/**
	 * ��Handler������UI
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			// �ر�ProgressDialog
			progressDialog.dismiss();
			Toast toast = Toast.makeText(ExportPointActivity.this, "�����������",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 100);
			toast.show();

		}
	};

	// ��������
	private class ButtonExportClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (txtFileName.getText().toString().trim().length() < 1) {
				Toast toast = Toast.makeText(ExportPointActivity.this, "��������Ҫ�������ļ�����",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 100);
				toast.show();				
				return;
			}
			// ��ʾProgressDialog
			progressDialog = ProgressDialog.show(ExportPointActivity.this,
					"���ݵ�����", "���Ժ�......", true, false);

			// �½��߳�
			new Thread() {
				@Override
				public void run() {
					Looper.prepare();
					// ��Ҫ��ʱ�����ķ���
					if (spnPointStyle.getSelectedItem().toString() == "��������") {
						ExportPoint(1);
					} else {
						ExportPoint(0);
					}

					// ��handler����Ϣ

					handler.sendEmptyMessage(0);
					Looper.loop();
				}
			}.start();

		}
	}
	// �����б�
	private void ExportPoint(int CoorStyle) {

		DecimalFormat df3 = new DecimalFormat("0.000"); // ����һ����ʽ����f
		String X = null,Y= null,LAT = null,LON = null,Z = null;
		SurveyAngle sA=new SurveyAngle();
		String[] sCols = {"POINTFLAG","NAME", "CODE", "X", "Y", "Z" };
		Cursor mCursor = MyDataBaseAdapter.fetchAllData(
				MyDataBaseAdapter.PointTable, sCols);

		String pointList="";
		if (mCursor.moveToFirst() && mCursor.getCount() > 0) {
			do {
				pointList=pointList + mCursor.getString(1) + ","+ mCursor.getString(2)+ ",";
    			
				int PointFlag=mCursor.getInt(0);//ԭʼ������

				//�û�Ҫ����ʾ�������ʱ
				if (CoorStyle == 0) {

					//��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
					if (PointFlag == 1 || PointFlag == 2) {
						sA.valueOfDEG(mCursor.getDouble(3));
						pointList = pointList + sA.GetDMS() + ",";
						sA.valueOfDEG(mCursor.getDouble(4));
						pointList = pointList + sA.GetDMS() + ",";
						Z = df3.format(mCursor.getDouble(5));
						pointList = pointList + Z;
					}
					//��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
					if (PointFlag == 3 || PointFlag == 4) {
     				    CartesianCoordinatePoint pcc=new CartesianCoordinatePoint();
						GeodeticCoordinatePoint pcg = new GeodeticCoordinatePoint();

						pcc.X=mCursor.getDouble(3);						
						pcc.Y=mCursor.getDouble(4);
						pcc.H=mCursor.getDouble(5);
						
						//��ʹ�ô�������ϵͳʱ��ʹ�����귴��
						if(PadApplication.UseCoordinateTransfomation == 0){
							CoordinateTransform ct =new CoordinateTransform();
							CoordinateSystem cs=PadApplication.CurrentCoordinateSystem;
							ct.CartesianToGeodetic(pcg, pcc, cs);
						}
						else//ʹ������ת������У����
						{
							double[] dC=new double[3];
							double[] dW=new double[3];
							dC[0]=pcc.X;
							dC[1]=pcc.Y;
							dC[2]=pcc.H;
							PadApplication.CoordinateTrans.CartesianTransWGS84(dC, dW);
							pcg.Latitude=dW[0];
							pcg.Longitude=dW[1];
							pcg.Height=dW[2];
						}
						sA.valueOfRadian(pcg.Latitude);
						pointList=pointList+sA.GetDMS() +",";						
						sA.valueOfRadian(pcg.Longitude);						
						pointList=pointList+sA.GetDMS() +",";
						Z = df3.format(pcg.Height);												
						pointList=pointList+Z;
					}					
				}
			    //���û�Ҫ����ʾ��������ʱ
				if (CoorStyle == 1) {
					//��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
					if (PointFlag == 1 || PointFlag == 2) {

						CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
						GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
						pgc.Latitude =SurveyMath.DEGToRadian(mCursor.getDouble(3));
						pgc.Longitude = SurveyMath.DEGToRadian(mCursor.getDouble(4));
						pgc.Height = mCursor.getDouble(5);
						
						//��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
						if(PadApplication.UseCoordinateTransfomation ==0){
							CoordinateTransform ct =new CoordinateTransform();
							CoordinateSystem cs=PadApplication.CurrentCoordinateSystem;
							ct.GeodeticToCartesian(pcc, pgc, cs);
						}
						else//ʹ������ת������У����
						{
							double[] dC=new double[3];
							double[] dW=new double[3];
							dW[0]=pgc.Latitude;
							dW[1]=pgc.Longitude;
							dW[2]=pgc.Height;
							PadApplication.CoordinateTrans.WGS84TransCartesian(dW, dC);
							pcc.X=dC[0];
							pcc.Y=dC[1];
							pcc.H=dC[2];
						}
						
						X = df3.format(pcc.X);
						Y = df3.format(pcc.Y);
						Z = df3.format(pcc.H);
					}
					//��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
					if (PointFlag == 3 || PointFlag == 4) {
						//���귴��
						X= df3.format(mCursor.getDouble(3));
						
						Y= df3.format(mCursor.getDouble(4));

						Z = df3.format(mCursor.getDouble(5));
					}
					pointList=pointList+ X + " , " + Y + " , " + Z;		
				}
				pointList=pointList+"\r\n";
			} while (mCursor.moveToNext());
		}

		m_FilePath = "mnt/sdcard/PadSurveyData/Export/"
				+ txtFileName.getText();
		TextFile tF=new TextFile();
		tF.Write(m_FilePath,pointList);

	}

}
