package com.jidd.basic.httpclient;


import java.io.Serializable;

public class HttpHeader implements Serializable {
	private static final long serialVersionUID = -2775833786937635259L;

	/** 协议版本号 **/
	private String version;
	/** 会话ID **/
	private String sessionID;
	/** 数据校验值 **/
	private String signature;
	/** 客户端用户ID **/
	private String terminalUserID;
	/** 数据内容长度 **/
	private String dataLength;
	/** 协议内容类型 **/
	private String contentType;
	/** 密钥协商请求数据 **/
	private String keyExchange;
	/** 客户端缓存方式 **/
	private String hpCache;
	private String clientTUDID;
	private String ClientIMEI;
	private String clientMAC;
	/** 客户端数据类型 **/
	//private MicroSerialTypeEnum dataType;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTerminalUserID() {
		return terminalUserID;
	}

	public void setTerminalUserID(String terminalUserID) {
		this.terminalUserID = terminalUserID;
	}

	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getKeyExchange() {
		return keyExchange;
	}

	public void setKeyExchange(String keyExchange) {
		this.keyExchange = keyExchange;
	}

	public String getHpCache() {
		return hpCache;
	}

	public void setHpCache(String hpCache) {
		this.hpCache = hpCache;
	}

	public String getClientTUDID() {
		return clientTUDID.toUpperCase();
	}

	public void setClientTUDID(String clientTUDID) {
		this.clientTUDID = clientTUDID;
	}

	public String getClientIMEI() {
		return ClientIMEI;
	}

	public void setClientIMEI(String clientIMEI) {
		ClientIMEI = clientIMEI;
	}

	public String getClientMAC() {
		return clientMAC;
	}

	public void setClientMAC(String clientMAC) {
		this.clientMAC = clientMAC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MicroHttpHeader [version=");
		builder.append(version);
		builder.append(", sessionID=");
		builder.append(sessionID);
		builder.append(", signature=");
		builder.append(signature);
		builder.append(", terminalUserID=");
		builder.append(terminalUserID);
		builder.append(", dataLength=");
		builder.append(dataLength);
		builder.append(", contentType=");
		builder.append(contentType);
		builder.append(", keyExchange=");
		builder.append(keyExchange);
		builder.append(", hpCache=");
		builder.append(hpCache);
		builder.append(", clientTUDID=");
		builder.append(clientTUDID);
		builder.append(", ClientIMEI=");
		builder.append(ClientIMEI);
		builder.append(", clientMAC=");
		builder.append(clientMAC);
		builder.append(", dataType=");
		builder.append("]");
		return builder.toString();
	}
}
