package com.padsurveysystem;

import java.util.*;

import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;

public class AddCoordTransSevenParamPointActivity extends Activity {

	String m_Name = "1";
	String m_Code = "";
	String m_X = "0";
	String m_Y = "0";
	String m_Z = "0";

	Button btnSave;

	TextView lableX;
	TextView lableY;

	TextView txtName;
	TextView txtCode;
	TextView txtX;
	TextView txtY;
	TextView txtZ;

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
		setTitle("����У����");
		setContentView(R.layout.view_file_addpoint);
		btnSave = (Button) findViewById(R.id.button_addpoint_save);
		btnSave.setOnClickListener(new ButtonAddClickEvent());

		txtName = (TextView) findViewById(R.id.edittext_addpoint_name);
		txtCode = (TextView) findViewById(R.id.edittext_addpoint_code);
		txtX = (TextView) findViewById(R.id.edittext_addpoint_x);
		txtY = (TextView) findViewById(R.id.edittext_addpoint_y);
		txtZ = (TextView) findViewById(R.id.edittext_addpoint_z);
	}

	// ��������
	private class ButtonAddClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {

		}
	}

	// �������ݵ��ļ�
	public void AddPoint(String CoorStyle) {

		if (txtName.getText().length() < 1) {
			Toast.makeText(this, "��������Ϊ��!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!CheckEmptyPointCoor()) {
			Toast.makeText(this, "����ֵ����Ϊ��!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (CoorStyle == "2") {
			KEY_NAME = txtName.getText().toString();
			KEY_CODE = txtCode.getText().toString();
			KEY_X = txtX.getText().toString();
			KEY_Y = txtY.getText().toString();
			KEY_Z = txtZ.getText().toString();
			long lP = PadApplication.AddPoint(CoorStyle, KEY_NAME, KEY_CODE,KEY_X, KEY_Y, KEY_Z);
			if (lP < 0) {
				Toast.makeText(this, "��" + KEY_NAME + "�Ѿ�����",
						Toast.LENGTH_SHORT).show();
			} else {
				SetNextPointName();

			}
		}
		if (CoorStyle == "3") {
			KEY_NAME = txtName.getText().toString();
			KEY_CODE = txtCode.getText().toString();
			KEY_X = txtX.getText().toString();
			KEY_Y = txtY.getText().toString();
			KEY_Z = txtZ.getText().toString();
			long lP = PadApplication.AddPoint(CoorStyle, KEY_NAME, KEY_CODE,
					KEY_X, KEY_Y, KEY_Z);
			if (lP < 0) {
				Toast.makeText(this, "��" + KEY_NAME + "�Ѿ�����",
						Toast.LENGTH_SHORT).show();
			} else {
				SetNextPointName();
			}
		}
	}

	// ����÷�����������Ƿ�Ϸ�
	private boolean CheckEmptyPointCoor() {
		if (txtX.getText().length() < 1 || txtY.getText().length() < 1
				|| txtZ.getText().length() < 1) {
			return false;
		}
		return true;
	}

	// �Զ����õ���
	private void SetNextPointName() {
		// ������һλ��������
		if ((int) (KEY_NAME.charAt(KEY_NAME.length() - 1)) < 48
				|| (int) (KEY_NAME.charAt(KEY_NAME.length() - 1)) > 57) {
			txtName.setText(KEY_NAME + "1");
		} else// ���������
		{
			for (int i = KEY_NAME.length() - 1; i > -1; i--) {
				if (((int) KEY_NAME.charAt(i)) < 48
						|| ((int) KEY_NAME.charAt(i)) > 57) {
					int tNumber = Integer.valueOf(KEY_NAME.substring(i + 1)) + 1;
					txtName.setText(KEY_NAME.substring(0, i + 1) + tNumber);
					break;
				}
				if (i == 0) {

					int tNumber = Integer.valueOf(KEY_NAME) + 1;
					txtName.setText(String.valueOf(tNumber));
				}
			}
		}
		txtX.setText("");
		txtY.setText("");
		txtZ.setText("");
	}
}
