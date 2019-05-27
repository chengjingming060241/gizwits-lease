package com.gizwits.lease.message.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 添加消息
 * Created by yinhui on 2017/8/8.
 */
public class SysMessageAdd implements Serializable {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发送角色
     */
    private List<RoleDto> roleDtoList;

    /**
     * 是否绑定微信 0 否 1 是
     */
    private int isBindWeChat;
    /**
     * 是否发送给系统用户 0 否 1 是
     */
    private int isSendToSysUser;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendtime;
    /**
     * 是否立即发送,0 定时发送 1 立即发送
     */
    private Integer isSendNow;

    public List<RoleDto> getRoleDtoList() {
        return roleDtoList;
    }

    public void setRoleDtoList(List<RoleDto> roleDtoList) {
        this.roleDtoList = roleDtoList;
    }

    public int getIsBindWeChat() {
        return isBindWeChat;
    }

    public void setIsBindWeChat(int isBindWeChat) {
        this.isBindWeChat = isBindWeChat;
    }

    public int getIsSendToSysUser() {
        return isSendToSysUser;
    }

    public void setIsSendToSysUser(int isSendToSysUser) {
        this.isSendToSysUser = isSendToSysUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getIsSendNow() {
        return isSendNow;
    }

    public void setIsSendNow(Integer isSendNow) {
        this.isSendNow = isSendNow;
    }
}
