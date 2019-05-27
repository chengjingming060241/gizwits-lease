package com.gizwits.lease.device.entity.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Jin
 * @date 2019/2/14
 */
public class DeviceForChangeOperateStatusDto {

    @NotBlank
    private String sno;

    @Min(0)
    @Max(1)
    private Integer operateStatus;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Integer operateStatus) {
        this.operateStatus = operateStatus;
    }
}
