package com.gizwits.lease.product.dto;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.lease.product.entity.Product;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.product.entity.ProductCommandConfig;

/**
 * Dto - 添加收费模式页面数据
 *
 * @author lilh
 * @date 2017/7/14 11:35
 */
public class ProductServiceModeForAddPageDto {

    private Integer productId;

    private String productName;

    private List<ServiceTypeDto> serviceTypes = new ArrayList<>();

    public ProductServiceModeForAddPageDto(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        //用于测试
        initServiceType();
    }

    public ProductServiceModeForAddPageDto(Product product, List<ProductCommandConfig> serviceCommandList){
        this.productId = product.getId();
        this.productName = product.getName();

        /**
         * 循环解析ServiceCommandConfig,模板JSON:
         * {
         *    "normal":{"Switch":true,"Speed":1}, //订单下发指令
         *    "special":{                         //需要经过换算后跟订单指令一起下发
         *        "name":"Area_Quantity",
         *       "type":"unit",
         *        "min":0,
         *        "max":50,
         *        "step":1,
         *        "displayUnit":"平方米",
         *        "dataPointUnit":"平方厘米",
         *        "multiplyValue": 10000
         *    },
         *    "deviceModel":{"Working_Mode":"limited"} //设备切换收费模式后下发
         *    }
         *
         */
        if(!ParamUtil.isNullOrEmptyOrZero(serviceCommandList)){
            for(ProductCommandConfig productCommandConfig:serviceCommandList){
                String command = productCommandConfig.getCommand();
                ServiceTypeDto dto = new ServiceTypeDto();
                if(!ParamUtil.isNullOrEmptyOrZero(command)){
                    JSONObject jsonObject = JSON.parseObject(command);
                    if(jsonObject.containsKey("special")){
                        dto.setUnit(jsonObject.getJSONObject("special").getString("displayUnit"));
                    }
                }
                dto.setServiceTypeId(productCommandConfig.getId());
                dto.setServiceTypeName(productCommandConfig.getName());
                dto.setIsFree(productCommandConfig.getIsFree());
                dto.setWorkingMode(productCommandConfig.getWorkingMode());
                serviceTypes.add(dto);
            }
        }
    }

    private void initServiceType() {
        serviceTypes.add(new ServiceTypeDto(1, "免费",""));
        serviceTypes.add(new ServiceTypeDto(2, "按时收费","时"));
        serviceTypes.add(new ServiceTypeDto(3, "按次收费","次"));
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ServiceTypeDto> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<ServiceTypeDto> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    static class ServiceTypeDto {
        private Integer serviceTypeId;
        private String serviceTypeName;
        private String unit;
        private Integer isFree;
        private String workingMode;

        public ServiceTypeDto() {
        }

        public ServiceTypeDto(Integer serviceTypeId, String serviceTypeName,String unit) {
            this.serviceTypeId = serviceTypeId;
            this.serviceTypeName = serviceTypeName;
            this.unit = unit;
        }
        public ServiceTypeDto(Integer serviceTypeId, String serviceTypeName,String unit, Integer isFree) {
            this.serviceTypeId = serviceTypeId;
            this.serviceTypeName = serviceTypeName;
            this.unit = unit;
            this.isFree = isFree;
        }

        public String getWorkingMode() {
            return workingMode;
        }

        public void setWorkingMode(String workingMode) {
            this.workingMode = workingMode;
        }

        public Integer getIsFree() {
            return isFree;
        }

        public void setIsFree(Integer isFree) {
            this.isFree = isFree;
        }

        public Integer getServiceTypeId() {
            return serviceTypeId;
        }

        public void setServiceTypeId(Integer serviceTypeId) {
            this.serviceTypeId = serviceTypeId;
        }

        public String getServiceTypeName() {
            return serviceTypeName;
        }

        public void setServiceTypeName(String serviceTypeName) {this.serviceTypeName = serviceTypeName;}

        public String getUnit() {return unit;}

        public void setUnit(String unit) {this.unit = unit;}
    }
}
