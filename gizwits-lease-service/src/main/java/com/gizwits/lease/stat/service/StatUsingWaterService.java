package com.gizwits.lease.stat.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.order.entity.OrderBase;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jin
 * @date 2019/2/25
 */
public interface StatUsingWaterService extends IService<OrderBase> {

    /**
     * 获取用水时段分析
     * @param operator
     * @param launchAreaName
     * @param fromDate
     * @param toDate
     * @return
     */
    List<Map<Integer, Integer>> getHourAnalysis(Date fromDate, Date toDate, String launchAreaName, String operator);

}
