package com.gizwits.lease.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;

import java.util.Date;

/**
 * @author Jin
 * @date 2019/2/12
 */
public class DeviceRunningRecordForOfflineListDto {
    private Integer id;
    private String sno;
    private String mac;
    private String area;
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;
    private String owner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
