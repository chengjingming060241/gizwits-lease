package com.gizwits.lease.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.gizwits.lease.event.UserChargeCardOperateEvent;
import com.gizwits.lease.user.entity.UserChargeCardOperationRecord;
import com.gizwits.lease.user.service.UserChargeCardOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 启用禁用充值卡操作记录
 *
 * @author lilh
 * @date 2017/8/29 19:43
 */
@Component
public class UserChargeCardOperateListener implements ApplicationListener<UserChargeCardOperateEvent> {

    @Autowired
    private UserChargeCardOperationRecordService userChargeCardOperationRecordService;

    @Async
    @Override
    public void onApplicationEvent(UserChargeCardOperateEvent event) {
        List<UserChargeCardOperationRecord> operationRecords = resolveOperationRecords(event);
        userChargeCardOperationRecordService.insertBatch(operationRecords);
    }

    private List<UserChargeCardOperationRecord> resolveOperationRecords(UserChargeCardOperateEvent event) {

        return event.getUserChargeCards().stream().map(item -> {
            UserChargeCardOperationRecord operationRecord = new UserChargeCardOperationRecord();
            operationRecord.setCardNum(item.getCardNum());
            operationRecord.setCtime(new Date());
            operationRecord.setOperateType(event.getOperationType().getCode());
            operationRecord.setIp(event.getIp());
            operationRecord.setSysUserId(event.getCurrent().getId());
            operationRecord.setSysUserName(event.getCurrent().getUsername());
            return operationRecord;
        }).collect(Collectors.toList());
    }
}
