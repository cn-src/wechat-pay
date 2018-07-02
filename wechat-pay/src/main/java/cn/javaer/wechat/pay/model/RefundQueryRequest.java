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

package cn.javaer.wechat.pay.model;

import cn.javaer.wechat.pay.model.base.BasePayRequest;
import cn.javaer.wechat.pay.support.OnlyOne;
import cn.javaer.wechat.pay.support.OnlyOneNotNull;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付-查询退款-请求.
 *
 * @author cn-src
 */
@OnlyOneNotNull
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class RefundQueryRequest extends BasePayRequest {

    @OnlyOne
    @XmlElement(name = "transaction_id")
    private String transactionId;

    @OnlyOne
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 商户退款单号.
     */
    @OnlyOne
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    @OnlyOne
    @XmlElement(name = "refund_id")
    private String refundId;

    @XmlElement(name = "offset")
    private Integer offset;

}
