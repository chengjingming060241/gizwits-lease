package com.gizwits.lease.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.constant.TradeStatus;
import com.gizwits.lease.constant.UserWalletChargeOrderType;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.dto.PrePayDto;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderExtPortService;
import com.gizwits.lease.trade.dao.TradeAlipayDao;
import com.gizwits.lease.trade.entity.TradeAlipay;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.trade.service.TradeAlipayService;
import com.gizwits.lease.trade.service.TradeBaseService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 支付宝交易表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-15
 */
@Service
public class TradeAlipayServiceImpl extends ServiceImpl<TradeAlipayDao, TradeAlipay> implements TradeAlipayService {

    private static Logger logger = LoggerFactory.getLogger("PAY_LOGGER");

    @Autowired
    private TradeAlipayDao tradeAlipayDao;

    @Autowired
    private TradeBaseService tradeBaseService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    @Autowired
    private OrderExtPortService orderExtPortService;

    @Override
    public TradeAlipay selectLastTrade(String orderNo) {
        if (ParamUtil.isNullOrEmptyOrZero(orderNo))
            return null;
        return tradeAlipayDao.selectLastTrade(orderNo);
    }


    /**
     * 支付宝预支付
     *
     * @param prePayDto
     * @param browserAgentType
     * @return
     */
    @Override
    public String prePay(PrePayDto prePayDto, Integer browserAgentType) {
        OrderExtPort orderExtPort = orderExtPortService.selectOne(new EntityWrapper<OrderExtPort>().eq("order_no", prePayDto.getOrderNo()));
        if (ParamUtil.isNullOrEmptyOrZero(orderExtPort)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        Map<String, Object> result = orderBaseService.checkBeforeOrder(prePayDto.getSno(), browserAgentType, prePayDto.getOpenid(), prePayDto.getMobile(), orderExtPort.getPort());
        //判断订单、设备、用户
        if (result == null) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        SysUserExt sysUserExt = (SysUserExt) result.get("sysUserExt");

        //根据订单号获取订单类型
        Integer orderType = LeaseUtil.getOrderType(prePayDto.getOrderNo());

        User user = userService.getUserByIdOrOpenidOrMobile(prePayDto.getMobile());
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        //判断订单类型
        if (orderType.equals(TradeOrderType.CONSUME.getCode())) {//消费订单
            OrderBase orderBase = orderBaseService.selectById(prePayDto.getOrderNo());
            if (Objects.isNull(orderBase)) {
                LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
            } else {
                orderBase.setPayType(prePayDto.getPayType());
                orderBase.setUtime(new Date());
                orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.PAYING.getCode());
            }
            return executePay(sysUserExt, orderBase.getOrderNo(), orderBase.getAmount(), orderBase.getServiceModeName(), orderBase.getMac(), orderType);
        } else if (orderType.equals(TradeOrderType.CHARGE.getCode())) {//充值订单
            UserWalletChargeOrder chargeOrder = userWalletChargeOrderService.selectOne(new EntityWrapper<UserWalletChargeOrder>().eq("charge_order_no", prePayDto.getOrderNo()));
            if (Objects.isNull(chargeOrder)) {
                LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
            }
            chargeOrder.setUtime(new Date());
            userWalletChargeOrderService.updateChargeOrderStatus(chargeOrder, UserWalletChargeOrderType.PAYING.getCode());
            return executePay(sysUserExt, chargeOrder.getChargeOrderNo(), chargeOrder.getFee(), chargeOrder.getWalletType() + "", chargeOrder.getWalletName(), orderType);
        } else {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_DONT_EXISTS);
        }
        return null;
    }

    public String executePay(SysUserExt sysUserExt, String orderNo, Double totalAmount, String subject, String body, Integer orderType) {
        if (Objects.isNull(sysUserExt)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_INUSING_FAIL);
        }

        //获得初始化的AlipayClient
        AlipayClient alipayClient = getAlipayClient(sysUserExt);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(sysUserExt.getAlipayReturnUrl());
        alipayRequest.setNotifyUrl(sysUserExt.getAlipayNotifyUrl());

        TradeBase tradeBase = tradeBaseService.createTrade(orderNo, totalAmount, sysUserExt.getAlipayNotifyUrl(), PayType.ALIPAY.getCode(), orderType);
        TradeAlipay tradeAlipay = new TradeAlipay();
        tradeAlipay.setSubject(subject);
        tradeAlipay.setAppid(sysUserExt.getAlipayAppid());
        tradeAlipay.setTradeNo(tradeBase.getTradeNo());
        insert(tradeAlipay);

        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(tradeBase.getTradeNo());
        model.setSubject(subject);
        model.setTotalAmount(totalAmount.toString());
        model.setBody(body);
        model.setProductCode("QUICK_WAP_PAY");
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(sysUserExt.getAlipayNotifyUrl());
        // 设置同步地址
        alipay_request.setReturnUrl(sysUserExt.getAlipayReturnUrl());

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + tradeBase.getTradeNo() + "\","
                + "\"total_amount\":\"" + totalAmount.toString() + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"QUICK_WAP_PAY\"}");

        String form = "";
        try {
            form = alipayClient.pageExecute(alipay_request).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
            LeaseException.throwSystemException(LeaseExceEnums.ALIPAY_PAY_FAILUR);
        }

        return form;
    }

    /**
     * 支付回调
     */
    @Override
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            //signVerified = AlipaySignature.rsaCheckV1(params, SystemConfigKey.ALIPAY_PUBLIC_KEY, SystemConfigKey.CHARSET, SysConfigUtils.get(CommonSystemConfig.class).getAlipaySignType()); //调用SDK验证签名
            SysUserExt sysUserExt = verifySignature(params);
            if (Objects.nonNull(sysUserExt)) {
                //商户订单号
                String out_trade_no = request.getParameter("out_trade_no");
                //支付宝交易号
                String alipay_trade_no = request.getParameter("trade_no");
                logger.info("支付宝交易号：" + alipay_trade_no);
                //交易状态
                String trade_status = request.getParameter("trade_status");
                //appId
                String appId = request.getParameter("app_id");
                logger.info("交易状态：" + trade_status + ",appId:" + appId);
                //判断appId是否是商户appId
                if (!Objects.equals(appId, sysUserExt.getAlipayAppid())) {
                    response.getWriter().write("fail");
                    return;
                }
                //交易金额
                Double totalAmount = Double.parseDouble(request.getParameter("total_amount"));
                //收款支付宝账号对应的支付宝唯一用户号
                String sellerId = request.getParameter("seller_id");
                //支付时间
                String time = request.getParameter("timestamp");

                if (alipayCallbackInnerLogic(sysUserExt, out_trade_no, totalAmount, trade_status, appId, alipay_trade_no, sellerId, time)) {
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("fail");
                }

            } else {
                response.getWriter().write("fail");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SysUserExt verifySignature(Map<String, String> params) {
        List<SysUserExt> list = sysUserExtService.selectList(new EntityWrapper<>());
        if (list == null || list.size() <= 0) {
            return null;
        }
        try {
            //由于微信验证的回调无法确定是哪个公众号,因此需要一个一个的循环验证,只要有一个验证通过就可以
            for (SysUserExt sysUserExt : list) {
                if (StringUtils.isNotBlank(sysUserExt.getAlipayAppid())
                        && StringUtils.isNotBlank(sysUserExt.getAlipayPublicKey())
                        && AlipaySignature.rsaCheckV1(params, sysUserExt.getAlipayPublicKey(),
                        SysConfigUtils.get(CommonSystemConfig.class).getAlipaySignCharset(), SysConfigUtils.get(CommonSystemConfig.class).getAlipaySignType())) {
                    return sysUserExt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LeaseException.throwSystemException(LeaseExceEnums.ALIPAY_NOTIFY_FAILUR);
            logger.error("===> 支付宝Token失败");
            return null;
        }
        return null;
    }

    /**
     * 回调内部逻辑处理
     */
    private boolean alipayCallbackInnerLogic(SysUserExt sysUserExt, String tradeNo, Double fee, String tradeStatus, String appId, String alipay_trade_no, String sellerId, String time) {
        String notify_url = sysUserExt.getAlipayNotifyUrl();
        TradeBase tradeBase = tradeBaseService.selectOne(new EntityWrapper<TradeBase>().eq("trade_no", tradeNo));
        Integer orderType = LeaseUtil.getOrderType(tradeBase.getOrderNo());

        //处理不同类型订单
        if (TradeOrderType.CONSUME.getCode().equals(orderType)) {
            if (!orderBaseService.checkAndUpdateConsumeOrder(tradeBase.getOrderNo(), fee)) {
                return false;
            }
        } else if (TradeOrderType.CHARGE.getCode().equals(orderType)) {
            if (!userWalletChargeOrderService.checkAndUpdateChargeOrder(fee, tradeBase.getOrderNo(), PayType.ALIPAY.getCode(), tradeNo)) {
                return false;
            }
        }

        TradeAlipay tradeAlipay = selectById(tradeBase.getTradeNo());
        if (Objects.isNull(tradeAlipay) || Objects.isNull(tradeBase)) {
            return false;
        }

        //如果交易单的状态为SUCCESS则不执行下面的操作
        if (TradeStatus.SUCCESS.getCode().equals(tradeBase.getStatus())) {
            logger.warn("===========>交易单:" + tradeBase.getTradeNo() + "已经交易成功，此次操作不做处理");
            return false;
        }
        tradeBase.setUtime(new Date());
        tradeBase.setNotifyUrl(notify_url);
        tradeBase.setStatus(TradeStatus.SUCCESS.getCode());
        tradeBase.setNofifyTime(DateKit.formatString2DateByDateTimePattern(time));

        tradeAlipay.setAlipayId(alipay_trade_no);
        tradeAlipay.setAppid(appId);
        tradeAlipay.setTradeNo(tradeBase.getTradeNo());
        tradeAlipay.setSellerId(sellerId);
        tradeAlipay.setTradeStatus(tradeStatus);
        return tradeBaseService.updateById(tradeBase) && updateById(tradeAlipay);
    }

    public boolean executePayStatusQuery(OrderBase orderBase) {
        if (orderBase.getPayType().equals(PayType.ALIPAY.getCode())) {
            TradeAlipay tradeAlipay = selectLastTrade(orderBase.getOrderNo());

            SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(orderBase.getSno());
            if (Objects.isNull(sysUserExt)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_INUSING_FAIL);
            }

            //获得初始化的AlipayClient
            AlipayClient alipayClient = getAlipayClient(sysUserExt);

            //设置请求参数
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

            //商户订单号，商户网站订单系统中唯一订单号
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + tradeAlipay.getTradeNo() + "\"}");

            String result = "";
            try {
                result = alipayClient.execute(alipayRequest).getBody();
            } catch (AlipayApiException e) {
                e.printStackTrace();
                LeaseException.throwSystemException(LeaseExceEnums.ALIPAY_PAY_FAILUR);
            }

            if (ParamUtil.isNullOrEmptyOrZero(result)) {
                LeaseException.throwSystemException(LeaseExceEnums.ALIPAY_ORDER_QUERY_FAIL);
            }
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject body = jsonObject.getJSONObject("alipay_trade_query_response");
            if (body.containsKey("code") && !body.getString("code").equals("10000")) {
                LeaseException.throwSystemException(LeaseExceEnums.ALIPAY_ORDER_QUERY_FAIL);
            }
            return body.containsKey("trade_status") && body.getString("trade_status").equals("TRADE_SUCCESS");
        }

        return false;
    }

    /**
     * 支付宝退款  此方法未经生产环境测试,如要使用请自测 Joey
     *
     * @param tradeNo 支付的时候对应的交易号
     * @return
     */
    public AlipayTradeRefundResponse refund(String tradeNo) {
        if (ParamUtil.isNullOrEmptyOrZero(tradeNo)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        TradeBase tradeBase = tradeBaseService.selectByTradeNo(tradeNo);
        if (Objects.isNull(tradeBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.TRADE_BASE_NOT_EXIST);
        }
        OrderBase orderBase = orderBaseService.getOrderBaseByOrderNo(tradeBase.getOrderNo());
        if (Objects.isNull(orderBase)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        refund(orderBase, tradeBase);
        return null;
    }

    /**
     * 单笔转账到支付宝账户接口
     */
    @Override
    public void transfer(String orderNo, String subject, Double amount, String account, String realName, Integer sysUserId) {
        // 查找支付宝配置
        SysUser sysUser = sysUserService.selectById(sysUserId);
        String[] parentIds = (sysUser.getTreePath() + sysUser.getId()).split(",");
        SysUserExt alipayConfig = sysUserExtService.selectOne(new EntityWrapper<SysUserExt>().in("sys_user_id", parentIds)
                .isNotNull("alipay_appid").orderBy("sys_user_id", false).last("limit 1"));
        if (alipayConfig == null) {
            LeaseException.throwSystemException(LeaseExceEnums.ALIPAY_PARAM_IS_NULL);
        }
        //创建跟踪单
        TradeBase tradeBase = tradeBaseService.createTrade(orderNo, amount, alipayConfig.getAlipayNotifyUrl(), PayType.ALIPAY.getCode(), TradeOrderType.REFUND.getCode());
        TradeAlipay tradeAlipay = new TradeAlipay();
        tradeAlipay.setSubject(subject);
        tradeAlipay.setAppid(alipayConfig.getAlipayAppid());
        tradeAlipay.setTradeNo(tradeBase.getTradeNo());
        insert(tradeAlipay);

        // 调用打款接口

        //获得初始化的AlipayClient
        AlipayClient alipayClient = getAlipayClient(alipayConfig);
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"out_biz_no\":\"").append(tradeBase.getTradeNo()).append("\",");
        sb.append("\"payee_type\":\"ALIPAY_LOGONID\",");
        sb.append("\"payee_account\":\"").append(account).append("\",");
        if (StringUtils.isNotBlank(realName)) {
            sb.append("\"payee_real_name\":\"").append(realName).append("\"");
        }
        sb.append("\"amount\":\"").append(amount).append("\"");
        sb.append("  }");
        request.setBizContent(sb.toString());
        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            logger.info("单笔转账到支付宝账户接口: Alipay response body: " + response.getBody());

            if (!response.isSuccess()) {
                logger.info("Alipay transfer is failure, reason code: " + response.getCode() + " reason :" + response.getMsg());
                LeaseException.throwSystemException(LeaseExceEnums.REFUND_FAIL);
            }
        } catch (AlipayApiException e) {
            logger.error("Alipay transfer is failure!", e);
            LeaseException.throwSystemException(LeaseExceEnums.REFUND_FAIL);
        }
    }

    @Override
    public void refund(OrderBase orderBase, TradeBase tradeBase) {
        SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(orderBase.getSno());
        if (Objects.isNull(sysUserExt)) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_INUSING_FAIL);
        }

        //获得初始化的AlipayClient
        AlipayClient alipayClient = getAlipayClient(sysUserExt);

        //验证参数
        TradeAlipay tradeAlipay = selectById(tradeBase.getTradeNo());
        if (Objects.isNull(tradeAlipay)) {
            LeaseException.throwSystemException(LeaseExceEnums.TRADE_BASE_NOT_EXIST);
        }

        //设置请求参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        //支付宝交易号
        String trade_no = tradeAlipay.getAlipayId();
        //支付宝交易金额
        String refund_amount = tradeBase.getTotalFee().toString();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + tradeBase.getTradeNo() + "\"," +
                "\"trade_no\":\"" + trade_no + "\"," +
                "\"refund_amount\":" + refund_amount + "," +
                "\"refund_reason\":\"正常退款\"," +
                "  }");

        //请求
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
            logger.info("支付宝退款结果：" + response.getBody());
            if (!response.isSuccess())
                LeaseException.throwSystemException(LeaseExceEnums.ORDER_REFUND_FAIL);
        } catch (AlipayApiException e) {
            logger.error("支付宝退款报错", e);
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_REFUND_FAIL);
        }
    }

    private AlipayClient getAlipayClient(SysUserExt sysUserExt) {
        //获得初始化的AlipayClient
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        return new DefaultAlipayClient(commonSystemConfig.getAlipayGetwayUrl(),
                sysUserExt.getAlipayAppid(), sysUserExt.getAlipayPrivateKey(), "json",
                commonSystemConfig.getAlipaySignCharset(), sysUserExt.getAlipayPublicKey(), commonSystemConfig.getAlipaySignType());
    }
}
