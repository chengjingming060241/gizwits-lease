package com.gizwits.lease.trade.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author gagi
 * @since 2017-07-29
 */
@TableName("trade_weixin")
public class TradeWeixin extends Model<TradeWeixin> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，与trade_base表关联
     */
    @TableId("trade_no")
	private String tradeNo;
    /**
     * 微信单号
     */
	@TableField("transaction_id")
	private String transactionId;
    /**
     * 微信开放平台审核通过的应用APPID
     */
	private String appid;
    /**
     * 微信支付分配的商户号
     */
	@TableField("mch_id")
	private String mchId;
    /**
     * 商品描述
     */
	private String body;
    /**
     * 微信端,支付完成时间
     */
	@TableField("time_end")
	private String timeEnd;


	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	@Override
	protected Serializable pkVal() {
		return this.tradeNo;
	}

}
