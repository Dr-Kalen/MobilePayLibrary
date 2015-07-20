package com.dr.pay.wechat;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.dr.pay.common.IMobilePayService;
import com.dr.pay.common.OnPayListener;
import com.dr.pay.wechat.sdk.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

/**
 * Created by kalen on 15/4/26.
 */
public class WeChatPayService implements IMobilePayService<WeChatPayInit, WeChatProduct> {

    public enum WeChatState {
        PREPAY_FAIL, PAYING
    }

    public static final String TAG = "WeChatPay";

    private WeChatPayInit mInit;

    private IWXAPI msgApi;

    private Context mContext;

    private OnPayListener onPayListener;



    public static WeChatPayService newInstance(Context context){
        return new WeChatPayService(context);
    }


    private WeChatPayService(Context context){
        this.mContext = context;
        msgApi = WXAPIFactory.createWXAPI(mContext, null);
    }

    @Override
    public void initPayService(WeChatPayInit init) {
        this.mInit = init;
        msgApi.registerApp(mInit.getPartner());
    }

    @Override
    public String getPayOrderDetail(WeChatProduct product) {
        return XmlUtils.genProductArgs(mInit, product);
    }


    @Override
    public void pay(final WeChatProduct product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String prepayId = loadPrepayId(product);

                Message msg = Message.obtain();
                msg.obj = prepayId;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String prepayId = (String) msg.obj;
            if (TextUtils.isEmpty(prepayId)){
                Log.d(TAG, "预支付订单ID失败");
                if (onPayListener != null){
                    onPayListener.onPayState(WeChatState.PREPAY_FAIL.ordinal());
                }
                return;
            }

            PayReq payReq = genPayReq(prepayId);
            msgApi.registerApp(mInit.getPartner());
            msgApi.sendReq(payReq);
            if (onPayListener != null){
                onPayListener.onPayState(WeChatState.PAYING.ordinal());
            }
        }
    };

    @Override
    public void setOnPayListener(OnPayListener listener) {

        this.onPayListener = listener;
    }

    @Override
    public boolean checkPayChannel() {
        return false;
    }

    @Override
    public String getPayVersion() {
        return null;
    }


    /**
     * **************
     */

    private String loadPrepayId(WeChatProduct product) {

        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String entity = getPayOrderDetail(product);

        Log.e("prepayId", entity);

        byte[] buf = Util.httpPost(url, entity);

        String content = new String(buf);
        Log.e("prepayId", content);
        Map<String, String> xml = XmlUtils.decodeXml(content);
        return xml.get("prepay_id");
    }



    private PayReq genPayReq(String prepayId) {

        PayReq payReq = new PayReq();
        payReq.appId = mInit.getPartner();
        payReq.partnerId = mInit.getSeller();
        payReq.prepayId = prepayId;
        payReq.packageValue = "prepay_id="+prepayId;
        payReq.nonceStr = WeChatProduct.getNonce();
        payReq.timeStamp = String.valueOf(WeChatProduct.genTimeStamp());

        List<NameValuePair> signParams = XmlUtils.getRequestParams(payReq);

        payReq.sign = XmlUtils.getAppSign(mInit, signParams);
        Log.e("orion", payReq.sign);

        Log.e("orion", signParams.toString());

        return payReq;
    }


}
