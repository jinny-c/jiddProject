package com.jidd.project.base.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class JiddProjectCacheManager {
	/** 日志 */
	private static Logger log = LoggerFactory.getLogger(JiddProjectCacheManager.class);

	private LoadingCache<String, String> jiddConfigCache;
	
	public static Map<String, String> cacheMap = new HashMap<String, String>();

	@PostConstruct
	public void init() {
		jiddConfigCache = CacheBuilder.newBuilder()
				.refreshAfterWrite(4, TimeUnit.HOURS) // 默认4个小时过期
				.build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) throws Exception {
						log.debug("LoadingCache start:key={}", key);
						return cacheMap.get(key);
					}
				});
	}
	
	/**
	 * 从缓存中读取信息
	 * @param key
	 * @return
	 */
	public String getCacheConfig(String key) {
		try {
			return jiddConfigCache.getUnchecked(key);//未声明CacheLoader异常
			//Optional<T> cacheRes = jiddConfigCache.asMap().get(key);
			//return jiddConfigCache.get(key);//已声明CacheLoader.load异常
		} catch (Exception e) {
			log.error("从内存中获取信息失败", e);
		}
		return null;
	}
	
	public void setCacheConfig(String key, String value) {
		try {
			jiddConfigCache.put(key, value);
		} catch (Exception e) {
			log.error("set cache exception", e);
		}
	}

	public void refreshCache(String key) {
		try {
			String cacheRes = jiddConfigCache.asMap().get(key);
			String value = "test,catch is not null";
			if (cacheRes == null) {
				value = "test,cache is null";
			}
			jiddConfigCache.asMap().put(key, value);
		} catch (Exception e) {
			log.error("refreshCache",e);
		}
	}

	public void refreshCache() {
		log.info("refreshCache start");
		try {
			if (null == cacheMap || cacheMap.isEmpty()) {
				log.error("refreshCache cacheMap is null");
				return;
			}
			log.error("cacheMap={}",cacheMap);
			for (Map.Entry<String, String> entry : cacheMap.entrySet()) {
				jiddConfigCache.put(entry.getKey(), entry.getValue());
			}
			
		} catch (Exception e) {
			log.error("refreshCache Exception", e);
		}
	}
	public void clearAllCache() {
		jiddConfigCache.invalidateAll();
	}

}