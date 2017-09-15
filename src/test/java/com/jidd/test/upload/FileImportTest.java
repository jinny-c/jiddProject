package com.jidd.test.upload;

import java.util.List;

import com.jidd.basic.utils.JiddStringUtils;

public class FileImportTest {
	public static void main(String[] args) {
		doTxt();
		//doCompare();
	}

	public static void doCompare() {
		String name1 = "isHave.txt";
		//String name1 = "isInWorkspace.txt";
		String name2 = "isAll.txt";
		//String name1 = "isDone.txt";
		
		StringBuffer tempA = new StringBuffer();
		List<String> resList1 = ImportTxt.readTxtFile(name1);
		List<String> resList2 = ImportTxt.readTxtFile(name2);
		System.out.println(resList1.size());
		System.out.println(resList2.size());
		
		for(String res2 : resList2){
			
			if(!resList1.contains(res2)){
				System.out.println(res2);
				tempA.append(res2).append("\r\n");
			}
			
		}
		ImportTxt.writeStr(tempA.toString(), "compareRes.txt");
	}
	public static void doTxt() {
		String name = "haveAll.txt";
		StringBuffer tempA = new StringBuffer();
		List<String> resList = ImportTxt.readTxtFile(name);
		for(String res : resList){
			tempA.append(JiddStringUtils.join("project.name.",res,"=",res)).append("\r\n");
		}
		ImportTxt.writeStr(tempA.toString(), "haveAll_res.txt");
	}
	public static void doTxt1() {
		String name = "test.txt";
		StringBuffer tempA = new StringBuffer();
		StringBuffer tempB = new StringBuffer();
		String temp = "";
		List<String> resList = ImportTxt.readTxtFile(name);
		for(String res : resList){
			temp = res.replaceAll("https://hpfs01.handpay.com.cn:8443/svn/paymentMaven/", "");
			tempB.append(JiddStringUtils.join("project.name.",temp,"=",temp)).append("\r\n");
			tempA.append(temp).append("\r\n");
		}
		ImportTxt.writeStr(tempB.toString(), "testRes_B.txt");
		ImportTxt.writeStr(tempA.toString(), "testRes_A.txt");
	}
}
