package com.gizwits.lease.device.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.device.entity.DeviceRunningRecord;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 列表
 *
 * @author lilh
 * @date 2017/7/26 11:33
 */
public class DeviceRunningRecordForListDto {

    private Integer id;

    private String sno;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    private Integer workStatus;

    private String workStatusDesc;

    private String content;

    public DeviceRunningRecordForListDto(DeviceRunningRecord deviceRunningRecord) {
        BeanUtils.copyProperties(deviceRunningRecord, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getWorkStatusDesc() {
        return workStatusDesc;
    }

    public void setWorkStatusDesc(String workStatusDesc) {
        this.workStatusDesc = workStatusDesc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
