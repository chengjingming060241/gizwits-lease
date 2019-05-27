package com.gizwits.lease.common.perm.dto;

import com.gizwits.lease.enums.AssignDestinationType;

/**
 * @author lilh
 * @date 2017/8/24 11:35
 */
public class AssignDestinationDto {

    private Integer code;

    private String desc;

    public AssignDestinationDto(AssignDestinationType type) {
        this.code = type.getCode();
        this.desc = type.getDesc();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
