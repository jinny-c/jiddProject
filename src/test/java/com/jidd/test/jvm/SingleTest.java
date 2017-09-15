package com.jidd.test.jvm;

import java.util.concurrent.atomic.AtomicInteger;

public class SingleTest {
	public volatile static int count = 0;
	private static AtomicInteger syCount = new AtomicInteger(0);
	
	private Byte[] obj = new Byte[0];
	
	private static SingleTest instance = new SingleTest();
	
	private SingleTest(){}
	
    public static SingleTest getInstance() {
    	//System.out.println("start creat instance");
    	return instance;
    }
    static {
    	System.out.println("this is static void!");
    }
	public synchronized void inc1() {
		System.out.println("inc1() count=" + count);
		//System.out.println("hashCode=" + instance.hashCode());
		// 这里延迟1毫秒，使得结果明显
/*		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/
		
		count++;
	}
	public void inc2() {
		// 这里延迟1毫秒，使得结果明显
		/*		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/
		synchronized(this){
			System.out.println("inc2() count=" + count);
			//System.out.println("hashCode=" + instance.hashCode());
			count++;
		}
	}
	public void inc3() {
		// 这里延迟1毫秒，使得结果明显
		/*		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/
		synchronized(obj){
			System.out.println("inc3(ojb) count=" + count);
			//System.out.println("hashCode=" + instance.hashCode());
			count++;
		}
	}
	public void inc4() {
		// 这里延迟1毫秒，使得结果明显
		/*		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/
		synchronized(SingleTest.class){
			System.out.println("inc4() count=" + count);
			//System.out.println("hashCode=" + instance.hashCode());
			count++;
		}
	}
	
	public void inc4(Object ojb) {
		// 这里延迟1毫秒，使得结果明显
		/*		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/
		synchronized(ojb){
			System.out.println("inc4(ojb) count=" + count);
			//System.out.println("hashCode=" + instance.hashCode());
			count++;
		}
	}
	
	public void add() {
		System.out.println("syCount=" + syCount);
		//System.out.println("hashCode=" + instance.hashCode());
		syCount.getAndIncrement();
	}
}
