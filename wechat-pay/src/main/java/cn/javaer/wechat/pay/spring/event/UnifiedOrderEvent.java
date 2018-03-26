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

import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;

/**
 * 微信支付-统一下单事件.
 *
 * @author zhangpeng
 */
public class UnifiedOrderEvent {

    private final UnifiedOrderRequest unifiedOrderRequest;
    private final UnifiedOrderResponse unifiedOrderResponse;

    /**
     * Instantiates a new WeChatPayUnifiedOrderEvent.
     *
     * @param unifiedOrderRequest the unified order request
     * @param unifiedOrderResponse the unified order response
     */
    public UnifiedOrderEvent(
            final UnifiedOrderRequest unifiedOrderRequest, final UnifiedOrderResponse unifiedOrderResponse) {

        this.unifiedOrderRequest = unifiedOrderRequest;
        this.unifiedOrderResponse = unifiedOrderResponse;
    }

    /**
     * 获取统一下单的响应.
     *
     * @return UnifiedOrderResponse
     */
    public UnifiedOrderResponse getUnifiedOrderResponse() {
        return this.unifiedOrderResponse;
    }

    public UnifiedOrderRequest getUnifiedOrderRequest() {
        return this.unifiedOrderRequest;
    }
}
