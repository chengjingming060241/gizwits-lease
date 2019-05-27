package com.gizwits.lease;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.stat.service.StatDeviceLocationService;
import com.gizwits.lease.stat.service.StatDeviceOrderWidgetService;
import com.gizwits.lease.stat.service.StatDeviceTrendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class SysUserTests {

    private static final Logger logger = LoggerFactory.getLogger(SysUserTests.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private StatDeviceOrderWidgetService statDeviceOrderWidgetService;

    @Autowired
    private StatDeviceTrendService statDeviceTrendService;

    @Autowired
    private StatDeviceLocationService statDeviceLocationService;

    @Test
    public void test(){
//        statDeviceLocationService.setDataForLocation();
//        statDeviceOrderWidgetService.setDataForWidget();
        statDeviceTrendService.setDataForStatDeviceTrend();
    }






//    /**
//     * 更新 sys_user 的 is_admin 和 parent_admin_id 字段
//     */
//    @Test
//    public void updateSysUserAdminRoleAndParentAdmin() {
//        updateSysUserAdminRole();
//        updateSysUserParentAdmin();
//    }
//
//    @Test
//    public void updateSysUserAdminRole() {
//        Wrapper operatorWrapper = new EntityWrapper();
//        operatorWrapper.eq("is_deleted", 0).isNotNull("sys_account_id");
//        List<Operator> operatorList = operatorService.selectList(operatorWrapper);
//        operatorList.forEach(operator -> {
//            SysUser sysUser = sysUserService.selectById(operator.getSysAccountId());
//            if (sysUser == null) {
//                logger.warn("cannot found sysUser {} for operator", operator.getSysAccountId());
//            } else {
//                SysUser sysUserForUpdate = new SysUser();
//                sysUserForUpdate.setId(sysUser.getId());
//                sysUserForUpdate.setIsAdmin(SysUserType.OPERATOR.getCode());
//                sysUserService.updateById(sysUserForUpdate);
//            }
//        });
//
//        Wrapper agentWrapper = new EntityWrapper();
//        operatorWrapper.eq("is_deleted", 0).isNotNull("sys_account_id");
//        List<Agent> agentList = agentService.selectList(agentWrapper);
//        agentList.forEach(agent -> {
//            SysUser sysUser = sysUserService.selectById(agent.getSysAccountId());
//            if (sysUser == null) {
//                logger.warn("cannot found sysUser {} for agent", agent.getSysAccountId());
//            } else {
//                SysUser sysUserForUpdate = new SysUser();
//                sysUserForUpdate.setId(sysUser.getId());
//                sysUserForUpdate.setIsAdmin(SysUserType.AGENT.getCode());
//                sysUserService.updateById(sysUserForUpdate);
//            }
//        });
//
//        Wrapper manufacturerWrapper = new EntityWrapper();
//        operatorWrapper.eq("is_deleted", 0).isNotNull("sys_account_id");
//        List<Manufacturer> manufacturerList = manufacturerService.selectList(manufacturerWrapper);
//        manufacturerList.forEach(manufacturer -> {
//            SysUser sysUser = sysUserService.selectById(manufacturer.getSysAccountId());
//            if (sysUser == null) {
//                logger.warn("cannot found sysUser {} for manufacturer", manufacturer.getSysAccountId());
//            } else {
//                SysUser sysUserForUpdate = new SysUser();
//                sysUserForUpdate.setId(sysUser.getId());
//                sysUserForUpdate.setIsAdmin(SysUserType.MANUFACTURER.getCode());
//                sysUserService.updateById(sysUserForUpdate);
//            }
//        });
//
//        SysUser sysUserForUpdate = new SysUser();
//        sysUserForUpdate.setId(1);
//        sysUserForUpdate.setIsAdmin(SysUserType.SUPERADMIN.getCode());
//        sysUserService.updateById(sysUserForUpdate);
//
//        logger.warn("update sys_user is_admin done");
//    }
//
//    @Test
//    public void updateSysUserParentAdmin() {
//        List<SysUser> sysUserList = sysUserService.selectList(null);
//        sysUserList.forEach(sysUser -> {
//            SysUser parentAdmin = getParentAdmin(sysUser);
//            if (parentAdmin == null) {
//                logger.warn("cannot found parent admin for sysUser {}", sysUser.getId());
//            } else {
//                SysUser sysUserForUpdate = new SysUser();
//                sysUserForUpdate.setId(sysUser.getId());
//                sysUserForUpdate.setParentAdminId(parentAdmin.getId());
//                sysUserService.updateById(sysUserForUpdate);
//            }
//        });
//
//        logger.warn("update sys_user parent_admin_id done");
//    }
//
//    private SysUser getParentAdmin(SysUser sysUser) {
//        if (Objects.equals(sysUser.getSysUserId(), sysUser.getId())) {
//            logger.warn("sysUser {} parent is self", sysUser.getId());
//            return null;
//        }
//
//        SysUser parent = sysUserService.selectById(sysUser.getSysUserId());
//        if (parent == null) {
//            logger.warn("cannot find sysUser {}", sysUser.getSysUserId());
//            return null;
//        }
//
//        if (parent.getIsAdmin().equals(SysUserType.NORMAL.getCode())) {
//            return getParentAdmin(parent);
//        } else {
//            return parent;
//        }
//    }
}
