package com.jidd.basic.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class WebBrowserFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(WebBrowserFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		//MicroTraceLogUtil.beginTrace(); //日志跟踪开始
		//long begin = System.currentTimeMillis();
		//logger.info("----do filter start--{}, begin time:{}", request.getRequestURI(), begin);
		logger.info("----address---{}", "http://127.0.0.1:8081/jiddProjects/");
		//initHeader(request);
		//RequestResponseContext.setRequest(request);
		//RequestResponseContext.setResponse(response);
		chain.doFilter(request, response);
		//logger.info("----do filter end--{}, execute time:{}", request.getRequestURI(), System.currentTimeMillis() - begin);
		//MicroTraceLogUtil.endTrace(); //日志跟踪结束
	}

}
