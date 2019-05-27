package com.gizwits.lease.device.entity.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author lilh
 * @date 2017/9/2 17:36
 */
public class DeviceLaunchAreaForAssignDto {

    /** 1,投放点 2,运营商 3,代理商 */
    @NotNull
    private Integer assignDestinationType;

    /** 运营商或代理商或投放点id */
    @NotNull
    private Integer assignedId;

    /**
     * 强制进行分润操作
     */
    private Boolean forceAssign = false;

    /** 投放点id */
    @NotEmpty
    private List<Integer> launchAreaIds;

    public Integer getAssignDestinationType() {
        return assignDestinationType;
    }

    public void setAssignDestinationType(Integer assignDestinationType) {
        this.assignDestinationType = assignDestinationType;
    }

    public Integer getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(Integer assignedId) {
        this.assignedId = assignedId;
    }

    public Boolean getForceAssign() {
        return forceAssign;
    }

    public void setForceAssign(Boolean forceAssign) {
        this.forceAssign = forceAssign;
    }

    public List<Integer> getLaunchAreaIds() {
        return launchAreaIds;
    }

    public void setLaunchAreaIds(List<Integer> launchAreaIds) {
        this.launchAreaIds = launchAreaIds;
    }
}
