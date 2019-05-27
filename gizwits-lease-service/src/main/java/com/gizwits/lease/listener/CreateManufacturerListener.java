package com.gizwits.lease.listener;

import java.util.Date;

import com.gizwits.boot.event.SysUserCreatedEvent;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.service.ManufacturerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 创建厂商
 *
 * @author lilh
 * @date 2017/7/26 19:39
 */
@Component
public class CreateManufacturerListener implements ApplicationListener<SysUserCreatedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateManufacturerListener.class);

    @Autowired
    private ManufacturerService manufacturerService;

    @Override
    @Async
    public void onApplicationEvent(SysUserCreatedEvent event) {
        if (support(event)) {
            saveManufacturer(event);
        }
    }

    private void saveManufacturer(SysUserCreatedEvent event) {
        SysUser user = event.getSysUser();
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCtime(new Date());
        manufacturer.setUtime(new Date());
        manufacturer.setName(StringUtils.isEmpty(user.getNickName()) ? user.getUsername() : user.getNickName());
        manufacturer.setSysAccountId(user.getId());
        manufacturer.setSysUserId(user.getSysUserId());
        manufacturer.setSysUserName(user.getUsername());
        manufacturerService.insert(manufacturer);
        LOGGER.info("创建厂商【" + user.getUsername() + "】成功");
    }

    private boolean support(SysUserCreatedEvent event) {
        return CollectionUtils.isNotEmpty(event.getRoleIds()) && event.getRoleIds().contains(getDefaultManufacturerRoleId());
    }

    private Integer getDefaultManufacturerRoleId() {
        //配置的厂商角色
        return Integer.parseInt(SysConfigUtils.get(CommonSystemConfig.class).getDefaultManufacturerRoleId());
    }
}
