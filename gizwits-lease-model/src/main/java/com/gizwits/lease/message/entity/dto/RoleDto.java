package com.gizwits.lease.message.entity.dto;

import java.io.Serializable;

/**
 * 角色Dto
 * Created by yinhui on 2017/8/8.
 */
public class RoleDto implements Serializable {

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
