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

import cn.javaer.wechat.pay.WeChatPayClient;
import cn.javaer.wechat.pay.WeChatPayException;
import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.spring.boot.autoconfigure.pay.event.WeChatPayUnifiedOrderEvent;
import org.springframework.context.ApplicationEventPublisher;

import javax.validation.constraints.NotNull;

/**
 * 微信支付服务.
 *
 * @author zhangpeng
 */
public class WeChatPayDefaultService implements WeChatPayService {

    private final WeChatPayClient weChatPayClient;

    private final ApplicationEventPublisher publisher;

    /**
     * 创建 WeChatPayDefaultService.
     *
     * @param weChatPayClient WeChatPayClient
     * @param publisher ApplicationEventPublisher
     */
    public WeChatPayDefaultService(
            @NotNull final WeChatPayClient weChatPayClient,
            @NotNull final ApplicationEventPublisher publisher) {
        this.weChatPayClient = weChatPayClient;
        this.publisher = publisher;
    }

    @Override
    public WeChatPayUnifiedOrderWithNativeResult unifiedOrderWithNative(
            @NotNull final String body,
            @NotNull final String outTradeNo,
            final int totalFee) throws WeChatPayException {

        final UnifiedOrderRequest request
                = UnifiedOrderRequest.createWithNative(body, outTradeNo, totalFee);

        final UnifiedOrderResponse responseBody = requestAndPublishEvent(request);

        return WeChatPayUnifiedOrderWithNativeResult.createFrom(responseBody);
    }

    @Override
    public void unifiedOrderWithJsApi(
            @NotNull final String openid,
            @NotNull final String body,
            @NotNull final String outTradeNo,
            final int totalFee) throws WeChatPayException {

        final UnifiedOrderRequest request
                = UnifiedOrderRequest.createWithJsApi(openid, body, outTradeNo, totalFee);

        requestAndPublishEvent(request);
    }

    private UnifiedOrderResponse requestAndPublishEvent(final UnifiedOrderRequest request) {

        final UnifiedOrderResponse responseBody
                = this.weChatPayClient.unifiedOrder(request);

        this.publisher.publishEvent(new WeChatPayUnifiedOrderEvent(responseBody));

        responseBody.checkSignAndSuccessful();

        return responseBody;
    }
}
