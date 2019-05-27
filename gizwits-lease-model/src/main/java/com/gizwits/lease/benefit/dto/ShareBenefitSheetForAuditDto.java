package com.gizwits.lease.benefit.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Dto - 分润单审核
 *
 * @author lilh
 * @date 2017/8/5 9:36
 */
public class ShareBenefitSheetForAuditDto {

    /** 分润单id */
    private Integer sheetId;

    /** 批量审核的分润单 */
    private List<Integer> batchSheetIds;

    /** 需要审核的订单 */
    private List<Integer> sheetOrderIds;

    public List<Integer> getBatchSheetIds() {
        return batchSheetIds;
    }

    public void setBatchSheetIds(List<Integer> batchSheetIds) {
        this.batchSheetIds = batchSheetIds;
    }

    public Integer getSheetId() {
        return sheetId;
    }

    public void setSheetId(Integer sheetId) {
        this.sheetId = sheetId;
    }

    public List<Integer> getSheetOrderIds() {
        return sheetOrderIds;
    }

    public void setSheetOrderIds(List<Integer> sheetOrderIds) {
        this.sheetOrderIds = sheetOrderIds;
    }
}
