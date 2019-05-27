package com.gizwits.lease.product.resolver;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.HttpRequestUtils;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.enums.DataType;
import com.gizwits.lease.enums.ProductOperateType;
import com.gizwits.lease.enums.ReadWriteType;
import com.gizwits.lease.event.DataPointSyncEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.product.dto.GizwitsDataPointReqDto;
import com.gizwits.lease.product.dto.ProductDataPointDto;
import com.gizwits.lease.product.dto.ProductDataPointForListDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Resolver - 机智云数据点
 *
 * @author lilh
 * @date 2017/7/4 14:54
 */
@Component
public class GizwitsDataPointResolver {

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDataPointService productDataPointService;

    public final static String ELECT_FENCE_NAME="电子围栏";


    public List<ProductDataPoint> resolve(Integer productId) {
        return resolve(productId, true);
    }

    public ProductDataPointForListDto resolve(GizwitsDataPointReqDto req) {
        //1.调用接口获取数据
        String token = new TokenResolver().resolve(req);
        List<ProductDataPoint> dataPoints = new DataPointResolver().resolve(token, req);
        if (CollectionUtils.isEmpty(dataPoints)) {
            LeaseException.throwSystemException(LeaseExceEnums.DATA_POINT_ILLEGAL_PARAM);
        }
        List<ProductDataPoint> result = dataPoints;
        if (Objects.nonNull(req.getProductId())) {
            //2.更新数据库,根据标识名定位
            result = updateFromGizwitsToLease(req.getProductId(), dataPoints);

        }
        //3.返回
        ProductDataPointForListDto dto = new ProductDataPointForListDto();
        dto.setDataPoints(result.stream().map(ProductDataPointDto::new).collect(Collectors.toList()));
        return dto;
    }

    private List<ProductDataPoint> updateFromGizwitsToLease(Integer productId, List<ProductDataPoint> fromGizwits) {
        Map<String, ProductDataPoint> fromGizwitsMap = fromGizwits.stream().collect(Collectors.toMap(ProductDataPoint::getIdentityName, item -> item));
        List<ProductDataPoint> fromDb = productDataPointService.selectList(new EntityWrapper<ProductDataPoint>().eq("product_id", productId));
        Map<String, ProductDataPoint> fromDbMap = fromDb.stream().collect(Collectors.toMap(ProductDataPoint::getIdentityName, item -> item));
        List<ProductDataPoint> toDel = fromDb.stream().filter(item -> Objects.isNull(fromGizwitsMap.get(item.getIdentityName()))).collect(Collectors.toList());
        List<ProductDataPoint> toAdd = fromGizwits.stream().filter(item -> Objects.isNull(fromDbMap.get(item.getIdentityName()))).collect(Collectors.toList());
        //增加电子围栏数据点的判断
        boolean isContainElectFence = false;
        for(ProductDataPoint dataPoint:fromDb){
            if(dataPoint.getIdentityName().equals(ELECT_FENCE_NAME)){
                isContainElectFence = true;
            }
        }
        if(!isContainElectFence){
            ProductDataPoint dataPoint = new ProductDataPoint();
            dataPoint.setCtime(new Date());
            dataPoint.setUtime(new Date());
            dataPoint.setShowName(ELECT_FENCE_NAME);
            dataPoint.setIdentityName(ELECT_FENCE_NAME);
            dataPoint.setReadWriteType(ReadWriteType.ALERT.getCode());
            dataPoint.setDataType(DataType.BOOL.getCode());
            dataPoint.setRemark("设备移开1000米报警");
            toAdd.add(dataPoint);
        }


        List<ProductDataPoint> toUpdate = fromDb.stream().filter(item -> Objects.nonNull(fromGizwitsMap.get(item.getIdentityName()))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(toDel)) {
            productDataPointService.deleteBatchIds(toDel.stream().map(ProductDataPoint::getId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(toAdd)) {
            toAdd.forEach(item -> {
                item.setProductId(productId);
                item.setCtime(new Date());
                item.setUtime(item.getCtime());
            });
            productDataPointService.insertBatch(toAdd);
        }
        if (CollectionUtils.isNotEmpty(toUpdate)) {
            toUpdate.forEach(item -> {
                ProductDataPoint remote = fromGizwitsMap.get(item.getIdentityName());
                item.setUtime(new Date());
                item.setValueLimit(remote.getValueLimit());
                item.setRemark(remote.getRemark());
                item.setShowName(remote.getShowName());
                item.setDataType(remote.getDataType());
                item.setReadWriteType(remote.getReadWriteType());
            });
            productDataPointService.updateBatchById(toUpdate);
        }
        productService.publishChangeEvent(productId, ProductOperateType.DATA_POINT);
        return productDataPointService.selectList(new EntityWrapper<ProductDataPoint>().eq("product_id", productId));
    }

    /**
     * 解析
     *
     * @param productId 产品id
     * @param isInit    是否初始化
     * @return 机智云返回的数据点信息
     */
    public List<ProductDataPoint> resolve(Integer productId, boolean isInit) {
        GizwitsDataPointReqDto reqDto = resolveReqDto(productId);
        String token = new TokenResolver().resolve(reqDto);
        List<ProductDataPoint> dataPoints = new DataPointResolver().resolve(token, reqDto);
        if (CollectionUtils.isNotEmpty(dataPoints)) {
            dataPoints.forEach(item -> item.setProductId(productId));
            initIfNecessary(dataPoints, isInit);
        }
        return dataPoints;
    }

    /**
     * 同步
     *
     * @param productId 产品id
     * @param hasRecord 数据库中是否有记录
     */
    public void sync(Integer productId, boolean hasRecord) {
        if (!hasRecord) {
            resolve(productId, true);
        } else {
            updateFromGizwitsToLease(productId);
        }
    }

    /**
     * 简化逻辑，直接删除原来的数据点，再插入
     */
    private void updateFromGizwitsToLease(Integer productId) {
        List<ProductDataPoint> fromGizwits = resolve(productId, false);
        boolean success = productDataPointService.delete(new EntityWrapper<ProductDataPoint>().eq("product_id", productId));
        initIfNecessary(fromGizwits, success);
        CommonEventPublisherUtils.publishEvent(new DataPointSyncEvent(fromGizwits));
    }

    private void initIfNecessary(List<ProductDataPoint> dataPoints, boolean isInit) {
        if (isInit) {
            productDataPointService.insertBatch(dataPoints);
        }
    }


    private GizwitsDataPointReqDto resolveReqDto(Integer productId) {
        GizwitsDataPointReqDto reqDto = new GizwitsDataPointReqDto();
        Product product = productService.selectById(productId);
        Assert.notNull(product);
        reqDto.setGizwitsProductKey(product.getGizwitsProductKey());
        reqDto.setGizwitsProductSecret(product.getGizwitsProductSecret());
        Manufacturer manufacturer = manufacturerService.selectById(product.getManufacturerId());
        Assert.notNull(manufacturer);
        reqDto.setGizwitsEnterpriseId(manufacturer.getEnterpriseId());
        reqDto.setGizwitsEnterpriseSecret(manufacturer.getEnterpriseSecret());
        return reqDto;
    }


    private String getAbsoluteUrl(String path, String productKey) {
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        String host = commonSystemConfig.getEnterpriseApiHost();
        return MessageFormat.format(host + path, productKey);
    }

    private class TokenResolver {

        String resolve(GizwitsDataPointReqDto reqDto) {
            String response = HttpRequestUtils.post(getAbsoluteUrl(getTokenUri(), reqDto.getGizwitsProductKey()), getHeaders(), JSON.toJSONString(reqDto));
            if (Objects.nonNull(response)) {
                return (String) JSONObject.parseObject(response).get("token");
            }
            return null;
        }

        String getTokenUri() {
            CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
            return commonSystemConfig.getEnterpriseTokenUri();
        }

        Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>(2);
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            return headers;
        }
    }

    private class DataPointResolver {

        List<ProductDataPoint> resolve(String token, GizwitsDataPointReqDto reqDto) {
            String response = HttpRequestUtils.get(getAbsoluteUrl(getDataPointUri(), reqDto.getGizwitsProductKey()), getHeaders(token));
            if (Objects.nonNull(response)) {
                SourceResponseData sourceResponseData = JSON.parseObject(response, SourceResponseData.class);
                return process(sourceResponseData);
            }
            return null;
        }

        private List<ProductDataPoint> process(SourceResponseData source) {
            if (CollectionUtils.isEmpty(source.getEntities())) {
                return Collections.emptyList();
            }
            List<ProductDataPoint> dataPoints = new ArrayList<>();
            source.getEntities().get(0).getAttrs().forEach(attr -> {
                ProductDataPoint dataPoint = new ProductDataPoint();
                dataPoint.setDataType(attr.getDataType());
                dataPoint.setShowName(attr.getDisplayName());
                dataPoint.setIdentityName(attr.getName());
                dataPoint.setReadWriteType(attr.getType());
                dataPoint.setRemark(attr.getDesc());
                String value;
                if (Objects.nonNull(attr.enums)) {
                    //枚举
                    value = attr.enums.toString();
                } else if (Objects.nonNull(attr.unitSpec)) {
                    //数值
                    attr.unitSpec.max = (int) (attr.unitSpec.max * attr.unitSpec.ratio);
                    attr.unitSpec.min = (int) (attr.unitSpec.min * attr.unitSpec.ratio);
                    value = JSON.toJSONString(attr.unitSpec);
                } else if (Objects.nonNull(attr.position) && Objects.equals(DataType.BINARY.getCode(), dataPoint.getDataType())) {
                    //扩展
                    value = JSON.toJSONString(attr.position);
                } else {
                    //布尔
                    value = Arrays.asList(Boolean.TRUE, Boolean.FALSE).toString();
                }
                dataPoint.setValueLimit(value);
                dataPoint.setIsMonit(1);
                dataPoints.add(dataPoint);
            });
            return dataPoints;
        }

        String getDataPointUri() {
            CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
            return commonSystemConfig.getProductDataPointUri();
        }

        Map<String, String> getHeaders(String token) {
            Map<String, String> headers = new HashMap<>(3);
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            headers.put("Authorization", "token " + token);
            return headers;
        }
    }

    private static class SourceResponseData {

        @JSONField(name = "name")
        String name;

        @JSONField(name = "product_key")
        String productKey;

        List<Entity> entities = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public List<Entity> getEntities() {
            return entities;
        }

        public void setEntities(List<Entity> entities) {
            this.entities = entities;
        }

        private static class Entity {

            @JSONField(name = "display_name")
            String displayName;

            @JSONField(name = "attrs")
            List<Attr> attrs = new ArrayList<>();

            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }

            public List<Attr> getAttrs() {
                return attrs;
            }

            public void setAttrs(List<Attr> attrs) {
                this.attrs = attrs;
            }

            private static class Attr {
                //显示名
                @JSONField(name = "display_name")
                String displayName;

                //标识名
                @JSONField(name = "name")
                String name;

                //数据类型
                @JSONField(name = "data_type")
                String dataType;

                //读写类型
                @JSONField(name = "type")
                String type;

                //备注描述
                @JSONField(name = "desc")
                String desc;

                //枚举
                @JSONField(name = "enum")
                List<String> enums;

                //数值约束
                @JSONField(name = "uint_spec")
                UnitSpec unitSpec;

                //扩展类型时长度限制
                @JSONField(name = "position")
                Position position;


                public String getDisplayName() {
                    return displayName;
                }

                public void setDisplayName(String displayName) {
                    this.displayName = displayName;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getDataType() {
                    return dataType;
                }

                public void setDataType(String dataType) {
                    this.dataType = dataType;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public List<String> getEnums() {
                    return enums;
                }

                public void setEnums(List<String> enums) {
                    this.enums = enums;
                }

                public UnitSpec getUnitSpec() {
                    return unitSpec;
                }

                public void setUnitSpec(UnitSpec unitSpec) {
                    this.unitSpec = unitSpec;
                }

                public Position getPosition() {
                    return position;
                }

                public void setPosition(Position position) {
                    this.position = position;
                }

                @JSONType(ignores = "ratio")
                public static class UnitSpec {
                    @JSONField(name = "min")
                    private Integer min;
                    @JSONField(name = "max")
                    private Integer max;

                    @JSONField(name = "ratio")
                    private Float ratio;

                    public Integer getMin() {
                        return min;
                    }

                    public void setMin(Integer min) {
                        this.min = min;
                    }

                    public Integer getMax() {
                        return max;
                    }

                    public void setMax(Integer max) {
                        this.max = max;
                    }

                    public Float getRatio() {
                        return ratio;
                    }

                    public void setRatio(Float ratio) {
                        this.ratio = ratio;
                    }
                }

                public static class Position {
                    @JSONField(name = "len")
                    private Integer len;

                    public Integer getLen() {
                        return len;
                    }

                    public void setLen(Integer len) {
                        this.len = len;
                    }
                }
            }

        }
    }

}
