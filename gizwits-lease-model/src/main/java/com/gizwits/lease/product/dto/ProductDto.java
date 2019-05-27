package com.gizwits.lease.product.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONType;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductToProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 用于录入产品信息
 *
 * @author lilh
 * @date 2017/6/29 16:35
 */
@JSONType(ignores = "gizwitsProductSecret")
public class ProductDto implements Serializable {
    private static final long serialVersionUID = -6219264226349451510L;

    /**
     * 主键,自增长
     */
    private Integer id;
    /**
     * 添加时间
     */
    private Date ctime;
    /**
     * 更新时间
     */
    private Date utime;
    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    private String name;
    /**
     * 产品图片地址
     */
    private String imgUrl;
    /**
     * 机智云产品key
     */
    @NotBlank(message = "产品key不能为空")
    private String gizwitsProductKey;
    /**
     * 机智云产品secret
     */
    @NotBlank(message = "产品secret不能为空")
    private String gizwitsProductSecret;
    /**
     * 产品状态,1:启用, 0:禁用
     */
    @NotNull
    private Integer status;
    /**
     * 产品类型
     */
    @NotNull
    private Integer categoryId;
    /**
     * 产品类型名称
     */
    private String categoryName;
    /**
     * 所属厂商
     */
    private Integer manufacturerId;

    /**
     * 厂商名称
     */
    private String manufacturerName;

    /**
     * 所属品牌
     */
    @NotNull
    private Integer brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 属性
     */
    private List<ProductToProperties> properties = new ArrayList<>();

    public ProductDto() {
    }

    public ProductDto(Product product) {
        BeanUtils.copyProperties(product, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public List<ProductToProperties> getProperties() {
        return properties;
    }

    public void setProperties(List<ProductToProperties> properties) {
        this.properties = properties;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
