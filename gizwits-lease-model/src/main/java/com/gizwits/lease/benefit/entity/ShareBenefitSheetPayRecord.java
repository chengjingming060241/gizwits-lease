package com.gizwits.lease.benefit.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 分润单支付记录
 * </p>
 *
 * @author zhl
 * @since 2017-08-08
 */
@TableName("share_benefit_sheet_pay_record")
public class ShareBenefitSheetPayRecord extends Model<ShareBenefitSheetPayRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 分润单ID
     */
	@TableField("sheet_id")
	private Integer sheetId;
    /**
     * 交易订单号
     */
	@TableField("trade_no")
	private String tradeNo;
    /**
     * 分润金额
     */
	private BigDecimal amount;
    /**
     * 分润执行消息体
     */
	private String content;
    /**
     * 状态：1，执行分润；2，分润成功；3，分润失败；
     */
	private Integer status;
    /**
     * 操作者
     */
	@TableField("user_id")
	private Integer userId;
	private Date ctime;
	private Date utime;
    /**
     * 支付反馈结果
     */
	@TableField("callback_content")
	private String callbackContent;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSheetId() {
		return sheetId;
	}

	public void setSheetId(Integer sheetId) {
		this.sheetId = sheetId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getCallbackContent() {
		return callbackContent;
	}

	public void setCallbackContent(String callbackContent) {
		this.callbackContent = callbackContent;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
