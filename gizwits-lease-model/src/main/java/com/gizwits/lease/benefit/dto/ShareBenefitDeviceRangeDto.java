package com.gizwits.lease.benefit.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zhl on 2017/9/1.
 */
public class ShareBenefitDeviceRangeDto {

    @NotNull
    private Integer sysAccountId;

    private List<String> snoList;

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public List<String> getSnoList() {
        return snoList;
    }

    public void setSnoList(List<String> snoList) {
        this.snoList = snoList;
    }
}
