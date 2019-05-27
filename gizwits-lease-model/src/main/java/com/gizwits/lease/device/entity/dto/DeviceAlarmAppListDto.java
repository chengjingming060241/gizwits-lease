package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/** 查询APP端故障列表Dto
 * Created by xian on 25/8/2017.
 */
public class DeviceAlarmAppListDto implements Serializable {
    private Integer pageSize;
    private Integer currentPage;

    @JsonIgnore
    private Integer start;
    @JsonIgnore
    private Integer end;

    @JsonIgnore
    private List<String> snos;

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public List<String> getSnos() {
        return snos;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public void setSnos(List<String> snos) {
        this.snos = snos;
    }
}
