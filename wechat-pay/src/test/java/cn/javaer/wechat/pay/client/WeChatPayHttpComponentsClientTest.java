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

import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.model.base.ResponseStatus;
import cn.javaer.wechat.pay.model.base.TradeType;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static cn.javaer.wechat.test.Assertions.assertThat;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * @author zhangpeng
 */
public class WeChatPayHttpComponentsClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private WeChatPayHttpComponentsClient client;

    @Before
    public void setUp() {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        this.client = new WeChatPayHttpComponentsClient("http://localhost:" + this.wireMockRule.port(), httpClient);
    }

    @Test
    public void unifiedOrder() {
        stubFor(post(urlEqualTo(WeChatPayClient.UNIFIED_ORDER_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        //language=xml
                        .withBody("<xml>\n" +
                                "    <return_code>SUCCESS</return_code>\n" +
                                "    <return_msg>OK</return_msg>\n" +
                                "    <appid>wx2421b1c4370ec43b</appid>\n" +
                                "    <mch_id>10000100</mch_id>\n" +
                                "    <nonce_str>IITRi8Iabbblz1Jc</nonce_str>\n" +
                                "    <sign>7921E432F65EB8ED0CE9755F0E86D72F</sign>\n" +
                                "    <result_code>SUCCESS</result_code>\n" +
                                "    <prepay_id>wx201411101639507cbf6ffd8b0779950874</prepay_id>\n" +
                                "    <trade_type>JSAPI</trade_type>\n" +
                                "</xml>")));
        final UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setProductId("ProductId");
        request.setTradeType(TradeType.NATIVE);
        request.setNotifyUrl("http://localhost");
        request.setOutTradeNo("outTradeNo");
        request.setBody("body");
        request.setTotalFee(1);
        request.setSpbillCreateIp("127.0.0.1");
        final UnifiedOrderResponse response = this.client.unifiedOrder(request);
        assertThat(response)
                .hasReturnCode(ResponseStatus.SUCCESS)
                .hasReturnMsg("OK")
                .hasAppId("wx2421b1c4370ec43b")
                .hasMchId("10000100")
                .hasNonceStr("IITRi8Iabbblz1Jc")
                .hasSign("7921E432F65EB8ED0CE9755F0E86D72F")
                .hasResultCode(ResponseStatus.SUCCESS)
                .hasPrepayId("wx201411101639507cbf6ffd8b0779950874")
                .hasTradeType(TradeType.JSAPI)
        ;
    }

    @Test
    public void orderQuery() {
    }

    @Test
    public void closeOrder() {
    }

    @Test
    public void refund() {
    }

    @Test
    public void refundQuery() {
    }

    @Test
    public void downloadBill() {
    }
}