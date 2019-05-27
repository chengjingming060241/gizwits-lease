package com.gizwits.lease.message.dao;

import com.gizwits.lease.message.entity.SysMessageToUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统消息用户表 Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-08-08
 */
public interface SysMessageToUserDao extends BaseMapper<SysMessageToUser> {

    String concatRoleName(@Param("sysMessageId") Integer sysMessageId);

    List<Integer> listSysMessageId(@Param("userId") Integer userId);
}