package com.gizwits.lease.device.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author zhl
 * @since 2017-07-11
 */
public class Device extends Model<Device> {

    private static final long serialVersionUID = 1L;

    /**
     * 设备序列号
     */
    @TableId("sno")
    private String sno;
    /**
     * 添加时间
     */
    private Date ctime;
    /**
     * 更新时间
     */
    private Date utime;
    /**
     * 设备名称
     */
    private String name;
    /**
     * MAC地址,通讯用
     */
    private String mac;
    /**
     * 设备工作状态,0:离线,1:空闲 2:使用中 3:故障中 4:禁用
     */
    @TableField("work_status")
    private Integer workStatus;

    /**
     * 设备在线状态
     */
    @TableField("online_status")
    private Integer onlineStatus;

    /**
     * 设备故障状态
     */
    @TableField("fault_status")
    private Integer faultStatus;

    /**
     * 设备状态,0:入库 1:出库 2:服务中 3:暂停服务 4:已返厂 5:已报废
     */
    private Integer status;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 维度
     */
    private BigDecimal latitude;
    /**
     * 最后上线时间
     */
    @TableField("last_online_time")
    private Date lastOnlineTime;
    /**
     * 运营商绑定的系统账号
     */
    @TableField("operator_id")
    private Integer operatorId;
    /**
     * 运营商绑定的系统账号名称
     */
    @TableField("operator_name")
    private String operatorName;
    /**
     * 投放点ID
     */
    @TableField("launch_area_id")
    private Integer launchAreaId;
    /**
     * 投放点名称
     */
    @TableField("launch_area_name")
    private String launchAreaName;
    /**
     * 所属产品
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 所属产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 收费模式ID
     */
    @TableField("service_id")
    private Integer serviceId;

    /**
     * 收费模式名称
     */
    @TableField("service_name")
    private String serviceName;
    /**
     * 删除标识，0：未删除，1：已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
    /**
     * 代理商ID
     */
    @TableField("agent_id")
    private Integer agentId;
    /**
     * 创建人
     */
    @TableField("sys_user_id")
    private Integer sysUserId;
    /**
     * 机智云设备did
     */
    @TableField("giz_did")
    private String gizDid;
    /**
     * 机智云设备passcode
     */

	@TableField("giz_pass_code")
	private String gizPassCode;
	/**
	 * 机智云websockt端口
	 */
	@TableField("giz_ws_port")
	private String gizWsPort;

	/**
	 * 拥有者
	 */
	@TableField("owner_id")
	private Integer ownerId;

	@TableField("giz_wss_port")
	private String gizWssPort;

	@TableField("giz_host")
	private String gizHost;

    /**
     * 微信ticket(为生成二维码所用)，当生成二维码方式为微信生成时，该字段不为空
     */
    @TableField("wx_ticket")
    private String wxTicket;
    /**
     * 微信绑定设备的id，用于找到该设备
     */
    @TableField("wx_did")
    private String wxDid;
    /**
     * 二维码路径，当生成二维码方式为WEB生成时，该字段不为空
     */
    @TableField("content_url")
    private String contentUrl;

    /**
     * 设备组id
     */
    @TableField("group_id")
    private Integer groupId;

    /**
     * 入库时间
     */
    @TableField("entry_time")
    private Date entryTime;

    /**
     * 出库时间
     */
    @TableField("shift_out_time")
    private Date shiftOutTime;

    /**
     * 设备到期时间
     */
    @TableField("expiration_time")
    private Date expirationTime;

    /**
     * 连续下单异常的次数
     */
    @TableField("abnormal_times")
    private Integer abnormalTimes;

    /**
     * 多次异常后锁定设备
     */
    @TableField("lock")
    private Boolean lock;

    /**
     * 设备用水总量
     */
    @TableField("total_water")
    private Double totalWater;

    /**
     * 剩余水量 根据数据点 a29heatingtemperature1最新值
     */
    @TableField("remain_water")
    private Double remainWater;

    @TableField("operate_status")
    private Integer operateStatus;

    public Integer getAbnormalTimes() {
        return abnormalTimes;
    }

    public void setAbnormalTimes(Integer abnormalTimes) {
        this.abnormalTimes = abnormalTimes;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getGizWsPort() {
		return gizWsPort;
	}

	public void setGizWsPort(String gizWsPort) {
		this.gizWsPort = gizWsPort;
	}

	public String getGizWssPort() {
		return gizWssPort;
	}

	public void setGizWssPort(String gizWssPort) {
		this.gizWssPort = gizWssPort;
	}

	public String getGizHost() {
		return gizHost;
	}

	public void setGizHost(String gizHost) {
		this.gizHost = gizHost;
	}


    public String getWxDid() {
        return wxDid;
    }

    public void setWxDid(String wxDid) {
        this.wxDid = wxDid;
    }

    public String getWxTicket() {
        return wxTicket;
    }

    public void setWxTicket(String wxTicket) {
        this.wxTicket = wxTicket;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getFaultStatus() {
        return faultStatus;
    }

    public void setFaultStatus(Integer faultStatus) {
        this.faultStatus = faultStatus;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getLaunchAreaId() {
        return launchAreaId;
    }

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

    public void setLaunchAreaId(Integer launchAreaId) {
        this.launchAreaId = launchAreaId;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getGizDid() {
        return gizDid;
    }

    public void setGizDid(String gizDid) {
        this.gizDid = gizDid;
    }

    public String getGizPassCode() {
        return gizPassCode;
    }

    public void setGizPassCode(String gizPassCode) {
        this.gizPassCode = gizPassCode;
    }


    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getShiftOutTime() {
        return shiftOutTime;
    }

    public void setShiftOutTime(Date shiftOutTime) {
        this.shiftOutTime = shiftOutTime;

    }

    public Date getExpirationTime() { return expirationTime; }

    public void setExpirationTime(Date expirationTime) { this.expirationTime = expirationTime; }

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

    public Double getRemainWater() {
        return remainWater;
    }

    public void setRemainWater(Double remainWater) {
        this.remainWater = remainWater;
    }

    @Override
    protected Serializable pkVal() {
        return this.sno;
    }

    @Override
    public String toString() {
        return "Device{" +
                "sno='" + sno + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", workStatus=" + workStatus +
                ", onlineStatus=" + onlineStatus +
                ", faultStatus=" + faultStatus +
                ", status=" + status +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", lastOnlineTime=" + lastOnlineTime +
                ", operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", launchAreaId=" + launchAreaId +
                ", launchAreaName='" + launchAreaName + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", isDeleted=" + isDeleted +
                ", agentId=" + agentId +
                ", sysUserId=" + sysUserId +
                ", gizDid='" + gizDid + '\'' +
                ", gizPassCode='" + gizPassCode + '\'' +
                ", gizWsPort='" + gizWsPort + '\'' +
                ", ownerId=" + ownerId +
                ", gizWssPort='" + gizWssPort + '\'' +
                ", gizHost='" + gizHost + '\'' +
                ", wxTicket='" + wxTicket + '\'' +
                ", wxDid='" + wxDid + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", groupId=" + groupId +
                ", entryTime=" + entryTime +
                ", shiftOutTime=" + shiftOutTime +
                ", expirationTime=" + expirationTime +
                ", abnormalTimes=" + abnormalTimes +
                ", lock=" + lock +
                ", totalWater=" + totalWater +
                ", remainWater=" + remainWater +
                ", operateStatus=" + operateStatus +
                '}';
    }
}
