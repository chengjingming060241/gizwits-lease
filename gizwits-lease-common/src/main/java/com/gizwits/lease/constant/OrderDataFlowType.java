package com.gizwits.lease.constant;

public enum OrderDataFlowType {

	OLD_STATUS(1, "设备原状态"),
	SEND_CMD(2, "下发的指令"),
	USING(3, "设备使用中"),
	ABNORMAL(4, "订单异常"),
	FINISH(5, "设备结束使用"),
	OTHER(6, "设备其他上报"),;

	public static OrderDataFlowType get(Integer code) {
		for (OrderDataFlowType orderDataFlowRoute : values()) {
			if (orderDataFlowRoute.code.equals(code)) {
				return orderDataFlowRoute;
			}
		}
		return null;
	}

	OrderDataFlowType(Integer code, String msg) {
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
