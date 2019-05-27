package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;



/**
 * 设别告警级别dto
 * Created by yinhui on 2017/7/17.
 */
public class DeviceAlarmRankDto implements Serializable {
    private Integer id;
    private  Integer ProductId;
    private String idetitfyName;
    private String showName;
    private String readWriteType;
    private String dataType;
    private String remark;
    private Integer rank;
    private String alarmRank;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getProductId() {return ProductId;}

    public void setProductId(Integer productId) {ProductId = productId;}

    public String getIdetitfyName() {
        return idetitfyName;
    }

    public void setIdetitfyName(String idetitfyName) {
        this.idetitfyName = idetitfyName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getReadWriteType() {
        return readWriteType;
    }

    public void setReadWriteType(String readWriteType) {
        this.readWriteType = readWriteType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getAlarmRank() {
        return alarmRank;
    }

    public void setAlarmRank(String alarmRank) {
        this.alarmRank = alarmRank;
    }
}
