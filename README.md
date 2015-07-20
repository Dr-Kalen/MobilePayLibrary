# MobilePayLibrary

```
针对移动App中常见的第三方支付平台的集成和封装。
```

针对移动App中常见的第三方支付平台的集成和封装。

### 微信和支付宝支付库的使用说明

当AAR添加成功之后，接下来还需要操作一下：

**支付宝支付**

1、导入支付宝需要的权限

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>

2、配置支付宝必须的Activity

<activity
    android:name="com.alipay.sdk.app.H5PayActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind"
    android:windowSoftInputMode="adjustResize|stateHidden" >
</activity>

3、编译开始使用


--------- AlipayService 使用 -----------

创建对象时参数必须是Activity，由于库中参数要求这样，则意味着必须再界面中调用，或则说必须每个界面重新初始化。
AlipayService service =  AlipayService.newInstance(this);
service.initPayService(new AlipayInit(Fields.PARTNER,Fields.SELLER, Fields.RSA_PRIVATE));
AlipayProductS product = new AlipayProductS();
service.pay(product);




** 微信支付 **

1、导入微信支付需要的权限
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>


2、配置微信支付必须的WXPayEntryActivity，这个WXPayEntryActivity是自定义但是必须继承WeChatPayHandlerActivity文件，同时实现集中的方法getWeChatAppId和handlePayState,而且这个WXPayEntryActivity必须放入packgage+wxapi目录中。


<activity
  android:name=".wxapi.WXPayEntryActivity"
  android:configChanges="orientation|keyboardHidden|navigation"
  android:exported="false"
  android:screenOrientation="behind"
  android:windowSoftInputMode="adjustResize|stateHidden" >
</activity>


<receiver
    android:name=".AppRegister">
    <intent-filter>
        <action android:name="com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP" />
    </intent-filter>
</receiver>

3、检测微信支付是否安装

MobilePayTool.checkWechatInvalidPay(context, appid),通过返回的boolean来判断是否安装。

4、注意事项
* 微信的价格是以分为计量单位，不能有小数因为分已经很精确了，所以必须再价格位置传入整数（真实价格*100）转换为分
* 微信支付的产品信息，一般需要修改价格（Price）、订单号（OutTradeNo）、附件信息（Attach）、回调url（AsynServer）、产品信息（body|Subject）

5、编译开始使用




--------- WeChatPayService 使用 -----------

WeChatPayService service =  WeChatPayService.newInstance(this);
service.initPayService(new WeChatPayInit(Fields.APP_ID, Fields.MCH_ID, Fields.API_KEY));
service.pay(new WeChatProductS());





4、修改build.gradle

添加：
repositories{
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile (name:'MobilePayLibrary', ext:'aar')
}
