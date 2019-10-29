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
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.*;

public class SelectPointActivity extends Activity {
	
	int CoorStyle;//0-������꣬1-��������
	Button btnEnter;

	ListView lsvPoint;

	View itemView = null;

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	MyAdapter adapter;
	int listPosition = -1;
	int selectListItemIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("ѡ���");
		setContentView(R.layout.view_selectpoint);
		CoorStyle=getIntent().getIntExtra("CoorStyle",0);//��ȡ��������
		btnEnter = (Button) findViewById(R.id.button_select_view_selectpoint);
		btnEnter.setOnClickListener(new ButtonEnterClickEvent());

		lsvPoint = (ListView) findViewById(R.id.listview_view_selectpoint);
        
		
		
		inflateListView(CoorStyle);


		lsvPoint.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				selectListItemIndex=position;
				adapter.setSelectItem(position);
				selectListItemIndex = position;
				adapter.notifyDataSetInvalidated();

			}
		});
		lsvPoint.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					listPosition = lsvPoint.getFirstVisiblePosition();

				}
				// Log.d("Pos",String.valueOf(listPosition));
			}

		});
	}

	// �����б�
	private void inflateListView(int CoorStyle) {
		// ����һ��List���ϣ�List���ϵ�Ԫ����Map
		listItems.clear();

		DecimalFormat df5 = new DecimalFormat("0.00000"); // ����һ����ʽ����f
		DecimalFormat df3 = new DecimalFormat("0.000"); // ����һ����ʽ����f
		
		String X = null,Y= null,LAT = null,LON = null,Z = null;
		SurveyAngle sA=new SurveyAngle();
		
		String[] sCols = {"POINTFLAG", "NAME", "CODE", "X", "Y", "Z"};
		Cursor mCursor = MyDataBaseAdapter.fetchAllData(
				MyDataBaseAdapter.PointTable, sCols);
		//����ʾԭʼ����Ϊ�������ĵ�
		if (CoorStyle == 0) {
			if (mCursor.moveToFirst() && mCursor.getCount() > 0) {
				do {
					if(mCursor.getInt(0)==1 || mCursor.getInt(0)==2){
						
						sA.valueOfDEG(mCursor.getDouble(3));
						LAT = String.valueOf(sA.GetSubDegree()) + "��"
								+ String.valueOf(sA.GetSubMinute()) + "��"
								+ sA.GetSubSecond(5) + "��";
						sA.valueOfDEG(mCursor.getDouble(4));
						LON = String.valueOf(sA.GetSubDegree()) + "��"
								+ String.valueOf(sA.GetSubMinute()) + "��"
								+ sA.GetSubSecond(5) + "��";
						Z = df3.format(mCursor.getDouble(5));
						Map<String, Object> listItem = new HashMap<String, Object>();
						listItem.put("name", mCursor.getString(1));
						listItem.put("code", mCursor.getString(2));
						listItem.put("coor", LAT+ " , " + LON + " , " + Z);
						// ���List��
						listItems.add(listItem);
					}

				} while (mCursor.moveToNext());

			}
		}
		//����ʾԭʼ����Ϊ��������ĵ�
		if (CoorStyle == 1) {
			if (mCursor.moveToFirst() && mCursor.getCount() > 0) {
				do {
					if(mCursor.getInt(0)==3 || mCursor.getInt(0)==4){
						Map<String, Object> listItem = new HashMap<String, Object>();
						listItem.put("name", mCursor.getString(1));
						listItem.put("code", mCursor.getString(2));
						listItem.put("coor", df3.format(mCursor.getDouble(3))
								+ " , " + df3.format(mCursor.getDouble(4))
								+ " , " + df3.format(mCursor.getDouble(5)));
						// ���List��
						listItems.add(listItem);
					}

				} while (mCursor.moveToNext());

			}
		}
		// �������ݵ��������������ʽ��ʾ
		if (CoorStyle == 3) {
			if (mCursor.moveToFirst() && mCursor.getCount() > 0) {
				do {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("name", mCursor.getString(1));
					listItem.put("code", mCursor.getString(2));
					int PointFlag = mCursor.getInt(0);
					// ��ԭʼ����Ϊ�����Ĵ��������û������ԭʼ�������ʱ
					if (PointFlag == 1 || PointFlag == 2) {

						CartesianCoordinatePoint pcc = new CartesianCoordinatePoint();
						GeodeticCoordinatePoint pgc = new GeodeticCoordinatePoint();
						pgc.Latitude = SurveyMath.DEGToRadian(mCursor
								.getDouble(3));
						pgc.Longitude = SurveyMath.DEGToRadian(mCursor
								.getDouble(4));
						pgc.Height = mCursor.getDouble(5);

						// ��ʹ�ô��ڵ�����ϵͳʱ��ʹ����������
						if (PadApplication.UseCoordinateTransfomation == 0) {
							CoordinateTransform ct = new CoordinateTransform();
							CoordinateSystem cs = PadApplication.CurrentCoordinateSystem;
							ct.GeodeticToCartesian(pcc, pgc, cs);
						} else// ʹ������ת������У����
						{
							double[] dC = new double[3];
							double[] dW = new double[3];
							dW[0] = pgc.Latitude;
							dW[1] = pgc.Longitude;
							dW[2] = pgc.Height;
							PadApplication.CoordinateTrans.WGS84TransCartesian(
									dW, dC);
							pcc.X = dC[0];
							pcc.Y = dC[1];
							pcc.H = dC[2];
						}

						X = df3.format(pcc.X);
						Y = df3.format(pcc.Y);
						Z = df3.format(pcc.H);
					}
					// ��ԭʼ����Ϊ�û������ԭʼ��������ͼ���Ľ���ʱ
					if (PointFlag == 3 || PointFlag == 4) {
						// ���귴��
						X = df3.format(mCursor.getDouble(3));

						Y = df3.format(mCursor.getDouble(4));

						Z = df3.format(mCursor.getDouble(5));
					}
					listItem.put("coor", X + " , " + Y + " , " + Z);
					// ���List��
					listItems.add(listItem);

				} while (mCursor.moveToNext());
			}
		}
		adapter = new MyAdapter(this);
		lsvPoint.setAdapter(adapter);
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

	// ȷ��ѡ��ĵ�
	private class ButtonEnterClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(adapter.getCount()<1){
				Toast toast = Toast.makeText(
						SelectPointActivity.this, "��ѡ��㣡",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 100);
				toast.show();
				return;
			}
			if (selectListItemIndex==-1){
				finish();
			}
			else{
                // ���÷�������
               Intent intent = new Intent();
               intent.putExtra("PointName", listItems.get(selectListItemIndex)
						.get("name").toString());
               // ����intent
               setResult(RESULT_OK, intent);
               finish();
			}
				return;

		}
	}
}
