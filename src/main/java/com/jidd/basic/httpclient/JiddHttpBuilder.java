package com.jidd.basic.httpclient;

import java.nio.charset.Charset;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.Args;

import com.jidd.basic.utils.JiddStringUtils;

public class JiddHttpBuilder {
	private JiddHttpParams params;
	private boolean isIgnoreUrl = false;

	/**
	 * 创建器创建
	 * 
	 * @return
	 */
	public static JiddHttpBuilder create() {
		return new JiddHttpBuilder(new JiddHttpParams());
	}

	private JiddHttpBuilder(JiddHttpParams params) {
		this.params = params;
	}

	/**
	 * 配置通讯URL
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadUrl(String url) throws Exception {
		Args.notBlank(url, "http request url");
		this.params.setUrl(url);
		return this;
	}

	/**
	 * 忽略通讯URL，调用时动态指定
	 *
	 * @history
	 */
	public JiddHttpBuilder loadIgnoreUrl() throws Exception {
		this.isIgnoreUrl = true;
		return this;
	}


	/**
	 * 配置通讯编码
	 * 
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadCharset(Charset charset) throws Exception {
		this.params.setCharset(charset);
		return this;
	}

	/**
	 * 配置超时时间
	 * 
	 * @param connTimeOut
	 *            [连接超时时间,单位:毫秒]
	 * @param soTimeOut
	 *            [响应超时时间,单位:毫秒]
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadTimeOut(Integer connTimeOut, Integer soTimeOut)
			throws Exception {
		this.params.setConnTimeOut(connTimeOut);
		this.params.setSoTimeOut(soTimeOut);
		return this;
	}

	/**
	 * 配置线程池
	 * 
	 * @param maxTotal
	 *            [多线程线程池的最大连接数]
	 * @param maxPerRoute
	 *            [多线程线程池每个域名的最大连接数]
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadPool(Integer maxTotal, Integer maxPerRoute)
			throws Exception {
		this.params.setMaxTotal(maxTotal);
		this.params.setMaxPerRoute(maxPerRoute);
		if ((maxTotal != null && maxTotal.intValue() > 0)
				|| (maxPerRoute != null && maxPerRoute.intValue() > 0)) {
			this.params.setPool(true);
		}
		return this;
	}

	/**
	 * 忽略SSL认证
	 * 
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadIgnoreSSL() throws Exception {
		this.params.setIgnoreSSL(true);
		return this;
	}

	/**
	 * 配置客户端信任证书
	 * 
	 * @param trustPath
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadSSLTrust(String trustPath, String pwd)
			throws Exception {
		Args.notNull(trustPath, "https trustPath");
		this.params.setIgnoreSSL(false);
		this.params.setTrustPath(trustPath);
		this.params.setTrustPwd(pwd);
		return this;
	}

	/**
	 * 配置客户端验证证书
	 * 
	 * @param keyPath
	 * @param storePwd
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public JiddHttpBuilder loadSSLKey(String keyPath, String storePwd,
			String pwd) throws Exception {
		Args.notNull(keyPath, "https keyPath");
		Args.notNull(pwd, "https keypwd");
		this.params.setIgnoreSSL(false);
		this.params.setKeyPath(keyPath);
		this.params.setKeyStorePwd(storePwd);
		this.params.setKeyPwd(pwd);
		return this;
	}

	/**
	 * 创建执行器
	 * 
	 * @return
	 * @throws Exception
	 */
	public JiddHttpExecutor build() throws Exception {
		Args.notNull(params, "http params");
		// 2017-05-08 调用loadIgnoreUrl方法，此时不校验url
		if (!isIgnoreUrl) {
			Args.notNull(params.getUrl(), "http url");
		}
		RequestConfig config = createReqConfig(params);
		HttpClientBuilder builder = createBuilder(params, config);
		return new JiddHttpExecutor(params.getCharset(),
				params.getUrl(), builder.build(), config);
	}

	/**
	 * 创建httpbuilder
	 * 
	 * @param params
	 * @param config
	 * @return
	 * @throws Exception
	 */
	private static HttpClientBuilder createBuilder(JiddHttpParams params,
			RequestConfig config) throws Exception {
		HttpClientBuilder builder = HttpClients.custom()
				.setDefaultRequestConfig(config);
		SSLConnectionSocketFactory ssf = null;
		if (params.isIgnoreSSL()) {
			ssf = JiddHttpSSLConfig.builderIgnoreSSL();
		}
		if (params.isHttps()) {
			if (!params.isIgnoreSSL()) {
				if (JiddStringUtils.isBlank(params.getKeyPath())) {
					Args.notNull(params.getTrustPath(), "SSL trustPath");
					ssf = JiddHttpSSLConfig.builderOnlyTrust(
							params.getTrustPath(), params.getTrustPwd());
				} else {
					ssf = JiddHttpSSLConfig.builder(params.getTrustPath(),
							params.getTrustPwd(), params.getKeyPath(),
							params.getKeyStorePwd(), params.getKeyPwd());
				}
			}
			builder.setSSLSocketFactory(ssf);
		}
		if (params.isPool()) {
			builder.setConnectionManager(JiddHttpPoolConfig.build(params, ssf));
		}
		return builder;
	}

	/**
	 * 适配HTTP请求配置
	 * 
	 * @param connTimeOut
	 * @param soTimeOut
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static RequestConfig createReqConfig(JiddHttpParams params) {
		RequestConfig.Builder cu = RequestConfig.custom();
		// 设置连接超时
		cu.setConnectTimeout(params.getConnTimeOut());
		// 设置读取超时
		cu.setSocketTimeout(params.getSoTimeOut());
		// 设置从连接池获取连接实例的超时
		cu.setConnectionRequestTimeout(params.getConnTimeOut());
		// 在提交请求之前 测试连接是否可用
		cu.setStaleConnectionCheckEnabled(true);
		return cu.build();
	}
}
