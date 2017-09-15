package com.jidd.view.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jidd.basic.common.ServiceProvider;
import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.view.base.JiddBaseController;
import com.jidd.view.controller.dto.UserLoginDto;
import com.jidd.view.controller.dto.UserLoginRespDto;
import com.jidd.view.exception.JiddControllerException;
import com.jidd.view.wechat.service.IJiddPubNumTokenCacheService;


/**
 *
 * @version 1.0 2017年01月13日
 * @history
 */
@Controller
@RequestMapping("/auxiliary")
public class JiddAuxiliaryController extends JiddBaseController {
	/* Slf4j */
	private static Logger log = LoggerFactory.getLogger(JiddAuxiliaryController.class);
	
	//@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/text;charset=UTF-8")
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object index(@JiddSecureAnno UserLoginDto reqDto)
			throws JiddControllerException {
		log.info("auxiliary index enter,reqDto={}", reqDto);
		
		//HttpServletRequest rep = getRequest();
		
		UserLoginRespDto respDto = new UserLoginRespDto();
		respDto.setMobile("13612346666");
		respDto.setImageCode("123abc");
		respDto.setVerifyCode("123654");
		respDto.setChannel("channel");
		respDto.setDesc("这是描述");
		
		log.info("auxiliary index resp={}", respDto);
		return toData(respDto);
	}
	@RequestMapping(value = "/keyExchange", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Object keyExchange() throws JiddControllerException{
		log.info("auxiliary keyExchange enter");
		IJiddPubNumTokenCacheService jiddTokeCache = ServiceProvider.getService("jiddPubNumTokenCacheService");
		if (null == jiddTokeCache) {
			log.error("该服务[jiddPubNumTokenCacheService]未注册，请检查配置");
			throw new JiddControllerException("error","服务未注册");
		}
		log.info("accToken={}",jiddTokeCache.getAccessToken("pubNo", "appId"));
		if (null != jiddTokeCache) {
			log.info("jidd test");
			//return toRequestForward("/user/tips.htm");
		}
        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put("status", 1);
		return toData(resp);
	}
	

}
