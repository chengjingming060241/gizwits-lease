package com.gizwits.lease.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.OperatorStatusChangeDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ExistEnum;
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.event.OperatorStatusChangeEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.dao.AgentDao;
import com.gizwits.lease.manager.dto.AgentForAddDto;
import com.gizwits.lease.manager.dto.AgentForDetailDto;
import com.gizwits.lease.manager.dto.AgentForListDto;
import com.gizwits.lease.manager.dto.AgentForQueryDto;
import com.gizwits.lease.manager.dto.AgentForUpdateDto;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.manager.service.OperatorService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 代理商表 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@Service
public class AgentServiceImpl extends ServiceImpl<AgentDao, Agent> implements AgentService, InitializingBean {


    private static final Logger LOGGER = LoggerFactory.getLogger(AgentServiceImpl.class);
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Override
    public boolean add(AgentForAddDto dto) {
        //1.必要时插入系统账号
        Integer accountId = acquireBindingAccount(dto);
        //2.插入运营商
        return addAgent(dto, accountId);
    }

    @Override
    public Agent getParentAgentBySysAccountId(Integer sysAccountId) {
        if (ParamUtil.isNullOrEmptyOrZero(sysAccountId)) {
            return null;
        }
        SysUser relAccount = sysUserService.selectById(sysAccountId);
        return resolveParentAgent(relAccount);
    }

    private Agent resolveParentAgent(SysUser child) {
        //查到管理员时，直接返回null
        if (Objects.isNull(child) || Objects.equals(child.getId(), child.getSysUserId())) {
            return null;
        } else {
            SysUser parent = sysUserService.selectById(child.getSysUserId());
            Agent agent = selectOne(new EntityWrapper<Agent>().eq("sys_account_id", parent.getId()));
            if (Objects.nonNull(agent)) {
                return agent;
            } else {
                return resolveParentAgent(parent);
            }
        }
    }

    @Override
    public List<SysUserForPullDto> availableSysUserList() {
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
    public Page<AgentForListDto> page(Pageable<AgentForQueryDto> pageable) {
        //1.获取有权限的用户列表
        Page<Agent> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<Agent> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<AgentForListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
            selectPage.getRecords().forEach(item -> {
                AgentForListDto dto = new AgentForListDto(item);
                dto.setLaunchAreaCount(resolveOperatorCount(item));
                dto.setDeviceCount(resolveDeviceCount(item));
                result.getRecords().add(dto);
            });
        }
        return result;
    }

    @Override
    public AgentForDetailDto detail(Integer id) {
        Agent agent = selectById(id);
        if (Objects.isNull(agent)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        AgentForDetailDto result = new AgentForDetailDto(agent);
        result.setOperatorCount(resolveOperatorCount(agent));
        result.setDeviceCount(resolveDeviceCount(agent));
        result.setAccount(sysUserService.basic(agent.getSysAccountId()));
        return result;
    }

    @Override
    public AgentForDetailDto update(AgentForUpdateDto dto) {
        Agent agent = selectById(dto.getId());
        if (Objects.isNull(agent)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        String oldName = agent.getName();
        checkAgentNameDup(dto.getName(), sysUserService.getCurrentUser().getId(), agent.getId());
        BeanUtils.copyProperties(dto, agent);
        agent.setUtime(new Date());
        updateById(agent);
        if (!ParamUtil.isNullOrEmptyOrZero(dto.getName()) && !oldName.equals(dto.getName())) {
            NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(agent, agent.getSysAccountId(), oldName, dto.getName());
            CommonEventPublisherUtils.publishEvent(nameModifyEvent);
        }
        SysUserExt ext = sysUserExtService.selectById(agent.getSysAccountId());
        if (Objects.isNull(ext)) {
            ext = new SysUserExt();
            ext.setSysUserId(agent.getSysAccountId());
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
        Agent agent = selectById(id);
        if (Objects.isNull(agent)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        OperatorStatusChangeDto changeDto = new OperatorStatusChangeDto(agent);
        agent.setStatus(statusMap.get(agent.getStatus()).resolve(agent.getSysAccountId()));
        agent.setUtime(new Date());
        boolean result = updateById(agent);
        changeDto.setTo(agent.getStatus());
        changeDto.setCurrent(sysUserService.getCurrentUser());
        if (result) {
            CommonEventPublisherUtils.publishEvent(new OperatorStatusChangeEvent(changeDto));
        }
        return result;
    }

    private Integer resolveDeviceCount(Agent item) {
        //直接分配给代理商的设备数量
        return deviceService.selectCount(new EntityWrapper<Device>()
                .eq("owner_id", item.getSysAccountId())
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
    }

    private Integer resolveOperatorCount(Agent item) {
        //代理商创建的运营商
        return operatorService.selectCount(new EntityWrapper<Operator>()
                .eq("sys_user_id", item.getSysAccountId()));
    }

    private List<SysUser> excludeAccountBinded(List<SysUser> ownAccounts) {
        List<Integer> excludeIds = new ArrayList<>(resolveBindAccount());
        excludeIds.addAll(operatorService.resolveBindAccount());
        excludeIds.addAll(manufacturerService.resolveBindAccount());
        return ownAccounts.stream().filter(item -> !excludeIds.contains(item.getId())).collect(Collectors.toList());
    }

    private boolean addAgent(AgentForAddDto dto, Integer accountId) {
        SysUser current = sysUserService.getCurrentUser();
        checkAgentNameDup(dto.getName(), current.getId(), null);
        Agent agent = new Agent();
        BeanUtils.copyProperties(dto, agent);
        agent.setSysAccountId(accountId);
        agent.setSysUserId(current.getId());
        agent.setSysUserName(StringUtils.defaultIfEmpty(current.getNickName(), current.getUsername()));
        agent.setCtime(new Date());
        agent.setUtime(agent.getCtime());
        agent.setStatus(StatusType.NEED_TO_ASSIGN.getCode());
        LOGGER.info("添加代理商【{}】", dto.getName());
        return insert(agent);
    }

    private void checkAgentNameDup(String operatorName, Integer creatorId, Integer operatorId) {
        if (Objects.isNull(operatorId)) {//添加
            if (selectCount(new EntityWrapper<Agent>().eq("name", operatorName).eq("sys_user_id", creatorId)) > 0) {
                LeaseException.throwSystemException(LeaseExceEnums.AGENT_NAME_DUP);
            }
        } else {//更新
            if (selectCount(new EntityWrapper<Agent>().eq("name", operatorName).ne("id", operatorId).eq("sys_user_id", creatorId)) > 0) {
                LeaseException.throwSystemException(LeaseExceEnums.AGENT_NAME_DUP);
            }
        }
    }

    private Integer acquireBindingAccount(AgentForAddDto dto) {

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
        if (selectCount(new EntityWrapper<Agent>().eq("sys_account_id", bindingAccountId)) > 0) {
            LeaseException.throwSystemException(LeaseExceEnums.ALREADY_BINDED_OPERATOR);
        }
    }

    @Override
    public List<Integer> resolveBindAccount() {
        SysUser current = sysUserService.getCurrentUser();
        List<Agent> agents = selectList(new EntityWrapper<Agent>().eq("sys_user_id", current.getId()));
        if (CollectionUtils.isNotEmpty(agents)) {
            return agents.stream().map(Agent::getSysAccountId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void assertNotNull(Object obj) {
        if (Objects.isNull(obj)) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_PARAM.getCode(), SysExceptionEnum.ILLEGAL_PARAM.getMessage());
        }
    }

    private Map<Integer, Resolver> statusMap = new HashMap<>();

    interface Resolver {
        Integer resolve(Integer agentAccountId);
    }

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

    @Override
    public Agent getAgentBySysAccountId(Integer sysAccountId){
        EntityWrapper<Agent> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_account_id",sysAccountId).eq("is_deleted",DeleteStatus.NOT_DELETED.getCode());
        return selectOne(entityWrapper);
    }

    @Override
    public List<Agent> getAgentByCreateId(Integer sysUserId) {
        if (ParamUtil.isNullOrEmptyOrZero(sysUserId)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        EntityWrapper<Agent> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_user_id", sysUserId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectList(entityWrapper);
    }

    @Override
    public String delete(List<Integer> ids) {
        SysUser current = sysUserService.getCurrentUser();
        List<Integer> userIds = sysUserService.resolveOwnerAccessableUserIds(current);
        List<String> fails = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        List<Agent> agents = selectList(new EntityWrapper<Agent>().eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()).in("id", ids));
        if (!ParamUtil.isNullOrEmptyOrZero(agents)) {
            for (Agent agent : agents) {
                if (userIds.contains(agent.getSysUserId())) {
                    int num = deviceService.selectCount(new EntityWrapper<Device>().eq("owner_id", agent.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                    int sonAgentNum = selectCount(new EntityWrapper<Agent>().eq("sys_user_id", agent.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                    int sonOperatorCount = operatorService.selectCount(new EntityWrapper<Operator>().eq("sys_user_id", agent.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                    if (num <= 0 && sonAgentNum <= 0 && sonOperatorCount <= 0) {
                        agent.setUtime(new Date());
                        agent.setIsDeleted(DeleteStatus.DELETED.getCode());
                        updateById(agent);
                        //删除代理商的系统账号
                        sysUserService.delete(Collections.singletonList(agent.getSysAccountId()));
                        //删除分配给该代理商的分润规则
                        ShareBenefitRule shareBenefitRule = shareBenefitRuleService.selectOne(new EntityWrapper<ShareBenefitRule>().eq("sys_account_id", agent.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
                        if (!ParamUtil.isNullOrEmptyOrZero(shareBenefitRule)) {
                            shareBenefitRuleService.deleteShareBenefitRule(Collections.singletonList(shareBenefitRule.getId()));
                        }
                        //删除代理商的分润规则
                        List<String> list = shareBenefitRuleService.selectList(new EntityWrapper<ShareBenefitRule>().eq("sys_user_id", agent.getSysAccountId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()))
                                .stream().map(ShareBenefitRule::getId).collect(Collectors.toList());
                        if (!ParamUtil.isNullOrEmptyOrZero(list)) {
                            shareBenefitRuleService.deleteShareBenefitRule(list);
                        }
                    } else {
                        fails.add(agent.getName());
                    }
                } else {
                    fails.add(agent.getName());
                }
            }
        }
        switch (fails.size()) {
            case 0:
                sb.append("删除成功");
                break;
            case 1:
                sb.append("您欲删除的代理商[" + fails.get(0) + "]已关联设备或下级，请先解绑设备或转移下级。");
                break;
            case 2:
                sb.append("您欲删除的代理商[" + fails.get(0) + "],[" + fails.get(1) + "]已关联设备或下级，请先解绑设备或转移下级。");
                break;
            default:
                sb.append("您欲删除的代理商[" + fails.get(0) + "],[" + fails.get(1) + "]等已关联设备或下级，请先解绑设备或转移下级。");
                break;
        }
        return sb.toString();
    }

}
