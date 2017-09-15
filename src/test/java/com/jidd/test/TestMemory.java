package com.jidd.test;

import com.jidd.basic.utils.JiddStringUtils;

public class TestMemory {

	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {

		System.out.println(" 内存信息 :" + toMemoryInfo());

		System.out.println(JiddStringUtils.replace("use 3des decrypt succ: cipher=[{}], plain=[{}], key=[{}]",
	            "85B3762A5F962FB6", "u:$4567", "3391cad7f846d35e3391cad7f846d35e3391cad7f846d35e"));
		
	}

	/**
	 * 获取当前 jvm 的内存信息
	 * @return
	 */

	public static String toMemoryInfo() {

		Runtime currRuntime = Runtime.getRuntime();

		int nFreeMemory = (int) (currRuntime.freeMemory() / 1024 / 1024);

		int nTotalMemory = (int) (currRuntime.totalMemory() / 1024 / 1024);

		return nFreeMemory + "M/" + nTotalMemory + "M(free/total)";

	}

}
