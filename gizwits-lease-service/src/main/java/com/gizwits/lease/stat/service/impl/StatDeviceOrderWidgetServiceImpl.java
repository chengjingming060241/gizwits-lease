package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.benefit.dao.ShareBenefitSheetOrderDao;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.device.dao.DeviceAlarmDao;
import com.gizwits.lease.device.dao.DeviceDao;
import com.gizwits.lease.enums.PanelModuleItemType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.panel.service.PersonalPanelService;
import com.gizwits.lease.stat.dao.StatDeviceOrderWidgetDao;
import com.gizwits.lease.stat.entity.StatDeviceOrderWidget;
import com.gizwits.lease.stat.service.StatDeviceOrderWidgetService;
import com.gizwits.lease.stat.vo.StatAlarmWidgetVo;
import com.gizwits.lease.stat.vo.StatDeviceWidgetVo;
import com.gizwits.lease.stat.vo.StatOrderWidgetVo;
import com.gizwits.lease.stat.vo.StatWidgetDataVo;
import com.gizwits.lease.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 设备订单看板数据统计表 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
@Service
public class StatDeviceOrderWidgetServiceImpl extends ServiceImpl<StatDeviceOrderWidgetDao, StatDeviceOrderWidget> implements StatDeviceOrderWidgetService {
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private OrderBaseDao orderBaseDao;
    @Autowired
    private StatDeviceOrderWidgetDao statDeviceOrderWidgetDao;
    @Autowired
    private DeviceAlarmDao deviceAlarmDao;
    @Autowired
    private PersonalPanelService personalPanelService;
    @Autowired
    private ShareBenefitSheetOrderDao shareBenefitSheetOrderDao;

    private static final Logger loggerOrder = LoggerFactory.getLogger("ORDER_LOGGER");

    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private static final String reg1 = "%Y-%m-%d";

    @Override
    public void setDataForWidget() {
        //设置日期
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        Date firstDay = c.getTime();
        Date now = new Date();
        Date yesterday = DateKit.addDate(now, -1);
        Date beforeYesterday = DateKit.addDate(now, -2);
        //先查询设备中存在的ownerId
        List<Integer> idList = deviceDao.getDiffOwnerId();
        Collections.sort(idList);
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < idList.size(); ++i) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    Integer ownerId = idList.get(index);
                    //根据sysUserId获取所有的productId
                    setDataForWidgetByOwnerId(firstDay, now, yesterday, beforeYesterday, ownerId);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    loggerOrder.error("==============用户" + idList.get(index) + "导入到stat_device_order_widget中不成功，原因：" + e);
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void setDataForWidgetByOwnerId(Date firstDay, Date now, Date yesterday, Date beforeYesterday, Integer ownerId) {
        long startTime = System.currentTimeMillis();

        Date start = DateKit.getDayStartTime(now);
        Date end = DateKit.getDayEndTime(now);

        Date yesterdayStart = DateKit.getDayStartTime(yesterday);
        Date yesterdayEnd = DateKit.getDayEndTime(yesterday);

        Date beforeYesterdayStart = DateKit.getDayStartTime(beforeYesterday);
        Date beforeYesterdayEnd = DateKit.getDayEndTime(beforeYesterday);

        List<Integer> productIdList = deviceDao.getProductId(ownerId);
        //删掉今天已经统计过的现在不存在的数据
        statDeviceOrderWidgetDao.deleteAlreadyEsixtData(ownerId, productIdList, now);
        for (int j = 0; j < productIdList.size(); ++j) {
            Integer totalCount = 0;
            Integer orderCountMonth = 0;
            Integer alarmCount = 0;
            Integer warnCount = 0;
            Integer warnRecord = 0;
            Integer shareOrderCount = 0;
            Integer newCount = 0;
            Integer orderedCount = 0;
            Integer orderCountToday = 0;
            Integer productId = productIdList.get(j);

            //统计设备
            newCount = deviceDao.getNewCount(ownerId, productId, start, end);
            totalCount = deviceDao.getTotalBySysUserIdAndIsNotDeleted(ownerId, productId, null);
            orderedCount = deviceDao.getOrderedCount(ownerId, productId, start, end);

            List<Integer> status = new ArrayList<>(1);
            status.add(OrderStatus.FINISH.getCode());
            //统计订单
            orderCountToday = orderBaseDao.getOrderCount(ownerId, productId, start, end, status, reg1);

            //统计故障设备
            alarmCount = deviceDao.getAlarmCount(ownerId, productId);//故障
            warnCount = deviceDao.getWarnCount(ownerId, productId);//报警
            warnRecord = alarmCount + warnCount;//报警+故障

            //统计分润单
//            shareOrderCount = shareBenefitSheetOrderDao.getOrderCount(ownerId, productId, now);

            StatDeviceOrderWidget statDeviceOrderWidget = new StatDeviceOrderWidget();
            statDeviceOrderWidget.setUtime(now);
            statDeviceOrderWidget.setTotalCount(totalCount);
            statDeviceOrderWidget.setAlarmPercent(0.0);
            statDeviceOrderWidget.setNewCount(newCount);
            statDeviceOrderWidget.setOrderedCount(orderedCount);
            statDeviceOrderWidget.setOrderedPercent(0.0);
            statDeviceOrderWidget.setAlarmCount(alarmCount);
            statDeviceOrderWidget.setOrderCountToday(orderCountToday);
            // statDeviceOrderWidget.setOrderCountMonth(orderCountMonth);
            statDeviceOrderWidget.setWarnCount(warnCount);
            statDeviceOrderWidget.setWarnRecord(warnRecord);
            statDeviceOrderWidget.setShareOrderCount(shareOrderCount);
//            没有将新分润统计写到personalPanel中
//            updateForPersonalPanelRealTime(statDeviceOrderWidget);

            //如果update实体statDeviceOrderWidget==0则insert
            // 先根据条件查询是否存在记录，根据id去更新避免死锁
//            StatDeviceOrderWidget exist = statDeviceOrderWidgetDao.selectByUtimeAndSysUserIdAndProductId(now, ownerId, productId);

            StatDeviceOrderWidget today = statDeviceOrderWidgetDao.selectByUtimeAndSysUserIdAndProductId(now, ownerId, productId);
            if (!ParamUtil.isNullOrEmptyOrZero(today)) {
                statDeviceOrderWidget.setId(today.getId());
                updateById(statDeviceOrderWidget);
            } else {
                Integer orderCountYesterday = 0;
                Integer orderCountBeforeYesterday = 0;
                //将非实时的给添加到数据库
                // 查询昨日统计数据
                StatDeviceOrderWidget yesterdayWidget = statDeviceOrderWidgetDao.selectByUtimeAndSysUserIdAndProductId(yesterday, ownerId, productId);
                if (!ParamUtil.isNullOrEmptyOrZero(yesterdayWidget)) {
                    orderCountYesterday = yesterdayWidget.getOrderCountToday();
                    orderCountBeforeYesterday = yesterdayWidget.getOrderCountYesterday();
                } else {
                    orderCountYesterday = orderBaseDao.getOrderCount(ownerId, productId, yesterdayStart, yesterdayEnd, status, reg1);
                    orderCountBeforeYesterday = orderBaseDao.getOrderCount(ownerId, productId, beforeYesterdayStart, beforeYesterdayEnd, status, reg1);
                }
                statDeviceOrderWidget.setOrderCountYesterday(orderCountYesterday == null ? 0 : orderCountYesterday);
                statDeviceOrderWidget.setOrderCountBeforeYesterday(orderCountBeforeYesterday == null ? 0 : orderCountBeforeYesterday);
                // 计算从月初到今天0点前的订单数，当前端获取实时月订单数时，用此数+当天实时订单数就行
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String todayStr = simpleDateFormat.format(now);
                String first = simpleDateFormat.format(firstDay);
                if (todayStr.equals(first)){
                    orderCountMonth = orderCountToday;
                } else {
                    orderCountMonth = orderBaseDao.getOrderCount(ownerId, productId, DateKit.getDayStartTime(firstDay), now, status, reg1);
                }
                statDeviceOrderWidget.setOrderCountMonth(orderCountMonth);
                // Double orderNewPercent = StatisticsUtil.calcIncreasePercent(orderCountBeforeYesterday, orderCountYesterday);
                // statDeviceOrderWidget.setOrderNewPercentYesterday(orderNewPercent);
                statDeviceOrderWidget.setCtime(now);
                statDeviceOrderWidget.setUtime(now);
                statDeviceOrderWidget.setSysUserId(ownerId);
                statDeviceOrderWidget.setProductId(productId);
                statDeviceOrderWidgetDao.insert(statDeviceOrderWidget);
//                updateForPersonalPanelNotRealTime(statDeviceOrderWidget);
            }

        }

        loggerOrder.info("统计用户{}的设备订单看板，耗时：{}ms", ownerId, System.currentTimeMillis() - startTime);
    }

    private void updateForPersonalPanelNotRealTime(StatDeviceOrderWidget widget) {
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.YESTERDAY_ORDER_TOTAL_NUMBER.getCode(), String.valueOf(widget.getOrderCountYesterday()), String.valueOf(widget.getOrderCountBeforeYesterday() - widget.getOrderCountYesterday()));
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.YESTERDAY_ORDER_INCREMENT_RATE.getCode(), String.valueOf(widget.getOrderNewPercentYesterday()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.CURRENT_MONTH_ORDER_TOTAL_NUMBER.getCode(), String.valueOf(widget.getOrderCountMonth()), null);
    }

    private void updateForPersonalPanelRealTime(StatDeviceOrderWidget widget) {
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.CURRENT_DEVICE_TOTAL_NUMBER.getCode(), String.valueOf(widget.getTotalCount()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.TODAY_DEVICE_NEW_ADD_NUMBER.getCode(), String.valueOf(widget.getNewCount()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.TODAY_DEVICE_ORDER_RATE.getCode(), String.valueOf(widget.getOrderedPercent()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.CURRENT_DEVICE_FAULT_NUMBER.getCode(), String.valueOf(widget.getAlarmCount()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.TODAY_ORDER_TOTAL_NUMBER.getCode(), String.valueOf(widget.getOrderCountToday()), null);
    }

    @Override
    public StatOrderWidgetVo orderWidget(Integer productId, SysUser currentUser, List<Integer> ids) {
        if (!Objects.nonNull(currentUser)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        Date now = new Date();
        StatDeviceOrderWidget widget = statDeviceOrderWidgetDao.orderWidgetByIds(ids, productId, now);
        Integer today = 0, month = 0;
        Double percent = 0.0;
        StatWidgetDataVo yesterday = new StatWidgetDataVo();
        //如果productId为空时则将所有的项统计下来
        if (!Objects.isNull(widget)) {
            today = widget.getOrderCountToday();
            month = widget.getOrderCountMonth();
            yesterday.setData(widget.getOrderCountYesterday());
            yesterday.setOdd(widget.getOrderCountYesterday() - widget.getOrderCountBeforeYesterday());
        } else {
            loggerOrder.warn("请查看statDeviceOrderWidget中是否有符合当天:" + DateKit.getTimestampString(now) + "，产品id:" + productId + "和当前用户:" + currentUser.getId() + "的订单数据");
            widget = new StatDeviceOrderWidget();
            widget.setShareOrderCount(0);
            widget.setShareOrderMoney(new BigDecimal(0));
            yesterday.setData(0);
            yesterday.setOdd(0);
        }
        // 类似这种百分比的东西，要获取的时候再计算，不能直接用数据库内的总和求平均值
        percent = ParamUtil.isNullOrZero(widget.getOrderCountBeforeYesterday()) ? 0 :
                yesterday.getOdd().doubleValue() / widget.getOrderCountBeforeYesterday();
        percent = BigDecimal.valueOf(percent).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        StatOrderWidgetVo statOrderWidgetVo = new StatOrderWidgetVo(today, yesterday, percent, month);
        statOrderWidgetVo.setShareOrderCount(widget.getShareOrderCount());
        statOrderWidgetVo.setShareOrderMoney(widget.getShareOrderMoney());
        return statOrderWidgetVo;
    }

    @Override
    public StatDeviceWidgetVo deviceWidget(Integer productId, SysUser currentUser, List<Integer> ids) {
        if (!Objects.nonNull(currentUser)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        Date now = new Date();
        StatDeviceOrderWidget widget = statDeviceOrderWidgetDao.deviceWidgetByIds(ids, productId, now);
        Integer total = 0, newToday = 0, alarm = 0, orderedCount = 0;
        Double orderedPercent = 0.0;
        if (!Objects.isNull(widget)) {
            total = widget.getTotalCount();
            newToday = widget.getNewCount();
            alarm = widget.getAlarmCount();
            orderedCount = (widget.getOrderedCount() == null ? 0 : widget.getOrderedCount());
            Double percent = (total == 0 ? 0.0 : (double) orderedCount / total);
            orderedPercent = new BigDecimal(percent).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            loggerOrder.warn("请查看statDeviceOrderWidget中是否有符合当天:" + DateKit.getTimestampString(now) + "，产品id:" + productId + "和当前用户:" + currentUser.getId() + "的设备数据");
        }
        StatDeviceWidgetVo statDeviceWidgetVo = new StatDeviceWidgetVo(total, newToday, orderedPercent, alarm);
        return statDeviceWidgetVo;
    }

    @Override
    public StatAlarmWidgetVo alarmWidget(Integer productId, SysUser currentUser, List<Integer> ids) {
        if (!Objects.nonNull(currentUser)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        Date now = new Date();
        StatDeviceOrderWidget widget = statDeviceOrderWidgetDao.alarmWidgetByIds(ids, productId, now);
        Integer alarmDevice = 0, todayAlarmRecord = 0;
        Double percent = 0.0;
        if (!Objects.isNull(widget)) {
            Integer total = widget.getTotalCount() == null ? 0 : widget.getTotalCount();
            Integer alarmCount = widget.getAlarmCount() == null ? 0 : widget.getAlarmCount();
            Integer warnCount = widget.getWarnCount() == null ? 0 : widget.getWarnCount();
            alarmDevice = alarmCount + warnCount;
            todayAlarmRecord = widget.getWarnRecord();
            Double alarmPercent = (total == 0 ? 0.0 : (double) alarmDevice / total);
            percent = new BigDecimal(alarmPercent).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            loggerOrder.warn("请查看statDeviceOrderWidget中是否有符合当天，产品id和当前用户的告警设备数据");
        }
        StatAlarmWidgetVo statAlarmWidgetVo = new StatAlarmWidgetVo(alarmDevice, todayAlarmRecord, percent);
        return statAlarmWidgetVo;
    }

    @Override
    public Integer count15DaysDevices() {
        return statDeviceOrderWidgetDao.count15DaysDevices();
    }

    @Override
    public Integer countOffDevices() {
        return statDeviceOrderWidgetDao.countOffDevices();
    }

    @Override
    public Integer countRemain10Devices() {
        return statDeviceOrderWidgetDao.countRemain10Devices();
    }

    @Override
    public Double sumTotalAmountToday() {
        return statDeviceOrderWidgetDao.sumTotalAmountToday();
    }

    @Override
    public List<Map<String, Double>> sumOperatorAndSort() {
        return statDeviceOrderWidgetDao.sumOperatorAndSort();
    }
}
