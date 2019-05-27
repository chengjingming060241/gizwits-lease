package com.gizwits.lease.panel.web;

import java.util.List;
import java.util.Map;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.panel.dto.ItemDto;
import com.gizwits.lease.panel.dto.ItemForUpdateDto;
import com.gizwits.lease.panel.dto.MainPageShowPanelDto;
import com.gizwits.lease.panel.service.PersonalPanelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Controller - 首页面板
 *
 * @author lilh
 * @date 2017/7/17 12:08
 */
@RestController
@EnableSwagger2
@Api(description = "首页面板")
@RequestMapping("/panel")
public class PersonalPanelController extends BaseController {

    @Autowired
    private PersonalPanelService personalPanelService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "个人面板", consumes = "application/json")
    @PostMapping("/main")
    public ResponseObject<MainPageShowPanelDto> mainPanel() {
        return success(personalPanelService.getCurrentShowPanelData(sysUserService.getCurrentUser()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "编辑", consumes = "application/json")
    @PostMapping("/edit")
    public ResponseObject<Map<String, Map<String, List<ItemDto>>>> edit() {
        return success(personalPanelService.getEditPanelData(sysUserService.getCurrentUser()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "保存", consumes = "application/json")
    @PostMapping("/update")
    public ResponseObject update(@RequestBody RequestObject<ItemForUpdateDto> requestObject) {
        return success(personalPanelService.update(requestObject.getData()));
    }

}
