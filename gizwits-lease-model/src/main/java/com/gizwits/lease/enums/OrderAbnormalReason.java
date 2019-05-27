package com.gizwits.lease.enums;

public enum OrderAbnormalReason {
	STATUS_COMMAND_NULL(1, "产品没有配置状态数据点"),
	SEND_COMMAND_FAIL(2, "云端下发指令失败"),
	DEVICE_STATUS_WRONG(3, "设备上报状态有误"),
	DEVICE_STATUS_TIMEOUT(4, "设备没有上报");

	OrderAbnormalReason(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	public static OrderAbnormalReason get(Integer code) {
		for (OrderAbnormalReason orderAbnormalReason : values()) {
			if (orderAbnormalReason.code.equals(code)) {
				return orderAbnormalReason;
			}
		}
		return null;
	}

	private Integer code;
	private String description;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
