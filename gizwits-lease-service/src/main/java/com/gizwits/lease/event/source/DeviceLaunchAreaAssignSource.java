package com.gizwits.lease.event.source;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.device.entity.DeviceLaunchArea;

/**
 * @author lilh
 * @date 2017/9/2 16:48
 */
public class DeviceLaunchAreaAssignSource {

    private Integer launchAreaId;

    private String launchAreaName;

    private Integer sourceOwnerId;

    private Integer targetOwnerId;

    private SysUser current;

    public DeviceLaunchAreaAssignSource(DeviceLaunchArea deviceLaunchArea, SysUser current, Integer targetOwnerId) {
        this.launchAreaId = deviceLaunchArea.getId();
        this.launchAreaName = deviceLaunchArea.getName();
        this.sourceOwnerId = deviceLaunchArea.getOwnerId();
        this.current = current;
        this.targetOwnerId = targetOwnerId;
    }

    public Integer getLaunchAreaId() {
        return launchAreaId;
    }

    public void setLaunchAreaId(Integer launchAreaId) {
        this.launchAreaId = launchAreaId;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
    }

    public Integer getSourceOwnerId() {
        return sourceOwnerId;
    }

    public void setSourceOwnerId(Integer sourceOwnerId) {
        this.sourceOwnerId = sourceOwnerId;
    }

    public Integer getTargetOwnerId() {
        return targetOwnerId;
    }

    public void setTargetOwnerId(Integer targetOwnerId) {
        this.targetOwnerId = targetOwnerId;
    }

    public SysUser getCurrent() {
        return current;
    }

    public void setCurrent(SysUser current) {
        this.current = current;
    }
}
