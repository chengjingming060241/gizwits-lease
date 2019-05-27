package com.gizwits.lease.product.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/13.
 */
public class ProductIdAndNameDto implements Serializable{
    private Integer productId;
    private String productName;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
