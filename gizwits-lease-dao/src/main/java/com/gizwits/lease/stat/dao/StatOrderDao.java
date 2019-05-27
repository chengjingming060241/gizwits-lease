package com.gizwits.lease.stat.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.stat.dto.StatOrderAnalysisDto;
import com.gizwits.lease.stat.entity.StatOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单分析统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
public interface StatOrderDao extends BaseMapper<StatOrder> {

    List<StatOrder> getOrderAnalysis(@Param("dto") StatOrderAnalysisDto dto, @Param("sysUserId") Integer sysUserId);

    /**
     * 获取订单金额、数量
     * @param statOrderAnalysisDto
     * @param ids
     * @return
     */
    List<StatOrder> getOrderAnalysisByIds(@Param("dto") StatOrderAnalysisDto statOrderAnalysisDto, @Param("ids") List<Integer> ids);

    /**
     * 获取平均客单价、机均订单数、机均金额
     * @param statOrderAnalysisDto
     * @return
     */
    Map<String, Double> getOrderAvgAnalysis(@Param("dto") StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 统计各时段订单数
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<Integer, Integer>> getOrderCountAnalysis(@Param("dto") StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 统计各时段订单金额
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<Integer, Double>> getOrderMoneyAnalysis(@Param("dto") StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 投放点总金额排序
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<String, Double>> getOrderMoneyAreaAnalysis(@Param("dto") StatOrderAnalysisDto statOrderAnalysisDto);

    /**
     * 机器总金额排序
     * @param statOrderAnalysisDto
     * @return
     */
    List<Map<String, Double>> getOrderMoneyMachineAnalysis(@Param("dto") StatOrderAnalysisDto statOrderAnalysisDto);
}