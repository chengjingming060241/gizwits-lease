package com.gizwits.lease.message.service.impl;

import com.gizwits.lease.message.entity.SysMessageToUser;
import com.gizwits.lease.message.dao.SysMessageToUserDao;
import com.gizwits.lease.message.service.SysMessageToUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统消息用户表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-08
 */
@Service
public class SysMessageToUserServiceImpl extends ServiceImpl<SysMessageToUserDao, SysMessageToUser> implements SysMessageToUserService {

    @Autowired
    private SysMessageToUserDao sysMessageToUserDao;

    @Override
    public String concatRoleName(Integer sysMessageId) {
        return sysMessageToUserDao.concatRoleName(sysMessageId);
    }

    @Override
    public List<Integer> listSysMessageId(Integer userId) {
        return sysMessageToUserDao.listSysMessageId(userId);
    }
}
