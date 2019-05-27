package com.gizwits.lease.product.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
@TableName("product_command_config")
public class ProductCommandConfig extends Model<ProductCommandConfig> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    /**
     * 产品ID
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 指令类型：SERVICE,收费类型指令；CONTROL,控制类型指令；STATUS,状态类型指令
     */
	@TableField("command_type")
	private String commandType;

	/**
	 * 状态指令类型：FREE,空闲指令；USING,使用中指令；FINISH,设备使用完成指令
	 */
	@TableField("status_command_type")
	private String statusCommandType;
    /**
     * 指令名称
     */
	private String name;
    /**
     * 下发指令
     */
	private String command;
    /**
     * 是否在后台展示：0,不展示；1,展示
     */
	@TableField("is_show")
	private Integer isShow;
	private Date ctime;
	private Date utime;
    /**
     * 是否删除：0,否；1,删除
     */
	@TableField("is_deleted")
	private Integer isDeleted;
    /**
     * 是否免费，0，收费，1，免费
     */
	@TableField("is_free")
	private Integer isFree;
    /**
     * 工作模式
     */
	@TableField("working_mode")
	private String workingMode;
    /**
     * 是否需要时钟校准,0 否,1 是
     */
	@TableField("is_clock_correct")
	private Integer isClockCorrect;
    /**
     * 换算数据点单位
     */
	@TableField("calculate_value")
	private Integer calculateValue;
    /**
     * 误差范围
     */
	@TableField("error_range")
	private Integer errorRange;
    /**
     * 时钟校准的数据点
     */
	@TableField("clock_correct_datapoint")
	private String clockCorrectDatapoint;
    /**
     * 数据点标识名
     */
	@TableField("identity_name")
	private String identityName;
    /**
     * 续费时参考的数据点
     */
	@TableField("ref_dp")
	private String refDataPoint;
    /**
     * 展示形式：1 文本 2饼状图 3进度条
     */
	@TableField("show_type")
	private Integer showType;

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

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}

	public String getWorkingMode() {
		return workingMode;
	}

	public void setWorkingMode(String workingMode) {
		this.workingMode = workingMode;
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

	public String getClockCorrectDatapoint() {
		return clockCorrectDatapoint;
	}

	public void setClockCorrectDatapoint(String clockCorrectDatapoint) {
		this.clockCorrectDatapoint = clockCorrectDatapoint;
	}

	public String getIdentityName() {
		return identityName;
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}


	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getRefDataPoint() { return refDataPoint; }

	public void setRefDataPoint(String refDataPoint) { this.refDataPoint = refDataPoint; }

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
