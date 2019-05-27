package com.gizwits.lease.panel.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 个人面板
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
@TableName("personal_panel")
public class PersonalPanel extends Model<PersonalPanel> {
    private static final long serialVersionUID = 5477834974773857820L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 创建时间
     */
    private Date ctime;
    /**
     * 更新时间
     */
    private Date utime;
    /**
     * 系统账号id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 面板项id
     */
    @TableField("panel_id")
    private Integer panelId;
    /**
     * 定时任务回写的值
     */
    @TableField("item_value")
    private String itemValue;
    /**
     * 是否显示，0.不显示，1.显示
     */
    @TableField("is_show")
    private Integer isShow;
    /**
     * 排序
     */
    private Integer sort;

    @TableField("item_odd")
    private String itemOdd;

    public String getItemOdd() {
        return itemOdd;
    }

    public void setItemOdd(String itemOdd) {
        this.itemOdd = itemOdd;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPanelId() {
        return panelId;
    }

    public void setPanelId(Integer panelId) {
        this.panelId = panelId;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
