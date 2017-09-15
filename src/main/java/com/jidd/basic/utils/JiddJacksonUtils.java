package com.jidd.basic.utils;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class JiddJacksonUtils {

	private static final Logger logger = Logger
			.getLogger(JiddJacksonUtils.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		// 设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
		// Include.Include.ALWAYS 默认
		// Include.NON_DEFAULT 属性为默认值不序列化
		// Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化
		// Include.NON_NULL 属性为NULL 不序列化
		mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
		// 设置将MAP转换为JSON时候只转换值不等于NULL的
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES,
				false);
		mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// 设置有属性不能映射成PO时不报错
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 解析java对象为jackson字符串
	 * 
	 * @param object
	 * @return
	 */
	public static <T> String toStr(T object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.warn("[jackson]:object to string data exception:", e);
			return null;
		}
	}

	/**
	 * 解析jackson字符串为java对象
	 * 
	 * @param data
	 * @param clazz
	 * @return
	 */
	public static <T> T toObj(String data, Class<T> clazz) {
		try {
			return mapper.readValue(data, clazz);
		} catch (Exception e) {
			logger.warn("[jackson]:string to object data exception:", e);
			return null;
		}
	}
}
