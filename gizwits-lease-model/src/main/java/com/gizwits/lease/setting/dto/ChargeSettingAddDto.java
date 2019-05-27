package com.gizwits.lease.setting.dto;

import javax.validation.constraints.NotNull;

/**
 * Dto - 添加充值设置
 *
 * @author lilh
 * @date 2017/9/1 15:32
 */
public class ChargeSettingAddDto {

    //@Pattern(regexp = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$")
    @NotNull
    private Double money;

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
