package com.gizwits.lease.user.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.enums.ChargeCardOperationType;
import com.gizwits.lease.user.entity.UserChargeCardOperationRecord;
import org.springframework.beans.BeanUtils;

/**
 * @author lilh
 * @date 2017/8/30 9:34
 */
public class UserChargeCardOperationListDto {

    private Integer id;

    private String cardNum;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    private String sysUserName;

    private String operateType;

    private String operateTypeDesc;

    private String ip;

    public UserChargeCardOperationListDto(UserChargeCardOperationRecord record) {
        BeanUtils.copyProperties(record, this);
        this.operateTypeDesc = ChargeCardOperationType.getDesc(this.getOperateType());
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

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateTypeDesc() {
        return operateTypeDesc;
    }

    public void setOperateTypeDesc(String operateTypeDesc) {
        this.operateTypeDesc = operateTypeDesc;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
