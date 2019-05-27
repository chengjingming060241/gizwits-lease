package com.gizwits.lease.stat.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.order.entity.OrderBase;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jin
 * @date 2019/2/25
 */
public interface StatUsingWaterDao extends BaseMapper<OrderBase> {
    /**
     * 获取用水时段分析
     * @param launchAreaName
     * @param operator
     * @param fromDate
     * @param toDate
     * @return
     */
    List<Map<Integer, Integer>> getHourAnalysis(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("launchAreaName") String launchAreaName, @Param("operator") String operator);

}
