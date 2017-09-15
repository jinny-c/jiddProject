package com.jidd.basic.redefine;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSONObject;
import com.jidd.basic.httpclient.RequestResponseContext;
import com.jidd.basic.serialize.JiddSerialTypeEnum;
import com.jidd.basic.utils.JiddObjectTypeUtils;
import com.jidd.basic.utils.JiddStringUtils;
import com.jidd.view.base.modelview.JiddModelAndView;
import com.jidd.view.exception.JiddGlobalValidException;
import com.jidd.view.secure.JiddSecureProcessor;

@SuppressWarnings("rawtypes")
public class JiddObjHttpMsgConvert extends
		AbstractHttpMessageConverter<JiddModelAndView> {
	
	private static Logger logger = LoggerFactory.getLogger(JiddObjHttpMsgConvert.class);

	@Autowired
	private JiddSecureProcessor jiddSecureProcessor;

	private static String CHARSET = "UTF-8";

	public JiddObjHttpMsgConvert() {
		/*this(new MediaType("text", "plain", Charset.forName("UTF-8")),
				new MediaType("json", "application", Charset.forName("UTF-8")),
				new MediaType("xml", "application", Charset.forName("UTF-8")));*/
		this(MediaType.TEXT_PLAIN,
				MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_XML);
	}

	public JiddObjHttpMsgConvert(MediaType... supportedMediaTypes) {
		super(supportedMediaTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.http.converter.AbstractHttpMessageConverter#supports
	 * (java.lang.Class)
	 */
	@Override
	protected boolean supports(Class<?> clazz) {
		return JiddModelAndView.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.http.converter.AbstractHttpMessageConverter#readInternal
	 * (java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
	@Override
	protected JiddModelAndView readInternal(
			Class<? extends JiddModelAndView> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		return super.read(clazz, inputMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal
	 * (java.lang.MicroModelAndView, org.springframework.http.HttpOutputMessage)
	 */
	@Override
	protected void writeInternal(JiddModelAndView model,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		if (model == null ) {
			logger.error("the httpobj or type is nll,url:"
					+ RequestResponseContext.getRequest().getRequestURL());
			throw new HttpMessageNotWritableException("response error");
		}
		JiddSerialTypeEnum mste = model.getTe();
		//JiddSerialTypeEnum mste = JiddSerialTypeEnum.SERILAL_TYPE_FASTJSON;
		Object data = model.getData();
		// 加密处理
		try {
			logger.info("resp encr judge start");
			this.encr(data);
		} catch (JiddGlobalValidException e) {
			/*String respMsg = MicroResponseUtils.toErr(RequestResponseContext.getRequest(),
					e.getErrorCode(), e.getErrorMsg());
			outputMessage.getBody().write(respMsg.getBytes(HpayCharset.UTF8.getValue()));*/
			return;
		}
		List<MediaType> mList = getSupportedMediaTypes();
		logger.info("MediaType list={}",mList);
		
		String msg = mste.toStr(data);

		if (JiddStringUtils.isBlank(msg)) {
			logger.error("the http data is null,url:"
					+ RequestResponseContext.getRequest().getRequestURL());
			throw new HttpMessageNotWritableException("response null");
		}

		if (mste.isJson()) {
			//HINT 打印返回参数
			logger.info("clientResponseData: " + model);
			JSONObject json = (JSONObject) JSONObject.toJSON(model);//将java对象转换为json对象
			String str = json.toString();//将json对象转换为字符串
			
			logger.info("clientResponseData: " + str);
			
			//outputMessage.getBody().write(dataCompile(data));
			//outputMessage.getBody().write(msg.getBytes(CHARSET));
			outputMessage.getBody().write(str.getBytes(CHARSET));
            return;
		}
		//HINT 打印返回参数(JSON)
		logger.info("clientResponseData: " + msg);
		outputMessage.getBody().write(msg.getBytes(CHARSET));
	}

	/**
	 * 加密处理
	 * 
	 * @param data
	 * @throws MicroGlobalValidException
	 */
	private void encr(Object data) throws JiddGlobalValidException {
        encr(data, data.getClass());
	}


    /**
     * 加密处理
     *
     * @param data
     * @throws MicroGlobalValidException
     */
    private void encr(Object data, Class<? extends Object> clazz) throws JiddGlobalValidException {
        // 2016-09-20 处理父类中需要加密的属性字段
        for(; clazz != Object.class; clazz = clazz.getSuperclass()) {
            if (!jiddSecureProcessor.isSecure(clazz)) {
                return;
            }
            PropertyDescriptor pd = null;
            Method reader = null;
            Method writer = null;
            /*MicroHttpHeader header = jiddSecureProcessor.getHeader();
            UserKeyBean userKey = jiddSecureProcessor.getUserKey(
                    header.getVersion(), header.getTerminalUserID());*/
            try {
                Object v = null;
                for (Field field : clazz.getDeclaredFields()) {
                    if (field == null) {
                        continue;
                    }

                    if (!jiddSecureProcessor.isEncr(field)) {
                        continue;
                    }
                    // 2016-09-20 递归处理被MicroEncrAnno注解标识的集合
                    if(JiddObjectTypeUtils.isCollections(field.getType())){
                        field.setAccessible(true);
                        List<?> list = (List<?>) field.get(data);
                        for (Object o : list) {
                            encr(o);
                        }
                    }else{
                        pd = new PropertyDescriptor(field.getName(), clazz);
                        reader = pd.getReadMethod();
                        if (reader == null) {
                            continue;
                        }
                        v = reader.invoke(data);
                        if (v == null) {
                            continue;
                        }
                        logger.info("jiddSecureProcessor encrypt field=[{}],value=[{}]",field.getName(),String.valueOf(v));
                        v = jiddSecureProcessor.encrypt("key", field.getName(), String.valueOf(v),
                                "version", "sessionId");
                        writer = pd.getWriteMethod();
                        if (writer == null) {
                            continue;
                        }
                        writer.invoke(data, v);
                    }
                }
            } catch (Exception e) {
                logger.error("resp data encr fail", e);
            }
        }
    }
}
