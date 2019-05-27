package com.gizwits.lease.user.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.OrderStatusMap;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.user.dao.UserChargeCardOrderDao;
import com.gizwits.lease.user.dto.*;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserChargeCard;
import com.gizwits.lease.user.entity.UserChargeCardOrder;
import com.gizwits.lease.user.service.UserChargeCardOperationRecordService;
import com.gizwits.lease.user.service.UserChargeCardOrderService;
import com.gizwits.lease.user.service.UserChargeCardService;
import com.gizwits.lease.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 充值卡充值订单 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@Service
public class UserChargeCardOrderServiceImpl extends ServiceImpl<UserChargeCardOrderDao, UserChargeCardOrder> implements UserChargeCardOrderService {
    @Autowired
    private UserChargeCardService userChargeCardService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserChargeCardOperationRecordService userChargeCardOperationRecordService;

    protected static Logger logger = LoggerFactory.getLogger(UserChargeCardOrder.class);

    @Override
    public Page<UserChargeCardChargeHistoryListDto> list(Pageable<UserChargeCardOrderQueryDto> pageable) {
        Page<UserChargeCardOrder> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<UserChargeCardOrder> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<UserChargeCardChargeHistoryListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(selectPage.getRecords().stream().map(item -> {
                UserChargeCardChargeHistoryListDto dto = new UserChargeCardChargeHistoryListDto(item);
                dto.setPayTypeDesc(PayType.getName(item.getPayType()));
                dto.setStatusDesc(OrderStatus.getMsg(item.getStatus()));
                return dto;
            }).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public UserChargeCardOrder createChargeCardOrder(ChargeCardOrderDto cardOrderDto) {
        //判断卡号是否存在
        UserChargeCard card = userChargeCardService.selectOne(new EntityWrapper<UserChargeCard>()
                .eq("card_num", cardOrderDto.getCardNum()));
        if (ParamUtil.isNullOrEmptyOrZero(card)) {
            LeaseException.throwSystemException(LeaseExceEnums.CARD_NOT_EXIST);
        }
        User user = userService.getUserByIdOrOpenidOrMobile(cardOrderDto.getOpenid());
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        Date now = new Date();
        UserChargeCardOrder userChargeCardOrder = new UserChargeCardOrder();
        userChargeCardOrder.setOrderNo(LeaseUtil.generateOrderNo(TradeOrderType.CARD.getCode()));
        userChargeCardOrder.setCtime(now);
        userChargeCardOrder.setUtime(now);
        userChargeCardOrder.setCardNum(card.getCardNum());
        userChargeCardOrder.setMoney(cardOrderDto.getFee());
        userChargeCardOrder.setUserId(user.getId());
        userChargeCardOrder.setUsername(user.getUsername());
        userChargeCardOrder.setStatus(OrderStatus.INIT.getCode());
        userChargeCardOrder.setPayType(0); // FIXME: pay_type NOT NULL
        insert(userChargeCardOrder);
        return userChargeCardOrder;
    }

    @Override
    public void updateCardOrderStatus(String orderNo, Integer toStatus) {
        if (StringUtils.isEmpty(orderNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        UserChargeCardOrder chargeOrder = selectById(orderNo);
        updateCardOrderStatus(chargeOrder, toStatus);
    }

    @Override
    public void updateCardOrderStatus(UserChargeCardOrder userChargeCardOrder, Integer toStatus) {
        if (Objects.isNull(userChargeCardOrder) || Objects.isNull(toStatus)) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        Boolean flag = false;
        OrderStatusMap map = new OrderStatusMap();
        List<Integer> statusList = map.get(userChargeCardOrder.getStatus());
        for (Integer status : statusList) {
            if (status.equals(toStatus)) {
                logger.info("订单:" + userChargeCardOrder.getOrderNo() + "从" + OrderStatus.getOrderStatus(userChargeCardOrder.getStatus()) + "修改成" + OrderStatus.getOrderStatus(status));
                userChargeCardOrder.setStatus(toStatus);
                updateById(userChargeCardOrder);
                flag = true;
                break;
            }
        }
        if (!flag) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_CHANGE_STATUS_FAIL);
        }
    }

    @Override
    public boolean checkAndUpdateCardOrder(String orderNo, Double totalFee, String tardeNo) {
        if (ParamUtil.isNullOrEmptyOrZero(totalFee) ||
                ParamUtil.isNullOrEmptyOrZero(orderNo))
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        UserChargeCardOrder cardOrder = selectById(orderNo);
        if (Objects.isNull(cardOrder)) {
            logger.error("====>>>>> 订单orderNo[" + cardOrder.getOrderNo() + "]在系统中未找到");
            return false;
        }

        //检查订单的状态是否是未支付
        if (cardOrder.getPayTime() != null || OrderStatus.PAYED.getCode().equals(cardOrder.getStatus())) {
            logger.warn("====>>>> 订单tradeNo[" + tardeNo + "]的状态为已支付,本次支付回调不做处理");
            return false;
        }

        //检查订单金额是否一致,注:微信回调中的金额单位是分,需要转换为元
        if (!totalFee.equals(cardOrder.getMoney())) {
            logger.error("====>>>>> 订单tradeNo[" + tardeNo + "]的金额为[" + cardOrder.getMoney() + "]与支付回调的金额[" + totalFee + "]的金额不匹配,本次支付回调不做处理");
            return false;
        }
        Date now = new Date();
        cardOrder.setPayTime(now);
        cardOrder.setPayType(PayType.WEIXINPAY.getCode());
        updateCardOrderStatus(cardOrder, OrderStatus.PAYED.getCode());
        //充值
        UserChargeCard card = userChargeCardService.selectOne(new EntityWrapper<UserChargeCard>().eq("card_num", cardOrder.getCardNum()));
        if (Objects.isNull(card)) {
            LeaseException.throwSystemException(LeaseExceEnums.CARD_NOT_EXIST);
        }
        Double money = cardOrder.getMoney() + card.getMoney();
        BigDecimal moneyForDecimal = new BigDecimal(money);
        card.setMoney(moneyForDecimal.doubleValue());
        userChargeCardService.updateById(card);

        return true;
    }

    @Override
    public Page<UserChargeCardRechargeRecordDto> getRechargeRecord(Pageable<UserChargeCardRechargeRecordQueryDto> pageable) {
        Page<UserChargeCardOrder> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);

        if (pageable.getQuery() == null) {
            pageable.setQuery(new UserChargeCardRechargeRecordQueryDto());
        }
        User user = userService.getUserByIdOrOpenidOrMobile(pageable.getQuery().getOpenid());
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        } else {
            pageable.getQuery().setUserId(user.getId());
        }
        pageable.getQuery().setStatus(OrderStatus.PAYED.getCode());

        page.setOrderByField("pay_time");
        page.setAsc(false);

        Wrapper wrapper = QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>());
        Page<UserChargeCardOrder> selectPage = selectPage(page, wrapper);
        Page<UserChargeCardRechargeRecordDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        result.setOrderByField(null);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(selectPage.getRecords().stream().map(UserChargeCardRechargeRecordDto::new).collect(Collectors.toList()));
        }
        return result;
    }
}
