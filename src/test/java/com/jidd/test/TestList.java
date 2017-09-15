package com.jidd.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class TestList {

	@Test
	public void listMethod() throws Exception {

		String[] arr = { "a1", "b2" };

		List<String> list = Arrays.asList(arr);
		// TODO 这里的list对象 无add()方法
		// list.add("c3");
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));
		arrayList.add("c3");

		System.out.println("===========" + list);
		System.out.println("===========" + arrayList);
		System.out.println("===========" + Arrays.asList(arr).contains("b2"));

		ArrayList<String> aList = new ArrayList<String>(Arrays.asList("a", "b",
				"c", "d"));
		Iterator<String> iter = aList.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			if (s.equals("a") || s.equals("d")) {
				System.out.println("remove s=" + s);
				iter.remove();
			}
		}
		System.out.println("===========" + aList);
		
		int h=123;
		//int length=(int) Math.pow(2,4);
		int length=2<<5;
		
		System.out.println("===========" + (h & (length-1)) );
		System.out.println("===========" + h%length );
		
	}

}
