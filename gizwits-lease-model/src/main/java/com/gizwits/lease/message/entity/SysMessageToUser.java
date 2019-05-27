package com.gizwits.lease.message.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 系统消息用户表
 * </p>
 *
 * @author yinhui
 * @since 2017-08-23
 * @since 2017-08-08
 */
@TableName("sys_message_to_user")
public class SysMessageToUser extends Model<SysMessageToUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 消息系统id
     */
    @TableField("sys_message_id")
    private Integer sysMessageId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 角色id
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 是否已读
     */
    @TableField("is_read")
    private Integer isRead;
    /**
     * 是否发送
     */
    @TableField("is_send")
    private Integer isSend;

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

    public Integer getSysMessageId() {
        return sysMessageId;
    }

    public void setSysMessageId(Integer sysMessageId) {
        this.sysMessageId = sysMessageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {return roleId;}

    public void setRoleId(Integer roleId) {this.roleId = roleId;}

    public String getRoleName() {return roleName;}

    public void setRoleName(String roleName) {this.roleName = roleName;}

    public Integer getIsRead() {return isRead;}

    public void setIsRead(Integer isRead) {this.isRead = isRead;}

    public Integer getIsSend() {return isSend;}

    public void setIsSend(Integer isSend) {this.isSend = isSend;}

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
