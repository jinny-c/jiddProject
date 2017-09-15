package com.jidd.project.wap.controller.user.dto;

import java.io.Serializable;

import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.security.JiddEncrAnno;
import com.jidd.basic.security.JiddNotBlank;
import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.ToStringUtils;
import com.jidd.project.common.JiddProjectConstants;
import com.jidd.view.base.JiddBaseReqDto;
import com.jidd.view.base.params.valid.JiddValidResp;
import com.jidd.view.common.JiddErrorCodes;

@JiddSecureAnno
public class UserWapQueryCodeReq  extends JiddBaseReqDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JiddNotBlank(code = JiddProjectConstants.E_NULL_ERROR, message = "渠道不能为空！")
	private String channel;

	@JiddEncrAnno
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
		builder.add("channel", channel).add("mobile", mobile);
		return builder.toString();
	}

	@Override
	public JiddValidResp valid() {
		String errMsg = JiddSymbolEnum.Blank.symbol();
		if (JiddStringUtils.isBlank(mobile)) {
			errMsg = "[手机号]为空";
			return new JiddValidResp(JiddErrorCodes.E_JIDD_NULL, errMsg);
		}
		return new JiddValidResp(true);
	}

}
