package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.stat.dao.StatUsingWaterDao;
import com.gizwits.lease.stat.service.StatUsingWaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jin
 * @date 2019/2/25
 */
@Service
public class StatUsingWaterServiceImpl extends ServiceImpl<StatUsingWaterDao, OrderBase> implements StatUsingWaterService {

    protected static Logger logger = LoggerFactory.getLogger(StatUsingWaterServiceImpl.class);

    @Autowired
    private StatUsingWaterDao statUsingWaterDao;

    @Override
    public List<Map<Integer, Integer>> getHourAnalysis(Date fromDate, Date toDate, String launchAreaName, String operator) {
        return statUsingWaterDao.getHourAnalysis(fromDate, toDate, launchAreaName, operator);
    }

}
