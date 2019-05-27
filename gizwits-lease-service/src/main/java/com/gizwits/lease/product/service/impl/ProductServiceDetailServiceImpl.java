package com.gizwits.lease.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.enums.DeleteStatus;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.product.dao.ProductServiceDetailDao;
import com.gizwits.lease.product.dto.AppServiceModeDetailDto;
import com.gizwits.lease.product.dto.PriceAndNumDto;
import com.gizwits.lease.product.dto.ProductServiceDetailDto;
import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import com.gizwits.lease.product.vo.AppProductServiceDetailVo;
import com.gizwits.lease.product.vo.ProductServiceDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 产品(或者设备)收费价格详情服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-13
 */
@Service
public class ProductServiceDetailServiceImpl extends ServiceImpl<ProductServiceDetailDao, ProductServiceDetail> implements ProductServiceDetailService {
    @Autowired
    private ProductServiceDetailDao productServiceDetailDao;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProductServiceModeService productServiceModeService;
    @Autowired
    private OperatorService operatorService;

    @Override
    public List<PriceAndNumDto> getProductPriceDetailByServiceModeId(Integer serviceModeId) {
        EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("service_mode_id", serviceModeId).eq("status", 1).eq("is_deleted", 0);
        List<PriceAndNumDto> priceAndNumDtos = new ArrayList<>();
        for (ProductServiceDetail p : selectList(entityWrapper)) {
            PriceAndNumDto priceAndNumDto = new PriceAndNumDto();
            priceAndNumDto.setId(p.getId());
            priceAndNumDto.setNum(p.getNum());
            priceAndNumDto.setPrice(p.getPrice());
            priceAndNumDto.setNormalPrice(p.getNormalPrice());
            priceAndNumDto.setNormalNum(p.getNormalNum());
            priceAndNumDto.setColdPrice(p.getColdPrice());
            priceAndNumDto.setColdNum(p.getColdNum());
            priceAndNumDto.setWarmPrice(p.getWarmPrice());
            priceAndNumDto.setWarmNum(p.getWarmNum());

            priceAndNumDtos.add(priceAndNumDto);
        }
        return priceAndNumDtos;
    }

    @Override
    public List<ProductServiceDetail> getProductServiceDetailByServiceModelId(Integer serviceModeId) {
        EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("service_mode_id", serviceModeId).eq("status", 1).eq("is_deleted", 0);
        return selectList(entityWrapper);
    }

    @Override
    public boolean deleteByServiceModeId(Integer serviceModeId) {
        EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("service_mode_id", serviceModeId);
        List<ProductServiceDetail> productPriceDetails = selectList(entityWrapper);
        for (ProductServiceDetail p : productPriceDetails) {
            p.setServiceModeId(0);
            p.setIsDeleted(1);
            p.setUtime(new Date());
            updateById(p);
        }
        return true;
    }

    @Override
    public boolean deleteBatchByModeId(List<Integer> serviceModeIds) {
        EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("service_mode_id", serviceModeIds);
        ProductServiceDetail detail = new ProductServiceDetail();
        detail.setIsDeleted(DeleteStatus.DELETED.getCode());
        detail.setUtime(new Date());
        return update(detail, entityWrapper);
    }

    @Override
    public boolean deleteByIds(List<Integer> id) {
        EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("id", id);
        List<ProductServiceDetail> productServiceDetails = selectList(entityWrapper);
        for (ProductServiceDetail productServiceDetail : productServiceDetails) {
            productServiceDetail.setStatus(0);
            productServiceDetail.setIsDeleted(1);
            productServiceDetail.setUtime(new Date());
            updateById(productServiceDetail);
        }

        return true;
    }

    @Override
    public boolean judgeProductServiceDetailIsExist(ProductServiceDetailDto pDto) {
        EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("service_mode_id", pDto.getServiceModeId())
                .eq("product_id", pDto.getProductId())
                .eq("service_type", pDto.getServiceType())
                .eq("sys_user_id", pDto.getSysUserId())
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        ProductServiceDetail productServiceDetail = selectOne(entityWrapper);
        return !ParamUtil.isNullOrEmptyOrZero(productServiceDetail);
    }

    @Override
    public Double getMinPrice(Integer serviceModeId) {
        ProductServiceDetail psd = productServiceDetailDao.getMinPriceDetailByServiceModeId(serviceModeId);
        if (Objects.isNull(psd)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }
        Double price = psd.getPrice();
        Double num = psd.getNum();
        if (ParamUtil.isNullOrZero(price) || ParamUtil.isNullOrZero(num)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_FREE);
        }
        double res = (double) price / num;
        BigDecimal temp = new BigDecimal(res);
        double minPrice = temp.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return minPrice;
    }

    @Override
    public Double getNormalMinPrice(Integer serviceModeId) {
        ProductServiceDetail psd = productServiceDetailDao.getMinPriceDetailByServiceModeId(serviceModeId);

        if (Objects.isNull(psd)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }
        Double price = psd.getNormalPrice();
        Double num = psd.getNormalNum();
        if (ParamUtil.isNullOrZero(price) || ParamUtil.isNullOrZero(num)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_FREE);
        }
        double res = (double) price / num;
        return
                new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public Double getColdMinPrice(Integer serviceModeId) {
        ProductServiceDetail psd = productServiceDetailDao.getMinPriceDetailByServiceModeId(serviceModeId);

        if (Objects.isNull(psd)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }
        Double price = psd.getColdPrice();
        Double num = psd.getColdNum();
        if (ParamUtil.isNullOrZero(price) || ParamUtil.isNullOrZero(num)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_FREE);
        }
        double res = (double) price / num;
        return
                new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public Double getWarmMinPrice(Integer serviceModeId) {
        ProductServiceDetail psd = productServiceDetailDao.getMinPriceDetailByServiceModeId(serviceModeId);

        if (Objects.isNull(psd)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }
        Double price = psd.getWarmPrice();
        Double num = psd.getWarmNum();
        if (ParamUtil.isNullOrZero(price) || ParamUtil.isNullOrZero(num)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_FREE);
        }
        double res = (double) price / num;
        return
                new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public AppProductServiceDetailVo getListForApp(AppServiceModeDetailDto data) {
        //判断该设备对应的sno是否存在
        Device device = deviceService.selectById(data.getSno());
        if (Objects.isNull(device) || device.getIsDeleted().equals(1)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
        }
//        //设备是否空闲
//        if (!DeviceStatus.FREE.getCode().equals(device.getWorkStatus())){
//            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_FREE);
//        }
        //设备是否有运营商
        Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("is_deleted",DeleteStatus.NOT_DELETED.getCode()).eq("sys_account_id", device.getOwnerId()));
        if (Objects.isNull(operator)) {

            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }
        //设备是否有收费模式
        ProductServiceMode productServiceMode = productServiceModeService.selectById(device.getServiceId());
        if (Objects.isNull(productServiceMode)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }

        //设备是否有收费详情
        List<ProductServiceDetail> list = selectList(new EntityWrapper<ProductServiceDetail>().eq("is_deleted",DeleteStatus.NOT_DELETED.getCode()).eq("service_mode_id", productServiceMode.getId()).orderBy("num"));
        if (CollectionUtils.isEmpty(list)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }
        //TODO:如果是免费模式则直接下发指令
        List<ProductServiceDetailVo> resList = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            ProductServiceDetailVo productServiceDetailVo = new ProductServiceDetailVo();
            ProductServiceDetail entity = list.get(i);
            productServiceDetailVo.setUnit(entity.getUnit());

            // 热水
            productServiceDetailVo.setNum(entity.getNum());
            productServiceDetailVo.setPrice(entity.getPrice());

            // 常温
            productServiceDetailVo.setNormalPrice(entity.getNormalPrice());
            productServiceDetailVo.setNormalNum(entity.getNormalNum());

            // 冰水
            productServiceDetailVo.setColdPrice(entity.getColdPrice());
            productServiceDetailVo.setColdNum(entity.getColdNum());

            // 温水
            productServiceDetailVo.setWarmPrice(entity.getWarmPrice());
            productServiceDetailVo.setWarmNum(entity.getWarmNum());

            productServiceDetailVo.setId(entity.getId());
            productServiceDetailVo.setServiceType(entity.getServiceType());
            resList.add(productServiceDetailVo);
        }
        AppProductServiceDetailVo appServiceModeDetailDto = new AppProductServiceDetailVo();
        appServiceModeDetailDto.setList(resList);
        appServiceModeDetailDto.setUnit(productServiceMode.getUnit());
        return appServiceModeDetailDto;
    }


}
