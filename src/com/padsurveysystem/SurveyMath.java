package com.padsurveysystem;

import java.text.DecimalFormat;

import android.util.Log;

public class SurveyMath {
	final static double ZERO = 0.00000001;// ������Сֵ

	// ��60���ȽǶ�ת��Ϊʮ����
	public static double DMSToDEG(double DMS) {
		double AngleDMS = Math.abs(DMS);
		double AngleDEG;
		double Degree, Minute, Second;
		Degree = (int) AngleDMS;
		Minute = ((int) ((AngleDMS - Degree) * 100.0));
		Second = (((AngleDMS - Degree) * 100.0 - Minute) * 100.0);
		AngleDEG = Degree + Minute / 60.0 + Second / 3600.0;
		if (DMS < 0) {
			return 0 - AngleDEG;
		} else {
			return AngleDEG;
		}

	}

	// ��ʮ���ƽǶ�ת��Ϊ��ʮ���ƽǶ�
	public static double DEGToDMS(double DEG) {
		double AngleDEG = Math.abs(DEG);
		double AngleDMS;
		double Degree, Minute, Second;
		Degree = (int) AngleDEG;
		Minute = (int) ((AngleDEG - Degree) * 60.0);
		Second = ((AngleDEG - Degree) * 60.0 - Minute) * 60;
		AngleDMS = Degree + Minute / 100.0 + Second / 10000.0;
		if (DEG < 0) {
			return 0 - AngleDMS;
		} else {
			return AngleDMS;
		}

	}

	/** ������ת��Ϊ�ȷ����ʽ�ĽǶ� */
	public static double RadianToDMS(double AngleRadian) {
		double AngleDEG;
		AngleDEG = AngleRadian * 180.0 / Math.PI;
		return DEGToDMS(AngleDEG);
	}

	/** ������ת��Ϊ�Զ�Ϊ��λ�ĽǶ� */
	public static double RadianToDEG(double AngleRadian) {
		double AngleDEG;
		AngleDEG = AngleRadian * 180.0 / Math.PI;
		return AngleDEG;
	}

	/** ���ȷ����ʽ�ĽǶ�ת��Ϊ���� */
	public static double DMSToRadian(double DMS) {
		double AngleDMS = Math.abs(DMS);
		double RadianAngle;
		double Degree, Minute, Second;
		Degree = (int) AngleDMS;
		Minute = ((int) ((AngleDMS - Degree) * 100.0));
		Second = (((AngleDMS - Degree) * 100.0 - Minute) * 100.0);
		RadianAngle = (Degree + Minute / 60.0 + Second / 3600.0) * Math.PI
				/ 180.0;
		if (DMS < 0) {
			return 0 - RadianAngle;
		} else {
			return RadianAngle;
		}

	}

	/** ���Զ�Ϊ��λ�ĽǶ�ת��Ϊ���� */
	public static double DEGToRadian(double AngleDEG) {
		return AngleDEG * Math.PI / 180.0;
	}
    /**����ƽ��*/
    public static double GetDistance(SurveyPoint StartPoint, SurveyPoint EndPoint)
    {
        return Math.sqrt((StartPoint.X - EndPoint.X) * (StartPoint.X - EndPoint.X)
               + (StartPoint.Y - EndPoint.Y) * (StartPoint.Y - EndPoint.Y));
    }
    /**���㷽λ��,�Ƕȵ�λΪ����*/
    public static double GetAzimuth(SurveyPoint StartPoint, SurveyPoint EndPoint)
    {
        double doubAng01, E0, N0, E1, N1;
        E0 = StartPoint.X;
        N0 = StartPoint.Y;
        E1 = EndPoint.X;
        N1 = EndPoint.Y;
        doubAng01 = 0;
        if ((E1 - E0) > 0 && (N1 - N0) > 0)
        {
            doubAng01 = Math.atan((Math.abs((E1 - E0) / (N1 - N0))));
        }
        if ((E1 - E0) > 0 && (N1 - N0) < 0)
        {
            doubAng01 = Math.PI - Math.atan(Math.abs((E1 - E0) / (N1 - N0)));
        }
        if ((E1 - E0) < 0 && (N1 - N0) < 0)
        {
            doubAng01 = Math.PI + Math.atan(Math.abs((E1 - E0) / (N1 - N0)));
        }
        if ((E1 - E0) < 0 && (N1 - N0) > 0)
        {
            doubAng01 = 2 * Math.PI - Math.atan(Math.abs((E1 - E0) / (N1 - N0)));
        }
        if (Math.abs(E1 - E0) < ZERO && N1 > N0)
        {
            doubAng01 = 0;
        }
        if (Math.abs(E1 - E0) < ZERO && N1 < N0)
        {
            doubAng01 = Math.PI;
        }
        if (Math.abs(N1 - N0) < ZERO && E1 > E0)
        {
            doubAng01 = Math.PI / 2;
        }
        if (Math.abs(N1 - N0) < ZERO && E1 < E0)
        {
            doubAng01 = 1.5 * Math.PI;
        }
        return doubAng01;
    }
/**����ƫ�����*/
public static LinePoint GetStationOffset(LinePoint CeZhan, LinePoint HouShi, LinePoint CeDian)
{
    double douJ0, douJ1, douJ2, douPJ;
    douPJ = GetDistance((SurveyPoint)CeZhan, (SurveyPoint)CeDian);//���������ƽ��
    douJ0 = GetAzimuth((SurveyPoint)CeZhan, (SurveyPoint)HouShi); //�����վ����ӵķ�λ��
    douJ1 = GetAzimuth((SurveyPoint)CeZhan, (SurveyPoint)CeDian); //���������վ��λ��	     

    douJ2 = douJ0 - douJ1;//������Լн�
    //�������ƫ�����
    CeDian.Station= CeZhan.Station + douPJ * Math.cos(douJ2);
    CeDian.Offset = douPJ * Math.sin(douJ2);

    Log.i("douPJ",String.valueOf(douPJ));
    Log.i("CeDian.X",String.valueOf(CeDian.X));
    Log.i("CeDian.Y",String.valueOf(CeDian.Y));
    Log.i("CeZhan.X",String.valueOf(CeZhan.X));
    Log.i("CeZhan.Y",String.valueOf(CeZhan.Y));
    return CeDian;
}
/**�õ���·��ת����ת�ĽǶȸ�ʽ*/
public static String GetTurnAngle(LinePoint CeZhan, LinePoint HouShi, LinePoint QianShi)
{
    double douJ0, douJ1, douJ2;
    String SAngle = new String();
    SurveyAngle  sA=new SurveyAngle();

    douJ0 = GetAzimuth(CeZhan, HouShi);
    douJ1 = GetAzimuth(CeZhan, QianShi);
    douJ2 = douJ1 - douJ0;


    if (douJ2 < 0) { douJ2 = 2 * Math.PI + douJ2; }
    if (douJ2 < Math.PI)
    {
        douJ2 = Math.PI - douJ2;
        sA.valueOfRadian(douJ2);
        SAngle = "��ת " + sA.GetSubDegree()+"��" + sA.GetSubMinute()+"��" + sA.GetSubSecond(0)+"��";
    }
    if (douJ2 >= Math.PI)
    {
        douJ2 = douJ2 - Math.PI;
        sA.valueOfRadian(douJ2);
        SAngle = "��ת " + sA.GetSubDegree()+"��" + sA.GetSubMinute()+"��" + sA.GetSubSecond(0)+"��";
    }
    return SAngle;
}
public static String setDigitsToString(double number,int digits) {

	String d = "0";
	if (digits == 0) {
		d = "0";
	} else {
		d="0.";
		for (int i = 0; i < digits; i++) {
			d = d + "0";
		}
	}	
	DecimalFormat df = new DecimalFormat(d); // ����һ����ʽ����f	
	return df.format(number);
}
public static double setDigitsToDouble(double number,int digits) {

	String d = "0";
	if (digits == 0) {
		d = "0";
	} else {
		d="0.";
		for (int i = 0; i < digits; i++) {
			d = d + "0";
		}
	}	
	DecimalFormat df = new DecimalFormat(d); // ����һ����ʽ����f	
	return Double.valueOf(df.format(number));
}
}
