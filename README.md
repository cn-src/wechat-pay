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

# 开发规范
1. 为避免理解上的混乱，命名尽可能保持与微信支付官方文档的一致（尽管官方有些不合理的地方）.
2. 对于 api 接口的字段避免使用基本数据类型，一是为了可为 null 的场景，二是为了避免不必要的隐式转换（字段参与签名时为包装类型）.
3. 对于 api 接口的请求字段使用更严格的类型（如：使用枚举替换字符串类型），对于响应字段则使用比较宽容的类型.
4. 对于 api 接口的响应能接收未知的字段.
5. 减少对第三方工具包的依赖.
