package com.jidd.view.exception;

public class JiddControllerException extends JiddGlobalValidException {
	
	private static final long serialVersionUID = -7081755423334874084L;

	public JiddControllerException() {
	}

	public JiddControllerException(String errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}
	
	public JiddControllerException(String errorCode, String errorMsg, Throwable caused) {
		super(errorCode, errorMsg, caused);
	}
}
