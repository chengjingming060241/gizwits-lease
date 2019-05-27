package com.gizwits.lease.panel.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto - 用于编辑更新
 *
 * @author lilh
 * @date 2017/7/17 14:52
 */
public class ItemForUpdateDto {

    private List<UpdateDto> items = new ArrayList<>();

    public List<UpdateDto> getItems() {
        return items;
    }

    public void setItems(List<UpdateDto> items) {
        this.items = items;
    }

    public static class UpdateDto {

        private Integer id;

        private Integer isShow;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getIsShow() {
            return isShow;
        }

        public void setIsShow(Integer isShow) {
            this.isShow = isShow;
        }

    }
}
