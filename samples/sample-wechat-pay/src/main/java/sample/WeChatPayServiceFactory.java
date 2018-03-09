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
import cn.javaer.wechat.pay.client.HttpClientFactory;
import cn.javaer.wechat.pay.client.WeChatPayClient;
import cn.javaer.wechat.pay.client.WeChatPayHttpComponentsClient;
import org.apache.http.client.HttpClient;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author zhangpeng
 */
public class WeChatPayServiceFactory {
    private static WeChatPayService weChatPayService;

    public static void init() {
        final WeChatPayConfigurator configurator = new WeChatPayConfigurator();
        configurator.setAppid(System.getenv("wechat.pay.appid"));
        configurator.setMchId(System.getenv("wechat.pay.mchId"));
        configurator.setMchKey(System.getenv("wechat.pay.mchKey"));
        configurator.setNotifyUrl(System.getenv("wechat.pay.notifyUrl"));
        configurator.setBasePath("https://api.mch.weixin.qq.com");
        final HttpClient httpClient = new HttpClientFactory(configurator.getMchId(), System.getenv("wechat.pay.certificatePath")).build();
        final WeChatPayClient weChatPayClient = new WeChatPayHttpComponentsClient(configurator.getBasePath(), httpClient);
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        weChatPayService = new WeChatPayService(weChatPayClient, configurator, validator);
    }

    public static WeChatPayService weChatPayService() {
        return weChatPayService;
    }
}
