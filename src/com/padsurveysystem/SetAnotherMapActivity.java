package com.padsurveysystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import android.R.string;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.AdapterView.*;

public class SetAnotherMapActivity extends Activity {
	View layout1, layout2;
	Button btnSelectFile;
	Button btnOpenFile;
	TextView txtMapName;
	MapTilesView  ivMapView;
	ListView lstMapFile;
	ZoomControls zooMap;
	String ActivateLayoutName;
	String strFilePath;
	List<String> mFileList;
	Bitmap bmpView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("��������ͼ");
	   LayoutInflater inflater = LayoutInflater.from(this);
		layout1 = inflater.inflate(R.layout.view_setting_anothermap, null);
		layout2 = inflater.inflate(R.layout.view_setting_anothermap_openfile,
				null);
		setView1();

	}
    @Override  
   protected void onResume() {  
       super.onResume();  
       if(ActivateLayoutName == "1"){
    	   setView1();
		//	ShowMap();
       }

   } 

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// ���¼����Ϸ��ذ�ť
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (ActivateLayoutName == "2") {
				setView1();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	private void setView1() {
		LayoutInflater inflater = LayoutInflater.from(this);
	    layout1 = inflater.inflate(R.layout.view_setting_anothermap, null);
		ActivateLayoutName = "1";
		setContentView(layout1);
		btnSelectFile = (Button) findViewById(R.id.button_view_setting_anothermap_selectmap);
		txtMapName = (TextView) findViewById(R.id.textView_view_setting_anothermap_selectmap);
		ivMapView=null;
		ivMapView = (MapTilesView) findViewById(R.id.maptiles_view_setting_anothermap);
		zooMap=(ZoomControls) findViewById(R.id.zoomcontrols_view_setting_anothermap);
		if (PadApplication.AnotherMapFileName != "") {
			txtMapName.setText(PadApplication.AnotherMapFileName);
		} else {
			txtMapName.setText("�޵�ͼ�ļ�");
		}

		layout1.setOnFocusChangeListener(new View.OnFocusChangeListener() {        
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    // TODO Auto-generated method stub
            	Log.i("onFocusChange","onFocusChange");
                    if(hasFocus){//��������ý���
                    	ShowMap() ;     
                    }else{

                    }
            }
    });

		btnSelectFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setView2();
			}
		});
		zooMap.setOnZoomInClickListener(new OnClickListener() {
			   @Override
			   public void onClick(View v) {
				if(ivMapView.getLevelNumber()==ivMapView.getLevelCount())
				{
					return;
				}
				int iNumber=ivMapView.getLevelNumber()+1;
				ivMapView.ZoomMap(iNumber);
			   }
			  });

		zooMap.setOnZoomOutClickListener(new OnClickListener() {
			   @Override
			   public void onClick(View v) {
					if(ivMapView.getLevelNumber()==1)
					{
						return;
					}
					int iNumber=ivMapView.getLevelNumber()-1;
					ivMapView.ZoomMap(iNumber);
				   }

			  });
	}

	private void setView2() {
		ActivateLayoutName = "2";
		setContentView(layout2);
		strFilePath = "";
		btnOpenFile = (Button) findViewById(R.id.button_view_setting_anothermap_openfile);
		lstMapFile = (ListView) findViewById(R.id.listView_view_setting_anothermap_openfile);
		GetMapFile();
		btnOpenFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setView1();
				ShowMap();

			}
		});
		// ΪListView���б���ĵ����¼��󶨼�����
		lstMapFile.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				strFilePath = "mnt/sdcard/PadSurveyData/AnotherMap/"
						+ mFileList.get(position);
				txtMapName.setText(mFileList.get(position));
				PadApplication.AnotherMapFileName = mFileList.get(position);
			}
		});

	}

	private void ShowMap() {

		strFilePath = "mnt/sdcard/PadSurveyData/AnotherMap/"
				+ PadApplication.AnotherMapFileName ;
		strFilePath = "mnt/sdcard/PadSurveyData/AnotherMap/7125235242";
		ivMapView.initializeMap(strFilePath);
	}

	// ����ͼƬ��ʾ�ı���
	// //////////////////////////

	public int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// ///////////////////////////
	// ��ȡ���������ļ��б�
	private void GetMapFile() {
		File[] FileList;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
			// �õ�һ��·����������sdcard���ļ���·��������
			String pathImport =  "mnt/sdcard/PadSurveyData/AnotherMap";
			File fImport = new File(pathImport);
			if (fImport.exists()) {
				FileList = fImport.listFiles();
				inflateListView(FileList);
			}

		} else {
			final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
					SetAnotherMapActivity.this);
			builder.setTitle("PadSurvey");
			builder.setMessage("û�м�⵽SD����");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

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
		mFileList = new ArrayList();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				mFileList.add(files[i].getName());
			}
		}
		// ΪListView����Adapter
		lstMapFile.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, mFileList));
		lstMapFile.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

}
