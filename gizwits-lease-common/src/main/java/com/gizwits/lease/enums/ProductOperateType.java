package com.gizwits.lease.enums;

import com.gizwits.lease.constant.CommandType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * Enum - 产品操作类型
 *
 * @author lilh
 * @date 2017/7/20 16:29
 */
public enum ProductOperateType {

    BASIC(1, "更新基本信息"),
    DATA_POINT(2, "数据点同步"),
    SERVICE_COMMAND(3, "修改收费类型"),
    CONTROL_COMMAND(4, "修改控制指令"),
    STATUS_COMMAND(5, "修改状态指令"),
    DEVICE_BASIC(6, "更新设备基本信息"),
    DEVICE_OPERATION(7, "更新运营信息"),
    SHOW_DATA(8,"数据点展示");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToDesc;

    private static Map<String, ProductOperateType> commandToOperateType = new HashMap<>();

    static {
        codeToDesc = Arrays.stream(ProductOperateType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
        commandToOperateType.put(CommandType.SERVICE.getCode(), SERVICE_COMMAND);
        commandToOperateType.put(CommandType.CONTROL.getCode(), CONTROL_COMMAND);
        commandToOperateType.put(CommandType.STATUS.getCode(), STATUS_COMMAND);
        commandToOperateType.put(CommandType.SHOW.getCode(), SHOW_DATA);
    }

    ProductOperateType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(Integer code) {
        return codeToDesc.get(code);
    }

    public static ProductOperateType getOperateType(String commandType) {
        return commandToOperateType.get(commandType);
    }
}
