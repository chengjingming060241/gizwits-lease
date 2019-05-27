package com.gizwits.lease.message.service;

import com.gizwits.lease.message.entity.SysMessageToUser;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 系统消息用户表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-08
 */
public interface SysMessageToUserService extends IService<SysMessageToUser> {

    /**
     * 拼接角色名
     * @param sysMessageId
     * @return
     */
    String concatRoleName(Integer sysMessageId);

    /**
     * 查询用户的消息ID
     * @param userId
     * @return
     */
    List<Integer> listSysMessageId(Integer userId);

}
