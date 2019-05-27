package com.gizwits.lease.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.base.Constants;

import java.util.Date;

/**
 * Created by xpg on 31/08/2017.
 */
public class UserChargeCardRechargeRecordQueryDto {
    @Query(field = "card_num")
    private String cardNum;

    private String openid;

    @Query(field = "user_id")
    private Integer userId;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "pay_time", operator = Query.Operator.ge)
    private Date startTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "pay_time", operator = Query.Operator.le)
    private Date endTime;

    @Query(field = "status")
    private Integer status;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
