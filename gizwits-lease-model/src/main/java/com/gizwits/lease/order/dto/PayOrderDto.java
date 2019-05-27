package com.gizwits.lease.order.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * Created by GaGi on 2017/7/31.
 */
public class PayOrderDto {
    @NotNull
    private String openid;
    @NotNull
    private String sno;

    private Integer productServiceDetailId;
    @Range(min=0, max=9999,message="不在范围之内")
    private Double quantity;


    /**出水口*/
    private Integer port;

    private String mobile;



    /** 数据点标识名 */
    private String name;

    /** 值 */
    private Object value;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getProductServiceDetailId() {
        return productServiceDetailId;
    }


    public void setProductServiceDetailId(Integer productServiceDetailId) {this.productServiceDetailId = productServiceDetailId;}

    public Integer getPort() {return port;}

    public void setPort(Integer port) {this.port = port;}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
