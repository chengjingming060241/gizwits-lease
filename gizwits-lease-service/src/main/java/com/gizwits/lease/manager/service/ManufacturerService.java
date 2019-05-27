package com.gizwits.lease.manager.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.manager.entity.Manufacturer;

/**
 * <p>
 * 厂商(或企业)表 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface ManufacturerService extends IService<Manufacturer> {

    /**
     * 获取绑定的厂商帐号
     */
    List<Integer> resolveBindAccount();
	
}
