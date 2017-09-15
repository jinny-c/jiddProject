package com.jidd.view.wechat.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jidd.basic.cache.JiddCacheManager;
import com.jidd.basic.httpclient.JiddHttpBuilder;
import com.jidd.basic.httpclient.JiddHttpExecutor;
import com.jidd.basic.utils.JiddDateUtils;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.JiddTraceLogUtil;
import com.jidd.view.wechat.dto.JiddPubNumTokenDTO;
import com.jidd.view.wechat.service.IJiddPubNumTokenCacheService;

@Service(value = "jiddPubNumTokenCacheService")
public class JiddPubNumTokenCacheServiceImpl implements IJiddPubNumTokenCacheService {
    /** SLF4J */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(JiddPubNumTokenCacheServiceImpl.class);

	private static final String PUB_NUM_CACHE_KEY = "PUB_NUMBER_";

	private static final Integer CACHE_INVALIDATE_TIME = 86400; //单位秒，24小时

	private static final String CHECK_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token={}";

	/** 分布式锁工厂接口 */
	//@Autowired
	//private DistributedLockFactory distributedLockFactory;

	private JiddHttpExecutor executor;
	
	@Autowired
	private JiddCacheManager jiddCacheManager;

	@PostConstruct
	private void initHttpExecutor() {
		try {
			executor = JiddHttpBuilder.create()
					.loadPool(1, 1)
					.loadTimeOut(3000, 3000)
					.loadIgnoreUrl()
					.build();
			log.info("create http executor success");
		} catch (Exception e) {
			log.error("create http executor fail", e);
		}
	}

	@Override
	public void refreshAccessToken(JiddPubNumTokenDTO req) {
		if (JiddStringUtils.isBlank(req.getAccessTokenUrl())) {
			log.warn("微信公众号[{}]access_token接口地址为空", req.getPubNo());
			return;
		}
		log.info("微信公众号access_token刷新请求参数：{}", req);
		//MutexLock lock = distributedLockFactory.buildMutexLock(req.getLockKey());
		//boolean bool = lock.tryLock(5, TimeUnit.SECONDS);
		boolean bool = true;
		try {
			if (!bool) {
				log.info("微信公众号[{}]access_token刷新获取分布式锁失败", req.getPubNo());
				return;
			}
			//强制刷新
			if (req.isMandatory()) {
				log.info("微信公众号[{}]access_token强制刷新", req.getPubNo());
				WechatAccessToken accessToken = requestAccessToken(req);
				store2RedisCache(req.getPubNo(), req.getAppId(), accessToken);
				return;
			}
			JiddPubNumTokenDTO pubNumToken = getValidPubNumTokenByCache(req.getPubNo(), req.getAppId());
			if (null != pubNumToken) {
				log.info("微信公众号[{}]access_token缓存未过期，无需刷新", req.getPubNo());
				return;
			}
			log.info("微信公众号[{}]access_token缓存开始", req.getPubNo());
			WechatAccessToken accessToken = requestAccessToken(req);
			store2RedisCache(req.getPubNo(), req.getAppId(), accessToken);
			log.info("微信公众号[{}]access_token缓存刷新成功", req.getPubNo());
		} catch (Throwable e) {
			log.error("微信公众号[{}]access_token刷新异常", req.getPubNo(), e);
		} finally {
			if (bool) {
				//lock.unlock();
			}
		}
	}

	private JiddPubNumTokenDTO getValidPubNumTokenByCache(String pubNo, String appId) {
		try {
			//JiddPubNumTokenDTO cache = microRedisExecutor.getH(PUB_NUM_CACHE_KEY + pubNo, appId);
			JiddPubNumTokenDTO cache = null;
			if (cache == null ) {
				return null;
			}
			if (!JiddDateUtils.afterCurrDate(JiddDateUtils.yyyyMMddHHmmss, cache.getInvalidTime())) {
				log.error("微信公众号[{}]access_token过期,当前时间=[{}],会话失效时间=[{}]", pubNo,
						JiddDateUtils.getCurrentDate(), cache.getInvalidTime());
				return null;
			}
			//检查access_token是否有效
			if (!checkAccessToken(cache.getAccessToken())) {
				log.error("微信公众号[{}]access_token无效", pubNo);
				return null;
			}
			return cache;
		} catch (Exception e) {
			log.error("缓存access_token查询失败", e);
		}
		return null;
	}

	private boolean checkAccessToken(String accessToken) {
		String requestUrl = JiddStringUtils.replace(CHECK_URL, accessToken);
		try {
			log.info("检查access_token有效性开始: {}", requestUrl);
			String result = executor.doGetWithUrl(requestUrl, null);
			log.info("检查access_token有效性结束, isContainsErrCode: {}", result.contains("errcode"));
			return JiddStringUtils.isNotBlank(result) && !result.contains("errcode");
		} catch (Exception e) {
			log.error("HTTP请求异常", e);
		}
		return false;
	}

	@Override
	public String getAccessToken(String pubNo, String appId) {
		try {
			//JiddPubNumTokenDTO cache = microRedisExecutor.getH(PUB_NUM_CACHE_KEY + pubNo, appId);
			String value = jiddCacheManager.getCacheConfig(pubNo);
			log.info("value={}",value);
			if (value !=null ) {
				return JiddStringUtils.join(pubNo,"_",value);
			}
			/*JiddPubNumTokenDTO cache = null;
			if (cache !=null ) {
				return cache.getAccessToken();
			}*/
		} catch (Exception e) {
			log.error("缓存access_token查询失败", e);
		}
		return "cache is null";
	}

	/**
	 * 存储缓存
	 *
	 * @param pubNo
	 * @param appId
	 * @param accessToken
	 */
	private void store2RedisCache(String pubNo, String appId, WechatAccessToken accessToken) {
		if (null == accessToken) {
			return;
		}
		String redisKey = PUB_NUM_CACHE_KEY + pubNo;
		JiddPubNumTokenDTO cache = new JiddPubNumTokenDTO();
		cache.setPubNo(pubNo);
		cache.setAppId(appId);
		cache.setAccessToken(accessToken.getAccess_token());
		cache.setCreateTime(JiddDateUtils.getCurrentDate());
		//计算无效时间，单位为：分
		String nextInvalidateTime = JiddDateUtils.getNextTime(JiddDateUtils.yyyyMMddHHmmss,
				cache.getCreateTime(), accessToken.getExpires_in()/60);
		cache.setInvalidTime(nextInvalidateTime);
		/*try {
			microRedisExecutor.setH(redisKey, appId, cache, CACHE_INVALIDATE_TIME);
		} catch (MicroCacheException e) {
			log.error("缓存access_token失败", e);
		}*/
	}


	/**
	 * 获取微信公众平台API接口调用的凭证
	 *
	 * @return
	 */
	private WechatAccessToken requestAccessToken(JiddPubNumTokenDTO req) {
		String requestUrl = JiddStringUtils.replace(req.getAccessTokenUrl(), req.getAppId(), req.getAppSecret());
		try {
			log.info("请求access_token: {}", requestUrl);
			String result = executor.doGetWithUrl(requestUrl, null);
			log.info("请求access_token结果: {}", result);
			if(JiddStringUtils.isBlank(result)){
				return null;
			}

			JSONObject jsonObject = JSON.parseObject(result);
			String access_token = jsonObject.getString("access_token");
			if(JiddStringUtils.isBlank(access_token)){
				log.error("获取access_token失败，errcode:{} errmsg:{}", jsonObject.getInteger("errcode"),
						jsonObject.getString("errmsg"));
				return null;
			}
			WechatAccessToken accessToken = new WechatAccessToken();
			accessToken.setAccess_token(access_token);
			accessToken.setExpires_in(jsonObject.getInteger("expires_in"));
			return accessToken;
		} catch (Exception e) {
			log.error("HTTP请求异常", e);
		}
		return null;
	}


	/**
	 * 获取JS API接口调用的凭证
	 *
	 * @return
	 */
	private String requestJsApiTicket(String ticketUrl, String accessToken) {
		String requestUrl = JiddStringUtils.replace(ticketUrl, accessToken);
		try {
			log.info("TraceID:{}, 请求ticket: {}", JiddTraceLogUtil.getTraceId(), requestUrl);
			String result = executor.doGetWithUrl(requestUrl, null);
			log.info("TraceID:{}, 请求ticket结果: {}", JiddTraceLogUtil.getTraceId(), result);
			if(JiddStringUtils.isBlank(result)){
				return "";
			}

			JSONObject jsonObject = JSON.parseObject(result);
			String ticket = jsonObject.getString("ticket");
			if(JiddStringUtils.isBlank(ticket)){
				log.error("TraceID:{}, 获取ticket失败，errcode:{} errmsg:{}",
						JiddTraceLogUtil.getTraceId(), jsonObject.getInteger("errcode"),
						jsonObject.getString("errmsg"));
				return "";
			}
			return ticket;
		} catch (Exception e) {
			log.error("TraceID:{}, HTTP请求异常", JiddTraceLogUtil.getTraceId(), e);
			return "";
		}
	}

	private class WechatAccessToken {
		private String access_token;
		private Integer expires_in;

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public Integer getExpires_in() {
			return expires_in;
		}

		public void setExpires_in(Integer expires_in) {
			this.expires_in = expires_in;
		}
	}
}
