package com.gizwits.lease.user.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户移动记录（移入/出黑名单）
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@TableName("user_move_record")
public class UserMoveRecord extends Model<UserMoveRecord> {

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
     * 移入/出的用户
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 移动类型，1:移入黑名单 2:移出黑名单
     */
	@TableField("move_type")
	private Integer moveType;
    /**
     * 操作人员
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 操作人员名称
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMoveType() {
		return moveType;
	}

	public void setMoveType(Integer moveType) {
		this.moveType = moveType;
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
