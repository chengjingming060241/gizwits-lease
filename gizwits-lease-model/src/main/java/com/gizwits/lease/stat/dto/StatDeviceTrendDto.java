package com.gizwits.lease.stat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.utils.ParamUtil;

import java.util.Date;

/**
 * Created by GaGi on 2017/7/15.
 */
public class StatDeviceTrendDto {
    private Integer productId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fromDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date toDate;

    public Integer getProductId() {
        if (ParamUtil.isNullOrZero(this.productId)) {
            return null;
        }
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

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
}
