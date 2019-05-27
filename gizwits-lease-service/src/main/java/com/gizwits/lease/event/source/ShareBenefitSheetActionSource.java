package com.gizwits.lease.event.source;

/**
 * Source - 分润单操作源对象
 *
 * @author lilh
 * @date 2017/8/5 11:59
 */
public class ShareBenefitSheetActionSource {

    /** 分润单id */
    private Integer sheetId;

    /** 操作类型 */
    private Integer actionType;

    /** 操作人 */
    private Integer userId;

    public ShareBenefitSheetActionSource() {}

    public ShareBenefitSheetActionSource(Integer sheetId, Integer actionType, Integer userId) {
        this.sheetId = sheetId;
        this.actionType = actionType;
        this.userId = userId;
    }

    public Integer getSheetId() {
        return sheetId;
    }

    public void setSheetId(Integer sheetId) {
        this.sheetId = sheetId;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
