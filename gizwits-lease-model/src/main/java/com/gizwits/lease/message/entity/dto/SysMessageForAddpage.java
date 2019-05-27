package com.gizwits.lease.message.entity.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.lease.message.entity.SysMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统消息添加页面所需数据
 * Created by yinhui on 2017/8/8.
 */
public class SysMessageForAddpage implements Serializable {
    /**
     * 所创建角色
     */
    private List<RoleDto> roleDtoList;

    /**
     * 是否绑定微信
     */
    private int isBindWeChat;

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
}
