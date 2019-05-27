package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yinhui on 2017/7/25.
 */
public class DeviceLaunchAreaAssociatedOperatorDto implements Serializable {
    private List<Integer> deviceLaunchAreaIds;
    private Integer operatorAccountId;
    private String operatorName;

    public List<Integer> getDeviceLaunchAreaIds() {
        return deviceLaunchAreaIds;
    }

    public void setDeviceLaunchAreaIds(List<Integer> deviceLaunchAreaIds) {
        this.deviceLaunchAreaIds = deviceLaunchAreaIds;
    }

    public Integer getOperatorAccountId() {return operatorAccountId;}

    public void setOperatorAccountId(Integer operatorAccountId) {this.operatorAccountId = operatorAccountId;}

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
