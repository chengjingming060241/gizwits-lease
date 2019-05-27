
package com.gizwits.lease.benefit.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yinhui on 2017/8/1.
 */
public class ShareBenefitRuleQueryDto implements Serializable{

    @Query(field = "share_benefit_rule_name",operator = Query.Operator.like)
    private String shareBenefitRuleName;

    @Query(field = "utime",operator = Query.Operator.ge)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date begin;

    @Query(field = "utime",operator = Query.Operator.le)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date end;


    @JsonIgnore
    @Query(field = "is_deleted")
    private Integer isDeleted;

    @JsonIgnore
    @Query(field = "sys_user_id")
    private Integer creatorId;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    @Query(field = "operator_name",operator = Query.Operator.like)
    private String operatorName;


    public String getShareBenefitRuleName() {return shareBenefitRuleName;}

    public void setShareBenefitRuleName(String shareBenefitRuleName) {this.shareBenefitRuleName = shareBenefitRuleName;}

    public Date getBegin() {return begin;}

    public void setBegin(Date begin) {this.begin = begin;}

    public Date getEnd() {return end;}

    public void setEnd(Date end) {this.end = end;}


    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getOperatorName() {return operatorName;}

    public void setOperatorName(String operatorName) {this.operatorName = operatorName;}

}
