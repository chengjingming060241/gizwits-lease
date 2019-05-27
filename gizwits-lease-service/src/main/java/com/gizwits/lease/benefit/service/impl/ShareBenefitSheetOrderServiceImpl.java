package com.gizwits.lease.benefit.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.benefit.dao.ShareBenefitSheetOrderDao;
import com.gizwits.lease.benefit.dto.ShareBenefitSheetForQueryDto;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.benefit.entity.ShareBenefitSheetOrder;
import com.gizwits.lease.benefit.service.ShareBenefitSheetOrderService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.constant.BaseEnum;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.SheetOrderStatusType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.dto.SheetOrderForListDto;
import com.gizwits.lease.order.entity.dto.SheetOrderForQueryDto;
import com.gizwits.lease.order.service.OrderBaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 所有要参与分润的订单 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@Service
public class ShareBenefitSheetOrderServiceImpl extends ServiceImpl<ShareBenefitSheetOrderDao, ShareBenefitSheetOrder> implements ShareBenefitSheetOrderService {

    private static Logger logger = LoggerFactory.getLogger("BENEFIT_LOGGER");

    private static Integer batchSize = 60;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private ShareBenefitSheetOrderDao shareBenefitSheetOrderDao;

    @Override
    public Page<SheetOrderForListDto> page(Pageable<SheetOrderForQueryDto> pageable) {
        prePage(pageable.getQuery());
        Page<ShareBenefitSheetOrder> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<ShareBenefitSheetOrder> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<SheetOrderForListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            List<String> orderNoList = selectPage.getRecords().stream().map(item -> item.getOrderNo()).collect(Collectors.toList());
            List<OrderBase> orderList = orderBaseService.selectBatchIds(orderNoList);
            Map<String, OrderBase> orderBaseMap = orderList.stream().collect(Collectors.toMap(order -> order.getOrderNo(), order -> order));
            List<String> deviceSnoList = orderList.stream().map(order -> order.getSno()).distinct().collect(Collectors.toList());
            List<Device> deviceList = deviceService.selectBatchIds(deviceSnoList);
            Map<String, Device> deviceMap = deviceList.stream().collect(Collectors.toMap(device -> device.getSno(), device -> device));

            result.setRecords(selectPage.getRecords().stream().map(item -> {
                OrderBase orderBase = orderBaseMap.get(item.getOrderNo());
                SheetOrderForListDto dto = new SheetOrderForListDto(orderBase);
                dto.setPayTypeDesc(PayType.getName(orderBase.getPayType()));
                dto.setStatusDesc(OrderStatus.getMsg(orderBase.getOrderStatus()));
                dto.setSheetNo(item.getSheetNo());
                dto.setId(item.getId());
                dto.setSharePercentage(item.getSharePercentage()-item.getChildrenSharePercentage());
                Device device = deviceMap.get(orderBase.getSno());
                if (Objects.nonNull(device)) {
                    dto.setDeviceLaunchAreaName(device.getLaunchAreaName());
                }
                dto.setSelected(!Objects.equals(item.getStatus(), SheetOrderStatusType.TO_AUDIT.getCode()));
                return dto;
            }).collect(Collectors.toList()));
        }
        return result;
    }

    private void prePage(SheetOrderForQueryDto dto) {
        ShareBenefitSheetForQueryDto query = new ShareBenefitSheetForQueryDto();
        query.setSheetNo(dto.getSheetNo());
        query.setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        ShareBenefitSheet shareBenefitSheet = shareBenefitSheetService.selectOne(QueryResolverUtils.parse(query, new EntityWrapper<>()));
        if (Objects.isNull(shareBenefitSheet)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
    }

    public boolean updateSheetOrderStatus(String sheetNo,Integer status,Integer preStatus){
        return shareBenefitSheetOrderDao.updateOrderStatusBySheetNo(sheetNo, status, preStatus) > 0;
    }

    public boolean checkOrderIsShareForRule(String ruleId, String orderNo, String sno){
        if(ParamUtil.isNullOrEmptyOrZero(ruleId) || ParamUtil.isNullOrEmptyOrZero(orderNo) || ParamUtil.isNullOrEmptyOrZero(sno)){
            logger.error("=====判断订单{}是否已给当前设备{}分润{}参数异常=====",orderNo,sno,ruleId);
            return false;
        }
        return CollectionUtils.isNotEmpty(shareBenefitSheetOrderDao.selectSheetOrderByRuleIdAndSnoAndOrderNo(ruleId, orderNo, sno));
    }

    public BigDecimal sumSheetOrderMoney(String sheetNo, List<Integer> excludeOrderList){
        if(StringUtils.isBlank(sheetNo)){
            return BigDecimal.ZERO;
        }
        return shareBenefitSheetOrderDao.sumSheetOrderMoney(sheetNo,excludeOrderList);
    }

    @Override
    public ShareBenefitSheetOrder sumOrderAmountAndShareMoney(String sheetNo, List<String> orderNoList) {
        return shareBenefitSheetOrderDao.selectSumOrderAmountAndShareMoney(sheetNo, orderNoList);
    }

    public BigDecimal sumSheetOrderTotalMoney(String sheetNo){
        if(StringUtils.isBlank(sheetNo)){
            return BigDecimal.ZERO;
        }
        return shareBenefitSheetOrderDao.sumSheetOrderTotalMoney(sheetNo);
    }
    /**
     * 根据分润单号查询所有的订单
     * @param sheetNo
     * @return
     */
    @Override
    public List<ShareBenefitSheetOrder> getShareBenefitSheerOrderBySheetNo(String sheetNo){
        if (StringUtils.isEmpty(sheetNo)){
            return null;
        }
        EntityWrapper<ShareBenefitSheetOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sheet_no", sheetNo);
        return selectList(entityWrapper);
    }

    /**
     * 批量修改分润订单的状态
     * @param status
     * @param sheetNo
     * @param sheetOrderList
     * @return
     */
    @Override
    public boolean updateBatchSheetOrderListStatus(Integer status, String sheetNo, List<ShareBenefitSheetOrder> sheetOrderList){
        if (CollectionUtils.isEmpty(sheetOrderList)){
            return false;
        }
        if (sheetOrderList.size()<=batchSize){
            return shareBenefitSheetOrderDao.updateBatchSheetOrderStatus(status, sheetNo, sheetOrderList)>0;
        }
        int sheetOrderCount = sheetOrderList.size();
        for (int i=0; i<sheetOrderCount; ){
            if ((i+ batchSize) <= sheetOrderCount){
                List<ShareBenefitSheetOrder> subList = sheetOrderList.subList(i, batchSize);
                if (CollectionUtils.isEmpty(subList)){
                    return true;
                }

                shareBenefitSheetOrderDao.updateBatchSheetOrderStatus(status, sheetNo, subList);
                i += batchSize;
            }else{
                List<ShareBenefitSheetOrder> subList = sheetOrderList.subList(i, sheetOrderCount);
                if (CollectionUtils.isEmpty(subList)){
                    return true;
                }
                shareBenefitSheetOrderDao.updateBatchSheetOrderStatus(status, sheetNo, subList);
                i = sheetOrderCount;
            }
        }
        return true;
    }

    /**
     * 修改分润单中所有订单的状态
     * @param sheetNo
     * @param toStatus
     * @return
     */
    @Override
    public boolean updateAllSheetOrderStatus(String sheetNo, Integer toStatus){
        return shareBenefitSheetOrderDao.updateAllSheetOrderStatus(toStatus,sheetNo)>0;
    }
}
