package com.jidd.test.jvm.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

public class MethodDoTest {

	public static void main(String[] args) {
		TicketCounter tc1 = new TicketCounter();
		TicketCounter tc2 = new TicketCounter(19);

		method1(tc1);
		//method1(tc2);
		method2();
		method2();
	}

	private static void method1(final TicketCounter tc) {
		Runnable rb = new Runnable() {
			public void run() {
				// 获取当前线程名字
				String threadName = Thread.currentThread().getName();
				int doCount = 0;
				while (true) {
					int ticket = tc.sale();
					//System.out.println(threadName + " sale第 " + ticket + "张");
					if (ticket <= 0) {
						System.out.println(threadName + " 卖了" + doCount + "张");
						break;
					}
					doCount++;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		new Thread(rb, "窗口A").start();
		new Thread(rb, "窗口B").start();
		//new Thread(rb, "窗口C").start();
		new Thread(rb, "窗口D").start();
		//new Thread(rb, "窗口E").start();

	}

	private static void method2() {
		int corePoolSize = 3;
		int maxPoolSize = 5;
		int keepAliveTime = 100;
		//直接提交策略
		ThreadPoolExecutor executor1 = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
				TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),new AbortPolicy());
		//无界队列策略
		ThreadPoolExecutor executor2 = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new CallerRunsPolicy());
		//有界队列
		ThreadPoolExecutor executor3 = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
		
		for (int i = 0; i < 5; i++) {
            // 产生一个任务，并将其加入到线程池
            String task = "task@ " + i;
            System.out.println("put task=" + task);
            executor3.execute(new SaleTickets1());
		}
		
	}
}
