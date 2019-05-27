package com.gizwits.lease.refund.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.constant.RefundStatus;
import com.gizwits.lease.constant.TradeStatus;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.refund.dao.RefundApplyDao;
import com.gizwits.lease.refund.dto.*;
import com.gizwits.lease.refund.entity.RefundApply;
import com.gizwits.lease.refund.service.RefundApplyService;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.trade.service.TradeAlipayService;
import com.gizwits.lease.trade.service.TradeBaseService;
import com.gizwits.lease.trade.service.TradeWeixinService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserChargeCardService;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.wallet.service.UserWalletService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 退款申请表 服务实现类
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
@Service
public class RefundApplyServiceImpl extends ServiceImpl<RefundApplyDao, RefundApply> implements RefundApplyService {

    private static final Logger log = LoggerFactory.getLogger(RefundApplyServiceImpl.class);

    @Autowired
    private OrderBaseService orderBaseService;
    @Autowired
    private TradeBaseService tradeBaseService;
    @Autowired
    private TradeWeixinService tradeWeixinService;
    @Autowired
    private TradeAlipayService tradeAlipayService;
    @Autowired
    private UserChargeCardService userChargeCardService;
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private UserService userService;
    @Autowired
    private SysUserService sysUserService;

	@Override
	public void apply(RefundAddDto dto) {
		OrderBase orderBase = orderBaseService.selectById(dto.getOrderNo());
		User user = userService.selectById(orderBase.getUserId());
		if (user == null) {
			LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
		}

		// 订单只有异常、完成、退款失败的时候可以申请退款
		OrderStatus orderStatus = OrderStatus.getOrderStatus(orderBase.getOrderStatus());
		if (orderStatus != OrderStatus.ABNORMAL && orderStatus != OrderStatus.FINISH &&
				orderStatus != OrderStatus.REFUND_FAIL) {
			LeaseException.throwSystemException(LeaseExceEnums.REFUND_ORDER_STATUS);
		}

		// 订单存在申请中、已通过、已退款的退款单时，不能再次申请退款
		RefundApply refundApply =
				getByOrderNo(dto.getOrderNo(), RefundStatus.APPLY, RefundStatus.PASS, RefundStatus.REFUNDED);
		if (refundApply != null) {
			LeaseException.throwSystemException(LeaseExceEnums.REFUND_EXIST);
		}

		RefundApply forInsert = new RefundApply();
		forInsert.setRefundNo(generateRefundNo());
		forInsert.setStatus(RefundStatus.APPLY.getCode());
		forInsert.setOrderNo(dto.getOrderNo());
		forInsert.setAmount(orderBase.getAmount());
		forInsert.setUserId(user.getId());
		forInsert.setUserMobile(StringUtils.isBlank(dto.getUserMobile()) ? user.getMobile() : dto.getUserMobile());
		// 没有指定支付宝收款帐号时，原路退款
		if (StringUtils.isBlank(dto.getUserAlipayAccount()) || StringUtils.isBlank(dto.getUserAlipayRealName())) {
			forInsert.setPath(orderBase.getPayType());
		} else {
			forInsert.setPath(PayType.ALIPAY.getCode());
			forInsert.setUserAlipayAccount(dto.getUserAlipayAccount());
			forInsert.setUserAlipayRealName(dto.getUserAlipayRealName());
		}
		forInsert.setRefundReason(dto.getRefundReason());
		forInsert.setSysUserId(orderBase.getSysUserId());
		Date now = new Date();
		forInsert.setCtime(now);
		forInsert.setUtime(now);
		insert(forInsert);
	}

    @Override
    public List<String> audit(RefundAuditDto dto) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<String> failIds = new LinkedList<>();
        List<RefundApply> forUpdate = new LinkedList<>();
        Integer status = dto.getAudit() ? RefundStatus.PASS.getCode() : RefundStatus.NO_PASS.getCode();
        Date now = new Date();
        for (String refundNo : dto.getRefundNos()) {
            RefundApply refundApply = selectById(refundNo);
            if (!ParamUtil.isNullOrEmptyOrZero(refundApply) && refundApply.getStatus().equals(RefundStatus.APPLY.getCode())) {
                RefundApply temp = new RefundApply();
                temp.setRefundNo(refundApply.getRefundNo());
                temp.setStatus(status);
                temp.setAuditorId(currentUser.getId());
                temp.setAuditReason(dto.getAuditReason());
                temp.setAuditTime(now);
                temp.setUtime(now);
                forUpdate.add(temp);
            } else {
                failIds.add(refundApply.getOrderNo());
            }
        }
        if (!ParamUtil.isNullOrEmptyOrZero(forUpdate)) {
            updateBatchById(forUpdate);
        }
        return failIds;
    }

	@Override
	public Page<RefundInfoDto> list(Pageable<RefundListQueryDto> pageable) {
		RefundListQueryDto dto = pageable.getQuery();
		Wrapper<RefundApply> wrapper = new EntityWrapper();
		if (dto.getRefundNos() != null && !dto.getRefundNos().isEmpty()) {
			wrapper.in("refund_no", dto.getRefundNos());
		}else {
			wrapper = QueryResolverUtils.parse(dto, wrapper);
		}
		Page<RefundApply> page = new Page<>();
		BeanUtils.copyProperties(pageable, page);
		page = selectPage(page, wrapper);
		Page<RefundInfoDto> result = new Page<>();
		BeanUtils.copyProperties(page, result);
		List<RefundInfoDto> records = page.getRecords().stream().map(this::getInfo).collect(Collectors.toList());
		result.setRecords(records);
		return result;
	}

	@Override
	public RefundInfoDto detail(String id) {
		return getInfo(selectById(id));
	}

	private RefundInfoDto getInfo(RefundApply refundApply) {
		log.info("ferund detail id="+refundApply.getRefundNo());
		RefundInfoDto info = new RefundInfoDto();
		BeanUtils.copyProperties(refundApply, info);
		info.setStatusStr(RefundStatus.get(info.getStatus()).getMsg());
		if(refundApply.getAuditorId()!=null) {
			SysUser auditor = sysUserService.selectById(refundApply.getAuditorId());
			info.setAuditor(StringUtils.isBlank(auditor.getNickName()) ? auditor.getUsername() : auditor.getNickName());
		}
		if(refundApply.getRefunderId()!=null) {
			SysUser refunder = sysUserService.selectById(refundApply.getRefunderId());
			info.setRefunder(
					StringUtils.isBlank(refunder.getNickName()) ? refunder.getUsername() : refunder.getNickName());
		}
		info.setPayTime(orderBaseService.selectById(info.getOrderNo()).getPayTime());
		User user = userService.selectById(refundApply.getUserId());
		if (!ParamUtil.isNullOrEmptyOrZero(user)) {
			info.setUserName(user.getNickname());
		}
		return info;
	}

	@Override
	public RefundInfoDto checkBeforeApply(String orderNo) {
		RefundApply refundApply = getByOrderNo(orderNo);
		if (refundApply == null) {
			return null;
		}
		return getInfo(refundApply);
	}

	@Override
	public RefundStatisticsDto checkedStatistics(List<String> ids) {
		return baseMapper.checkedStatistics(ids);
	}

	@Override
	public List<String> refund(List<String> ids) {
		List<String> failIds = new LinkedList<>();
		List<RefundApply> forUpdate = new LinkedList<>();
		SysUser currentUser = sysUserService.getCurrentUser();
		Date now = new Date();
		for (String refundNo : ids) {
			RefundApply refundApply = selectById(refundNo);
			OrderBase orderBase = orderBaseService.selectById(refundApply.getOrderNo());
			boolean success = false;
			if (StringUtils.isBlank(refundApply.getUserAlipayAccount()) ||
					StringUtils.isBlank(refundApply.getUserAlipayRealName())) {
				// 没有填写完整支付宝信息，原路返回
				success = refund(orderBase);
			} else {
				// 支付宝打款
				success = transfer(refundApply, orderBase);
			}
			if (success) {
				// 退款成功
				RefundApply temp = new RefundApply();
				temp.setRefundNo(refundApply.getRefundNo());
				temp.setStatus(RefundStatus.REFUNDED.getCode());
				temp.setRefunderId(currentUser.getId());
				temp.setRefundTime(now);
				temp.setUtime(now);
				forUpdate.add(temp);
			} else {
				failIds.add(refundApply.getOrderNo());
			}
		}
		if (!forUpdate.isEmpty()) {
			updateBatchById(forUpdate);
		}
		return failIds;
	}

	private boolean transfer(RefundApply refundApply, OrderBase orderBase){
		log.info("=====>>> 执行打款操作，orderNo：{}，支付宝帐号：{}，支付宝真实姓名：{}", orderBase.getOrderNo(),
				refundApply.getUserAlipayAccount(), refundApply.getUserAlipayRealName());
		try {
			orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.REFUNDING.getCode());
			tradeAlipayService
					.transfer(refundApply.getRefundNo(), "订单" + refundApply.getOrderNo() + "退款", refundApply.getAmount()
							, refundApply.getUserAlipayAccount(), refundApply.getUserAlipayRealName(),
							refundApply.getSysUserId());
		} catch (Exception e) {
			log.error("打款失败！！！", e);
			orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.REFUND_FAIL.getCode());
			return false;
		}
		log.info("打款成功，orderNo：{}", orderBase.getOrderNo());
		orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.REFUNDED.getCode());
		return true;
	}

	@Override
	public boolean refund(OrderBase orderBase) {
		log.info("=====>>> 执行退款操作，orderNo：{}", orderBase.getOrderNo());
		orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.REFUNDING.getCode());
		try {
			TradeBase tradeBase =
					tradeBaseService.selectOne(new EntityWrapper<TradeBase>().eq("order_no", orderBase.getOrderNo())
							.eq("status", TradeStatus.SUCCESS.getCode()).orderBy("ctime", false).last("limit 1"));
			if (tradeBase == null) {
				LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
			}
			switch (PayType.getPayType(orderBase.getPayType())) {
				case WX_JSAPI:
				case WX_APP:
				case WEIXINPAY:
				case WX_H5:
					tradeWeixinService.refund(orderBase, tradeBase);
					break;
				case ALIPAY:
					tradeAlipayService.refund(orderBase, tradeBase);
					break;
				case CARD:
					userChargeCardService.refund(orderBase, tradeBase);
					break;
				case BALANCE:
				case DISCOUNT:
				case BALANCE_DISCOUNT:
					userWalletService.refund(orderBase, tradeBase);
					break;
			}
		} catch (Exception e) {
			log.error("退款失败！！！", e);
			orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.REFUND_FAIL.getCode());
			return false;
		}
		log.info("退款成功，orderNo：{}", orderBase.getOrderNo());
		orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.REFUNDED.getCode());
		return true;
	}

	public String generateRefundNo() {
		return RandomStringUtils.randomNumeric(19);
	}

	public RefundApply getByOrderNo(String orderNo, RefundStatus... status) {
		Wrapper<RefundApply> wrapper = new EntityWrapper<RefundApply>().eq("order_no", orderNo);
		if(status!=null && status.length>0) {
			Object[] statusCode = Arrays.stream(status).map(RefundStatus::getCode).toArray();
			wrapper.in("status", statusCode);
		}
		return selectOne(wrapper.orderBy("ctime").last("limit 1"));
	}


}
