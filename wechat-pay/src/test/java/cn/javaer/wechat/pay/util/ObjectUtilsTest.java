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

import cn.javaer.wechat.pay.model.base.Coupon;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import static cn.javaer.wechat.test.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
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
}