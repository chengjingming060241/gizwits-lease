package com.gizwits.lease.product.vo;

/**
 * Created by zhl on 2017/7/14.
 */
public class ProductCommandVO {

    private Integer id;
    private Integer productId;
    private String productKey;
    private String commandType;
    private String statusCommandType;
    private String name;
    private String command;
    private Integer isFree;

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getStatusCommandType() {
        return statusCommandType;
    }

    public void setStatusCommandType(String statusCommandType) {
        this.statusCommandType = statusCommandType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
