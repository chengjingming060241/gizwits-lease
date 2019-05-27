package com.gizwits.lease.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.gizwits.boot.enums.SysUserStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.device.entity.dto.OperatorStatusChangeDto;
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.event.OperatorStatusChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 更新运营商绑定的系统账号的状态
 *
 * @author lilh
 * @date 2017/8/3 10:03
 */
@Component
public class UpdateOperatorAccountListener implements ApplicationListener<OperatorStatusChangeEvent> {

    private static Map<Integer, Integer> map = new HashMap<>();

    @Autowired
    private SysUserService sysUserService;

    @Async
    @Override
    public void onApplicationEvent(OperatorStatusChangeEvent event) {
        OperatorStatusChangeDto dto = event.getChangeSource();
        Integer to = map.get(dto.getTo());
        if (Objects.nonNull(to)) {
            SysUser operatorUser = sysUserService.selectById(dto.getSysAccountId());
            if (Objects.nonNull(operatorUser) && !Objects.equals(to, operatorUser.getIsEnable())) {
                operatorUser.setIsEnable(to);
                operatorUser.setUtime(new Date());
                sysUserService.updateById(operatorUser);
            }
        }
    }

    static {
        map.put(StatusType.OPERATING.getCode(), SysUserStatus.ENABLE.getCode());
        map.put(StatusType.SUSPENDED.getCode(), SysUserStatus.DISABLE.getCode());
        map.put(StatusType.NEED_TO_ASSIGN.getCode(), SysUserStatus.ENABLE.getCode());
    }
}
