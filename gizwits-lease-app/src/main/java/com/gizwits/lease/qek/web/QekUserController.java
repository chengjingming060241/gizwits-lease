package com.gizwits.lease.qek.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.user.dto.UserForDetailDto;
import com.gizwits.lease.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/23.
 */
@RestController
@EnableSwagger2
@Api(description = "沁尔康用户个人中心")
@RequestMapping("/app/qek/user")
public class QekUserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "个人中心", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<UserForDetailDto> detail(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(userService.getUserDetail(requestObject.getData()));
    }
}
