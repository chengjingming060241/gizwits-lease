package com.gizwits.lease.majiang.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.user.dto.UserChargeCardOpenidDto;
import com.gizwits.lease.user.dto.UserForInfoDto;
import com.gizwits.lease.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;

/**
 * Created by xpg on 01/09/2017.
 */
@RestController("mahjong")
@RequestMapping("/mahjong/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "麻将用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ResponseObject<UserForInfoDto> getUserInfo(@RequestBody @Valid RequestObject<UserChargeCardOpenidDto> requestObject) {
        return success(userService.getUserInfo(requestObject.getData()));
    }
}
