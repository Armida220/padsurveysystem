package com.padsurveysystem;

/** �ο����� */
class ReferenceEllipsoid {
	public String Name;
	private double A;// ���򳤰���
	private double B;// ����̰���
	private double F;// �������
	private double E1;// ��һƫ����e
	private double E2;// �ڶ�ƫ����e'

	public ReferenceEllipsoid(String name,double a, double f) {
		Name=name;
		A = a;
		F = f;
		B = A - A * F;
		E1 = Math.sqrt(2.0 * F - F * F);
		E2 = Math.sqrt(A * A - B * B) / B;
	}

	public double a() {
		return A;
	}

	public double b() {
		return B;
	}

	public double f() {
		return F;
	}

	public double e1() {
		return E1;
	}

	public double e2() {
		return E2;
	}

}

/** ����ϵͳ */
public class CoordinateSystem {
	public ReferenceEllipsoid ReferenceEllipsoid;
	private ReferenceEllipsoid re;
	public double MiddleLongitude = 0.0;// ����������
	public double OffsetNorth = 0.0;// ������ƫ����
	public double OffsetEast = 500000.0;// ������ƫ����
	public double CorrectNorth = 0.0;// �����������
	public double CorrectEast = 0;// �����������

	public static final ReferenceEllipsoid WGS84 = new ReferenceEllipsoid("WGS84����ϵ",
			6378137.0, 1 / 298.257223563);
	public static final ReferenceEllipsoid BEIJIN54 = new ReferenceEllipsoid("1954�걱������ϵ",
			6378245.0, 1 / 298.3);
	public static final ReferenceEllipsoid XIAN80 = new ReferenceEllipsoid("1980��������ϵ",
			6378140.0, 1 / 298.257);
	public static final ReferenceEllipsoid NATIONAL2000 = new ReferenceEllipsoid("����2000����ϵ",
			6378137.0, 1 / 298.257222101);

	public static ReferenceEllipsoid GetReferenceEllipsoid(int Index) {
		switch (Index) {
		case 0:
			return WGS84;
		case 1:
			return BEIJIN54;
		case 2:
			return XIAN80;
		case 3:
			return NATIONAL2000;
		default:
			return WGS84;
		}
	}

	public CoordinateSystem(String Name, double a, double f, double middlelongitude) {
		ReferenceEllipsoid = new ReferenceEllipsoid(Name,a, f);
		MiddleLongitude = middlelongitude;
	}

	public CoordinateSystem() {
	}

}