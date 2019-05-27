package com.gizwits.lease.device.vo;

import java.io.Serializable;

/**
 * Created by gagi on 2017/8/5.
 */
public class DeviceDetailWebSocketVo   implements Serializable {
    public DeviceDetailWebSocketVo() {
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

    public String getGizDid() {
        return gizDid;
    }

    public void setGizDid(String gizDid) {
        this.gizDid = gizDid;
    }

    public DeviceDetailWebSocketVo(String gizDid, String gizWsPort, String gizWssPort, String gizHost) {
        this.gizDid = gizDid;
        this.gizWsPort = gizWsPort;
        this.gizWssPort = gizWssPort;
        this.gizHost = gizHost;
    }

    private String gizDid;
    private String gizWsPort;
    private String gizWssPort;
    private String gizHost;

}
