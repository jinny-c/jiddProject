package com.jidd.test.jvm.test;



public class SaleTickets1 extends Thread {
	public int total;
	public int count;
	
	public SaleTickets1() {
		total = 10;
		count = 0;
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
				if (!sale(threadName)) {
					break;
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
		Thread.sleep(10);
		// 打印火车票,休眠10毫秒模拟打印车票时间
		System.out.println(threadName + " 售出火车票No." + ++count);
		total--;
		return true;
	}
	
	public static void main(String[] args) {
		SaleTickets1 st = new SaleTickets1();
		st.setName("st1线程");
		SaleTickets1 st2 = new SaleTickets1();
		st2.setName("st2线程");
		st.start();
		st2.start();
	}
}
