package com.gizwits.lease.enums;

/**
 * Enum - 操作类型
 *
 * @author lilh
 * @date 2017/8/5 12:03
 */
public enum ShareBenefitSheetActionType {

    //操作类型：0，创建分润单；1，审核通过；2、重新审核；3，执行分润

    CREATE_BENEFIT_SHEET(0, "创建分润单"),
    AUDIT_PASS(1, "审核通过"),
    REAUDIT(2, "重新审核"),
    SHARING(3, "执行分润");

    private Integer code;

    private String desc;

    ShareBenefitSheetActionType(Integer code, String desc) {
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
