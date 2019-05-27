package com.gizwits.lease.user.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.base.Constants;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 充值卡充值记录查询
 *
 * @author lilh
 * @date 2017/8/29 20:43
 */
public class UserChargeCardOperationRecordQueryDto {

    @NotBlank
    @Query(field = "card_num")
    private String cardNum;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "ctime", operator = Query.Operator.ge)
    private Date startTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "ctime", operator = Query.Operator.le)
    private Date endTime;

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
}
