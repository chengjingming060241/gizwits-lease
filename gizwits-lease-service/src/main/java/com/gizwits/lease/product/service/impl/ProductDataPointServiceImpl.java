package com.gizwits.lease.product.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.constant.DeviceAlarmRankEnum;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.entity.dto.DeviceAlarmRankDto;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.enums.ReadWriteType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.dao.ProductDataPointDao;
import com.gizwits.lease.product.dto.ProductDataPointQueryDto;
import com.gizwits.lease.product.dto.ProductdataPointUpdateDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.resolver.GizwitsDataPointResolver;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 产品数据点 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@Service
public class ProductDataPointServiceImpl extends ServiceImpl<ProductDataPointDao, ProductDataPoint> implements ProductDataPointService {

    protected Logger product_logger = LoggerFactory.getLogger("PRODUCT_LOGGER");
    protected Logger device_logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private GizwitsDataPointResolver gizwitsDataPointResolver;

    @Autowired
    private ProductDataPointDao productDataPointDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @Override
    public Page<ProductDataPoint> getDataPointByPage(Integer productId, Page<ProductDataPoint> page) {
        if (!hasRecord(productId)) {
            gizwitsDataPointResolver.resolve(productId, true);
        }
        return selectPage(page, new EntityWrapper<ProductDataPoint>().eq("product_id", productId));
    }

    public List<ProductDataPoint> getProdcutAllDataPoint(String productIdOrKey){
        EntityWrapper<Product> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("id",productIdOrKey).or().eq("gizwits_product_key",productIdOrKey);

        Product product = productService.selectOne(entityWrapper);
        if(Objects.isNull(product)){
            product_logger.error("====获取产品{}不存在======",productIdOrKey);
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        EntityWrapper<ProductDataPoint> dataPointEntityWrapper = new EntityWrapper<>();
        dataPointEntityWrapper.eq("product_id",product.getId());
        return selectList(dataPointEntityWrapper);
    }

    @Override
    public Page<ProductDataPoint> sync(Integer productId) {
        gizwitsDataPointResolver.sync(productId, hasRecord(productId));
        return selectPage(new Page<>(), new EntityWrapper<ProductDataPoint>().eq("product_id", productId));
    }

    private boolean hasRecord(Integer productId) {
        return selectCount(new EntityWrapper<ProductDataPoint>().eq("product_id", productId)) > 0;
    }
    public Page<DeviceAlarmRankDto> getDeviceAlarmRankDtoPage(Pageable<ProductDataPointQueryDto> pageable) {
        ProductDataPointQueryDto productDataPointQueryDto = pageable.getQuery();
        List<Integer> productIds = new ArrayList<>();
        if(ParamUtil.isNullOrEmptyOrZero(productDataPointQueryDto) || ParamUtil.isNullOrEmptyOrZero(productDataPointQueryDto.getProductId())){
            List<Product> products = productService.getProductsWithPermission();
            for(Product p:products){
                productIds.add(p.getId());
            }
        }else {
            Integer productId = productDataPointQueryDto.getProductId();
            productIds.add(productId);
        }
        product_logger.info("查看一下产品id的告警："+productIds.toString());
        Page<ProductDataPoint> page = new Page<>();
        BeanUtils.copyProperties(pageable,page);
        EntityWrapper<ProductDataPoint> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("product_id",productIds)
                .in("read_write_type", Arrays.asList("fault","alert"));
        Page<ProductDataPoint> page1 = selectPage(page,
                QueryResolverUtils.parse(pageable.getQuery(),entityWrapper));
        List<DeviceAlarmRankDto> deviceAlarmRankDtoList = new ArrayList<>();
        List<ProductDataPoint> productDataPoints = page1.getRecords();
        for(ProductDataPoint p:productDataPoints){
            DeviceAlarmRankDto deviceAlarmRankDto = new DeviceAlarmRankDto();
            deviceAlarmRankDto.setId(p.getId());
            Integer rank =1;
            if(!ParamUtil.isNullOrEmptyOrZero(p.getDeviceAlarmRank())){
                 rank = p.getDeviceAlarmRank();
            }
            deviceAlarmRankDto.setRank(rank);
            deviceAlarmRankDto.setAlarmRank( DeviceAlarmRankEnum.get(rank).getValue());
            deviceAlarmRankDto.setDataType(p.getDataType());
            deviceAlarmRankDto.setIdetitfyName( p.getIdentityName());
            deviceAlarmRankDto.setReadWriteType(ReadWriteType.getDesc(p.getReadWriteType()));
            deviceAlarmRankDto.setRemark(p.getRemark());
            deviceAlarmRankDto.setShowName(p.getShowName());
            deviceAlarmRankDto.setProductId(p.getProductId());
            deviceAlarmRankDtoList.add(deviceAlarmRankDto);
        }
        Page<DeviceAlarmRankDto> result = new Page<>();
        BeanUtils.copyProperties(page1,result);
        result.setRecords(deviceAlarmRankDtoList);
        return result;
    }

    @Override
    public ProductDataPoint getProductDataPointByProductIdAndIdentityName(Device device, DeviceAlarm deviceAlarm) {
        EntityWrapper<ProductDataPoint> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("product_id", device.getProductId())
                .eq("identity_name", deviceAlarm.getAttr());
        return selectOne(entityWrapper);
    }

    public List<ProductDataPoint> getMonitDataPoint(String productKey){
        if(StringUtils.isEmpty(productKey)){
            return null;
        }
        return productDataPointDao.findAllMonitPoint(productKey);
    }

    @Override
    public List<ProductDataPoint> list(Integer productId) {
        return selectList(new EntityWrapper<ProductDataPoint>().eq("product_id", productId));
    }

    @Override
    public void updateProductDataPointByRank(ProductdataPointUpdateDto productdataPointUpdateDto) {
        product_logger.info("更新数据点的告警级别：id="+productdataPointUpdateDto.getId()+",级别："+productdataPointUpdateDto.getRank());
        ProductDataPoint productdataPoint = selectById(productdataPointUpdateDto.getId());
        if(productdataPoint != null){
            productdataPoint.setDeviceAlarmRank(productdataPointUpdateDto.getRank());
            updateById(productdataPoint);
            DeviceAlarm deviceAlarm = deviceAlarmService.getDeviceAlarmByProductDataPoint(productdataPoint);
            if(Objects.nonNull(deviceAlarm)) {
                device_logger.info(" 设备故障——序列号：" + deviceAlarm.getSno() + "，mac:" + deviceAlarm.getMac());
                deviceAlarmService.sendDeviceAlarmMessage(deviceAlarm);
            }
        }

    }
}
