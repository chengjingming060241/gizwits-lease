package com.gizwits.lease.stat.service;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.entity.StatDeviceLocation;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.stat.vo.StatLocationVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备地图分布统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
public interface StatDeviceLocationService extends IService<StatDeviceLocation> {
    /**
     *
     * @param yesterday 昨天
     * @param ownerId 设备归属id
     */
    void setDataForLocationByOwnerId(Date yesterday, Integer ownerId);

    void setDataForLocation();

    List<StatLocationVo> getDistribution(Integer productId, SysUser currentUser, List<Integer> sysUserIds);

    List<StatLocationVo> getRank(Integer productId, SysUser currentUser, List<Integer> ids);
}
