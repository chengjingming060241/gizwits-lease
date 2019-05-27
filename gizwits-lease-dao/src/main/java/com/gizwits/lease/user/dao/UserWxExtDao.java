package com.gizwits.lease.user.dao;

import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserWxExt;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 微信用户信息 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-08-10
 */
public interface UserWxExtDao extends BaseMapper<UserWxExt> {

    List<User> findUserByOpenids(@Param("openids")List<String> openids, @Param("sysUserIds")List<Integer> sysUserIds);
}
