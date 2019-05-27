package com.gizwits.lease.stat.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.stat.dao.StatFaultAlertTypeDao;
import com.gizwits.lease.stat.dto.StatFaultDto;
import com.gizwits.lease.stat.entity.StatFaultAlertType;
import com.gizwits.lease.stat.service.StatFaultAlertTypeService;
import com.gizwits.lease.stat.vo.StatFaultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gagi
 * @since 2017-08-15
 */
@Service
public class StatFaultAlertTypeServiceImpl extends ServiceImpl<StatFaultAlertTypeDao, StatFaultAlertType> implements StatFaultAlertTypeService {
    protected static Logger logger = LoggerFactory.getLogger(StatFaultAlertTypeServiceImpl.class);
    @Autowired
    private StatFaultAlertTypeDao statFaultAlertTypeDao;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDataPointService productDataPointService;
    @Autowired
    private DeviceAlarmService deviceAlarmService;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public List<StatFaultVo> getDataForStat(StatFaultDto statFaultDto, SysUser currentUser, List<Integer> ids) {
        if (Objects.isNull(currentUser) || Objects.isNull(ids) || ids.size() == 0) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        List<StatFaultAlertType> list = statFaultAlertTypeDao.getDataForStat(statFaultDto, ids);
        return turnIntoVo(list);
    }

    /**
     * 将StatFaultAlertType转换成Vo
     *
     * @param list
     * @return
     */
    private List<StatFaultVo> turnIntoVo(List<StatFaultAlertType> list) {
        List<StatFaultVo> resList = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            StatFaultAlertType source = list.get(i);
            resList.add(new StatFaultVo(source.getRemark(), source.getCount()));
        }
        return resList;
    }


    @Override
    public void setDataForType() {
        Date now = new Date();
        logger.info("===============售后类型频次统计开始" + DateKit.getTimestampString(now));
        //先查询所有的设备
        List<Device> deviceList = deviceService.selectList(new EntityWrapper<Device>().eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        //查询产品对应的alert,fault数据点
        Map<Integer, List<ProductDataPoint>> map = getProductDataPoint();
        //在根据设备对应的数据点查询deviceAlarm表中attr的数量
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < deviceList.size(); ++i) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    Device device = deviceList.get(index);
                    List<ProductDataPoint> productDataPointList = map.get(device.getProductId());
                    if(productDataPointList == null || productDataPointList.isEmpty()) {
                        logger.error("===================>设备:" + deviceList.get(index).getSno() + "没有对应的数据点（可能已被删除）");
                        return;
                    }
                    for (int j = 0; j < productDataPointList.size(); ++j) {
                        StatFaultAlertType statFaultAlertType = new StatFaultAlertType();
                        ProductDataPoint productDataPoint = productDataPointList.get(j);
                        int count = deviceAlarmService.selectCount(new EntityWrapper<DeviceAlarm>().eq("attr", productDataPoint.getIdentityName()).eq("sno", device.getSno()));
                        //如果更新不成功
                        int updateResult = statFaultAlertTypeDao
                                .updateByCtimeAndSysUserIdAndSno(now, device.getOwnerId(), device.getSno(), productDataPoint.getIdentityName(), count);
                        if (updateResult <= 0) {
                            statFaultAlertType.setCount(count);
                            statFaultAlertType.setSysUserId(device.getOwnerId());
                            statFaultAlertType.setCtime(now);
                            statFaultAlertType.setIdentityName(productDataPoint.getIdentityName());
                            statFaultAlertType.setSno(device.getSno());
                            statFaultAlertType.setRemark(productDataPoint.getRemark());
                            statFaultAlertType.setShowName(productDataPoint.getShowName());
                            statFaultAlertType.setProductId(device.getProductId());
                            insert(statFaultAlertType);
                        }
                    }
                } catch (Exception e) {
                    logger.error("===================>设备:" + deviceList.get(index).getSno() + "错误：" + e);
                }
            });
        }
    }

    /**
     * 查询所有产品对应的警告数据点
     *
     * @return
     */
    public Map<Integer, List<ProductDataPoint>> getProductDataPoint() {
        List<Product> productList = productService.getAllUseableProduct();
        List<String> type = new ArrayList<>();
        type.add("alert");
        type.add("fault");
        Map<Integer, List<ProductDataPoint>> map = new HashMap<>(productList.size());
        for (int i = 0; i < productList.size(); ++i) {
            Product product = productList.get(i);
            List<ProductDataPoint> pdpList = productDataPointService.selectList(new EntityWrapper<ProductDataPoint>().in("read_write_type", type).eq("product_id", product.getId()));
            if (Objects.nonNull(pdpList) && pdpList.size() > 0) {
                map.put(product.getId(), pdpList);
            }
        }
        return map;
    }
}
