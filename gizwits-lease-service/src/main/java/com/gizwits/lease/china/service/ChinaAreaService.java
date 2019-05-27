package com.gizwits.lease.china.service;

import com.gizwits.lease.china.entity.ChinaArea;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.china.entity.china.dto.AreaDto;

import java.util.List;

/**
 * <p>
 * 全国省市行政编码表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
public interface ChinaAreaService extends IService<ChinaArea> {
	
    /**
     * 根据编码查询下一级地区
     * @param code
     * @return
     */
    public List<AreaDto> getAreas(Integer code);


    ChinaArea getAreaByName(String name, String parentName);
}
