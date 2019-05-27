package com.gizwits.lease.benefit.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

/**
 * Dto - 参与分润的订单查询
 *
 * @author lilh
 * @date 2017/8/5 9:58
 */
public class ShareBenefitSheetOrderForQueryDto {

    /** 分润单sheetNo */
    @JsonIgnore
    @Query(field = "sheet_no")
    private String sheetNo;

    /** 参与分润的订单的状态 */
    @JsonIgnore
    @Query(field = "status")
    private Integer status;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
