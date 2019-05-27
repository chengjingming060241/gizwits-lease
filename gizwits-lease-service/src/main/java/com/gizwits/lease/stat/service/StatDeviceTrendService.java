package com.gizwits.lease.stat.service;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.dto.StatDeviceTrendDto;
import com.gizwits.lease.stat.entity.StatDeviceTrend;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.stat.vo.StatTrendVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备趋势统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
public interface StatDeviceTrendService extends IService<StatDeviceTrend> {

    void setDataForStatDeviceTrend();

    /**
     * @param today                今天
     * @param yesterday            昨天
     * @param ownerId              归属id
     */
    void setDataForDeviceTrendForOwnerId(Date today, Date yesterday, Integer ownerId);

    List<StatTrendVo> getNewTrend(StatDeviceTrendDto statDeviceTrendDto, SysUser currentUser, List<Integer> ids);

    List<StatTrendVo> getActiveTrend(StatDeviceTrendDto statDeviceTrendDto, SysUser currentUser, List<Integer> ids);

    List<StatTrendVo> getUsePercentTrend(StatDeviceTrendDto statDeviceTrendDto, SysUser currentUser, List<Integer> ids);

    Map<Integer, Integer> getActiveCountFromGizwits();
}
