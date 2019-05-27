package com.gizwits.lease.setting.dto;

import com.gizwits.lease.setting.entity.ChargeSetting;

/**
 * @author lilh
 * @date 2017/9/1 16:00
 */
public class ChargeSettingListDto {

    private Integer id;

    private Double money;

    public ChargeSettingListDto(ChargeSetting chargeSetting) {
        this.id = chargeSetting.getId();
        this.money = chargeSetting.getMoney();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
