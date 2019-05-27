package com.gizwits.lease.stat.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.ShareBenefitType;
import com.gizwits.boot.sys.entity.SysRole;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.entity.SysUserToRole;
import com.gizwits.boot.sys.service.SysRoleService;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserToRoleService;
import com.gizwits.boot.utils.IdGenerator;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.benefit.dto.ShareBenefitOperator;
import com.gizwits.lease.benefit.entity.*;
import com.gizwits.lease.benefit.service.*;
import com.gizwits.lease.config.CronConfig;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.ShareBenefitRuleFrequency;
import com.gizwits.lease.enums.ShareBenefitSheetStatusType;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhl on 2017/8/4.
 */
@Component
public class ShareBenefitScheduler {

    private static Logger logger = LoggerFactory.getLogger("BENEFIT_LOGGER");

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserToRoleService sysUserToRoleService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitRuleDetailService shareBenefitRuleDetailService;

    @Autowired
    private ShareBenefitRuleDetailDeviceService shareBenefitRuleDetailDeviceService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private ShareBenefitSheetOrderService shareBenefitSheetOrderService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CronConfig cronConfig;


    /**
     * 生成分润账单
     * 1.查询系统中所有可用的分润规则
     * 2.判断分润规则是否到了执行时间
     * 3.加载分润规则中相应设备的订单
     * 4.计算并生成分润单
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void schedulerShareBenefit() {
        logger.info("====开始执行分润操作=====");
        List<ShareBenefitRule> allShareRuleList = shareBenefitRuleService.getAllUsableShareRule();
        logger.info("====分润规则个数{}=====",allShareRuleList.size());
        if(CollectionUtils.isNotEmpty(allShareRuleList)){
            Date now = new Date();
            //循环可用的分润规则
            for(ShareBenefitRule rule:allShareRuleList){
                if(!checkShareRuleIsRightTime(rule.getStartTime(),rule.getLastExecuteTime(),rule.getFrequency())){
                    logger.info("======分润规则{},startTime:{},lastExecuteTime:{},frequency:{}不符合执行条件,暂不执行分润===",rule.getId(),rule.getStartTime(),rule.getLastExecuteTime(),rule.getFrequency());
                    continue;
                }
                shareBenefitSheetService.generateShareBenefitForShareRule(rule);
            }
        }
    }

    /**
     * 此方法废弃,单可作为日后其他规则的参考
     *
     * 1.查询所有独立运营的用户,并开始循环
     * 2.查询独立运营创建直属的所有收账角色的用户(包含收账角色创建的子角色,如收账角色用户已与运营商关联,则不再往下查询)
     */
    @Deprecated
    //@Scheduled(cron = "#{cronConfig.getDaily()}")  //每天凌晨过5分执行
    public void summaryOrder(){
        List<SysUserExt> sysUserExtList = getAllCanIncomeUser();
        if(!ParamUtil.isNullOrEmptyOrZero(sysUserExtList)){
            for(SysUserExt userExt:sysUserExtList){ //循环独立运营者(不同厂商)
                List<Map<SysUserExt,ShareBenefitOperator>> firstLevelOperators = new ArrayList<>();
                Map<String,BigDecimal> devicePercentage = new HashedMap();//设备的分润比例: key为设备的sno; value为设备在此运营商的分润比例(运营商按层级比例收取分润金额)
                getChildrenNeedShareUserExt(userExt.getSysUserId(),firstLevelOperators); //获取直属第一级运营商

                circleOperator(userExt, devicePercentage,firstLevelOperators);//执行循环回调的方法,每循环一次,代表执行一个层级的运营商
            }
        }
    }

    /**
     * 1.循环该层级的运营商
     *  1.1.获取运营商的下级运营商列表,并存放在nextLevelOperators链表中
     *  1.2.获取运营商的分润规则
     *  1.3.为该运营商设备的所有订单进行统计,生成分润单
     * 2.回调函数,执行下一个层级的运营商分润逻辑
     *
     * @param independenceOperatorUser 独立运营的顶级用户信息(入账信息)
     * @param devicePercentage 设备在该层级的分润比例
     * @param childrenOperators 同一层级的运营商列表
     */
    public void circleOperator(SysUserExt independenceOperatorUser, Map<String,BigDecimal> devicePercentage, List<Map<SysUserExt,ShareBenefitOperator>> childrenOperators){
        if(childrenOperators !=null&& childrenOperators.size()>0){
            List<Map<SysUserExt,ShareBenefitOperator>> nextLevelOperators = new ArrayList<>();

            for(Map<SysUserExt,ShareBenefitOperator> currentOperator: childrenOperators){
                SysUserExt sysUserExt = currentOperator.keySet().iterator().next();
                ShareBenefitOperator operatorObj = currentOperator.get(sysUserExt);
                getChildrenNeedShareUserExt(operatorObj.getSysAccountId(), nextLevelOperators);//获取运营商的下一级运营商列表

                if(ParamUtil.isNullOrEmptyOrZero(operatorObj.getShareBenefitRuleId())){//判断当前运营商是否分配了分润规则
                    continue;
                }

                //查询分润规则
                ShareBenefitRule rule = shareBenefitRuleService.getRuleBySysAccountId(operatorObj.getId());
                if(rule==null){
                    continue;
                }
                executeShareOrderForOperator(independenceOperatorUser, sysUserExt, rule, operatorObj, devicePercentage);
            }

            circleOperator(independenceOperatorUser, devicePercentage, nextLevelOperators);
        }

    }


    /**
     * 为运营商生成分润单
     * 1.devicePercentage为空则说明该运营商没有设备参与分润
     * 2.判断分润规则是否要执行分润操作
     * 3.获取分润规则的详细规则信息
     * 4.循环详细分润规则
     *  4.1.获取详细分润规则中涉及的设备列表
     *  4.2.循环设备列表
     *   4.2.1.判断设备在devicePercentage中是否存在,如果存在说明上一级参与了分润,并给定分润比例,然后乘以本级设置的分润比例作为作为本级的真实分润比例
     *   4.2.2.获取设备上所有未分润的订单
     *    4.2.2.1.将订单放到分润单的详细表中,同时计算订单在该运营商的分润金额
     * 5.保存分润单主表数据和分润单的详细信息
     *
     * @param independenceOperatorUser 独立运营的顶级用户信息(入账信息)
     * @param sysUserExt 运营商关联的用户扩展信息
     * @param rule 分润规则
     * @param operator 运营商
     * @param devicePercentage 设备在上个层级的分润比例
     * @return
     */
    private boolean executeShareOrderForOperator(SysUserExt independenceOperatorUser, SysUserExt sysUserExt, ShareBenefitRule rule, ShareBenefitOperator operator, Map<String,BigDecimal> devicePercentage){
        if(rule==null || operator==null || devicePercentage==null || devicePercentage.keySet().size()<=0){
            return false;
        }

        //判断运营商的分润规则是否满足执行条件
        boolean isExecute = checkShareRuleIsRightTime(rule.getStartTime(),rule.getLastExecuteTime(),rule.getFrequency());

        Date now = new Date();
        ShareBenefitSheet sheet = new ShareBenefitSheet();
        sheet.setSheetNo(IdGenerator.generateOrderNo());

        List<ShareBenefitSheetOrder> sheetOrderList = new ArrayList<>();
        int orderCount = 0;
        BigDecimal totalMoney = BigDecimal.ZERO;
        BigDecimal shareMoney = BigDecimal.ZERO;

        List<ShareBenefitRuleDetail> ruleDetailList = shareBenefitRuleDetailService.selectByRuleId(rule.getId());
        if(!ParamUtil.isNullOrEmptyOrZero(ruleDetailList)){
            for (ShareBenefitRuleDetail ruleDetail:ruleDetailList) {//循环每个运营商的分润规则详情
                List<ShareBenefitRuleDetailDevice> deviceList = shareBenefitRuleDetailDeviceService.selectByRuleDetailId(ruleDetail.getId());
                if(!ParamUtil.isNullOrEmptyOrZero(deviceList)){
                    for(ShareBenefitRuleDetailDevice deviceDto:deviceList) {//循环设备
                        BigDecimal preDevicePercentage = BigDecimal.ZERO;
                        BigDecimal nowDevicePercentage = ruleDetail.getSharePercentage().divide(new BigDecimal(100),5,BigDecimal.ROUND_HALF_UP);//本级运营商设置的分润比例

                        if(devicePercentage.containsKey(deviceDto.getSno())){
                            preDevicePercentage = devicePercentage.get(deviceDto.getSno());
                            devicePercentage.put(deviceDto.getSno(), preDevicePercentage.multiply(nowDevicePercentage));//上级分润比例与本级运营上的乘积
                        }else{
                            devicePercentage.put(deviceDto.getSno(), nowDevicePercentage);
                        }

                        List<OrderBase> orderList = orderBaseService.getReadyForShareBenefit(deviceDto.getSno(),rule.getLastExecuteTime());
                        //需要执行分润才生成分润单,否则只需要将设备的分润比例填充到Map中
                        if(isExecute && !ParamUtil.isNullOrEmptyOrZero(orderList)){
                            for(OrderBase order:orderList){
                                ShareBenefitSheetOrder sheetOrder = new ShareBenefitSheetOrder();
                                sheetOrder.setSheetNo(sheet.getSheetNo());
                                sheetOrder.setOrderNo(order.getOrderNo());
                                sheetOrder.setSysAccountId(operator.getSysAccountId());
                                sheetOrder.setDeviceSno(deviceDto.getSno());

                                // 订单全部有优惠金额支付,不参与分润
                                if (order.getPayType().equals(PayType.DISCOUNT.getCode())) {
                                    sheetOrder.setOrderAmount(0.00D);
                                } else if (order.getPayType().equals(PayType.BALANCE_DISCOUNT.getCode())) {
                                    // 订单由钱包余额和优惠金额组成
                                    // realMoney字段已被删除
                                    // sheetOrder.setOrderAmount(order.getRealMoney());
                                    // 订单由钱包余额和优惠金额组成时 除去优惠金额
                                    // sheetOrder.setOrderAmount(order.getAmount() - order.getPromotionMoney());

                                    // 产于分润的订单金额为该用户实际充值金额/（该用户实际充值金额+赠送金额）*订单总额
                                    sheetOrder.setOrderAmount(
                                            (order.getRealRecharge() / (order.getRealRecharge() + order.getRealDiscount())) * order.getAmount());

                                } else if (order.getPayType().equals(PayType.BALANCE.getCode())) {
                                    // 订单全部由钱包金额支付
                                    sheetOrder.setOrderAmount(order.getAmount());
                                } else {
                                    sheetOrder.setOrderAmount(order.getAmount());
                                }
                                sheetOrder.setShareRuleDetailId(ruleDetail.getId());
                                sheetOrder.setStatus(ShareBenefitSheetStatusType.TO_AUDIT.getCode());
                                sheetOrder.setSharePercentage(ruleDetail.getSharePercentage().doubleValue());
                                sheetOrder.setChildrenSharePercentage(preDevicePercentage.doubleValue());
                                //计算分润金额
                                sheetOrder.setShareMoney(devicePercentage.get(deviceDto.getSno()).multiply(new BigDecimal(sheetOrder.getOrderAmount())).doubleValue());

                                orderCount++;
                                // totalMoney = totalMoney.add(new BigDecimal(order.getAmount()));
                                totalMoney = totalMoney.add(new BigDecimal(sheetOrder.getOrderAmount()));
                                shareMoney = shareMoney.add(new BigDecimal(sheetOrder.getShareMoney()));
                                sheetOrderList.add(sheetOrder);
                            }
                        }
                    }
                }
            }
            sheet.setSysAccountId(operator.getSysAccountId());
            sheet.setOperatorName(operator.getName());
            sheet.setStatus(ShareBenefitSheetStatusType.TO_AUDIT.getCode());
            sheet.setPayType(PayType.WEIXINPAY.getCode());
            sheet.setCtime(now);
            sheet.setPayAccount(independenceOperatorUser.getWxId());
            sheet.setReceiverOpenid(sysUserExt.getReceiverOpenId());
            sheet.setReceiverName(sysUserExt.getReceiverWxName());
            sheet.setOrderCount(orderCount);
            sheet.setTotalMoney(totalMoney.doubleValue());
            sheet.setShareMoney(shareMoney.doubleValue());
            if(isExecute){
                if(shareBenefitSheetService.insert(sheet)){
                    shareBenefitSheetOrderService.insertBatch(sheetOrderList);
                    return true;
                }
            }else{
                return true;
            }

        }

        return false;
    }

    /**
     *判断分润规则是否到执行时间
     * @param startTime
     * @param lastExecuteTime
     * @param frequency
     * @return
     */
    private boolean checkShareRuleIsRightTime(Date startTime, Date lastExecuteTime, String frequency){
        if(startTime.after(new Date())){//还未到结算时间
            return false;
        }
        if(Objects.isNull(lastExecuteTime)){//还未执行过
            return true;
        }

        if(frequency.equals(ShareBenefitRuleFrequency.DAY.getCode())){
            return true;
        }else if(frequency.equals(ShareBenefitRuleFrequency.WEEK.getCode())){
            return equalDay(lastExecuteTime, Calendar.DAY_OF_WEEK);
        }else if(frequency.equals(ShareBenefitRuleFrequency.MONTH.getCode())){
            return equalDay(lastExecuteTime, Calendar.DAY_OF_MONTH);
        }else if(frequency.equals(ShareBenefitRuleFrequency.YEAR.getCode())){
            return equalDay(lastExecuteTime, Calendar.DAY_OF_YEAR);
        }

        return false;
    }

    private boolean equalDay(Date toEqualDate, int field){
        if(Objects.isNull(toEqualDate)){
            return true;
        }

        Calendar lastCal =  Calendar.getInstance();
        lastCal.setTime(toEqualDate);
        int toEq = lastCal.get(field);

        Calendar nowCal =  Calendar.getInstance();
        nowCal.setTime(new Date());
        int nowWeekDay = nowCal.get(field);
        return toEq == nowWeekDay;
    }

    /**
     * 1.获取运营商创建的所有拥有收款权限的角色
     * 2.查询所有拥有收款权限的用户
     * 3.循环用户列表
     *  3.1.判断用户是否跟运营商关联
     *   3.1.1.如果用户和运营商已经关联,说明创建出子运营商,查询出运营商的信息,存放在 List<Map<SysUserExt,ShareBenefitOperator>> resultList中
     *   3.1.2.如果用户没有跟运营商关联,回调该函数,查询该用户创建的子用户,直至子用户跟运营商关联或者没有子用户
     *
     * @param userId
     * @param resultList
     * @return
     */
    private void getChildrenNeedShareUserExt(Integer userId, List<Map<SysUserExt,ShareBenefitOperator>> resultList){
        List<SysRole> roleList = getUserCreatedNeedShareRoles(userId);//获取用户创建的拥有收款权限的角色
        if(!ParamUtil.isNullOrEmptyOrZero(roleList)){
            List<Integer> roleIds = roleList.stream().map(SysRole::getSysUserId).collect(Collectors.toList());
            EntityWrapper<SysUserToRole> userToRoleEntityWrapper = new EntityWrapper<>();
            userToRoleEntityWrapper.in("role_id",roleIds);
            List<SysUserToRole> userToRoleList = sysUserToRoleService.selectList(userToRoleEntityWrapper);

            if(!ParamUtil.isNullOrEmptyOrZero(userToRoleList)){
                for(SysUserToRole userToRole:userToRoleList){
                    Operator operator = getOperatorByAccountId(userToRole.getUserId());
                    if(operator!=null){
                        Map<SysUserExt,ShareBenefitOperator> map = new HashedMap();
                        map.put(sysUserExtService.selectById(userToRole.getUserId()),new ShareBenefitOperator(operator));
                        resultList.add(map);
                    }else{
                        Agent agent = agentService.getAgentBySysAccountId(userToRole.getUserId());
                        if(Objects.nonNull(agent)){
                            Map<SysUserExt,ShareBenefitOperator> map = new HashedMap();
                            map.put(sysUserExtService.selectById(userToRole.getUserId()),new ShareBenefitOperator(agent));
                            resultList.add(map);
                        }else{
                            getChildrenNeedShareUserExt(userToRole.getUserId(),resultList);//回调函数
                        }
                    }
                }
            }
        }
    }


    /**
     * 获取用户创建的有收款权限的角色
     * @param userId
     * @return
     */
    private List<SysRole> getUserCreatedNeedShareRoles(Integer userId){
        EntityWrapper<SysRole> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("share_benefit_type", ShareBenefitType.BOOKED.getCode())
                .eq("sys_user_id",userId);
        return sysRoleService.selectList(entityWrapper);
    }

    /**
     * 根据Account_ID查询运营商
     * @param userId
     * @return
     */
    private Operator getOperatorByAccountId(Integer userId){
        EntityWrapper<Operator> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sys_account_id",userId);
        return operatorService.selectOne(entityWrapper);
    }

    /**
     * 获取所有的入账角色
     * @return
     */
    private List<SysRole> getAllCanIncomeRole(){
        EntityWrapper<SysRole> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("share_benefit_type", ShareBenefitType.RECEIPT.getCode());
        return sysRoleService.selectList(entityWrapper);
    }

    /**
     * 获取所有入账用户扩展信息
     * @return
     */
    private List<SysUserExt> getAllCanIncomeUser(){
        List<SysRole> roleList = getAllCanIncomeRole();
        if(!ParamUtil.isNullOrEmptyOrZero(roleList)){
            List<Integer> roleIds = roleList.stream().map(SysRole::getSysUserId).collect(Collectors.toList());

            EntityWrapper<SysUserToRole> entityWrapper = new EntityWrapper<>();
            entityWrapper.in("role_id",roleIds);
            List<SysUserToRole> userToRoleList = sysUserToRoleService.selectList(entityWrapper);
            if(!ParamUtil.isNullOrEmptyOrZero(userToRoleList)){
                List<Integer> userIds = userToRoleList.stream().map(SysUserToRole::getUserId).collect(Collectors.toList());
                EntityWrapper<SysUserExt> userExtEntityWrapper = new EntityWrapper<>();
                userExtEntityWrapper.in("sys_user_id",userIds);
                return sysUserExtService.selectList(userExtEntityWrapper);
            }
        }

        return null;
    }
}
