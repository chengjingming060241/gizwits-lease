/*

package com.gizwits.boot.gen;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.model.HttpRespObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.HttpUtil;
import com.gizwits.lease.china.dao.ChinaAreaDao;
import com.gizwits.lease.constant.UserWalletUseEnum;
import com.gizwits.lease.constant.WalletEnum;
import com.gizwits.lease.device.dao.DeviceLaunchAreaDao;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.stat.dao.StatDeviceLocationDao;
import com.gizwits.lease.stat.dao.StatDeviceOrderWidgetDao;
import com.gizwits.lease.stat.dao.StatDeviceTrendDao;
import com.gizwits.lease.stat.dao.StatUserTrendDao;
import com.gizwits.lease.stat.dto.StatDeviceTrendDto;
import com.gizwits.lease.stat.entity.StatDeviceOrderWidget;
import com.gizwits.lease.stat.entity.StatDeviceTrend;
import com.gizwits.lease.stat.entity.StatUserTrend;
import com.gizwits.lease.stat.service.*;
import com.gizwits.lease.stat.vo.StatLocationVo;
import com.gizwits.lease.trade.dao.TradeBaseDao;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.user.dao.UserDao;
import com.gizwits.lease.util.QrcodeUtil;
import com.gizwits.lease.wallet.entity.UserWallet;
import com.gizwits.lease.wallet.service.UserWalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//*

*/
/**
 * Created by GaGi on 2017/7/13.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.gizwits.lease.Application.class)
@WebAppConfiguration
public class StatTest {
    @Autowired
    private DeviceLaunchAreaDao deviceLaunchAreaDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private StatDeviceLocationService statDeviceLocationService;
    @Autowired
    private StatUserLocationService statUserLocationService;
    @Autowired
    private StatDeviceTrendService statDeviceTrendService;
    @Autowired
    private StatUserTrendService statUserTrendService;
    @Autowired
    private ChinaAreaDao chinaAreaDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StatUserTrendDao statUserTrendDao;
    @Autowired
    private StatOrderService statOrderService;
    @Autowired
    private StatDeviceLocationDao statDeviceLocationDao;
    @Autowired
    private StatDeviceTrendDao statDeviceTrendDao;
    @Autowired
    private StatDeviceOrderWidgetService statDeviceOrderWidgetService;
    @Autowired
    private StatUserWidgetService statUserWidgetService;
    @Autowired
    private OrderBaseDao orderBaseDao;
    @Test
    public void test(){
        List<Map<String, Number>> temp = deviceLaunchAreaDao.findProvinceAndCount(12, 1);
        System.out.println(temp);
    }

    @Test
    public void test1(){
        List<Map<String, Number>> t = userDao.findProvinceAndCount(12);
        System.out.println(t);
    }

    @Test
    public void test2(){
        SysUser currentUser = new SysUser();
        currentUser.setId(1);
        List<StatLocationVo> list = statDeviceLocationService.getDistribution(1, currentUser);
        System.out.println(list);
    }

    @Test
    public void test3(){
        statDeviceTrendService.getDataForStatDeviceTrend();
    }

    @Test
    public void testStatDeviceTrendService(){
        statDeviceTrendService.getDataForStatDeviceTrend();
    }

    @Test
    public void test4(){
        List<Map<String, Number>> tempp = userDao.findProvinceAndCount(1);
        System.out.println(tempp);
    }

    @Test
    public void testStatDeviceTrend(){
        statDeviceTrendService.getDataForStatDeviceTrend();
    }

    @Test
    public void testStatUserTrend(){
        statUserTrendService.getDataForStatUserTrend();
    }
    @Test
    public void testStatDeviceLocationService(){
        statDeviceLocationService.getDataForLocation();
    }
    @Test
    public void testStatUserDao(){
        Date yesterdayBefor7 = DateKit.addDate(new Date(),-8);
        Date yesterday =DateKit.addDate(new Date(),-2);
        Integer count = userDao.getTimesByOrder(1, 1, 2, yesterdayBefor7, yesterday, false);
        System.out.println(count);
    }

    @Test
    public void testStatUserTrendService(){
        statUserTrendService.getDataForStatUserTrend();
    }
    @Test
    public void testStatDeviceLocation(){
        statDeviceLocationService.getDataForLocation();
    }
    @Test
    public void testStatUserLocation(){
        statUserLocationService.getDataForLocation();
    }

    public void testRun(){
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService
    }

    @Test
    public void testAccessToken() {
        String token = redisService.getGizwitsAccessTokenPrefix("66051514d8e944a0a412a18e1d2d9878");
        String url = "http://enterpriseapi.gizwits.com/v1/products/66051514d8e944a0a412a18e1d2d9878/devices/report/liveness";
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "token " + "1233003bda9a4efcaa07d85f38406aa2");

        HttpRespObject res = HttpUtil.sendGet(url, header);
        if (res.getStatusCode()==200){
            String content = String.valueOf(res.getContent());
            JSONObject json = JSONObject.parseObject(content);
            Integer obj = Integer.valueOf(String.valueOf(json.get("latest_1")));
        }
        System.out.println(res);

    }

    @Test
    public void testStatUserTrend1(){
        Date yDate = DateKit.addDate(new Date(),-1);
        StatUserTrend te = statUserTrendDao.getSex(1, yDate);
        System.out.println(te);
    }

    @Test
    public void testA(){
        Date yesterday = DateKit.addDate(new Date(),-3);
//        List<StatDeviceLocation>list=statDeviceLocationDao.getDistribution(null,26,yesterday);
//        System.out.println(list);

        List<StatDeviceTrend> list = statDeviceTrendDao.getUsePecentTrend(11, new StatDeviceTrendDto());
                System.out.println(list);
    }

    @Test
    public void testWidget(){
        statDeviceOrderWidgetService.getDataForWidget();
//        statUserWidgetService.getDataForWidget();

    }
    @Autowired
    private StatDeviceOrderWidgetDao statDeviceOrderWidgetDao;
    @Test
    public void testV(){
        StatDeviceOrderWidget statDeviceOrderWidget = new StatDeviceOrderWidget();
        statDeviceOrderWidget.setTotalCount(100);
        statDeviceOrderWidget.setOrderedPercent(100.0);
        statDeviceOrderWidget.setNewCount(100);
        statDeviceOrderWidget.setAlarmCount(100);
        statDeviceOrderWidget.setOrderCountToday(100);
        statDeviceOrderWidget.setWarnCount(100);
        statDeviceOrderWidget.setWarnRecord(100);
        Integer res = statDeviceOrderWidgetDao.updateByUtimeAndSysUserIdAndProductId(new Date(), 26, 9, statDeviceOrderWidget);
        System.out.println(res);
    }

    @Test
    public void testStatOrder(){
        statOrderService.getDataForStatOrder();
    }

    @Test
    public void testOrderDao(){
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.DATE, 1);
//        Date firstDay = c.getTime();
//        Date yesterday = DateKit.addDate(new Date(), -1);
//        Date beforeYesterday = DateKit.addDate(new Date(),-2);
//        Date beforeYesterday1 = DateKit.addDate(new Date(),-3);
//        Integer orderCountMonth = orderBaseDao.getOrderCount(1, 1, firstDay, yesterday);
//        Integer orderCount1=orderBaseDao.getOrderCount(1, 1, beforeYesterday, beforeYesterday);
//        Integer orderCount2=orderBaseDao.getOrderCount(1, 1, beforeYesterday1, beforeYesterday1);
//        Double orderNewPercent = orderCount2==0?0.0:(double)(orderCount1-orderCount2)/orderCount2;
//        System.out.println(123);
    }

    @Test
    public void test999(){
//        Date d = DateKit.addDate(new Date(), -1);
//        List<Integer> status = new ArrayList<>();
//        status.add(2);
//        status.add(3);
//        Map<String, Number> abc = orderBaseDao.findForStatOrder("28563566129768454380", d, status);
//        Integer count = orderBaseDao.getOrderCount(1, 19, d, d, null);
        SysUser sysUser = new SysUser();
        sysUser.setId(1);
        List<StatLocationVo> res = statDeviceLocationService.getDistribution(27, sysUser);
//        List<StatLocationVo> res1 = statDeviceLocationService.getRank(27, sysUser);
//        StatDeviceTrendDto statDeviceTrendDto = new StatDeviceTrendDto();
//        List<StatTrendVo> res = statDeviceTrendService.getNewTrend(statDeviceTrendDto, sysUser);
//        List<StatTrendVo> res1 = statDeviceTrendService.getActiveTrend(statDeviceTrendDto, sysUser);
//        List<StatTrendVo> res2 = statDeviceTrendService.getUserPecentTrend(statDeviceTrendDto, sysUser);
//        statUserTrendService.getDataForStatUserTrend();
        return;
    }
    @Autowired
    private TradeBaseDao tradeBaseDao;
    @Test
    public void test1001(){
        TradeBase tradeBase = new TradeBase();
        tradeBase.setTradeNo(123+"");
        tradeBase.setCtime(new Date());
        tradeBase.setOrderNo(12312+"");
        tradeBase.setOrderType(1);
        tradeBase.setTotalFee(100d);
        tradeBase.setStatus(1);
        tradeBaseDao.insert(tradeBase);
        return;
    }


    @Test
    public void test1000(){

        StatDeviceOrderWidget res = statDeviceOrderWidgetDao.alarmWidget(26, null, new Date());
        System.out.println(res);
	}
    @Autowired
    private UserWalletService userWalletService;
    @Test
    public void test1002(){
        UserWallet uw = userWalletService.updateMoney(1000d, UserWalletUseEnum.RECHARGE.getCode(),null, WalletEnum.BALENCE.getCode(), "JZN8fkwMb");
        return;
    }

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SysUserExtService sysUserExtService;
    @Autowired
    private ProductService productService;

    @Test
    public void testQrcode(){
        Device device = deviceService.selectById("32503740656849744612");
        Product product = productService.selectById(7);
        SysUserExt sysUserExt =new SysUserExt();
        sysUserExt.setWxAppid("wxa74db3552f84d3c6");
        sysUserExt.setWxParenterId("1230594302");
        sysUserExt.setWxAppSecret("9d0af31fa31735e83e5d4874a943147c");
        sysUserExt.setWxPartnerSecret("201568ce1563dd45abc532a6b017f703");
        sysUserExt.setWxId("gh_29ffd8a7f58c");
        sysUserExt.setWxToken("cGyvBJQpoo");

        QrcodeUtil.createAndSaveQrcode(device,product,sysUserExt);
        int res = deviceService.deviceAuthResult(device, sysUserExt);
    }
}
*/
