package com.gizwits.lease.product.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/15.
 */
public class ProductServiceDetailDto implements Serializable{
    private Integer serviceModeId;
    private  Integer productId;
    private String serviceType;
    private Integer sysUserId;

    public Integer getServiceModeId() {
        return serviceModeId;
    }

    public void setServiceModeId(Integer serviceModeId) {
        this.serviceModeId = serviceModeId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }
}
