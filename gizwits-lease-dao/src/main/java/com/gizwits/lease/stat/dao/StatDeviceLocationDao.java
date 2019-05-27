package com.gizwits.lease.stat.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.stat.entity.StatDeviceLocation;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备地图分布统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
public interface StatDeviceLocationDao extends BaseMapper<StatDeviceLocation> {

    List<StatDeviceLocation> getDistribution(@Param("productId") Integer productId, @Param("sysUserId") Integer id, @Param("date") Date date);

    List<StatDeviceLocation> getRank(@Param("productId") Integer productId, @Param("sysUserId") Integer id, @Param("date") Date date);

    List<StatDeviceLocation> getDistributionByIds(@Param("productId") Integer productId, @Param("ids") List<Integer> ids, @Param("date") Date yesterday);

    List<StatDeviceLocation> getRankByIds(@Param("productId") Integer productId, @Param("ids") List<Integer> ids, @Param("date") Date yesterday);

    void countDeviceLocation(@Param("ctime") Date ctime);

    void countDeviceWithoutLocation(@Param("ctime") Date ctime);
}