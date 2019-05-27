package com.gizwits.lease.stat.dao;

import com.gizwits.lease.stat.dto.StatDeviceTrendDto;
import com.gizwits.lease.stat.entity.StatDeviceTrend;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备趋势统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
public interface StatDeviceTrendDao extends BaseMapper<StatDeviceTrend> {

    List<StatDeviceTrend> getNewTrend(@Param("sysUserId") Integer sysUserId, @Param("dto") StatDeviceTrendDto statDeviceTrendDto);

    List<StatDeviceTrend> getActiveTrend(@Param("sysUserId") Integer sysUserId, @Param("dto") StatDeviceTrendDto statDeviceTrendDto);

    List<StatDeviceTrend> getUsePecentTrend(@Param("sysUserId") Integer sysUserId, @Param("dto") StatDeviceTrendDto statDeviceTrendDto);

    List<StatDeviceTrend> getNewTrendByIds(@Param("ids") List<Integer> ids, @Param("dto") StatDeviceTrendDto statDeviceTrendDto);

    List<StatDeviceTrend> getActiveTrendByIds(@Param("ids") List<Integer> ids, @Param("dto") StatDeviceTrendDto statDeviceTrendDto);

    List<StatDeviceTrend> getUsePecentTrendByIds(@Param("ids") List<Integer> ids, @Param("dto") StatDeviceTrendDto statDeviceTrendDto);

    void countDeviceTrend(@Param("ctime")Date ctime, @Param("activeCount")Integer activeCount, @Param("productId")Integer productId);
}