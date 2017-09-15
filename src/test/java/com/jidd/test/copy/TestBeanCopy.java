package com.jidd.test.copy;

import org.junit.Test;

import com.jidd.basic.utils.JiddObjectUtils;
import com.jidd.test.copy.bean.TestBean;
import com.jidd.test.copy.bean.TestBean2;


public class TestBeanCopy {

	public static void main(String[] args) {
		TestBean tb = new TestBean();
		tb.setChannel("channel");
		tb.setUserAge("123");
		System.out.println("======" + tb.toString());
		
		TestBean2 tb2 = new TestBean2();
		
		JiddObjectUtils.copyProperty(tb2, tb);
		
		System.out.println("======" + tb2.toString());
	}
    @Test
    public void testNeedTime() throws Exception{
    	TestBean tb = new TestBean();
		tb.setChannel("channel");
		tb.setUserName("userName");
		tb.setUserRealName("姓名");
		tb.setUserAge("123");
		System.out.println("======" + tb.toString());
		int c = 0;
		while (c < 10) {
			long begin_h = System.currentTimeMillis();
			long begin_n = System.nanoTime();
			
			for (int i = 0; i < 1000; i++) {
				TestBean2 tb2 = new TestBean2();
				JiddObjectUtils.copyProperty(tb2, tb);
			}
			long end1_h = System.currentTimeMillis();
			long end1_n = System.nanoTime();
			
			System.out.println(end1_h - begin_h);
			System.out.println(end1_n - begin_n);
			
			for (int j = 0; j < 1000; j++) {
				JiddObjectUtils.copyProperty(TestBean2.class, tb);
			}
			
			System.out.println(System.currentTimeMillis() - end1_h);
			System.out.println(System.nanoTime() - end1_n);
			
			c++;
		}
    }

}
