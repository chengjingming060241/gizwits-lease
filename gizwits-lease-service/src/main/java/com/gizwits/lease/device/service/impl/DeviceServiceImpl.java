package com.gizwits.lease.device.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.*;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetailDevice;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailDeviceService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.common.perm.SysUserRoleTypeHelperResolver;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.BooleanEnum;
import com.gizwits.lease.constant.CommandType;
import com.gizwits.lease.constant.DeviceOnlineStatus;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.constant.DeviceWorkStatus;
import com.gizwits.lease.constant.LocationType;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.QrcodeType;
import com.gizwits.lease.device.dao.DeviceDao;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceExt;
import com.gizwits.lease.device.entity.DeviceGroupToDevice;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.DeviceServiceModeSetting;
import com.gizwits.lease.device.entity.dto.*;
import com.gizwits.lease.device.service.DeviceExtService;
import com.gizwits.lease.device.service.DeviceGroupToDeviceService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.service.DeviceServiceModeSettingService;
import com.gizwits.lease.device.vo.BatchDeviceDetailWebSocketVo;
import com.gizwits.lease.device.vo.BatchDevicePageDto;
import com.gizwits.lease.device.vo.BatchDeviceWebSocketVo;
import com.gizwits.lease.device.vo.DeviceAuth;
import com.gizwits.lease.device.vo.DeviceDetailWebSocketVo;
import com.gizwits.lease.device.vo.DevicePageDto;
import com.gizwits.lease.device.vo.DevicePortDto;
import com.gizwits.lease.device.vo.DeviceWebSocketVo;
import com.gizwits.lease.enums.*;
import com.gizwits.lease.event.DeviceLocationEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.dto.OperatorAllotDeviceDto;
import com.gizwits.lease.manager.dto.OperatorExtDto;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.model.DeviceAddressModel;
import com.gizwits.lease.model.MapDataEntity;
import com.gizwits.lease.operator.entity.OperatorExt;
import com.gizwits.lease.operator.service.OperatorExtService;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderExtPortService;
import com.gizwits.lease.product.dto.*;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductDeviceChangeService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.user.service.UserWeixinService;
import com.gizwits.lease.util.DeviceControlAPI;
import com.gizwits.lease.util.GizwitsUtil;
import com.gizwits.lease.util.QrcodeUtil;
import com.gizwits.lease.util.WxUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-07-11
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceDao, Device> implements DeviceService {
    protected static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceDao deviceDao;


    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorExtService operatorExtService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired

    private SysUserExtService sysUserExtService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @Autowired
    private ProductServiceDetailService productServiceDetailService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private ProductDeviceChangeService productDeviceChangeService;

    @Autowired
    private ShareBenefitRuleDetailDeviceService shareBenefitRuleDetailDeviceService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private OrderBaseService orderBaseService;


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDataPointService productDataPointService;

    @Autowired
    private UserWeixinService userWeixinService;

    @Autowired
    private DeviceGroupToDeviceService deviceGroupToDeviceService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private UserService userService;
    @Autowired
    private DeviceServiceModeSettingService deviceServiceModeSettingService;

    @Autowired
    private SysUserRoleTypeHelperResolver resolverHelper;

    @Autowired
    private DeviceExtService deviceExtService;

    @Autowired
    private OrderExtPortService orderExtPortService;

    @Override
    public Integer countDeviceByProductId(Integer productId) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("product_id", productId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectCount(entityWrapper);

    }

    @Override
    public List<Integer> getProductIdByDeviceSno(List<String> snos) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("sno", snos);
        List<Device> devices = selectList(entityWrapper);
        List<Integer> ids = new ArrayList<>();
        for (Device device : devices) {
            ids.add(device.getProductId());
        }
        return ids;
    }

    @Override
    public List<Integer> getDeviceLaunchAreaIdIdByDeviceSno(List<String> snos) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("sno", snos);
        List<Device> devices = selectList(entityWrapper);
        List<Integer> ids = new ArrayList<>();
        for (Device device : devices) {
            ids.add(device.getLaunchAreaId());
        }
        return ids;
    }

    @Override
    public Integer countDeviceByDeviceLaunchAreaId(Integer deviceLaunchAreaId) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("launch_area_id", deviceLaunchAreaId)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectCount(entityWrapper);
    }


    /**
     * 通过设备ID获取设备所属的微信运营配置
     */
    public SysUserExt getWxConfigByDeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        Device device = selectById(deviceId);
        if (device == null) {
            return null;
        }

        return operatorService.getWxConfigByOperator(device.getOwnerId());
    }

    /**
     * 检查设备是否投入运营
     */
    public boolean checkDeviceIsInOperator(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return false;
        }
        Device device = selectById(deviceId);
        if (device == null) {

            return false;
        }
        if (ParamUtil.isNullOrEmptyOrZero(device.getOwnerId())) {
            return false;
        }
        Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", device.getOwnerId()));
        if (operator == null) {
            return false;
        }

        return Objects.equals(operator.getStatus(), StatusType.OPERATING.getCode());
    }


    /**
     * 获取设备直接运营商的SysUserid
     */
    public Integer getDeviceOperatorSysuserid(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }
        Device device = selectById(deviceId);
        if (device == null) {
            return null;
        }
        if (ParamUtil.isNullOrEmptyOrZero(device.getOwnerId())) {
            return null;
        }
        Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", device.getOwnerId()));
        if (operator == null) {

            return null;
        }
        return operator.getSysAccountId();
    }

    @Override
    public Device getDeviceInfoBySno(String deviceSno) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno", deviceSno).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectOne(entityWrapper);
    }

    /**
     * 根据Mac和ProductKey获取设备信息
     */
    public Device getDeviceByMacAndPk(String mac, String productKey) {
        if (StringUtils.isBlank(mac) || StringUtils.isBlank(productKey)) {
            return null;
        }
        return deviceDao.findByMacAndProductKey(mac, productKey);
    }

    /**
     * 从机智云接口获取坐标
     */
    public DeviceAddressModel getDeviceAddressByGizwitsAPI(String productKey, String did, String mac) {
        DeviceAddressModel addressDTO = DeviceControlAPI.getDeviceAddress(productKey, did);
        return addressDTO;
    }

    /**
     * 根据设备上报的数据点调用高德地图接口
     *
     * @param productKey
     * @param mac
     * @param networkType
     * @return
     */
    public DeviceAddressModel getDeviceAddressByGD(String productKey, String mac, String networkType) {
        if (StringUtils.isBlank(productKey) || StringUtils.isBlank(mac)) {
            return null;
        }
        if (!redisService.containsDeviceCurrentStatusData(productKey, mac)) {//设备还未上报数据点
            return null;
        }
        JSONObject cacheData = redisService.getDeviceCurrentStatus(productKey, mac);
        if (Objects.equals(cacheData, null)) {
            logger.warn("=====设备{}未上报相应的基站信息,无法执行定位操作======");
            return null;
        }
        MapDataEntity mapDataEntity = JSONObject.parseObject(cacheData.toJSONString(), MapDataEntity.class);
        if (mapDataEntity == null) {
            return null;
        }
        mapDataEntity.setImei("460");
        // TODO: 2017/8/26 mnc参数通过系统配置,判断是移动还是联通,0是移动，1是联通
        mapDataEntity.setMnc(networkType);
        mapDataEntity.setLac1(Integer.parseInt(mapDataEntity.getLac1(), 16) + "");
        mapDataEntity.setCellid1(Integer.parseInt(mapDataEntity.getCellid1(), 16) + "");
        mapDataEntity.setRssi1(Integer.parseInt(mapDataEntity.getRssi1(), 16) + "");

        mapDataEntity.setLac2(Integer.parseInt(mapDataEntity.getLac2(), 16) + "");
        mapDataEntity.setCellid2(Integer.parseInt(mapDataEntity.getCellid2(), 16) + "");
        mapDataEntity.setRssi2(Integer.parseInt(mapDataEntity.getRssi2(), 16) + "");

        mapDataEntity.setLac2(Integer.parseInt(mapDataEntity.getLac2(), 16) + "");
        mapDataEntity.setCellid2(Integer.parseInt(mapDataEntity.getCellid2(), 16) + "");
        mapDataEntity.setRssi2(Integer.parseInt(mapDataEntity.getRssi2(), 16) + "");

        mapDataEntity.setLac3(Integer.parseInt(mapDataEntity.getLac3(), 16) + "");
        mapDataEntity.setCellid3(Integer.parseInt(mapDataEntity.getCellid3(), 16) + "");
        mapDataEntity.setRssi3(Integer.parseInt(mapDataEntity.getRssi3(), 16) + "");


        return DeviceControlAPI.getDeviceAddressByGDMap(mapDataEntity);
    }

    /**
     * 设备控制
     */
    @Override
    public boolean remoteDeviceControl(String deviceId, String name, Object value) {
        if (ParamUtil.isNullOrEmptyOrZero(deviceId)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        Device device = selectById(deviceId);
        if (device == null) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        if (StringUtils.isBlank(device.getGizDid())
                || DeviceStatus.OFFLINE.getCode().equals(device.getOnlineStatus())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_OFFLINE);
        }
        Product product = productService.getProductByProductId(device.getProductId());
        return DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), name, value);

    }

    public boolean remoteDeviceControl(String deviceId, JSONObject attrs) {
        if (ParamUtil.isNullOrEmptyOrZero(deviceId)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        Device device = selectById(deviceId);
        if (device == null) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        if (StringUtils.isBlank(device.getGizDid())
                || DeviceStatus.OFFLINE.getCode().equals(device.getOnlineStatus())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_OFFLINE);
        }
        Product product = productService.getProductByProductId(device.getProductId());
        return DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), attrs);
    }

    /**
     * 获取设备的实时数据点状态
     */
    @Override
    public String getDeviceNowTimeStatus(String sno) {
        if (ParamUtil.isNullOrEmptyOrZero(sno)) {
            return null;
        }
        Device device = selectById(sno);
        if (device == null) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        if (StringUtils.isBlank(device.getGizDid())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_OFFLINE);
        }
        Product product = productService.getProductByProductId(device.getProductId());
        return DeviceControlAPI.deviceNowTimeData(device.getGizDid(), product.getGizwitsAppId());
    }

    /**
     * 下发设备实时状态查询指令,设备在2s内上报所有数据点
     *
     * @param deviceId
     * @param username
     * @return
     */
    @Override
    public boolean sendDeviceStatusQueryCommand(String deviceId, String username) {
        if (ParamUtil.isNullOrEmptyOrZero(deviceId)) {
            return false;
        }
        Device device = selectById(deviceId);
        if (device == null) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
            return false;
        }
        if (StringUtils.isBlank(device.getGizDid())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_OFFLINE);
        }
        Product product = productService.getProductByProductId(device.getProductId());
        return DeviceControlAPI.sendDataQueryCommand(device.getGizDid(), product.getGizwitsProductKey());
    }

    /**
     * 根据订单发送控制指令
     */
    public boolean remoteDeviceControlByOrder(OrderBase orderBase) {
        if (ParamUtil.isNullOrEmptyOrZero(orderBase.getSno())
                || ParamUtil.isNullOrEmptyOrZero(orderBase.getCommand())) {
            return false;
        }

        Device device = selectById(orderBase.getSno());
        if (device == null || ParamUtil.isNullOrEmptyOrZero(device.getGizDid())) {
            //获取设备的gizDid
            return false;
        }
        Product product = productService.getProductByProductId(device.getProductId());

        return DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), orderBase.getCommand());
    }

    /**
     * 更新设备的在线状态
     */
    public boolean updateDeviceOnOffLineStatus(String mac, String productKey, String did, boolean isOnline,String leaseType) {
        Device device = getDeviceByMacAndPk(mac, productKey);
        if (device != null && StringUtils.isNotBlank(did)) {
            device.setGizDid(did);
            device.setUtime(new Date());
            if (isOnline && !DeviceStatus.ONLINE.getCode().equals(device.getOnlineStatus())) {
                device.setOnlineStatus(DeviceStatus.ONLINE.getCode());
                if (device.getWorkStatus().equals(DeviceStatus.OFFLINE.getCode())) {
                    device.setWorkStatus(DeviceStatus.ONLINE.getCode());
                    //判断设备的租赁模式
                    if (!ParamUtil.isNullOrEmptyOrZero(leaseType)&&LeaseType.WATER.getCode().equals(leaseType)&&!ParamUtil.isNullOrEmptyOrZero(device.getExpirationTime())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("a7Operationmode","02");
                        Product product = productService.getProductByDeviceSno(device.getSno());
                        if (!ParamUtil.isNullOrEmptyOrZero(product)&&!ParamUtil.isNullOrEmptyOrZero(device.getGizDid())) {
                             DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), jsonObject);
                        }
                    }
                }
                device.setLastOnlineTime(new Date());

                /***调整设备的经纬度信息***/
                locationDeviceAddress(productKey, mac);
            } else if (!isOnline && !DeviceStatus.OFFLINE.getCode().equals(device.getOnlineStatus())) {
                if (device.getWorkStatus().equals(DeviceStatus.ONLINE.getCode())) {
                    device.setWorkStatus(DeviceStatus.OFFLINE.getCode());
                }
                device.setOnlineStatus(DeviceStatus.OFFLINE.getCode());
            }
            return updateById(device);
        }
        return false;
    }

    @Override
    public List<String> getSnosByProductId(List<Integer> productId) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("product_id", productId);
        List<Device> list = selectList(entityWrapper);
        List<String> snos = new ArrayList<>(list.size());
        for (Device d : list) {
            snos.add(d.getSno());
        }
        return snos;
    }

    public List<Integer> getDeviceLaunchAreaIdByProductId(Integer productId) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("product_id", productId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        List<Device> deviceList = selectList(entityWrapper);
        List<Integer> deviceLaunchAreaId = new ArrayList<>(deviceList.size());
        for (Device device : deviceList) {
            deviceLaunchAreaId.add(device.getLaunchAreaId());
        }
        return deviceLaunchAreaId;
    }

    public void addDevice(DeviceAddDto deviceAddDto) {
        SysUser sysUser = sysUserService.getCurrentUser();
        Device device = new Device();
        String sno = getSno();
        device.setSno(sno);
        device.setUtime(new Date());
        device.setCtime(new Date());
        if (judgeMacIsExsit(deviceAddDto.getMac())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_EXISTS);
        }
        //判断产品是否符合规则
        Product product = judgeProductIsExist(deviceAddDto.getProductId());
        if (Objects.isNull(product)) {
            LeaseException.throwSystemException(LeaseExceEnums.PRODUCT_DONT_EXISTS);
        }
        device.setMac(deviceAddDto.getMac());
        device.setName(deviceAddDto.getName());
        device.setLaunchAreaId(deviceAddDto.getDeviceLaunchAreaId());
        device.setLaunchAreaName(deviceAddDto.getDeivceLaunchAreaName());
        if (!ParamUtil.isNullOrEmptyOrZero(deviceAddDto.getProductServiceModeId())) {
            device.setServiceId(deviceAddDto.getProductServiceModeId());
            device.setServiceName(deviceAddDto.getProductServiceModeName());
        }

        device.setProductId(deviceAddDto.getProductId());
        device.setProductName(deviceAddDto.getProductName());
        device.setSysUserId(sysUser.getId());
        //关键
        device.setOwnerId(sysUser.getId());

        //创建二维码
        device = createQrcodeAndAuthDevice(device, product, sysUser);

        device.setStatus(0);
       /* device.setWorkStatus(1);
        device.setOnlineStatus(1);
        device.setFaultStatus(0);*/
        device.setSysUserId(sysUser.getId());
        device.setTotalWater(deviceAddDto.getTotalWater());
        boolean flag = insert(device);
        //如果设备有出水口 需要添加
        if (flag && !ParamUtil.isNullOrEmptyOrZero(deviceAddDto.getPortDtos())) {
            for (DeviceAddPortDto portDto : deviceAddDto.getPortDtos()) {
                DeviceExt deviceExt = new DeviceExt();
                deviceExt.setCtime(new Date());
                deviceExt.setUtime(new Date());
                deviceExt.setSno(device.getSno());
                deviceExt.setPort(portDto.getPort());
                deviceExt.setMac(device.getMac());
                deviceExt.setPortType(portDto.getWaterType());
                deviceExt.setStatus(DeviceWorkStatus.FREE.getCode());
                deviceExt.setSort(portDto.getSort());
                deviceExtService.insert(deviceExt);
            }

        }

    }

    /**
     * 返回含有二维码内容的device
     */
    @Override
    public Device createQrcodeAndAuthDevice(Device device, Product product, SysUser currentUser) {
        //根据当前登录用户获取公众号信息
        SysUserExt sysUserExt = sysUserExtService.selectById(currentUser.getId());
        Map<String, String> map = QrcodeUtil.createAndSaveQrcode(device, product, sysUserExt);
        //如果是生成微信二维码，则需要微信授权
        if (product.getQrcodeType().equals(QrcodeType.WEIXIN.getCode())) {
            //判断map中是否存在wxDid
            if (!map.containsKey("wxDid")) {
                LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_DEVICE_ID_GET_ERROR);
            }
            device.setWxDid(String.valueOf(map.get("wxDid")));
            device.setWxTicket(String.valueOf(map.get("content")));
            deviceAuthResult(device, sysUserExt);
        } else if (product.getQrcodeType().equals(QrcodeType.WEB.getCode())) {
            device.setContentUrl(map.get("content"));
        }
        return device;
    }

    private Product judgeProductIsExist(Integer productId) {
        Product product = productService.selectById(productId);
        //如果产品被删除或者产品为空
        if (product.getIsDeleted().equals(1) || Objects.isNull(product)) {
            return null;
        }
        return product;
    }

    @Override
    public String getSno() {
        String sno = RandomStringUtils.random(20, false, true);
        if (judgeSnoIsExist(sno)) {
            sno = RandomStringUtils.random(20, false, true);
        }
        return sno;
    }


    private boolean judgeSnoIsExist(String sno) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno", sno);
        Device device = selectOne(entityWrapper);
        return !ParamUtil.isNullOrEmptyOrZero(device);
    }

    @Override
    public boolean judgeMacIsExsit(String mac) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("mac", mac).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        Device device = selectOne(entityWrapper);
        return !ParamUtil.isNullOrEmptyOrZero(device);

    }

    /**
     * 获取指定设备的经纬度信息并更新到数据库
     *
     * @param productKey
     * @param mac
     * @return
     */
    private Device locationDeviceAddress(String productKey, String mac) {
        Device device = getDeviceByMacAndPk(mac, productKey);
        if (Objects.isNull(device)) {
            logger.error("=====产品{}的设备{}不存在====", productKey, mac);
            return null;
        }
        Product product = productService.getProductByProductId(device.getProductId());
        if (Objects.isNull(product)) {
            logger.error("====产品ID为{}的产品PK{}不存在=====", device.getProductId(), productKey);
            return null;
        }
        DeviceAddressModel deviceAddressModel = null;
        if (product.getLocationType().equals(LocationType.GD.getCode())) {
            logger.info("====使用高德地图获取设备经纬度======");
            deviceAddressModel = getDeviceAddressByGD(product.getGizwitsProductKey(), device.getMac(), product.getNetworkType());
        } else if (product.getLocationType().equals(LocationType.GIZWITS.getCode())) {
            logger.info("====使用机智云接口获取设备经纬度======");
            if (StringUtils.isBlank(device.getGizDid())) {
                logger.error("====设备{}的GizDid为空,无法使用机智云接口获取设备经纬度====", device.getSno());
                return device;
            }
            deviceAddressModel = getDeviceAddressByGizwitsAPI(product.getGizwitsProductKey(), device.getGizDid(), device.getMac());
        }

        if (Objects.nonNull(deviceAddressModel)) {
            logger.info("====获取经纬度不为空======");
            //定位后做额外操作
            CommonEventPublisherUtils.publishEvent(new DeviceLocationEvent("DeviceLocation", deviceAddressModel, device));

            device.setLatitude(deviceAddressModel.getLatitude());
            device.setLongitude(deviceAddressModel.getLongitude());
            device.setUtime(new Date());
            updateById(device);
        }

        return device;
    }

    @Override
    public DeviceForDetailDto detailForApp(String sno) {
        Device device = selectById(sno);

        if (Objects.isNull(device)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        Product product = productService.getProductByProductId(device.getProductId());

        try {
            device = locationDeviceAddress(product.getGizwitsProductKey(), device.getMac());
        } catch (Exception e) {
            logger.error("The device has no base station information. mac:{}",device.getMac(),e);
        }
        DeviceForDetailDto result = new DeviceForDetailDto(device);
	    result.setWorkStatusDesc(DeviceStatus.getShowName(device.getWorkStatus(), device.getFaultStatus(), device.getLock()));

	    if (device.getFaultStatus().equals(DeviceStatus.FAULT.getCode())) {
		    result.setWorkStatus(device.getFaultStatus());
	    }
	    result.setProductKey(product.getGizwitsProductKey());
        result.setOnlineStatusDesc(DeviceStatus.getName(result.getOnlineStatus()));
        result.setServiceMode(resolveServiceMode(device.getServiceId()));
        result.setLaunchArea(resolveLaunchArea(device.getLaunchAreaId()));

        if (!ParamUtil.isNullOrEmptyOrZero(result.getLaunchArea())) {
            result.setLatitude(result.getLaunchArea().getLatitude());
            result.setLongitude(result.getLaunchArea().getLongitude());
        }
//        result.setCanModifyLaunchArea(deviceLaunchAreaAssignService.canModify(device.getLaunchAreaId()));
        result.setBelongOperatorName(resolveOwner(device.getOwnerId()));
        result.setControlCommands(getDeviceControlCommandWithNowStatus(product, device.getMac()));
        //判断设备运营商是否存在
        Integer operatorSysUserId = getDeviceOperatorSysuserid(sno);

        if (operatorSysUserId != null) {
            Operator operator = operatorService.getOperatorByAccountId(operatorSysUserId);

            if (operator != null) {
                result.setLogoUrl(operator.getLogoUrl());
                result.setDescription(operator.getDescription());
                result.setWebSite(operator.getWebSite());
                result.setPhone(operator.getPhone());
                OperatorExtDto extDto = new OperatorExtDto();
                extDto.setLogoUrl(operator.getLogoUrl());
                extDto.setDescription(operator.getDescription());
                extDto.setPhone(operator.getPhone());
                EntityWrapper<OperatorExt> extEntityWrapper = new EntityWrapper<>();
                extEntityWrapper.eq("operator_id", operator.getId());
                OperatorExt operatorExt = operatorExtService.selectOne(extEntityWrapper);

                if (operatorExt != null) {
                    BeanUtils.copyProperties(operatorExt, extDto);
                }
                result.setOperatorExt(extDto);
            }
        }

        // 查询设备出水口
        List<DeviceExt> deviceExts = deviceExtService.selectList(new EntityWrapper<DeviceExt>().eq("sno", sno).orderBy("port", true));

        if (!ParamUtil.isNullOrEmptyOrZero(deviceExts)) {

            for (DeviceExt deviceExt : deviceExts) {
                DevicePortDto portDto = new DevicePortDto();
                portDto.setId(deviceExt.getId());
                portDto.setPort(deviceExt.getPort());
                portDto.setStatus(DeviceStatus.getName(deviceExt.getStatus()));
                portDto.setWater(deviceExt.getPortType());
                portDto.setSort(deviceExt.getSort());
                result.getDevicePortDtoList().add(portDto);
            }
        }
        JSONObject existDevice = redisService.getDevicePortCurrentStatus(product.getGizwitsProductKey(), device.getMac());

        if (!ParamUtil.isNullOrEmptyOrZero(existDevice)){
            String leaseType = existDevice.getString("a7Operationmode");
            result.setLeaseType(leaseType);
        }

        // 设备到期时间判断
        Date expirationTime = device.getExpirationTime();
        result.setExpirationTime(expirationTime);
        if (!ParamUtil.isNullOrEmptyOrZero(expirationTime)) {
            result.setLeaseType(LeaseType.NORMAL.getCode());
            Date now = new Date();

            try {

                if (daysBetween(now, expirationTime) <= 7) {
                    result.setTime(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (ParamUtil.isNullOrEmptyOrZero(result.getLeaseType())){
            result.setLeaseType(LeaseType.WATER.getCode());
        }

        // 根据数据点a29heatingtemperature1最新状态更新剩余水量
        if (!ParamUtil.isNullOrEmpty(device.getGizDid())) {
            String lastDeviceData = getDeviceNowTimeStatus(device.getSno());
            JSONObject lastDeviveJson = JSON.parseObject(lastDeviceData);

            if (lastDeviveJson != null) {
                JSONObject res = lastDeviveJson.getJSONObject("attr");

                if (res != null) {
                    Double remainWater = res.getDouble("a29heatingtemperature1");
                    device.setRemainWater(remainWater);
                    updateById(device);
                }
            }
        }

        // 用水量
        if (device.getTotalWater() != null && device.getRemainWater() != null) {
            result.setTotalWater(device.getTotalWater());
            result.setRemainWater(device.getRemainWater());
            result.setUsedWater(device.getTotalWater() - device.getRemainWater());
        }

        // 是否投入运营
        result.setOperateStatus(device.getOperateStatus());

        return result;
    }

    @Override
    public DeviceForDetailDto detail(String id) {
        DeviceForDetailDto result = detailForApp(id);
        Device device = selectById(id);

        //查看是否有权限修改
        SysUser sysUser = sysUserService.getCurrentUser();
        if (Objects.equals(device.getOwnerId(), sysUser.getId())) {
            result.setCanModify(true);
            result.setCanModifyLaunchArea(true);
        }
        //只有厂商层级具有修改收费模式的权限
        if (Objects.equals(device.getSysUserId(),sysUser.getId())){
            result.setCanModifyServiceMode(true);
            result.setCanModify(true);
            result.setCanModifyLaunchArea(true);
        }

        return result;
    }

    private List<ProductCommandForDeviceDetailDto> getDeviceControlCommandWithNowStatus(Product product, String mac) {
        List<ProductCommandConfig> commandConfigList = productCommandConfigService.selectList(
                new EntityWrapper<ProductCommandConfig>()
                        .eq("product_id", product.getId())
                        .eq("command_type", CommandType.CONTROL.getCode())
                        .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                        .eq("is_show", ShowType.DISPLAY.getCode()));
        if (CollectionUtils.isNotEmpty(commandConfigList)) {
            List<ProductCommandForDeviceDetailDto> result = new ArrayList<>();
            for (ProductCommandConfig commandConfig : commandConfigList) {
                ProductCommandForDeviceDetailDto deviceDetailDto = new ProductCommandForDeviceDetailDto(commandConfig);
                if (redisService.containsDeviceCurrentStatusData(product.getGizwitsProductKey(), mac)) {
                    JSONObject cacheData = redisService.getDeviceCurrentStatus(product.getGizwitsProductKey(), mac);
                    JSONObject command = JSONObject.parseObject(commandConfig.getCommand());
                    if (command.containsKey("displayName")) {
                        String displayName = command.getString("displayName");
                        deviceDetailDto.setValue(cacheData.get(displayName));
                    }
                }
                result.add(deviceDetailDto);
            }
            return result;
        }

        return null;
    }

    private void resolveModifyLaunchArea(DeviceForDetailDto result) {
        if (Objects.isNull(result.getLaunchArea())) {
            result.setCanModifyLaunchArea(true);
        } else {

        }
    }

    @Override
    public boolean update(DeviceForUpdateDto dto) {
        Device exist = selectById(dto.getSno());
        Date oldExpire = exist.getExpirationTime();
        exist.setUtime(new Date());
        resolveBasic(exist, dto);
        resolveOperation(exist, dto);

        if (dto.getTotalWater() != null) {
            // TODO 总用水量下发设备
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("a29heatingtemperature1", dto.getTotalWater());
            Product product = productService.getProductByDeviceSno(exist.getSno());
            if (!ParamUtil.isNullOrEmptyOrZero(product)&&!ParamUtil.isNullOrEmptyOrZero(exist.getGizDid())) {
                DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), exist.getGizDid(), jsonObject);
            }
        }

        // 前端会把过期时间置空，所以这里要用updateAllColumnById，将过期时间set null
        if(updateAllColumnById(exist)){
            //第一次设置过期时间时下发收费模式为长租模式
            if (ParamUtil.isNullOrEmptyOrZero(oldExpire)&&!ParamUtil.isNullOrEmptyOrZero(exist.getExpirationTime())){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("a7Operationmode","02");
                Product product = productService.getProductByDeviceSno(exist.getSno());
                if (!ParamUtil.isNullOrEmptyOrZero(product)&&!ParamUtil.isNullOrEmptyOrZero(exist.getGizDid())) {
                    return DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), exist.getGizDid(), jsonObject);
                }
            }
        }
        return false;
    }

    @Override
    public int deviceAuthResult(Device device, SysUserExt sysUserExt) {
        List<DeviceAuth> deviceAuths = new ArrayList<>();
        //由于微信绑定设备需要记录mac并且长度不能长于16的16进制的字符串
        String mac = device.getMac().substring(device.getMac().length() - 12, device.getMac().length());
        DeviceAuth deviceAuth = new DeviceAuth(device.getWxDid(), mac);
        deviceAuths.add(deviceAuth);
        String res = userWeixinService.authorizeDevice(deviceAuths, sysUserExt);
        logger.info("设备" + device.getSno() + "," + device.getWxDid() + "微信授权结果" + res);

        JSONObject jsonRes = JSON.parseObject(res);
        int errcode = 1;
        if (jsonRes.containsKey("resp")) {
            errcode = jsonRes.getJSONArray("resp").getJSONObject(0)
                    .getInteger("errcode");
        }
        return errcode;
    }

    @Override
    public List<DeviceShowDto> getDeviceByGroupId(Integer groupId) {
        List<DeviceGroupToDevice> deviceGroupToDevices = deviceGroupToDeviceService.selectList(new EntityWrapper<DeviceGroupToDevice>().eq("device_group_id", groupId));
        if (CollectionUtils.isNotEmpty(deviceGroupToDevices)) {
            Set<String> deviceSnos = deviceGroupToDevices.stream().map(DeviceGroupToDevice::getDeviceSno).collect(Collectors.toSet());
            List<Device> devices = selectBatchIds(new ArrayList<>(deviceSnos));
            return devices.stream().map(this::getDeviceShowDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public BatchDeviceWebSocketVo getInfoForControlBatchDevice(BatchDevicePageDto data) {
        SysUser currentUser = sysUserService.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        List<String> snoList = data.getSnoList();
        if (Objects.isNull(snoList) || snoList.size() == 0) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        List<Device> deviceList = selectBatchIds(snoList);
        if (Objects.isNull(deviceList) || deviceList.size() == 0) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        if (ParamUtil.isNullOrZero(deviceList.get(0).getProductId())) {
            LeaseException.throwSystemException(LeaseExceEnums.PRODUCT_DONT_EXISTS);
        }
        Product product = productService.selectById(deviceList.get(0).getProductId());
        if (Objects.isNull(product)) {
            LeaseException.throwSystemException(LeaseExceEnums.PRODUCT_DONT_EXISTS);
        }

        //根据product找到数据点
        List<ProductDataPoint> dts = productDataPointService.selectList(new EntityWrapper<ProductDataPoint>().eq("product_id", product.getId()));
        if (dts.size() == 0 || Objects.isNull(dts)) {
            LeaseException.throwSystemException(LeaseExceEnums.DATA_POINT_ILLEGAL_PARAM);
        }
        List<ProductDataPointDto> productDataPointDtos = new ArrayList<>();
        for (ProductDataPoint dt : dts) {
            productDataPointDtos.add(new ProductDataPointDto(dt));
        }

        String userToken = redisService.getUserTokenByUserName(currentUser.getId() + "");
        String uid = redisService.getUidByUsername(currentUser.getId() + "");
        if (StringUtils.isEmpty(userToken) || StringUtils.isEmpty(uid)) {//该操作只操作userToken和uid，其他passcode和host都不处理
            String res = GizwitsUtil.createUser(currentUser.getId() + "", product.getGizwitsAppId());
            JSONObject json = JSONObject.parseObject(res);
            redisService.setUserTokenByUsername(currentUser.getId() + "", String.valueOf(json.get("uid")),
                    String.valueOf(json.get("token")),
                    Long.valueOf(String.valueOf(json.get("expire_at"))));
            userToken = String.valueOf(json.get("token"));
            uid = String.valueOf(json.get("uid"));
        }
        String host = "sandbox.gizwits.com";
        String port = "8080";
        List<String> didList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); ++i) {
            Device device = deviceList.get(i);
            String res = GizwitsUtil.bindDevice(device.getMac(), product, userToken);
            JSONObject json = JSONObject.parseObject(res);
            if (!Objects.isNull(json.get("host")) && !Objects.isNull(json.get("ws_port"))) {
                host = String.valueOf(json.get("host"));
                port = String.valueOf(json.get("ws_port"));
            }
            didList.add(device.getGizDid());
        }
        BatchDeviceWebSocketVo batchDeviceWebSocketVo = new BatchDeviceWebSocketVo();
        batchDeviceWebSocketVo.setUid(uid);
        batchDeviceWebSocketVo.setToken(userToken);
        batchDeviceWebSocketVo.setBatchDeviceDetailWebSocketVo(new BatchDeviceDetailWebSocketVo(didList, port, port, host));
        batchDeviceWebSocketVo.setGizAppId(product.getGizwitsAppId());
        batchDeviceWebSocketVo.setDataPoints(productDataPointDtos);
        return batchDeviceWebSocketVo;
    }

    @Override
    public String getSnoByOpenid(String openid) {
        String sno = deviceDao.getSnoByOpenid(openid, OrderStatus.USING.getCode());
        return sno;

    }

    @Override
    public void checkDeviceIsRenting(String sno, Integer  port) {

        Device device = getDeviceInfoBySno(sno);
        if (Objects.isNull(device)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_RENT);
        }

        if (DeviceStatus.OFFLINE.getCode().equals(device.getOnlineStatus())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_OFFLINE);
        }

        if (!ParamUtil.isNullOrEmptyOrZero(port)) {
            DeviceExt deviceExt = deviceExtService.selectBySnoAndPort(sno, port);
            if (null == deviceExt || !Objects.equals(deviceExt.getStatus(), DeviceStatus.FREE.getCode())) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_PORT_NOT_REFF);
            }
        } else {
            if ( !Objects.equals(device.getWorkStatus(), DeviceStatus.FREE.getCode())) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_PORT_NOT_REFF);
            }

        }

        //投放点判断
        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(device.getLaunchAreaId());
        if (Objects.isNull(deviceLaunchArea)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
        }
        //运营商运营
        SysUserExt sysUserExt = getWxConfigByDeviceId(sno);
        if (Objects.isNull(sysUserExt)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }
        Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", device.getOwnerId()));
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }
    }


    @Override
    public Boolean checkDeviceIfUsedByOpenid(Device device, String openId) {
        if (Objects.isNull(device)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        OrderBase orderBase = orderBaseService.getUsingOrderByOpenid(device.getSno(), openId);
        return !Objects.isNull(orderBase) && device.getSno().equals(orderBase.getSno());
    }

    private void resolveOperation(Device exist, DeviceForUpdateDto dto) {
        boolean has = false;
        if (Objects.nonNull(dto.getServiceId()) && Objects.nonNull(dto.getServiceName())) {
            has = true;
            checkDeviceServiceMode(exist, dto.getServiceId());
            exist.setServiceId(dto.getServiceId());
            exist.setServiceName(dto.getServiceName());
        }
        if (Objects.nonNull(dto.getLaunchAreaId()) && Objects.nonNull(dto.getLaunchAreaName())) {
            has = true;
            exist.setLaunchAreaId(dto.getLaunchAreaId());
            exist.setLaunchAreaName(dto.getLaunchAreaName());
        }
        if (has) {
            publishEvent(exist.getSno(), ProductOperateType.DEVICE_OPERATION);
        }
    }

    private void checkDeviceServiceMode(Device device, Integer newServiceModeId) {
        //判断新的收费模式是否是免费模式
        EntityWrapper<ProductServiceMode> serviceModeEntityWrapper = new EntityWrapper<>();
        serviceModeEntityWrapper.eq("id", newServiceModeId);
        ProductServiceMode productServiceMode = productServiceModeService.selectOne(serviceModeEntityWrapper);
        if (Objects.isNull(productServiceMode)) {
            logger.error("=====收费模式{}不存在=====", newServiceModeId);
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        if (StringUtils.isNotBlank(productServiceMode.getCommand()) && !productServiceMode.getCommand().equals("{}")) {
            Product product = productService.getProductByProductId(device.getProductId());
            if (StringUtils.isBlank(device.getGizDid())
                    || !DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), productServiceMode.getCommand())) {
                logger.warn("====设备{}修改收费模式为{}并下发指令{}失败,将指令放入缓存,等设备上线后下发=====", device.getSno(), productServiceMode.getId(), productServiceMode.getCommand());
                redisService.cacheDeviceControlCommand(product.getGizwitsProductKey(), device.getMac(), productServiceMode.getCommand());
            }
        }
        if (productServiceMode.getIsFree().equals(BooleanEnum.FALSE.getCode())) {//收费的设置不需要判断上级设置情况
            return;
        }

        //对于免费的收费模式需要判断上级给当前设备设置的收费模式类型
        EntityWrapper<DeviceServiceModeSetting> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno", device.getSno()).eq("assign_account_id", device.getOwnerId()).eq("is_deleted", BooleanEnum.FALSE.getCode());
        DeviceServiceModeSetting deviceServiceModeSetting = deviceServiceModeSettingService.selectOne(entityWrapper);
        if (Objects.nonNull(deviceServiceModeSetting)) {
            if (deviceServiceModeSetting.getIsFree().equals(BooleanEnum.FALSE.getCode())) {//上级给设备设置的是收费类型而非免费,禁止用户修改设备收费模式
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_PARENT_SERVICE_MODE_SETTING_LIMIT);
            }
        }
    }

    private void resolveBasic(Device exist, DeviceForUpdateDto dto) {
        if (Objects.nonNull(dto.getMac()) && Objects.nonNull(dto.getName())) {
            exist.setMac(dto.getMac());
            exist.setName(dto.getName());

            if (!ParamUtil.isNullOrEmptyOrZero(dto.getPortDtos())) {
                dto.getPortDtos().forEach(item -> {
                    DeviceExt deviceExt = deviceExtService.selectById(item.getId());
                    deviceExt.setUtime(new Date());
                    deviceExt.setPort(item.getPort());
                    deviceExt.setPortType(item.getWaterType());
                    deviceExt.setSort(item.getSort());
                    deviceExtService.updateById(deviceExt);
                });
            }

            // 设置设备到期时间
            exist.setExpirationTime(dto.getExpirationTime());

            // 设置设备用水总量
            exist.setTotalWater(dto.getTotalWater());

            publishEvent(exist.getSno(), ProductOperateType.DEVICE_BASIC);
        }
    }

    private void publishEvent(String deviceSno, ProductOperateType operateType) {
        productDeviceChangeService.publishChangeEvent(deviceSno, operateType);
    }

    private DeviceLaunchAreaForDeviceDto resolveLaunchArea(Integer launchAreaId) {
        if (Objects.nonNull(launchAreaId)) {
            DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(launchAreaId);
            if (Objects.nonNull(deviceLaunchArea)) {
                return new DeviceLaunchAreaForDeviceDto(deviceLaunchArea);
            }
        }
        return null;
    }

    private ProductServiceDetailForDeviceDto resolveServiceMode(Integer serviceId) {
        if (Objects.nonNull(serviceId)) {
            ProductServiceMode mode = productServiceModeService.selectById(serviceId);
            if (Objects.nonNull(mode)) {
                ProductServiceDetailForDeviceDto dto = new ProductServiceDetailForDeviceDto(mode);
                dto.setPriceAndNumDtoList(productServiceDetailService.getProductPriceDetailByServiceModeId(serviceId));
                return dto;
            }
        }
        return null;
    }

    @Override
    public Integer countByServiceId(Integer serviceId) {
        SysUser sysUser = sysUserService.getCurrentUser();
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("service_id", serviceId).eq("sys_user_id", sysUser.getId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectCount(entityWrapper);
    }

    @Override
    public Page<DeviceShowDto> listPage(Pageable<DeviceQueryDto> pageable) {

        Page<Device> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);

        Wrapper<Device> wrapper = new EntityWrapper<>();
        // 根据需求将设备排序字段由设备最后一次在线时间修改为更新时间
        wrapper.orderBy("utime", false);

        // 水量剩余10%设备数跳转
        if (pageable.getQuery().getIsRemain10Device() != null && pageable.getQuery().getIsRemain10Device().equals(0)) {
            wrapper.eq("remain_water/total_water", 0.1);
        }

//        pageable.getQuery().setExpirationTimeBegin(now);
//        pageable.getQuery().setExpirationTimeEnd(now+15);
        if (pageable.getQuery().getUsedWater() != null &&
                !ParamUtil.isNullOrEmpty(pageable.getQuery().getSign())) {
            if ("<".equals(pageable.getQuery().getSign())) {
                wrapper.lt("remain_water/total_water", pageable.getQuery().getUsedWater());
            }
            if ("=".equals(pageable.getQuery().getSign())) {
                wrapper.eq("remain_water/total_water", pageable.getQuery().getUsedWater());
            }
            if (">".equals(pageable.getQuery().getSign())) {
                wrapper.gt("remain_water/total_water", pageable.getQuery().getUsedWater());
            }
        }

	    if (DeviceStatus.LOCKED.getCode().equals(pageable.getQuery().getWorkStatus())) {
		    pageable.getQuery().setWorkStatus(null);
		    pageable.getQuery().setLock(true);
	    }

        Page<Device> page1 = selectPage(page,
                QueryResolverUtils.parse(pageable.getQuery(), wrapper));
        List<Device> devices = page1.getRecords();
        Page<DeviceShowDto> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        List<DeviceShowDto> list = new ArrayList<>(devices.size());
        for (Device device : devices) {
            list.add(getDeviceShowDto(device));
        }
        result.setRecords(list);
        return result;
    }

    @Override
    public Page<DeviceShowDto> currentListPage(Pageable<DeviceQueryDto> pageable, Integer type) {

        Page<Device> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);

        Wrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.orderBy("last_online_time", false);
        switch (type) {
            case 1:
                wrapper.isNull("launch_area_name");
                break;
            case 2:
                wrapper.isNotNull("launch_area_name");
                break;
            default:
                break;
        }

        Page<Device> page1 = selectPage(page,
                QueryResolverUtils.parse(pageable.getQuery(), wrapper));
        List<Device> devices = page1.getRecords();
        Page<DeviceShowDto> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        List<DeviceShowDto> list = new ArrayList<>(devices.size());
        for (Device device : devices) {
            list.add(getDeviceShowDto(device));
        }
        result.setRecords(list);
        return result;
    }

    private DeviceShowDto getDeviceShowDto(Device device) {
        DeviceShowDto deviceShowDto = new DeviceShowDto();
        deviceShowDto.setSno(device.getSno());
        deviceShowDto.setMac(device.getMac());
        deviceShowDto.setName(device.getName());
        deviceShowDto.setLastOnLineTime(device.getLastOnlineTime());
        deviceShowDto.setProduct(device.getProductName());
        deviceShowDto.setLaunchArea(device.getLaunchAreaName());
        deviceShowDto.setServiceMode(device.getServiceName());
	    deviceShowDto.setLock(device.getLock());
        String onLineStatus = DeviceOnlineStatus.getName(device.getOnlineStatus());
	    String workStatus = DeviceStatus.getShowName(device.getWorkStatus(), device.getFaultStatus(), device.getLock());
        deviceShowDto.setWorkStatus(device.getWorkStatus());
        deviceShowDto.setWorkStatusDesc(workStatus);
        String status = DeviceStatus.getName(device.getStatus());
        deviceShowDto.setOnlineStatus(onLineStatus);
        Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", device.getOwnerId()));
        if (Objects.nonNull(operator)) {
            deviceShowDto.setBelongOperatorName(operator.getName());
        }
        Agent agent = agentService.getAgentBySysAccountId(device.getOwnerId());
        if (Objects.nonNull(agent)) {
            deviceShowDto.setBelongOperatorName(agent.getName());
        }
        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(device.getLaunchAreaId());
        if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchArea)) {
            deviceShowDto.setProvince(deviceLaunchArea.getProvince());
            deviceShowDto.setAddress(deviceLaunchArea.getAddress());
            deviceShowDto.setCity(deviceLaunchArea.getCity());
            deviceShowDto.setArea(deviceLaunchArea.getArea());
        }

        deviceShowDto.setStatus(status);

        //设备到期时间判断
        Date expirationTime = device.getExpirationTime();
        deviceShowDto.setExpirationTime(expirationTime);
        if (!ParamUtil.isNullOrEmptyOrZero(expirationTime)) {
            Date now = new Date();
            try {
                if (daysBetween(now, expirationTime) <= 7) {
                    deviceShowDto.setTime(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        deviceShowDto.setTotalWater(device.getTotalWater());
        deviceShowDto.setRemainWater(device.getRemainWater());

        return deviceShowDto;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    @Override
    public String resolveOwner(Integer ownerId) {
        if (Objects.nonNull(ownerId)) {
            Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", ownerId));
            if (Objects.nonNull(operator)) {
                return operator.getName();
            }
            Agent agent = agentService.selectOne(new EntityWrapper<Agent>().eq("sys_account_id", ownerId));
            if (Objects.nonNull(agent)) {
                return agent.getName();
            }
        }
        return null;
    }


    @Override
    public String deleteDevice(List<String> snos) {
        List<String> fails = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        List<Device> devices = selectList(new EntityWrapper<Device>().in("sno", snos).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (!ParamUtil.isNullOrEmptyOrZero(devices)) {
            for (Device device : devices) {
                //判断是否拥有删除此设备的权限
                if (manufacturerService.selectOne(new EntityWrapper<Manufacturer>().eq("sys_account_id", device.getOwnerId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())) != null) {
                    device.setUtime(new Date());
                    device.setIsDeleted(DeleteStatus.DELETED.getCode());
                    updateById(device);
                    //删除分润与设备的对应关系
                    ShareBenefitRuleDetailDevice detailDevice = new ShareBenefitRuleDetailDevice();
                    detailDevice.setUtime(new Date());
                    detailDevice.setIsDeleted(DeleteStatus.DELETED.getCode());
                    EntityWrapper<ShareBenefitRuleDetailDevice> entityWrapper = new EntityWrapper<>();
                    entityWrapper.eq("sno", detailDevice.getSno());
                    shareBenefitRuleDetailDeviceService.updateById(detailDevice);
                    // 删除设备出水口
                    EntityWrapper<DeviceExt> portEntityWrapper = new EntityWrapper<>();
                    portEntityWrapper.eq("sno", device.getSno()).eq("mac", device.getMac());
                    deviceExtService.delete(portEntityWrapper);
                } else {
                    fails.add(device.getMac());
                }
            }
        }
        switch (fails.size()) {
            case 0:
                sb.append("删除成功");
                break;
            case 1:
                sb.append("设备mac[" + fails.get(0) + "]已有归属，请先解绑设备并重试。");
                break;
            case 2:
                sb.append("设备mac[" + fails.get(0) + "],[" + fails.get(1) + "]已有归属，请先解绑设备并重试。");
                break;
            default:
                sb.append("设备mac[" + fails.get(0) + "],[" + fails.get(1) + "]等已有归属，请先解绑设备并重试。");
                break;
        }
        return sb.toString();
    }

    @Override
    public boolean generateShareSheet(List<Device> devices, Boolean isUnbind) {
        return shareBenefitSheetService.generateShareBenefitSheetForDevices(devices, isUnbind);
    }

    @Override
    public List<Device> listDeviceByOperatorId(List<Integer> operatorIds) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("operator_id", operatorIds);
        return selectList(entityWrapper);
    }

    public DeviceWebSocketVo getInfoForControlDevice(DevicePageDto data) {
        //不断的获取orderNo的订单的状态是否为USING
        OrderBase orderBase = orderBaseService.selectById(data.getOrderNo());
        //如果是退款就返回错误信息
        if (OrderStatus.REFUNDING.getCode().equals(orderBase.getOrderStatus())
                || OrderStatus.REFUND_FAIL.getCode().equals(orderBase.getOrderStatus())
                || OrderStatus.REFUNDED.getCode().equals(orderBase.getOrderStatus())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_START_FAIL);
        }
        if (!orderBase.getOrderStatus().equals(OrderStatus.USING.getCode())) {
            logger.warn("===============>订单状态" + orderBase.getOrderStatus() + "不符合获取websocket信息");
            return null;
        }
        //如果为USING将设备的gizwits的内容返回出去
        Device device = getDeviceInfoBySno(data.getSno());
        if (Objects.isNull(device) || device.getIsDeleted() == 1) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        //如订单的设备号与设备号不一样
        if (!orderBase.getSno().equals(device.getSno())) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        //根据sno查找product
        Product product = productService.getProductByProductId(device.getProductId());
        if (Objects.isNull(product) || product.getIsDeleted() == 1) {
            LeaseException.throwSystemException(LeaseExceEnums.PRODUCT_DONT_EXISTS);
        }
        //根据product找到数据点
        List<ProductDataPoint> dts = productDataPointService.selectList(new EntityWrapper<ProductDataPoint>().eq("product_id", product.getId()));
        if (dts.size() == 0 || Objects.isNull(dts)) {
            LeaseException.throwSystemException(LeaseExceEnums.DATA_POINT_ILLEGAL_PARAM);
        }
        List<ProductDataPointDto> productDataPointDtos = new ArrayList<>();
        for (ProductDataPoint dt : dts) {
            productDataPointDtos.add(new ProductDataPointDto(dt));
        }
        User user = userService.getUserByIdOrOpenidOrMobile(orderBase.getUserId() + "");
        String userToken = redisService.getUserTokenByUserName(user.getUsername());
        String uid = redisService.getUidByUsername(user.getUsername());
        //如果从redis中获取不到，则重新获取
        if (StringUtils.isEmpty(userToken) || StringUtils.isEmpty(uid)) {//该操作只操作userToken和uid，其他passcode和host都不处理
            String res = GizwitsUtil.createUser(user.getUsername(), product.getGizwitsAppId());
            JSONObject json = JSONObject.parseObject(res);
            redisService.setUserTokenByUsername(user.getUsername(), String.valueOf(json.get("uid")),
                    String.valueOf(json.get("token")),
                    Long.valueOf(String.valueOf(json.get("expire_at"))));
            userToken = String.valueOf(json.get("token"));
            uid = String.valueOf(json.get("uid"));
        }
        DeviceWebSocketVo deviceWebSocketVo = new DeviceWebSocketVo();
        deviceWebSocketVo.setUid(uid);
        deviceWebSocketVo.setToken(userToken);
        deviceWebSocketVo.setDeviceDetailWebSocketVo(new DeviceDetailWebSocketVo(device.getGizDid(), device.getGizWsPort(), device.getGizWssPort(), device.getGizHost()));
        deviceWebSocketVo.setGizAppId(product.getGizwitsAppId());
        deviceWebSocketVo.setDataPoints(productDataPointDtos);
        return deviceWebSocketVo;
    }

    @Override
    public List<String> deviceAllotOperator(OperatorAllotDeviceDto operatorAllotDeviceDto) {

        List<String> snos = new ArrayList<>();
        List<Device> devices = selectBatchIds(operatorAllotDeviceDto.getSno());
        for (Device device : devices) {
            if (judgeSnoIsAllot(device)) {
                snos.add(device.getSno());
            } else {
                device.setOwnerId(operatorAllotDeviceDto.getOwnerId());
                updateById(device);
            }
        }
        return snos;
    }

    @Override
    public boolean judgeSnoIsAllot(Device device) {
        boolean flag = false;
        SysUser sysUser = sysUserService.getCurrentUser();
        if (!Objects.equals(device.getOwnerId(), sysUser.getId())) {
            flag = true;
        }
        return flag;
    }

    @Override
    public MJDeviceDetailDto deviceDetailForMahjong(String sno) {
        SysUser user = sysUserService.getCurrentUser();
        Device device = selectOne(new EntityWrapper<Device>().eq("sno", sno)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (Objects.isNull(device)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
        MJDeviceDetailDto deviceDetailDto = new MJDeviceDetailDto(device);
        deviceDetailDto.setWorkStatus(device.getWorkStatus());
        deviceDetailDto.setWorkStatusDesc(DeviceStatus.getName(device.getWorkStatus()));
        if (Objects.equals(device.getOwnerId(), user.getId())) {
            deviceDetailDto.setIsModifyLaunchArea(1);
            deviceDetailDto.setIsModifyServiceMode(1);
        }
        Product product = productService.getProductByDeviceSno(device.getSno());
        if (Objects.isNull(product)) {
            LeaseException.throwSystemException(LeaseExceEnums.PRODUCT_DONT_EXISTS);
        }

        deviceDetailDto.setOnlineStatus(device.getOnlineStatus());
        deviceDetailDto.setOnlineStatusDesc(DeviceOnlineStatus.getName(device.getOnlineStatus()));

       /* JSONObject jsonObject = redisService.getDeviceControlCommand(product.getGizwitsProductKey(), device.getMac());
        if (Objects.nonNull(jsonObject)) {
            String voltage = jsonObject.getString("Voltage");
            Boolean Switch = jsonObject.getBoolean("Switch");
            deviceDetailDto.setVoltage(voltage);
            if (Switch) {
                deviceDetailDto.setIsOpen(1);
            } else {
                deviceDetailDto.setIsOpen(0);
            }
        }*/
        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.getLaunchAreaInfoById(device.getLaunchAreaId());
        if (!ParamUtil.isNullOrEmptyOrZero(deviceLaunchArea)) {
            deviceDetailDto.setProvince(deviceLaunchArea.getProvince());
            deviceDetailDto.setCity(deviceLaunchArea.getCity());
            deviceDetailDto.setArea(deviceLaunchArea.getArea());
            deviceDetailDto.setAddress(deviceLaunchArea.getAddress());
        }

        return deviceDetailDto;
    }

    @Override
    public List<String> deviceDeleteLaunchArea(DeviceWithLaunchArea deviceWithLaunchArea) {
        List<String> snos = new ArrayList<>();
        Integer launchAreaId = deviceWithLaunchArea.getLaunchAreaId();
        logger.info("投放点id=" + launchAreaId + "设备序列号：" + deviceWithLaunchArea.getSno().toString());
        List<Device> devices = selectBatchIds(deviceWithLaunchArea.getSno());
        for (Device device : devices) {
            if (!Objects.equals(device.getLaunchAreaId(), launchAreaId)) {
                snos.add(device.getSno());
            } else {
                device.setLaunchAreaId(null);
                device.setLaunchAreaName(null);
                boolean flag = updateAllColumnById(device);
                if (!flag) {
                    snos.add(device.getSno());
                }
            }
        }
        return snos;
    }

    @Override
    public List<String> deviceWithLaunchArea(DeviceWithLaunchArea deviceWithLaunchArea) {
        List<String> snos = new ArrayList<>();
        Integer launchAreaId = deviceWithLaunchArea.getLaunchAreaId();
        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(launchAreaId);
        List<Device> devices = selectBatchIds(deviceWithLaunchArea.getSno());
        for (Device device : devices) {
            if (!ParamUtil.isNullOrEmptyOrZero(device.getLaunchAreaId())) {
                snos.add(device.getSno());
            } else {
                device.setLaunchAreaId(launchAreaId);
                device.setLaunchAreaName(deviceLaunchArea.getName());
                boolean flag = updateById(device);
                if (!flag) {
                    snos.add(device.getSno());
                }
            }
        }
        return snos;
    }

    @Override
    public List<String> getFireFailSnos(ListDeviceForFireDto dto) {
        List<String> snos = new ArrayList<>();
        for (String sno : dto.getSnos()) {
            if (ParamUtil.isNullOrEmptyOrZero(dto.getAttrs())) {
                if (!remoteDeviceControl(sno, dto.getName(), dto.getValue())) {
                    snos.add(sno);
                }
            } else {
                if (!remoteDeviceControl(sno, dto.getAttrs())) {
                    snos.add(sno);
                }
            }
        }
        return snos;
    }

    public void sendCallOutToManage(Device device) {
        if (Objects.isNull(device)) {
            logger.error("====设备信息为空====");
            return;
        }
        if (Objects.isNull(device.getLaunchAreaId())) {
            logger.error("====设备{}未设置投放点====", device.getSno());
            return;
        }
        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(device.getLaunchAreaId());
        if (Objects.isNull(deviceLaunchArea)) {
            logger.error("===设备{}的投放点{}不存在====", device.getSno(), device.getLaunchAreaId());
            return;
        }
        SysUserExt sysUserExt = getWxConfigByDeviceId(device.getSno());
        if (Objects.isNull(sysUserExt)) {
            logger.error("===设备{}未找到相关的微信配置=====", device.getSno());
            return;
        }
        SysUserExt toUser = sysUserExtService.selectById(deviceLaunchArea.getMaintainerId());
        if (Objects.isNull(toUser)) {
            logger.error("===设备{}的投放点{}的维护人员{}不存在=====", device.getSno(), deviceLaunchArea.getId(), deviceLaunchArea.getMaintainerId());
            return;
        }
        if (Objects.isNull(toUser.getWxOpenId())) {
            logger.error("===设备{}的投放点{}的维护人员{}未配置微信openID=====", device.getSno(), deviceLaunchArea.getId(), deviceLaunchArea.getMaintainerId());
            return;
        }

        JSONObject sendData = new JSONObject();
        sendData.put("touser", sysUserExt.getWxOpenId());
        sendData.put("template_id", SysConfigUtils.get(CommonSystemConfig.class).getMahjongCallTemplateId());

        JSONObject firstData = new JSONObject();
        firstData.put("value", "您好，有新的呼叫。");
        firstData.put("color", "#173177");

        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", deviceLaunchArea.getName() + "--" + device.getMac());
        keyword2.put("color", "#173177");

        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", com.gizwits.boot.utils.DateKit.getTimestampString(new Date()));
        keyword1.put("color", "#173177");

        JSONObject remarkData = new JSONObject();
        remarkData.put("value", "请及时处理。");
        remarkData.put("color", "#173177");

        JSONObject body = new JSONObject();
        body.put("first", firstData);
        body.put("keyword1", keyword1);
        body.put("keyword2", keyword2);
        body.put("remark", remarkData);

        sendData.put("data", body);
        logger.info("====发送串货信息信息-====>>>" + body);
        //发送模板消息
        WxUtil.sendTemplateNotice(sendData.toJSONString(), sysUserExt);
    }

    @Override
    public Device getDeviceByQrcode(String qrcode) {
        Wrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("wx_ticket", qrcode).or().eq("content_url", qrcode);
        return selectOne(wrapper);
    }

    @Override
    public Device getDeviceByMac(String mac) {
        Wrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("mac", mac).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectOne(wrapper);
    }

    @Override
    public Device selectById(String sno) {
        Wrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("sno", sno).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectOne(wrapper);
    }


    @Override
    public List<DeviceProductShowDto> show(String sno) {
        Device device = getDeviceInfoBySno(sno);
        if (ParamUtil.isNullOrEmptyOrZero(device)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }

        ProductCommandConfigForQueryDto queryDto = new ProductCommandConfigForQueryDto();
        queryDto.setProductId(device.getProductId());
        queryDto.setCommandType("SHOW");
        List<ProductCommandConfigForDetailDto> detailDtos = productCommandConfigService.list(queryDto);
        Product product = productService.getProductByDeviceSno(sno);
        JSONObject existDevice = redisService.getDeviceCurrentStatus(product.getGizwitsProductKey(), device.getMac());
        if (Objects.isNull(existDevice)) {
            return ListUtils.EMPTY_LIST;
        }

        logger.info("getDeviceCurrentStatus mac {} {}", device.getMac(), existDevice.toJSONString());
        List<DeviceProductShowDto> showDtos = new ArrayList<>(detailDtos.size());
        if (!ParamUtil.isNullOrEmptyOrZero(existDevice)) {
            for (ProductCommandConfigForDetailDto detailDto : detailDtos) {
                DeviceProductShowDto deviceProductShowDto = new DeviceProductShowDto();
                String identityName = detailDto.getIdentityName();
                if (StringUtils.isEmpty(identityName)) {
                    identityName = JSONObject.parseObject(detailDto.getCommand()).getString("name");
                }
                String range = JSONObject.parseObject(detailDto.getCommand()).getString("value");
                deviceProductShowDto.setRange(range);
                String value = existDevice.getString(identityName);
                logger.info("get value by identity name {} mac {} -> value {}", identityName, device.getMac(), value);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(detailDto.getName(), value);
                deviceProductShowDto.setJsonObject(jsonObject);
                deviceProductShowDto.setShowType(detailDto.getShowType());
                showDtos.add(deviceProductShowDto);
            }
        }
        return showDtos;
    }

    @Override
    public void assingModeOrArea(DeviceAssignDto deviceAssignDto) {
        SysUser sysUser = sysUserService.getCurrentUserOwner();
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        Device device = new Device();
        Date now = new Date();
        Integer assignId = deviceAssignDto.getAssignId();
        switch (deviceAssignDto.getType()) {
            case 1:
                ProductServiceMode serviceMode = productServiceModeService.getProductServiceMode(assignId);
                if (ParamUtil.isNullOrEmptyOrZero(serviceMode)) {
                    LeaseException.throwSystemException(LeaseExceEnums.SERVICE_MODE_NOT_EXIST);
                }
                entityWrapper.in("sno", deviceAssignDto.getSno()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
                List<Device> devices = selectList(entityWrapper);
                if (!ParamUtil.isNullOrEmptyOrZero(device)) {
                    for (Device exist : devices) {
                        exist.setServiceId(serviceMode.getId());
                        exist.setServiceName(serviceMode.getName());
                        exist.setUtime(now);
                        checkDeviceServiceMode(exist, assignId);
                    }
                    updateBatchById(devices);
                }
                break;
            case 2:
                DeviceLaunchArea launchArea = deviceLaunchAreaService.getLaunchAreaInfoById(deviceAssignDto.getAssignId());
                if (ParamUtil.isNullOrEmptyOrZero(launchArea) || !sysUser.getId().equals(launchArea.getSysUserId())) {
                    LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LAUNCH_AREA_CANT_ASSIGN);
                }
                entityWrapper.in("sno", deviceAssignDto.getSno());
                device.setLaunchAreaId(launchArea.getId());
                device.setLaunchAreaName(launchArea.getName());
                device.setUtime(new Date());
                update(device, entityWrapper);
                break;
            default:
                break;
        }

    }

    @Override
    public void updateLockFlag(String sno, Integer abnormalTimes, Boolean lock) {
        Device forUpdate = new Device();
        forUpdate.setSno(sno);
        forUpdate.setAbnormalTimes(abnormalTimes);
        forUpdate.setLock(lock);
        updateById(forUpdate);
    }

    @Override
    public boolean changeOperateStatus(String sno, Integer operateStatus) {
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sno", sno);

        Device device = new Device();
        device.setUtime(new Date());
        device.setOperateStatus(operateStatus);

        return update(device, entityWrapper);
    }

    @Override
    public boolean resetFilter(String sno) {
        Device device = getDeviceInfoBySno(sno);
        JSONObject json = new JSONObject();
        json.put("a31Watertemperaturesetting", 100);
        Product product = productService.getProductByDeviceSno(device.getSno());
        if (!ParamUtil.isNullOrEmptyOrZero(product) &&
                !ParamUtil.isNullOrEmptyOrZero(device.getGizDid())) {
            return
                    DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), json);
        }
        return false;
    }
}
