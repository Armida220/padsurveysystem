package com.padsurveysystem;

/** �Ͽ�������ϵ */
class CartesianCoordinatePoint {
	public double X;
	public double Y;
	public double H;
}

/** �������ϵ */
class GeodeticCoordinatePoint {
	public double Longitude; // ����
	public double Latitude; // γ��
	public double Height; // ��ظ�
}

/** �ռ�ֱ������ϵ */
class CartesianSpaceCoordinatePoint {
	public double X;
	public double Y;
	public double Z;
}

class CoordinateTransform {
	public static CoordinateSystem BEIJIN54 = new CoordinateSystem("1954�걱������ϵ",6378245.0,
			1 / 298.3, 0);
	public static CoordinateSystem XIAN80 = new CoordinateSystem("1980��������ϵ",6378140.0,
			1 / 298.257, 0);
	public static CoordinateSystem NATIONAL2000 = new CoordinateSystem("����2000����ϵ",
			6378137.0, 1 / 298.257222101, 0);
	public static CoordinateSystem WGS84 = new CoordinateSystem("WGS84����ϵ",6378137.0,
			1 / 298.257223563, 0);

	/** ת�������ת��Ϊ�ռ�ֱ������ */
	public static void GeodeticToCartesianSpace(
			CartesianSpaceCoordinatePoint pcs, GeodeticCoordinatePoint pcg,
			CoordinateSystem cy) {
		double X;// �ռ�ֱ������ϵ
		double Y;// �ռ�ֱ������ϵ
		double Z;// �ռ�ֱ������ϵ
		double B;// γ�ȶ���
		double L;// ���ȶ���
		double H;// ��ظ�
		double a;// Ϊ���򳤰���
		double e1;// ��һƫ����
		double W;// �м����
		double N;// î��Ȧ���ʰ뾶

		B = pcg.Latitude;// ������γ��
		L = pcg.Longitude;// �����ľ���
		H = pcg.Height;

		a = cy.ReferenceEllipsoid.a();
		e1 = cy.ReferenceEllipsoid.e1();
		W = Math.sqrt(1.0 - Math.pow(e1 * Math.sin(B), 2.0));
		N = a / W;

		X = (N + H) * Math.cos(B) * Math.cos(L);
		Y = (N + H) * Math.cos(B) * Math.sin(L);
		Z = (N * (1.0 - e1 * e1) + H) * Math.sin(B);

		pcs.X = X;
		pcs.Y = Y;
		pcs.Z = Z;
	}

	/** ת�ռ�ֱ�����껻Ϊ������� */
	public static void CartesianSpaceToGeodetic(
			CartesianSpaceCoordinatePoint pcs, GeodeticCoordinatePoint pcg,
			CoordinateSystem cy) {
		double X;// �ռ�ֱ������ϵ
		double Y;// �ռ�ֱ������ϵ
		double Z;// �ռ�ֱ������ϵ
		double B;// γ�ȶ���
		double L;// ���ȶ���
		double H;// ��ظ�
		double a;// Ϊ���򳤰���
		double b;// Ϊ����̰���
		double e1;// ��һƫ����
		double e2;
		double W;// �м����
		double N;// î��Ȧ���ʰ뾶
		double BT1;
		double BT2;
		double U;
		double B0;
		double H0;
		double H1;

		X = pcs.X;
		Y = pcs.Y;
		Z = pcs.Z;

		a = cy.ReferenceEllipsoid.a();
		b = cy.ReferenceEllipsoid.b();
		e1 = cy.ReferenceEllipsoid.e1();
		e2 = cy.ReferenceEllipsoid.e2();

		U = Math.atan((Z * a) / (Math.sqrt(X * X + Y * Y) * b));
		B0 = Math.atan((Z + b * e2 * e2 * Math.sin(U) * Math.sin(U)
				* Math.sin(U))
				/ (Math.sqrt(X * X + Y * Y) - a * e1 * e1 * Math.cos(U)
						* Math.cos(U) * Math.cos(U)));
		W = Math.sqrt(1.0 - Math.pow(e1 * Math.sin(B0), 2.0));
		N = a / W;

		H0 = X * X + Y * Y;
		H1 = Z + N * e1 * e1 * Math.sin(B0);
		H = Math.sqrt(H0 + H1 * H1) - N;

		BT1 = Z / Math.sqrt(X * X + Y * Y);
		BT2 = (1 - e1 * e1 * N / (N + H));
		B = Math.atan(BT1 / BT2);

		L = Math.atan(Y / X);
		if (L < 0)
			L = L + Math.PI;
		pcg.Longitude = L;
		pcg.Latitude = B;
		pcg.Height = H;
	}

	/** ��˹��������,���������ת��Ϊ�������� */
	public static void GeodeticToCartesian(CartesianCoordinatePoint pcc,
			GeodeticCoordinatePoint pgc, CoordinateSystem cy) {
		double x;// �Ͽ�������ϵ
		double y;// �Ͽ�������ϵ
		double B;// γ�ȶ���
		double L;// ���ȶ���
		double L0;// ���뾭�߶���
		double l;// L-L0
		double a;// Ϊ���򳤰���
		double e1;// ��һƫ����
		double e2;// �ڶ�ƫ����
		double g;// �м������
		double t;// �м����
		double m;// �м����
		double W;// �м����
		double N;// î��Ȧ���ʰ뾶
		double X;// �����߻���
		double X1, X2, X3;// ����X���м����
		double Y1, Y2;// ����y���м����

		B = pgc.Latitude;// ������γ��
		L = pgc.Longitude;// �����ľ���
		L0 = cy.MiddleLongitude;// ����������
		l = L - L0;
		a = cy.ReferenceEllipsoid.a();
		e1 = cy.ReferenceEllipsoid.e1();
		e2 = cy.ReferenceEllipsoid.e2();
		g = e2 * Math.cos(B);
		t = Math.tan(B);
		m = Math.cos(B) * l;
		W = Math.sqrt(1.0 - Math.pow(e1 * Math.sin(B), 2.0));
		N = a / W;

		X = X0(B, a, e1);

		X1 = N * t * Math.pow(Math.cos(B) * l, 2.0);
		X2 = (1.0 / 24.0)
				* (5.0 - Math.pow(t, 2.0) + 9.0 * Math.pow(g, 2.0) + 4.0 * Math
						.pow(g, 4.0)) * Math.pow(Math.cos(B) * l, 2.0);
		X3 = (1.0 / 720.0)
				* (61.0 - 58.0 * Math.pow(t, 2.0) + Math.pow(t, 4.0))
				* Math.pow(Math.cos(B) * l, 4.0);

		x = X + X1 * (0.5 + X2 + X3);

		Y1 = (1.0 / 6.0) * (1.0 - Math.pow(t, 2.0) + Math.pow(g, 2.0))
				* Math.pow(m, 3.0);
		Y2 = (1.0 / 120.0)
				* (5.0 - 18.0 * Math.pow(t, 2.0) + Math.pow(t, 4.0) + 14.0
						* Math.pow(g, 2.0) - 58.0 * Math.pow(g * t, 2.0))
				* Math.pow(m, 5.0);

		y = N * (m + Y1 + Y2);

		pcc.X = x;
		pcc.Y = y + cy.OffsetEast;
		pcc.H = pgc.Height;
	}

	// ��˹���귴��
	public static void CartesianToGeodetic(GeodeticCoordinatePoint pcg,
			CartesianCoordinatePoint pcc, CoordinateSystem cy) {
		double x;// �Ͽ�������ϵ
		double y;// �Ͽ�������ϵ
		double B;// γ�ȶ���
		double L;// ���ȶ���
		double Bf = 0;// ����γ��ʱҪ�õ��м�ֵ���׵�γ��
		double tFB0, tFB1;// ����BfҪ�õ��м����
		double L0;// ���뾭�߶���
		double l;// L-L0
		double a;// Ϊ���򳤰���
		double e1;// ��һƫ����
		double e2;// �ڶ�ƫ����
		double gf;// �м������
		double tf;// �м����
		double Mf;// �м����
		double Wf;// �м����
		double Nf;// î��Ȧ���ʰ뾶
		double B1, B2, B3;// ����X���м����
		double L1, L2, L3;// ����y���м����

		x = pcc.X;// ������
		y = pcc.Y - cy.OffsetEast;// ��ȥ��ƫ���������ֵ
		L0 = cy.MiddleLongitude;// ����������
		a = cy.ReferenceEllipsoid.a();
		e1 = cy.ReferenceEllipsoid.e1();
		e2 = cy.ReferenceEllipsoid.e2();

		Bf = SurveyMath.DEGToRadian(x / 1000000);
		// ��������׵�γ��Bf
		while (true) {
			B = Bf;
			tFB0 = FB0(B, a, e1);
			tFB1 = FB1(B, a, e1);
			Bf = B + (x - tFB0) / tFB1;
			if (Math.abs((B - Bf)) < 0.0000000001)
				break;
		}

		gf = e2 * Math.cos(Bf);
		tf = Math.tan(Bf);
		Wf = Math.sqrt(1.0 - Math.pow(e1 * Math.sin(Bf), 2.0));
		Mf = a * (1.0 - Math.pow(e1, 2)) / Math.pow(Wf, 3.0);
		Nf = a / Wf;

		B1 = (tf / (2.0 * Mf)) * y * (y / Nf);
		B2 = (1.0 / 12.0)
				* (5.0 + 3.0 * Math.pow(tf, 2.0) + Math.pow(gf, 2.0) - 9.0 * Math
						.pow(gf * tf, 2.0)) * Math.pow((y / Nf), 2.0);
		B3 = (1.0 / 360.0)
				* (61.0 + 90.0 * Math.pow(tf, 2.0) + 45.0 * Math.pow(tf, 4.0))
				* Math.pow((y / Nf), 4.0);
		B = Bf - B1 * (1.0 - B2 + B3);

		L1 = y / (Math.cos(Bf) * Nf);
		L2 = (1.0 / 6.0) * (1.0 + 2.0 * Math.pow(tf, 2.0) + Math.pow(gf, 2.0))
				* Math.pow((y / Nf), 2.0);
		L3 = (1.0 / 120.0)
				* (5.0 + 28.0 * Math.pow(tf, 2.0) + 24.0 * Math.pow(tf, 4.0)
						+ 6.0 * Math.pow(gf, 2.0) + 8.0 * Math
						.pow(gf * tf, 2.0)) * Math.pow((y / Nf), 4.0);
		l = L1 * (1 - L2 + L3);
		L = L0 + l;

		pcg.Longitude = L;
		pcg.Latitude = B;

	}

	private static double X0(double B, double a, double e1) {
		double X, A0, A1, A2, A3, A4, A5, A6;// ����N���м����
		A0 = 1.0 + (3.0 / 4.0) * Math.pow(e1, 2.0) + (45.0 / 64.0)
				* Math.pow(e1, 4.0) + (175.0 / 256.0) * Math.pow(e1, 6.0)
				+ (11025.0 / 16384.0) * Math.pow(e1, 8.0) + (43659.0 / 65536.0)
				* Math.pow(e1, 10.0) + (693693.0 / 1048576.0)
				* Math.pow(e1, 12.0);
		A1 = (3.0 / 8.0) * Math.pow(e1, 2.0) + (15.0 / 32.0)
				* Math.pow(e1, 4.0) + (525.0 / 1024.0) * Math.pow(e1, 6.0)
				+ (2205.0 / 4096.0) * Math.pow(e1, 8.0) + (72765.0 / 131072.0)
				* Math.pow(e1, 10.0) + (297297.0 / 524288.0)
				* Math.pow(e1, 12.0);
		A2 = (15.0 / 256.0) * Math.pow(e1, 4.0) + (105.0 / 1024.0)
				* Math.pow(e1, 6.0) + (2205.0 / 16384.0) * Math.pow(e1, 8.0)
				+ (10395.0 / 65536.0) * Math.pow(e1, 10.0)
				+ (1486485.0 / 8388608.0) * Math.pow(e1, 12.0);
		A3 = (35.0 / 3072.0) * Math.pow(e1, 6.0) + (105.0 / 4096.0)
				* Math.pow(e1, 8.0) + (10395.0 / 262144.0) * Math.pow(e1, 10.0)
				+ (55055.0 / 1048576.0) * Math.pow(e1, 12.0);
		A4 = (315.0 / 131072.0) * Math.pow(e1, 8.0) + (3465.0 / 524288.0)
				* Math.pow(e1, 10.0) + (99099.0 / 8388608.0)
				* Math.pow(e1, 12.0);
		A5 = (693.0 / 1310720.0) * Math.pow(e1, 10.0) + (9009.0 / 5242880.0)
				* Math.pow(e1, 12.0);
		A6 = (1001.0 / 8388608.0) * Math.pow(e1, 12.0);

		X = a
				* (1.0 - Math.pow(e1, 2.0))
				* (A0 * B - A1 * Math.sin(2.0 * B) + A2 * Math.sin(4.0 * B)
						- A3 * Math.sin(6.0 * B) + A4 * Math.sin(8.0 * B) - A5
						* Math.sin(10.0 * B) + A6 * Math.sin(12.0 * B));

		return X;
	}// �����߻���

	private static double FB0(double B, double a, double e1) {
		double F, A0, A1, A2, A3, A4, A5, A6;// ����N���м����

		A0 = 1.0 + (3.0 / 4.0) * Math.pow(e1, 2.0) + (45.0 / 64.0)
				* Math.pow(e1, 4.0) + (175.0 / 256.0) * Math.pow(e1, 6.0)
				+ (11025.0 / 16384.0) * Math.pow(e1, 8.0) + (43659.0 / 65536.0)
				* Math.pow(e1, 10.0) + (693693.0 / 1048576.0)
				* Math.pow(e1, 12.0);
		A1 = (3.0 / 8.0) * Math.pow(e1, 2.0) + (15.0 / 32.0)
				* Math.pow(e1, 4.0) + (525.0 / 1024.0) * Math.pow(e1, 6.0)
				+ (2205.0 / 4096.0) * Math.pow(e1, 8.0) + (72765.0 / 131072.0)
				* Math.pow(e1, 10.0) + (297297.0 / 524288.0)
				* Math.pow(e1, 12.0);
		A2 = (15.0 / 256.0) * Math.pow(e1, 4.0) + (105.0 / 1024.0)
				* Math.pow(e1, 6.0) + (2205.0 / 16384.0) * Math.pow(e1, 8.0)
				+ (10395.0 / 65536.0) * Math.pow(e1, 10.0)
				+ (1486485.0 / 8388608.0) * Math.pow(e1, 12.0);
		A3 = (35.0 / 3072.0) * Math.pow(e1, 6.0) + (105.0 / 4096.0)
				* Math.pow(e1, 8.0) + (10395.0 / 262144.0) * Math.pow(e1, 10.0)
				+ (55055.0 / 1048576.0) * Math.pow(e1, 12.0);
		A4 = (315.0 / 131072.0) * Math.pow(e1, 8.0) + (3465.0 / 524288.0)
				* Math.pow(e1, 10.0) + (99099.0 / 8388608.0)
				* Math.pow(e1, 12.0);
		A5 = (693.0 / 1310720.0) * Math.pow(e1, 10.0) + (9009.0 / 5242880.0)
				* Math.pow(e1, 12.0);
		A6 = (1001.0 / 8388608.0) * Math.pow(e1, 12.0);

		F = a
				* (1.0 - Math.pow(e1, 2.0))
				* (A0 * B - A1 * Math.sin(2.0 * B) + A2 * Math.sin(4.0 * B)
						- A3 * Math.sin(6.0 * B) + A4 * Math.sin(8.0 * B) - A5
						* Math.sin(10.0 * B) + A6 * Math.sin(12.0 * B));
		return F;
	}// �׵�γ���õ��ĺ���1

	private static double FB1(double B, double a, double e1) {

		double F, A0, A1, A2, A3, A4, A5, A6;// ����N���м����

		A0 = 1.0 + (3.0 / 4.0) * Math.pow(e1, 2.0) + (45.0 / 64.0)
				* Math.pow(e1, 4.0) + (175.0 / 256.0) * Math.pow(e1, 6.0)
				+ (11025.0 / 16384.0) * Math.pow(e1, 8.0) + (43659.0 / 65536.0)
				* Math.pow(e1, 10.0) + (693693.0 / 1048576.0)
				* Math.pow(e1, 12.0);
		A1 = (3.0 / 8.0) * Math.pow(e1, 2.0) + (15.0 / 32.0)
				* Math.pow(e1, 4.0) + (525.0 / 1024.0) * Math.pow(e1, 6.0)
				+ (2205.0 / 4096.0) * Math.pow(e1, 8.0) + (72765.0 / 131072.0)
				* Math.pow(e1, 10.0) + (297297.0 / 524288.0)
				* Math.pow(e1, 12.0);
		A2 = (15.0 / 256.0) * Math.pow(e1, 4.0) + (105.0 / 1024.0)
				* Math.pow(e1, 6.0) + (2205.0 / 16384.0) * Math.pow(e1, 8.0)
				+ (10395.0 / 65536.0) * Math.pow(e1, 10.0)
				+ (1486485.0 / 8388608.0) * Math.pow(e1, 12.0);
		A3 = (35.0 / 3072.0) * Math.pow(e1, 6.0) + (105.0 / 4096.0)
				* Math.pow(e1, 8.0) + (10395.0 / 262144.0) * Math.pow(e1, 10.0)
				+ (55055.0 / 1048576.0) * Math.pow(e1, 12.0);
		A4 = (315.0 / 131072.0) * Math.pow(e1, 8.0) + (3465.0 / 524288.0)
				* Math.pow(e1, 10.0) + (99099.0 / 8388608.0)
				* Math.pow(e1, 12.0);
		A5 = (693.0 / 1310720.0) * Math.pow(e1, 10.0) + (9009.0 / 5242880.0)
				* Math.pow(e1, 12.0);
		A6 = (1001.0 / 8388608.0) * Math.pow(e1, 12.0);
		F = a
				* (1.0 - Math.pow(e1, 2.0))
				* (A0 - 2.0 * A1 * Math.cos(2.0 * B) + 4.0 * A2
						* Math.cos(4.0 * B) - 6.0 * A3 * Math.cos(6.0 * B)
						+ 8.0 * A4 * Math.cos(8.0 * B) - 10.0 * A5
						* Math.cos(10.0 * B) + 12.0 * A6 * Math.cos(12.0 * B));
		return F;
	}// �׵�γ���õ��ĺ���1

}
