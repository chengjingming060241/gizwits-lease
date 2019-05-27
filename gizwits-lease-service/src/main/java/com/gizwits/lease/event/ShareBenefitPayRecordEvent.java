package com.gizwits.lease.event;

import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import org.springframework.context.ApplicationEvent;

/**
 * Created by zhl on 2017/8/8.
 */
public class ShareBenefitPayRecordEvent extends ApplicationEvent {
    public ShareBenefitPayRecordEvent(Object source) {
        super(source);
    }

    public ShareBenefitPayRecordEvent(Object source, ShareBenefitSheet shareBenefitSheet, String content, Integer actionUserId) {
        super(source);
        this.shareBenefitSheet = shareBenefitSheet;
        this.content = content;
        this.actionUserId = actionUserId;
    }

    private ShareBenefitSheet shareBenefitSheet;

    private Integer actionUserId;

    private String content;

    public ShareBenefitSheet getShareBenefitSheet() {
        return shareBenefitSheet;
    }

    public void setShareBenefitSheet(ShareBenefitSheet shareBenefitSheet) {
        this.shareBenefitSheet = shareBenefitSheet;
    }

    public Integer getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(Integer actionUserId) {
        this.actionUserId = actionUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
