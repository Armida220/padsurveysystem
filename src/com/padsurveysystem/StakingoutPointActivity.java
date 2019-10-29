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

import android.util.Log;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//�����������
public class StakingoutPointActivity extends Activity implements
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
	Button btnSelectPoint;
	GoogleMap googleMap;
	TextView tvPointName;
	TextView tvDX;
	TextView tvDY;
	TextView tvS;
	TextView tvD;
	// ������ʾָ�����ͼƬ
	ImageView CompassImage;
	// ��¼ָ����ͼƬת���ĽǶ�
	float currentDegree = 0;

	// ���������Sensor������
	SensorManager mSensorManager;
	// GPSλ�÷���
	LocationManager locationManager;
	double Latitude = 0;
	double Longitude = 0;
	double Altitude = 0;
	double Direction = 0;// ����
	double Speed = 0;// �ٶ�

	// ��ʾ��������ͼ
	MapTilesView ivMapView;
	ZoomControls zooMap;
	ImageButton imbLocation;
	boolean mFreshAnotherMap = true;// ��ǰλ�������ڵ�������ͼ������
	boolean mHaveLocation = false;// �Ƿ��л�ȡλ����Ϣ
	double mX = 0;
	double mY = 0;

	double targetX, targetY;
	double currentX, currentY;
	double drawGoogleMapLat, drawGoogleMapLon;
	View viewNormal;
	View viewGoogleMap;
	View viewAnotherMap;
	int currentView = 0;// ��ǰ��ʾ��ͼ0-��ͨ��1-googlemap,2-anothermap
	boolean isShowSetView = false;// �Ƿ���ʾ��������ͼ

	public static String PointName = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// instance=SurveyPositionNormalActivity.this;

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewNormal = (View) inflater.inflate(
				R.layout.view_survey_stakingoutpoint_normal, null);
		viewGoogleMap = (View) inflater.inflate(
				R.layout.view_survey_stakingoutpoint_googlemap, null);
		viewAnotherMap = (View) inflater.inflate(
				R.layout.view_survey_stakingoutpoint_anothermap, null);
		showNormalView();
		// ��ȡ����Ĵ�������������
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// GPS�������
		String serviceString = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceString);
		Criteria criteria = new Criteria();
		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		locationManager.requestLocationUpdates(provider, 1000, 0,
				locationListener);

		// ��������ϴη����ĵ㣬���������ϴεĵ�
		stakingOutPoint();
	}

	private void showNormalView() {
		setTitle("������-��ͼģʽ"); // ���ô��ڱ���������
		setContentView(viewNormal);
		currentView = 0;
		// ��ʼ���ؼ�
		tvPointName = (TextView) findViewById(R.id.textview_pointname_view_survey_stakingoutpoint_normal);
		tvDX = (TextView) findViewById(R.id.textview_dx_view_survey_stakingoutpoint_normal);
		tvDY = (TextView) findViewById(R.id.textview_dy_view_survey_stakingoutpoint_normal);

		tvS = (TextView) findViewById(R.id.textview_speed_view_survey_stakingoutpoint_normal);
		tvD = (TextView) findViewById(R.id.textview_direction_view_survey_stakingoutpoint_normal);

		btnSave = (Button) this
				.findViewById(R.id.button_save_view_survey_stakingoutpoint_normal);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());

		btnSelectPoint = (Button) this
				.findViewById(R.id.button_select_view_survey_stakingoutpoint_normal);
		btnSelectPoint.setOnClickListener(new buttonSelectPointClickEvent());
		// ��ȡ��������ʾָ�����ͼƬ
		CompassImage = (ImageView) findViewById(R.id.imageview_compass_view_survey_stakingoutpoint_normal);
	}

	private void showGoogleMapView() {
		setTitle("������-�ȸ��ͼģʽ"); // ���ô��ڱ���������
		setContentView(viewGoogleMap);
		currentView = 1;

		btnSelectPoint = (Button) this
				.findViewById(R.id.button_select_view_survey_stakingoutpoint_googlemap);
		btnSelectPoint.setOnClickListener(new buttonSelectPointClickEvent());
		btnSave = (Button) findViewById(R.id.button_save_view_survey_stakingoutpoint_googlemap);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		tvPointName = (TextView) findViewById(R.id.textview_pointname_view_survey_stakingoutpoint_googlemap);
		tvDX = (TextView) findViewById(R.id.textview_dx_view_survey_stakingoutpoint_googlemap);
		tvDY = (TextView) findViewById(R.id.textview_dy_view_survey_stakingoutpoint_googlemap);
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
			mapView = (MapFragment) getFragmentManager()
					.findFragmentById(
							R.id.fragment_googlemap_view_survey_stakingoutpoint_googlemap);

			// Getting GoogleMap object from the fragment
			googleMap = mapView.getMap();
			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);
			googleMap.setMapType(googleMap.MAP_TYPE_HYBRID);
			if (PointName != null) {
				LatLng MELBOURNE = new LatLng(drawGoogleMapLat,
						drawGoogleMapLon);
				Marker melbourne = googleMap.addMarker(new MarkerOptions()
						.position(MELBOURNE)
						.title("������")
						.snippet(PointName)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.stakingout_point)));
			}
		}

	}

	private void showAnotherMapView() {
		setTitle("������-��������ͼģʽ"); // ���ô��ڱ���������

		if (PadApplication.AnotherMapFileName.trim() == "") {
			final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
					StakingoutPointActivity.this);
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
				R.layout.view_survey_stakingoutpoint_anothermap, null);
		setContentView(viewAnotherMap);
		btnSelectPoint = (Button) this
				.findViewById(R.id.button_select_view_survey_stakingoutpoint_anothermap);
		btnSelectPoint.setOnClickListener(new buttonSelectPointClickEvent());
		btnSave = (Button) findViewById(R.id.button_save_view_survey_stakingoutpoint_anothermap);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		tvPointName = (TextView) findViewById(R.id.textview_pointname_view_survey_stakingoutpoint_anothermap);
		tvDX = (TextView) findViewById(R.id.textview_dx_view_survey_stakingoutpoint_anothermap);
		tvDY = (TextView) findViewById(R.id.textview_dy_view_survey_stakingoutpoint_anothermap);

		imbLocation = (ImageButton) findViewById(R.id.locationbutton_view_survey_stakingoutpoint_anothermap);
		ivMapView = (MapTilesView) findViewById(R.id.maptiles_view_survey_stakingoutpoint_anothermap);
		zooMap = (ZoomControls) findViewById(R.id.zoomcontrols_view_survey_stakingoutpoint_anothermap);

		currentView = 2;
		ShowAnotherMap();

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
					ivMapView.setViewCenter(currentY, currentX);
				}
			}

		});
	}

	private void ShowAnotherMap() {
		String strFilePath = "mnt/sdcard/PadSurveyData/AnotherMap/"
				+ PadApplication.AnotherMapFileName;
		ivMapView.initializeMap(strFilePath);
	}

	private void showSetView() {

		setContentView(R.layout.view_survey_stakingoutpoint_viewstyle);
		setTitle("��ʾ����");
		isShowSetView = true;
		btnEnter = (Button) findViewById(R.id.button_save_survey_stakingoutpoint_viewstyle);
		btnEnter.setOnClickListener(new ButtonEnterClickEvent());

		rbtNoMap = (RadioButton) findViewById(R.id.radiobutton_nomap_survey_stakingoutpoint_viewstyle);
		rbtGoogleMap = (RadioButton) findViewById(R.id.radiobutton_googlemap_survey_stakingoutpoint_viewstyle);
		rbtAnotherMap = (RadioButton) findViewById(R.id.radiobutton_anothermap_survey_stakingoutpoint_viewstyle);

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
			getLocationInfomation(location);
			if (currentView == 1) {
				locationMap(location);
			}
			if (currentView == 2) {
				locationAnotherMap(location);
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
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};

	// ��ȡ������
	private void getLocationInfomation(Location location) {
		if (tvPointName.getText().toString().equals("��ѡ����Ҫ�����ĵ�"))
			return;
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
		DecimalFormat df = new DecimalFormat("0.0"); // ����һ����ʽ����f
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
			currentX = pcc.X;
			currentY = pcc.Y;
		}
		// ʹ�ù���У���߲���
		if (PadApplication.UseCoordinateTransfomation == 1) {
			double[] WGS84Coordinate = new double[3];
			double[] UserCoordinate = new double[3];
			WGS84Coordinate[0] = bA.GetRadian();
			WGS84Coordinate[1] = lA.GetRadian();
			WGS84Coordinate[2] = Altitude;
			PadApplication.CoordinateTrans.WGS84TransCartesian(WGS84Coordinate,
					UserCoordinate);
			currentX = UserCoordinate[0];
			currentY = UserCoordinate[1];
		}
		mX = currentX;
		mY = currentY;
		double dX = targetX - currentX;
		double dY = targetY - currentY;
		if (dX < 0) {
			tvDX.setText("�ϣ� " + df.format(Math.abs(dX)) + " ��");
		} else {
			tvDX.setText("���� " + df.format(dX) + " ��");
		}
		if (dY < 0) {
			tvDY.setText("���� " + df.format(Math.abs(dY)) + " ��");
		} else {
			tvDY.setText("���� " + df.format(dY) + " ��");
		}
		if (tvS != null) {
			tvS.setText(String.valueOf("�ٶȣ�" + df.format(Speed)) + "����/Сʱ");
		}
		if (tvD != null) {
			tvD.setText(String.valueOf("����" + df.format(Direction)));
		}
	}

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
			PadApplication.CoordinateTrans.WGS84TransCartesian(WGS84Coordinate,
					UserCoordinate);
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
			// �ƶ�intentҪ��������
			Intent intent = new Intent(StakingoutPointActivity.this,
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

	// ��д�÷������÷����Իص��ķ�ʽ����ȡָ��Activity���صĽ��
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// ��requestCode��resultCodeͬʱΪ0��Ҳ���Ǵ����ض��Ľ��
		// �õ�Ҫ�����ĵ������
		if (requestCode == 0 && resultCode == RESULT_OK) {
			PointName = intent.getStringExtra("PointName");
			stakingOutPoint();
		}
	}

	private void stakingOutPoint() {
		if (PointName == null)
			return;
		Cursor mCursor = PadApplication.FindPointByName(PointName);
		if (mCursor == null)
			return;
		tvPointName.setText("������:" + PointName);
		int PointFlag = mCursor.getInt(0);
		// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
		if (PointFlag == 1 || PointFlag == 2) {
			CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
			GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
			pgc.Latitude = SurveyMath.DEGToRadian(mCursor.getDouble(3));
			pgc.Longitude = SurveyMath.DEGToRadian(mCursor.getDouble(4));
			pgc.Height = mCursor.getDouble(5);
			drawGoogleMapLat = mCursor.getDouble(3);
			drawGoogleMapLon = mCursor.getDouble(4);
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
			targetX = pcc.X;
			targetY = pcc.Y;
		}
		// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
		if (PointFlag == 3 || PointFlag == 4) {
			targetX = mCursor.getDouble(3);
			targetY = mCursor.getDouble(4);

			// // ���귴��,�����γ�ȣ���GOOGLEMAP�ϻ�����

			CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
			GeodeticCoordinatePoint pcg = new GeodeticCoordinatePoint();

			pcc.X = mCursor.getDouble(3);
			pcc.Y = mCursor.getDouble(4);
			pcc.H = mCursor.getDouble(5);

			// ��ʹ�ô�������ϵͳʱ��ʹ�����귴��
			if (PadApplication.UseCoordinateTransfomation == 0) {
				CoordinateTransform ct = new CoordinateTransform();
				CoordinateSystem cs = PadApplication.CurrentCoordinateSystem;
				ct.CartesianToGeodetic(pcg, pcc, cs);
			} else// ʹ������ת������У����
			{
				double[] dC = new double[3];
				double[] dW = new double[3];
				dC[0] = pcc.X;
				dC[1] = pcc.Y;
				dC[2] = pcc.H;
				PadApplication.CoordinateTrans.CartesianTransWGS84(dC, dW);
				pcg.Latitude = dW[0];
				pcg.Longitude = dW[1];
				
				pcg.Height = dW[2];
			}
			SurveyAngle saLat = new SurveyAngle(pcg.Latitude);
			SurveyAngle saLon = new SurveyAngle(pcg.Longitude);
			drawGoogleMapLat = saLat.GetDEG();
			drawGoogleMapLon = saLon.GetDEG();
		}
		// ����Ҫ�����ĵ���GOOGLEMAP��
		if (currentView == 1) {
			LatLng MELBOURNE = new LatLng(drawGoogleMapLat, drawGoogleMapLon);
			Marker melbourne = googleMap.addMarker(new MarkerOptions()
					.position(MELBOURNE)
					.title("������")
					.snippet(PointName)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.stakingout_point)));
		}
		// ����Ҫ�����ĵ���GOOGLEMAP��
		if (currentView == 2) {
			if(PointName==null)return;
			SurveyPoint sP=new SurveyPoint();
			sP.Name=PointName;
			sP.X=targetY;
			sP.Y=targetX;			
			ivMapView.drawPoint(sP);
		}

	}

	private class ButtonEnterClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			try {
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
				isShowSetView = false;
				PadApplication.ReSetInformation();
				// ��������ϴη����ĵ㣬���������ϴεĵ�
				if (mHaveLocation) {
					setLocationInformation();
				}
				stakingOutPoint();

			} catch (NumberFormatException n) {

			}

		}
	}

	// ѡ��Ҫ�Ű�ĵ�
	private class buttonSelectPointClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// �½�һ��Intent
			Intent intent = new Intent();
			intent.putExtra("CoorStyle", 3);
			// �ƶ�intentҪ��������
			intent.setClass(StakingoutPointActivity.this,
					SelectPointActivity.class);
			// ����һ���µ�Activity
			startActivityForResult(intent, 0);
		}
	}
}