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

package cn.javaer.wechat.spring.boot.autoconfigure.pay;

import cn.javaer.wechat.pay.model.notify.NotifyResponse;
import cn.javaer.wechat.pay.model.notify.PaymentNotify;
import cn.javaer.wechat.pay.model.notify.RefundNotify;
import cn.javaer.wechat.spring.boot.autoconfigure.pay.event.WeChatPayPaymentNotifyEvent;
import cn.javaer.wechat.spring.boot.autoconfigure.pay.event.WeChatPayRefundNotifyEvent;
import org.springframework.context.ApplicationEventPublisher;
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
public class WeChatPayController {
    private final ApplicationEventPublisher publisher;

    public WeChatPayController(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 接收支付结果通知, 将其发布为事件.
     */
    @RequestMapping(path = "${wechat.pay.paymentNotifyPath:" + WeChatPayProperties.PAYMENT_NOTIFY_PATH + "}",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public NotifyResponse notifyResult(@RequestBody final PaymentNotify paymentNotify) {
        this.publisher.publishEvent(new WeChatPayPaymentNotifyEvent(paymentNotify));
        return NotifyResponse.SUCCESS;
    }

    /**
     * 接收退款结果通知, 将其发布为事件.
     */
    @RequestMapping(path = "${wechat.pay.refundNotifyPath:" + WeChatPayProperties.REFUND_NOTIFY_PATH + "}",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public NotifyResponse refundNotifyResult(@RequestBody final RefundNotify payNotifyResult) {
        this.publisher.publishEvent(new WeChatPayRefundNotifyEvent(payNotifyResult));
        return NotifyResponse.SUCCESS;
    }
}
