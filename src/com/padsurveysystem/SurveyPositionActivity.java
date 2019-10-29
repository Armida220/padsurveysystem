package com.padsurveysystem;

import java.text.DecimalFormat;

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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

//�����������
public class SurveyPositionActivity extends Activity implements
		SensorEventListener {

	// �˵���ͼ�ؼ�
	Button btnEnter;
	RadioButton rbtGrid;
	RadioButton rbtLatlon;
	RadioButton rbtNoMap;
	RadioButton rbtGoogleMap;
	RadioButton rbtAnotherMap;

	// λ����ͼ�ؼ�
	MapFragment mapView;
	Button btnSave;
	GoogleMap googleMap;
	TextView tvX;
	TextView tvY;
	TextView tvZ;
	TextView tvS;
	TextView tvD;

	// ��ʾ��������ͼ
	MapTilesView ivMapView;
	ZoomControls zooMap;
	ImageButton imbLocation;
	boolean mFreshAnotherMap = true;// ��ǰλ�������ڵ�������ͼ������
	boolean mHaveLocation = false;// �Ƿ��л�ȡλ����Ϣ
	// ������ʾָ�����ͼƬ
	ImageView CompassImage;
	// ��¼ָ����ͼƬת���ĽǶ�
	float currentDegree = 0;

	// ���������Sensor������
	SensorManager mSensorManager;
	// GPSλ�÷���
	LocationManager locationManager;
	double mX = 0;
	double mY = 0;
	double Latitude = 100;
	double Longitude = 400;
	double Altitude = 0;
	double Direction = 0;// ����
	double Speed = 0;// �ٶ�

	View viewNormal;
	View viewGoogleMap;
	View viewAnotherMap;
	int currentView = 0;// ��ǰ��ʾ��ͼ0-��ͨ��1-googlemap,2-anothermap
	boolean isShowSetView = false;// �Ƿ���ʾ��������ͼ
	public static SurveyPositionActivity instance = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// instance=SurveyPositionNormalActivity.this;

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewNormal = (View) inflater.inflate(
				R.layout.view_survey_position_normal, null);
		viewGoogleMap = (View) inflater.inflate(
				R.layout.view_survey_position_googlemap, null);
		viewAnotherMap = (View) inflater.inflate(
				R.layout.view_survey_position_anothermap, null);
		showNormalView();
		// ��ȡ����Ĵ������������
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// GPS�������
		String serviceString = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceString);

		/*
		 * if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		 * { final android.app.AlertDialog.Builder builder = new
		 * AlertDialog.Builder( this); builder.setTitle("PadSurvey");
		 * builder.setMessage("���GPS���ܣ�"); builder.setPositiveButton("ȷ��", new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { //
		 * TODO Auto-generated method stub finish();
		 * 
		 * } }); builder.create().show();
		 * 
		 * }
		 */

		Criteria criteria = new Criteria();

		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting Current Location
		// Location location = locationManager.getLastKnownLocation(provider);

		locationManager.requestLocationUpdates(provider, 1000, 0,
				locationListener);
	}

	private void showNormalView() {
		setTitle("ʵʱλ��"); // ���ô��ڱ���������
		setContentView(viewNormal);
		currentView = 0;
		// ��ʼ���ؼ�
		tvX = (TextView) findViewById(R.id.text_surveyposition_X);
		tvY = (TextView) findViewById(R.id.text_surveyposition_Y);
		tvZ = (TextView) findViewById(R.id.text_surveyposition_Z);
		tvS = (TextView) findViewById(R.id.text_surveyposition_speed);
		tvD = (TextView) findViewById(R.id.text_surveyposition_direction);

		btnSave = (Button) this.findViewById(R.id.button_surveyposition_save);

		btnSave.setOnClickListener(new ButtonSaveClickEvent());

		// ��ȡ��������ʾָ�����ͼƬ
		CompassImage = (ImageView) findViewById(R.id.imageview_compass_view_survey_position_normal);

	}

	private void showGoogleMapView() {
		setTitle("ʵʱλ��-�ȸ��ͼ"); // ���ô��ڱ���������
		setContentView(viewGoogleMap);
		currentView = 1;
		btnSave = (Button) findViewById(R.id.button_save_view_survey_position_googlemap);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		tvX = (TextView) findViewById(R.id.textView_x_view_survey_position_googlemap);
		tvY = (TextView) findViewById(R.id.textView_y_view_survey_position_googlemap);
		tvZ = (TextView) findViewById(R.id.textView_z_view_survey_position_googlemap);
		
		if(mHaveLocation){
			setLocationInformation();			
		}

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else {
			// Getting reference to the SupportMapFragment of activity_main.xml
			mapView = (MapFragment) getFragmentManager().findFragmentById(
					R.id.fragment_googlemap_view_survey_position_googlemap);

			// Getting GoogleMap object from the fragment
			googleMap = mapView.getMap();
			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);
			googleMap.setMapType(googleMap.MAP_TYPE_HYBRID);

		}

	}

	private void showAnotherMapView() {
		setTitle("ʵʱλ��-��������ͼ"); // ���ô��ڱ���������

		if (PadApplication.AnotherMapFileName.trim() == "") {
			final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
					SurveyPositionActivity.this);
			builder.setTitle("PadSurvey");
			builder.setMessage("�����õ�������ͼ��");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			});
			builder.create().show();
			if (currentView == 1) {
				showGoogleMapView();
			}
			if (currentView == 1) {
				showNormalView();
			}
			return;
		}

		// ��ʾ��������ͼ
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		viewAnotherMap = (View) inflater.inflate(
				R.layout.view_survey_position_anothermap, null);
		setContentView(viewAnotherMap);
		btnSave = (Button) findViewById(R.id.button_save_view_survey_position_anothermap);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		imbLocation = (ImageButton) findViewById(R.id.locationbutton_view_survey_position_anothermap);
		tvX = (TextView) findViewById(R.id.textView_x_view_survey_position_anothermap);
		tvY = (TextView) findViewById(R.id.textView_y_view_survey_position_anothermap);
		tvZ = (TextView) findViewById(R.id.textView_z_view_survey_position_anothermap);
		ivMapView = (MapTilesView) findViewById(R.id.maptiles_view_survey_position_anothermap);
		zooMap = (ZoomControls) findViewById(R.id.zoomcontrols_view_survey_position_anothermap);

		currentView = 2;
		ShowAnotherMap();
		if(mHaveLocation){
			setLocationInformation();			
		}

		
		zooMap.setOnZoomInClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ivMapView.getLevelNumber() == ivMapView.getLevelCount()) {
					return;
				}
				int iNumber = ivMapView.getLevelNumber() + 1;
				ivMapView.ZoomMap(iNumber);
			}
		});

		zooMap.setOnZoomOutClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ivMapView.getLevelNumber() == 1) {
					return;
				}
				int iNumber = ivMapView.getLevelNumber() - 1;
				ivMapView.ZoomMap(iNumber);
			}

		});
		imbLocation.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mHaveLocation) {
					ivMapView.setViewCenter(mY, mX);
				}
			}

		});
	}

	private void showSetView() {

		setContentView(R.layout.view_survey_setting_viewstyle);
		setTitle("��ʾ����");
		isShowSetView = true;
		btnEnter = (Button) findViewById(R.id.button_save_survey_setting_viewstyle);
		btnEnter.setOnClickListener(new ButtonEnterClickEvent());

		rbtNoMap = (RadioButton) findViewById(R.id.radiobutton_nomap_survey_setting_viewstyle);
		rbtGoogleMap = (RadioButton) findViewById(R.id.radiobutton_googlemap_survey_setting_viewstyle);
		rbtAnotherMap = (RadioButton) findViewById(R.id.radiobutton_anothermap_survey_setting_viewstyle);

		rbtGrid = (RadioButton) findViewById(R.id.radiobutton_grid_survey_setting_viewstyle);
		rbtLatlon = (RadioButton) findViewById(R.id.radiobutton_latlon_survey_setting_viewstyle);

		if (PadApplication.PointViewFormat == 0) {
			rbtLatlon.setChecked(true);
			rbtGrid.setChecked(false);
		} else {
			rbtLatlon.setChecked(false);
			rbtGrid.setChecked(true);
		}
		if (currentView == 0) {
			rbtNoMap.setChecked(true);
			rbtGoogleMap.setChecked(false);
			rbtAnotherMap.setChecked(false);
		}
		if (currentView == 1) {
			rbtNoMap.setChecked(false);
			rbtGoogleMap.setChecked(true);
			rbtAnotherMap.setChecked(false);
		}
		if (currentView == 2) {
			rbtNoMap.setChecked(false);
			rbtGoogleMap.setChecked(false);
			rbtAnotherMap.setChecked(true);
		}

	}

	// GPS������
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

			getLocationInformation(location);
			if (currentView == 1) {
				locationMap(location);
			}
			if (currentView == 2) {
				if (mFreshAnotherMap == true) {
					locationAnotherMap(location);
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	// ��ȡ������
	private void getLocationInformation(Location location) {

		if (location != null) {
			mHaveLocation = true;
			Latitude = location.getLatitude();
			Longitude = location.getLongitude();
			Altitude = location.getAltitude();
			Direction = location.getBearing();// ����
			Speed = location.getSpeed() * 3.6f;// �ٶ�
		} else {
			mHaveLocation = false;

		}
		setLocationInformation();
	}

	private void setLocationInformation() {
		DecimalFormat df = new DecimalFormat("0.000"); // ����һ����ʽ����f

		float aX = 0;
		float aY = 0;
		SurveyAngle bA = new SurveyAngle();
		SurveyAngle lA = new SurveyAngle();
		bA.valueOfDEG(Latitude);
		lA.valueOfDEG(Longitude);
		// ������ʾ��ʽΪ������꾭γ��
		if (PadApplication.PointViewFormat == 0) {
			if (tvX != null) {
				if (Latitude < 0) {
					tvX.setText("��γ�� " + Math.abs(bA.GetSubDegree()) + "��"
							+ bA.GetSubMinute() + "��"
							+ df.format(bA.GetSubSecond(3)) + "��");
				} else {
					tvX.setText("��γ�� " + Math.abs(bA.GetSubDegree()) + "��"
							+ bA.GetSubMinute() + "��" + bA.GetSubSecond(3)
							+ "��");
				}

			}
			if (tvY != null) {
				if (Longitude < 0) {
					tvY.setText("���� ��" + Math.abs(lA.GetSubDegree()) + "��"
							+ lA.GetSubMinute() + "��" + lA.GetSubSecond(3)
							+ "��");
				} else {
					tvY.setText("������ " + Math.abs(lA.GetSubDegree()) + "��"
							+ lA.GetSubMinute() + "��" + lA.GetSubSecond(3)
							+ "��");
				}

			}
			if (tvZ != null) {
				tvZ.setText("�߶ȣ�" + df.format(Altitude));
			}
		}
		// ������ʾ��ʽΪ��������
		else {
			// ʹ���Ѿ����ڵ�����ϵͳ
			if (PadApplication.UseCoordinateTransfomation == 0) {

				CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
				GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
				pgc.Latitude = bA.GetRadian();
				pgc.Longitude = lA.GetRadian();
				CoordinateTransform.GeodeticToCartesian(pcc, pgc,
						PadApplication.CurrentCoordinateSystem);

				if (tvX != null) {
					tvX.setText("�����꣺ " + df.format(pcc.X));
				}
				if (tvY != null) {
					tvY.setText("�����꣺ " + df.format(pcc.Y));
				}
				if (tvZ != null) {
					tvZ.setText("�߶ȣ�" + df.format(Altitude));
				}

			}
			// ʹ�ù���У���߲���
			if (PadApplication.UseCoordinateTransfomation == 1) {
				double[] WGS84Coordinate = new double[3];
				double[] UserCoordinate = new double[3];
				WGS84Coordinate[0] = bA.GetRadian();
				WGS84Coordinate[1] = lA.GetRadian();
				WGS84Coordinate[2] = Altitude;
				PadApplication.CoordinateTrans.WGS84TransCartesian(
						WGS84Coordinate, UserCoordinate);
				if (tvX != null) {
					tvX.setText("�����꣺ " + df.format(UserCoordinate[0]));
				}
				if (tvY != null) {
					tvY.setText("�����꣺ " + df.format(UserCoordinate[1]));
				}
				if (tvZ != null) {
					tvZ.setText("�߶ȣ�" + df.format(UserCoordinate[2]));
				}
			}
		}

		if (tvS != null) {
			tvS.setText(String.valueOf("�ٶȣ�" + df.format(Speed)));
		}
		if (tvD != null) {
			tvD.setText(String.valueOf("����" + df.format(Direction)));
		}
	}

	// ����ȸ��ͼ
	private void locationMap(Location location) {
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	// ���� ��������ͼ
	private void locationAnotherMap(Location location) {

		double aX = 0;
		double aY = 0;
		if (location != null) {
			Latitude = location.getLatitude();
			Longitude = location.getLongitude();
			Altitude = location.getAltitude();
			Direction = location.getBearing();// ����
			Speed = location.getSpeed() * 3.6f;// �ٶ�
			SurveyAngle bA = new SurveyAngle();
			SurveyAngle lA = new SurveyAngle();
			bA.valueOfDEG(Latitude);
			lA.valueOfDEG(Longitude);

			// ʹ���Ѿ����ڵ�����ϵͳ
			if (PadApplication.UseCoordinateTransfomation == 0) {

				CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
				GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
				pgc.Latitude = bA.GetRadian();
				pgc.Longitude = lA.GetRadian();
				CoordinateTransform.GeodeticToCartesian(pcc, pgc,
						PadApplication.CurrentCoordinateSystem);

				aX = pcc.X;
				aY = pcc.Y;
			}
			// ʹ�ù���У���߲���
			if (PadApplication.UseCoordinateTransfomation == 1) {
				double[] WGS84Coordinate = new double[3];
				double[] UserCoordinate = new double[3];
				WGS84Coordinate[0] = bA.GetRadian();
				WGS84Coordinate[1] = lA.GetRadian();
				WGS84Coordinate[2] = Altitude;
				PadApplication.CoordinateTrans.WGS84TransCartesian(
						WGS84Coordinate, UserCoordinate);
				aX = UserCoordinate[0];
				aY = UserCoordinate[1];
			}
			mX = aX;
			mY = aY;
			if (mFreshAnotherMap) {
				if (mHaveLocation) {
					ivMapView.setViewCenter(mY, mX);
				}
			}

		}
	}

	private void ShowAnotherMap() {

		String strFilePath = "mnt/sdcard/PadSurveyData/AnotherMap/"
				+ PadApplication.AnotherMapFileName;
		ivMapView.initializeMap(strFilePath);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// Ϊϵͳ�ķ��򴫸���ע�������
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, locationListener);

	}

	@Override
	protected void onPause() {
		// ȡ��ע��
		mSensorManager.unregisterListener(this);

		super.onPause();
		// ȡ��GPS����
		locationManager.removeUpdates(locationListener);
	}

	@Override
	protected void onStop() {
		// ȡ��ע��
		mSensorManager.unregisterListener(this);
		super.onStop();
		locationManager.removeUpdates(locationListener);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// ����ϻ�ȡ����event�Ĵ���������
		int sensorType = event.sensor.getType();
		// // ģ�����ϻ�ȡ����event�Ĵ���������
		// int sensorType = event.type;
		switch (sensorType) {
		case Sensor.TYPE_ORIENTATION:
			// ��ȡ��Z��ת���ĽǶȡ�
			float degree = event.values[0];
			// ������ת����������ת��degree�ȣ�
			RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			// ���ö����ĳ���ʱ��
			ra.setDuration(200);
			// ���ж���
			CompassImage.startAnimation(ra);
			currentDegree = -degree;
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	// ����۲�����
	private class ButtonSaveClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(Latitude==100){
				Toast toast = Toast.makeText(SurveyPositionActivity.this, "λ�û�ȡ�У����Ժ�",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 100);
				toast.show();
				return;
			}
			// �ƶ�intentҪ��������
			Intent intent = new Intent(SurveyPositionActivity.this,
					SavePointActivity.class);
			intent.putExtra("X", Latitude);
			intent.putExtra("Y", Longitude);
			intent.putExtra("Z", Altitude);
			// ����һ���µ�Activity
			startActivity(intent);
		}
	}

	// �˵��Ĳ���
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!isShowSetView) {
				super.openOptionsMenu();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isShowSetView) {
				if (currentView == 0) {
					showNormalView();
				}
				if (currentView == 1) {
					showGoogleMapView();
				}
				if (currentView == 2) {
					showAnotherMapView();
				}
				isShowSetView = false;
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
		// ���¼����Ϸ��ذ�ť

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "����");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == 1) {
			// ��ʾ������ͼ
			showSetView();
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private class ButtonEnterClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (rbtGrid.isChecked()) {
					PadApplication.PointViewFormat = 1;
				} else {
					PadApplication.PointViewFormat = 0;
				}
				if (rbtNoMap.isChecked()) {
					currentView = 0;
					// ��ͼģʽ
					showNormalView();
				}
				if (rbtGoogleMap.isChecked()) {
					currentView = 1;
					showGoogleMapView();

					// ��ʾ���ȸ��ͼ��λ����ͼ
				}
				if (rbtAnotherMap.isChecked()) {
					currentView = 2;
					// ��ʾ����������ͼ��λ����ͼ
					showAnotherMapView();
				}
				mHaveLocation=false;
				isShowSetView = false;
				PadApplication.ReSetInformation();
				setLocationInformation();
			} catch (NumberFormatException n) {

			}

		}
	}
}
