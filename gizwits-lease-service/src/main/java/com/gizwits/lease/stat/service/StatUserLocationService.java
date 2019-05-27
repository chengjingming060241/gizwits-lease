package com.gizwits.lease.stat.service;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.entity.StatUserLocation;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.stat.vo.StatLocationVo;

import java.util.List;

/**
 * <p>
 * 用户地图分布统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
public interface StatUserLocationService extends IService<StatUserLocation> {

    void setDataForLocation();

    List<StatLocationVo> getDitribution(SysUser currentUser, List<Integer> ids);

    List<StatLocationVo> getRank(SysUser currentUser, List<Integer> ids);
}
