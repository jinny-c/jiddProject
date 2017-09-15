package com.jidd.test.copy.bean;

import com.jidd.basic.utils.ToStringUtils;

public class TestBean2 extends TestBaseBean2 {

	private String userRealName;
	private Integer userAge;

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
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
