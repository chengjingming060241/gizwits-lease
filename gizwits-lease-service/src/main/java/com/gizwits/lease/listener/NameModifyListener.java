package com.gizwits.lease.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.device.entity.*;
import com.gizwits.lease.device.service.*;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.message.entity.*;
import com.gizwits.lease.message.service.FeedbackUserService;
import com.gizwits.lease.message.service.SysMessageToUserService;
import com.gizwits.lease.product.entity.*;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserChargeCard;
import com.gizwits.lease.user.entity.UserChargeCardOrder;
import com.gizwits.lease.user.service.UserChargeCardOrderService;
import com.gizwits.lease.user.service.UserChargeCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NameModifyListener implements ApplicationListener<NameModifyEvent> {

    private static final Logger log = LoggerFactory.getLogger(NameModifyListener.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;


    @Autowired
    private DeviceGroupService deviceGroupService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;


    @Autowired
    private DeviceLaunchAreaAssignRecordService deviceLaunchAreaAssignRecordService;


    @Autowired
    private UserChargeCardService userChargeCardService;

    @Autowired
    private SysMessageToUserService sysMessageToUserService;

    @Autowired
    private UserChargeCardOrderService userChargeCardOrderService;

    @Autowired
    private FeedbackUserService feedbackUserService;


    @Override
    @Async
    public void onApplicationEvent(NameModifyEvent nameModifyEvent) {
        log.info("实体id={}的名称从{}变为{}", nameModifyEvent.getId(),
                nameModifyEvent.getOldName(), nameModifyEvent.getNewName());
        Object entity = nameModifyEvent.getSource();
        if (entity instanceof Device) {
            doAfterDeviceName(nameModifyEvent);
        } else if (entity instanceof DeviceLaunchArea) {
            doAfterLaunchAreaName(nameModifyEvent);
        } else if (entity instanceof ProductServiceMode) {
            doAfterServiceModeName(nameModifyEvent);
        } else if (entity instanceof User) {
            doAfterUserName(nameModifyEvent);
        } else if (entity instanceof Operator) {
            doAfterOperatorName(nameModifyEvent);
        } else if (entity instanceof Agent) {
            doAfterAgentName(nameModifyEvent);
        } else if (entity instanceof Product) {
            doAfterProductName(nameModifyEvent);
        } else if (entity instanceof ProductCategory) {
            doAfterProductCategorylName(nameModifyEvent);
        }
    }

    private void doAfterDeviceName(NameModifyEvent<String> nameModifyEvent) {

    }

    private void doAfterLaunchAreaName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info("投放点名称修改 id=" + nameModifyEvent.getId());
        // 更新设备列表信息
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()).eq("launch_area_id", nameModifyEvent.getId());
        Device device = new Device();
        device.setUtime(new Date());
        device.setLaunchAreaName(nameModifyEvent.getNewName());
        deviceService.update(device, entityWrapper);
        // 更新投放点的分配记录
        DeviceLaunchAreaAssignRecord areaAssignRecord = new DeviceLaunchAreaAssignRecord();
        areaAssignRecord.setDeviceLaunchAreaName(nameModifyEvent.getNewName());
        EntityWrapper<DeviceLaunchAreaAssignRecord> entityWrapper2 = new EntityWrapper<>();
        entityWrapper2.eq("device_launch_area_id", nameModifyEvent.getId());
        deviceLaunchAreaAssignRecordService.update(areaAssignRecord, entityWrapper2);
    }

    private void doAfterServiceModeName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info("收费模式名称修改 id=" + nameModifyEvent.getId());
        //更新设备列表信息
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()).eq("service_id", nameModifyEvent.getId());
        Device device = new Device();
        device.setUtime(new Date());
        device.setServiceName(nameModifyEvent.getNewName());
        deviceService.update(device, entityWrapper);
    }


    private void doAfterUserName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info("用户名称修改 id=" + nameModifyEvent.getId());
        // 用户充值卡
        EntityWrapper<UserChargeCard> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", nameModifyEvent.getId());
        UserChargeCard userChargeCard = new UserChargeCard();
        userChargeCard.setUtime(new Date());
        userChargeCard.setUserName(nameModifyEvent.getNewName());
        userChargeCardService.update(userChargeCard, entityWrapper);
        // 系统消息
        SysMessageToUser sysMessageToUser = new SysMessageToUser();
        sysMessageToUser.setUtime(new Date());
        sysMessageToUser.setUsername(nameModifyEvent.getNewName());
        EntityWrapper<SysMessageToUser> entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.eq("user_id", nameModifyEvent.getId());
        sysMessageToUserService.update(sysMessageToUser, entityWrapper1);
        // 用户充值卡订单
        UserChargeCardOrder userChargeCardOrder = new UserChargeCardOrder();
        userChargeCard.setUtime(new Date());
        userChargeCard.setUserName(nameModifyEvent.getNewName());
        EntityWrapper<UserChargeCardOrder> entityWrapper2 = new EntityWrapper<>();
        entityWrapper2.eq("user_id", nameModifyEvent.getId());
        userChargeCardOrderService.update(userChargeCardOrder, entityWrapper2);
        // 意见反馈
        EntityWrapper<FeedbackUser> entityWrapper3 = new EntityWrapper<>();
        entityWrapper3.eq("user_id", nameModifyEvent.getId());
        FeedbackUser feedbackUser = new FeedbackUser();
        feedbackUser.setUtime(new Date());
        feedbackUser.setNickName(nameModifyEvent.getNewName());
        feedbackUserService.update(feedbackUser, entityWrapper3);
    }

    private void doAfterOperatorName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info("运营商名称修改 id=" + nameModifyEvent.getId());
        // 分润规则修改
        Wrapper entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.eq("sys_account_id", nameModifyEvent.getId());
        ShareBenefitRule shareBenefitRule = new ShareBenefitRule();
        shareBenefitRule.setUtime(new Date());
        shareBenefitRule.setOperatorName(nameModifyEvent.getNewName());
        shareBenefitRuleService.update(shareBenefitRule, entityWrapper1);
        // 分润单修改
        EntityWrapper<ShareBenefitSheet> entityWrapper2 = new EntityWrapper<>();
        entityWrapper2.eq("sys_account_id", nameModifyEvent.getId());
        ShareBenefitSheet shareBenefitSheet = new ShareBenefitSheet();
        shareBenefitSheet.setOperatorName(nameModifyEvent.getNewName());
        shareBenefitSheet.setUtime(new Date());
        shareBenefitSheetService.update(shareBenefitSheet, entityWrapper2);
        // 更新设备分组
        EntityWrapper<DeviceGroup> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("assigned_account_id", nameModifyEvent.getId());
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setUtime(new Date());
        deviceGroup.setAssignedName(nameModifyEvent.getNewName());
        deviceGroupService.update(deviceGroup, entityWrapper);

        //押金钱包修改
       /* UserWallet userWallet = new UserWallet();
        userWallet.setUsername(nameModifyEvent.getNewName());
        userWallet.setUtime(new Date());
        EntityWrapper<UserWallet> entityWrapper3 = new EntityWrapper<>();
        entityWrapper3.eq("sys_user_id",nameModifyEvent.getId());
        userWalletService.update(userWallet,entityWrapper3);*/

    }

    private void doAfterAgentName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info(" 代理商名称修改 id=" + nameModifyEvent.getId());
        // 分润规则修改
        Wrapper entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.eq("sys_account_id", nameModifyEvent.getId());
        ShareBenefitRule shareBenefitRule = new ShareBenefitRule();
        shareBenefitRule.setUtime(new Date());
        shareBenefitRule.setOperatorName(nameModifyEvent.getNewName());
        shareBenefitRuleService.update(shareBenefitRule, entityWrapper1);
        // 分润单修改
        EntityWrapper<ShareBenefitSheet> entityWrapper2 = new EntityWrapper<>();
        entityWrapper2.eq("sys_account_id", nameModifyEvent.getId());
        ShareBenefitSheet shareBenefitSheet = new ShareBenefitSheet();
        shareBenefitSheet.setOperatorName(nameModifyEvent.getNewName());
        shareBenefitSheet.setUtime(new Date());
        shareBenefitSheetService.update(shareBenefitSheet, entityWrapper2);
        // 更新设备分组
        EntityWrapper<DeviceGroup> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("assigned_account_id", nameModifyEvent.getId());
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setUtime(new Date());
        deviceGroup.setAssignedName(nameModifyEvent.getNewName());
        deviceGroupService.update(deviceGroup, entityWrapper);
    }

    private void doAfterProductName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info("产品名称修改 id =" + nameModifyEvent.getId());
        // 更新设备列表
        EntityWrapper<Device> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("product_id", nameModifyEvent.getId());
        Device device = new Device();
        device.setUtime(new Date());
        device.setProductName(nameModifyEvent.getNewName());
        deviceService.update(device, entityWrapper);
        // 更新产品属性对应表
       /* EntityWrapper<ProductToProperties> entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.eq("product_id", nameModifyEvent.getId());
        ProductToProperties productToProperties = new ProductToProperties();
        productToProperties.setUtime(new Date());
        productToProperties.setProductName(nameModifyEvent.getNewName());
        productToPropertiesService.update(productToProperties, entityWrapper1);*/
    }

    private void doAfterProductCategorylName(NameModifyEvent<Integer> nameModifyEvent) {
        log.info("产品种类名称修改 id=" + nameModifyEvent.getId());
        // 更新产品列表
        EntityWrapper<Product> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("category_id", nameModifyEvent.getId());
        Product product = new Product();
        product.setUtime(new Date());
        product.setCategoryName(nameModifyEvent.getNewName());
        productService.update(product, entityWrapper);
        // 更新产品属性表
        /*EntityWrapper<ProductProperties> entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.eq("category_id", nameModifyEvent.getId());
        ProductProperties productProperties = new ProductProperties();
        productProperties.setUtime(new Date());
        productProperties.setCategoryName(nameModifyEvent.getNewName());
        productPropertiesService.update(productProperties, entityWrapper1);*/

    }
}
