package com.gizwits.lease.stat.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.stat.dto.StatOrderAnalysisDto;
import com.gizwits.lease.stat.entity.StatOrder;
import com.gizwits.lease.stat.vo.StatOrderAnalysisVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单分析统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
public interface StatOrderService extends IService<StatOrder> {
    /**
     *
     * @param device 解绑的设备
     * @param yesterday 昨天
     * @param beforeYesterday 前天
     * @param status 状态值为2
     */
    void setDataForStatByDevice(Device device, Date yesterday, Date beforeYesterday, List<Integer> status);
    void setDataForStatOrder();
    void setDataForStatOrder(List<Device> deviceList, Date date);

    /**
     * 获取订单金额、数量
     * @param statOrderAnalysisDto
     * @param currentUser
     * @param ids
     * @return
     */
    List<StatOrderAnalysisVo> getOrderAnalysis(StatOrderAnalysisDto statOrderAnalysisDto, SysUser currentUser, List<Integer> ids);

    /**
     * 获取平均客单价、机均订单数、机均金额
     * @param statOrderAnalysisDto
     * @return
     */
    Map<String, Double> getOrderAvgAnalysis(StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 统计各时段的订单数
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<Integer, Integer>> getOrderCountAnalysis(StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 统计各时段订单金额
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<Integer, Double>> getOrderMoneyAnalysis(StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 投放点金额排序
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<String, Double>> getOrderMoneyAreaAnalysis(StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 机器金额排序
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<String, Double>> getOrderMoneyMachineAnalysis(StatOrderAnalysisDto statOrderAnalysisDto);
}
