package com.gizwits.lease.product.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

/**
 * 收费模式列表查询Dto
 * Created by yinhui on 2017/7/11.
 */
public class ProductServiceListQuerytDto implements Serializable{

    @Query(field = "name",operator = Query.Operator.like)
    private String modeName;

    @Query(field = "product_id")
    private Integer productId;

    @JsonIgnore
    @Query(field = "sys_user_id")
    private Integer creatorId;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    public String getModeName() {return modeName;}

    public void setModeName(String modeName) {this.modeName = modeName;}

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
