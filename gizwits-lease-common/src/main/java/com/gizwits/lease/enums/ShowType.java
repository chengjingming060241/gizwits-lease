package com.gizwits.lease.enums;

/**
 * Enum - 面板是否显示
 *
 * @author lilh
 * @date 2017/7/15 17:49
 */
public enum ShowType {

    HIDDEN(0, "隐藏"),
    DISPLAY(1, "显示");

    private Integer code;

    private String desc;

    ShowType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
