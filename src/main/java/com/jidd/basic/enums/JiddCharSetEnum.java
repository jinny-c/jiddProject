package com.jidd.basic.enums;

import java.nio.charset.Charset;

import com.jidd.basic.utils.JiddStringUtils;

public enum JiddCharSetEnum {

	/** UTF8 **/
	UTF8("UTF-8"),
	/** GBK **/
	GBK("GBK"),
	/** GB18030 **/
	GB18030("GB18030"),
	/** GB2312 **/
	GB2312("GB2312"),
	/** ISO8859-1 **/
	ISO88591("ISO8859-1"),
	/** UTF-16 **/
	UTF16("UTF-16"),

	;

	/** �ַ� **/
	private String charset;

	private JiddCharSetEnum(String charset) {
		this.charset = charset;
	}

	public String charset() {
		return charset;
	}

	public Charset forName() {
		return Charset.forName(this.charset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return JiddStringUtils.join( "name=", name(),JiddSymbolEnum.Comma.symbol(),
				" charset=", charset);
	}
	
	public static void main(String[] args) {
		String s = JiddCharSetEnum.GB18030+"";
		System.out.println(JiddCharSetEnum.GB18030);
		System.out.println(s);
	}
}
