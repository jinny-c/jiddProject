package com.jidd.view.base.params.valid;

import org.springframework.stereotype.Component;

import java.io.Serializable;

//@Component
public class JiddValidResp implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isSucc = true;

	private String errCode;

	private String errMsg;

	private String className;

	/**
	 * @param isSucc
	 */
	public JiddValidResp(boolean isSucc) {
		this(isSucc, null, null);
	}

	public JiddValidResp(boolean isSucc, String errCode, String errMsg) {
		this.isSucc = isSucc;
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.className = this.getClass().getName();
	}
	
	public JiddValidResp(String errCode, String errMsg) {
		this(false, errCode, errMsg);
	}

	public boolean isSucc() {
		return isSucc;
	}

	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MicroValidResp [isSucc=").append(isSucc)
				.append(", errCode=").append(errCode).append(", errMsg=")
				.append(errMsg).append(", className=").append(className)
				.append("]");
		return builder.toString();
	}
}
