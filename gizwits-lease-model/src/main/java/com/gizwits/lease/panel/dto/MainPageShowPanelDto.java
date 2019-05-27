package com.gizwits.lease.panel.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto - 首页显示的统计项
 *
 * @author lilh
 * @date 2017/7/17 11:09
 */
public class MainPageShowPanelDto {

    private Integer sysUserId;

    private List<ModuleItemDto> items = new ArrayList<>();

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public List<ModuleItemDto> getItems() {
        return items;
    }

    public void setItems(List<ModuleItemDto> items) {
        this.items = items;
    }

}
