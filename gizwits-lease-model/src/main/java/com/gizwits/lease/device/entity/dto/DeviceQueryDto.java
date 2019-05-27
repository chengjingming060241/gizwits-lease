package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.enums.DeleteStatus;

import java.util.Date;
import java.util.List;


/**
 * 设备分页查询dto
 * Created by yinhui on 2017/7/19.
 */
public class DeviceQueryDto {

    @Query(field = "product_id")
    private Integer productId;

    @Query(field = "sno", operator = Query.Operator.like)
    private String sno;

    @Query(field = "mac", operator = Query.Operator.like)
    private String mac;

    @Query(field = "name", operator = Query.Operator.like)
    private String deviceName;

    @Query(field = "launch_area_name", operator = Query.Operator.like)
    private String deviceLaunchArea;

    private Double usedWater;

    private String sign;

    @Query(field = "launch_area_id")
    private String deviceLaunchAreaId;

    @Query(field = "status")
    private Integer status;

    @Query(field = "online_status")
    private Integer onlineStatus;

    @Query(field = "work_status")
    private Integer workStatus;

    @JsonIgnore
    @Query(field = "owner_id", operator = Query.Operator.in)
    private List<Integer> accessableOwnerIds;

    /**
     * 设备组为空
     */
    @JsonIgnore
    @Query(field = "group_id", operator = Query.Operator.isNull, condition = "-1")
    private Integer deviceGroupId;

    @JsonIgnore
    @Query(field = "is_deleted")
    private Integer isDeleted = DeleteStatus.NOT_DELETED.getCode();


    @JsonIgnore
    @Query(field = "sno", operator = Query.Operator.in)
    private List<String> ids;

    /**
     * 排除
     */
    @JsonIgnore
    @Query(field = "sno", operator = Query.Operator.notIn)
    private List<String> excludeIds;

    /**
     * 排除待入库的数据
     */
    @JsonIgnore
    @Query(field = "status", operator = Query.Operator.ne)
    private Integer excludeStatus = 6;


    /**
     * 直接被分配的运营商绑定的账号
     */
    private Integer operatorAccountId;

    /**
     * 创建人
     */
    private Integer creatorId;
    /**
     * 当前用户设备是否分配投放点 1未分配 2已分配
     */
    private Integer type;
    /**
     * 设备到期时间开始
     */
    @Query(field = "expiration_time",operator = Query.Operator.ge)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTimeBegin;

    @Query(field = "expiration_time",operator = Query.Operator.le)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    /**设备到期时间结束*/
    private Date expirationTimeEnd;

    @JsonIgnore
    @Query(field = "lock")
    private Boolean lock;

    /**
     * 0->跳转剩余10%水量设备
     */
    private Integer isRemain10Device;

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceLaunchArea() {
        return deviceLaunchArea;
    }

    public void setDeviceLaunchArea(String deviceLaunchArea) {
        this.deviceLaunchArea = deviceLaunchArea;
    }

    public String getDeviceLaunchAreaId() {
        return deviceLaunchAreaId;
    }

    public void setDeviceLaunchAreaId(String deviceLaunchAreaId) {
        this.deviceLaunchAreaId = deviceLaunchAreaId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getOperatorAccountId() {
        return operatorAccountId;
    }

    public void setOperatorAccountId(Integer operatorAccountId) {
        this.operatorAccountId = operatorAccountId;
    }

    public List<Integer> getAccessableOwnerIds() {
        return accessableOwnerIds;
    }

    public void setAccessableOwnerIds(List<Integer> accessableOwnerIds) {
        this.accessableOwnerIds = accessableOwnerIds;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Integer getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(Integer deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }


    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(List<String> excludeIds) {
        this.excludeIds = excludeIds;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getExcludeStatus() {
        return excludeStatus;
    }

    public void setExcludeStatus(Integer excludeStatus) {
        this.excludeStatus = excludeStatus;

    }

    public Double getUsedWater() {
        return usedWater;
    }

    public void setUsedWater(Double usedWater) {
        this.usedWater = usedWater;
    }

    public Date getExpirationTimeBegin() {
        return expirationTimeBegin;
    }

    public void setExpirationTimeBegin(Date expirationTimeBegin) {
        this.expirationTimeBegin = expirationTimeBegin;
    }

    public Date getExpirationTimeEnd() {
        return expirationTimeEnd;
    }

    public void setExpirationTimeEnd(Date expirationTimeEnd) {
        this.expirationTimeEnd = expirationTimeEnd;
    }

    public Integer getIsRemain10Device() {
        return isRemain10Device;
    }

    public void setIsRemain10Device(Integer isRemain10Device) {
        this.isRemain10Device = isRemain10Device;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
