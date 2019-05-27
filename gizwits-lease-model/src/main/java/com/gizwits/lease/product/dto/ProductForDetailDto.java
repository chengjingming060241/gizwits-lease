package com.gizwits.lease.product.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.product.entity.Product;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 产品详情
 *
 * @author lilh
 * @date 2017/7/20 10:54
 */
public class ProductForDetailDto {

    private Integer id;

    private String name;

    private Integer categoryId;

    private String categoryName;

    private Integer manufacturerAccountId;

    private String gizwitsProductKey;

    private String gizwitsProductSecret;

    private String gizwitsEnterpriseId;

    private String gizwitsEnterpriseSecret;

    private String authId;

    private String authSecret;

    private String qrcodeType;

    private String locationType;

    private String gizwitsAppId;

    private String gizwitsAppSecret;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date utime;

    private Integer deviceCount;

    private String wxProductId;

    private String networkType;

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    /** 数据点 */
    private List<ProductDataPointDto> productDataPoints = new ArrayList<>();

    // --- 用于更新的下拉列表

    /** 品类下拉列表 */
    private List<ProductCategoryForPullDto> productCategories = new ArrayList<>();

    /** 厂商账号 */
    private List<ManufacturerUserDto> manufacturers = Collections.emptyList();

    public ProductForDetailDto() {
    }

    public ProductForDetailDto(Product product) {
        BeanUtils.copyProperties(product, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getManufacturerAccountId() {
        return manufacturerAccountId;
    }

    public void setManufacturerAccountId(Integer manufacturerAccountId) {
        this.manufacturerAccountId = manufacturerAccountId;
    }

    public String getGizwitsProductKey() {
        return gizwitsProductKey;
    }

    public void setGizwitsProductKey(String gizwitsProductKey) {
        this.gizwitsProductKey = gizwitsProductKey;
    }

    public String getGizwitsProductSecret() {
        return gizwitsProductSecret;
    }

    public void setGizwitsProductSecret(String gizwitsProductSecret) {
        this.gizwitsProductSecret = gizwitsProductSecret;
    }

    public String getGizwitsEnterpriseId() {
        return gizwitsEnterpriseId;
    }

    public void setGizwitsEnterpriseId(String gizwitsEnterpriseId) {
        this.gizwitsEnterpriseId = gizwitsEnterpriseId;
    }

    public String getGizwitsEnterpriseSecret() {
        return gizwitsEnterpriseSecret;
    }

    public void setGizwitsEnterpriseSecret(String gizwitsEnterpriseSecret) {
        this.gizwitsEnterpriseSecret = gizwitsEnterpriseSecret;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public String getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(String qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getGizwitsAppId() {
        return gizwitsAppId;
    }

    public void setGizwitsAppId(String gizwitsAppId) {
        this.gizwitsAppId = gizwitsAppId;
    }

    public String getGizwitsAppSecret() {
        return gizwitsAppSecret;
    }

    public void setGizwitsAppSecret(String gizwitsAppSecret) {
        this.gizwitsAppSecret = gizwitsAppSecret;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public String getWxProductId() {
        return wxProductId;
    }

    public void setWxProductId(String wxProductId) {
        this.wxProductId = wxProductId;
    }

    public List<ProductDataPointDto> getProductDataPoints() {
        return productDataPoints;
    }

    public void setProductDataPoints(List<ProductDataPointDto> productDataPoints) {
        this.productDataPoints = productDataPoints;
    }

    public List<ProductCategoryForPullDto> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategoryForPullDto> productCategories) {
        this.productCategories = productCategories;
    }

    public List<ManufacturerUserDto> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(List<ManufacturerUserDto> manufacturers) {
        this.manufacturers = manufacturers;
    }
}
