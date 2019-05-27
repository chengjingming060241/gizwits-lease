package com.gizwits.lease.device.vo;

import java.util.List;

/**
 * Created by GaGi on 2017/8/18.
 */
public class BatchDeviceDetailWebSocketVo {
    private List<String> gizDid;
    private String gizWsPort;
    private String gizWssPort;
    private String gizHost;

    public BatchDeviceDetailWebSocketVo(List<String> gizDid, String gizWsPort, String gizWssPort, String gizHost) {
        this.gizDid = gizDid;
        this.gizWsPort = gizWsPort;
        this.gizWssPort = gizWssPort;
        this.gizHost = gizHost;
    }

    public BatchDeviceDetailWebSocketVo() {

    }

    public List<String> getGizDid() {
        return gizDid;
    }

    public void setGizDid(List<String> gizDid) {
        this.gizDid = gizDid;
    }

    public String getGizWsPort() {
        return gizWsPort;
    }

    public void setGizWsPort(String gizWsPort) {
        this.gizWsPort = gizWsPort;
    }

    public String getGizWssPort() {
        return gizWssPort;
    }

    public void setGizWssPort(String gizWssPort) {
        this.gizWssPort = gizWssPort;
    }

    public String getGizHost() {
        return gizHost;
    }

    public void setGizHost(String gizHost) {
        this.gizHost = gizHost;
    }
}
