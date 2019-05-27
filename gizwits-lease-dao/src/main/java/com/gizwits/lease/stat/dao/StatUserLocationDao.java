package com.gizwits.lease.stat.dao;

import com.gizwits.lease.stat.entity.StatUserLocation;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
  * 用户地图分布统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
public interface StatUserLocationDao extends BaseMapper<StatUserLocation> {

    /**
     * @param id
     * @param yesterday
     * @return 设备分布列表
     */
    List<StatUserLocation> getDistribution(@Param("ids") List<Integer> ids,@Param("date") Date yesterday);

    /**
     * @param id
     * @param yesterday
     * @return 设备分布排行
     */
    List<StatUserLocation> getRank(@Param("ids") List<Integer> ids,@Param("date") Date yesterday);
}