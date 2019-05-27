package com.gizwits.lease.app.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.message.service.FeedbackUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Created by zhl on 2017/9/8.
 */
@EnableSwagger2
@Api(description = "移动端用户反馈")
@RestController
@RequestMapping("/app/feedback")
public class FeedBackController extends BaseController {
    @Autowired
    private FeedbackUserService feedbackUserService;

    @ApiOperation(value = "移动端问题反馈", consumes = "application/json")
    @PostMapping("/create")
    public ResponseObject create(@RequestParam("files") List<MultipartFile> files,
                                 @RequestParam("sno") String sno,
                                 @RequestParam("phone") String phone,
                                 @RequestParam(value = "content", required = true) String content,
                                 @RequestParam("origin")Integer origin,
                                 @RequestParam("openid") String openid) {
        feedbackUserService.saveUserFeedback(files, sno, phone, content, origin, openid);
        return success();
    }
}
