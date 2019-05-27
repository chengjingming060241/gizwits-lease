package com.gizwits.lease.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceExt;
import com.gizwits.lease.device.dao.DeviceExtDao;
import com.gizwits.lease.device.service.DeviceExtService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.vo.DevicePortDto;
import com.gizwits.lease.device.vo.DevicePortVo;
import com.gizwits.lease.enums.DevicePortType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备扩展表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-24
 */
@Service
public class DeviceExtServiceImpl extends ServiceImpl<DeviceExtDao, DeviceExt> implements DeviceExtService {

    @Autowired
    private DeviceService deviceService;

    @Override
    public DeviceExt selectBySnoAndPort(String sno, Integer port) {
        EntityWrapper<DeviceExt> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno",sno).eq("port",port);
        return selectOne(entityWrapper);
    }

    @Override
    public DevicePortVo listDevicePort(String sno) {
        Device device = deviceService.selectById(sno);
        if(device == null){
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        DevicePortVo portVo = new DevicePortVo();
        List<DevicePortDto> portDtos = getDevicePortDtoList(device);
        portVo.setSno(device.getSno());
        portVo.setDevicePortDtoList(portDtos);
        return portVo;
    }

    @Override
    public List<DevicePortDto> getDevicePortDtoList(Device device) {
        EntityWrapper<DeviceExt> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno",device.getSno()).orderBy("port",true);

        List<DeviceExt> list = selectList(entityWrapper);
        List<DevicePortDto> portDtos = new ArrayList<>();
        for(DeviceExt deviceExt:list){
            DevicePortDto devicePortDto = new DevicePortDto();
            devicePortDto.setPort(deviceExt.getPort());
            devicePortDto.setWater(deviceExt.getPortType());
            devicePortDto.setSort(deviceExt.getSort());
            devicePortDto.setStatus(DeviceStatus.getName(deviceExt.getStatus()));
            portDtos.add(devicePortDto);
        }
        return portDtos;
    }

    @Override
    public int countDeviceByStatus(String sno,Integer status){
        return selectCount(new EntityWrapper<DeviceExt>().eq("sno",sno).eq("status",status));
    }

    /**
     * 将设备的所有出水口置为空闲
     * @param sno
     * @return
     */
    @Override
    public boolean resetStatus(String sno){
        EntityWrapper<DeviceExt> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno",sno);
        DeviceExt deviceExt = new DeviceExt();
        deviceExt.setUtime(new Date());
        deviceExt.setStatus(DeviceStatus.FREE.getCode());
        return update(deviceExt,entityWrapper);
    }
}
