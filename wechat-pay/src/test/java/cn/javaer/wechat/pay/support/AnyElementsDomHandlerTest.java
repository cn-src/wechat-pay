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

package cn.javaer.wechat.pay.support;

import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.model.OrderQueryResponse;
import org.junit.Test;
import util.AssertJson;
import util.TestUtils;

/**
 * @author zhangpeng
 */
public class AnyElementsDomHandlerTest {

    @Test
    public void elementsToMap() {
        WeChatPayConfigurator.DEFAULT.setMchKey("key");

        final OrderQueryResponse response = TestUtils.jaxbUnmarshal(
                "<xml><sign>d</sign><nonce_str>nonce_str_value</nonce_str><coupon_type_0>CASH</coupon_type_0></xml>",
                OrderQueryResponse.class);
        response.beforeSign();
        AssertJson.assertWithJson(response, "AnyElementsDomHandlerTest_elementsToMap.json");
    }
}