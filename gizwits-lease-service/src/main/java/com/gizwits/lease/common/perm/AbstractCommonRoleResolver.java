package com.gizwits.lease.common.perm;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.enums.ShareType;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetailDevice;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailDeviceService;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailService;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceGroup;
import com.gizwits.lease.device.entity.DeviceGroupToDevice;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceAssignRecordDto;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.device.service.DeviceGroupService;
import com.gizwits.lease.device.service.DeviceGroupToDeviceService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.service.DeviceServiceModeSettingService;
import com.gizwits.lease.enums.AssignDestinationType;
import com.gizwits.lease.enums.ShareBenefitRuleFrequency;
import com.gizwits.lease.enums.ShareBenefitType;
import com.gizwits.lease.enums.ShareRuleDetailType;
import com.gizwits.lease.event.DeviceAssignEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;

import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lilh
 * @date 2017/8/24 14:28
 */
public abstract class AbstractCommonRoleResolver implements CommonRoleResolver, InitializingBean {

    private Logger logger = LoggerFactory.getLogger("BENEFIT_LOGGER");
    @Autowired
    protected DeviceService deviceService;

    @Autowired
    protected SysUserService sysUserService;

    @Autowired
    protected OperatorService operatorService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private DeviceGroupService deviceGroupService;

    @Autowired
    private DeviceGroupToDeviceService deviceGroupToDeviceService;

    @Autowired
    private DeviceServiceModeSettingService deviceServiceModeSettingService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private ShareBenefitRuleDetailDeviceService shareBenefitRuleDetailDeviceService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitRuleDetailService shareBenefitRuleDetailService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private OrderBaseService orderBaseService;


    protected Map<AssignDestinationType, AccountResolver> map = new HashMap<>();

    @Override
    @Transactional
    public boolean assign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        initDevice(dto);
        preAssign(dto, helper);
        return doAssign(dto, helper);
    }

    @Override
    @Transactional
    public boolean unbind(DeviceForUnbindDto unbindDto, SysUserRoleTypeHelper helper) {
        DeviceForAssignDto dto = new DeviceForAssignDto();
        BeanUtils.copyProperties(unbindDto, dto);
        initDevice(dto);
        preUnbind(dto, helper);
        return doUnbind(dto, helper);
    }

    protected boolean doUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //解绑，回滚到原来
        //1.设备回滚
        if(!ParamUtil.isNullOrEmptyOrZero(dto.getDevices())) {
            List<DeviceAssignRecordDto> recordDtos = resolveRecord(dto.getDevices(), helper.getSysAccountId());
            dto.getDevices().forEach(item -> {
                //清空投放点和收费模式
                resetLaunchAreaAndServiceMode(item);
            });
            resetOwnerBatch(dto.getDevices(), helper.getSysAccountId(), helper.getOrigizationName());
            customUnBind(dto, helper);
            CommonEventPublisherUtils.publishEvent(new DeviceAssignEvent(recordDtos, DeviceAssignEvent.Type.UNBIND));
        }
        return true;
    }

    private void customUnBind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        if (CollectionUtils.isNotEmpty(dto.getDeviceGroups())) {
            dto.getDeviceGroups().forEach(deviceGroup -> {
                //将设备组的当前归属置空，回到创建者手里
                deviceGroup.setAssignedAccountId(null);
                deviceGroup.setAssignedName(null);
                deviceGroup.setUtime(new Date());
                deviceGroupService.updateAllColumnById(deviceGroup);
            });

        }
    }

    /**
     * 1.检查设备是不是在自己的直接下级
     * 2.为所有的设备执行分润规则(后面可能调整为按设备执行分润规则),同时将设备的分润规则从原来的分润规则中移除
     * 3.修改当前用户分润规则中设备对子级的分润比例为0
     * 4.执行解绑的的后续操作,清除设备的收费模式投放点,修改设备的归属
     * @param dto
     * @param helper
     */
    protected void preUnbind(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //检验设备的当前拥有者是当前登录人创建的
        if(!ParamUtil.isNullOrEmptyOrZero(dto.getDevices())) {
            /** 判断设备是否归于直属的下级 */
            SysUser currentOwnerUser = sysUserService.getCurrentUser();
            List<Integer> directSubAdminIds = sysUserService.resolveAccessableUserIds(currentOwnerUser);
            Set<Integer> directSubAdminIdsSet = new HashSet<>(directSubAdminIds);
            /** 移除当前登录用户 */
            directSubAdminIdsSet.remove(currentOwnerUser.getId());
            for (Device device:dto.getDevices()){
                if (!directSubAdminIdsSet.contains(device.getOwnerId())){
                    LeaseException.throwSystemException(device.getSno() ,LeaseExceEnums.DEVICE_NOT_BELONG_DIRECT_USER);
                }
            }

            //沁尔康没有分润功能
          /*  *//** 为解绑对象生成分润规则,同时删除解绑对象分润规则中的设备分润规则 *//*
            logger.info("======preUnbind generateShareBenefitForShareRule for device:{}", dto.getDevices().get(0));
            deviceService.generateShareSheet(dto.getDevices(), true);

            *//** 为当前用户执行分润规则 *//*
            ShareBenefitRule currentLoginUserRule = shareBenefitRuleService.getRuleBySysAccountId(sysUserService.getCurrentUser().getId());
            if (Objects.nonNull(currentLoginUserRule)){
                logger.info("======preUnbind generateShareBenefitForShareRule for currentUser:{}",currentLoginUserRule.getId());
                shareBenefitSheetService.generateShareBenefitForShareRule(currentLoginUserRule);
                *//** 修改当前用户的分润规则中相应设备的下级分润比例为0 *//*
                for (Device device:dto.getDevices()){
                    shareBenefitRuleDetailDeviceService.updateDeviceChildrenPercentage(device.getSno(),BigDecimal.ZERO,currentLoginUserRule.getId());
                }
            }*/

            //解绑时删除设备收费模式中的相关配置
            deviceServiceModeSettingService.deleteAssignDeviceServiceMode(dto.getDevices());
        }
    }

    protected boolean doAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //1.获取运营商或代理商关联的系统帐号
        Integer sysAccountId = resolveAccountId(dto);
        if (Objects.isNull(sysAccountId)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        //生成分润单
//        logger.info("======doAssign generateShareBenefitForShareRule for device:{}", dto.getDevices().get(0));
//        deviceService.generateShareSheet(dto.getDevices(), false);
        //分配
        List<DeviceAssignRecordDto> recordDtos = resolveRecord(dto.getDevices(), sysAccountId);
        dto.getDevices().forEach(item -> {
            //在清空收费模式前,记录设备的收费模式
            recordDeviceServiceModeSetting(item, sysAccountId);
            //清空投放点和收费模式
            resetLaunchAreaAndServiceMode(item);
        });
        resetOwnerBatch(dto.getDevices(), sysAccountId, dto.getAssignedName());
        //deviceService.updateBatchById(dto.getDevices());
        dto.setResolvedAccountId(sysAccountId);
        customAssign(dto, helper);
        recordDtos.forEach(item -> {
            item.setIsAgent(dto.getIsAgent());
            item.setIsOperator(dto.getIsOperator());
        });
        CommonEventPublisherUtils.publishEvent(new DeviceAssignEvent(recordDtos, DeviceAssignEvent.Type.ASSIGN));
        return true;
    }

    private void recordDeviceServiceModeSetting(Device device, Integer assignAccountId) {
        deviceServiceModeSettingService.saveDeviceServiceMode(device.getSno(), device.getOwnerId(), assignAccountId);
    }

    //做一些检验
    protected void preAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        //若设备属于自己，则可分配,仅供厂商和代理商使用，运营商可单独实现
        boolean has = dto.getDevices().stream().anyMatch(item -> !Objects.equals(item.getOwnerId(), helper.getSysAccountId()));
        if (has) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_CANNOT_ASSIGN);
        }
        fillAssignSysAccountId(dto);

        // 沁尔康没有分润功能
      /*  //检测设备是否具有分配规则,,为当前登录用户生成分润规则
        if (helper.isOperator() || helper.isAgent()) {
            List<ShareBenefitRuleDeviceVo> deviceShareBenefitRuleList = shareBenefitRuleDetailDeviceService.getDeviceShareRuleByRuleIdOrSysAccountIdAndSno(null,helper.getSysAccountId(),dto.getDeviceSnos());
            if (CollectionUtils.isNotEmpty(deviceShareBenefitRuleList)){
                *//**
                 * 循环检查设备是否有使用中订单,如果有使用中订单则不能分配设置,,因为订单需要在当前用户身上完结分润才行
                 *//*
                for (ShareBenefitRuleDeviceVo deviceVo:deviceShareBenefitRuleList){
                    OrderBase usingOrder = orderBaseService.getDeviceLastOrderByStatus(deviceVo.getSno(), OrderStatus.USING.getCode());
                    if (Objects.nonNull(usingOrder)){
                        LeaseException.throwSystemException(deviceVo.getSno(), LeaseExceEnums.HAS_UNFINISH_ORDER ,usingOrder.getOrderNo());
                    }
                }
                *//**
                 * 因为分配的设备中包含在了当前用户的规则中,需要执行一次分润将订单完结掉
                 *//*
                logger.info("======preAssign generateShareBenefitForShareRule :{}", helper.getSysAccountId());
                shareBenefitSheetService.generateShareBenefitForShareRule(shareBenefitRuleService.getRuleBySysAccountId(helper.getSysAccountId()));
            }

            dto.getDevices().forEach(item -> {
                *//**
                 * 如果这些设备在当前用户的分润规则中不存在,,那么不需要将设备添加到自己的规则中
                 * 如果这些设备在分润规则中存在,,那么需要执行一次分润规则,将之前的订单结算一下
                 *//*
                ShareBenefitRuleDeviceVo rule = shareBenefitRuleDetailDeviceService.getDeviceShareRuleBySysAccountId(item.getSno(), helper.getSysAccountId());
                if (ParamUtil.isNullOrEmptyOrZero(rule)) {
                    saveDeviceShareRuleForOperator(dto, item, helper.getSysAccountId());
                }else{//有设备在当前用户的分润规则中,执行分润规则
                    //shareBenefitSheetService.generateShareBenefitForShareRule(shareBenefitRuleService.selectById(rule.getRuleId()));
                }
            });
        }
        *//**
         * 分配设备到下级,不用为下级创建分润规则,可以手动为下级创建分润规则,或者下级将设备继续往下分配时创建
         *//*
        if (!dto.getAssignDestinationType().equals(AssignDestinationType.LAUNCH_AREA.getCode())){
            dto.getDevices().forEach(item -> {
                ShareBenefitRuleDeviceVo assignRule = shareBenefitRuleDetailDeviceService.getDeviceShareRuleBySysAccountId(item.getSno(), dto.getAssignedId());
                if (ParamUtil.isNullOrEmptyOrZero(assignRule)) {
                    saveDeviceShareRuleForOperator(dto, item, dto.getResolvedAccountId());
                }
            });
        }*/
    }

    private void fillAssignSysAccountId(DeviceForAssignDto dto){
        if (dto.getAssignDestinationType().equals(AssignDestinationType.AGENT.getCode())){
            Agent agent = agentService.selectById(dto.getAssignedId());
            if (Objects.nonNull(agent)){
                dto.setResolvedAccountId(agent.getSysAccountId());
            }
        }
        if (dto.getAssignDestinationType().equals(AssignDestinationType.OPERATOR.getCode())){
            Operator operator = operatorService.selectById(dto.getAssignedId());
            if (Objects.nonNull(operator)){
                dto.setResolvedAccountId(operator.getSysAccountId());
            }
        }
        if (dto.getAssignDestinationType().equals(AssignDestinationType.LAUNCH_AREA.getCode())){
            DeviceLaunchArea launchArea = deviceLaunchAreaService.selectById(dto.getAssignedId());
            if (Objects.nonNull(launchArea)){
                dto.setResolvedAccountId(launchArea.getSysUserId());
            }
        }
    }

    private void saveDeviceShareRuleForOperator(DeviceForAssignDto dto, Device device, Integer sysAccountId){
        //Integer assignSysAccountId = resolveAccountId(dto);
        Integer assignSysAccountId = sysAccountId;
        EntityWrapper<ShareBenefitRule>  shareBenefitRuleEntityWrapper = new EntityWrapper<>();
        shareBenefitRuleEntityWrapper.eq("sys_account_id", assignSysAccountId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        ShareBenefitRule shareBenefitRule = shareBenefitRuleService.selectOne(shareBenefitRuleEntityWrapper);
        if(Objects.nonNull(shareBenefitRule)){
            EntityWrapper<ShareBenefitRuleDetail> allDeviceEntityWrapper = new EntityWrapper<>();
            allDeviceEntityWrapper.eq("rule_id",shareBenefitRule.getId()).eq("is_deleted",DeleteStatus.NOT_DELETED.getCode()).eq("share_type", ShareRuleDetailType.ALL.getCode());
            ShareBenefitRuleDetail shareBenefitRuleDetail = shareBenefitRuleDetailService.selectOne(allDeviceEntityWrapper);
            if (Objects.nonNull(shareBenefitRuleDetail)){
                saveDetailDeviceRule(device, shareBenefitRuleDetail);
            }else {
                EntityWrapper<ShareBenefitRuleDetail> shareBenefitRuleDetailEntityWrapper = new EntityWrapper<>();
                shareBenefitRuleDetailEntityWrapper.eq("rule_id",shareBenefitRule.getId()).eq("is_deleted",DeleteStatus.NOT_DELETED.getCode()).eq("share_percentage", BigDecimal.ZERO);
                shareBenefitRuleDetail = shareBenefitRuleDetailService.selectOne(shareBenefitRuleDetailEntityWrapper);
                if (Objects.nonNull(shareBenefitRuleDetail)){
                    saveDetailDeviceRule(device, shareBenefitRuleDetail);
                }else{
                    //新建一个分润比例为0的Detail,同事将该设备放在该Detail下
                    if (!dto.getResolvedAccountId().equals(sysAccountId)){
                        saveDetailRuleWithDevice(device, shareBenefitRule);
                    }
                }
            }
        }else{
            /**
             * 如果下级不存在分润规则,就不给下级创建默认的规则了
             */
            if (!dto.getResolvedAccountId().equals(sysAccountId)){
                saveRuleWithDetailAndDevice(device,assignSysAccountId);
            }
        }

    }

    private void saveDetailDeviceRule(Device device, ShareBenefitRuleDetail shareBenefitRuleDetail){
        ShareBenefitRuleDetailDevice ruleDetailDevice = new ShareBenefitRuleDetailDevice();
        ruleDetailDevice.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
        ruleDetailDevice.setRuleDetailId(shareBenefitRuleDetail.getId());
        ruleDetailDevice.setSno(device.getSno());
        ruleDetailDevice.setCtime(new Date());
        ruleDetailDevice.setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        if (shareBenefitRuleDetail.getShareType().equals(ShareRuleDetailType.ALL.getCode())){
            ruleDetailDevice.setSharePercentage(shareBenefitRuleDetail.getSharePercentage());
        }else{
            ruleDetailDevice.setSharePercentage(BigDecimal.ZERO);
        }
        ruleDetailDevice.setChildrenPercentage(BigDecimal.ZERO);
        shareBenefitRuleDetailDeviceService.insert(ruleDetailDevice);
    }

    private void saveDetailRuleWithDevice(Device device, ShareBenefitRule shareBenefitRule){
        ShareBenefitRuleDetail shareBenefitRuleDetail = new ShareBenefitRuleDetail();
        shareBenefitRuleDetail.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
        shareBenefitRuleDetail.setRuleId(shareBenefitRule.getId());
        shareBenefitRuleDetail.setName("分润比例0规则");
        shareBenefitRuleDetail.setSharePercentage(BigDecimal.ZERO);
        shareBenefitRuleDetail.setShareType(ShareRuleDetailType.SINGLE.getCode());
        shareBenefitRuleDetail.setCtime(new Date());
        shareBenefitRuleDetail.setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        shareBenefitRuleDetailService.insert(shareBenefitRuleDetail);
        saveDetailDeviceRule(device, shareBenefitRuleDetail);
    }

    private void saveRuleWithDetailAndDevice(Device device, Integer assignSysAccountId){
        ShareBenefitRule shareBenefitRule = new ShareBenefitRule();
        shareBenefitRule.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
        shareBenefitRule.setShareBenefitRuleName("系统生成分润规则");
        shareBenefitRule.setSysAccountId(assignSysAccountId);
        shareBenefitRule.setStartTime(new Date());
        shareBenefitRule.setFrequency(ShareBenefitRuleFrequency.DAY.getCode());
        shareBenefitRule.setCtime(new Date());
        shareBenefitRule.setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        shareBenefitRule.setLastExecuteTime(new Date());
        Agent agent = agentService.getAgentBySysAccountId(assignSysAccountId);
        if(Objects.nonNull(agent)){
            shareBenefitRule.setOperatorName(agent.getName());
            shareBenefitRule.setSysUserId(agent.getSysUserId());
            agent.setShareBenefitRuleId(shareBenefitRule.getId());
            agent.setUtime(new Date());
            agentService.updateById(agent);
        }else {
            Operator operator = operatorService.getOperatorByAccountId(assignSysAccountId);
            if(Objects.nonNull(operator)){
                shareBenefitRule.setOperatorName(operator.getName());
                shareBenefitRule.setSysUserId(operator.getSysUserId());
                operator.setShareBenefitRuleId(shareBenefitRule.getId());
                operator.setUtime(new Date());
                operatorService.updateById(operator);
            }
        }
        if(!ParamUtil.isNullOrEmptyOrZero(shareBenefitRule.getOperatorName())){
            shareBenefitRuleService.insert(shareBenefitRule);
            saveDetailRuleWithDevice(device,shareBenefitRule);
        }
    }


    protected void customAssign(DeviceForAssignDto dto, SysUserRoleTypeHelper helper) {
        if (CollectionUtils.isNotEmpty(dto.getDeviceGroups())) {//设备组分配
            dto.getDeviceGroups().forEach(deviceGroup -> {
                deviceGroup.setAssignedAccountId(dto.getResolvedAccountId());
                deviceGroup.setAssignedName(dto.getAssignedName());
                deviceGroup.setUtime(new Date());
            });
            deviceGroupService.updateBatchById(dto.getDeviceGroups());
        }
    }

    protected Integer resolveAccountId(DeviceForAssignDto dto) {
        AccountResolver resolver = map.get(AssignDestinationType.getType(dto.getAssignDestinationType()));
        Integer result = null;
        if (Objects.nonNull(resolver)) {
            result = resolver.resolve(dto);
        }
        return result;
    }

    private void resetOwnerBatch(List<Device> deviceList, Integer ownerId, String ownerName){
        deviceList.forEach(item -> {
            item.setOwnerId(ownerId);
//            item.setOwnerName(ownerName);
            item.setUtime(new Date());

            deviceService.updateAllColumnById(item);
        });
        //deviceService.updateBatchById(deviceList);
    }

    private void resetLaunchAreaAndServiceMode(Device item) {
        item.setLaunchAreaId(null);
        item.setLaunchAreaName(null);
        item.setServiceId(null);
        item.setServiceName(null);
    }

    private List<DeviceAssignRecordDto> resolveRecord(List<Device> devices, Integer sysAccountId) {
        SysUser current = sysUserService.getCurrentUser();
        if(ParamUtil.isNullOrEmptyOrZero(devices)){
            return null;
        }
        return devices.stream().map(item -> {
            DeviceAssignRecordDto dto = new DeviceAssignRecordDto();
            dto.setSourceOperator(item.getOwnerId());
            dto.setDestinationOperator(sysAccountId);
            dto.setMac(item.getMac());
            dto.setSno(item.getSno());
            dto.setCurrent(current);
            return dto;
        }).collect(Collectors.toList());
    }

    private void initDevice(DeviceForAssignDto dto) {
        if (CollectionUtils.isEmpty(dto.getDevices())) {
            List<Device> devices = null;
            if (CollectionUtils.isNotEmpty(dto.getDeviceGroupIds())) {//设备组
                //1.检验设备组的有效性：存在和自身创建
                List<DeviceGroup> deviceGroups = deviceGroupService.selectList(new EntityWrapper<DeviceGroup>().in("id", dto.getDeviceGroupIds()).in("sys_user_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser())));
                if (CollectionUtils.isEmpty(deviceGroups) || deviceGroups.size() != dto.getDeviceGroupIds().size()) {
                    LeaseException.throwSystemException(LeaseExceEnums.DEVICE_CANNOT_ACCESS_GROUP);
                }
                dto.setDeviceGroups(deviceGroups);
                //2.查询设备
                List<DeviceGroupToDevice> deviceGroupToDevices = deviceGroupToDeviceService.selectList(new EntityWrapper<DeviceGroupToDevice>().in("device_group_id", deviceGroups.stream().map(DeviceGroup::getId).collect(Collectors.toList())));
                if (CollectionUtils.isEmpty(deviceGroupToDevices)) {
                    LeaseException.throwSystemException(LeaseExceEnums.DEVICE_CANNOT_ACCESS);
                }
                List<String> snos = deviceGroupToDevices.stream().map(DeviceGroupToDevice::getDeviceSno).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(snos)) {
                    LeaseException.throwSystemException(LeaseExceEnums.DEVICE_CANNOT_ACCESS);
                }
                devices = deviceService.selectBatchIds(snos);
            } else {
                if (CollectionUtils.isNotEmpty(dto.getDeviceSnos())) {
                    devices = deviceService.selectList(new EntityWrapper<Device>().in("sno", dto.getDeviceSnos()).in("owner_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser())));
                    if (devices.size() != dto.getDeviceSnos().size()) {
                        LeaseException.throwSystemException(LeaseExceEnums.DEVICE_CANNOT_ACCESS);
                    }
                }
            }
            if (CollectionUtils.isEmpty(devices)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_LAUNCH_AREA);
            }
            dto.setDevices(devices);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        map.put(AssignDestinationType.OPERATOR, dto -> {
            Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("id", dto.getAssignedId()).in("sys_user_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser())));
            if (Objects.isNull(operator)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_ASSIGN_OPERATOR_NOT_EXIST);
            }
            dto.setIsOperator(true);
            dto.setAssignedName(operator.getName());
            return operator.getSysAccountId();
        });

        map.put(AssignDestinationType.AGENT, dto -> {
            Agent agent = agentService.selectOne(new EntityWrapper<Agent>().eq("id", dto.getAssignedId()).in("sys_user_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser())));
            if (Objects.isNull(agent)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_ASSIGN_AGENT_NOT_EXIST);
            }
            dto.setIsAgent(true);
            dto.setAssignedName(agent.getName());
            return agent.getSysAccountId();
        });

        map.put(AssignDestinationType.LAUNCH_AREA, dto -> {
            DeviceLaunchArea launchArea = deviceLaunchAreaService.getLaunchAreaInfoById(dto.getAssignedId());
            if (Objects.isNull(launchArea)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
            }
            dto.setLaunchAreaIds(Collections.singletonList(launchArea.getId()));
            return launchArea.getId();
        });
    }


    @FunctionalInterface
    protected interface AccountResolver {
        Integer resolve(DeviceForAssignDto dto);
    }
}
