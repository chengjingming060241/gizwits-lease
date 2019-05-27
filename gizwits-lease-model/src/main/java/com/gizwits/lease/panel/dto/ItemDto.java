package com.gizwits.lease.panel.dto;

/**
 * @author lilh
 * @date 2017/7/17 14:09
 */
public class ItemDto {

    private Integer id;

    private String panelTypeName;

    private String moduleName;

    private String moduleItemName;

    private Integer isShow;

    private Integer sort;

    private Integer itemId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPanelTypeName() {
        return panelTypeName;
    }

    public void setPanelTypeName(String panelTypeName) {
        this.panelTypeName = panelTypeName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleItemName() {
        return moduleItemName;
    }

    public void setModuleItemName(String moduleItemName) {
        this.moduleItemName = moduleItemName;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
