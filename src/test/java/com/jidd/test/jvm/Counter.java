package com.jidd.test.jvm;

public class Counter {
	public volatile static int count = 0;

	public synchronized static void inc() {
		//System.out.println("count="+count);
//		synchronized(Counter.class){
//		}
		// 这里延迟1毫秒，使得结果明显
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		
		count++;
	}

	public static void main(String[] args) {

		// 同时启动1000个线程，去进行i++计算，看看实际结果

		for (int i = 0; i < 1000; i++) {
			//System.out.println(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Counter.inc();
					//SingleCounter.getInstance().inc();
				}
			}).start();
		}

		// 这里每次运行的值都有可能不同,可能为1000
		System.out.println("运行结果:1Counter.count=" + Counter.count);
		try {
			Thread.sleep(10);
			System.out.println("运行结果:10Counter.count=" + Counter.count);
			Thread.sleep(100);
			System.out.println("运行结果:100Counter.count=" + Counter.count);
			Thread.sleep(1000);
			System.out.println("运行结果:1000Counter.count=" + Counter.count);
		} catch (InterruptedException e) {
		}
	}
}
