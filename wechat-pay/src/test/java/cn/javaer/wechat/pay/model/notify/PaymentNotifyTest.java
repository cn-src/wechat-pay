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

package cn.javaer.wechat.pay.model.notify;

import cn.javaer.wechat.pay.model.base.Coupon;
import cn.javaer.wechat.pay.model.base.ResponseStatus;
import cn.javaer.wechat.pay.util.CodecUtils;
import org.junit.Test;

import static cn.javaer.wechat.test.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author cn-src
 */
public class PaymentNotifyTest {

    //language=xml
    private final String testData = "<xml>\n" +
            "    <appid><![CDATA[wx2421b1c4370ec43b]]></appid>\n" +
            "    <attach><![CDATA[支付测试]]></attach>\n" +
            "    <bank_type><![CDATA[CFT]]></bank_type>\n" +
            "    <fee_type><![CDATA[CNY]]></fee_type>\n" +
            "    <is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
            "    <mch_id><![CDATA[10000100]]></mch_id>\n" +
            "    <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>\n" +
            "    <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>\n" +
            "    <out_trade_no><![CDATA[1409811653]]></out_trade_no>\n" +
            "    <result_code><![CDATA[SUCCESS]]></result_code>\n" +
            "    <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "    <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>\n" +
            "    <sub_mch_id><![CDATA[10000100]]></sub_mch_id>\n" +
            "    <time_end><![CDATA[20140903131540]]></time_end>\n" +
            "    <total_fee>1</total_fee>\n" +
            "    <coupon_fee><![CDATA[10]]></coupon_fee>\n" +
            "    <coupon_count><![CDATA[1]]></coupon_count>\n" +
            "    <trade_type><![CDATA[JSAPI]]></trade_type>\n" +
            "    <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>\n" +
            "\n" +
            "    <coupon_type_0>CASH</coupon_type_0>\n" +
            "    <coupon_id_0>coupon_id_0</coupon_id_0>\n" +
            "    <coupon_fee_0>0</coupon_fee_0>\n" +
            "\n" +
            "    <coupon_type_1>CASH</coupon_type_1>\n" +
            "    <coupon_id_1>coupon_id_1</coupon_id_1>\n" +
            "    <coupon_fee_1>1</coupon_fee_1>\n" +
            "\n" +
            "    <coupon_type_2>CASH</coupon_type_2>\n" +
            "    <coupon_id_2>coupon_id_2</coupon_id_2>\n" +
            "    <coupon_fee_2>2</coupon_fee_2>\n" +
            "\n" +
            "    <coupon_type_3>NO_CASH</coupon_type_3>\n" +
            "    <coupon_id_3>coupon_id_3</coupon_id_3>\n" +
            "    <coupon_fee_3>3</coupon_fee_3>\n" +
            "\n" +
            "    <coupon_type_4>CASH</coupon_type_4>\n" +
            "    <coupon_id_4>coupon_id_4</coupon_id_4>\n" +
            "    <coupon_fee_4>4</coupon_fee_4>\n" +
            "</xml>";

    @Test
    public void beforeSign() {
        final PaymentNotify notifyResult = CodecUtils.unmarshal(this.testData, PaymentNotify.class);
        assertThat(notifyResult)
                .hasAppId("wx2421b1c4370ec43b")
                .hasAttach("支付测试")
                .hasBankType("CFT")
                .hasFeeType("CNY")
                .hasIsSubscribe("Y")
                .hasMchId("10000100")
                .hasNonceStr("5d2b6c2a8db53831f7eda20af46e531c")
                .hasOpenId("oUpF8uMEb4qRXf22hE3X68TekukE")
                .hasOutTradeNo("1409811653")
                .hasResultCode(ResponseStatus.SUCCESS)
                .hasReturnCode(ResponseStatus.SUCCESS)
                .hasSign("B552ED6B279343CB493C5DD0D78AB241")
                .hasSubMchId("10000100")
                .hasTimeEnd("20140903131540")
                .hasTotalFee(1)
                .hasCouponFee(10)
                .hasCouponCount(1)
                .hasTradeType("JSAPI")
                .hasTransactionId("1004400740201409030005092168");

        assertThat(notifyResult.getCoupons())
                .hasSize(5)
                .extracting("id", "type", "fee")
                .containsOnly(
                        tuple("coupon_id_0", Coupon.Type.CASH, 0),
                        tuple("coupon_id_1", Coupon.Type.CASH, 1),
                        tuple("coupon_id_2", Coupon.Type.CASH, 2),
                        tuple("coupon_id_3", Coupon.Type.NO_CASH, 3),
                        tuple("coupon_id_4", Coupon.Type.CASH, 4)
                );
    }
}