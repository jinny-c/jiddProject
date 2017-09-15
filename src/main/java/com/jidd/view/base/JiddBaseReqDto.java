package com.jidd.view.base;

import com.jidd.view.base.params.valid.JiddValidResp;
import com.jidd.view.base.params.valid.VerifyControllerUtil;
import com.jidd.view.exception.JiddControllerException;

/**
 * 请求参数验证类
 * @history
 */
public abstract class JiddBaseReqDto {

	/**
	 * 参数验证
	 * 
	 * @return
	 */
	public abstract JiddValidResp valid();

    /**
     * 验证参数的正确性 【该方法由未使用@JiddSecureAnno注解的Controller手动调用】
     *
     * @throws com.micro.app.commcon.ControllerException
     */
    public void paramsValid() throws JiddControllerException {
        JiddValidResp valid = VerifyControllerUtil.validateReqDto(this);
        if (valid != null && !valid.isSucc()) {
            throw new JiddControllerException(valid.getErrCode(), valid.getErrMsg());
        }
    }
}
