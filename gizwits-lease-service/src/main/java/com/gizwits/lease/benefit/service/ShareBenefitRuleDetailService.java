package com.gizwits.lease.benefit.service;

import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分润规则详细表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
public interface ShareBenefitRuleDetailService extends IService<ShareBenefitRuleDetail> {

    /**
     * 添加
     * @param shareBenefitRuleDto
     * @return
     */
    boolean insertShareBenefitRuleDetail(ShareBenefitRuleDto shareBenefitRuleDto);

    /**
     * 查询详细分润规则
     * @param ruleId
     * @param name
     * @return
     */
    ShareBenefitRuleDetail selectByRuleIdAndName(String ruleId,String name);

    /**
     * 更新
     * @param shareBenefitRuleDto
     * @return
     */
    boolean updateShareBenefitRuleDatail(ShareBenefitRuleDto shareBenefitRuleDto,Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap);

    /**
     * 删除
     * @param ids
     */
    void deleteShareBenefitRuleDatailByIds(List<String> ids);

    /**
     * 查询
     * @param ruleId
     * @return
     */
    List<ShareBenefitRuleDetail> selectByRuleId(String ruleId);

    /**
     * 删除
     * @param list
     */
    void deleteShareBenefitRuleDatail(List<ShareBenefitRuleDetail> list);
    /**
     * 根据RuleId和sno查询设备上的收费规则详情
     * @param ruleId
     * @param sno
     * @return
     */
    ShareBenefitRuleDetail getRuleDetailByRuleIdAndSno(Integer ruleId,String sno);

}
