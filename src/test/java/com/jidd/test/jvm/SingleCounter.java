package com.jidd.test.jvm;

public class SingleCounter {
	public static int count = 0;
	
	private static SingleCounter instance = new SingleCounter();
	
	private SingleCounter(){}
	
    public static SingleCounter getInstance() {
    	return instance;
    }
    
	public synchronized void inc() {
		System.out.println("count=" + count);
		// 这里延迟1毫秒，使得结果明显
/*		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/
		
		count++;
	}
}
