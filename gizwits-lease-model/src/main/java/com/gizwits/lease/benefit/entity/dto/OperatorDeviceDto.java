package com.gizwits.lease.benefit.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

/**
 * 查询运营商的设备Dto
 * Created by yinhui on 2017/8/5.
 */
public class OperatorDeviceDto implements Serializable{
    private Integer sysAccountId;
    private Integer ruleId;
    @JsonIgnore
    @Query(field = "owner_id",operator = Query.Operator.in)
    /**运营商id及其子运营商id*/
    private List<Integer> operators;

    @Query(field = "sno",operator = Query.Operator.notIn)
    private List<String> snos;

    @Query(field = "is_deleted")
    private Integer isDeleted = 0;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<String> getSnos() {
        return snos;
    }

    public void setSnos(List<String> snos) {
        this.snos = snos;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public List<Integer> getOperators() {return operators;}

    public void setOperators(List<Integer> operators) {this.operators = operators;}
}
