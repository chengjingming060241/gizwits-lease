package com.gizwits.lease.constant;

public enum RechargeMoneyType {
	CONSTANT_MONEY(1, "固定充值金额"),
	CUSTOM_MONEY(2, "自定义充值金额"),
	;

	private Integer code;
	private String name;

	RechargeMoneyType(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
