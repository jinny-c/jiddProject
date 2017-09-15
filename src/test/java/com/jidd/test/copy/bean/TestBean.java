package com.jidd.test.copy.bean;

import com.jidd.basic.utils.ToStringUtils;

public class TestBean extends TestBaseBean {
	private static final long serialVersionUID = 1L;

	private String userRealName;
	private String userAge;

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	@Override
	public String toString() {
		ToStringUtils builder = new ToStringUtils(this);
		builder.add("userRealName", userRealName).add("userAge", userAge);
		builder.addValue(super.toString());
		return builder.toString();
	}

}
