package com.gizwits.lease.device.vo;

import com.gizwits.lease.product.dto.ProductDataPointDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gagi on 2017/8/5.
 */
public class DeviceWebSocketVo implements Serializable{

    private DeviceDetailWebSocketVo deviceDetailWebSocketVo;
    private String gizAppId;
    private String uid;
    private String token;
    private List<ProductDataPointDto> dataPoints;

    public DeviceWebSocketVo() {
    }

    public List<ProductDataPointDto> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<ProductDataPointDto> dataPoints) {

        this.dataPoints = dataPoints;
    }



    public DeviceDetailWebSocketVo getDeviceDetailWebSocketVo() {
        return deviceDetailWebSocketVo;
    }

    public void setDeviceDetailWebSocketVo(DeviceDetailWebSocketVo deviceDetailWebSocketVo) {
        this.deviceDetailWebSocketVo = deviceDetailWebSocketVo;
    }

    public String getGizAppId() {
        return gizAppId;
    }

    public void setGizAppId(String gizAppId) {
        this.gizAppId = gizAppId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

