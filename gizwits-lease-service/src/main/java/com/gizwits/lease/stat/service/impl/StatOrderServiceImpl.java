package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.device.dao.DeviceDao;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.stat.dao.StatDeviceOrderWidgetDao;
import com.gizwits.lease.stat.dao.StatOrderDao;
import com.gizwits.lease.stat.dto.StatOrderAnalysisDto;
import com.gizwits.lease.stat.entity.StatOrder;
import com.gizwits.lease.stat.service.StatOrderService;
import com.gizwits.lease.stat.vo.StatOrderAnalysisVo;
import com.gizwits.lease.util.StatisticsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单分析统计表 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@Service
public class StatOrderServiceImpl extends ServiceImpl<StatOrderDao, StatOrder> implements StatOrderService {

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private OrderBaseDao orderBaseDao;
    @Autowired
    private StatOrderDao statOrderDao;
    @Autowired
    private StatDeviceOrderWidgetDao statDeviceOrderWidgetDao;

    protected static Logger logger = LoggerFactory.getLogger("ORDER_LOGGER");

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void setDataForStatOrder() {

        //1.查询所有没有删除的设备
        List<Device> deviceList = deviceDao.selectList(new EntityWrapper<Device>().eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));

        Date earliestOrderTime = orderBaseDao.earliestOrderTime();
        if (earliestOrderTime == null) {
            logger.info("===============没有任何订单需要统计");
            return;
        }
        StatOrderAnalysisDto statOrderAnalysisDto = new StatOrderAnalysisDto();
        statOrderAnalysisDto.setDateFormat("%Y-%m-%d");
        statOrderAnalysisDto.setFromDate(earliestOrderTime);
        statOrderAnalysisDto.setToDate(earliestOrderTime);
        List<StatOrder> statOrderList = statOrderDao.getOrderAnalysisByIds(statOrderAnalysisDto, null);
        // 如果最早的订单没有对应的分析结果，则认为分析结果需要全部初始化
        if (statOrderList.isEmpty()) {
            // 2017年前这个系统还不存在，如果有这样的订单，那就是脏数据
            if (earliestOrderTime.getYear() < 117) {
                logger.info("===============存在日期早于2017年的订单，为了避免统计表爆炸，不统计之前的日期，请尽快查明原因");
                // 只计算昨天的订单分析
                // 因为是每天晚上12点定时任务,所以获取昨天的时间
                Date yesterday = DateKit.addDate(new Date(), -1);
                setDataForStatOrder(deviceList, yesterday);
                return;
            }
            // 获取当前日期
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            // 最早一天
            Calendar earliestDay = Calendar.getInstance();
            earliestDay.setTime(earliestOrderTime);
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            // 逐天统计
            for (Calendar date = earliestDay; date.before(today); date.add(Calendar.DATE, 1)) {
                setDataForStatOrder(deviceList, date.getTime());
                try {
                    // 所有日子一起计算会爆炸，所以等个5秒，计算一年的量也就半小时（笑）
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 只计算昨天的订单分析
            // 因为是每天晚上12点定时任务,所以获取昨天的时间
            Date yesterday = DateKit.addDate(new Date(), -1);
            setDataForStatOrder(deviceList, yesterday);
        }
    }

    @Override
    public void setDataForStatOrder(List<Device> deviceList, Date date) {
        logger.info("===============订单统计开始" + DateKit.getTimestampString(date));

        StatOrderAnalysisDto statOrderAnalysisDto = new StatOrderAnalysisDto();
        statOrderAnalysisDto.setDateFormat("%Y-%m-%d");
        statOrderAnalysisDto.setFromDate(date);
        statOrderAnalysisDto.setToDate(date);
        List<StatOrder> statOrderList = statOrderDao.getOrderAnalysisByIds(statOrderAnalysisDto, null);
        if (!statOrderList.isEmpty()) {
            logger.info("==================={}已统计过，不需要再次统计", DateKit.getTimestampString(date));
        }

        Date beforeDate = DateKit.addDate(date, -1);
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        /** 需求是统计所有订单
         //定义订单的状态为已使用
         List<Integer> status = new ArrayList<>();
         status.add(5);
         */
        //2.根据每个设备的sno查询订单的总额和订单的数量
        for (int i = 0; i < deviceList.size(); ++i) {
            final int index = i;
            executorService.execute(() -> {
                Device deviceDB = deviceList.get(index);
                List<Integer> status = new ArrayList<>(1);
                status.add(OrderStatus.FINISH.getCode());
                try {
                    //添加到数据库
                    setDataForStatByDevice(deviceDB, date, beforeDate, status);
                } catch (Exception e) {
                    logger.warn("===================设备：" + deviceList.get(index).getSno() + "导入到订单分析表失败,原因如下：" + e.getMessage());
                    StatOrder statOrder = new StatOrder();
                    //根据设备查询设备昨天的总金额与总数量
                    Map<String, Number> map = orderBaseDao.findForStatOrder(deviceDB.getSno(), date, status);

                    // TODO 查询微信和余额分别对应的数据源进行插入
                    Map<String, Number> mapWx = orderBaseDao.findForStatOrderWx(deviceDB.getSno(), date, status);

                    Map<String, Number> mapWallet = orderBaseDao.findForStatOrderWallet(deviceDB.getSno(), date, status);

                    Integer orderCountYesterday = map.get("oCount").intValue();
//                    //根据设备的ownerId查询订单数
//                    Integer orderCountBeforeYesterday = orderBaseDao.findForStatOrder(deviceDB.getSno(), beforeDate, status)
//                            .get("oCount").intValue();
                    //将设备的device的ownerId设为统计表中的sysUserId
                    statOrder.setSysUserId(deviceDB.getOwnerId());
                    statOrder.setSno(deviceDB.getSno());
                    statOrder.setCtime(date);
                    //将统计表中的运营商id设为ownerId
                    statOrder.setOperatorId(deviceDB.getOwnerId());
                    statOrder.setOrderAmount(map.get("oAmount").doubleValue());
                    statOrder.setOrderCount(orderCountYesterday);

                    statOrder.setOrderAmountWx(mapWx.get("oAmountWx").doubleValue());
                    statOrder.setOrderCountWx(mapWx.get("oCountWx").intValue());

                    statOrder.setOrderAmountWallet(mapWallet.get("oAmountWallet").doubleValue());
                    statOrder.setOrderCountWallet(mapWallet.get("oCountWallet").intValue());

//                    Double orderNewPercent = ParamUtil.isNullOrZero(orderCountBeforeYesterday) ? 0.0 : (double) (orderCountYesterday - orderCountBeforeYesterday) / orderCountBeforeYesterday;
                    statOrder.setOrderedPercent(0.0);
                    insert(statOrder);
                }
            });
        }
    }

    @Override
    public void setDataForStatByDevice(Device device, Date yesterday, Date beforeYesterday, List<Integer> status) {
        StatOrder statOrder = new StatOrder();
        //根据设备查询设备昨天的总金额与总数量
        Map<String, Number> map = orderBaseDao.findForStatOrder(device.getSno(), yesterday, status);

        // 微信及钱包
        Map<String, Number> mapWx = orderBaseDao.findForStatOrderWx(device.getSno(), yesterday, status);
        Map<String, Number> mapWallet = orderBaseDao.findForStatOrderWallet(device.getSno(), yesterday, status);

        Integer orderCountYesterday = map.get("oCount").intValue();
        //根据设备的ownerId查询订单数
        Integer orderCountBeforeYesterday = orderBaseDao.findForStatOrder(device.getSno(), beforeYesterday, status)
                .get("oCount").intValue();
        //将设备的device的ownerId设为统计表中的sysUserId
        statOrder.setSysUserId(device.getOwnerId());
        statOrder.setSno(device.getSno());
        statOrder.setCtime(yesterday);
        //将统计表中的运营商id设为ownerId
        statOrder.setOperatorId(device.getOwnerId());
        statOrder.setOrderAmount(map.get("oAmount").doubleValue());
        statOrder.setOrderCount(orderCountYesterday);

        statOrder.setOrderAmountWx(mapWx.get("oAmountWx").doubleValue());
        statOrder.setOrderCountWx(mapWx.get("oCountWx").intValue());

        statOrder.setOrderAmountWallet(mapWallet.get("oAmountWallet").doubleValue());
        statOrder.setOrderCountWallet(mapWallet.get("oCountWallet").intValue());

        Double orderNewPercent = ParamUtil.isNullOrZero(orderCountBeforeYesterday) ? 0.0 : (double) (orderCountYesterday - orderCountBeforeYesterday) / orderCountBeforeYesterday;
        statOrder.setOrderedPercent(orderNewPercent * 100);
        insert(statOrder);
    }

    @Override
    public List<StatOrderAnalysisVo> getOrderAnalysis(StatOrderAnalysisDto statOrderAnalysisDto, SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }

        Date fromDate = statOrderAnalysisDto.getFromDate();
        Date toDate = statOrderAnalysisDto.getToDate();
        SimpleDateFormat sdf = null;
        if (fromDate != null && toDate != null) {
            // 超过30天按月显示
            if (toDate.getTime() - fromDate.getTime() > TimeUnit.DAYS.toMillis(30)) {
                statOrderAnalysisDto.setDateFormat("%Y-%m");
                sdf = new SimpleDateFormat("yyyy-MM");
            } else {
                // 30天以内，按日显示
                statOrderAnalysisDto.setDateFormat("%Y-%m-%d");
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
        } else {
            // 全部时段，按月显示
            statOrderAnalysisDto.setDateFormat("%Y-%m");
            sdf = new SimpleDateFormat("yyyy-MM");
        }

        //默认情况下是通过sysUserId和productId来查询
        //如果dto里面的成员变量有值，则使用mybatis里面的语法来查询
        List<StatOrder> statOrderList = statOrderDao.getOrderAnalysisByIds(statOrderAnalysisDto, ids);
        if (statOrderList.size() == 0) {
            logger.warn("请查看stat_order表中是否含有当前用户：" + currentUser.getId()
                    + "时间为:" + DateKit.getTimestampString(fromDate)
                    + "~" + DateKit.getTimestampString(toDate) + "的记录");
        }
        //将list里面的值赋值到vo中
        List<StatOrderAnalysisVo> statOrderVoList = new ArrayList<>();
        StatOrder preStatOrder = null;
        StatOrder statOrder;
        StatOrderAnalysisVo statOrderVo;
        for (int i = 0; i < statOrderList.size(); ++i) {
            statOrderVo = new StatOrderAnalysisVo();
            statOrder = statOrderList.get(i);
            BeanUtils.copyProperties(statOrder, statOrderVo);
            if (preStatOrder == null) {
                statOrderVo.setCountPercent(0.0);
                statOrderVo.setAmountPercent(0.0);
            } else {
                statOrderVo.setCountPercent(
                        StatisticsUtil.calcIncreasePercent(preStatOrder.getOrderCount(), statOrder.getOrderCount()));
                statOrderVo.setAmountPercent(
                        StatisticsUtil.calcIncreasePercent(preStatOrder.getOrderAmount(), statOrder.getOrderAmount()));
            }
            statOrderVo.setCtime(sdf.format(statOrder.getCtime()));
            statOrderVoList.add(statOrderVo);
            preStatOrder = statOrder;
        }
        return statOrderVoList;
    }

    @Override
    public Map<String, Double> getOrderAvgAnalysis(StatOrderAnalysisDto statOrderAnalysisDto) {
        return statOrderDao.getOrderAvgAnalysis(statOrderAnalysisDto);
    }

    @Override
    public List<Map<Integer, Integer>> getOrderCountAnalysis(StatOrderAnalysisDto statOrderAnalysisDto) {
        return statOrderDao.getOrderCountAnalysis(statOrderAnalysisDto);
    }

    @Override
    public List<Map<Integer, Double>> getOrderMoneyAnalysis(StatOrderAnalysisDto statOrderAnalysisDto) {
        return statOrderDao.getOrderMoneyAnalysis(statOrderAnalysisDto);
    }

    @Override
    public List<Map<String, Double>> getOrderMoneyAreaAnalysis(StatOrderAnalysisDto statOrderAnalysisDto) {
        return statOrderDao.getOrderMoneyAreaAnalysis(statOrderAnalysisDto);
    }

    @Override
    public List<Map<String, Double>> getOrderMoneyMachineAnalysis(StatOrderAnalysisDto statOrderAnalysisDto) {
        return statOrderDao.getOrderMoneyMachineAnalysis(statOrderAnalysisDto);
    }
}
