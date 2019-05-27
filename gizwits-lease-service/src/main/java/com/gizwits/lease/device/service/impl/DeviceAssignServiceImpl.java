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
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.device.service.DeviceAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service - 设备分配
 *
 * @author lilh
 * @date 2017/8/24 18:37
 */
@Service
public class DeviceAssignServiceImpl implements DeviceAssignService {

    @Autowired
    private SysUserRoleTypeHelperResolver resolverHelper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DefaultCommonRoleResolverManager resolverManager;

    @Override
    public List<AssignDestinationDto> preAssign() {
        SysUserRoleTypeHelper helper = resolverHelper.resolve(sysUserService.getCurrentUser());
        CommonRoleResolver resolver = resolverManager.getCommonRoleResolver(helper.getCommonRole());
        if (Objects.nonNull(resolver)) {
            return resolver.resolveAssignDest();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean assign(DeviceForAssignDto dto) {
        SysUserRoleTypeHelper helper = resolverHelper.resolve(sysUserService.getCurrentUser());
        CommonRoleResolver resolver = resolverManager.getCommonRoleResolver(helper.getCommonRole());
        if (Objects.nonNull(resolver)) {
            return resolver.assign(dto, helper);
        }
        return false;
    }

    @Override
    public boolean unbind(DeviceForUnbindDto dto) {
        SysUserRoleTypeHelper helper = resolverHelper.resolve(sysUserService.getCurrentUser());
        CommonRoleResolver resolver = resolverManager.getCommonRoleResolver(helper.getCommonRole());
        if (Objects.nonNull(resolver)) {
            return resolver.unbind(dto, helper);
        }
        return false;
    }
}
