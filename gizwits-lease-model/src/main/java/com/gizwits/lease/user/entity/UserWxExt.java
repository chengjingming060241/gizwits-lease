package com.gizwits.lease.user.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 微信用户信息
 * </p>
 *
 * @author zhl
 * @since 2017-08-10
 */
@TableName("user_wx_ext")
public class UserWxExt extends Model<UserWxExt> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    /**
     * 对应user表的ID
     */
	@TableField("user_openid")
	private String userOpenid;
    /**
     * 微信用户ID
     */
	private String openid;
    /**
     * 微信公众号ID
     */
	@TableField("wx_id")
	private String wxId;
    /**
     * 微信用户所属运营用户ID
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
	private Date ctime;
	private Date utime;
    /**
     * 用户状态: 1,正常; 2,黑名单
     */
	private Integer status;
    /**
     * 移入黑名单时间
     */
	@TableField("move_in_black_time")
	private Date moveInBlackTime;
    /**
     * 移出黑名单时间
     */
	@TableField("move_out_black_time")
	private Date moveOutBlackTime;
    /**
     * 授权时间
     */
	@TableField("authorization_time")
	private Date authorizationTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserOpenid() {
		return userOpenid;
	}

	public void setUserOpenid(String userOpenid) {
		this.userOpenid = userOpenid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
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

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getMoveInBlackTime() {
		return moveInBlackTime;
	}

	public void setMoveInBlackTime(Date moveInBlackTime) {
		this.moveInBlackTime = moveInBlackTime;
	}

	public Date getMoveOutBlackTime() {
		return moveOutBlackTime;
	}

	public void setMoveOutBlackTime(Date moveOutBlackTime) {
		this.moveOutBlackTime = moveOutBlackTime;
	}

	public Date getAuthorizationTime() {
		return authorizationTime;
	}

	public void setAuthorizationTime(Date authorizationTime) {
		this.authorizationTime = authorizationTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
