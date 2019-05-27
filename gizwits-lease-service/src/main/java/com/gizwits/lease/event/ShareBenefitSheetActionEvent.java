package com.gizwits.lease.event;

import com.gizwits.lease.event.source.ShareBenefitSheetActionSource;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 分润单操作事件
 *
 * @author lilh
 * @date 2017/8/5 11:47
 */
public class ShareBenefitSheetActionEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8152182479713134542L;

    public ShareBenefitSheetActionEvent(Object source) {
        super(source);
    }

    public ShareBenefitSheetActionSource getActionSource() {
        return (ShareBenefitSheetActionSource) getSource();
    }
}
