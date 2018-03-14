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

import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付-下载对账单-请求.
 *
 * @author zhangpeng
 */
@Getter
@Setter
public class BillItem {
    /**
     * 交易时间
     */
    private String tradeTime;
    /**
     * 公众账号ID
     */
    private String appId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 子商户号
     */
    private String subMchId;
    /**
     * 设备号
     */
    private String deviceInfo;
    /**
     * 微信订单号
     */
    private String transactionId;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 用户标识
     */
    private String openId;
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 交易状态
     */
    private String tradeState;
    /**
     * 付款银行
     */
    private String bankType;
    /**
     * 货币种类
     */
    private String feeType;
    /**
     * 总金额
     */
    private String totalFee;
    /**
     * 企业红包金额
     */
    private String couponFee;
    /**
     * 微信退款单号
     */
    private String refundId;
    /**
     * 商户退款单号
     */
    private String outRefundNo;
    /**
     * 退款金额
     */
    private String settlementRefundFee;
    /**
     * 企业红包退款金额
     */
    private String couponRefundFee;
    /**
     * 退款类型
     */
    private String refundChannel;
    /**
     * 退款状态
     */
    private String refundState;
    /**
     * 商品名称
     */
    private String body;
    /**
     * 商户数据包
     */
    private String attach;
    /**
     * 手续费
     */
    private String poundage;
    /**
     * 费率
     */
    private String poundageRate;
}
