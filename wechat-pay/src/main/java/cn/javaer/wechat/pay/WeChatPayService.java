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

import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.model.base.BasePayRequest;
import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.JsParams;
import cn.javaer.wechat.pay.model.base.TradeType;

import java.util.function.Function;

/**
 * @author zhangpeng
 */
public class WeChatPayService {
    private final WeChatPayClient client;
    private final WeChatPayConfigurator configurator;

    public WeChatPayService(final WeChatPayClient client, final WeChatPayConfigurator configurator) {
        this.client = client;
        this.configurator = configurator;
    }

    /**
     * 微信支付-统一下单-NATIVE 类型.
     *
     * @param body 商品简述
     * @param outTradeNo 商户订单号
     * @param totalFee 待支付的金额
     *
     * @return 二维码链接
     */
    public String unifiedOrderWithNative(final String outTradeNo, final String body, final int totalFee, final String spbillCreateIp) {
        final UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setProductId(WeChatPayUtils.uuid32());
        request.setTradeType(TradeType.NATIVE);
        request.setNotifyUrl(this.configurator.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        request.setBody(body);
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(spbillCreateIp);
        final UnifiedOrderResponse response = call(this.client::unifiedOrder, request);
        return response.getCodeUrl();
    }

    /**
     * 微信支付-统一下单-JSAPI 类型.
     *
     * @param openid openid
     * @param body 商品简述
     * @param outTradeNo 商户订单号
     * @param totalFee 待支付的金额
     *
     * @return UnifiedOrderRequest
     */
    public JsParams unifiedOrderWithJsApi(final String outTradeNo, final String body, final int totalFee, final String spbillCreateIp, final String openid) {
        final UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setTradeType(TradeType.JSAPI);
        request.setNotifyUrl(this.configurator.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        request.setBody(body);
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(spbillCreateIp);
        request.setOpenid(openid);
        final UnifiedOrderResponse response = call(this.client::unifiedOrder, request);
        return JsParams.create(response.getPrepayId());
    }

    private <T extends BasePayRequest, R extends BasePayResponse> R call(final Function<T, R> fun, final T request) {
        configureAndSign(request);
        final R response = fun.apply(request);
        processAndCheck(response);
        return response;
    }

    private void configureAndSign(final BasePayRequest request) {
        request.setAppid(this.configurator.getAppid());
        request.setMchId(this.configurator.getMchId());
        request.setNonceStr(WeChatPayUtils.uuid32());
        request.setSign(WeChatPayUtils.generateSign(request, this.configurator.getMchKey()));
    }

    private void processAndCheck(final BasePayResponse response) {
        response.beforeSign();
        WeChatPayUtils.checkSign(response, this.configurator.getMchKey());
        WeChatPayUtils.checkSuccessful(response);
    }
}
