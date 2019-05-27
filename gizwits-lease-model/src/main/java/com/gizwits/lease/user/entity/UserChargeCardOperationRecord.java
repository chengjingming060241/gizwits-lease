package com.gizwits.lease.user.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 充值卡操作记录(启用/禁用)
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@TableName("user_charge_card_operation_record")
public class UserChargeCardOperationRecord extends Model<UserChargeCardOperationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 充值卡号
     */
	@TableField("card_num")
	private String cardNum;
    /**
     * 操作类型,ENABLE和DISABLE
     */
	@TableField("operate_type")
	private String operateType;
    /**
     * 操作说明
     */
	private String remark;
    /**
     * 访问ip
     */
	private String ip;
    /**
     * 创建人
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 创建人名称
     */
	@TableField("sys_user_name")
	private String sysUserName;


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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
