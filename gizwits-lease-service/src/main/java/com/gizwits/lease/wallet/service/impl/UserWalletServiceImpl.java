package com.gizwits.lease.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.*;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.operator.service.OperatorExtService;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderExtPortService;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.trade.service.TradeBaseService;
import com.gizwits.lease.trade.service.TradeWeixinService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.wallet.dao.UserWalletDao;
import com.gizwits.lease.wallet.dto.RechargeDto;
import com.gizwits.lease.wallet.dto.UserWalletDto;
import com.gizwits.lease.wallet.dto.WalletPayDto;
import com.gizwits.lease.wallet.entity.UserWallet;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.RechargeMoneyService;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;
import com.gizwits.lease.wallet.service.UserWalletService;
import com.gizwits.lease.wallet.service.UserWalletUseRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * UserWalletServiceImpl.
 * <p>
 * 用户钱包表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-28
 */
@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletDao, UserWallet> implements UserWalletService {
    protected final static Logger logger = LoggerFactory.getLogger("PAY_LOGGER");

    @Autowired
    private UserService userService;

    @Autowired
    private UserWalletUseRecordService userWalletUseRecordService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private OrderBaseDao orderBaseDao;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorExtService operatorExtService;

    @Autowired
    private RechargeMoneyService rechargeMoneyService;

    @Autowired
    private TradeBaseService tradeBaseService;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    @Autowired
    private TradeWeixinService tradeWeixinService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OrderExtPortService orderExtPortService;

    public UserWallet createWallet(UserWalletDto userWalletDto) {
        User user = userService.getUserByIdOrOpenidOrMobile(userWalletDto.getMobile());
        if (ParamUtil.isNullOrEmptyOrZero(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        String username = user.getUsername();
        UserWallet userWallet = new UserWallet();
        userWallet.setCtime(new Date());
        userWallet.setUsername(username);
        userWallet.setWalletType(userWalletDto.getWalletType());
        String wallet = WalletEnum.getWalletEnumm(userWalletDto.getWalletType()).getName();
        userWallet.setWalletName(wallet);
        userWallet.setMoney(userWalletDto.getFee());
        userWallet.setUserId(user.getId());
        insert(userWallet);
        return userWallet;
    }

    /**
     * 页面查询余额(余额+赠送金额）
     */
    @Override
    public UserWallet selectUserWallet(UserWalletDto userWalletDto) {
        User user = userService.getUserByIdOrOpenidOrMobile(userWalletDto.getOpenid());
        if (ParamUtil.isNullOrEmptyOrZero(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        logger.info("查询钱包：用户id=" + user.getId());
        UserWallet userWallet = selectUserWallet(user.getId(), WalletEnum.BALENCE.getCode());
        UserWallet userWallet1 = selectUserWallet(user.getId(), WalletEnum.DISCOUNT.getCode());
        BigDecimal money = new BigDecimal(userWallet.getMoney() + userWallet1.getMoney());
        money = money.setScale(2, BigDecimal.ROUND_HALF_UP);
        userWallet.setMoney(money.doubleValue());
        logger.info("页面查询余额（余额+赠送）：" + money.doubleValue());
        return userWallet;
    }

    @Override
    public UserWallet selectUserWallet(Integer userId, Integer walletType) {
        if (Objects.isNull(userId) || Objects.isNull(walletType)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        logger.info("查询钱包：用户id=" + userId + "，钱包类型=" + walletType);
        EntityWrapper<UserWallet> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", userId).eq("wallet_type", walletType);
        UserWallet userWallet = selectOne(entityWrapper);
        if (Objects.isNull(userWallet)) {
            userWallet = create(userId, walletType);
        }
        return userWallet;
    }

    @Override
    public UserWallet updateMoney(Integer userId, Double fee, Double discountMoney, Integer operatorType, String tradeNo) {
        logger.info("钱包操作：类型=" + operatorType + ",金额=" + fee + "，赠送金额=" + discountMoney + "，userId =" + userId);
        logger.info("交易号=" + tradeNo);
        TradeBase tradeBase = tradeBaseService.selectByTradeNo(tradeNo);

        if (Objects.isNull(tradeBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        if (Objects.equals(UserWalletUseEnum.RECHARGE.getCode(), operatorType)) {
            UserWalletChargeOrder chargeOrder = userWalletChargeOrderService.selectById(tradeBase.getOrderNo());

            if (Objects.isNull(chargeOrder)) {
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }

            // 押金单
            if (chargeOrder.getWalletType().equals(WalletEnum.DEPOSIT.getCode())) {
                UserWallet userWallet = create(userId, WalletEnum.DEPOSIT.getCode(), chargeOrder.getFee());
                return userWallet;
            }
        }

        // 充值或消费单
        UserWallet userWallet = selectUserWallet(userId, WalletEnum.BALENCE.getCode());
        UserWallet userWallet1 = selectUserWallet(userId, WalletEnum.DISCOUNT.getCode());
        boolean flag = false;
        boolean flag1 = false;
        // 支付订单优惠金额
        // Double promotionMoney = 0.0;
        String orderNo = tradeBaseService.selectByTradeNo(tradeNo).getOrderNo();
        OrderBase orderBase = orderBaseService.selectById(orderNo);

        if (Objects.equals(UserWalletUseEnum.RECHARGE.getCode(), operatorType)) {

            // 充值订单
            userWallet.setMoney(userWallet.getMoney() + fee);
            userWallet.setUtime(new Date());
            userWallet1.setMoney(userWallet1.getMoney() + discountMoney);
            userWallet1.setUtime(new Date());
            flag = userWalletUseRecordService.insertUserWalletUseRecord(userWallet, fee, operatorType, tradeNo);
            flag1 = userWalletUseRecordService.insertUserWalletUseRecord(userWallet1, 0.0, operatorType, tradeNo);

        } else if (Objects.equals(UserWalletUseEnum.PAY.getCode(), operatorType)) {

            // 该订单用户此时实际赠送金额
            // orderBase.getRealDiscount();
            // 该订单用户此时实际充值金额
            // orderBase.getRealRecharge();
            // 比列
            Double rate = orderBase.getRealRecharge() / (orderBase.getRealDiscount() + orderBase.getRealRecharge());
            Double real = new BigDecimal(fee * rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Double discount = new BigDecimal(fee * (1 - rate)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            userWallet1.setMoney(userWallet1.getMoney() - discount);
            userWallet.setMoney(userWallet.getMoney() - real);
            flag1 = userWalletUseRecordService.insertUserWalletUseRecord(userWallet1, discount, operatorType, tradeNo);
            flag = userWalletUseRecordService.insertUserWalletUseRecord(userWallet, real, operatorType, tradeNo);
            // promotionMoney = discount;

            if (discount > 0) {
                orderBase.setPayType(PayType.BALANCE_DISCOUNT.getCode());
            } else {
                orderBase.setPayType(PayType.BALANCE.getCode());
            }

            orderBase.setPromotionMoney(real);
            logger.info("订单类型--->{}", orderBase.getPayType());
            orderBaseService.updateById(orderBase);

            // 支付订单 优先使用优惠金额
            // Double surpls = fee - userWallet1.getMoney();
            // if (surpls <= 0) {
            //
            //     // 优惠金额足以支付
            //     userWallet1.setMoney(userWallet1.getMoney() - fee);
            //     flag1 = userWalletUseRecordService.insertUserWalletUseRecord(userWallet1, fee, operatorType, tradeNo);
            //     flag = userWalletUseRecordService.insertUserWalletUseRecord(userWallet, 0.0, operatorType, tradeNo);
            //
            //     // 订单优惠金额
            //     promotionMoney = fee;
            //
            //     // 设置订单支付类型
            //     orderBase.setPayType(PayType.DISCOUNT.getCode());
            // } else {
            //
            //     // 优惠金额和余额一起支付 扣除优惠金额 一部分余额
            //     Double momey = userWallet1.getMoney();
            //     userWallet1.setMoney(0.0);
            //
            //     flag1 = userWalletUseRecordService.insertUserWalletUseRecord(userWallet1, momey, operatorType, tradeNo);
            //     userWallet.setMoney(userWallet.getMoney() - surpls);
            //     flag = userWalletUseRecordService.insertUserWalletUseRecord(userWallet, surpls, operatorType, tradeNo);
            //
            //     // 订单优惠金额
            //     promotionMoney = momey;
            //
            //     // 设置订单支付类型 此种类型有种情况优惠金额为0则全部由余额支付
            //     if (momey.equals(0.0)) {
            //         orderBase.setPayType(PayType.BALANCE.getCode());
            //     } else {
            //         orderBase.setPayType(PayType.BALANCE_DISCOUNT.getCode());
            //     }
            // }
        }

        if (updateById(userWallet) && updateById(userWallet1) && flag && flag1) {
            if (Objects.nonNull(userWallet1)) {
                userWallet.setMoney(userWallet.getMoney() + userWallet1.getMoney());
            } else {
                userWallet.setMoney(userWallet.getMoney());
            }

            // 更新订单优惠金额
            // if (Objects.nonNull(orderBase)) {
            //     logger.info("orderBase====" + orderBase.toString());
            //     logger.info("promotionMoney====" + promotionMoney);
            //     orderBase.setPromotionMoney(promotionMoney);
            //     orderBaseService.updateById(orderBase);
            // }

            return userWallet;
        } else {
            LeaseException.throwSystemException(LeaseExceEnums.WALLET_OPERATE_FAIL);
        }
        return userWallet;
    }

    @Override
    public UserWallet create(Integer userId, Integer walletType) {
        UserWallet userWallet = new UserWallet();
        userWallet.setCtime(new Date());
        userWallet.setUtime(new Date());
        User user = userService.getUserByIdOrOpenidOrMobile(userId + "");
        if (!ParamUtil.isNullOrEmptyOrZero(user)) {
            userWallet.setUsername(user.getNickname());
            userWallet.setUserId(userId);
        }

        userWallet.setWalletType(walletType);
        String wallet = WalletEnum.getWalletEnumm(walletType).getName();
        userWallet.setWalletName(wallet);
        userWallet.setMoney(0.00);
        insert(userWallet);
        return userWallet;
    }

    private UserWallet create(Integer userId, Integer walletType, Double money) {
        UserWallet userWallet = create(userId, walletType);
        userWallet.setMoney(money);
        updateById(userWallet);
        return userWallet;
    }

    @Override
    public void pay(WalletPayDto data) {
        logger.info("钱包支付：mobile=" + data.getMobile() + "orderNo=" + data.getOrderNo());
        User user = userService.getUserByIdOrOpenidOrMobile(data.getMobile());
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        //判断该用户是否有使用中的订单
        synchronized (data) {
            List<OrderBase> unfinishOrderList = orderBaseService.findByUserIdAndStatus(user.getId(), OrderStatus.USING.getCode());
            if (unfinishOrderList != null && unfinishOrderList.size() > 0) {
                LeaseException.throwSystemException(LeaseExceEnums.HAS_UNFINISH_ORDER);
            }
        }
        Integer userId = user.getId();
        //根据openid和orderNo获取订单信息
        UserWallet userWallet = selectUserWallet(userId, WalletEnum.BALENCE.getCode());
        UserWallet userWallet1 = selectUserWallet(userId, WalletEnum.DISCOUNT.getCode());
        OrderBase orderBase = orderBaseService.getOrderBaseByOrderNo(data.getOrderNo());
        if (Objects.isNull(orderBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        //1.钱包里面的钱要比订单的钱要多才能支付
        if (orderBase.getAmount().compareTo(userWallet.getMoney() + userWallet1.getMoney()) > 0) {
            LeaseException.throwSystemException(LeaseExceEnums.BALANCE_NOT_ENOUGH);
        }
        OrderExtPort orderExtPort = orderExtPortService.selectOne(new EntityWrapper<OrderExtPort>().eq("order_no", orderBase.getOrderNo()));

        if (ParamUtil.isNullOrEmptyOrZero(orderExtPort)) {
            //判断设备是否可用租赁
            deviceService.checkDeviceIsRenting(orderBase.getSno(), 0);
        } else {
            Integer port = orderExtPort.getPort();
            //判断设备是否可用租赁
            deviceService.checkDeviceIsRenting(orderBase.getSno(), port);
        }


        //兼容支付中的订单
        if (orderBase.getOrderStatus().equals(OrderStatus.PAYING.getCode())) {
            orderBase.setOrderStatus(OrderStatus.INIT.getCode());
        }
        //创建tradeBase
        TradeBase tradeBase = tradeBaseService.createTrade(orderBase.getOrderNo(), orderBase.getAmount(), "", PayType.BALANCE.getCode(), TradeOrderType.CONSUME.getCode());
        //先将订单状态更改为支付中
        orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.PAYING.getCode());
        //支付金额
        updateMoney(user.getId(), orderBase.getAmount(), orderBase.getPromotionMoney(), UserWalletUseEnum.PAY.getCode(), tradeBase.getTradeNo());
        //修改交易单状态和回调时间
        tradeBase.setUtime(new Date());
        tradeBase.setNofifyTime(new Date());
        if (!tradeBaseService.updateTradeStatus(tradeBase, TradeStatus.SUCCESS.getCode())) {
            logger.error("===============>修改交易号:" + tradeBase.getTradeNo() + "的状态失败");
        }
        //2.更新订单状态
        orderBase.setPayTime(new Date());
        orderBase.setPayType(PayType.BALANCE.getCode());
        orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.PAYED.getCode());
    }


    @Override
    public RechargeDto getRechargeDto(UserWalletDto userWalletDto) {
        RechargeDto rechargeDto = new RechargeDto(selectUserWallet(userWalletDto));
        RechargeDto rechargeDto1 = rechargeMoneyService.getRechargeMoney(userWalletDto.getProjectId());
        rechargeDto.setRechargeMoneyDtos(rechargeDto1.getRechargeMoneyDtos());
        return rechargeDto;
    }

    @Override
    public void refund(OrderBase orderBase, TradeBase tradeBase) {
        User user = userService.selectById(orderBase.getUserId());
        if (user == null) LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        UserWallet userWallet = selectUserWallet(user.getId(), WalletEnum.BALENCE.getCode());
        if (userWallet == null) LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        UserWallet forUpdate = new UserWallet();
        forUpdate.setId(userWallet.getId());
        forUpdate.setMoney(userWallet.getMoney() + orderBase.getAmount());
        updateById(forUpdate);
    }

}

