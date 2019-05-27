package com.gizwits.lease.manager.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/8/30.
 */
public class MJSonOperatorDto  implements Serializable{
    /**
     * 运营商id
     */
    private Integer id;
    /**
     * 运营商名称
     */
    private String operatorName;
    /**
     * 拥有的设备数
     */
    private Integer deviceCount;

    /**
     * 拥有的投放点数
     * @return
     */
    private Integer devicelaunchAreaCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Integer getDevicelaunchAreaCount() {return devicelaunchAreaCount;}

    public void setDevicelaunchAreaCount(Integer devicelaunchAreaCount) {this.devicelaunchAreaCount = devicelaunchAreaCount;}
}
