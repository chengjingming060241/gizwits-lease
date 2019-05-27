package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.user.dao.UserDao;
import com.gizwits.lease.constant.SexType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.stat.dao.StatUserTrendDao;
import com.gizwits.lease.stat.dto.StatUserTrendDto;
import com.gizwits.lease.stat.entity.StatUserTrend;
import com.gizwits.lease.stat.service.StatUserTrendService;
import com.gizwits.lease.stat.vo.StatSexVo;
import com.gizwits.lease.stat.vo.StatTimesVo;
import com.gizwits.lease.stat.vo.StatTrendVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <p>
 * 用户趋势及性别，使用次数统计表 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@Service
public class StatUserTrendServiceImpl extends ServiceImpl<StatUserTrendDao, StatUserTrend> implements StatUserTrendService {
    @Autowired
    private StatUserTrendDao statUserTrendDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderBaseDao orderBaseDao;

    protected static Logger loggerStat =LoggerFactory.getLogger("USER_LOGGER");

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    @Override
    public void setDataForStatUserTrend() {
        loggerStat.info("================获取StatUserTrend数据开始");
        Date yesterday = DateKit.addDate(new Date(), -1);
        Date yesterdayBefore7 = DateKit.addDate(yesterday, -7);
        Date yesterdayStart = DateKit.getDayStartTime(yesterday);
        Date yesterdayEnd = DateKit.getDayEndTime(yesterday);
        //先从user表中获取所有的sysUserId
        List<Integer> idList = userDao.getDiffSysUserId();
        //定义一个线程池
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        //根据sysUserId查询昨天的用户总量，用户活跃数，用户新增数
        loggerStat.info("=============开始入库");
        for (int i = 0; i < idList.size(); ++i) {
            //固定好当前的index
            final int index = i;
            fixedThreadPool.execute(() -> {
                Integer sysUserId = idList.get(index);
                try {
                    StatUserTrend statUserTrend = new StatUserTrend();
                    Integer male = userDao.getSex(sysUserId, SexType.MALE.getCode());//这种不要写常量,写成枚举
                    Integer female = userDao.getSex(sysUserId, SexType.FEMALE.getCode());
                    List<Map<Integer, Number>> orderTimes =
                            userDao.getOrderTimes(sysUserId, yesterdayBefore7, yesterday);
                    int zero = 0;
                    int oneTwo = 0;
                    int threeFour = 0;
                    int fiveSix = 0;
                    int sevenEight = 0;
                    int nineTen = 0;
                    int tenMore = 0;
                    for (Map<Integer, Number> oneUserOrderTimes : orderTimes) {
                        Number times = oneUserOrderTimes.get("orderTimes");
                        if(times==null)continue;
                        switch (times.intValue()){
                            case 0:zero++;break;
                            case 1:
                            case 2:oneTwo++;break;
                            case 3:
                            case 4:threeFour++;break;
                            case 5:
                            case 6:fiveSix++;break;
                            case 7:
                            case 8:sevenEight++;break;
                            case 9:
                            case 10:nineTen++;break;
                            default:tenMore++;break;
                        }
                    }
                    statUserTrend.setCtime(yesterday);
                    statUserTrend.setSysUserId(sysUserId);
                    statUserTrend.setTotalCount(userDao.getTotal(sysUserId));
                    statUserTrend.setNewCount(userDao.getNewByDate(sysUserId,yesterdayStart,yesterdayEnd));
                    statUserTrend.setActiveCount(userDao.getActive(sysUserId,yesterdayStart,yesterdayEnd));
                    statUserTrend.setMale(male);
                    statUserTrend.setFemale(female);
                    statUserTrend.setZero(zero);
                    statUserTrend.setOneTwo(oneTwo);
                    statUserTrend.setThreeFour(threeFour);
                    statUserTrend.setFiveSix(fiveSix);
                    statUserTrend.setSevenEight(sevenEight);
                    statUserTrend.setNineTen(nineTen);
                    statUserTrend.setTenMore(tenMore);
                    statUserTrendDao.insert(statUserTrend);
                } catch (Exception e) {
                    loggerStat.warn("入库失败的sysUserId:" + sysUserId + "=========" + e);
                    e.printStackTrace();
                }
            });
        }
        loggerStat.info("=============结束入库");
    }

    @Override
    public List<StatTrendVo> getNewTrend(SysUser currentUser, StatUserTrendDto statUserTrendDto, List<Integer> ids) {
        //判断当前currentUser
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        List<StatTrendVo> statTrendVos = new ArrayList<>();
        //根据给定的日期获取数据
        List<StatUserTrend> list = statUserTrendDao.getNewTrend(ids, statUserTrendDto);
        if (list.size()==0){
            loggerStat.warn("请查看stat_user_trend表中是否含有当前用户："+ currentUser.getId()+"时间为："+DateKit.getTimestampString(statUserTrendDto.getFromDate())+"~"+DateKit.getTimestampString(statUserTrendDto.getToDate())+"的新增数量记录");
        }
        for (int i = 0; i < list.size(); ++i) {
            StatTrendVo statTrendVo = new StatTrendVo();
            StatUserTrend statUserTrend = list.get(i);
            statTrendVo.setTime(DateKit.getTimestampString(statUserTrend.getCtime()));
            statTrendVo.setCount(statUserTrend.getNewCount());
            statTrendVos.add(statTrendVo);
        }
        return statTrendVos;
    }

    @Override
    public List<StatTrendVo> getActiveTrend(SysUser currentUser, StatUserTrendDto statUserTrendDto, List<Integer> ids) {
        //判断当前currentUser
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        List<StatTrendVo> statTrendVos = new ArrayList<>();
        //根据给定的日期获取数据
        List<StatUserTrend> list = statUserTrendDao.getActiveTrend(ids, statUserTrendDto);
        if (list.size()==0){
            loggerStat.warn("请查看stat_user_trend表中是否含有当前用户："+ currentUser.getId()+"时间为："+DateKit.getTimestampString(statUserTrendDto.getFromDate())+"~"+DateKit.getTimestampString(statUserTrendDto.getToDate())+"的活跃数量记录");
        }
        for (int i = 0; i < list.size(); ++i) {
            StatTrendVo statTrendVo = new StatTrendVo();
            StatUserTrend statUserTrend = list.get(i);
            statTrendVo.setTime(DateKit.getTimestampString(statUserTrend.getCtime()));
            statTrendVo.setCount(statUserTrend.getActiveCount());
            statTrendVos.add(statTrendVo);
        }
        return statTrendVos;
    }

    @Override
    public List<StatTrendVo> getTotalTrend(SysUser currentUser, StatUserTrendDto statUserTrendDto, List<Integer> ids) {
        //判断当前currentUser
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        List<StatTrendVo> statTrendVos = new ArrayList<>();
        //根据给定的日期获取数据
        List<StatUserTrend> list = statUserTrendDao.getTotalTrend(ids, statUserTrendDto);
        if (list.size()==0){
            loggerStat.warn("请查看stat_user_trend表中是否含有当前用户："+ currentUser.getId()+"时间为："+DateKit.getTimestampString(statUserTrendDto.getFromDate())+"~"+DateKit.getTimestampString(statUserTrendDto.getToDate())+"的总数量记录");
        }
        for (int i = 0; i < list.size(); ++i) {
            StatTrendVo statTrendVo = new StatTrendVo();
            StatUserTrend statUserTrend = list.get(i);
            statTrendVo.setTime(DateKit.getTimestampString(statUserTrend.getCtime()));
            statTrendVo.setCount(statUserTrend.getTotalCount());
            statTrendVos.add(statTrendVo);
        }
        return statTrendVos;
    }

    @Override
    public StatSexVo getSexDistribution(SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        //获取日期为昨天
        Date yesterday = DateKit.addDate(new Date(),-1);
        StatUserTrend statUserTrend = statUserTrendDao.getSex(ids, yesterday);
        StatSexVo statSexVo = new StatSexVo();
        //判断statUserTrend是否为空
        if (!Objects.isNull(statUserTrend)){
            statSexVo.setFemale(statUserTrend.getFemale());
            statSexVo.setMale(statUserTrend.getMale());
        }else {
            loggerStat.warn("请查看stat_user_trend中，是否含有当前用户："+currentUser.getId()+"日期为:"+DateKit.getTimestampString(yesterday)+"含有省对应用户人数");
        }
        return statSexVo;
    }

    @Override
    public StatTimesVo getTimesDistribution(SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        Date yesterday = DateKit.addDate(new Date(),-1);
        StatUserTrend statUserTrend = statUserTrendDao.getTimes(ids,yesterday);
        StatTimesVo statTimesVo = new StatTimesVo();
        if (!Objects.isNull(statUserTrend)){
            BeanUtils.copyProperties(statUserTrend,statTimesVo);
        }else {
            loggerStat.warn("请查看stat_user_trend中，是否含有当前用户："+currentUser.getId()+"日期为:"+DateKit.getTimestampString(yesterday)+"含有省对应用户人数");
        }
        return statTimesVo;
    }

}
