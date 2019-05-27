package com.gizwits.lease.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.api.SmsApi;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.entity.SysUserToRole;

import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.sys.service.SysUserToRoleService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.boot.utils.WebUtils;

import com.gizwits.lease.device.vo.DeviceAlarmListVo;

import com.gizwits.lease.device.entity.dto.DeviceAlarmListPageQueryDto;

import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.message.entity.SysMessageToUser;
import com.gizwits.lease.message.service.SysMessageToUserService;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.boot.sys.service.SysRoleService;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.sys.service.SysUserToRoleService;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.constant.AlarmStatus;
import com.gizwits.lease.constant.AlarmType;
import com.gizwits.lease.device.entity.dto.*;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.device.entity.dto.DeviceAlarmAppListDto;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.AlarmStatus;
import com.gizwits.lease.constant.AlarmType;
import com.gizwits.lease.device.dao.DeviceAlarmDao;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.message.entity.SysMessage;
import com.gizwits.lease.message.service.SysMessageService;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.util.WxUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 设备故障(警告)记录表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
@Service
public class DeviceAlarmServiceImpl extends ServiceImpl<DeviceAlarmDao, DeviceAlarm> implements DeviceAlarmService {
    protected final static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private ProductDataPointService productDataPointService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceAlarmDao deviceAlarmDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private SysUserToRoleService sysUserToRoleService;

    @Autowired
    private SysMessageToUserService sysMessageToUserService;

    @Override
    public Integer countDeviceAlram(DeviceAlarmQueryDto deviceAlarmQueryDto) {

        return deviceAlarmDao.countNum(deviceAlarmQueryDto);
    }

    public Integer countAppDeviceAlram(DeviceAlarmAppListDto deviceAlarmAppListDto) {

        return deviceAlarmDao.appCountNum(deviceAlarmAppListDto);
    }

    @Override
    public List<DeviceAlramInfoDto> listPage(DeviceAlarmQueryDto deviceAlarmQueryDto) {
        Integer pageSize = deviceAlarmQueryDto.getPageSize();
        Integer currentPage = deviceAlarmQueryDto.getCurrentPage();
        Integer start = (currentPage - 1) * pageSize;
        Integer end = currentPage * pageSize;
        deviceAlarmQueryDto.setStart(start);
        deviceAlarmQueryDto.setEnd(end);
        List<DeviceAlramInfoDto> deviceAlramInfoDtos = deviceAlarmDao.listPage(deviceAlarmQueryDto);
        for (DeviceAlramInfoDto d : deviceAlramInfoDtos) {
            resolveAlarmDuration(d);
            Integer alarmType = d.getAlarmType();
            if (alarmType != null) {
                String type = AlarmType.getName(alarmType);
                d.setDeviceAlarmType(type);
            }
            String status = AlarmStatus.getName(d.getStatus());
            d.setDeviceAlarmStatus(status);
            if (!ParamUtil.isNullOrEmptyOrZero(d.getDeviceLaunchAreaId())) {
                DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(d.getDeviceLaunchAreaId());
                d.setMobile(deviceLaunchArea.getMobile());
            }
        }

        return deviceAlramInfoDtos;
    }

    /**
     * 获取故障时长
     * @param d
     */
    private void resolveAlarmDuration(DeviceAlramInfoDto d) {
        Date happenTime = d.getHappenTime();
        Date fixTime = d.getFixTime();
        d.setFixTime(fixTime);
        if (fixTime == null) {
            fixTime = new Date();
        }
        d.setHappenTime(happenTime);
        long time = fixTime.getTime() - happenTime.getTime();
        d.setDeviceTime(time);
        d.setAlarmDuringTime(resolveDuringTime(time));
    }

    public List<DeviceAlramAppInfoDto> appListPage(DeviceAlarmAppListDto deviceAlarmAppListDto) {
        List<DeviceAlramAppInfoDto> deviceAlramAppInfoDtos = new ArrayList<>();
        List<DeviceAlramInfoDto> deviceAlramInfoDtos = new ArrayList<>();

        Integer pageSize = deviceAlarmAppListDto.getPageSize();
        Integer currentPage = deviceAlarmAppListDto.getCurrentPage();
        Integer start = (currentPage - 1) * pageSize;
        Integer end = currentPage * pageSize;
        deviceAlarmAppListDto.setStart(start);
        deviceAlarmAppListDto.setEnd(end);

        //找出用户及子用户下的设备序列号
        List<Product> products = productService.getProductsWithPermission();
        List<Integer> productIds = new ArrayList<>();
        for (Product p : products) {
            productIds.add(p.getId());
        }
        List<String> deviceSnos = deviceService.getSnosByProductId(productIds);

        if (deviceSnos.size() == 0) {
            return deviceAlramAppInfoDtos;
        }else {
            deviceAlarmAppListDto.setSnos(deviceSnos);
            deviceAlramInfoDtos = deviceAlarmDao.appListPage(deviceAlarmAppListDto);
            for (DeviceAlramInfoDto d : deviceAlramInfoDtos) {
                Date happenTime = d.getHappenTime();
                Date fixTime = d.getFixTime();
                if (fixTime == null) {
                    fixTime = new Date();
                }
                d.setHappenTime(happenTime);
                d.setFixTime(fixTime);
                long time = fixTime.getTime() - happenTime.getTime();
                d.setDeviceTime(time);
                Integer alarmType = d.getAlarmType();
                if (alarmType != null) {
                    String type = AlarmType.getAlarmTypeEnum(alarmType).getName();
                    d.setDeviceAlarmType(type);
                }
                Integer alarmStatus = d.getStatus();
                String status = AlarmStatus.getAlarmStatusEnum(alarmStatus).getName();
                d.setDeviceAlarmStatus(status);

                DeviceAlramAppInfoDto deviceAlramAppInfoDto = new DeviceAlramAppInfoDto();
                BeanUtils.copyProperties(d, deviceAlramAppInfoDto);

                if (!ParamUtil.isNullOrEmptyOrZero(d.getDeviceLaunchAreaId())) {
                    DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(d.getDeviceLaunchAreaId());
                    if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchArea)) {
                        d.setMobile(deviceLaunchArea.getMobile());
                        Integer maintainer_id = deviceLaunchArea.getMaintainerId();
                        if (!ParamUtil.isNullOrEmptyOrZero(maintainer_id)) {
                            SysUser sysUser = sysUserService.selectById(maintainer_id);
                            if (!ParamUtil.isNullOrEmptyOrZero(sysUser)) {
                                deviceAlramAppInfoDto.setMaintainerMobile(sysUser.getMobile());
                            }
                        }
                    }
                }

                //添加设备运行状态字段
                Device device = deviceService.getDeviceInfoBySno(d.getSno());
                if (!ParamUtil.isNullOrEmptyOrZero(device)) {
                    deviceAlramAppInfoDto.setWork_status(device.getWorkStatus());
                    deviceAlramAppInfoDto.setDeviceWorkStatus(DeviceAlramAppInfoDto.convertWorkStatusToString(device.getWorkStatus()));
                }
                deviceAlramAppInfoDtos.add(deviceAlramAppInfoDto);
            }
        }

        return deviceAlramAppInfoDtos;
    }

    @Override
    public DeviceAlarmDetailDto getDeviceAlramInfoById(Pageable<DeviceAlarmListPageQueryDto> pageable, Integer id) {
        DeviceAlarmDetailDto deviceAlarmDetailDto = new DeviceAlarmDetailDto();
        logger.info(" 查询设备故障信息byId：" + id);
        DeviceAlarm deviceAlarm = selectById(id);
        if(ParamUtil.isNullOrEmptyOrZero(deviceAlarm)){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        DeviceAlramInfoDto deviceAlramInfoDto = getDeviceAlramInfoDto(deviceAlarm);


        logger.info("  查询设备信息BySno：" + deviceAlarm.getSno());
        Device device = deviceService.getDeviceInfoBySno(deviceAlarm.getSno());
        if(ParamUtil.isNullOrEmptyOrZero(device)){
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        deviceAlramInfoDto.setDeviceName(device.getName());
        resolveAlarmDuration(deviceAlramInfoDto);
        deviceAlarmDetailDto.setDeviceSno(device.getSno());
        deviceAlarmDetailDto.setLongtitude(device.getLongitude());
        deviceAlarmDetailDto.setLatitude(device.getLatitude());


        Integer deviceLaunchAreaId = device.getLaunchAreaId();
        logger.info("查询设备投放点信息ById：" + deviceLaunchAreaId);
        if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchAreaId)) {
            DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(deviceLaunchAreaId);
            if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchArea)) {
                deviceAlarmDetailDto = getDeviceLaunchAreaInfo(deviceAlarmDetailDto, deviceAlramInfoDto, deviceLaunchArea);
                Integer maintainer_id = deviceLaunchArea.getMaintainerId();
                if (!ParamUtil.isNullOrEmptyOrZero(maintainer_id)) {
                    SysUser sysUser = sysUserService.selectById(maintainer_id);
                    if (!ParamUtil.isNullOrEmptyOrZero(sysUser)) {
                        deviceAlarmDetailDto.setMaintainerMobile(sysUser.getMobile());
                    }
                }
            }
        }

        logger.info("该设备所有的故障信息：" + deviceAlarm.getSno());
        deviceAlarmDetailDto.setPage(listPageByDeviceSon(pageable, device.getSno()));

        return deviceAlarmDetailDto;
    }

    public DeviceAlarmDetailDto getDeviceLaunchAreaInfo(DeviceAlarmDetailDto deviceAlarmDetailDto, DeviceAlramInfoDto deviceAlramInfoDto, DeviceLaunchArea deviceLaunchArea) {
        deviceAlramInfoDto.setDeviceLaunchArea(deviceLaunchArea.getName());
        deviceAlramInfoDto.setMaintainerId(deviceLaunchArea.getMaintainerId());
        deviceAlramInfoDto.setMaintainer(deviceLaunchArea.getMaintainerName());
        deviceAlramInfoDto.setPeopleInchargeName(deviceLaunchArea.getPersonInCharge());
        deviceAlramInfoDto.setMobile(deviceLaunchArea.getMobile());

        deviceAlarmDetailDto.setAddress(deviceLaunchArea.getAddress());
        deviceAlarmDetailDto.setProvince(deviceLaunchArea.getProvince());
        deviceAlarmDetailDto.setCity(deviceLaunchArea.getCity());
        if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchArea.getArea())) {
            deviceAlarmDetailDto.setArea(deviceLaunchArea.getArea());

        }
        deviceAlarmDetailDto.setDeviceAlramInfoDto(deviceAlramInfoDto);
        return deviceAlarmDetailDto;
    }

    @Override
    public Page<DeviceAlramInfoDto> listPageByDeviceSon(Pageable<DeviceAlarmListPageQueryDto> pageable, String deviceSno) {

        Page<DeviceAlarm> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<DeviceAlarm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno", deviceSno);
        Page<DeviceAlarm> page1 = selectPage(page,
                QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
        List<DeviceAlarm> deviceAlarms = page1.getRecords();

        List<DeviceAlramInfoDto> deviceAlramInfoDtos = new ArrayList<>();
        for (DeviceAlarm deviceAlarm : deviceAlarms) {
            DeviceAlramInfoDto deviceAlramInfoDto = getDeviceAlramInfoDto(deviceAlarm);
            resolveAlarmDuration(deviceAlramInfoDto);
            logger.info("  查询设备信息BySno：" + deviceAlarm.getSno());
            Device device = deviceService.getDeviceInfoBySno(deviceAlarm.getSno());
            deviceAlramInfoDto.setDeviceName(device.getName());

            Integer deviceLaunchAreaId = device.getLaunchAreaId();
            logger.info("查询设备投放点信息ById：" + deviceLaunchAreaId);
            if(!ParamUtil.isNullOrEmptyOrZero(deviceLaunchAreaId)) {
                DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(deviceLaunchAreaId);
                if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchArea)) {
                    deviceAlramInfoDto.setDeviceLaunchArea(deviceLaunchArea.getName());

                }
            }

            deviceAlramInfoDtos.add(deviceAlramInfoDto);
        }
        Page<DeviceAlramInfoDto> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        result.setRecords(deviceAlramInfoDtos);
        return result;
    }

    public DeviceAlramInfoDto getDeviceAlramInfoDto(DeviceAlarm deviceAlarm) {
        DeviceAlramInfoDto deviceAlramInfoDto = new DeviceAlramInfoDto();
        deviceAlramInfoDto.setAttr(deviceAlarm.getAttr());
        deviceAlramInfoDto.setDeviceMac(deviceAlarm.getMac());
        deviceAlramInfoDto.setDeviceAlarmName(deviceAlarm.getName());
        deviceAlramInfoDto.setDeviceMac(deviceAlarm.getMac());
        deviceAlramInfoDto.setSno(deviceAlarm.getSno());
        Date happenTime = deviceAlarm.getHappenTime();
        Date fixTime = deviceAlarm.getFixedTime();
        deviceAlramInfoDto.setFixTime(fixTime);
        if (ParamUtil.isNullOrEmptyOrZero(fixTime)) {
            fixTime = new Date();
        }
        deviceAlramInfoDto.setHappenTime(happenTime);
        long time = fixTime.getTime() - happenTime.getTime();
        deviceAlramInfoDto.setDeviceTime(time);
        deviceAlramInfoDto.setStatus(deviceAlarm.getStatus());
        String status = AlarmStatus.getName(deviceAlarm.getStatus());
        deviceAlramInfoDto.setDeviceAlarmStatus(status);
        return deviceAlramInfoDto;
    }

    @Override
    public List<String> getDeviceAlarmNameBySno(List<String> snos) {
        return deviceAlarmDao.getDeviceAlarmName(snos);
    }

    @Override
    public List<String> getDeviceAlarmAttrBySno(List<String> snos) {
        EntityWrapper<DeviceAlarm> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("sno", snos);
        List<DeviceAlarm> deviceAlarms = selectList(entityWrapper);
        List<String> attrs = new ArrayList<>();
        for (DeviceAlarm deviceAlarm : deviceAlarms) {
            attrs.add(deviceAlarm.getAttr());
        }
        return attrs;
    }

    /**
     * 保存并推送消息
     */
    public boolean saveOne(DeviceAlarm deviceAlarm) {
        if (insert(deviceAlarm) && deviceAlarm.getStatus().equals(AlarmStatus.UNRESOLVE.getCode())) {
            sendDeviceAlarmMessage(deviceAlarm);
            return true;
        }
        return false;
    }

    @Override
    public void sendDeviceAlarmMessage(DeviceAlarm deviceAlarm) {
        Device device = deviceService.getDeviceInfoBySno(deviceAlarm.getSno());
        if (Objects.isNull(device)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        ProductDataPoint productDataPoint = productDataPointService.getProductDataPointByProductIdAndIdentityName(device, deviceAlarm);
        Integer rank = productDataPoint.getDeviceAlarmRank();
        Integer deviceLaunchAreaId = device.getLaunchAreaId();
        logger.info("查询设备投放点信息ById：" + deviceLaunchAreaId);
        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(deviceLaunchAreaId);
        if (Objects.isNull(deviceLaunchArea)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
        }
        Integer maintainerId = deviceLaunchArea.getMaintainerId();
        logger.info("维护人员id="+maintainerId);
        String maintainer = deviceLaunchArea.getMaintainerName();
        String content = "设备mac:" + device.getMac() + "发生故障，故障信息："
                + deviceAlarm.getName() + ",发生时间：" + DateKit.getTimestampString(deviceAlarm.getHappenTime());
        String mobile = deviceLaunchArea.getMobile();
        logger.info("推送故障到系统消息：" + content);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setCtime(new Date());
        sysMessage.setUtime(new Date());
        sysMessage.setContent(content);
        sysMessage.setTitle("故障通报通知");
        //判断是否是HTTP请求
        if (RequestContextHolder.getRequestAttributes()!=null&&
                !Objects.isNull(WebUtils.getHeader(Constants.TOKEN_HEADER_NAME))) {
            SysUser sysUser = sysUserService.getCurrentUser();
            sysMessage.setAddresserId(sysUser.getId());
            sysMessage.setAddresserName(sysUser.getUsername());
        }
        if (rank == 2) {
            if (!ParamUtil.isNullOrEmptyOrZero(maintainerId)) {
                //发送故障信息到维护人员收件箱
                sysMessageService.insert(sysMessage);
                InsertSysMessageToUser(maintainerId, maintainer, sysMessage);
                deviceAlarm.setNotifyUserId(maintainerId);
                updateById(deviceAlarm);

            }
        }
        if (rank == 3) {
            if (!ParamUtil.isNullOrEmptyOrZero(maintainerId)) {
                //发送故障信息到维护人员收件箱
                sysMessageService.insert(sysMessage);
                InsertSysMessageToUser(maintainerId, maintainer, sysMessage);
                deviceAlarm.setNotifyUserId(maintainerId);
                updateById(deviceAlarm);

                SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(device.getSno());
                //推送故障信息到公众号
                String openid = sysUserExtService.selectById(maintainerId).getWxOpenId();
                if (ParamUtil.isNullOrEmptyOrZero(openid)) {
                    LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_OPENID_IS_NULL);
                }
                JSONObject sendData = new JSONObject();
                sendData.put("touser", openid);
                sendData.put("template_id", sysUserExt.getWxTemplateId());

                JSONObject firstData = new JSONObject();
                firstData.put("value", "设备mac" + deviceAlarm.getMac() + "发生故障");
                firstData.put("color", "#173177");

                JSONObject performanceData = new JSONObject();
                performanceData.put("value", deviceAlarm.getName());
                performanceData.put("color", "#173177");

                JSONObject timeData = new JSONObject();
                timeData.put("value", com.gizwits.boot.utils.DateKit.getTimestampString(deviceAlarm.getHappenTime()));
                timeData.put("color", "#173177");

                JSONObject remarkData = new JSONObject();
                remarkData.put("value", "请及时处理");
                remarkData.put("color", "#173177");

                JSONObject body = new JSONObject();
                body.put("first", firstData);
                body.put("performance", performanceData);
                body.put("time", timeData);
                body.put("remark", remarkData);

                sendData.put("data", body);
                logger.info("====发送故障信息-====>>>"+body);
                //发送模板消息
                WxUtil.sendTemplateNotice(sendData.toJSONString(), sysUserExt);
            } else {
                String tplValue = SysConfigUtils.get(CommonSystemConfig.class).getMessageDeviceAlarmTemplate();
                String apiKey = SysConfigUtils.get(CommonSystemConfig.class).getMessageApiKey();
                String templageId = SysConfigUtils.get(CommonSystemConfig.class).getMessageDeviceAlarmTemplateId();


                String mess = SmsApi.sendAlarmMessage(device.getMac(), deviceAlarm.getName(), DateKit.getTimestampString(deviceAlarm.getHappenTime()), mobile, templageId, tplValue, apiKey);
                logger.info("推送消息：" + mess);
            }
        }
    }

    public void InsertSysMessageToUser(Integer maintainerId, String maintainer, SysMessage sysMessage) {
        SysMessage message = sysMessageService.selectById(sysMessage.getId());
        SysMessageToUser sysMessageToUser = new SysMessageToUser();
        sysMessageToUser.setCtime(sysMessage.getCtime());
        sysMessageToUser.setUtime(sysMessage.getUtime());
        sysMessageToUser.setUserId(maintainerId);
        sysMessageToUser.setUsername(maintainer);
        sysMessageToUser.setSysMessageId(message.getId());
        Integer roleId = sysUserToRoleService.selectList(new EntityWrapper<SysUserToRole>()
                .eq("user_id", maintainerId)).get(0).getRoleId();
        sysMessageToUser.setRoleId(roleId);
        sysMessageToUser.setRoleName("维护人员");
        sysMessageToUserService.insert(sysMessageToUser);
    }

    /**
     * 查找设备指定的未解决故障
     */
    public DeviceAlarm getUnresolveAlarm(String mac, String productKey, String attr) {
        if (StringUtils.isBlank(mac) || StringUtils.isBlank(productKey) || StringUtils.isBlank(attr)) {
            return null;
        }
        return deviceAlarmDao.findDeviceUnresolveAlarm(mac, productKey, attr, AlarmStatus.UNRESOLVE.getCode());
    }

    @Override
    public List<String> getDeviceSnos(DeviceAlarmQueryDto deviceAlarmQueryDto) {
        List<String> deviceSno = new ArrayList<>();
        List<Integer> productIds = new ArrayList<>();
        if (ParamUtil.isNullOrEmptyOrZero(deviceAlarmQueryDto.getProductId())) {
            List<Product> products = productService.getProductsWithPermission();
            for (Product p : products) {
                productIds.add(p.getId());
            }
        } else {
            Integer productId = deviceAlarmQueryDto.getProductId();
            productIds.add(productId);
        }
        deviceSno = deviceService.getSnosByProductId(productIds);
        deviceAlarmQueryDto.setSnos(deviceSno);
        return deviceSno;
    }

    @Override
    public List<String> getSnos(Integer productId) {
        List<String> deviceSnos = new ArrayList<>();
        if (ParamUtil.isNullOrEmptyOrZero(productId)) {
            List<Product> products = productService.getProductsWithPermission();
            List<Integer> productIds = new ArrayList<>();
            for (Product p : products) {
                productIds.add(p.getId());
            }
            deviceSnos = deviceService.getSnosByProductId(productIds);
        } else {
            List<Integer> ids = new ArrayList<>();
            ids.add(productId);
            deviceSnos = deviceService.getSnosByProductId(ids);
        }
        return deviceSnos;
    }

    @Override
    public DeviceAlarm getDeviceAlarmByProductDataPoint(ProductDataPoint productDataPoint) {
        EntityWrapper<DeviceAlarm> entityWrapper = new EntityWrapper<>();
        Integer productId = productDataPoint.getProductId();
        List<Integer> ids = new ArrayList<>();
        ids.add(productId);
        List<String> snos = deviceService.getSnosByProductId(ids);
        entityWrapper.in("sno", snos).eq("attr", productDataPoint.getIdentityName())
                .eq("status", 0);
        return selectOne(entityWrapper);
    }

    @Override
    public Page getPage(DeviceAlarmQueryDto deviceAlarmQueryDto) {
        List<String> deviceSno = new ArrayList<>();
        deviceSno = getDeviceSnos(deviceAlarmQueryDto);
        int count = 0;
        Page<DeviceAlramInfoDto> page = new Page<>();
        if (!ParamUtil.isNullOrEmptyOrZero(deviceSno)) {
            deviceAlarmQueryDto.setSnos(deviceSno);
            logger.info(" 设备序列号：" + deviceSno.toString() + "，设备序列号查询：" + deviceAlarmQueryDto.getDeviceSno());
            List<DeviceAlramInfoDto> deviceAlramInfoDtos = listPage(deviceAlarmQueryDto);
            count = countDeviceAlram(deviceAlarmQueryDto);
            page.setRecords(deviceAlramInfoDtos);
            page.setCurrent(deviceAlarmQueryDto.getCurrentPage());
            page.setTotal(count);
            page.setSize(deviceAlarmQueryDto.getPageSize());
        }
        return page;
    }

    public Page getAppPage(DeviceAlarmAppListDto deviceAlarmAppListDto) {
        int count = 0;
        Page page = new Page();

        List<DeviceAlramAppInfoDto> deviceAlramInfoDtos = appListPage(deviceAlarmAppListDto);
        count = countAppDeviceAlram(deviceAlarmAppListDto);
        page.setRecords(deviceAlramInfoDtos);
        page.setCurrent(deviceAlarmAppListDto.getCurrentPage());
        page.setTotal(count);
        page.setSize(deviceAlarmAppListDto.getPageSize());
        return page;
    }

    @Override
    public List<String> getDeviceSnoByProductId(Integer productId) {
        List<Integer> productIds = new ArrayList<>();
        if (ParamUtil.isNullOrEmptyOrZero(productId)) {
            List<Product> products = productService.getProductsWithPermission();
            for (Product p : products) {
                productIds.add(p.getId());
            }
        } else {
            productIds.add(productId);
        }
        return deviceService.getSnosByProductId(productIds);
    }

    private String resolveDuringTime(long duration) {
        long day = duration / (24 * 60 * 60 * 1000);
        long hour = (duration - day * 24 * 60 * 60 * 1000) / (60 * 60 * 1000);
        long minute = (duration - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (duration - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append("天");
        }
        if (!StringUtils.isEmpty(sb.toString()) || hour > 0L) {
            sb.append(hour).append("时");
        }
        if (StringUtils.isNotEmpty(sb.toString()) || minute > 0L) {
            sb.append(minute).append("分");
        }
        sb.append(second).append("秒");
        return sb.toString();
    }

    @Override
    public Page<DeviceAlarmListVo> deviceAlarmListManager(Pageable<String> pageable) {
        Page<DeviceAlarm> page = new Page<>();
        BeanUtils.copyProperties(pageable,page);
        SysUser sysUser = sysUserService.getCurrentUser();
        EntityWrapper<DeviceAlarm> entityWrapper  = new EntityWrapper<DeviceAlarm>();
        if(!ParamUtil.isNullOrEmptyOrZero(pageable) && (!ParamUtil.isNullOrEmptyOrZero(pageable.getQuery()))){
            entityWrapper.eq("sno",pageable.getQuery());
        } else{
            List<String> snos = deviceLaunchAreaService.getDeviceSnoByMaintainerId(sysUser.getId());
            entityWrapper.in("sno",snos);
        }
        entityWrapper.orderBy("happen_time",false);
        Page<DeviceAlarm> page1 = selectPage(page,QueryResolverUtils.parse(pageable.getQuery(),entityWrapper));
        List<DeviceAlarm> deviceAlarms = page1.getRecords();
        List<DeviceAlarmListVo> deviceAlarmListVos = new ArrayList<>(deviceAlarms.size());
        deviceAlarms.forEach(item->{
            DeviceAlarmListVo deviceAlarmListVo = new DeviceAlarmListVo();
            deviceAlarmListVo.setAlarmId(item.getId());
            deviceAlarmListVo.setName(item.getName());
            Device device = deviceService.selectOne(new EntityWrapper<Device>().eq("sno",item.getSno()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            if(ParamUtil.isNullOrEmptyOrZero(device)){
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
            }
            deviceAlarmListVo.setDeviceName(device.getName());
            deviceAlarmListVo.setHappenTime(item.getCtime());
            deviceAlarmListVo.setStatus(AlarmStatus.getName(item.getStatus()));
            deviceAlarmListVos.add(deviceAlarmListVo);
        });
        Page<DeviceAlarmListVo> result = new Page<>();
        BeanUtils.copyProperties(page1,result);
        result.setRecords(deviceAlarmListVos);
        return result;
    }
}
