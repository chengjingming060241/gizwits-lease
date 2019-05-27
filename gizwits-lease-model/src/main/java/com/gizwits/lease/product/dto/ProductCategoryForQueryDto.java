package com.gizwits.lease.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

/**
 * Dto - 品类列表页查询
 *
 * @author lilh
 * @date 2017/7/18 11:35
 */
public class ProductCategoryForQueryDto {

    @Query(field = "name", operator = Query.Operator.like)
    private String name;

    @JsonIgnore
    @Query(field = "sys_user_id", operator = Query.Operator.in)
    private List<Integer> accessableUserIds;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getAccessableUserIds() {
        return accessableUserIds;
    }

    public void setAccessableUserIds(List<Integer> accessableUserIds) {
        this.accessableUserIds = accessableUserIds;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
