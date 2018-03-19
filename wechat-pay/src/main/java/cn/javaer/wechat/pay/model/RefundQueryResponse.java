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

import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.Coupon;
import cn.javaer.wechat.pay.model.base.Refund;
import cn.javaer.wechat.pay.support.SignIgnore;
import cn.javaer.wechat.pay.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 微信支付-查询退款-响应.
 *
 * @author zhangpeng
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"refunds"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class RefundQueryResponse extends BasePayResponse {

    @XmlElement(name = "total_refund_count")
    private Integer totalRefundCount;

    @XmlElement(name = "transaction_id")
    private String transactionId;

    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    @XmlElement(name = "total_fee")
    private Integer totalFee;

    @XmlElement(name = "settlement_total_fee")
    private Integer settlementTotalFee;

    @XmlElement(name = "fee_type")
    private String feeType;

    @XmlElement(name = "cash_fee")
    private Integer cashFee;

    @XmlElement(name = "refund_count")
    private Integer refundCount;

    @SignIgnore
    private List<Refund> refunds;

    @Override
    public void processResponse() {
        if (null == this.refunds && null != this.otherParams) {
            final Map<String, Refund> refundsMap =
                    ObjectUtils.beansMapFrom(this.otherParams, createRefundMapping(), Refund::new);
            initCoupons(refundsMap);
            this.refunds = new ArrayList<>(refundsMap.values());
        }
    }

    private void initCoupons(final Map<String, Refund> refundMap) {
        for (final Map.Entry<String, Refund> entry : refundMap.entrySet()) {
            final String key = entry.getKey();
            final List<Coupon> coupons =
                    ObjectUtils.beansFrom(this.otherParams, createCouponMapping(key), Coupon::new);
            entry.getValue().setCoupons(coupons);
        }
    }


    private Map<String, BiConsumer<String, Coupon>> createCouponMapping(final String key) {
        final Map<String, BiConsumer<String, Coupon>> couponMapping = new HashMap<>(3);
        couponMapping.put("coupon_type_" + key + "_", (val, coupon) -> coupon.setType(Coupon.Type.valueOf(val)));
        couponMapping.put("coupon_refund_id_" + key + "_", (val, coupon) -> coupon.setId(val));
        couponMapping.put("coupon_refund_fee_" + key + "_", (val, coupon) -> coupon.setFee(Integer.valueOf(val)));
        return couponMapping;
    }


    private Map<String, BiConsumer<String, Refund>> createRefundMapping() {
        final Map<String, BiConsumer<String, Refund>> refundMapping = new HashMap<>(11);
        refundMapping.put("out_refund_no_", (val, coupon) -> coupon.setOutRefundNo(val));
        refundMapping.put("refund_id_", (val, coupon) -> coupon.setRefundId(val));
        refundMapping.put("refund_channel_", (val, coupon) -> coupon.setRefundChannel(val));
        refundMapping.put("refund_fee_", (val, coupon) -> coupon.setRefundFee(Integer.valueOf(val)));
        refundMapping.put("settlement_refund_fee_",
                (val, coupon) -> coupon.setSettlementRefundFee(Integer.valueOf(val)));
        refundMapping.put("coupon_refund_fee_", (val, coupon) -> coupon.setCouponRefundFee(Integer.valueOf(val)));
        refundMapping.put("coupon_refund_count_", (val, coupon) -> coupon.setCouponRefundCount(Integer.valueOf(val)));
        refundMapping.put("refund_status_", (val, coupon) -> coupon.setRefundStatus(val));
        refundMapping.put("refund_account_", (val, coupon) -> coupon.setRefundAccount(val));
        refundMapping.put("refund_recv_accout_", (val, coupon) -> coupon.setRefundRecvAccout(val));
        refundMapping.put("refund_success_time_", (val, coupon) -> coupon.setRefundSuccessTime(val));
        return refundMapping;
    }

}
