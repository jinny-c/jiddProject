package com.jidd.basic.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jidd.basic.common.JiddResponseUtils;
import com.jidd.basic.httpclient.HttpHeader;
import com.jidd.basic.httpclient.RequestResponseContext;
import com.jidd.basic.traffic.JiddTrafficCounter;
import com.jidd.basic.utils.JiddTraceLogUtil;
import com.jidd.view.common.JiddErrorCode;

/**
 * 初始化过滤器
 * 
 * 
 */
public class InitFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(InitFilter.class);

	private JiddTrafficCounter jiddTrafficCounter;
	
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		String maxToken = super.getFilterConfig().getInitParameter("traffic.maxToken");
		jiddTrafficCounter = new JiddTrafficCounter(maxToken);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		JiddTraceLogUtil.beginTrace(); // 日志跟踪开始
		long begin = System.currentTimeMillis();//毫秒 /1000s
		logger.info("----do filter start--{},currentCount=[{}],maxTokenCount=[{}]",
				request.getRequestURI(), jiddTrafficCounter.get() + 1, jiddTrafficCounter.getMaxToken());
		try {
			if (!jiddTrafficCounter.acquire()) {
				logger.info("流量超限[{}]，当前会话请求[{}]被阻断",
						JiddErrorCode.ERROR_CODE_MW999.getErrorCode(),
						request.getRequestURI());
				String respMsg = JiddResponseUtils.toErr(request,
						JiddErrorCode.ERROR_CODE_MW999.getErrorCode(),
						JiddErrorCode.ERROR_CODE_MW999.getErrorMsg());
				JiddResponseUtils.writeToResponse(response, respMsg);
				return;
			}
			initHeader(request);
			RequestResponseContext.setRequest(request);
			RequestResponseContext.setResponse(response);
			chain.doFilter(request, response);
		} finally {
			jiddTrafficCounter.release();
		}
		
		logger.info("----do filter end--{},execute time:{}(ms)",
				request.getRequestURI(), System.currentTimeMillis() - begin);
		JiddTraceLogUtil.endTrace(); // 日志跟踪结束
	}

	/**
	 * 初始化头信息
	 * 
	 * @param request
	 */
	private void initHeader(HttpServletRequest request) {
		HttpHeader header = new HttpHeader();
		header.setContentType(request.getHeader("contentType"));
		header.setHpCache(request.getHeader("hpCache"));

		logger.info(header.toString());
		request.setAttribute(HttpHeader.class.getName(), header); // org.apache.catalina.connector.RequestFacade
	}
}
