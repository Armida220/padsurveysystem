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
public class ToolsActivity extends Activity {
	private GridView gridView;
	// ͼƬ�����ֱ���
	private String[] titles = new String[] { "�������", "�������", "ת�Ǽ���", "���ƫ���߸�",
			"���Ǹ߳�", "��������", "���귴��" };
	// ͼƬID����
	private int[] images = new int[] { R.drawable.tools_distance,
			R.drawable.tools_point, R.drawable.tools_stationoffset,
			R.drawable.tools_changezone, R.drawable.tools_height,
			R.drawable.tools_gtoc, R.drawable.tools_ctog };

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
					intent.setClass(ToolsActivity.this,
							CalculateDistanceActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
				if (position == 1) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(ToolsActivity.this,
							CalculatePointActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
				if (position == 2) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(ToolsActivity.this,
							CalculateTurnAngleActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
				// �������ƫ���߸߼������
				if (position == 3) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(ToolsActivity.this,
							CalculateHighActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);

				}
				// �������Ǹ̼߳������
				if (position == 4) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(ToolsActivity.this,
							CalculatetrigonometriclevelingActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				// ���� �����������
				if (position == 5) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(ToolsActivity.this,
							GeodeticToCartesianActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
				// �������귴�����
				if (position == 6) {
					// �½�һ��Intent
					Intent intent = new Intent();
					// �ƶ�intentҪ��������
					intent.setClass(ToolsActivity.this,
							CartesianToGeodeticActivity.class);
					// ����һ���µ�Activity
					startActivity(intent);
				}
			}
		});
	}
}
