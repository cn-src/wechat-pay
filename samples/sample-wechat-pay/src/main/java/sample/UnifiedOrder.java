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

package sample;

import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;

/**
 * @author zhangpeng
 */
public class UnifiedOrder {
    public static void main(final String[] args) {
        final UnifiedOrderRequest unifiedOrderRequest = UnifiedOrderRequest.createWithNative("test", "TEST01", 1);
        final UnifiedOrderResponse unifiedOrderResponse = WeChatPayClientFactory.client().unifiedOrder(unifiedOrderRequest);
        System.out.println(unifiedOrderResponse);
    }
}