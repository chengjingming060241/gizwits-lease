package com.gizwits.lease.product.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/28.
 */
public class ProductdataPointUpdateDto implements Serializable {
    private Integer id;
    private Integer rank;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
