package com.jidd.basic.common;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jidd.basic.httpclient.HttpHeader;
import com.jidd.basic.serialize.JiddSerialTypeEnum;
import com.jidd.view.base.modelview.JiddModelAndView;
import com.jidd.view.base.modelview.RespErr;

/**
 * 维护响应信息
 *
 * @history
 */
public class JiddResponseUtils {

	/**
	 * HTTP ContentType 取值 - 文本
	 */
	public static String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";
	/**
	 * 协议版本号
	 */
	public static String NAME_HP_CONTENTTYPE = "X-HPContentType";
	/**
	 * HANDPAY HTTP ContentType 取值 - 文本
	 */
	public static String HP_CONTENT_TYPE_TEXT = "data/text";
	/**
	 * 数据内容长度
	 */
	public static String NAME_HP_DATALENGTH = "X-HPDataLength";
	public static String CHARSET_UTF8 = "UTF-8";
	
	
	public static String toErr(HttpServletRequest request, String errorCode, String errorMsg) {
		JiddModelAndView<RespErr> errModel = JiddModelAndView.err(getSerialType(request), errorCode, errorMsg);
		JiddSerialTypeEnum mste = errModel.getTe();
		String msg = mste.toStr(errModel.getData());
		if (mste.isLua()) {
			msg = "data=" + msg;
		}
		return msg;
	}


	/**
	 * 返回错误信息
	 *
	 * @history
	 */
	public static void writeToResponse(HttpServletResponse response, String msg)
			throws IOException {
		byte[] data = msg.getBytes(CHARSET_UTF8);
		response.setHeader(NAME_HP_DATALENGTH, data.length + "");
		response.setHeader(NAME_HP_CONTENTTYPE, HP_CONTENT_TYPE_TEXT);
		response.setContentType(CONTENT_TYPE_TEXT);
		ServletOutputStream outputStream = response.getOutputStream();
		try {
			outputStream.write(data);
			outputStream.flush();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}


	private static JiddSerialTypeEnum getSerialType(HttpServletRequest request) {
		Object header = request.getAttribute(HttpHeader.class.getName());
		if (header == null) {
			return JiddSerialTypeEnum.SERILAL_TYPE_LUA;
		}

		//HttpHeader httpHeader = (HttpHeader) header;
		
		return JiddSerialTypeEnum.SERILAL_TYPE_LUA;
	}


}