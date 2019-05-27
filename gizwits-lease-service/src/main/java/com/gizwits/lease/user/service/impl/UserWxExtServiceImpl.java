package com.gizwits.lease.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserWxExt;
import com.gizwits.lease.user.dao.UserWxExtDao;
import com.gizwits.lease.user.service.UserWxExtService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 微信用户信息 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-08-10
 */
@Service
public class UserWxExtServiceImpl extends ServiceImpl<UserWxExtDao, UserWxExt> implements UserWxExtService {

    @Autowired
    private UserWxExtDao userWxExtDao;

    @Autowired
    private SysUserService sysUserService;

    public List<User> getUserByOpenid(List<String> openids){

        return userWxExtDao.findUserByOpenids(openids,sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
    }

    @Override
    public UserWxExt getByOpenid(String openid) {
        UserWxExt userWxExt = selectOne(new EntityWrapper<UserWxExt>().eq("user_openid", openid));
        if (!Objects.isNull(userWxExt)){
            return userWxExt;
        }
        userWxExt = selectOne(new EntityWrapper<UserWxExt>().eq("openid", openid));
        return userWxExt;
    }
}
