package com.padsurveysystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.*;
import android.widget.AdapterView.*;

public class ImportPointActivity extends Activity {

	Button btnImport;
	Button btnExit;
	Spinner spnCS;
	ListView listFile;
	CheckBox chkCover;
	ProgressDialog progressDialog;

	// List<Map<String, Object>> listItems = new ArrayList<Map<String,
	// Object>>();
	List<String> mData;
	String m_Name = "1";
	String m_Code = "";
	String m_X = "0";
	String m_Y = "0";
	String m_Z = "0";
	String m_FilePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("�����");
		setContentView(R.layout.view_file_importpoint);
		btnImport = (Button) findViewById(R.id.button_import_view_file_importpoint);
		btnImport.setOnClickListener(new ButtonImportClickEvent());

		String[] slCoorType = { "��������", "�������" };
		ArrayAdapter<String> spAdapter;
		spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, slCoorType);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCS = (Spinner) findViewById(R.id.spinner_import_view_file_importpoint);
		spnCS.setAdapter(spAdapter);

		listFile = (ListView) findViewById(R.id.listView_importpoint);
		chkCover = (CheckBox) findViewById(R.id.checkBox_importpoint);
		GetImportFile();// ��ȡ�ļ��б�

		// ���Spinner�¼�����
		spnCS.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// ������ʾ��ǰѡ�����
				if (arg0.getSelectedItem().toString() == "��������") {

				} else {

				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}

		});
		// ΪListView���б���ĵ����¼��󶨼�����
		listFile.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				m_FilePath = "mnt/sdcard/PadSurveyData/Import/"
						+ mData.get(position);

			}
		});
	}

	/**
	 * ��Handler������UI
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			// �ر�ProgressDialog
			progressDialog.dismiss();
			Toast toast = Toast.makeText(ImportPointActivity.this, "�����������",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 100);
			toast.show();

		}
	};

	// ��������
	private class ButtonImportClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// ��ʾProgressDialog
		progressDialog = ProgressDialog.show(ImportPointActivity.this,
					"���ݵ�����", "���Ժ�......", true, false);

			// �½��߳�
			new Thread() {

				@Override
				public void run() {
					Looper.prepare(); 
					// ��Ҫ��ʱ�����ķ���
					if (spnCS.getSelectedItem().toString() == "��������") {
						ImportPoint(m_FilePath, "3", chkCover.isChecked());
					} else {
						ImportPoint(m_FilePath, "2", chkCover.isChecked());
					}

					// ��handler����Ϣ

					handler.sendEmptyMessage(0);
					Looper.loop();
				}
			}.start();
			
		}
	}

	// ��ȡ���������ļ��б�
	private void GetImportFile() {
		File[] FileList;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
			File sdcardDir = Environment.getExternalStorageDirectory();
			// �õ�һ��·����������sdcard���ļ���·��������
			String pathImport = sdcardDir.getPath() + "/PadSurveyData/Import";
			File fImport = new File(pathImport);
			if (fImport.exists()) {
				FileList = fImport.listFiles();
				inflateListView(FileList);
			}

		} else {
			final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
					ImportPointActivity.this);
			builder.setTitle("PadSurvey");
			builder.setMessage("û�м�⵽SD����ϵͳ�޷����뵼�����ݣ�");
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {

				}
			});
			builder.create().show();
			return;
		}

	}

	// ����ļ��б�
	private void inflateListView(File[] files) {
		// ����һ��List���ϣ�List���ϵ�Ԫ����Map
		mData = new ArrayList();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				mData.add(files[i].getName());
			}
		}
		// ΪListView����Adapter
		listFile.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, mData));
		listFile.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}


	// �������ݵ��ļ�
	public void ImportPoint(String path, String CoorStyle, boolean CoverFlag) {
		// �ֶε�����
		String KEY_POINTFLAG = CoorStyle;
		// �ֶε���
		String KEY_NAME = "";
		// �ֶδ���
		String KEY_CODE = "";
		// �ֶ�X
		String KEY_X = "";
		// �ֶ�Y
		String KEY_Y = "";
		// �ֶ�Z
		String KEY_Z = "";
		SurveyAngle sA=new SurveyAngle();
		// ���ļ�
		File file = new File(path);
		// ���path�Ǵ��ݹ����Ĳ�����������һ����Ŀ¼���ж�
		if (file.isDirectory()) {
			Toast.makeText(ImportPointActivity.this, "û��ָ�������ļ���", 1000).show();
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					List<String> PointString;
					//ԭʼ����Ϊ�������
					if (CoorStyle == "2") {
						// ���ж�ȡ
						while ((line = buffreader.readLine()) != null) {
							PointString = StringConver.GetSubString(line, ",");
							KEY_NAME = PointString.get(0);
							KEY_CODE = PointString.get(1);
							sA.valueOfDMS(Double.valueOf(PointString.get(2)));
							KEY_X =String.valueOf(sA.GetDEG());
							sA.valueOfDMS(Double.valueOf(PointString.get(3)));
							KEY_Y = String.valueOf(sA.GetDEG());
							KEY_Z = PointString.get(4);
							long lP = PadApplication.AddPoint(CoorStyle,KEY_NAME, KEY_CODE, KEY_X,KEY_Y, KEY_Z);
							if (lP < 0 && chkCover.isChecked()) {
								PadApplication.EditPoint(CoorStyle,KEY_NAME, KEY_CODE, KEY_X,KEY_Y, KEY_Z);
							}
						}
					}
					//ԭʼ����Ϊ���������ļ�
					if (CoorStyle == "3") {
						// ���ж�ȡ
						while ((line = buffreader.readLine()) != null) {
							PointString = StringConver.GetSubString(line, ",");
							KEY_NAME = PointString.get(0);
							KEY_CODE = PointString.get(1);
							KEY_X = PointString.get(2);
							KEY_Y = PointString.get(3);
							KEY_Z = PointString.get(4);
							long lP = PadApplication.AddPoint(CoorStyle,KEY_NAME, KEY_CODE, KEY_X, KEY_Y,KEY_Z);
							if (lP < 0 && chkCover.isChecked()) {
								PadApplication.EditPoint(CoorStyle,KEY_NAME, KEY_CODE, KEY_X, KEY_Y,KEY_Z);
							}
						}
					}
					instream.close();
				}
			} catch (java.io.FileNotFoundException e) {
				Toast.makeText(ImportPointActivity.this, "�ļ�������",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(ImportPointActivity.this, "���ļ�����",
						Toast.LENGTH_SHORT).show();
			} catch (java.lang.IndexOutOfBoundsException e) {
				Toast.makeText(ImportPointActivity.this, "�������ݸ�ʽ����",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

}
