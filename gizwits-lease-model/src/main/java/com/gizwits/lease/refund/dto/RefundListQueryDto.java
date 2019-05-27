package com.gizwits.lease.refund.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.base.Constants;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 退款申请表
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
public class RefundListQueryDto {
    @Query(field = "status", operator = Query.Operator.eq)
    private Integer status;

    @Query(field = "order_no", operator = Query.Operator.like)
    private String orderNo;

    @Query(field = "user_mobile", operator = Query.Operator.like)
    private String userMobile;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN)
    @Query(field = "ctime", operator = Query.Operator.ge)
    private Date startTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN)
    @Query(field = "ctime", operator = Query.Operator.le)
    private Date endTime;

    @JsonIgnore
    @Query(field = "sys_user_id", operator = Query.Operator.in)
    private List<Integer> sysUserIds;

    @JsonIgnore
    private List<String> refundNos;

    @ApiModelProperty("设备mac")
    private String mac;

    @ApiModelProperty("投放点")
    private String launchArea;

    @ApiModelProperty("用户昵称")
    private String nickname;


    @ApiModelProperty("设备sno")
    private String sno;

    @ApiModelProperty("分页数据大小")
    @JsonIgnore
    private Integer pageSize;

    @ApiModelProperty("开始条数")
    @JsonIgnore
    private Integer beginPage;

    @ApiModelProperty("归属人id")
    private Integer currentId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public List<Integer> getSysUserIds() {
        return sysUserIds;
    }

    public void setSysUserIds(List<Integer> sysUserIds) {
        this.sysUserIds = sysUserIds;
    }

    public List<String> getRefundNos() {
        return refundNos;
    }

    public void setRefundNos(List<String> refundNos) {
        this.refundNos = refundNos;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLaunchArea() {
        return launchArea;
    }

    public void setLaunchArea(String launchArea) {
        this.launchArea = launchArea;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getBeginPage() { return beginPage; }

    public void setBeginPage(Integer beginPage) { this.beginPage = beginPage; }

    public Integer getCurrentId() { return currentId; }

    public void setCurrentId(Integer currentId) { this.currentId = currentId; }
}
