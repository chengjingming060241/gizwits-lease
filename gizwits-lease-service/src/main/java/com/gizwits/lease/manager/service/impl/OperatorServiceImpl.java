package com.gizwits.lease.manager.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.dto.SysUserExtForAddDto;
import com.gizwits.boot.dto.SysUserForPullDto;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.OperatorStatusChangeDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ExistEnum;
import com.gizwits.lease.enums.IsTrueEnum;
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.event.OperatorStatusChangeEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.dao.OperatorDao;
import com.gizwits.lease.manager.dto.*;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.operator.entity.OperatorExt;
import com.gizwits.lease.operator.service.OperatorExtService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 运营商表 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@Service
public class OperatorServiceImpl extends ServiceImpl<OperatorDao, Operator> implements OperatorService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorServiceImpl.class);

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperatorExtService operatorExtService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private OperatorDao operatorDao;

    @Override
    public SysUserExt getWxConfigByOperator(Integer operatorAccount) {

        //沿着用户树往上找，找到第一个
        return resolveExt(operatorAccount);
    }

    @Override
    public boolean add(OperatorForAddDto dto) {
        //1.必要时插入系统账号
        Integer accountId = acquireBindingAccount(dto);
        //2.插入运营商
        return addOperator(dto, accountId);
    }

    @Override
    public List<SysUserForPullDto> bindingExistedSysUserList() {
        //1.当前账号创建的所有账号
        List<SysUser> ownAccounts = sysUserService.selectList(new EntityWrapper<SysUser>()
                .eq("sys_user_id", sysUserService.getCurrentUser().getId())
                .eq("is_enable", 1));
        if (CollectionUtils.isNotEmpty(ownAccounts)) {
            //2.去除已经绑定的账号
            return excludeAccountBinded(ownAccounts).stream().map(SysUserForPullDto::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Page<OperatorForListDto> page(Pageable<OperatorForQueryDto> pageable) {
        //1.获取有权限的用户列表
        Page<Operator> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<Operator> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<OperatorForListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
            selectPage.getRecords().forEach(item -> {
                OperatorForListDto dto = new OperatorForListDto(item);
                dto.setLaunchAreaCount(resolveDeviceLaunchAreaCount(item));
                dto.setDeviceCount(resolveDeviceCount(item));
                result.getRecords().add(dto);
            });
        }
        return result;
    }

    @Override
    public OperatorForDetailDto detail(Integer id) {
        Operator operator = selectById(id);
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        EntityWrapper<OperatorExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("operator_id", operator.getId());
        OperatorExt operatorExt = operatorExtService.selectOne(extEntityWrapper);
        OperatorExtDto extDto = new OperatorExtDto();
        if (operatorExt != null) {
            BeanUtils.copyProperties(operatorExt, extDto);
        }

        OperatorForDetailDto result = new OperatorForDetailDto(operator);
        result.setLaunchAreaCount(resolveDeviceLaunchAreaCount(operator));
        result.setDeviceCount(resolveDeviceCount(operator));
        result.setAccount(sysUserService.basic(operator.getSysAccountId()));
        result.setExt(extDto);
        return result;
    }

    @Override
    public OperatorForDetailDto update(OperatorForUpdateDto dto) {
        Operator operator = selectById(dto.getId());
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        if(dto.getIsAllot() == null ){
            dto.setIsAllot(0);
        }

        if(dto.getCoverLevel() == null ){
            dto.setCoverLevel(0);
        }

        checkOperatorNameDup(dto.getName(), sysUserService.getCurrentUser().getId(), operator.getId());
        String oldName = operator.getName();
        //能不能严谨一点,dto 没有值,还copy?
        BeanUtils.copyProperties(dto, operator);
        operator.setUtime(new Date());
        updateAllColumnById(operator);
        if (!ParamUtil.isNullOrEmptyOrZero(dto.getName()) && !oldName.equals(dto.getName())) {
            NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(operator, operator.getSysAccountId(), oldName, operator.getName());
            CommonEventPublisherUtils.publishEvent(nameModifyEvent);
        }
        OperatorExt operatorExt = operatorExtService.selectOne(new EntityWrapper<OperatorExt>().eq("operator_id", operator.getId()));
        if (Objects.isNull(operatorExt)) {
            operatorExt = new OperatorExt();
            operatorExt.setOperatorId(operator.getId());
        }
        operatorExt.setCashPledge(dto.getCashPledge());
        operatorExtService.insertOrUpdate(operatorExt);

        SysUserExt ext = sysUserExtService.selectById(operator.getSysAccountId());
        if (Objects.isNull(ext)) {
            ext = new SysUserExt();
            ext.setSysUserId(operator.getSysAccountId());
            ext.setSysUserName(sysUserService.selectById(ext.getSysUserId()).getUsername());
            ext.setCtime(new Date());
        }
        String oldWxReveiceName = ext.getReceiverWxName();
        String olOpenId = ext.getReceiverOpenId();
        SysUserExtForAddDto newExt = dto.getAccount().getExt();
        BeanUtils.copyProperties(newExt, ext);
        sysUserExtService.update(ext);

        //更新收款信息
        shareBenefitSheetService.updateReceiverInfo(
                olOpenId, newExt.getReceiverOpenId(), oldWxReveiceName, newExt.getReceiverWxName(), ext.getSysUserId());

        return detail(dto.getId());
    }

    @Override
    public boolean change(Integer id) {
        assertNotNull(id);
        Operator operator = selectById(id);
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        OperatorStatusChangeDto changeDto = new OperatorStatusChangeDto(operator);
        operator.setStatus(statusMap.get(operator.getStatus()).resolve(operator.getSysAccountId()));
        operator.setUtime(new Date());
        boolean result = updateById(operator);
        changeDto.setTo(operator.getStatus());
        changeDto.setCurrent(sysUserService.getCurrentUser());
        if (result) {
            CommonEventPublisherUtils.publishEvent(new OperatorStatusChangeEvent(changeDto));
        }
        return result;
    }

    @Override
    public List<Operator> getDirectOperatorByCreator(Integer creatorId) {
        return selectList(new EntityWrapper<Operator>().eq("sys_user_id", creatorId));
    }

    @Override
    public Page<Operator> getAssignableOperator(Pageable<Integer> pageable) {
        Page<Operator> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<Operator> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_user_id", pageable.getQuery());
        List<Operator> operators = new ArrayList<>();
        Page<Operator> page1 = selectPage(page, entityWrapper);
        page1.getRecords().forEach(item -> {
            if (Objects.equals(item.getIsAllot(), 1)) {
                operators.add(item);
            }
        });
        Page<Operator> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        result.setRecords(operators);
        result.setTotal(operators.size());
        return result;
    }

    @Override
    public Operator getParentOperator(Integer sysAccountId) {
        if (Objects.isNull(sysAccountId)) {
            return null;
        }
        SysUser relAccount = sysUserService.selectById(sysAccountId);
        return resolveParentOperator(relAccount);
    }

    private Operator resolveParentOperator(SysUser child) {
        //查到管理员时，直接返回null
        if (Objects.isNull(child) || Objects.equals(child.getId(), child.getSysUserId())) {
            return null;
        } else {
            SysUser parent = sysUserService.selectById(child.getSysUserId());
            Operator operator = selectOne(new EntityWrapper<Operator>().eq("sys_account_id", parent.getId()));
            if (Objects.nonNull(operator)) {
                return operator;
            } else {
                return resolveParentOperator(parent);
            }
        }
    }

    private SysUserExt resolveExt(Integer child) {
        SysUserExt ext = sysUserExtService.selectById(child);
        if (valid(ext)) {
            return ext;
        } else {
            SysUser user = sysUserService.selectById(child);
            if (Objects.nonNull(user) && !Objects.equals(user.getId(), user.getSysUserId())) {
                return resolveExt(user.getSysUserId());
            } else {
                return null;
            }
        }
    }

    private boolean valid(SysUserExt ext) {
        return Objects.nonNull(ext) && StringUtils.isNotBlank(ext.getWxId())
                && StringUtils.isNotBlank(ext.getWxAppid())
                && StringUtils.isNotBlank(ext.getWxAppSecret())
                && (Objects.nonNull(selectOne(new EntityWrapper<Operator>().eq("sys_account_id", ext.getSysUserId())))
                || Objects.nonNull(manufacturerService.selectOne(new EntityWrapper<Manufacturer>().eq("sys_account_id", ext.getSysUserId()))));
    }

    private Integer deviceCount1(Integer ownerId) {
        //该运营商或代理商直接运营的设备
        return deviceService.selectCount(new EntityWrapper<Device>()
                .eq("owner_id", ownerId)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                .isNotNull("launch_area_name"));
    }

    private Integer deviceCount2(Integer ownerId) {
        //该运营商或代理商直接运营的设备
        return deviceService.selectCount(new EntityWrapper<Device>()
                .eq("owner_id", ownerId)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                .isNull("launch_area_name"));
    }

    private Integer launchAreaCount(Integer owenerId) {
        //运营商或代理商创建的投放点（未分配运营商）
        return deviceLaunchAreaService.selectCount(new EntityWrapper<DeviceLaunchArea>()
                .eq("sys_user_id", owenerId)
                .eq("status", 1)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                .isNull("operator_name"));
    }

    private Integer resolveDeviceCount(Operator item) {
        //该运营商直接运营的设备
        return resolveDeviceCount(item.getSysAccountId());
    }

    private Integer resolveDeviceCount(Integer ownerId) {
        //该运营商或代理商直接运营的设备
        return deviceService.selectCount(new EntityWrapper<Device>()
                .eq("owner_id", ownerId)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
    }

    private Integer resolveDeviceLaunchAreaCount(Operator item) {
        //运营商创建的投放点
        return resolveDeviceLaunchAreaCount(item.getSysAccountId());
    }

    private Integer resolveDeviceLaunchAreaCount(Integer owenerId) {
        //运营商或代理商创建的投放点
        return deviceLaunchAreaService.selectCount(new EntityWrapper<DeviceLaunchArea>()
                .eq("sys_user_id", owenerId)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
    }

    private List<SysUser> excludeAccountBinded(List<SysUser> ownAccounts) {
        List<Integer> excludeIds = new ArrayList<>(resolveBindAccount());
        excludeIds.addAll(agentService.resolveBindAccount());
        excludeIds.addAll(manufacturerService.resolveBindAccount());
        return ownAccounts.stream().filter(item -> !excludeIds.contains(item.getId())).collect(Collectors.toList());
    }

    private boolean addOperator(OperatorForAddDto dto, Integer accountId) {
        SysUser current = sysUserService.getCurrentUser();
        checkOperatorNameDup(dto.getName(), current.getId(), null);
        Operator operator = new Operator();
        BeanUtils.copyProperties(dto, operator);
        operator.setSysAccountId(accountId);
        operator.setSysUserId(current.getId());
        operator.setSysUserName(StringUtils.defaultIfEmpty(current.getNickName(), current.getUsername()));
        operator.setCtime(new Date());
        operator.setUtime(operator.getCtime());
        operator.setStatus(StatusType.NEED_TO_ASSIGN.getCode());
        LOGGER.info("添加运营商【{}】", dto.getName());
        if (insert(operator)) {
            if ((dto.getCashPledge() != null && dto.getCashPledge().compareTo(BigDecimal.ZERO) == 1)
                    || !ParamUtil.isNullOrEmptyOrZero(dto.getRechargePromotion())) {
                OperatorExt operatorExt = new OperatorExt();
                operatorExt.setOperatorId(operator.getId());
                operatorExt.setCashPledge(dto.getCashPledge());
                operatorExtService.insert(operatorExt);
            }
            return true;
        }
        return false;
    }

    private void checkOperatorNameDup(String operatorName, Integer creatorId, Integer operatorId) {
        if (Objects.isNull(operatorId)) {//添加
            if (selectCount(new EntityWrapper<Operator>().eq("name", operatorName).eq("sys_user_id", creatorId)) > 0) {
                LeaseException.throwSystemException(LeaseExceEnums.OPERATOR_NAME_DUP);
            }
        } else {//更新
            if (selectCount(new EntityWrapper<Operator>().eq("name", operatorName).ne("id", operatorId).eq("sys_user_id", creatorId)) > 0) {
                LeaseException.throwSystemException(LeaseExceEnums.OPERATOR_NAME_DUP);
            }
        }
    }

    private Integer acquireBindingAccount(OperatorForAddDto dto) {

        Integer bindingAccount;
        //绑定已有系统账号
        if (Objects.equals(ExistEnum.EXIST.getCode(), dto.getBindingExistAccount())) {
            checkIfBinded(dto.getBindingAccountId());
            bindingAccount = dto.getBindingAccountId();
        } else {
            //未绑定账号
            assertNotNull(dto.getUser());
            bindingAccount = sysUserService.add(dto.getUser());
        }

        assertNotNull(bindingAccount);

        return bindingAccount;
    }

    private void checkIfBinded(Integer bindingAccountId) {
        if (selectCount(new EntityWrapper<Operator>().eq("sys_account_id", bindingAccountId)) > 0) {
            LeaseException.throwSystemException(LeaseExceEnums.ALREADY_BINDED_OPERATOR);
        }
    }

    private void assertNotNull(Object obj) {
        if (Objects.isNull(obj)) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_PARAM.getCode(), SysExceptionEnum.ILLEGAL_PARAM.getMessage());
        }
    }

    public Operator getOperatorByAccountId(Integer sysAccountId) {
        if (ParamUtil.isNullOrEmptyOrZero(sysAccountId)) {
            return null;
        }
        EntityWrapper<Operator> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_account_id", sysAccountId);
        return selectOne(entityWrapper);
    }

    @Override

    public List<Integer> resolveBindAccount() {
        SysUser current = sysUserService.getCurrentUser();
        List<Operator> operators = selectList(new EntityWrapper<Operator>().eq("sys_user_id", current.getId()));
        if (CollectionUtils.isNotEmpty(operators)) {
            return operators.stream().map(Operator::getSysAccountId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public String uploadAvatar(MultipartFile file) {
        try {

            if (!file.getOriginalFilename().contains(".")) {
                LeaseException.throwSystemException(LeaseExceEnums.PICTURE_SUFFIX_ERROR);
            }
            if (!file.getContentType().equals("image/jpeg")) {
                LeaseException.throwSystemException(LeaseExceEnums.PICTURE_SUFFIX_ERROR);
            }
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String newFilename = UUID.randomUUID() + suffix;
            file.transferTo(new File(SysConfigUtils.get(CommonSystemConfig.class).getAvatarPath() + newFilename));
            return newFilename;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



    /**
     * 子级运营商的设备统计
     */
    @Override
    public Page<MJSonOperatorDto> getMJOperator(Pageable<Integer> pageable) {
        SysUser user = sysUserService.getCurrentUser();
        Page<Operator> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Wrapper<Operator> wrapper = new EntityWrapper<>();
        //1.获取有权限的用户列表
        wrapper.eq("sys_user_id", user.getId());
        Page<Operator> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), wrapper));
        Page<MJSonOperatorDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
            selectPage.getRecords().forEach(item -> {
                MJSonOperatorDto sonOperatorDto = new MJSonOperatorDto();
                sonOperatorDto.setId(item.getSysAccountId());
                sonOperatorDto.setOperatorName(item.getName());
                sonOperatorDto.setDeviceCount(resolveDeviceCount(item));
                sonOperatorDto.setDevicelaunchAreaCount(resolveDeviceLaunchAreaCount(item));
                result.getRecords().add(sonOperatorDto);
            });
        }
        return result;
    }

    /**
     * 统计子级代理商的设备
     */
    @Override
    public Page<MJSonOperatorDto> getMJAgent(Pageable<Integer> pageable) {
        SysUser user = sysUserService.getCurrentUser();
        Page<Agent> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Wrapper<Agent> wrapper = new EntityWrapper<>();
        //1.获取有权限的用户列表
        wrapper.eq("parent_agent_id", user.getId()).eq("is_deleted", 0);
        Page<Agent> selectPage = agentService.selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), wrapper));
        Page<MJSonOperatorDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
            selectPage.getRecords().forEach(item -> {
                MJSonOperatorDto sonOperatorDto = new MJSonOperatorDto();
                sonOperatorDto.setId(item.getSysAccountId());
                sonOperatorDto.setOperatorName(item.getName());
                sonOperatorDto.setDeviceCount(resolveDeviceCount(item.getSysAccountId()));
                sonOperatorDto.setDevicelaunchAreaCount(resolveDeviceLaunchAreaCount(item.getSysAccountId()));
                result.getRecords().add(sonOperatorDto);
            });
        }
        return result;
    }

    @Override
    public MJSonOperatorDto getOperatorDeviceCount() {
        SysUser sysUser = sysUserService.getCurrentUser();
        Operator operator = selectOne(new EntityWrapper<Operator>().eq("sys_account_id", sysUser.getId()));
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.OPERATOR_DONT_EXISTS);
        }
        MJSonOperatorDto operatorDto = new MJSonOperatorDto();
        operatorDto.setId(operator.getSysAccountId());
        operatorDto.setOperatorName(operator.getName());
        operatorDto.setDeviceCount(deviceCount1(operator.getSysAccountId()));
        operatorDto.setDevicelaunchAreaCount(launchAreaCount(operator.getSysAccountId()));
        return operatorDto;
    }

    @Override
    public MJSonOperatorDto getOperatorUnAllotDeviceCount() {
        SysUser sysUser = sysUserService.getCurrentUser();
        Operator operator = selectOne(new EntityWrapper<Operator>().eq("sys_account_id", sysUser.getId()));
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.OPERATOR_DONT_EXISTS);
        }
        MJSonOperatorDto operatorDto = new MJSonOperatorDto();
        operatorDto.setId(operator.getSysAccountId());
        operatorDto.setOperatorName("未分配");
        operatorDto.setDeviceCount(deviceCount2(operator.getSysAccountId()));
        operatorDto.setDevicelaunchAreaCount(launchAreaCount(operator.getSysAccountId()));
        return operatorDto;
    }

    @Override
    public boolean isAllot() {
        SysUser user = sysUserService.getCurrentUser();
        Operator operator = selectOne(new EntityWrapper<Operator>().eq("sys_account_id", user.getId()));
        if (Objects.isNull(operator)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_IS_NOT_OPERATOR);
        }
        return Objects.equals(operator.getIsAllot(), IsTrueEnum.TRUE.getCode());

    }


    @Override
    public Integer getRole() {
        Integer role = 0;
        SysUser sysUser = sysUserService.getCurrentUser();
        Operator operator = getOperatorByAccountId(sysUser.getId());
        if (Objects.nonNull(operator)) {
            role = 1;
        }
        Agent agent = agentService.getAgentBySysAccountId(sysUser.getId());
        if (Objects.nonNull(agent)) {
            role = 2;
        }
        return role;
    }

    private Map<Integer, AgentServiceImpl.Resolver> statusMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        //待分配 --> 暂停
        statusMap.put(StatusType.NEED_TO_ASSIGN.getCode(), agentAccountId -> StatusType.SUSPENDED.getCode());

        //正常 --> 暂停
        statusMap.put(StatusType.OPERATING.getCode(), agentAccountId -> StatusType.SUSPENDED.getCode());

        //暂停 --> 正常, 待分配
        statusMap.put(StatusType.SUSPENDED.getCode(), agentAccountId -> {
            SysUser agentAccount = sysUserService.selectById(agentAccountId);
            if (Objects.nonNull(agentAccount)) {
                //判断该代理商下是否有设备
                List<Integer> ownerIds = sysUserService.resolveAccessableUserIds(agentAccount, false, true);
                DeviceQueryDto query = new DeviceQueryDto();
                query.setAccessableOwnerIds(ownerIds);
                int count = deviceService.selectCount(QueryResolverUtils.parse(query, new EntityWrapper<>()));
                return count > 0 ? StatusType.OPERATING.getCode() : StatusType.NEED_TO_ASSIGN.getCode();
            }
            return null;
        });
    }

    interface Resolver {
        Integer resolve(Integer agentAccountId);
    }


    @Override
    public String delete(List<Integer> ids) {
        SysUser current = sysUserService.getCurrentUser();
        List<Integer> userIds = sysUserService.resolveOwnerAccessableUserIds(current);
        StringBuilder sb = new StringBuilder();
        List<String> fails = new LinkedList<>();
        List<Operator> operators = selectList(new EntityWrapper<Operator>().eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()).in("id", ids));
        if (!ParamUtil.isNullOrEmptyOrZero(operators)) {
            for (Operator operator : operators) {
                if (userIds.contains(operator.getSysUserId())) {
                    int deviceCount = deviceService.selectCount(new EntityWrapper<Device>().eq("owner_id", operator.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                    int sonOperatorCount = selectCount(new EntityWrapper<Operator>().eq("sys_user_id", operator.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                    if (deviceCount <= 0 && sonOperatorCount <= 0) {
                        operator.setUtime(new Date());
                        operator.setIsDeleted(DeleteStatus.DELETED.getCode());
                        updateById(operator);
                        //删除运营商的系统账号
                       sysUserService.delete(Collections.singletonList(operator.getSysAccountId()));
                        //删除分配给该运营商的分润规则
                        ShareBenefitRule shareBenefitRule = shareBenefitRuleService.selectOne(new EntityWrapper<ShareBenefitRule>().eq("sys_account_id", operator.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                        if (!ParamUtil.isNullOrEmptyOrZero(shareBenefitRule)) {
                            shareBenefitRuleService.deleteShareBenefitRule(Collections.singletonList(shareBenefitRule.getId()));
                        }
                        //删除运营商商的分润规则
                        List<String> list = shareBenefitRuleService.selectList(new EntityWrapper<ShareBenefitRule>().eq("sys_user_id", operator.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()))
                                .stream().map(ShareBenefitRule::getId).collect(Collectors.toList());
                        if (!ParamUtil.isNullOrEmptyOrZero(list)) {
                            shareBenefitRuleService.deleteShareBenefitRule(list);
                        }
                    } else {
                        fails.add(operator.getName());
                    }
                } else {
                    fails.add(operator.getName());
                }
            }
        }
        switch (fails.size()) {
            case 0:
                sb.append("删除成功");
                break;
            case 1:
                sb.append("您欲删除的运营商[" + fails.get(0) + "]已关联设备或下级，请先解绑设备或转移下级。");
                break;
            case 2:
                sb.append("您欲删除的运营商[" + fails.get(0) + "],[" + fails.get(1) + "]已关联设备或下级，请先解绑设备或转移下级。");
                break;
            default:
                sb.append("您欲删除的运营商[" + fails.get(0) + "],[" + fails.get(1) + "]等已关联设备或下级，请先解绑设备或转移下级。");
                break;
        }
        return sb.toString();
    }

    @Override
    public List<OperatorForCascaderDto> getAllParentOperator() {
        List<OperatorForCascaderDto> res = operatorDao.getAllParentOperator();
        res.stream().forEach(item -> item.setLeaf(false));
        return res;
    }

    @Override
    public List<OperatorForCascaderDto> getAllChildOperatorById(Integer sysUserId) {
        List<OperatorForCascaderDto> res = operatorDao.getAllChildOperatorById(sysUserId);
        res.stream().forEach(item -> item.setLeaf(true));
        return res;
    }

}
