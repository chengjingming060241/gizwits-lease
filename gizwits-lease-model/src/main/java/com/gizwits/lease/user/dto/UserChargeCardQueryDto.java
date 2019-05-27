package com.gizwits.lease.user.dto;

import com.gizwits.boot.annotation.Query;

/**
 * @author lilh
 * @date 2017/8/29 15:46
 */
public class UserChargeCardQueryDto {

    /** 充值卡号 */
    @Query(field = "card_num", operator = Query.Operator.like)
    private String cardNum;

    /** 用户名 */
    @Query(field = "user_name", operator = Query.Operator.like)
    private String username;

    /** 手机号 */
    @Query(field = "mobile", operator = Query.Operator.like)
    private String mobile;

    /** 状态 */
    @Query(field = "status")
    private Integer status;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
