package com.gizwits.lease.benefit.dto;

import java.util.List;

/**
 * Created by zhl on 2017/9/6.
 */
public class ShareBenefitCalculateDto {

    private String sheetNo;
    private List<Integer> excludeOrderList;

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public List<Integer> getExcludeOrderList() {
        return excludeOrderList;
    }

    public void setExcludeOrderList(List<Integer> excludeOrderList) {
        this.excludeOrderList = excludeOrderList;
    }
}
