package com.gizwits.lease.stat.service;

import com.gizwits.lease.device.entity.Device;

/**
 * Created by GaGi on 2017/8/26.
 */
public interface StatService {
    /**
     *  解绑后使用
     * @param device 要解绑的设备
     * @param ownerId 解绑前的ownerId
     */
    void setDataForDeviceAssign(Device device, Integer ownerId);
}
