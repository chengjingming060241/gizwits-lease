package com.gizwits.lease.device.web;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.DeviceExcelTemplate;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceQrcodeService;
import com.gizwits.lease.device.vo.DeviceQrcodeExportDto;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.utils.ImportExcelUtils;
import com.gizwits.lease.utils.ZipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller - 导出设备二维码
 *
 * @author lilh
 * @date 2017/8/30 13:59
 */
@Api(description = "导出设备二维码")
@RestController
@RequestMapping("/device/qrcode")
public class DeviceQrcodeExportController extends BaseController {


    @Autowired
    private DeviceQrcodeService deviceQrcodeService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "导出二维码", consumes = "application/json")
    @PostMapping(value = "/export")
    public void export(@RequestBody @Valid RequestObject<DeviceQrcodeExportDto> requestObject, HttpServletResponse response) throws IOException {
        //1.生成二维码图片：增加设备，生成二维码
        List<Device> devices = deviceQrcodeService.resolveDevices(requestObject.getData());
        //2.生成模板文件
        deviceQrcodeService.initTemplateExcel();
        //压缩
        Set<String> files = resolveFiles(devices);
        ZipUtils.doCompress(files, response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "导入设备")
    @PostMapping("/upload")
    public ResponseObject<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        List<List<Object>> originData = ImportExcelUtils.parse(file.getInputStream(), file.getOriginalFilename());
        return success(deviceQrcodeService.importExcel(convert(originData)));
    }

    private List<DeviceExcelTemplate> convert(List<List<Object>> originData) {
        if (CollectionUtils.isEmpty(originData)) {
            LeaseException.throwSystemException(LeaseExceEnums.EXCEL_NO_DATA);
        }
        return originData.stream().filter(this::isValid).map(list -> new DeviceExcelTemplate(String.valueOf(list.get(0)), String.valueOf(list.get(1)))).collect(Collectors.toList());
    }

    private boolean isValid(List<Object> list) {
        return CollectionUtils.isNotEmpty(list) && list.size() == 2 && Objects.nonNull(list.get(0)) && Objects.nonNull(list.get(1));
    }

    private Set<String> resolveFiles(List<Device> devices) {
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        Set<String> files = devices.stream().map(item -> commonSystemConfig.getQrcodePath() + "/" + item.getSno() + ".jpg").collect(Collectors.toSet());
        files.add(commonSystemConfig.getQrcodePath() + commonSystemConfig.getDefaultDeviceExcelTemplateFile());
        return files;
    }
}
