package com.gizwits.lease.manager.dao;

import com.gizwits.lease.manager.dto.OperatorForCascaderDto;
import com.gizwits.lease.manager.entity.Operator;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 运营商表 Mapper 接口
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface OperatorDao extends BaseMapper<Operator> {

    /**
     * 获取所有父运营商
     * @return
     */
    List<OperatorForCascaderDto> getAllParentOperator();

    /**
     * 根据父运营商获取所有子运营商
     * @param sysUserId
     * @return
     */
    List<OperatorForCascaderDto> getAllChildOperatorById(@Param("sysUserId") Integer sysUserId);
}