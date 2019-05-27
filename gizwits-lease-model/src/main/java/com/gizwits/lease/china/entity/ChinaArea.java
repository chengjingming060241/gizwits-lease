package com.gizwits.lease.china.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 全国省市行政编码表
 * </p>
 *
 * @author yinhui
 * @since 2017-07-14
 */
@TableName("china_area")
public class ChinaArea extends Model<ChinaArea> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 省市县的名字
     */
	private String name;
    /**
     * 行政区编码
     */
	private Integer code;
	@TableField("parent_code")
	private Integer parentCode;
    /**
     * 所属父级的中文名称
     */
	@TableField("parent_name")
	private String parentName;
	@TableField("is_leaf")
	private Integer isLeaf;


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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getParentCode() {
		return parentCode;
	}

	public void setParentCode(Integer parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
