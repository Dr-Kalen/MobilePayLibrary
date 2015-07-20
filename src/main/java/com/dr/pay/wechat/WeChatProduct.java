package com.dr.pay.wechat;


import com.dr.pay.common.Product;
import com.dr.pay.wechat.sdk.MD5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by kalen on 15/4/26.
 */
public abstract class WeChatProduct extends Product {


    /**
     * 交易类型，默认为App
     * @return
     */
    public String getTradeType() {
        return "APP";
    }

    /**
     * 支付终端的IP
     * @return
     */
    public String getSpbillIp() {
        return "127.0.0.1";
    }

    /**
     * 生成随机字符串
     * @return
     */
    public static String getNonce() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 支付中通知服务器地址
     * @return
     */
    public abstract String getAsynServer();

    /**
     * 生成时间戳
     * @return
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获得附加信息
     * @return
     */
    public String getAttach(){
        return null;
    }

    /**
     * 获得商户订单号
     * @return
     */
    public abstract String getOutTradeNo();

    /**
     * 产生商户订单号
     */
    public static String createOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
