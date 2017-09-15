package com.jidd.test;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jidd.basic.httpclient.JiddHttpBuilder;
import com.jidd.basic.httpclient.JiddHttpExecutor;
import com.jidd.basic.utils.JiddStringUtils;


public class TestShareCode {

    @Test
    public void toShortUrl() throws Exception{

    	String acctoken = "ZjHTZ-Vu5aCrkXGNQWp_5TdwDZKbgW1izVwqkITK9OkuUCR94n0o_Hq8nO-Facc-LG9s8pMd1f5SHIVx6t4upmyca9SjSw0Fe1TxY2LqlNTCKExhsw35i7FF3ofpwuc_TZMdAFAWCP";
		String url = "http://10.148.21.80:8081/jiddProjects";
		
		String requestUrl = JiddStringUtils.replace("https://api.weixin.qq.com/cgi-bin/shorturl?access_token={}", acctoken);
		JSONObject object = new JSONObject();
		object.put("action", "long2short");
		object.put("long_url", url);
		String requestData = object.toString();
		
		JiddHttpExecutor executor = JiddHttpBuilder.create()
				.loadPool(1, 1)
				.loadTimeOut(3000, 3000)
				.loadIgnoreUrl()
				.build();
		String result = executor.doPostWithUrl(requestUrl, requestData, null); //����JSON��ʽ���
		
		System.out.println("===========" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		System.out.println("===========" + jsonObject);
		System.out.println("===========" + jsonObject.getString("short_url"));
		
    }

}
