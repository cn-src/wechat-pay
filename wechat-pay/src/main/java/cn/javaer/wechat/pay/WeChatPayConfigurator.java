/*
 * Copyright (c) 2018 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.javaer.wechat.pay;

import cn.javaer.wechat.pay.client.WeChatPayClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 微信支付-配置.
 *
 * @author zhangpeng
 */
@Getter
@Setter
public class WeChatPayConfigurator {
    public static final String PAYMENT_NOTIFY_PATH = "/public/wechat/pay/payment_notify";
    public static final String REFUND_NOTIFY_PATH = "/public/wechat/pay/refund_notify";
    /**
     * 公众号 ID
     */
    @NonNull
    private String appId;

    /**
     * 商户号 ID
     */
    @NonNull
    private String mchId;

    /**
     * 商户号 key
     */
    @NonNull
    private String mchKey;

    /**
     * 支付结果通知 url
     */
    @NonNull
    private String paymentNotifyUrl;

    /**
     * api 证书路径
     */
    @NonNull
    private String certificatePath;

    /**
     * 微信支付 api base path
     */
    private String basePath = WeChatPayClient.BASE_PATH;

    /**
     * 接收支付结果通知 Controller 的 path.
     */
    private String paymentNotifyPath = PAYMENT_NOTIFY_PATH;

    /**
     * 接收退款结果通知 Controller 的 path.
     */
    private String refundNotifyPath = REFUND_NOTIFY_PATH;
}
