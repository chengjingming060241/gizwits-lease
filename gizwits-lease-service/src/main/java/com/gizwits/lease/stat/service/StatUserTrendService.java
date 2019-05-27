package com.gizwits.lease.stat.service;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.dto.StatUserTrendDto;
import com.gizwits.lease.stat.entity.StatUserTrend;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.stat.vo.StatSexVo;
import com.gizwits.lease.stat.vo.StatTimesVo;
import com.gizwits.lease.stat.vo.StatTrendVo;

import java.util.List;

/**
 * <p>
 * 用户趋势及性别，使用次数统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
public interface StatUserTrendService extends IService<StatUserTrend> {

    void setDataForStatUserTrend();

    List<StatTrendVo> getNewTrend(SysUser currentUser, StatUserTrendDto statUserTrendDto, List<Integer> ids);

    List<StatTrendVo> getActiveTrend(SysUser currentUser, StatUserTrendDto statUserTrendDto, List<Integer> ids);

    List<StatTrendVo> getTotalTrend(SysUser currentUser, StatUserTrendDto statUserTrendDto, List<Integer> ids);

    StatSexVo getSexDistribution(SysUser currentUser, List<Integer> ids);

    StatTimesVo getTimesDistribution(SysUser currentUser, List<Integer> ids);
}
