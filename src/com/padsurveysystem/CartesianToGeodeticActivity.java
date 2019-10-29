package com.padsurveysystem;



import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class CartesianToGeodeticActivity extends Activity {
	String mName;
	String mEllipsoidbase;
	String mMiddleLongitulate;
	String mLat;
	String mLon;

	Button btnCalculate;
	Button btnSave;

	TextView txtPointName;
	TextView txtMiddleLongitulate;
	TextView txtX;
	TextView txtY;
	TextView txtLat;
	TextView txtLon;

	Spinner spnCS;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_tools_cartesiantogeodetic);
		setTitle("��˹���귴��");
		String[] slCoordinateSystem = { "WGS84����ϵ", "1954�걱������ϵ", "1980��������ϵ",
				"����2000����ϵ" };
		ArrayAdapter<String> spAdapter;
		spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, slCoordinateSystem);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCS = (Spinner) findViewById(R.id.spinner_view_tools_cartesiantogeodetic);
		spnCS.setAdapter(spAdapter);

			
		txtPointName = (TextView) findViewById(R.id.edittext_pointname_view_tools_cartesiantogeodetic);
		txtMiddleLongitulate = (TextView) findViewById(R.id.edittext_middleline_view_tools_cartesiantogeodetic);
		txtX = (TextView) findViewById(R.id.edittext_x_view_tools_cartesiantogeodetic);
		txtY = (TextView) findViewById(R.id.edittext_y_view_tools_cartesiantogeodetic);
		txtLat = (TextView) findViewById(R.id.textview_lat_view_tools_cartesiantogeodetic);
		txtLon = (TextView) findViewById(R.id.textview_lon_view_tools_cartesiantogeodetic);
		
		btnSave = (Button) findViewById(R.id.button_save_view_tools_cartesiantogeodetic);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		btnCalculate = (Button) findViewById(R.id.button_calculate_view_tools_cartesiantogeodetic);
		btnCalculate.setOnClickListener(new ButtonCalculateClickEvent());		
		SetPointName();
	}

	// ���㾭γ��
	private class ButtonCalculateClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			CoordinateSystem cs = new CoordinateSystem();
			cs.ReferenceEllipsoid = CoordinateSystem
					.GetReferenceEllipsoid((int) spnCS.getSelectedItemId());
			cs.MiddleLongitude = SurveyMath.DMSToRadian(Double
					.valueOf(txtMiddleLongitulate.getText().toString()));
			CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
			GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
			pcc.X = Double.valueOf(txtX.getText().toString());
			pcc.Y = Double.valueOf(txtY.getText().toString());
			CoordinateTransform.CartesianToGeodetic(pgc, pcc, cs);
			SurveyAngle sA = new SurveyAngle();
			sA.valueOfRadian(pgc.Latitude);
			txtLat.setText(sA.GetSubDegree() + "��" + sA.GetSubMinute() + "��"
					+ sA.GetSubSecond(5) + "��");
			mLat=String.valueOf(sA.GetDEG());
			sA.valueOfRadian(pgc.Longitude);
			txtLon.setText(sA.GetSubDegree() + "��" + sA.GetSubMinute() + "��"
					+ sA.GetSubSecond(5) + "��");
			mLon=String.valueOf(sA.GetDEG());;

		}
	}
	// �����½����� ������
	private class ButtonSaveClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			mName = txtPointName.getText().toString();
			if (mName.trim().length() == 0) {
				Toast toast = Toast.makeText(CartesianToGeodeticActivity.this, "��������Ϊ�գ�",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 100);
				toast.show();
				return;
			}
			Cursor mCursor = MyDataBaseAdapter.fetchData(
					MyDataBaseAdapter.PointTable, "NAME", mName, null);
			// �������ظ�ʱ
			if (mCursor != null) {
				if (mCursor.getCount() > 0) {
					SetPointName();// ������һ������
					Toast toast = Toast.makeText(CartesianToGeodeticActivity.this,
							"�����ظ���", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 100);
					toast.show();
					return;
				}
			}
			// ���浱ǰ�۲�����
			PadApplication.AddPoint("2", mName, "The transform geodetic point", mLat, mLon, "0");
			Log.i("mLat", mLat);
			Toast toast = Toast.makeText(CartesianToGeodeticActivity.this, "���ݵ��ѱ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 100);
			toast.show();
		}
	}
	// �Զ����õ���
	private void SetPointName() {
		mName="TG1";
		int tNumber =0;
		String[] tKeys = new String[] { "ID", "NAME" };
		Cursor mCursor = MyDataBaseAdapter.fetchAllData(
				MyDataBaseAdapter.PointTable, tKeys);
		if (mCursor != null && mCursor.getCount() > 0) {
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
						mName = "" + tNumber;
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
			mName = "TG1";
		}
		txtPointName.setText(mName);
	}

}

