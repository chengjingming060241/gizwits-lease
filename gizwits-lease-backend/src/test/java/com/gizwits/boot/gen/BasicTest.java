package com.gizwits.boot.gen;

import com.gizwits.lease.device.entity.Device;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by GaGi on 2017/7/19.
 */
public class BasicTest {
    @Test
    public void test(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE,1);
        Date firstDay = c.getTime();
        System.out.print(firstDay);
    }

    @Test
    public void test1(){
//        OrderStatusMap map = new OrderStatusMap();
//        System.out.println(map);
//        IdGenerator.generateTradeNo()
    }
}
