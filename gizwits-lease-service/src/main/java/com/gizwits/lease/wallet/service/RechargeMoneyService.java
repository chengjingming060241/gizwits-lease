package com.gizwits.lease.wallet.service;

import com.gizwits.lease.wallet.dto.RechargeDto;
import com.gizwits.lease.wallet.entity.RechargeMoney;
import com.baomidou.mybatisplus.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 充值优惠表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-21
 */
public interface RechargeMoneyService extends IService<RechargeMoney> {
    /**
     * 充值页面数据
     * @Param projectId
     * @return
     */
    RechargeDto getRechargeMoney(Integer projectId);


    /**
     * 获得充值优惠比例
     * @param projectId 项目id 1艾芙芮 2卡励
     * @return
     */
    BigDecimal getRate(Integer projectId);

}
