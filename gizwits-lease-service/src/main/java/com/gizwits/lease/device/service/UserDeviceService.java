package com.gizwits.lease.device.service;

import com.gizwits.lease.device.entity.UserDevice;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 用户绑定设备表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-12
 */
public interface UserDeviceService extends IService<UserDevice> {

    /**
     * 通过用户名查询该用户拥有的设备号
     * @param username
     * @return
     */
    public List<String> getDeviceSnoByUserName(String username);
	
}
