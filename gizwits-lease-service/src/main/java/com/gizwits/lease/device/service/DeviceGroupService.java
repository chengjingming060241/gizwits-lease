package com.gizwits.lease.device.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.device.entity.DeviceGroup;
import com.gizwits.lease.device.entity.dto.DeviceGroupForAddDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForDetailDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForListDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForUpdateDto;

/**
 * <p>
 * 设备组 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-08-15
 */
public interface DeviceGroupService extends IService<DeviceGroup> {

    /**
     * 添加
     */
    boolean add(DeviceGroupForAddDto dto);

    /**
     * 列表
     */
    Page<DeviceGroupForListDto> page(Pageable<DeviceGroupForQueryDto> pageable);

    /**
     * 更新
     */
    boolean update(DeviceGroupForUpdateDto dto);

    /**
     * 详情
     */
    DeviceGroupForDetailDto detail(Integer id);

    /**
     * 获取已经当前用户已经分组的设备
     */
    List<String> resolveDeviceAlreadyInGroup();
}
