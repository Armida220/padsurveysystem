package com.padsurveysystem;

import java.text.DecimalFormat;
import java.util.*;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;

public class CalculatePointActivity extends Activity {
	Button btnCalculate;
	Button btnSave;
	Button btnSelectStart1;
	Button btnSelectStart2;
	Button btnSelectEnd1;
	Button btnSelectEnd2;
	TextView txtStart1;
	TextView txtEnd1;
	TextView txtStart2;
	TextView txtEnd2;
	TextView txtPointName;
	TextView txtX;
	TextView txtY;
	TextView txtSF;

	View layout1, layout2;
	Spinner spnClassItem;
	
	int selectPointFlag=0;
	String Start1=null;
	String Start2=null;
	String End1=null;
	String End2=null;
	String mName;
	String mX;
	String mY;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("�������");
		LayoutInflater inflater = LayoutInflater.from(this);
		layout1 = inflater.inflate(R.layout.view_tools_calculatepoint_item1,
				null);
		layout2 = inflater.inflate(R.layout.view_tools_calculatepoint_item2,
				null);

		setView1();
	}

	private void setView1() {
		setContentView(layout1);
		// �л���main
		spnClassItem = (Spinner) findViewById(R.id.spinner_view_tools_calculatepoint_item1);
		String[] slCoorType = { "�߶ν���", "���㽻��" };
		ArrayAdapter<String> spAdapter;
		spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, slCoorType);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnClassItem.setAdapter(spAdapter);
		spnClassItem.setSelection(0);

		// ���Spinner�¼�����
		spnClassItem.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// ������ʾ��ǰѡ�����
				if (arg0.getSelectedItem().toString() == "���㽻��") {
					setView2();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}

		});
		
		 btnCalculate=(Button)findViewById(R.id.button_calculate_view_tools_calculatepoint_item1); 
		 btnSave=(Button)findViewById(R.id.button_save_view_tools_calculatepoint_item1); 
		 btnSelectStart1=(Button)findViewById(R.id.button_select_start1_view_tools_calculatepoint_item1);
		 btnSelectStart2=(Button)findViewById(R.id.button_select_start2_view_tools_calculatepoint_item1);
		 btnSelectEnd1=(Button)findViewById(R.id.button_select_end1_view_tools_calculatepoint_item1);
		 btnSelectEnd2=(Button)findViewById(R.id.button_select_end2_view_tools_calculatepoint_item1);
		 txtStart1=(TextView)findViewById(R.id.edittext_start1_view_tools_calculatepoint_item1);
		 txtStart2=(TextView)findViewById(R.id.edittext_start2_view_tools_calculatepoint_item1);
		 txtEnd1=(TextView)findViewById(R.id.edittext_end1_view_tools_calculatepoint_item1);
		 txtEnd2=(TextView)findViewById(R.id.edittext_end2_view_tools_calculatepoint_item1);
		 txtPointName=(TextView)findViewById(R.id.edittext_pointname_view_tools_calculatepoint_item1);
		 txtX=(TextView)findViewById(R.id.textview_x_view_tools_calculatepoint_item1);
		 txtY=(TextView)findViewById(R.id.textview_y_view_tools_calculatepoint_item1);
		 btnCalculate.setOnClickListener(new ButtonCalculateClickEvent());
		 btnSave.setOnClickListener(new ButtonSaveClickEvent());
		 btnSelectStart1.setOnClickListener(new ButtonSelectClickEvent());
		 btnSelectStart2.setOnClickListener(new ButtonSelectClickEvent());
		 btnSelectEnd1.setOnClickListener(new ButtonSelectClickEvent());	
		 btnSelectEnd2.setOnClickListener(new ButtonSelectClickEvent());

	}

	private void setView2() {
		setContentView(layout2);
		spnClassItem = (Spinner) findViewById(R.id.spinner_view_tools_calculatepoint_item2);
		String[] slCoorType = { "�߶ν���", "���㽻��" };
		ArrayAdapter<String> spAdapter;
		spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, slCoorType);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnClassItem.setAdapter(spAdapter);
		spnClassItem.setSelection(1);
		// ���Spinner�¼�����
		spnClassItem.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// ������ʾ��ǰѡ�����
				if (arg0.getSelectedItem().toString() == "�߶ν���") {
					setView1();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}

		});
		 btnCalculate=(Button)findViewById(R.id.button_calculate_view_tools_calculatepoint_item2); 
		 btnSave=(Button)findViewById(R.id.button_save_view_tools_calculatepoint_item2); 
		 btnSelectStart1=(Button)findViewById(R.id.button_select_start_view_tools_calculatepoint_item2);
		 btnSelectEnd1=(Button)findViewById(R.id.button_select_end_view_tools_calculatepoint_item2);
		 btnSelectStart2=(Button)findViewById(R.id.button_select_offset_view_tools_calculatepoint_item2);
		 txtStart1=(TextView)findViewById(R.id.edittext_start_view_tools_calculatepoint_item2);
		 txtEnd1=(TextView)findViewById(R.id.edittext_end_view_tools_calculatepoint_item2);
		 txtStart2=(TextView)findViewById(R.id.edittext_offsetpoint_view_tools_calculatepoint_item2);
		 txtPointName=(TextView)findViewById(R.id.edittext_pointname_view_tools_calculatepoint_item2);
		 txtX=(TextView)findViewById(R.id.textview_x_view_tools_calculatepoint_item2);
		 txtY=(TextView)findViewById(R.id.textview_y_view_tools_calculatepoint_item2);
		 txtSF=(TextView)findViewById(R.id.textview_sf_view_tools_calculatepoint_item2);
		 btnCalculate.setOnClickListener(new ButtonCalculateClickEvent());
		 btnSave.setOnClickListener(new ButtonSaveClickEvent());
		 btnSelectStart1.setOnClickListener(new ButtonSelectClickEvent());
		 btnSelectStart2.setOnClickListener(new ButtonSelectClickEvent());
		 btnSelectEnd1.setOnClickListener(new ButtonSelectClickEvent());	

	}

	// ���������
	private class ButtonCalculateClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Start1=txtStart1.getText().toString();			
			End1=txtEnd1.getText().toString();
			Start2=txtStart2.getText().toString();
			End2=txtEnd2.getText().toString();
			DecimalFormat df = new DecimalFormat("0.000"); // ����һ����ʽ����f
			try {
				CartesianCoordinatePoint cs1,cs2,ce1,ce2,cd;
				LinePoint l1,l2,p1;
				cs1=new CartesianCoordinatePoint();
				Cursor cs1Cursor = PadApplication.FindPointByName(Start1);
				if(cs1Cursor.getCount()<1){
					Toast toast = Toast.makeText(
							CalculatePointActivity.this, "��"
									+ Start1 + "�����ڣ�",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 100);
					toast.show();
					return;
				}
				int PointFlag = cs1Cursor.getInt(0);
				// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
				if (PointFlag == 1 || PointFlag == 2) {
					CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
					GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
					pgc.Latitude = SurveyMath.DEGToRadian(cs1Cursor.getDouble(3));
					pgc.Longitude = SurveyMath.DEGToRadian(cs1Cursor.getDouble(4));
					pgc.Height = cs1Cursor.getDouble(5);

					// ��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
					if (PadApplication.UseCoordinateTransfomation == 0) {
						CoordinateTransform ct = new CoordinateTransform();
						CoordinateSystem cs = PadApplication.CurrentCoordinateSystem;
						ct.GeodeticToCartesian(pcc, pgc, cs);
					} else// ʹ������ת������У����
					{
						double[] dC = new double[3];
						double[] dW = new double[3];
						dW[0] = pgc.Latitude;
						dW[1] = pgc.Longitude;
						dW[2] = pgc.Height;
						PadApplication.CoordinateTrans.WGS84TransCartesian(dW, dC);
						pcc.X = dC[0];
						pcc.Y = dC[1];
						pcc.H = dC[2];
					}

					cs1.X = pcc.X;
					cs1.Y = pcc.Y;
				}
				// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
				if (PointFlag == 3 || PointFlag == 4) {
					// ���귴��
					cs1.X = cs1Cursor.getDouble(3);
					cs1.Y = cs1Cursor.getDouble(4);
				}
				
				ce1=new CartesianCoordinatePoint();
				Cursor ce1Cursor = PadApplication.FindPointByName(End1);
				if(ce1Cursor.getCount()<1){
					Toast toast = Toast.makeText(
							CalculatePointActivity.this, "��"
									+ End1 + "�����ڣ�",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 100);
					toast.show();
					return;
				}
				PointFlag = ce1Cursor.getInt(0);
				// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
				if (PointFlag == 1 || PointFlag == 2) {
					CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
					GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
					pgc.Latitude = SurveyMath.DEGToRadian(ce1Cursor.getDouble(3));
					pgc.Longitude = SurveyMath.DEGToRadian(ce1Cursor.getDouble(4));
					pgc.Height = ce1Cursor.getDouble(5);

					// ��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
					if (PadApplication.UseCoordinateTransfomation == 0) {
						CoordinateTransform ct = new CoordinateTransform();
						CoordinateSystem cs = PadApplication.CurrentCoordinateSystem;
						ct.GeodeticToCartesian(pcc, pgc, cs);
					} else// ʹ������ת������У����
					{
						double[] dC = new double[3];
						double[] dW = new double[3];
						dW[0] = pgc.Latitude;
						dW[1] = pgc.Longitude;
						dW[2] = pgc.Height;
						PadApplication.CoordinateTrans.WGS84TransCartesian(dW, dC);
						pcc.X = dC[0];
						pcc.Y = dC[1];
						pcc.H = dC[2];
					}

					ce1.X = pcc.X;
					ce1.Y = pcc.Y;
				}
				// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
				if (PointFlag == 3 || PointFlag == 4) {
					// ���귴��
					ce1.X = ce1Cursor.getDouble(3);
					ce1.Y = ce1Cursor.getDouble(4);
				}
				cs2=new CartesianCoordinatePoint();
				Cursor cs2Cursor = PadApplication.FindPointByName(Start2);
				if(cs2Cursor.getCount()<1){
					Toast toast = Toast.makeText(
							CalculatePointActivity.this, "��"
									+ Start2 + "�����ڣ�",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 100);
					toast.show();
					return;
				}
				PointFlag = cs2Cursor.getInt(0);
				// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
				if (PointFlag == 1 || PointFlag == 2) {
					CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
					GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
					pgc.Latitude = SurveyMath.DEGToRadian(cs2Cursor.getDouble(3));
					pgc.Longitude = SurveyMath.DEGToRadian(cs2Cursor.getDouble(4));
					pgc.Height = cs2Cursor.getDouble(5);

					// ��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
					if (PadApplication.UseCoordinateTransfomation == 0) {
						CoordinateTransform ct = new CoordinateTransform();
						CoordinateSystem cs = PadApplication.CurrentCoordinateSystem;
						ct.GeodeticToCartesian(pcc, pgc, cs);
					} else// ʹ������ת������У����
					{
						double[] dC = new double[3];
						double[] dW = new double[3];
						dW[0] = pgc.Latitude;
						dW[1] = pgc.Longitude;
						dW[2] = pgc.Height;
						PadApplication.CoordinateTrans.WGS84TransCartesian(dW, dC);
						pcc.X = dC[0];
						pcc.Y = dC[1];
						pcc.H = dC[2];
					}

					cs2.X = pcc.X;
					cs2.Y = pcc.Y;
				}
				// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
				if (PointFlag == 3 || PointFlag == 4) {
					// ���귴��
					cs2.X = cs2Cursor.getDouble(3);
					cs2.Y = cs2Cursor.getDouble(4);
				}
				if(spnClassItem.getSelectedItem().toString()=="�߶ν���")
				{
					ce2=new CartesianCoordinatePoint();
					Cursor ce2Cursor = PadApplication.FindPointByName(End2);
					if(ce2Cursor==null)return;
					PointFlag = ce2Cursor.getInt(0);
					// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
					if (PointFlag == 1 || PointFlag == 2) {
						CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
						GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
						pgc.Latitude = SurveyMath.DEGToRadian(ce2Cursor.getDouble(3));
						pgc.Longitude = SurveyMath.DEGToRadian(ce2Cursor.getDouble(4));
						pgc.Height = ce2Cursor.getDouble(5);

						// ��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
						if (PadApplication.UseCoordinateTransfomation == 0) {
							CoordinateTransform ct = new CoordinateTransform();
							CoordinateSystem cs = PadApplication.CurrentCoordinateSystem;
							ct.GeodeticToCartesian(pcc, pgc, cs);
						} else// ʹ������ת������У����
						{
							double[] dC = new double[3];
							double[] dW = new double[3];
							dW[0] = pgc.Latitude;
							dW[1] = pgc.Longitude;
							dW[2] = pgc.Height;
							PadApplication.CoordinateTrans.WGS84TransCartesian(dW, dC);
							pcc.X = dC[0];
							pcc.Y = dC[1];
							pcc.H = dC[2];
						}

						ce2.X = pcc.X;
						ce2.Y = pcc.Y;
					}
					// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
					if (PointFlag == 3 || PointFlag == 4) {
						// ���귴��
						ce2.X = ce2Cursor.getDouble(3);
						ce2.Y = ce2Cursor.getDouble(4);
					}
					
					cd=CalculatePoint(cs1,ce1,cs2,ce2);
				}
				else//���㽻��
				{
					cd=CalculatePoint(cs1,ce1,cs2);
					l1=new LinePoint();
					l2=new LinePoint();
					p1=new LinePoint();
					l1.X=cs1.X;
					l1.Y=cs1.Y;
					l2.X=ce1.X;
					l2.Y=ce1.Y;
					p1.X=cs2.X;
					p1.Y=cs2.Y;
					l1.Station=0;
					SurveyMath.GetStationOffset(l1, l2, p1);
					txtSF.setText("��̣�" + df.format(p1.Station) + "��ƫ�ࣺ" +	df.format(p1.Offset));					
				}

				txtX.setText("X��" + df.format(cd.X));
				txtY.setText("Y��" + df.format(cd.Y));
				mX=df.format(cd.X);
				mY=df.format(cd.Y);
				SetPointName();

			} catch (Exception e) {
				final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
						CalculatePointActivity.this);
				builder.setTitle("PadSurvey");
				builder.setMessage("��������������Ƿ���ȷ��");
				builder.setPositiveButton("ȷ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				});
				builder.create().show();

			}

		}
	}
	// �����
	private class ButtonSaveClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			mName = txtPointName.getText().toString();
			if (mName.trim().length() == 0) {
				Toast toast = Toast.makeText(CalculatePointActivity.this, "��������Ϊ�գ�",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 100);
				toast.show();
				return;
			}
			Cursor mCursor = MyDataBaseAdapter.fetchData(
					MyDataBaseAdapter.PointTable, "NAME", mName, null);
			// �������ظ�ʱ
			if (mCursor != null) {
				if (mCursor.getCount() > 0) {
					SetPointName();// ������һ������
					Toast toast = Toast.makeText(CalculatePointActivity.this,
							"�����ظ���", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 100);
					toast.show();
					return;
				}
			}
			// ���浱ǰ�۲�����
			PadApplication.AddPoint("4", mName, "The intersection point", mX, mY, "0");
			Toast toast = Toast.makeText(CalculatePointActivity.this, "���ݵ��ѱ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
		}
	}
	// ѡ���
	private class ButtonSelectClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {			// �½�һ��Intent
			if(v.getId()==R.id.button_select_start1_view_tools_calculatepoint_item1
					|| v.getId()==R.id.button_select_start_view_tools_calculatepoint_item2){
				selectPointFlag=0;
			}
			if(v.getId()==R.id.button_select_start2_view_tools_calculatepoint_item1
					|| v.getId()==R.id.button_select_offset_view_tools_calculatepoint_item2){
				selectPointFlag=2;
			}
			if(v.getId()==R.id.button_select_end1_view_tools_calculatepoint_item1
					|| v.getId()==R.id.button_select_end_view_tools_calculatepoint_item2){
				selectPointFlag=1;
			}
			if(v.getId()==R.id.button_select_end2_view_tools_calculatepoint_item1){
				selectPointFlag=3;
			}
			Intent intent = new Intent();
			intent.putExtra("CoorStyle", 3);
			// �ƶ�intentҪ��������
			intent.setClass(CalculatePointActivity.this,
					SelectPointActivity.class);
			// ����һ���µ�Activity
			startActivityForResult(intent, 0);}
	}
	// ��д�÷������÷����Իص��ķ�ʽ����ȡָ��Activity���صĽ��
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// ��requestCode��resultCodeͬʱΪ0��Ҳ���Ǵ����ض��Ľ��
		if (requestCode == 0 && resultCode == RESULT_OK) {
			String selectPointName =intent.getStringExtra("PointName");
			if(selectPointFlag==0){
				Start1=selectPointName;
				txtStart1.setText(selectPointName);
			}
			if(selectPointFlag==1){
				End1=selectPointName;
				txtEnd1.setText(selectPointName);
			}
			if(selectPointFlag==2){
				Start2=selectPointName;
				txtStart2.setText(selectPointName);
			}
			if(selectPointFlag==3){
				End2=selectPointName;
				txtEnd2.setText(selectPointName);
			}


		}
	}
	// �Զ����õ���
		private void SetPointName() {
			int tNumber=0;
			String mName="C1";
			String[] tKeys = new String[] { "ID", "NAME" };
			Cursor mCursor = MyDataBaseAdapter.fetchAllData(
					MyDataBaseAdapter.PointTable, tKeys);
			if (mCursor.getCount() > 0) {
				mCursor.moveToLast();
				String tS = mCursor.getString(1);
				// ������һλ��������
				if ((int) (tS.charAt(tS.length() - 1)) < 48
						|| (int) (tS.charAt(tS.length() - 1)) > 57) {
					mName = tS + "1";
				} else// ���������
				{
					for (int i = tS.length() - 1; i > -1; i--) {
						if (((int) tS.charAt(i)) < 48 || ((int) tS.charAt(i)) > 57) {
							tNumber = Integer.valueOf(tS.substring(i + 1)) + 1;
							mName = tS.substring(0, i + 1) + tNumber;
							break;
						}
						if (i == 0) {
							tNumber = Integer.valueOf(tS) + 1;
							mName = String.valueOf(tNumber);
						}
					}
				}
				Cursor nCursor = PadApplication.FindPointByName(mName);
				while(nCursor.getCount()>0){
					for (int i = mName.length() - 1; i > -1; i--) {
						if (((int) mName.charAt(i)) < 48 || ((int) mName.charAt(i)) > 57) {
							tNumber = Integer.valueOf(mName.substring(i + 1)) + 1;
							mName = mName.substring(0, i + 1) + tNumber;
							break;
						}
						if (i == 0) {
							tNumber = Integer.valueOf(tS) + 1;
							mName = String.valueOf(tNumber);
						}
					}
					nCursor = PadApplication.FindPointByName(mName);
				}
			} else {
				mName = "C1";
			}
			txtPointName.setText(mName);
		}
	//�����߶��󽻵�
	private CartesianCoordinatePoint CalculatePoint(CartesianCoordinatePoint Point1,CartesianCoordinatePoint Point2
			,CartesianCoordinatePoint Point3,CartesianCoordinatePoint Point4) {
		CartesianCoordinatePoint point=new CartesianCoordinatePoint();
		double k1=0,k2,b1,b2;
		double x1=Point1.X;
		double y1=Point1.Y;
		double x2=Point2.X;
		double y2=Point2.Y;
		double x3=Point3.X;
		double y3=Point3.Y;
		double x4=Point4.X;
		double y4=Point4.Y;
		
		//������ƽ����Y��
		if(x1==x2){
			point.X=x1;
			k2=(y4-y3)/(x4-x3);
			b2=y3-k2*x3;
			point.Y=k2*point.X+b2;	
		}
		if(x3==x4){
			point.X=x3;
			k1=(y2-y1)/(x2-x1);
			b1=y1-k1*x1;
			point.Y=k1*point.X+b1;	
		}
		//�����߶���ƽ����Y��
		if(x1!=x2 && x3!=x4){

			k1=(y2-y1)/(x2-x1);
			k2=(y4-y3)/(x4-x3);
			b1=y1-k1*x1;
			b2=y3-k2*x3;
			if(k1==k2){
				point=null;
			}else{
			    point.X=(b2-b1)/(k1-k2);
			    point.Y=k1* point.X+b1;
			}
			
		}
		Log.i("x",String.valueOf(k1));
		return point;
	}
	//һ�㴹ֱ��һ���߶εĴ���
	private CartesianCoordinatePoint CalculatePoint(CartesianCoordinatePoint Point1,CartesianCoordinatePoint Point2
			,CartesianCoordinatePoint Point3) {
		CartesianCoordinatePoint point=new CartesianCoordinatePoint();
		double k1,k2,b1,b2;
		double x1=Point1.X;
		double y1=Point1.Y;
		double x2=Point2.X;
		double y2=Point2.Y;
		double x3=Point3.X;
		double y3=Point3.Y;

		
		//ֱ������ƽ����Y��

		if(x1==x2){
			point.X=x1;
			point.Y=y3;	
		}
		//�����߶���ƽ����Y��
		if(x1!=x2){
			k1=(y2-y1)/(x2-x1);
			k2=-1/k1;
			b1=y1-k1*x1;
			b2=y3-k2*x3;
			if(k1==k2){
				point=null;
			}else{
			    point.X=(b2-b1)/(k1-k2);
			    point.Y=k1* point.X+b1;
			}
		}
		return point;
	}
}
