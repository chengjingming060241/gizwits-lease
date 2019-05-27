package com.gizwits.lease.device.service;

import com.gizwits.lease.device.entity.DeviceExtForMajiang;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 设备扩展表(麻将机) 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-30
 */
public interface DeviceExtForMajiangService extends IService<DeviceExtForMajiang> {

    void setGame(DeviceExtForMajiang deviceExtByMajiang);

    DeviceExtForMajiang select(String sno);
}
