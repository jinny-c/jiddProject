package com.jidd.basic.httpclient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 请求响应上下文
 * 
 * @history
 */
public class RequestResponseContext {

	private static ThreadLocal<HttpServletRequest> request_threadLocal = new ThreadLocal<HttpServletRequest>();

	private static ThreadLocal<HttpServletResponse> reponse_threadLocal = new ThreadLocal<HttpServletResponse>();

	public static void setRequest(HttpServletRequest request) {
		request_threadLocal.set(request);
	}

	public static HttpServletRequest getRequest() {
		return request_threadLocal.get();
	}

	public static void removeRequest() {
		request_threadLocal.remove();
	}

	public static void setResponse(HttpServletResponse response) {
		reponse_threadLocal.set(response);
	}

	public static HttpServletResponse getResponse() {
		return reponse_threadLocal.get();
	}

	public static void removeResponse() {
		reponse_threadLocal.remove();
	}
	
	
	public static String getTerminalUserId(HttpServletRequest request,
			HttpServletResponse response, HttpHeader header) {
		String tuid = header != null ? header.getTerminalUserID() : null;
		if (null == tuid) {
			setTerminalUserID(request, response, header, tuid);
			return tuid;
		}
		tuid = (String) request
				.getAttribute("terminalUserID");
		if (null == tuid) {
			setTerminalUserID(request, response, header, tuid);
            return tuid;
		}
		return null;
	}
	
	public static void setTerminalUserID(HttpServletRequest request,
			HttpServletResponse response, HttpHeader header,String terminalUserID){
		request.setAttribute("terminalUserID", terminalUserID);
        header.setTerminalUserID(terminalUserID);
		response.setHeader("terminalUserID", terminalUserID);
	}
}
