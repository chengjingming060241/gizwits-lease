package com.gizwits.lease.order.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/8/9.
 */
public class OrderTimerEnableDto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer isEnable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
