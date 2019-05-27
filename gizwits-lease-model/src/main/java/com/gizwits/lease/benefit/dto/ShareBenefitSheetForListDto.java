package com.gizwits.lease.benefit.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 分润单列表
 *
 * @author lilh
 * @date 2017/8/3 16:05
 */
public class ShareBenefitSheetForListDto {

    /** id */
    private Integer id;

    /** 创建时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    /** 分润单号 */
    private String sheetNo;

    /** 运营商名称 */
    private String operatorName;

    /** 账单金额 */
    private Double totalMoney;

    private Double shareMoney;

    /** 状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    public ShareBenefitSheetForListDto(ShareBenefitSheet shareBenefitSheet) {
        BeanUtils.copyProperties(shareBenefitSheet, this);
    }

    public Double getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Double shareMoney) {
        this.shareMoney = shareMoney;
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

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
