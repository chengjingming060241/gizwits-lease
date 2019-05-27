package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.dao.DeviceDao;
import com.gizwits.lease.device.dao.DeviceLaunchAreaDao;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.stat.dao.StatDeviceLocationDao;
import com.gizwits.lease.stat.entity.StatDeviceLocation;
import com.gizwits.lease.stat.service.StatDeviceLocationService;
import com.gizwits.lease.stat.vo.StatLocationVo;

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
 * 设备地图分布统计表 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
@Service
public class StatDeviceLocationServiceImpl extends ServiceImpl<StatDeviceLocationDao, StatDeviceLocation> implements StatDeviceLocationService {
    @Autowired
    private StatDeviceLocationDao statDeviceLocationDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DeviceLaunchAreaDao deviceLaunchAreaDao;

    @Autowired
    private DeviceService deviceService;

    protected static Logger logger = LoggerFactory.getLogger("SQL_LOGGER");

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void setDataForLocation() {
        logger.info("=====================开始导入设备分布:" + DateKit.getTimestampString(new Date()));
        Date yesterday = DateKit.addDate(new Date(), -1);
//        List<StatDeviceLocation> statDeviceLocationList = new ArrayList<>();
        //查询所有存在于设备表中的ownerId
//        List<Integer> idList = deviceDao.getDiffOwnerId();
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //根据sysUserId获取产品id对应的List<省名,设备数>
        executorService.execute(() -> {
            //统计分配了投放点的设备
            logger.info("=====================开始统计设备地区分布");
            statDeviceLocationDao.countDeviceLocation(yesterday);
            //统计未分配投放点的设备
            statDeviceLocationDao.countDeviceWithoutLocation(yesterday);
            logger.info("====================完成统计设备地区分布");

        });

       /* for (int i = 0; i < idList.size(); ++i) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    Integer ownerId = idList.get(index);
                    //根据ownerId获取对应的productIdList
                    setDataForLocationByOwnerId(yesterday, ownerId);
                    logger.info("=======================sysUserId:" + ownerId + "导入设备分布成功");
                } catch (Exception e) {
                    logger.warn("=======================sysUserId:" + idList.get(index) + "导入设备分布失败,原因如下：" + e);
                    e.printStackTrace();
                }
            });
        }*/
    }

    @Override
    public void setDataForLocationByOwnerId(Date yesterday, Integer ownerId) {
        List<Integer> productIdList = deviceDao.getProductId(ownerId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(yesterday);
        Date todayStart = DateUtil.getDayStartTime(new Date());
        for (int k = 0; k < productIdList.size(); ++k) {
            Integer productId = productIdList.get(k);
            //根据sysUserId和productId获取List<省，设备数>
            List<Map<String, Number>> mapDeviceList = deviceLaunchAreaDao.findProvinceAndCount(ownerId, productId);
            Integer total = deviceDao.getTotalBySysUserIdAndIsNotDeleted(ownerId, productId, todayStart);
            if (ownerId == 57) {
                int a = 0;
            }
            for (int j = 0; j < mapDeviceList.size(); ++j) {
                Map<String, Number> mapDevice = mapDeviceList.get(j);
                //如果省名存在
                if (mapDevice.get("province") != null) {
                    StatDeviceLocation statDeviceLocation = selectOne(new EntityWrapper<StatDeviceLocation>().eq("sys_user_id", ownerId).eq("province", mapDevice.get("province"))
                            .like("ctime", time));
                    if (ParamUtil.isNullOrEmptyOrZero(statDeviceLocation)) {
                        statDeviceLocation = new StatDeviceLocation();
                    }
                    statDeviceLocation.setSysUserId(ownerId);
                    statDeviceLocation.setProvince(String.valueOf(mapDevice.get("province")));
                    int deviceCount = mapDevice.get("count").intValue();
                    Double propotion = total != 0 ? (double) deviceCount / total : 0.0;//如果total为0会怎样???
                    statDeviceLocation.setDeviceCount(deviceCount);
                    statDeviceLocation.setCtime(yesterday);
                    statDeviceLocation.setProportion(propotion);
                    statDeviceLocation.setProductId(productId);
                    insertOrUpdate(statDeviceLocation);
                }
            }
            //未分配投放点的设备
            Integer deviceWithoutArea = deviceDao.getDeviceWithoutArea(ownerId, productId);
            if (!ParamUtil.isNullOrEmptyOrZero(deviceWithoutArea)) {
                StatDeviceLocation statDeviceLocation = selectOne(new EntityWrapper<StatDeviceLocation>().eq("sys_user_id", ownerId).eq("province", "其他")
                        .like("ctime", time));
                if (ParamUtil.isNullOrEmptyOrZero(statDeviceLocation)) {
                    statDeviceLocation = new StatDeviceLocation();
                }
                statDeviceLocation.setSysUserId(ownerId);
                statDeviceLocation.setProvince("其他");
                Double propotion = (total != 0 ? (double) deviceWithoutArea / total : 0.0);//如果total为0会怎样???
                statDeviceLocation.setDeviceCount(deviceWithoutArea);
                statDeviceLocation.setCtime(yesterday);
                statDeviceLocation.setProportion(propotion);
                statDeviceLocation.setProductId(productId);
                insertOrUpdate(statDeviceLocation);
            }
        }

    }

    @Override
    public List<StatLocationVo> getDistribution(Integer productId, SysUser currentUser, List<Integer> sysUserIds) {
        if (Objects.isNull(sysUserIds) || sysUserIds.size() == 0 || Objects.isNull(currentUser)) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        Date yesterday = DateKit.addDate(new Date(), -1);
        List<StatDeviceLocation> list = statDeviceLocationDao.getDistributionByIds(ParamUtil.isNullOrZero(productId) ? null : productId, sysUserIds, yesterday);
        if (list.size() == 0) {
            logger.warn("请确定statDeviceLocation中是否有日期为:" + DateKit.getTimestampString(yesterday) + ",当前用户为:" + currentUser.getId() + "，产品id为：" + productId + "的stat_device_location设备分布数据");
        }
        //查询用户拥有的设备总数
        Integer total = deviceService.selectCount(new EntityWrapper<Device>().in("owner_id", sysUserIds)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()).ne("status", 6));
        return turnIntoVoFromEntity(list, false, total);
    }

    @Override
    public List<StatLocationVo> getRank(Integer productId, SysUser currentUser, List<Integer> ids) {
        if (currentUser == null) {
            throw new SystemException(LeaseExceEnums.ENTITY_NOT_EXISTS.getCode(), "请以正确的参数请求");
        }
        Date yesterday = DateKit.addDate(new Date(), -1);
        List<StatDeviceLocation> list = statDeviceLocationDao.getRankByIds(productId, ids, yesterday);
        if (list.size() == 0) {
            logger.warn("请确定statDeviceLocation中是否有日期为:" + DateKit.getTimestampString(yesterday) + ",当前用户为:" + currentUser.getId() + "，产品id为：" + productId + "的stat_device_location设备分布数据");
        }
        Boolean isAll = ParamUtil.isNullOrZero(productId);
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                .in("owner_id", ids).ne("status", 6);
        if (!ParamUtil.isNullOrEmptyOrZero(productId)) {
            entityWrapper.eq("product_id", productId);
        }
        Integer total = deviceService.selectCount(entityWrapper);
        return turnIntoVoFromEntity(list, isAll, total);
    }

    private List<StatLocationVo> turnIntoVoFromEntity(List<StatDeviceLocation> list, Boolean isAll, Integer total) {
        List<StatLocationVo> statLocationVoList = new ArrayList<>();
        StatLocationVo statLocationVo1 = null;
        for (int i = 0; i < list.size(); ++i) {
            StatDeviceLocation statDeviceLocation = list.get(i);
            if (statDeviceLocation.getProvince().indexOf("其他") >= 0) {
                statLocationVo1 = new StatLocationVo();
                statLocationVo1.setProvince(statDeviceLocation.getProvince());
                statLocationVo1.setCount(statDeviceLocation.getDeviceCount());
                Double proportion = (ParamUtil.isNullOrZero(total) ? 0.0 : (double) statDeviceLocation.getDeviceCount() / total);
                proportion = new BigDecimal(proportion).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                statLocationVo1.setProportion(proportion);
                continue;
            }
            StatLocationVo statLocationVo = new StatLocationVo();
            statLocationVo.setProvince(statDeviceLocation.getProvince());
            statLocationVo.setCount(statDeviceLocation.getDeviceCount());
            Double proportion = (ParamUtil.isNullOrZero(total) ? 0.0 : (double) statDeviceLocation.getDeviceCount() / total);
            proportion = new BigDecimal(proportion).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            statLocationVo.setProportion(proportion);
           /* if (isAll) {
                statLocationVo.setProportion((double) statDeviceLocation.getDeviceCount() / total);
            }*/
            statLocationVoList.add(statLocationVo);
        }
        //将其他地区放在最后
        if (!ParamUtil.isNullOrEmptyOrZero(statLocationVo1)) {
            statLocationVoList.add(statLocationVo1);
        }
        return statLocationVoList;
    }
}
