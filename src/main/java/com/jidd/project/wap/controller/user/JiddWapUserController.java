package com.jidd.project.wap.controller.user;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.project.base.JiddProjectBaseController;
import com.jidd.project.base.cache.JiddProjectCacheManager;
import com.jidd.project.wap.controller.user.dto.UserWapQueryCodeReq;
import com.jidd.view.controller.dto.UserLoginDto;
import com.jidd.view.exception.JiddControllerException;


/**
 *
 *用户登录
 *
 */
@Controller
@RequestMapping("/wap/user")
public class JiddWapUserController extends JiddProjectBaseController {
	/* Slf4j */
	private static Logger log = LoggerFactory.getLogger(JiddWapUserController.class);
	
	@Autowired
	private JiddProjectCacheManager cacheManager;
	
    // 验证码字符个数
    private static final int CODE_COUNT = 4;
    private static final char[] CODE_SEQUENCE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	@RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
	public String index() throws JiddControllerException{
		log.info("index enter");
		return toRedirect("/user/info.htm");
	}
	
	@RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
	public String login(@JiddSecureAnno UserLoginDto reqDto) throws JiddControllerException{
		log.info("login enter,reqDto:{}", reqDto);
		if(JiddStringUtils.isNotBlank(reqDto.getMobile())){
			reqDto.getChannel().equals(reqDto.getMobile());
		}
		if(JiddStringUtils.isBlank(reqDto.getAction())){
			throw new JiddControllerException("error","登录出错了");
		}
		return toWapHtml("tips");
	}
	
	@RequestMapping(value = "/userInfo", method = {RequestMethod.GET, RequestMethod.POST})
	public String userInfo(Model model) {
		log.info("userInfo enter");
		return toWapHtml("userInfo");
	}

	/**
	 * 获取验证码
	 * 通配符
	 * @return
	 */
	
	@RequestMapping(value = "/getVerifiCode{flag}", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String getVerifiCode(@JiddSecureAnno UserWapQueryCodeReq req,@PathVariable String flag){
		try {
			log.info("getVerifiCode,req={},flag={}", req, flag);
			if("mobile".equals(flag)){
				
			}
			SecureRandom random = new SecureRandom();
			StringBuilder randomCode = new StringBuilder();
			for (int i = 0; i < CODE_COUNT; i++) {
				String strRand = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
				randomCode.append(strRand);
			}
			cacheManager.setCacheConfig(randomCode.toString(), randomCode.toString());
			//cacheManager.setCacheConfig(req.getMobile(), randomCode.toString());
			return randomCode.toString();
		} catch (Exception e) {
			log.info("getVerifiCode exception:{}", e);
			return "error";
		}
	}

}
