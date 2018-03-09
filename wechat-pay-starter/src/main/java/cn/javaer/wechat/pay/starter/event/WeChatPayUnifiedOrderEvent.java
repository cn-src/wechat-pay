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

package cn.javaer.wechat.pay.starter.event;

import cn.javaer.wechat.pay.model.UnifiedOrderResponse;

/**
 * 微信支付-统一下单事件.
 *
 * @author zhangpeng
 */
public class WeChatPayUnifiedOrderEvent {

    private final UnifiedOrderResponse unifiedOrderResponse;
    private boolean successful;


    public WeChatPayUnifiedOrderEvent(final UnifiedOrderResponse unifiedOrderResponse) {
        this.unifiedOrderResponse = unifiedOrderResponse;
        // TODO
//        this.successful = unifiedOrderResponse.isSuccessful();
    }

    /**
     * 获取统一下单的响应.
     *
     * @return UnifiedOrderResponse
     */
    public UnifiedOrderResponse getUnifiedOrderResponse() {
        return this.unifiedOrderResponse;
    }

    /**
     * 判断下单是否成功.
     *
     * @return 成功为 true
     */
    public boolean isPlaceOrderSuccessful() {
        return this.successful;
    }
}
