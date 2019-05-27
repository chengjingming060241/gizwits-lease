package com.gizwits.lease.order.dto;

import java.io.Serializable;

/**
 * @author Jin
 * @date 2019/2/14
 */
public class LastOrderForUserWalletChargeOrderDto implements Serializable {
    private String sno;
    private String launchAreaName;
    private String deviceName;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
