package com.jidd.test.copy.bean;

import java.io.Serializable;

import com.jidd.basic.utils.ToStringUtils;

public class TestBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;// 用户名
	private String password;// 密码
	private String channel; //渠道
	
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		ToStringUtils builder = new ToStringUtils(this);
		builder.add("userName", userName).add("password", password)
				.add("channel", channel);
		return builder.toString();
	}
}
