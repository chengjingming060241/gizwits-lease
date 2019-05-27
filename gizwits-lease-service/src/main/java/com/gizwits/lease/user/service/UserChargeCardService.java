package com.gizwits.lease.user.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.user.dto.*;
import com.gizwits.lease.user.entity.UserChargeCard;

import java.util.List;

/**
 * <p>
 * 充值卡 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
public interface UserChargeCardService extends IService<UserChargeCard> {

    /**
     * 列表
     */
    Page<UserChargeCardListDto> list(Pageable<UserChargeCardQueryDto> pageable);

    /**
     * 详情
     */
    UserChargeCardDetailDto detail(Integer id);

    /**
     * 启用
     */
    boolean enable(UserChargeCardOperationDto dto);

    /**
     * 禁用
     */
    boolean disable(UserChargeCardOperationDto dto);

    /**
     * 绑定充值卡
     */
    void bindChargeCard(UserChargeCardBindDto dto);

    /**
     * 用户获取充值卡列表
     */
    List getChargeCardList(UserChargeCardOpenidDto dto);

    /**
     * 用户获取充值卡基本信息
     */
    UserChargeCardListDto getChargeCardDetail(UserChargeCardIdDto dto);

    /**
     * 查询用户充值卡数量
     * @param userId
     * @return
     */
    int countChargeCard(Integer userId);

    void pay(OrderBase orderBase, String cardNum);

    void refund(OrderBase order, TradeBase tradeBase);
}
