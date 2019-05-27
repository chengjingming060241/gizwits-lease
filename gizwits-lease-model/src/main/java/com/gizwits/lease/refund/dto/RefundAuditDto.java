package com.gizwits.lease.refund.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 退款申请表
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
public class RefundAuditDto {
	@NotEmpty
	private List<String> refundNos;
	@NotNull
	private Boolean audit;
	private String auditReason;

	public List<String> getRefundNos() {
		return refundNos;
	}

	public void setRefundNos(List<String> refundNos) {
		this.refundNos = refundNos;
	}

	public Boolean getAudit() {
		return audit;
	}

	public void setAudit(Boolean audit) {
		this.audit = audit;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
}
