package com.gizwits.lease.benefit.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 分润单详情
 *
 * @author lilh
 * @date 2017/8/3 16:56
 */
public class ShareBenefitSheetForDetailDto {

    /** id */
    private Integer id;

    /** 创建时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    /** 分润单号 */
    private String sheetNo;

    /** 运营商名称 */
    private String operatorName;

    /** 状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 账单金额 */
    private Double totalMoney;

    private Double shareMoney;

    /** 订单数 */
    private Integer orderCount;

    /** 支付类型 */
    private Integer payType;

    /** 支付类型 */
    private String payTypeDesc;

    /** 审核时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date auditTime;

    /** 支付账号 */
    private String payAccount;

    private String receiverOpenid;

    /**
     * 收款人真实姓名
     */
    private String receiverName;

    public ShareBenefitSheetForDetailDto(ShareBenefitSheet shareBenefitSheet) {
        BeanUtils.copyProperties(shareBenefitSheet, this);
    }

    public String getReceiverOpenid() {
        return receiverOpenid;
    }

    public void setReceiverOpenid(String receiverOpenid) {
        this.receiverOpenid = receiverOpenid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Double getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Double shareMoney) {
        this.shareMoney = shareMoney;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

}
