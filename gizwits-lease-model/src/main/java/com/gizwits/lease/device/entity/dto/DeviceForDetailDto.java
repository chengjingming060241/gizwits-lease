package com.gizwits.lease.device.entity.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.vo.DevicePortDto;
import com.gizwits.lease.manager.dto.OperatorExtDto;
import com.gizwits.lease.product.dto.ProductCommandForDeviceDetailDto;
import com.gizwits.lease.product.dto.ProductServiceDetailForDeviceDto;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 设备详情
 *
 * @author lilh
 * @date 2017/7/21 11:36
 */
public class DeviceForDetailDto {

    private String sno;

    private String name;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    private String mac;

    private Integer workStatus;

    private String workStatusDesc;

    private Integer onlineStatus;

    private String onlineStatusDesc;

    private Integer productId;

    private String productName;

    private Date lastOnlineTime;

    private String belongOperatorName;
    /**
     * 投放点经度
     */
    private String longitude;
    /**
     * 投放点纬度
     */
    private String latitude;

    private String logoUrl;

    private String description;

    private String webSite;

    private String phone;

    private String productKey;

    private ProductServiceDetailForDeviceDto serviceMode;

    private DeviceLaunchAreaForDeviceDto launchArea;

    private OperatorExtDto operatorExt;

    /**
     * 是否可以更新投放点
     */
    private Boolean canModifyLaunchArea = false;
    /**
     * 是否可以更新收费模式
     */
    private Boolean canModifyServiceMode = false;
    /**
     * 是否可以修改设备
     */
    private Boolean canModify = false;

    /**
     * 设备到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;
    /**
     * 是否到期 true 即将到期 false未到期
     */
    private Boolean isTime = false;

    private String leaseType;

    /**
     * 控制指令
     */
    private List<ProductCommandForDeviceDetailDto> controlCommands = new ArrayList<>();

    /**
     * 出水口状态
     */
    private List<DevicePortDto> devicePortDtoList = new ArrayList<>();

    /**
     * 设备用水总量
     */
    private Double totalWater;

    private Double usedWater;

    private Double remainWater;

    private Integer operateStatus;

    public DeviceForDetailDto(Device device) {
        BeanUtils.copyProperties(device, this);
    }

    public OperatorExtDto getOperatorExt() {
        return operatorExt;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public void setOperatorExt(OperatorExtDto operatorExt) {
        this.operatorExt = operatorExt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getWorkStatusDesc() {
        return workStatusDesc;
    }

    public void setWorkStatusDesc(String workStatusDesc) {
        this.workStatusDesc = workStatusDesc;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getOnlineStatusDesc() {
        return onlineStatusDesc;
    }

    public void setOnlineStatusDesc(String onlineStatusDesc) {
        this.onlineStatusDesc = onlineStatusDesc;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getBelongOperatorName() {
        return belongOperatorName;
    }

    public void setBelongOperatorName(String belongOperatorName) {
        this.belongOperatorName = belongOperatorName;
    }

    public ProductServiceDetailForDeviceDto getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(ProductServiceDetailForDeviceDto serviceMode) {
        this.serviceMode = serviceMode;
    }

    public DeviceLaunchAreaForDeviceDto getLaunchArea() {
        return launchArea;
    }

    public void setLaunchArea(DeviceLaunchAreaForDeviceDto launchArea) {
        this.launchArea = launchArea;
    }

    public Boolean getCanModifyLaunchArea() {
        return canModifyLaunchArea;
    }

    public void setCanModifyLaunchArea(Boolean canModifyLaunchArea) {
        this.canModifyLaunchArea = canModifyLaunchArea;
    }

    public List<ProductCommandForDeviceDetailDto> getControlCommands() {
        return controlCommands;
    }

    public void setControlCommands(List<ProductCommandForDeviceDetailDto> controlCommands) {
        this.controlCommands = controlCommands;
    }


    public List<DevicePortDto> getDevicePortDtoList() {
        return devicePortDtoList;
    }

    public void setDevicePortDtoList(List<DevicePortDto> devicePortDtoList) {
        this.devicePortDtoList = devicePortDtoList;
    }

    public Boolean getCanModifyServiceMode() {
        return canModifyServiceMode;
    }

    public void setCanModifyServiceMode(Boolean canModifyServiceMode) {
        this.canModifyServiceMode = canModifyServiceMode;
    }

    public Boolean getCanModify() {
        return canModify;
    }

    public void setCanModify(Boolean canModify) {
        this.canModify = canModify;

    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Boolean getTime() {
        return isTime;
    }

    public void setTime(Boolean time) {
        isTime = time;
    }

    public String getLeaseType() { return leaseType; }

    public void setLeaseType(String leaseType) { this.leaseType = leaseType; }

    public Double getUsedWater() {
        return usedWater;
    }

    public void setUsedWater(Double usedWater) {
        this.usedWater = usedWater;
    }

    public Double getRemainWater() {
        return remainWater;
    }

    public void setRemainWater(Double remainWater) {
        this.remainWater = remainWater;
    }

    public Double getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(Double totalWater) {
        this.totalWater = totalWater;
    }

    public Integer getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Integer operateStatus) {
        this.operateStatus = operateStatus;
    }
}
