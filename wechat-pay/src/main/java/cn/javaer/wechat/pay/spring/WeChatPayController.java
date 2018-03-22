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

package cn.javaer.wechat.pay.spring;

import cn.javaer.wechat.pay.model.notify.NotifyResponse;
import cn.javaer.wechat.pay.model.notify.PaymentNotify;
import cn.javaer.wechat.pay.model.notify.RefundNotify;
import cn.javaer.wechat.pay.spring.event.WeChatPayPaymentNotifyEvent;
import cn.javaer.wechat.pay.spring.event.WeChatPayRefundNotifyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付 Controller.
 *
 * @author zhangpeng
 */
@RestController
public class WeChatPayController implements ApplicationEventPublisherAware {

    private static final String PAYMENT_NOTIFY_PATH = "/public/wechat/pay/payment_notify";
    private static final String REFUND_NOTIFY_PATH = "/public/wechat/pay/refund_notify";
    /**
     * 接收支付结果通知 Controller 的 path.
     */
    private final String paymentNotifyPath = PAYMENT_NOTIFY_PATH;

    /**
     * 接收退款结果通知 Controller 的 path.
     */
    private final String refundNotifyPath = REFUND_NOTIFY_PATH;
    private final String mchKey;

    private ApplicationEventPublisher eventPublisher;

    public WeChatPayController(final String mchKey) {
        this.mchKey = mchKey;
    }

    /**
     * 接收支付结果通知, 将其发布为事件.
     */
    @RequestMapping(path = "${wechat.pay.paymentNotifyPath:" + PAYMENT_NOTIFY_PATH + "}",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public NotifyResponse notifyResult(@RequestBody final PaymentNotify paymentNotify) {
        this.eventPublisher.publishEvent(new WeChatPayPaymentNotifyEvent(paymentNotify, this.mchKey));
        return NotifyResponse.SUCCESS;
    }

    /**
     * 接收退款结果通知, 将其发布为事件.
     */
    @RequestMapping(path = "${wechat.pay.refundNotifyPath:" + REFUND_NOTIFY_PATH + "}",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public NotifyResponse refundNotifyResult(@RequestBody final RefundNotify payNotifyResult) {
        this.eventPublisher.publishEvent(new WeChatPayRefundNotifyEvent(payNotifyResult, this.mchKey));
        return NotifyResponse.SUCCESS;
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
