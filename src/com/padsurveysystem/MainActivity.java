package com.padsurveysystem;

import java.text.SimpleDateFormat;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	// �������ݿ����

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_main);// ����ʹ�������洴����xml�ļ���Tabҳ��Ĳ��֣�
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabSpec spec;
		Intent intent; // Reusable Intent for each tab
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd"); // ("yyyy-MM-dd hh:mm:ss");
		String sDate = sDateFormat.format(new java.util.Date());
		// ��ʼ��ϵͳ��Ϣ��
		PadApplication.Initialize(this);
		PadApplication.ReGetInformation();

		setTitle("ƽ�����ϵͳ - " + PadApplication.ProjectName);

		// ��һ��TAB
		intent = new Intent(this, FileActivity.class);// �½�һ��Intent����Tab1��ʾ������
		spec = tabHost
				.newTabSpec("tab1")
				// �½�һ�� Tab
				.setIndicator("�ļ�",
						getResources().getDrawable(R.drawable.tab_file))// ���������Լ�ͼ��
				.setContent(intent);// ������ʾ��intent������Ĳ���Ҳ������R.id.xxx
		tabHost.addTab(spec);// ��ӽ�tabHost

		// �ڶ���TAB
		intent = new Intent(this, SurveyActivity.class);// �½�һ��Intent����Tab1��ʾ������
		spec = tabHost
				.newTabSpec("tab2")
				// �½�һ�� Tab
				.setIndicator("����",
						getResources().getDrawable(R.drawable.tab_survey))// ���������Լ�ͼ��
				.setContent(intent);// ������ʾ��intent������Ĳ���Ҳ������R.id.xxx
		tabHost.addTab(spec);// ��ӽ�tabHost

		// ������TAB
		intent = new Intent(this, ToolsActivity.class);// �½�һ��Intent����Tab1��ʾ������
		spec = tabHost
				.newTabSpec("tab3")
				// �½�һ�� Tab
				.setIndicator("����",
						getResources().getDrawable(R.drawable.tab_tools))// ���������Լ�ͼ��
				.setContent(intent);// ������ʾ��intent������Ĳ���Ҳ������R.id.xxx
		tabHost.addTab(spec);// ��ӽ�tabHost

		// ���ĸ�TAB
		intent = new Intent(this, SettingActivity.class);// �½�һ��Intent����Tab1��ʾ������
		spec = tabHost
				.newTabSpec("tab4")
				// �½�һ�� Tab
				.setIndicator("����",
						getResources().getDrawable(R.drawable.tab_setting))// ���������Լ�ͼ��
				.setContent(intent);// ������ʾ��intent������Ĳ���Ҳ������R.id.xxx
		tabHost.addTab(spec);// ��ӽ�tabHost
		// ��һ��TAB��Ϊ��ǰ
		tabHost.setCurrentTab(0);
	}

	public void onResume() {
		super.onResume();
		setTitle("ƽ�����ϵͳ - " + PadApplication.ProjectName);

	}
}
