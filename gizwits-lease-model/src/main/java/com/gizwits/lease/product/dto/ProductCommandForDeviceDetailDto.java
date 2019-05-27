package com.gizwits.lease.product.dto;

import com.gizwits.lease.product.entity.ProductCommandConfig;
import org.springframework.beans.BeanUtils;

/**
 * @author lilh
 * @date 2017/7/21 12:01
 */
public class ProductCommandForDeviceDetailDto {

    private Integer id;

    private String name;

    private String commandType;

    private String command;

    private Object value;


    public ProductCommandForDeviceDetailDto(ProductCommandConfig productCommandConfig) {
        BeanUtils.copyProperties(productCommandConfig, this);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
