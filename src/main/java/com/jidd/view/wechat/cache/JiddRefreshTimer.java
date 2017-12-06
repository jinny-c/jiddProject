package com.jidd.view.wechat.cache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jidd.view.wechat.service.IJiddPubNumTokenCacheService;

/**
 * 管理无卡支付微信公众平台的API调用凭证 【区别网页授权凭证】
 * 1，access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效
 * 2，每天调用获取access_token接口的上限是2000次。
 *
 * @history
 */
public class JiddRefreshTimer {
	/** 日志 */
	private static Logger log = LoggerFactory.getLogger(JiddRefreshTimer.class);

	private static final int PERIOD = 4; //刷新周期，单位：分钟

	private IJiddPubNumTokenCacheService jiddPubNumTokenCacheService;

	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	public void start() {
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				log.info("refreshToken任务执行开始");
				try {
					refreshToken();
				} catch (Exception e) {
					log.error("refreshToken任务执行失败", e);
				}
				log.info("refreshToken任务执行成功");
			}
		}, 0, PERIOD, TimeUnit.MINUTES); //延迟0分钟，每隔PERIOD分钟检查TOKEN
	}

	public void finish() {
		scheduledExecutorService.shutdown();
	}

	private void refreshToken() {
		log.info("do refreshToken start");
		
		log.info("accToken={}",jiddPubNumTokenCacheService.getAccessToken("pubNo", "appId"));
	}

	public void setJiddPubNumTokenCacheService(
			IJiddPubNumTokenCacheService jiddPubNumTokenCacheService) {
		this.jiddPubNumTokenCacheService = jiddPubNumTokenCacheService;
	}

}