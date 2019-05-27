package com.gizwits.lease.stat.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.entity.StatDeviceOrderWidget;
import com.gizwits.lease.stat.vo.StatAlarmWidgetVo;
import com.gizwits.lease.stat.vo.StatDeviceWidgetVo;
import com.gizwits.lease.stat.vo.StatOrderWidgetVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备订单看板数据统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
public interface StatDeviceOrderWidgetService extends IService<StatDeviceOrderWidget> {
    /**
     *
     * @param firstDay 本月第一天
     * @param now 当前时间
     * @param yesterday 昨天
     * @param beforeYesterday 前天
     * @param ownerId 设备归属者
     */
    void setDataForWidgetByOwnerId(Date firstDay, Date now, Date yesterday, Date beforeYesterday, Integer ownerId);

    void setDataForWidget();

    StatOrderWidgetVo orderWidget(Integer productId, SysUser currentUser, List<Integer> ids);

    StatDeviceWidgetVo deviceWidget(Integer productId, SysUser currentUser, List<Integer> ids);

    StatAlarmWidgetVo alarmWidget(Integer productId, SysUser currentUser, List<Integer> ids);

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
