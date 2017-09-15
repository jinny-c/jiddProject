package com.jidd.basic.redefine;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.JiddTraceLogUtil;
import com.jidd.basic.utils.JiddValidateUtils;
import com.jidd.view.common.JiddErrorCode;
import com.jidd.view.exception.JiddGlobalValidException;

public class JiddSpringExceptionResolver extends SimpleMappingExceptionResolver {

	private Logger log = LoggerFactory
			.getLogger(JiddSpringExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		// Expose ModelAndView for chosen error view.
		String viewName = determineViewName(ex, request);
		if (JiddStringUtils.isBlank(viewName)) {
			return null;
		}

		// Apply HTTP status code for error views, if specified.
		// Only apply it if we're processing a top-level request.
		Integer statusCode = determineStatusCode(request, viewName);
		if (statusCode != null) {
			applyStatusCodeIfPossible(request, response, statusCode);
		}

		String errorMsg = JiddErrorCode.ERROR_CODE_MW999.getErrorMsg();
		if (ex instanceof JiddGlobalValidException) {
			JiddGlobalValidException microEX = (JiddGlobalValidException) ex;
			errorMsg = filterErrMsg(microEX.getErrorMsg());
			log.error("TraceID:{}, 捕获MicroGlobalValidException: {}",
					JiddTraceLogUtil.getTraceId(), ex.toString());
		} else {
			log.error("TraceID:{}, 捕获Exception", JiddTraceLogUtil.getTraceId(),
					ex);
		}
		log.error("TraceID:{}, 返回客户端的错误提示：{}", JiddTraceLogUtil.getTraceId(),
				errorMsg);

		ModelAndView modelAndView = getModelAndView(viewName, ex, request);
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "0");
		result.put("message", errorMsg);
		modelAndView.addObject("result", result);
		modelAndView.addObject("message", errorMsg);
		return modelAndView;
	}

	/**
	 * 过滤错误消息 第一种情况：[手机号]为空 -> [手机号]为空 第二种情况：[M001]手机号为空 -> 手机号为空
	 * 第三种情况：[M001][手机号]为空 -> [手机号]为空 第四种情况：手机号为空 -> 手机号为空
	 * 
	 * @param errMsg
	 * @return
	 */
	private static String filterErrMsg(String errMsg) {
		if (JiddStringUtils.isNotBlank(errMsg)) {
			int startIndex = errMsg.indexOf("[");
			if (startIndex != 0) {
				return errMsg;
			}
			int endIndex = errMsg.indexOf("]");
			String wrapMsg = errMsg.substring(startIndex + 1, endIndex);
			if (JiddValidateUtils.isLettOrNum(wrapMsg)) {
				return errMsg.substring(endIndex + 1);
			}
			return errMsg;
		}
		return errMsg;
	}

}
