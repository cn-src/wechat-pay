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
import cn.javaer.wechat.pay.spring.event.PaymentNotifyEvent;
import cn.javaer.wechat.pay.spring.event.RefundNotifyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String mchKey;

    private ApplicationEventPublisher eventPublisher;

    public WeChatPayController(final String mchKey) {
        this.mchKey = mchKey;
    }

    /**
     * 接收支付结果通知, 将其发布为事件.
     *
     * @param paymentNotify 付款通知
     *
     * @return the notify response
     */
    @RequestMapping(path = "${wechat.pay.paymentNotifyPath:" + PAYMENT_NOTIFY_PATH + "}",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public NotifyResponse paymentNotify(@RequestBody final PaymentNotify paymentNotify) {
        this.log.info("Received payment notify, outTradeNo is '{}'", paymentNotify.getOutTradeNo());
        this.eventPublisher.publishEvent(new PaymentNotifyEvent(paymentNotify, this.mchKey));
        return NotifyResponse.SUCCESS;
    }

    /**
     * 接收退款结果通知, 将其发布为事件.
     *
     * @param refundNotify 退款通知
     *
     * @return the notify response
     */
    @RequestMapping(path = "${wechat.pay.refundNotifyPath:" + REFUND_NOTIFY_PATH + "}",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public NotifyResponse refundNotify(@RequestBody final RefundNotify refundNotify) {
        refundNotify.decrypt(this.mchKey);
        this.log.info("Received refund notify, outTradeNo is '{}'", refundNotify.getOutTradeNo());
        this.eventPublisher.publishEvent(new RefundNotifyEvent(refundNotify));
        return NotifyResponse.SUCCESS;
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
