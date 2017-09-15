package com.jidd.view.secure;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import com.jidd.basic.security.JiddDecrAnno;
import com.jidd.basic.security.JiddEncrAnno;
import com.jidd.basic.security.JiddSecureAnno;
import com.jidd.basic.security.utils.JiddDESedeUtils;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.view.exception.JiddControllerException;
import com.jidd.view.secure.utils.SecureManager;
/**
 * 处理器
 * 
 * @history
 */
@Component
public class JiddSecureProcessor implements Serializable {
	private static final long serialVersionUID = 5419849524867234626L;

	
	private static Logger logger = LoggerFactory.getLogger(JiddSecureProcessor.class);


/*	@Autowired
	private IUserLogService userLogService;

	public UserKeyBean getUserKey(String version, String terminalUserID)
			throws MicroGlobalValidException {}

	*//**
	 * 获取头信息
	 * 
	 * @return
	 *//*
	public MicroHttpHeader getHeader() {}*/

	public boolean isSecure(MethodParameter parameter) {
		return parameter.getParameterAnnotation(JiddSecureAnno.class) != null;
	}

	public boolean isSecure(Class<? extends Object> clazz) {
		return clazz.isAnnotationPresent(JiddSecureAnno.class);
	}

	/**
	 * 是否需要加密
	 * 
	 * @param filed
	 * @return
	 */
	public boolean isEncr(Field filed) {
		return filed != null && filed.isAnnotationPresent(JiddEncrAnno.class);
	}

	/**
	 * 是否需要解密
	 * 
	 * @param filed
	 * @return
	 */
	public boolean isDecr(Field filed) {
		return filed != null && filed.isAnnotationPresent(JiddDecrAnno.class);
	}

	/**
	 * 客户端数据加密
	 * 
	 * @param userKey
	 * @param plain
	 * @param version
	 * @param sessionId
	 * @return
	 * @throws ActionException
	 */
	public String encrypt(String key, String fieldName, String plain,
			String version, String sessionId) throws JiddControllerException {
		logger.info("encrypt start------------------");
		if (JiddStringUtils.isBlank(plain)) {
			return "";
		}
		return encryptDes(key, plain);
	}

	/***
	 * 客户端数据解密
	 * 
	 * @param userKey
	 * @param cipherText
	 * @param version
	 * @param sessionId
	 * @return
	 * @throws ActionException
	 */
	public String decrypt(String key, String cipherText,
			String version, String sessionId) throws JiddControllerException {
		logger.info("decrypt start------------------");
		if (JiddStringUtils.isBlank(cipherText)) {
			return "";
		}
		return decryptDes(key, cipherText);
	}

	private String encryptDes(String key, String data)
			throws JiddControllerException {
		if("key".equals(key)){
			return JiddStringUtils.join("enc_",data);
		}
		//des
		data = new SecureManager().Des(data, 1,key);
		//3des
		data = JiddDESedeUtils.doEncr(key, data);
		
		data = data != null ? data.trim() : data;
		return data;
	}
	private String decryptDes(String key, String data)
			throws JiddControllerException {
		return JiddStringUtils.join("decryptDes_",data);
		/*data = JiddStringUtils.trimStr(new SecureManager().Des(data, 0,
				key));
		return data;*/
	}
	
	public static void main(String[] args) {
		//des加解密 key[16]位
		//加密
		String encStr = new SecureManager().Des("123123", 1, "50d43ea2a4e66786");
		System.out.println("---------"+encStr);
		//解密
		String desStr = new SecureManager().Des(encStr, 0, "50d43ea2a4e66786");
		System.out.println("---------"+desStr);
		
		
	}
}