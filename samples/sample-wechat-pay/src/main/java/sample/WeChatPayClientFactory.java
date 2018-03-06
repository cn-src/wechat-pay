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

import cn.javaer.wechat.pay.WeChatPayClient;
import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.WeChatPayRestTemplateClient;
import cn.javaer.wechat.pay.support.WeChatPayJaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhangpeng
 */
public class WeChatPayClientFactory {
    private static WeChatPayClient weChatPayClient;

    public static void init() {
        WeChatPayConfigurator.DEFAULT.setAppid(System.getenv("wechat.pay.appid"));
        WeChatPayConfigurator.DEFAULT.setMchId(System.getenv("wechat.pay.mchId"));
        WeChatPayConfigurator.DEFAULT.setMchKey(System.getenv("wechat.pay.mchKey"));
        WeChatPayConfigurator.DEFAULT.setNotifyUrl(System.getenv("wechat.pay.notifyUrl"));
        WeChatPayConfigurator.DEFAULT.setApiBasePath("https://api.mch.weixin.qq.com");
        WeChatPayConfigurator.DEFAULT.setSpbillCreateIp("127.0.0.1");

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WeChatPayJaxb2RootElementHttpMessageConverter());
        weChatPayClient = new WeChatPayRestTemplateClient(restTemplate);
    }

    public static WeChatPayClient client() {
        return weChatPayClient;
    }
}
