package com.gizwits.lease.device.service;

import java.util.List;

import com.gizwits.lease.constant.DeviceExcelTemplate;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.vo.DeviceQrcodeExportDto;

/**
 * @author lilh
 * @date 2017/8/30 16:51
 */
public interface DeviceQrcodeService {

    /**
     * 预插入设备
     */
    List<Device> resolveDevices(DeviceQrcodeExportDto dto);

    /**
     * 初始化模板数据
     */
    boolean initTemplateExcel();


    /**
     * 导入数据
     */
    boolean importExcel(List<DeviceExcelTemplate> needToImportData);
}
