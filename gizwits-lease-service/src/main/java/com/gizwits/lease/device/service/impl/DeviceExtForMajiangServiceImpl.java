package com.gizwits.lease.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.device.entity.DeviceExtForMajiang;
import com.gizwits.lease.device.dao.DeviceExtForMajiangDao;
import com.gizwits.lease.device.service.DeviceExtForMajiangService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 设备扩展表(麻将机) 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-30
 */
@Service
public class DeviceExtForMajiangServiceImpl extends ServiceImpl<DeviceExtForMajiangDao, DeviceExtForMajiang> implements DeviceExtForMajiangService {

    @Override
    public void setGame(DeviceExtForMajiang deviceExtByMajiang){
        DeviceExtForMajiang deviceExtByMajiang1 = selectById(deviceExtByMajiang.getSno());
        if(Objects.isNull(deviceExtByMajiang1)){
            deviceExtByMajiang.setCtime(new Date());
            deviceExtByMajiang.setUtime(new Date());
        }else {
            deviceExtByMajiang.setUtime(new Date());
        }
        insertOrUpdate(deviceExtByMajiang);
    }

    @Override
    public DeviceExtForMajiang select(String sno){
        return selectOne(new EntityWrapper<DeviceExtForMajiang>().eq("sno",sno));
    }
}
