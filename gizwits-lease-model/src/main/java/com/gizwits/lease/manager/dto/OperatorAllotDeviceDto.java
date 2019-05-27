package com.gizwits.lease.manager.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 设备分配给运营商dto
 * Created by yinhui on 2017/8/30.
 */
public class OperatorAllotDeviceDto implements Serializable {
    /**设备sno*/
    private List<String> sno;
    /**运营商id／代理商id*/
    private Integer ownerId;

    public List<String> getSno() {return sno;}

    public void setSno(List<String> sno) {this.sno = sno;}

    public Integer getOwnerId() {return ownerId;}

    public void setOwnerId(Integer ownerId) {this.ownerId = ownerId;}
}
