package com.jidd.view.interceptor;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jidd.basic.cache.JiddCacheManager;
import com.jidd.basic.common.SecretKeyBean;
import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.httpclient.HttpHeader;
import com.jidd.basic.utils.JiddDateUtils;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.view.exception.JiddControllerException;
import com.jidd.view.exception.JiddGlobalValidException;
import com.jidd.view.secure.utils.DesEncrypt;

public class JiddSecretKeyInterceptor extends HandlerInterceptorAdapter {
	/* Slf4j */
	private static Logger log = LoggerFactory.getLogger(JiddSecretKeyInterceptor.class);
	private static final String DEF_RESP_CODE = "99";
	private static final String DEF_ERR_MSG = "密钥协商失败!";

	private String privateKey;

	private String desKey;
	
	@Autowired
	private JiddCacheManager jiddCacheManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 过滤不需要交换密钥的URI
		/*if (microFilterConfig.isNoExchange(request.getRequestURI())) {
			logger.info(MicroStringUtils.replace(
					"this uri[{}] is not exchangeKey", request.getRequestURI()));
			response.setHeader(MicroHttpConstants.NAME_HP_KEYEXCHANGE, "0");
			return true;
		}*/
		// 获取头信息
		HttpHeader header = (HttpHeader) request
				.getAttribute(HttpHeader.class.getName());
		String key = header.getKeyExchange();
		//final String version = header.getVersion();
		log.info("key exchange:{}", key);
		// 无需交换密钥，直接返回
		if (JiddStringUtils.isBlank(key)) {
			key = "theKey";
			//String keyTest1 = jiddCacheManager.getCacheConfig(key);
			//log.info("keyTest1={}",keyTest1);
			jiddCacheManager.refreshCache(key);
			String keyTest2 = jiddCacheManager.getCacheConfig(key);
			log.info("keyTest2={}",keyTest2);
			return true;
		}
		String[] keyAry = key
				.split(JiddSymbolEnum.BracketAndColon.symbol());
		if (keyAry.length <= 1) {
			log.error("this key is no | split");
			throw new JiddGlobalValidException(DEF_RESP_CODE, DEF_ERR_MSG);
		}
		// 获取头信息中索引1的字符串为新key
		key = keyAry[1];
		// 获取terminalUserID
		String uniqueId = "";
		// 转换解密钥的RSA私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) this
				.getPrivateKeyFromBase64(this.privateKey);
		// 将客户端传过的密钥串进行hex to byte后解密
		byte[] keyBytes = this.rsaPriDecrypt(privateKey,
				Hex.decodeHex(key.toCharArray()));

		key = new String(keyBytes, "UTF-8");
		log.debug("decryptedkey:{}", key);
		String[] ary = key.split(JiddSymbolEnum.BracketAndColon.symbol());
		final String seed = ary[0];
		log.debug("seed:{}" ,seed);
		SecretKeyBean userKey = exchageKey(seed, uniqueId);
		
        request.setAttribute(SecretKeyBean.class.getName(), userKey);
        //response.setHeader(MicroHttpConstants.NAME_HP_KEYEXCHANGE, "0");
		return true;
	}


	/**
	 * rsa私钥解密
	 * 
	 * @param prvKeyIn
	 * @param encodedBytes
	 * @return byte[]
	 */
	private byte[] rsaPriDecrypt(RSAPrivateKey prvKeyIn, byte[] encodedBytes) {
		RSAPrivateCrtKey prvKey = (RSAPrivateCrtKey) prvKeyIn;
		BigInteger mod = prvKey.getModulus();
		BigInteger pubExp = prvKey.getPublicExponent();
		BigInteger privExp = prvKey.getPrivateExponent();
		BigInteger pExp = prvKey.getPrimeExponentP();
		BigInteger qExp = prvKey.getPrimeExponentQ();
		BigInteger p = prvKey.getPrimeP();
		BigInteger q = prvKey.getPrimeQ();
		BigInteger crtCoef = prvKey.getCrtCoefficient();

		RSAKeyParameters privParameters = new RSAPrivateCrtKeyParameters(mod,
				pubExp, privExp, p, q, pExp, qExp, crtCoef);

		AsymmetricBlockCipher eng = new RSAEngine();
		eng = new PKCS1Encoding(eng);

		eng.init(false, privParameters);
		byte[] data = null;
		try {
			data = eng.processBlock(encodedBytes, 0, encodedBytes.length);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return data;
	}

	private PrivateKey getPrivateKeyFromBase64(String privateKeyBase64) {
		PrivateKey privateKey = null;
		try {
			byte[] privateKeyBytes = Base64.decodeBase64(privateKeyBase64
					.getBytes());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
					privateKeyBytes);
			privateKey = keyFactory.generatePrivate(privateKeySpec);
		} catch (InvalidKeySpecException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		}
		return privateKey;
	}
	
	private SecretKeyBean exchageKey(String seed, String uniqueId)
			throws JiddControllerException {
		try {
			final String seedTime = JiddDateUtils.getCurrentDate();
			SecretKeyBean keyBean = new SecretKeyBean();
			// 密钥失效时间为1个小时后
			keyBean.setInvalidTime(JiddDateUtils.getNextTime(
					JiddDateUtils.yyyyMMddHHmmss, seedTime, 1 * 60));
			keyBean.setKey(calcFinalKey(seed));
			keyBean.setSeed(seed);
			keyBean.setSeedTime(seedTime);
			keyBean.setUniqueId(uniqueId);
			log.info("exchangeUserKey uniqueId=", uniqueId);
            return keyBean;
		} catch (Exception e) {
			log.error("exchange key to memory fail", e);
			throw new JiddControllerException(DEF_RESP_CODE, DEF_ERR_MSG, e);
		}
	}
	private String calcFinalKey(String seed) throws JiddControllerException {
		try {
			byte[] b1 = Hex.decodeHex(seed.substring(0, 16).toCharArray());
			byte[] b2 = Hex.decodeHex(seed.substring(16, 32).toCharArray());
			byte[] result = new byte[16];
			for (int i = 0; i < b1.length; i++) {
				result[i] = (byte) (b1[i] ^ b2[i]);
			}
			String seedKey = String.valueOf(Hex.encodeHex(result)).substring(0,
					16);
			byte[] keyByte = new DesEncrypt().Des(
					Hex.decodeHex(this.desKey.toCharArray()),
					Hex.decodeHex(seedKey.toCharArray()), 1);
			String finalKey = String.valueOf(Hex.encodeHex(keyByte));
			log.debug("des key:{}", finalKey);
			return finalKey;
		} catch (Exception e) {
			log.error("calc final key fail", e);
			throw new JiddControllerException(DEF_RESP_CODE, DEF_ERR_MSG, e);
		}
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}
}
