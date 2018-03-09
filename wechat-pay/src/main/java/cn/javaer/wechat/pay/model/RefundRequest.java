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
import cn.javaer.wechat.pay.support.Groups;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付-申请退款-请求.
 *
 * @author zhangpeng
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class RefundRequest extends BasePayRequest {

    @NotNull(groups = Groups.TransactionId.class)
    @Length(min = 1, max = 32)
    @XmlElement(name = "transaction_id")
    private String transactionId;

    @NotNull(groups = Groups.OutTradeNo.class)
    @Length(min = 1, max = 32)
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 商户退款单号.
     */
    @NotNull
    @Length(min = 1, max = 64)
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    /**
     * 订单金额.
     */
    @NotNull
    @Min(1)
    @Max(10_0000_00)
    @XmlElement(name = "total_fee")
    private Integer totalFee;

    /**
     * 退款金额.
     */
    @NotNull
    @Min(1)
    @Max(10_0000_00)
    @XmlElement(name = "refund_fee")
    private int refundFee;

    @XmlElement(name = "refund_fee_type")
    private String refundFeeType;
    /**
     * 退款原因.
     */
    @XmlElement(name = "refund_desc")
    private String refundDesc;
    /**
     * 退款资金来源.
     */
    @XmlElement(name = "refund_account")
    private String refundAccount;

}
