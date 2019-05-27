package com.gizwits.lease.china.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.china.entity.ChinaArea;

import java.util.List;
import java.util.Map;
/**
 * <p>
  * 全国省市行政编码表 Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-07-14
 */
public interface ChinaAreaDao extends BaseMapper<ChinaArea> {

    List<Map<String,String>> getProvince();
}