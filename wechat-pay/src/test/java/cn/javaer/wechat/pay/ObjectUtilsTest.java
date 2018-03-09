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

package cn.javaer.wechat.pay;

import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.util.ObjectUtils;
import cn.javaer.wechat.pay.util.SignUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    public void sign() {
        final UnifiedOrderResponse response = new UnifiedOrderResponse();
        response.setReturnCode("SUCCESS");
        response.setReturnMsg("OK");
        response.setAppid("wx2421b1c4370ec43b");
        response.setMchId("10000100");
        response.setNonceStr("IITRi8Iabbblz1Jc");
        response.setResultCode("SUCCESS");
        response.setPrepayId("wx201411101639507cbf6ffd8b0779950874");
        response.setTradeType("JSAPI");

        response.beforeSign();

        assertEquals("BC884153761883FE608EA956BD05A6F5", SignUtils.generateSign(response, "key"));
    }

    @Test
    public void sign4Cache() throws Exception {
        final UnifiedOrderResponse response = new UnifiedOrderResponse();
        response.setReturnCode("SUCCESS");
        response.setReturnMsg("OK");
        response.setAppid("wx2421b1c4370ec43b");
        response.setMchId("10000100");
        response.setNonceStr("IITRi8Iabbblz1Jc");
        response.setResultCode("SUCCESS");
        response.setPrepayId("wx201411101639507cbf6ffd8b0779950874");
        response.setTradeType("JSAPI");

        // 2次调用测试缓存
        SignUtils.generateSign(response, "key");

        assertEquals("BC884153761883FE608EA956BD05A6F5", SignUtils.generateSign(response, "key"));
        final Field field = WeChatPayUtils.class.getDeclaredField("CACHE_FOR_SIGN");
        field.setAccessible(true);
        final Map<Class, List<Field>> cache = (Map<Class, List<Field>>) field.get(null);
        assertThat(cache).hasSize(1);
    }
}