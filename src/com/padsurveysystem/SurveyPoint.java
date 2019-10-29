package com.padsurveysystem;

//��������
public class SurveyPoint {
	public int PointFlag; // �ֶε�����
	public String Name; // �ֶε���
	public String Code; // �ֶδ���
	public double X; // �ֶ�X
	public double Y; // �ֶ�Y
	public double Altitude; // �ֶκ� �ϸ�
	public double Latitude; // �ֶ�γ��
	public double Longitude;// �ֶξ���
	public double Elevation; // �ֶ�GPS��

	public SurveyPoint() {
		PointFlag = 0;
		Name = "0";
		Code = "0";
		X = 0;
		Y = 0;
		Altitude = 0;
		Latitude = 0;
		Longitude = 0;
		Elevation = 0;
	}

	public SurveyPoint(int pointFlag, String pointName, String pointCode,
			double x, double y, double altitude, double latitude,
			double longitude, double elevation) {
		PointFlag = pointFlag;
		Name = pointName;
		Code = pointCode;
		X = x;
		Y = y;
		Altitude = altitude;
		Latitude = latitude;
		Longitude = longitude;
		Elevation = elevation;
	}

	public SurveyPoint(int pointFlag, String pointName, String pointCode,
			double x, double y, double z) {
		PointFlag = pointFlag;
		Name = pointName;
		Code = pointCode;
		if (pointFlag == 0) {
			X = x;
			Y = y;
			Altitude = z;
		}
		if (pointFlag == 1) {
			Latitude = x;
			Longitude = y;
			Elevation = z;
		}
	}
}
//��·��������
class LinePoint extends SurveyPoint{	
	public double Station; // �ֶ�ƫ��
	public double Offset; // �ֶ����
	public double Angle; // �ֶ�ת�Ƕ���
}