package com.gizwits.lease.order.dto;


import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by zhl on 2017/8/9.
 */
public class OrderTimerAddDto {
    @NotEmpty
    private String orderNo;
    @NotEmpty
    private String weekDay;
    @NotEmpty
    private String time;
    @NotEmpty
    private String command;

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
