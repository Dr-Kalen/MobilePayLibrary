package com.dr.pay.common;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by kalen on 15/7/16.
 */
public class MobilePayTool {

    /**
     * 检测微信支付是否安装
     * @param context
     * @param app_id
     * @return
     */
    public static boolean checkWechatInvalidPay(final Context context, String app_id) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, app_id);
        return iwxapi.isWXAppInstalled();

    }

}
