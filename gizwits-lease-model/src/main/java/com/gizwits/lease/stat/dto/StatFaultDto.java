package com.gizwits.lease.stat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;

import java.util.Date;

/**
 * Created by GaGi on 2017/8/15.
 */
public class StatFaultDto {

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN,timezone = "GMT+8")
    private Date fromDate;
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN,timezone = "GMT+8")
    private Date toDate;
    private Integer productId;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
