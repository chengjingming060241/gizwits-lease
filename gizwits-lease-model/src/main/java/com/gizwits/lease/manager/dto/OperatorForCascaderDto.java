package com.gizwits.lease.manager.dto;

/**
 * @author Jin
 * @date 2019/3/6
 */
public class OperatorForCascaderDto {
    private String value;
    private String label;
    private Boolean isLeaf;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
