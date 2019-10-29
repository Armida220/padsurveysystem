package com.padsurveysystem;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.*;

public class ViewPointActivity extends Activity {
	int CoorStyle;
	Button btnEdit;
	Button btnDelete;
	ListView listPoint;
	View itemView = null;

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	String selectPointName = "";
	MyAdapter adapter;
	int listPosition = -1;
	int selectListItemIndex = -1;

	String currentView="��ʾ�������";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("�鿴��");
		setContentView(R.layout.view_file_viewpoint);
		String[] slCoorType = { "��������", "�������" };

		btnEdit = (Button) findViewById(R.id.button_viewpoint_edit);
		btnEdit.setOnClickListener(new ButtonEditClickEvent());
		btnDelete = (Button) findViewById(R.id.button_viewpoint_remove);
		btnDelete.setOnClickListener(new ButtonDeleteClickEvent());

		listPoint = (ListView) findViewById(R.id.listview_viewpoint);

		inflateListView(1);// Ĭ����ʾ��������

		listPoint.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				selectPointName = listItems.get(position)
						.get("name").toString();

				adapter.setSelectItem(position);
				selectListItemIndex = position;
				adapter.notifyDataSetInvalidated();

			}
		});
		listPoint.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					listPosition = listPoint.getFirstVisiblePosition();

				}
			}

		});
	}

	// �����б�
	private void inflateListView(int CoorStyle) {
		// ����һ��List���ϣ�List���ϵ�Ԫ����Map
		listItems.clear();

		DecimalFormat df0 = new DecimalFormat("00"); // ����һ����ʽ����f
		DecimalFormat df3 = new DecimalFormat("0.000"); // ����һ����ʽ����f
		DecimalFormat df5 = new DecimalFormat("00.00000"); // ����һ����ʽ����f
		String X = null,Y= null,LAT = null,LON = null,Z = null;
		SurveyAngle sA=new SurveyAngle();
		String[] sCols = {"POINTFLAG","NAME", "CODE", "X", "Y", "Z" };
		Cursor mCursor = MyDataBaseAdapter.fetchAllData(
				MyDataBaseAdapter.PointTable, sCols);

		if (mCursor.moveToFirst() && mCursor.getCount() > 0) {
			do {
				Map<String, Object> listItem = new HashMap<String, Object>();
    			listItem.put("name", mCursor.getString(1));
    			listItem.put("code", mCursor.getString(2));
    			
				int PointFlag=mCursor.getInt(0);//ԭʼ������


				//�û�Ҫ����ʾ�������ʱ
				if (CoorStyle == 0) {

					//��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
					if (PointFlag == 1 || PointFlag == 2) {
						sA.valueOfDEG(mCursor.getDouble(3));
						LAT = String.valueOf(sA.GetSubDegree()) + "��"
								+ df0.format(sA.GetSubMinute()) + "��"
								+ sA.GetSubSecond(5) + "��";
						sA.valueOfDEG(mCursor.getDouble(4));
						LON = String.valueOf(sA.GetSubDegree()) + "��"
								+ df0.format(sA.GetSubMinute()) + "��"
								+ sA.GetSubSecond(5) + "��";
						Z = df3.format(mCursor.getDouble(5));

					}
					//��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
					if (PointFlag == 3 || PointFlag == 4) {
     				    CartesianCoordinatePoint pcc=new CartesianCoordinatePoint();
						GeodeticCoordinatePoint pcg = new GeodeticCoordinatePoint();

						pcc.X=mCursor.getDouble(3);						
						pcc.Y=mCursor.getDouble(4);
						pcc.H=mCursor.getDouble(5);
						
						//��ʹ�ô�������ϵͳʱ��ʹ�����귴��
						if(PadApplication.UseCoordinateTransfomation == 0){
							CoordinateTransform ct =new CoordinateTransform();
							CoordinateSystem cs=PadApplication.CurrentCoordinateSystem;
							ct.CartesianToGeodetic(pcg, pcc, cs);

						}
						else//ʹ������ת������У����
						{
							double[] dC=new double[3];
							double[] dW=new double[3];
							dC[0]=pcc.X;
							dC[1]=pcc.Y;
							dC[2]=pcc.H;
							PadApplication.CoordinateTrans.CartesianTransWGS84(dC, dW);
							pcg.Latitude=dW[0];
							pcg.Longitude=dW[1];
							pcg.Height=dW[2];
						}

						sA.valueOfRadian(pcg.Latitude);
						LAT = String.valueOf(sA.GetSubDegree()) + "��"
								+ df0.format(sA.GetSubMinute()) + "��"
								+ sA.GetSubSecond(5)+ "��";
						
						sA.valueOfRadian(pcg.Longitude);						
						LON = String.valueOf(sA.GetSubDegree()) + "��"
								+ df0.format(sA.GetSubMinute()) + "��"
								+ sA.GetSubSecond(5) + "��";
						Z = df3.format(pcg.Height);
					}
					
					listItem.put("coor", LAT + " , " + LON + " , " + Z);
				}
			    //���û�Ҫ����ʾ��������ʱ
				if (CoorStyle == 1) {
					//��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
					if (PointFlag == 1 || PointFlag == 2) {

						CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
						GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
						pgc.Latitude =SurveyMath.DEGToRadian(mCursor.getDouble(3));
						pgc.Longitude = SurveyMath.DEGToRadian(mCursor.getDouble(4));
						pgc.Height = mCursor.getDouble(5);
						
						//��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
						if(PadApplication.UseCoordinateTransfomation ==0){
							CoordinateTransform ct =new CoordinateTransform();
							CoordinateSystem cs=PadApplication.CurrentCoordinateSystem;
							ct.GeodeticToCartesian(pcc, pgc, cs);
						}
						else//ʹ������ת������У����
						{
							double[] dC=new double[3];
							double[] dW=new double[3];
							dW[0]=pgc.Latitude;
							dW[1]=pgc.Longitude;
							dW[2]=pgc.Height;
							PadApplication.CoordinateTrans.WGS84TransCartesian(dW, dC);
							pcc.X=dC[0];
							pcc.Y=dC[1];
							pcc.H=dC[2];
						}
						
						X = df3.format(pcc.X);
						Y = df3.format(pcc.Y);
						Z = df3.format(pcc.H);
					}
					//��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
					if (PointFlag == 3 || PointFlag == 4) {
						//���귴��
						X= df3.format(mCursor.getDouble(3));
						
						Y= df3.format(mCursor.getDouble(4));

						Z = df3.format(mCursor.getDouble(5));
					}
					listItem.put("coor", X + " , " + Y + " , " + Z);
				}
				// ���List��
				listItems.add(listItem);

			} while (mCursor.moveToNext());

		}
		adapter = new MyAdapter(this);
		listPoint.setAdapter(adapter);
		if (listPosition > -1 && selectListItemIndex > -1) {
			listPoint.setSelection(listPosition);
			adapter.setSelectItem(selectListItemIndex);
			adapter.notifyDataSetInvalidated();

		}

	}

	public final class ViewHolder {
		public TextView lName;
		public TextView lCode;
		public TextView lCoor;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listItems.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.item_viewpoint_listview, null);
				holder.lName = (TextView) convertView
						.findViewById(R.id.textview_viewpoint_name);
				holder.lCode = (TextView) convertView
						.findViewById(R.id.textview_viewpoint_code);
				holder.lCoor = (TextView) convertView
						.findViewById(R.id.textview_viewpoint_coor);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.lName.setText((String) listItems.get(position).get("name"));
			holder.lCode.setText((String) listItems.get(position).get("code"));
			holder.lCoor.setText((String) listItems.get(position).get("coor"));

			if (position == selectItem) {
				convertView.setBackgroundColor(Color.rgb(255, 165, 0));
				holder.lName.setTextColor(Color.BLACK);
				holder.lCode.setTextColor(Color.BLACK);
				holder.lCoor.setTextColor(Color.BLACK);
			} else {
				holder.lName.setTextColor(Color.WHITE);
				holder.lCode.setTextColor(Color.WHITE);
				holder.lCoor.setTextColor(Color.WHITE);
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}

			// convertView.getBackground().setAlpha(80);

			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = -1;
	}

	// �༭��
	private class ButtonEditClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (selectListItemIndex == -1 ) return;
			// �½�һ��Intent
			Intent intent = new Intent();
			intent.putExtra("PointName", selectPointName);
			// �ƶ�intentҪ��������
			intent.setClass(ViewPointActivity.this, EditPointActivity.class);
			// ����һ���µ�Activity
			startActivityForResult(intent, 0);

		}
	}

	// ɾ����
	private class ButtonDeleteClickEvent implements View.OnClickListener {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			if (selectListItemIndex<0)
				return;

			AlertDialog dialogOK = new AlertDialog.Builder(
					ViewPointActivity.this).create();
			dialogOK.setTitle("ɾ����");// ���ñ���
			dialogOK.setMessage("ȷ��Ҫɾ���� " + selectPointName + " ��?");// ��������
			dialogOK.setButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// ���"ȷ��"��ť֮��ɾ����
					PadApplication.RemovePoint(selectPointName);
					// ��������һ������
					if (selectListItemIndex == listItems.size() - 1) {
						if (listItems.size() == 1) {
							selectListItemIndex = -1;
						}
						if (listItems.size() > 1) {
							selectListItemIndex = selectListItemIndex - 1;
						}
					}
					// ����ǵ�һ������
					if (selectListItemIndex == 0) {
						if (listItems.size() == 1) {
							selectListItemIndex = -1;
						}
						if (listItems.size() > 1) {
							selectListItemIndex = 0;
						}
					}
					inflateListView(CoorStyle);
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

	// ��д�÷������÷����Իص��ķ�ʽ����ȡָ��Activity���صĽ��
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// ��requestCode��resultCodeͬʱΪ0��Ҳ���Ǵ����ض��Ľ��
		if (requestCode == 0 && resultCode == 0) {
			inflateListView(CoorStyle);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, currentView);	
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);  
        menu.clear();  
        menu.add(0, 1, 1,currentView);  
        return super.onPrepareOptionsMenu(menu);  
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == 1) {
			// ��ʾ������ͼ			
			if (item.getTitle().equals("��ʾ�������")) {
				currentView="��ʾ��������";
				CoorStyle = 0;

			} else {
				currentView="��ʾ�������";
				CoorStyle = 1;
			}
			inflateListView(CoorStyle);
		}

		return super.onMenuItemSelected(featureId, item);
	}

}
