package com.gizwits.lease.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.entity.dto.DeviceAlarmAppListDto;
import com.gizwits.lease.device.entity.dto.DeviceAlarmQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceAlramInfoDto;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备故障(警告)记录表 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-07-13
 */
public interface DeviceAlarmDao extends BaseMapper<DeviceAlarm> {


    DeviceAlarm findDeviceUnresolveAlarm(@Param("mac") String mac, @Param("productKey") String productKey, @Param("attr") String attr, @Param("status") Integer status);


    List<DeviceAlramInfoDto> listPage(DeviceAlarmQueryDto deviceAlarmQueryDto);

    List<DeviceAlramInfoDto> appListPage(DeviceAlarmAppListDto deviceAlarmAppListDto);

    Integer countNum(DeviceAlarmQueryDto deviceAlarmQueryDto);

    Integer appCountNum(DeviceAlarmAppListDto deviceAlarmAppListDto);

    List<String> getDeviceAlarmName(@Param("snos") List<String> sno);


    Integer getRecord(@Param("sysUserId") Integer sysUserId,@Param("productId") Integer productId,@Param("date") Date now);

}