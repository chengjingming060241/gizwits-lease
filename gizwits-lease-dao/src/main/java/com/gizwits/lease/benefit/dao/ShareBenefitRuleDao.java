package com.gizwits.lease.benefit.dao;

import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
public interface ShareBenefitRuleDao extends BaseMapper<ShareBenefitRule> {

    int updateRuleLastExecuteTime(@Param("ruleId")String ruleId);
}