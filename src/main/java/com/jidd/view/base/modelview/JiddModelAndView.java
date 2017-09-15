package com.jidd.view.base.modelview;

import java.io.Serializable;

import com.jidd.basic.serialize.JiddSerialTypeEnum;


public class JiddModelAndView<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private JiddSerialTypeEnum te;

	private T data;

	public JiddModelAndView(JiddSerialTypeEnum te, T data) {
		this.te = te;
		this.data = data;
	}

	public JiddSerialTypeEnum getTe() {
		return te;
	}

	public T getData() {
		return data;
	}

	public static JiddModelAndView<RespSucc> succ(JiddSerialTypeEnum te) {
		return new JiddModelAndView<RespSucc>(te, new RespSucc());
	}

	public static JiddModelAndView<RespErr> err(JiddSerialTypeEnum te,
			String code, String msg) {
		return new JiddModelAndView<RespErr>(te, new RespErr(code, msg));
	}

}