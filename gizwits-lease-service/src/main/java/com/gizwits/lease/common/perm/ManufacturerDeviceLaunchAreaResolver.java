package com.gizwits.lease.common.perm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.enums.AssignDestinationType;
import com.gizwits.lease.enums.CommonRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Resolver - 厂商分配投放点
 *
 * @author lilh
 * @date 2017/9/4 14:42
 */
@Component
public class ManufacturerDeviceLaunchAreaResolver extends AbstractDeviceLaunchAreaCommonRoleResolver {

    @Autowired
    private ManufacturerCommonRoleResolver manufacturerCommonRoleResolver;

    @Override
    public CommonRole getCommonRole() {
        return CommonRole.MANUFACTURER;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        return Stream.of(AssignDestinationType.OPERATOR).map(AssignDestinationDto::new).collect(Collectors.toList());
    }

    @Override
    protected void doUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        manufacturerCommonRoleResolver.preUnbind(dto, helper);
        manufacturerCommonRoleResolver.doUnbind(dto, helper);
    }

    @Override
    protected void doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        manufacturerCommonRoleResolver.assign(dto, helper);
    }
}
