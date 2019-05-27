package com.gizwits.lease.benefit.dao;

import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 分润规则详细表 Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
public interface ShareBenefitRuleDetailDao extends BaseMapper<ShareBenefitRuleDetail> {

    ShareBenefitRuleDetail findDetailByRuleIdAndSno(@Param("ruleId")Integer ruleId, @Param("sno")String sno);

    int updateRuleDetailAndDeviceToDeleted(@Param("ruleId") String ruleId);
}