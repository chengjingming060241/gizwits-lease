package com.gizwits.lease.common.perm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.event.DeviceLaunchAreaAssignEvent;
import com.gizwits.lease.event.source.DeviceLaunchAreaAssignSource;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Resolver - 投放点分配相关
 *
 * @author lilh
 * @date 2017/9/2 15:13
 */
public abstract class AbstractDeviceLaunchAreaCommonRoleResolver implements CommonRoleResolver {

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    protected SysUserService sysUserService;

    @Override
    public boolean assign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        initDevices(dto, helper);
        preAssign(dto, helper);
        doAssign(dto, helper);//会把投放点和收费模式清空
        return afterAssign(dto, helper);
    }

    @Override
    public boolean unbind(DeviceForUnbindDto unbindDto, SysUserRoleTypeHelper helper) {
        DeviceForAssignDto dto = new DeviceForAssignDto();
        BeanUtils.copyProperties(unbindDto, dto);
        initDevices(dto, helper);
        preUnbind(dto, helper);
        doUnbind(dto, helper);
        return afterUnbind(dto, helper);
    }

    protected abstract void doUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper);

    protected abstract void doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper);

    private void initDevices(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //根据所选择的投放点查询设备，若投放点不是自身创建或该投放点下无设备，则结束流程
        if (CollectionUtils.isEmpty(dto.getDevices())) {
            if (CollectionUtils.isEmpty(dto.getLaunchAreaIds())) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
            }
            //1.投放点是自建的
            List<DeviceLaunchArea> deviceLaunchAreas = deviceLaunchAreaService.selectList(new EntityWrapper<DeviceLaunchArea>()
                    .eq("sys_user_id", helper.getSysAccountId())
                    .in("id", dto.getLaunchAreaIds())
                    .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            //单独写一个方法检验
            //.eq("owner_id", helper.getSysAccountId()));
            if (CollectionUtils.isEmpty(deviceLaunchAreas)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
            }
            if (deviceLaunchAreas.size() != dto.getLaunchAreaIds().size()) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
            }
            dto.setDeviceLaunchAreas(deviceLaunchAreas);
            //2.投放点下的设备 投放点下可无设备
            List<Device> devices = deviceService.selectList(new EntityWrapper<Device>()
                    .in("launch_area_id", deviceLaunchAreas.stream().map(DeviceLaunchArea::getId).collect(Collectors.toSet()))
                    .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            if (CollectionUtils.isNotEmpty(devices)) {
                dto.setDevices(devices);
            }

            //用于记录设备原来的投放点信息
            fillMap(dto);
        }
    }

    private boolean afterUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //投放点回到创建人手中
        dto.setResolvedAccountId(helper.getSysAccountId());
        updateAndPublishEvent(dto, helper, DeviceLaunchAreaAssignEvent.Type.UNBIND);
        return true;
    }

    private void preUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        if(!ParamUtil.isNullOrEmptyOrZero(dto.getDevices())) {
            //当前投放点的拥有都必须是当前登录人创建的
            Set<Integer> ownerIds = dto.getDeviceLaunchAreas().stream().map(DeviceLaunchArea::getOwnerId).collect(Collectors.toSet());
            int count = sysUserService.selectCount(new EntityWrapper<SysUser>().in("id", new ArrayList<>(ownerIds)).eq("sys_user_id", helper.getSysAccountId()));
            if (ownerIds.size() != count) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LAUNCH_AREA_CANT_UNBIND);
            }
            //解绑时，若投放点的当前拥有人是当前登录人，则该操作无意义
            if (ownerIds.contains(helper.getSysAccountId())) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LAUNCH_AREA_ALREADY_BELONG_SELF);
            }
        }
    }

    //检验投放点目前是否在当前人手里
    private void preAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        boolean has = dto.getDeviceLaunchAreas().stream().anyMatch(item -> !Objects.equals(item.getOwnerId(), item.getSysUserId()));
        if (has) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LAUNCH_AREA_CANT_ASSIGN);
        }
    }

    private boolean afterAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        updateAndPublishEvent(dto, helper, DeviceLaunchAreaAssignEvent.Type.ASSIGN);
        return true;
    }

    private void updateAndPublishEvent(DeviceForAssignDto dto, SysUserRoleTypeHelper helper, DeviceLaunchAreaAssignEvent.Type type) {
        //因为分配的时候会把投放点清空，故此时设置回来
        if(!ParamUtil.isNullOrEmptyOrZero(dto.getDevices())) {
            resetDeviceLaunch(dto);
        }
        //更新投放点
        if (CollectionUtils.isNotEmpty(dto.getDeviceLaunchAreas())) {
            //发布投放点分配事件
            SysUser current = sysUserService.getCurrentUser();
            List<DeviceLaunchAreaAssignSource> sources = dto.getDeviceLaunchAreas().stream().map(item -> new DeviceLaunchAreaAssignSource(item, current, dto.getResolvedAccountId())).collect(Collectors.toList());
            dto.getDeviceLaunchAreas().forEach(item -> {
                item.setUtime(new Date());
                item.setOwnerId(dto.getResolvedAccountId());
            });
            deviceLaunchAreaService.updateBatchById(dto.getDeviceLaunchAreas());
            CommonEventPublisherUtils.publishEvent(new DeviceLaunchAreaAssignEvent(sources, type));
        }
    }

    private void resetDeviceLaunch(DeviceForAssignDto dto) {
        //是否要重新查询
        List<Device> devices = deviceService.selectBatchIds(dto.getDevices().stream().map(Device::getSno).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(devices)) {
            devices.forEach(device -> {
                device.setLaunchAreaId(dto.getSnoToDeviceLaunchArea().get(device.getSno()).getId());
                device.setLaunchAreaName(dto.getSnoToDeviceLaunchArea().get(device.getSno()).getName());
            });
            deviceService.updateBatchById(devices);
        }
    }

    private void fillMap(DeviceForAssignDto dto) {
        Map<Integer, DeviceLaunchArea> idToObj = dto.getDeviceLaunchAreas().stream().collect(Collectors.toMap(DeviceLaunchArea::getId, item -> item));
        if (!ParamUtil.isNullOrEmptyOrZero(dto.getDevices())) {
            dto.setSnoToDeviceLaunchArea(dto.getDevices().stream().collect(Collectors.toMap(Device::getSno, item -> idToObj.get(item.getLaunchAreaId()))));
        }
    }
}
