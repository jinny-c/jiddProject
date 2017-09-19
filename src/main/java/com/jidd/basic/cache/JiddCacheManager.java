package com.jidd.basic.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class JiddCacheManager {
	/** 日志 */
	private static Logger log = LoggerFactory.getLogger(JiddCacheManager.class);

	private LoadingCache<String, String> jiddConfigCache;

	@PostConstruct
	public void init() {
		jiddConfigCache = CacheBuilder.newBuilder()
				.refreshAfterWrite(1, TimeUnit.HOURS)  //默认24个小时过期
				.build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) throws Exception {
						//JiddPubNoInfoDTO microPubNoInfoDTO = microPubNoInfoService.queryPubNoInfoById(pubId);
						log.info("init add start,key={}", key);
						return "cache init value";
					}
				});
	}
	
	/**
	 * 从缓存中读取信息
	 *
	 * @return
	 */
	
	public String getCacheConfig(String key) {
		try {
			//Optional<T> cacheRes = jiddConfigCache.asMap().get(key);
			//return jiddConfigCache.getUnchecked(key);//未声明CacheLoader异常
			return jiddConfigCache.get(key);//已声明CacheLoader异常
		} catch (Exception e) {
			log.error("从内存中获取信息失败", e);
		}
		return null;
	}

	public void refreshCache(String key) {
		try {
			String cacheRes = jiddConfigCache.asMap().get(key);
			String value = "catch is not null";
			if (cacheRes == null) {
				value = "test,cache is null";
			}
			jiddConfigCache.asMap().put(key, value);
		} catch (Exception e) {
			log.error("refreshCache",e);
		}
	}

	public void clearAllCache() {
		jiddConfigCache.invalidateAll();
	}

}