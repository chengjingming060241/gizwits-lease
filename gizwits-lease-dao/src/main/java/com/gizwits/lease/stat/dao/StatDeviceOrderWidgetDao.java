package com.gizwits.lease.stat.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.stat.entity.StatDeviceOrderWidget;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 设备订单看板数据统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
public interface StatDeviceOrderWidgetDao extends BaseMapper<StatDeviceOrderWidget> {

    StatDeviceOrderWidget orderWidget(@Param("sysUserId") Integer id, @Param("productId") Integer productId, @Param("date") Date now);

    StatDeviceOrderWidget deviceWidget(@Param("sysUserId") Integer id, @Param("productId") Integer productId, @Param("date") Date now);

    /**
     * @param id
     * @param productId
     * @param now
     * @return
     */
    StatDeviceOrderWidget alarmWidget(@Param("sysUserId") Integer id, @Param("productId") Integer productId, @Param("date") Date now);

    Integer updateByUtimeAndSysUserIdAndProductId(@Param("date") Date now, @Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId,@Param("widget") StatDeviceOrderWidget statDeviceOrderWidget);

    Double getOrderNewPercent(@Param("sysUserId") Integer sysUserId,@Param("productId") Integer productId,@Param("date") Date yesterday);

    StatDeviceOrderWidget deviceWidgetByIds(@Param("ids")List<Integer> ids,@Param("productId") Integer productId,@Param("date") Date now);

    StatDeviceOrderWidget alarmWidgetByIds(@Param("ids")List<Integer> ids,@Param("productId") Integer productId,@Param("date") Date now);

    StatDeviceOrderWidget orderWidgetByIds(@Param("ids")List<Integer> ids,@Param("productId") Integer productId,@Param("date") Date now);

    void deleteAlreadyEsixtData(@Param("sysUserId") Integer sysUserId,@Param("productList") List<Integer> productList,@Param("date") Date date);

    StatDeviceOrderWidget selectByUtimeAndSysUserIdAndProductId(@Param("now") Date now, @Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId);

    /**
     * 15天内到期设备数
     * @return
     */
    Integer count15DaysDevices();

    /**
     * 已上线离线设备数
     * @return
     */
    Integer countOffDevices();

    /**
     * 水量剩余10%设备数
     * @return
     */
    Integer countRemain10Devices();

    /**
     * 今日订单总额
     * @return
     */
    Double sumTotalAmountToday();

    /**
     * 运营商总金额排序
     * @return
     */
    List<Map<String, Double>> sumOperatorAndSort();
}