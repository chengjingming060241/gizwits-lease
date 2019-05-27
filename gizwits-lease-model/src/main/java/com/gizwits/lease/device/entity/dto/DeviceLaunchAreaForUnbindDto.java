package com.gizwits.lease.device.entity.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author lilh
 * @date 2017/9/4 14:36
 */
public class DeviceLaunchAreaForUnbindDto {

    @NotEmpty
    private List<Integer> launchAreaIds;

    public List<Integer> getLaunchAreaIds() {
        return launchAreaIds;
    }

    public void setLaunchAreaIds(List<Integer> launchAreaIds) {
        this.launchAreaIds = launchAreaIds;
    }
}
