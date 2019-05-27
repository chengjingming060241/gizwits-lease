package com.gizwits.lease.event;

import com.gizwits.lease.device.entity.dto.OperatorStatusChangeDto;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 运营商状态变更
 *
 * @author lilh
 * @date 2017/8/3 9:28
 */
public class OperatorStatusChangeEvent extends ApplicationEvent {
    private static final long serialVersionUID = -7021057494758915683L;

    public OperatorStatusChangeEvent(Object source) {
        super(source);
    }

    public OperatorStatusChangeDto getChangeSource() {
        return (OperatorStatusChangeDto) getSource();
    }
}
