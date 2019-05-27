package com.gizwits.lease.product.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.product.entity.ProductOperationHistory;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 产品操作历史列表
 *
 * @author lilh
 * @date 2017/7/20 18:05
 */
public class ProductOperationHistoryForListDto {

    private Integer id;

    private String sysUserName;

    private String content;

    private String ip;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    public ProductOperationHistoryForListDto(ProductOperationHistory history) {
        BeanUtils.copyProperties(history, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}
