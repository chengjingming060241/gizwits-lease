package com.gizwits.lease.product.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 产品属性定义表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-19
 */
@TableName("product_properties")
public class ProductProperties extends Model<ProductProperties> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
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
	@TableField("property_key")
	private String propertyKey;
    /**
     * 属性名称
     */
	@TableField("property_name")
	private String propertyName;
    /**
     * 提示语
     */
	private String tips;
    /**
     * 产品类型
     */
	@TableField("category_id")
	private Integer categoryId;
    /**
     * 产品类型名称
     */
	@TableField("category_name")
	private String categoryName;
    /**
     * 是否必填,1:是 0:否
     */
	@TableField("is_not_null")
	private Integer isNotNull;
    /**
     * 是否选择值,1:是,选择 0:否,填写
     */
	@TableField("is_select_value")
	private Integer isSelectValue;


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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
