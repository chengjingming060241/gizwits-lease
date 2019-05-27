package com.gizwits.lease.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.benefit.entity.ShareBenefitSheetPayRecord;
import com.gizwits.lease.benefit.service.ShareBenefitSheetPayRecordService;
import com.gizwits.lease.enums.ShareBenefitSheetStatusType;
import com.gizwits.lease.event.ShareBenefitPayRecordEvent;
import com.gizwits.lease.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhl on 2017/8/8.
 */
public class ShareBenefitPayRecordListener implements ApplicationListener<ShareBenefitPayRecordEvent> {

    @Autowired
    private ShareBenefitSheetPayRecordService shareBenefitSheetPayRecordService;

    @Override
    public void onApplicationEvent(ShareBenefitPayRecordEvent shareBenefitPayRecordEvent) {
        ShareBenefitSheet sheet = shareBenefitPayRecordEvent.getShareBenefitSheet();
        EntityWrapper<ShareBenefitSheetPayRecord> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sheet_id",sheet.getId()).eq("trade_no",sheet.getTradeNo()).orderBy("ctime",false);
        ShareBenefitSheetPayRecord payRecord = shareBenefitSheetPayRecordService.selectOne(entityWrapper);
        if(payRecord==null){
            payRecord = new ShareBenefitSheetPayRecord();
            payRecord.setSheetId(sheet.getId());
            payRecord.setTradeNo(sheet.getTradeNo());
            payRecord.setAmount(new BigDecimal(sheet.getShareMoney()));
            payRecord.setContent(shareBenefitPayRecordEvent.getContent());
            payRecord.setStatus(ShareBenefitSheetStatusType.SHARING.getCode());
            payRecord.setCtime(new Date());
            payRecord.setUserId(shareBenefitPayRecordEvent.getActionUserId());
            shareBenefitSheetPayRecordService.insert(payRecord);
        }else{//执行结果
            int status = WxUtil.checkShareBenefitResult(shareBenefitPayRecordEvent.getContent());
            payRecord.setStatus(status);
            payRecord.setCallbackContent(shareBenefitPayRecordEvent.getContent());
            payRecord.setUtime(new Date());
            shareBenefitSheetPayRecordService.updateById(payRecord);
        }
    }
}
