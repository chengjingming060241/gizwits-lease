package com.gizwits.lease.refund.dao;

import com.gizwits.lease.refund.dto.RefundListQueryDto;
import com.gizwits.lease.refund.dto.RefundStatisticsDto;
import com.gizwits.lease.refund.entity.RefundApply;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 退款申请表 Mapper 接口
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
public interface RefundApplyDao extends BaseMapper<RefundApply> {

	RefundStatisticsDto checkedStatistics(@Param("ids") List<String> ids);

}