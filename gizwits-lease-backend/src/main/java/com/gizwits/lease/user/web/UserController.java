package com.gizwits.lease.user.web;

import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.enums.MoveType;
import com.gizwits.lease.user.dto.UserForDetailDto;
import com.gizwits.lease.user.dto.UserForListDto;
import com.gizwits.lease.user.dto.UserForMoveDto;
import com.gizwits.lease.user.dto.UserForQueryDto;
import com.gizwits.lease.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 用户表,不要前缀,因为用户模块计划抽象成通用功能 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@EnableSwagger2
@Api(value = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "用户列表", notes = "分页查询", consumes = "application/json")
    @PostMapping(value = "/page")
    public ResponseObject<Page<UserForListDto>> page(@RequestBody @Valid RequestObject<Pageable<UserForQueryDto>> requestObject) {
        Pageable<UserForQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new UserForQueryDto());
        }
        return success(userService.page(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<UserForDetailDto> detail(@RequestBody RequestObject<String> requestObject) {
        return success(userService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "移入黑名单", consumes = "application/json")
    @PostMapping("/moveIn")
    public ResponseObject moveInBlack(@RequestBody @Valid RequestObject<UserForMoveDto> requestObject) {
        return success(userService.move(requestObject.getData(), MoveType.MOVE_IN_BLACK));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "移出黑名单", consumes = "application/json")
    @PostMapping("/moveOut")
    public ResponseObject moveOutBlack(@RequestBody @Valid RequestObject<UserForMoveDto> requestObject) {
        return success(userService.move(requestObject.getData(), MoveType.MOVE_OUT_BLACK));
    }

    @ApiOperation(value = "短信验证码（忘记密码）", notes = "短信验证码（忘记密码）", consumes = "application/json")
    @RequestMapping(value = "/messageCode", method = RequestMethod.POST)
    public @ResponseBody ResponseObject messageCode(@RequestBody @Valid RequestObject<String> requestObject) {
        userService.messageCode(requestObject.getData());
        return success();
    }
}
