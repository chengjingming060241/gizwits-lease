package com.gizwits.lease.product.vo;

import java.util.List;

/**
 * Created by GaGi on 2017/8/3.
 */
public class AppProductServiceDetailVo {

    private List<ProductServiceDetailVo> list;
    private String unit;

    public List<ProductServiceDetailVo> getList() {
        return list;
    }

    public void setList(List<ProductServiceDetailVo> list) {
        this.list = list;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
