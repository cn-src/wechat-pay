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

import cn.javaer.wechat.pay.model.RefundRequest;
import cn.javaer.wechat.pay.model.RefundResponse;

/**
 * @author zhangpeng
 */
public class RefundSample {
    public static void main(final String[] args) {
        WeChatPayClientFactory.init();
        final RefundRequest request = RefundRequest.create("TEST03", "T_TEST03", 1, 1);
        final RefundResponse response = WeChatPayClientFactory.client().refund(request);
        System.out.println(response);
    }
}
