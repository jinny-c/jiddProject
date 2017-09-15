package com.jidd.db.user.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jidd.db.user.mapper.JiddMerchRecommendInfoMapper;
import com.jidd.db.user.service.IJiddMgmtUmgService;

@Service(value = "jiddMgmtUmgService")
public class JiddMgmtUmgServiceImpl implements IJiddMgmtUmgService {
	private static Logger log = LoggerFactory.getLogger(JiddMgmtUmgServiceImpl.class);
    @Autowired
    private JiddMerchRecommendInfoMapper jiddMerchRecommendInfoMapper;
    
	@Override
	public Integer queryCount() {
		// TODO Auto-generated method stub
		log.info("queryCount start");
		jiddMerchRecommendInfoMapper.selectByPrimaryKey(1);
		return 0;
	}
	
}
