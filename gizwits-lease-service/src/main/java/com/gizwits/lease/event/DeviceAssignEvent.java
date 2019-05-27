package com.gizwits.lease.event;

import java.util.List;

import com.gizwits.lease.device.entity.dto.DeviceAssignRecordDto;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 设备分配事件
 *
 * @author lilh
 * @date 2017/8/2 16:08
 */
public class DeviceAssignEvent extends ApplicationEvent {
    private static final long serialVersionUID = -2980046611601882656L;

    private Type type;

    private DeviceAssignEvent(Object source) {
        super(source);
    }

    public DeviceAssignEvent(Object source, Type type) {
        this(source);
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public List<DeviceAssignRecordDto> getRecords() {
        return (List<DeviceAssignRecordDto>) getSource();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ASSIGN("ASSIGN"),
        UNBIND("UNBIND");

        private String desc;

        Type(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
