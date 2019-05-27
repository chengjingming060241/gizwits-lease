package com.gizwits.lease.stat.vo;

/**
 * Created by GaGi on 2017/7/13.
 */
public class StatOrderAnalysisVo {

    /**
     * 时间
     */
    private String ctime;

    /**
     * 当天订单总数量
     */
    private Integer orderCount;

    /**
     * 当天订单总金额
     */
    private Double orderAmount;

    /**
     * 订单数量日增长率
     */
    private Double countPercent;

    /**
     * 订单金额日增长率
     */
    private Double amountPercent;

    private Double orderAmountWx;

    private Integer orderCountWx;

    private Double orderAmountWallet;

    private Integer orderCountWallet;

    public Double getCountPercent() {
        return countPercent;
    }

    public void setCountPercent(Double countPercent) {
        this.countPercent = countPercent;
    }

    public Double getAmountPercent() {
        return amountPercent;
    }

    public void setAmountPercent(Double amountPercent) {
        this.amountPercent = amountPercent;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getOrderAmountWx() {
        return orderAmountWx;
    }

    public void setOrderAmountWx(Double orderAmountWx) {
        this.orderAmountWx = orderAmountWx;
    }

    public Integer getOrderCountWx() {
        return orderCountWx;
    }

    public void setOrderCountWx(Integer orderCountWx) {
        this.orderCountWx = orderCountWx;
    }

    public Double getOrderAmountWallet() {
        return orderAmountWallet;
    }

    public void setOrderAmountWallet(Double orderAmountWallet) {
        this.orderAmountWallet = orderAmountWallet;
    }

    public Integer getOrderCountWallet() {
        return orderCountWallet;
    }

    public void setOrderCountWallet(Integer orderCountWallet) {
        this.orderCountWallet = orderCountWallet;
    }
}
