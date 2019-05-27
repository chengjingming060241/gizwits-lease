package com.gizwits.lease.event;

import java.util.List;

import com.gizwits.lease.event.source.DeviceLaunchAreaAssignSource;
import org.springframework.context.ApplicationEvent;

/**
 * @author lilh
 * @date 2017/9/2 16:43
 */
public class DeviceLaunchAreaAssignEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1156714761738282454L;
    private Type type;

    public DeviceLaunchAreaAssignEvent(Object source) {
        super(source);
    }

    public DeviceLaunchAreaAssignEvent(Object source, Type type) {
        this(source);
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public List<DeviceLaunchAreaAssignSource> getDevicelaunchAreaSource() {
        return (List<DeviceLaunchAreaAssignSource>) getSource();
    }

    public Type getType() {
        return type;
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
