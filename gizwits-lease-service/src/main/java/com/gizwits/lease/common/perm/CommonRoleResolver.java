package com.gizwits.lease.common.perm;

import java.util.List;

import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.enums.CommonRole;

/**
 * @author lilh
 * @date 2017/7/8 14:18
 */
public interface CommonRoleResolver {

    CommonRole getCommonRole();

    /**
     * 分配目标
     */
    List<AssignDestinationDto> resolveAssignDest();

    /**
     * 分配
     */
    boolean assign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper);

    /**
     * 解绑
     */
    boolean unbind(DeviceForUnbindDto dto, SysUserRoleTypeHelper helper);
}
