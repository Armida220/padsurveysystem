package com.padsurveysystem;

import java.text.DecimalFormat;
import java.util.*;

import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;

public class EditPointActivity extends Activity {


	Button btnSave;

	TextView lableX;
	TextView lableY;
	TextView lableCoorStyle;
	TextView lableName;

	TextView txtCode;
	TextView txtX;
	TextView txtY;
	TextView txtZ;

	double X,Y,Z;
	// �ֶε�����
	String KEY_POINTFLAG = "POINTFLAG";
	// �ֶε���
	String KEY_NAME = "";
	// �ֶδ���
	String KEY_CODE = "";
	// �ֶ�X
	String KEY_X = "";
	// �ֶ�Y
	String KEY_Y = "";
	// �ֶκ� �ϸ�
	String KEY_Z = "";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("�༭��");
		setContentView(R.layout.view_file_editpoint);
		DecimalFormat df9 = new DecimalFormat("0.0000000000"); // ����һ����ʽ����f
		DecimalFormat df3 = new DecimalFormat("0.000"); // ����һ����ʽ����f
		KEY_NAME = getIntent().getStringExtra("PointName");
		Cursor cP = PadApplication
				.FindPointByName(KEY_NAME);
		KEY_POINTFLAG = cP.getString(0);
		KEY_CODE = cP.getString(2);
		X = cP.getDouble(3);
		Y = cP.getDouble(4);
		Z = cP.getDouble(5);

		btnSave = (Button) findViewById(R.id.button_addpoint_save);
		btnSave.setOnClickListener(new ButtonSaveClickEvent());

		lableCoorStyle = (TextView) findViewById(R.id.TextView_editpoint_coorstyle);

		lableName = (TextView) findViewById(R.id.textView_editpoint_name);
		lableX = (TextView) findViewById(R.id.textview_editpoint_x);
		lableY = (TextView) findViewById(R.id.textview_editpoint_y);

		txtCode = (TextView) findViewById(R.id.edittext_editpoint_code);
		txtX = (TextView) findViewById(R.id.edittext_editpoint_x);
		txtY = (TextView) findViewById(R.id.edittext_editpoint_y);
		txtZ = (TextView) findViewById(R.id.edittext_editpoint_z);

		lableName.setText("������" + KEY_NAME);
		txtCode.setText(KEY_CODE);
		if (KEY_POINTFLAG.equals("1")) {
			lableCoorStyle.setText("�������ͣ�GPSԭʼ��������");
			lableX.setText("γ�ȣ�");
			lableY.setText("���ȣ�");

			txtX.setText(df9.format(SurveyMath.DEGToDMS(X)));
			txtY.setText(df9.format(SurveyMath.DEGToDMS(Y)));
			txtZ.setText(df3.format(Z));
			txtX.setEnabled(false);
			txtY.setEnabled(false);
			txtZ.setEnabled(false);
		}
		if (KEY_POINTFLAG.equals("2")) {
			lableCoorStyle.setText("�������ͣ��û�����Ĵ������");
			lableX.setText("γ�ȣ�");
			lableY.setText("���ȣ�");
			txtX.setText(df9.format(SurveyMath.DEGToDMS(X)));
			txtY.setText(df9.format(SurveyMath.DEGToDMS(Y)));
			txtZ.setText(df3.format(Z));
		}
		if (KEY_POINTFLAG.equals("3")) {
			lableCoorStyle.setText("�������ͣ��û��������������");
			lableX.setText("X��");
			lableY.setText("Y��");
			txtX.setText(df3.format(X));
			txtY.setText(df3.format(Y));
			txtZ.setText(df3.format(Z));

		}

	}

	// ��������
	private class ButtonSaveClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (!CheckEmptyPointCoor()) {
				Toast.makeText(EditPointActivity.this, "����ֵ����Ϊ��!",
						Toast.LENGTH_SHORT).show();
				return;
			}
			EditPoint(KEY_POINTFLAG);
			finish();
		}
	}

	// �����޸ĺ�����ݵ�
	public void EditPoint(String CoorStyle) {

		if (CoorStyle.equals("1") || CoorStyle.equals("2")) {
			KEY_CODE = txtCode.getText().toString();
			KEY_X = String.valueOf(SurveyMath.DMSToDEG(Double.valueOf(txtX.getText().toString())));
			KEY_Y = String.valueOf(SurveyMath.DMSToDEG(Double.valueOf(txtY.getText().toString())));
			KEY_Z = txtZ.getText().toString();
		}
		if (CoorStyle.equals("3") || CoorStyle.equals("4")) {
			KEY_CODE = txtCode.getText().toString();
			KEY_X = txtX.getText().toString();
			KEY_Y = txtY.getText().toString();
			KEY_Z = txtZ.getText().toString();
		}
		PadApplication.EditPoint(CoorStyle, KEY_NAME, KEY_CODE, KEY_X, KEY_Y,KEY_Z);
	}

	// ����û�����������Ƿ�Ϸ�
	private boolean CheckEmptyPointCoor() {
		if (txtX.getText().length() < 1 || txtY.getText().length() < 1
				|| txtZ.getText().length() < 1) {
			return false;
		}
		return true;
	}

}
