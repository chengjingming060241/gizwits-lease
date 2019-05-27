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
 * @since 2017-07-14
 */
@TableName("order_status_flow")
public class OrderStatusFlow extends Model<OrderStatusFlow> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 订单号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 订单前置状态
     */
	@TableField("pre_status")
	private Integer preStatus;
    /**
     * 当前状态
     */
	@TableField("now_status")
	private Integer nowStatus;
	private Date ctime;
    /**
     * 操作者
     */
	private String creatorId;
    /**
     * 备注
     */
	private String remark;


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

	public Integer getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(Integer preStatus) {
		this.preStatus = preStatus;
	}

	public Integer getNowStatus() {
		return nowStatus;
	}

	public void setNowStatus(Integer nowStatus) {
		this.nowStatus = nowStatus;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
