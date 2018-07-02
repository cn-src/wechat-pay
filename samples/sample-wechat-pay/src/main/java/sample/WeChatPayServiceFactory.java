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

import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.WeChatPayService;

/**
 * @author cn-src
 */
public class WeChatPayServiceFactory {
    private static final WeChatPayService weChatPayService;

    static {
        final WeChatPayConfigurator configurator = new WeChatPayConfigurator();
        configurator.setAppId(System.getenv("wechat.pay.appId"));
        configurator.setMchId(System.getenv("wechat.pay.mchId"));
        configurator.setMchKey(System.getenv("wechat.pay.mchKey"));
        configurator.setPaymentNotifyUrl(System.getenv("wechat.pay.paymentNotifyUrl"));
        configurator.setCertificatePath(System.getenv("wechat.pay.certificatePath"));
        weChatPayService = new WeChatPayService(configurator);
    }

    public static WeChatPayService weChatPayService() {
        return weChatPayService;
    }
}
