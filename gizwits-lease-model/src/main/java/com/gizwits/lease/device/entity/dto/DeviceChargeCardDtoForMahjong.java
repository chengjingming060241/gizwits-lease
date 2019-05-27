package com.gizwits.lease.device.entity.dto;

import com.alibaba.fastjson.JSON;

/**
 * Created by GaGi on 2017/9/2.
 */
public class DeviceChargeCardDtoForMahjong {

    private Boolean Switch;
    private Boolean Stop;
    private String CardNum;
    /**
     * 工作模式 0.按次收费，1.按圈收费，2.按锅收费，3.免费模式，4.定时收费，5.不定时收费
     */
    private String Working_Mode;
    private Boolean WorkCompleted;
    private Integer EndTime;
//    private Date Machine_Time;
    /**
     * 时钟校准, 仅下发为true时, MCU执行时钟校准动作
     */
    private Boolean Clock_Correction;

    private Boolean FaultA;
    private Boolean FaultB;
    private Boolean FaultC;
    private Boolean FaultD;
    private Boolean FaultE;
    private Boolean FaultF;
    private Boolean FaultG;
    private Boolean FaultH;
    private Boolean FaultI;
    private Boolean FaultJ;
    private Boolean FaultK;
    private Boolean FaultL;
    private Boolean FaultM;
    private Boolean FaultN;
    private Boolean FaultO;

    private Integer Voltage;
    private Boolean Await;

    public Boolean getSwitch() {
        return Switch;
    }

    public void setSwitch(Boolean aSwitch) {
        Switch = aSwitch;
    }

    public Boolean getStop() {
        return Stop;
    }

    public void setStop(Boolean stop) {
        Stop = stop;
    }

    public String getCardNum() {
        return CardNum;
    }

    public void setCardNum(String cardNum) {
        CardNum = cardNum;
    }

    public String getWorking_Mode() {
        return Working_Mode;
    }

    public void setWorking_Mode(String working_Mode) {
        Working_Mode = working_Mode;
    }

    public Boolean getWorkCompleted() {
        return WorkCompleted;
    }

    public void setWorkCompleted(Boolean workCompleted) {
        WorkCompleted = workCompleted;
    }

    public Integer getEndTime() {
        return EndTime;
    }

    public void setEndTime(Integer endTime) {
        EndTime = endTime;
    }

    public Boolean getClock_Correction() {
        return Clock_Correction;
    }

    public void setClock_Correction(Boolean clock_Correction) {
        Clock_Correction = clock_Correction;
    }

    public Boolean getFaultA() {
        return FaultA;
    }

    public void setFaultA(Boolean faultA) {
        FaultA = faultA;
    }

    public Boolean getFaultB() {
        return FaultB;
    }

    public void setFaultB(Boolean faultB) {
        FaultB = faultB;
    }

    public Boolean getFaultC() {
        return FaultC;
    }

    public void setFaultC(Boolean faultC) {
        FaultC = faultC;
    }

    public Boolean getFaultD() {
        return FaultD;
    }

    public void setFaultD(Boolean faultD) {
        FaultD = faultD;
    }

    public Boolean getFaultE() {
        return FaultE;
    }

    public void setFaultE(Boolean faultE) {
        FaultE = faultE;
    }

    public Boolean getFaultF() {
        return FaultF;
    }

    public void setFaultF(Boolean faultF) {
        FaultF = faultF;
    }

    public Boolean getFaultG() {
        return FaultG;
    }

    public void setFaultG(Boolean faultG) {
        FaultG = faultG;
    }

    public Boolean getFaultH() {
        return FaultH;
    }

    public void setFaultH(Boolean faultH) {
        FaultH = faultH;
    }

    public Boolean getFaultI() {
        return FaultI;
    }

    public void setFaultI(Boolean faultI) {
        FaultI = faultI;
    }

    public Boolean getFaultJ() {
        return FaultJ;
    }

    public void setFaultJ(Boolean faultJ) {
        FaultJ = faultJ;
    }

    public Boolean getFaultK() {
        return FaultK;
    }

    public void setFaultK(Boolean faultK) {
        FaultK = faultK;
    }

    public Boolean getFaultL() {
        return FaultL;
    }

    public void setFaultL(Boolean faultL) {
        FaultL = faultL;
    }

    public Boolean getFaultM() {
        return FaultM;
    }

    public void setFaultM(Boolean faultM) {
        FaultM = faultM;
    }

    public Boolean getFaultN() {
        return FaultN;
    }

    public void setFaultN(Boolean faultN) {
        FaultN = faultN;
    }

    public Boolean getFaultO() {
        return FaultO;
    }

    public void setFaultO(Boolean faultO) {
        FaultO = faultO;
    }

    public Integer getVoltage() {
        return Voltage;
    }

    public void setVoltage(Integer voltage) {
        Voltage = voltage;
    }

    public Boolean getAwait() {
        return Await;
    }

    public void setAwait(Boolean await) {
        Await = await;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof DeviceChargeCardDtoForMahjong) {
            DeviceChargeCardDtoForMahjong deviceChargeCardDtoForMahjong = (DeviceChargeCardDtoForMahjong) obj;
            if (deviceChargeCardDtoForMahjong.FaultA == this.FaultA &&
                    deviceChargeCardDtoForMahjong.FaultB == this.FaultB &&
                    deviceChargeCardDtoForMahjong.FaultC == this.FaultC &&
                    deviceChargeCardDtoForMahjong.FaultD == this.FaultD &&
                    deviceChargeCardDtoForMahjong.FaultE == this.FaultE &&
                    deviceChargeCardDtoForMahjong.FaultF == this.FaultF &&
                    deviceChargeCardDtoForMahjong.FaultG == this.FaultG &&
                    deviceChargeCardDtoForMahjong.FaultH == this.FaultH &&
                    deviceChargeCardDtoForMahjong.FaultI == this.FaultI &&
                    deviceChargeCardDtoForMahjong.FaultJ == this.FaultJ &&
                    deviceChargeCardDtoForMahjong.FaultK == this.FaultK &&
                    deviceChargeCardDtoForMahjong.FaultL == this.FaultL &&
                    deviceChargeCardDtoForMahjong.FaultM == this.FaultM &&
                    deviceChargeCardDtoForMahjong.FaultN == this.FaultN &&
                    deviceChargeCardDtoForMahjong.FaultO == this.FaultO) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static DeviceChargeCardDtoForMahjong fromJson(String deviceData) {
        return JSON.parseObject(deviceData, DeviceChargeCardDtoForMahjong.class);
    }
}
