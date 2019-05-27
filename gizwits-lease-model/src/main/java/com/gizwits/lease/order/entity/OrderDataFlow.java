package com.gizwits.lease.order.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单指令跟踪表
 * </p>
 *
 * @author Joke
 * @since 2018-02-11
 */
@TableName("order_data_flow")
public class OrderDataFlow extends Model<OrderDataFlow> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 对应的订单id
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 对应的设备sno
     */
	private String sno;
    /**
     * 对应的设备mac
     */
	private String mac;
    /**
     * 数据方向：1业务云到设备，2设备到业务云
     */
	private Integer route;
    /**
     * 类型：1设备原状态，2下发的指令，3设备使用中，4设备异常，5设备结束使用，6设备其他上报
     */
	private Integer type;
    /**
     * 数据内容
     */
	private String data;
    /**
     * 备注
     */
	private String remark;
    /**
     * 设备拥有者
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 创建时间
     */
	private Date ctime;


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

	public Integer getRoute() {
		return route;
	}

	public void setRoute(Integer route) {
		this.route = route;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
