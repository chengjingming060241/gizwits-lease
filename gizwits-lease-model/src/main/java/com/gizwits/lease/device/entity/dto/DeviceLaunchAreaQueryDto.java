package com.gizwits.lease.device.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

/**
 * 设备投放点查询dto
 * Created by yinhui on 2017/7/12.
 */
public class DeviceLaunchAreaQueryDto {

    @Query(field = "name", operator = Query.Operator.like)
    private String name;

    private Integer operatorAccountId;

    private Integer productId;

    /** 是否自已创建 */
    private Boolean selfOperating = true;

    @JsonIgnore
    @Query(field = "owner_id", operator = Query.Operator.in)
    private List<Integer> accessableUserIds;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    @JsonIgnore
    private Integer pagesize;

    @JsonIgnore
    private Integer begin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOperatorAccountId() {
        return operatorAccountId;
    }

    public Boolean getSelfOperating() {
        return selfOperating;
    }

    public void setSelfOperating(Boolean selfOperating) {
        this.selfOperating = selfOperating;
    }

    public void setOperatorAccountId(Integer operatorAccountId) {
        this.operatorAccountId = operatorAccountId;
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

    public Integer getPagesize() { return pagesize; }

    public void setPagesize(Integer pagesize) { this.pagesize = pagesize; }

    public Integer getBegin() { return begin; }

    public void setBegin(Integer begin) { this.begin = begin; }

}
