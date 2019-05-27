package com.gizwits.lease.common.perm;

import java.util.Collections;
import java.util.Set;

import com.gizwits.lease.enums.CommonRole;

/**
 * @author lilh
 * @date 2017/7/8 11:22
 */
public class SysUserRoleTypeHelper {

    private boolean isAdmin = false;

    private boolean isManufacturer = false;

    private boolean isAgent = false;

    private boolean isOperator = false;

    private boolean resolved = false;
    /** 用户所属的组织名称 */
    private String origizationName;

    private CommonRole commonRole;

    private Set<Integer> ids = Collections.emptySet();

    private Integer sysAccountId;


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        if (admin) {
            commonRole = CommonRole.MANAGER;
        }
    }

    public boolean isManufacturer() {
        return isManufacturer;
    }

    public void setManufacturer(boolean manufacturer) {
        isManufacturer = manufacturer;
        if (manufacturer) {
            commonRole = CommonRole.MANUFACTURER;
        }
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
        if (agent) {
            commonRole = CommonRole.AGENT;
        }
    }

    public boolean isOperator() {
        return isOperator;
    }

    public void setOperator(boolean operator) {
        isOperator = operator;
        if (operator) {
            commonRole = CommonRole.OPERATOR;
        }
    }

    public String getOrigizationName() {
        return origizationName;
    }

    public void setOrigizationName(String origizationName) {
        this.origizationName = origizationName;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public CommonRole getCommonRole() {
        return commonRole;
    }

    public void setCommonRole(CommonRole commonRole) {
        this.commonRole = commonRole;
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public void setIds(Set<Integer> ids) {
        this.ids = ids;
    }

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }
}
