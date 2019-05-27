package com.gizwits.lease.product.dao;

import com.gizwits.lease.product.entity.ProductDataPoint;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 产品数据点 Mapper 接口
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
public interface ProductDataPointDao extends BaseMapper<ProductDataPoint> {

    List<ProductDataPoint> findAllMonitPoint(@Param("productKey") String productKey);
}