package com.jidd.view.controller.dto;

import java.io.Serializable;

import com.jidd.basic.security.JiddEncrAnno;
import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.basic.utils.ToStringUtils;

/**
 * 用户登录返回
 * 
 */
@JiddSecureAnno
public class UserLoginRespDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String channel;

	@JiddEncrAnno
	private String mobile;
	// 验证码
	@JiddEncrAnno
	private String verifyCode;
	@JiddEncrAnno
	// 登录图形验证码
	private String imageCode;
	
	private String desc ;

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		ToStringUtils builder = new ToStringUtils(this);
		builder.add("channel", channel).add("mobile", mobile)
				.add("verifyCode", verifyCode).add("imageCode", imageCode)
				.add("desc", desc);
		return builder.toString();
	}

}
