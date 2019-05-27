package com.gizwits.lease.event;

import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.model.DeviceAddressModel;
import org.springframework.context.ApplicationEvent;

/**
 * Created by zhl on 2017/9/6.
 */
public class DeviceLocationEvent extends ApplicationEvent {

    private DeviceAddressModel deviceAddressModel;
    private Device device;

    public DeviceLocationEvent(Object source, DeviceAddressModel deviceAddressMode, Device device) {
        super(source);
        this.deviceAddressModel = deviceAddressMode;
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public DeviceAddressModel getDeviceAddressModel() {
        return deviceAddressModel;
    }

    public void setDeviceAddressModel(DeviceAddressModel deviceAddressModel) {
        this.deviceAddressModel = deviceAddressModel;
    }
}
