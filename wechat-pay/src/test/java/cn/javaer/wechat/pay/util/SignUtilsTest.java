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

import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.model.base.ResponseStatus;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author cn-src
 */
public class SignUtilsTest {
    @Test
    public void generateSign() {
        final UnifiedOrderResponse response = new UnifiedOrderResponse();
        response.setReturnCode(ResponseStatus.SUCCESS);
        response.setReturnMsg("OK");
        response.setAppId("wx2421b1c4370ec43b");
        response.setMchId("10000100");
        response.setNonceStr("IITRi8Iabbblz1Jc");
        response.setResultCode(ResponseStatus.SUCCESS);
        response.setPrepayId("wx201411101639507cbf6ffd8b0779950874");

        assertEquals("A1BEA583D7519373D44343ADF19D73AD", SignUtils.generateSign(response, "key"));
    }

    @Test
    public void generateSign2() {
        // 签名需要兼容空值以及未知字段
        final String xmlStr = "<xml>\n" +
                "    <device_info>device_info</device_info>\n" +
                "    <prepay_id></prepay_id>\n" +
                "    <unknown></unknown>\n" +
                "    <unknown2>unknown2</unknown2>\n" +
                "</xml>";
        final UnifiedOrderResponse response = CodecUtils.unmarshal(xmlStr, UnifiedOrderResponse.class);

        assertEquals("8A6F076B5D945D5109A1B600908F41A7", SignUtils.generateSign(response, "key"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void generateSign4Cache() throws Exception {
        final UnifiedOrderResponse response = new UnifiedOrderResponse();
        response.setReturnCode(ResponseStatus.SUCCESS);
        response.setReturnMsg("OK");
        response.setAppId("wx2421b1c4370ec43b");
        response.setMchId("10000100");
        response.setNonceStr("IITRi8Iabbblz1Jc");
        response.setResultCode(ResponseStatus.SUCCESS);
        response.setPrepayId("wx201411101639507cbf6ffd8b0779950874");

        // 2次调用测试缓存
        SignUtils.generateSign(response, "key");

        assertEquals("A1BEA583D7519373D44343ADF19D73AD", SignUtils.generateSign(response, "key"));
        final Field field = SignUtils.class.getDeclaredField("CACHE_FOR_SIGN");
        field.setAccessible(true);
        final Map<Class, List<Field>> cache = (Map<Class, List<Field>>) field.get(null);
        assertThat(cache).containsKey(UnifiedOrderResponse.class);
    }
}