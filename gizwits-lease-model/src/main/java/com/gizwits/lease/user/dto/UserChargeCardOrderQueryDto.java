package com.gizwits.lease.user.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.base.Constants;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 充值卡充值记录查询
 *
 * @author lilh
 * @date 2017/8/29 20:43
 */
public class UserChargeCardOrderQueryDto {

    @Query(field = "card_num" ,operator = Query.Operator.like)
    private String cardNum;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "ctime", operator = Query.Operator.ge)
    private Date startTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "ctime", operator = Query.Operator.le)
    private Date endTime;

    @Query(field = "username",operator = Query.Operator.like)
    private String nickName;

    @Query(field = "user_id")
    private Integer userId;

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

    public String getNickName() {return nickName;}

    public void setNickName(String nickName) {this.nickName = nickName;}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
