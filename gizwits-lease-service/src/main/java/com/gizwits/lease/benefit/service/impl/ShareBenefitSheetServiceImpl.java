package com.gizwits.lease.benefit.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.*;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.benefit.dao.ShareBenefitSheetDao;
import com.gizwits.lease.benefit.dto.*;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.benefit.entity.ShareBenefitSheetOrder;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailDeviceService;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetOrderService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ShareBenefitRuleFrequency;
import com.gizwits.lease.enums.ShareBenefitSheetActionType;
import com.gizwits.lease.enums.ShareBenefitSheetStatusType;
import com.gizwits.lease.enums.SheetOrderStatusType;
import com.gizwits.lease.event.ShareBenefitSheetActionEvent;
import com.gizwits.lease.event.source.ShareBenefitSheetActionSource;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.message.service.SysMessageService;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.util.WxUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 分润账单表 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@Service
public class ShareBenefitSheetServiceImpl extends ServiceImpl<ShareBenefitSheetDao, ShareBenefitSheet> implements ShareBenefitSheetService {

    private Logger logger = LoggerFactory.getLogger("ORDER_LOGGER");

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitSheetOrderService shareBenefitSheetOrderService;

    @Autowired
    private ShareBenefitRuleDetailDeviceService shareBenefitRuleDetailDeviceService;

    @Autowired
    private ShareBenefitSheetDao shareBenefitSheetDao;

    @Autowired
    private SysMessageService sysMessageService;


    @Override
    public Double total(ShareBenefitSheetForQueryDto queryDto) {
        if (!ParamUtil.isNullOrEmptyOrZero(queryDto.getStatus())) {
            if (queryDto.getStatus().equals(ShareBenefitSheetStatusType.TO_SHARE.getCode())) {
                queryDto.setQueryStatus(Arrays.asList(ShareBenefitSheetStatusType.TO_SHARE.getCode(), ShareBenefitSheetStatusType.SHARE_FAILED.getCode(), ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode()));
            } else {
                queryDto.setQueryStatus(Collections.singletonList(queryDto.getStatus()));
            }
        }
        return shareBenefitSheetDao.countTotal(queryDto.getAccessableUserIds(),queryDto.getStartTime(),queryDto.getEndTime());
    }

    @Override
    public Page<ShareBenefitSheetForListDto> page(Pageable<ShareBenefitSheetForQueryDto> pageable) {
        if (!ParamUtil.isNullOrEmptyOrZero(pageable.getQuery().getStatus())) {
            if (pageable.getQuery().getStatus().equals(ShareBenefitSheetStatusType.TO_SHARE.getCode())) {
                pageable.getQuery().setQueryStatus(Arrays.asList(ShareBenefitSheetStatusType.TO_SHARE.getCode(), ShareBenefitSheetStatusType.SHARE_FAILED.getCode(), ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode()));
            } else {
                pageable.getQuery().setQueryStatus(Arrays.asList(pageable.getQuery().getStatus()));
            }
        }
        pageable.setOrderByField("ctime");
        pageable.setAsc(false);
        Page<ShareBenefitSheet> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<ShareBenefitSheet> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<ShareBenefitSheet>().orderBy("ctime", false)));
        Page<ShareBenefitSheetForListDto> result = new Page<>();
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
            result.setTotal(selectPage.getTotal());
            result.setCurrent(selectPage.getCurrent());
            selectPage.getRecords().forEach(item -> {
                ShareBenefitSheetForListDto dto = new ShareBenefitSheetForListDto(item);
                dto.setStatusDesc(ShareBenefitSheetStatusType.getDesc(item.getStatus()));
                result.getRecords().add(dto);
            });
        }
        return result;
    }

    @Override
    public ShareBenefitSheetForDetailDto detail(ShareBenefitSheetForQueryDto query) {
        ShareBenefitSheet shareBenefitSheet = selectOne(QueryResolverUtils.parse(query, new EntityWrapper<>()));
        if (Objects.isNull(shareBenefitSheet)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        ShareBenefitSheetForDetailDto result = new ShareBenefitSheetForDetailDto(shareBenefitSheet);
        result.setStatusDesc(ShareBenefitSheetStatusType.getDesc(shareBenefitSheet.getStatus()));
        result.setPayTypeDesc(PayType.getName(shareBenefitSheet.getPayType()));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShareBenefitSheetForDetailDto audit(ShareBenefitSheetForAuditDto dto) {
        /** 优先处理批量审核的操作 */
        if (CollectionUtils.isNotEmpty(dto.getBatchSheetIds())) {
            List<ShareBenefitSheet> sheetList = checkAndGetShareSheet(dto, ShareBenefitSheetStatusType.TO_AUDIT);
            for (ShareBenefitSheet benefitSheet : sheetList) {
                List<ShareBenefitSheetOrder> orderList = shareBenefitSheetOrderService.getShareBenefitSheerOrderBySheetNo(benefitSheet.getSheetNo());
                updateShareBenefitSheet(benefitSheet, orderList);
                updateShareBenefitSheetOrders(orderList, benefitSheet.getSheetNo());
                publishEvent(benefitSheet.getId(), ShareBenefitSheetActionType.AUDIT_PASS.getCode(), sysUserService.getCurrentUser().getId());
            }
            return null;
        }
        //1.分润单状态必须为待分润
        ShareBenefitSheet shareBenefitSheet = resolveShareSheet(dto, ShareBenefitSheetStatusType.TO_AUDIT);
        //2.更新分润单金额和参与分润的订单数量
        List<ShareBenefitSheetOrder> sheetOrders = resolveSheetOrders(shareBenefitSheet, dto);
        updateShareBenefitSheet(shareBenefitSheet, sheetOrders);
        //3.更新参与分润的订单的状态
        updateShareBenefitSheetOrders(sheetOrders, shareBenefitSheet.getSheetNo());
        //4.发送审核通过的事件
        publishEvent(shareBenefitSheet.getId(), ShareBenefitSheetActionType.AUDIT_PASS.getCode(), sysUserService.getCurrentUser().getId());

        //5.返回最新数据
        ShareBenefitSheetForQueryDto query = new ShareBenefitSheetForQueryDto();
        query.setId(shareBenefitSheet.getId());
        return detail(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShareBenefitSheetForDetailDto reaudit(ShareBenefitSheetForAuditDto dto) {
        /** 优先处理批量审核的操作 */
        if (CollectionUtils.isNotEmpty(dto.getBatchSheetIds())) {
            List<ShareBenefitSheet> sheetList = checkAndGetShareSheet(dto, ShareBenefitSheetStatusType.TO_SHARE);
            for (ShareBenefitSheet benefitSheet : sheetList) {
                updateBenefitSheetToAudit(benefitSheet);
                updateBenefitSheetOrdersToAudit(benefitSheet);
                publishEvent(benefitSheet.getId(), ShareBenefitSheetActionType.REAUDIT.getCode(), sysUserService.getCurrentUser().getId());
            }
            return null;
        }

        ShareBenefitSheet shareBenefitSheet = resolveShareSheet(dto, ShareBenefitSheetStatusType.TO_SHARE);
        if (shareBenefitSheet.getStatus().equals(ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode())) {
            LeaseException.throwSystemException(LeaseExceEnums.SHARE_FAIL_BUT_NOT_CHANGE);
        }
        //1.更新分润单状态为待审核
        updateBenefitSheetToAudit(shareBenefitSheet);
        //2.更新之前选择的参与订单为待审核
        updateBenefitSheetOrdersToAudit(shareBenefitSheet);
        //3.发送重新审核事件
        publishEvent(shareBenefitSheet.getId(), ShareBenefitSheetActionType.REAUDIT.getCode(), sysUserService.getCurrentUser().getId());
        //4.返回最新数据
        ShareBenefitSheetForQueryDto query = new ShareBenefitSheetForQueryDto();
        query.setId(shareBenefitSheet.getId());
        return detail(query);
    }

    private void updateBenefitSheetOrdersToAudit(ShareBenefitSheet shareBenefitSheet) {
        shareBenefitSheetOrderService.updateAllSheetOrderStatus(shareBenefitSheet.getSheetNo(), SheetOrderStatusType.TO_AUDIT.getCode());

//        List<ShareBenefitSheetOrder> orders = shareBenefitSheetOrderService.selectList(new EntityWrapper<ShareBenefitSheetOrder>()
//                .eq("sheet_no", shareBenefitSheet.getSheetNo())
//                .in("status", Arrays.asList(SheetOrderStatusType.AUDIT_PASSED.getCode(), SheetOrderStatusType.AUDIT_NOT_PASSED.getCode())));
//        if (CollectionUtils.isNotEmpty(orders)) {
//            orders.forEach(item -> item.setStatus(SheetOrderStatusType.TO_AUDIT.getCode()));
//            shareBenefitSheetOrderService.updateBatchById(orders);
//        }
    }

    private void updateBenefitSheetToAudit(ShareBenefitSheet shareBenefitSheet) {
        ShareBenefitSheet toUpdateSheet = new ShareBenefitSheet();
        toUpdateSheet.setId(shareBenefitSheet.getId());
        toUpdateSheet.setStatus(ShareBenefitSheetStatusType.TO_AUDIT.getCode());

        // reset order count and money
        Wrapper wrapper = new EntityWrapper<ShareBenefitSheetOrder>().eq("sheet_no", shareBenefitSheet.getSheetNo());
        List<ShareBenefitSheetOrder> sheetOrders = shareBenefitSheetOrderService.selectList(wrapper);
        toUpdateSheet.setOrderCount(sheetOrders.size());
        toUpdateSheet.setTotalMoney(sheetOrders.stream().mapToDouble(ShareBenefitSheetOrder::getOrderAmount).sum());
        toUpdateSheet.setShareMoney(sheetOrders.stream().mapToDouble(ShareBenefitSheetOrder::getShareMoney).sum());

        toUpdateSheet.setUtime(new Date());
        toUpdateSheet.setAuditTime(new Date());
        //更新订单数量和分润金额
        BigDecimal shareMoney = shareBenefitSheetOrderService.sumSheetOrderMoney(shareBenefitSheet.getSheetNo(),null);
        int orderCount = shareBenefitSheetOrderService.selectCount(new EntityWrapper<ShareBenefitSheetOrder>().eq("sheet_no",shareBenefitSheet.getSheetNo()));
        BigDecimal totalMoney = shareBenefitSheetOrderService.sumSheetOrderTotalMoney(shareBenefitSheet.getSheetNo());
        toUpdateSheet.setShareMoney(shareMoney.doubleValue());
        toUpdateSheet.setOrderCount(orderCount);
        toUpdateSheet.setTotalMoney(totalMoney.doubleValue());
        updateById(toUpdateSheet);
    }

    private void publishEvent(Integer sheetId, Integer actionType, Integer userId) {
        CommonEventPublisherUtils.publishEvent(new ShareBenefitSheetActionEvent(new ShareBenefitSheetActionSource(sheetId, actionType, userId)));
    }

    private void updateShareBenefitSheetOrders(List<ShareBenefitSheetOrder> sheetOrders, String sheetNo) {
        List<ShareBenefitSheetOrder> forUpdate = new ArrayList<>(sheetOrders);
        sheetOrders.forEach(item -> {
            item.setStatus(SheetOrderStatusType.AUDIT_PASSED.getCode());
            item.setUtime(new Date());
        });
//        List<ShareBenefitSheetOrder> unPassOrders = shareBenefitSheetOrderService.selectList(new EntityWrapper<ShareBenefitSheetOrder>()
//                .eq("sheet_no", sheetNo).notIn("id", sheetOrders.stream().map(ShareBenefitSheetOrder::getId).collect(Collectors.toList())));
//        if (CollectionUtils.isNotEmpty(unPassOrders)) {
//            unPassOrders.forEach(item -> {
//                item.setUtime(new Date());
//                item.setStatus(SheetOrderStatusType.AUDIT_NOT_PASSED.getCode());
//            });
//            //forUpdate.addAll(unPassOrders);
//        }
        shareBenefitSheetOrderService.updateSheetOrderStatus(sheetNo, SheetOrderStatusType.AUDIT_NOT_PASSED.getCode(), SheetOrderStatusType.TO_AUDIT.getCode());
        shareBenefitSheetOrderService.updateBatchSheetOrderListStatus(SheetOrderStatusType.AUDIT_PASSED.getCode(), sheetNo, forUpdate);
        //shareBenefitSheetOrderService.updateBatchSheetOrderListStatus(SheetOrderStatusType.AUDIT_NOT_PASSED.getCode(), sheetNo, unPassOrders);
        //shareBenefitSheetOrderService.updateBatchById(forUpdate);
    }

    private void updateShareBenefitSheet(ShareBenefitSheet shareBenefitSheet, List<ShareBenefitSheetOrder> sheetOrders) {
        SysUser current = sysUserService.getCurrentUser();
        shareBenefitSheet.setOrderCount(sheetOrders.size());

        List<String> orderNoList = sheetOrders.stream().map(item -> item.getOrderNo()).collect(Collectors.toList());
        ShareBenefitSheetOrder total = shareBenefitSheetOrderService.sumOrderAmountAndShareMoney(shareBenefitSheet.getSheetNo(), orderNoList);
        Double totalMoney = 0.0;
        if (total.getOrderAmount() != null) {
            totalMoney = new BigDecimal(total.getOrderAmount().toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        Double shareMoney = 0.0;
        if (total.getShareMoney() != null) {
            shareMoney = new BigDecimal(total.getShareMoney().toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        
        shareBenefitSheet.setTotalMoney(totalMoney);
        shareBenefitSheet.setShareMoney(shareMoney);
        shareBenefitSheet.setStatus(ShareBenefitSheetStatusType.TO_SHARE.getCode());
        shareBenefitSheet.setUtime(new Date());
        shareBenefitSheet.setAuditTime(new Date());
//        shareBenefitSheet.setAuditId(current.getId());
//        shareBenefitSheet.setAuditName(current.getUsername());
        updateById(shareBenefitSheet);
    }

    private List<ShareBenefitSheetOrder> resolveSheetOrders(ShareBenefitSheet shareBenefitSheet, ShareBenefitSheetForAuditDto dto) {
        //1.根据前台传递的参与分润的订单id进行查询，且该订单的状态必须为待审核
        ShareBenefitSheetOrderForQueryDto query = new ShareBenefitSheetOrderForQueryDto();
        query.setSheetNo(shareBenefitSheet.getSheetNo());
        query.setIds(dto.getSheetOrderIds());
        query.setStatus(SheetOrderStatusType.TO_AUDIT.getCode());
        EntityWrapper<ShareBenefitSheetOrder> sheetOrderEntityWrapper = new EntityWrapper<>();
        sheetOrderEntityWrapper.eq("sheet_no", shareBenefitSheet.getSheetNo());
        List<ShareBenefitSheetOrder> dbSheetOrders = shareBenefitSheetOrderService.selectList(sheetOrderEntityWrapper);

        if (CollectionUtils.isNotEmpty(dbSheetOrders) && CollectionUtils.isNotEmpty(dto.getSheetOrderIds())) {
            Set<Integer> dbSheetOrderIds = new HashSet<>();
            for (Integer orderId : dto.getSheetOrderIds()) {
                dbSheetOrderIds.add(orderId);
            }

            List<ShareBenefitSheetOrder> resultList = new ArrayList<>();
            for (ShareBenefitSheetOrder order : dbSheetOrders) {
                if (dbSheetOrderIds.contains(order.getId())) {
                    resultList.add(order);
                }
            }

            return resultList;
        }

        return null;
    }

    private List<ShareBenefitSheet> checkAndGetShareSheet(ShareBenefitSheetForAuditDto dto, ShareBenefitSheetStatusType statusType) {
        EntityWrapper<ShareBenefitSheet> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("id", dto.getBatchSheetIds()).in("sys_account_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        entityWrapper.eq("status", statusType.getCode());
        List<ShareBenefitSheet> resultList = selectList(entityWrapper);
        if (CollectionUtils.isEmpty(resultList) || dto.getBatchSheetIds().size() != resultList.size()) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR, ",您无权查看分润单");
        }
        return resultList;
    }

    private ShareBenefitSheet resolveShareSheet(ShareBenefitSheetForAuditDto dto, ShareBenefitSheetStatusType statusType) {
        ShareBenefitSheetForQueryDto query = new ShareBenefitSheetForQueryDto();
        query.setId(dto.getSheetId());
        query.setStatus(statusType.getCode());
        query.setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        ShareBenefitSheet shareBenefitSheet = selectOne(QueryResolverUtils.parse(query, new EntityWrapper<>()));
        if (Objects.isNull(shareBenefitSheet)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        return shareBenefitSheet;
    }

    /**
     * 执行分润操作
     *
     * @param query
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public synchronized ResponseObject executeShareBenefit(ShareBenefitSheetForQueryDto query) {
        List<Integer> queryStatus = new ArrayList<>();
        queryStatus.add(ShareBenefitSheetStatusType.TO_SHARE.getCode());
        queryStatus.add(ShareBenefitSheetStatusType.SHARE_FAILED.getCode());
        queryStatus.add(ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode());
        query.setQueryStatus(queryStatus);

        List<ShareBenefitSheet> sheetList = selectList(QueryResolverUtils.parse(query, new EntityWrapper<>()));
        if (CollectionUtils.isEmpty(sheetList)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        StringBuffer resultBuffer = new StringBuffer("分润执行结果:\\n");
        Integer shareFailCount = 0;
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (httpRequest == null) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        String ip = WxUtil.getIpAddr(httpRequest);
        boolean isMatch = Pattern.matches("/^(?:(?:2[0-4][0-9]\\.)|(?:25[0-5]\\.)|(?:1[0-9][0-9]\\.)|(?:[1-9][0-9]\\.)|(?:[0-9]\\.)){3}(?:(?:2[0-5][0-5])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$/", ip);
        if (!isMatch) {
            ip = "127.0.0.1";
        }

        for (ShareBenefitSheet benefitSheet : sheetList) {
            if (PayType.WEIXINPAY.getCode().equals(benefitSheet.getPayType())) {
                SysUserExt sysUserExt = sysUserExtService.selectById(benefitSheet.getPayAccount());
                if (sysUserExt == null) {
                    logger.error("=====>>> 分润单[" + benefitSheet.getId() + "] 关联的微信公众号配置为空,不能进行分润");
                    resultBuffer.append("分润单").append(benefitSheet.getSheetNo()).append("分润失败,原因:分润的的支付账户").append(benefitSheet.getPayAccount()).append("未配置;\\n");
                    shareFailCount++;
                }
                try {
                    //生成交易单号
                    if (!benefitSheet.getIsTryAgain().equals(ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode())) {
                        benefitSheet.setTradeNo(IdGenerator.generateTradeNo(benefitSheet.getSheetNo()));
                    }
                    //修改分润单状态为执行中
                    updateSheetStatus(benefitSheet, ShareBenefitSheetStatusType.SHARING.getCode(),
                            SheetOrderStatusType.SHARING.getCode(), SheetOrderStatusType.AUDIT_PASSED.getCode());

                    String resultStr = WxUtil.postShareBenefitOrder(benefitSheet, ip, sysUserExt, sysUserService.getCurrentUser().getId());
                    //保存分润结果
                    saveAndUpdateShareBenefitResult(benefitSheet, resultStr);

                    if (WxUtil.checkShareBenefitResult(resultStr) != ShareBenefitSheetStatusType.SHARE_SUCCESS.getCode()) {
                        Map<String, String> resultMap = WxUtil.parseXmlToMap(resultStr);
                        shareFailCount++;
                        resultBuffer.append("分润单").append(benefitSheet.getSheetNo()).append("分润失败,原因:").append(resultMap.get("err_code")).append(resultMap.get("err_code_des")).append("\\n");
                    }

                } catch (Exception ex) {
                    shareFailCount++;
                    updateSheetStatus(benefitSheet, ShareBenefitSheetStatusType.SHARE_FAILED.getCode(),
                            SheetOrderStatusType.SHARING.getCode(), SheetOrderStatusType.SHARING.getCode());

                    return new ResponseObject(LeaseExceEnums.SHARE_ACTION_FAIL.getCode(), LeaseExceEnums.SHARE_ACTION_FAIL.getMessage());
                }
            }
        }
        if (shareFailCount > 0) {
            resultBuffer.append("分润失败").append(shareFailCount).append("个分润单,具体失败原因如上所示\\n");
        } else {
            resultBuffer.append("分润成功!");
        }
        return new ResponseObject(shareFailCount > 0 ? (shareFailCount + "") : "200", resultBuffer.toString());
    }

    private boolean updateSheetStatus(ShareBenefitSheet shareBenefitSheet, Integer sheetStatus, Integer orderStatus, Integer orderPreStatus) {
        if (shareBenefitSheet == null) {
            return false;
        }
        Date now = new Date();
        shareBenefitSheet.setStatus(sheetStatus);
        shareBenefitSheet.setUtime(now);
        shareBenefitSheetOrderService.updateSheetOrderStatus(shareBenefitSheet.getSheetNo(), orderStatus, orderPreStatus);
        updateById(shareBenefitSheet);
        return true;
    }

    private boolean saveAndUpdateShareBenefitResult(ShareBenefitSheet shareBenefitSheet, String resultStr) {
        if (StringUtils.isBlank(resultStr)) {
            return false;
        }
        Map<String, String> resultMap = WxUtil.parseXmlToMap(resultStr);
        if (WxUtil.checkShareBenefitResult(resultStr) == ShareBenefitSheetStatusType.SHARE_SUCCESS.getCode()) {
            String tradeNO = resultMap.get("partner_trade_no");
            String paymentNo = resultMap.get("payment_no");
            String paymentTime = resultMap.get("payment_time");
            if (!shareBenefitSheet.getTradeNo().equals(tradeNO)) {
                logger.error("====>>>> 分润结果中的tradeNo[" + tradeNO + "]与分润单中的tradeNo[" + shareBenefitSheet.getTradeNo() + "]不匹配");
                return false;
            }
            shareBenefitSheet.setPaymentNo(paymentNo);
            shareBenefitSheet.setPaymentTime(DateKit.formatString2DateByDateTimePattern(paymentTime));
            return updateSheetStatus(shareBenefitSheet, ShareBenefitSheetStatusType.SHARE_SUCCESS.getCode(),
                    SheetOrderStatusType.SHARE_SUCCESS.getCode(), SheetOrderStatusType.SHARING.getCode());

        } else if (WxUtil.checkShareBenefitResult(resultStr) == ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode()) {
            shareBenefitSheet.setIsTryAgain(ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode());
            return updateSheetStatus(shareBenefitSheet, ShareBenefitSheetStatusType.TO_SHARE.getCode(),
                    SheetOrderStatusType.AUDIT_PASSED.getCode(), SheetOrderStatusType.SHARING.getCode());

        } else if (WxUtil.checkShareBenefitResult(resultStr) == ShareBenefitSheetStatusType.SHARE_FAILED.getCode()) {
            shareBenefitSheet.setIsTryAgain(ShareBenefitSheetStatusType.CREATED.getCode());
            return updateSheetStatus(shareBenefitSheet, ShareBenefitSheetStatusType.SHARE_FAILED.getCode(),
                    SheetOrderStatusType.AUDIT_PASSED.getCode(), SheetOrderStatusType.SHARING.getCode());

        }

        return false;
    }

    /**
     * 对一个分润规则执行分润,不用判断是否到了执行时间
     *
     * @param rule
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean generateShareBenefitForShareRule(ShareBenefitRule rule) {
        logger.info("======generateShareBenefitForShareRule : {}",rule.getId());
        //运营者收款的对象
        SysUserExt shareMoneyAccount = sysUserExtService.selectById(rule.getSysAccountId());
        if (Objects.isNull(shareMoneyAccount)
                || Objects.isNull(shareMoneyAccount.getReceiverOpenId())
                || Objects.isNull(shareMoneyAccount.getReceiverWxName())) {
            logger.error("======分润规则{}的收账用户{}的收账微信配置信息不正确,不能正常生成分润单=====", rule.getId(), rule.getSysAccountId());
            return false;
        }
        //独立运营者的配置信息
        SysUserExt independenceOperatorUser = null;

        ShareBenefitSheet sheet = new ShareBenefitSheet();
        sheet.setSheetNo(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
        sheet.setTotalMoney(0.00D);
        sheet.setShareMoney(0.00D);
//        sheet.setWxTotalMoney(0.00D);
//        sheet.setAlipayTotalMoney(0.00D);
//        sheet.setCardTotalMoney(0.00D);
//        sheet.setOtherTotalMoney(0.00D);
        sheet.setRuleId(rule.getId());
        List<ShareBenefitSheetOrder> sheetOrderList = new ArrayList<>();
        Integer orderCount = 0;

        //获取分润规则下的所有参与分润的设备
        List<ShareBenefitRuleDeviceVo> ruleDetailDeviceList = shareBenefitRuleDetailDeviceService.getAllDeviceShareRuleByRuleId(rule.getId());
        if (CollectionUtils.isNotEmpty(ruleDetailDeviceList)) {

            //循环设备列表,根据设备获取设备上未分润的订单
            for (ShareBenefitRuleDeviceVo deviceDto : ruleDetailDeviceList) {

                //获取独立运营者的配置信息
                if (Objects.isNull(independenceOperatorUser)) {
                    independenceOperatorUser = deviceService.getWxConfigByDeviceId(deviceDto.getSno());
                }

                Date lastExecuteTime = rule.getLastExecuteTime();
                if (Objects.isNull(lastExecuteTime)) {
                    lastExecuteTime = rule.getCtime();
                }
                List<OrderBase> orderList = orderBaseService.getReadyForShareBenefit(deviceDto.getSno(), lastExecuteTime);
                if (CollectionUtils.isNotEmpty(orderList)) {
                    orderCount += orderList.size();
                    sheet = generateSheetOrder(orderList, deviceDto, sheet.getSheetNo(), sheetOrderList, sheet);
                } else {
                    logger.warn("======分润规则{},的设备{}没有订单,不参与分润单生成====", rule.getId(), deviceDto.getSno());
                    continue;
                }
            }
            if (CollectionUtils.isEmpty(sheetOrderList)) {
                logger.warn("====分润规则{},中的设备都没有未分润的订单,所以不生成分润单===", rule.getId());
                return false;
            }
            if (sheet.getShareMoney().compareTo(0.00D) > 0 && sheet.getShareMoney().compareTo(0.01D) < 0) {
                logger.warn("====分润规则{},计算出来的总分润金额为{},分润接口限制最小为0.01,因此设置分润金额为0.01====", rule.getId(), sheet.getShareMoney());
                sheet.setShareMoney(0.01D);
            }
            sheet.setSysAccountId(rule.getSysAccountId());
            sheet.setOperatorName(rule.getOperatorName());
            sheet.setStatus(ShareBenefitSheetStatusType.TO_AUDIT.getCode());
            sheet.setPayType(PayType.WEIXINPAY.getCode());
           /* String shareBenefitPayType = SysConfigUtils.get(CommonSystemConfig.class).getShareBenefitPayType();
            if ("WEIXIN".equalsIgnoreCase(shareBenefitPayType) || PayType.WEIXINPAY.getCode().equals(shareBenefitPayType)) {
                sheet.setPayType(PayType.WEIXINPAY.getCode());
            }
            if ("ALIPAY".equalsIgnoreCase(shareBenefitPayType) || PayType.ALIPAY.getCode().equals(shareBenefitPayType)) {
                sheet.setPayType(PayType.ALIPAY.getCode());
            }*/
            sheet.setCtime(new Date());
            sheet.setPayAccount(independenceOperatorUser.getSysUserId() + "");
            sheet.setReceiverOpenid(shareMoneyAccount.getReceiverOpenId().trim());
            sheet.setReceiverName(shareMoneyAccount.getReceiverWxName().trim());
            sheet.setOrderCount(sheetOrderList.size());

            if (orderCount > 0
                    && insert(sheet)) {
                shareBenefitRuleService.updateRuleLastExecuteTime(rule.getId());
                shareBenefitSheetOrderService.insertBatch(sheetOrderList);
            }
        }else{
            logger.warn("====分润规则{},没有对应的设备====", rule.getId());
            return false;
        }
        return false;
    }

    /**
     * 对于有设备变动的运营者,直接执行运营者身上的分润规则
     *
     * @param deviceList
     * @param isUnbind   解绑操作需要删除解绑的运营者中的分润规则
     * @return
     */
    public boolean generateShareBenefitSheetForDevices(List<Device> deviceList, Boolean isUnbind) {
        if (CollectionUtils.isEmpty(deviceList)) {
            return false;
        }

        Map<Integer, Device> sysAccountMap = new HashedMap();
        deviceList.forEach(device -> {//合并设备中相同的运营者
            if (!sysAccountMap.containsKey(device.getOwnerId())) {
                sysAccountMap.put(device.getOwnerId(), device);
            }

        });

        Iterator<Integer> iterator = sysAccountMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer sysAccountId = iterator.next();
            ShareBenefitRule rule = shareBenefitRuleService.getRuleBySysAccountId(sysAccountId);
            if (Objects.nonNull(rule)) {
                logger.info("======generateShareBenefitSheetForDevices :{}",isUnbind);
                generateShareBenefitForShareRule(rule);
            }
        }

        //根据操作类型处理分润规则中的设备分润比例问题
        deviceList.forEach(device -> {
            if (isUnbind) {//解绑操作,需要将设备旧的运营者中的分润细则中删除
                ShareBenefitRule rule = shareBenefitRuleService.getRuleBySysAccountId(device.getOwnerId());
                if (Objects.nonNull(rule)) {
                    shareBenefitRuleDetailDeviceService.deletedDeviceRuleInShareRule(rule.getId(), device.getSno());
                }
            } else {//绑定操作,需要将设备旧有分润规则详情中的子运营者分润比例置为0
                ShareBenefitRule rule = shareBenefitRuleService.getRuleBySysAccountId(device.getOwnerId());
                if (Objects.nonNull(rule)) {
                    shareBenefitRuleDetailDeviceService.updateDeviceChildrenPercentage(device.getSno(), BigDecimal.ZERO, rule.getId());
                }
            }
        });

        return true;
    }

    @Override
    public ShareBenefitSheet generateSheetOrder(List<OrderBase> orderList, ShareBenefitRuleDeviceVo deviceVo, String sheetNo, List<ShareBenefitSheetOrder> sheetOrderList, ShareBenefitSheet sheet) {
        BigDecimal totalMoney = new BigDecimal(sheet.getTotalMoney());
        BigDecimal shareMoney = new BigDecimal(sheet.getShareMoney());
//        BigDecimal wxTotalMoney = new BigDecimal(sheet.getWxTotalMoney());
//        BigDecimal cardTotalMoney = new BigDecimal(sheet.getCardTotalMoney());
//        BigDecimal alipayTotalMoney = new BigDecimal(sheet.getAlipayTotalMoney());
//        BigDecimal otherTotalMoney = new BigDecimal(sheet.getOtherTotalMoney());

        for (OrderBase order : orderList) {
            if (shareBenefitSheetOrderService.checkOrderIsShareForRule(deviceVo.getRuleId(), order.getOrderNo(), deviceVo.getSno())) {
                logger.warn("=====设备{}上的订单{}已执行分润{}=====", deviceVo.getSno(), order.getOrderNo(), deviceVo.getRuleId());
                continue;
            }

            ShareBenefitSheetOrder sheetOrder = new ShareBenefitSheetOrder();
            sheetOrder.setSheetNo(sheetNo);
            sheetOrder.setOrderNo(order.getOrderNo());
            sheetOrder.setSysAccountId(deviceVo.getSysAccountId());
            sheetOrder.setDeviceSno(deviceVo.getSno());

            if (order.getPayType().equals(PayType.BALANCE.getCode())) {

                // 产于分润的订单金额为该用户实际充值金额/（该用户实际充值金额+赠送金额）*订单总额
                sheetOrder.setOrderAmount(order.getPromotionMoney());
            } else {
                sheetOrder.setOrderAmount(order.getAmount());
            }
            // sheetOrder.setOrderAmount(order.getAmount());
            sheetOrder.setShareRuleDetailId(deviceVo.getRuleDetailId());
            sheetOrder.setStatus(SheetOrderStatusType.TO_AUDIT.getCode());
            sheetOrder.setUtime(new Date());
            sheetOrder.setCtime(new Date());
            sheetOrder.setSysAccountId(order.getSysUserId());
            sheetOrder.setSharePercentage(deviceVo.getSharePercentage().doubleValue());
            sheetOrder.setChildrenSharePercentage(deviceVo.getChildrenPercentage().doubleValue());
            // 计算分润金额
            // sheetOrder.setShareMoney(calculateMoney(order.getAmount(), deviceVo.getSharePercentage(), deviceVo.getChildrenPercentage()));
            sheetOrder.setShareMoney(calculateMoney(sheetOrder.getOrderAmount(), deviceVo.getSharePercentage(), deviceVo.getChildrenPercentage()));

//            if (PayType.ALIPAY.getCode().equals(order.getPayType())) {
//                alipayTotalMoney = alipayTotalMoney.add(new BigDecimal(order.getAmount()));
//            } else if (PayType.WEIXINPAY.getCode().equals(order.getPayType()) || PayType.WX_APP.getCode().equals(order.getPayType())
//                    || PayType.WX_H5.getCode().equals(order.getPayType()) || PayType.WX_JSAPI.getCode().equals(order.getPayType())) {
//                wxTotalMoney = wxTotalMoney.add(new BigDecimal(order.getAmount()));
//            } else if (PayType.CARD.getCode().equals(order.getPayType())) {
//                cardTotalMoney = cardTotalMoney.add(new BigDecimal(order.getAmount()));
//            } else {
//                otherTotalMoney = otherTotalMoney.add(new BigDecimal(order.getAmount()));
//            }

            // totalMoney = totalMoney.add(new BigDecimal(order.getAmount()));
            totalMoney = totalMoney.add(new BigDecimal(sheetOrder.getOrderAmount()));
            shareMoney = shareMoney.add(new BigDecimal(sheetOrder.getShareMoney()));
            sheetOrderList.add(sheetOrder);
        }
//        sheet.setWxTotalMoney(wxTotalMoney.doubleValue());
//        sheet.setAlipayTotalMoney(alipayTotalMoney.doubleValue());
//        sheet.setCardTotalMoney(cardTotalMoney.doubleValue());
//        sheet.setOtherTotalMoney(otherTotalMoney.doubleValue());
        sheet.setTotalMoney(totalMoney.doubleValue());
        sheet.setShareMoney(shareMoney.doubleValue());
        return sheet;
    }

    private Double calculateMoney(Double money, BigDecimal sharePercentage, BigDecimal childrenPercentage) {
        if (Objects.isNull(childrenPercentage)) {
            childrenPercentage = BigDecimal.ZERO;
        }
        if (childrenPercentage.compareTo(sharePercentage) > 0) {
            logger.error("====分润规则设置错误,,分润比例{}小于子级分润比例{}====", sharePercentage.doubleValue(), childrenPercentage.doubleValue());
        }
        BigDecimal percentage = sharePercentage.subtract(childrenPercentage).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP);

        return percentage.multiply(new BigDecimal(money)).doubleValue();
    }

    public Map<String, Object> calculateSheetMoney(String sheetNo, List<Integer> excludeOrderList) {
        if (CollectionUtils.isEmpty(excludeOrderList)) {
            excludeOrderList = new ArrayList<>();
        }

        BigDecimal totalMoney = shareBenefitSheetOrderService.sumSheetOrderMoney(sheetNo, excludeOrderList);

        if (totalMoney == null) {
            totalMoney = BigDecimal.ZERO;
        }
        totalMoney = totalMoney.setScale(2, RoundingMode.HALF_UP);

        Map<String, Object> resultMap = new HashedMap();
        resultMap.put("totalMoney", totalMoney.doubleValue());
        resultMap.put("count", 0);

        EntityWrapper<ShareBenefitSheet> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sheet_no", sheetNo);
        ShareBenefitSheet sheet = selectOne(entityWrapper);
        if (Objects.nonNull(sheet)) {
            resultMap.put("count", sheet.getOrderCount() - excludeOrderList.size());
        }

        return resultMap;
    }

    public Map<String, List> groupOrderByStatus(String sheetNo) {
        if (StringUtils.isBlank(sheetNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        EntityWrapper<ShareBenefitSheetOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sheet_no", sheetNo);
        List<ShareBenefitSheetOrder> allOrder = shareBenefitSheetOrderService.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(allOrder)) {
            return null;
        }

        EntityWrapper<ShareBenefitSheet> shareBenefitSheetEntityWrapper = new EntityWrapper<>();
        shareBenefitSheetEntityWrapper.eq("sheet_no", sheetNo);
        ShareBenefitSheet sheet = selectOne(shareBenefitSheetEntityWrapper);


        Map<String, List> result = new HashedMap();
        List<Integer> noPassOrderList = new ArrayList<>();
        List<Integer> passOrderList = new ArrayList<>();
        if (sheet.getStatus().equals(ShareBenefitSheetStatusType.TO_SHARE.getCode())) {//审核通过待分润
            for (ShareBenefitSheetOrder order : allOrder) {
                if (order.getStatus().equals(SheetOrderStatusType.AUDIT_PASSED.getCode())) {
                    passOrderList.add(order.getId());
                } else {
                    noPassOrderList.add(order.getId());
                }
            }
        } else if (sheet.getStatus().equals(ShareBenefitSheetStatusType.TO_AUDIT.getCode())) {
            for (ShareBenefitSheetOrder order : allOrder) {
                passOrderList.add(order.getId());
            }
        } else if (sheet.getStatus().equals(ShareBenefitSheetStatusType.SHARE_FAILED.getCode())
                || sheet.getStatus().equals(ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode())) {
            for (ShareBenefitSheetOrder order : allOrder) {
                if (order.getStatus().equals(SheetOrderStatusType.AUDIT_NOT_PASSED.getCode())
                        || order.getStatus().equals(SheetOrderStatusType.TO_AUDIT.getCode())) {
                    noPassOrderList.add(order.getId());
                } else {
                    passOrderList.add(order.getId());
                }
            }
        }

        result.put("noPassList", noPassOrderList);
        result.put("passList", passOrderList);
        return result;
    }

    /**
     * 手动触发分润执行
     */
    @Override
    public void executeAllShareBenefit() {
        logger.info("====开始执行分润操作=====");
        List<ShareBenefitRule> allShareRuleList = shareBenefitRuleService.getAllUsableShareRule();
        logger.info("====分润规则个数{}=====", allShareRuleList.size());
        if (CollectionUtils.isNotEmpty(allShareRuleList)) {
            Date now = new Date();
            //循环可用的分润规则
            for (ShareBenefitRule rule : allShareRuleList) {
                if (!checkShareRuleIsRightTime(rule.getStartTime(), rule.getLastExecuteTime(), rule.getFrequency())) {
                    logger.info("======分润规则{},startTime:{},lastExecuteTime:{},frequency:{}不符合执行条件,暂不执行分润===", rule.getId(), rule.getStartTime(), rule.getLastExecuteTime(), rule.getFrequency());
                    continue;
                }
                logger.info("=====executeAllShareBenefit:{}",rule.getId());
                generateShareBenefitForShareRule(rule);
            }
        }
        sendExecuteShareBenefitSuccess();
    }

    private void sendExecuteShareBenefitSuccess() {
        String[] openidArr = SysConfigUtils.get(CommonSystemConfig.class).getShareBenefitSuccessNotifyOpenids().split(",");
        SysUserExt sysUserExt = sysUserExtService.getSysUserExtByWeixinAppid("wx986498e34607b4b6");
        if (Objects.nonNull(sysUserExt) && openidArr.length > 0) {
            JSONObject firstData = new JSONObject();
            firstData.put("value", "分润单生成功啦!");
            firstData.put("color", "#173177");

            JSONObject performanceData = new JSONObject();
            performanceData.put("value", "分润单生成执行成功了");
            performanceData.put("color", "#173177");

            JSONObject timeData = new JSONObject();
            timeData.put("value", "没有异常");
            timeData.put("color", "#173177");

            JSONObject remarkData = new JSONObject();
            remarkData.put("value", "不用处理");
            remarkData.put("color", "#173177");

            JSONObject body = new JSONObject();
            body.put("first", firstData);
            body.put("performance", performanceData);
            body.put("time", timeData);
            body.put("remark", remarkData);

            for (String openid : openidArr) {
                JSONObject sendData = new JSONObject();
                sendData.put("touser", openid);
                sendData.put("template_id", sysUserExt.getWxTemplateId());

                sendData.put("data", body);
                WxUtil.sendTemplateNotice(sendData.toJSONString(), sysUserExt);
            }
        }


    }


    /**
     * 判断分润规则是否到执行时间
     *
     * @param startTime
     * @param lastExecuteTime
     * @param frequency
     * @return
     */
    private boolean checkShareRuleIsRightTime(Date startTime, Date lastExecuteTime, String frequency) {
        if (startTime.after(new Date())) {//还未到结算时间
            return false;
        }
        if (Objects.isNull(lastExecuteTime)) {//还未执行过
            return true;
        }

        if (frequency.equals(ShareBenefitRuleFrequency.DAY.getCode())) {
            return true;
        } else if (frequency.equals(ShareBenefitRuleFrequency.WEEK.getCode())) {
            return equalDay(lastExecuteTime, Calendar.DAY_OF_WEEK);
        } else if (frequency.equals(ShareBenefitRuleFrequency.MONTH.getCode())) {
            return equalDay(lastExecuteTime, Calendar.DAY_OF_MONTH);
        } else if (frequency.equals(ShareBenefitRuleFrequency.YEAR.getCode())) {
            return equalDay(lastExecuteTime, Calendar.DAY_OF_YEAR);
        }

        return false;
    }

    private boolean equalDay(Date toEqualDate, int field) {
        if (Objects.isNull(toEqualDate)) {
            return true;
        }

        Calendar lastCal = Calendar.getInstance();
        lastCal.setTime(toEqualDate);
        int toEq = lastCal.get(field);

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(new Date());
        int nowWeekDay = nowCal.get(field);
        return toEq == nowWeekDay;
    }

    @Override
    public void updateReceiverInfo(String oldOpenId, String newOpenId, String oldReceiverWxName, String newReceiverWxName, Integer sysAccountId) {
        if (shareBenefitSheetChanged(oldOpenId, newOpenId, oldReceiverWxName, newReceiverWxName)) {
            ShareBenefitSheet shareBenefitSheet = new ShareBenefitSheet();
            shareBenefitSheet.setReceiverName(newReceiverWxName);
            shareBenefitSheet.setReceiverOpenid(newOpenId);
            Wrapper wrapper = new EntityWrapper<ShareBenefitSheet>();
            wrapper.eq("sys_account_id", sysAccountId);
            Integer[] status = {ShareBenefitSheetStatusType.CREATED.getCode(),
                    ShareBenefitSheetStatusType.TO_AUDIT.getCode(),
                    ShareBenefitSheetStatusType.TO_SHARE.getCode(),
                    ShareBenefitSheetStatusType.SHARING.getCode(),
                    ShareBenefitSheetStatusType.SHARE_FAILED.getCode(),
                    ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode()};
            wrapper.in("status", status);

            update(shareBenefitSheet, wrapper);
        }
    }

    private boolean shareBenefitSheetChanged(String oldOpenId, String newOpenId, String oldReceiverWxName, String newReceiverWxName) {
        //判断收款信息是否有更改
        //以前的不为空，现在的也不为空，并且两者不相同的时候就需要更新，其他的情况就不用更新了
        if(ParamUtil.isOneOfNullorEmpty(oldOpenId, newOpenId, oldReceiverWxName, newReceiverWxName)){
            return false;
        }

        return !Objects.equals(oldOpenId, newOpenId)
                || !Objects.equals(oldReceiverWxName, newReceiverWxName);
    }

}
