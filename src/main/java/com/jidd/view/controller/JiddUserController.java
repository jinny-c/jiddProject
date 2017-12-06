package com.jidd.view.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jidd.basic.async.IAsyncTaskExecutor;
import com.jidd.basic.async.SimpleAsyncTaskExecutor;
import com.jidd.basic.common.ServiceProvider;
import com.jidd.basic.httpclient.RequestResponseContext;
import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.basic.utils.JiddTraceLogUtil;
import com.jidd.db.user.service.IJiddMgmtUmgService;
import com.jidd.view.base.JiddBaseController;
import com.jidd.view.controller.dto.UserBaseDto;
import com.jidd.view.controller.dto.UserLoginDto;
import com.jidd.view.exception.JiddControllerException;
import com.jidd.view.exception.JiddGlobalValidException;
import com.jidd.view.wechat.service.IJiddPubNumTokenCacheService;


/**
 *
 * @history
 */
@Controller
@RequestMapping("/user")
public class JiddUserController extends JiddBaseController {
	/* Slf4j */
	private static Logger log = LoggerFactory.getLogger(JiddUserController.class);
	
	@Autowired
	private IJiddMgmtUmgService jiddMgmtUmgService;
	
	// SpringContextListener中初始化公众号别名，在生成推广码时使用别名
	public static Map<String, String> pubMap = new HashMap<String, String>();
	
	/** 异步线程 **/
	private IAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
	
    // 验证码字符个数
    private static final int CODE_COUNT = 4;
    private static final char[] CODE_SEQUENCE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	@RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
	public String index() throws JiddControllerException{
		log.info("index enter");
		//清除Session
		//getSession().removeAttribute("");
		//getSession().setAttribute(PAYMENT_TOKEN, scanReq.getToken());
		IJiddPubNumTokenCacheService jiddTokeCache = ServiceProvider.getService("jiddPubNumTokenCacheService");
		if (null == jiddTokeCache) {
			log.error("该服务[jiddPubNumTokenCacheService]未注册，请检查配置");
			throw new JiddControllerException("error","服务未注册");
		}
		//jiddTokeCache.getAccessToken("pubNo", "appId");
		log.info("accToken={}",jiddTokeCache.getAccessToken("pubNo", "appId"));
		if (null != jiddTokeCache) {
			log.info("jidd test");
			//return toRequestForward("/user/tips.htm");
		}
		return toRedirect("/user/info.htm");
	}
	
	@RequestMapping(value = "/toLogin", method = {RequestMethod.GET, RequestMethod.POST})
	public String toLogin() throws JiddControllerException{
		log.info("toLogin enter");
		//getSession().removeAttribute("");
		//getSession().setAttribute(PAYMENT_TOKEN, scanReq.getToken());
		int res = jiddMgmtUmgService.queryCount();
		log.info("res={}",res);
		return toUserHtml("login");
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
		return toUserHtml("tips");
	}
	
	@RequestMapping(value = "/info", method = {RequestMethod.GET, RequestMethod.POST})
	public String info(Model model, UserBaseDto reqDto) {
		log.info("info enter");
		if(JiddStringUtils.isBlank(reqDto.getMobile())){
			//reqDto.getChannel().equals(reqDto.getMobile());
			reqDto.getMobile().equals(reqDto.getChannel());
		}
		model.addAttribute("message", "hello word!");
		return toUserHtml("tips");
	}
	
	@RequestMapping(value = "/toSuccess", method = {RequestMethod.GET, RequestMethod.POST})
	public String toSuccess(Model model) {
		log.info("info enter");
		HttpServletRequest request = RequestResponseContext.getRequest();
		String flag = request.getParameter("flag");
		log.info("toSuccess,flag={}", flag);
		flag ="02";
		model.addAttribute("flag", flag);
		return toUserHtml("res_success");
	}
	
	@RequestMapping(value = "/userInfo", method = {RequestMethod.GET, RequestMethod.POST})
	public String userInfo(Model model) {
		log.info("userInfo enter");
		return toUserHtml("userInfo");
	}
	/**
	 * 通配符入口
	 * @param model
	 * @param wildcard
	 * @return
	 * @throws JiddControllerException 
	 */
	@RequestMapping(value = "/publicEntrance{wildcard}", method = {RequestMethod.GET, RequestMethod.POST})
	//@ResponseBody
	public Object publicEntrance(Model model, @PathVariable String wildcard) throws JiddControllerException {
		log.info("interaction enter,wildcard=[{}]",wildcard);
		if("_1".equals(wildcard)){
			log.info("userInfo start");
			return toRequestForward("/user/userInfo.htm");
		}
		if("_2".equals(wildcard)){
			log.info("toSuccess start");
			return toRedirect("/user/toSuccess.htm");
		}
		
		log.info("toSucc start");
		return toSucc();
	}

	/**
	 * 手机验证码
	 * @return
	 */
	@RequestMapping(value = "/getMobileVerifiCode", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String getMobileVerifiCode(UserLoginDto reqDto){
		try {
			log.info("getMobileVerifiCode start,reqDto={}", reqDto);
            SecureRandom random = new SecureRandom();
            StringBuilder randomCode = new StringBuilder();
            for (int i = 0; i < CODE_COUNT; i++) {
                String strRand = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
                randomCode.append(strRand);
            }
            log.info("success,verifyCode=" + randomCode.toString());
            return randomCode.toString();
		} catch (Exception e) {
			log.info("getMobileVerifiCode exception:{}", e);
			return "error";
		}
	}
	/**
	 * 图形验证码
	 * @return
	 */
	@RequestMapping(value = "/getVerifiCode", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String getVerifiCode(){
		try {
			log.info("TraceID:{}, getVerifiCode start:", JiddTraceLogUtil.getTraceId());
			SecureRandom random = new SecureRandom();
			StringBuilder randomCode = new StringBuilder();
			for (int i = 0; i < CODE_COUNT; i++) {
				String strRand = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
				randomCode.append(strRand);
			}
			
			List<Future<String>> results = new ArrayList<Future<String>>();
			final String rCode = randomCode.toString();
			initThreadPool();
			int count = 1;
			do {
				this.executorService.execute(new Runnable() {
					@Override
					public void run() {
						asyncSendVerifiCode(rCode);
					}
				});
				
				results.add(executorService.submit(new ImportDataProcessor(
						rCode)));
				
				count++;
			} while (count < 3);
			
			log.info("success,verifyCode=" + randomCode.toString());
			
			String st = null;
			for (Future<String> result : results) {
				try {
					st = result.get();
					log.info("st={}",st);
				} catch (Exception e) {
					log.error("exception",e);
				}
			}
			
			return randomCode.toString();
			
		} catch (Exception e) {
			log.info("getVerifiCode exception:{}", e);
			return "error";
		}
	}
	

	/**
	 * 异步线程
	 *
	 */
	private void asyncSendVerifiCode(final String code) {
		try {
			asyncTaskExecutor.exeWithoutResult(new IAsyncTaskExecutor.AsyncTaskCallBack<Object>() {
				@Override
				public Object invork() throws JiddGlobalValidException {
					try {
						log.info("asynchronous start,code={}",code);
					} catch (Exception e) {
						log.error("asynchronous exception", e);
					}
					return null;
				}
			});
		} catch (Exception e) {
			log.error("do asynchronous exception",e);
		}
	}
	// 任务执行器
	private ExecutorService executorService;
	
	// 5线程执行
	private void initThreadPool() {
		if (executorService == null) {
			executorService = new ThreadPoolExecutor(5, 5, 0L,
					TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
					new ThreadPoolExecutor.DiscardPolicy());
		}
	}
	
	/**
	 * 线程执行的类
	 * 
	 */
	private class ImportDataProcessor implements
			Callable<String> {
		private String value;

		public ImportDataProcessor(String value) {
			this.value = value;
		}

		@Override
		public String call() throws Exception {
			try {
				log.info("ImportDataProcessor,code={}",value);
				return JiddStringUtils.join(value,"123");
			} catch (Exception e) {
				log.error("query userBean exception:",e);
			}
			return null;
		}
	}

}
