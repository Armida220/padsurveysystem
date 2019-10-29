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
public class SelectStakingoutLineActivity extends Activity {
	
	Button btnStartPoint;
	Button btnEndPoint;
	Button btnStakingoutPoint;
	
	
	GoogleMap googleMap;
	TextView tvStartPointName;
	TextView tvEndPointName;
	TextView tvStartStation;
	
	int selectPointFlag;
	public static String  StartPointName=null;
	public static String  EndPointName=null;
	public static String  StartStation=null;
	  		

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_survey_stakingoutline);
		tvStartPointName = (TextView) findViewById(R.id.edittext_start_view_survey_stakingoutline);
		tvEndPointName = (TextView) findViewById(R.id.edittext_end_view_survey_stakingoutline);
		tvStartStation= (TextView) findViewById(R.id.edittext_station_view_survey_stakingoutline);
		
		if(StartPointName!=null){
			tvStartPointName.setText(StartPointName);		
		}
		if(EndPointName!=null){
			tvEndPointName.setText(EndPointName);	
		}
		if(StartStation!=null){
			tvStartStation.setText(StartStation);		
		}
		btnStartPoint= (Button) this.findViewById(R.id.button_start_view_survey_stakingoutline);
		btnStartPoint.setOnClickListener(new ButtonStartClickEvent());
		
		btnEndPoint= (Button) this.findViewById(R.id.button_end_view_survey_stakingoutline);
		btnEndPoint.setOnClickListener(new ButtonEndClickEvent());
		
		btnStakingoutPoint= (Button) this.findViewById(R.id.button_stakingout_view_survey_stakingoutline);
		btnStakingoutPoint.setOnClickListener(new ButtonStakingoutClickEvent());
	}
	//��ʼ������
	private class ButtonStakingoutClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			
			
			if (tvStartPointName.getText().toString().trim().length()<1)
			{
				Toast.makeText(SelectStakingoutLineActivity.this, "��ʼ�㲻��Ϊ��!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (tvEndPointName.getText().toString().trim().length()<1 )
			{
				Toast.makeText(SelectStakingoutLineActivity.this, "�����㲻��Ϊ��!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (tvStartStation.getText().toString().trim().length()<1 )
			{
				Toast.makeText(SelectStakingoutLineActivity.this, "��������ʼ���!", Toast.LENGTH_SHORT).show();
				return;
			}
			StartPointName=tvStartPointName.getText().toString().trim();
			EndPointName=tvEndPointName.getText().toString().trim();
			StartStation=tvStartStation.getText().toString().trim();
			

			Cursor sCursor = PadApplication.FindPointByName(StartPointName);
			if (sCursor.getCount()<1)
			{
				Toast.makeText(SelectStakingoutLineActivity.this, "��ʼ�㲻����!", Toast.LENGTH_SHORT).show();
				return;
			}
			Cursor eCursor = PadApplication.FindPointByName(EndPointName);
			if (eCursor.getCount()<1) {
				Toast.makeText(SelectStakingoutLineActivity.this, "�����㲻����!", Toast.LENGTH_SHORT).show();
				return;
			}
			bundle.putString("StartPointName",tvStartPointName.getText().toString());
			bundle.putString("EndPointName", tvEndPointName.getText().toString());
			bundle.putString("StartStation", tvStartStation.getText().toString());
			intent.putExtras(bundle);
			// �ƶ�intentҪ��������
			intent.setClass(SelectStakingoutLineActivity.this,
					StakingoutLineActivity.class);
			// ����һ���µ�Activity
			startActivity(intent);
		}
	}
	// ѡ�����
	private class ButtonStartClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// �½�һ��Intent
			selectPointFlag=0;
			Intent intent = new Intent();
			intent.putExtra("CoorStyle", 3);
			// �ƶ�intentҪ��������
			intent.setClass(SelectStakingoutLineActivity.this,
					SelectPointActivity.class);
			// ����һ���µ�Activity
			startActivityForResult(intent, 0);
		}
	}
	// ѡ���յ�
	private class ButtonEndClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// �½�һ��Intent
			selectPointFlag=1;
			Intent intent = new Intent();
			intent.putExtra("CoorStyle", 3);
			// �ƶ�intentҪ��������
			intent.setClass(SelectStakingoutLineActivity.this,
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
			String selectPointName =intent.getStringExtra("PointName");
			if(selectPointFlag==0){
				StartPointName=selectPointName;
				tvStartPointName.setText(selectPointName);
			}
			if(selectPointFlag==1){
				EndPointName=selectPointName;
				tvEndPointName.setText(selectPointName);
			}


		}
	}

	
}
