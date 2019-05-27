package com.gizwits.lease.product.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.common.perm.SysUserRoleTypeHelper;
import com.gizwits.lease.common.perm.SysUserRoleTypeHelperResolver;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ProductOperateType;
import com.gizwits.lease.enums.ReadWriteType;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.event.ProductUpdatedEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.product.dao.ProductDao;
import com.gizwits.lease.product.dto.GizwitsDataPointReqDto;
import com.gizwits.lease.product.dto.ManufacturerUserDto;
import com.gizwits.lease.product.dto.PreProductDto;
import com.gizwits.lease.product.dto.ProductCategoryForPullDto;
import com.gizwits.lease.product.dto.ProductDataPointDto;
import com.gizwits.lease.product.dto.ProductDataPointForListDto;
import com.gizwits.lease.product.dto.ProductDataPointForUpdateDto;
import com.gizwits.lease.product.dto.ProductDto;
import com.gizwits.lease.product.dto.ProductForAddDto;
import com.gizwits.lease.product.dto.ProductForDetailDto;
import com.gizwits.lease.product.dto.ProductForListDto;
import com.gizwits.lease.product.dto.ProductForUpdateDto;
import com.gizwits.lease.product.dto.ProductQueryDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCategory;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.resolver.GizwitsDataPointResolver;
import com.gizwits.lease.product.service.ProductCategoryService;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductDeviceChangeService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.service.ProductToPropertiesService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductDao, Product> implements ProductService {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductToPropertiesService productToPropertiesService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private GizwitsDataPointResolver gizwitsDataPointResolver;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ProductDataPointService productDataPointService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private ProductDeviceChangeService productDeviceChangeService;

    @Autowired
    private SysUserRoleTypeHelperResolver helperResolver;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public boolean add(ProductDto productDto) {
        preAdd(productDto);
        Product product = copy(productDto);
        if (this.insert(product)) {
            productDto.setId(product.getId());
            return addOthers(productDto);
        }
        return false;
    }

    @Override
    public Integer add(ProductForAddDto dto) {
        //1.保存产品
        Product product = saveProduct(dto);
        //2.保存数据点
        saveDataPoint(dto, product.getId());
        //3.保存指令
        saveCommand(dto, product.getId());
        //4.发送redis订阅事件
        redisTemplate.convertAndSend("sprinboot-redis-messaage","modify product");

        return product.getId();
    }


    @Override
    public PreProductDto getAddProductPageData() {
        PreProductDto preProductDto = new PreProductDto();
        //类别列表
        preProductDto.setProductCategories(resolveProductCategories());
        //厂商账号
        preProductDto.setManufacturers(resolveManufacturerUsers());
        return preProductDto;
    }

    @Override
    public ProductForDetailDto detail(Integer id) {
        Product product = selectById(id);
        if (Objects.isNull(product)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        ProductForDetailDto result = new ProductForDetailDto(product);
        result.setManufacturerAccountId(product.getManufacturerId());
        result.setManufacturers(resolveManufacturerUsers());
        result.setProductCategories(resolveProductCategories());
        result.setProductDataPoints(productDataPointService.list(id).stream().map(ProductDataPointDto::new).collect(Collectors.toList()));
        result.setDeviceCount(deviceService.selectCount(new EntityWrapper<Device>().eq("product_id", id).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())));
        return result;
    }

    @Override
    public String delete(List<Integer> ids) {
        SysUser current = sysUserService.getCurrentUserOwner();
        List<Integer> userIds = sysUserService.resolveAccessableUserIds(current);
        List<String> fails = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        List<Product> products = selectList(new EntityWrapper<Product>().in("id", ids).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (!ParamUtil.isNullOrEmptyOrZero(products)) {
            for (Product product : products) {
                if (userIds.contains(product.getSysUserId())) {
                    if (deviceService.selectCount(new EntityWrapper<Device>().eq("product_id", product.getId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())) <= 0) {
                        product.setUtime(new Date());
                        product.setIsDeleted(DeleteStatus.DELETED.getCode());
                        updateById(product);
                        //删除产品指令
                        ProductCommandConfig productCommandConfig = new ProductCommandConfig();
                        productCommandConfig.setUtime(new Date());
                        productCommandConfig.setIsDeleted(DeleteStatus.DELETED.getCode());
                        productCommandConfigService.update(productCommandConfig, new EntityWrapper<ProductCommandConfig>().eq("product_id", product.getId()));
                        //删除产品数据点
                        ProductDataPoint productDataPoint = new ProductDataPoint();
                        productDataPoint.setUtime(new Date());
                        productDataPoint.setIsDeleted(DeleteStatus.DELETED.getCode());
                        productDataPointService.update(productDataPoint, new EntityWrapper<ProductDataPoint>().eq("product_id", product.getId()));
                    } else {
                        fails.add(product.getName());
                    }
                } else {
                    fails.add(product.getName());
                }
            }
        }
        /*products.forEach(item -> item.setIsDeleted(1));
        updateBatchById(products);*/
        //这里可以使用事件模型来解ou
        productDataPointService.delete(new EntityWrapper<ProductDataPoint>().in("product_id", ids));
        redisTemplate.convertAndSend("sprinboot-redis-messaage", "modify product");
        switch (fails.size()) {
            case 0:
                sb.append("删除成功");
                break;
            case 1:
                sb.append("您欲删除的产品[" + fails.get(0) + "]已存在设备，请先删除产品下的所有设备。");
                break;
            case 2:
                sb.append("您欲删除的产品[" + fails.get(0) + "],[" + fails.get(1) + "]已存在设备，请先删除产品下的所有设备。");
                break;
            default:
                sb.append("您欲删除的产品[" + fails.get(0) + "],[" + fails.get(1) + "]已存在设备，请先删除产品下的所有设备。");
                break;
        }
        return sb.toString();
    }

    @Override
    public List<Product> getAllUseableProduct() {
        return productDao.findAllUseableProduct();
    }

    @Override
    public List<Product> getProductsWithPermission() {
        //拥有的产品
        SysUser current = sysUserService.getCurrentUser();
        ProductQueryDto query = new ProductQueryDto();
        Integer manufacturerAccountId = resolveManufacturerAccount(current);
        if (Objects.nonNull(manufacturerAccountId)) {//若当前帐号是厂商体系下
            query.setManufacturerAccountId(manufacturerAccountId);
        } else { //当前帐号非厂商体系下，则走数据共享
            query.setAccessableUserIds(sysUserService.resolveAccessableUserIds(current));
        }
        query.setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        return selectList(QueryResolverUtils.parse(query, new EntityWrapper<Product>().orderBy("ctime", true)));
    }

    @Override
    public Page<ProductForListDto> page(Pageable<ProductQueryDto> pageable) {
        Page<Product> page = new Page<>();
        pageable.setOrderByField("ctime");
        pageable.setAsc(false);
        BeanUtils.copyProperties(pageable, page);
        Page<Product> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<ProductForListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
        selectPage.getRecords().forEach(item -> {
            ProductForListDto tmp = new ProductForListDto(item);
            tmp.setDeviceCount(deviceService.selectCount(new EntityWrapper<Device>().eq("product_id", item.getId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())));
            tmp.setProductCategoryName(productCategoryService.selectById(item.getCategoryId()).getName());
            result.getRecords().add(tmp);
        });
        return result;
    }

    @Override
    public ProductDataPointForListDto sync(GizwitsDataPointReqDto req) {
        ProductDataPointForListDto result = gizwitsDataPointResolver.resolve(req);
        //可写的数据点单独分成一组
        result.setWritableDataPoints(result.getDataPoints().stream().filter(item -> Objects.equals(item.getReadWriteType(), ReadWriteType.WRITABLE.getCode())).collect(Collectors.toList()));
        return result;
    }

    @Override
    public boolean update(ProductForUpdateDto dto) {
        Product exist = selectById(dto.getId());
        if (Objects.isNull(exist)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        String oldName = exist.getName();
        BeanUtils.copyProperties(dto, exist);
        exist.setManufacturerId(dto.getManufacturerAccountId());
        updateById(exist);
        updateDataPointIfNecessary(dto);
        publishChangeEvent(dto.getId(), ProductOperateType.BASIC);
        CommonEventPublisherUtils.publishEvent(new ProductUpdatedEvent(exist.getGizwitsProductKey()));
        if (!ParamUtil.isNullOrEmptyOrZero(dto.getName()) && !oldName.equals(dto.getName())) {
            NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(exist, exist.getId(), oldName, dto.getName());
            CommonEventPublisherUtils.publishEvent(nameModifyEvent);
        }

        if (isUpdateNettyConfigParams(exist, dto)) {
            redisTemplate.convertAndSend("sprinboot-redis-messaage", "modify product");
        }
        return true;
    }

    private boolean isUpdateNettyConfigParams(Product dbProduct, ProductForUpdateDto updateDto){
        return !dbProduct.getGizwitsProductSecret().equals(updateDto.getGizwitsAppSecret())
                || !dbProduct.getGizwitsProductSecret().equals(updateDto.getGizwitsAppSecret())
                || !dbProduct.getGizwitsEnterpriseId().equals(updateDto.getGizwitsEnterpriseId())
                || !dbProduct.getGizwitsEnterpriseSecret().equals(updateDto.getGizwitsEnterpriseSecret())
                || !dbProduct.getAuthId().equals(updateDto.getAuthId())
                || !dbProduct.getAuthSecret().equals(updateDto.getAuthSecret());

    }

    @Override
    public void publishChangeEvent(Integer productId, ProductOperateType operateType) {
        //CommonEventPublisherUtils.publishEvent(ProductDeviceChangeEvent.build(productId, operateType, sysUserService.getCurrentUser()));
        productDeviceChangeService.publishChangeEvent(productId, operateType);
    }

    @Override
    public Product getProductByDeviceSno(String sno) {
        Product product = productDao.getProductBySno(sno);
        return product;
    }

    @Override
    public Integer resolveManufacturerAccount(SysUser current) {
        SysUserRoleTypeHelper helper = helperResolver.resolveManufacturer(current);
        if (Objects.nonNull(helper)) {
            return helper.getSysAccountId();
        }
        return null;
    }

    private void updateDataPointIfNecessary(ProductForUpdateDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getDataPoints())) {
            List<ProductDataPoint> dataPoints = productDataPointService.selectBatchIds(dto.getDataPoints().stream().map(ProductDataPointForUpdateDto::getId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(dataPoints)) {
                Map<Integer, Integer> idToMonitMap = dto.getDataPoints().stream().collect(Collectors.toMap(ProductDataPointForUpdateDto::getId, ProductDataPointForUpdateDto::getIsMonit));
                List<ProductDataPoint> needToUpdate = dataPoints.stream().filter(item -> !Objects.equals(item.getIsMonit(), idToMonitMap.get(item.getId()))).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(needToUpdate)) {
                    needToUpdate.forEach(item -> {
                        item.setUtime(new Date());
                        item.setIsMonit(idToMonitMap.get(item.getId()));
                    });
                    productDataPointService.updateBatchById(needToUpdate);
                }
            }
        }
    }

    private List<ManufacturerUserDto> resolveManufacturerUsers() {
        Integer manufacturerRoleId = Integer.parseInt(SysConfigUtils.get(CommonSystemConfig.class).getDefaultManufacturerRoleId());
        List<SysUser> users = sysUserService.getUsersByRoleId(manufacturerRoleId);
        return users.stream().map(ManufacturerUserDto::new).collect(Collectors.toList());
    }

    private List<ProductCategoryForPullDto> resolveProductCategories() {
        List<ProductCategory> productCategories = productCategoryService.list(sysUserService.getCurrentUser());
        return productCategories.stream().map(ProductCategoryForPullDto::new).collect(Collectors.toList());
    }

    private Integer resolveManufacturerAccountId(Integer manufacturerId) {
        Manufacturer manufacturer = manufacturerService.selectById(manufacturerId);
        return Objects.nonNull(manufacturer) ? manufacturer.getSysUserId() : -1;
    }

    private void saveCommand(ProductForAddDto dto, Integer productId) {
        if (CollectionUtils.isNotEmpty(dto.getProductCommands())) {
            List<ProductCommandConfig> commands = dto.getProductCommands().stream().map(item -> {
                ProductCommandConfig command = new ProductCommandConfig();
                BeanUtils.copyProperties(item, command);
                command.setCtime(new Date());
                command.setUtime(command.getCtime());
                command.setProductId(productId);
                return command;
            }).collect(Collectors.toList());

            productCommandConfigService.insertBatch(commands);
        }
    }

    private void saveDataPoint(ProductForAddDto dto, Integer productId) {
        if (CollectionUtils.isNotEmpty(dto.getProductDataPoints())) {
            List<ProductDataPoint> dataPoints = dto.getProductDataPoints().stream().map(item -> {
                ProductDataPoint dataPoint = new ProductDataPoint();
                BeanUtils.copyProperties(item, dataPoint);
                dataPoint.setProductId(productId);
                dataPoint.setCtime(new Date());
                dataPoint.setUtime(dataPoint.getCtime());
                return dataPoint;
            }).collect(Collectors.toList());

            productDataPointService.insertBatch(dataPoints);
        }
    }

    private Product saveProduct(ProductForAddDto dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        product.setManufacturerId(dto.getManufacturerAccountId());
        product.setCtime(new Date());
        product.setUtime(new Date());
        product.setSubkey(RandomStringUtils.random(32, true, false));
        product.setEvents(SysConfigUtils.get(CommonSystemConfig.class).getDefaultGizwitsEvent());
        product.setSysUserId(sysUserService.getCurrentUser().getId());
        insert(product);
        return product;
    }

    private Integer resolveManufacturer(Integer manufacturerAccountId) {
        Manufacturer manufacturer = manufacturerService.selectOne(new EntityWrapper<Manufacturer>().eq("sys_user_id", manufacturerAccountId));
        return Objects.nonNull(manufacturer) ? manufacturer.getId() : -1;
    }

    private boolean addOthers(ProductDto productDto) {
        addProductProperties(productDto);
        return true;
    }

    private void addProductProperties(ProductDto productDto) {
        if (CollectionUtils.isNotEmpty(productDto.getProperties())) {
            productDto.getProperties().forEach(item -> {
                item.setCtime(new Date());
                item.setUtime(item.getCtime());
                item.setProductId(productDto.getId());
                item.setProductName(productDto.getName());
            });
            productToPropertiesService.insertBatch(productDto.getProperties());
        }
    }

    private void preAdd(ProductDto productDto) {
        productDto.setCtime(new Date());
        productDto.setUtime(productDto.getCtime());
        if (StringUtils.isEmpty(productDto.getCategoryName())) {
            ProductCategory productCategory = productCategoryService.selectById(productDto.getCategoryId());
            if (Objects.nonNull(productCategory)) {
                productDto.setCategoryName(productCategory.getName());
            }
        }
    }

    private Product copy(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }

    @Override
    public Product getProductByProductId(Integer productId) {
        return productDao.selectById(productId);
    }
}
