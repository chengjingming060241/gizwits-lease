
package com.gizwits.lease.product.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.CommandType;
import com.gizwits.lease.enums.ProductOperateType;
import com.gizwits.lease.event.ProductServiceCommandUpdateEvent;
import com.gizwits.lease.event.ProductUpdatedEvent;
import com.gizwits.lease.event.StatusCommandUpdatedEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.dao.ProductCommandConfigDao;
import com.gizwits.lease.product.dto.ProductCommandConfigForAddDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForDetailDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForQueryDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForUpdateDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.vo.ProductCommandVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
@Service
public class ProductCommandConfigServiceImpl extends ServiceImpl<ProductCommandConfigDao, ProductCommandConfig> implements ProductCommandConfigService {

    @Autowired
    private ProductCommandConfigDao productCommandConfigDao;

    @Autowired
    private ProductService productService;

    @Override
    public List<ProductCommandVO> getAllUseableConfig() {
        return productCommandConfigDao.findAllUseableStatusCommandConfig();
    }

    /**
     * 获取收费指令中的设备模式切换指令
     * @param commandConfig
     * @return
     */
    public String getDeviceModelCommand(ProductCommandConfig commandConfig){
        String command = commandConfig.getCommand();
        if(!ParamUtil.isNullOrEmptyOrZero(command)){
            JSONObject commandJson = JSONObject.parseObject(command);
            if(commandJson.containsKey("deviceModel")){
                return commandJson.getString("deviceModel");
            }
        }
        return null;
    }

    /**
     * 获取指令中的单位
     * @param commandConfig
     * @return
     */
    public String getSpecialDisplayUnit(ProductCommandConfig commandConfig) {
        String command = commandConfig.getCommand();
        if (!ParamUtil.isNullOrEmptyOrZero(command)) {
            JSONObject commandJson = JSONObject.parseObject(command);
            if (commandJson.containsKey("special")) {
                JSONObject special = commandJson.getJSONObject("special");
                return special.getString("displayUnit");
            }
        }
        return "";

    }

    @Override
    public List<ProductCommandConfigForDetailDto> list(ProductCommandConfigForQueryDto query) {
        Wrapper<ProductCommandConfig> wrapper = new EntityWrapper<>();
        wrapper.eq("product_id", query.getProductId()).eq("command_type", query.getCommandType())
        .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        List<ProductCommandConfig> list = selectList(wrapper);
        return list.stream().map(ProductCommandConfigForDetailDto::new).collect(Collectors.toList());

    }

    @Override
    public boolean delete(Integer id) {
        ProductCommandConfig exist = selectById(id);
        if (Objects.isNull(exist)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        exist.setIsDeleted(DeleteStatus.DELETED.getCode());
        updateById(exist);
        //发布事件
        publish(exist);
        return true;
    }

    @Override
    public boolean update(ProductCommandConfigForUpdateDto dto) {
        ProductCommandConfig commandConfig = selectOne(new EntityWrapper<ProductCommandConfig>().eq("id", dto.getId()).eq("product_id", dto.getProductId()));
        if (Objects.isNull(commandConfig)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        BeanUtils.copyProperties(dto, commandConfig);
        commandConfig.setUtime(new Date());
        updateById(commandConfig);
        //发布事件
        publish(commandConfig);
        return true;
    }

    @Override
    public ProductCommandConfig add(ProductCommandConfigForAddDto dto) {
        ProductCommandConfig command = new ProductCommandConfig();
        BeanUtils.copyProperties(dto, command);
        command.setCtime(new Date());
        command.setUtime(command.getCtime());
        command.setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        insert(command);
        //发布事件
        publish(command);
        return command;
    }

    private void publish(ProductCommandConfig command) {
        Product product = productService.getProductByProductId(command.getProductId());

        productService.publishChangeEvent(command.getProductId(), ProductOperateType.getOperateType(command.getCommandType()));
        //如果是更新状态指令，则发个事件
        if (Objects.equals(command.getCommandType(), CommandType.STATUS.getCode())) {
            CommonEventPublisherUtils.publishEvent(new ProductUpdatedEvent(product.getGizwitsProductKey()));
            CommonEventPublisherUtils.publishEvent(new StatusCommandUpdatedEvent(command));
        }
        //收费指令修改
        if (Objects.equals(command.getCommandType(), CommandType.SERVICE.getCode())){
            CommonEventPublisherUtils.publishEvent(new ProductServiceCommandUpdateEvent(command));
        }
    }


    /**
     * 根据收费模式指令和数量,生成指定模式的下发指令
     *
     * @param commandConfig
     * @param num
     * @return {
     * "normal":{"Switch":true,"Speed":1}, //订单下发指令
     * "special":{                         //需要经过换算后跟订单指令一起下发
     * "name":"Area_Quantity",
     * "type":"unit",
     * "min":0,
     * "max":50,
     * "step":1,
     * "displayUnit":"平方米",
     * "dataPointUnit":"平方厘米",
     * "multiplyValue": 10000
     * },
     * "deviceModel":{"Working_Mode":"limited"} //设备切换收费模式后下发
     * }
     */

    public String getCommandByConfig(ProductCommandConfig commandConfig, Double num){
        String command = commandConfig.getCommand();
        if (!ParamUtil.isNullOrEmptyOrZero(command)) {
            JSONObject commandJson = JSONObject.parseObject(command);
            if (!commandJson.containsKey("special")) {
                return command;

            }else{
//                JSONObject newCommand = new JSONObject();
//                if(commandJson.containsKey("deviceModel")){
//                    newCommand.put("deviceModel",commandJson.getJSONObject("deviceModel").toJSONString());
//                }

                JSONObject normalCommand = commandJson.getJSONObject("normal");
                if(commandJson.containsKey("special") && num!=null && num>=0){
                    JSONObject special = commandJson.getJSONObject("special");
                    if(!StringUtils.isEmpty(special.getString("name"))){
                        normalCommand.put(special.getString("name"),num * special.getInteger("multiplyValue"));
                    }
                }


                //newCommand.put("normal",normalCommand.toJSONString());
                return normalCommand.toJSONString();
            }
        }

        return null;
    }



}
