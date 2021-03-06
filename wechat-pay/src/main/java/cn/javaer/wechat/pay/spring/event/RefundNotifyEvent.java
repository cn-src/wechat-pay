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

import cn.javaer.wechat.pay.model.base.ResponseStatus;
import cn.javaer.wechat.pay.model.notify.RefundNotify;

/**
 * 微信支付-退款结果通知事件.
 *
 * @author cn-src
 */

public class RefundNotifyEvent {

    private final RefundNotify refundNotify;
    private final boolean refundSuccessful;


    /**
     * Instantiates a new WeChatPayRefundNotifyEvent.
     *
     * @param refundNotify the refund notify
     */
    public RefundNotifyEvent(final RefundNotify refundNotify) {
        this.refundNotify = refundNotify;
        this.refundSuccessful = ResponseStatus.SUCCESS.equals(refundNotify.getReturnCode());
    }

    public RefundNotify getRefundNotify() {
        return this.refundNotify;
    }

    public boolean isRefundSuccessful() {
        return this.refundSuccessful;
    }
}
