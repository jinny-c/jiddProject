package com.jidd.view.wechat;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.httpclient.JiddHttpExecutor;
import com.jidd.basic.utils.ConfigRef;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.JiddTraceLogUtil;
import com.jidd.view.base.dto.OAuthUser;
import com.jidd.view.util.JiddHttpExecutorHolder;
import com.jidd.view.util.JiddPayBizUtil;
import com.jidd.view.wechat.cache.JiddPubNoCacheManager;
import com.jidd.view.wechat.dto.JiddPubNoInfoDTO;
import com.jidd.view.wechat.dto.WechatTemplateMsg;

@Component
public class JiddPubNoClient {

	@Autowired
	private JiddPubNoCacheManager jiddPubNoCacheManager;

	private static Logger log = LoggerFactory.getLogger(JiddPubNoClient.class);

	/** HTTP 执行器 */
	private static JiddHttpExecutor executor = JiddHttpExecutorHolder.getExecutor();

	/**
	 * 清除本地公众号缓存
	 *
	 * @return
	 */
	public void clearCache() {
		jiddPubNoCacheManager.clearAllCache();
	}

	/**
	 * 刷新公众号TOKEN缓存
	 *
	 * @return
	 */
	public void refreshToken(String pubId) {
		jiddPubNoCacheManager.refreshToken(pubId);
	}


	/**
	 * 获取微信公众号配置
	 *
	 * @return
	 */
	public String getPubNoAppId(String pubId) {
		JiddPubNoInfoDTO pubNoConfig = jiddPubNoCacheManager.getPubNoConfig(pubId);
		if (pubNoConfig == null) {
			return "";
		}
		return pubNoConfig.getAppId();
	}

	/**
	 * 获取微信公众号模板ID
	 *
	 * @return
	 */
	public String getMsgTemplateId(String pubId, String msgType) {
		JiddPubNoInfoDTO pubNoConfig = jiddPubNoCacheManager.getPubNoConfig(pubId);
		if (pubNoConfig == null) {
			return "";
		}
		return pubNoConfig.getMsgTempId(msgType);
	}

	/**
	 * 获取微信公众号图片资源URL
	 *
	 * @return
	 */
	public String getPicUrl(String pubId, String picType) {
		JiddPubNoInfoDTO pubNoConfig = jiddPubNoCacheManager.getPubNoConfig(pubId);
		if (pubNoConfig == null) {
			return "";
		}
		return pubNoConfig.getPicUrl(picType);
	}

	/**
	 * 获取微信用户信息
	 * @param code
	 * @return
	 */
	public OAuthUser getWXAuthUser(String pubId, String code, String scope) {
		JiddPubNoInfoDTO pubNoConfig = jiddPubNoCacheManager.getPubNoConfig(pubId);
		if (pubNoConfig == null) {
			return null;
		}
		return JiddPayBizUtil.getWXAuthUser(pubNoConfig.getAppId(), pubNoConfig.getAppSecret(), code, scope);
	}

	/**
	 * 长链接转短链接
	 *
	 */
	public String long2short(String pubId, String url) {
		String requestUrl = JiddStringUtils.replace(ConfigRef.WX_SHORT_TOKEN, jiddPubNoCacheManager.getAccessToken(pubId));
		String result = null;
		try {
			JSONObject object = new JSONObject();
			object.put("action", "long2short");
			object.put("long_url", url);
			String requestData = object.toString();
			log.debug("TraceID:{}, start long2short, requestData={}, requestUrl={}", JiddTraceLogUtil.getTraceId(),
					requestData, requestUrl);
			result = executor.doPostWithUrl(requestUrl, requestData, null); //发送JSON格式数据
			log.info("TraceID:{}, finish long2short, result={}", JiddTraceLogUtil.getTraceId(), result);
		} catch (Exception e) {
			log.error("TraceID:{}, 长链接转短链接，HTTP请求异常", JiddTraceLogUtil.getTraceId(), e);
		}
		if (JiddStringUtils.isNotBlank(result)) {
			JSONObject jsonObject = JSON.parseObject(result);
			if (jsonObject == null) {
				return url;
			}
			if (isContainsErrorCode(jsonObject.toString())) {
				log.info("TraceID:{}, long2short failed, 强制刷新access_token", JiddTraceLogUtil.getTraceId());
				jiddPubNoCacheManager.refreshToken(pubId);
			}
			return jsonObject.getString("short_url");
		}
		return JiddSymbolEnum.Blank.symbol();
	}


	/**
	 * 创建菜单
	 *
	 */
	public boolean createMenu(String pubId, String menuJson) {
		String requestUrl = JiddStringUtils.replace(ConfigRef.WX_MENU_CREATE_URI, jiddPubNoCacheManager.getAccessToken(pubId));
		try {
			log.info("TraceID:{}, 创建菜单开始, menuJson={}, requestUrl={}", JiddTraceLogUtil.getTraceId(),
					menuJson, requestUrl);
			String result = executor.doPostWithUrl(requestUrl, menuJson, null); //发送JSON格式数据
			log.info("TraceID:{}, 创建菜单完成, result={}", JiddTraceLogUtil.getTraceId(), result);
			if (JiddStringUtils.isNotBlank(result)) {
				JSONObject jsonObject = JSON.parseObject(result);
				return "ok".equals(jsonObject.getString("errmsg"));
			}
		} catch (Exception e) {
			log.error("TraceID:{}, 创建菜单失败，HTTP请求异常", JiddTraceLogUtil.getTraceId(), e);
		}
		return false;
	}

	/**
	 * 推送客服消息
	 *
	 * @param openId
	 * @param text
	 */
	public void pushCustomMessage(String pubId, String openId, String text) {
		JSONObject textObj = new JSONObject();
		textObj.put("content", text);

		JSONObject msgObj = new JSONObject();
		msgObj.put("touser", openId);
		msgObj.put("msgtype", "text");
		msgObj.put("text", textObj);

		String msgContent = msgObj.toString();
		String requestUrl = JiddStringUtils.replace(ConfigRef.WX_MSG_KF_URI, jiddPubNoCacheManager.getAccessToken(pubId));
		try {
			log.info("TraceID:{}, 推送客服消息开始, msgContent={}, requestUrl={}", JiddTraceLogUtil.getTraceId(),
					msgContent, requestUrl);
			String result = executor.doPostWithUrl(requestUrl, msgContent, null); //发送JSON格式数据
			log.info("TraceID:{}, 推送客服消息完成, result={}", JiddTraceLogUtil.getTraceId(), result);
		} catch (Exception e) {
			log.error("TraceID:{}, 推送客服消息失败，HTTP请求异常", JiddTraceLogUtil.getTraceId(), e);
		}
	}

	/**
	 * 推送模板消息
	 * 
	 * @param toUser
	 * @param templateID
	 * @param url
	 * @param dataMap
	 * @param count 发送失败时重发次数
	 */
	public void pushTemplateMessage(String pubId, String toUser, String templateID,
	                                String url, Map<String, String> dataMap, Integer count) {
		WechatTemplateMsg templateMsg = new WechatTemplateMsg(toUser, templateID, url);
		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			templateMsg.putData(entry.getKey(), entry.getValue());
		}
		String msgContent = JSON.toJSONString(templateMsg);
		String requestUrl = JiddStringUtils.replace(ConfigRef.WX_MSG_TMP_URI, jiddPubNoCacheManager.getAccessToken(pubId));
		String result = null;
		try {
			log.info("TraceID:{}, 推送模板消息开始, msgContent: {}, requestUrl: {}", JiddTraceLogUtil.getTraceId(),
					msgContent, requestUrl);
			result = executor.doPostWithUrl(requestUrl, msgContent, null);
			log.info("TraceID:{}, 推送模板消息完成, result: {}", JiddTraceLogUtil.getTraceId(), result);
		} catch (Exception e) {
			log.error("TraceID:{}, 推送模板消息失败，HTTP请求异常", JiddTraceLogUtil.getTraceId(), e);
		}
		if (JiddStringUtils.isNotBlank(result)) {
			JSONObject jsonObject = JSON.parseObject(result);
			boolean errorFlag = isContainsErrorCode(jsonObject == null ? "" : jsonObject.toString());
			if (errorFlag && count > 0) {
				jiddPubNoCacheManager.refreshToken(pubId);
				pushTemplateMessage(pubId, toUser, templateID, url, dataMap, count-1);
			}
		}
	}

	private boolean isContainsErrorCode(String result) {
		boolean error40001 = result.indexOf("40001") > 0; //access_token无效，检查AppSecret的正确性
		boolean error42001 = result.indexOf("42001") > 0; //access_token超时
		boolean error40014 = result.indexOf("40014") > 0; //不合法的access_token，检查是否过期
		boolean error40003 = result.indexOf("40003") > 0; //不合法的OpenID
		boolean error45015 = result.indexOf("45015") > 0; //回复时间超时
		return error40014 || error42001 || error40001 || error40003 || error45015;
	}

}
