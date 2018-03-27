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

import cn.javaer.wechat.pay.WeChatPayService;
import cn.javaer.wechat.pay.model.base.JsParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangpeng
 */
@RestController
public class SamplePayController {
    private final WeChatPayService weChatPayService;

    @Autowired
    public SamplePayController(final WeChatPayService weChatPayService) {
        this.weChatPayService = weChatPayService;
    }

    @RequestMapping("/unifiedOrderWithNative")
    public String unifiedOrderWithNative(final String outTradeNo, final HttpServletRequest request) {
        return this.weChatPayService.unifiedOrderWithNative(outTradeNo, "TEST", 1, request.getRemoteAddr());
    }

    @RequestMapping("/unifiedOrderWithJsApi")
    public JsParams unifiedOrderWithJsApi(final String outTradeNo, final HttpServletRequest request) {
        return this.weChatPayService.unifiedOrderWithJsApi(outTradeNo, "TEST", 1, request.getRemoteAddr(), System.getenv("openid"));
    }

}
