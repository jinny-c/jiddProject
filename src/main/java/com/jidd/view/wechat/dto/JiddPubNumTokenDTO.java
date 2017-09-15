package com.jidd.view.wechat.dto;

import java.io.Serializable;

import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.utils.JiddStringUtils;

public class JiddPubNumTokenDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String LOCK_MICRO_PUB_NUM_TOKEN = "/hpay_micro_pub_num_token_lock";

	/** 微信公众号 【原始ID（微信号）：gh_xxxx】 */
	private String pubNo;

	/** 开发者ID */
	private String appId;

	/** 开发者Secret */
	private String appSecret;

	private String accessTokenUrl;

	private String jsApiTicketUrl;

	private String accessToken;

	private String jsApiTicket;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 会话失效时间
	 */
	private String invalidTime;

	/**
	 * 是否强制刷新
	 */
	private boolean mandatory;

	public String getLockKey() {
		return JiddStringUtils.join(JiddSymbolEnum.Underline, LOCK_MICRO_PUB_NUM_TOKEN, pubNo);
	}

	public String getPubNo() {
		return pubNo;
	}

	public void setPubNo(String pubNo) {
		this.pubNo = pubNo;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getJsApiTicketUrl() {
		return jsApiTicketUrl;
	}

	public void setJsApiTicketUrl(String jsApiTicketUrl) {
		this.jsApiTicketUrl = jsApiTicketUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getJsApiTicket() {
		return jsApiTicket;
	}

	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(String invalidTime) {
		this.invalidTime = invalidTime;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

}
