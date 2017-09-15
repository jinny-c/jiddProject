package com.jidd.view.controller.dto;

import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.security.JiddDecrAnno;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.ToStringUtils;
import com.jidd.view.base.JiddBaseReqDto;
import com.jidd.view.base.params.valid.JiddValidResp;
import com.jidd.view.common.JiddErrorCodes;

/**
 * 用户登录
 * 
 */
public class UserLoginDto extends JiddBaseReqDto {

	private String channel;
	@JiddDecrAnno
	private String userName;// 用户名
	private String password;// 密码

	private String action;
	@JiddDecrAnno
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
		ToStringUtils builder = new ToStringUtils(this);
		builder.add("channel", channel).add("userName", userName)
				.add("password", password).add("action", action)
				.add("mobile", mobile).add("verifyCode", verifyCode)
				.add("imageCode", imageCode);
		return builder.toString();
	}

	@Override
	public JiddValidResp valid() {
		String errMsg = JiddSymbolEnum.Blank.symbol();
		if (JiddStringUtils.isBlank(channel)) {
			errMsg = "[渠道号]为空";
			return new JiddValidResp(JiddErrorCodes.E_JIDD_NULL, errMsg);
		}

		if (JiddStringUtils.isBlank(userName)) {
			errMsg = "[用户名]为空";
			return new JiddValidResp(JiddErrorCodes.E_JIDD_NULL, errMsg);
		}
		return new JiddValidResp(true);
	}
}
