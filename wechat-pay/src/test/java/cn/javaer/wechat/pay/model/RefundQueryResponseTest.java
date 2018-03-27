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

import cn.javaer.wechat.pay.util.CodecUtils;
import cn.javaer.wechat.pay.util.SignUtils;
import org.junit.Test;

import static cn.javaer.wechat.test.Assertions.assertThat;

/**
 * @author zhangpeng
 */
public class RefundQueryResponseTest {
    //language=xml
    private final String testData = "<xml>\n" +
            "    <return_code>SUCCESS</return_code>\n" +
            "    <result_code>SUCCESS</result_code>\n" +
            "    <appid>appid</appid>\n" +
            "    <mch_id>mch_id</mch_id>\n" +
            "    <nonce_str>4EDCA0A0220F466D95C524FA3FE3C100</nonce_str>\n" +
            "    <sign>A6BC15B5F996FC8F6807659456238FAB</sign>\n" +
            "    <total_refund_count>36</total_refund_count>\n" +
            "    <transaction_id>transaction_id</transaction_id>\n" +
            "    <out_trade_no>out_trade_no</out_trade_no>\n" +
            "    <total_fee>100</total_fee>\n" +
            "    <cash_fee>90</cash_fee>\n" +
            "    <refund_count>21</refund_count>\n" +
            "\n" +
            "    <out_refund_no_0>out_refund_no_0</out_refund_no_0>\n" +
            "    <refund_id_0>refund_id_0</refund_id_0>\n" +
            "    <refund_channel_0>ORIGINAL</refund_channel_0>\n" +
            "    <refund_fee_0>10</refund_fee_0>\n" +
            "    <settlement_refund_fee_0>8</settlement_refund_fee_0>\n" +
            "    <coupon_refund_fee_0>20</coupon_refund_fee_0>\n" +
            "    <coupon_refund_count_0>3</coupon_refund_count_0>\n" +
            "    <refund_status_0>SUCCESS</refund_status_0>\n" +
            "    <refund_account_0>REFUND_SOURCE_RECHARGE_FUNDS</refund_account_0>\n" +
            "    <refund_recv_accout_0>招商银行信用卡0403</refund_recv_accout_0>\n" +
            "    <refund_success_time_0>2016-07-25 15:26:26</refund_success_time_0>\n" +
            "\n" +
            "    <coupon_refund_id_0_0>BE2F2D9E4D9848E2963BF39A8DE0A2CD</coupon_refund_id_0_0>\n" +
            "    <coupon_type_0_0>CASH</coupon_type_0_0>\n" +
            "    <coupon_refund_fee_0_0>2</coupon_refund_fee_0_0>\n" +
            "\n" +
            "    <coupon_refund_id_0_1>2DC61D51BE6C4CEA9FAA7E41893B2D97</coupon_refund_id_0_1>\n" +
            "    <coupon_type_0_1>CASH</coupon_type_0_1>\n" +
            "    <coupon_refund_fee_0_1>3</coupon_refund_fee_0_1>\n" +
            "\n" +
            "    <coupon_refund_id_1_0>FCBA60A940224716A77D06EF202A57AD</coupon_refund_id_1_0>\n" +
            "    <coupon_type_1_0>NO_CASH</coupon_type_1_0>\n" +
            "    <coupon_refund_fee_1_0>5</coupon_refund_fee_1_0>\n" +
            "\n" +
            "    <coupon_refund_id_1_1>E9D17A8E767847299B038836A5CE34D9</coupon_refund_id_1_1>\n" +
            "    <coupon_type_1_1>CASH</coupon_type_1_1>\n" +
            "    <coupon_refund_fee_1_1>3</coupon_refund_fee_1_1>\n" +
            "</xml>";

    @Test
    public void beforeSign() {
        final RefundQueryResponse response = CodecUtils.unmarshal(this.testData, RefundQueryResponse.class);

        assertThat(response)
                .hasReturnCode("SUCCESS")
                .hasResultCode("SUCCESS")
                .hasAppId("appid")
                .hasMchId("mch_id")
                .hasNonceStr("4EDCA0A0220F466D95C524FA3FE3C100")
                .hasTotalRefundCount(36)
                .hasTransactionId("transaction_id")
                .hasOutTradeNo("out_trade_no")
                .hasTotalFee(100)
                .hasCashFee(90)
                .hasRefundCount(21);

        assertThat(response.getOtherParams())
                .containsEntry("out_refund_no_0", "out_refund_no_0")
                .containsEntry("refund_id_0", "refund_id_0")
                .containsEntry("refund_channel_0", "ORIGINAL")
                .containsEntry("refund_fee_0", "10")
                .containsEntry("settlement_refund_fee_0", "8")
                .containsEntry("coupon_refund_fee_0", "20")
                .containsEntry("coupon_refund_count_0", "3")
                .containsEntry("refund_status_0", "SUCCESS")
                .containsEntry("refund_account_0", "REFUND_SOURCE_RECHARGE_FUNDS")
                .containsEntry("refund_recv_accout_0", "招商银行信用卡0403")
                .containsEntry("refund_success_time_0", "2016-07-25 15:26:26")

                .containsEntry("coupon_refund_id_0_0", "BE2F2D9E4D9848E2963BF39A8DE0A2CD")
                .containsEntry("coupon_type_0_0", "CASH")
                .containsEntry("coupon_refund_fee_0_0", "2")

                .containsEntry("coupon_refund_id_0_1", "2DC61D51BE6C4CEA9FAA7E41893B2D97")
                .containsEntry("coupon_type_0_1", "CASH")
                .containsEntry("coupon_refund_fee_0_1", "3")

                .containsEntry("coupon_refund_id_1_0", "FCBA60A940224716A77D06EF202A57AD")
                .containsEntry("coupon_type_1_0", "NO_CASH")
                .containsEntry("coupon_refund_fee_1_0", "5")

                .containsEntry("coupon_refund_id_1_1", "E9D17A8E767847299B038836A5CE34D9")
                .containsEntry("coupon_type_1_1", "CASH")
                .containsEntry("coupon_refund_fee_1_1", "3")
        ;

        final String generateSign = SignUtils.generateSign(response, "key");

        assertThat(response).hasSign(generateSign);

    }
}