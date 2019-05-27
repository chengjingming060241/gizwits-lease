package com.gizwits.lease.product.dto;

import com.gizwits.boot.sys.entity.SysUser;

/**
 * Dto - 厂商账号
 *
 * @author lilh
 * @date 2017/7/19 11:13
 */
public class ManufacturerUserDto {

    private Integer manufacturerAccountId;

    private String name;

    public ManufacturerUserDto(SysUser user) {
        this.manufacturerAccountId = user.getId();
        this.name = user.getUsername();
    }

    public Integer getManufacturerAccountId() {
        return manufacturerAccountId;
    }

    public void setManufacturerAccountId(Integer manufacturerAccountId) {
        this.manufacturerAccountId = manufacturerAccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
