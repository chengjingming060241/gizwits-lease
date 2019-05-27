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
 * Resolver - 运营商角色
 *
 * @author lilh
 * @date 2017/7/8 14:36
 */
@Component
public class OperatorCommonRoleResolver extends AbstractCommonRoleResolver {

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Override
    public CommonRole getCommonRole() {
        return CommonRole.OPERATOR;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        //投放点和运营商
        return Stream.of(AssignDestinationType.LAUNCH_AREA, AssignDestinationType.OPERATOR).map(AssignDestinationDto::new).collect(Collectors.toList());
    }

    @Override
    protected boolean doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //1.区分投放点
        if (Objects.equals(dto.getAssignDestinationType(), AssignDestinationType.LAUNCH_AREA.getCode())) {
            //投放点
            DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectOne(new EntityWrapper<DeviceLaunchArea>().eq("id", dto.getAssignedId())
                    .eq("sys_user_id", sysUserService.getCurrentUser().getId())
                    .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            if (Objects.isNull(deviceLaunchArea)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LAUNCH_AREA_NOT_EXIST);
            }
            //将设备和投放点关联
            dto.getDevices().forEach(item -> {
                item.setLaunchAreaId(deviceLaunchArea.getId());
                item.setLaunchAreaName(deviceLaunchArea.getName());
                item.setUtime(new Date());
            });
            //将当前运营商关联到设备组
            dto.setResolvedAccountId(helper.getSysAccountId());
            dto.setAssignedName(operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", helper.getSysAccountId())).getName());
            customAssign(dto, helper);
            return deviceService.updateBatchById(dto.getDevices());
        } else if (Objects.equals(dto.getAssignDestinationType(), AssignDestinationType.OPERATOR.getCode())) {
            return super.doAssign(dto, helper);
        }
        return false;
    }

    @Override
    protected Integer resolveAccountId(DeviceForAssignDto dto) {
        //运营商只支持分配到运营商和投放点，投放点额外处理
        if (Objects.equals(dto.getAssignDestinationType(), AssignDestinationType.OPERATOR.getCode())) {
            return map.get(AssignDestinationType.getType(dto.getAssignDestinationType())).resolve(dto);
        }
        return null;
    }

    @Override
    protected void preAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        super.preAssign(dto, helper);
        //1.生成分润单
//        deviceService.generateShareSheet(dto.getDevices(), dto.getForceAssign());
    }

    public static void main(String[] args) {
        System.out.println("before");
        LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        System.out.println("after");
    }
}
