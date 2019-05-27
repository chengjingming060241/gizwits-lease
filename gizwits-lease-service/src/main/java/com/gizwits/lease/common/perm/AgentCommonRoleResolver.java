package com.gizwits.lease.common.perm;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.enums.AssignDestinationType;
import com.gizwits.lease.enums.CommonRole;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lilh
 * @date 2017/7/8 14:34
 */
@Component
public class AgentCommonRoleResolver extends AbstractCommonRoleResolver {
    @Override
    public CommonRole getCommonRole() {
        return CommonRole.AGENT;
    }

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        //运营商和代理商
        return Stream.of(AssignDestinationType.OPERATOR, AssignDestinationType.AGENT).map(AssignDestinationDto::new).collect(Collectors.toList());
    }

}
