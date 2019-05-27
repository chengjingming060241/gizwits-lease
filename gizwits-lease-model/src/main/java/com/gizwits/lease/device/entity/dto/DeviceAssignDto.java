package com.gizwits.lease.device.entity.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Description:设备分配收费模式，投放点Dto
 * User: yinhui
 * Date: 2018-01-05
 */
public class DeviceAssignDto implements Serializable{

    /**
     * 设备sno
     */
    @NotNull
    private List<String> sno ;
    /**
     * 分配的收费模式/投放点id
     */
    @NotNull
    private Integer assignId;
    /**
     * 1 收费模式 2投放点
     */
    @NotNull
    private Integer type;

    public List<String> getSno() {
        return sno;
    }

    public void setSno(List<String> sno) {
        this.sno = sno;
    }

    public Integer getAssignId() {
        return assignId;
    }

    public void setAssignId(Integer assignId) {
        this.assignId = assignId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
