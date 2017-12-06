package com.jidd.view.wechat.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.view.controller.JiddUserController;
import com.jidd.view.wechat.service.IJiddPubNumTokenCacheService;

/**
 * SpringContext监听器
 *
 * @history
 */
public class SpringContextListener implements ApplicationListener<ApplicationContextEvent> {
	/** SLF4J */
	private static final Logger log = LoggerFactory.getLogger(SpringContextListener.class);

	private static final String BEAN_ID_TOKEN = "jiddPubNumTokenCacheService";

	private JiddRefreshTimer timer = new JiddRefreshTimer();;

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event.getApplicationContext().getParent() == null) {//root application context
			if (event instanceof ContextRefreshedEvent) {
				IJiddPubNumTokenCacheService jiddPubNumTokenCacheService = event.getApplicationContext()
						.getBean(BEAN_ID_TOKEN, IJiddPubNumTokenCacheService.class);
				if (jiddPubNumTokenCacheService == null) {
					log.error("IJiddPubNumTokenCacheService不存在，请检查dubbo配置");
					throw new IllegalArgumentException("服务还未注册,请检查配置");
				}

				timer.setJiddPubNumTokenCacheService(jiddPubNumTokenCacheService);
				log.info("启动ACCESS_TOKEN刷新线程");
				timer.start();

				// 初始化公众号别名
				List<String> stList = new ArrayList<String>(Arrays.asList("a","b","c","d"));
				for (int i = 0; i < stList.size(); i++) {
					String alias = JiddStringUtils.join(i,"_",stList.get(i));
					JiddUserController.pubMap.put(alias, alias);
				}
				log.info("initialization finish,JiddUserController.pubMap:[{}]",
						JiddUserController.pubMap);
			}

			if (event instanceof ContextClosedEvent) {
				log.info("销毁ACCESS_TOKEN刷新线程");
				timer.finish();
				timer = null;
			}
		}
	}
}