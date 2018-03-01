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

package cn.javaer.wechat.spring.boot.autoconfigure.pay;

import cn.javaer.wechat.pay.WeChatPayClient;
import cn.javaer.wechat.pay.WeChatPayRestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * 微信支付自动配置.
 *
 * @author zhangpeng
 */
@Configuration
@ConditionalOnClass(WeChatPayClient.class)
@ConditionalOnWebApplication
@AutoConfigureAfter({WebClientAutoConfiguration.class})
@EnableConfigurationProperties(WeChatPayProperties.class)
public class WeChatPayAutoConfiguration {
    private final WeChatPayProperties weChatPayProperties;

    public WeChatPayAutoConfiguration(final WeChatPayProperties weChatPayProperties) {
        this.weChatPayProperties = weChatPayProperties;
    }

    /**
     * WeChatPayClient 配置.
     *
     * @param restTemplate RestTemplate
     *
     * @return WeChatPayClient
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatPayClient weChatPayClient(
            @Autowired(required = false) RestTemplate restTemplate) {
        if (null == restTemplate) {
            restTemplate = new RestTemplate();
        }
        return new WeChatPayRestTemplateClient(restTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatPayService weChatPayService(
            final WeChatPayClient weChatPayClient, final ApplicationEventPublisher publisher) {
        return new WeChatPayDefaultService(weChatPayClient, publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatPayController weChatPayController(final ApplicationEventPublisher publisher) {
        return new WeChatPayController(publisher);
    }
}
