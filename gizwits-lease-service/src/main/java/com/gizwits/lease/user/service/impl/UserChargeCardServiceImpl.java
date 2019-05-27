package com.gizwits.lease.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.boot.utils.WebUtils;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.constant.TradeStatus;
import com.gizwits.lease.enums.ChargeCardOperationType;
import com.gizwits.lease.enums.ChargeCardStatus;
import com.gizwits.lease.enums.IsTrueEnum;
import com.gizwits.lease.event.UserChargeCardOperateEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.trade.service.TradeBaseService;
import com.gizwits.lease.user.dao.UserChargeCardDao;
import com.gizwits.lease.user.dto.*;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserChargeCard;
import com.gizwits.lease.user.service.UserChargeCardService;
import com.gizwits.lease.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 充值卡 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@Service
public class UserChargeCardServiceImpl extends ServiceImpl<UserChargeCardDao, UserChargeCard> implements UserChargeCardService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private TradeBaseService tradeBaseService;
    @Autowired
    private OrderBaseService orderBaseService;

    protected static Logger logger = LoggerFactory.getLogger(UserChargeCard.class);

    @Override
    public Page<UserChargeCardListDto> list(Pageable<UserChargeCardQueryDto> pageable) {
        Page<UserChargeCard> page = new Page<>();
        pageable.setOrderByField("ctime");
        pageable.setAsc(false);
        BeanUtils.copyProperties(pageable, page);
        Page<UserChargeCard> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<UserChargeCardListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result, "records");
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(selectPage.getRecords().stream().map(UserChargeCardListDto::new).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public UserChargeCardDetailDto detail(Integer id) {
        if (Objects.isNull(id)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        UserChargeCard userChargeCard = selectById(id);
        if (Objects.isNull(userChargeCard)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        UserChargeCardDetailDto result = new UserChargeCardDetailDto(userChargeCard);
        result.setIsBindWxDesc(IsTrueEnum.getDesc(userChargeCard.getIsBindWx()));
        return result;
    }

    @Override
    public boolean enable(UserChargeCardOperationDto dto) {
        List<Integer> ids = dto.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        dto.setStatus(ChargeCardStatus.DISABLE.getCode());
        List<UserChargeCard> userChargeCards = selectList(QueryResolverUtils.parse(dto, new EntityWrapper<>()));
        if (CollectionUtils.isNotEmpty(userChargeCards)) {
            userChargeCards.forEach(item -> {
                item.setUtime(new Date());
                item.setStatus(ChargeCardStatus.USING.getCode());
            });
            updateBatchById(userChargeCards);
            publishEvent(userChargeCards, ChargeCardOperationType.ENABLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean disable(UserChargeCardOperationDto dto) {
        List<Integer> ids = dto.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        dto.setStatus(ChargeCardStatus.USING.getCode());
        List<UserChargeCard> userChargeCards = selectList(QueryResolverUtils.parse(dto, new EntityWrapper<>()));
        if (CollectionUtils.isNotEmpty(userChargeCards)) {
            userChargeCards.forEach(item -> {
                item.setUtime(new Date());
                item.setStatus(ChargeCardStatus.DISABLE.getCode());
            });
            updateBatchById(userChargeCards);
            publishEvent(userChargeCards, ChargeCardOperationType.DISABLE);
            return true;
        }
        return false;
    }

    private void publishEvent(List<UserChargeCard> userChargeCards, ChargeCardOperationType operationType) {
        UserChargeCardOperateEvent.Builder builder = UserChargeCardOperateEvent.Builder.create();
        builder.chargeCards(userChargeCards).operationType(operationType).current(sysUserService.getCurrentUser()).ip(WebUtils.getRemoteAddr());
        CommonEventPublisherUtils.publishEvent(builder.build());
    }

    @Override
    public void bindChargeCard(UserChargeCardBindDto dto) {
        User user = userService.getUserByIdOrOpenidOrMobile(dto.getOpenid());

        // card_num validate
        if (StringUtils.isEmpty(dto.getCardNum())) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        List<UserChargeCard> cards = baseMapper.selectList(new EntityWrapper<UserChargeCard>().eq("card_num", dto.getCardNum()));
        if (cards.size() > 0) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_CARD_WAS_BOUND);
        }

        Date now = new Date();
        UserChargeCard card = new UserChargeCard();
        card.setUserId(user.getId());
        card.setCardNum(dto.getCardNum());
        card.setMobile(dto.getMobile());
        card.setUserName(dto.getUserName());
        card.setCtime(now);
        card.setBindCardTime(now);
        card.setIsBindWx(1);
        baseMapper.insert(card);
    }

    @Override
    public List getChargeCardList(UserChargeCardOpenidDto dto) {
        User user = userService.getUserByIdOrOpenidOrMobile(dto.getOpenid());
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        List<UserChargeCard> cards = baseMapper.selectList(new EntityWrapper<UserChargeCard>().eq("user_id", user.getId()));
        return cards.stream().map(UserChargeCardListDto::new).collect(Collectors.toList());
    }

    @Override
    public UserChargeCardListDto getChargeCardDetail(UserChargeCardIdDto dto) {
        UserChargeCard card = baseMapper.selectById(dto.getId());
        if (card == null) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_CARD_DONT_EXISTS);
        }
        UserChargeCardListDto cardDto = new UserChargeCardListDto(card);
        return cardDto;
    }

    @Override
    public int countChargeCard(Integer userId) {
        return baseMapper.selectCount(new EntityWrapper<UserChargeCard>().eq("user_id", userId));
    }

    @Override
    public void pay(OrderBase orderBase, String cardNum) {
        UserChargeCard chargeCard = selectOne(new EntityWrapper<UserChargeCard>().eq("card_num", cardNum));
        if (ParamUtil.isNullOrEmptyOrZero(chargeCard)) {
            LeaseException.throwSystemException(LeaseExceEnums.CARD_NOT_EXIST);
        }
        Double payMoney = orderBase.getAmount();
        TradeBase tradeBase = tradeBaseService.createTrade(orderBase.getOrderNo(), orderBase.getAmount(), "充值卡支付", PayType.CARD.getCode(), LeaseUtil.getOrderType(orderBase.getOrderNo()));
        orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.PAYING.getCode());
        if (chargeCard.getMoney().compareTo(payMoney) >= 0) {
            double resultMoney = chargeCard.getMoney() - payMoney;
            chargeCard.setMoney(resultMoney);
            tradeBaseService.updateTradeStatus(tradeBase, TradeStatus.SUCCESS.getCode());
            orderBase.setPayTime(new Date());
            updateById(chargeCard);//修改充值卡金额
            orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.PAYED.getCode());
        } else {
            logger.error("充值卡[" + chargeCard.getCardNum() + "] 余额 [" + chargeCard.getMoney() + "] 不足以支付需要支付的金额 [" + payMoney + "]");
        }
    }

    @Transactional
    public void refund(OrderBase orderBase, TradeBase tradeBase){
        logger.info("====订单{}开始执行刷卡退款逻辑====",orderBase.getOrderNo());
        if (!(OrderStatus.PAYED.getCode().equals(orderBase.getOrderStatus()) || OrderStatus.REFUNDING.getCode().equals(orderBase.getOrderStatus()))) {
            logger.error("====订单{}状态为{},不符合退款逻辑====",orderBase.getOrderNo(),orderBase.getOrderStatus());
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_STATUS_ERROR);
        }

        UserChargeCard chargeCard = selectOne(new EntityWrapper<UserChargeCard>().eq("card_num", orderBase.getPayCardNum()));
        if (ParamUtil.isNullOrEmptyOrZero(chargeCard)) {
            logger.error("====订单{}中的卡号{}在系统中未找到====",orderBase.getOrderNo(),orderBase.getPayCardNum());
            LeaseException.throwSystemException(LeaseExceEnums.CARD_NOT_EXIST);
        }

        chargeCard.setMoney(chargeCard.getMoney()+orderBase.getAmount());
        chargeCard.setUtime(new Date());
        updateById(chargeCard);
    }
}
