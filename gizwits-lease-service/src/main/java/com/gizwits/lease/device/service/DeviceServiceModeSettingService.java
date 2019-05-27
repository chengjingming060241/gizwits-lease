package com.gizwits.lease.device.service;

import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceServiceModeSetting;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 设备收费模式设定(麻将机系统特有需求) 服务类
 * </p>
 *
 * @author zhl
 * @since 2017-09-04
 */
public interface DeviceServiceModeSettingService extends IService<DeviceServiceModeSetting> {

    boolean deleteAssignDeviceServiceMode(Integer assignAccountId, List<String> snoList);

    boolean deleteAssignDeviceServiceMode(List<Device> deviceList);

    boolean saveDeviceServiceMode(String sno, Integer sysAccountId, Integer assignAccountId);
}
