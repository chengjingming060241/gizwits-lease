package com.gizwits.lease.product.dao;

import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.product.vo.ProductCommandVO;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
public interface ProductCommandConfigDao extends BaseMapper<ProductCommandConfig> {

    List<ProductCommandVO> findAllUseableStatusCommandConfig();
}