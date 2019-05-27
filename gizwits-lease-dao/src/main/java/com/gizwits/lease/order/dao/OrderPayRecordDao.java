package com.gizwits.lease.order.dao;

import com.gizwits.lease.order.entity.OrderPayRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-07-17
 */
public interface OrderPayRecordDao extends BaseMapper<OrderPayRecord> {

    OrderPayRecord findByPayingOrderNo(@Param("orderNo") String orderNo);
}