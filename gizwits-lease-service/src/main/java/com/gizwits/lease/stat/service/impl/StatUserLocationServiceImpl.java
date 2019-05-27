package com.gizwits.lease.stat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.stat.dao.StatUserLocationDao;
import com.gizwits.lease.stat.entity.StatUserLocation;
import com.gizwits.lease.stat.service.StatUserLocationService;
import com.gizwits.lease.stat.vo.StatLocationVo;
import com.gizwits.lease.user.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户地图分布统计表 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
@Service
public class StatUserLocationServiceImpl extends ServiceImpl<StatUserLocationDao, StatUserLocation> implements StatUserLocationService {
    @Autowired
    private StatUserLocationDao statUserLocationDao;
    @Autowired
    private UserDao userDao;
    protected static Logger logger= LoggerFactory.getLogger("USER_LOGGER");

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Override
    public void setDataForLocation() {
        logger.info("=====================用户分布统计开始"+DateKit.getTimestampString(new Date()));
        Date yesterday  = DateKit.addDate(new Date(),-1);
        //找出用户存在的sysUserId
        List<Integer> idList = userDao.getDiffSysUserId();
        //根据sysUserId获取List<省名，用户数>
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < idList.size(); ++i) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Integer sysUserId = idList.get(index);
                    try {
                        List<Map<String, Object>> mapAreaList = userDao.findProvinceAndCount(sysUserId);
                        Integer total = userDao.getTotal(sysUserId);
                        for (int j = 0; j < mapAreaList.size(); ++j) {
                            Map<String, Object> mapArea = mapAreaList.get(j);
                            Object province = mapArea.get("province");
                            if (province == null) {
                                province = "其他";
                            }
                            StatUserLocation statUserLocation = new StatUserLocation();
                            statUserLocation.setSysUserId(sysUserId);
                            statUserLocation.setCtime(yesterday);
                            statUserLocation.setProvince(province.toString());
                            Object count = mapArea.get("count");
                            statUserLocation.setUserCount(Integer.valueOf(count.toString()));
                            statUserLocationDao.insert(statUserLocation);
                        }
                    }catch (Exception e){
                        logger.warn("=======================用户："+ sysUserId +"导入用户分布失败，原因如下："+e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public List<StatLocationVo> getDitribution(SysUser currentUser, List<Integer> ids) {
        if (currentUser==null){
            //使用LeaseException 抛出异常!!
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        Date yesterday = DateKit.addDate(new Date(),-1);
        List<StatUserLocation> list = statUserLocationDao.getDistribution(ids, yesterday);
        if (list.size() == 0) {
            logger.warn("查看stat_user_location中是否有当前用户：" + currentUser.getId() + "日期为：" +
                    DateKit.getTimestampString(yesterday) + "的记录");
        } else {
            int total = list.stream().mapToInt(StatUserLocation::getUserCount).sum();
            list.stream().forEach(statUserLocation -> statUserLocation
                    .setProportion((double) statUserLocation.getUserCount() / total));
        }
        return turnIntoVoFromEntity(list);
    }

    @Override
    public List<StatLocationVo> getRank(SysUser currentUser, List<Integer> ids) {
        if (currentUser==null){
            //使用LeaseException 抛出异常!!
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        Date yesterday = DateKit.addDate(new Date(),-1);
        List<StatUserLocation> list = statUserLocationDao.getRank(ids,yesterday);
        if (list.size() == 0) {
            logger.warn("查看stat_user_location中是否有当前用户：" + currentUser.getId() + "日期为：" +
                    DateKit.getTimestampString(yesterday) + "的记录");
        } else {
            int total = list.stream().mapToInt(StatUserLocation::getUserCount).sum();
            list.stream().forEach(statUserLocation -> statUserLocation
                    .setProportion((double) statUserLocation.getUserCount() / total));
        }
        return turnIntoVoFromEntity(list);
    }

    private List<StatLocationVo>turnIntoVoFromEntity(List<StatUserLocation>list){
        List<StatLocationVo> statLocationVoList = new ArrayList<>();
        for (int i =0;i<list.size();++i){
            StatLocationVo statLocationVo = new StatLocationVo();
            StatUserLocation statUserLocation = list.get(i);
            statLocationVo.setProvince(statUserLocation.getProvince());
            statLocationVo.setCount(statUserLocation.getUserCount());
            statLocationVo.setProportion(statUserLocation.getProportion());
            statLocationVoList.add(statLocationVo);
        }
        return statLocationVoList;
    }
}
