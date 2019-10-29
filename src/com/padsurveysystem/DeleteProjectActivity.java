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

public class DeleteProjectActivity extends Activity {
	Button btnDelete;
	Button btnCancle;
	private ListView listView;
	List<String> mData;
	String mDeleteProjectName = "NO";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_file_deletefile);
		listView = (ListView) findViewById(R.id.listview_deleteproject); // �����б�
		mData = PadApplication.GetProjectList(PadApplication.ProjectName);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, mData));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		btnDelete = (Button) findViewById(R.id.button_deleteproject_delete);
		btnDelete.setOnClickListener(new ButtonDeleteClickEvent());

		/* Ϊm_ListView��ͼ���setOnItemClickListener���� */
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listView.setItemChecked(arg2, true);
				mDeleteProjectName = mData.get(arg2);
			}

		});
	}

	// ɾ������
	private class ButtonDeleteClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (mDeleteProjectName.equals("NO")) {
				AlertDialog dialog = new AlertDialog.Builder(
						DeleteProjectActivity.this).create();
				dialog.setTitle("ɾ������");// ���ñ���
				dialog.setMessage("��ѡ��Ҫɾ���Ĺ��̣�");// ��������
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
			AlertDialog dialogOK = new AlertDialog.Builder(
					DeleteProjectActivity.this).create();
			dialogOK.setTitle("ɾ������");// ���ñ���
			dialogOK.setMessage("ȷ��Ҫɾ������ " + mDeleteProjectName + " ��?");// ��������
			dialogOK.setButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// ���"ȷ��"��ť֮��ɾ������
					PadApplication.DeleteProject(mDeleteProjectName);
					mData = PadApplication
							.GetProjectList(PadApplication.ProjectName);
					listView.setAdapter(new ArrayAdapter<String>(
							DeleteProjectActivity.this,
							android.R.layout.simple_list_item_single_choice,
							mData));
				}
			});
			dialogOK.setButton2("ȡ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// ���"�˳�"��ť֮���Ƴ�����
					dialog.cancel();
				}
			});
			// ��ʾ�Ի���
			dialogOK.show();

		}
	}

}
