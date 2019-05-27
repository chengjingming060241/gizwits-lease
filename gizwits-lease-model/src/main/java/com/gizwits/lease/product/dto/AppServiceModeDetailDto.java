package com.gizwits.lease.product.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by GaGi on 2017/8/3.
 */
public class AppServiceModeDetailDto {
    @NotNull
    private String sno;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
