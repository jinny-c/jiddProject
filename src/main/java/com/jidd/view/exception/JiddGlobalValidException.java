
package com.jidd.view.exception;

public class JiddGlobalValidException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private String errorMsg;

	public JiddGlobalValidException() {
	}

	public JiddGlobalValidException(String errorCode, String errorMsg) {
		super(errorCode + "#" + errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public JiddGlobalValidException(String errorCode, Throwable caused) {
		super(errorCode, caused);
		this.errorCode = errorCode;
	}
	
	public JiddGlobalValidException(String errorCode, String errorMsg, Throwable caused) {
		super(errorCode + "#" + errorMsg, caused);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
