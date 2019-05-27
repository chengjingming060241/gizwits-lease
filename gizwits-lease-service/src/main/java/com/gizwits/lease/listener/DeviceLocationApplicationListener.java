package com.gizwits.lease.listener;

import com.gizwits.lease.china.entity.ChinaArea;
import com.gizwits.lease.china.service.ChinaAreaService;
import com.gizwits.lease.constant.AlarmStatus;
import com.gizwits.lease.constant.AlarmType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.enums.CoverLevel;
import com.gizwits.lease.event.DeviceLocationEvent;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.model.DeviceAddressModel;
import com.gizwits.lease.model.LocationPoint;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.resolver.GizwitsDataPointResolver;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.util.DeviceControlAPI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by zhl on 2017/9/6.
 */
@Component
public class DeviceLocationApplicationListener implements ApplicationListener<DeviceLocationEvent> {
    private final static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private ChinaAreaService chinaAreaService;

    private final static Long ELECT_FENCE_RADIUS = 1000L;//电子围栏半径范围,单位米

    @Async
    @Override
    public void onApplicationEvent(DeviceLocationEvent deviceLocationEvent) {
        Device device = deviceLocationEvent.getDevice();
        DeviceAddressModel deviceAddressModel = deviceLocationEvent.getDeviceAddressModel();
        if(Objects.nonNull(device) && Objects.nonNull(deviceAddressModel)){
            //串货告警设置,防串货不需要投放点的配置
            checkAndSaveDeviceTransfor(device,deviceAddressModel);

            if(Objects.isNull(device.getLaunchAreaId())){
                logger.error("===设备{}未设置投放点,不能进行电子围栏处理=====",device.getSno());
                return;
            }
            DeviceLaunchArea launchArea = deviceLaunchAreaService.getLaunchAreaInfoById(device.getLaunchAreaId());
            if(Objects.isNull(launchArea)){
                logger.error("===设备{}的投放点{}不存在,不能进行电子围栏处理======",device.getSno(),device.getLaunchAreaId());
                return;
            }
            if(StringUtils.isBlank(launchArea.getLatitude()) || StringUtils.isBlank(launchArea.getLongitude())){
                logger.error("===设备{}的投放点{}定位信息不完整,不能进行电子围栏处理====",device.getSno(),device.getLaunchAreaId());
                return;
            }
            if(Objects.isNull(deviceAddressModel.getLatitude()) || Objects.isNull(deviceAddressModel.getLongitude())){
                logger.error("===设备{}的最新定位信息中经纬度信息不完整,不能进行电子围栏处理====",device.getSno());
                return;
            }

            LocationPoint sourcePoint = new LocationPoint();
            sourcePoint.setLatitude(new BigDecimal(launchArea.getLatitude()));
            sourcePoint.setLongitude(new BigDecimal(launchArea.getLongitude()));

            LocationPoint targetPoint = new LocationPoint();
            targetPoint.setLatitude(deviceAddressModel.getLatitude());
            targetPoint.setLongitude(deviceAddressModel.getLongitude());

            Long distance = DeviceControlAPI.calculatePointDistance(sourcePoint, targetPoint);
            if(Objects.isNull(distance)){
                logger.error("===无法执行距离计算====");
                return;
            }
            //电子围栏提醒
            if(distance.compareTo(ELECT_FENCE_RADIUS)>=0){
                logger.info("====设备{}的位置latitude:{},longitude:{}超出电子围栏范围{},发出电子围栏警告====",device.getSno(),deviceAddressModel.getLatitude(),deviceAddressModel.getLongitude(),ELECT_FENCE_RADIUS);
                createAndSaveAlarm(device, deviceAddressModel);
            }
        }
    }

    private void checkAndSaveDeviceTransfor(Device device, DeviceAddressModel deviceAddressModel){
        if(Objects.isNull(device.getOwnerId())){
            logger.warn("====设备{}的归属者ownerId为空====",device.getSno());
            return;
        }
        Agent agent = agentService.getAgentBySysAccountId(device.getOwnerId());
        if(Objects.nonNull(agent)){
            logger.info("====设备{}归属于代理商{}====",device.getSno(),agent.getId());
            if(checkIsBeyondCover(agent.getCoverLevel(), deviceAddressModel, agent.getProvince(), agent.getCity(), agent.getArea())){
                justSaveAlert(device,deviceAddressModel);
            }
            return;
        }

        Operator operator = operatorService.getOperatorByAccountId(device.getOwnerId());
        if(Objects.nonNull(operator)){
            logger.info("====设备{}归属于代理商{}====",device.getSno(),operator.getId());
            if(checkIsBeyondCover(operator.getCoverLevel(), deviceAddressModel, operator.getProvince(), operator.getCity(), operator.getArea())){
                justSaveAlert(device,deviceAddressModel);
                return;
            }
        }

    }

    private boolean checkIsBeyondCover(Integer coverLevel, DeviceAddressModel deviceAddressModel, String province, String city, String area){

        switch (coverLevel){
            case 1:
            case 2:
                if(!deviceAddressModel.getProvince().equals(province)){
                    return true;
                }
                break;
            case 3:
                if(!deviceAddressModel.getProvince().equals(province) || !deviceAddressModel.getCity().equals(city)){
                    return true;
                }
                break;
            case 4:
                if(!deviceAddressModel.getProvince().equals(province) || !deviceAddressModel.getCity().equals(city)){
                    return true;
                }
                ChinaArea chinaArea = chinaAreaService.getAreaByName(area,city);
                if(Objects.nonNull(chinaArea)){
                    if(!chinaArea.getCode().equals(deviceAddressModel.getAdcode())){
                        return true;
                    }
                }
                break;
            default:
                return false;
        }

        return false;

    }

    private void justSaveAlert(Device device,DeviceAddressModel deviceAddressModel){
        logger.warn("====设备{}出现串货情况=====",device.getSno());
        DeviceAlarm alarm = new DeviceAlarm();
        alarm.setCtime(new Date());
        alarm.setName("串货警告");
        alarm.setAttr("串货警告");
        alarm.setHappenTime(new Date());
        alarm.setMac(device.getMac());
        alarm.setSno(device.getSno());
        Product product = productService.getProductByProductId(device.getProductId());
        if(Objects.nonNull(product)){
            alarm.setProductKey(product.getGizwitsProductKey());
        }

        alarm.setLongitude(deviceAddressModel.getLongitude());
        alarm.setLatitude(deviceAddressModel.getLatitude());
        alarm.setStatus(AlarmStatus.UNRESOLVE.getCode());
        alarm.setAlarmType(AlarmType.FLAUT.getCode());
        deviceAlarmService.insert(alarm);
    }

    private void createAndSaveAlarm(Device device,DeviceAddressModel deviceAddressModel){
        DeviceAlarm alarm = new DeviceAlarm();
        alarm.setCtime(new Date());
        alarm.setName(GizwitsDataPointResolver.ELECT_FENCE_NAME);
        alarm.setAttr(GizwitsDataPointResolver.ELECT_FENCE_NAME);
        alarm.setHappenTime(new Date());
        alarm.setMac(device.getMac());
        alarm.setSno(device.getSno());

        Product product = productService.getProductByProductId(device.getProductId());
        if(Objects.nonNull(product)){
            alarm.setProductKey(product.getGizwitsProductKey());
        }

        alarm.setLongitude(deviceAddressModel.getLongitude());
        alarm.setLatitude(deviceAddressModel.getLatitude());
        alarm.setStatus(AlarmStatus.UNRESOLVE.getCode());
        alarm.setAlarmType(AlarmType.FLAUT.getCode());
        deviceAlarmService.saveOne(alarm);
    }


}
