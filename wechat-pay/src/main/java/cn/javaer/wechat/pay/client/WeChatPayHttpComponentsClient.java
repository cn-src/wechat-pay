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

package cn.javaer.wechat.pay.client;

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
import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.util.CodecUtils;
import cn.javaer.wechat.pay.util.ObjectUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 微信支付客户端-RestTemplate实现.
 *
 * @author zhangpeng
 */
public class WeChatPayHttpComponentsClient implements WeChatPayClient {

    private final HttpClient httpClient;
    private final String basePath;

    /**
     * Instantiates a new WeChatPayHttpComponentsClient.
     *
     * @param apiBasePath apiBasePath
     * @param httpClient httpClient
     */
    public WeChatPayHttpComponentsClient(final String apiBasePath, final HttpClient httpClient) {
        this.basePath = apiBasePath;
        this.httpClient = httpClient;
    }

    @Override
    public UnifiedOrderResponse unifiedOrder(final UnifiedOrderRequest request) {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.UNIFIED_ORDER_PATH, request, UnifiedOrderResponse.class);
    }

    @Override
    public OrderQueryResponse orderQuery(final OrderQueryRequest request) {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.ORDER_QUERY_PATH, request, OrderQueryResponse.class);
    }

    @Override
    public CloseOrderResponse closeOrder(final CloseOrderRequest request) {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.CLOSE_ORDER_PATH, request, CloseOrderResponse.class);
    }

    @Override
    public RefundResponse refund(final RefundRequest request) {
        Objects.requireNonNull(request);

        return postForEntity(WeChatPayClient.REFUND_PATH, request, RefundResponse.class);
    }

    @Override
    public RefundQueryResponse refundQuery(final RefundQueryRequest request) {
        Objects.requireNonNull(request);
        return postForEntity(WeChatPayClient.REFUND_QUERY_PATH, request, RefundQueryResponse.class);
    }

    @Override
    public byte[] downloadBill(final DownloadBillRequest request) {
        final HttpPost httpPost = new HttpPost();
        try {
            httpPost.setURI(new URI(ObjectUtils.fullApiUrl(this.basePath, WeChatPayClient.DOWNLOAD_BILL_PATH)));
            httpPost.setEntity(new StringEntity(CodecUtils.marshal(request), "UTf-8"));
            // TODO GZIP
            return this.httpClient.execute(httpPost, new BasicResponseHandler()).getBytes();
        } catch (final URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <Q, S extends BasePayResponse> S postForEntity(final String apiPath, final Q request, final Class<S> responseClass) {
        final HttpPost httpPost = new HttpPost();
        final String responseStr;
        try {
            httpPost.setURI(new URI(ObjectUtils.fullApiUrl(this.basePath, apiPath)));
            httpPost.setEntity(new StringEntity(CodecUtils.marshal(request), "UTf-8"));
            responseStr = this.httpClient.execute(httpPost, new BasicResponseHandler());
        } catch (final URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return CodecUtils.unmarshal(responseStr, responseClass);
    }
}
