package com.gizwits.lease.device.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 设备故障(警告)记录表
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
@TableName("device_alarm")
public class DeviceAlarm extends Model<DeviceAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 故障名称
     */
	private String name;
    /**
     * 故障参数
     */
	private String attr;
    /**
     * 故障发生时间
     */
	@TableField("happen_time")
	private Date happenTime;
    /**
     * 故障修复时间
     */
	@TableField("fixed_time")
	private Date fixedTime;
    /**
     * 故障状态,0:未修复 1:已修复
     */
	private Integer status;
    /**
     * MAC地址
     */
	private String mac;
    /**
     * 经度
     */
	private BigDecimal longitude;
    /**
     * 维度
     */
	private BigDecimal latitude;
    /**
     * 需要通知的人员ID
     */
	@TableField("notify_user_id")
	private Integer notifyUserId;
    /**
     * 对应设备序列号
     */
	private String sno;
	@TableField("product_key")
	private String productKey;

	/**
	 * 告警类型:0,报警;1,故障
     */
	@TableField("alarm_type")
	private Integer alarmType;

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
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

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public Date getFixedTime() {
		return fixedTime;
	}

	public void setFixedTime(Date fixedTime) {
		this.fixedTime = fixedTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
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

	public Integer getNotifyUserId() {
		return notifyUserId;
	}

	public void setNotifyUserId(Integer notifyUserId) {
		this.notifyUserId = notifyUserId;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
