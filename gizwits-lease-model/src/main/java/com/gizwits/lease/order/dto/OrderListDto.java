package com.gizwits.lease.order.dto;

import java.io.Serializable;

/**
 * 订单列表dto
 * Created by yinhui on 2017/7/20.
 */
public class OrderListDto implements Serializable{
   private OrderBaseListDto orderBaseListDto;
   private  String orderStatus;
   private String deviceName;
   private String service_mode_unit;

   private String payTypeDesc;

    private String abnormalReasonDesc;

    public String getAbnormalReasonDesc() {
        return abnormalReasonDesc;
    }

    public void setAbnormalReasonDesc(String abnormalReasonDesc) {
        this.abnormalReasonDesc = abnormalReasonDesc;
    }


    public OrderBaseListDto getOrderBaseListDto() {
        return orderBaseListDto;
    }

    public void setOrderBaseListDto(OrderBaseListDto orderBaseListDto) {
        this.orderBaseListDto = orderBaseListDto;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getService_mode_unit() {
        return service_mode_unit;
    }

    public void setService_mode_unit(String service_mode_unit) {
        this.service_mode_unit = service_mode_unit;
    }
}
