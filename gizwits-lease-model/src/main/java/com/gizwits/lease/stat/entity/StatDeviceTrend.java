package com.gizwits.lease.stat.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 设备趋势统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@TableName("stat_device_trend")
public class StatDeviceTrend extends Model<StatDeviceTrend> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 添加时间
     */
    private Date ctime;
    /**
     * 归属系统用户id
     */
    @TableField("sys_user_id")
    private Integer sysUserId;
    /**
     * 新增上线数量
     */
    @TableField("new_count")
    private Integer newCount;

    /**
     * 设备订单数
     */
    @TableField("ordered_count")
    private Integer orderedCount;
    /**
     * 设备订单数设备订单率(当天含有订单的设备数/当天设备总数)
     */
    @TableField("ordered_percent")
    private BigDecimal orderedPercent;

    @TableField("product_id")
    private Integer productId;

    @TableField("active_count")
    private Integer activeCount;

    @TableField("previous_deivce_total")
    private Integer previousDeviceTotal;

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getOrderedCount() {
        return orderedCount;
    }

    public void setOrderedCount(Integer orderedCount) {
        this.orderedCount = orderedCount;
    }

    public BigDecimal getOrderedPercent() {
        return orderedPercent;
    }

    public void setOrderedPercent(BigDecimal orderedPercent) {
        this.orderedPercent = orderedPercent;
    }

    public Integer getPreviousDeviceTotal() {return previousDeviceTotal;}

    public void setPreviousDeviceTotal(Integer previousDeviceTotal) {this.previousDeviceTotal = previousDeviceTotal;}

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
