package com.gizwits.lease.order.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/8/9.
 */
public class OrderTimerUpdateDto {
    @NotNull
    private Integer id;
    @NotNull
    private String orderNo;
    @NotNull
    private String weekDay;
    @NotNull
    private String time;
    @NotNull
    private String command;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
