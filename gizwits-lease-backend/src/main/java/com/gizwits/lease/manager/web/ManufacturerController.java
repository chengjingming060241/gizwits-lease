package com.gizwits.lease.manager.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.service.ManufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 厂商(或企业)表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@RestController
@EnableSwagger2
@Api(description = "厂商接口")
@RequestMapping("/manager/manufacturer")
public class ManufacturerController extends BaseController {

    @Autowired
    private ManufacturerService manufacturerService;

    @ApiOperation(value = "添加", notes = "添加",consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody    ResponseObject add(@RequestBody @Valid RequestObject<Manufacturer> requestObject){
        manufacturerService.insert(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "分页查询", notes = "分页查询",consumes = "application/json")
    @RequestMapping(value = "/getListByPage",method = RequestMethod.POST)
    public @ResponseBody ResponseObject getListByPage(@RequestBody  @Valid RequestObject<Page<Manufacturer>> requestObject ){
        EntityWrapper<Manufacturer> entityWrapper = new EntityWrapper<Manufacturer>();
        Page<Manufacturer>  page = requestObject.getData();
        entityWrapper.orderBy(page.getOrderByField(),page.isAsc());
        Page<Manufacturer> selectPage= manufacturerService.selectPage(page,entityWrapper);
        return success(selectPage);
    }

    @ApiOperation(value = "更新", notes = "更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody ResponseObject update(@RequestBody Manufacturer Manufacturer){
        Manufacturer.setUtime(new Date());
        manufacturerService.updateById(Manufacturer);
        return success();
    }

    @ApiOperation(value = "删除", notes = "删除",consumes = "application/json")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public @ResponseBody ResponseObject delete(@RequestBody List<Integer> ids){
        manufacturerService.deleteBatchIds(ids);
        return success();
    }
}
