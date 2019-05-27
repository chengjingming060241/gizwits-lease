package com.gizwits.lease.common.perm;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserToRole;
import com.gizwits.boot.sys.service.SysRoleService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.sys.service.SysUserToRoleService;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.manager.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lilh
 * @date 2017/7/8 10:00
 */
@Component
public class SysUserRoleTypeHelperResolver {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserToRoleService sysUserToRoleService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private AgentService agentService;

    public SysUserRoleTypeHelper resolve(SysUser current) {
        SysUserRoleTypeHelper helper = new SysUserRoleTypeHelper();
        resolveOperator(helper, current);

        if (!helper.isResolved()) {
            resolveAgent(helper, current);
        }

        if (!helper.isResolved()) {
            resolveManufacturer(helper, current);
        }
        if (!helper.isResolved()) {
            resolveAdmin(helper, current);
        }
        return helper;
    }

    public SysUserRoleTypeHelper resolveManufacturer(SysUser current) {
        SysUserRoleTypeHelper helper = new SysUserRoleTypeHelper();
        resolveManufacturer(helper, current);
        return helper;
    }

    private void resolveAdmin(SysUserRoleTypeHelper helper, SysUser current) {
        if (isAdmin(current)) {
            helper.setAdmin(true);
            helper.setResolved(true);
            helper.setSysAccountId(current.getId());
        }
    }

    private boolean isAdmin(SysUser current) {
        List<Integer> roleIds = sysUserToRoleService.getSysUserToRoleListByUserId(current.getId()).stream().map(SysUserToRole::getRoleId).collect(Collectors.toList());
        List<Integer> whiteRoleIds = getWhiteRoleIds();
        for (Integer roleId : roleIds) {
            if (whiteRoleIds.contains(roleId)) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> getWhiteRoleIds() {
        return Collections.singletonList(sysRoleService.getSuperManagerRoleId());
    }

    private void resolveManufacturer(SysUserRoleTypeHelper helper, SysUser current) {

        Manufacturer manufacturer = resolveManufacturerByAccountId(current.getId());
        SysUser child = current;
        while (Objects.isNull(manufacturer)) {
            SysUser parent = sysUserService.selectById(child.getSysUserId());
            if (Objects.isNull(parent) || Objects.equals(parent.getId(), parent.getSysUserId())) {
                break;
            }
            manufacturer = resolveManufacturerByAccountId(parent.getId());
            child = parent;
        }

        if (Objects.nonNull(manufacturer)) {
            helper.setManufacturer(true);
            helper.setResolved(true);
            helper.setSysAccountId(manufacturer.getSysAccountId());
        }
    }

    private void resolveAgent(SysUserRoleTypeHelper helper, SysUser current) {
        Agent agent = resolveAgentByAccountId(current.getId());
        SysUser child = current;
        while (Objects.isNull(agent)) {
            SysUser parent = sysUserService.selectById(child.getSysUserId());
            if (Objects.isNull(parent) || Objects.equals(parent.getId(), parent.getSysUserId())) {
                break;
            }
            agent = resolveAgentByAccountId(parent.getId());
            child = parent;
        }

        if (Objects.nonNull(agent)) {
            helper.setAgent(true);
            helper.setResolved(true);
            helper.setSysAccountId(agent.getSysAccountId());
        }

    }

    private void resolveOperator(SysUserRoleTypeHelper helper, SysUser current) {

        Operator operator = resolveOperatorByAccountId(current.getId());
        SysUser child = current;
        while (Objects.isNull(operator)) {
            SysUser parent = sysUserService.selectById(child.getSysUserId());
            if (Objects.isNull(parent) || Objects.equals(parent.getId(), parent.getSysUserId())) {
                break;
            }
            operator = resolveOperatorByAccountId(parent.getId());
            child = parent;
        }

        if (Objects.nonNull(operator)) {
            helper.setOperator(true);
            helper.setResolved(true);
            helper.setSysAccountId(operator.getSysAccountId());
        }
    }

    private Agent resolveAgentByAccountId(Integer sysAccountId) {
        return agentService.selectOne(new EntityWrapper<Agent>().eq("sys_account_id", sysAccountId));
    }

    private Manufacturer resolveManufacturerByAccountId(Integer sysUserId) {
        return manufacturerService.selectOne(new EntityWrapper<Manufacturer>().eq("sys_account_id", sysUserId));
    }

    private Operator resolveOperatorByAccountId(Integer sysAccountId) {
        return operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", sysAccountId));
    }
}
