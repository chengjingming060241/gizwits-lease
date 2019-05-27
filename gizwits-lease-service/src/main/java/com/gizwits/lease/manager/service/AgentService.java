package com.gizwits.lease.manager.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.dto.SysUserForPullDto;
import com.gizwits.lease.manager.dto.AgentForAddDto;
import com.gizwits.lease.manager.dto.AgentForDetailDto;
import com.gizwits.lease.manager.dto.AgentForListDto;
import com.gizwits.lease.manager.dto.AgentForQueryDto;
import com.gizwits.lease.manager.dto.AgentForUpdateDto;
import com.gizwits.lease.manager.entity.Agent;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 代理商表 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface AgentService extends IService<Agent> {

    Agent getParentAgentBySysAccountId(Integer sysAccountId);

    Agent getAgentBySysAccountId(Integer sysAccountId);
    /**
     * 添加
     */
    boolean add(AgentForAddDto dto);

    List<SysUserForPullDto> availableSysUserList();

    /**
     * 列表
     */
    Page<AgentForListDto> page(Pageable<AgentForQueryDto> pageable);

    /**
     * 详情
     */
    AgentForDetailDto detail(Integer id);

    /**
     * 更新
     */
    AgentForDetailDto update(AgentForUpdateDto dto);

    /**
     * 状态切换
     */
    boolean change(Integer id);

    /**
     * 获取已绑定代理商的系统帐号
     */
    List<Integer> resolveBindAccount();

    /**
     * 获取子级代理商
     */
    List<Agent> getAgentByCreateId(Integer sysUserId);

    String delete(List<Integer> ids);
}
