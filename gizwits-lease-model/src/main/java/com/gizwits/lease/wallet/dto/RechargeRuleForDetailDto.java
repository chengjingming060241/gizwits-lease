package com.gizwits.lease.wallet.dto;

import java.math.BigDecimal;
import java.util.List;

public class RechargeRuleForDetailDto {

	private List<RechargeMoneyDto> details;

	private BigDecimal rate;

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
