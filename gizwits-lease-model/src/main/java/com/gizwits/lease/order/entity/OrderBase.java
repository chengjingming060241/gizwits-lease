package com.gizwits.lease.order.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@TableName("order_base")
public class OrderBase extends Model<OrderBase> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,订单号,按照一定规则生成
     */
    @TableId("order_no")
	private String orderNo;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 对应设备序列号
     */
	private String sno;
    /**
     * 对应设备MAC
     */
	private String mac;
    /**
     * 发货指令
     */
	private String command;
    /**
     * 服务方式ID
     */
	@TableField("service_mode_id")
	private Integer serviceModeId;
    /**
     * 服务方名称
     */
	@TableField("service_mode_name")
	private String serviceModeName;

	/**
	 * 具体的服务方式ID
     */
	@TableField("service_mode_detail_id")
	private Integer serviceModeDetailId;
    /**
     * 订单状态,0:创建 1:支付中 2:支付完成 3:服务中 4:订单完成 5:订单失败
     */
	@TableField("order_status")
	private Integer orderStatus;
    /**
     * 订单支付时间
     */
	@TableField("pay_time")
	private Date payTime;

	/**
	 * 支付类型:1,公众号支付;2,微信APP支付;3,支付宝支付;4,充值卡支付
     */
	@TableField("pay_type")
	private Integer payType;
    /**
     * 订单总价
     */
	private Double amount;

    /**
     * 用户ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 用户姓名
     */
	@TableField("user_name")
	private String userName;

	/**
	 * 订单所属的微信运营配置中的sys_user_id
     */
	@TableField("sys_user_id")
	private Integer sysUserId;

	/**
	 * 是否删除
	 */
	@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted = 0;


	/**
	 * 归档
	 */
	@TableField("is_archive")
	private Integer isArchive;


	/**
	 * 支付方式为充值卡支付时的卡号
	 */
	@TableField("promotion_money")
	private Double promotionMoney;




	@TableField("pay_card_num")
	private String payCardNum;

	/**
	 * 退款结果
	 */
	@TableField("refund_result")
	private String refundResult;

	/**
	 * 异常原因
	 */
	@TableField("abnormal_reason")
	private Integer abnormalReason;

	/**
	 * 投放点id
	 */
	@TableField("launch_area_id")
	private Integer launchAreaId;

	/**
	 * 投放点名称
	 */
	@TableField("launch_area_name")
	private String launchAreaName;

	/**
	 * 取水量
	 */
	@TableField("intake_water")
	private Double intakeWater;

	/**
	 * 该订单用户此时实际充值金额
	 */
	@TableField("real_recharge")
	private Double realRecharge;

	/**
	 * 该订单用户此时实际赠送金额
	 */
	@TableField("real_discount")
	private Double realDiscount;

	public Integer getLaunchAreaId() {
		return launchAreaId;
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

	public Integer getAbnormalReason() {
		return abnormalReason;
	}

	public void setAbnormalReason(Integer abnormalReason) {
		this.abnormalReason = abnormalReason;
	}

	public Integer getServiceModeDetailId() {
		return serviceModeDetailId;
	}

	public void setServiceModeDetailId(Integer serviceModeDetailId) {
		this.serviceModeDetailId = serviceModeDetailId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Integer getServiceModeId() {
		return serviceModeId;
	}

	public void setServiceModeId(Integer serviceModeId) {
		this.serviceModeId = serviceModeId;
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

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsDeleted() {return isDeleted;}

	public void setIsDeleted(Integer isDeleted) {this.isDeleted = isDeleted;}


	public Integer getIsArchive() {
		return isArchive;
	}

	public void setIsArchive(Integer isArchive) {
		this.isArchive = isArchive;
	}

	public Double getPromotionMoney() {return promotionMoney;}

	public void setPromotionMoney(Double promotionMoney) {this.promotionMoney = promotionMoney;}

	public String getPayCardNum() {
		return payCardNum;
	}

	public void setPayCardNum(String payCardNum) {
		this.payCardNum = payCardNum;
	}

	public String getRefundResult() { return refundResult; }

	public void setRefundResult(String refundResult) { this.refundResult = refundResult; }

	public Double getIntakeWater() {
		return intakeWater;
	}

	public void setIntakeWater(Double intakeWater) {
		this.intakeWater = intakeWater;
	}

	public Double getRealRecharge() {
		return realRecharge;
	}

	public void setRealRecharge(Double realRecharge) {
		this.realRecharge = realRecharge;
	}

	public Double getRealDiscount() {
		return realDiscount;
	}

	public void setRealDiscount(Double realDiscount) {
		this.realDiscount = realDiscount;
	}

	@Override
	protected Serializable pkVal() {
		return this.orderNo;
	}

}
