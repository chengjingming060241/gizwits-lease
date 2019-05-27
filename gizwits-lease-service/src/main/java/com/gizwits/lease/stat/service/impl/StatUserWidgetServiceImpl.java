package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.user.dao.UserDao;
import com.gizwits.lease.enums.PanelModuleItemType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.panel.service.PersonalPanelService;
import com.gizwits.lease.stat.dao.StatUserWidgetDao;
import com.gizwits.lease.stat.entity.StatUserWidget;
import com.gizwits.lease.stat.service.StatUserWidgetService;
import com.gizwits.lease.stat.vo.StatUserWidgetVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
public class StatUserWidgetServiceImpl extends ServiceImpl<StatUserWidgetDao, StatUserWidget> implements StatUserWidgetService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private StatUserWidgetDao statUserWidgetDao;
    @Autowired
    private PersonalPanelService personalPanelService;

    protected static Logger logger = LoggerFactory.getLogger("USER_LOGGER");

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    public void setDataForWidget() {
        Date today = new Date();
        Date todayStart = DateKit.getDayStartTime(today);
        Date todayEnd = DateKit.getDayEndTime(today);
        Date before7Days = DateKit.addDate(today, -7);
        List<Integer> idList = userDao.getDiffSysUserId();
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //用户看板整合
        for (int i = 0; i < idList.size(); ++i) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    StatUserWidget statUserWidget = new StatUserWidget();
                    Integer sysUserId = idList.get(index);
                    Integer totalCount = userDao.getTotal(sysUserId);
                    // 活跃用户：7天内至少授权微信公众号过1次 or 7天内至少下单过1次的用户
                    Integer activeCount = userDao.getActive(sysUserId, before7Days, today);
                    Integer newCount = userDao.getNewByDate(sysUserId, todayStart,todayEnd);
                    statUserWidget.setTotalCount(totalCount == null ? 0 : totalCount);
                    statUserWidget.setActiveCount(activeCount == null ? 0 : activeCount);
                    statUserWidget.setNewCount(newCount == null ? 0 : newCount);
                    statUserWidget.setUtime(today);
                    statUserWidget.setSysUserId(sysUserId);
                    updatePersonalPanelRealTime(statUserWidget);
                    //如果修改不成功，则需要添加
                    if (statUserWidgetDao.updateByUtimeAndSysUserId(statUserWidget) == 0) {
                        statUserWidget.setCtime(today);
                        statUserWidgetDao.insert(statUserWidget);
                        updatePersonalPanelNotRealTime(statUserWidget);
                    }
                } catch (Exception e) {
                    logger.warn("===================设备："+idList.get(index)+"导入到stat_user_widget表失败,原因如下："+e);
                }
            });
        }
    }

    private void updatePersonalPanelNotRealTime(StatUserWidget widget) {
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.YESTERDAY_USER_INCREMENT_RATE.getCode(), String.valueOf(widget.getNewPercent()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.YESTERDAY_ACTIVE_USER_RATE.getCode(), String.valueOf(widget.getActivePercent()), null);
    }

    private void updatePersonalPanelRealTime(StatUserWidget widget) {
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.CURRENT_USER_TOTAL_NUMBER.getCode(), String.valueOf(widget.getTotalCount()), null);
        personalPanelService.updateDataItemValue(widget.getSysUserId(), PanelModuleItemType.TODAY_ACTIVE_USER_TOTAL_NUMBER.getCode(), String.valueOf(widget.getActiveCount()), null);
    }

    @Override
    public StatUserWidgetVo widget(SysUser currentUser, List<Integer> ids) {
        if (!Objects.nonNull(currentUser)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        Date now = new Date();
        StatUserWidget w = statUserWidgetDao.widgetByIds(ids, now);
        StatUserWidget yesterdayW = statUserWidgetDao.widgetByIds(ids, DateKit.addDate(now, -1));
        StatUserWidgetVo statUserWidgetVo;
        if (!Objects.isNull(w)) {
            double yesterdayNewPercent = 0;
            double yesterdayActivePercent = 0;
            if (!Objects.isNull(yesterdayW)) {
                int beforeYesterdayTotalCount = yesterdayW.getTotalCount() - yesterdayW.getNewCount();
                yesterdayNewPercent =
                        beforeYesterdayTotalCount == 0 ? 0 : (double) yesterdayW.getNewCount() / beforeYesterdayTotalCount;
                yesterdayNewPercent =
                        BigDecimal.valueOf(yesterdayNewPercent).setScale(4, BigDecimal.ROUND_HALF_UP)
                                .doubleValue();
                yesterdayActivePercent =
                        yesterdayW.getTotalCount() == 0 ? 0 : (double) yesterdayW.getActiveCount() / yesterdayW.getTotalCount();
                yesterdayActivePercent =
                        BigDecimal.valueOf(yesterdayActivePercent).setScale(4, BigDecimal.ROUND_HALF_UP)
                                .doubleValue();
            }
            statUserWidgetVo = new StatUserWidgetVo(w.getTotalCount(), yesterdayNewPercent, w.getActiveCount(),
                    yesterdayActivePercent);
        } else {
            logger.warn("请查看stat_user_widget中，当前用户：" + currentUser.getId() + "日期为：" + DateKit.getTimestampString(now) + "是否含有数据");
            statUserWidgetVo = new StatUserWidgetVo();
        }
        return statUserWidgetVo;
    }
}
