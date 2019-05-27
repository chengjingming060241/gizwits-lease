package com.gizwits.lease.export.web;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dto - 导出
 *
 * @author lilh
 * @date 2017/8/10 10:37
 */
public class ExportHelper<T, ID> {

    /** 标题部分 */
    @NotEmpty
    private String[] titles;

    /** 与标题对应的属性 */
    @NotEmpty
    private String[] properties;

    /** 选中的记录，若选中，优先查询 */
    private List<ID> ids;

    /** 各个列表的查询条件 */
    private T query;

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public List<ID> getIds() {
        return ids;
    }

    public void setIds(List<ID> ids) {
        this.ids = ids;
    }

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }
}
