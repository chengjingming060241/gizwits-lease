package com.gizwits.lease.operator.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 运营商信息扩展
 * </p>
 *
 * @author zhl
 * @since 2017-08-11
 */
@TableName("operator_ext")
public class OperatorExt extends Model<OperatorExt> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    /**
     * 对应的运营商ID字段
     */
	@TableField("operator_id")
	private Integer operatorId;
    /**
     * 押金(若配置此项，则用户必须交过押金才可使用设备)
     */
	@TableField("cash_pledge")
	private BigDecimal cashPledge;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public BigDecimal getCashPledge() {
		return cashPledge;
	}

	public void setCashPledge(BigDecimal cashPledge) {
		this.cashPledge = cashPledge;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
