package com.gizwits.lease.device.service;

import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceExt;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.device.vo.DevicePortDto;
import com.gizwits.lease.device.vo.DevicePortVo;

import java.util.List;

/**
 * <p>
 * 设备扩展表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-24
 */
public interface DeviceExtService extends IService<DeviceExt> {

    DeviceExt selectBySnoAndPort(String sno,Integer port);

    DevicePortVo listDevicePort(String sno);

    List<DevicePortDto> getDevicePortDtoList(Device device);

    int countDeviceByStatus(String sno, Integer status);

    boolean resetStatus(String sno);
}
