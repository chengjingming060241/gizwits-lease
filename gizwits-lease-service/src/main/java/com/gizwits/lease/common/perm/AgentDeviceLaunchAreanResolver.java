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
 * @author lilh
 * @date 2017/9/2 15:15
 */
@Component
public class AgentDeviceLaunchAreanResolver extends AbstractDeviceLaunchAreaCommonRoleResolver {

    @Autowired
    private AgentCommonRoleResolver agentCommonRoleResolver;


    @Override
    public CommonRole getCommonRole() {
        return CommonRole.AGENT;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        return Stream.of(AssignDestinationType.OPERATOR).map(AssignDestinationDto::new).collect(Collectors.toList());
    }


    @Override
    protected void doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        agentCommonRoleResolver.assign(dto, helper);
    }

    @Override
    protected void doUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        agentCommonRoleResolver.preUnbind(dto, helper);
        agentCommonRoleResolver.doUnbind(dto, helper);
    }
}
