package com.gizwits.lease.device.service;

import java.util.List;

import com.gizwits.lease.device.vo.DeviceMapDetailDto;
import com.gizwits.lease.device.vo.DeviceMapDto;
import com.gizwits.lease.device.vo.DeviceMapQueryDto;

/**
 * @author lilh
 * @date 2017/7/27 15:49
 */
public interface DeviceMapService {

    /**
     * 列表
     */
    List<DeviceMapDto> list(DeviceMapQueryDto query);

    /**
     * 详情
     */
    DeviceMapDetailDto detail(Integer launchAreaId);
}
