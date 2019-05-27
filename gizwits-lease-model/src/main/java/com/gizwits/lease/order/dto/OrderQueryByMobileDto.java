package com.gizwits.lease.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/8/11.
 */
public class OrderQueryByMobileDto implements Serializable{
    /**用户手机号码*/
    @NotBlank
    private String mobile;

    @JsonIgnore
    @Query(field = "user_id")
    private Integer userId;

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}
}
