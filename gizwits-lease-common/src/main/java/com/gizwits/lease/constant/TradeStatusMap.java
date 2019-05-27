package com.gizwits.lease.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by GaGi on 2017/8/1.
 */
public class TradeStatusMap extends HashMap<Integer,List<Integer>>{
    public TradeStatusMap() {
        Integer[] arrForInit = {TradeStatus.SUCCESS.getCode(), TradeStatus.FAIL.getCode()};
        this.put(TradeStatus.INIT.getCode(),new ArrayList<>(Arrays.asList(arrForInit)));
    }
}
