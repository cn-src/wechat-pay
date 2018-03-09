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

import cn.javaer.wechat.pay.client.WeChatPayClient;
import cn.javaer.wechat.pay.model.CloseOrderRequest;
import cn.javaer.wechat.pay.model.CloseOrderResponse;
import cn.javaer.wechat.pay.model.DownloadBillRequest;
import cn.javaer.wechat.pay.model.OrderQueryRequest;
import cn.javaer.wechat.pay.model.OrderQueryResponse;
import cn.javaer.wechat.pay.model.RefundQueryRequest;
import cn.javaer.wechat.pay.model.RefundQueryResponse;
import cn.javaer.wechat.pay.model.RefundRequest;
import cn.javaer.wechat.pay.model.RefundResponse;
import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.model.base.BasePayRequest;
import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.BillType;
import cn.javaer.wechat.pay.model.base.JsParams;
import cn.javaer.wechat.pay.model.base.TradeType;
import cn.javaer.wechat.pay.util.ObjectUtils;
import cn.javaer.wechat.pay.util.SignUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;

import static cn.javaer.wechat.pay.util.ObjectUtils.checkNotNull;

/**
 * @author zhangpeng
 */
public class WeChatPayService {
    private final WeChatPayClient client;
    private final WeChatPayConfigurator configurator;
    private final Validator validator;

    public WeChatPayService(final WeChatPayClient client, final WeChatPayConfigurator configurator,
                            final Validator validator) {
        checkNotNull(client, "client");
        checkNotNull(configurator, "configurator");
        checkNotNull(validator, "validator");

        this.client = client;
        this.configurator = configurator;
        this.validator = validator;
    }

    /**
     * 统一下单, 扫码支付(NATIVE).
     *
     * @param body 商品简述
     * @param outTradeNo 商户订单号
     * @param totalFee 待支付的金额
     *
     * @return 二维码链接
     */
    public String unifiedOrderWithNative(final String outTradeNo, final String body, final int totalFee, final String spbillCreateIp) {
        final UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setProductId(ObjectUtils.uuid32());
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
     * 统一下单, 公众号支付(JSAPI).
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
        return JsParams.create(response.getPrepayId(), request.getSignType(), request.getAppid());
    }

    /**
     * 查询订单, 根据商户订单号查询(outTradeNo).
     *
     * @param outTradeNo 商户订单号
     *
     * @return OrderQueryRequest
     */
    public OrderQueryResponse orderQueryWithOutTradeNo(final String outTradeNo) {
        final OrderQueryRequest request = new OrderQueryRequest();
        request.setOutTradeNo(outTradeNo);
        return call(this.client::orderQuery, request);
    }

    /**
     * create CloseOrderRequest.
     *
     * @param outTradeNo 商户订单号
     *
     * @return CloseOrderRequest
     */
    public CloseOrderResponse closeOrder(final String outTradeNo) {
        final CloseOrderRequest request = new CloseOrderRequest();
        request.setOutTradeNo(outTradeNo);
        return call(this.client::closeOrder, request);
    }

    /**
     * create RefundRequest.
     *
     * @param outTradeNo 商户订单号
     * @param outRefundNo 商户退款单号, 同一退款单号多次请求只退一笔
     * @param totalFee 订单总金额
     * @param refundFee 退款金额
     * @param refundDesc 退款原因, 发给用户的退款消息中体现退款原因
     *
     * @return RefundRequest
     */
    public RefundResponse refund(final String outTradeNo,
                                 final String outRefundNo,
                                 final int totalFee,
                                 final int refundFee,
                                 final String refundDesc) {

        final RefundRequest request = new RefundRequest();
        request.setOutTradeNo(outTradeNo);
        request.setOutRefundNo(outRefundNo);
        request.setTotalFee(totalFee);
        request.setRefundFee(refundFee);
        request.setRefundDesc(refundDesc);
        return call(this.client::refund, request);
    }

    /**
     * 查询退款.
     *
     * @param outTradeNo 商户订单号
     *
     * @return RefundQueryResponse
     */
    public RefundQueryResponse refundQueryWithOutTradeNo(final String outTradeNo) {
        final RefundQueryRequest request = new RefundQueryRequest();
        request.setOutTradeNo(outTradeNo);
        return call(this.client::refundQuery, request);
    }

    /**
     * 查询退款.
     *
     * @param outTradeNo 商户订单号
     * @param offset 分页查询的偏移量,
     *         举例：当商户想查询第25笔时，可传入订单号及offset=24，微信支付平台会返回第25笔到第35笔的退款单信息.
     *
     * @return RefundQueryResponse
     */
    public RefundQueryResponse refundQueryWithOutTradeNo(final String outTradeNo, final int offset) {
        final RefundQueryRequest request = new RefundQueryRequest();
        request.setOutTradeNo(outTradeNo);
        request.setOffset(offset);
        return call(this.client::refundQuery, request);
    }

    /**
     * 查询退款.
     *
     * @param outRefundNo 商户退款单号
     *
     * @return RefundQueryResponse
     */
    public RefundQueryResponse refundQueryWithOutRefundNo(final String outRefundNo) {
        final RefundQueryRequest request = new RefundQueryRequest();
        request.setOutRefundNo(outRefundNo);
        return call(this.client::refundQuery, request);
    }

    /**
     * 下载对账单.
     *
     * @return 字节数据
     */
    public byte[] downloadBill(final LocalDate queryDate, final BillType billType) {
        final DownloadBillRequest request = new DownloadBillRequest();
        request.setBillDate(queryDate);
        request.setBillType(billType);
        return this.client.downloadBill(request);
    }

    private <T extends BasePayRequest, R extends BasePayResponse> R call(
            final Function<T, R> fun, final T request) {
        configureAndSign(request);
        final Set<ConstraintViolation<T>> violationSet = this.validator.validate(request);
        if (violationSet.size() > 0) {
            throw new ValidationException(violationSet.iterator().next().getMessage());
        }
        final R response = fun.apply(request);
        processAndCheck(response);
        return response;
    }

    private void configureAndSign(final BasePayRequest request) {
        request.setAppid(this.configurator.getAppid());
        request.setMchId(this.configurator.getMchId());
        request.setNonceStr(ObjectUtils.uuid32());
        request.setSign(SignUtils.generateSign(request, this.configurator.getMchKey()));
    }

    private void processAndCheck(final BasePayResponse response) {
        response.beforeSign();
        ObjectUtils.checkSign(response, this.configurator.getMchKey());
        ObjectUtils.checkSuccessful(response);
    }
}
