package com.jidd.view.base.params.valid;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.view.base.JiddBaseReqDto;
import com.jidd.view.common.JiddErrorCodes;
import com.jidd.view.exception.JiddControllerException;

/**
 * 验证Controller层DTO工具类
 *
 * @history
 */
public class VerifyControllerUtil {
    private static Logger logger = Logger.getLogger(VerifyControllerUtil.class);
    private final static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证是否为空字符串
     *
     * @param parameter 请求参数
     * @param message  错误描述
     */
    public static void assertNotBlank(String parameter, String message) throws JiddControllerException{
        if (JiddStringUtils.isBlank(parameter)) {
            throw new JiddControllerException(JiddErrorCodes.E_JIDD_NULL, message);
        }
    }


    /**
     * 校验客户端请求DTO
     *
     * @history
     */
    public static JiddValidResp validateReqDto(JiddBaseReqDto reqDto) {
        //校验Validator注解约束
    	JiddValidResp validResp = validateObject(reqDto);
        if(validResp.isSucc()){
            return reqDto.valid();
        }
        return validResp;
    }

    /**
     * 请求参数非空、格式验证，请求对象
     *
     * @history
     */
    public static JiddValidResp validateObject(Object object){
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (violations.size() == 0) return new JiddValidResp(true);

        //校验失败
        ConstraintViolation<Object> next = violations.iterator().next();
        Path propertyPath = next.getPropertyPath();
        Iterator<Path.Node> nodeIter = propertyPath.iterator();
        logger.warn("字段[" + nodeIter.next().getName() + "]校验失败");
        if(nodeIter.hasNext()){
            return new JiddValidResp(nodeIter.next().getName(), next.getMessage());
        }
        return new JiddValidResp(JiddErrorCodes.E_JIDD_NULL, next.getMessage());
    }

    /**
     * 多个对象 请求参数非空、格式验证，请求对象
     *
     * @param objects
     * @return
     */
    public static JiddValidResp validateObjects(Object... objects){
    	JiddValidResp resp = new JiddValidResp(true);
        for (Object obj: objects){
            resp = validateObject(obj);
            if(!resp.isSucc()){
                break;
            }
        }
        return resp;
    }

    /**
     * 请求参数校验 指定属性
     *
     * @param object
     * @param properties
     * @return
     */
    public static void validateProperty(Object object, String... properties){
        Validator validator = factory.getValidator();
        for (String property: properties){
            Set<ConstraintViolation<Object>> violations = validator.validateProperty(object, property);
            if (violations.size() == 0)  continue;
            throw new RuntimeException(violations.iterator().next().getMessage());
        }
    }
}