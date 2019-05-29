package com.gizwits.lease.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.IdGenerator;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.*;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.OrderStatusMap;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.constant.ThirdPartyUserType;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceExt;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.service.DeviceExtService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.vo.DeviceUsingVo;
import com.gizwits.lease.enums.OrderAbnormalReason;
import com.gizwits.lease.event.BindGizwitsDeviceEvent;
import com.gizwits.lease.event.WxPayCallbackEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.message.entity.SysMessage;
import com.gizwits.lease.message.service.SysMessageService;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.order.dto.*;
import com.gizwits.lease.order.dto.OrderBaseListDto;
import com.gizwits.lease.order.dto.OrderDetailDto;
import com.gizwits.lease.order.dto.OrderListDto;
import com.gizwits.lease.order.dto.OrderQueryByMobileDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.dto.PageOrderAppList;
import com.gizwits.lease.order.dto.PayOrderDto;
import com.gizwits.lease.order.dto.WXOrderListDto;
import com.gizwits.lease.order.dto.WXOrderQueryDto;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtByQuantity;
import com.gizwits.lease.order.entity.OrderExtByTime;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.gizwits.lease.order.service.*;
import com.gizwits.lease.order.vo.AppOrderDetailVo;
import com.gizwits.lease.order.vo.AppOrderVo;
import com.gizwits.lease.product.dto.ProductServiceModeDetailForAppDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import com.gizwits.lease.product.vo.AppProductServiceDetailVo;
import com.gizwits.lease.product.vo.ProductServiceDetailVo;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.trade.service.TradeAlipayService;
import com.gizwits.lease.trade.service.TradeWeixinService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserChargeCard;
import com.gizwits.lease.user.entity.UserWxExt;
import com.gizwits.lease.user.service.UserChargeCardService;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.user.service.UserWxExtService;
import com.gizwits.lease.util.GizwitsUtil;
import com.gizwits.lease.util.WxUtil;
import com.gizwits.lease.wallet.entity.UserWallet;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;
import com.gizwits.lease.wallet.service.UserWalletService;
import org.apache.commons.collections.CollectionUtils;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@Service
public class OrderBaseServiceImpl extends ServiceImpl<OrderBaseDao, OrderBase> implements OrderBaseService {

    private static Logger loggerOrder = LoggerFactory.getLogger("ORDER_LOGGER");

    @Autowired
    private OrderBaseDao orderBaseDao;

    @Autowired
    TradeWeixinService tradeWeixinService;

    @Autowired
    private OrderStatusFlowService orderStatusFlowService;

    @Autowired
    private OrderExtByQuantityService orderExtByQuantityService;

    @Autowired
    private ProductServiceDetailService productServiceDetailService;

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OrderPayRecordService orderPayRecordService;

    @Autowired
    private OrderExtByTimeService orderExtByTimeService;

    @Autowired
    TradeAlipayService tradeAlipayService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private UserChargeCardService userChargeCardService;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private OrderExtPortService orderExtPortService;

    @Autowired
    private UserWxExtService userWxExtService;

    @Autowired
    private DeviceExtService deviceExtService;

    @Autowired
    private UserWalletService userWalletService;

    @Override
    public OrderBase getOrderBaseByTradeNo(String tradeNo) {
        if (ParamUtil.isNullOrEmpty(tradeNo)) {
            return null;
        }
        return orderBaseDao.findByTradeNo(tradeNo);
    }

    /**
     * 获取设备最近的一个使用中订单
     */
    @Override
    public OrderBase getDeviceLastUsingOrder(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }
        return orderBaseDao.findByDeviceIdAndStatus(deviceId, OrderStatus.USING.getCode());
    }


    /**
     * 获取设备上指定状态的最新订单
     *
     * @param deviceId
     * @param status
     * @return
     */
    @Override
    public OrderBase getDeviceLastOrderByStatus(String deviceId, Integer status) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }
        return orderBaseDao.findByDeviceIdAndStatus(deviceId, status);
    }

    @Override
    public OrderBase getOrderBaseByOrderNo(String orderNo) {
        return selectOne(new EntityWrapper<OrderBase>().eq("order_no", orderNo).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
    }


    @Override
    public Page<OrderListDto> getOrderListDtoPage(OrderQueryDto orderQueryDto) {
        Pageable<OrderQueryDto> pageable = new Pageable<>();
        pageable.setCurrent(orderQueryDto.getCurrentPage());
        pageable.setSize(orderQueryDto.getPagesize());
        pageable.setQuery(orderQueryDto);
        return getOrderListDtoPage(pageable);
    }

    public Page<OrderListDto> getOrderListDtoPage(Pageable<OrderQueryDto> pageable) {
        OrderQueryDto query = pageable.getQuery();
        if (pageable.getCurrent() < 1) {
            pageable.setCurrent(1);
        }
        query.setBegin((pageable.getCurrent() - 1) * pageable.getSize());
        query.setEnd(pageable.getCurrent() * pageable.getSize());
        query.setPagesize(pageable.getSize());
        List<OrderBaseListDto> orderBaseListDtos = orderBaseDao.listPage(query);
        List<OrderListDto> orderListDtos = new ArrayList<>();

        for (OrderBaseListDto o : orderBaseListDtos) {
            OrderListDto orderListDto = new OrderListDto();
            orderListDto.setOrderBaseListDto(o);
            String status = OrderStatus.getMsg(o.getStatus());
            orderListDto.setOrderStatus(status);
            orderListDto.setPayTypeDesc(PayType.getName(Integer.parseInt(o.getPayType())));
            if (o.getAbnormalReason() != null) {
                orderListDto.setAbnormalReasonDesc(OrderAbnormalReason.get(o.getAbnormalReason()).getDescription());
            }
            orderListDtos.add(orderListDto);
        }
        Page<OrderListDto> page = new Page<>();
        page.setSize(pageable.getSize());
        page.setCurrent(pageable.getCurrent());
        page.setRecords(orderListDtos);
        page.setTotal(orderBaseDao.findTotalSize(query));
        return page;
    }

    @Override
    public PageOrderAppList<OrderListDto> getOrderAppListDtoPage(OrderQueryDto orderQueryDto) {
        Integer currentPage = orderQueryDto.getCurrentPage();
        Integer pageSize = orderQueryDto.getPagesize();
        Integer begin = (currentPage - 1) * pageSize;
        Integer end = currentPage * pageSize;
        orderQueryDto.setBegin(begin);
        orderQueryDto.setEnd(end);

        SysUser sysUser = sysUserService.getCurrentUser();
        orderQueryDto.setOperatorAccountId(sysUser.getId());
        List<OrderBaseListDto> orderBaseListDtos = orderBaseDao.appListPage(orderQueryDto);
        List<OrderListDto> orderListDtos = new ArrayList<>();

        for (OrderBaseListDto o : orderBaseListDtos) {
            OrderListDto orderListDto = new OrderListDto();
            orderListDto.setOrderBaseListDto(o);
            String status = OrderStatus.getOrderStatus(o.getStatus()).getMsg();
            orderListDto.setOrderStatus(status);
            String sno = o.getSno();
            if (!StringUtils.isEmpty(sno)) {
                Device device = deviceService.getDeviceInfoBySno(sno);
                if (!ParamUtil.isNullOrEmptyOrZero(device)) {
                    orderListDto.setDeviceName(device.getName());
                }
            }
            Integer service_mode_id = o.getService_mode_id();
            if (!ParamUtil.isNullOrEmptyOrZero(service_mode_id)) {
                ProductServiceMode productServiceMode = productServiceModeService.selectById(service_mode_id);
                if (!ParamUtil.isNullOrEmptyOrZero(productServiceMode)) {
                    orderListDto.setService_mode_unit(productServiceMode.getUnit());
                }
            }
            orderListDtos.add(orderListDto);
        }
        PageOrderAppList<OrderListDto> page = new PageOrderAppList<>();
        page.setSize(orderQueryDto.getPagesize());
        page.setCurrent(orderQueryDto.getCurrentPage());
        page.setRecords(orderListDtos);
        page.setTotal(orderBaseDao.findAppListTotalSize(orderQueryDto));

        Double totalPay = orderBaseDao.appListPaySum(orderQueryDto);
        page.setTotalPay(totalPay);
        return page;
    }

    @Override
    public OrderDetailDto orderDetail(OrderBase orderBase) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setDeviceMac(orderBase.getMac());
        orderDetailDto.setOrderNo(orderBase.getOrderNo());
        orderDetailDto.setStatus(orderBase.getOrderStatus());
        String status = OrderStatus.getOrderStatus(orderBase.getOrderStatus()).getMsg();
        orderDetailDto.setOrderStatus(status);
        orderDetailDto.setOrderTime(orderBase.getCtime());
        orderDetailDto.setPay(orderBase.getAmount());
        orderDetailDto.setPayTime(orderBase.getPayTime());
        Integer payTypeCode = orderBase.getPayType();
        orderDetailDto.setPayTypeCode(payTypeCode);
        String payType = PayType.getPayType(payTypeCode).getName();
        orderDetailDto.setPayType(payType);
        if (orderBase.getAbnormalReason() != null) {
            orderDetailDto.setAbnormalReasonDesc(OrderAbnormalReason.get(orderBase.getAbnormalReason()).getDescription());
        }
        orderDetailDto.setUserName(orderBase.getUserName());
        Device device = deviceService.selectById(orderBase.getSno());
        orderDetailDto.setDeviceLaunchArea(device.getLaunchAreaName());
        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (!ParamUtil.isNullOrEmptyOrZero(orderExtByQuantity)) {
            String serviceType = orderExtByQuantity.getPrice() + "元" + orderExtByQuantity.getQuantity() + orderExtByQuantity.getUnit();
            orderDetailDto.setServicetype(serviceType);
            orderDetailDto.setQuantity(orderExtByQuantity.getQuantity());
            orderDetailDto.setUnit(orderExtByQuantity.getUnit());
        }
        orderDetailDto.setServiceMode(orderBase.getServiceModeName());
        Date ctime = orderBase.getCtime();
        if (Objects.equals(orderBase.getOrderStatus(), OrderStatus.FINISH.getCode())) {

            Date utime = orderBase.getUtime();
            Long time = utime.getTime() - ctime.getTime();
            orderDetailDto.setDeviceUseTime(time);
        }

        if (Objects.equals(orderBase.getOrderStatus(), OrderStatus.USING.getCode())) {
            Date utime = new Date();
            Long time = utime.getTime() - ctime.getTime();
            orderDetailDto.setDeviceUseTime(time);
        }

        // TODO
        Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", orderBase.getSysUserId()));

        if (Objects.nonNull(operator)) {
            orderDetailDto.setOperator(operator.getName());
        }

        // 取水量
        orderDetailDto.setIntakeWater(orderBase.getIntakeWater());

        return orderDetailDto;
    }

    @Override
    public OrderAppDetailDto orderAppDetail(OrderBase orderBase) {
        OrderAppDetailDto orderDetailDto = new OrderAppDetailDto();
        orderDetailDto.setDeviceMac(orderBase.getMac());
        orderDetailDto.setOrderNo(orderBase.getOrderNo());
        orderDetailDto.setStatus(orderBase.getOrderStatus());
        String status = OrderStatus.getOrderStatus(orderBase.getOrderStatus()).getMsg();
        orderDetailDto.setOrderStatus(status);
        orderDetailDto.setOrderTime(orderBase.getCtime());
        orderDetailDto.setPay(orderBase.getAmount());
        orderDetailDto.setPayTime(orderBase.getPayTime());
        Integer payTypeCode = orderBase.getPayType();
        orderDetailDto.setPayTypeCode(payTypeCode);
        String payType = PayType.getPayType(payTypeCode).getName();
        orderDetailDto.setPayType(payType);
        orderDetailDto.setUserName(orderBase.getUserName());
        Device device = deviceService.selectById(orderBase.getSno());
        orderDetailDto.setDeviceLaunchArea(device.getLaunchAreaName());
        orderDetailDto.setDeviceName(device.getName());
        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (!ParamUtil.isNullOrEmptyOrZero(orderExtByQuantity)) {
            String serviceType = orderExtByQuantity.getPrice() + "元" + orderExtByQuantity.getQuantity() + orderExtByQuantity.getUnit();
            orderDetailDto.setServicetype(serviceType);
            orderDetailDto.setQuantity(orderExtByQuantity.getQuantity());
            orderDetailDto.setUnit(orderExtByQuantity.getUnit());
        }
        orderDetailDto.setServiceMode(orderBase.getServiceModeName());
        return orderDetailDto;
    }

    /**
     * 查找待分润的的订单
     */
    @Override
    public List<OrderBase> getReadyForShareBenefit(String sno, Date lastExecuteTime) {
        return orderBaseDao.listReadyForShareBenefitOrder(lastExecuteTime, sno);
    }

    private OrderBase createOrderForWeiXin(ProductServiceDetail productServiceDetail, ProductServiceMode serviceMode, User user, Device device, SysUserExt sysUserExt) {
        //查询该用户是否有未完成支付的订单
//        List<OrderBase> unfinishOrderList = orderBaseDao.findByUserIdAndStatus(user.getId(), OrderStatus.USING.getCode());
//        if (unfinishOrderList != null && unfinishOrderList.size() > 0) {
//            loggerOrder.warn("====>>>用户[" + user.getOpenid() + "]存在未完成订单,不能下单");
//            LeaseException.throwSystemException(LeaseExceEnums.HAS_UNFINISH_ORDER);
//        }

        //查询该设备的收费模式详情
        List<ProductServiceDetail> serviceDetailList = productServiceDetailService.getProductServiceDetailByServiceModelId(serviceMode.getId());
        if (ParamUtil.isNullOrEmptyOrZero(serviceDetailList)) {
            LeaseException.throwSystemException(LeaseExceEnums.SERVICE_MODE_CONFIG_ERROR);
        }
        //判断收费模式详情的serviceModeId与设备的seviceId是否一致
        if (!productServiceDetail.getServiceModeId().equals(device.getServiceId())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }

        return createOrderBase(productServiceDetail, serviceMode, user, device, sysUserExt.getSysUserId());
    }

    /**
     * 根据收费模式，用户，设备，订单归属人创建订单
     *
     * @param productServiceDetail 收费模式
     * @param serviceMode
     * @param user                 用户
     * @param device               设备
     * @param sysUserId            订单归属人
     * @return
     */
    private OrderBase createOrderBase(ProductServiceDetail productServiceDetail, ProductServiceMode serviceMode, User user, Device device, Integer sysUserId) {
        OrderBase orderBase = new OrderBase();
        orderBase.setOrderNo(LeaseUtil.generateOrderNo(TradeOrderType.CONSUME.getCode()));
        orderBase.setCtime(new Date());
        orderBase.setSno(device.getSno());
        orderBase.setMac(device.getMac());
        //将收费模式详情的价格填上去
        orderBase.setAmount(productServiceDetail.getPrice());
        //将订单状态设置为创建状态
        orderBase.setOrderStatus(OrderStatus.INIT.getCode());
        orderBase.setServiceModeId(serviceMode.getId());
        orderBase.setServiceModeDetailId(productServiceDetail.getId());
        orderBase.setServiceModeName(serviceMode.getName());
        orderBase.setServiceModeDetailId(productServiceDetail.getId());
        orderBase.setLaunchAreaId(device.getLaunchAreaId());
        orderBase.setLaunchAreaName(device.getLaunchAreaName());
        orderBase.setUserId(user.getId());

        orderBase.setUserName(user.getNickname());
        orderBase.setSysUserId(device.getOwnerId());
        orderBase.setCommand(productServiceDetail.getCommand());

        // 插入此时该订单用户实际充值金额及赠送金额
        UserWallet realWallet = userWalletService.selectUserWallet(user.getId(), WalletEnum.BALENCE.getCode());
        UserWallet discountWallet = userWalletService.selectUserWallet(user.getId(), WalletEnum.DISCOUNT.getCode());
        orderBase.setRealRecharge(realWallet.getMoney());
        orderBase.setRealDiscount(discountWallet.getMoney());

        insert(orderBase);
        orderStatusFlowService.saveOne(orderBase, OrderStatus.INIT.getCode(), user.getId() + "");

        //TODO 需要根据serviceMode来判断是按量,按时......收费,这样直接写肯定是有问题的
        OrderExtByQuantity orderExtByQuantity = new OrderExtByQuantity();
        orderExtByQuantity.setOrderNo(orderBase.getOrderNo());
        orderExtByQuantity.setCtime(new Date());
        orderExtByQuantity.setQuantity(productServiceDetail.getNum());
        orderExtByQuantity.setPrice(orderBase.getAmount());
        orderExtByQuantity.setUnit(productServiceDetail.getUnit());
        orderExtByQuantityService.insert(orderExtByQuantity);

        return orderBase;
    }

    @Override
    public void updateOrderStatusAndHandle(OrderBase orderBase, Integer status) {
        //修改的标志
        Boolean flag = false;
        //判断订单状态是否存在
        if (Objects.isNull(orderBase.getOrderStatus()) || Objects.isNull(status)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_STATUS_ERROR);
        }
        //获取订单流程map
        OrderStatusMap map = new OrderStatusMap();
        //根据现有的订单状态获取要转换的订单状态
        List<Integer> statusList = map.get(orderBase.getOrderStatus());
        if (statusList != null && statusList.size() > 0) {
            for (Integer s : statusList) {
                if (s.equals(status)) {
                    //记录订单状态流向
                    loggerOrder.info("订单:" + orderBase.getOrderNo() + "从" + OrderStatus.getOrderStatus(orderBase.getOrderStatus()) + "修改成" + OrderStatus.getOrderStatus(status));
                    orderStatusFlowService.saveOne(orderBase, status, orderBase.getUserId() + "");

                    orderBase.setOrderStatus(status);
                    orderBase.setUtime(new Date());
                    try {
                        updateById(orderBase);
                    } catch (Exception e) {
                        loggerOrder.warn("更新订单:{}状态为：{}报错,再尝试一次", orderBase.getOrderNo(), status, e);
                        updateById(orderBase);
                        flag = true;
                    }
                    flag = true;
                    break;
                }
            }
        }
        //修改不成功
        if (!flag) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_CHANGE_STATUS_FAIL);
        }
        //修改成功后做相应的操作
        handleDueToOrderStatus(orderBase);
    }

    @Override
    public void updateOrderStatusAndHandle(String orderNo, Integer status) {
        if (StringUtils.isEmpty(orderNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        OrderBase orderBase = selectById(orderNo);
        //如果更新不成功
        updateOrderStatusAndHandle(orderBase, status);
    }


    public Page<WXOrderListDto> getWXOrderListPage(Pageable<WXOrderQueryDto> pageable) {
        Page<OrderBase> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<OrderBase> entityWrapper = new EntityWrapper<>();

        String openId = pageable.getQuery().getOpenid();
        User user = userService.getUserByIdOrOpenidOrMobile(openId);
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        Integer userId = user.getId();
        entityWrapper.eq("user_id", userId).eq("is_deleted", 0);
        if (pageable.getQuery().getCardNum() != null) {
            entityWrapper.eq("pay_card_num", pageable.getQuery().getCardNum());
        }
        if (pageable.getQuery().getStartTime() != null) {
            entityWrapper.ge("ctime", pageable.getQuery().getStartTime());
        }
        if (pageable.getQuery().getEndTime() != null) {
            entityWrapper.le("ctime", pageable.getQuery().getEndTime());
        }
        Integer[] orderStatuss = {OrderStatus.USING.getCode(), OrderStatus.FINISH.getCode(), OrderStatus.REFUNDED.getCode(),
                OrderStatus.ABNORMAL.getCode(), OrderStatus.REFUNDING.getCode(), OrderStatus.REFUND_FAIL.getCode()};
        entityWrapper.in("order_status", orderStatuss);
        entityWrapper.orderBy("ctime", false);
        Page<OrderBase> basePage = selectPage(page,
                QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
        List<OrderBase> orderBases = basePage.getRecords();
        List<WXOrderListDto> wxOrderListDtos = new ArrayList<>(orderBases.size());
        Page<WXOrderListDto> result = new Page<>();
        for (OrderBase orderBase : orderBases) {
            WXOrderListDto wxOrderListDto = getWxOrderListDto(orderBase);
            wxOrderListDtos.add(wxOrderListDto);
        }
        BeanUtils.copyProperties(basePage, result);
        result.setRecords(wxOrderListDtos);
        return result;
    }

    public WXOrderListDto getWxOrderListDto(OrderBase orderBase) {
        WXOrderListDto wxOrderListDto = new WXOrderListDto();
        wxOrderListDto.setCtime(orderBase.getCtime());
        String orderNo = orderBase.getOrderNo();
        wxOrderListDto.setOrderNo(orderNo);
        wxOrderListDto.setDeviceSno(orderBase.getSno());
        wxOrderListDto.setServiceModeName(orderBase.getServiceModeName());
        wxOrderListDto.setStatusCode(orderBase.getOrderStatus());
        String status = OrderStatus.getOrderStatus(orderBase.getOrderStatus()).getMsg();
        wxOrderListDto.setStatus(status);
//        Integer payTypeCode = orderBase.getPayType();
//        String paytype = PayType.getPayType(payTypeCode).getName();
//        wxOrderListDto.setPayType(paytype);
        wxOrderListDto.setPayMoney(orderBase.getAmount());
        Device device = deviceService.selectById(orderBase.getSno());
        if (!ParamUtil.isNullOrEmptyOrZero(device)) {
            wxOrderListDto.setDeviceArea(device.getLaunchAreaName());
            wxOrderListDto.setDeviceName(device.getName());
        }
//        ProductServiceMode serviceMode = productServiceModeService.selectById(orderBase.getServiceModeId());
//        if (Objects.nonNull(serviceMode)) {
//            List<ProductServiceDetail> list = productServiceDetailService.selectList(new EntityWrapper<ProductServiceDetail>().eq("service_mode_id", serviceMode.getId()).orderBy("num"));
//            if (CollectionUtils.isNotEmpty(list)) {
//                List<ProductServiceDetailVo> resList = new ArrayList<>();
//                for (int i = 0; i < list.size(); ++i) {
//                    ProductServiceDetailVo productServiceDetailVo = new ProductServiceDetailVo(list.get(i));
//                    resList.add(productServiceDetailVo);
//                    wxOrderListDto.setPrice(productServiceDetailVo.getPrice());
//                }
//                AppProductServiceDetailVo appServiceModeDetailDto = new AppProductServiceDetailVo();
//                appServiceModeDetailDto.setList(resList);
//                appServiceModeDetailDto.setUnit(serviceMode.getUnit());
//                wxOrderListDto.setAppProductServiceDetailVo(appServiceModeDetailDto);
//            }
//            wxOrderListDto.setWorkingMode(serviceMode.getWorkingMode());
//        }
        wxOrderListDto.setPayTime(orderBase.getPayTime());
        //获取收费模式名称
        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (!ParamUtil.isNullOrEmptyOrZero(orderExtByQuantity)) {
            wxOrderListDto.setDuration(orderExtByQuantity.getQuantity() + orderExtByQuantity.getUnit());
        }
//
//        ProductServiceDetail productServiceDetail = productServiceDetailService.selectById(orderBase.getServiceModeDetailId());
//        if (productServiceDetail != null) {
//            wxOrderListDto.setPrice(productServiceDetail.getPrice());
//        }

        return wxOrderListDto;
    }

    @Override
    public WXOrderListDto getWxPayingOrder(String deviceSno) {
        if (StringUtils.isBlank(deviceSno)) {
            return null;
        }
        OrderBase orderBase = orderBaseDao.findByDeviceIdAndStatus(deviceSno, OrderStatus.PAYING.getCode());
        WXOrderListDto wxOrderListDto = getWxOrderListDto(orderBase);
        return wxOrderListDto;
    }

    @Override
    public void deleteUserShowOrder(List<String> orderNos) {
        List<OrderBase> orderBases = selectBatchIds(orderNos);
        for (OrderBase orderBase : orderBases) {
            orderBase.setIsDeleted(1);
            updateById(orderBase);
        }
    }

    /**
     * 检查设备和订单,是否满足下单和支付的条件
     *
     * @param sno
     * @param userIdentify 第三方用户标示,手机号或微信openid或支付宝userid或百度、微博ID
     * @param mobile       手机号
     * @return
     */
    @Override
    public Map<String, Object> checkBeforeOrder(String sno, Integer userBrowserAgentType, String userIdentify, String mobile, Integer port) {
        /**
         * 1、检查设备是否存在
         * 2、检查设备是否投入运营,并获取投入运营的微信配置
         * 3、查看该设备对应的收费模式是否为免费
         * 4、检查设备是否空闲可用
         * 5、检查用户是否存在
         * 6、检查用户是否存在未完成的订单
         */
        Device device = deviceService.selectById(sno);
        if (device == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(sno);

        if (sysUserExt == null || ParamUtil.isNullOrEmptyOrZero(device.getServiceId())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }

        //查询该设备对应的收费模式
        ProductServiceMode serviceMode = productServiceModeService.selectById(device.getServiceId());

        if (Objects.isNull(serviceMode) || ParamUtil.isNullOrEmptyOrZero(serviceMode.getUnit())) {//免费模式
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_FREE);
        }


        if (device.getLock()) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LOCKED);
        }
        if (device.getOnlineStatus().equals(DeviceStatus.OFFLINE.getCode())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_OFFLINE);
        }

        if (ParamUtil.isNullOrEmptyOrZero(port)) {
            if (!DeviceStatus.FREE.getCode().equals(device.getWorkStatus())) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_FREE);
            }
        } else {
            //判断设备出水口是否空闲
            DeviceExt deviceExtPort = deviceExtService.selectBySnoAndPort(device.getSno(), port);
            if (ParamUtil.isNullOrEmptyOrZero(deviceExtPort) || !Objects.equals(deviceExtPort.getStatus(), DeviceWorkStatus.FREE.getCode())) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_UNRENT);
            }
        }

        // 有人支付完了，正在启动设备，这时候不允许下单
        EntityWrapper<OrderBase> wrapper = new EntityWrapper<>();
        wrapper.eq("sno", device.getSno()).orderBy("ctime", false).last("limit 1");
        List<OrderBase> orders = baseMapper.selectList(wrapper);
        if (orders.size() > 0 && orders.get(0).getOrderStatus().equals(OrderStatus.PAYED.getCode())) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_INUSING_FAIL);
        }

        //先使用第三方的用户ID获取用户信息,如果获取不到,使用手机号获取,如果都获取不到,则判断用户不存在
        User user = null;
        if (!ParamUtil.isNullOrEmptyOrZero(mobile)) {
            user = userService.getUserByIdOrOpenidOrMobile(mobile);
        }
        if (user == null && !ParamUtil.isNullOrEmptyOrZero(userIdentify)) {
            user = userService.getUserByIdOrOpenidOrMobile(userIdentify);
        }

        if (user == null && !ParamUtil.isNullOrEmptyOrZero(userIdentify)) {
            if (userBrowserAgentType.equals(ThirdPartyUserType.WEIXIN.getCode())) {
                String userinfo = WxUtil.getUserInfo(sno, sysUserExt);
                user = userService.addUserByWx(userinfo, sysUserExt, deviceService.getDeviceOperatorSysuserid(sno));
                if (user == null) {
                    LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
                }
            } else if (userBrowserAgentType.equals(ThirdPartyUserType.ALIPAY.getCode())) {
                // TODO: 2017/8/19 支付宝根据userid获取用户信息
            } else if (userBrowserAgentType.equals(ThirdPartyUserType.BAIDU.getCode())) {
                // TODO: 2017/8/19 百度根据用户id获取信息
            } else if (userBrowserAgentType.equals(ThirdPartyUserType.SINA.getCode())) {
                // TODO: 2017/8/19 微博根据用户ID获取信息
            }
        }
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }

       /* //判断用户是否有使用中订单
        if (hasUsingOrderOnProduct(user.getId(),device.getProductId())) {
            LeaseException.throwSystemException(LeaseExceEnums.EXISTS_UNFINISH_ORDER);
        }*/
        Map<String, Object> result = new HashedMap();
        result.put("device", device);
        result.put("serviceMode", serviceMode);
        result.put("sysUserExt", sysUserExt);
        result.put("user", user);

        return result;
    }

    private boolean hasUsingOrderOnProduct(Integer userId, Integer productId) {
        if (Objects.isNull(userId) || Objects.isNull(productId)) {
            loggerOrder.error("=====执行方法hasUsingOrderOnProduct的两个参数userId:{},productId:{}有异常====", userId, productId);
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
            return true;
        }
        if (orderBaseDao.countUsingOrderByUserIdAndProductId(userId, productId) > 0) {
            loggerOrder.info("=====用户{},在产品{}已有在使用中的订单,不能重复下单======", userId, productId);
            return true;
        }

        return false;
    }


    @Override
    public AppOrderVo createOrder(PayOrderDto payOrderDto, Integer userBrowserAgentType) {
        loggerOrder.info("创建订单：微信用户mobile=" + payOrderDto.getMobile() + "openid=" + payOrderDto.getOpenid() + ",sno=" + payOrderDto.getSno());
        Map<String, Object> map = checkBeforeOrder(payOrderDto.getSno(), userBrowserAgentType, payOrderDto.getOpenid(), payOrderDto.getMobile(), payOrderDto.getPort());
        if (map == null) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        User user = (User) map.get("user");
        Device device = (Device) map.get("device");
        ProductServiceMode serviceMode = (ProductServiceMode) map.get("serviceMode");
        SysUserExt sysUserExt = (SysUserExt) map.get("sysUserExt");

        //判断用户是否有使用中订单
        if (hasUsingOrderOnProduct(user.getId(), device.getProductId())) {
            OrderBase orderBase = orderBaseDao.findUsingOrderByUserIdAndProductId(user.getId(), device.getProductId());
            loggerOrder.info("用户存在使用中订单：orderNo=" + orderBase.getOrderNo() + ",orderStatus=" + orderBase.getOrderStatus());
            AppOrderVo appOrderVo = new AppOrderVo();
            appOrderVo.setPayMoney(new BigDecimal(orderBase.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
            appOrderVo.setOrderNo(orderBase.getOrderNo());
            appOrderVo.setSno(orderBase.getSno());
            appOrderVo.setCtime(orderBase.getCtime());
            appOrderVo.setStatusCode(orderBase.getOrderStatus());
            appOrderVo.setStatus(OrderStatus.getMsg(orderBase.getOrderStatus()));
            return appOrderVo;
        }

        ProductServiceDetail productServiceDetail = null;
        //获取时长
        Double duration = null;
        //选择数据设定好的收费模式
        if (!ParamUtil.isNullOrZero(payOrderDto.getProductServiceDetailId())) {
            productServiceDetail = productServiceDetailService.selectById(payOrderDto.getProductServiceDetailId());
            if (Objects.isNull(productServiceDetail)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
            }
            duration = productServiceDetail.getNum();
        } else if (!ParamUtil.isNullOrZero(payOrderDto.getQuantity())) {
            //选择自定义数量

            ProductCommandConfig commandConfig = productCommandConfigService.selectById(serviceMode.getServiceTypeId());

            // TODO 热水
            Double price = productServiceDetailService.getMinPrice(serviceMode.getId());

            // 常温
            Double normalPrice = productServiceDetailService.getNormalMinPrice(serviceMode.getId());

            // 冰水
            Double coldPrice = productServiceDetailService.getColdMinPrice(serviceMode.getId());

            // 温开水
            Double warmPrice = productServiceDetailService.getWarmMinPrice(serviceMode.getId());

            productServiceDetail = new ProductServiceDetail();
            productServiceDetail.setServiceModeId(device.getServiceId());
            String command = productCommandConfigService.getCommandByConfig(commandConfig, payOrderDto.getQuantity());

            // 增加冷热水的控制指令
            if (!ParamUtil.isNullOrEmptyOrZero(payOrderDto.getName()) && (!ParamUtil.isNullOrEmptyOrZero(payOrderDto.getValue()))) {
                JSONObject jsonObject = JSON.parseObject(command);
                jsonObject.put(payOrderDto.getName(), payOrderDto.getValue());
                command = jsonObject.toJSONString();
            }
            productServiceDetail.setCommand(command);

            // 根据出水口判断冷热水再计算对应价格 出水类型：1常温，2热水，3冰水,4温开水
            DeviceExt deviceExt = deviceExtService.selectBySnoAndPort(payOrderDto.getSno(), payOrderDto.getPort());
            switch (deviceExt.getPortType()) {
                case 0:
                    break;
                case 1:
                    productServiceDetail.setPrice(payOrderDto.getQuantity() * normalPrice);
                    break;
                case 2:
                    productServiceDetail.setPrice(payOrderDto.getQuantity() * price);
                    break;
                case 3:
                    productServiceDetail.setPrice(payOrderDto.getQuantity() * coldPrice);
                    break;
                case 4:
                    productServiceDetail.setPrice(payOrderDto.getQuantity() * warmPrice);
                    break;
                default:
                    LeaseException.throwSystemException(LeaseExceEnums.SERVICE_MODE_CONFIG_ERROR);
                    break;
            }

            productServiceDetail.setNum(payOrderDto.getQuantity());
            productServiceDetail.setUnit(serviceMode.getUnit());
            //TODO:有问题这种获取订单时长
            duration = payOrderDto.getQuantity();
        } else {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_SERVICE_MODEL_DETAIL_NOT_EXIST);
        }

        OrderBase orderBase = null;
        //创建订单
        if (ParamUtil.isNullOrEmptyOrZero(payOrderDto.getPort())) {
            //创建订单
            orderBase = createOrderForWeiXin(productServiceDetail, serviceMode, user, device, sysUserExt);
        } else {
            //针对沁尔康，存储下单设备对应的出水口
            orderBase = createOrder(productServiceDetail, serviceMode, user, device, sysUserExt, payOrderDto);
        }

        DeviceLaunchArea area = deviceLaunchAreaService.getLaunchAreaInfoById(device.getLaunchAreaId());
        if (Objects.isNull(area)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_WITHOUT_LAUNCH_AREA);
        }
        AppOrderVo appOrderVo = new AppOrderVo();
        appOrderVo.setPayMoney(new BigDecimal(orderBase.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        appOrderVo.setOrderNo(orderBase.getOrderNo());
        appOrderVo.setUnit(serviceMode.getUnit());
        appOrderVo.setCtime(orderBase.getCtime());
        appOrderVo.setDuration(duration + "");
        appOrderVo.setSno(device.getSno());
        appOrderVo.setAddress("地址不详");
        if (!Objects.isNull(area)) {
            appOrderVo.setAddress(area.getName());
        }
        return appOrderVo;
    }

    private Boolean validParamForChargeCard(String cardNum, Device device) {
        if (StringUtils.isBlank(cardNum)) {
            loggerOrder.error("创建订单时，充值卡号为空");
            return false;
        }
        if (ParamUtil.isNullOrEmptyOrZero(device)) {
            loggerOrder.error("设备不存在，请查看数据库");
            return false;
        }
        return true;
    }

    @Override
    public OrderBase createOrderForChargeCard(String cardNum, Device device) {
        if (!validParamForChargeCard(cardNum, device)) {
            return null;
        }
        //获取设备的收费模式，详情，最终获取设备对应的消费金额
        ProductServiceMode serviceMode = productServiceModeService.selectById(device.getServiceId());
        if (ParamUtil.isNullOrEmptyOrZero(serviceMode)) {
            loggerOrder.error("收费模式" + device.getServiceId() + "不存在");
            return null;
        }
        List<ProductServiceDetail> serviceDetailList = productServiceDetailService.getProductServiceDetailByServiceModelId(serviceMode.getId());
        if (CollectionUtils.isEmpty(serviceDetailList)) {
            loggerOrder.error("收费模式" + serviceMode.getId() + "对应的收费详情不存在");
            return null;
        }
        //无论是定时，按次，按锅，按圈都是使用收费详情里面的第一个
        ProductServiceDetail productServiceDetail = serviceDetailList.get(0);
        //获取cardNum对应的用户
        UserChargeCard userChargeCard = userChargeCardService.selectOne(new EntityWrapper<UserChargeCard>().eq("card_num", cardNum));
        if (ParamUtil.isNullOrEmptyOrZero(userChargeCard)) {
            loggerOrder.error("充值卡:" + cardNum + "不存在");
            return null;
        }
        if (userChargeCard.getMoney().compareTo(productServiceDetail.getPrice()) < 0) {
            loggerOrder.error("===充值卡{}的剩余金额{}不够本地设备收费{}使用-====", userChargeCard.getCardNum(), userChargeCard.getMoney(), productServiceDetail.getPrice());
            return null;
        }

        User user = userService.selectById(userChargeCard.getUserId());
        if (ParamUtil.isNullOrEmptyOrZero(user)) {
            loggerOrder.error("用户" + userChargeCard.getUserId() + "不存在");
            return null;
        }
        //获取该订单的归属人员
        SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(device.getSno());
        if (ParamUtil.isNullOrEmptyOrZero(sysUserExt)) {
            loggerOrder.error("设备为投入运营，请查看数据库中是否有运营商与设备" + device.getSno() + "有关联");
            return null;
        }
        DeviceLaunchArea area = deviceLaunchAreaService.selectById(device.getLaunchAreaId());
        if (ParamUtil.isNullOrEmptyOrZero(area)) {
            loggerOrder.error("投放点：" + device.getLaunchAreaId() + "不存在");
            return null;
        }
        redisService.cacheDeviceLockByOrder(device.getSno(), userChargeCard.getCardNum(), 5L);//锁定5秒钟,防止重复刷卡问题
        OrderBase orderBase = createOrderBase(productServiceDetail, serviceMode, user, device, sysUserExt.getSysUserId());
        orderBase.setPayCardNum(cardNum);
        orderBase.setPayType(PayType.CARD.getCode());
        return orderBase;
    }

    @Override
    public AppOrderVo getUsingOrderByUserIdentify(String userIdentify) {
        //如果根据openid可用找到该用户在该公众号存在订单并且订单状态为使用中，则返回该订单信息
        User user = userService.getUserByIdOrOpenidOrMobile(userIdentify);
        List<OrderBase> list = selectList(new EntityWrapper<OrderBase>().eq("user_id", user.getId()).eq("order_status", OrderStatus.USING.getCode()));
        if (ParamUtil.isNullOrZero(list)) {//判断订单是否存在
            return null;
        }
        if (list.size() > 1) {//判断是否存在多个使用中的订单
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_IN_USING_REPEAT);
        }

        OrderBase orderBase = list.get(0);

        //如果不存在就返回null回去
        if (Objects.isNull(orderBase)) {
            return null;
        }
        ProductServiceMode produtServiceMode = productServiceModeService.selectById(orderBase.getServiceModeId());

        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (Objects.isNull(orderExtByQuantity)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_EXT_QUANTITY_NOT_EXIST);
        }
        AppOrderVo appOrderVo = new AppOrderVo();
        appOrderVo.setSno(orderBase.getSno());
        appOrderVo.setPayMoney(new BigDecimal(orderBase.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        appOrderVo.setPayTime(orderBase.getPayTime());
        appOrderVo.setUnit(produtServiceMode.getUnit());
        appOrderVo.setCtime(orderBase.getCtime());
        appOrderVo.setOrderNo(orderBase.getOrderNo());
        appOrderVo.setDuration(orderExtByQuantity.getQuantity() + "");
        return appOrderVo;
    }

    public List<DeviceUsingVo> getUsingDeviceList(String openid) {
        User user = userService.getUserByIdOrOpenidOrMobile(openid);
        if (Objects.isNull(user)) {
            loggerOrder.error("=====用户{}不存在====", openid);
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        EntityWrapper<OrderBase> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", user.getId()).eq("order_status", OrderStatus.USING.getCode());
        List<OrderBase> usingOrderList = selectList(entityWrapper);
        if (CollectionUtils.isNotEmpty(usingOrderList)) {
            List<DeviceUsingVo> deviceList = new ArrayList<>();
            for (OrderBase order : usingOrderList) {
                DeviceUsingVo deviceVo = new DeviceUsingVo();
                deviceVo.setOrderNo(order.getOrderNo());
                deviceVo.setPayTime(order.getPayTime());
                Device device = deviceService.getDeviceInfoBySno(order.getSno());
                if (Objects.isNull(device)) {
                    loggerOrder.error("====订单{}d关联的设备{}不存在=====", order.getOrderNo(), order.getSno());
                    continue;
                }
                Product product = productService.getProductByProductId(device.getProductId());
                if (Objects.isNull(product)) {
                    loggerOrder.error("====订单{}关联的设备{}相关的产品{}不存在{}=====", order.getOrderNo(), device.getSno(), device.getProductId());
                    continue;
                }
                OrderExtByQuantity extByQuantity = orderExtByQuantityService.selectById(order.getOrderNo());
                if (Objects.nonNull(extByQuantity)) {
                    deviceVo.setQuantity(extByQuantity.getQuantity());
                    deviceVo.setUnit(extByQuantity.getUnit());
                }
                deviceVo.setProductKey(product.getGizwitsProductKey());
                deviceVo.setMac(device.getMac());
                deviceVo.setDeviceName(device.getName());
                deviceVo.setSno(device.getSno());
                deviceVo.setStatusCode(device.getWorkStatus() + "");
                deviceVo.setStatus(DeviceStatus.getShowName(device.getWorkStatus(), device.getFaultStatus(), device.getLock()));
                deviceList.add(deviceVo);
            }
            return deviceList;
        }
        return null;
    }

    public AppOrderVo getOrderDetail(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        OrderBase orderBase = selectById(orderNo);
        if (Objects.isNull(orderBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        User user = userService.getUserByIdOrOpenidOrMobile(orderBase.getUserId() + "");
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        ProductServiceMode serviceMode = productServiceModeService.selectById(orderBase.getServiceModeId());
        Device device = deviceService.getDeviceInfoBySno(orderBase.getSno());

        AppOrderVo appOrderVo = new AppOrderVo();
        appOrderVo.setSno(orderBase.getSno());
        appOrderVo.setPayMoney(new BigDecimal(orderBase.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        appOrderVo.setPayTime(orderBase.getPayTime());
        appOrderVo.setUnit(serviceMode.getUnit());
        appOrderVo.setWorkingMode(serviceMode.getWorkingMode());
        appOrderVo.setCtime(orderBase.getCtime());
        appOrderVo.setOrderNo(orderBase.getOrderNo());
        appOrderVo.setUsername(user.getNickname());
        appOrderVo.setMac(device.getMac());
        appOrderVo.setDeviceLaunchName(device.getLaunchAreaName());
        String status = OrderStatus.getOrderStatus(orderBase.getOrderStatus()).getMsg();
        appOrderVo.setStatusCode(orderBase.getOrderStatus());
        appOrderVo.setStatus(status);
        if (Objects.nonNull(serviceMode)) {
            EntityWrapper<ProductServiceDetail> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("id", orderBase.getServiceModeDetailId());
            ProductServiceDetail productServiceDetail = productServiceDetailService.selectOne(entityWrapper);
            if (Objects.nonNull(productServiceDetail)) {
                if (Objects.nonNull(serviceMode)) {
                    List<ProductServiceDetail> list = productServiceDetailService.selectList(new EntityWrapper<ProductServiceDetail>().eq("service_mode_id", serviceMode.getId()).orderBy("num"));
                    if (CollectionUtils.isNotEmpty(list)) {
                        List<ProductServiceDetailVo> resList = new ArrayList<>();
                        for (int i = 0; i < list.size(); ++i) {
                            ProductServiceDetailVo productServiceDetailVo = new ProductServiceDetailVo(list.get(i));
                            resList.add(productServiceDetailVo);
                        }
                        AppProductServiceDetailVo appServiceModeDetailDto = new AppProductServiceDetailVo();
                        appServiceModeDetailDto.setList(resList);
                        appServiceModeDetailDto.setUnit(serviceMode.getUnit());
                        appOrderVo.setAppServiceModeDetailDto(appServiceModeDetailDto);
                    }
                }

                appOrderVo.setServiceModeDetail(new ProductServiceDetailVo(productServiceDetail));
            }
        }
        return appOrderVo;
    }

    @Override
    public OrderBase getUsingOrderByOpenid(String sno, String openid) {
        //如果根据openid可用找到该用户在该公众号存在订单并且订单状态为使用中，则返回该订单信息
        User user = userService.getUserByIdOrOpenidOrMobile(openid);
        List<OrderBase> list = selectList(new EntityWrapper<OrderBase>().eq("user_id", user.getId())
                .eq("sno", sno)
                .eq("order_status", OrderStatus.USING.getCode()));
        if (ParamUtil.isNullOrZero(list)) {//判断订单是否存在
            return null;
        }
        if (list.size() > 1) {//判断是否存在多个使用中的订单
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_IN_USING_REPEAT);
        }
        return list.get(0);
    }

    @Override
    public AppOrderVo judgeOrder(String orderNo) {
        OrderBase orderBase = selectById(orderNo);
        if (Objects.isNull(orderBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        AppOrderVo appOrderVo = new AppOrderVo();
        appOrderVo.buildOrderVo(orderBase, null, null, null);
        return appOrderVo;
    }

    //异常
    @Override
    public void handleAbnormalOrder(OrderBase orderBase, OrderAbnormalReason reason) {
        orderBase.setAbnormalReason(reason.getCode());
        updateOrderStatusAndHandle(orderBase, OrderStatus.ABNORMAL.getCode());
    }

    //已退款
    @Override
    public void handleRefundOrder(OrderBase orderBase, OrderAbnormalReason reason) {
        orderBase.setAbnormalReason(reason.getCode());
        updateOrderStatusAndHandle(orderBase, OrderStatus.REFUNDED.getCode());
    }



    @Override
    public List<OrderBase> findByUserIdAndStatus(Integer id, Integer code) {
        return orderBaseDao.findByUserIdAndStatus(id, code);
    }


    private void handleDueToOrderStatus(OrderBase orderBase) {
        if (Objects.isNull(orderBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        loggerOrder.info("====>订单状态为" + orderBase.getOrderStatus() + "执行操作");
        switch (orderBase.getOrderStatus()) {

            case 2:
                CommonEventPublisherUtils.publishEvent(new WxPayCallbackEvent("WxPayCallback", orderBase));
                createGizUserAndBindGizevice(orderBase);
                break;
            case 3:
                break;
            case 4:

                break;
        }
    }

    private void createGizUserAndBindGizevice(OrderBase orderBase) {
        //获取当前设备的产品
        Product product = productService.getProductByDeviceSno(orderBase.getSno());
        User user = userService.getUserByIdOrOpenidOrMobile(orderBase.getUserId() + "");
        //支付回调成功就说明该订单可用
        //判断redis里面是否存在token和uid等信息
        if (StringUtils.isEmpty(redisService.getUserTokenByUserName(user.getUsername()))) {
            String res = GizwitsUtil.createUser(user.getUsername(), product.getGizwitsAppId());
            JSONObject json = JSONObject.parseObject(res);
            redisService.setUserTokenByUsername(user.getUsername(), String.valueOf(json.get("uid")),
                    String.valueOf(json.get("token")),
                    Long.valueOf(String.valueOf(json.get("expire_at"))));
            //异步绑定设备
            CommonEventPublisherUtils.publishEvent(new BindGizwitsDeviceEvent("BindGizwitsDevice", product.getId(), String.valueOf(json.get("token")), orderBase.getSno(), user.getUsername()));
        } else { //如果存在就绑定该设备
            String userToken = redisService.getUserTokenByUserName(user.getUsername());
            CommonEventPublisherUtils.publishEvent(new BindGizwitsDeviceEvent("BindGizwitsDevice", product.getId(), userToken, orderBase.getSno(), user.getUsername()));
        }
    }

    @Override
    public Page<WXOrderListDto> WxOrderListPage(Pageable<OrderQueryByMobileDto> pageable) {
        OrderQueryByMobileDto dto = pageable.getQuery();
        String mobile = dto.getMobile();
        User user = userService.getUserByIdOrOpenidOrMobile(mobile);
        dto.setUserId(user.getId());
        Page<OrderBase> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<OrderBase> page1 = selectPage(page, QueryResolverUtils.parse(dto, new EntityWrapper<OrderBase>()));
        List<OrderBase> list = page1.getRecords();
        List<WXOrderListDto> wxOrderListDtos = new ArrayList<>(list.size());
        for (OrderBase orderBase : list) {
            WXOrderListDto wxOrderListDto = getWxOrderListDto(orderBase);
            wxOrderListDtos.add(wxOrderListDto);
        }
        Page<WXOrderListDto> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        result.setRecords(wxOrderListDtos);
        return result;
    }


    @Transactional
    public Boolean checkAndUpdateConsumeOrder(String orderNo, Double totalFee) {
        OrderBase orderBase = getOrderBaseByOrderNo(orderNo);
        if (orderBase == null) {
            loggerOrder.error("====>>>>> 订单orderNo[" + orderBase.getOrderNo() + "]在系统中未找到");
            return false;
        }
        //检查订单的状态是否是未支付
        if (orderBase.getPayTime() != null || OrderStatus.PAYED.getCode().equals(orderBase.getOrderStatus())) {
            loggerOrder.warn("====>>>> 订单tradeNo[" + IdGenerator.generateTradeNo(orderBase.getOrderNo()) + "]的状态为已支付,本次支付回调不做处理");
            return false;
        }

        //检查订单金额是否一致,注:微信回调中的金额单位是分,需要转换为元
        if (!totalFee.equals(orderBase.getAmount())) {
            loggerOrder.error("====>>>>> 订单orderNo[" + orderNo + "]的金额为[" + orderBase.getAmount() + "]与支付回调的金额[" + totalFee + "]的金额不匹配,本次支付回调不做处理");
            return false;
        }

        orderBase.setPayTime(new Date());
        updateOrderStatusAndHandle(orderBase, OrderStatus.PAYED.getCode());
        orderPayRecordService.updateOne(orderBase.getOrderNo(), OrderStatus.PAYED.getCode());

        return true;
    }

    @Override
    public AppOrderVo getForAppOrder(AppUsingOrderDto orderDto) {
        OrderBase orderBase = getUsingOrderByOpenid(orderDto.getSno(), orderDto.getOpenid());
        //如果不存在就返回null回去
        if (Objects.isNull(orderBase)) {
            return null;
        }
        ProductServiceMode produtServiceMode = productServiceModeService.selectById(orderBase.getServiceModeId());

        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (Objects.isNull(orderExtByQuantity)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_EXT_QUANTITY_NOT_EXIST);
        }
        AppOrderVo appOrderVo = new AppOrderVo();
        appOrderVo.setSno(orderBase.getSno());
        appOrderVo.setPayMoney(new BigDecimal(orderBase.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        appOrderVo.setPayTime(orderBase.getPayTime());
        appOrderVo.setUnit(produtServiceMode.getUnit());
        appOrderVo.setCtime(orderBase.getCtime());
        appOrderVo.setOrderNo(orderBase.getOrderNo());
        appOrderVo.setDuration(orderExtByQuantity.getQuantity() + "");
        appOrderVo.setStatusCode(orderBase.getOrderStatus());
        appOrderVo.setOrderStatus(orderBase.getOrderStatus());
        appOrderVo.setStatus(OrderStatus.getStatus(orderBase.getOrderStatus()));
        return appOrderVo;
    }


    @Override
    public void sendOrderMessage(OrderBase orderBase) {
        if (orderBase == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        if (!Objects.equals(orderBase.getOrderStatus(), OrderStatus.FINISH.getCode())) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_NOT_FINISH);
        }

        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (orderExtByQuantity == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }

        SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(orderBase.getSno());
        if (Objects.isNull(sysUserExt)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }
        User user = userService.selectOne(new EntityWrapper<User>().eq("id", orderBase.getUserId()).eq("status", 1));
        UserWxExt userWxExt = userWxExtService.selectOne(new EntityWrapper<UserWxExt>().eq("user_openid", user.getOpenid())
                .eq("status", 1).eq("wx_id", sysUserExt.getWxId()));
        if (user == null || userWxExt == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        JSONObject sendData = new JSONObject();
        sendData.put("touser", userWxExt.getOpenid());
        sendData.put("template_id", sysUserExt.getWxTemplateId());

        JSONObject firstData = new JSONObject();
        firstData.put("value", " 订单已完成");
        firstData.put("color", "#173177");

        JSONObject timeData = new JSONObject();
        timeData.put("value", orderBase.getUtime());
        timeData.put("color", "#173177");

        JSONObject performanceData = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        performanceData.put("value", "您的账号在" + sdf.format(orderBase.getUtime()) + "完成" + orderExtByQuantity.getQuantity() + "升水量的交易");
        performanceData.put("color", "#173177");

        JSONObject body = new JSONObject();
        body.put("first", firstData);
        body.put("performance", performanceData);
        body.put("time", timeData);

        sendData.put("data", body);
        //发送模板消息
        WxUtil.sendTemplateNotice(sendData.toJSONString(), sysUserExt);
    }

    private OrderBase createOrder(ProductServiceDetail productServiceDetail, ProductServiceMode serviceMode, User user, Device device, SysUserExt sysUserExt, PayOrderDto payOrderDto) {
        //创建订单
        OrderBase orderBase = createOrderForWeiXin(productServiceDetail, serviceMode, user, device, sysUserExt);
        //拼接下发指令
        String command = orderBase.getCommand();
        JSONObject jsonObject = JSON.parseObject(command);
        int random4 = (int) (Math.random() * 9000.0D) + 1000;
        jsonObject.put("a13QRcodePaymentTransfer_Serialnumber", random4);
        jsonObject.put("a14QRcodePaymentTransfer_Usernumber", user.getId());
        jsonObject.put("a15QRcodePaymentTransfer_Outletnumbe", payOrderDto.getPort());
        orderBase.setCommand(jsonObject.toJSONString());
        updateById(orderBase);

        //存储设备出水口到订单信息中
        OrderExtPort orderExtPort = new OrderExtPort();
        orderExtPort.setCtime(new Date());
        orderExtPort.setUtime(new Date());
        orderExtPort.setOrderNo(orderBase.getOrderNo());
        orderExtPort.setPort(payOrderDto.getPort());
        orderExtPort.setSno(orderBase.getSno());
        orderExtPortService.insert(orderExtPort);

        return orderBase;
    }

    @Override
    public void closeOrder(String orderNo) {
        OrderBase orderBase = selectById(orderNo);
        if (orderBase == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        //订单状态流转
        orderStatusFlowService.saveOne(orderBase, OrderStatus.FINISH.getCode(), "Netty Receive");

        orderBase.setOrderStatus(OrderStatus.FINISH.getCode());
        orderBase.setUtime(new Date());
        updateById(orderBase);
        //推送一条消息到用户公众号
//        sendOrderMessage(orderBase);
        //插入系统消息
        InsertMessage(orderBase);

    }

    private void InsertMessage(OrderBase orderBase) {
        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderBase.getOrderNo());
        if (orderExtByQuantity == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        SysMessage sysMessage = new SysMessage();
        sysMessage.setAddresserId(orderBase.getUserId());
        sysMessage.setAddresserName(orderBase.getUserName());
        sysMessage.setCtime(orderBase.getUtime());
        sysMessage.setUtime(new Date());
        sysMessage.setTitle("订单已完成");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sysMessage.setContent("您的账号在" + sdf.format(orderBase.getUtime()) + "完成" + orderExtByQuantity.getQuantity() + "升水量的交易");
        sysMessageService.insert(sysMessage);
    }

    /**
     * 支付
     */
//    @Override
//    public String alipayPrePay(PrePayDto prePayDto, Integer browserAgentType) {
//        OrderExtPort orderExtPort = orderExtPortService.selectOne(new EntityWrapper<OrderExtPort>().eq("order_no",prePayDto.getOrderNo()));
//        if (ParamUtil.isNullOrEmptyOrZero(orderExtPort)){
//            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
//        }
//        Map<String,Object> result = checkBeforeOrder(prePayDto.getSno(), browserAgentType, prePayDto.getOpenid(),prePayDto.getMobile(),orderExtPort.getPort());
//        //判断订单、设备、用户
//        if (result == null) {
//            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
//        }
//        SysUserExt sysUserExt = (SysUserExt) result.get("sysUserExt");
//
//        //根据订单号获取订单类型
//        Integer orderType = LeaseUtil.getOrderType(prePayDto.getOrderNo());
//
//        User user = userService.getUserByIdOrOpenidOrMobile(prePayDto.getMobile());
//        if (Objects.isNull(user)) {
//            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
//        }
//
//        //判断订单类型
//        if (orderType.equals(TradeOrderType.CONSUME.getCode())) {//消费订单
//            OrderBase orderBase = selectById(prePayDto.getOrderNo());
//            if (Objects.isNull(orderBase)) {
//                LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
//            } else {
//                orderBase.setPayType(prePayDto.getPayType());
//                orderBase.setUtime(new Date());
//                updateOrderStatusAndHandle(orderBase, OrderStatus.PAYING.getCode());
//            }
//            return tradeAlipayService.executePay(sysUserExt, orderBase.getOrderNo(), orderBase.getAmount(), orderBase.getServiceModeName(), orderBase.getMac(), orderType);
//        } else if (orderType.equals(TradeOrderType.CHARGE.getCode())) {//充值订单
//            UserWalletChargeOrder chargeOrder = userWalletChargeOrderService.selectOne(new EntityWrapper<UserWalletChargeOrder>().eq("charge_order_no", prePayDto.getOrderNo()));
//            if (Objects.isNull(chargeOrder)) {
//                LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
//            }
//            chargeOrder.setUtime(new Date());
//            userWalletChargeOrderService.updateChargeOrderStatus(chargeOrder, UserWalletChargeOrderType.PAYING.getCode());
//            return tradeAlipayService.executePay(sysUserExt, chargeOrder.getChargeOrderNo(), chargeOrder.getFee(), chargeOrder.getWalletType() + "", chargeOrder.getWalletName(), orderType);
//        } else {
//            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
//        }
//        return null;
//    }
    public AppOrderDetailVo getOrderDetailForApp(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        OrderBase orderBase = selectById(orderNo);
        if (Objects.isNull(orderBase)) {
            loggerOrder.error("====订单{}不存在====", orderBase.getOrderNo());
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        AppOrderDetailVo appOrderDetailVo = new AppOrderDetailVo();
        Device device = deviceService.selectById(orderBase.getSno());
        if (Objects.isNull(device)) {
            loggerOrder.error("====订单{}关联的设备{}不存在====", orderBase.getOrderNo(), orderBase.getSno());
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
//        User user = userService.getUserByIdOrOpenidOrMobile(orderBase.getUserId()+"");
//        if(Objects.isNull(user)){
//            loggerOrder.error("====订单{}关联的用户{}不存在===",orderBase.getOrderNo(),orderBase.getUserId());
//            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
//        }
//
//        Operator operator = operatorService.getOperatorByAccountId(orderBase.getSysUserId());
//        if(Objects.isNull(operator)){
//            loggerOrder.error("====订单{}关联的运营商sysAccountId:{}不存在====",orderBase.getOrderNo(),orderBase.getSysUserId());
//            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
//        }

        ProductServiceMode serviceMode = productServiceModeService.selectById(orderBase.getServiceModeId());
        if (Objects.isNull(serviceMode)) {
            loggerOrder.error("====订单{}关联的收费模式{}不存在====", orderBase.getOrderNo(), orderBase.getServiceModeId());
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderNo);
        if (!ParamUtil.isNullOrEmptyOrZero(orderExtByQuantity)) {
            appOrderDetailVo.setDuration(orderExtByQuantity.getQuantity());
        }


        appOrderDetailVo.setCtime(orderBase.getCtime());
        appOrderDetailVo.setPayTime(orderBase.getPayTime());
        appOrderDetailVo.setMoney(orderBase.getAmount());
        appOrderDetailVo.setOrderNo(orderNo);
//        if(StringUtils.isBlank(user.getNickname())){
//            appOrderDetailVo.setUsername(user.getUsername());
//        }else{
//            appOrderDetailVo.setUsername(user.getNickname());
//        }
        appOrderDetailVo.setMac(device.getMac());
        appOrderDetailVo.setSno(device.getSno());
        appOrderDetailVo.setDeviceName(device.getName());
        appOrderDetailVo.setLaunchName(orderBase.getLaunchAreaName() == null ? device.getLaunchAreaName() : orderBase.getLaunchAreaName());
//        appOrderDetailVo.setOperatorName(operator.getName());
//        appOrderDetailVo.setServiceDetail(new ProductServiceModeDetailForAppDto(serviceDetail));
        appOrderDetailVo.setUnit(serviceMode.getUnit());
        appOrderDetailVo.setServiceName(serviceMode.getName());

        appOrderDetailVo.setStatusCode(orderBase.getOrderStatus());
        appOrderDetailVo.setStatus(OrderStatus.getMsg(orderBase.getOrderStatus()));
        appOrderDetailVo.setPayType(PayType.getName(orderBase.getPayType()));
        return appOrderDetailVo;
    }

    @Override
    public void finish(String orderNo) {
        OrderBase orderBase = selectById(orderNo);
        if (ParamUtil.isNullOrEmptyOrZero(orderBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        if (!orderBase.getOrderStatus().equals(OrderStatus.USING.getCode())) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_STATUS_ERROR);
        }
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser, false, true);
        Set<Integer> idSet = new HashSet<>(ids);
        if (idSet.contains(orderBase.getSysUserId())) {
            updateOrderStatusAndHandle(orderBase, OrderStatus.FINISH.getCode());
        } else {
            LeaseException.throwSystemException(LeaseExceEnums.ILLEGAL_TO_OPERATOR);
        }
        // 查找设备出水口
        OrderExtPort orderExtPort = orderExtPortService.selectOne(new EntityWrapper<OrderExtPort>().eq("order_no", orderNo));
        if (!ParamUtil.isNullOrEmptyOrZero(orderExtPort)) {
            // 将设备出水口置为空闲
            String sno = orderExtPort.getSno();
            Integer port = orderExtPort.getPort();
            DeviceExt deviceExtPort = deviceExtService.selectBySnoAndPort(sno, port);
            if (!ParamUtil.isNullOrEmptyOrZero(deviceExtPort)) {
                deviceExtPort.setUtime(new Date());
                deviceExtPort.setStatus(DeviceStatus.FREE.getCode());
                deviceExtService.updateById(deviceExtPort);
                Device device = deviceService.getDeviceInfoBySno(sno);
                int num = deviceExtService.countDeviceByStatus(device.getSno(), DeviceStatus.FREE.getCode());
                if (num == 6) {
                    device.setWorkStatus(DeviceStatus.FREE.getCode());
                } else {
                    device.setWorkStatus(DeviceStatus.USING.getCode());
                }
                device.setUtime(new Date());
                deviceService.updateById(device);
            }

        }
    }

    @Override
    public LastOrderForUserWalletChargeOrderDto getLastOrderForUserWalletChargeOrder(Integer userId) {
        return orderBaseDao.getLastOrderForUserWalletChargeOrder(userId, new Date());
    }

}
