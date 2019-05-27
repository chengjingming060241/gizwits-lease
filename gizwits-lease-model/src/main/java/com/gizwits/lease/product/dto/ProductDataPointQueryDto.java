package com.gizwits.lease.product.dto;

import com.gizwits.boot.annotation.Query;

import java.io.Serializable;

/**
 * 数据点分页查询dto
 * Created by yinhui on 2017/7/21.
 */
public class ProductDataPointQueryDto implements Serializable {
    @Query(field = "product_id")
    Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
