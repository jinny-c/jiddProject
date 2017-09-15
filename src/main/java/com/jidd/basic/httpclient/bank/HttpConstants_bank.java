/*
 * @(#)ProtocolConstants.java        1.0 2009-8-22
 *
 * Copyright (c) 2007-2009 Shanghai Handpay IT, Co., Ltd.
 * 16/F, 889 YanAn Road. W., Shanghai, China
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Shanghai Handpay IT Co., Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement you 
 * entered into with Handpay.
 */

package com.jidd.basic.httpclient.bank;


public class HttpConstants_bank {

	public static String NAME_RESPONSE_MAP = "responseMap";
	/**
	 * 会话ID
	 */
	public static String NAME_HP_SESSIONID = "X-HPSessionID";
	/**
	 * 协议版本号
	 */
	public static String NAME_HP_VERSION = "X-HPVersion";
	/**
	 * 数据校验值
	 */
	public static String NAME_HP_SIGNATURE = "X-HPSignature";
	/**
	 * 客户端用户ID
	 */
	public static String NAME_HP_TERMINALUSERID = "X-HPTerminalUserID";

	/**
	 * 数据内容长度
	 */
	public static String NAME_HP_DATALENGTH = "X-HPDataLength";
	/**
	 * 返回数据是否压缩
	 */
	public static String NAME_HP_COMPRESSED = "X-HPCompressed";
	/**
	 * 协议版本号
	 */
	public static String NAME_HP_CONTENTTYPE = "X-HPContentType";
	/**
	 * 密钥协商请求数据
	 */
	public static String NAME_HP_KEYEXCHANGE = "X-HPKeyExchange";
	
	/**
	 * 客户端数据协商类型
	 */
	public static String NAME_HP_DATATYPE = "X-HPDataType";

	/**
	 * 客户端缓存方式
	 */
	public static String NAME_HP_CACHE = "X-HPCache";

    //终端设备唯一标识 TUDID
    public static final String FIELD_CLIENT_TUDID = "X-HPTUDID";

    //交易请求的IMEI
    public static final String FIELD_CLIENT_IMEI = "X-HPIMEI";

    //交易请求的MAC
    public static final String FIELD_CLIENT_MAC = "X-HPMAC";

	/**
	 * HTTP ContentType 取值 - 文本
	 */
	public static String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";
	/**
	 * HTTP ContentType 取值 - 二进制
	 */
	public static String CONTENT_TYPE_BINARY = "application/octet-stream";

	/**
	 * HANDPAY HTTP ContentType 取值 - 文本
	 */
	public static String HP_CONTENT_TYPE_TEXT = "data/text";
	/**
	 * HANDPAY HTTP ContentType 取值 - 二进制
	 */
	public static String HP_CONTENT_TYPE_BINARY = "data/binary";
	/**
	 * HANDPAY HTTP ContentType 取值 - 文件
	 */
	public static String HP_CONTENT_TYPE_FILE = "file";

	/**
	 * 内部jsp页面跳转、包含 属性 key, 指定页面输出JSP文件名称
	 */
	public static String JSP_OUTPUT_KEY = "outputPage";

	/**
	 * 返回报文成功响应码
	 */
	public static String VALUE_RESPONSE_CODE_SUCCESS = "00";
	/**
	 * attr-key:小微HTTP头信息名
	 */
//	public static String ATTR_MICRO_HTTP_HEADER = "MICRO_HTTP_HEADER";
	
	/**
	 * attr-key:requestData
	 */
	public static String ATTR_REQUEST_DATA = "requestData";
	/**
	 * attr-key:terminalUserID
	 */
	public static String ATTR_TERMINALUSERID = "terminalUserID";

	public static String DATA_LUA_SUCC = "data={responseCode=0}";

	public static final String ATTR_NAME_RESPONSECODE = "responseCode";
	public static final String ATTR_NAME_ERRMESSAGE = "errorMessage";
	/** 单DES加解密版本 **/
	public static final String SECURE_DES_VERSION = "3.5";
	/** 3DES加解密版本 **/
	public static final String SECURE_3DES_VERSION = "3.7";

	/**
	 * 是否需要交换密钥的版本
	 * 
	 * @param v
	 * @return
	 */
	public static boolean isExchangeVer(String v) {
		return isDesVer(v) || is3DesVer(v);
	}

	/**
	 * 是否是单DES加解密的版本
	 * 
	 * @param v
	 * @return
	 */
	public static boolean isDesVer(String v) {
		return HttpConstants_bank.SECURE_DES_VERSION.equals(v);
	}

	/**
	 * 判断是否是3des加解密的版本
	 * 
	 * @param v
	 * @return
	 */
	public static boolean is3DesVer(String v) {
		return HttpConstants_bank.SECURE_3DES_VERSION.equals(v);
	}
}
