package com.gizwits.lease.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.event.SysUserNameModifyEvent;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description:
 * User: yinhui
 * Date: 2018-03-15
 */
@Component
public class SysUserNameModifyListener implements ApplicationListener<SysUserNameModifyEvent> {


    private static final Logger log = LoggerFactory.getLogger(SysUserNameModifyListener.class);


    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;


    @Autowired
    private AgentService agentService;


    @Autowired
    private OperatorService operatorService;

    @Override
    @Async
    public void onApplicationEvent(SysUserNameModifyEvent event) {
        log.info("实体id{}的名称从{}变为{}",  event.getId(),
                event.getOldName(), event.getNewName());
        if (event.getSource() instanceof SysUser) {
            doAfterSysUserName(event);
        }else if(event.getSource() instanceof SysUserExt){
            doAfterSysUserExtName(event);
        }
    }

    private void doAfterSysUserName(SysUserNameModifyEvent<Integer> nameModifyEvent) {
        log.info("系统用户名称修改 id="+nameModifyEvent.getId());
        //代理商
        EntityWrapper<Agent> entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.eq("sys_user_id",nameModifyEvent.getId());
        Agent agent = new Agent();
        agent.setUtime(new Date());
        agent.setSysUserName(nameModifyEvent.getNewName());
        agentService.update(agent,entityWrapper1);
        //设备拥有者
//        EntityWrapper<Device> entityWrapper3 = new EntityWrapper<>();
//        entityWrapper3.eq("owner_id",nameModifyEvent.getId());
//        Device device = new Device();
//        device.setUtime(new Date());
//        device.setOwnerName(nameModifyEvent.getNewName());
//        deviceService.update(device,entityWrapper3);
        //运营商
        EntityWrapper<Operator> entityWrapper9 = new EntityWrapper<>();
        entityWrapper9.eq("sys_user_id",nameModifyEvent.getId());
        Operator operator = new Operator();
        operator.setUtime(new Date());
        operator.setSysUserName(nameModifyEvent.getNewName());
        operatorService.update(operator,entityWrapper9);

    }

    private void doAfterSysUserExtName(SysUserNameModifyEvent<Integer> nameModifyEvent) {
        log.info("修改分润单里面的收款信息 sysUserId = "+nameModifyEvent.getId());
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("sys_account_id",nameModifyEvent.getId());
        ShareBenefitSheet shareBenefitSheet = new ShareBenefitSheet();
        shareBenefitSheet.setReceiverName(nameModifyEvent.getNewName());
        shareBenefitSheet.setReceiverOpenid(nameModifyEvent.getNewOpenId());
        shareBenefitSheet.setUtime(new Date());
        shareBenefitSheetService.update(shareBenefitSheet,wrapper);
    }
}