package com.gizwits.lease.config.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Jcxcc
 * @date 2018/9/28
 * @since 1.0
 */
@Data
@ToString
@Accessors(chain = true)
public class SnotiDeviceControlDTO {

    private String did;

    private String mac;

    /**
     * pk
     */
    private String productKey;

    /**
     * 数据点控制
     */
    private JSONObject attrs;
}
