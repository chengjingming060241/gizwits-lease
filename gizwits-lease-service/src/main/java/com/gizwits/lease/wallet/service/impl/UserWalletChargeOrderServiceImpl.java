package com.gizwits.lease.wallet.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.*;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.RechargeType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.operator.entity.OperatorExt;
import com.gizwits.lease.operator.service.OperatorExtService;
import com.gizwits.lease.order.dto.ChargeOrderDto;
import com.gizwits.lease.order.dto.DepositOrderDto;
import com.gizwits.lease.order.dto.LastOrderForUserWalletChargeOrderDto;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.trade.service.TradeWeixinService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.wallet.dao.UserWalletChargeOrderDao;
import com.gizwits.lease.wallet.dto.*;
import com.gizwits.lease.wallet.entity.RechargeMoney;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.RechargeMoneyService;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;
import com.gizwits.lease.wallet.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 用户钱包充值单表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-31
 */
@Service
public class UserWalletChargeOrderServiceImpl extends ServiceImpl<UserWalletChargeOrderDao, UserWalletChargeOrder> implements UserWalletChargeOrderService {
    private static Logger logger = LoggerFactory.getLogger("ORDER_LOGGER");

    @Autowired
    private UserService userService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorExtService operatorExtService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private RechargeMoneyService rechargeMoneyService;

    @Autowired
    private UserWalletChargeOrderDao userWalletChargeOrderDao;

    @Autowired
    private TradeWeixinService tradeWeixinService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Override
    public UserWalletChargeOrder createRechargeOrder(ChargeOrderDto chargeOrderDto) {
        logger.info("微信用户mobile = "+chargeOrderDto.getMobile()+",openid="+chargeOrderDto.getOpenid());
        User user = userService.getUserByIdOrOpenidOrMobile(chargeOrderDto.getMobile());

        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        UserWalletChargeOrder userWalletChargeOrder = new UserWalletChargeOrder();
        Double fee = 0.0;

        if (!ParamUtil.isNullOrEmptyOrZero(chargeOrderDto.getFee())) {
            fee = chargeOrderDto.getFee();
        }
        double discountMoney;

        if (ParamUtil.isNullOrEmptyOrZero(fee)) {

            // 进入该判断表示rechargeId不为空
            if (ParamUtil.isNullOrEmptyOrZero(chargeOrderDto.getRechargeId())) {
                LeaseException.throwSystemException(LeaseExceEnums.INPUT_FEE);
            }
            RechargeMoney rechargeMoney = rechargeMoneyService.selectById(chargeOrderDto.getRechargeId());
            fee = rechargeMoney.getChargeMoney();
            discountMoney = rechargeMoney.getDiscountMoney();
        } else {
            RechargeMoney rechargeMoney = rechargeMoneyService.selectOne(new EntityWrapper<RechargeMoney>().eq("type", RechargeType.CUSTOM.getCode()));
            discountMoney = rechargeMoney.getRate().multiply(new BigDecimal(fee)).doubleValue();
        }
        logger.info("创建充值单：fee=" + chargeOrderDto.getFee() + ",rechargeId=" + chargeOrderDto.getRechargeId() + ",projectId=" + chargeOrderDto.getProjectId());
        logger.info("discountMoney=" + discountMoney);
        userWalletChargeOrder.setFee(fee);
        userWalletChargeOrder.setChargeOrderNo(LeaseUtil.generateOrderNo(TradeOrderType.CHARGE.getCode()));
        userWalletChargeOrder.setWalletType(WalletEnum.BALENCE.getCode());
        userWalletChargeOrder.setWalletName(WalletEnum.BALENCE.getName());
        userWalletChargeOrder.setCtime(new Date());
        userWalletChargeOrder.setDiscountMoney(discountMoney);
        userWalletChargeOrder.setUserId(user.getId());
        userWalletChargeOrder.setBalance(fee + discountMoney);
        userWalletChargeOrder.setUsername(user.getUsername());
        userWalletChargeOrder.setStatus(UserWalletChargeOrderType.INIT.getCode());

        // TODO 添加用户最后一次消费的投放点及机器
        LastOrderForUserWalletChargeOrderDto lastRes = orderBaseService.getLastOrderForUserWalletChargeOrder(user.getId());

        if (lastRes != null) {
            userWalletChargeOrder.setLaunchAreaName(lastRes.getLaunchAreaName());
            userWalletChargeOrder.setDeviceName(lastRes.getDeviceName());
        }
        insert(userWalletChargeOrder);

        return userWalletChargeOrder;
    }

    @Override
    public UserWalletChargeOrder createDepositOrder(DepositOrderDto depositOrderDto) {
        User user = userService.getUserByMobile(depositOrderDto.getMobile());
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        Integer operatorSysuserid = deviceService.getDeviceOperatorSysuserid(depositOrderDto.getSno());
        if (operatorSysuserid == null) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }
        Operator operator = operatorService.getOperatorByAccountId(operatorSysuserid);
        if (operator == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        OperatorExt operatorExt = operatorExtService.selectOne((new EntityWrapper<OperatorExt>().eq("operator_id", operator.getId())));
//        if(operatorExt==null){
//            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
//        }

        BigDecimal fee = BigDecimal.ZERO;
        if (operatorExt != null) {
            fee = operatorExt.getCashPledge();
        }

        UserWalletChargeOrder userWalletChargeOrder = new UserWalletChargeOrder();
        userWalletChargeOrder.setChargeOrderNo(LeaseUtil.generateOrderNo(TradeOrderType.CHARGE.getCode()));
        userWalletChargeOrder.setWalletType(WalletEnum.DEPOSIT.getCode());
        userWalletChargeOrder.setCtime(new Date());
        userWalletChargeOrder.setFee(fee.doubleValue());
        userWalletChargeOrder.setUsername(user.getUsername());
        userWalletChargeOrder.setUserId(user.getId());
        userWalletChargeOrder.setStatus(UserWalletChargeOrderType.INIT.getCode());
        insert(userWalletChargeOrder);

        return userWalletChargeOrder;
    }

    @Override
    public void updateChargeOrderStatus(String chargeOrderNo, Integer toStatus) {
        if (StringUtils.isEmpty(chargeOrderNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        UserWalletChargeOrder chargeOrder = selectById(chargeOrderNo);
        updateChargeOrderStatus(chargeOrder, toStatus);
    }

    @Override
    public void updateChargeOrderStatus(UserWalletChargeOrder userWalletChargeOrder, Integer toStatus) {
        if (Objects.isNull(userWalletChargeOrder) || Objects.isNull(toStatus)) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        Boolean flag = false;
        UserChargeStatusMap map = new UserChargeStatusMap();
        List<Integer> statusList = map.get(userWalletChargeOrder.getStatus());
        for (Integer status : statusList) {
            if (status.equals(toStatus)) {
                userWalletChargeOrder.setStatus(toStatus);
                updateById(userWalletChargeOrder);
                flag = true;
                break;
            }
        }
        if (!flag) {
            logger.info("待修改状态："+toStatus);
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_CHANGE_STATUS_FAIL);
        }
    }

    @Override
    public Page<UserWalletChargeListDto> list(Pageable<UserWalletChargeOrderQueryDto> pageable) {

        SysUser sysUser = sysUserService.getCurrentUser();
        List<Integer> userIds = sysUserService.resolveAccessableUserIds(sysUser,false,true);

        UserWalletChargeOrderQueryDto queryDto = pageable.getQuery();
        if (Objects.isNull(queryDto)) {
            queryDto = new UserWalletChargeOrderQueryDto();
        }
        List<String> usernames = queryDto.getUsername();
        if (!ParamUtil.isNullOrEmptyOrZero(queryDto.getNickName())) {
            EntityWrapper<User> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("nickname", queryDto.getNickName()).in("sys_user_id", userIds);
            List<User> users = userService.selectList(entityWrapper);
            for (User user : users) {
                usernames.add(user.getUsername());
            }
        } else if (ParamUtil.isNullOrEmptyOrZero(queryDto.getUsername())) {
            usernames = new ArrayList<>();
            List<User> userList = userService.selectList(new EntityWrapper<User>().in("sys_user_id", userIds));
            for (User user : userList) {
                usernames.add(user.getUsername());
            }
        }
        queryDto.setUsername(usernames);
        Page<UserWalletChargeOrder> page = new Page<>();
        BeanUtils.copyProperties(queryDto, page);
        Page<UserWalletChargeListDto> result = new Page<>();
        if (ParamUtil.isNullOrEmptyOrZero(usernames)) {
            result.setRecords(new ArrayList<>(1));
            return result;
        }
        List<Integer> status = new ArrayList<>(2);
        status.add(UserWalletChargeOrderType.FINISH.getCode());
        status.add(UserWalletChargeOrderType.PAYED.getCode());
        Page<UserWalletChargeOrder> page1 = selectPage(page,
                QueryResolverUtils.parse(queryDto, new EntityWrapper<UserWalletChargeOrder>().in("status",status)));
        List<UserWalletChargeOrder> chargeOrders = page1.getRecords();
        List<UserWalletChargeListDto> listDtos = new ArrayList<>(chargeOrders.size());
        for (UserWalletChargeOrder chargeOrder : chargeOrders) {
            UserWalletChargeListDto listDto = new UserWalletChargeListDto();
            listDto.setChargeNo(chargeOrder.getChargeOrderNo());
            listDto.setFee(chargeOrder.getFee());
            listDto.setPayTime(chargeOrder.getPayTime());
            listDto.setPayType(PayType.getName(chargeOrder.getPayType()));
            listDto.setStatus(UserWalletChargeOrderType.FINISH.getName());
            User user = userService.selectOne(new EntityWrapper<User>().eq("username", chargeOrder.getUsername()));
            listDto.setNickName(user.getNickname());
            listDtos.add(listDto);
        }

        BeanUtils.copyProperties(page1, result);
        result.setRecords(listDtos);
        return result;
    }


    @Override
    public Page<DepositListDto> listDeposit(Pageable<DepositQueryDto> pageable) {
        SysUser sysUser = sysUserService.getCurrentUser();
        List<Integer> userIds = sysUserService.resolveAccessableUserIds(sysUser,false,true);
        DepositQueryDto queryDto = pageable.getQuery();
        if (queryDto == null) {
            queryDto = new DepositQueryDto();
        }
        queryDto.setUserIds(userIds);

        queryDto.setStatus(OrderStatus.FINISH.getCode());
        Page<DepositListDto> result = new Page<>();
        Integer pagesize = pageable.getSize();
        Integer current = pageable.getCurrent();
        Integer start = (current - 1) * pagesize;
        queryDto.setPagesize(pagesize);
        queryDto.setStart(start);

        // 获取投放点
        if (queryDto.getDeviceLaunchAreaId() != null) {
            DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(queryDto.getDeviceLaunchAreaId());
            queryDto.setLaunchAreaName(deviceLaunchArea.getName());
        }

        List<DepositListDto> listDtos = userWalletChargeOrderDao.listDeposit(queryDto);
        for (DepositListDto listDto : listDtos) {
            if (!ParamUtil.isNullOrEmptyOrZero(listDto.getPayType())) {
                listDto.setPayType(PayType.getName(Integer.parseInt(listDto.getPayType())));
            }
            listDto.setStatus(UserWalletChargeOrderType.FINISH.getCode());

        }
        BeanUtils.copyProperties(pageable, result);
        result.setRecords(listDtos);
        result.setTotal(userWalletChargeOrderDao.countNum(queryDto));
        return result;
    }

    @Override
    public DepositListDto depositDetail(String chargeOrderNo) {
        DepositListDto depositListDto = new DepositListDto();
        UserWalletChargeOrder chargeOrder = selectById(chargeOrderNo);
        if (chargeOrder == null) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        depositListDto.setChargeOrderNo(chargeOrder.getChargeOrderNo());
        depositListDto.setMoney(chargeOrder.getFee());
        depositListDto.setPayTime(chargeOrder.getPayTime());
        if (ParamUtil.isNullOrEmptyOrZero(chargeOrder.getPayType())) {
            depositListDto.setPayType(PayType.getName(chargeOrder.getPayType()));
        }
        User user = userService.getUserByUsername(chargeOrder.getUsername());
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }

        depositListDto.setMobile(user.getMobile());
        depositListDto.setNickName(user.getNickname());
        return depositListDto;
    }

    @Override
    public RefundDto refundInfo(String chargeOrderNo) {
        RefundDto refundDto = new RefundDto();
        UserWalletChargeOrder chargeOrder = selectById(chargeOrderNo);
        if (Objects.isNull(chargeOrder)) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        User user = userService.getUserByUsername(chargeOrder.getUsername());
        SysUserExt sysUserExt = sysUserExtService.selectOne(new EntityWrapper<SysUserExt>().eq("wx_id", user.getWxId()));

        refundDto.setUsername(user.getNickname());
        BigDecimal money = new BigDecimal(chargeOrder.getFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
        refundDto.setMoney(money);
        refundDto.setOrderNo(chargeOrderNo);
        refundDto.setPayAccount(sysUserExt.getWxParenterId());
        return refundDto;
    }

    @Override
    public void refund(String chargeOrderNo) {
        UserWalletChargeOrder chargeOrder = selectById(chargeOrderNo);
        if (Objects.isNull(chargeOrder)) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_ORDER_NOT_EXIST);
        }
        //目前只有微信退款
        tradeWeixinService.handleWxRefund(chargeOrderNo);
    }


    @Transactional
    public Boolean checkAndUpdateChargeOrder(Double totalFee, String orderNo, Integer payType, String tradeNo) {
        if (ParamUtil.isNullOrEmptyOrZero(totalFee) ||
                ParamUtil.isNullOrEmptyOrZero(orderNo) ||
                ParamUtil.isNullOrEmptyOrZero(tradeNo) ||
                ParamUtil.isNullOrEmptyOrZero(payType)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }

        UserWalletChargeOrder userWalletOrder = selectById(orderNo);
        if (userWalletOrder == null) {
            logger.error("====>>>>> 订单orderNo[" + userWalletOrder.getChargeOrderNo() + "]在系统中未找到");
            return false;
        }

        //检查订单的状态是否是未支付
        if (userWalletOrder.getPayTime() != null || UserWalletChargeOrderType.PAYED.getCode().equals(userWalletOrder.getStatus())) {
            logger.warn("====>>>> 订单tradeNo[" + tradeNo + "]的状态为已支付,本次支付回调不做处理");
            return false;
        }

        //检查订单金额是否一致,注:微信回调中的金额单位是分,需要转换为元
        if (!totalFee.equals(userWalletOrder.getFee())) {
            logger.error("====>>>>> 订单tradeNo[" + tradeNo + "]的金额为[" + userWalletOrder.getFee() + "]与支付回调的金额[" + totalFee + "]的金额不匹配,本次支付回调不做处理");
            return false;
        }

        Date now = new Date();
        userWalletOrder.setPayTime(now);
        userWalletOrder.setPayType(payType);
        //修改订单状态和修改订单支付时间
        updateChargeOrderStatus(userWalletOrder, UserWalletChargeOrderType.PAYED.getCode());
        //充值
        userWalletService.updateMoney(userWalletOrder.getUserId(),
                userWalletOrder.getFee(),
                userWalletOrder.getDiscountMoney(),
                UserWalletUseEnum.RECHARGE.getCode(), tradeNo);
        //修改订单状态和修改订单支付时间
        updateChargeOrderStatus(userWalletOrder, UserWalletChargeOrderType.FINISH.getCode());
        return true;
    }

    @Override
    public double sumRealRecharge() {
        return userWalletChargeOrderDao.sumRealRecharge();
    }

    @Override
    public double sumRealUse() {
        return userWalletChargeOrderDao.sumRealUse();
    }

    @Override
    public double sumRealGive() {
        return userWalletChargeOrderDao.sumRealGive();
    }

    @Override
    public double sumRealGiveUse() {
        return userWalletChargeOrderDao.sumRealGiveUse();
    }

}
