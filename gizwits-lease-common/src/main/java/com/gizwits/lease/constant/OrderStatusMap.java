package com.gizwits.lease.constant;


import java.util.*;

/**
 * Created by GaGi on 2017/7/31.
 */
public class OrderStatusMap extends HashMap<Integer, List<Integer>> {
    public OrderStatusMap() {
        Integer[] arrForInit = {OrderStatus.PAYING.getCode(), OrderStatus.EXPIRE.getCode()};
        Integer[] arrForPaying = {OrderStatus.PAYING.getCode(), OrderStatus.PAYED.getCode(), OrderStatus.FAIL.getCode(), OrderStatus.EXPIRE.getCode()};
        Integer[] arrForPayed = {OrderStatus.USING.getCode(), OrderStatus.REFUNDING.getCode(), OrderStatus.ABNORMAL.getCode()};
        Integer[] arrForUsing = {OrderStatus.FINISH.getCode(), OrderStatus.REFUNDING.getCode()};
        Integer[] arrForFinish = {OrderStatus.REFUNDING.getCode()};
        Integer[] arrForFail = {OrderStatus.PAYING.getCode()};
        Integer[] arrForRefuning = {OrderStatus.REFUNDED.getCode(), OrderStatus.REFUND_FAIL.getCode()};
        Integer[] arrForAbnormal = {OrderStatus.REFUNDING.getCode(), OrderStatus.FINISH.getCode()};
        Integer[] arrForRefundFail = {OrderStatus.REFUNDING.getCode()};
        this.put(OrderStatus.INIT.getCode(), new ArrayList<>(Arrays.asList(arrForInit)));
        this.put(OrderStatus.PAYING.getCode(), new ArrayList<>(Arrays.asList(arrForPaying)));
        this.put(OrderStatus.PAYED.getCode(), new ArrayList<>(Arrays.asList(arrForPayed)));
        this.put(OrderStatus.USING.getCode(), new ArrayList<>(Arrays.asList(arrForUsing)));
        this.put(OrderStatus.FAIL.getCode(), new ArrayList<>(Arrays.asList(arrForFail)));
        this.put(OrderStatus.FINISH.getCode(), new ArrayList<>(Arrays.asList(arrForFinish)));
        this.put(OrderStatus.REFUNDING.getCode(), new ArrayList<>(Arrays.asList(arrForRefuning)));
        this.put(OrderStatus.ABNORMAL.getCode(), new ArrayList<>(Arrays.asList(arrForAbnormal)));
        this.put(OrderStatus.REFUND_FAIL.getCode(), new ArrayList<>(Arrays.asList(arrForRefundFail)));
    }
}
