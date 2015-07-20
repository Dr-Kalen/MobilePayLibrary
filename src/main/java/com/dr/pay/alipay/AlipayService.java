package com.dr.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dr.pay.common.IMobilePayService;
import com.dr.pay.common.OnPayListener;
import com.dr.pay.alipay.sdk.Result;
import com.dr.pay.alipay.sdk.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class AlipayService implements IMobilePayService<AlipayInit, AlipayProduct> {


    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private OnPayListener mOnPayListener;

    private AlipayInit mInit;

    private Activity mContext;

    private AlipayService(Activity context) {
        this.mContext = context;
    }

    public static AlipayService newInstance(Activity context){
        return new AlipayService(context);
    }


    @Override
    public void initPayService(AlipayInit init) {
        this.mInit = init;
    }

    @Override
    public String getPayOrderDetail(AlipayProduct product) {


        if (mInit == null) {
            throw new IllegalArgumentException("未初始化");
        }

        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + mInit.getPartner() + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + mInit.getSeller() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + product.getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + product.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + product.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + product.getPrice() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + product.getAsynServer()
                + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"" + product.getPayOverTime() + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"" + product.getPayRedirectURL() + "\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    @Override
    public void pay(AlipayProduct product) {
        String orderInfo = getPayOrderDetail(product);
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void setOnPayListener(OnPayListener listener) {
        this.mOnPayListener = listener;
    }

    /**
     * 此方法为同步方法，需要再线程中运行，检查是否与支付宝已经建立支付渠道
     * @return
     */
    @Override
    public boolean checkPayChannel() {
        PayTask payTask = new PayTask(mContext);
        return payTask.checkAccountIfExist();
    }

    /**
     * 此方法为同步方法，需要再线程中运行，获得支付宝接口版本
     * @return
     */
    @Override
    public String getPayVersion() {
        PayTask payTask = new PayTask(mContext);
        return payTask.getVersion();
    }


    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, mInit.getRsaPrivate());
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;

                    if (mOnPayListener == null) {
                        defaultTips(resultStatus);
                        return;
                    }

                    if (TextUtils.equals(resultStatus, "9000")) {
                        mOnPayListener.onPayState(AlipayState.SUCCESS.ordinal());
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        mOnPayListener.onPayState(AlipayState.PAYING.ordinal());
                    } else {
                        mOnPayListener.onPayState(AlipayState.FAILED.ordinal());
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mContext, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public enum AlipayState {
        PAYING, SUCCESS, FAILED
    }

    private void defaultTips(String resultStatus) {
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
        } else {
            // 判断resultStatus 为非“9000”则代表可能支付失败
            // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(mContext, "支付结果确认中",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(mContext, "支付失败",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }
}
