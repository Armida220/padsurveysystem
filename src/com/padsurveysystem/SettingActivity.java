package com.padsurveysystem;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

//�ļ��ٻ
public class SettingActivity extends Activity {
	private GridView gridView;
	// ͼƬ�����ֱ���
	private String[] titles = new String[] { "����ϵͳ", "����ת��", "��������ͼ" };
	// ͼƬID����
	private int[] images = new int[] { R.drawable.setting_cs,
			R.drawable.setting_ct, R.drawable.setting_map };

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
				if (position == 0) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(SettingActivity.this,
							SetCoordinateSystemActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
				if (position == 1) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(SettingActivity.this,
							SetCoordTransSevenParamPointActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
				if (position == 2) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(SettingActivity.this,
							SetAnotherMapActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
			}
		});
	}
}
