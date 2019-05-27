package com.gizwits.lease.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.device.entity.DeviceLaunchArea;

import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaAndServiceDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备投放点表 Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-07-12
 */
public interface DeviceLaunchAreaDao extends BaseMapper<DeviceLaunchArea> {
    /**
     * 根据设备的对应的sysUserId获取List<省名，设备数>
     */
    List<Map<String, Number>> findProvinceAndCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId);

    Integer listPageTotal(DeviceLaunchAreaQueryDto deviceLaunchAreaQueryDto);

    List<DeviceLaunchAreaAndServiceDto> listPage(DeviceLaunchAreaQueryDto deviceLaunchAreaQueryDto);

}