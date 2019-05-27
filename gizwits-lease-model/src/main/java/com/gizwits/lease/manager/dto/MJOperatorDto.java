package com.gizwits.lease.manager.dto;

import java.io.Serializable;

/**
 * 麻将机管理段运营商
 * Created by yinhui on 2017/8/30.
 */
public class MJOperatorDto implements Serializable {
    /**
     * 是否有分配权限
     */
    private Integer isAllot;

    public Integer getIsAllot() {
        return isAllot;
    }

    public void setIsAllot(Integer isAllot) {this.isAllot = isAllot;}
}
