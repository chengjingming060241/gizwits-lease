package com.gizwits.lease.benefit.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.base.Constants;

/**
 * Dto - 分润单查询
 *
 * @author lilh
 * @date 2017/8/3 16:00
 */
public class ShareBenefitSheetForQueryDto {

    @JsonIgnore
    @Query(field = "id")
    private Integer id;

    /** 分润单号 */
    @Query(field = "sheet_no", operator = Query.Operator.like)
    private String sheetNo;

    /** 运营商名称 */
    @Query(field = "operator_name", operator = Query.Operator.like)
    private String operatorName;

    private Integer status;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "ctime", operator = Query.Operator.ge)
    private Date startTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "ctime", operator = Query.Operator.le)
    private Date endTime;

    @JsonIgnore
    @Query(field = "sys_account_id", operator = Query.Operator.in)
    private List<Integer> accessableUserIds;

    @JsonIgnore
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    @Query(field = "status", operator = Query.Operator.in)
    private List<Integer> queryStatus;

    public List<Integer> getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(List<Integer> queryStatus) {
        this.queryStatus = queryStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getAccessableUserIds() {
        return accessableUserIds;
    }

    public void setAccessableUserIds(List<Integer> accessableUserIds) {
        this.accessableUserIds = accessableUserIds;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
