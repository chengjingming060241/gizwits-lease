package com.gizwits.lease.order.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by xian on 30/8/2017.
 */
public class PageOrderAppList<T> extends Pagination {
    private Double totalPay;

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 查询参数
     */
    private Map<String, Object> condition;

    public PageOrderAppList() {
        /* 注意，传入翻页参数 */
    }

    public PageOrderAppList(int current, int size) {
        super(current, size);
    }

    public PageOrderAppList(int current, int size, String orderByField) {
        super(current, size);
        this.setOrderByField(orderByField);
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        StringBuilder pg = new StringBuilder();
        pg.append(" Page:{ [").append(super.toString()).append("], ");
        if (records != null) {
            pg.append("records-size:").append(records.size());
        } else {
            pg.append("records is null");
        }
        return pg.append(" }").toString();
    }
}
