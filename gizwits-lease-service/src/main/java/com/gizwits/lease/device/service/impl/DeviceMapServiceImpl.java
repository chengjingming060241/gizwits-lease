package com.gizwits.lease.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceMapService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.vo.DeviceMapDetailDto;
import com.gizwits.lease.device.vo.DeviceMapDto;
import com.gizwits.lease.device.vo.DeviceMapQueryDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service - 设备地图
 *
 * @author lilh
 * @date 2017/7/27 15:57
 */
@Service
public class DeviceMapServiceImpl implements DeviceMapService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private DeviceService deviceService;

    @Override
    public List<DeviceMapDto> list(DeviceMapQueryDto query) {
        //1.查询符合条件的投放点
        Wrapper<DeviceLaunchArea> wrapper = new EntityWrapper<>();
        wrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                .in("sys_user_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        List<DeviceLaunchArea> deviceLaunchAreas = deviceLaunchAreaService.selectList(QueryResolverUtils.parse(new DeviceMapQueryDto.LaunchAreaPart(query), wrapper));
        if (CollectionUtils.isNotEmpty(deviceLaunchAreas)) {

            //2.查询符合条件的设备
            Wrapper<Device> deviceWrapper = new EntityWrapper<>();
            deviceWrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                    .in("launch_area_id", deviceLaunchAreas.stream().map(DeviceLaunchArea::getId).collect(Collectors.toSet()));
            List<Device> devices = deviceService.selectList(QueryResolverUtils.parse(new DeviceMapQueryDto.DevicePart(query), deviceWrapper));
            if (CollectionUtils.isNotEmpty(devices)) {
                //以投放点进行分组统计
                Map<Integer, Long> idToCount = devices.stream().collect(Collectors.groupingBy(Device::getLaunchAreaId, Collectors.counting()));
                //过滤掉次数为0的
                List<DeviceLaunchArea> matchAreas = deviceLaunchAreas.stream().filter(item -> Objects.nonNull(idToCount.get(item.getId())) &&  idToCount.get(item.getId())> 0).collect(Collectors.toList());
                List<DeviceMapDto> result = new ArrayList<>(matchAreas.size());
                matchAreas.forEach(item -> result.add(new DeviceMapDto(item)));
                result.forEach(item -> item.setDeviceCount((Long) ObjectUtils.defaultIfNull(idToCount.get(item.getId()), 0L)));
                return result;
            }
        }


        return new ArrayList<>();
    }

    @Override
    public DeviceMapDetailDto detail(Integer launchAreaId) {
        DeviceMapDetailDto result = new DeviceMapDetailDto();
        result.setId(launchAreaId);
        List<Device> devices = deviceService.selectList(
                new EntityWrapper<Device>().eq("launch_area_id", launchAreaId)
                        .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (CollectionUtils.isNotEmpty(devices)) {
            Map<Integer, Long> workStatusToCount = devices.stream().collect(Collectors.groupingBy(Device::getWorkStatus, Collectors.counting()));
            result.setFreeCount((Long) ObjectUtils.defaultIfNull(workStatusToCount.get(DeviceStatus.FREE.getCode()), 0L));
            result.setFaultCount((Long) ObjectUtils.defaultIfNull(workStatusToCount.get(DeviceStatus.FAULT.getCode()), 0L));
            result.setUsingCount((Long) ObjectUtils.defaultIfNull(workStatusToCount.get(DeviceStatus.USING.getCode()), 0L));
            result.setOfflineCount((Long) ObjectUtils.defaultIfNull(workStatusToCount.get(DeviceStatus.OFFLINE.getCode()), 0L));
        }
        return result;
    }
}
