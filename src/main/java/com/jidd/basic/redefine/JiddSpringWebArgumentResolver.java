package com.jidd.basic.redefine;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.basic.utils.JiddObjectUtils;
import com.jidd.view.base.JiddBaseReqDto;
import com.jidd.view.base.params.valid.JiddValidResp;
import com.jidd.view.base.params.valid.VerifyControllerUtil;
import com.jidd.view.exception.JiddGlobalValidException;
import com.jidd.view.secure.JiddSecureProcessor;

/**
 * web参数处理类
 * 
 * @history
 */
public class JiddSpringWebArgumentResolver implements
		HandlerMethodArgumentResolver {

	private static final String SERIALVERSIONUID = "serialVersionUID";

	private static Logger logger = LoggerFactory.getLogger(JiddSpringWebArgumentResolver.class);
	
	@Autowired
	private JiddSecureProcessor jiddSecureProcessor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.method.support.HandlerMethodArgumentResolver#
	 * supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 仅作用于添加了该【类注解】的入参
		return parameter.hasParameterAnnotation(JiddSecureAnno.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.method.support.HandlerMethodArgumentResolver#
	 * resolveArgument(org.springframework.core.MethodParameter,
	 * org.springframework.web.method.support.ModelAndViewContainer,
	 * org.springframework.web.context.request.NativeWebRequest,
	 * org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		if (!jiddSecureProcessor.isSecure(parameter)) {
			return WebArgumentResolver.UNRESOLVED;
		}
		// 参数赋值
		Object target = argToObj(parameter, request, binderFactory);
		// 参数校验
		paramsValid(target, parameter.getParameterType(), request);
		return target;
	}

	/**
	 * 参数校验
	 * 
	 * @param target
	 * @param clazz
	 * @param request
	 * @throws JiddGlobalValidException
	 */
	private void paramsValid(Object target, Class<? extends Object> clazz,
			NativeWebRequest request) throws JiddGlobalValidException {
		// 2017-03-07 microView工程DTO目前继承关系为二层
		if (clazz.getSuperclass() == JiddBaseReqDto.class
				|| clazz.getSuperclass().getSuperclass() == JiddBaseReqDto.class) {
			JiddBaseReqDto req = (JiddBaseReqDto) target;
			JiddValidResp valid = VerifyControllerUtil.validateReqDto(req);
			logger.info("校验参数返回：" + valid);
			if (valid != null && !valid.isSucc()) {
				throw new JiddGlobalValidException(valid.getErrCode(),
						valid.getErrMsg());
			}
		}
	}

	/**
	 * 参数转换
	 * 
	 */
	private Object argToObj(MethodParameter parameter,
			NativeWebRequest request, WebDataBinderFactory binderFactory)
			throws Exception {
		Class<? extends Object> clazz = parameter.getParameterType();
		Object target = BeanUtils.instantiateClass(clazz);

		String shortName = ClassUtils.getShortNameAsProperty(clazz);
		WebDataBinder binder = binderFactory.createBinder(request, null,
				shortName);
		Object arg = null;
		/*
		 * MicroHttpHeader header = jiddSecureProcessor.getHeader(); UserKeyBean
		 * userKey = jiddSecureProcessor.getUserKey( header.getVersion(),
		 * header.getTerminalUserID()); logger.debug(userKey);
		 */
		// 2016-09-20 处理父类中需要解密的属性字段
		for (; clazz != JiddBaseReqDto.class; clazz = clazz.getSuperclass()) {
			// 参数赋值
			for (Field filed : clazz.getDeclaredFields()) {
				// 字段属性为空跳过
				if (filed == null) {
					continue;
				}
				if (SERIALVERSIONUID.equals(filed.getName())) {
					continue;
				}
				filed.setAccessible(true);
				arg = binder.convertIfNecessary(
						request.getParameter(filed.getName()), filed.getType(),
						parameter);

				// 数据解密
				if (jiddSecureProcessor.isDecr(filed) && !JiddObjectUtils.isObjNull(arg)) {
					//logger.info(JiddStringUtils.replace("jiddSecureProcessor decrypt field=[{}]",filed.getName()));
					logger.info("jiddSecureProcessor decrypt field=[{}],value=[{}]",filed.getName(),String.valueOf(arg));
					arg = jiddSecureProcessor.decrypt("key", String.valueOf(arg), null, null);
				}
				filed.set(target, arg);
			}
		}
		return target;
	}
}
