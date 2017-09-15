package com.jidd.db.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jidd.db.common.bean.JiddPage;
import com.jidd.db.user.mapper.domain.JiddAgentApplyInfo;

public interface JiddAgentApplyInfoMapper {

	int insert(JiddAgentApplyInfo record);
	JiddAgentApplyInfo selectByPrimaryKey(Long applyId);
	int updateByPrimaryKeySelective(JiddAgentApplyInfo record);

	int statApplyCountByDay(
			@Param(value = "applyLoginUserId") Long applyLoginUserId,
			@Param(value = "applyDate") String applyDate);

	int isExsitOfNameMobileCert(@Param(value = "fullName") String fullName,
			@Param(value = "mobile") String mobile,
			@Param(value = "certNo") String certNo);
	List<JiddAgentApplyInfo> selectByPaging(
			@Param(value = "req") JiddAgentApplyInfo req,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "page") JiddPage page);
	int countRecordsByPaging(@Param(value = "req") JiddAgentApplyInfo req,
			@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate);
	
	List<JiddAgentApplyInfo> queryByApplyIds(@Param(value = "applyIds") List<Long> applyIds);
}