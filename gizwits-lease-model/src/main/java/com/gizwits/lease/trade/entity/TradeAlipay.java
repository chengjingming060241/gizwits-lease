package com.gizwits.lease.trade.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 支付宝交易表
 * </p>
 *
 * @author yinhui
 * @since 2017-08-15
 */
@TableName("trade_alipay")
public class TradeAlipay extends Model<TradeAlipay> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，与trade_base表关联
     */
    @TableId("trade_no")
	private String tradeNo;
    /**
     * 支付宝单号
     */
	@TableField("alipay_id")
	private String alipayId;
    /**
     * 支付宝应用APPID
     */
	private String appid;
    /**
     * 收款支付宝账号对应的支付宝唯一用户号
     */
	@TableField("seller_id")
	private String sellerId;
    /**
     * 订单标题
     */
	private String subject;
    /**
     * 交易状态：交易完成TRADE_FINISHED  交易成功TRADE_SUCCESS
     */
	@TableField("trade_status")
	private String tradeStatus;


	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getAlipayId() {
		return alipayId;
	}

	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	@Override
	protected Serializable pkVal() {
		return this.tradeNo;
	}

}
