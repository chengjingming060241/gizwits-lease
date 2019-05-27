package com.gizwits.lease.constant;

public enum OrderDataFlowRoute {

	SERVER_TO_DEVICE(1, "业务云到设备"),
	DEVICE_TO_SERVER(2, "设备到业务云"),;

	public static OrderDataFlowRoute get(Integer code) {
		for (OrderDataFlowRoute orderDataFlowRoute : values()) {
			if (orderDataFlowRoute.code.equals(code)) {
				return orderDataFlowRoute;
			}
		}
		return null;
	}

	OrderDataFlowRoute(Integer code, String msg) {
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
