package com.jidd.view.wechat.service;

import com.jidd.view.wechat.dto.JiddPubNumTokenDTO;

/**
 * 微信公众号接口凭证
 *
 * @history
 */

public interface IJiddPubNumTokenCacheService {

	/**
	 * 刷新微信公众号接口访问凭证
	 *
	 * @param req
	 * @return 刷新成功返回有效的access_token，失败返回空串
	 */
	void refreshAccessToken(JiddPubNumTokenDTO req);

	/**
	 * 查询缓存Token
	 * @param pubNo
	 * @param appId
	 * @return
	 */
	String getAccessToken(String pubNo, String appId);


}