package com.jidd.test.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ImportTxt {
	public static String encoding = "GBK";
	public static String SOURCESURL ="D:/workSpace/hpay-micro/javaMaven/javaMaven/src/main/resources/file/";
	
	public static List<String> readTxtFile(String name) {
		//StringBuffer content = new StringBuffer();
		List<String> res = new ArrayList<String>();
		try {
			File file = new File(SOURCESURL + name);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//System.out.println(lineTxt);
					//content.append(lineTxt);
					res.add(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return res;
	}

	public static void writeFile(String name) {
		StringBuffer strb = new StringBuffer();
		for (int i = 0; i < 20000; i++) {
			strb.append("wenbo00" + i).append("\r\n");
		}
		writeStr(strb.toString(), name);
	}
	// 写文件
	public static void writeStr(String content, String name) {
		try {
			// FileWriter fw = new FileWriter( name );
			// PrintWriter pw = new PrintWriter(fw);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(SOURCESURL + name), encoding));
			// System.out.println(name);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
