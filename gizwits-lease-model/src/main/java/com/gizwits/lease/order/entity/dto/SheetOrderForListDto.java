package com.gizwits.lease.order.entity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.order.entity.OrderBase;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 分润订单列表
 *
 * @author lilh
 * @date 2017/8/4 10:46
 */
public class SheetOrderForListDto {

    private Integer id;
    /** 分润单sheetNo*/
    private String sheetNo;

    /** 订单号 */
    private String orderNo;

    /** 用户昵称 */
    private String userName;

    /** 投放点名称 */
    private String deviceLaunchAreaName;

    /** 设备mac */
    private String mac;

    /** 支付费用 */
    private Double amount;

    /** 支付时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date payTime;

    /** 支付方式 */
    private Integer payType;

    /** 支付方式描述 */
    private String payTypeDesc;

    /** 收费名称 */
    private String serviceModeName;

    /** 状态 */
    private Integer orderStatus;

    /** 状态描述 */
    private String statusDesc;

    /** 前台展示用，是否应该选中 */
    private Boolean selected;

    /** 订单在分润单中的分润比例 **/
    private Double sharePercentage;

    public SheetOrderForListDto() {}

    public SheetOrderForListDto(OrderBase orderBase) {
        BeanUtils.copyProperties(orderBase, this);
    }

    public Double getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(Double sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceLaunchAreaName() {
        return deviceLaunchAreaName;
    }

    public void setDeviceLaunchAreaName(String deviceLaunchAreaName) {
        this.deviceLaunchAreaName = deviceLaunchAreaName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public String getServiceModeName() {
        return serviceModeName;
    }

    public void setServiceModeName(String serviceModeName) {
        this.serviceModeName = serviceModeName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
