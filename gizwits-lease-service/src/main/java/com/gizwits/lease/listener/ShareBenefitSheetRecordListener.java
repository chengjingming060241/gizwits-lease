package com.gizwits.lease.listener;

import java.util.Date;

import com.gizwits.lease.benefit.entity.ShareBenefitSheetActionRecord;
import com.gizwits.lease.benefit.service.ShareBenefitSheetActionRecordService;
import com.gizwits.lease.event.ShareBenefitSheetActionEvent;
import com.gizwits.lease.event.source.ShareBenefitSheetActionSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 分润单操作记录
 *
 * @author lilh
 * @date 2017/8/5 16:59
 */
@Component
public class ShareBenefitSheetRecordListener implements ApplicationListener<ShareBenefitSheetActionEvent> {

    @Autowired
    private ShareBenefitSheetActionRecordService shareBenefitSheetActionRecordService;

    @Async
    @Override
    public void onApplicationEvent(ShareBenefitSheetActionEvent event) {
        ShareBenefitSheetActionSource source = event.getActionSource();
        ShareBenefitSheetActionRecord record = new ShareBenefitSheetActionRecord();
        record.setActionType(source.getActionType());
        record.setSheetId(source.getSheetId());
        record.setUserId(source.getUserId());
        record.setCtime(new Date());
        shareBenefitSheetActionRecordService.insert(record);
    }
}
