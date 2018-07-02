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

package sample;

import cn.javaer.wechat.pay.spring.event.PaymentNotifyEvent;
import cn.javaer.wechat.pay.spring.event.RefundNotifyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author cn-src
 */
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
