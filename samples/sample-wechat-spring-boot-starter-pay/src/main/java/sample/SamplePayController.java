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

import cn.javaer.wechat.pay.WeChatPayService;
import cn.javaer.wechat.pay.model.CloseOrderResponse;
import cn.javaer.wechat.pay.model.DownloadBillResponse;
import cn.javaer.wechat.pay.model.OrderQueryResponse;
import cn.javaer.wechat.pay.model.RefundQueryResponse;
import cn.javaer.wechat.pay.model.RefundResponse;
import cn.javaer.wechat.pay.model.base.BillType;
import cn.javaer.wechat.pay.model.base.JsParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author cn-src
 */
@RestController
public class SamplePayController {
    private final WeChatPayService weChatPayService;

    @Autowired
    public SamplePayController(final WeChatPayService weChatPayService) {
        this.weChatPayService = weChatPayService;
    }

    @RequestMapping("/unifiedOrderWithNative")
    public String unifiedOrderWithNative(final String outTradeNo, final HttpServletRequest request) {
        return this.weChatPayService.unifiedOrderWithNative(outTradeNo, "TEST", 1, request.getRemoteAddr());
    }

    @RequestMapping("/unifiedOrderWithJsApi")
    public JsParams unifiedOrderWithJsApi(@RequestParam("outTradeNo") final String outTradeNo, final HttpServletRequest request) {
        return this.weChatPayService.unifiedOrderWithJsApi(outTradeNo, "TEST", 1, request.getRemoteAddr(), System.getenv("openid"));
    }

    @RequestMapping(value = "/orderQuery", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderQueryResponse orderQuery(final String outTradeNo) {
        return this.weChatPayService.orderQuery(outTradeNo);
    }

    @RequestMapping(value = "/closeOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public CloseOrderResponse closeOrder(final String outTradeNo) {
        return this.weChatPayService.closeOrder(outTradeNo);
    }

    @RequestMapping(value = "/refund", produces = MediaType.APPLICATION_JSON_VALUE)
    public RefundResponse refund(
            @RequestParam("outTradeNo") final String outTradeNo,
            @RequestParam("outRefundNo") final String outRefundNo) {
        return this.weChatPayService.refund(outTradeNo, outRefundNo, 1, 1, "商品退款");
    }

    @RequestMapping(value = "/refundQuery", produces = MediaType.APPLICATION_JSON_VALUE)
    public RefundQueryResponse refundQuery(
            @RequestParam("outTradeNo") final String outTradeNo) {
        return this.weChatPayService.refundQuery(outTradeNo);
    }

    @RequestMapping(value = "/downloadBill", produces = MediaType.APPLICATION_JSON_VALUE)
    public DownloadBillResponse downloadBill(
            @RequestParam("queryDate") final String queryDate) {
        return this.weChatPayService.downloadBill(LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(queryDate)), BillType.ALL);
    }

}
