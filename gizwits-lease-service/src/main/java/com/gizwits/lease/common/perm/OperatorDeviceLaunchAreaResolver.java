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
 * Resolver - 运营商分配投放点相关
 *
 * @author lilh
 * @date 2017/9/2 17:20
 */
@Component
public class OperatorDeviceLaunchAreaResolver extends AbstractDeviceLaunchAreaCommonRoleResolver {

    @Autowired
    private OperatorCommonRoleResolver operatorCommonRoleResolver;

    @Override
    public CommonRole getCommonRole() {
        return CommonRole.OPERATOR;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        return Stream.of(AssignDestinationType.OPERATOR).map(AssignDestinationDto::new).collect(Collectors.toList());
    }

    @Override
    protected void doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        operatorCommonRoleResolver.assign(dto, helper);
    }

    @Override
    protected void doUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        operatorCommonRoleResolver.preUnbind(dto, helper);
        operatorCommonRoleResolver.doUnbind(dto, helper);
    }
}
