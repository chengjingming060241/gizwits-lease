package com.gizwits.lease.product.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.product.dto.ProductCommandConfigForAddDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForDetailDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForQueryDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForUpdateDto;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.vo.ProductCommandVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
public interface ProductCommandConfigService extends IService<ProductCommandConfig> {

    List<ProductCommandVO> getAllUseableConfig();


    String getCommandByConfig(ProductCommandConfig commandConfig, Double num);

    String getDeviceModelCommand(ProductCommandConfig commandConfig);

    String getSpecialDisplayUnit(ProductCommandConfig commandConfig);

    /**
     * 列表
     */
    List<ProductCommandConfigForDetailDto> list(ProductCommandConfigForQueryDto query);


    /**
     * 删除
     */
    boolean delete(Integer id);

    /**
     * 更新
     */
    boolean update(ProductCommandConfigForUpdateDto dto);

    /**
     * 添加
     */
    ProductCommandConfig add(ProductCommandConfigForAddDto dto);



}
