package com.gizwits.lease.refund.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.refund.dto.*;
import com.gizwits.lease.refund.entity.RefundApply;

import java.util.List;

/**
 * <p>
 * 退款申请表 服务类
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
public interface RefundApplyService extends IService<RefundApply> {
	//申请退款
	void apply(RefundAddDto dto);
	//审核
	List<String> audit(RefundAuditDto dto);
	//退款列表
	Page<RefundInfoDto> list(Pageable<RefundListQueryDto> pageable);
	//退款详情
	RefundInfoDto detail(String id);
	//申请退款前，检查一下是否有申请中或已审核的退款单
	RefundInfoDto checkBeforeApply(String orderNo);
	//已选退款单统计
	RefundStatisticsDto checkedStatistics(List<String> ids);
	//执行批量退款
	List<String> refund(List<String> ids);
	//执行退款
	boolean refund(OrderBase orderBase);

}
