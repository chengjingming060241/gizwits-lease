package com.gizwits.lease.event;

import java.util.List;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.enums.ChargeCardOperationType;
import com.gizwits.lease.user.entity.UserChargeCard;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 充值卡操作事件
 *
 * @author lilh
 * @date 2017/8/29 18:39
 */
public class UserChargeCardOperateEvent extends ApplicationEvent {
    private static final long serialVersionUID = 4583513796449771911L;

    private ChargeCardOperationType operationType;

    private SysUser current;

    private List<UserChargeCard> userChargeCards;

    private String ip;

    private UserChargeCardOperateEvent(List<UserChargeCard> userChargeCards, ChargeCardOperationType operationType, SysUser current, String ip) {
        super(new Object());
        this.userChargeCards = userChargeCards;
        this.operationType = operationType;
        this.current = current;
        this.ip = ip;
    }

    public ChargeCardOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ChargeCardOperationType operationType) {
        this.operationType = operationType;
    }

    public SysUser getCurrent() {
        return current;
    }

    public void setCurrent(SysUser current) {
        this.current = current;
    }

    public List<UserChargeCard> getUserChargeCards() {
        return userChargeCards;
    }

    public void setUserChargeCards(List<UserChargeCard> userChargeCards) {
        this.userChargeCards = userChargeCards;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static class Builder {

        private ChargeCardOperationType operationType;

        private SysUser current;

        private List<UserChargeCard> userChargeCards;

        private String ip;

        public static Builder create() {
            return new Builder();
        }

        public Builder operationType(ChargeCardOperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public Builder current(SysUser current) {
            this.current = current;
            return this;
        }

        public Builder chargeCards(List<UserChargeCard> chargeCards) {
            this.userChargeCards = chargeCards;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public UserChargeCardOperateEvent build() {
            return new UserChargeCardOperateEvent(userChargeCards, operationType, current, ip);
        }

    }
}
