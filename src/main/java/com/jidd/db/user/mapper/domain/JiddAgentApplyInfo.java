package com.jidd.db.user.mapper.domain;

import java.io.Serializable;
import java.util.Date;

public class JiddAgentApplyInfo implements Serializable {
	
	private static final long serialVersionUID = 3769552542822291995L;

	private Long applyId;

	private Long applyLoginUserid;
	private String fullName;

	private String mobile;

	private String certNo;

	private String province;

	private String applyDate;
	private Date createTime;
	private Date uptTime;

	public JiddAgentApplyInfo(Long applyId, Long applyLoginUserid,
			String fullName, String mobile, String certNo, String province,
			String applyDate, Date createTime, Date uptTime) {
		this.applyId = applyId;
		this.applyLoginUserid = applyLoginUserid;
		this.fullName = fullName;
		this.mobile = mobile;
		this.certNo = certNo;
		this.province = province;
		this.applyDate = applyDate;
		this.createTime = createTime;
		this.uptTime = uptTime;
	}

	public JiddAgentApplyInfo() {
		super();
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public Long getApplyLoginUserId() {
		return applyLoginUserid;
	}

	public void setApplyLoginUserId(Long applyLoginUserid) {
		this.applyLoginUserid = applyLoginUserid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName == null ? null : fullName.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo == null ? null : certNo.trim();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate == null ? null : applyDate.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUptTime() {
		return uptTime;
	}
	public void setUptTime(Date uptTime) {
		this.uptTime = uptTime;
	}
}
