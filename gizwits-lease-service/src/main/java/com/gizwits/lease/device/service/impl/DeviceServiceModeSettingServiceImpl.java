package com.gizwits.lease.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.BooleanEnum;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceServiceModeSetting;
import com.gizwits.lease.device.dao.DeviceServiceModeSettingDao;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.service.DeviceServiceModeSettingService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.service.ProductServiceModeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 设备收费模式设定(麻将机系统特有需求) 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-09-04
 */
@Service
public class DeviceServiceModeSettingServiceImpl extends ServiceImpl<DeviceServiceModeSettingDao, DeviceServiceModeSetting> implements DeviceServiceModeSettingService {

    @Autowired
    private DeviceServiceModeSettingDao deviceServiceModeSettingDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductServiceModeService productServiceModeService;


    public boolean deleteAssignDeviceServiceMode(Integer assignAccountId, List<String> snoList){
        if(ParamUtil.isNullOrEmptyOrZero(assignAccountId)
                || CollectionUtils.isEmpty(snoList)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        return deviceServiceModeSettingDao.deleteDeviceServiceModeSettingByAssignAccountIdAndSnoList(assignAccountId, snoList)>0;
    }

    public boolean deleteAssignDeviceServiceMode(List<Device> deviceList){
        if(CollectionUtils.isEmpty(deviceList)){
            return false;
        }
        deviceList.forEach(device -> {
            deviceServiceModeSettingDao.deleteDeviceServiceModeSettingByAssignAccountIdAndSno(device.getOwnerId(),device.getSno());
        });
        return true;
    }

    public boolean saveDeviceServiceMode(String sno, Integer sysAccountId, Integer assignAccountId){
        if(ParamUtil.isNullOrEmptyOrZero(sno)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        Device device = deviceService.getDeviceInfoBySno(sno);
        if(Objects.isNull(device)){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        EntityWrapper<ProductServiceMode> serviceModeWapper = new EntityWrapper<>();
        serviceModeWapper.eq("id",device.getServiceId()).eq("is_deleted", BooleanEnum.FALSE.getCode());
        ProductServiceMode serviceMode = productServiceModeService.selectOne(serviceModeWapper);
       /* if(Objects.isNull(serviceMode)){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }*/

       if (Objects.nonNull(serviceMode)) {
           DeviceServiceModeSetting modeSetting = new DeviceServiceModeSetting();
           modeSetting.setCtime(new Date());
           modeSetting.setSno(device.getSno());
           modeSetting.setSysAccountId(sysAccountId);
           modeSetting.setAssignAccountId(assignAccountId);
           modeSetting.setIsFree(serviceMode.getIsFree());
           modeSetting.setIsDeleted(BooleanEnum.FALSE.getCode());
           return insert(modeSetting);
       }
       return true;
    }

}
