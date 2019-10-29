package com.padsurveysystem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

//�����������
public class CalculateDistanceActivity extends Activity {
	
	Button btnEnter;
	Button btnStart;
	Button btnEnd;
	TextView tvStart;
	TextView tvEnd;
	TextView tvH;
	TextView tvV;
	TextView tvA;
	TextView tvP;
	
    String selectPointName;
    int selectPointFlag;
    SurveyPoint pointStart;
    SurveyPoint pointEnd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_tools_calculatedistance);
		btnEnter=(Button) this.findViewById(R.id.button_enter_view_tools_calculatedistance);
		btnEnter.setOnClickListener(new ButtonEnterClickEvent());
		btnStart=(Button) this.findViewById(R.id.button_start_view_tools_calculatedistance);
		btnStart.setOnClickListener(new ButtonStartClickEvent());
		btnEnd=(Button) this.findViewById(R.id.button_end_view_tools_calculatedistance);
		btnEnd.setOnClickListener(new ButtonEndClickEvent());
		
		tvStart=(TextView) this.findViewById(R.id.edittext_start_view_tools_calculatedistance);
		tvEnd=(TextView) this.findViewById(R.id.edittext_end_view_tools_calculatedistance);
	
		tvH=(TextView) this.findViewById(R.id.textview_h_view_tools_calculatedistance);
		tvV=(TextView) this.findViewById(R.id.textview_v_view_tools_calculatedistance);
		tvA=(TextView) this.findViewById(R.id.textview_a_view_tools_calculatedistance);
		tvP=(TextView) this.findViewById(R.id.textview_p_view_tools_calculatedistance);
	}
	// ����ת��
	private class ButtonEnterClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			pointStart=new SurveyPoint();
			Cursor sCursor = PadApplication.FindPointByName(tvStart.getText().toString());
			if(sCursor.getCount()<1){
				Toast toast = Toast.makeText(
						CalculateDistanceActivity.this, "��"
								+ tvStart.getText().toString().trim() + "�����ڣ�",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 100);
				toast.show();
				return;
			}
			int PointFlag = sCursor.getInt(0);
			// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
			if (PointFlag == 1 || PointFlag == 2) {
				CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
				GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
				pgc.Latitude = SurveyMath.DEGToRadian(sCursor.getDouble(3));
				pgc.Longitude = SurveyMath.DEGToRadian(sCursor.getDouble(4));
				pgc.Height = sCursor.getDouble(5);

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

				pointStart.X = pcc.X;
				pointStart.Y = pcc.Y;
				pointStart.Elevation=pcc.H;
			}
			// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
			if (PointFlag == 3 || PointFlag == 4) {
				// ���귴��
				pointStart.X = sCursor.getDouble(3);
				pointStart.Y = sCursor.getDouble(4);
				pointStart.Elevation = sCursor.getDouble(5);
			}
			
			pointEnd=new SurveyPoint();
			Cursor eCursor = PadApplication.FindPointByName(tvEnd.getText().toString());
			if(eCursor.getCount()<1){
				Toast toast = Toast.makeText(
						CalculateDistanceActivity.this, "��"
								+ tvEnd.getText().toString().trim() + "�����ڣ�",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 100);
				toast.show();
				return;
			}
			PointFlag = eCursor.getInt(0);
			// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
			if (PointFlag == 1 || PointFlag == 2) {
				CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
				GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
				pgc.Latitude = SurveyMath.DEGToRadian(eCursor.getDouble(3));
				pgc.Longitude = SurveyMath.DEGToRadian(eCursor.getDouble(4));
				pgc.Height = eCursor.getDouble(5);

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

				pointEnd.X = pcc.X;
				pointEnd.Y = pcc.Y;
				pointEnd.Elevation=pcc.H;
			}
			// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
			if (PointFlag == 3 || PointFlag == 4) {
				// ���귴��
				pointEnd.X = eCursor.getDouble(3);
				pointEnd.Y = eCursor.getDouble(4);
				pointEnd.Elevation= eCursor.getDouble(5);
			}
			
			double dH,dV,dP;
			DecimalFormat df = new DecimalFormat("0.000"); // ����һ����ʽ����f
			dH=SurveyMath.GetDistance(pointStart, pointEnd);
			dV=Math.abs(pointStart.Elevation-pointEnd.Elevation);
			tvH.setText("ƽ�ࣺ" + df.format(dH));
			tvV.setText("�߲" + df.format(dV));
			SurveyAngle sA=new SurveyAngle(SurveyMath.GetAzimuth(pointStart, pointEnd));
			tvA.setText("��λ�ǣ�" + String.valueOf( sA.GetSubDegree()+"��" +
			sA.GetSubMinute()+"��" + sA.GetSubSecond(0))+"��");
			dP=Math.tan(dV/dH);
			sA.valueOfRadian(dP);
			tvP.setText("�¶ȣ�" + df.format(sA.GetDEG())+ "�ȣ��ٷ�֮" + df.format(dV/dH*100) + "��");
			
		};
	}
	// ѡ��ת�ǵ�
	private class ButtonStartClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// �½�һ��Intent
			
			selectPointFlag=0;
			Intent intent = new Intent();
			intent.putExtra("CoorStyle", 3);
			// �ƶ�intentҪ��������
			intent.setClass(CalculateDistanceActivity.this,
					SelectPointActivity.class);
			// ����һ���µ�Activity
			startActivityForResult(intent, 0);
		}
	}
	// ѡ����ӵ�
	private class ButtonEndClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// �½�һ��Intent
			selectPointFlag=1;
			Intent intent = new Intent();
			intent.putExtra("CoorStyle", 3);
			// �ƶ�intentҪ��������
			intent.setClass(CalculateDistanceActivity.this,
					SelectPointActivity.class);
			// ����һ���µ�Activity
			startActivityForResult(intent, 0);
		}
	}

	// ��д�÷������÷����Իص��ķ�ʽ����ȡָ��Activity���صĽ��
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// ��requestCode��resultCodeͬʱΪ0��Ҳ���Ǵ����ض��Ľ��
		if (requestCode == 0 && resultCode == RESULT_OK) {
			selectPointName =intent.getStringExtra("PointName");
			if(selectPointFlag==0){
				tvStart.setText(selectPointName);
			}
			if(selectPointFlag==1){
				tvEnd.setText(selectPointName);
			}


		}
	}

	
}
