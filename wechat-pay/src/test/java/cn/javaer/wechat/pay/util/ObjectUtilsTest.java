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

package cn.javaer.wechat.pay.util;

import cn.javaer.wechat.pay.model.DownloadBillResponse;
import cn.javaer.wechat.pay.model.base.Coupon;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import static cn.javaer.wechat.test.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author zhangpeng
 */
public class ObjectUtilsTest {

    @Test
    public void fullApiUrl() {
        assertEquals("http://demo.com/demo", ObjectUtils.fullApiUrl("http://demo.com", "/demo"));
        assertEquals("http://demo.com/demo", ObjectUtils.fullApiUrl("http://demo.com", "/demo/"));
        assertEquals("http://demo.com/demo", ObjectUtils.fullApiUrl("http://demo.com", "demo/"));

        assertEquals("http://demo.com/demo", ObjectUtils.fullApiUrl("http://demo.com/", "/demo"));
        assertEquals("http://demo.com/demo", ObjectUtils.fullApiUrl("http://demo.com/", "/demo/"));
        assertEquals("http://demo.com/demo", ObjectUtils.fullApiUrl("http://demo.com/", "demo/"));
    }

    @Test
    public void beansMapFrom() {
        final SortedMap<String, String> params = new TreeMap<>();
        params.put("coupon_refund_id_0", "BC884153761883FE608EA956BD05A6F5");
        params.put("coupon_type_0", "CASH");
        params.put("coupon_refund_fee_0", "100");

        params.put("coupon_refund_id_1", "16BE80B8FD1044069950ADAEDEB812C5");
        params.put("coupon_type_1", "NO_CASH");
        params.put("coupon_refund_fee_1", "1");

        final Map<String, BiConsumer<String, Coupon>> mapping = new HashMap<>(3);
        mapping.put("coupon_refund_id_", (val, coupon) -> coupon.setId(val));
        mapping.put("coupon_type_", (val, coupon) -> coupon.setType(Coupon.Type.valueOf(val)));
        mapping.put("coupon_refund_fee_", (val, coupon) -> coupon.setFee(Integer.valueOf(val)));

        final Map<String, Coupon> couponMap = ObjectUtils.beansMapFrom(
                params, mapping, Coupon::new);

        assertThat(couponMap)
                .containsOnlyKeys("0", "1");

        assertThat(couponMap.get("0"))
                .hasId("BC884153761883FE608EA956BD05A6F5")
                .hasType(Coupon.Type.CASH)
                .hasFee(100);

        assertThat(couponMap.get("1"))
                .hasId("16BE80B8FD1044069950ADAEDEB812C5")
                .hasType(Coupon.Type.NO_CASH)
                .hasFee(1);
    }

    @Test
    public void billResponseItemsFrom() {
        final String data = "交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率\n" +
                "`2014-11-1016：33：45,`wx2421b1c4370ec43b,`10000100,`0,`1000,`1001690740201411100005734289,`1415640626,`085e9858e3ba5186aafcbaed1,`MICROPAY,`SUCCESS,`CFT,`CNY,`0.01,`0.0,`0,`0,`0,`0,`,`,`被扫支付测试,`订单额外描述,`0,`0.60%\n" +
                "`2014-11-1016：46：14,`wx2421b1c4370ec43b,`10000100,`0,`1000,`1002780740201411100005729794,`1415635270,`085e9858e90ca40c0b5aee463,`MICROPAY,`SUCCESS,`CFT,`CNY,`0.01,`0.0,`0,`0,`0,`0,`,`,`被扫支付测试,`订单额外描述,`0,`0.60%\n" +
                "总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额\n" +
                "`2,`0.02,`0.0,`0.0,`0";
        final DownloadBillResponse response = ObjectUtils.billResponseFrom(data);
        assertThat(response)
                .hasTotalRecord(2)
                .hasTotalFee("0.02")
                .hasTotalRefundFee("0.0")
                .hasTotalCouponFee("0.0")
                .hasTotalPoundageFee("0");
        assertThat(response.getBillResponseItems()).hasSize(2);

    }
}