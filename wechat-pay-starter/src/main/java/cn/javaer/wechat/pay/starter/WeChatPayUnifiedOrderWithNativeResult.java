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

package cn.javaer.wechat.pay.starter;

import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import lombok.Data;

/**
 * 微信支付-NATIVE支付类型, 响应结果.
 *
 * @author zhangpeng
 */
@Data
public class WeChatPayUnifiedOrderWithNativeResult {
    private String codeUrl;

    /**
     * 创建 WeChatPayUnifiedOrderWithNativeResult 从 UnifiedOrderResponse 中的信息.
     *
     * @param response UnifiedOrderResponse
     *
     * @return WeChatPayUnifiedOrderWithNativeResult
     */
    public static WeChatPayUnifiedOrderWithNativeResult createFrom(final UnifiedOrderResponse response) {
        final WeChatPayUnifiedOrderWithNativeResult nativeResult = new WeChatPayUnifiedOrderWithNativeResult();
        nativeResult.setCodeUrl(response.getCodeUrl());
        return nativeResult;
    }
}
