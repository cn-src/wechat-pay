[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.javaer/wechat-pay/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.javaer/wechat-pay)
[![Build Status](https://travis-ci.org/cn-src/wechat-pay.svg?branch=master)](https://travis-ci.org/cn-src/wechat-pay)
[![Libraries.io for GitHub](https://img.shields.io/librariesio/github/cn-src/wechat-pay.svg)](https://libraries.io/github/cn-src/wechat-pay)

[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=coverage)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=bugs)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=code_smells)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=cn.javaer.wechat%3Awechat-pay-parent&metric=ncloc)](https://sonarcloud.io/dashboard?id=cn.javaer.wechat%3Awechat-pay-parent)

# wechat-pay
微信支付

# 使用前提
1. 申请微信商户号
2. 获取到 公众号 ID(appid), 商户号(mchId)
3. 登录商户号设置：【账户中心】-【API安全】, 设置 API 秘钥(mchKey)，下载 API 证书(退款需要)
4. 登录商户号设置：【产品中心】-【开发配置】, 配置支付目录
5. 登录商户号设置：【交易中心】-【退款配置】, 配置通知 url(推荐: http://your_host/public/wechat/pay/refund_notify)

# maven 坐标
```xml
<dependency>
    <groupId>cn.javaer.wechat</groupId>
    <artifactId>wechat-pay</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>cn.javaer.wechat</groupId>
    <artifactId>wechat-spring-boot-starter-pay</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

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

# spring & spring boot 集成

* 【spring集成】配置 `WeChatPayController`, `WeChatPayServiceFactoryBean`, 可参照 wechat-spring-boot-starter-pay 中的自动配置.
* 【spring boot 集成】配置
```
wechat.pay.appId=
wechat.pay.mchId=
wechat.pay.mchKey=
wechat.pay.paymentNotifyUrl=http://your_host/public/wechat/pay/payment_notify
wechat.pay.certificatePath=
```
* 使用样例

```java
// 调用接口
@Autowired
private WeChatPayService weChatPayService
```

```java
// 接收结果通知
@Component
public class SamplePayEvent {

    /**
     * 付款结果通知.
     *
     * @param event PaymentNotifyEvent
     */
    @EventListener
    public void paymentNotifyEvent(final PaymentNotifyEvent event) {
        System.out.println(event);
    }

    /**
     * 退款结果通知.
     *
     * @param event RefundNotifyEvent
     */
    @EventListener
    public void refundNotifyEvent(final RefundNotifyEvent event) {
        System.out.println(event);
    }
}

```
