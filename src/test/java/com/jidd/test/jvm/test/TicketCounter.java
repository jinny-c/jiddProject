package com.jidd.test.jvm.test;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketCounter {
	private int count = 0;
	private int ticket = 100;
	private Lock lock = new ReentrantLock(true);// 公平锁

	public TicketCounter() {
		super();
	}

	public TicketCounter(int ticket) {
		this.ticket = ticket;
	}

	public int sale() {
		lock.lock();
		try {
			if (ticket <= 0) {
				System.out.println("没票了！！");
				return 0;
			}
			ticket--;
			count++;
			System.out.println("第" + count + "张卖出");
			return count;
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		} finally {
			lock.unlock();
		}
		return 0;
	}

	public static void main(String[] args) {
		Properties res = System.getProperties();
		System.out.println(res);
	}
}
