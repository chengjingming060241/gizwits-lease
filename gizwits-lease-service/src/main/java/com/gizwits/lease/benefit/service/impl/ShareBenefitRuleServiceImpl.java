package com.gizwits.lease.benefit.service.impl;

import java.math.BigDecimal;
import java.rmi.dgc.Lease;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.enums.ShareType;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.benefit.dao.ShareBenefitRuleDao;
import com.gizwits.lease.benefit.dto.ShareBenefitDeviceRangeVo;
import com.gizwits.lease.benefit.dto.ShareBenefitOperatorObjectDto;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetailDevice;
import com.gizwits.lease.benefit.entity.dto.*;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailDeviceService;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailService;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.constant.BooleanEnum;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ShareBenefitRuleFrequency;
import com.gizwits.lease.enums.ShareRuleDetailType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@Service
public class ShareBenefitRuleServiceImpl extends ServiceImpl<ShareBenefitRuleDao, ShareBenefitRule> implements ShareBenefitRuleService {

    protected final static Logger logger = LoggerFactory.getLogger("BENEFIT_LOGGER");

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private ShareBenefitRuleDetailService shareBenefitRuleDetailService;

    @Autowired
    private ShareBenefitRuleDetailDeviceService shareBenefitRuleDetailDeviceService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ShareBenefitRuleDao shareBenefitRuleDao;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    /**
     * 获取所有可用的分润规则
     * @return
     */
    public List<ShareBenefitRule> getAllUsableShareRule(){
        EntityWrapper<ShareBenefitRule> shareBenefitRuleEntityWrapper = new EntityWrapper<>();
        shareBenefitRuleEntityWrapper.eq("is_deleted", BooleanEnum.FALSE.getCode());

        return selectList(shareBenefitRuleEntityWrapper);
    }

    @Override
    public Page<ShareBenefitRuleListDto> listPage(Pageable<ShareBenefitRuleQueryDto> pageable) {
        logger.info("查看用户【：" + pageable.getQuery().getCreatorId() + "】创建的分润规则");
        pageable.setOrderByField("utime");
        pageable.setAsc(false);
        Page<ShareBenefitRule> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<ShareBenefitRule> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<ShareBenefitRuleListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
            selectPage.getRecords().forEach(item -> {
                ShareBenefitRuleListDto dto = new ShareBenefitRuleListDto(item);
                dto.setFrequencyDesc(ShareBenefitRuleFrequency.getDesc(item.getFrequency()));
                result.getRecords().add(dto);
            });
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertShareBenefitRule(ShareBenefitRuleDto shareBenefitRuleDto) {
        //全选操作,本级所有设备都参与分润
        if(CollectionUtils.isNotEmpty(shareBenefitRuleDto.getRuleDetailDtoList())
                && shareBenefitRuleDto.getRuleDetailDtoList().size()==1
                && shareBenefitRuleDto.getRuleDetailDtoList().get(0).getShareType().equals(ShareRuleDetailType.ALL.getCode())){

            Wrapper wrapper = new EntityWrapper<Device>().eq("owner_id",getCurrentModifySysAccountId(shareBenefitRuleDto));
            wrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()); // fix http://redmine.gizwits.com/issues/9866
            List<Device> deviceList = deviceService.selectList(wrapper);
            if(CollectionUtils.isNotEmpty(deviceList)){
                List<ShareBenefitRuleDetailDeviceDto> list = new ArrayList<>();
                for(Device device:deviceList){
                    ShareBenefitRuleDetailDeviceDto deviceDto = new ShareBenefitRuleDetailDeviceDto();
                    deviceDto.setSno(device.getSno());
                    list.add(deviceDto);
                }
                shareBenefitRuleDto.getRuleDetailDtoList().get(0).setRuleDetailDeviceDtoList(list);
            }

        }

        //检查新增的分润规则是否满足条件
        if(!checkShareBenefitRulePercentage(shareBenefitRuleDto)){
            LeaseException.throwSystemException(LeaseExceEnums.SHARE_RULE_SET_ERROR);
            return false;
        }

        //生成rule主表
        ShareBenefitRule shareBenefitRule = convertRuleDtoToShareBenefitRuleAndUpdate(shareBenefitRuleDto);
        String ruleId = shareBenefitRule.getId();
        logger.info("新插入的分润规则：" + shareBenefitRule.getId() + ",名称：" + shareBenefitRuleDto.getRuleName());

        Integer currentModifySysAccountId = getCurrentModifySysAccountId(shareBenefitRuleDto);
        if(Objects.isNull(currentModifySysAccountId)){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        Operator operator = operatorService.getOperatorByAccountId(shareBenefitRuleDto.getSysAccountId());
        if(Objects.nonNull(operator)){
            operator.setShareBenefitRuleId(ruleId);
            operator.setUtime(new Date());
            operatorService.updateById(operator);
        }else {
            Agent currentAgent = agentService.getAgentBySysAccountId(shareBenefitRuleDto.getSysAccountId());
            if(Objects.nonNull(currentAgent)){
                currentAgent.setShareBenefitRuleId(ruleId);
                currentAgent.setUtime(new Date());
                agentService.updateById(currentAgent);
            }else{
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
        }

        shareBenefitRuleDto.setRuleId(ruleId);
        shareBenefitRuleDetailService.insertShareBenefitRuleDetail(shareBenefitRuleDto);
        return true;
    }

    /**
     * 检查分润规则明细是否满足条件
     * @param shareBenefitRuleDto
     * @return
     */
    public boolean checkShareBenefitRulePercentage(ShareBenefitRuleDto shareBenefitRuleDto) {
        if(Objects.isNull(shareBenefitRuleDto)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        if(ParamUtil.isNullOrEmptyOrZero(shareBenefitRuleDto.getRuleDetailDtoList())){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }

        //获取运营商或者代理商的系统用户ID
        Integer currentSysAccountId = getCurrentModifySysAccountId(shareBenefitRuleDto);

        //获取级的分润规则
        ShareBenefitRule parentRule = getParentShareBenefitRule(currentSysAccountId);
        /**
         * 获取分润规则对象的父级,如果父级不存在,或者父级是代理商或运营商,则说明上级没有设置分润规则,不返回任何设备
         */
        SysUser currentSysUser = sysUserService.selectById(currentSysAccountId);
        SysUser parentSysUser = sysUserService.selectById(currentSysUser.getSysUserId());
        boolean isOperator = operatorService.getOperatorByAccountId(parentSysUser.getId()) != null;
        boolean isAgent = agentService.getAgentBySysAccountId(parentSysUser.getId()) != null;
        if (Objects.isNull(parentRule)){
            return !Objects.isNull(parentSysUser)
                    && (!Objects.nonNull(parentSysUser)
                    || (!isOperator
                    && !isAgent));
        }

        for(ShareBenefitRuleDetailDto ruleDetail:shareBenefitRuleDto.getRuleDetailDtoList()){
            if(!checkAndUpdateRuleDetailPercentage(ruleDetail,parentRule)){
                LeaseException.throwSystemException(LeaseExceEnums.SHARE_RULE_SET_ERROR);
                return false;
            }
        }

        return true;
    }

    /**
     * 检查设置的分润规则与父级设置的分润规则范围是否有出入,即设置的分润比例是否大于父级的比例
     * @param ruleDetailDto
     * @param parentRule
     * @return
     */
    public boolean checkAndUpdateRuleDetailPercentage(ShareBenefitRuleDetailDto ruleDetailDto, ShareBenefitRule parentRule){
        if(ParamUtil.isNullOrEmptyOrZero(ruleDetailDto)||ParamUtil.isNullOrEmptyOrZero(ruleDetailDto.getRuleDetailDeviceDtoList())){
            return false;
        }
        List<String> updateDeviceSnos = new ArrayList<>();

        for(ShareBenefitRuleDetailDeviceDto deviceDto:ruleDetailDto.getRuleDetailDeviceDtoList()){
            //获取设备在父级上的分润比例及规则
            ShareBenefitRuleDeviceVo parentDeviceRule = shareBenefitRuleDetailDeviceService.getDeviceShareRuleBySysAccountId(deviceDto.getSno(),parentRule.getSysAccountId());
            if(Objects.isNull(parentDeviceRule) //设备在上一级未设置分润比例,说明上一级不参与该设备的分润,则子级也不能参与分润
                    ||parentDeviceRule.getSharePercentage().compareTo(ruleDetailDto.getSharePercentage())<0){//设置的分润规则大于父级的分润比例
                logger.error("===设备{}设置的分润比例{}大于父级的分润比例{},分润规则创建失败===",deviceDto.getSno(),ruleDetailDto.getSharePercentage().doubleValue(), parentDeviceRule.getSharePercentage().doubleValue());
                LeaseException.throwSystemException(LeaseExceEnums.SHARE_PERCENTAGE_RANG_ERROR);
                return false;
            }

            updateDeviceSnos.add(deviceDto.getSno());
        }

        ShareBenefitDeviceUpdateDto updateDto = new ShareBenefitDeviceUpdateDto();
        updateDto.setChildrenPercentage(ruleDetailDto.getSharePercentage());
        updateDto.setDeviceSnoList(updateDeviceSnos);
        updateDto.setRuleId(parentRule.getId());
        shareBenefitRuleDetailDeviceService.updateDevicePercentage(updateDto);
        return true;
    }

    /**
     * 获取当前操作的分润规则的对象sysAccountId
     * @param shareBenefitRuleDto
     * @return
     */
    public Integer getCurrentModifySysAccountId(ShareBenefitRuleDto shareBenefitRuleDto){
//        if(!ParamUtil.isNullOrEmptyOrZero(shareBenefitRuleDto.getOperatorId())){//为运营商设置分润规则
//            Operator currentOperator = operatorService.selectById(shareBenefitRuleDto.getOperatorId());
//            if(Objects.isNull(currentOperator)){
//                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
//            }
//            return currentOperator.getSysAccountId();
//        }else if(!ParamUtil.isNullOrEmptyOrZero(shareBenefitRuleDto.getAgentId())){//为代理商设置分润规则
//            Agent currentAgent = agentService.selectById(shareBenefitRuleDto.getAgentId());
//            if(Objects.isNull(currentAgent)){
//                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
//            }
//            return currentAgent.getSysAccountId();
//        }else{
//            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
//            return null;
//        }
        return shareBenefitRuleDto.getSysAccountId();
    }

    /**
     * 获取当前用户的上一级的分润规则,上一级有可能是运营商也有可能是代理商
     * @param sysAccountId
     * @return
     */
    public ShareBenefitRule getParentShareBenefitRule(Integer sysAccountId){
        Integer shareRuleSysAccountId = resolveParentOperatorAccountId(sysAccountId);
        if(Objects.nonNull(shareRuleSysAccountId)){
            return getRuleBySysAccountId(shareRuleSysAccountId);
        }

        return null;
    }

    /**
     * 获取currentSysAccountId的直接上级运营SysAccountId
     * @param currentSysAccountId
     * @return
     */
    public Integer resolveParentOperatorAccountId(Integer currentSysAccountId){
        if(Objects.nonNull(currentSysAccountId)){
            Operator parentOperator = operatorService.getParentOperator(currentSysAccountId);
            if(Objects.nonNull(parentOperator)){//父级运营商
                return parentOperator.getSysAccountId();
            }

            Agent agent = agentService.getParentAgentBySysAccountId(currentSysAccountId);
            if(Objects.nonNull(agent)){
                return agent.getSysAccountId();
            }
        }
        return null;
    }

    /**
     * 将shareBenefitRuleDto转换为ShareBenefitRule对象
     * @param shareBenefitRuleDto
     * @return
     */
    public ShareBenefitRule convertRuleDtoToShareBenefitRuleAndUpdate(ShareBenefitRuleDto shareBenefitRuleDto) {
        SysUser sysUser = sysUserService.getCurrentUser();
        ShareBenefitRule shareBenefitRule = new ShareBenefitRule();
        if (!ParamUtil.isNullOrEmptyOrZero(shareBenefitRuleDto.getRuleId())) {
            shareBenefitRule = selectById(shareBenefitRuleDto.getRuleId());
            if(Objects.isNull(shareBenefitRule)){
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
        }else{
            shareBenefitRule.setCtime(new Date());
            shareBenefitRule.setLastExecuteTime(new Date());
            Integer currentModifySysAccountId = getCurrentModifySysAccountId(shareBenefitRuleDto);
            if(Objects.isNull(currentModifySysAccountId)){
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
            shareBenefitRule.setSysAccountId(currentModifySysAccountId);
            shareBenefitRule.setOperatorName(shareBenefitRuleDto.getOperatorName());
            shareBenefitRule.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
        }

        shareBenefitRule.setUtime(new Date());
        shareBenefitRule.setShareBenefitRuleName(shareBenefitRuleDto.getRuleName());
        shareBenefitRule.setStartTime(shareBenefitRuleDto.getStartTime());
        shareBenefitRule.setFrequency(shareBenefitRuleDto.getFrequency());
        shareBenefitRule.setSysUserId(sysUser.getId());
        insertOrUpdate(shareBenefitRule);
        return shareBenefitRule;
    }


    /**
     * 获取一批设备在currentOperatorSysAccountId的分润比例区间
     * @param deviceSnoList
     * @param currentOperatorSysAccountId
     * @return
     */
    public ShareBenefitDeviceRangeVo calculateShareRuleRange(List<String> deviceSnoList, Integer currentOperatorSysAccountId){
        if(CollectionUtils.isEmpty(deviceSnoList)){
            deviceSnoList = new ArrayList<>();
            logger.error("====传递进来的设备列表参数为空=====");
            //LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
            EntityWrapper<Device> deviceEntityWrapper = new EntityWrapper<>();
            deviceEntityWrapper.eq("owner_id",currentOperatorSysAccountId).eq("is_deleted",BooleanEnum.FALSE.getCode());
            List<Device> deviceList = deviceService.selectList(deviceEntityWrapper);
            if(CollectionUtils.isEmpty(deviceList)){
                logger.error("====未查询到用户{}有设备=====",currentOperatorSysAccountId);
                LeaseException.throwSystemException(LeaseExceEnums.HAS_NO_DEVICE_CANT_SET_SHARE_RULE);
            }
            for(Device device:deviceList){
                deviceSnoList.add(device.getSno());
            }
        }
        //获取父级设置的分润规则
        Map<String,ShareBenefitRuleDeviceVo> parentDeviceRuleMap = getParentRuleDeviceMap(currentOperatorSysAccountId);

        //获取子级运营商或代理商设置的分润规则
        Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap = getChildrenRuleDeviceMap(currentOperatorSysAccountId);

        BigDecimal minPercentage = BigDecimal.ZERO;
        BigDecimal maxPercentage = new BigDecimal(100.00D).setScale(2);
        //父级没有分润规则,说明是厂商,分润比例的最大比例的下限为100.00
        if(Objects.isNull(parentDeviceRuleMap)){
            maxPercentage = new BigDecimal(100.00D).setScale(2);
        }

        for (String sno: deviceSnoList){

            if(Objects.nonNull(parentDeviceRuleMap)){//父级有分润比例设置,说明非厂商
                if(!parentDeviceRuleMap.containsKey(sno)){ //父级分润规则未包含该设备,则子级也不能包含该设备的分润
                    logger.error("=====父级未对设备{}设置分润比例,因此子级也不能有分润=====",sno);
                    LeaseException.throwSystemException(LeaseExceEnums.SHARE_PARENT_NOT_SET_RULE_FOR_DEVICE);
                }
                //获取最大比例的下限
                if(maxPercentage.compareTo(parentDeviceRuleMap.get(sno).getSharePercentage())>0){//父级有分润比例设置,说明非厂商,需要计算出分润区间的上限
                    maxPercentage = parentDeviceRuleMap.get(sno).getSharePercentage();
                }
            }
            //获取最小比例的上限
            if(Objects.nonNull(childrenDeviceRuleMap)){
                if(childrenDeviceRuleMap.containsKey(sno)){
                    if(minPercentage.compareTo(childrenDeviceRuleMap.get(sno).getSharePercentage())<0){
                        minPercentage = childrenDeviceRuleMap.get(sno).getSharePercentage();
                    }
                }
            }
        }
        return new ShareBenefitDeviceRangeVo(minPercentage, maxPercentage);
    }


    private Map<String,ShareBenefitRuleDeviceVo> getParentRuleDeviceMap(Integer currentOperatorSysAccountId){
        Map<String,ShareBenefitRuleDeviceVo> parentDeviceRuleMap = null;
        ShareBenefitRule parentShareRule = getParentShareBenefitRule(currentOperatorSysAccountId);
        if(Objects.nonNull(parentShareRule)){
            logger.info("=====当前登录用户不是厂商,操作直接子级运营者=====");
            List<ShareBenefitRuleDeviceVo> allParentDeviceRules = shareBenefitRuleDetailDeviceService.getAllDeviceShareRuleByRuleId(parentShareRule.getId());
            if(ParamUtil.isNullOrEmptyOrZero(allParentDeviceRules)){
                logger.info("====运营者{}的父级运营者{}未设置分润规则=====",currentOperatorSysAccountId, parentShareRule.getSysAccountId());
            }
            parentDeviceRuleMap = covertDeviceListToDeviceMap(allParentDeviceRules);
            if (Objects.isNull(parentDeviceRuleMap)){
                logger.info("====运营者{}的父级运营者{}未设置分润规则=====",currentOperatorSysAccountId, parentShareRule.getSysAccountId());
            }
        }
        return parentDeviceRuleMap;
    }

    private Map<String,ShareBenefitRuleDeviceVo> getChildrenRuleDeviceMap(Integer currentOperatorSysAccountId){
        List<ShareBenefitRule> childrenRuleList = new ArrayList<>();
        resolveChildrenRules(currentOperatorSysAccountId, childrenRuleList);
        Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap = covertRuleListToDeviceMap(childrenRuleList);
        return childrenDeviceRuleMap;
    }

    public Map<String,ShareBenefitRuleDeviceVo> covertRuleListToDeviceMap(List<ShareBenefitRule> childrenRuleList){
        if(CollectionUtils.isEmpty(childrenRuleList)){
            return null;
        }
        List<ShareBenefitRuleDeviceVo> allDeviceRuleVoList = new ArrayList<>();
        for (ShareBenefitRule rule:childrenRuleList){
            List<ShareBenefitRuleDeviceVo> list = shareBenefitRuleDetailDeviceService.getAllDeviceShareRuleByRuleId(rule.getId());
            if(CollectionUtils.isNotEmpty(list)){
                allDeviceRuleVoList.addAll(list);
            }
        }
        return covertDeviceListToDeviceMap(allDeviceRuleVoList);
    }

    private Map<String,ShareBenefitRuleDeviceVo> covertDeviceListToDeviceMap(List<ShareBenefitRuleDeviceVo> deviceVoList){
        Map<String,ShareBenefitRuleDeviceVo> map = new HashedMap();
        if(CollectionUtils.isNotEmpty(deviceVoList)){
            for(ShareBenefitRuleDeviceVo deviceVo:deviceVoList){
                map.put(deviceVo.getSno(),deviceVo);
            }
            return map;
        }
        return null;
    }

    /**
     * 回调函数获取当前用户的下一级的分润规则列表
     * @param sysUserId
     * @param ruleList
     */
    @Override
    public void resolveChildrenRules(Integer sysUserId, List<ShareBenefitRule> ruleList) {
        if (Objects.nonNull(sysUserId)) {
            List<SysUser> children = sysUserService.selectList(new EntityWrapper<SysUser>().eq("sys_user_id", sysUserId).ne("id", sysUserId));
            if (CollectionUtils.isNotEmpty(children)) {
                for(SysUser user:children){
                    ShareBenefitRule rule = getRuleBySysAccountId(user.getId());
                    if(Objects.nonNull(rule)){
                        ruleList.add(rule);
                        continue;
                    }
                    resolveChildrenRules(user.getId(), ruleList);
                }
            }
        }
    }

    /**
     * 获取SysUserId的所有直接子用户运营者
     * @param sysUserIds
     */
    private void resolveChildrenOperatorIdsHasNoShareRule(List<Integer> sysUserIds, List<Integer> results){
        if (Objects.nonNull(sysUserIds)) {
            List<SysUser> children = sysUserService.selectList(new EntityWrapper<SysUser>().in("sys_user_id", sysUserIds));
            if (CollectionUtils.isNotEmpty(children)) {
                for(SysUser user:children){
                    Operator currentOperator = operatorService.getOperatorByAccountId(user.getId());
                    if(Objects.nonNull(currentOperator)&& StringUtils.isBlank(currentOperator.getShareBenefitRuleId())
                            && Objects.isNull(getRuleBySysAccountId(currentOperator.getSysAccountId()))){
                        results.add(user.getId());
                        continue;
                    }
                    Agent agent = agentService.getAgentBySysAccountId(user.getId());
                    if(Objects.nonNull(agent)&& StringUtils.isBlank(agent.getShareBenefitRuleId())
                            && Objects.isNull(getRuleBySysAccountId(agent.getSysAccountId()))){
                        results.add(user.getId());
                        continue;
                    }
                    //resolveChildrenOperatorIdsHasNoShareRule(user.getId(), results);
                }
            }
        }
    }


    /**
     * 获取SysUserId的所有直接子用户运营者
     * @param sysUserId
     * @param results
     */
    @Override
    public void resolveChildrenOperatorIds(Integer sysUserId, List<Integer> results){
        if (Objects.nonNull(sysUserId)) {
            SysUser sysUser = sysUserService.selectById(sysUserId);
            List<SysUser> children = sysUserService.selectList(new EntityWrapper<SysUser>().in("sys_user_id", sysUserService.resolveAccessableUserIds(sysUser)).ne("id", sysUserId));
            if (CollectionUtils.isNotEmpty(children)) {
                for(SysUser user:children){
                    Operator currentOperator = operatorService.getOperatorByAccountId(user.getId());
                    if(Objects.nonNull(currentOperator)){
                        results.add(user.getId());
                        //continue;
                    }
                    Agent agent = agentService.getAgentBySysAccountId(user.getId());
                    if(Objects.nonNull(agent)){
                        results.add(user.getId());
                        //continue;
                    }
                    //resolveChildrenOperatorIds(user.getId(), results);
                }
            }
        }
    }


    @Override
    public ShareBenefitRule getRuleBySysAccountId(Integer sysAccountId) {
        logger.info("对比运营商【" + sysAccountId + "】的分润规则");
        EntityWrapper<ShareBenefitRule> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_account_id", sysAccountId).eq("is_deleted",DeleteStatus.NOT_DELETED.getCode());

        return selectOne(entityWrapper);
    }

    @Override
    public boolean selectNameIsExist(ShareBenefitRuleNameCheckDto checkDto) {
        logger.info("查看分润规则名：" + checkDto.getName() + "是否重复");
        SysUser sysUser = sysUserService.getCurrentUser();
        EntityWrapper<ShareBenefitRule> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("share_benefit_rule_name", checkDto.getName()).eq("sys_user_id", sysUser.getId())
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        if(!ParamUtil.isNullOrEmptyOrZero(checkDto.getId())){
            entityWrapper.ne("id",checkDto.getId());
        }

        ShareBenefitRule shareBenefitRule = selectOne(entityWrapper);
        return !ParamUtil.isNullOrEmptyOrZero(shareBenefitRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateShareBenefitRule(ShareBenefitRuleDto shareBenefitRuleDto) {
        ShareBenefitRule dbShareRule = selectById(shareBenefitRuleDto.getRuleId());
        if (Objects.isNull(dbShareRule)
                || !dbShareRule.getSysUserId().equals(sysUserService.getCurrentUser().getId())){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        Integer modifySysAccountId = getCurrentModifySysAccountId(shareBenefitRuleDto);
        ShareBenefitRule shareBenefitRule = convertRuleDtoToShareBenefitRuleAndUpdate(shareBenefitRuleDto);

        //用户修改后的设备分润比例
        Map<String,ShareBenefitRuleDeviceVo> newDeviceRuleMap = covertRuleDtoToDeviceMap(shareBenefitRuleDto);
        if(MapUtils.isEmpty(newDeviceRuleMap)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }

        //获取父运营者的分润规则
        Map<String,ShareBenefitRuleDeviceVo> parentDeviceRuleMap = getParentRuleDeviceMap(modifySysAccountId);

        /**
         * 将旧分润规则关联的父级设备分润规则中的childrenPercentage置为0
         */
        //获取旧的设备分润规则相关信息
        List<ShareBenefitRuleDeviceVo> oldDeviceRuleList = shareBenefitRuleDetailDeviceService.getAllDeviceShareRuleByRuleId(shareBenefitRule.getId());
        Map<String,ShareBenefitRuleDeviceVo> oldDeviceRuleMap = covertDeviceListToDeviceMap(oldDeviceRuleList);
        if(MapUtils.isNotEmpty(oldDeviceRuleMap)){
            Iterator<String> iterator = oldDeviceRuleMap.keySet().iterator();
            while (iterator.hasNext()){
                String sno = iterator.next();
//                if(Objects.isNull(parentDeviceRuleMap)||
//                        !parentDeviceRuleMap.containsKey(sno)){
//                    logger.error("=====父级未给设备{}设置分润规则===",sno);
//                    LeaseException.throwSystemException(LeaseExceEnums.SHARE_PARENT_NOT_SET_RULE_FOR_DEVICE);
//                }
                if(Objects.nonNull(parentDeviceRuleMap)&&parentDeviceRuleMap.containsKey(sno)){
                    shareBenefitRuleDetailDeviceService.updateDeviceChildrenPercentage(sno, BigDecimal.ZERO, parentDeviceRuleMap.get(sno).getRuleId()+"");
                }
            }
        }

        /**
         * 检查新的分润规则设备是否在父级运营者分润规则中存在,以及比例是否大于父级分润比例
         * 如果检验通过则修改父级相应设备的子运营者的分润比例childrenPercentage
         */
        if(MapUtils.isNotEmpty(newDeviceRuleMap)){
            Iterator<String> iterator = newDeviceRuleMap.keySet().iterator();
            while (iterator.hasNext()){
                String sno = iterator.next();
//                if(Objects.isNull(parentDeviceRuleMap)||
//                        !parentDeviceRuleMap.containsKey(sno)){
//                    logger.error("=====父级未给设备{}设置分润规则===",sno);
//                    LeaseException.throwSystemException(LeaseExceEnums.SHARE_PARENT_NOT_SET_RULE_FOR_DEVICE);
//                }
//                if(Objects.nonNull(parentDeviceRuleMap)&&
//                        parentDeviceRuleMap.get(sno).getSharePercentage().compareTo(newDeviceRuleMap.get(sno).getSharePercentage())<0){
//                    logger.error("=====设备{}设置的分润比例{}大于父级设置的分润比例{}=====",sno,newDeviceRuleMap.get(sno).getSharePercentage().doubleValue(),parentDeviceRuleMap.get(sno).getSharePercentage().doubleValue());
//                    LeaseException.throwSystemException(LeaseExceEnums.SHARE_DEVICE_PERCENTAGE_BIG_THAN_PARENT);
//                }
                if(Objects.nonNull(parentDeviceRuleMap)&&
                        parentDeviceRuleMap.containsKey(sno)){
                    shareBenefitRuleDetailDeviceService.updateDeviceChildrenPercentage(sno, newDeviceRuleMap.get(sno).getSharePercentage(), parentDeviceRuleMap.get(sno).getRuleId()+"");
                }
            }
        }

        /**
         * 检查子运营者中所有设备的分润规则在当前新的分润规则中是否存在,以及分润比例是否小于子运营者的比例
         */
        //获取子运营者所有的分润规则
        List<ShareBenefitRule> childrenRuleList = new ArrayList<>();
        resolveChildrenRules(modifySysAccountId, childrenRuleList);
        Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap = covertRuleListToDeviceMap(childrenRuleList);
        //当前运营者有子运营者同时设置有分润规则
        if(MapUtils.isNotEmpty(childrenDeviceRuleMap)){
            Iterator<String> iterator = childrenDeviceRuleMap.keySet().iterator();
            while (iterator.hasNext()){
                String sno = iterator.next();
                if(Objects.nonNull(newDeviceRuleMap)&&
                        !newDeviceRuleMap.containsKey(sno)){//新的分润规则中不包含子运营者中的设备分润
                    logger.error("====设备{}在子运营者中有分润规则{}百分比{},在修改后的分润规则中不包含=====",sno,childrenDeviceRuleMap.get(sno).getRuleDetailId(),childrenDeviceRuleMap.get(sno).getSharePercentage().doubleValue());
                    LeaseException.throwSystemException(LeaseExceEnums.SHARE_RULE_SET_ERROR);
                }
                if(Objects.nonNull(newDeviceRuleMap)&&
                        childrenDeviceRuleMap.get(sno).getSharePercentage().compareTo(newDeviceRuleMap.get(sno).getSharePercentage())>0){
                    logger.error("====设备{}在子运营者中的分润比例{}大于修改后的分润比例{}=====",sno,childrenDeviceRuleMap.get(sno).getSharePercentage().doubleValue(),newDeviceRuleMap.get(sno).getSharePercentage().doubleValue());
                    LeaseException.throwSystemException(LeaseExceEnums.SHARE_DEVICE_PERCENTAGE_LITTLE_THAN_CHILDREN);
                }
            }
        }

        return shareBenefitRuleDetailService.updateShareBenefitRuleDatail(shareBenefitRuleDto,childrenDeviceRuleMap);
    }

    private Map<String,ShareBenefitRuleDeviceVo> covertRuleDtoToDeviceMap(ShareBenefitRuleDto shareBenefitRuleDto){
        if(Objects.isNull(shareBenefitRuleDto)){
            return null;
        }
        if(CollectionUtils.isEmpty(shareBenefitRuleDto.getRuleDetailDtoList())){
            return null;
        }
        Map<String,ShareBenefitRuleDeviceVo> map = new HashedMap();
        for(ShareBenefitRuleDetailDto detailDto:shareBenefitRuleDto.getRuleDetailDtoList()){
            if(detailDto.getShareType().equals(ShareRuleDetailType.SINGLE.getCode())
                    &&CollectionUtils.isNotEmpty(detailDto.getRuleDetailDeviceDtoList())){
                for(ShareBenefitRuleDetailDeviceDto deviceDto:detailDto.getRuleDetailDeviceDtoList()){
                    ShareBenefitRuleDeviceVo deviceVo = new ShareBenefitRuleDeviceVo();
                    deviceVo.setSno(deviceDto.getSno());
                    deviceVo.setSharePercentage(detailDto.getSharePercentage());
                    map.put(deviceDto.getSno(),deviceVo);
                }
            }
            if(detailDto.getShareType().equals(ShareRuleDetailType.ALL.getCode())){
                SysUser sysUser = sysUserService.selectById(shareBenefitRuleDto.getSysAccountId());
                List<Integer> accessSysAccountIds = sysUserService.resolveAccessableUserIds(sysUser,false,true);
                List<Device> deviceList = deviceService.selectList(new EntityWrapper<Device>().in("owner_id",accessSysAccountIds).eq("is_deleted",BooleanEnum.FALSE.getCode()));
                if (CollectionUtils.isNotEmpty(deviceList)){
                    List<ShareBenefitRuleDetailDeviceDto> detailDeviceDtos = new ArrayList<>();
                    for (Device device:deviceList){
                        ShareBenefitRuleDeviceVo deviceVo = new ShareBenefitRuleDeviceVo();
                        deviceVo.setSno(device.getSno());
                        deviceVo.setSharePercentage(detailDto.getSharePercentage());
                        map.put(device.getSno(),deviceVo);

                        ShareBenefitRuleDetailDeviceDto shareBenefitRuleDetailDeviceDto = new ShareBenefitRuleDetailDeviceDto();
                        shareBenefitRuleDetailDeviceDto.setSno(device.getSno());
                        detailDeviceDtos.add(shareBenefitRuleDetailDeviceDto);
                    }
                    shareBenefitRuleDto.getRuleDetailDtoList().get(0).setRuleDetailDeviceDtoList(detailDeviceDtos);
                }
            }
        }

        return map;
    }

    public void checkRule(ShareBenefitRuleDto shareBenefitRuleDto){
        if(Objects.isNull(shareBenefitRuleDto)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        if(ParamUtil.isNullOrEmptyOrZero(shareBenefitRuleDto.getRuleDetailDtoList())){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }

        Map<String, ShareBenefitRuleDetailDeviceDto> deviceDtoMap = new HashedMap();
        for(ShareBenefitRuleDetailDto detailDto:shareBenefitRuleDto.getRuleDetailDtoList()){
            for(ShareBenefitRuleDetailDeviceDto deviceDto:detailDto.getRuleDetailDeviceDtoList()){

            }
        }
    }

    @Override
    public ShareBenefitRuleDto shareBenefitRuleDetail(String ruleId) {
        logger.info("分润规则【" + ruleId + "】的详情");
        ShareBenefitRule shareBenefitRule = selectById(ruleId);
        ShareBenefitRuleDto shareBenefitRuleDto = new ShareBenefitRuleDto();
        shareBenefitRuleDto.setRuleId(shareBenefitRule.getId());
        shareBenefitRuleDto.setFrequency(shareBenefitRule.getFrequency());
        shareBenefitRuleDto.setFrequencyDescription(ShareBenefitRuleFrequency.getDesc(shareBenefitRule.getFrequency()));
        Operator operator = operatorService.getOperatorByAccountId(shareBenefitRule.getSysAccountId());
        if(Objects.nonNull(operator)){
            shareBenefitRuleDto.setSysAccountId(operator.getSysAccountId());
        }else{
            Agent agent = agentService.getAgentBySysAccountId(shareBenefitRule.getSysAccountId());
            if(Objects.nonNull(agent)){
                shareBenefitRuleDto.setSysAccountId(agent.getSysAccountId());
            }else{
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
        }
        shareBenefitRuleDto.setOperatorName(shareBenefitRule.getOperatorName());
        shareBenefitRuleDto.setStartTime(shareBenefitRule.getStartTime());
        shareBenefitRuleDto.setRuleName(shareBenefitRule.getShareBenefitRuleName());
        List<ShareBenefitRuleDetailDto> detailDtos = getShareBenefitRuleDetailDtoList(ruleId, shareBenefitRuleDto.getSysAccountId());
        shareBenefitRuleDto.setRuleDetailDtoList(detailDtos);
        return shareBenefitRuleDto;
    }

    /**
     * 获得详细分润数据
     * @param ruleId
     * @return
     */
    @Override
    public List<ShareBenefitRuleDetailDto> getShareBenefitRuleDetailDtoList(String ruleId, Integer sysAccountId) {
        List<ShareBenefitRuleDetailDto> detailDtos = new ArrayList<>();
        List<ShareBenefitRuleDetail> details = shareBenefitRuleDetailService.selectByRuleId(ruleId);
        for(ShareBenefitRuleDetail detail:details){
            ShareBenefitRuleDetailDto detailDto = new ShareBenefitRuleDetailDto();
            detailDto.setRuleDetailId(detail.getId());
            detailDto.setRuleDetailName(detail.getName());
            detailDto.setSharePercentage(detail.getSharePercentage());
            detailDto.setShareType(detail.getShareType());
            detailDto.setShareTypeDescription(ShareRuleDetailType.getDescription(detail.getShareType()));

            List<ShareBenefitRuleDetailDeviceDto> detailDeviceDtos = getShareBenefitRuleDetailDeviceDtoList(detail);
            //查询该批设备的分润区间
            detailDto.setDevicePercentageRange(getShareRuleDetailRangeVo(detailDeviceDtos,sysAccountId));
            detailDto.setRuleDetailDeviceDtoList(detailDeviceDtos);
            detailDto.setDevicePercentageRange(getShareRuleDetailRangeVo(detailDeviceDtos, sysAccountId));//获取分润规则明细规则中的分润比例区间
            detailDtos.add(detailDto);
        }
        return detailDtos;
    }

    private ShareBenefitDeviceRangeVo getShareRuleDetailRangeVo(List<ShareBenefitRuleDetailDeviceDto> deviceDtoList, Integer sysAccountId){
        if(CollectionUtils.isEmpty(deviceDtoList)){
            return null;
        }
        List<String> snoList = new ArrayList<>();
        for(ShareBenefitRuleDetailDeviceDto deviceDto:deviceDtoList){
            snoList.add(deviceDto.getSno());
        }
        return calculateShareRuleRange(snoList,sysAccountId);
    }

    /**
     * 获得详细分润设备数据
     * @param detail
     * @return
     */
    public List<ShareBenefitRuleDetailDeviceDto> getShareBenefitRuleDetailDeviceDtoList(ShareBenefitRuleDetail detail) {
        List<ShareBenefitRuleDetailDeviceDto> detailDeviceDtos = new ArrayList<>();
        List<ShareBenefitRuleDetailDevice> detailDevices = shareBenefitRuleDetailDeviceService.selectByRuleDetailId(detail.getId());

        for(ShareBenefitRuleDetailDevice detailDevice:detailDevices){
            ShareBenefitRuleDetailDeviceDto detailDeviceDto = new ShareBenefitRuleDetailDeviceDto();
            detailDeviceDto.setRuleDetailDeviceId(detailDevice.getId());
            detailDeviceDto.setSno(detailDevice.getSno());
            Device device = deviceService.selectById(detailDevice.getSno());
            detailDeviceDto.setDeviceName(device.getName());
            detailDeviceDto.setLaunchArea(device.getLaunchAreaName());
            detailDeviceDto.setMac(device.getMac());
            detailDeviceDtos.add(detailDeviceDto);
        }
        return detailDeviceDtos;
    }

    @Override
    public List<Operator> listSonOperator(Integer sysAccountId) {
        logger.info("获得【" + sysAccountId + "】的子运营商");
        List<Operator> operatorList = new ArrayList<>();
        List<Operator> operators = operatorService.getDirectOperatorByCreator(sysAccountId);
        for (Operator operator : operators) {
            if (ParamUtil.isNullOrEmptyOrZero(operator.getShareBenefitRuleId())) {
                operatorList.add(operator);
            }
        }
        return operatorList;
    }

    /**
     * 获取当前用户的直接子运营用户信息
     * @return
     */
    @Override
    public List<ShareBenefitOperatorObjectDto> listShareRuleOperatorObject(){
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> directAccountIdList  = sysUserService.resolveAccessableUserIds(currentUser);
        //resolveChildrenOperatorIdsHasNoShareRule(sysUserService.resolveOwnerAccessableUserIds(currentUser),directAccountIdList);
        if(CollectionUtils.isEmpty(directAccountIdList)){
            return null;
        }

        List<ShareBenefitOperatorObjectDto> list = new ArrayList<>();
        for(Integer userId:directAccountIdList){
            ShareBenefitOperatorObjectDto dto = getOperatorObject(userId);
            if(Objects.nonNull(dto)){
                list.add(dto);
            }
        }
        return list;
    }


    private ShareBenefitOperatorObjectDto getOperatorObject(Integer sysAccountId){
        SysUser current = sysUserService.getCurrentUser();
        Operator operator = operatorService.getOperatorByAccountId(sysAccountId);
        if(Objects.nonNull(operator) && Objects.isNull(operator.getShareBenefitRuleId()) && operator.getSysUserId().equals(current.getId())){
            return new ShareBenefitOperatorObjectDto(operator);
        }
        Agent agent = agentService.getAgentBySysAccountId(sysAccountId);
        if(Objects.nonNull(agent) && Objects.isNull(agent.getShareBenefitRuleId()) && agent.getSysUserId().equals(current.getId())){
            return new ShareBenefitOperatorObjectDto(agent);
        }
        return null;
    }

    @Override
    public Page<Device> listDeviceByOperatorId(Pageable<OperatorDeviceDto> pageable) {
        OperatorDeviceDto operatorDeviceDto = pageable.getQuery();
        logger.info("获得【" + operatorDeviceDto.getSysAccountId() + "】运营商的设备");

        /**
         * 获取当前分润规则对象的所有子孙级的管理者,,因为有的设备已经分配到下级,查询设备需要通过owner_id去获取
         */
        List<Integer> operatorIds = new ArrayList<>();
        SysUser assignSysUser = sysUserService.selectById(operatorDeviceDto.getSysAccountId());
        operatorIds = sysUserService.resolveAccessableUserIds(assignSysUser);
        pageable.getQuery().setOperators(operatorIds);

        /**
         * 获取当前分润规则的上一级管理者,,因为只有上一级设置了分润规则的设备,才能设置到当前分润规则中,限定分润规则设备的范围
         */
        Map<String,ShareBenefitRuleDeviceVo> parentRuleDeviceMap = getParentRuleDeviceMap(assignSysUser.getId());
        if (Objects.nonNull(parentRuleDeviceMap) && parentRuleDeviceMap.size()>=0){
            List<String> snos = new ArrayList<>();
            snos.addAll(parentRuleDeviceMap.keySet());
            pageable.getQuery().setSnos(snos);
        }else{
            /**
             * 获取分润规则对象的父级,如果父级不存在,或者父级是代理商或运营商,则说明上级没有设置分润规则,不返回任何设备
             */
            SysUser parentSysUser = sysUserService.selectById(assignSysUser.getSysUserId());
            boolean isOperator = operatorService.getOperatorByAccountId(parentSysUser.getId()) != null;
            boolean isAgent = agentService.getAgentBySysAccountId(parentSysUser.getId()) != null;
            if (Objects.isNull(parentSysUser)
                    || (Objects.nonNull(parentSysUser)
                        &&(isOperator
                            ||isAgent))){
                return new Page<>();
            }
        }

        /**
         * 如果用户有传递进来分润规则ID,,则分润规则限定设备的范围,,,,此段逻辑有问题
         */
//        if(!ParamUtil.isNullOrEmptyOrZero(pageable.getQuery().getRuleId())){
//            List<String> snos = new ArrayList<>();
//            List<ShareBenefitRuleDeviceVo> deviceList = shareBenefitRuleDetailDeviceService.getAllDeviceShareRuleByRuleId(pageable.getQuery().getRuleId()+"");
//            deviceList.forEach(item ->{
//                snos.add(item.getSno());
//            });
//            if(CollectionUtils.isNotEmpty(snos)){
//                operatorDeviceDto.setSnos(snos);
//            }
//        }

        Page<Device> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        return deviceService.selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<Device>().eq("is_deleted",DeleteStatus.NOT_DELETED.getCode())));
    }

    @Override
    public String deleteShareBenefitRule(List<String> ids) {
        SysUser current = sysUserService.getCurrentUserOwner();
        List<Integer> userIds = Collections.singletonList(current.getId());
        List<String> fails = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        List<ShareBenefitRule> shareBenefitRules = selectList(new EntityWrapper<ShareBenefitRule>().in("id", ids).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        for (ShareBenefitRule s : shareBenefitRules) {
            //如果分配对象有为下级分配分润规则不能删除
            int sonRule = selectCount(new EntityWrapper<ShareBenefitRule>().eq("sys_user_id", s.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            if (sonRule > 0) {
                fails.add(s.getShareBenefitRuleName());
                continue;
            }
            //判断分润规则下是否使用中的订单，有则不能删除
            int usingDevice = orderBaseService.selectCount(new EntityWrapper<OrderBase>().eq("sys_user_id",s.getSysAccountId()).eq("order_status", OrderStatus.USING.getCode()));
            if (usingDevice>0){
                fails.add(s.getShareBenefitRuleName());
                continue;
            }
            if (userIds.contains(s.getSysUserId())) {
                shareBenefitSheetService.generateShareBenefitForShareRule(s);
                s.setIsDeleted(DeleteStatus.DELETED.getCode());
                updateById(s);
                List<ShareBenefitRuleDetail> list = shareBenefitRuleDetailService.selectByRuleId(s.getId());
                if (!Objects.isNull(list)) {
                    shareBenefitRuleDetailService.deleteShareBenefitRuleDatail(list);
                }
                Operator operator = operatorService.getOperatorByAccountId(s.getSysAccountId());
                if (!ParamUtil.isNullOrEmptyOrZero(operator)) {
                    operator.setShareBenefitRuleId(null);
                    operator.setUtime(new Date());
                    operatorService.updateAllColumnById(operator);
                }
                Agent agent = agentService.getAgentBySysAccountId(s.getSysAccountId());
                if (!ParamUtil.isNullOrEmptyOrZero(agent)) {
                    agent.setShareBenefitRuleId(null);
                    agent.setUtime(new Date());
                    agentService.updateAllColumnById(agent);
                }
            } else {
                fails.add(s.getShareBenefitRuleName());
            }
        }
        switch (fails.size()) {
            case 0:
                sb.append("删除成功");
                break;
            case 1:
                sb.append("删除失败，您欲删除的分润规则[" + fails.get(0) + "]旗下的设备存在多个分润规则或有使用中订单。");
                break;
            case 2:
                sb.append("删除失败，您欲删除的分润规则[" + fails.get(0) + "],[" + fails.get(1) + "]旗下的设备存在多个分润规则或有使用中订单。");
                break;
            default:
                sb.append("删除失败，您欲删除的分润规则[" + fails.get(0) + "],[" + fails.get(1) + "]等旗下的设备存在多个分润规则或有使用中订单。");
                break;
        }
        return sb.toString();
    }


    @Override
    public boolean updateRuleLastExecuteTime(String ruleId) {
        if (ParamUtil.isNullOrEmptyOrZero(ruleId)) {
            return false;
        }
        return shareBenefitRuleDao.updateRuleLastExecuteTime(ruleId) > 0;
    }

}
