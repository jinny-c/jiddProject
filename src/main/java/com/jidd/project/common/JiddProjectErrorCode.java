package com.jidd.project.common;


public enum JiddProjectErrorCode {

    ERROR_CODE_0001("0001", "获取信息异常，会话失效！"),
    ERROR_CODE_0002("0002", "参数为空！"),
    ERROR_CODE_9999("9999", "系统繁忙，请稍后再试！"),
    ;

    private String errorCode;
    private String errorMsg;

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

    JiddProjectErrorCode(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
