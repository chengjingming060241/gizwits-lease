package com.gizwits.lease.trade.dao;

import com.gizwits.lease.trade.entity.TradeWeixin;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-29
 */
public interface TradeWeixinDao extends BaseMapper<TradeWeixin> {

    TradeWeixin selectLastTrade(@Param("orderNo")String orderNo);

    int inserOnDunplicateKey(TradeWeixin tradeWeixin);
}