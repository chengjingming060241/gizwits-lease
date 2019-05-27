package com.gizwits.lease.user.service;

import com.gizwits.lease.user.entity.UserWxExt;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 微信用户信息 服务类
 * </p>
 *
 * @author zhl
 * @since 2017-08-10
 */
public interface UserWxExtService extends IService<UserWxExt> {

    List<com.gizwits.lease.user.entity.User> getUserByOpenid(List<String> openids);

    UserWxExt getByOpenid(String openid);

}
