package com.jidd.test.jvm.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SaleTickets implements Runnable {
	public int total;
	public int count;
	private Lock lock;
	private Condition condition;

	public SaleTickets() {
		total = 100;
		count = 0;
		lock = new ReentrantLock(true);// 公平锁
		// lock = new ReentrantLock();
		// 即便是公平锁，如果通过不带超时时间限制的tryLock()的方式获取锁的话，它也是不公平的
		// 但是带有超时时间限制的tryLock(long timeout, TimeUnit unit)方法则不一样，还是会遵循公平或非公平的原则的
		condition = lock.newCondition();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		method();
	}

	private void method() {
		while (true) {
			try {

				// 买票前准备,休眠1毫秒模拟拿出证件
				Thread.sleep(4);
				// 获取当前线程名字
				String threadName = Thread.currentThread().getName();
				if (lock.tryLock()) {
					try {
						System.out.println("-----" + threadName + " tryLock()");
						if (!sale(threadName)) {
							break;
						}
					} finally {
						lock.unlock();
					}
				} else {
					if (lock.tryLock(2, TimeUnit.SECONDS)) {
						try {
							System.out.println("======" + threadName
									+ " tryLock(5)");
							if (!sale(threadName)) {
								break;
							}
						} finally {
							lock.unlock();
						}
					} else {
						System.out.println(threadName + " 啥也没干!");
					}
				}
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.getStackTrace();
			}
		}
	}

	private boolean sale(String threadName) throws InterruptedException {
		if (total <= 0) {
			System.out.println(threadName + " 车票已售罄!");
			return false;
		}
		if (total > 90 && threadName.contains("A")) {
			// Thread.sleep(3000);
			System.out.println(threadName + " 休息片刻!");
			condition.await();
		}
		// Thread.sleep(1000);
		if (total < 90 && total > 80 && threadName.contains("B")) {
			System.out.println(threadName + " 休息片刻!");
			condition.await();
		}
		if (total < 80 && total > 70 && threadName.contains("C")) {
			System.out.println(threadName + " 休息片刻!");
			condition.await();
		}
		// if (total == 1) {
		if (total == 60||total == 50||total == 40||total == 30) {
			System.out.println(threadName + " 都来继续工作!");
			//condition.signalAll();
			condition.signal();
			//Thread.sleep(2001);
		}

		Thread.sleep(10);
		// 打印火车票,休眠10毫秒模拟打印车票时间
		System.out.println(threadName + " 售出火车票No." + (++count));
		total--;
		return true;
	}

	private void method1() {
		while (true) {
			try {
				// 获取当前线程名字
				String threadName = Thread.currentThread().getName();
				lock.lock();
				if (total <= 0) {
					System.out.println(threadName + " 车票已售罄!");
					break;
				}
				// 打印火车票,休眠20毫秒模拟打印车票时间
				Thread.sleep(20);
				System.out.println(threadName + " 售出火车票No." + ++count);
				total--;
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.getStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	private void method2() {
		while (true) {
			// 获取当前线程名字
			String threadName = Thread.currentThread().getName();

			synchronized (SaleTickets.class) {
				if (total <= 0) {
					System.out.println(threadName + " 车票已售罄!");
					break;
				}
				// 打印火车票,休眠20毫秒模拟打印车票时间
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(threadName + " 售出火车票No." + ++count);
				total--;
			}
		}
	}

	public static void main(String[] args) {
		SaleTickets ticket = new SaleTickets();
		// 创建售票线程 ,并设置窗口名字,然后启动线程,这里设置四个窗口
		new Thread(ticket, "窗口A").start();
		new Thread(ticket, "窗口B").start();
		new Thread(ticket, "窗口C").start();
		new Thread(ticket, "窗口D").start();
	}
}
