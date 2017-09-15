package com.jidd.view.interceptor;

import static com.jidd.view.common.JiddWapConstants.CURRENT_USER;
import static com.jidd.view.common.JiddWapConstants.PUB_NO_ID;
import static com.jidd.view.common.JiddWapConstants.SCAN_SHARE_CODE_URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.view.base.dto.OAuthUser;
import com.jidd.view.common.JiddErrorCode;
import com.jidd.view.common.JiddWapConstants;
import com.jidd.view.exception.JiddGlobalValidException;
import com.jidd.view.wechat.JiddPubNoClient;

public class JiddCompatibleInterceptor extends HandlerInterceptorAdapter {
	/** SLF4J */
	private static final Logger log = LoggerFactory.getLogger(JiddCompatibleInterceptor.class);
	
	//private static final String HYSFT_PUB_ID = "gh_51790c1ef5c3";
	private static final String HYSFT_PUB_ID = "gh_d8ca418ebb2b";
	
	@Autowired
	private JiddPubNoClient jiddPubNoClient;
	
	/** 需要刷新商户审核状态的URI集合 **/
	private static final String[] needRefreshUris = {
			"/userAccessCtrl.htm",
			"/advertise.htm"
	};

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在多个Interceptor，
	 * 然后SpringMVC会根据声明的前后顺序一个接一个的执行，
	 * 而且所有的Interceptor中的preHandle方法都会在Controller方法调用之前调用。
	 * SpringMVC的这种Interceptor链式结构也是可以进行中断的，
	 * 这种中断方式是令preHandle的返回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response, Object handler) throws Exception {
		log.info("preHandle enter");
		if (request.getRequestURI().endsWith(SCAN_SHARE_CODE_URI)
				&& JiddStringUtils.isBlank(request.getParameter(PUB_NO_ID))) {
			request.setAttribute(PUB_NO_ID, HYSFT_PUB_ID);
		}
		validWechatUser(HYSFT_PUB_ID,request);
		// 刷新商户信息
		if (isNeedRefreshUri(request.getRequestURI())) {
			refreshNoCardUser(request);
		}
		return true;
	}

	private void validWechatUser(String pubId,HttpServletRequest request) throws JiddGlobalValidException {
		//用户点击菜单进入
		String currentCode = request.getParameter(JiddWapConstants.OAUTH2_CODE);
		if (JiddStringUtils.isBlank(currentCode)) {
			log.info("非微信，无需授权");
			return;
			//throw new JiddGlobalValidException(JiddErrorCode.ERROR_CODE_MW001.getErrorCode(), JiddErrorCode.ERROR_CODE_MW001.getErrorMsg());
		}

		log.info("微信用户开始网页授权,公众号ID:[{}]", pubId);
		OAuthUser authUser = jiddPubNoClient.getWXAuthUser(pubId, currentCode, JiddWapConstants.WX_OAUTH2_BASE_SCOPE);
		if(authUser == null || JiddStringUtils.isBlank(authUser.getOpenid()) && JiddStringUtils
				.isBlank(authUser.getAliPayUserID())){
			log.error("获取微信用户信息失败,当前授权码[{}]无效", currentCode);
			throw new JiddGlobalValidException(JiddErrorCode.ERROR_CODE_MW001.getErrorCode(), JiddErrorCode.ERROR_CODE_MW001.getErrorMsg());
		}

		//存缓存
		request.getSession().setAttribute(JiddWapConstants.OAUTH2_CODE, currentCode);
		//noCardUserVO.setWechatOpenId(authUser.getOpenid());
	}
	
	private void refreshNoCardUser(HttpServletRequest request) throws JiddGlobalValidException {
		Object userVO = request.getSession().getAttribute(CURRENT_USER);
		log.info("refreshNoCardUser,userVO=[{}]", userVO);
		if (userVO == null) {
			return;
		}
	}

	private boolean isNeedRefreshUri(String uri) {
		for (String listUri : needRefreshUris) {
			if (uri.endsWith(listUri)) {
				return true;
			}
		}
		return false;
	}

}