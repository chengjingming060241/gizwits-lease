package com.gizwits.lease.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.lease.user.dto.UserForQueryDto;
import com.gizwits.lease.user.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 用户表,不要前缀,因为用户模块计划抽象成通用功能 Mapper 接口
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
public interface UserDao extends BaseMapper<User> {

    User findByUsername(String username);

    /**
     * @param openid
     * @return
     */
    User findByOpenid(@Param("openid") String openid);

    List<Integer> getDiffSysUserId();



    List<String> findDiffProvince(@Param("sysUserId") Integer sysUserId);

    List<Map<String, Object>> findProvinceAndCount(@Param("sysUserId")Integer sysUserId);

    Map<String,Number> getTrendDate(@Param("sysUserId") Integer sysUserId,@Param("date") Date date);

    Integer getTotal(@Param("sysUserId") Integer sysUserId);

    Integer getNewByDate(@Param("sysUserId") Integer sysUserId,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);


    Integer getActive(@Param("sysUserId") Integer sysUserId,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);

    Integer getSex(@Param("sysUserId") Integer sysUserId,@Param("gender") Integer gender);

    List<Map<Integer, Number>> getOrderTimes(@Param("sysUserId") Integer sysUserId,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);

    List<User> findByUnionids(@Param("unionids")List<String> unionids, @Param("sysUserIds")List<Integer> sysUserIds);

    List<User> listPage(UserForQueryDto query);

    int findTotalSize(UserForQueryDto query);
}