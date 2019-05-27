package com.gizwits.lease.product.dao;

import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-07-13
 */
public interface ProductServiceDetailDao extends BaseMapper<ProductServiceDetail> {
    ProductServiceDetail getMinPriceDetailByServiceModeId(@Param("serviceModeId") Integer serviceModeId);
}