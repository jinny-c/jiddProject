package com.jidd.view.controller.dto;

import java.io.Serializable;

import com.jidd.basic.utils.JiddStringUtils;

/**
 * 用户登录
 * 
 */
public class UserBaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String channel;
	private String userName;// 用户名
	private String password;// 密码

	private String action;
	private String mobile;
	// 验证码
	private String verifyCode;
	// 登录图形验证码
	private String imageCode;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	
	@Override
	public String toString() {
		return JiddStringUtils.join("userName{},action{}", userName, action);
	}
}
