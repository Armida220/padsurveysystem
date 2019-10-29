package com.padsurveysystem;


import java.text.DecimalFormat;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//�����������
public class SavePointActivity extends Activity {
	String mFlag = "1";// GPS��ȡ��ԭʼ����
	String mName = "1";
	String mCode = "";
	String mX = "0";
	String mY = "0";
	String mZ = "0";
	String mLat = "0";
	String mLon = "0";
	String mE = "0";

	Button btnSave;
	Button btnCancel;
	TextView tvName;
	TextView tvCode;
	TextView tvX;
	TextView tvY;
	TextView tvZ;

	double Latitude = 0;
	double Longitude = 0;
	double Altitude = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_survey_position_savepoint);
		// ���ô��ڱ���������
		setTitle("�����");

		Intent intent = getIntent();

		Latitude = intent.getDoubleExtra("X", 0);
		Longitude = intent.getDoubleExtra("Y", 0);
		Altitude = intent.getDoubleExtra("Z", 0);

		mLat = String.valueOf(Latitude);
		mLon = String.valueOf(Longitude);
		mE = String.valueOf(Altitude);

		SurveyAngle bA = new SurveyAngle();
		SurveyAngle lA = new SurveyAngle();
		bA.valueOfDEG(Latitude);
		lA.valueOfDEG(Longitude);
		// ��ʼ���ؼ�
		tvName = (TextView) findViewById(R.id.edittext_position_savepoint_name);
		tvCode = (TextView) findViewById(R.id.edittext_position_savepoint_code);
		tvX = (TextView) findViewById(R.id.textview_position_savepoint_x);
		tvY = (TextView) findViewById(R.id.textview_position_savepoint_y);
		tvZ = (TextView) findViewById(R.id.textview_position_savepoint_z);

		btnSave = (Button) this
				.findViewById(R.id.button_position_savepoint_save);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());
		DecimalFormat df = new DecimalFormat("0.000"); // ����һ����ʽ����f

		// ������ʾ��ʽΪ������꾭γ��
		if (PadApplication.PointViewFormat == 0) {
			if (tvX != null) {
				if (Latitude < 0) {
					tvX.setText("��γ�� " + Math.abs(bA.GetSubDegree()) + "��"
							+ bA.GetSubMinute() + "��"
							+ bA.GetSubSecond(3) + "��");
				} else {
					tvX.setText("��γ�� " + Math.abs(bA.GetSubDegree()) + "��"
							+ bA.GetSubMinute() + "��"
							+ bA.GetSubSecond(3) + "��");
				}

			}
			if (tvY != null) {
				if (Longitude < 0) {
					tvY.setText("���� ��" + Math.abs(lA.GetSubDegree()) + "��"
							+ lA.GetSubMinute() + "��"
							+ lA.GetSubSecond(3) + "��");
				} else {
					tvY.setText("������ " + Math.abs(lA.GetSubDegree()) + "��"
							+ lA.GetSubMinute() + "��"
							+ lA.GetSubSecond(3) + "��");
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
				mX = df.format(pcc.X);
				mY = df.format(pcc.Y);
				mZ = df.format(Altitude);
				tvX.setText("�����꣺ " + df.format(pcc.X));
				tvY.setText("�����꣺ " + df.format(pcc.Y));
				tvZ.setText("�߶ȣ�" + df.format(Altitude));
			}
			// ʹ�ù���У���߲���
			if (PadApplication.UseCoordinateTransfomation == 1) {
				   double[] WGS84Coordinate=new double[3];
				   double[] UserCoordinate=new double[3];
					WGS84Coordinate[0] = bA.GetRadian();
					WGS84Coordinate[1] = lA.GetRadian();
					WGS84Coordinate[2] = Altitude;
					PadApplication.CoordinateTrans.WGS84TransCartesian(WGS84Coordinate, UserCoordinate);

					tvX.setText("�����꣺ " + df.format(UserCoordinate[0]));
					tvY.setText("�����꣺ " + df.format(UserCoordinate[1]));
					tvZ.setText("�߶ȣ�" + df.format(UserCoordinate[2]));
			}
		}
		tvCode.setText(PadApplication.LastCode);

		SetPointName();// ���ó�ʼ����

	}

	// ����۲�����
	private class ButtonSaveClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			mName = tvName.getText().toString();
			mCode = tvCode.getText().toString();
			if (mName.trim().length() == 0) {
				Toast toast = Toast.makeText(SavePointActivity.this, "��������Ϊ�գ�",
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
					Toast toast = Toast.makeText(SavePointActivity.this,
							"�����ظ���", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 100);
					toast.show();
					return;
				}
			}
			// ���浱ǰ�۲�����
			PadApplication.AddPoint(mFlag, mName, mCode, mLat, mLon, mE);
			PadApplication.LastCode = mCode;
			Toast toast = Toast.makeText(SavePointActivity.this, "���ݵ��ѱ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			finish();

		}
	}

	// �Զ����õ���
	private void SetPointName() {
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
			mName = "1";
		}
		tvName.setText(mName);
	}

}
