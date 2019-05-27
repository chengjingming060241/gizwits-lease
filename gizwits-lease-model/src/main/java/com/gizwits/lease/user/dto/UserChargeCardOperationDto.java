package com.gizwits.lease.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dto - 用于启用和禁用接收数据
 *
 * @author lilh
 * @date 2017/8/29 18:27
 */
public class UserChargeCardOperationDto {

    /** 充值卡id */
    @NotEmpty
    @Query(field = "id", operator = Query.Operator.in)
    private List<Integer> ids;

    @JsonIgnore
    @Query(field = "status")
    private Integer status;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
