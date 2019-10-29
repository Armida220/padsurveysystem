package com.padsurveysystem;

import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class OpenProjectActivity extends Activity {
	Button btnOpen;
	Button btnCancle;
	private ListView listView;
	List<String> mData;
	String mSelectProjectName = "NO";

	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_file_openfile);
		listView = (ListView) findViewById(R.id.listview_openproject); // �����б�
		mData = PadApplication.GetProjectList(PadApplication.ProjectName);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, mData));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		btnOpen = (Button) findViewById(R.id.button_openproject_open);
		btnOpen.setOnClickListener(new ButtonOpenClickEvent());

		/* Ϊm_ListView��ͼ���setOnItemClickListener���� */
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listView.setItemChecked(arg2, true);
				mSelectProjectName = mData.get(arg2);
			}

		});
	}

	// �򿪹���
	private class ButtonOpenClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (mSelectProjectName.equals("NO")) {
				AlertDialog dialog = new AlertDialog.Builder(
						OpenProjectActivity.this).create();
				dialog.setTitle("�򿪹���");// ���ñ���
				dialog.setMessage("��ѡ��Ҫ�򿪵Ĺ��̣�");// ��������
				dialog.setButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// ���"�˳�"��ť֮���Ƴ�����
						dialog.cancel();
					}
				});
				// ��ʾ�Ի���
				dialog.show();
				return;
			}
			PadApplication.SetProjectName(mSelectProjectName);
			
			finish();
		}
	}

}
