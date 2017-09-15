package com.jidd.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestLock {
	private Lock lock1;
	private Lock lock2;

	@Test
	public void lockMethod1() throws Exception {
		lock1 = new ReentrantLock(); // 参数默认false，不公平锁
		if (lock1.tryLock()) { // 如果已经被lock，则立即返回false不会等待，达到忽略操作的效果
			try {
				System.out.println("lock1=======");
			} finally {
				lock1.unlock();
			}
		}
	}

	public void lockMethod3() throws Exception {
		lock1 = new ReentrantLock(); // 参数默认false，不公平锁
		if (lock1.tryLock(5, TimeUnit.SECONDS)) {
			// 如果已经被lock，尝试等待5s，看是否可以获得锁，如果5s后仍然无法获得锁则返回false继续执行
			try {
				System.out.println("lock1=======");
			} finally {
				lock1.unlock();
			}
		}
	}

	public void lockMethod2() throws Exception {
		lock2 = new ReentrantLock(true);// 公平锁
		lock2.lock(); // 如果被其它资源锁定，会在此等待锁释放，达到暂停的效果
		try {
			System.out.println("lock2======");
		} finally {
			lock2.unlock();
		}
	}
	public void lockMethod4() throws Exception {
		lock2 = new ReentrantLock(true);// 公平锁
		lock2.lockInterruptibly();
		try {
			System.out.println("lock2======");
		} finally {
			lock2.unlock();
		}
	}

}
