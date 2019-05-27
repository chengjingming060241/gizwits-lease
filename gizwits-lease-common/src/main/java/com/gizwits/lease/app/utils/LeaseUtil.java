package com.gizwits.lease.app.utils;

import com.alibaba.druid.util.StringUtils;
import com.gizwits.boot.utils.IdGenerator;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;

import java.util.Objects;

/**
 * Created by GaGi on 2017/8/1.
 */
public class LeaseUtil extends IdGenerator {


    public static String generateOrderNo(Integer tradeOrderType) {
        int random4 = (int) (Math.random() * 9000.0D) + 1000;
        StringBuffer sb = (new StringBuffer(tradeOrderType + "0")).append(System.currentTimeMillis()).append(random4);
        return sb.toString();
    }

    /**
     * 生成orderNo规则如下
     * 前两位控制的订单生成的规则
     * x0:微信支付，x1:支付宝支付，
     * 1x:消费订单，2x:分润订单，3x:充值订单
     */
    public static String generateOrderNo(Integer payType, Integer tradeOrderType) {
        String rule = getRule(payType, tradeOrderType);
        int random4 = (int) (Math.random() * 9000.0D) + 1000;
        StringBuffer sb = (new StringBuffer(rule)).append(System.currentTimeMillis()).append(random4);
        return sb.toString();
    }

    private static String getRule(Integer payType, Integer tradeOrderType) {
        if (Objects.isNull(tradeOrderType) || Objects.isNull(payType)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(tradeOrderType);
        switch (payType) {
            case 1:
            case 2:
                sb.append("0");
                break;
            case 3:
                sb.append("1");
                break;
            case 4:
                sb.append("2");
                break;
            case 5:
                sb.append("3");
                break;
            default:
                sb.append("99");
                break;
        }
        return sb.toString();
    }

    public static Integer getOrderType(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        String sub = orderNo.substring(0, 1);
        switch (sub) {
            case "1":
                return TradeOrderType.CONSUME.getCode();
            case "2":
                return TradeOrderType.SHARE.getCode();
            case "3":
                return TradeOrderType.CHARGE.getCode();
            case "4":
                return TradeOrderType.CARD.getCode();
            default:
                return null;
        }
    }

    /**
     * 根据param获取wxId
     * @param param
     * @return
     */
    public static String judgeWxId(String param) {
        //使用wxId接受param，param可能含有$符
        String wxId = param;
        if (param.contains("__")) {
            int idx = param.indexOf("__");
            wxId = param.substring(0, idx);
        }
        return wxId;
    }

    /**
     * 获取pageType
     * @param param
     * @return
     */
    public static String judgePageType(String param) {
        //使用wxId接受param，param可能含有$符
        String type = null;
        if (param.contains("__")) {
            int idx = param.indexOf("__");
            type = param.substring(idx+2);
        }
        return type;
    }
}
