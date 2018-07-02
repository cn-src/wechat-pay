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

package cn.javaer.wechat.pay.model.base;

import lombok.Data;

import java.util.List;

/**
 * 退款信息.
 *
 * @author cn-src
 */
@Data
public class Refund {

    /**
     * 商户退款单号.
     */
    private String outRefundNo;

    /**
     * 微信退款单号.
     */
    private String refundId;

    /**
     * 退款渠道.
     */
    private String refundChannel;

    /**
     * 申请退款金额.
     */
    private Integer refundFee;

    /**
     * 退款金额.
     */
    private Integer settlementRefundFee;

    /**
     * 总代金券退款金额.
     */
    private Integer couponRefundFee;

    /**
     * 退款代金券使用数量.
     */
    private Integer couponRefundCount;

    /**
     * 退款状态.
     */
    private RefundStatus refundStatus;

    /**
     * 退款资金来源.
     */
    private String refundAccount;

    /**
     * 退款入账账户.
     */
    private String refundRecvAccout;

    /**
     * 退款成功时间.
     */
    private String refundSuccessTime;

    private List<Coupon> coupons;

}
