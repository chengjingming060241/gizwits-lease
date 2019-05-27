package com.gizwits.lease.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.app.utils.ContextUtil;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.QrcodeType;
import com.gizwits.lease.device.dao.DeviceDao;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.entity.Product;
import com.swetake.util.Qrcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;


/**
 * Created by GaGi on 2017/7/26.
 */
public class QrcodeUtil {
    protected static Logger logger = LoggerFactory.getLogger(QrcodeUtil.class);
    @Autowired
    private DeviceDao deviceDao;

    /**
     * 根据设备的sno和访问路径获取二维码的内容
     *
     * @param sno
     * @param url
     * @return 二维码内容
     */
    private static String getQrcodeContent(String sno, String url) {
        //如果host为空时
        if (StringUtils.isEmpty(url)) {
            url = ContextUtil.getHostWithContextPath();
        }
//        if(StringUtils.isEmpty(SystemConfigKey.QRCODE_SUFFIX_PATH)){
//            url += "/app/scan/init?deviceId=" + sno;
//        }else {
//            url += SystemConfigKey.QRCODE_SUFFIX_PATH;
//        }
//        url += "/app/wx/weixin?deviceId=" + sno;
        url += SysConfigUtils.get(CommonSystemConfig.class).getQrcodeAccessUrl() + sno;
        return url;
    }

    /**
     * 根据设备，产品，当前用户对应的东西返回contentUrl
     *
     * @param device
     * @param product
     * @param sysUserExt
     * @return
     */
    public static Map<String, String> createAndSaveQrcode(Device device, Product product, SysUserExt sysUserExt) {
        String title = "sno:" + device.getSno();
        String contentUrl = getQrcodeContent(device.getSno(), SysConfigUtils.get(CommonSystemConfig.class).getHostWithContext());
        //如果是WEB生成二维码
        if (product.getQrcodeType().equals(QrcodeType.WEB.getCode())) {
            return createQrcodeForWeb(title, contentUrl, device.getSno());
        } else if (product.getQrcodeType().equals(QrcodeType.WEIXIN.getCode())) {//如果是微信方式生成二维码
            if (Objects.isNull(sysUserExt)) {
                LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_SYS_USER_EXT_NOT_EXIST);
            }
            return createQrcodeForWeiXin(title, device, sysUserExt, product);
        }
        return null;
    }

    private static Map<String, String> createQrcodeForWeiXin(String title, Device device, SysUserExt sysUserExt, Product product) {
        String accessToken = WxUtil.getAccessToken(sysUserExt);
        if (StringUtils.isEmpty(accessToken)) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_GET_TOKEN_ERROR);
        }
        String res = WxUtil.getQrcodeTicket(accessToken, product.getWxProductId());
        //如果res返回失败
        if (!getQrTicket(res)) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_GET_QR_TICKET_ERROR);
        }
        JSONObject json = JSONObject.parseObject(res);
        String ticket = String.valueOf(json.get("qrticket"));
        String wxDid = String.valueOf(json.get("deviceid"));
        Map<String, String> map = createQrcodeForWeb(title, ticket, device.getSno());
        map.put("wxDid", wxDid);
        return map;
    }

    /**
     * 为WEB类型的产品设备生成二维码
     *
     * @param title
     * @param contentUrl
     * @param sno
     * @return
     */
    private static Map<String, String> createQrcodeForWeb(String title, String contentUrl, String sno) {
        int width = 426;
        int height = 440;
        Qrcode qrcode = new Qrcode();
        qrcode.setQrcodeEncodeMode('M');
        qrcode.setQrcodeErrorCorrect('B');
        qrcode.setQrcodeVersion(7);
        byte[] content;
        try {
            content = contentUrl.getBytes("utf-8");
            BufferedImage bufImg = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB); // 图片的大小
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, width, height);
            gs.setColor(Color.black);

            if (content.length > 0 && content.length < 123) {
                boolean[][] codeOut = qrcode.calQrcode(content);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 9 + 6, i * 9 + 6, 9, 9);

                        }
                    }
                }
            }
            Font font = new Font("HelveticaNeue", Font.BOLD, 21);
            gs.setFont(font);
            FontMetrics fm = gs.getFontMetrics();
            int strWight = fm.stringWidth(title);
            int x = (width - strWight) / 2;

            //在这里写mac
            gs.drawString(title, x, 431);
            gs.dispose();
            bufImg.flush();
            logger.debug("二维码创建在"+SysConfigUtils.get(CommonSystemConfig.class).getQrcodePath() + "/" + sno + ".jpg");
            File f = new File(SysConfigUtils.get(CommonSystemConfig.class).getQrcodePath() + "/" + sno + ".jpg");
            logger.debug("文件路径："+SysConfigUtils.get(CommonSystemConfig.class).getQrcodePath() + "/" + sno + ".jpg");
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
                f.createNewFile();
                ImageIO.write(bufImg, "jpg", f);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            Map<String, String> map = new HashMap<>(1);
            map.put("content", contentUrl);
            return map;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static boolean getQrTicket(String res) {
        try {
            JSONObject object = JSONObject.parseObject(res);
            if (!object.containsKey("base_resp")) {
                logger.warn("==> 生成二维码错误：" + res);
                return false;
            }
            if (object.getJSONObject("base_resp").getIntValue("errcode") == 0) {
//                return QrTicket.fromJson(res);
                return true;
            } else {
                logger.error("==> 生产设备二维码错误" + res);
                return false;
            }
        } catch (Exception e) {
            logger.error("==> 生成qrcode时异常", e);
            return false;
        }
    }
}