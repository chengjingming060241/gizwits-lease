package com.gizwits.lease.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.wallet.dto.RechargeRuleForDetailDto;
import com.gizwits.lease.wallet.dto.RechargeRuleForEditDto;
import com.gizwits.lease.wallet.entity.RechargeMoney;

/**
 * <p>
 * 充值优惠规则表 服务类
 * </p>
 *
 * @author Joke
 * @since 2017-10-11
 */
public interface RechargeMoneyRuleService extends IService<RechargeMoney> {

	void editRule(RechargeRuleForEditDto dto);

	RechargeRuleForDetailDto getRuleDetail(Integer projectId);
}
