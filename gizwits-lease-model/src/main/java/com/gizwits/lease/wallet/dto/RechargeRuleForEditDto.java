package com.gizwits.lease.wallet.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class RechargeRuleForEditDto {
	@NotNull
	private Integer projectId;

	@NotEmpty
	private List<RechargeMoneyDto> details;

	@NotNull
	private BigDecimal rate;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public List<RechargeMoneyDto> getDetails() {
		return details;
	}

	public void setDetails(List<RechargeMoneyDto> details) {
		this.details = details;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
}
