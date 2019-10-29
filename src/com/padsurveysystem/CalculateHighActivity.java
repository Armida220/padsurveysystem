package com.padsurveysystem;

import java.text.DecimalFormat;
import java.util.*;

import android.view.Gravity;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;

public class CalculateHighActivity extends Activity {
	Button btnCalculate;
	TextView txtBS;
	TextView txtBH;
	TextView txtHI;
	TextView txtVI;
	TextView txtSD;
	TextView txtHA;
	TextView txtLV;
	TextView txtHV;
	TextView txtPSO;
	TextView txtLH;
	TextView txtHH;
	TextView txtDH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_tools_calculateheight);
		setTitle("�߸߼���");
		btnCalculate = (Button) findViewById(R.id.button_calculateheight_calculate);
		btnCalculate.setOnClickListener(new ButtonCalculateClickEvent());
		txtBS = (TextView) findViewById(R.id.text_calculateheight_bs);
		txtBH = (TextView) findViewById(R.id.text_calculateheight_bh);
		txtHI = (TextView) findViewById(R.id.text_calculateheight_hi);
		txtVI = (TextView) findViewById(R.id.text_calculateheight_vi);
		txtSD = (TextView) findViewById(R.id.text_calculateheight_sd);
		txtHA = (TextView) findViewById(R.id.text_calculateheight_ha);
		txtLV = (TextView) findViewById(R.id.text_calculateheight_lv);
		txtHV = (TextView) findViewById(R.id.text_calculateheight_hv);
		txtPSO = (TextView) findViewById(R.id.textview_calculateheight_pso);
		txtLH = (TextView) findViewById(R.id.textview_calculateheight_lh);
		txtHH = (TextView) findViewById(R.id.textview_calculateheight_hh);
		txtDH = (TextView) findViewById(R.id.textview_calculateheight_dh);

	}

	// ����ƽ��߲�
	private class ButtonCalculateClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				Calculate();
			} catch (NumberFormatException n) {
				final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
						CalculateHighActivity.this);
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

	private void Calculate() {
		double dBS, dBH, dHI, dVI, dSD, dHA, dLV, dHV, dPS, dPO, dLH, dHH, dDH;
		double dHD;
		dBS = Double.valueOf(txtBS.getText().toString());
		dBH = Double.valueOf(txtBH.getText().toString());
		dHI = Double.valueOf(txtHI.getText().toString());
		dVI = Double.valueOf(txtVI.getText().toString());
		dSD = Double.valueOf(txtSD.getText().toString());
		dHA = Double.valueOf(txtHA.getText().toString());
		dLV = Double.valueOf(txtLV.getText().toString());
		dHV = Double.valueOf(txtHV.getText().toString());

		// ����ƽ��
		dHD = dSD
				* Math.sin(SurveyMath.DMSToRadian(dLV)
						+ (0.87 * 206265 / 3600 / (2 * 6371000) * dSD * Math
								.sin(SurveyMath.DMSToRadian(dLV))) * Math.PI
						/ 180);
		// �������
		dPS = dBS - dHD * Math.cos(SurveyMath.DMSToRadian(dHA));
		// ����ƫ��
		dPO = dHD * Math.sin(SurveyMath.DMSToRadian(dHA));
		// ����͵�߳�
		dLH = dBH + dHD * Math.tan(Math.PI / 2 - SurveyMath.DMSToRadian(dLV))
				+ dSD * dSD * 0.00000006831 + dHI - dVI;
		// ����ߵ�߳�
		dHH = dBH + dHD * Math.tan(Math.PI / 2 - SurveyMath.DMSToRadian(dHV))
				+ dSD * dSD * 0.00000006831 + dHI;

		DecimalFormat df = new DecimalFormat("0.000"); // ����һ����ʽ����f

		txtPSO.setText("��̣�" + df.format(dPS) + "  ƫ�ࣺ" + df.format(dPO));
		txtLH.setText("�͵�̣߳�" + df.format(dLH));
		txtHH.setText("�ߵ�̣߳�" + df.format(dHH));
		txtDH.setText("�߲" + df.format(dHH - dLH));
	}

}
