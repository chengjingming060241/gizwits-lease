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
 * 设备订单看板数据统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
@TableName("stat_device_order_widget")
public class StatDeviceOrderWidget extends Model<StatDeviceOrderWidget> {

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
     * 修改时间
     */
    private Date utime;
    /**
     * 归属系统用户id
     */
    @TableField("sys_user_id")
    private Integer sysUserId;
    /**
     * 产品id
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 当前设备总数
     */
    @TableField("total_count")
    private Integer totalCount;
    /**
     * 今日设备新增数
     */
    @TableField("new_count")
    private Integer newCount;
    /**
     * 今天下单设备数
     */
    @TableField("ordered_count")
    private Integer orderedCount;
    /**
     * 设备订单率（今天下单设备数）/今天总
     */
    @TableField("ordered_percent")
    private Double orderedPercent;
    /**
     * 当前故障设备数
     */
    @TableField("alarm_count")
    private Integer alarmCount;
    /**
     * 当前警告设备数
     */
    @TableField("warn_count")
    private Integer warnCount;
    /**
     * 今日告警记录(故障+报警的记录)
     */
    @TableField("warn_record")
    private Integer warnRecord;
    /**
     * 当前设备故障率
     */
    @TableField("alarm_percent")
    private Double alarmPercent;
    /**
     * 今日订单数量
     */
    @TableField("order_count_today")
    private Integer orderCountToday;
    /**
     * 昨日订单数量
     */
    @TableField("order_count_yesterday")
    private Integer orderCountYesterday;

    @TableField("order_count_before_yesterday")
    private Integer orderCountBeforeYesterday;
    /**
     * 当前设备故障数
     */
    @TableField("order_new_percent_yesterday")
    private Double orderNewPercentYesterday;
    /**
     * 本月订单数
     */
    @TableField("order_count_month")
    private Integer orderCountMonth;

    /**
     * 今日分润单数量
     */
    @TableField("share_order_count")
    private Integer shareOrderCount;

    /**
     * 今日分润单分润金额
     */
    @TableField("share_order_money")
    private BigDecimal shareOrderMoney;

    public Integer getShareOrderCount() {
        return shareOrderCount;
    }

    public void setShareOrderCount(Integer shareOrderCount) {
        this.shareOrderCount = shareOrderCount;
    }

    public BigDecimal getShareOrderMoney() {
        return shareOrderMoney;
    }

    public void setShareOrderMoney(BigDecimal shareOrderMoney) {
        this.shareOrderMoney = shareOrderMoney;
    }

    public Integer getOrderCountBeforeYesterday() {
        return orderCountBeforeYesterday;
    }

    public void setOrderCountBeforeYesterday(Integer orderCountBeforeYesterday) {
        this.orderCountBeforeYesterday = orderCountBeforeYesterday;
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

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getOrderedCount() {return orderedCount;}

    public void setOrderedCount(Integer orderedCount) {this.orderedCount = orderedCount;}

    public Double getOrderedPercent() {
        return orderedPercent;
    }

    public void setOrderedPercent(Double orderedPercent) {
        this.orderedPercent = orderedPercent;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public Integer getWarnCount() {
        return warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public Integer getWarnRecord() {
        return warnRecord;
    }

    public void setWarnRecord(Integer warnRecord) {
        this.warnRecord = warnRecord;
    }

    public Double getAlarmPercent() {
        return alarmPercent;
    }

    public void setAlarmPercent(Double alarmPercent) {
        this.alarmPercent = alarmPercent;
    }

    public Integer getOrderCountToday() {
        return orderCountToday;
    }

    public void setOrderCountToday(Integer orderCountToday) {
        this.orderCountToday = orderCountToday;
    }

    public Integer getOrderCountYesterday() {
        return orderCountYesterday;
    }

    public void setOrderCountYesterday(Integer orderCountYesterday) {
        this.orderCountYesterday = orderCountYesterday;
    }

    public Double getOrderNewPercentYesterday() {
        return orderNewPercentYesterday;
    }

    public void setOrderNewPercentYesterday(Double orderNewPercentYesterday) {
        this.orderNewPercentYesterday = orderNewPercentYesterday;
    }

    public Integer getOrderCountMonth() {
        return orderCountMonth;
    }

    public void setOrderCountMonth(Integer orderCountMonth) {
        this.orderCountMonth = orderCountMonth;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
