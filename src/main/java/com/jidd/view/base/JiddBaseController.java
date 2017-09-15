package com.jidd.view.base;

import static com.jidd.view.common.JiddErrorCode.ERROR_CODE_MW001;
import static com.jidd.view.common.JiddWapConstants.CURRENT_USER;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.httpclient.HttpHeader;
import com.jidd.basic.httpclient.RequestResponseContext;
import com.jidd.basic.serialize.JiddSerialTypeEnum;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.JiddTraceLogUtil;
import com.jidd.view.base.modelview.JiddModelAndView;
import com.jidd.view.base.modelview.RespErr;
import com.jidd.view.base.modelview.RespSucc;
import com.jidd.view.common.JiddErrorCode;
import com.jidd.view.exception.JiddControllerException;

/**
 * 基础Controller
 * 
 * @history
 */
public class JiddBaseController {

	public static String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";

	/** 日志 */
	private static Logger log = LoggerFactory
			.getLogger(JiddBaseController.class);


    /**
     * 获取HTTP关信息
     *
     * @history
     * @throws ControllerException 没有进行初始化过滤（MicroInitFilter）调用此方法将抛异常
     */
	protected HttpHeader getHttpHeader() throws JiddControllerException{
		Object header = RequestResponseContext.getRequest().getAttribute(HttpHeader.class.getName());
		return (HttpHeader) header;
	}
	
	protected String toHtml(String viewName) {
		return JiddStringUtils.join("wap/", viewName);
	}
	
	protected String toUserHtml(String viewName) {
		return JiddStringUtils.join("user/", viewName);
	}
	
	public String convertJson(Object object) {
		return JSON.toJSONString(object);
	}

	protected String toRedirect(String url) {
		return JiddStringUtils.join("redirect:", url);
	}
	
	protected JiddModelAndView<RespSucc> toSucc() {
		return JiddModelAndView.succ(getSerialType());
	}
	protected JiddModelAndView<RespErr> toErr(String code, String msg) {
		return JiddModelAndView.err(getSerialType(), code, msg);
	}

	protected <T> JiddModelAndView<T> toData(T data) {
		return new JiddModelAndView<T>(getSerialType(), data);
	}
	
	protected JiddModelAndView<RespSucc> toRequestForward(String url)
			throws JiddControllerException {
		try {
			log.info("toRequestForward start");
			RequestResponseContext
					.getRequest()
					.getRequestDispatcher(url)
					.forward(RequestResponseContext.getRequest(),
							RequestResponseContext.getResponse());
		} catch (ServletException e) {
			e.printStackTrace();
			throw new JiddControllerException(JiddErrorCode.ERROR_CODE_MW999.getErrorCode(),"umg请求异常");
		} catch (IOException e) {
			e.printStackTrace();
			throw new JiddControllerException(JiddErrorCode.ERROR_CODE_MW999.getErrorCode(),"umg请求异常");
		}
		log.info("toRequestForward success");
		return toSucc();
	}
	
	protected HttpServletRequest getRequest() {
		return RequestResponseContext.getRequest();
	}

	protected HttpSession getSession() {
		return RequestResponseContext.getRequest().getSession();
	}

	private JiddSerialTypeEnum getSerialType() {
		HttpHeader httpHeader = null;
		try {
			httpHeader = getHttpHeader();
		} catch (JiddControllerException e) {
			log.error("error:",e);
		}
		if (httpHeader == null ) {
			return JiddSerialTypeEnum.SERILAL_TYPE_JSON;
		}
		//return httpHeader.getDataType();
		return JiddSerialTypeEnum.SERILAL_TYPE_JSON;
	}
	/**
	 * 
	 * 获取请求的IP地址
	 * 
	 * @return
	 */
	protected String getRemoteIpAddr() {
		HttpServletRequest request = RequestResponseContext.getRequest();
		String ip = request.getHeader("X-Forwarded-For");
		log.info("TraceID:{}, X-Forwarded-For: {}",
				JiddTraceLogUtil.getTraceId(), ip);
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			log.info("TraceID:{}, X-Real-IP: {}",
					JiddTraceLogUtil.getTraceId(), ip);
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Cdn-Src-Ip");
			log.info("TraceID:{}, Cdn-Src-Ip: {}",
					JiddTraceLogUtil.getTraceId(), ip);
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			log.info("TraceID:{}, Proxy-Client-IP: {}",
					JiddTraceLogUtil.getTraceId(), ip);
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			log.info("TraceID:{}, WL-Proxy-Client-IP: {}",
					JiddTraceLogUtil.getTraceId(), ip);
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("TraceID:{}, RemoteAddr: {}",
					JiddTraceLogUtil.getTraceId(), ip);
		}
		// 2017-03-21 多级反向代理，取第一个IP
		String[] msgArr = JiddStringUtils
				.splitToArray(ip, JiddSymbolEnum.Comma);
		if (msgArr != null && msgArr.length > 0) {
			return JiddStringUtils.trimStr(msgArr[0]);
		}
		return null;
	}

	/**
	 * 输出响应信息
	 * 
	 * @param responseMsg
	 * @param args
	 */
	protected void writeToResponse(String responseMsg, String... args) {
		HttpServletResponse response = RequestResponseContext.getResponse();
		if (args != null && args.length > 0) {
			response.setContentType(args[0]);
		} else {
			response.setContentType(CONTENT_TYPE_TEXT);
		}
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(responseMsg);
			out.flush();
		} catch (IOException e) {
			log.error("TraceID:{}, HTTP响应异常", JiddTraceLogUtil.getTraceId(), e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	protected Object getUserBySession() throws JiddControllerException{
		Object userVO = getSession().getAttribute(CURRENT_USER);
		if(userVO == null){
			log.error("TraceID:{}, 无卡用户会话不存在，请检查拦截器配置或重新进入", JiddTraceLogUtil.getTraceId());
			throw new JiddControllerException(ERROR_CODE_MW001.getErrorCode(), ERROR_CODE_MW001.getErrorMsg());
		}
		return userVO;
	}
}
