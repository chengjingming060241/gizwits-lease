package com.gizwits.lease.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by GaGi on 2017/8/1.
 */
public class UserChargeStatusMap extends HashMap<Integer, List<Integer>> {
    public UserChargeStatusMap() {
        Integer[] arrForInit = {UserWalletChargeOrderType.PAYING.getCode(), UserWalletChargeOrderType.EXPIRE.getCode()};
        Integer[] arrForPaying = {UserWalletChargeOrderType.PAYED.getCode(), UserWalletChargeOrderType.PAY_FAIL.getCode()};
        Integer[] arrForPayed = {UserWalletChargeOrderType.FINISH.getCode()};
        this.put(UserWalletChargeOrderType.INIT.getCode(), new ArrayList<>(Arrays.asList(arrForInit)));
        this.put(UserWalletChargeOrderType.PAYING.getCode(), new ArrayList<>(Arrays.asList(arrForPaying)));
        this.put(UserWalletChargeOrderType.PAYED.getCode(), new ArrayList<>(Arrays.asList(arrForPayed)));
    }
}
