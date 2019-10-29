package com.padsurveysystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.widget.Toast;

//�ļ��ļ�������
public class TextFile {

	public void Write(String FilePath, String Content) {
		// ���filePath�Ǵ��ݹ����Ĳ�����������һ����׺�����жϣ� û��ָ�����ļ���û�к�׺�����Զ�����Ϊ.txt��ʽ
		if (!FilePath.endsWith(".csv") && !FilePath.endsWith(".log"))
			FilePath += ".csv";
		// �����ļ�
		File file = new File(FilePath);

		try {
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(Content);
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

}
