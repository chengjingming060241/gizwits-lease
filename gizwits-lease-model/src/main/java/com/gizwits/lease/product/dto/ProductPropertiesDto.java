package com.gizwits.lease.product.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.gizwits.lease.product.entity.ProductProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 负责产品属性的传递
 *
 * @author lilh
 * @date 2017/6/29 11:05
 */
public class ProductPropertiesDto implements Serializable {
    private static final long serialVersionUID = 294568821541159209L;

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
     * 属性key
     */
    @NotBlank(message = "属性key不能为空")
    private String propertyKey;
    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空")
    private String propertyName;
    /**
     * 提示语
     */
    private String tips;
    /**
     * 产品类型
     */
    @NotNull(message = "产品类型id不能为空")
    private Integer categoryId;
    /**
     * 产品类型名称
     */
    @NotNull(message = "产品类型名称不能为空")
    private String categoryName;
    /**
     * 是否必填,1:是 0:否
     */
    @NotNull(message = "是否必填不能为空")
    private Integer isNotNull;
    /**
     * 是否选择值,1:是,选择 0:否,填写
     */
    @NotNull(message = "是否选择值不能为空")
    private Integer isSelectValue;

    /**
     * 属性值
     */
    private List<String> values = new ArrayList<>();

    public ProductPropertiesDto() {
    }

    public ProductPropertiesDto(ProductProperties productProperties) {
        BeanUtils.copyProperties(productProperties, this);
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

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
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

    public Integer getIsNotNull() {
        return isNotNull;
    }

    public void setIsNotNull(Integer isNotNull) {
        this.isNotNull = isNotNull;
    }

    public Integer getIsSelectValue() {
        return isSelectValue;
    }

    public void setIsSelectValue(Integer isSelectValue) {
        this.isSelectValue = isSelectValue;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
