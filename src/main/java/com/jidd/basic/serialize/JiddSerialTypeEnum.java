package com.jidd.basic.serialize;

import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.basic.utils.JiddStringUtils;

public enum JiddSerialTypeEnum {
	/** json-json序列化 **/
	SERILAL_TYPE_JSON("json", "json序列化", 0),
	/** gson-gson序列化 **/
	SERILAL_TYPE_GSON("gson", "gson序列化", 1),
	/** fastJson-fastJson序列化 **/
	SERILAL_TYPE_FASTJSON("fastJson", "fastJson序列化", 2),
	/** jackson-jackson序列化 **/
	SERILAL_TYPE_JACKSON("jackson", "jackson序列化", 3),
	/** lua-lua序列化 **/
	SERILAL_TYPE_LUA("lua", "lua序列化", 4),

	;

	/** 序列化类型 **/
	private String serilalType;
	/** 序列化类型描述 **/
	private String desc;
	/** 序列化标标识(不可重复) **/
	private int flag;

	private JiddSerialTypeEnum(String serilalType, String desc, int flag) {
		this.serilalType = serilalType;
		this.desc = desc;
		this.flag = flag;
	}

	public String serilalType() {
		return serilalType;
	}

	public String desc() {
		return desc;
	}

	/**
	 * 解析java对象为序列化字符串
	 * 
	 * @param object
	 *            [java对象]
	 * @return[序列化字符串]
	 */
	public String toStr(Object object) {
		if (object == null) {
			return null;
		}
		return serilalObj2Data(object, this);
	}

	/**
	 * 解析序列化字符串为java对象
	 * 
	 * @param data
	 *            [序列化字符串]
	 * @param clazz
	 * @return
	 */
	public <T> T toObj(String data, Class<T> clazz) {
		if (JiddStringUtils.isBlank(data) || clazz == null) {
			return null;
		}
		return serilalData2Obj(clazz, data, this);
	}

	/**
	 * 解析java对象为序列化字符串
	 * 
	 * @param object
	 * @param e
	 * @return
	 */
	public static String serilalObj2Data(Object object, JiddSerialTypeEnum e) {
		if (object == null || e == null) {
			return null;
		}
		switch (e.flag) {
		case 0:
			return JiddJsonUtils.toStr(object);
		default:
			return JiddSymbolEnum.Blank.symbol();
		}
	}

	/**
	 * 解析序列化字符串为java对象
	 * 
	 * @param clazz
	 * @param data
	 * @param e
	 * @return
	 */
	public static <T> T serilalData2Obj(Class<T> clazz, String data,
			JiddSerialTypeEnum e) {
		if (JiddStringUtils.isBlank(data) || clazz == null) {
			return null;
		}
		switch (e.flag) {
		case 0:
			return JiddJsonUtils.toObj(data, clazz);
		default:
			return null;
		}
	}

	public boolean isJson() {
		return this == SERILAL_TYPE_JSON;
	}

	public boolean isGson() {
		return this == SERILAL_TYPE_GSON;
	}

	public boolean isFastJson() {
		return this == SERILAL_TYPE_FASTJSON;
	}

	public boolean isJackson() {
		return this == SERILAL_TYPE_JACKSON;
	}

	public boolean isLua() {
		return this == SERILAL_TYPE_LUA;
	}
	
	/**
	 * 转换
	 * 
	 * @param serilalType
	 * @return
	 */
	public static JiddSerialTypeEnum convert2Self(String serilalType) {
		if (JiddStringUtils.isBlank(serilalType)) {
			return null;
		}
		for (JiddSerialTypeEnum e : values()) {
			if (e.serilalType().equals(serilalType)) {
				return e;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return JiddStringUtils.join("name=", name(),
				JiddSymbolEnum.Comma.symbol(), " serilalType=", serilalType,
				" desc=", desc);
	}
}
