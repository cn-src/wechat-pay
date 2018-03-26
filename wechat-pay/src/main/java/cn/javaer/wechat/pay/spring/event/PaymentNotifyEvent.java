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

package cn.javaer.wechat.pay.spring.event;

import cn.javaer.wechat.pay.model.notify.PaymentNotify;
import cn.javaer.wechat.pay.util.ObjectUtils;

/**
 * 微信支付-支付结果通知事件.
 *
 * @author zhangpeng
 */

public class PaymentNotifyEvent {

    private final PaymentNotify paymentNotify;
    private final boolean paymentSuccessful;

    /**
     * Instantiates a new WeChatPayPaymentNotifyEvent.
     *
     * @param paymentNotify the payment notify
     * @param mchKey the mch key
     */
    public PaymentNotifyEvent(final PaymentNotify paymentNotify, final String mchKey) {
        paymentNotify.processResponse();
        this.paymentNotify = paymentNotify;
        this.paymentSuccessful = ObjectUtils.isSuccessful(paymentNotify, mchKey);
    }

    /**
     * 获取支付结果通知事件的响应.
     *
     * @return PaymentNotify
     */
    public PaymentNotify getPaymentNotify() {
        return this.paymentNotify;
    }

    public boolean isPaymentSuccessful() {
        return this.paymentSuccessful;
    }
}
