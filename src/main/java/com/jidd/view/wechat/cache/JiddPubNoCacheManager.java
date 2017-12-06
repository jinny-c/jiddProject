package com.jidd.view.wechat.cache;

import static com.jidd.basic.utils.ConfigRef.WX_TOKEN_URI;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jidd.basic.enums.JiddSymbolEnum;
import com.jidd.view.wechat.dto.JiddPubNoInfoDTO;
import com.jidd.view.wechat.dto.JiddPubNumTokenDTO;
import com.jidd.view.wechat.service.IJiddPubNumTokenCacheService;

@Component
public class JiddPubNoCacheManager {
	/** 日志 */
	private static Logger log = LoggerFactory.getLogger(JiddPubNoCacheManager.class);

	@Autowired
	private IJiddPubNumTokenCacheService jiddPubNumTokenCacheService;

	private LoadingCache<String, Optional<JiddPubNoInfoDTO>> jiddPubNoConfigCache;

	@PostConstruct
	public void init() {
		jiddPubNoConfigCache = CacheBuilder.newBuilder()
				.refreshAfterWrite(24, TimeUnit.HOURS)  //默认24个小时过期
				.build(new CacheLoader<String, Optional<JiddPubNoInfoDTO>>() {
					@Override
					public Optional<JiddPubNoInfoDTO> load(String pubId) throws Exception {
						JiddPubNoInfoDTO pubNoInfoDTO = getTestDto();
						//JiddPubNoInfoDTO PubNoInfoDTO = PubNoInfoService.queryPubNoInfoById(pubId);
						log.info("微信公众号信息：{}", pubNoInfoDTO);
						return Optional.fromNullable(pubNoInfoDTO);
					}
				});
	}
	private JiddPubNoInfoDTO getTestDto(){
		JiddPubNoInfoDTO dto = new JiddPubNoInfoDTO();
		dto.setPubId("gh_d8ca418ebb2b");
		dto.setAppId("wxb17ce3d03ed8073e");
		dto.setAppSecret("ea3b47e92bc17d958ea2f168c3f62dad");
		dto.setPubStatus("00");
		dto.setPubName("开发用");
		return dto;
	}
	private JiddPubNoInfoDTO getTestDto2(){
		JiddPubNoInfoDTO dto = new JiddPubNoInfoDTO();
		dto.setPubId("gh_51790c1ef5c3");
		dto.setAppId("wxc265b22e9ecff5cc");
		dto.setAppSecret("473a6a9b85a1e282d0623c71a91a6df5");
		dto.setPubStatus("00");
		dto.setPubName("开发用");
		return dto;
	}
	/**
	 * 从缓存中读取公众号信息
	 *
	 * @return
	 */
	public JiddPubNoInfoDTO getPubNoConfig(String pubId) {
		try {
			Optional<JiddPubNoInfoDTO> result = jiddPubNoConfigCache.getUnchecked(pubId);
			if (result.isPresent()) { // 如果不为null
				return result.get();
			}
		} catch (Exception e) {
			log.error("从缓存中获取微信公众号信息失败", e);
		}
		return null;
	}

	public String getAccessToken(String pubId) {
		JiddPubNoInfoDTO pubNoConfig = this.getPubNoConfig(pubId);
		if (pubNoConfig == null) {
			return JiddSymbolEnum.Blank.symbol();
		}
		return jiddPubNumTokenCacheService.getAccessToken(pubNoConfig.getPubId(), pubNoConfig.getAppId());
	}


	public void refreshToken(String pubId) {
		JiddPubNoInfoDTO pubNoConfig = this.getPubNoConfig(pubId);
		if (pubNoConfig == null) {
			return;
		}
		JiddPubNumTokenDTO tokenDTO = new JiddPubNumTokenDTO();
		tokenDTO.setPubNo(pubNoConfig.getPubId());
		tokenDTO.setAppId(pubNoConfig.getAppId());
		tokenDTO.setAppSecret(pubNoConfig.getAppSecret());
		tokenDTO.setAccessTokenUrl(WX_TOKEN_URI);
		tokenDTO.setMandatory(true); //强制刷新
		jiddPubNumTokenCacheService.refreshAccessToken(tokenDTO);
	}

	public void clearAllCache() {
		jiddPubNoConfigCache.invalidateAll();
	}

	public String getTicketByCache() {
		return ""; //暂不实现
	}

}