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

import cn.javaer.wechat.pay.WeChatPayUtils;
import cn.javaer.wechat.pay.support.SignIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 微信支付-申请退款-响应.
 *
 * @author zhangpeng
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"refundCoupons"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class RefundResponse extends BasePayResponse {

    @XmlElement(name = "transaction_id")
    private String transactionId;

    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    @XmlElement(name = "refund_id")
    private String refundId;

    @XmlElement(name = "refund_fee")
    private Integer refundFee;

    @XmlElement(name = "settlement_refund_fee")
    private Integer settlementRefundFee;

    @XmlElement(name = "total_fee")
    private Integer totalFee;

    @XmlElement(name = "settlement_total_fee")
    private Integer settlementTotalFee;

    @XmlElement(name = "fee_type")
    private String feeType;

    @XmlElement(name = "cash_fee")
    private Integer cashFee;

    @XmlElement(name = "cash_fee_type")
    private String cashFeeType;

    @XmlElement(name = "cash_refund_fee")
    private Integer cashRefundFee;

    @XmlElement(name = "coupon_refund_fee")
    private Integer couponRefundFee;

    @XmlElement(name = "coupon_refund_count")
    private Integer couponRefundCount;

    /**
     * 退款代金券.
     */
    @SignIgnore
    private List<Coupon> refundCoupons;

    @Override
    public void beforeSign() {
        if (null == this.refundCoupons && null != this.otherParams) {
            final Map<String, BiConsumer<String, Coupon>> mappingMap = new HashMap<>(3);
            mappingMap.put("coupon_refund_id_", (val, coupon) -> coupon.setId(val));
            mappingMap.put("coupon_type_", (val, coupon) -> coupon.setType(Coupon.Type.valueOf(val)));
            mappingMap.put("coupon_refund_fee_", (val, coupon) -> coupon.setFee(Integer.valueOf(val)));
            this.refundCoupons = WeChatPayUtils.beansFrom(this.otherParams, mappingMap, Coupon::new);
        }
    }

}
