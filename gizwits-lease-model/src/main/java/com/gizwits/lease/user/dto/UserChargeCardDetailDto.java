package com.gizwits.lease.user.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.enums.ChargeCardStatus;
import com.gizwits.lease.user.entity.UserChargeCard;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 充值卡详情
 *
 * @author lilh
 * @date 2017/8/29 16:54
 */
public class UserChargeCardDetailDto {

    private Integer id;

    private String cardNum;

    private String userName;

    private String mobile;

    private Double money;

    private Integer status;

    private String statusDesc;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date bindCardTime;

    private Integer isBindWx;

    private String isBindWxDesc;

    public UserChargeCardDetailDto(UserChargeCard userChargeCard) {
        BeanUtils.copyProperties(userChargeCard, this);
        this.statusDesc = ChargeCardStatus.getDesc(userChargeCard.getStatus());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Date getBindCardTime() {
        return bindCardTime;
    }

    public void setBindCardTime(Date bindCardTime) {
        this.bindCardTime = bindCardTime;
    }

    public Integer getIsBindWx() {
        return isBindWx;
    }

    public void setIsBindWx(Integer isBindWx) {
        this.isBindWx = isBindWx;
    }

    public String getIsBindWxDesc() {
        return isBindWxDesc;
    }

    public void setIsBindWxDesc(String isBindWxDesc) {
        this.isBindWxDesc = isBindWxDesc;
    }
}
