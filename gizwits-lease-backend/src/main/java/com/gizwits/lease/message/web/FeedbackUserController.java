package com.gizwits.lease.message.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.message.entity.FeedbackUser;
import com.gizwits.lease.message.entity.dto.FeedbackUserDto;
import com.gizwits.lease.message.entity.dto.FeedbackQueryDto;
import com.gizwits.lease.message.service.FeedbackUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * <p>
 * 问题反馈表 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
@RestController
@EnableSwagger2
@Api(description = "意见反馈接口")
@RequestMapping("/message/feedbackUser")
public class FeedbackUserController extends BaseController {
    @Autowired
    private FeedbackUserService feedbackUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = "分页列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject list(@RequestBody @Valid RequestObject<Pageable<FeedbackQueryDto>> requestObject) {
        return success(feedbackUserService.page(requestObject.getData()));
    }

   /* @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加",notes = "添加",consumes = "application")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseObject add(@RequestBody @Valid RequestObject<FeedbackUserDto> requestObject){
        FeedbackUserDto feedbackDto = requestObject.getData();
        feedbackUserService.insertFeedbackUser(feedbackDto);
        return success();
    }*/

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", notes = "详情", consumes = "application")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseObject detail(@RequestBody @Valid RequestObject<Integer> requestObject) {
        FeedbackUser feedbackUser = feedbackUserService.selectById(requestObject.getData());
        String pictureUrl = feedbackUser.getPictureUrl();
        if (StringUtils.isNotBlank(pictureUrl)) {
            String[] picUrls = pictureUrl.split(";");
            if (picUrls.length > 0) {
                for (int i = 0; i < picUrls.length; i++) {
                    picUrls[i] = "/feedback/picture/" + picUrls[i];
                }
                feedbackUser.setPictureUrl(String.join(";", picUrls));
            }
        }
        return success(feedbackUser);
    }

}
