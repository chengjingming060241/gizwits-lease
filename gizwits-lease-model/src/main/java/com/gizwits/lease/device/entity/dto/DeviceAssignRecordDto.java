package com.gizwits.lease.device.entity.dto;

import com.gizwits.boot.sys.entity.SysUser;

/**
 * @author lilh
 * @date 2017/8/2 16:11
 */
public class DeviceAssignRecordDto {

    private Integer sourceOperator;

    private Integer destinationOperator;

    private String mac;

    private String sno;

    private SysUser current;

    private Boolean isOperator;

    private Boolean isAgent;

    public Integer getSourceOperator() {
        return sourceOperator;
    }

    public void setSourceOperator(Integer sourceOperator) {
        this.sourceOperator = sourceOperator;
    }

    public Integer getDestinationOperator() {
        return destinationOperator;
    }

    public void setDestinationOperator(Integer destinationOperator) {
        this.destinationOperator = destinationOperator;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public SysUser getCurrent() {
        return current;
    }

    public void setCurrent(SysUser current) {
        this.current = current;
    }

    public Boolean getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(Boolean isOperator) {
        this.isOperator = isOperator;
    }

    public Boolean getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(Boolean isAgent) {
        this.isAgent = isAgent;
    }
}
