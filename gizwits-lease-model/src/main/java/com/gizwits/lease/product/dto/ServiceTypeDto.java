package com.gizwits.lease.product.dto;

import java.io.Serializable;

/**
 *
 * Created by yinhui on 2017/7/12.
 */
public class ServiceTypeDto implements Serializable{
    private String serviceType;
    private String unit;

    public String getServiceType() {return serviceType;}

    public void setServiceType(String serviceType) {this.serviceType = serviceType;}

    public String getUnit() {return unit;}

    public void setUnit(String unit) {this.unit = unit;}
}
