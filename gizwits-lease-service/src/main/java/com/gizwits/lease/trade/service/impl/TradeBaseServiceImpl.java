package com.gizwits.lease.trade.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.constant.TradeStatus;
import com.gizwits.lease.constant.TradeStatusMap;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.trade.dao.TradeBaseDao;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.trade.service.TradeBaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-29
 */
@Service
public class TradeBaseServiceImpl extends ServiceImpl<TradeBaseDao, TradeBase> implements TradeBaseService {
    @Autowired
    private TradeBaseDao tradeBaseDao;


    public TradeBase createTradeBase(String orderNo, Double fee, String notifyUrl, Integer tradeType, Integer orderType) {
        //创建tradeBase和tradeWeixin
        TradeBase tradeBase = new TradeBase();
        tradeBase.setTradeNo(LeaseUtil.generateTradeNo(orderNo));
        tradeBase.setCtime(new Date());
        tradeBase.setOrderNo(orderNo);
        tradeBase.setOrderType(orderType);
        tradeBase.setNotifyUrl(notifyUrl);
        tradeBase.setTradeType(tradeType);
        //将交易单设置为创建状态
        tradeBase.setStatus(TradeStatus.INIT.getCode());
        //将订单中的金额转成以分为单位
        tradeBase.setTotalFee(fee);
        tradeBaseDao.insert(tradeBase);
        return tradeBase;
    }


    public TradeBase createTrade(String orderNo, Double fee, String notifyUrl, Integer tradeType, Integer orderType) {
        //查询是否存在tradeBase
        TradeBase tradeBase = selectOne(new EntityWrapper<TradeBase>().eq("order_no", orderNo));
        Date now = new Date();
        if (Objects.isNull(tradeBase)) {
            //创建tradeBase
            return createTradeBase(orderNo, fee, notifyUrl, tradeType, orderType);
        } else {
            // 当再次对同一个订单进行支付时需要创建新的交易单,同时取消原来的交易单
            tradeBase.setStatus(TradeStatus.EXPIRED.getCode());
            tradeBase.setUtime(now);
            updateById(tradeBase);
            return createTradeBase(orderNo, fee, notifyUrl, tradeType, orderType);
        }

    }

    @Override
    public Boolean updateTradeStatus(TradeBase tradeBase, Integer toStatus) {
        if (Objects.isNull(tradeBase) || ParamUtil.isNullOrZero(toStatus)) {
            LeaseException.throwSystemException(LeaseExceEnums.TRADE_NOT_EXIST);
        }
        TradeStatusMap tradeStatusMap = new TradeStatusMap();
        List<Integer> statusList = tradeStatusMap.get(tradeBase.getStatus());
        for (Integer status : statusList) {
            if (toStatus.equals(status)) {
                tradeBase.setStatus(toStatus);
                updateById(tradeBase);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean updateTradeStatus(String tradeNo, Integer toStatus) {
        if (StringUtils.isEmpty(tradeNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.TRADE_NOT_EXIST);
        }
        //根据tradeNo获取tradeBase
        TradeBase tradeBase = selectById(tradeNo);
        //调用自己的方法
        return updateTradeStatus(tradeBase, toStatus);
    }

    @Override
    public TradeBase selectByTradeNo(String tradeNo) {
        if (ParamUtil.isNullOrEmptyOrZero(tradeNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        TradeBase tradeBase = selectOne(new EntityWrapper<TradeBase>().eq("trade_no", tradeNo));
        return tradeBase;
    }
}
