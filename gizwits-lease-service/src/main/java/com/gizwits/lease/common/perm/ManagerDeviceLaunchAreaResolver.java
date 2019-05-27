package com.gizwits.lease.common.perm;

import java.util.Collections;
import java.util.List;

import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.enums.CommonRole;
import org.springframework.stereotype.Component;

/**
 * @author lilh
 * @date 2017/9/4 14:44
 */
@Component
public class ManagerDeviceLaunchAreaResolver extends AbstractDeviceLaunchAreaCommonRoleResolver {
    @Override
    public CommonRole getCommonRole() {
        return CommonRole.MANAGER;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        return Collections.emptyList();
    }

    @Override
    protected void doUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        throw new UnsupportedOperationException();
    }
}
