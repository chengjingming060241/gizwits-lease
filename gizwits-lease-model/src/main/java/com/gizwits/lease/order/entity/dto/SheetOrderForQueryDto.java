package com.gizwits.lease.order.entity.dto;

import com.gizwits.boot.annotation.Query;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author lilh
 * @date 2017/8/4 17:26
 */
public class SheetOrderForQueryDto {

    @NotBlank
    @Query(field = "sheet_no")
    private String sheetNo;

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }
}
