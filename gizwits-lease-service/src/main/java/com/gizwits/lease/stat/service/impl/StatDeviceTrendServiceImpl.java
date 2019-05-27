package com.gizwits.lease.stat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.model.HttpRespObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.HttpUtil;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.dao.DeviceDao;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.product.dao.ProductDao;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.stat.dao.StatDeviceOrderWidgetDao;
import com.gizwits.lease.stat.dao.StatDeviceTrendDao;
import com.gizwits.lease.stat.dto.StatDeviceTrendDto;
import com.gizwits.lease.stat.entity.StatDeviceOrderWidget;
import com.gizwits.lease.stat.entity.StatDeviceTrend;
import com.gizwits.lease.stat.entity.StatUserTrend;
import com.gizwits.lease.stat.service.StatDeviceOrderWidgetService;
import com.gizwits.lease.stat.service.StatDeviceTrendService;
import com.gizwits.lease.stat.vo.StatTrendVo;
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
 * 设备趋势统计表 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@Service
public class StatDeviceTrendServiceImpl extends ServiceImpl<StatDeviceTrendDao, StatDeviceTrend> implements StatDeviceTrendService {

    //这两个日志不合理
    protected static Logger loggerStat = LoggerFactory.getLogger(StatUserTrend.class);

    protected static Logger loggerDevice = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private StatDeviceTrendDao statDeviceTrendDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProductDao productDao;

    @Autowired
    private StatDeviceOrderWidgetDao statDeviceOrderWidgetDao;

//    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static String reg1 = "%Y-%m-%d";


    @Override
    public void setDataForStatDeviceTrend() {
        Date today = new Date();
        loggerStat.info(DateKit.getTimestampString(today) + "<=============开始录入信息到stat_device_trend===============>");
        Date yesterday = DateKit.addDate(today, -1);

//        //已经存在了就先删掉吧,因为一天统计一次无需删除数据
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String time = sdf.format(yesterday);
//        delete(new EntityWrapper<StatDeviceTrend>().like("ctime", time, SqlLike.RIGHT));

        //查询设备中存在的ownerId的List
        List<Integer> idList = deviceDao.getDiffOwnerId();
        //根据从产品id查询
        for (int i = 0; i < idList.size(); ++i) {
            final int index = i;
            try {
                Integer ownerId = idList.get(index);
                loggerDevice.info(ownerId + "的statDeviceTrend录入开始");
                setDataForDeviceTrendForOwnerId(today, yesterday, ownerId);
                loggerDevice.info(ownerId + "的statDeviceTrend录入结束");
            } catch (Exception e) {
                loggerDevice.error("入库失败的sysUserId:" + idList.get(index) + "=========" + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setDataForDeviceTrendForOwnerId(Date today, Date yesterday, Integer ownerId) {
        Date todayStartTime = DateUtil.getDayStartTime(today);
        Date yesterdayStartTime = DateUtil.getDayStartTime(yesterday);

        Date yesterEndTime = DateUtil.getDayEndTime(yesterday);
        //查询sysUserId对应的所有productId
        List<Integer> productIdList = deviceDao.getProductId(ownerId);
        for (int j = 0; j < productIdList.size(); ++j) {
            Integer productId = productIdList.get(j);
            Integer newCount = 0;
            Integer orderedCount = 0;
            Integer previousDeviceTotal = 0;
            StatDeviceTrend statDeviceTrend = new StatDeviceTrend();
            StatDeviceOrderWidget orderWidget = statDeviceOrderWidgetDao.selectByUtimeAndSysUserIdAndProductId(yesterday, ownerId, productId);
            if (ParamUtil.isNullOrEmptyOrZero(orderWidget)) {
                newCount = orderWidget.getNewCount();
                orderedCount = orderWidget.getOrderedCount();
                previousDeviceTotal = orderWidget.getTotalCount() - newCount;

            } else {
                newCount = deviceDao.getNewCount(ownerId, productId, yesterdayStartTime, yesterEndTime);
                orderedCount = deviceDao.getOrderedCount(ownerId, productId, yesterdayStartTime, yesterEndTime);
                previousDeviceTotal = deviceDao.getTotalBySysUserIdAndIsNotDeleted(ownerId, productId, todayStartTime);
            }

            //这里改为从设备上报记录表统计
            Integer activeCount = deviceDao.getActiveCount(ownerId, productId, yesterdayStartTime, todayStartTime);

            //建议改成big decimal 类型!!
//            BigDecimal orderedPercent = (previousDeviceTotal) <= 0 ? BigDecimal.ZERO : new BigDecimal(((double) orderedCount / (previousDeviceTotal)));
            //将设备趋势表的sysUserId设为ownerId
            statDeviceTrend.setSysUserId(ownerId);
            statDeviceTrend.setCtime(yesterday);
            statDeviceTrend.setNewCount(newCount);
            statDeviceTrend.setOrderedCount(orderedCount);
            statDeviceTrend.setOrderedPercent(BigDecimal.ZERO);
            statDeviceTrend.setProductId(productId);
            statDeviceTrend.setActiveCount(activeCount);
            statDeviceTrend.setPreviousDeviceTotal(previousDeviceTotal);
            insert(statDeviceTrend);
        }
    }

    /**
     * 请求机智云平台获取最近一天的设备活跃数
     *
     * @return Integer
     */
    private Integer getActiveCount(String productKey) {
        //获取token
        String token = redisService.getGizwitsAccessTokenPrefix(productKey);
        //设置url
        //常量能不能放到系统配置里面???
        String url = "http://enterpriseapi.gizwits.com/v1/products/" + productKey + "/devices/report/liveness";
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "token " + token);
        HttpRespObject res = HttpUtil.sendGet(url, header);
        String content = (res.getStatusCode() == 200) ? String.valueOf(res.getContent()) : "{latest_1:0}";
        JSONObject json = JSONObject.parseObject(content);
        Integer activeCount = Integer.valueOf(String.valueOf(json.get("latest_1")));
        return activeCount;
    }

    @Override
    public Map<Integer, Integer> getActiveCountFromGizwits() {
//        List<Map<String, Object>> productIdAndKey = deviceDao.getProductIdAndKey();
        List<Map<String, Object>> productIdAndKey = productDao.getProductIdAndKey();
        Map<Integer, Integer> mapProductIdAndCount = new HashMap<>(productIdAndKey.size());
        for (int i = 0; i < productIdAndKey.size(); ++i) {
            Map<String, Object> map = productIdAndKey.get(i);
            String productKey = String.valueOf(map.get("productKey"));
            Integer activeCount = getActiveCount(productKey);
            mapProductIdAndCount.put(Integer.valueOf(String.valueOf(map.get("productId"))), activeCount);
        }
        return mapProductIdAndCount;
    }

    @Override
    public List<StatTrendVo> getNewTrend(StatDeviceTrendDto statDeviceTrendDto, SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        List<StatTrendVo> statTrendVoList = new ArrayList<>();

        List<StatDeviceTrend> list = statDeviceTrendDao.getNewTrendByIds(ids, statDeviceTrendDto);
        if (list.size() == 0) {
            loggerDevice.warn("请查看stat_device_trend表中是否含有当前用户：" + currentUser.getId() + "日期在" + DateKit.getTimestampString(statDeviceTrendDto.getFromDate()) + "和" + DateKit.getTimestampString(statDeviceTrendDto.getToDate()) + "的新增数量记录");
        }
        for (int i = 0; i < list.size(); ++i) {
            StatTrendVo statTrendVo = new StatTrendVo();
            StatDeviceTrend statDeviceTrend = list.get(i);
            statTrendVo.setTime(DateKit.getTimestampString(statDeviceTrend.getCtime()));
            statTrendVo.setCount(statDeviceTrend.getNewCount());
            if (statDeviceTrend.getPreviousDeviceTotal() != 0) {
                Double rate = statDeviceTrend.getNewCount() * 1.0 / statDeviceTrend.getPreviousDeviceTotal();
                BigDecimal precent = new BigDecimal(rate).setScale(4, BigDecimal.ROUND_HALF_UP);
                statTrendVo.setPrecent(precent);
            }
            statTrendVoList.add(statTrendVo);
        }
        return statTrendVoList;
    }

    @Override
    public List<StatTrendVo> getActiveTrend(StatDeviceTrendDto statDeviceTrendDto, SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        List<StatTrendVo> statTrendVoList = new ArrayList<>();
        List<StatDeviceTrend> list = statDeviceTrendDao.getActiveTrendByIds(ids, statDeviceTrendDto);
        if (list.size() == 0) {
            loggerDevice.warn("请查看stat_device_trend表中是否含有当前用户：" + currentUser.getId() + "日期在" + DateKit.getTimestampString(statDeviceTrendDto.getFromDate()) + "和" + DateKit.getTimestampString(statDeviceTrendDto.getToDate()) + "的活跃数量记录");
        }
        for (int i = 0; i < list.size(); ++i) {
            StatTrendVo statTrendVo = new StatTrendVo();
            StatDeviceTrend statDeviceTrend = list.get(i);
            statTrendVo.setTime(DateKit.getTimestampString(statDeviceTrend.getCtime()));
            statTrendVo.setCount(statDeviceTrend.getActiveCount());
            if (statDeviceTrend.getPreviousDeviceTotal() != 0) {
                Double rate = statDeviceTrend.getActiveCount() * 1.0 / statDeviceTrend.getPreviousDeviceTotal();
                BigDecimal precent = new BigDecimal(rate).setScale(4, BigDecimal.ROUND_HALF_UP);
                statTrendVo.setPrecent(precent);
            }
            statTrendVoList.add(statTrendVo);
        }
        return statTrendVoList;
    }

    @Override
    public List<StatTrendVo> getUsePercentTrend(StatDeviceTrendDto statDeviceTrendDto, SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        List<StatTrendVo> statTrendVoList = new ArrayList<>();
        List<StatDeviceTrend> list = statDeviceTrendDao.getUsePecentTrendByIds(ids, statDeviceTrendDto);
        if (list.size() == 0) {
            loggerDevice.warn("请查看stat_device_trend表中是否含有当前用户：" + currentUser.getId() + "日期在" + DateKit.getTimestampString(statDeviceTrendDto.getFromDate()) + "和" + DateKit.getTimestampString(statDeviceTrendDto.getToDate()) + "的订单率数量记录");
        }
        for (int i = 0; i < list.size(); ++i) {
            StatTrendVo statTrendVo = new StatTrendVo();
            StatDeviceTrend statDeviceTrend = list.get(i);
            statTrendVo.setTime(DateKit.getTimestampString(statDeviceTrend.getCtime()));
            Integer orderedCount = statDeviceTrend.getOrderedCount();
            Integer previousDeviceTotal = statDeviceTrend.getPreviousDeviceTotal();
            Double percent = (ParamUtil.isNullOrZero(previousDeviceTotal) ? 0.0 : (double) orderedCount / previousDeviceTotal);
            BigDecimal decimal = new BigDecimal(percent).setScale(4, BigDecimal.ROUND_HALF_UP);
            statTrendVo.setCount(orderedCount);
            statTrendVo.setPrecent(decimal);
            statTrendVoList.add(statTrendVo);
        }
        return statTrendVoList;
    }
}
