package com.jidd.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class MapTest {
	private static final Logger logger = Logger.getLogger(MapTest.class.getName());

    public final static int THREAD_POOL_SIZE = 5;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MapTest().safetyMap();
	}
	
	public void safetyMap(){
		//Hashtable
		Map<String, String> hashtable = new Hashtable<String, String>();
		doMapTest(hashtable);
		
		//synchronizedMap
		Map<String, String> synchronizedHashMap = Collections.synchronizedMap(new HashMap<String, String>());
		doMapTest(synchronizedHashMap);
		
		//ConcurrentHashMap
		Map<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();
		doMapTest(concurrentHashMap);
		
	}
	
	public void doMapTest(final Map<String, String> map){
		logger.info("-------------start," + map.getClass());
		
		//毫秒
		//long a_s = System.currentTimeMillis();
		//纳秒
		long a_n = System.nanoTime();
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		int count = 0;
		do{
			for (int c = 0; c < THREAD_POOL_SIZE; c++) {
				executor.execute(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						for (int k = 0; k < 50000; k++) {
							Integer randomNumber = (int) Math.ceil(Math.random() * 550000);
							String val = map.get(String.valueOf(randomNumber));
							map.put(String.valueOf(k), String.valueOf(randomNumber));
						}
					}
				});
			}
			count++;
			System.out.println("========="+count);
		} while (count < 5);
		
		executor.shutdown();
		
		long b_n = System.nanoTime();
		logger.info("-------------start time:" + a_n + ",time consuming:" + (b_n - a_n)/1000 + " ms");
	}
	
	public void doTable(Map<String, String> map){
		
	}
	public void doSynchronized(Map<String, String> map){
		
	}
	public void doConcurrent(Map<String, String> map){
		
	}
}
