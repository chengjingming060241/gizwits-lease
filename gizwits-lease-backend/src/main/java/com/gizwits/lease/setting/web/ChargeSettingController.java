package com.gizwits.lease.setting.web;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.setting.dto.ChargeSettingAddDto;
import com.gizwits.lease.setting.dto.ChargeSettingListDto;
import com.gizwits.lease.setting.dto.ChargeSettingQueryDto;
import com.gizwits.lease.setting.service.ChargeSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 充值设定 前端控制器
 * </p>
 *
 * @author lilh
 * @since 2017-09-01
 */
@Api(description = "充值设定")
@RestController
@RequestMapping("/setting/chargeSetting")
public class ChargeSettingController extends BaseController {

    @Autowired
    private ChargeSettingService chargeSettingService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", consumes = "application/json")
    @PostMapping("/add")
    public ResponseObject<Boolean> add(@RequestBody @Valid RequestObject<ChargeSettingAddDto> requestObject) {
        return success(chargeSettingService.add(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<List<ChargeSettingListDto>> list(@RequestBody RequestObject<ChargeSettingQueryDto> requestObject) {
        ChargeSettingQueryDto query = requestObject.getData();
        if (Objects.isNull(query)) {
            query = new ChargeSettingQueryDto();
        }
        //query.setAccessableUserIds(Collections.singletonList(sysUserService.getCurrentUser().getId()));
        return success(chargeSettingService.list(query));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", consumes = "application/json")
    @PostMapping("/delete")
    public ResponseObject<Boolean> delete(@RequestBody @Valid RequestObject<Integer> requestObject) {
        return success(chargeSettingService.deleteById(requestObject.getData()));
    }
}
