package com.jidd.view.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jidd.basic.httpclient.JiddHttpBuilder;
import com.jidd.basic.httpclient.JiddHttpExecutor;

public class JiddHttpExecutorHolder {
	/** SLF4J */
	private static final Logger log = LoggerFactory.getLogger(JiddHttpExecutorHolder.class);

	private static JiddHttpExecutor executor = null;
	static {
		if (null == executor) {
			try {
				executor = JiddHttpBuilder.create()
						.loadPool(30, 10)
						.loadTimeOut(3000, 3000)
						.loadIgnoreUrl()
						.build();
			} catch (Exception e) {
				log.error("create http executor fail", e);
			}
		}
	}

	public static JiddHttpExecutor getExecutor(){
		return executor;
	}
}