package com.jidd.view.common;


public enum JiddErrorCode {

    ERROR_CODE_MW001("MW001", "获取信息失败，当前会话失效！"),
    ERROR_CODE_MW999("MW999", "系统繁忙，请稍后再试！"), //系统异常

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

    JiddErrorCode(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
