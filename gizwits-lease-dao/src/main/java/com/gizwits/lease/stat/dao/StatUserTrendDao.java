package com.gizwits.lease.stat.dao;

import com.gizwits.lease.stat.dto.StatUserTrendDto;
import com.gizwits.lease.stat.entity.StatUserTrend;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户趋势及性别，使用次数统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
public interface StatUserTrendDao extends BaseMapper<StatUserTrend> {

    List<StatUserTrend> getNewTrend(@Param("ids") List<Integer> ids, @Param("dto") StatUserTrendDto statUserTrendDto);

    List<StatUserTrend> getActiveTrend(@Param("ids") List<Integer> ids, @Param("dto") StatUserTrendDto statUserTrendDto);

    List<StatUserTrend> getTotalTrend(@Param("ids") List<Integer> ids, @Param("dto") StatUserTrendDto statUserTrendDto);

    StatUserTrend getSex(@Param("ids") List<Integer> ids,@Param("date") Date yesterday);

    StatUserTrend getTimes(@Param("ids") List<Integer> ids,@Param("date") Date yesterday);
}