package com.gizwits.lease.panel.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 首页面板
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
public class Panel extends Model<Panel> {

    private static final long serialVersionUID = -7620978718002890569L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 更新时间
     */
    private Date utime;
    /**
     * 创建时间
     */
    private Date ctime;
    /**
     * 1.数据，2.图表
     */
    private Integer type;
    /**
     * 1.设备分析，2.用户分析，3.订单分析
     */
    private Integer module;
    /**
     * 模块中的具体项，比如当前设备总数
     */
    @TableField("module_item")
    private Integer moduleItem;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图表数据时前端请求的服务uri
     */
    private String uri;
    /**
     * 后续使用的条件等信息
     */
    private String condition;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getModuleItem() {
        return moduleItem;
    }

    public void setModuleItem(Integer moduleItem) {
        this.moduleItem = moduleItem;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
