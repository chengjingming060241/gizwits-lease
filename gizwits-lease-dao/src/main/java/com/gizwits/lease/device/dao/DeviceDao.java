package com.gizwits.lease.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.device.entity.Device;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 设备表 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-07-11
 */
public interface DeviceDao extends BaseMapper<Device> {
    /**
     * 根据sysUserId获取设备的总数量
     *
     * @return int
     */
    Integer getTotalBySysUserIdAndIsNotDeleted(@Param("sysUserId") int sysUserId, @Param("productId") Integer productId, @Param("endTime") Date endTime);

    /**
     * 获取设备中存在的所有的sysUserId
     */
    List<Integer> getDiffSysUserId();

    /**
     * 获取device表中不重复的ownerId
     */
    List<Integer> getDiffOwnerId();

    /**
     * 根据sysUserId获取productIdList
     */
    List<Integer> getProductId(@Param("sysUserId") Integer sysUserId);

    /**
     * 获取device中的productId和productKey
     */
    List<Map<String, Object>> getProductIdAndKey();

    Device findByMacAndProductKey(@Param("mac") String mac, @Param("productKey") String productKey);

    /**
     * 根据参数查询date对应的设备新增数量
     */
    Integer getNewCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId, @Param("start") Date start, @Param("end") Date end);

    /**
     * 根据参数查询date对应的设备订单数量
     */
    Integer getOrderedCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId, @Param("start") Date start, @Param("end") Date end);

    Integer getAlarmCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId);

    Integer getWarnCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId);

    String getSnoByOpenid(@Param("openid") String openid, @Param("status") Integer status);

    /**
     * 未分配投放点的设备
     */
    Integer getDeviceWithoutArea(@Param("ownerId") Integer ownerId, @Param("productId") Integer productId);

    /**
     * 从设备上报记录表中查询设备活跃数
     *
     * @param sysUserId
     * @param productId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getActiveCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}