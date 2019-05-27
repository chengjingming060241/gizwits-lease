package com.gizwits.lease.benefit.dao;

import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetailDevice;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitDeviceUpdateDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 分润规则详细设备表 Mapper 接口
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
public interface ShareBenefitRuleDetailDeviceDao extends BaseMapper<ShareBenefitRuleDetailDevice> {

    ShareBenefitRuleDeviceVo selectRuleBySnoAndSysAccountId(@Param("sno")String sno, @Param("sysAccountId")Integer sysAccountId);

    List<ShareBenefitRuleDeviceVo> selectRulesBySno(@Param("sno")String sno);

    List<ShareBenefitRuleDeviceVo> selectDeviceRuleByRuleId(@Param("ruleId")String ruleId);

    List<ShareBenefitRuleDeviceVo> selectRuleDevicesBySnos(@Param("ruleId")String ruleId, @Param("sysAccountId")Integer sysAccountId, @Param("snoList")List<String> snoList);

    int updateParentDeviceSharePercentage(ShareBenefitDeviceUpdateDto shareBenefitDeviceUpdateDto);

    int updateDeviceForOperatorToDeleted(@Param("ruleId")String ruleId, @Param("sno")String sno);
}