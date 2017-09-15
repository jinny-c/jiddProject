package com.jidd.basic.common;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SecretKeyBean implements Serializable {
	private static final long serialVersionUID = 1L;
	// 唯一标识
	private String uniqueId;
	private String session;
	// 种子
	private String seed;
	// 生成时间
	private String seedTime;
	// 过期时间
	private String invalidTime;
	// 秘钥
	private String key;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getSeed() {
		return this.seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getSeedTime() {
		return this.seedTime;
	}

	public void setSeedTime(String seedTime) {
		this.seedTime = seedTime;
	}

	public String getInvalidTime() {
		return this.invalidTime;
	}

	public void setInvalidTime(String invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSession() {
		return this.session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String toString() {
		return new ToStringBuilder(this).append("uniqueId", this.uniqueId)
				.append("seed", this.seed).append("seedTime", this.seedTime)
				.append("invalidTime", this.invalidTime)
				.append("key", this.key).toString();
	}
}
