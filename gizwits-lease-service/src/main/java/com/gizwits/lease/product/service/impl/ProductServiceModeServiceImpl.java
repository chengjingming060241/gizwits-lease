package com.gizwits.lease.product.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.constant.CommandType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ServiceType;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.dao.ProductServiceModeDao;
import com.gizwits.lease.product.dto.PriceAndNumDto;
import com.gizwits.lease.product.dto.ProductServiceDetailDto;
import com.gizwits.lease.product.dto.ProductServiceListQuerytDto;
import com.gizwits.lease.product.dto.ProductServiceModeForAddPageDto;
import com.gizwits.lease.product.dto.ProductServicecModeListDto;
import com.gizwits.lease.product.dto.ServiceTypeDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 产品(或者设备)服务方式 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@Service
public class ProductServiceModeServiceImpl extends ServiceImpl<ProductServiceModeDao, ProductServiceMode> implements ProductServiceModeService {

    @Autowired
    private ProductService productService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductServiceDetailService productServiceDetailService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private ProductServiceModeDao productServiceModeDao;

    @Override
    public List<ServiceTypeDto> getServiceTypeByProductId(Integer ProductId) {
        EntityWrapper<ProductServiceMode> entity = new EntityWrapper<>();
        entity.eq("product_id", ProductId);
        List<ServiceTypeDto> serviceTypes = new ArrayList<>();
        List<ProductServiceMode> productServiceModes = selectList(entity);
        for (ProductServiceMode productServiceMode : productServiceModes) {
            ServiceTypeDto serviceTypeDto = new ServiceTypeDto();
            serviceTypeDto.setServiceType(productServiceMode.getServiceType());
            serviceTypeDto.setUnit(productServiceMode.getUnit());
            serviceTypes.add(serviceTypeDto);
        }
        return serviceTypes;
    }

    @Override
    public String deleteProductServiceModeById(List<Integer> productServiceModeIds) {
        SysUser current = sysUserService.getCurrentUser();
        List<Integer> userIds = sysUserService.resolveOwnerAccessableUserIds(current);
        StringBuilder sb = new StringBuilder("删除收费模式后，若设备不存在其他的收费模式，则设备将无法租赁");
        Date now = new Date();
        List<ProductServiceMode> serviceModes = selectBatchIds(productServiceModeIds);
        List<String> fails = new LinkedList<>();
        if (!ParamUtil.isNullOrEmptyOrZero(serviceModes)) {
            for (ProductServiceMode serviceMode : serviceModes) {
                if (userIds.contains(serviceMode.getSysUserId())) {
                    //置空设备的收费模式
                    EntityWrapper<Device> entity = new EntityWrapper<>();
                    entity.eq("service_id", serviceMode.getId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
                    List<Device> devices = deviceService.selectList(entity);
                    if (!ParamUtil.isNullOrEmptyOrZero(devices)) {
                        for (Device device : devices) {
                            device.setServiceId(null);
                            device.setServiceName(null);
                            device.setUtime(now);
                            deviceService.updateAllColumnById(device);
                        }
                    }
                    //删除收费模式详情
                    productServiceDetailService.deleteBatchByModeId(Collections.singletonList(serviceMode.getId()));
                    serviceMode.setUtime(now);
                    serviceMode.setIsDeleted(DeleteStatus.DELETED.getCode());
                    updateById(serviceMode);
                    // 删除收费模式
                    serviceMode.setUtime(now);
                    deleteById(serviceMode);
                } else {
                    fails.add(serviceMode.getName());
                }
            }
        }

        return sb.toString();
    }


    @Override
    public List<ProductServiceModeForAddPageDto> getAddServiceModePageData() {
        List<Product> products = productService.getProductsWithPermission();
        List<ProductServiceModeForAddPageDto> result = new LinkedList<>();
        if (!ParamUtil.isNullOrEmptyOrZero(products)) {
            for (Product product : products) {
                EntityWrapper<ProductCommandConfig> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())
                        .eq("command_type", CommandType.SERVICE.getCode());
                entityWrapper.eq("product_id", product.getId());
                List<ProductCommandConfig> serviceCommandList = productCommandConfigService.selectList(entityWrapper);
                result.add(new ProductServiceModeForAddPageDto(product, serviceCommandList));
            }
        }

        return result;
    }

    @Override
    public boolean judgeProductServiceModeIsExist(String serviceMode) {
        SysUser sysUser = sysUserService.getCurrentUser();
        EntityWrapper<ProductServiceMode> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("name", serviceMode)
                .eq("sys_user_id", sysUser.getId())
                .eq("status", 1)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        ProductServiceMode productServiceMode = selectOne(entityWrapper);
        return !ParamUtil.isNullOrEmptyOrZero(productServiceMode);

    }

    /**
     * 获取收费模式的单位
     */
    public String getServiceModeUnit(ProductServiceMode serviceMode) {
        if (serviceMode == null || ParamUtil.isNullOrEmptyOrZero(serviceMode.getServiceTypeId()) || ParamUtil.isNullOrEmptyOrZero(serviceMode.getServiceType())) {
            LeaseException.throwSystemException(LeaseExceEnums.SERVICE_MODE_CONFIG_ERROR);
        }
        //免费
        if (serviceMode.getServiceType().indexOf(ServiceType.FREE.getDesc()) >= 0) {
            return "";
        }
        ProductCommandConfig commandConfig = productCommandConfigService.selectById(serviceMode.getServiceTypeId());
        if (commandConfig == null) {
            LeaseException.throwSystemException(LeaseExceEnums.SERVICE_MODE_CONFIG_ERROR);
        }

        return productCommandConfigService.getSpecialDisplayUnit(commandConfig);
    }

    @Override
    public void updateProductServiceMode(ProductServicecModeListDto productServicecModeListDto, SysUser sysUser) {
        //更新收费模式表中数据
        ProductCommandConfig commandConfig = productCommandConfigService.selectById(productServicecModeListDto.getServiceTypeId());

        ProductServiceMode productServiceMode = selectOne(new EntityWrapper<ProductServiceMode>().eq("id", productServicecModeListDto.getId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (ParamUtil.isNullOrEmptyOrZero(productServiceMode)) {
            LeaseException.throwSystemException(LeaseExceEnums.SERVICE_MODE_NOT_EXIST);
        }
        productServiceMode.setUtime(new Date());
        String mode = productServicecModeListDto.getServiceMode();
        if (ParamUtil.isNullOrEmptyOrZero(mode)) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_PARAM.getCode(), SysExceptionEnum.ILLEGAL_PARAM.getMessage());
        }
        BeanUtils.copyProperties(productServicecModeListDto, productServiceMode);
        String oldMode = productServiceMode.getName();
        productServiceMode.setName(mode);
//        productServiceMode.setServiceType(productServicecModeListDto.getServiceType());
//        productServiceMode.setServiceTypeId(productServicecModeListDto.getServiceTypeId());
//        productServiceMode.setProductId(productServicecModeListDto.getProductId());
        productServiceMode.setSysUserId(sysUser.getId());
        productServiceMode.setSysUserName(sysUser.getUsername());
        productServiceMode.setStatus(1);
        productServiceMode.setCommand(productCommandConfigService.getDeviceModelCommand(commandConfig));
        productServiceMode.setIsFree(commandConfig.getIsFree());
//        productServiceMode.setWorkingMode(commandConfig.getWorkingMode());
//        productServiceMode.setUnitPrice(productServicecModeListDto.getUnitPrice());

        if (productServicecModeListDto.getServiceType().indexOf(ServiceType.FREE.getDesc()) >= 0) {
            productServiceMode.setUnit(null);
            productServiceDetailService.deleteByServiceModeId(productServicecModeListDto.getId());
            updateAllColumnById(productServiceMode);
            return;
        } else {
            productServiceMode.setUnit(productServicecModeListDto.getUnit());
        }

        updateAllColumnById(productServiceMode);
        if (!oldMode.equals(mode)) {
            NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(productServiceMode, productServiceMode.getId(), oldMode, mode);
            publishEvent(nameModifyEvent);
        }
        // 删除原来的收费模式详情
        productServiceDetailService.deleteBatchByModeId(Collections.singletonList(productServiceMode.getId()));
        // 更新收费详情表中数据
        List<PriceAndNumDto> priceAndNumDtos = productServicecModeListDto.getPriceAndNumDtoList();
        if (!ParamUtil.isNullOrEmptyOrZero(priceAndNumDtos)) {
            for (PriceAndNumDto p : priceAndNumDtos) {
                Integer id = p.getId();
                Double num = p.getNum();
                Double price = p.getPrice();
                ProductServiceDetailDto productServiceDetailDto = new ProductServiceDetailDto();
                productServiceDetailDto.setServiceModeId(productServicecModeListDto.getId());
                productServiceDetailDto.setProductId(productServicecModeListDto.getProductId());
                //免费模式
                productServiceDetailDto.setServiceType("免费");
                productServiceDetailDto.setSysUserId(sysUser.getId());
                boolean flag = productServiceDetailService.judgeProductServiceDetailIsExist(productServiceDetailDto);
                if (!flag) {
                    ProductServiceDetail productPriceDetail = new ProductServiceDetail();
                    productPriceDetail.setCtime(new Date());
                    productPriceDetail.setUtime(new Date());
                    productPriceDetail.setServiceModeId(productServicecModeListDto.getId());
                    productPriceDetail.setProductId(productServicecModeListDto.getProductId());
                    productPriceDetail.setServiceType(productServicecModeListDto.getServiceType());
                    productPriceDetail.setServiceTypeId(productServicecModeListDto.getServiceTypeId());
                    productPriceDetail.setCommand(productCommandConfigService.getCommandByConfig(commandConfig, p.getNum()));
                    productPriceDetail.setSysUserId(sysUser.getId());
                    productPriceDetail.setPrice(p.getPrice());
                    productPriceDetail.setNum(p.getNum());
                    productPriceDetail.setSysUserName(sysUser.getUsername());
                    productPriceDetail.setUnit(productServicecModeListDto.getUnit());
                    productPriceDetail.setNormalPrice(p.getNormalPrice());
                    productPriceDetail.setNormalNum(p.getNormalNum());
                    productPriceDetail.setColdPrice(p.getColdPrice());
                    productPriceDetail.setColdNum(p.getColdNum());
                    productPriceDetail.setWarmPrice(p.getWarmPrice());
                    productPriceDetail.setWarmNum(p.getWarmNum());

                    productServiceDetailService.insert(productPriceDetail);
                }
            }
        }
    }

    private void publishEvent(NameModifyEvent<Integer> nameModifyEvent) {
        CommonEventPublisherUtils.publishEvent(nameModifyEvent);
    }

    @Override
    public void addProductServiceMode(SysUser sysUser, ProductServicecModeListDto productServiceModeListDto) {
        ProductCommandConfig commandConfig = productCommandConfigService.selectById(productServiceModeListDto.getServiceTypeId());

        //添加收费模式数据
        ProductServiceMode productServiceMode = new ProductServiceMode();
        productServiceMode.setCtime(new Date());
        productServiceMode.setUtime(new Date());
        String mode = productServiceModeListDto.getServiceMode();
        if (ParamUtil.isNullOrEmptyOrZero(mode)) {
            throw new com.gizwits.boot.exceptions.SystemException(SysExceptionEnum.ILLEGAL_PARAM.getCode(), SysExceptionEnum.ILLEGAL_PARAM.getMessage());
        }
        productServiceMode.setName(mode);
        productServiceMode.setServiceType(productServiceModeListDto.getServiceType());
        productServiceMode.setProductId(productServiceModeListDto.getProductId());
        productServiceMode.setUnit(productServiceModeListDto.getUnit());
        productServiceMode.setServiceTypeId(productServiceModeListDto.getServiceTypeId());
        productServiceMode.setSysUserId(sysUser.getId());
        productServiceMode.setSysUserName(sysUser.getUsername());
        productServiceMode.setCommand(productCommandConfigService.getDeviceModelCommand(commandConfig));
        productServiceMode.setIsFree(commandConfig.getIsFree());
        productServiceMode.setWorkingMode(commandConfig.getWorkingMode());

        if (productServiceModeListDto.getServiceType().indexOf(ServiceType.FREE.getDesc()) >= 0) {
            productServiceMode.setUnit(" ");
        }


        insert(productServiceMode);


        EntityWrapper<ProductServiceMode> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_user_id", sysUser.getId())
                .eq("name", productServiceModeListDto.getServiceMode());
        Integer serviceModeId = selectOne(entityWrapper).getId();


        List<PriceAndNumDto> priceAndNumDtos = productServiceModeListDto.getPriceAndNumDtoList();
        if (commandConfig != null && !ParamUtil.isNullOrEmptyOrZero(priceAndNumDtos)) {
            //添加收费详情数据
            for (PriceAndNumDto p : priceAndNumDtos) {
                ProductServiceDetail productPriceDetail = new ProductServiceDetail();
                productPriceDetail.setCtime(new Date());
                productPriceDetail.setUtime(new Date());
                productPriceDetail.setServiceModeId(serviceModeId);
                productPriceDetail.setProductId(productServiceModeListDto.getProductId());
                productPriceDetail.setServiceType(productServiceModeListDto.getServiceType());
                productPriceDetail.setServiceTypeId(productServiceModeListDto.getServiceTypeId());
                productPriceDetail.setPrice(p.getPrice());
                productPriceDetail.setNum(p.getNum());
                productPriceDetail.setUnit(productServiceModeListDto.getUnit());
                productPriceDetail.setSysUserId(sysUser.getId());
                productPriceDetail.setSysUserName(sysUser.getUsername());
                //设置指令
                productPriceDetail.setCommand(productCommandConfigService.getCommandByConfig(commandConfig, p.getNum()));
                productPriceDetail.setNormalPrice(p.getNormalPrice());
                productPriceDetail.setNormalNum(p.getNormalNum());
                productPriceDetail.setColdPrice(p.getColdPrice());
                productPriceDetail.setColdNum(p.getColdNum());
                productPriceDetail.setWarmPrice(p.getWarmPrice());
                productPriceDetail.setWarmNum(p.getWarmNum());

                productServiceDetailService.insert(productPriceDetail);
            }
        }
    }

    public Page<ProductServicecModeListDto> getProductServiceModeListPage(Pageable<ProductServiceListQuerytDto> pageable) {
        Page<ProductServiceMode> page = new Page<>();
//        page.setOrderByField("utime");
        EntityWrapper<ProductServiceMode> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("status", 1)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        entityWrapper.orderBy("utime", false);
        BeanUtils.copyProperties(pageable, page);
        Page<ProductServiceMode> page1 = selectPage(
                page, QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
        List<ProductServiceMode> productServiceModes = page1.getRecords();
        Page<ProductServicecModeListDto> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        List<ProductServicecModeListDto> productServicecModeListDtos = new ArrayList<ProductServicecModeListDto>();

        for (ProductServiceMode psm : productServiceModes) {
            ProductServicecModeListDto productServicecModeListDto = new ProductServicecModeListDto();
            productServicecModeListDto.setId(psm.getId());
            productServicecModeListDto.setServiceMode(psm.getName());
            productServicecModeListDto.setServiceType(psm.getServiceType());
            productServicecModeListDto.setServiceTypeId(psm.getServiceTypeId());
            productServicecModeListDto.setProductId(psm.getProductId());
            productServicecModeListDto.setUnit(psm.getUnit());
            Product product = productService.selectOne(new EntityWrapper<Product>()
                    .eq("id", psm.getProductId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            if (!Objects.isNull(product)) {
                productServicecModeListDto.setProduct(product.getName());
            }
            productServicecModeListDto.setuTime(psm.getUtime());
            productServicecModeListDto.setcTime(psm.getCtime());


            //添加价格和数量
            List<PriceAndNumDto> priceAndNumDtos = productServiceDetailService.
                    getProductPriceDetailByServiceModeId(psm.getId());
            productServicecModeListDto.setPriceAndNumDtoList(priceAndNumDtos);

            // 统计使用该收费模式的设备数
            productServicecModeListDto.setDeviceCount(deviceService.countByServiceId(psm.getId()));
            productServicecModeListDtos.add(productServicecModeListDto);

        }

        result.setRecords(productServicecModeListDtos);
        return result;
    }

    @Override
    public Page<ProductServicecModeListDto> getProductServicecModeListDtoPage(Pageable<ProductServiceListQuerytDto> pageable) {
        SysUser sysUser = sysUserService.getCurrentUser();
        Integer userId = sysUser.getId();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new ProductServiceListQuerytDto());
        }
        pageable.getQuery().setCreatorId(userId);
        return getProductServiceModeListPage(pageable);
    }

    @Override
    public ProductServicecModeListDto getProductServiceModeDetail(Integer user_id, Integer serviceModeId) {
        ProductServiceMode productServiceMode = productServiceModeDao.selectById(serviceModeId);
        if (!ParamUtil.isNullOrEmptyOrZero(productServiceMode)) {
            ProductServicecModeListDto productServicecModeListDto = new ProductServicecModeListDto();
            productServicecModeListDto.setId(productServiceMode.getId());
            productServicecModeListDto.setServiceMode(productServiceMode.getName());
            productServicecModeListDto.setServiceType(productServiceMode.getServiceType());
            productServicecModeListDto.setServiceTypeId(productServiceMode.getServiceTypeId());
            productServicecModeListDto.setProductId(productServiceMode.getProductId());
            productServicecModeListDto.setUnit(productServiceMode.getUnit());
            productServicecModeListDto.setProduct(productService.selectById(productServiceMode.getProductId()).getName());
            productServicecModeListDto.setuTime(productServiceMode.getUtime());
            productServicecModeListDto.setcTime(productServiceMode.getCtime());

            //添加价格和数量
            List<PriceAndNumDto> priceAndNumDtos = productServiceDetailService.
                    getProductPriceDetailByServiceModeId(productServiceMode.getId());
            productServicecModeListDto.setPriceAndNumDtoList(priceAndNumDtos);

            // 统计使用该收费模式的设备数
            productServicecModeListDto.setDeviceCount(deviceService.countByServiceId(productServiceMode.getId()));

            return productServicecModeListDto;
        }

        return null;
    }

    @Override
    public ProductServiceMode getProductServiceMode(Integer serviceModeId) {
        return selectOne(new EntityWrapper<ProductServiceMode>().eq("id", serviceModeId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
    }
}


