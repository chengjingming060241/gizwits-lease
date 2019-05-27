package com.gizwits.lease.constant;

public enum RefundStatus {
	APPLY(1,"待审核"),
	PASS(2,"已通过"),
	NO_PASS(3,"不通过"),
	REFUNDED(4,"已退款"),
	;

	public static RefundStatus get(Integer code) {
		for (RefundStatus refundStatus : values()) {
			if (refundStatus.getCode().equals(code)) {
				return refundStatus;
			}
		}
		return null;
	}

	RefundStatus(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Integer code;
	private String msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
