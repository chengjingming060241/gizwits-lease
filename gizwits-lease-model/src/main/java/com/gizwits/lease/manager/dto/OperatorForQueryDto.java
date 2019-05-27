package com.gizwits.lease.manager.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

/**
 * Dto - 运营商查询
 *
 * @author lilh
 * @date 2017/7/31 18:09
 */
public class OperatorForQueryDto {

    @Query(field = "name", operator = Query.Operator.like)
    private String name;

    /**
     * 省
     */
    @Query(field = "province")
    private String province;
    /**
     * 市
     */
    @Query(field = "city")
    private String city;
    /**
     * 区/县
     */
    @Query(field = "area")
    private String area;

    @Query(field = "status")
    private Integer status;

    @JsonIgnore
    @Query(field = "sys_user_id", operator = Query.Operator.in)
    private List<Integer> userIds;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    @JsonIgnore
    @Query(field = "status", operator = Query.Operator.in)
    private List<Integer> statuses;

    private Integer operatorAccountId;

    /** 创建者id */
    private Integer creatorId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Integer> statuses) {
        this.statuses = statuses;
    }

    public Integer getOperatorAccountId() {
        return operatorAccountId;
    }

    public void setOperatorAccountId(Integer operatorAccountId) {
        this.operatorAccountId = operatorAccountId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}
