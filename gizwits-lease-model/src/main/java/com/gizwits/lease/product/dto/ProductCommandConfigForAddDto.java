package com.gizwits.lease.product.dto;

/**
 * Dto - 添加指令（保存产品时）
 *
 * @author lilh
 * @date 2017/7/19 19:45
 */
public class ProductCommandConfigForAddDto {

    private Integer productId;

    /** 指令名称 */
    private String name;

    /** 指令类型 */
    private String commandType;

    /** 当指令类型为状态指令时，具体的状态指令类型 */
    private String statusCommandType;

    /** 指令，json格式 */
    private String command;
    /** 数据点名称*/
    private String identityName;

    /** 是否显示，在设备页面展示 */
    private Integer isShow;

    private Integer isFree;

    private String workingMode;

    private Integer isClockCorrect;

    /**
     * 换算数据点单位
     */
    private Integer calculateValue;

    /**
     * 误差范围
     */
    private Integer errorRange;

    /**展示形式 1文本 2饼状图 2进度条*/
    private Integer showType;

    private String clockCorrectDatapoint;

    /** 续费时参考的数据点 */
    private String refDataPoint;

    public String getClockCorrectDatapoint() {
        return clockCorrectDatapoint;
    }

    public void setClockCorrectDatapoint(String clockCorrectDatapoint) {
        this.clockCorrectDatapoint = clockCorrectDatapoint;
    }

    public Integer getIsClockCorrect() {
        return isClockCorrect;
    }

    public void setIsClockCorrect(Integer isClockCorrect) {
        this.isClockCorrect = isClockCorrect;
    }

    public Integer getCalculateValue() {
        return calculateValue;
    }

    public void setCalculateValue(Integer calculateValue) {
        this.calculateValue = calculateValue;
    }

    public Integer getErrorRange() {
        return errorRange;
    }

    public void setErrorRange(Integer errorRange) {
        this.errorRange = errorRange;
    }

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public String getIdentityName() { return identityName; }

    public void setIdentityName(String identityName) { this.identityName = identityName; }

    public String getStatusCommandType() {
        return statusCommandType;
    }

    public void setStatusCommandType(String statusCommandType) {
        this.statusCommandType = statusCommandType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getShowType() { return showType; }

    public void setShowType(Integer showType) { this.showType = showType; }

    public String getRefDataPoint() { return refDataPoint; }

    public void setRefDataPoint(String refDataPoint) { this.refDataPoint = refDataPoint; }
}
