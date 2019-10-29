package com.padsurveysystem;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//�ļ������
public class FileActivity extends Activity {
	ProgressDialog progressDialog;
	private GridView gridView;
	// ͼƬ�����ֱ���
	private String[] titles = new String[] { "�½�", "��", "ɾ��", "����", "����",
			"�����", "�鿴����" };
	// ͼƬID����
	private int[] images = new int[] { R.drawable.file_new,
			R.drawable.file_open, R.drawable.file_delete,
			R.drawable.file_import, R.drawable.file_export,
			R.drawable.file_inputpoint, R.drawable.file_viewpoint };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_gridview);
		gridView = (GridView) findViewById(R.id.tab_gridview); // �Ź���GRIDVIEW
		PictureAdapter adapter = new PictureAdapter(titles, images, this); // �Զ���������
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == 0)// �½�����
				{
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this,
							CreateNewProjectActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				if (position == 1)// �򿪹���
				{
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this,
							OpenProjectActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				if (position == 2)// ɾ������
				{
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this,
							DeleteProjectActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				if (position == 3)// �����
				{
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this,
							ImportPointActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				if (position == 4)// ������
				{				
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this,
							ExportPointActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				if (position == 5)// �����µ�
				{
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this, AddPointActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				if (position == 6)// �鿴��
				{
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(FileActivity.this, ViewPointActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
			}
		});
	}

}
