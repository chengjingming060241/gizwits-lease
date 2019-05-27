package com.gizwits.lease.common.perm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.enums.AssignDestinationType;
import com.gizwits.lease.enums.CommonRole;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;

import org.springframework.stereotype.Component;

/**
 * Resolver - 管理员
 *
 * @author lilh
 * @date 2017/7/8 14:24
 */
@Component
public class ManagerCommonRoleResolver extends AbstractCommonRoleResolver {

    @Override
    public CommonRole getCommonRole() {
        return CommonRole.MANAGER;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        return Arrays.stream(AssignDestinationType.values()).map(AssignDestinationDto::new).collect(Collectors.toList());
    }

    @Override
    public boolean assign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //管理员不执行分配操作
        //return true;
        LeaseException.throwSystemException(LeaseExceEnums.UNSUPPORT_OPERATOIN);
//        throw new UnsupportedOperationException();
        return true;
    }

    @Override
    public boolean unbind(DeviceForUnbindDto unbindDto, SysUserRoleTypeHelper helper) {
        LeaseException.throwSystemException(LeaseExceEnums.UNSUPPORT_OPERATOIN);
//        throw new UnsupportedOperationException();
        return true;
    }
}
