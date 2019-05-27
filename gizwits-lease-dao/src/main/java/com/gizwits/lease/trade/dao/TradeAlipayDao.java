package com.gizwits.lease.trade.dao;

import com.gizwits.lease.trade.entity.TradeAlipay;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 支付宝交易表 Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-08-15
 */
public interface TradeAlipayDao extends BaseMapper<TradeAlipay> {

    TradeAlipay selectLastTrade(@Param("orderNo") String orderNo);

}