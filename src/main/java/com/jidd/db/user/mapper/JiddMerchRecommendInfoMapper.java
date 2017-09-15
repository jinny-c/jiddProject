package com.jidd.db.user.mapper;

import com.jidd.db.user.mapper.domain.JiddMerchRecommendInfo;

public interface JiddMerchRecommendInfoMapper {
    int deleteByPrimaryKey(Integer loginUserId);

    int insert(JiddMerchRecommendInfo record);

    JiddMerchRecommendInfo selectByPrimaryKey(Integer loginUserId);

    int updateByPrimaryKeySelective(JiddMerchRecommendInfo record);

}