# 使用前提
1. 申请微信商户号
2. 获取到 公众号 ID(appid), 商户号(mchId)
3. 登录商户号设置：【账户中心】-【API安全】, 设置 API 秘钥(mchKey)，下载 API 证书(退款需要)
4. 登录商户号设置：【产品中心】-【开发配置】, 配置支付目录
5. 登录商户号设置：【交易中心】-【退款配置】, 配置通知 url(推荐: http://your_host/public/wechat/pay/refund_notify)

# 常规使用

1. 启动接收结果通知的 web 服务, 测试下可运行 run-wiremock.sh 启动模拟服务器，用于接收结果通知以及响应.
2. 使用样例

```java
WeChatPayConfigurator configurator = new WeChatPayConfigurator();
configurator.setAppId("");
configurator.setMchId("");
configurator.setMchKey("");
configurator.setPaymentNotifyUrl("http://your_host/public/wechat/pay/payment_notify");
configurator.setCertificatePath("");

WeChatPayService weChatPayService = new WeChatPayService(configurator);
// weChatPayService 调用相关方法
```

# spring 集成

1. 配置 `WeChatPayController`, `WeChatPayServiceFactoryBean`, 可参照 wechat-spring-boot-starter-pay 中的自动配置.
2. 使用样例

```
@Autowired
private WeChatPayService weChatPayService
```

## spring boot 集成
1. 配置
```
wechat.pay.appId=
wechat.pay.mchId=
wechat.pay.mchKey=
wechat.pay.paymentNotifyUrl=http://your_host/public/wechat/pay/payment_notify
wechat.pay.certificatePath=
```

2. 使用样例
```
@Autowired
private WeChatPayService weChatPayService
```