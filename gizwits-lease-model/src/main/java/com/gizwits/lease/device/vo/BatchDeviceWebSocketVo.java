package com.gizwits.lease.device.vo;

import com.gizwits.lease.product.dto.ProductDataPointDto;

import java.util.List;

/**
 * Created by GaGi on 2017/8/18.
 */
public class BatchDeviceWebSocketVo {
    private BatchDeviceDetailWebSocketVo batchDeviceDetailWebSocketVo;
    private String gizAppId;
    private String uid;
    private String token;
    private List<ProductDataPointDto> dataPoints;

    public BatchDeviceWebSocketVo() {
    }

    public BatchDeviceDetailWebSocketVo getBatchDeviceDetailWebSocketVo() {
        return batchDeviceDetailWebSocketVo;
    }

    public void setBatchDeviceDetailWebSocketVo(BatchDeviceDetailWebSocketVo batchDeviceDetailWebSocketVo) {
        this.batchDeviceDetailWebSocketVo = batchDeviceDetailWebSocketVo;
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

    public List<ProductDataPointDto> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<ProductDataPointDto> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
