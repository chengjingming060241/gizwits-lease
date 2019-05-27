package com.gizwits.lease.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.device.entity.UserDevice;
import com.gizwits.lease.device.dao.UserDeviceDao;
import com.gizwits.lease.device.service.UserDeviceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户绑定设备表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-12
 */
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceDao, UserDevice> implements UserDeviceService {

    @Override
    public List<String> getDeviceSnoByUserName(String username) {
        EntityWrapper<UserDevice> entityWrapper  = new EntityWrapper<>();
        entityWrapper.eq("username",username);
        List<String> snos = new ArrayList<>();
        List<UserDevice> userDeviceLsit = selectList(entityWrapper);
        for(UserDevice ud:userDeviceLsit){
            snos.add(ud.getSno());
        }
        return snos;
    }
}
