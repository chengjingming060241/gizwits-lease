package com.gizwits.lease.order.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhl
 * @since 2017-07-17
 */
@TableName("order_pay_record")
public class OrderPayRecord extends Model<OrderPayRecord> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 订单号
     */
	@TableField("order_no")
	private String orderNo;
	private Date ctime;
	private Date utime;
    /**
     * 支付类型:1,公众号支付;2,微信APP支付;3,支付宝支付;4,充值卡支付
     */
	@TableField("pay_type")
	private Integer payType;
    /**
     * 支付提交参数
     */
	private String params;
    /**
     * 订单状态,0:创建 1:支付中 2:支付完成 3:服务中 4:订单完成 5:订单失败
     */
	private Integer status;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
