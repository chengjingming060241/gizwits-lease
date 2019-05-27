package com.gizwits.lease.product.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 产品操作记录
 * </p>
 *
 * @author lilh
 * @since 2017-07-20
 */
@TableName("product_operation_history")
public class ProductOperationHistory extends Model<ProductOperationHistory> {

    private static final long serialVersionUID = 1L;

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
     * 产品id
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 设备序列号
     */
    @TableField("device_sno")
    private String deviceSno;

    /**
     * 操作类型
     */
    @TableField("operate_type")
    private Integer operateType;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 操作人
     */
    @TableField("sys_user_id")
    private Integer sysUserId;
    /**
     * 操作人
     */
    @TableField("sys_user_name")
    private String sysUserName;


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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getDeviceSno() {
        return deviceSno;
    }

    public void setDeviceSno(String deviceSno) {
        this.deviceSno = deviceSno;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
