package com.gizwits.lease.device.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.common.perm.CommonRoleResolver;
import com.gizwits.lease.common.perm.DefaultCommonRoleResolverManager;
import com.gizwits.lease.common.perm.SysUserRoleTypeHelper;
import com.gizwits.lease.common.perm.SysUserRoleTypeHelperResolver;
import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaForUnbindDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaAssignService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lilh
 * @date 2017/9/2 17:38
 */
@Service
public class DeviceLaunchAreaAssignServiceImpl implements DeviceLaunchAreaAssignService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleTypeHelperResolver helperResolver;

    @Autowired
    private DefaultCommonRoleResolverManager resolverManager;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Override
    public List<AssignDestinationDto> preAssign() {
        SysUserRoleTypeHelper helper = helperResolver.resolve(sysUserService.getCurrentUser());
        CommonRoleResolver resolver = resolverManager.getLaunchResolver(helper.getCommonRole());
        if (Objects.nonNull(resolver)) {
            return resolver.resolveAssignDest();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean assign(DeviceLaunchAreaForAssignDto dto) {
        SysUserRoleTypeHelper helper = helperResolver.resolve(sysUserService.getCurrentUser());
        CommonRoleResolver resolver = resolverManager.getLaunchResolver(helper.getCommonRole());
        DeviceForAssignDto assignDto = new DeviceForAssignDto();
        BeanUtils.copyProperties(dto, assignDto);
        return Objects.nonNull(resolver) && resolver.assign(assignDto, helper);
    }

    @Override
    public boolean unbind(DeviceLaunchAreaForUnbindDto dto) {
        SysUserRoleTypeHelper helper = helperResolver.resolve(sysUserService.getCurrentUser());
        CommonRoleResolver resolver = resolverManager.getLaunchResolver(helper.getCommonRole());
        DeviceForUnbindDto unbindDto = new DeviceForUnbindDto();
        BeanUtils.copyProperties(dto, unbindDto);
        return Objects.nonNull(resolver) && resolver.unbind(unbindDto, helper);
    }

    @Override
    public boolean canModify(Integer launchAreaId) {
        if (Objects.nonNull(launchAreaId)) {
            DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(launchAreaId);
            if (Objects.nonNull(deviceLaunchArea)) {
                if (Objects.isNull(deviceLaunchArea.getOwnerId())) {
                    deviceLaunchArea.setOwnerId(deviceLaunchArea.getSysUserId());
                    deviceLaunchAreaService.updateById(deviceLaunchArea);
                }
                return Objects.equals(deviceLaunchArea.getSysUserId(), deviceLaunchArea.getOwnerId());
            }
        }
        return true;
    }
}
