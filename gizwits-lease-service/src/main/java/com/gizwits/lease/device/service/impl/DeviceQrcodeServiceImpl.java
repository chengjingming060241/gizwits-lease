package com.gizwits.lease.device.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.DeviceExcelTemplate;
import com.gizwits.lease.constant.DeviceNormalStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceQrcodeService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.vo.DeviceQrcodeExportDto;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.utils.JxlExcelUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lilh
 * @date 2017/8/30 16:52
 */
@Service
public class DeviceQrcodeServiceImpl implements DeviceQrcodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceQrcodeServiceImpl.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<Device> resolveDevices(DeviceQrcodeExportDto dto) {
        Product product = resolveProduct(dto.getProductId());
        SysUser current = sysUserService.getCurrentUser();
        List<Device> devices = initDevices(product, current, dto.getCount());
        deviceService.insertBatch(devices);
        return devices;
    }

    @Override
    public boolean initTemplateExcel() {
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        File template = new File(commonSystemConfig.getQrcodePath() + commonSystemConfig.getDefaultDeviceExcelTemplateFile());
        if (!template.getParentFile().exists()) {
            template.getParentFile().mkdirs();
        }
        if (!template.exists()) {
            //生成模板文件
            try (FileOutputStream outputStream = new FileOutputStream(template)) {
                JxlExcelUtils.listToExcel(null, StringUtils.split(commonSystemConfig.getDefaultDeviceExcelTemplateTitle(), ','), null, "列表", outputStream);
                return true;
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return true;
    }

    @Override
    public boolean importExcel(List<DeviceExcelTemplate> validData) {
        if (CollectionUtils.isEmpty(validData)) {
            LeaseException.throwSystemException(LeaseExceEnums.EXCEL_NO_DATA);
        }
        LOGGER.info("待导入的数据共:{}条", validData.size());
        //1.根据二维码查询出待更新的设备数据
        List<Device> needToUpdateDevices = resolveDevicesByQrcode(validData);
        if (CollectionUtils.isEmpty(needToUpdateDevices)) {
            LOGGER.info("没有数据需要导入");
            return true;
        }
        Map<String, String> codeToMac = validData.stream().collect(Collectors.toMap(DeviceExcelTemplate::getQrcode, DeviceExcelTemplate::getMac));
        needToUpdateDevices.forEach(device -> {
            device.setUtime(new Date());
            device.setEntryTime(new Date());
            device.setStatus(DeviceNormalStatus.ENTRY.getCode());
            device.setMac(Objects.isNull(codeToMac.get(device.getContentUrl())) ? codeToMac.get(device.getWxTicket()) : codeToMac.get(device.getContentUrl()));
            device.setName(device.getMac());
        });
        deviceService.updateBatchById(needToUpdateDevices);
        LOGGER.info("已导入{}条设备数据", needToUpdateDevices.size());
        LOGGER.info("已导入设备的数据为:{}", needToUpdateDevices.stream().collect(Collectors.toMap(Device::getMac, item -> Objects.isNull(codeToMac.get(item.getContentUrl())) ? codeToMac.get(item.getWxTicket()) : codeToMac.get(item.getContentUrl()))));
        return true;
    }

    private List<Device> resolveDevicesByQrcode(List<DeviceExcelTemplate> validData) {
        Set<String> qrcodes = validData.stream().map(DeviceExcelTemplate::getQrcode).collect(Collectors.toSet());
        Wrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.in("wx_ticket", new ArrayList<>(qrcodes)).eq("status", DeviceNormalStatus.WAIT_TO_ENTRY.getCode())
                .or().in("content_url", qrcodes).eq("status", DeviceNormalStatus.WAIT_TO_ENTRY.getCode());
        return deviceService.selectList(wrapper);
    }

    private List<Device> initDevices(Product product, SysUser current, Integer count) {
        return IntStream.range(0, count).mapToObj(item -> {
            Device device = new Device();
            device.setProductId(product.getId());
            device.setProductName(product.getName());
            device.setOwnerId(current.getId());
            device.setSysUserId(current.getId());
            device.setStatus(DeviceNormalStatus.WAIT_TO_ENTRY.getCode());
            device.setSno(deviceService.getSno());
            //把mac和name设置成sno,入库后再更新
            device.setMac(device.getSno());
            device.setName(device.getSno().substring(0, 10));
            device.setCtime(new Date());
            device.setUtime(new Date());
            //创建二维码及设备认证
            device = deviceService.createQrcodeAndAuthDevice(device, product, current);
            return device;
        }).collect(Collectors.toList());
    }

    private Product resolveProduct(Integer productId) {
        if (Objects.isNull(productId)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        Product product = productService.selectOne(new EntityWrapper<Product>().eq("id", productId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (Objects.isNull(product)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        return product;
    }
}
