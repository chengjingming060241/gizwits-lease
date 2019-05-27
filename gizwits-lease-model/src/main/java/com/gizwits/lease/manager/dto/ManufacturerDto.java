package com.gizwits.lease.manager.dto;

import java.io.Serializable;

import com.gizwits.lease.manager.entity.Manufacturer;

/**
 * Dto - 厂商数据
 *
 * @author lilh
 * @date 2017/6/30 11:09
 */
public class ManufacturerDto implements Serializable {
    private static final long serialVersionUID = -8001711515777702061L;

    /**
     * 厂商id
     */
    private Integer id;

    /**
     * 厂商名称
     */
    private String name;

    public ManufacturerDto() {
    }

    public ManufacturerDto(Manufacturer manufacturer) {
        this.id = manufacturer.getId();
        this.name = manufacturer.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
