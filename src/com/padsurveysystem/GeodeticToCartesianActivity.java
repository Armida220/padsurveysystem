package com.padsurveysystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class GeodeticToCartesianActivity extends Activity {
	String mName;
	String mEllipsoidbase;
	String mMiddleLongitulate;
	String mX;
	String mY;

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
		setContentView(R.layout.view_tools_geodetictocartesian);
		setTitle("��˹��������");
		String[] slCoordinateSystem = { "WGS84����ϵ", "1954�걱������ϵ", "1980��������ϵ",
				"����2000����ϵ" };
		ArrayAdapter<String> spAdapter;
		spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, slCoordinateSystem);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCS = (Spinner) findViewById(R.id.spinner_view_tools_geodetictocartesian);
		spnCS.setAdapter(spAdapter);

		txtPointName = (TextView) findViewById(R.id.edittext_pointname_view_tools_geodetictocartesian);
		txtMiddleLongitulate = (TextView) findViewById(R.id.edittext_middleline_view_tools_geodetictocartesian);
		txtLat = (TextView) findViewById(R.id.edittext_lat_view_tools_geodetictocartesian);
		txtLon = (TextView) findViewById(R.id.edittext_lon_view_tools_geodetictocartesian);
		txtX = (TextView) findViewById(R.id.textview_x_view_tools_geodetictocartesian);
		txtY = (TextView) findViewById(R.id.textview_y_view_tools_geodetictocartesian);

		btnSave = (Button) findViewById(R.id.button_save_view_tools_geodetictocartesian);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		btnCalculate = (Button) findViewById(R.id.button_calculate_view_tools_geodetictocartesian);
		btnCalculate.setOnClickListener(new ButtonCalculateClickEvent());
		SetPointName();

	}

	// ����ƽ������
	private class ButtonCalculateClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				CoordinateSystem cs = new CoordinateSystem();
				cs.ReferenceEllipsoid = CoordinateSystem
						.GetReferenceEllipsoid((int) spnCS.getSelectedItemId());
				cs.MiddleLongitude = SurveyMath.DMSToRadian(Double
						.valueOf(txtMiddleLongitulate.getText().toString()));
				CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
				GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
				SurveyAngle sLat = new SurveyAngle();
				SurveyAngle sLon = new SurveyAngle();
				sLat.valueOfDMS(txtLat.getText().toString());
				pgc.Latitude = sLat.GetRadian();
				sLon.valueOfDMS(txtLon.getText().toString());
				pgc.Longitude = sLon.GetRadian();
				CoordinateTransform.GeodeticToCartesian(pcc, pgc, cs);
				mX = SurveyMath.setDigitsToString(pcc.X, 3);
				mY = SurveyMath.setDigitsToString(pcc.Y, 3);
				txtX.setText("X:" + mX);
				txtY.setText("Y:" + mY);
			} catch (Exception e) {
				final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
						GeodeticToCartesianActivity.this);
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

	// �����½����� ������
	private class ButtonSaveClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			mName = txtPointName.getText().toString();
			if (mName.trim().length() == 0) {
				Toast toast = Toast.makeText(GeodeticToCartesianActivity.this,
						"��������Ϊ�գ�", Toast.LENGTH_SHORT);
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
					Toast toast = Toast.makeText(
							GeodeticToCartesianActivity.this, "�����ظ���",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 100);
					toast.show();
					return;
				}
			}
			// ���浱ǰ�۲�����
			PadApplication.AddPoint("3", mName, "The transform grid point", mX,
					mY, "0");
			Toast toast = Toast.makeText(GeodeticToCartesianActivity.this,
					"���ݵ��ѱ���", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 100);
			toast.show();
		}
	}

	// �Զ����õ���
	private void SetPointName() {
		mName = "TC1";
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
			mName = "TC1";
		}
		txtPointName.setText(mName);
	}
}
