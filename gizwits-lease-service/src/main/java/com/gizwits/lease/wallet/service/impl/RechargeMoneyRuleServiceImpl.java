package com.gizwits.lease.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.enums.RechargeType;
import com.gizwits.lease.wallet.dao.RechargeMoneyDao;
import com.gizwits.lease.wallet.dto.RechargeMoneyDto;
import com.gizwits.lease.wallet.dto.RechargeRuleForDetailDto;
import com.gizwits.lease.wallet.dto.RechargeRuleForEditDto;
import com.gizwits.lease.wallet.entity.RechargeMoney;
import com.gizwits.lease.wallet.service.RechargeMoneyRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 充值优惠规则表 服务实现类
 * </p>
 *
 * @author Joke
 * @since 2017-10-11
 */
@Service
public class RechargeMoneyRuleServiceImpl extends ServiceImpl<RechargeMoneyDao, RechargeMoney>
		implements RechargeMoneyRuleService {

	@Autowired
	private SysUserService sysUserService;

	@Override
	public void editRule(RechargeRuleForEditDto dto) {
		SysUser currentUser = sysUserService.getCurrentUser();
		Wrapper<RechargeMoney> forDeleteDetail = new EntityWrapper<>();
		forDeleteDetail.eq("project_id", dto.getProjectId());
		if (delete(forDeleteDetail)) {
			List<RechargeMoney> forInsert = new LinkedList<>();
			for (int i = 0, len = dto.getDetails().size(); i < len; i++) {
				RechargeMoneyDto detailDto = dto.getDetails().get(i);
				RechargeMoney detail = new RechargeMoney();
				detail.setType(RechargeType.FIXED.getCode());
				detail.setChargeMoney(detailDto.getRechargeMoney());
				detail.setDiscountMoney(detailDto.getDiscountMoney());
				forInsert.add(detail);
			}

			RechargeMoney detail = new RechargeMoney();
			detail.setType(RechargeType.CUSTOM.getCode());
			detail.setRate(dto.getRate());
			forInsert.add(detail);

			for (int i = 0, len = forInsert.size(); i < len; i++) {
				detail = forInsert.get(i);
				detail.setCtime(new Date());
				detail.setProjectId(dto.getProjectId());
				detail.setSort(i + 1);
				detail.setSysUserId(currentUser.getId());
			}
			insertBatch(forInsert);
		}
	}

	@Override
	public RechargeRuleForDetailDto getRuleDetail(Integer projectId) {
		Wrapper<RechargeMoney> wrapper = new EntityWrapper<>();
		wrapper.eq("project_id", projectId).orderBy("sort");
		List<RechargeMoney> details = selectList(wrapper);
		List<RechargeMoneyDto> detailDtos = new LinkedList<>();
		RechargeRuleForDetailDto result = new RechargeRuleForDetailDto();
		result.setDetails(detailDtos);
		for (RechargeMoney detail : details) {
			if (Objects.equals(detail.getType(), RechargeType.FIXED.getCode())) {
				RechargeMoneyDto dto = new RechargeMoneyDto();
				dto.setId(detail.getId());
				dto.setRechargeMoney(detail.getChargeMoney());
				dto.setDiscountMoney(detail.getDiscountMoney());
				dto.setSort(detail.getSort());
				detailDtos.add(dto);
			} else {
				result.setRate(detail.getRate());
			}
		}
		return result;
	}
}
